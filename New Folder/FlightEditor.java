

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.Timer;
import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.*;

public class FlightEditor extends JFrame implements KeyListener, ActionListener, MouseListener, MouseMotionListener{
    public static void main(String[] args){
        new FlightEditor();
    }
    
    class Order{
        int O;
        Point3DM TargetP;
        Sprite3DM TargetS;
        public Order(int N, Point3DM P){
            O = N;
            TargetP = P;
        }
        public Order(int N, Sprite3DM S){
            O = N;
            TargetS = S;
        }
        public Order(){
            
        }
        public void toBytes(DataOutput Output) throws IOException{
            Output.writeInt(O);
            if(TargetP != null){
                Output.writeChar('p');
                TargetP.toBareBytes(Output);
            }else if(TargetS != null){
                Output.writeChar('v');
                Output.writeInt(Units.indexOf(TargetS));
            }else{
                Output.writeChar(' ');
            }
        }
        public void fromBytes(DataInput Input) throws IOException{
            O = Input.readInt();
            switch(Input.readChar()){
                case 'p': TargetP = new Point3DM(); TargetP.fromBareBytes(Input); break;
                case 'v': TargetS = (Sprite3DM)Units.elementAt(Input.readInt()); break;
                case ' ': ; break;
            }
        }
    }
    abstract class Objective{
        abstract void fromBytes(DataInput I) throws IOException;
        abstract void toBytes(DataOutput O) throws IOException;
        public void typeToBytes(DataOutput O) throws IOException{
            if(this instanceof UnitObjective){
                O.writeChar('u');
            }else if(this instanceof TimeObjective){
                O.writeChar('t');
            }/*else if(this instanceof DefaultObjective){
                O.writeChar('d');
            }*/
        }

    }
    public Objective makeObjective(char C){
        switch(C){
            case 'u': return new UnitObjective();
            case 't': return new TimeObjective();
          //  case 'd': return new DefaultObjective();
        }
        return null;
    }
  /*  class DefaultObjective extends Objective{
        public void fromBytes(DataInput I){
            
        } 
        public void toBytes(DataOutput O){
        }
    }*/
    class UnitObjective extends Objective{
        Sprite3DM[] Targets = new Sprite3DM[1000];
        int N;
        boolean All;
        void removeTarget(Sprite3DM S){
            boolean TargetFound = false;
            for(int i = 0; i < N; i++){
                if(!TargetFound){
                    TargetFound = Targets[i] == S;
                }else{
                    Targets[i - 1] = Targets[i];
                    Targets[i] = null;
                }
            }
            if(TargetFound){
                N--;
            }
            
        }
        void fromBytes(DataInput I) throws IOException{
            All = I.readBoolean();
            N = I.readInt();
            Targets = new Sprite3DM[1000];
            for(int i = 0; i < N; i++){
                Targets[i] = (Sprite3DM)Units.elementAt(I.readInt());
            }
        }
        
        void toBytes(DataOutput O) throws IOException{
            System.out.println(All);
            O.writeBoolean(All);
            O.writeInt(N);
            for(int i = 0; i < N; i++){
                O.writeInt(Units.indexOf(Targets[i]));
            }
        }
        
    }
    class TimeObjective extends Objective{
        int Time;
        
        void fromBytes(DataInput I) throws IOException{
            Time = I.readInt();
        }
        
        void toBytes(DataOutput O) throws IOException{
            O.writeInt(Time);
        }
        
    }
    final int X = 800;
    final int Y = 600;
    Dimension ScreenSize = GUI.getScreenSize();
    

    Timer Stopwatch = new Timer(20, this);
    double FramesPerSecond = 50;
    TimeKeeper Time = new TimeKeeper(50, false);
    JCanvas Area;
    
    ObserverM View;
    Color[] ColorChart = {Color.white, Color.cyan, Color.yellow, Color.red, Color.magenta};
    Color Tint;
    boolean Left;
    boolean Right;
    boolean Forward;
    boolean Back;
    
    char PlaceType; //0 -> 
    int PlaceSide;
    Shape3DM Grid;
    Vector<Point> PolygonData = new Vector<Point>();
    
    DataArray InputData = new DataArray(32, true);
    DataArray OutputData = new DataArray(128, true);
    
    Vector Units = new Vector();
    Vector UnitData = new Vector();
    Vector UnitGuns = new Vector();
    Vector Terrain = new Vector();
    Vector NavPoints = new Vector();
    Vector UnitOrders = new Vector();//1 = Guard; 2 = Move; 3 = Attack Move; 4 = Attack/Follow Target;
    Object Selectee;
    
    Objective WinCondition;
    Objective LoseCondition;
    
    int[] Guns = {1, 1, 0, 0, 0}; //MachineGun, Cannon, Missile
    int[] Data = {1, 25, 3, 0, 0}; //PlaneType, Hitpoints, AI, Z, SpawnDelay
    int[] BaseData = {1, 25, 3, 0, 0};
    int[] PlaneData = {1000, 1000, 5000, 0, 0};
    int[] BasePlaneData = {1000, 1000, 5000, 0, 0};
    
    String[] DataNames = {"PlaneType", "HitPoints", "AILevel", "StartHeight", "SpawnDelay"};
    String[] PlaneDataNames = {"Height", "Width", "Radius", "", ""};
    Shape3DM LatestShape = new Shape3DM();
    Vector Planes = new Vector();
    public void init(){
        View = new ObserverM(0, 0, 2000, new Rotation3DM(new double[][]{{0, 1, 0}, {0, 0, -1}, {-1, 0, 0}}), Area.Painter);

     //   View.Rotation.print();
        View.setLookVector();
        View.setArea(0, 0, Area.W, Area.H, 1);
        Grid = new Shape3DM(false, Color.GREEN.darker(), 0, 0, 0);
        Utils3DM.setPoints(Grid, 'g', 150, 150, 100);
    }
  
    
    public void makePyramid(int PX, int PY, int Height, int Width){
        int SideCount = 4;
        
        for(int i = 0; i < 4; i++){
            Point3DM[] PV = new Point3DM[3];
            PV[0] = new Point3DM(PX, PY, Height);
            PV[1] = new Point3DM(Math.sin(i * 2 * Math.PI / 4) * Width + PX, Math.cos(i * 2 * Math.PI / 4) * Width + PY, 0);
            PV[2] = new Point3DM(Math.sin((i + 1) * 2 * Math.PI / 4) * Width + PX, Math.cos((i + 1) * 2 * Math.PI / 4) * Width + PY, 0);
            Planes.add(PV);
            
        }
    }
    public void makeRing(int pX, int pY, int Height, int Width, int Radius){
        int Count = (int)(Radius * Math.PI * 2 / Width / Math.sqrt(2));
        
        for(int j = 0; j < Count; j++){
           
           
            double Offset = Math.PI * 2 * ((float)j / Count + 0.125); 

            int PX = (int)(Radius * Math.sin((float)j / Count * 2 * Math.PI)) + pX;
            int PY = (int)(Radius * Math.cos((float)j / Count * 2 * Math.PI)) + pY;

            
            for(int i = 0; i < 4; i++){
                Point3DM[] PV = new Point3DM[3];
                PV[0] = new Point3DM(PX, PY, Height);
                PV[1] = new Point3DM(Math.sin(i * 2 * Math.PI / 4 + Offset) * Width + PX, Math.cos(i * 2 * Math.PI / 4 + Offset) * Width + PY, 0);
                PV[2] = new Point3DM(Math.sin((i + 1) * 2 * Math.PI / 4 + Offset) * Width + PX, Math.cos((i + 1) * 2 * Math.PI / 4 + Offset) * Width + PY, 0);
                Planes.add(PV);
            }
        }
    }
    public FlightEditor(){
        //BASIC INITIALIZATION
        super("FlightEditor");
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        contentArea.setLayout(null);
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new JCanvas(); 
        Area.setBounds(0, 0, X, Y);
        
        
        contentArea.add(Area);
        Area.init(BufferedImage.TYPE_INT_RGB);
        this.setLocation(ScreenSize.width / 2 - X / 2, ScreenSize.height / 2 - Y / 2);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
      //  initSockets();
        init();
        Stopwatch.start();
        
        //ADD CONTENT PANE AND PACK
        this.setUndecorated(true);
        this.setContentPane(contentArea);
        this.setVisible(true);
        //this.pack();   
    }
    
    public void checkSelect(int A){
        if(Selectee instanceof Sprite3DM){
            ((Sprite3DM)Selectee).Tint = ColorChart[A];
        }
        
    }
    public void doOrder(){
        if(Selectee instanceof Sprite3DM){
            int Input = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Order"));
            ((Order)UnitOrders.elementAt(Units.indexOf(Selectee))).O = Input;
            Selectee = ((Order)UnitOrders.elementAt(Units.indexOf(Selectee)));
            PlaceType = 'o';
        }else if(Selectee instanceof Order){
            int Input = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Order"));
            ((Order)Selectee).O = Input;
        }
        
    }
    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){
        String Modifiers = e.getKeyModifiersText(e.getModifiers());
        if(Modifiers.equals("")){
            switch(e.getKeyCode()){
                case 38: Forward = true; break; //u
                case 40: Back = true; break; //d
                case 37: Left = true; break; //l
                case 39: Right = true; break; //r
                case 107: View.POS.POSZ *= 2; break; //+
                case 109: View.POS.POSZ /= 2; break; //+
                case 48: PlaceSide = 0; checkSelect(0); break; //0
                case 49: PlaceSide = 1; checkSelect(1); break; //1
                case 50: PlaceSide = 2; checkSelect(2); break; //2
                case 51: PlaceSide = 3; checkSelect(3); break; //3
                case 52: PlaceSide = 4; checkSelect(4); break; //4
                case 83: PlaceType = 's'; deselect(); break; //s
                case 80: PlaceType = 'p'; deselect(); break; //p
                case 67: PlaceType = 'c'; deselect();  break; //c
                case 84: PlaceType = 't';  deselect(); break; //t
                case 82: PlaceType = 'r'; deselect(); break; //r
                case 78: PlaceType =  'n'; deselect(); break; //n
                case 69: PlaceType = 'e'; deselect(); break; //e
                case 74: doObjective(); break; //j
                case 27: PlaceType = '\0';System.out.println(UnitOrders.size() + "\t" + Units.size()); deselect(); break; //esc      
                case 79: doOrder(); break; //O
                case 127: deleteSelection(); break; //del
                

            }
        }else if(Modifiers.equals("Shift")){
            switch(e.getKeyCode()){
                case 49: case 50: case 51: case 52: case 53:
                    Guns[e.getKeyCode() - 49] ^= 1;
                    break;
                    
            }
        }else if(Modifiers.equals("Alt")){
            switch(e.getKeyCode()){
                case 49: case 50: case 51: case 52: case 53:
                    if(PlaceType != 't' && PlaceType != 'c' && PlaceType != '\0'){
                        int Input = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Value for " + PlaneDataNames[e.getKeyCode() - 49]));
                        PlaneData[e.getKeyCode() - 49] = Input;
                    }else if(PlaceType != 'p' || PlaceType != 'r'){
                        int Input = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Value for " + DataNames[e.getKeyCode() - 49]));
                        Data[e.getKeyCode() - 49] = Input;
                    }
                    break;
                    
            }
        }else if(Modifiers.equals("Ctrl")){
            switch(e.getKeyCode()){
                case 76: loadData(); break; //l
                case 83: saveData(); break; //s
                case 67: prepCopy(); break; //c
                case 86: ; break; //v
            }
        }
        System.out.println(e.getKeyCode());
    }
    public void doObjective(){
        Objective O = JOptionPane.showOptionDialog(this, "Select Objective To Set", "Objective", 0, 0, null, new String[]{"Win", "Lose"}, 0) == 0 ? WinCondition : LoseCondition;
        int Type = JOptionPane.showOptionDialog(this, "Select Objective To Set", "Objective", 0, 0, null, new String[]{"None", "Time", "Unit"}, 0);
        
        if(O == WinCondition){
            if(Type == 0){
                WinCondition = null;
            }else if(Type == 1){
                int T = Integer.parseInt(JOptionPane.showInputDialog(this, "Set Time Limit"));
                WinCondition = new TimeObjective();
                ((TimeObjective)WinCondition).Time = T;
            }else if(Type == 2){
                boolean All = JOptionPane.showOptionDialog(this, "Select Objective To Set", "Objective", 0, 0, null, new String[]{"All", "Any"}, 0) == 0;
                
                WinCondition = new UnitObjective();
                ((UnitObjective)WinCondition).All = All;
                PlaceType = 'j';
            }
            
        }else if(O == LoseCondition){
            if(Type == 0){
                LoseCondition = null;
            }else if(Type == 1){
                int T = Integer.parseInt(JOptionPane.showInputDialog(this, "Set Time Limit"));
                LoseCondition = new TimeObjective();
                ((TimeObjective)LoseCondition).Time = T;
            }else if(Type == 2){
                boolean All = JOptionPane.showOptionDialog(this, "Select Objective To Set", "Objective", 0, 0, null, new String[]{"All", "Any"}, 0) == 0;
                
                PlaceType = 'J';
                LoseCondition = new UnitObjective();
                ((UnitObjective)LoseCondition).All = All;
            }
        }
        
    }
    public void prepCopy(){
        if(!(Selectee instanceof Sprite3DM)){
            return;
        }
        int N = Units.indexOf(Selectee);
        PlaceSide = getSide(((Sprite3DM)Selectee).Tint);
        Guns = (int[])UnitGuns.elementAt(N);
        Data = (int[])UnitData.elementAt(N);
        PlaceType = 'c';
    }
    public void loadData(){
        try{
            FileInputStream F = new FileInputStream(FilePicker.userGetFile("Select Source", "", this));
            
            InputData.ensureCapacity(F.available());
            System.out.println("Available: " + F.available());
            F.read(InputData.Buffer, 0, F.available());
            InputData.reset();
            //Read Planes
            int N = InputData.readInt(); 
            System.out.println(N);
            for(int i = 0; i < N; i++){
                Point3DM[] Plane = new Point3DM[InputData.readInt()];
                for(int j = 0; j < Plane.length; j++){
                    Plane[j] = new Point3DM(0, 0, 0);
                    Plane[j].fromBareBytes(InputData);
                }
                Planes.add(Plane);
            }
            //Read Units
            N = InputData.readInt();
            for(int i = 0; i < N; i++){
                System.out.println("Unit " + i);
              
                Shape Sh = InputData.readChar() == 't' ? TurretShape : CraftShape; //Read Shape
                Point3DM P = new Point3DM(0, 0, 0); P.fromBareBytes(InputData); //Read Position
                Color C = new Color(InputData.readInt()); //Read Color
                Sprite3DM Sp = new Sprite3DM(Sh, P, C);
                Units.add(Sp);

                int[] DataToRead = new int[InputData.readInt()];//Read Guns
                
                for(int j = 0; j < DataToRead.length; j++){
                    DataToRead[j] = InputData.readBoolean() ? 1 : 0;
                }
                UnitGuns.add(DataToRead);

                DataToRead = new int[InputData.readInt()]; //Read Data
                
                for(int j = 0; j < DataToRead.length; j++){
                    
                    DataToRead[j] = InputData.readInt();
                }
                UnitData.add(DataToRead);
                
            }
            for(int i = 0; i < N; i++){
                Order O = new Order();
                O.fromBytes(InputData);
                UnitOrders.add(O);
            }
            //Read NavPoints
            N = InputData.readInt();
            System.out.println(N + " NavPoints");
            for(int i = 0; i < N; i++){
                Point3DM P = new Point3DM(0, 0, 0); P.fromBareBytes(InputData);
                Color C = ColorChart[InputData.readInt()];
                NavPoints.add(new Sprite3DM(NavPointShape, P, C));
                
            }
            //Load Win/Lose Conditions
            WinCondition = makeObjective(InputData.readChar());
            WinCondition.fromBytes(InputData);
            LoseCondition = makeObjective(InputData.readChar());
            LoseCondition.fromBytes(InputData);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void deselect(){
        Selectee = null;
        Guns = new int[5]; Data = (int[])BaseData.clone();
    }
    public void deleteSelection(){
        if(Units.contains(Selectee)){
            int A = Units.indexOf(Selectee);
            UnitData.removeElementAt(A);
            UnitOrders.removeElementAt(A);
            UnitGuns.removeElementAt(A);
            Units.removeElementAt(A);
            if(WinCondition instanceof UnitObjective){
                ((UnitObjective)WinCondition).removeTarget((Sprite3DM)Selectee);
            }
            if(LoseCondition instanceof UnitObjective){
                ((UnitObjective)LoseCondition).removeTarget((Sprite3DM)Selectee);
            }
            deselect();
        }else if(Planes.contains(Selectee)){
            Planes.remove(Selectee);
            deselect();
        }else if(NavPoints.contains(Selectee)){
            NavPoints.remove(Selectee);
            deselect();
        }
    }
    public void saveData(){
        try{
            OutputData.reset();
            
            //Do Planes
            OutputData.writeInt(Planes.size());
            
            for(int i = 0; i < Planes.size(); i++){
                Point3DM[] Plane = (Point3DM[])Planes.elementAt(i);
                OutputData.writeInt(Plane.length);
                for(int j = 0; j < Plane.length; j++){
                    Plane[j].toBareBytes(OutputData);
                }
            }
            
            //Do Units
            OutputData.writeInt(Units.size());
            for(int i = 0; i < Units.size(); i++){
                OutputData.writeChar(((Sprite3DM)Units.elementAt(i)).Image instanceof Rectangle2D ? 't' : 'c'); //Write Type
                ((Sprite3DM)Units.elementAt(i)).Center.toBareBytes(OutputData); //Write Position
                OutputData.writeInt(((Sprite3DM)Units.elementAt(i)).Tint.getRGB()); //Write Color
                
                int[] DataToWrite = ((int[])UnitGuns.elementAt(i));
                OutputData.writeInt(DataToWrite.length); // Write Guns
                for(int j = 0; j < DataToWrite.length; j++){
                    OutputData.writeBoolean(DataToWrite[j]==1);
                }
                
                
                DataToWrite = ((int[])UnitData.elementAt(i));
                OutputData.writeInt(DataToWrite.length); // Write Data
                for(int j = 0; j < DataToWrite.length; j++){
                    OutputData.writeInt(DataToWrite[j]);
                }
            }
            for(int i = 0; i < UnitOrders.size(); i++){
                ((Order)UnitOrders.elementAt(i)).toBytes(OutputData);
            }
            //Do NavPoints
            OutputData.writeInt(NavPoints.size());
            for(int i = 0; i < NavPoints.size(); i++){
                ((Sprite3DM)NavPoints.elementAt(i)).Center.toBareBytes(OutputData);
                OutputData.writeInt(getSide(((Sprite3DM)NavPoints.elementAt(i)).Tint));
            }
            
            //Do Win/Lose Conditions
            
            if(WinCondition == null){
                System.out.println("Defaulting Victory Condition");
                WinCondition = new UnitObjective();
                ((UnitObjective)WinCondition).All = true;
                System.out.println(ColorChart[0] + "\t" + ColorChart[1]);
                for(int i = 0; i < Units.size(); i++){
                    Sprite3DM U = (Sprite3DM)Units.elementAt(i);
                    System.out.println(U.Tint);
                    if(!U.Tint.equals(ColorChart[0]) && !U.Tint.equals(ColorChart[1])){
                        System.out.println("IN");
                        ((UnitObjective)WinCondition).Targets[((UnitObjective)WinCondition).N] = U;
                        ((UnitObjective)WinCondition).N++;
                    }
                }
            }
            WinCondition.typeToBytes(OutputData);
            WinCondition.toBytes(OutputData);
            
            if(LoseCondition == null){
                System.out.println("Defaulting Loss Condition");
                LoseCondition = new UnitObjective();
                for(int i = 0; i < Units.size(); i++){
                    Sprite3DM U = (Sprite3DM)Units.elementAt(i);
                    if(U.Tint.equals(ColorChart[0])){
                        ((UnitObjective)LoseCondition).Targets[((UnitObjective)LoseCondition).N] = U;
                        ((UnitObjective)LoseCondition).N++;
                    }
                }
            }
            LoseCondition.typeToBytes(OutputData);
            LoseCondition.toBytes(OutputData);
            
            
            FileOutputStream F = new FileOutputStream(FilePicker.userGetFile("Select Target", "", this));
            F.write(OutputData.Buffer, 0, OutputData.Count);    
            System.out.println("Planes: " + Planes.size());
            System.out.println("Bytes: " + OutputData.Count);
        }catch(IOException e){
            
        }
        
    }
    public void loadDataObjects(){
        int[][] Data = FileHandler.readInts(FilePicker.userGetFile("Select Source", "", this), "\t");
        for(int i = 0; i < Data.length; i++){
            int[] A = Data[i];
            if(A[0] == 0){
                makePyramid(A[1], A[2], A[3], A[4]);
            }else if(A[0] == 1){
                makeRing(A[1], A[2], A[3], A[4], A[5]);
            }
        }
    }
    public void keyReleased(KeyEvent e){
        switch(e.getKeyCode()){
            case 38: Forward = !true; break; //u
            case 40: Back = !true; break; //d
            case 37: Left = !true; break; //l
            case 39: Right = !true; break; //r
        }
    }
 
    
    public void keyTyped(KeyEvent e){

    }
    
 
    public void paint(Graphics G){
        
    }
    public void paintBackground(){
        Area.Painter.setColor(Color.black);
        Area.Painter.fillRect(0, 0, X, Y);
        

    }
    public void shiftGrid(Point3DM Target){
        int INTX = (int)GUI.round(Target.POSX, 1.0/100);
        int INTY = (int)GUI.round(Target.POSY, 1.0/100);
        boolean ReRotate = false;
        if(Grid.Center.POSX != INTX || Grid.Center.POSY != INTY){
            ReRotate = true;
        }
        if(ReRotate){
            Grid.Center.POSX = INTX;
            Grid.Center.POSY = INTY;
            
        }
    }
    public void doGrid(){
        shiftGrid(View.POS);
        Grid.calcPoints();
        Grid.printNow(Area.Painter, View, GUI.blackenColor(Grid.Tint, 80 / Math.sqrt(View.POS.POSZ)));
    }   
    public void doControls(){
        if(Right){View.POS.POSX -= 0.3 * View.POS.POSZ / FramesPerSecond;}
        if(Left){View.POS.POSX += 0.3 * View.POS.POSZ / FramesPerSecond;}
        if(Forward){View.POS.POSY -= 0.3 * View.POS.POSZ / FramesPerSecond;}
        if(Back){View.POS.POSY += 0.3 * View.POS.POSZ / FramesPerSecond;}
    }
    
    AffineTransform DeScaler = new AffineTransform(0.15, 0, 0, 0.15, 0, 0);
    public void drawGUI(){
        int tX = 100; int tY = 50;
        Area.printLine(tX, tY + 0, "X: " + GUI.round(View.POS.POSX, 10), 16, Color.red, 'c');
        Area.printLine(tX, tY + 20, "Y: " + GUI.round(View.POS.POSY, 10), 16, Color.red, 'c');
        Area.printLine(tX, tY + 40, "Z: " + GUI.round(View.POS.POSZ, 10), 16, Color.red, 'c');
        
        Area.printLine(tX, tY  + 80, "" + (char)PlaceType + (Selectee instanceof Order ? ((Order)Selectee).O+"" : "") + (Units.contains(Selectee) ? ((Order)UnitOrders.get(Units.indexOf(Selectee))).O: ""), 16, ColorChart[PlaceSide], 'c');
        
        if(PlaceType == 't' || PlaceType == 'c' || PlaceType == '\0'){
            Color C = Color.white;
            if(Selectee != null){
                C = Selectee instanceof Sprite3DM ? ((Sprite3DM)Selectee).Tint : Color.white;   
            }else{
                C = ColorChart[PlaceSide];
            }
            Area.printLine(tX, tY  + 100, Arrays.toString(Guns), 16, C, 'c');
            Area.printLine(tX, tY  + 120, Arrays.toString(Data), 16, C, 'c');
            
        }else if(PlaceType == 'p' || PlaceType == 'r'){
            Area.printLine(tX, tY  + 120, Arrays.toString(PlaneData), 16, Color.white, 'c');   
        }
        if(PlaceType == 's'){
            for(int i = 0; i < PolygonData.size(); i++){
                Point PA = PolygonData.get(i);
                Point PB = PolygonData.get((i+1)%PolygonData.size());
                View.print(new Point3DM[]{new Point3DM(PA.x, PA.y, 0), new Point3DM(PB.x, PB.y, 0)}, Color.white);
                View.print(new Point3DM[]{new Point3DM(PA.x, PA.y, PlaneData[0]), new Point3DM(PB.x, PB.y, PlaneData[0])}, Color.white);
                
            }
        }
        if(PlaceType == '\0'){
            
            if(Selectee instanceof Sprite3DM){
                Area.Painter.translate(tX, tY + 80);
                Area.Painter.setColor(((Sprite3DM)Selectee).Tint);
                Area.Painter.draw(DeScaler.createTransformedShape(((Sprite3DM)Selectee).Image));
                
                Area.Painter.translate(-tX, -tY - 80);
            }else if(Selectee instanceof Point3DM[]){
                Area.printLine(tX, tY + 80, "Plane", 16, Color.white, 'c');
            }
        }
        
        Area.printLine(tX, tY + 140, GUI.round(Target.POSX, 1) + " " + GUI.round(Target.POSY, 1), 16, Color.white, 'c');
        
        if(WinCondition instanceof UnitObjective){
            UnitObjective O = (UnitObjective)WinCondition;
            Sprite3DM S = new Sprite3DM(CircleShape, new Point3DM(0, 0, 0), Color.red);
            for(int i = 0; i < O.N; i++){
                S.Center = O.Targets[i].Center;
                S.printNow(Area.Painter, View);
            }
        }
        if(LoseCondition instanceof UnitObjective){
            UnitObjective O = (UnitObjective)LoseCondition;
            Sprite3DM S = new Sprite3DM(CircleShape, new Point3DM(0, 0, 0), Color.cyan);
            for(int i = 0; i < O.N; i++){
                
                S.Center = O.Targets[i].Center;
                S.printNow(Area.Painter, View);
            }
        }
        
        Area.Painter.setColor(Color.white);
        Area.Painter.drawRect(1, 1, X - 3, Y - 3);
        Area.Painter.drawRect(3, 3, X - 7, Y - 7);
    }   
    public void actionPerformed(ActionEvent e){
        
        paintBackground();
        
        doGrid();
        for(int i = 0; i < Planes.size(); i++){
            Point3DM[] Points = (Point3DM[])Planes.elementAt(i);
            View.print(Points, Color.white);
            View.print(new Point3DM[]{Points[0], Points[Points.length-1]}, Color.white);
          
        }
        for(int i = 0; i < Units.size(); i++){
            ((Sprite3DM)Units.elementAt(i)).printNow(Area.Painter, View);
           
        }
        for(int i = 0; i < NavPoints.size(); i++){
            ((Sprite3DM)NavPoints.elementAt(i)).printNow(Area.Painter, View);
           
        }
        Point2D.Double PA = new Point2D.Double();
        Point2D.Double PB = new Point2D.Double();
        Point3DM P3 = new Point3DM(0,0,0);
        
        for(int i = 0; i < UnitOrders.size(); i++){
            Area.Painter.setColor(((Sprite3DM)Units.get(i)).Tint);
            Order O = (Order)UnitOrders.elementAt(i);
            if(O.TargetS != null){
                O.TargetS.Center.center(View, P3);
            }else if(O.TargetP != null){
                O.TargetP.center(View, P3);
            }else{
                continue;
            }
            P3.toScreen(View, PA);
            ((Sprite3DM)Units.elementAt(i)).Center.center(View, P3);
            P3.toScreen(View, PB);
            Area.Painter.drawLine((int)PA.x, (int)PA.y, (int)PB.x, (int)PB.y);
            
        }
      //  View.printAll();
        drawGUI();
        doControls();

        Area.paintComponent(null);
    }
    Shape TurretShape = new Rectangle2D.Float(-50, -50, 100, 100);
    Shape CraftShape = new Polygon(new int[]{-50, 0, 50}, new int[]{-50, 50, -50}, 3);
    Shape NavPointShape = new Polygon(new int[]{50, 50, -50, -50, /**/-50, 50, 50}, new int[]{-50, 50, -50, 50,/**/ -50, 50, -50}, 7);
    Shape CircleShape = new Ellipse2D.Double(-75, -75, 150, 150);
    
    public void selectSomething(Point3DM Target){
        Selectee = null;
        double Distance = 100000000;
        
        
        for(int i = 0; i < Planes.size(); i++){
            Point3DM P = new Point3DM(0, 0, 0);
            Point3DM[] Plane = (Point3DM[])Planes.elementAt(i);
            for(int j = 0; j < Plane.length; j++){
                P.add(Plane[j], P);
            }
            P.divide(Plane.length, P);
            
            double D = P.distanceTo(Target);
            if(D < Distance){
                Selectee = Plane;
                Distance = D;
            }   
        }
        for(int i = 0; i < Units.size(); i++){
            Point3DM P = ((Sprite3DM)Units.elementAt(i)).Center;
            double D = P.distanceTo(Target);
            if(D < Distance){
                Selectee = Units.elementAt(i);
                Distance = D;
            }
        }
        for(int i = 0; i < NavPoints.size(); i++){
            Point3DM P = ((Sprite3DM)NavPoints.elementAt(i)).Center;
            double D = P.distanceTo(Target);
            if(D < Distance){
                Selectee = NavPoints.elementAt(i);
                Distance = D;
            }
        }
        if(Distance < 0.2 * View.POS.POSZ){
            if(Units.contains(Selectee)){
                int A = Units.indexOf(Selectee);
                Guns = (int[])UnitGuns.elementAt(A);
                Data = (int[])UnitData.elementAt(A);
            }else if(Planes.contains(Selectee)){
                Guns = new int[5];
                Data = (int[])BaseData.clone();
            }
            
            
        }else{
            deselect();
        }
    }
    public void addToObjective(Point3DM Target){
        double Distance = 0.2 * View.POS.POSZ;
        Sprite3DM MyUnit = null;
        for(int i = 0; i < Units.size(); i++){
            Point3DM P = ((Sprite3DM)Units.elementAt(i)).Center;
            double D = P.distanceTo(Target);
            if(D < Distance){
                MyUnit = (Sprite3DM)Units.elementAt(i);
                Distance = D;
            }
        }
        
        if(MyUnit != null){
            
            UnitObjective O = (UnitObjective)(PlaceType == 'j' ? WinCondition : LoseCondition);

            O.Targets[O.N] = MyUnit;
            O.N++;

        }
    }
    public void mouseClicked(MouseEvent Event) {
        
    }
    
    public void mouseEntered(MouseEvent Event) {
    }
    
    public void mouseExited(MouseEvent Event) {
    } 
    public void placeOrder(Point3DM Target){
        Sprite3DM Selected = null;
        double Distance = 100000000f;
        for(int i = 0; i < Units.size(); i++){
            Point3DM P = ((Sprite3DM)Units.elementAt(i)).Center;
            double D = P.distanceTo(Target);
            if(D < Distance){
                Selected = (Sprite3DM)Units.elementAt(i);
                Distance = D;
            }
        }
      
        if(Distance < 0.2 * View.POS.POSZ && ((Order)Selectee).O >= 4){
            ((Order)Selectee).TargetS = Selected;
            ((Order)Selectee).TargetP = null;
        }else{
            ((Order)Selectee).TargetS = null;
            ((Order)Selectee).TargetP = Target.copy();
        }
        System.out.println(((Order)Selectee).TargetS + "\t" + ((Order)Selectee).TargetP);
        PlaceType = '\0';
        deselect();
        Selectee = null;
    }
    Point3DM Target = new Point3DM(0, 0, 0);
    public void makePolygon(){
        for(int i = 0; i < PolygonData.size(); i++){
            Point PA = PolygonData.get(i);
            Point PB = PolygonData.get((i+1)%PolygonData.size());
            int Z = PlaneData[0];
            Planes.add(new Point3DM[]{new Point3DM(PA.x, PA.y, 0), new Point3DM(PA.x, PA.y, Z), new Point3DM(PB.x, PB.y, Z), new Point3DM(PB.x, PB.y, 0)});
        }
        PolygonData.clear();
    }
    Vector<Point3DM> Sealant = new Vector<Point3DM>();
    public void sealTarget(Point3DM Target){
        
        Point3DM PTarget = null;
        double MinDistance = 1.0/0.0;
        for(int i = 0; i < Planes.size(); i++){
            for(int j = 0; j < ((Point3DM[])Planes.get(i)).length; j++){
                
                Point3DM P = ((Point3DM[])Planes.get(i))[j];
                
                if(P.POSZ != 0 && (P.flatDistanceTo(Target) < MinDistance)){
                    PTarget = P;
                    MinDistance = P.flatDistanceTo(Target);
                }
            }
        }
        System.out.println(PTarget + " added to Sealant");
        Sealant.add(PTarget);
    }
    public void makeSeal(){
        Point3DM[] Seal = new Point3DM[Sealant.size()];
        Sealant.toArray(Seal);
        Planes.add(Seal);
        System.out.println("Sealed " + Sealant.size());
        Sealant.clear();
    }
    public void mousePressed(MouseEvent Event) {
        System.out.println("CLICK");
        
        Target.POSY = View.POS.POSY + (Event.getY() - Y / 2) * View.POS.POSZ / View.ScreenY *2;
        Target.POSX = View.POS.POSX + -(Event.getX() - X / 2) * View.POS.POSZ / View.ScreenY *2;
        switch(PlaceType){
            case 's':{
                if(Event.getButton() == 1){
                    PolygonData.add(new Point((int)Target.POSX, (int)Target.POSY));
                }else if(Event.getButton() == 3){
                    makePolygon();
                }
                }break;
            case 'e':{
                if(Event.getButton() == 1){   
                    sealTarget(Target);
                }else{
                    makeSeal();
                }
                }break;
            case 'j': case 'J':{
                addToObjective(Target);
                }break;
            case 'o':{
                
                placeOrder(Target);
                }break;
            case '\0':{
                selectSomething(Target);
                }break;
            case 'r':{
                
                makeRing((int)Target.POSX, (int)Target.POSY, PlaneData[0], PlaneData[1], PlaneData[2]);
                }break;
            case 'p':{
                makePyramid((int)Target.POSX, (int)Target.POSY, (int)(PlaneData[0] > 0 ? PlaneData[0] : -PlaneData[0] * (0.5 + Math.random())), (int)(PlaneData[1] > 0 ? PlaneData[1] : -PlaneData[1] * (0.5 + Math.random())));
                }break;
            case 't':{
                Sprite3DM S = new Sprite3DM(TurretShape, Target, ColorChart[PlaceSide]);
                System.out.println(Target);
                Units.add(S);
                UnitData.add(Data.clone());
                UnitGuns.add(Guns.clone());
                UnitOrders.add(new Order());
                }break;
            case 'c':{
                Sprite3DM S = new Sprite3DM(CraftShape, Target, ColorChart[PlaceSide]);
                Units.add(S);
                UnitData.add(Data.clone());
                UnitGuns.add(Guns.clone());
                UnitOrders.add(new Order());
                }break;
            case 'n':{
                Sprite3DM S = new Sprite3DM(NavPointShape, Target, ColorChart[PlaceSide]);
                NavPoints.add(S);
                }break;
        }
    }
    
    public void mouseReleased(MouseEvent Event) {
    }
    public int getSide(Color C){
        for(int i = 0; i < ColorChart.length; i++){
            if(C.equals(ColorChart[i])){
                return i;
            }
        }
        return -1;
    }   
    
    public void mouseDragged(MouseEvent Event) {
        Target.POSY = View.POS.POSY + (Event.getY() - Y / 2) * View.POS.POSZ / View.ScreenY *2;
        Target.POSX = View.POS.POSX + (Event.getX() - X / 2) * View.POS.POSZ / View.ScreenY *2;
    }
    
    public void mouseMoved(MouseEvent Event) {
        Target.POSY = View.POS.POSY + (Event.getY() - Y / 2) * View.POS.POSZ / View.ScreenY *2;
        Target.POSX = View.POS.POSX + (Event.getX() - X / 2) * View.POS.POSZ / View.ScreenY *2;
    }
    
}


