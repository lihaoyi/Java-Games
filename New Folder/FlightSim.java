import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.Timer;
import java.util.*;
import javax.sound.sampled.*;
import javax.sound.midi.*;
import java.lang.Character;
import javax.imageio.*;
public class FlightSim{
    public static void main(String[] args){
        new FlightWindow();
    }
    
}

class FlightWindow extends JFrame{
    final int X = 1024;
    final int Y = 768;
    final Dimension ScreenSize = GUI.getScreenSize();
    
    private Vector<GameState> StateStack = new Vector<GameState>();
    public GameState BaseS;

    public GameState LevelS;
    public GameState HelpS;
    public GameState ContrS;
    public GameState InstrS;
	public GameState LevelMenuS;
	
    public JCanvas Area;
    public String RunDirectory = System.getProperty("user.dir") + "\\";
    public String BaseLevelPath = RunDirectory + "FlightLevels\\";
    public String BaseCraftDataPath = RunDirectory + "FlightCrafts\\";
    public String BaseTextPath = RunDirectory + "FlightText\\";
    public String BaseSoundPath = RunDirectory + "FlightSound\\";
    
    public static float[][] CraftDatas;
    public static String[] LevelPaths;
    public static String[] LevelNames;
    public static String[] LevelTexts;
    
    public static HashMap<String, WaveSound> SoundMap = new HashMap<String, WaveSound>();
    private GameState currentState(){
        return (GameState)StateStack.lastElement();
    }
    private void addListeners(GameState S){
        if(S instanceof MouseWheelListener){ Area.addMouseWheelListener((MouseWheelListener)S); }
        if(S instanceof KeyListener){ this.addKeyListener((KeyListener)S); }
        if(S instanceof MouseListener){ Area.addMouseListener((MouseListener)S); }
        if(S instanceof MouseMotionListener){ Area.addMouseMotionListener((MouseMotionListener)S); }
    }
    private void removeListeners(GameState S){
        if(S instanceof MouseWheelListener){ Area.removeMouseWheelListener((MouseWheelListener)S); }
        if(S instanceof KeyListener){ this.removeKeyListener((KeyListener)S); }
        if(S instanceof MouseListener){ Area.removeMouseListener((MouseListener)S); }
        if(S instanceof MouseMotionListener){ Area.removeMouseMotionListener((MouseMotionListener)S); }
    }
   
    public void addState(GameState S){
        if(StateStack.size() != 0){
            removeListeners(currentState());
        }
        try{
            currentState().exit();
        }catch(Exception e){}
        addListeners(S);
        StateStack.add(S);
        S.enter();
    }
    public void popState(int I){
        for(int i = 0; i < I; i++){
            popState();
        }
    }
    public GameState popState(){
        GameState S = currentState();
        removeListeners(S);
        
        
        S.exit();
        StateStack.remove(S);
        
        if(StateStack.size() == 0){
            System.exit(0);
        }
        addListeners(currentState());
        currentState().enter();
        return S;
    }

    public FlightWindow(){
        //BASIC INITIALIZATION
        super("Window"); 
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        //SET CONTENT PANE
        Container ContentArea = getContentPane();
        ContentArea.setLayout(null);
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        this.setLocation(ScreenSize.width / 2 - X / 2, ScreenSize.height / 2 - Y / 2);
        this.setUndecorated(true); 
        
        Area = new JCanvas();
        this.setLocation(ScreenSize.width / 2 - X / 2, ScreenSize.height / 2 - Y / 2);
        Area.setBounds(0, 0, X, Y);
     
        Area.init(BufferedImage.TYPE_BYTE_INDEXED);
     
        ContentArea.add(Area);
        Area.Painter.setClip(0, 0, Area.W, Area.H);
        Area.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        
        
        
        
        
       
        //ADD CONTENT PANE AND PACK
        this.setContentPane(ContentArea);
        this.setVisible(true);
        
        loadStuff();
        initStates();

        //this.pack();   
    }
    
    private void initStates(){

        ContrS = new TextState(this, Area, "Controls", BaseTextPath + "ControlInstructions.txt");
        InstrS = new TextState(this, Area, "Instructions", BaseTextPath + "PlayInstructions.txt");
        HelpS = new MenuState(this, Area, "Help", "Instructions  Controls".split("  "), new Object[]{InstrS, ContrS});
        LevelS = new MenuState(this, Area, "Select Level", LevelNames, LevelPaths);
        BaseS = new MenuState(this, Area, "Menu", new String[]{"Help", "Play"}, new Object[]{HelpS, LevelS});
        LevelMenuS = new MenuState(this, Area, "Menu", new String[]{"Exit", "Controls"}, new Object[]{2, ContrS});
        addState(BaseS);
    }
    
    private void loadStuff(){
        loadCraftDatas();
        loadLevelPaths();
        loadSounds();
    }
    private void loadCraftDatas(){
        System.out.println("Loading Craft Data");
        String[] Crafts = FileHandler.readFile(BaseCraftDataPath + "CraftList.txt");
        CraftDatas = new float[Crafts.length][];
        for(int i = 0; i < Crafts.length; i++){
            String[][] MyData = FileHandler.readFile(BaseCraftDataPath + Crafts[i], " = ");
            CraftDatas[i] = new float[MyData.length];
            for(int j = 0; j < MyData.length; j++){
                CraftDatas[i][j] = Float.parseFloat(MyData[j][1]);
            }
            System.out.println(Crafts[i] + ":\t" + Arrays.deepToString(MyData));
        }
        
    }
    public void addFlightState(String Path, int Index){
    	FlightState FS = new FlightState(this, Path, Index, LevelMenuS);
        addState(FS);
        addState(new MapState(FS, this, FS.Area, true));	
	}
	public void loadSounds(){
        System.out.println("Loading Sounds");
        String[] Data = FileHandler.readFile(BaseSoundPath + "SoundData.txt");

        for(int i = 0; i < Data.length; i++){
            String[] Tokens = Data[i].split("\t");
            System.out.print(Tokens[0] + "\t");
            SoundMap.put(Tokens[0], new WaveSound(BaseSoundPath + Tokens[0] + ".wav", Float.parseFloat(Tokens[1]), Float.parseFloat(Tokens[2])));
            
            
            
        }

        System.out.println("\n" + Data.length + " 3D Sounds Loaded");
        
        Data = FileHandler.readFile(BaseSoundPath + "FlatSoundData.txt");
        for(int i = 0; i < Data.length; i++){
            System.out.print(Data[i] + "\t");
            SoundMap.put(Data[i], new WaveSound(BaseSoundPath + Data[i] + ".wav", 0));            
        }
        System.out.println("\n" + Data.length + " Flat Sounds Loaded");
    }
    private void loadLevelPaths(){
        System.out.println("Loading Levels");
        String[][] Levels = FileHandler.readFile(BaseLevelPath + "LevelList.txt", "\t");
        LevelPaths = new String[Levels.length];
        LevelNames = new String[Levels.length];
        LevelTexts = new String[Levels.length];
        for(int i = 0; i < LevelPaths.length; i++){
            LevelPaths[i] = BaseLevelPath + Levels[i][0];
        }
        for(int i = 0; i < LevelPaths.length; i++){
            LevelNames[i] = Levels[i][1];
        }
        for(int i = 0; i < LevelPaths.length; i++){
            LevelTexts[i] = Levels[i][2];
        }
        System.out.println(Arrays.deepToString(Levels));
        
    }
    
    
}

abstract class GameState{
    FlightWindow Base;
    BufferedImage Background;
    public abstract void print(); //print screen
    public abstract void exit(); //what to do when exiting state
    public abstract void enter(); //what to do when entering state
    public void drawBorder(){
    	//Border
    	if(Base == null)return;
        Base.Area.Painter.setColor(Color.white);
        Base.Area.Painter.drawRect(1, 1, Base.Area.W - 3, Base.Area.H - 3);
        Base.Area.Painter.drawRect(3, 3, Base.Area.W - 7, Base.Area.H - 7);	
    }
 
}

class TextState extends MenuState implements MouseListener, MouseWheelListener, ActionListener{
    String[] Text;
    final int TextFont = 16;
    final int TextGap = 8;
    final int LineHalfWidth = 200;
    String TabBlanks = "";
    int TextOffset;
    int LineCount;
    Timer Stopwatch = new Timer(50, this);
    
    public TextState(FlightWindow F, JCanvas Area, String Title, String Path){
        this(F, Area, Title, FileHandler.readFile(Path));
    }
    public TextState(FlightWindow F, JCanvas Area, String Title, String[] Txt){
        
        super(F, Area, Title, new String[]{}, new Object[]{});
        for(int i = 0; i < Count; i++){
            ButtonAreas[i].y += Area.H / 4;
        }
        Boundary = new Rectangle(ButtonAreas[0].x - ButtonGap, ButtonAreas[0].y - ButtonGap, ButtonAreas[Count-1].width + 2*ButtonGap, ButtonHeight * Count + ButtonGap * (Count + 1));
        process(Txt, Area);

    }
    public void process(String[] Txt, JCanvas Area){
        Font FONT = Area.Painter.getFont();
        Area.Painter.setFont(FONT.deriveFont((float)TextFont));
        FontMetrics M = Area.Painter.getFontMetrics();
        Area.Painter.setFont(FONT);
        Vector Strings = new Vector();
        for(TabBlanks = ""; M.stringWidth(TabBlanks) < M.stringWidth("______"); TabBlanks+=" ");
        LineCount = Area.H/3 / (TextFont + TextGap);
        
        for(int i = 0; i < Txt.length; i++){
            Txt[i] = Txt[i].replaceAll("\t", " ______ ");
            String[] TxtBits = Txt[i].split(" ");
            String S = "";

            for(int j = 0; j < TxtBits.length; j++){
                if(M.stringWidth(S + " " + TxtBits[j]) > LineHalfWidth*2){
                    S = S.replaceAll("______", TabBlanks);
                    Strings.add(S);
                    S = TxtBits[j];
                }else{
                    S += (S.length() == 0 ? "" : " ") + TxtBits[j];
                }
            }
            S = S.replaceAll("______", TabBlanks);
            Strings.add(S);
            
        }
        String[] FinalText = new String[Strings.size()];
        Strings.toArray(FinalText);
        
        Text = FinalText;
        
    }
    public void exit(){
        Stopwatch.stop();
    }
    public void enter(){
        Stopwatch.start();
    }
    public void actionPerformed(ActionEvent Event){
        print();
        
    }
    public void print(){
        JCanvas Area = Base.Area;
        Area.Painter.setColor(Color.black);
        Area.Painter.fillRect(0, 0, Area.W, Area.H);
        
        printButtons(Area, 0, Area.H / 4);
        int Y = Area.H/3-30 + TextGap;
        int X = Area.W / 2 - LineHalfWidth;
       // Area.Painter.drawRect(Area.W/2-LineHalfWidth, Area.H/3-30, 2*LineHalfWidth, Area.H/3);
        
        Area.printLine(Base.X / 2, Base.Y / 4, Title, TitleFont, Color.green, 'c');
        
        for(int i = 0; i < Text.length && Y <= 2*Area.H/3-30; i++){
            try{
                Area.printLine(X, Y, Text[i + TextOffset], TextFont, Color.green, 'l');
            }catch(Exception e){}
            Y += TextFont + TextGap;
        }
        if(LineCount < Text.length){
            Area.printLine(Base.X/2, Base.Y*2/3, "use mousewheel to scroll", 11, Color.green, 'c');
        }
        drawBorder();
        Area.paintComponent(null);
    }
    public void mouseWheelMoved(MouseWheelEvent Event){
        TextOffset += Event.getWheelRotation();
        TextOffset = GUI.cap(0, TextOffset, Math.max(0, Text.length-1-LineCount));
        System.out.println(TextOffset);
    }
}
class MenuState extends GameState implements MouseListener, ActionListener, KeyListener{
    String Title;
    int Count;
    String[] Buttons;
    Object[] Destinations;
    Rectangle[] ButtonAreas;
    Rectangle Boundary;
    Timer Stopwatch = new Timer(50, this);
    
    
    WaveSound ButtonClickSound;
    final int ButtonHeight = 50;
    final int ButtonWidth = 200;
    final int ButtonGap = 10;
    final int ButtonFont = 16;
    
    final int TitleFont = 20;
    public MenuState(FlightWindow F, JCanvas Area, String T, String[] B,  Object[] D){
        
        Title = T;
        Base = F;
        
        Buttons = new String[B.length+1];
        System.arraycopy(B, 0, Buttons, 0, B.length);
        Destinations = new Object[D.length+1];
        System.arraycopy(D, 0, Destinations, 0, D.length);
        Buttons[B.length] = "Back";
        Destinations[D.length] = new Integer(1);
        
        if(B.length != D.length){
            throw new IllegalArgumentException("Buttons and Destinations do not correspond");
        }
        Count = Buttons.length;
        ButtonAreas = new Rectangle[Count];
        
        int Y = Area.H / 2 - ButtonHeight * Count / 2 + ButtonGap * (Count - 1) / 2;
        int X = Area.W / 2;
        for(int i = 0; i < Count; i++){
            ButtonAreas[i] = new Rectangle(X - ButtonWidth / 2, Y - ButtonHeight / 2, ButtonWidth, ButtonHeight);
            Y += ButtonHeight + ButtonGap;
        }
        Boundary = new Rectangle(ButtonAreas[0].x - ButtonGap, ButtonAreas[0].y - ButtonGap, ButtonAreas[Count-1].width + 2*ButtonGap, ButtonHeight * Count + ButtonGap * (Count + 1));
		ButtonClickSound = (WaveSound)Base.SoundMap.get("BEEPPURE");
    }
    public void setBackground(BufferedImage B){
    	Background = B;	
    }
    public void exit(){
        Stopwatch.stop();
    }
    public void enter(){
  
        
        Stopwatch.start();

    }
    public void actionPerformed(ActionEvent Event){
        print();
        
    }
    public void print(){
        JCanvas Area = Base.Area;
        if(Background != null){
        	Area.Painter.drawImage(Background, 0, 0, null);
        }else{
	        Area.Painter.setColor(Color.black);
    	    Area.Painter.fillRect(0, 0, Area.W, Area.H);
    	 }
        Area.printLine(Area.W / 2, Area.H / 2 - ButtonHeight * Count / 2 + ButtonGap * (Count - 1) / 2 - ButtonHeight * 3 / 2, Title, TitleFont, Color.green, 'c');
        printButtons(Area, 0, 0);
        drawBorder();
        Area.paintComponent(null);
    }
    public void keyReleased(KeyEvent E){}
    public void keyPressed(KeyEvent E){
    	if(E.getKeyCode() == 27){ //Key = Esc
    		
    		hitButton(Count-1);
    	}
    	try{
    		
    		char C = E.getKeyChar();
    		int N = Integer.parseInt(""+C);
    		if(N == 0){
    			
    			hitButton(Count - 1);
    		}else if(N <= Count - 1){
    			
    			hitButton(N-1);
    		}
    			
    		
    	}catch(Exception e){
    		
    	}
    		
    }
    public void keyTyped(KeyEvent E){}
    public void printButtons(JCanvas Area, int dX, int dY){
        int Y = Area.H / 2 - ButtonHeight * Count / 2 + ButtonGap * (Count - 1) / 2 + dY;
        int X = Area.W / 2 + dX;
        
        for(int i = 0; i < Count; i++){
            Area.Painter.setColor(Color.green);
            Area.Painter.draw(ButtonAreas[i]);
            Area.printLine(X, Y , Buttons[i], ButtonFont, Color.green, 'c');
            Area.printLine(X - ButtonWidth/3, Y , "(" + (i == Count - 1 ? 0 : (i+1)) + ")", ButtonFont, Color.green, 'c');
            Y += ButtonHeight + ButtonGap;
        }
        
        Area.Painter.draw(Boundary);
    }
    public void mouseClicked(MouseEvent Event) {
        
    }
    
    public void mouseEntered(MouseEvent Event) {
    }
    
    public void mouseExited(MouseEvent Event) {
    }
    
    public void mousePressed(MouseEvent Event) {
        Point P = new Point(Event.getX(), Event.getY());
        for(int i = 0; i < Count; i++){
            if(ButtonAreas[i].contains(P)){
                hitButton(i);
            }
        }
    }
    public void hitButton(int i){
    	ButtonClickSound.play();
    	if(Destinations[i] instanceof GameState){
        	Base.addState((GameState)Destinations[i]);
        }else if(Destinations[i] instanceof String){
        	Base.addFlightState((String)Destinations[i], i);
            
        }else if(Destinations[i] instanceof Integer){
            //for(int j = 0; j < Math.abs(((Integer)).intValue()); j++){
                Base.popState(((Integer)Destinations[i]).intValue());
            //}
            
        }
    }
    public void mouseReleased(MouseEvent Event) {
    }
}
class MapState extends TextState implements KeyListener, MouseMotionListener, ActionListener{
    FlightState BaseWorld;
    ObserverM MapView;
    Timer Stopwatch = new Timer(30, this);
    Shape NavShape = new Polygon(new int[]{5, 5, -5, -5, /**/-5, 5, 5}, new int[]{-5, 5, -5, 5,/**/ -5, 5, -5}, 7);
    float BorderFactor = 0.5f;
    Rectangle BB = new Rectangle();
    int pS = 0;
    int[] Pings = new int[10];
    int PingSize = 50;
    public MapState(FlightState BW, FlightWindow B, JCanvas Area, boolean Scroll){
        //public TextState(FlightWindow F, JCanvas Area, String Title, String Path){
        super(B, BW.Area, FlightWindow.LevelNames[BW.Index], B.BaseLevelPath + FlightWindow.LevelTexts[BW.Index]);

        BaseWorld = BW;
        
        Base = B;
        
        MapView = new ObserverM(0, 0, 1500000, new Rotation3DM(new double[][]{{0, 1, 0}, {0, 0, -1}, {-1, 0, 0}}), Base.Area.Painter);//new double[][]{{0, 1, 0}, {0, 0, -1}, {-1, 0, 0}}
        
        MapView.setArea(BW.X/2, 0, BW.X, BW.Y, 1);

        MapView.setLookVector();
        initBox();
        process(Text, Area);
        if(Scroll){
            pS = 0;
        }else{
            pS = 10000;
        }
        for(int i = 0; i < Text.length; i++){
            Text[i] += " ";
        }
    }
    public void initBox(){
        
        float sX = -1.0f/0; float sY = -1.0f/0; float tX = 1.0f/0; float tY = 1.0f/0;
        for(int i = 0; i < BaseWorld.NavPoints.size(); i++){
            Point3DM P = BaseWorld.NavPoints.ps(i).Form.Center;
            sX = (float)Math.max(P.POSX, sX);
            sY = (float)Math.max(P.POSY, sY);
            tX = (float)Math.min(P.POSX, tX);
            tY = (float)Math.min(P.POSY, tY);
        }
        BB.x = (int)tX;
        BB.y = (int)tY;
        BB.width = (int)(sX - tX);
        BB.height = (int)(sY - tY);
     
        BB.x -= (int)(BB.width * BorderFactor / 2);
        BB.y -= (int)(BB.height * BorderFactor / 2);
        BB.width *= 1 + BorderFactor;
        BB.height *= 1 + BorderFactor;
        
    }
   
    public void enter(){
        Stopwatch.start();
    }
    public void exit(){
        Stopwatch.stop();
    }
    public void keyPressed(KeyEvent Event){
        Base.popState();
    }
    public void keyTyped(KeyEvent Event){}
    public void keyReleased(KeyEvent Event){}
    public void mouseMoved(MouseEvent Event){
   //    BorderFactor += 0.01f;
    }
    public void mouseDragged(MouseEvent Event){
       // BaseWorld.NavPoints.ps(0).Form.Center.POSX += 100;
    }
    public void actionPerformed(ActionEvent Event){
        
 
        pS++;
        print();
     //   System.out.println("ACTION");
    }
    
    public void print(){
        initBox();
        //System.out.println(BB);
        MapView.POS.POSX = BB.x + BB.width/2;
        MapView.POS.POSY = BB.y + BB.height/2;
        
        double gX = MapView.ScreenX* 1.0/MapView.ScreenY;
        double gY = 1;
        double AR = BB.width* 1.0/BB.height;
        if(AR < gX){
            MapView.setMag(2 * gY / (BB.height/MapView.POS.POSZ));
            
        }else{
            MapView.setMag(2 * gX / (BB.width/MapView.POS.POSZ));
        }
     
        JCanvas Area = Base.Area;
        MapView.Painter = Area.Painter;
        Area.Painter.setColor(Color.black);
        Area.Painter.fillRect(0, 0, Base.X, Base.Y);
           
        Area.Painter.setColor(Color.green);
        Area.Painter.drawRect(Base.X/2+2, 0+2, Base.X/2-4, Base.Y-4);
        Area.Painter.drawRect(Base.X/2+4, 0+4, Base.X/2-8, Base.Y-8);
        
        for(int i = 0; i < BaseWorld.Planes.size(); i++){
            BaseWorld.Planes.pl(i).Form.printNow(Area.Painter, MapView);
        }
        Point3DM Difference = new Point3DM();
        Point2D.Double Flat = new Point2D.Double();
        Area.Painter.setColor(Color.green);
        for(int i = 0; i < BaseWorld.NavPoints.size(); i++){
            
            Point3DM N = BaseWorld.NavPoints.ps(i).Form.Center;
           
            
            N.center(MapView, Difference);
            Difference.toScreen(MapView, Flat);
            Area.Painter.translate(Flat.x, Flat.y);
            Area.Painter.draw(NavShape);
            Area.printLine(0, 15, GUI.getGreek(i), 12, Color.green, 'c');
            if(Pings[i] != 0){
                Pings[i] = PingSize - Pings[i];
                Area.Painter.drawOval(-Pings[i]/2, -Pings[i]/2, Pings[i], Pings[i]);
                Pings[i] = PingSize - Pings[i];
                Pings[i]--;
            }
            Area.Painter.translate(-Flat.x, -Flat.y);
            
        }
        
        int Y = Area.H/3-30 + TextGap;
        int X = Area.W / 4 - LineHalfWidth;
       // Area.Painter.drawRect(Area.W/2-LineHalfWidth, Area.H/3-30, 2*LineHalfWidth, Area.H/3);
        
        Area.printLine(Base.X / 4, Base.Y / 4, Title, TitleFont, Color.green, 'c');
        Area.printLine(X + LineHalfWidth, 2 * Y, "press any key to continue.", 12, Color.green, 'c');
        for(int i = 0, P = 0; i < Text.length && Y <= 2*Area.H/3-30; i++){
      
            if(P + Text[i + TextOffset].length() < pS){
                Area.printLine(X, Y, Text[i + TextOffset].substring(0, Text[i + TextOffset].length()), TextFont, Color.green, 'l');
                P += Text[i + TextOffset].length();
            }else{
                Area.printLine(X, Y, Text[i + TextOffset].substring(0, pS - P), TextFont, Color.green, 'l');
                for(int j = 0; j < 10; j++){
                    String S = GUI.getGreek(j);
                    if(pS - P >= S.length() && Text[i + TextOffset].substring(pS - P - S.length(), pS - P).equals(S)){
                        Pings[j] = PingSize;
                    }
                }
                break;
            }

            Y += TextFont + TextGap; 
        }
        drawBorder();
        Area.paintComponent(null);
    
    }
}
class FlightState extends GameState implements KeyListener, ActionListener, MouseListener, MouseMotionListener, MouseWheelListener{
    
    final int TopBorderWidth = 0;
    final int X = 1024 - TopBorderWidth;
    final int Y = 768;
    final Dimension ScreenSize = GUI.getScreenSize();
    
    static class World{
        final static Point3DM Gravity = new Point3DM(0, 0, -9.81);

        public static Point3DM getGravity(){
            return Gravity;
        }
        static double K = 1.402; //Adiabetic Index
        
        public static double getSpeedofSound(double h){
            return Math.sqrt(K * R * getAirTemperature(h) / M);
        }
        static double TO = 288; //Temperature at Sea Level
        static double L = -0.0065; //Rate of Temperature Decrease
        public static double getAirTemperature(double h){
            
            return TO + L*h;
        }
        static double PO = 101325; //Pressure at Sea Level
        
        
        static double R = 8.314; //Universal Gas Constant
        static double M = 0.02896; //Molecular Weight of Dry Air
        static double G = 9.81; //Acceleration due to Gravity
        static double Konstant= - (G * M) / (R * L);
    
        public static double airDensity(double h){
            
            double P = PO * Math.pow(1 + L*h / TO, Konstant);
            double Rho = (P*M)/(R*getAirTemperature(h));
            return Rho;
        }
        
    }
    abstract class Object3DM{
        Shape3DM Form;
        Point3DM Momentum;
        Point3DM Acceleration;
        boolean DynamicCollision = true;
        double CollisionRadius;
        boolean Exists;
        String Name;
        int Side;
    }
    class Position3DM extends Object3DM implements Comparable{
        public Position3DM(){
            Form = new Shape3DM();
            Momentum = new Point3DM(0, 0, 0);
            Acceleration = new Point3DM(0, 0, 0);
        }
        public Position3DM(Point3DM XYZ, double Radius, int S){
            this();
            Form = new Shape3DM();
            CollisionRadius = Radius;
            Form.Center = XYZ;
            Side = S;
        }
        public Position3DM(Object3DM Target, double Radius, int S){
            Form = Target.Form;
            Momentum = Target.Momentum;
            Acceleration = Target.Acceleration;
            CollisionRadius = Radius;
            Side = S;
        }
        public String toString(){
            return Form.Center.toString() + "\t" + Momentum.toString() + "\t" + CollisionRadius + " " + Side;
        }
        public int compareTo(Object O){
            return new Double(CollisionRadius).compareTo(new Double(((Position3DM)O).CollisionRadius));
        }
    }
    
    class GeneralVector extends Vector{
      
        public Plane pl(int INTA){
            return (Plane)elementAt(INTA);
        }
 
        public Gun gun(int INTA){
            return (Gun)elementAt(INTA);
        }
     
        public void removeLastElement(){
            removeElementAt(size() - 1);
        }
        
    }
    class ObjectVector{
        private Vector V;
        public boolean contains(Object3DM A){
            return V.contains(A);
        }
        public ObjectVector(){
            V = new Vector();
        }
        public Object3DM obj(int INTA){
            return (Object3DM)V.get(INTA);
        }
        public Doodad d(int INTA){
            return (Doodad)V.get(INTA);
        }

        public Plane pl(int INTA){
            return (Plane)V.get(INTA);
        }

        public Craft cr(int INTA){
            return (Craft)V.get(INTA);
        }

        public Turret tu(int INTA){
            return (Turret)V.get(INTA);
        }

        public Vehicle ve(int INTA){
            return (Vehicle)V.get(INTA);
        }

        public TempSprite ts(int INTA){
            return (TempSprite)V.get(INTA);
        }

        public Projectile pr(int INTA){
            return (Projectile)V.get(INTA);
        }

        public Trail tr(int INTA){
            return (Trail)V.get(INTA);
        }

        public Position3DM ps(int INTA){
            return (Position3DM)V.get(INTA);
        }

        public Object3DM lastElement(){
            return (Object3DM)V.get(V.size() - 1);
        }
        public boolean add(Object3DM A){
            V.add(A); 
            A.Exists = true;        
            return true;
        }
        public void removeLastElement(){
            obj(V.size()-1).Exists = false;
            V.remove(V.size() - 1);
        }
        public void removeElementAt(int A){
            
            Object3DM O = obj(A);
            O.Exists = false;
            V.remove(A);
            
        }
        public boolean remove(Object3DM A){
            boolean B = V.remove(A);
            if(B){
                A.Exists = false;
            }
            return B;
        }
        
        public void clear(){
            for(int i = 0; i < V.size(); i++){
                obj(i).Exists = false;
            }
            V.clear();
        }
        public int size(){
            return V.size();
        }
        public Object[] toArray(){
            return V.toArray();
        }
        public int indexOf(Object3DM O){
            return V.indexOf(O);
        }
    }
    abstract class Objective{
        boolean HasBeenCompleted;
        abstract void fromBytes(DataInput I) throws IOException;
        abstract int checkComplete();
        abstract String getText();
        public void disable(){
            HasBeenCompleted = true;
        }
        
    }
    public Objective makeObjective(char C){
        switch(C){
            case 'u': return new UnitObjective();
            case 't': return new TimeObjective();
        
        }
        return null;
    }
   
    class UnitObjective extends Objective{
        Vehicle[] Targets;
        int N;
        boolean All;
        public void fromBytes(DataInput I) throws IOException{
            All = I.readBoolean();
            N = I.readInt();
            Targets = new Vehicle[N];
            for(int i = 0; i < N; i++){
                Targets[i] = VehicleStore.ve(I.readInt());
            }
        }  
        public String getText(){
            return "All " + N + " Targets Destroyed";
        }
        public int checkComplete(){
            
            if(All){
                for(int i = 0; i < N; i++){
                    if(Vehicles.contains(Targets[i]) || VehicleStore.contains(Targets[i])){
                        return 0;
                    }
                }
                if(HasBeenCompleted){
                    return 1;
                }else{   
                    HasBeenCompleted = true; 
                    return 2;
                }
                
            }else{
                for(int i = 0; i < N; i++){
                    if(Vehicles.contains(Targets[i]) || VehicleStore.contains(Targets[i])){
                        
                    }else{
                        if(HasBeenCompleted){
                            return 1;
                        }else{
                            HasBeenCompleted = true;
                            return 2;
                        }
                    }
                    
                }
                return 0;
            }
        }
    }
    class TimeObjective extends Objective{
        int EndTime;     
        public void fromBytes(DataInput I)  throws IOException{
            EndTime = I.readInt();
        }
        public String getText(){
            return EndTime + " Seconds Passed";
        }
        public int checkComplete(){
            if(UnPausedTime > EndTime){
                if(HasBeenCompleted){
                    return 1;
                }else{
                    HasBeenCompleted = true;
                    return 2;
                }
            }else{
                return 0;
            }
            
        } 
    }
    
    class Trail extends Object3DM{
        
        final Counter TrailCounter;
        Point3DM Target;
        final int TrailLength;
        boolean Active;
        final Counter DeathCounter;
        Object Owner;
       
        /*public static Trail getTrail(int PointCount, Point3DM Targ, Color T, double TotalDuration){

        }*/
        public Trail(Object O, int PointCount, Point3DM Targ, Color T, double TotalDuration){
            Owner = O;
            Form = new Shape3DM(true, T, Targ.POSX, Targ.POSY, Targ.POSZ);
            Utils3DM.setPoints(Form, 'h', PointCount, 0, 0);
            DeathCounter = new Counter(0, 3, 3);
            for(int i = 0; i < PointCount; i++){
                Form.Center.copyTo(Form.ActualPoints[i]);
            }
            TrailLength = PointCount;
            Form.MaxDistance = 0;
            TrailCounter = new Counter(0, TotalDuration / PointCount, TotalDuration / PointCount);
            Target = Targ;
            Active = true;
            
        }
        
        public void reset(){
            Target.copyTo(Form.Center);
            for(int i = 0; i < TrailLength; i++){
                Target.copyTo(Form.ActualPoints[i]);
            }
            DeathCounter.Max = 3;
            DeathCounter.Current = DeathCounter.Max;
            
            Active = true;
          
        }
        public boolean finished(){
            if(DeathCounter.checkBounds() < 0){
                return true;
            }
            return false;
        }
        public void disable(){
            Active = false;
        }
        public void enable(){
            if(!Active)reset();
            Active = true;

        }
        public void chase(float FramesPerSecond){

            Target.copyTo(Form.ActualPoints[0]); //First Point
            Target.copyTo(Form.Center); //Center


            TrailCounter.increment(-1.0 / FramesPerSecond);

            if(TrailCounter.checkBounds() >= 0){

                double LastDistance =(TrailCounter.Current) / (TrailCounter.Current + 1.0/FramesPerSecond);

                Form.ActualPoints[TrailLength - 1].subtract(Form.ActualPoints[TrailLength - 2], Form.ActualPoints[TrailLength - 1]);

                Form.ActualPoints[TrailLength - 1].multiply(LastDistance, Form.ActualPoints[TrailLength - 1]);

                Form.ActualPoints[TrailLength - 1].add(Form.ActualPoints[TrailLength - 2], Form.ActualPoints[TrailLength - 1]);
                
                Target.copyTo(Form.Center);
                Form.MaxDistance = Form.Center.distanceTo(Form.ActualPoints[Form.ActualPoints.length - 1]);
                return;
            }

            TrailCounter.Current = TrailCounter.Max;
            for(int i = TrailLength - 1; i > 0; i--){
                Form.ActualPoints[i - 1].copyTo(Form.ActualPoints[i]);   
            }
            
            Form.MaxDistance = Form.Center.distanceTo(Form.ActualPoints[Form.ActualPoints.length - 1]);

            if(Form.MaxDistance <= 0.001){
                DeathCounter.increment(-1);
                
            }

        }
        public void printNow(Graphics2D Painter, ObserverM Viewer){
            if(Active){
                
                Form.printNow(Painter, Viewer);
            }
        }
        public void printNow(Graphics2D Painter, ObserverM Viewer, Color Tint){
            if(Active){
                
                Form.printNow(Painter, Viewer, Tint);
            }
        }
        public void print(ObserverM Viewer){
            if(Active){
                
                Form.print(Viewer);

            }
        }

    }
    class Plane{
        Shape3DM Form;
        Color Tint = Color.white;
        Point3DM Normal;
        float BounceFactor = 0.0f;
        boolean Finite = true;
        //double[] CollConst = new double[3];
        
      //  Point3DM[] MidPoints; //Mid Point of Point N+1 and Point N
      // Point3DM[] MidNormals; //Outward facing unit parallel to plane perpendicular to line of points N+1 and N
      //  Point3DM[] EdgeUnits; //Unit Vector from Point N+1 to Point N
        
        
        public Plane(Point3DM[] C){
            Form = new Shape3DM();
            Point3DMShape[] Corners = new Point3DMShape[C.length];
            for(int i = 0; i < C.length; i++){
                Corners[i] = new Point3DMShape(Form, C[i].POSX, C[i].POSY, C[i].POSZ);
            }
            /*
            MidPoints = new Point3DM[Corners.length];
            MidNormals = new Point3DM[Corners.length];
            EdgeUnits = new Point3DM[Corners.length];
            */
            for(int i = 0; i < Corners.length; i++){
                Corners[i].Links = new Vector<Point3DM>();
                Corners[i].Links.add(Corners[(i + 1) % Corners.length]);
                Form.BasePoints.add(Corners[i]);
                Corners[i].Base = Form;

                
                
            }
            
            Form.finalizeLinks();
            for(int i = 0; i < Form.Points.length; i++){
                Form.ActualPoints[i] = Form.Points[i].copy();
            }
            /*
            Form.Center = new Point3DM(0, 0, 0);
          
            for(int i = 0; i < Form.Points.length; i++){
                Form.Center.add(Form.Points[i], Form.Center);
            }
            
            Form.Center.divide(Form.Points.length, Form.Center);
            */
            Utils3DM.center(Form, 'p');
            //System.out.println("Plane " + Form.MaxDistance);
            calculateNormal();
           

        }
       
        public void doCollision(Doodad Target, Point3DM ImpactSpot){
            final Point3DM EffectiveNormal = new Point3DM();
            final Point3DM Difference = new Point3DM();
            Target.Form.Center.subtract(ImpactSpot, Difference);
            Difference.copyTo(EffectiveNormal);
            EffectiveNormal.normalize();
            
            //
            if(Difference.dotProduct(Target.Momentum) < 0){
                EffectiveNormal.multiply(EffectiveNormal.dotProduct(Target.Momentum), Difference);
                Difference.multiply(Target.BounceFactor + 1.0, Difference);
                Target.Momentum.subtract(Difference, Target.Momentum);
            }   
            
        }
        public void doCollision(Vehicle Target, Point3DM ImpactSpot){
            final Point3DM Temp = new Point3DM();
            final Point3DM EffectiveNormal = new Point3DM();
            Target.Form.Center.subtract(ImpactSpot, EffectiveNormal);
            EffectiveNormal.normalize();
            
            //Push Craft away from Point of Collision
            
            Target.Form.Center.subtract(ImpactSpot, Temp);
            
            EffectiveNormal.multiply(Math.max(0,  Target.CollisionRadius - Temp.length()), Temp);
            Target.Form.Center.add(Temp, Target.Form.Center);
            
            
            //Cut out movement towards Point of Collision
            if(ImpactSpot.subtract(Target.Form.Center).dotProduct(Target.Momentum) > 0){
                EffectiveNormal.multiply(EffectiveNormal.dotProduct(Target.Momentum), Temp);
                Temp.multiply(BounceFactor + 1.0, Temp);
             
                double RemovedSpeed = Temp.length();
                
                playSound("VehicleHitExplosion", ImpactSpot, RemovedSpeed / 200);
                Target.Momentum.subtract(Temp, Target.Momentum);
             //   System.out.println(Temp.length());
                for(int i = 2; i < RemovedSpeed; i += 5){
                    if(GUI.randomInt(1, 3) == 3){
                        Doodad Debris = createDebris(GUI.randomInt(1, 15), ImpactSpot.add(Utils3DM.getRandomUnit().multiply(Math.random() * 2)), Math.random() * Target.Momentum.length() * 5, (Math.random() > 0.5 ? this.Form.Tint : Target.Form.Tint));
                        Doodads.add(Debris);
                    }
                }
            }else{
                
            }
             
        }
        
        //public static Point3DM lineSphereCollision(Point3DM LineA, Point3DM LineB, Point3DM SphereCenter, double Radius){
        public Point3DM checkCollision(Point3DM PA, Point3DM PB){
            return checkCollision(PA, PB, 0);
        }
        public Point3DM checkCollision(Point3DM PA, Point3DM PB, double Extension){
            final Point3DM DA = new Point3DM();
            final Point3DM DB = new Point3DM();
            final Point3DM DD = new Point3DM();
            final Point3DM DeltaA = new Point3DM();
            final Point3DM DeltaB = new Point3DM();

            Form.Center.subtract(PA, DA);
            Form.Center.subtract(PB, DB);
            PA.subtract(PB, DD);
            double NA = DA.dotProduct(Normal);
            double NB = DB.dotProduct(Normal);
            if(NA / NB > 0){

                return null;


            }else{

                DD.multiply(NB / (NB - NA), DD);
                PB.add(DD, DB);
                if(!Finite){
                    return DB;
                }
                
                for(int i = 0; i < Form.ActualPoints.length; i++){
                    Form.ActualPoints[(i+1)%Form.ActualPoints.length].subtract(Form.ActualPoints[i], DeltaA);
                    DeltaA.normalize();
                    DB.subtract(Form.ActualPoints[i], DeltaB);
                    if(DeltaA.crossProduct(DeltaB).dotProduct(Normal) < -Extension){
                        return null;
                    }
                }
                return DB;
                
            }
           

        }
        public Point3DM checkCollision(Object3DM Target){
            if(Target.Form == null || !Target.DynamicCollision){
                //System.out.println(12);
                return null;
                
            }
            
            Point3DM Center = Target.Form.Center;
            double CollisionRadius = Target.CollisionRadius;
            final Point3DM SurfaceImage = new Point3DM();
            final Point3DM Difference = new Point3DM();
            final Point3DM Temp = new Point3DM();

            //Check Bounding Sphere for Triangle

            Center.subtract(Form.Center, Difference);

            if(Difference.length() > CollisionRadius + Form.MaxDistance){ 
                return null;
            }

            //Gets perpendicular projection of center of craft onto infinite plane
            Normal.multiply(Difference.dotProduct(Normal), SurfaceImage);
            Center.subtract(SurfaceImage, SurfaceImage);

            //Calculates distance of craft to perpendicular plane
            Center.subtract(SurfaceImage, Difference);

            if(Difference.length() > CollisionRadius){
    
                return null;
            }
            //If plane is infinite, return collision
            if(!Finite){
                return SurfaceImage;
            }

            //If close enough, check to see if it collides with the limited plane
            Point3DM P = SurfaceImage;

            /*
            for(int i = 0; i < MidPoints.length; i++){
                MidPoints[i].subtract(P, Difference);
                if(Difference.dotProduct(MidNormals[i]) > 0) Out = true;
            }
            if(Out == false){   
                return SurfaceImage.copy();          
            }
             */
            boolean Out = false;
            Point3DM DeltaA = new Point3DM();
            Point3DM DeltaB = new Point3DM();
            for(int i = 0; i < Form.ActualPoints.length; i++){
                Form.ActualPoints[(i+1)%Form.ActualPoints.length].subtract(Form.ActualPoints[i], DeltaA);
                SurfaceImage.subtract(Form.ActualPoints[i], DeltaB);
                if(DeltaA.crossProduct(DeltaB).dotProduct(Normal) < 0){
                    Out = true;
                }
            }
            if(Out == false){
                
                return SurfaceImage;
            }

            



            //Otherwise check to see if it collides with the edges
            for(int a = 0; a < Form.ActualPoints.length; a++){
                Point3DM Image = Utils3DM.lineSphereCollision(Form.ActualPoints[a], Form.ActualPoints[(a + 1) % Form.ActualPoints.length], Center, CollisionRadius);
                
                if(Image != null){

                    return Image.copy();
                }

            }
            //Lastly, check to see if it collides with corners
            for(int a = 0; a < Form.ActualPoints.length; a++){
                Center.subtract(Form.ActualPoints[a], Difference);
                if(Difference.length() <= CollisionRadius){
                    return Form.ActualPoints[a].copy();
                }
            }

            return null;

        }
        public void calculateNormal(){
            Normal = (Form.Points[1].subtract(Form.Points[0])).crossProduct(Form.Points[2].subtract(Form.Points[0]));
            Normal.normalize();
            
        }

        public void print(ObserverM Viewer){
            
            Form.print(Viewer);
          //  S.ActualCenter = Form.Center;
            //S.print(Painter, Viewer);
        }
     //   Sprite3D S = new Sprite3D(new Ellipse2D.Double(-10, -10, 20, 20), new Point3DM(0, 0, 0), Color.white);
    }
    class Gun{
        Object3DM Base;
        Point3DM[] POS;
        Rotation3DM Facing;
        
        String Name;
        
        ProjectileType Shot;
        
        int Ammo;
        
        Counter ReloadTimer;
        
        
        Counter CooldownCounter;
        public Gun copy(Object3DM base, Point3DM[] pos, Rotation3DM facing){
            Gun G = new Gun(Name, base, pos, facing, Shot, Ammo, ReloadTimer.Max, ReloadTimer.BaseIncrement);
            return G;
            
        }
        public Gun(String N, Object3DM base, Point3DM[] pos, Rotation3DM facing, ProjectileType shot, int ammo, double maxr, double rr){
           Name = N;
           Base = base;
           POS = pos;
           Facing = facing;
           Shot = shot;
           Ammo = ammo;
           ReloadTimer = new Counter(0, maxr, 0);
           ReloadTimer.setBaseIncrement(rr);
           CooldownCounter = new Counter(0, 0.05, 0.05);
        }
        public void reload(){
            ReloadTimer.increment(ReloadTimer.BaseIncrement * 1.0 / FramesPerSecond);
            ReloadTimer.cap();
            
            if(CooldownCounter.checkBounds() < 1){
                CooldownCounter.increment(1.0 / FramesPerSecond);
            }
            
        }
        public Projectile fire(Object3DM Target){
            if(Paused){
                return null;
            }
            if(CooldownCounter.Current < CooldownCounter.Max){
                return null;
            }
            if(ReloadTimer.Current < Shot.FireCost){
                return null;
            }
            Projectile NewShot = null;
            for(int i = 0; i < POS.length; i++){
                if(Ammo > 0 || Ammo == -1337){
                    if(Ammo > 0){
                        Ammo--;
                    }
                    
                    NewShot = Shot.createProjectile(Base, POS[i], Facing, Target);
                    playSound(Shot.FireSound, NewShot.Form.Center, 1);
                    Projectiles.add(NewShot);
                    Trails.add(NewShot.BulletTrail);
                    
                }
            }
            CooldownCounter.increment(-CooldownCounter.Max);
            ReloadTimer.increment(-Shot.FireCost);
            return NewShot;
        }
    }
    
    class ProjectileType extends Object3DM{
        //Object3D Stuff
        //Shape3DM Form;
        //Point3DM Momentum;
        //double CollisionRadius;
        
        float Duration;
        boolean Rotating;
        float FireCost;
        float Spread;
        float Damage;
        
        boolean Homes;
        float TurnPerSecond;
        float HomingDelay;
        
        float MaxEffectiveRange;
        float MinEffectiveRange;
        int AIPrefMod;
        
        int Fragments;
        
        float TrailTime;
        int Attachment;
        WaveSound FireSound;
        
        ObjectVector Store = new ObjectVector();

        public ProjectileType(double M, double CR, double FC, double D, boolean R, double S, double DMG, int Fragment){
            Duration = (float)D;
            Momentum = new Point3DM(M, 0, 0);
            CollisionRadius = CR;
            Rotating = R;
            FireCost = (float)FC;
            Spread = (float)S;
            Damage = (float)DMG;
            Fragments = Fragment;
            
        }
        public void setTrail(double TT, int Hook){
            TrailTime = (float)TT;
            Attachment = Hook;
        }
        
        public void setAIData(double MER, double MER2, int AIPreference){
            MaxEffectiveRange = (float)MER;
            MinEffectiveRange = (float)MER2;
            AIPrefMod = AIPreference;
        }
        public void setHoming(double TPS, double SD){
            TurnPerSecond = (float)TPS;
            Homes = true;
            HomingDelay = (float)SD; 
        }
        public void setShape(Shape3DM F){
            Form = F.copy();
        }
        public Projectile createProjectile(Object3DM Base, Point3DM Position, Rotation3DM Facing, Object3DM Target){
           // if(Fragments == 1)System.out.println(Store.size());
            Point3DM Direction = new Point3DM(0, 0, 0);
            Facing.getOriginalAxis('x', Direction);
            Projectile P;
            if(Store.size() == 0){
                
                P = new Projectile(Base, Form.copy(), Position, Direction.multiply(Momentum.POSX).add(Base.Momentum), Duration, Form.Tint);
                
                P.BulletTrail = new Trail(P, (P.Homing ? 8 : 3), P.Form.Center, P.Form.Tint, TrailTime);
                P.Damage = Damage;
                P.Homing = Homes;
                
                P.MaxTurn = TurnPerSecond;
                P.Fragments = Fragments;
                P.HomingDelay = new Counter(0, HomingDelay, HomingDelay);
                P.Turn = new Rotation3DM();
                P.Type = this;
            }else{
                P = (Projectile)Store.lastElement();
                
                
                Store.removeLastElement();
                P.Base = Base;
                Position.copyTo(P.Form.Center);
                Direction.multiply(Momentum.POSX, P.Momentum);
                P.Momentum.add(Base.Momentum, P.Momentum);
                P.Form.Center.copyTo(P.LastPosition);
                P.Duration.Current = Duration;
                P.HomingDelay.Current = P.HomingDelay.Max;
                
                P.BulletTrail.reset();
            }
            
            Direction = Utils3DM.getRandomUnit();
            Direction.multiply(Spread * Momentum.POSX, Direction);
            
            P.Momentum.add(Direction, P.Momentum);
            Facing.copyTo(P.Form.Rotation);
            //Rotation3D.multiply(Facing, P.Form.Rotation, P.Form.Rotation);
            P.Form.calcPoints();
            P.Target = Target;
            //System.out.println(P.BulletTrail);
            /*if((ShortTrailStore.size() == 0 && !P.Homing) || (LongTrailStore.size() == 0 && P.Homing)){             
                P.BulletTrail = new Trail(P, (P.Homing ? 8 : 3), (Attachment == -1 ? P.Form.Center : P.Form.ActualPoints[Attachment]), P.Form.Tint, TrailTime);
            }else{
                ObjectVector TrailStore = (P.Homing ? LongTrailStore : ShortTrailStore);
                P.BulletTrail = P.Homing ? (Trail)TrailStore.lastElement() : (Trail)TrailStore.lastElement();
                TrailStore.removeLastElement();

                P.BulletTrail.Target = (Attachment == -1 ? P.Form.Center : P.Form.ActualPoints[Attachment]);
                P.BulletTrail.reset();
                P.BulletTrail.TrailCounter.Current = TrailTime / P.BulletTrail.TrailLength; P.BulletTrail.TrailCounter.Max = TrailTime / P.BulletTrail.TrailLength;
                
            }*/
            //P.BulletTrail.Form.MaxDistance = 100;
            
           
           // System.out.println("NEW TRAIL " + P.BulletTrail.Active + " " + P.BulletTrail.Form.MaxDistance);
            return P;
            
        }
    }
    class Projectile  extends Object3DM{
        //Object3D Stuff
        //Shape3DM Form;
        //Point3DM Momentum;
        //double CollisionRadius;
        
        Trail BulletTrail;
        Point3DM LastPosition;
        float Damage = 0;
        float Speed;
        
        Counter Duration;
        boolean Homing;
        float MaxTurn;
        Counter HomingDelay;
        Rotation3DM Turn;
        
        
        int Fragments;
        
        Object3DM Target;
        
        Object Base;
        ProjectileType Type;
        
        public Projectile(Object B, Shape3DM form, Point3DM Center, Point3DM V, double D, Color T){
            Base = B;
            
            if(form == null){Form = new Shape3DM(false, Color.white, Center.POSX, Center.POSY, Center.POSZ);
            }else{Form = form; Form.Center = Center.copy();}
            
            LastPosition = new Point3DM(Center.POSX, Center.POSY, Center.POSZ);
            
            Form.Tint = T;
            
            Momentum = V.copy();
            Speed = (float)Momentum.length();
            Duration = new Counter(0, D, D);
        
            CollisionRadius = 4; //Affects Craft Collision but not Plane
            
        }
        public void setForm(char Switch, double ArgA, double ArgB, double ArgC){
            Utils3DM.setPoints(Form, Switch, ArgA, ArgB, ArgC);
            if(Switch == 'p'){
                Form.Rotation.rotateAround('y', Math.toRadians(-90));
            }
        }
        public void turnProjectile(){
          //  System.out.println(Momentum);
            
            
            
            
            
            
            
            Point3DM Direction = judgeDirection();
            
           // if(Base == Player)System.out.println(projectileJudgeLead().length());
            Rotation3DM.getRotationBetween(Momentum, Direction, MaxTurn * 1.0 / FramesPerSecond, Turn);
            Rotation3DM.multiply(Turn, Form.Rotation, Form.Rotation);
            
            Form.Rotation.getOriginalAxis('x', Momentum);
            Momentum.multiply(Speed, Momentum);
           
        }
        public Point3DM judgeDirection(){

            Point3DM V = Target.Momentum;
            Point3DM A = Utils3DM.Origin;
            Point3DM D = Target.Form.Center.subtract(Form.Center);
            
            double P = Speed;
            double T = GUI.newtonRaphson(0.25 * A.dot(A), 0.5 * V.dot(A), V.dot(V) - P * P + 0.5 * D.dot(A), D.dot(V), D.dot(D), Form.Center.distanceTo(Target.Form.Center) / Speed, 5, 0.01);
            if(T == T){
                Point3DM PCap = D.add(V.multiply(T)).add(A.multiply(0.5 * T*T)).divide(Speed * T);

                
                return PCap;
            }else{
                return Utils3DM.Origin;
            }
            
            
        }
        public void handleSound(){

            if(HomingDelay.checkBounds() < 0 && HomingDelay.Current + 1.0 / FramesPerSecond  > 0){
                if(Base == Player || Target == Player){
                    playSound("WeaponHoming - Missile");
                }
            }
        }
        public void move(){
            
           
            
           // if(HomingDelay.checkBounds() >= 0 && Homing){
            HomingDelay.increment(-1.0 / FramesPerSecond);
                
            //}
            if(Homing && Target != null && HomingDelay.checkBounds() < 0){
                turnProjectile();
            }
            Form.Center.copyTo(LastPosition);
            Form.Center.add(Momentum.divide(FramesPerSecond), Form.Center);
            handleSound();
            
       //    System.out.println(this + "\t" + Form.Center);
            
      //   System.out.println(Form.Center + "\t" + Duration);
            Duration.increment(-1.0 / FramesPerSecond);
            if(Duration.checkBounds() < 0){
                die();
            }
        }
        public Point3DM checkCollision(Plane Target){
            Point3DM A = (Form.Center);
            Point3DM B = (LastPosition);
            
            return Target.checkCollision(A, B);
            
            
           // return null;
        }
        public void doCollision(Plane Target, Point3DM ImpactSpot){
            die();
            System.out.println("F " + Fragments);
         //   System.out.println("BOOM PLANESHOT");
          //  Explosions.add(new TempSprite(new Sprite3D(Form, ImpactSpot, Tint), 1, Utils3DM.Origin));
            for(int i = 0; i < Fragments; i++){
                Doodad Debris = createDebris(GUI.randomInt(1, 3) + i, ImpactSpot, Momentum.length() * DebrisSpeedMultiplier, (Math.random() > 0.5 ? this.Form.Tint : Target.Form.Tint));
                
                Doodads.add(Debris);
            }
            //Momentum = Utils3DM.Origin;
        }
        public Point3DM checkCollision(Object3DM Target){
            if(Target == Base || !Exists){
                return null;
            }
            if(Form.Center.distanceTo(Target.Form.Center) > CollisionRadius + Target.CollisionRadius + Speed * 1.0 / FramesPerSecond){
                return null;
            }
            Point3DM Impact = Utils3DM.lineSphereCollision(Form.Center, LastPosition, Target.Form.Center, Target.CollisionRadius + CollisionRadius);
            
            return Impact;
        }
        public void die(){
            if(this.Exists && !Finished){
                playSound("VehicleHitExplosion", Form.Center, Damage);
                Duration.setTo(0);
                
                Projectiles.remove(this);
            }
        }
        public void doCollision(Vehicle Target, Point3DM ImpactSpot){
            
            die();
            for(int i = 0; i < Fragments; i++){
                Doodad Debris = createDebris(GUI.randomInt(1, 3) + i, ImpactSpot, Math.random() * (Target.Momentum.subtract(Momentum)).length() * DebrisSpeedMultiplier, (Math.random() > 0.5 ? this.Form.Tint : Target.Form.Tint));
                Debris.Spin.rotateAround(GUI.getRandomAxis(), Math.PI * Math.random() * 3 * 0.02);
                Doodads.add(Debris);
            }
            
            
            Target.hit(ImpactSpot, Damage);
            
            //System.out.println(Impulse.length());
            
            //Momentum = Utils3DM.Origin;
            
         //   Explosions.add(new TempSprite(new Sprite3D(Form, ImpactSpot, Tint), 1, Utils3DM.Origin));
            
            //System.out.println("BOOM HEADSHOT");
        }
        public void print(ObserverM View){
            Form.print(View);
        }
    }
    
    class TempSprite{
        Sprite3DM Form;
        
        Point3DM Momentum;
        Counter Duration;
        
        public TempSprite(Sprite3DM F, double D, Point3DM M){
            Form = F;
            Duration = new Counter(0, D, D);
            Momentum = M.copy();

        }
        public void move(){
            Form.Center.add(Momentum.divide(FramesPerSecond), Form.Center);
            Duration.increment(-1.0 / FramesPerSecond);
        }
        public void print(ObserverM Viewer){
            
            Form.print(Viewer);
        }
    }
    
    class Doodad extends Object3DM{
        //Object3D Stuff
        //Shape3DM Form;
        //Point3DM Momentum;
        //double CollisionRadius;
        
        Counter Duration;
        float Gravity;
        
        
        float BounceFactor = 1.0f;
        Rotation3DM Spin = new Rotation3DM(); //Spin per 20 milliseconds
        Counter SpinCounter = new Counter(0, 20, 20);

        public Doodad(double G, Shape3DM S, double Dur, Point3DM Pos, Point3DM Mom, Color T, double Size){
            Gravity = (float)G;
            Form = S.copy();
            CollisionRadius = Size;
            Duration = new Counter(0, Dur, Dur);
            Momentum = Mom.copy();
            
            Form.Tint = T;
        }
        public Doodad(double G, char F, double Dur, Point3DM Pos, Point3DM Mom, Color T, double Size){
            Gravity = (float)G;
            Form = new Shape3DM(false, Color.white, Pos.POSX, Pos.POSY, Pos.POSZ);
            
            if(F == 'r'){
                switch(GUI.randomInt(1, 3)){
                    case 1: F = 't'; break;
                    case 2: F = 'p'; break;
                    case 3: F = 'c'; break;
                }
            }
            switch(F){
                case 't':
                    Utils3DM.setPoints(Form, 't', 1 * Size, 1 * Size, 1 * Size);
                    break;
                case 'p':
                    Utils3DM.setPoints(Form, 'p', 1 * Size, 1, 0);
                    break;
                case 'c':
                    Utils3DM.setPoints(Form, 'c', 1 * Size, 1 * Size, 1 * Size);
                break;
            }
            CollisionRadius = Size;
            Duration = new Counter(0, Dur, Dur);
            Momentum = Mom.copy();
            
            Form.Tint = T;
        }
        public void move(){
        
            Momentum.add(Utils3DM.Vectork.multiply(-10.0 / FramesPerSecond * Gravity), Momentum);
             //   System.out.println(Momentum);
            
            SpinCounter.increment(-1000f / FramesPerSecond);
            while(SpinCounter.checkBounds() < 0){
                Rotation3DM.multiply(Form.Rotation, Spin, Form.Rotation);
                SpinCounter.increment(20);
                
                
            }
            Form.Center.add(Momentum.divide(FramesPerSecond), Form.Center);
            Duration.increment(-1.0 / FramesPerSecond);
            
        }
       
        public void print(ObserverM Viewer){
            Form.print(Viewer);
        }
    }
    
    /*\
     *f-16
     *Dimensions 14.5m X 9.5m X 5m
     *Weight 8000kg-12000kg (10000kg average)
     *Max Speed 2150km/h = 600m/s
     *Thrust 24000lb = 110000N 
     *Acceleration 110000N / 10000kg = 11m/s/s
     *Rate of Climb 15420M/minute = 250m/s
     *Cruising speed: 577 m/h = 256 m/s
    */
    
    abstract class Vehicle extends Object3DM{
        boolean UseAI = true;
        Counter Hitpoints;
        int AILevel = -1;
        

        Gun SelectedGun;
        Object3DM DecisionTarget;
        final Point3DM ControlTarget = new Point3DM();
        final Point3DM LeadTarget = new Point3DM();
        final Point3DM MissBox = new Point3DM();
        double MissBoxSize;
        Projectile TrackedShot = null;
        Counter Fudge = new Counter(-0.75, 0.75, 0, 0.1);
        float FudgeDecayMod = 0.015f;
        boolean Firing;
        public void AILead(){
            if(SelectedGun == null){
                LeadTarget.copyFrom(DecisionTarget.Form.Center);
                Firing = false;
                return;
            }
            
            
            double ProjectileSpeed = SelectedGun.Shot.Momentum.POSX;
            Point3DM Temp = judgeShot(DecisionTarget, ProjectileSpeed);
            if(Temp == null){
                Firing = false;
                return;
            }else{
                Temp.copyTo(LeadTarget);
            }
            LeadTarget.multiply(1 + Fudge.Current, LeadTarget);
            LeadTarget.add(DecisionTarget.Form.Center, LeadTarget);
            
            updateMissBox();
            if(DecisionTarget.Momentum.length() == 0){
                LeadTarget.add(MissBox.divide(10), LeadTarget);
            }else{
                LeadTarget.add(MissBox, LeadTarget);
            }
        }
        
        public void updateMissBox(){
            Point3DM New = Utils3DM.getRandomUnit();
            New.multiply(MissBoxSize / 10 / FramesPerSecond, New);
            MissBox.add(New, MissBox);
            if(MissBox.length() > MissBoxSize){
                MissBox.normalize();
                MissBox.multiply(MissBoxSize, MissBox);
            }
        }
        public void setName(String N){
            if(N == null){
                Name = getGreek();
                if(this instanceof Craft){
                    Name = "Craft " + Name;
                }else if(this instanceof Turret){
                    Name = "Turret " + Name;
                }
                Name = getFaction(Side) + " " + Name;
            }else{
                Name = N;
                
            }
            
            
        }
        public void AITrackShots(){
            
            if(TrackedShot == null){return;}
            if(TrackedShot.Duration.Current <= 0 || DecisionTarget.Form.Center.subtract(TrackedShot.Form.Center).dotProduct(TrackedShot.Momentum) < 0){
                Fudge.increment(-GUI.sign(Fudge.Current) * FudgeDecayMod * 1.0 / FramesPerSecond);
                if(DecisionTarget.Momentum.dotProduct(TrackedShot.Form.Center.subtract(DecisionTarget.Form.Center)) < 0){
                    //shot was behind
                    Fudge.increment(Fudge.BaseIncrement);
                }else{
                    //shot was in front
                    Fudge.increment(-Fudge.BaseIncrement);
                }
                Fudge.cap();
//                AIHandleTrackedShot(TrackedShot);
                TrackedShot = null;
            }
            
        }
        public Point3DM judgeShot(Object3DM Target, double ProjectileSpeed){
            if(ProjectileSpeed == 0 || Target == this){
                return Utils3DM.Origin;
            }
            Point3DM V = Target.Momentum.subtract(Momentum);
            Point3DM A = Target.Acceleration;
            Point3DM D = Target.Form.Center.subtract(Form.Center);
            
            double P = ProjectileSpeed;
            double T = GUI.newtonRaphson(0.25 * A.dot(A), 0.5 * V.dot(A), V.dot(V) - P * P + 0.5 * D.dot(A), D.dot(V), D.dot(D), Form.Center.distanceTo(Target.Form.Center) / ProjectileSpeed, 5, 0.01);
            if(T == T){
                Point3DM PCap = D.add(V.multiply(T)).add(A.multiply(0.5 * T*T)).divide(ProjectileSpeed * T);

                PCap.multiply(Form.Center.distanceTo(Target.Form.Center), PCap);
                PCap.add(Form.Center, PCap);
                PCap.subtract(Target.Form.Center, PCap);
                return PCap;
            }else{
                return null;
            }

        }
        
        abstract public void print(ObserverM Viewer);
        abstract public void printNow(Graphics2D Painter, ObserverM Viewer);
        abstract public void printNow(Graphics2D Painter, ObserverM Viewer, Color Tint);
        abstract public String getTask();
        abstract public void hit(Point3DM ImpactSpot, float Damage);
        abstract public void move();
        abstract public void initAI(int Level);
        abstract public void doAI();
        abstract public void die();
    }
    class Structure extends Turret{
        //Object3DM Stuff
        //Shape3DM Form;
        //Point3DM Momentum;
        //double CollisionRadius;
        //Point3DM Acceleration;
        //int Side
        //boolean Exists
        
        //Vehicle Stuff
        //boolean UseAI
        //Counter Hitpoints
        //Point3DM[] TempPoints = new Point3DM[6];
        //Rotation3D[] TempRotations = new Rotation3D[6];
        

       
        
        public Structure(String N, Point3DM P, int S, int HP, int M){
            super(N, P, S, 0, null);
            Hitpoints = new Counter(0, HP, HP);
            makeShape(0, false, M);
            GunForm.Visible = false;
            Form.Center.add(P, Form.Center);
            GunForm.Center.add(P, GunForm.Center);
            TurretForm.Center.add(P, TurretForm.Center);
            Flag.Center.add(P, Flag.Center);
           // System.out.println(Flag.Tint);
            Flag.Tint = getColor(Side);
        }
        
      
        public String getTask(){
            return "";
        }
        public void move(){}
        public void initAI(int A){}
        public void doAI(){}
        public void print(ObserverM Viewer){
            TurretForm.calcPoints();
            Form.calcPoints();
            GunForm.calcPoints();
            Flag.calcPoints();
            Flag.print(Viewer);
            Form.print(Viewer);
            TurretForm.print(Viewer);
            GunForm.print(Viewer);    
        }

        public void printNow(Graphics2D Painter, ObserverM Viewer){
            TurretForm.calcPoints();
            Form.calcPoints();
            GunForm.calcPoints();
            Flag.calcPoints();
            Flag.printNow(Painter, Viewer);
            Form.printNow(Painter, Viewer);
            TurretForm.printNow(Painter, Viewer);
            GunForm.printNow(Painter, Viewer);    
        }
        public void printNow(Graphics2D Painter, ObserverM Viewer, Color Tint){
            TurretForm.calcPoints();
            Form.calcPoints();
            GunForm.calcPoints();
            Flag.calcPoints();
            Flag.printNow(Painter, Viewer, Tint);
            Form.printNow(Painter, Viewer, Tint);
            TurretForm.printNow(Painter, Viewer, Tint);
            GunForm.printNow(Painter, Viewer, Tint);    
        }
        
    }
    class Turret extends Vehicle{
        //Object3DM Stuff
        //Shape3DM Form;
        //Point3DM Momentum;
        //double CollisionRadius;
        //Point3DM Acceleration;
        
        //Vehicle Stuff
        //int Side
        //boolean Exists
        //boolean UseAI
        //Counter Hitpoints
        
        Shape3DM TurretForm; //model of turret
        Shape3DM GunForm; //model of barrels
        Shape3DM Flag; //model of base
        Rotation3DM Orientation = new Rotation3DM();
        GeneralVector Guns = new GeneralVector();
        
        double TurnSpeed = Math.PI / 3;
        //float HalfHeight = 4;
        float Theta = 0;
        float Alpha = (float)Math.toRadians(45);
        
        float MinAlpha = (float)Math.toRadians(25);
        float MaxAlpha = (float)Math.toRadians(65);
        
        float RangeMultiple = 1.5f;
        
        
        
        
        
        
        float FireChance = 0.1f;
        float AngleToFire = (float)Math.toRadians(5);
        Counter StunCounter = new Counter(0, 1.0, 0);
        float StunChance = 0.1f;
        float Range = 1000;
        

        
        public Turret(String N, Point3DM Position, int S, int GunCount, Gun Weapon){
            Side = S;
            setName(N);
            

            Momentum = new Point3DM(0, 0, 0);
            Acceleration = new Point3DM(0, 0, 0);
            
            CollisionRadius = 4;
            DynamicCollision = false;
            Hitpoints = new Counter(0, 15, 15);
            
            makeShape(GunCount, Weapon == MissileLauncher);
            
            initWeapons(GunCount, Weapon);
            Form.Center.add(Position, Form.Center);
            GunForm.Center.add(Position, GunForm.Center);
            TurretForm.Center.add(Position, TurretForm.Center);
            Flag.Center.add(Position, Flag.Center);
            
            if(Guns.size() > 0){
                SelectedGun = Guns.gun(0);
                Range = (float)(Guns.gun(0).Shot.Momentum.POSX * Guns.gun(0).Shot.Duration * RangeMultiple);
            }
            Flag.Tint = getColor(Side);
            
        }
        public void setBounds(float Min, float Max){
            MinAlpha = (float)Math.toRadians(Min);
            MaxAlpha = (float)Math.toRadians(Max);
        }
        public void initAI(int Level){//Initializes the AI
            AILevel = Level;
            Fudge = new Counter(-0.25 + 0.05 * Level, 0.25 * 0.05 * Level, 0);

            AngleToFire = (float)Math.toRadians(6 - Level / 2.0);
            StunCounter.Max = 1.75 - 0.15 * Level;
            MissBoxSize = 135 /(Math.max(0, Level) +1);
            FireChance = 0.05f + (0.01f) * (Level);
            StunChance = 0.1f - (0.01f) * (Level);
        }
        public void orient(Point3DM N){
            Rotation3DM.getRotationBetween(Utils3DM.Vectork, N, 100, Orientation);
            
            
            
            Orientation.copyTo(TurretForm.Rotation);
            Orientation.copyTo(GunForm.Rotation);
            Orientation.copyTo(Form.Rotation);
            Point3DM Difference = new Point3DM(0, 0, 0);
            TurretForm.Center.subtract(Form.Center, Difference);
            Orientation.transform(Difference, Difference);
            Form.Center.add(Difference, TurretForm.Center);
            
            GunForm.Center.subtract(Form.Center, Difference);
            Orientation.transform(Difference, Difference);
            Form.Center.add(Difference, GunForm.Center);
        }
        
        public void initWeapons(int GunCount, Gun Weapon){
            for(int i = 0; i < 2*GunCount; i++){
                Gun G = Weapon.copy(this, new Point3DM[]{GunForm.ActualPoints[6*i+0]}, GunForm.Rotation);
              //  G.CooldownCounter.Max *= 2; G.ReloadTimer.BaseIncrement /= 2;
                Guns.add(G);
                G.ReloadTimer.Current = G.ReloadTimer.Max;
            }
        }
        public void makeShape(int GC, boolean S){
            makeShape(GC, S, 1);
        }
        public void makeShape(int GunCount, boolean Symmetry, int M){
            CollisionRadius *= M;
            float HalfHeight = 4 * M;
            Form = new Shape3DM();
            TurretForm = new Shape3DM();
            GunForm = new Shape3DM();
            Form.Static = false;
            TurretForm.Static = false;
            Utils3DM.setPoints(Form, 'c', 1.5 * HalfHeight, 1.5 * HalfHeight, HalfHeight);
            Utils3DM.setPoints(TurretForm, 'c', 0.9 * HalfHeight, 0.75 * HalfHeight, 0.9 * HalfHeight);

            float GunLength = 9 * M;
           
            Form.Center.POSZ += HalfHeight;
            Point3DM Normal = new Point3DM(0, 0, 0);
            Point3DM Forward = new Point3DM(0, 0, 0);
 
            TurretForm.Center.add(Utils3DM.Vectork.multiply(HalfHeight *1.45), TurretForm.Center);
            for(int i = 0; i < Form.Points.length; i++){
                Form.Points[i].POSZ -= HalfHeight/2;
                
            }
            
            for(int i = 0; i < GunCount; i++){
                Utils3DM.setPoints(GunForm, 'r', 3, GunLength / 25, GunLength);
                for(int j = GunForm.BasePoints.size() - 6; j < GunForm.BasePoints.size(); j++){
                    GunForm.BasePoints.get(j).POSX += i;
                    GunForm.BasePoints.get(j).POSY += HalfHeight / 2;
                }
                Utils3DM.setPoints(GunForm, 'r', 3, GunLength / 25, 9);
                for(int j = GunForm.BasePoints.size() - 6; j < GunForm.BasePoints.size(); j++){
                    GunForm.BasePoints.get(j).POSX += i;
                    GunForm.BasePoints.get(j).POSY -= HalfHeight / 2;
                }
            }
 
            Rotation3DM R = new Rotation3DM();
            R.rotateAround('y', Math.PI/2);
            if(GunCount == 0){
                GunForm.Points = new Point3DMShape[0];
            }
            for(int i = 0; i < GunForm.Points.length; i++){
                R.transform(GunForm.Points[i], GunForm.Points[i]);
            }
            
            Utils3DM.center(GunForm, 'p');
            GunForm.Center = Form.Center.copy();
            GunForm.Center.POSZ += HalfHeight * 0.5;
            Utils3DM.centerStatic(GunForm, 'p');
            Utils3DM.centerStatic(TurretForm, 'p');
      
            if(!Symmetry){
                for(int i = 0; i < GunForm.Points.length; i++){
                    GunForm.Points[i].POSX += GunLength / 4;
                }
            }
            
            Flag = new Shape3DM();
            Utils3DM.setPoints(Flag, 'l', 1, 0, 0);
            Utils3DM.setPoints(Flag, 'l', 1, 0, 0);
            Utils3DM.setPoints(Flag, 'l', 1, 0, 0);
            Utils3DM.setPoints(Flag, 'l', 1, 0, 0);
            
            HalfHeight /= Math.sqrt(M);
            Flag.Points[0].setLocation(-HalfHeight*3, -HalfHeight*3 , 0);
            Flag.Points[1].setLocation(HalfHeight*3, HalfHeight*3, 0);
            
            Flag.Points[2].setLocation(-HalfHeight*3, HalfHeight*3, 0);
            Flag.Points[3].setLocation(HalfHeight*3, -HalfHeight*3, 0);
            
            Flag.Points[4].setLocation(0, -HalfHeight*3*Math.sqrt(2), 0);
            Flag.Points[5].setLocation(0, HalfHeight*3*Math.sqrt(2), 0);
            
            Flag.Points[6].setLocation(-HalfHeight*3*Math.sqrt(2), 0, 0);
            Flag.Points[7].setLocation(HalfHeight*3*Math.sqrt(2), 0, 0);
            
            HalfHeight *= Math.sqrt(M);
            Utils3DM.center(Flag, 'p');
            
            Utils3DM.center(TurretForm, 'p');
            Form.Center.copyTo(Flag.Center);
            Flag.Center.POSZ -= HalfHeight;
            
        }
        public void move(){//Does the movement of the turret
            handleAngles();
            for(int i = 0; i < Guns.size(); i++){
                Guns.gun(i).reload();
                
            }
           	
            Utils3DM.Origin.copyTo(Momentum);
        }
        public void handleAngles(){
            
            TurretForm.Rotation.identify();
            TurretForm.Rotation.rotateAround('z', Theta);
            Rotation3DM.multiply(Form.Rotation, TurretForm.Rotation, TurretForm.Rotation);   
            
            GunForm.Rotation.identify();
            GunForm.Rotation.rotateAround('z', Theta);
            Rotation3DM R = new Rotation3DM();
            R.rotateAround('y', Alpha);
            Rotation3DM.multiply(GunForm.Rotation, R, GunForm.Rotation);   
            Rotation3DM.multiply(Form.Rotation, GunForm.Rotation, GunForm.Rotation);   
            
            
            
            if(Alpha > 0){
                Alpha = (float)GUI.cap(MinAlpha, Alpha, MaxAlpha);
            }else{
                Alpha = (float)GUI.cap(-MaxAlpha, Alpha, -MinAlpha);
            }
        }
        
        public void doAI(){//Uses the AI
            if(Guns.size() == 0 || AILevel < 0 ){ return; }
            AITrackShots();
            AICountdowns();
            AITargetting();
            if(DecisionTarget != null){    
                AILead();
                LeadTarget.copyTo(ControlTarget);
                AIControl();
                AIFire();
            }
        }
        public float getAngleError(){
            Point3DM Difference = new Point3DM(0, 0, 0);
            ControlTarget.subtract(GunForm.Center, Difference);
            Difference.normalize();
            Point3DM Direction = new Point3DM(0, 0, 0);
            GunForm.Rotation.getOriginalAxis('x', Direction);
            return (float)Math.acos(GUI.trigCap(Difference.dotProduct(Direction)));
        }
        public void AICountdowns(){
            if(StunCounter.Current != 0){
                StunCounter.Current -= 1.0/FramesPerSecond;
                StunCounter.cap();
            }
            
        }
        public void AIFire(){
            if(Firing && StunCounter.checkBounds() < 0 && !mightIntersectPlane(Form.Center, ControlTarget)){
                
                if(getAngleError() < AngleToFire){
                    for(int i = 0; i < Guns.size(); i++){
                        if(Math.random() < FireChance){
                            Projectile P = Guns.gun(i).fire(DecisionTarget);
                            if(P != null){
                            	if(Math.random() < StunChance){
                            		StunCounter.Current = StunCounter.Max;	
                            	}
                                P.Duration.Current *= RangeMultiple;
                                P.Duration.Max *= RangeMultiple;
                            }
                        }
                    }
                }
            }
            int Max = 0;
            int Current = 0;
            for(int i = 0; i < Guns.size(); i++){
                Max += Guns.gun(i).ReloadTimer.Max;
                Current += Guns.gun(i).ReloadTimer.Current;
            }
            float Frac = Current * 1.0f / Max;
            if(Frac > 0.8 && !Firing){
                Firing = true;
            }else if(Frac < 0.2 && Firing){
                Firing = false;
            }else{
                Firing = Firing;
            }
            
        }
        public void AITargetting(){
            Range = (float)(Guns.gun(0).Shot.Momentum.POSX * Guns.gun(0).Shot.Duration * RangeMultiple);
            float MinDistance = Range;
            Vehicle ClosestTarget = null;
            for(int i = 0; i < Vehicles.size(); i++){
                if(Vehicles.ve(i).Side != Side){
                    if(MinDistance > Vehicles.ve(i).Form.Center.distanceTo(Form.Center)){
                        ClosestTarget = Vehicles.ve(i);
                        MinDistance = (float)Vehicles.ve(i).Form.Center.distanceTo(Form.Center);
                    }
                }
            }
            DecisionTarget = ClosestTarget;
        }
        
       
        
        public void AIControl(){
            if(getAngleError() < AngleToFire / 4){
                return;
            }
            final Point3DM vX = new Point3DM();
            final Point3DM vY = new Point3DM();
            final Point3DM vZ = new Point3DM();

            Orientation.getOriginalAxis('x', vX);
            Orientation.getOriginalAxis('y', vY);
            Orientation.getOriginalAxis('z', vZ);

            Point3DM Difference = ControlTarget.subtract(Form.Center);

            double TargetTheta = Math.atan2(vY.dotProduct(Difference), vX.dotProduct(Difference));
            double TargetAlpha = Math.PI/2 - Math.acos(vZ.dotProduct(Difference) /  Difference.length());
            double dTheta = GUI.angleDiffR(Theta, TargetTheta);  
            double dAlpha = GUI.angleDiffR(Alpha, TargetAlpha);
            double dS = TurnSpeed / FramesPerSecond;
            if(Math.abs(dTheta) > dS){
                dTheta *= dS / Math.abs(dTheta);
            }
            if(Math.abs(dAlpha) > dS){
                dAlpha *= dS / Math.abs(dAlpha);
            }

            if(dTheta != 0){
                Theta += dTheta;
            }
            if(dAlpha != 0){
                Alpha += dAlpha;
            }
            if(Alpha > 0){
                Alpha = (float)GUI.cap(MinAlpha, Alpha, MaxAlpha);
            }else{
                Alpha = (float)GUI.cap(-MaxAlpha, Alpha, -MinAlpha);
            }
          
        }   
        public void die(){//Destroys craft and generates fragments
            if(Finished)return;
            playSound("VehicleDieExplosion", Form.Center, 1);
            TickerTape.addLine(Name + " Destroyed.");
            Vehicles.remove(this);
            
            Doodad[] Fragment = new Doodad[8];
            //Big
            Fragment[0] = new Doodad(5, Form, GUI.randomInt(1, 2), Form.Center, Utils3DM.getRandomUnit().multiply(GUI.randomInt(120, 220)).add(Momentum), Color.white, GUI.randomInt(9, 12));
            Fragment[1] = new Doodad(6, TurretForm, GUI.randomInt(2, 4), Form.Center, Utils3DM.getRandomUnit().multiply(GUI.randomInt(120, 220)).add(Momentum), Color.white, GUI.randomInt(8, 10));
            
            
            //Various
            //public Doodad createDebris(int Duration, Point3DM Position, double Momentum, Color Tint){
            
            Fragment[2] = createDebris(11, Form.Center.add(Utils3DM.getRandomUnit()), 50, Color.white);
            Fragment[3] = createDebris(7, Form.Center.add(Utils3DM.getRandomUnit()), 90, Color.red);
            
            
            Fragment[4] = createDebris(7, Form.Center.add(Utils3DM.getRandomUnit()), 190, Color.white);
            Fragment[5] = createDebris(10, Form.Center.add(Utils3DM.getRandomUnit()), 210, Color.red);
            
            Fragment[6] = createDebris(3, Form.Center.add(Utils3DM.getRandomUnit()), 240, Color.white);
            Fragment[7] = createDebris(2, Form.Center.add(Utils3DM.getRandomUnit()), 270, Color.red);
            
            for(int i = 0; i < Fragment.length; i++){
            Doodads.add(Fragment[i]);
            }
        }
        public String getTask(){
            
            if(DecisionTarget == null){
                return "Idle";
            }else{
                return "Attacking " + DecisionTarget.Name;
            }
        }
        public void print(ObserverM Viewer){
            TurretForm.calcPoints();
            Form.calcPoints();
            GunForm.calcPoints();
            Flag.calcPoints();
            Flag.print(Viewer);
            Form.print(Viewer);
            TurretForm.print(Viewer);
            GunForm.print(Viewer);    
        }

        public void printNow(Graphics2D Painter, ObserverM Viewer){
            TurretForm.calcPoints();
            Form.calcPoints();
            GunForm.calcPoints();
            Flag.calcPoints();
            Flag.printNow(Painter, Viewer);
            Form.printNow(Painter, Viewer);
            TurretForm.printNow(Painter, Viewer);
            GunForm.printNow(Painter, Viewer);    
        }
        public void printNow(Graphics2D Painter, ObserverM Viewer, Color Tint){
            TurretForm.calcPoints();
            Form.calcPoints();
            GunForm.calcPoints();
            Flag.calcPoints();
            Flag.printNow(Painter, Viewer, Tint);
            Form.printNow(Painter, Viewer, Tint);
            TurretForm.printNow(Painter, Viewer, Tint);
            GunForm.printNow(Painter, Viewer, Tint);    
        }
        public void hit(Point3DM ImpactSpot, float Damage){
            StunCounter.Current = StunCounter.Max;
            
            Hitpoints.increment(-Damage);
            if((Hitpoints.checkBounds()) < 0){
                die();
            }
        }
    }
    class Craft extends Vehicle{
        
        
        //Sound
        Counter LoopCounter = new Counter(0, 0, 0.29);
        
        
        
        //Extra Graphics
        Trail[] Trails;
        
        Point3DM[] AfterburnerPoint;
        
  //      Shape3DM RightWingTrail;
     //   Shape3DM LeftWingTrail;
        
        
        //Data Constants
        int Mass = 10000; //Kilograms
        int Thrust = 90000; //Newton
        float WingArea = 28; //Square Meters
        float Length = 15f; //Meters
        float WingSpan = 9.8f; //Meters
        float MaxCL = 1.6f; //Maximum Coefficient of Lift
        float InducedDragE = 0.9084f; //Divisor fudge factor for induced drag
        float CoefficientOfParasiticDrag = 0.0208f; //Coefficient of drag
        
        
        float AspectRatio = WingSpan * WingSpan / WingArea;
        
        
        float AfterburnerBonus = 0.6f; //Fraction of Thrust
        float Gravity = 10; //Meters/Second/Second
        float MaxSpeed = 650; //Meters/Second
        float CruiseSpeed = 400; //Meters/Second
        
        //Controls
        
        float Throttle;
        float TargetThrottle;
        float ThrottleIncrement = 5;
        
        boolean Afterburner;
        
        boolean Telemetry = false;
        
        //Variables
        
        Counter AfterburnerTimer = new Counter(0, 10, 10);
        
        GeneralVector Guns = new GeneralVector();
        //Limits and Constants
        
        
        
        float HeightAboveGround = 4;
        float MaxElevatorControl = (float)Math.toRadians(25);
        float StandardSpeed = 240;
        
        
        float StallAngle = (float)Math.toRadians(20); //Angle at which CL starts dropping
        
        float StallSpeed = 0.8f; //Rate at which lift drops to 0 across AoA
        
        
        
        double RollSpeedMultiplier = Math.toRadians(Math.sqrt(90)); //Roll angle at ideal speed at sea level per second
        double GroundDragMultiplier = 5; //not used
        double TurnSpeedMultiplier = 0.75; //Multiplier to increase or decrease Alignment Speed
        
        int TrailLength = 8; //Length of trails in sections
        float FlightCeiling = 15000; //Maximum engine height
        Counter TrailCounter = new Counter(0, 0.5 / TrailLength, 0.5 / TrailLength);
        //Calculated Values
        
        double Speed; //Absolute value of momentum
        double Mach; //Speed in terms of the Speed of Sound
        double AngleOfAttack; //Angle between facing of craft and movement in crafts plane of symmetry
        double CoefficientOfLift; //Coefficient of the lift generated
        
        double CoefficientOfInducedDrag; //Coefficient of lift induced drag
        double Lift; //Final Lift value
        
        
        //Keyboard Controls
        boolean Forward;
        boolean Back;
        boolean Left;
        boolean Right;
        boolean RudderLeft;
        boolean RudderRight;
        
        //Mouse Controls
        double MouseVertical;
        double MouseHorizontal;
        double ElevatorControl = 0;
        
        
        //AI
        
        GeneralVector Objectives = new GeneralVector();
        GeneralVector ObjectiveNames = new GeneralVector();
        char State; //What AI is doing
        
        float IdealSpeed = 280f; //Ideal speed AI aims to achieve
        float OverSpeedDec = 0.1f; //Rate at which AI moves throttle to 0 or 10 across Speed
        float SightRange = 5000f;
        float IdealHeight = 1250f;
        float TakeOffSpeed = 125f;
        int CircleSide = 1;
        
        //Avoid Collision Data
        Vector<Point3DM> Path = null;
        float LateralCollisionMultiplier = 0.75f; //Radius to check around position
        float ForwardCollisionDistance = 75f; //Radius to check around from position to target
        float HeightFloor = 200; //Height below take-off is ordered.
        Object3DM CollisionBounds = new Position3DM(); //Test Object for ease of checking Lateral Collision
        
        
        
        
        //Attack Data
        char AState = 'a'; //Assuming AI is attacking, attack or back off
        float TurnDistance = 200; //Distance at which Craft breaks off attack and retreats
        float ResumeDistance = 1000; //Distance at which Craft resumes attack after retreating
        Counter RetreatTimer = new Counter(0, 10, 0); //Counter to prevent AI from retreating indefinately
        
        Counter AttackTimer = new Counter(0, 30, 0); //Counter to prevent AI from attacking indefinately
        

        
        //Firing Data
        float AngleToFire = (float)Math.toRadians(1); //Angle between sights and target which AI will fire
       

        //Evasion Data
        final Point3DM EvasionTarget = new Point3DM(0, 0, 0);
        final Counter EvasionTCounter = new Counter(-12, 4, 6);
        
        
        
        float DodgeImpactTime = 1.0f;
        float DodgeImpactRadius = 50.0f;
        boolean Evading = false;
        
        //Follow Data
        double CircleDistance;
        
        //Control Data
        
        final Point2D.Double AIMouseTarget = new Point2D.Double(0, 0);
        
        
        
        float MaxAngleTurn = 1.5f;
        float RollResponsiveness = 1;
        //double MaxRollSpeedAngle = 1.5;
        
        
        //Temp Calculation Points
        
        
        //Max Speeds
        //~600 9
        //~565 8
        //~530 7
        //~490 6
        //~450 5
        //~400 4
        //~345 3
        //~280 2
        //~200 1

        public void setOrder(Craft Target, char St){
            DecisionTarget = Target;
            State = St;
        }
        
        public void initAI(int Level){ //nub 0 -> 5 pro
            AILevel = Level;
            IdealSpeed = 250 + AILevel * 10;

            AngleToFire = (float)Math.toRadians(4.5 - AILevel / 2.0);
            Fudge = new Counter(-(AILevel+5) * 0.25, (AILevel+5) * 0.25, 0, 0.1);
            AttackTimer = new Counter(0, 20 + 2 * AILevel, 0);
            RetreatTimer = new Counter(0, 30 - 2 * AILevel, 0);
            ResumeDistance = 1500 - AILevel * 50;
            
            MissBoxSize = 90 / (Level + 1);
          
        }
        public String getTask(){
            if(this==Player)return "";
            if(ObjectiveNames.size() == 0){
                return "Idle";
            }else{
                return ""+ObjectiveNames.lastElement();
            }
        }
        public void die(){
            if(Finished)return;
            playSound("VehicleDieExplosion", Form.Center, 1);
            TickerTape.addLine(Name + " Destroyed.");
            Vehicles.remove(this);
            
       //    public Doodad(boolean G, Shape3DM S, double Dur, Point3DM Pos, Point3DM Mom, Color T, double Size){
            Doodad[] Fragment = new Doodad[10];
            
            //Big Fragments
            Fragment[0] = new Doodad(1, Form, GUI.randomInt(15, 20), Form.Center, Utils3DM.getRandomUnit().multiply(GUI.randomInt(0, 25)).add(Momentum), Form.Tint, CollisionRadius);
            Fragment[0].Form.Points[1].setToMidpoint(Fragment[0].Form.Points[1], Fragment[0].Form.Points[2]);
            
            Fragment[1] = new Doodad(1, Form, GUI.randomInt(15, 20), Form.Center, Utils3DM.getRandomUnit().multiply(GUI.randomInt(0, 25)).add(Momentum), Form.Tint, CollisionRadius);
            Fragment[1].Form.Points[2].setToMidpoint(Fragment[1].Form.Points[1], Fragment[1].Form.Points[2]);
            
            
            //Coloured Fragments
            Fragment[2] = createDebris(GUI.randomInt(3, 12), Form.Center.add(Utils3DM.getRandomUnit()), GUI.randomInt(30, 190), Color.red);
            Fragment[3] = createDebris(GUI.randomInt(3, 12), Form.Center.add(Utils3DM.getRandomUnit()), GUI.randomInt(30, 190), Color.red);
            
            
            Fragment[4] = createDebris(GUI.randomInt(3, 12), Form.Center.add(Utils3DM.getRandomUnit()), GUI.randomInt(30, 190), Color.red);
            Fragment[5] = createDebris(GUI.randomInt(3, 12), Form.Center.add(Utils3DM.getRandomUnit()), GUI.randomInt(30, 190), Color.red);
            //White Fragments
            Fragment[6] = createDebris(GUI.randomInt(3, 12), Form.Center.add(Utils3DM.getRandomUnit()), GUI.randomInt(30, 190), Color.white);
            Fragment[7] = createDebris(GUI.randomInt(3, 12), Form.Center.add(Utils3DM.getRandomUnit()), GUI.randomInt(30, 190), Color.white);
            
            Fragment[8] = createDebris(GUI.randomInt(3, 12), Form.Center.add(Utils3DM.getRandomUnit()), GUI.randomInt(30, 190), Color.white);
            Fragment[9] = createDebris(GUI.randomInt(3, 12), Form.Center.add(Utils3DM.getRandomUnit()), GUI.randomInt(30, 190), Color.white);
            
            for(int i = 0; i < Fragment.length; i++){
                Fragment[i].Spin.rotateAround(GUI.getRandomAxis(), Math.PI * Math.random() * 3 * 0.02);
                Doodads.add(Fragment[i]);
            }
        }
        public Craft(String N, Point3DM Position, int Side, int Guns, float[] Data){
            this(N, Position, Side, Guns);
            Mass = (int)Data[0];
            Thrust = (int)Data[1];
            WingArea = Data[2];
            //Length = Data[3];
            WingSpan = Data[4];
            MaxCL = Data[5];
            InducedDragE = Data[6];
            CoefficientOfParasiticDrag = Data[7];
            
            Form.Points[0].setLocation(Length, 0, 0);

            Form.Points[1].setLocation(0, WingSpan/2, 0);
            Form.Points[2].setLocation(0, -WingSpan/2, 0);
            Form.Points[3].setLocation(0, 0, 3);
            Utils3DM.center(Form, 'p');
            
        }
        public Craft(String N, Point3DM Position, int side, int GunList){
            Side = side;
            setName(N);
            


            Form = new Shape3DM(false, Color.white, Position.POSX, Position.POSY, Position.POSZ);
            Acceleration = new Point3DM(0, 0, 0);
            
            Utils3DM.setPoints(Form, 'p', 0, 0, 0);
            
            Form.Points[0].setLocation(15, 0, 0);
            Form.Points[1].setLocation(0, 5, 0);
            Form.Points[2].setLocation(0, -5, 0);
            Form.Points[3].setLocation(0, 0, 3);
            

            Hitpoints = new Counter(0, 25, 25);
            Utils3DM.center(Form, 'p');
            
            
            Trails = new Trail[4];

            
            Momentum = new Point3DM(0, 0, 0);
            Form.Rotation.rotateAround('z', Math.toRadians(90));
            //groundEffect();
            CollisionRadius = 4;
            if(Form.Center.POSZ < CollisionRadius){
                Form.Center.POSZ = CollisionRadius;
            }
            Form.calcPoints();
            
            initTrails();
        //    updateTrails();
            
            
            AfterburnerTimer.setBaseIncrement(0.25);
            
            
      
            Firing = true;
            
            String S = GunList + "";
            for(int j = 0; j < S.length(); j++){
                int SS = Integer.parseInt(""+S.charAt(j));
                switch(SS){
                    case 0: Guns.add(MachineGun.copy(this, new Point3DM[] {Form.ActualPoints[1], Form.ActualPoints[2]}, Form.Rotation)); break;
                    case 1: Guns.add(Cannon.copy(this, new Point3DM[] {Form.ActualPoints[1], Form.ActualPoints[2]}, Form.Rotation)); break;
                    case 2: Guns.add(MissileLauncher.copy(this, new Point3DM[] {Form.ActualPoints[1]}, Form.Rotation));break;
                }
            }
            
            if(Guns.size() > 0){
                SelectedGun = Guns.gun(0);
            }
           // Guns.add(new Gun(this, new Point3DM[] {Form.ActualPoints[1], Form.ActualPoints[2]}, Form.Rotation, MachineGun, -1337, 30, 5));
        }
        public void initTrails(){
            
            Trails[0] = new Trail(this, 7, Form.ActualPoints[1], getColor(Side), 0.5);
            Trails[1] = new Trail(this, 7, Form.ActualPoints[2], getColor(Side), 0.5);
            
            
            AfterburnerPoint = new Point3DM[2];
            AfterburnerPoint[0] = new Point3DM(0, 0, 0);
            AfterburnerPoint[1] = new Point3DM(0, 0, 0);
            AfterburnerPoint[0].setToMidpoint(Form.Center, Form.ActualPoints[1]);
            AfterburnerPoint[1].setToMidpoint(Form.Center, Form.ActualPoints[2]);
            
            Trails[2] = new Trail(this, 8, AfterburnerPoint[0], Color.orange, 0.6);
            Trails[3] = new Trail(this, 8, AfterburnerPoint[1], Color.orange, 0.6);
        }
 
        public void updateTrails(){
            AfterburnerPoint[0].setToMidpoint(Form.Center, Form.ActualPoints[1]);
            AfterburnerPoint[1].setToMidpoint(Form.Center, Form.ActualPoints[2]);
            for(int i = 0; i < Trails.length; i++){
                Trails[i].chase((float)FramesPerSecond);
            }
            
        }
        
        public void moveWeapons(){
            for(int i = 0; i < Guns.size(); i++){
                Guns.gun(i).reload();
                
            }
        }
        public void hit(Point3DM ImpactSpot, float Damage){
            Point3DM Impulse = new Point3DM(0,0,0);
            ImpactSpot.subtract(Form.Center, Impulse);
            Impulse.normalize();
            Impulse.multiply(Damage * 5.0, Impulse);
            Momentum.add(Impulse, Momentum);
            Hitpoints.increment(-Damage);
            if((Hitpoints.checkBounds()) < 0){
                die();
            }
        }
        public void move(){
            Acceleration.POSX = 0;
            Acceleration.POSY = 0;
            Acceleration.POSZ = 0;
            
            moveWeapons();
            controls();
            
            setAngleOfAttack();
            
            setCoefficientOfLift();
            
            throttle();
            
            
            
            lift();
            
       
            
            drag();
            
            inducedDrag();
            
            gravity();
            
            groundEffect();
            
            final Point3DM T1 = new Point3DM();
            final Point3DM T2 = new Point3DM();
            Acceleration.divide(FramesPerSecond, T1);
            Momentum.add(T1, Momentum);
            Momentum.divide(FramesPerSecond, T2);
            Form.Center.add(T2, Form.Center);
            Speed = Momentum.length();
            Mach = Speed / World.getSpeedofSound(Form.Center.POSZ);
            
           
            if(Speed> 0){
                alignCraft();
            }
            
           // System.out.println();
        }
        public void setAngleOfAttack(){
            if(Speed > 0){
                final Point3DM T0 = new Point3DM();
                final Point3DM T1 = new Point3DM();
                final Point3DM T2 = new Point3DM();
                Momentum.copyTo(T0);
               
                Form.Rotation.getOriginalAxis('y', T1);
                T1.multiply(T0.dotProduct(T1), T2);
                T0.subtract(T2, T0);
            
                T0.normalize();
            
            
            
                Form.Rotation.getOriginalAxis('z', T1);
                double DotProduct = -T0.dotProduct(T1);
                DotProduct = GUI.trigCap(DotProduct);
                

            
                AngleOfAttack = Math.asin(DotProduct);
            }
        }
        
        public void setCoefficientOfLift(){
        		
            CoefficientOfLift = (MaxCL * AngleOfAttack / StallAngle); 
            if(AngleOfAttack > StallAngle){
                CoefficientOfLift = -MaxCL * GUI.square((Math.toDegrees(AngleOfAttack - StallAngle)) * StallSpeed) + MaxCL;
                
                CoefficientOfLift = Math.max(0, CoefficientOfLift);                
            } else
            if(AngleOfAttack < -StallAngle){
                CoefficientOfLift = MaxCL * GUI.square((Math.toDegrees(AngleOfAttack + StallAngle)) * StallSpeed) - MaxCL;
                CoefficientOfLift = Math.min(0, CoefficientOfLift);                
            }
            //Correct for supersonic flight
            
            if(Mach > 0.8){
                double CCL = 0.6 * Math.sin(10 * Mach + 6) + 1.9 - 0.6;
                if(Mach > 1 && (CCL > 0.5 || Mach > 1.4)){
                    CCL = 0.5;
                }
                CoefficientOfLift = CoefficientOfLift * CCL;
            }
            //System.out.println(CoefficientOfLift);
                //
            //if(this==Player)System.out.println(CoefficientOfLift+"\t"+Math.toDegrees(AngleOfAttack));
        }
        public void alignCraft(){
            if(Speed > 1){
                final Rotation3DM TempRotation = Utils3DM.getRotation();
                final Point3DM Facing = new Point3DM();
                Facing.setLocation(1, 0, 0);
                TempRotation.rotateAround('y', ElevatorControl);
                TempRotation.transform(Facing, Facing);
                Form.Rotation.transform(Facing, Facing);
            
                double Angle = Momentum.dotProduct(Facing) / Speed;
                Angle = GUI.trigCap(Angle);
                Angle = Math.acos(Angle);
                double PercentClosedPerSecond = Speed * Speed / StandardSpeed * World.airDensity(Form.Center.POSZ) * TurnSpeedMultiplier * 10000 / Mass;
                PercentClosedPerSecond = PercentClosedPerSecond / (1 + PercentClosedPerSecond);
                Angle = Angle * (1 - Math.pow(1-PercentClosedPerSecond, 1.0 / FramesPerSecond));
                
                Rotation3DM.getRotationBetween(Facing, Momentum, Angle, TempRotation);
                Rotation3DM.multiply(TempRotation, Form.Rotation, Form.Rotation);
                Utils3DM.putRotation(TempRotation);
            }
            
               
            
        }
        
        
        public void inducedDrag(){
           //Cdi =    CL²   / (p e AR)
           //Di = Cdi x S x ½rV2
            if(Speed > 0){
                final Point3DM T0 = new Point3DM();
                CoefficientOfInducedDrag =  CoefficientOfLift * CoefficientOfLift / Math.PI / InducedDragE / AspectRatio;
                
                
                //Correct for supersonic flight
                if(Mach > 0.8){
                    CoefficientOfInducedDrag = CoefficientOfInducedDrag * (1 + 2.5 * (Mach - 0.8));
                }
                
                //
                double InducedDrag = CoefficientOfInducedDrag * WingArea * 0.5 * getAirDensity() * Speed * Speed;
                if(Telemetry && this==Player)System.out.println("Di"+InducedDrag / Mass);
                Momentum.copyTo(T0);
                T0.normalize();
                T0.multiply(-InducedDrag / Mass, T0);
                Acceleration.add(T0, Acceleration);
            }

        }
        public void drag(){
            //Drag = Coefficient * Wing Area * 1/2(Air Density)(Velocity Squared)
            if(Speed > 0){
                
                final Point3DM T0 = new Point3DM();
                //Correct for supersonic flight
                double RealCD = CoefficientOfParasiticDrag;
                
                if(Mach > 1.1){
                    RealCD = 2.5 * RealCD * (1 + (Mach - 1.2) * (-0.3));
                }else if(Mach > 0.8){
                    RealCD = RealCD * (1 + (Mach - 0.8) * (5));
                }
                
                //
                Momentum.copyTo(T0);
                T0.normalize();
                double Drag = RealCD * WingArea * 0.5 * getAirDensity() * Speed * Speed;
                if(Telemetry && this==Player)System.out.println("Dp"+Drag / Mass + "\t" + RealCD);
                T0.multiply(-Drag / Mass, T0);
                Acceleration.add(T0, Acceleration);
           //     System.out.println("Drag"+Drag);
            }
            /*
            
           
            
            
            Temp = Temp.multiply(Momentum.length());
            
            Acceleration.add(Temp, Acceleration);
            
             */
        }
        public double getAirDensity(){
          
            return World.airDensity(Form.Center.POSZ);
        }
        public void lift(){
            //Lift = Coefficient * Wing Area * 1/2(Air Densicty)(Velocity Squared)
            
            if(Speed > 0){
                
                double Velocity = 0;
                final Point3DM Temp = new Point3DM();
                Form.Rotation.getOriginalAxis('y', Temp);
                
                final Point3DM MomentumCopy = new Point3DM();
                Momentum.copyTo(MomentumCopy);
                Temp.multiply(Momentum.dotProduct(Temp), Temp);
                MomentumCopy.subtract(Temp, MomentumCopy);
                
                Velocity = MomentumCopy.length();
                
                
                Lift = CoefficientOfLift * WingArea * 0.5 * getAirDensity() * (Velocity) * (Velocity) * 1;
                
                if(Telemetry && this==Player)System.out.println("Lift"+Lift / Mass + "\t" + CoefficientOfLift);
                if(Telemetry && this==Player)System.out.println("Velocity" + Speed);

                Form.Rotation.getOriginalAxis('y', Temp);
                
                Momentum.crossProduct(Temp, MomentumCopy);
                
                MomentumCopy.normalize();
               // MomentumCopy.multiply(10, ShapeX.Center);
                //ShapeX.Center.add(Form.Center, ShapeX.Center);
                //Temp.multiply(10, ShapeY.Center);
                //ShapeY.Center.add(Form.Center, ShapeY.Center);
                MomentumCopy.multiply(Lift / Mass, MomentumCopy);
                //System.out.println(Temp);
                //System.out.println(MomentumCopy);
                Acceleration.add(MomentumCopy, Acceleration); 
            }
           
            
        }
        public void gravity(){
            
 


            Acceleration.add(World.getGravity(), Acceleration);
            if(Telemetry && this==Player)System.out.println("G"+World.getGravity());
        }
        public void groundEffect(){
            if(Form.Center.POSZ <= HeightAboveGround){
                
                Form.Center.POSZ = HeightAboveGround;
                Momentum.POSZ = Math.max(Momentum.POSZ, 0);
                Acceleration.POSZ = Math.max(Acceleration.POSZ, 0);
                if(Speed > 0){
                    final Point3DM T0 = new Point3DM();
                    Utils3DM.Vectork.multiply(Momentum.dotProduct(Utils3DM.Vectork));
                    
                    Momentum.subtract(T0, T0);
                    T0.normalize();
                    T0.multiply(-Math.min(GroundDragMultiplier, T0.length()), T0);
                    Acceleration.add(T0, Acceleration);
                }
            }
            
        }
        public double getThrust(){
            
            if(Form.Center.POSZ > FlightCeiling){
                return 0;
            }else{
                

                return Thrust * Throttle / 10;
                
            }
        }
        public void throttle(){
            if(Throttle > 0){
                final Point3DM T0 = new Point3DM();
                if(Throttle > 10){ 
                    Throttle = 10;
                }
                if(Throttle < 0){
                    Throttle = 0;
                }
                
                Form.Rotation.getOriginalAxis('x', T0);
                
                T0.multiply(getThrust(), T0);
                //if(this==Player)System.out.println(AfterburnerTimer.Current);
                if(Afterburner && AfterburnerTimer.checkBounds() >= 0){
                    
                    
                    Trails[2].enable();
                    Trails[3].enable();
                    AfterburnerTimer.increment(-1.0 / FramesPerSecond);
                    AfterburnerTimer.cap();
                    T0.multiply(1 + AfterburnerBonus, T0);
                }else if(!Afterburner){
                    Trails[2].disable();
                    Trails[3].disable();
                    
                    AfterburnerTimer.increment(AfterburnerTimer.BaseIncrement / FramesPerSecond);
                    AfterburnerTimer.cap();
                }else{
                    Trails[2].disable();
                    Trails[3].disable();
                }
                
                if(Telemetry && this==Player)System.out.println("Thr"+T0.length() / Mass);
                Acceleration.add(T0.divide(Mass), Acceleration);
            }
        }
        public void controls(){
            MouseHorizontal = GUI.trigCap(MouseHorizontal);
            MouseVertical = GUI.trigCap(MouseVertical);
            
            //Horizontal Roll Control
            
            final Rotation3DM TempRotation = Utils3DM.getRotation();
            TempRotation.rotateAround('x', -MouseHorizontal * RollSpeedMultiplier * Math.sqrt(Momentum.length()) * World.airDensity(Form.Center.POSZ) * 1.0 / FramesPerSecond);
          //  Rotation3D.multiply(View.Rotation, TempRotations[0], View.Rotation);
            Rotation3DM.multiply(Form.Rotation, TempRotation, Form.Rotation);
            Utils3DM.putRotation(TempRotation);
            //Elevator Control
            ElevatorControl = MaxElevatorControl * -MouseVertical;
            
            //Throttle Control
            if(Throttle < TargetThrottle){
                Throttle = (float)Math.min(TargetThrottle, Throttle + ThrottleIncrement / FramesPerSecond);
            }else if(Throttle > TargetThrottle){
                Throttle = (float)Math.max(TargetThrottle, Throttle - ThrottleIncrement / FramesPerSecond);
            }
            
            /*LoopCounter.Current += 1.0 / FramesPerSecond;
            if(LoopCounter.checkBounds() > 0){
                LoopCounter.Current = 0;
                playSound(ThrustSounds[GUI.randomInt(0, 2)], Form.Center, Momentum.length()/200);
                if(Afterburner && AfterburnerTimer.checkBounds() >= 0){
                    playSound(AfterburnerSound, Form.Center, Momentum.length()/200);
                }
            }*/
            
        }
        
      
        public void doAI(){
            
            if(AILevel < 0){return;}
            
            AITrackShots();
            AIPickWeapon();
            AIEvasion();
            AIStrategy(); //->decisiontarget
            AILead(); //if attacking decisiontarget->leadtarget
            AIDecisions(); //
            AICollision();
            AIJudgeFire();
            AIControls();
            AIAvoidStall();
            
            AIMouseMovement();

        }
        public void radioOK(){
            if(this.Side == Player.Side){
                playSound("BEEPDOUB");
            }
        }
        public void radioDone(){
            if(this.Side == Player.Side){
                playSound("BEEP_FM");
            }
        }
        public void orderBlank(int N){
            switch(N){
                case -1: cancelOrder(); break;                //disregard
                case 0: clearOrders(); break;                      //clear
                
            }
        }
        public void orderPosition(int N, Point3DM Target){
            switch(N){
                case 1: Objectives.add(new Position3DM(Target, 0, 0)); ObjectiveNames.add("Guarding Position"); radioOK(); break;                          //hold
                case 2: Objectives.add(new Position3DM(Target, -400, 0)); ObjectiveNames.add("Moving"); radioOK();  break;                //move
                case 3: Objectives.add(new Position3DM(Target, 400, 0)); ObjectiveNames.add("Attacking"); radioOK();  break;                 //attack move
                
            }
            
        }
        public void orderVehicle(int N, Vehicle Target){
            switch(N){
                case 4: Objectives.add(Target); ObjectiveNames.add(Target.Side == Side ? "Following " + Target.Name : "Attacking " + Target.Name); radioOK();  break;              //follow-attack
             
            }
        }
        
        public void clearOrders(){
            radioDone();
            Objectives.clear();
            ObjectiveNames.clear();
        }
        public void cancelOrder(){
            radioDone();
            Objectives.removeLastElement();
            ObjectiveNames.removeLastElement();
        }
        public void AIStrategy(){
            
            boolean Telemetry = !!!true;
            if(Side==Player.Side && Telemetry){System.out.println(Objectives.size());}
            if(Objectives.size() == 0){//for no objective
                if(Side==Player.Side && Telemetry){System.out.println(Name + " Has No Orders");}
                if(Form.Center.POSZ < HeightFloor){
                    State = 't';
                    DecisionTarget = this;
                }else{
                    Vehicle T = scanForEnemies(SightRange, Side, Form.Center);
                    
                    if(T == null){
                        DecisionTarget = this;
                        State = 'c';
                    }else{
                        if(DecisionTarget != T){
                            attackTarget(T);
                        }
                    }
                }
            }else{ //if got objective
                Object3DM Obj = (Object3DM)Objectives.lastElement();
              
                if(Obj instanceof Position3DM){ //if objective is a position
                    Position3DM O = (Position3DM)Obj;
                    if(Side != Player.Side){
                        
                    }
                    if(O.CollisionRadius > 0){ //for attack move
                        
                        if(Side==Player.Side && Telemetry){System.out.println(Name + " Attack Moving");}
                        if(Form.Center.POSZ < HeightFloor){ //if too low take off
                            State = 't';
                            DecisionTarget = this;
                        }else{
                            Vehicle T = scanForEnemies(Math.abs(SightRange), Side, Form.Center);
     
                            if(T == null){ //else if nothing to attack then circle
                                State = 'c';
                                DecisionTarget = O;
                            }else if(DecisionTarget != T){ //or if something new to attack then go whack
                                System.out.println("Enemy Target Found");
                                
                                attackTarget(T);
                            }
                        }
                    }else if(O.CollisionRadius < 0){ //for force move
                        if(Side==Player.Side && Telemetry){System.out.println(Name + " Moving");}
                        if(Form.Center.POSZ < HeightFloor){ //if too low take off
                            State = 't';
                            DecisionTarget = this;
                        }else{
                        
                            double D = O.Form.Center.distanceTo(Form.Center);
                            if(D < Math.abs(O.CollisionRadius)){ //if close enough then finish
                                TickerTape.addLine(Name + ": Objective Complete: Move");
                                cancelOrder();
                            }else{
                                DecisionTarget = O;
                                State = 'c';
                            }
                        }
                    }else{ //for hold position
                        if(Side==Player.Side && Telemetry){System.out.println(Name + " Holding Position");}
                        if(Form.Center.POSZ < HeightFloor){ //if too low take off
                            State = 't';
                            DecisionTarget = this;
                        }else{
                            Vehicle T = scanForEnemies(Math.abs(SightRange), Side, O.Form.Center);
                            
                            if(T == null){ //else if nothing to attack then circle
                                State = 'c';
                                DecisionTarget = O;
                            }else if(DecisionTarget != T){ //or if something new to attack then go whack
                                attackTarget(T);
                                
                            }
                        }
                    }
                }else if(Obj instanceof Vehicle){ //if objective is a unit
                    
                    Vehicle V = (Vehicle)Obj;
                    if(V.Side == this.Side){ //for follow
                        if(Side==Player.Side && Telemetry){System.out.println(Name + " Following " + V.Name);}
                        if(Form.Center.POSZ < HeightFloor){ //if too low take off
                            State = 't';
                            DecisionTarget = this;
                        }else{
                            Vehicle T = scanForEnemies(Math.abs(SightRange), Side, V.Form.Center);
                            if(T == null){ //if nothing near then circle
                                State = 'c';
                                DecisionTarget = Obj;
                            }else if(DecisionTarget != T){ //if something near then attack
                                attackTarget(T);
                            }
                        }
                    }else{ //for attack
                        if(Side==Player.Side && Telemetry){System.out.println(Name + " Attacking " + V.Name);}
                        if(Form.Center.POSZ < HeightFloor){ //if too low take off
                            State = 't';
                            DecisionTarget = this;
                        }else{
                           
                            if(Obj == null || !Obj.Exists){ //if target invalid then cancel
                                cancelOrder();
                                TickerTape.addLine(Name + ": Objective Complete: Attack");
                            }else{ //else if target still valid 
                                if(DecisionTarget != Obj){ //if new target then attack
                                    attackTarget(V);
                                }
                                
                                
                            }
                        }
                    }
                }
            }
        }
        public void attackTarget(Vehicle T){
            DecisionTarget = T;
            State = 'a';
            AState = 'a';
            AttackTimer.setTo(0);
            RetreatTimer.setTo(0);
        }
        public void AIDecisions(){
            
            final Point3DM Difference = new Point3DM();
            final Point3DM Target = new Point3DM();
            
            DecisionTarget.Form.Center.copyTo(Target);
            Point3DM TempPoint = new Point3DM();
            
            
            Form.Center.subtract(Target, Difference);
            
            Firing = true;

            
 
           
            
            switch((Evading && State != 't' ? 'e' : State)){
                case 't': //Take Off
                    
                    Form.Rotation.getOriginalAxis('x', ControlTarget);
                    
                    ControlTarget.POSZ = 0;
                    ControlTarget.normalize();
                    if(Momentum.length() > 75){
                        ControlTarget.POSZ = 0.75; 
                    }else{
                        ControlTarget.POSZ = 0.1; //0 would make it crash 
                    }
                    ControlTarget.normalize();
                    ControlTarget.multiply(100, ControlTarget);
                    
                    
                    
                    ControlTarget.add(Form.Center, ControlTarget);
                    Firing = false;
                   
                    break;
                case 'c': //Circling
                    Firing = false;
                    DecisionTarget.Form.Center.copyTo(ControlTarget);
                    ControlTarget.POSZ = Math.max(HeightFloor, ControlTarget.POSZ);
                    if(Momentum.length() > 0){
                        Momentum.crossProduct(Utils3DM.Vectork, TempPoint);
                        TempPoint.normalize();
                    }else{
                        TempPoint.POSX = 1; TempPoint.POSY = 0; TempPoint.POSZ = 0;
                    }
                    
                    TempPoint.multiply(125 * CircleSide, Difference);
                    Difference.add(ControlTarget, ControlTarget);
                    if(DecisionTarget.Momentum.length() > 0){
                        DecisionTarget.Momentum.copyTo(TempPoint);
                        TempPoint.normalize();
                        TempPoint.multiply(175, Difference);
                        Difference.add(ControlTarget, ControlTarget);
                    }
                    
                    if(DecisionTarget.Form.Center == Form.Center){
                        ControlTarget.POSZ += GUI.sign(IdealHeight - ControlTarget.POSZ) * 45;
                    }else{
                        ControlTarget.POSZ += 25;
                    }
                    if(ControlTarget.distanceTo(Form.Center) < 100){
                       /* DecisionTarget.Momentum.copyTo(TempPoint);
                        TempPoint.normalize();
                        ControlTarget.subtract(Form.Center, ControlTarget);
                        ControlTarget.subtract(TempPoint.multiply(ControlTarget.dotProduct(TempPoint)), ControlTarget);
                        ControlTarget.add(TempPoint.multiply(10), ControlTarget);
                        ControlTarget.add(Form.Center, ControlTarget);*/
                    }
                    
                    break;
                case 'e': //Evading Target
                    EvasionTCounter.increment(-1.0/FramesPerSecond);
                    EvasionTarget.copyTo(ControlTarget);
                    
                    
                    if(EvasionTCounter.Current < 0){
                        Evading = false;
                    }
                    
                    break;
                case 'a': // Attacking Target

                    if(AState == 'a'){ //if Attacking
                        
                        
                        
                        
                        LeadTarget.copyTo(ControlTarget);
                        
                        if(Difference.length() < ResumeDistance){
                            AttackTimer.increment(1.0 / FramesPerSecond);
                        }
                        
                        if(Difference.length() < TurnDistance || AttackTimer.Current > AttackTimer.Max){ //If too close or too long
                            
                            beginRetreat(TempPoint);
                        }
                        
                        
                    }else if(AState == 'r'){ //if Retreating
                        
                        //Count Retreat Time
                        RetreatTimer.increment(1.0 / FramesPerSecond);
                        
                        if(Difference.length() > ResumeDistance || RetreatTimer.Current > RetreatTimer.Max){//If retreating for too long
                            //attack
                            AState = 'a';
                            
                            AttackTimer.setTo(0);
                            RetreatTimer.setTo(0);
                        }
                        
               
                    }
                    
                    break;
                case 'f':
                    
                    break;
                
            }

               
        }
        public void AICollision(){
         	Point3DM Target;
        	
         
            Vector<Point3DM> NewPath = find(Form.Center, ControlTarget.copy(), 1.0/0, new HashSet<Plane>(), 9);
            if(NewPath == null){
            	NewPath = find(Form.Center, DecisionTarget.Form.Center.copy(), 1.0/0, new HashSet<Plane>(), 9);		
            }
            if(NewPath != null){
            	Path = NewPath;	
            	Path.insertElementAt(Form.Center, 0);
            	process(Path);
            }

    
            if(Path != null){

                ControlTarget.copyFrom(Path.get(1));
            }else{
                
            }
        }   
       	public void process(Vector<Point3DM> PathPoints){
       		for(int i = 0; i < PathPoints.size(); i++){
       			for(int j = PathPoints.size()-1; j > i+1; j--){
       				Point3DM Start = PathPoints.get(i);
       				Point3DM End = PathPoints.get(j);
       				Vector<Plane> PossiblePlanes = Planes;
       				Point3DM Hit = null;
       				for(int h = 0; h < PossiblePlanes.size(); h++){
       					Plane P = (Plane)PossiblePlanes.get(h);	
       					if(Hit == null)Hit = P.checkCollision(Start, End);
       				}
                	if(Hit == null){
                		for(int k = j-1; k > i; k--){
                			PathPoints.removeElementAt(k);
                		}
                		process(PathPoints);
                		return;
                	}
       			}	
       		}
       	}
        public Vector<Point3DM> find(Point3DM Start, Point3DM End, double MaxDistance, Set<Plane> Avoided, int Depth){
     		//Check to see if path is impossible
     		if(Start.distanceTo(End) > MaxDistance){
     			return null;	
     		}
        	
        	//Check to see if path is clear
            Vector PossiblePlanes = PlaneTable.testArea(Start.POSX, Start.POSY, End.POSX, End.POSY);
            //Vector PossiblePlanes = Planes;
            Point3DM FirstHit = null;
            Plane HitPlane = null;
            int HitCount = 0;
            for(int i = 0; i < PossiblePlanes.size(); i++){
                Plane P = (Plane)PossiblePlanes.get(i);
                Point3DM Hit = P.checkCollision(Start, End);
                if(Hit != null){
                	HitCount++;	
                	if(Depth == 0){//if no more splits then return
            			return null;
            		}
                	if(!P.Finite){//if on other side of infinite plane give up
                    	return null;	
                	}
                	if(FirstHit == null || Hit.distanceTo(Start) < FirstHit.distanceTo(Start)){//if find new collision closer than old one, save it
                		FirstHit = Hit;
                    	HitPlane = P;
                	}
                }
            }
        	if(HitCount % 2 == 1){//if odd number of planes, then outside/inside separation, give up
        		return null;	
        	}
        
            if(FirstHit == null){
            	Vector<Point3DM> Path = new Vector<Point3DM>();
       //     Path.add(Start);
            	Path.add(End);
            	return Path;
            
            }else if(Avoided.contains(HitPlane)){//if retracing same route then backtrack
            	return null;
            }else{//else
            	
                double CutOff = MaxDistance;
                Vector<Point3DM> ShortestPath = new Vector<Point3DM>();
                Avoided.add(HitPlane);
                //Vector<Vector<Point3DM>> PathList = new Vector<Vector<Point3DM>>();
                for(int i = 0; i < HitPlane.Form.ActualPoints.length; i++){//check every possible detour to avoid polygon
  
                    Point3DM Unit = HitPlane.Form.ActualPoints[(i + 1) % HitPlane.Form.ActualPoints.length].subtract(HitPlane.Form.ActualPoints[i]);
                    Unit.normalize();
                    Point3DM Delta = HitPlane.Form.ActualPoints[i].subtract(FirstHit);

                    Unit.multiply(Delta.dotProduct(Unit), Unit);
  
                    Delta.subtract(Unit, Delta);
            		Delta.normalize(Delta.length() + 30);
            		Delta.add(Start.subtract(End).normalize(30));
            		/*
            		if(HitPlane.Normal.dotProduct(Start.subtract(End)) > 0 ){
            			Delta.add(HitPlane.Normal.multiply(30), Delta);	
            		}else{
            			Delta.add(HitPlane.Normal.multiply(-30), Delta);	
            		}*/
                    Point3DM NewPoint = Delta.add(FirstHit);
                    
                    
	             
	                
                    
                    Vector<Point3DM> PathA = find(Start, NewPoint, CutOff - End.distanceTo(NewPoint), Avoided, Depth-1);
                    if(PathA == null) continue;
                    
                    
                    //find path segments needed to complete detour
                    double DA = Utils3DM.pathLength(PathA);
   
                    DA += Start.distanceTo(PathA.get(0));
       
                    
                    Vector<Point3DM> PathB = find(NewPoint, End, CutOff - DA, Avoided, Depth-1);
                    if(PathB == null) continue;
                    
                    
                    double DB = Utils3DM.pathLength(PathB);
      				DB += NewPoint.distanceTo(PathB.get(0));

                    //if detour is short enough hold in store in case shorter detour appears
                    CutOff = DA + DB;
                    ShortestPath.clear();
                    ShortestPath.addAll(PathA);
                  //  ShortestPath.removeElementAt(ShortestPath.size()-1);
                    ShortestPath.addAll(PathB);
                }
            
                if(ShortestPath.size() > 0){
                	Avoided.remove(HitPlane);
                    return ShortestPath;
                }else{
                	Avoided.remove(HitPlane);
                    return null;
                }
            }
        }
        public double distanceToUrgency(double Distance){
            double X = GUI.cap(0, Distance, CollisionBounds.CollisionRadius) / CollisionBounds.CollisionRadius;
            return GUI.cap(0, 1.5 - 1.5 * X, 1);
        }
        public void beginRetreat(Point3DM TempPoint){
            if(this == Player)System.out.println("ARR");
            this.Form.Center.copyTo(ControlTarget);
            TempPoint = Utils3DM.getRandomUnit();
            TempPoint.multiply(ResumeDistance * 10000, TempPoint);
            if(GUI.randomInt(1, 4) == 1){
                
                TempPoint.POSX /= 10;
                TempPoint.POSY /= 10;
                TempPoint.normalize();
                TempPoint.multiply(ResumeDistance * 10000, TempPoint);
                if(Math.random() * Form.Center.POSZ / 1000 > 2.5){
                    TempPoint.POSZ = -Math.abs(TempPoint.POSZ);
                }
                
            }

            ControlTarget.add(TempPoint, ControlTarget);

            AState = 'r';
            AttackTimer.setTo(0);
            RetreatTimer.setTo(0);
        }
        public void manualFire(){
            if(this == Player && SpaceDown && Player.SelectedGun != null){
                Player.SelectedGun.fire(DecisionTarget);
            }
        }
        public void AIPickWeapon(){
            double Distance = ControlTarget.subtract(this.Form.Center).length();
            int Stickiness = 10; //Reluctance to change weapons
            int Total = 100; //Total Preference
            int[] GunPriorities = new int[Guns.size()];
            for(int i = 0; i < GunPriorities.length; i++){
                GunPriorities[i] = Total;
                
                GunPriorities[i] += Guns.gun(i).Shot.AIPrefMod;
                GunPriorities[i] = (int)(GunPriorities[i] * Guns.gun(i).ReloadTimer.Current / Guns.gun(i).ReloadTimer.Max);
                
                
                if(Guns.gun(i) == SelectedGun){
                    GunPriorities[i] = GunPriorities[i] + Stickiness;
                }
                
                if(Guns.gun(i).Shot.MaxEffectiveRange < Distance || Guns.gun(i).Shot.MinEffectiveRange > Distance){
                    GunPriorities[i] = 0;
                }
                
                if(Guns.gun(i).Shot.FireCost > Guns.gun(i).ReloadTimer.Current){
                    GunPriorities[i] = 0;
                }
            }
            int Max = 0;
            for(int i = 0; i < GunPriorities.length; i++){
                if(GunPriorities[i] > Max){
                    Max = GunPriorities[i];
                    SelectedGun = Guns.gun(i);
                }
            }
            if(Max == 0){
                SelectedGun = null;;
            }
            
        }
        public boolean willFire(){
            if(this.State != 'a' || this.AState != 'a'){
                return false;
            }

            if(!Firing){
                return false;
                
            }
            if(SelectedGun == null){
                return false;
            }
            return true;
        }
        public void AIJudgeFire(){

            if(!willFire()){
                Firing = false;
                return;
            }
            Point3DM TempPoint = new Point3DM(0, 0, 0);
            Point3DM Difference = new Point3DM(0, 0, 0);
            LeadTarget.subtract(Form.Center, Difference);
            
            Form.Rotation.getOriginalAxis('x', TempPoint);
            
            if(Math.acos(GUI.trigCap(TempPoint.dotProduct(Difference) / Difference.length())) < AngleToFire){//If close enough fire
                if(mightIntersectPlane(Form.Center, LeadTarget)){
                    return;
                    
                }
                /*
                for(int i = 0; i < Planes.size(); i++){
                    if(Planes.pl(i).checkCollision(LeadTarget, Form.Center, 0) != null){
                        return;
                    }
                }*/
                
                Projectile NewShot = SelectedGun.fire(DecisionTarget);
                if(TrackedShot == null && NewShot != null){
                    TrackedShot = NewShot;
                }
            }
        }
        
        public void AIControls(){
            ///Throttle
            Point3DM Target = ControlTarget;
            TargetThrottle = 7.5f - (float)(Speed - Math.max(DecisionTarget.Momentum.length(), IdealSpeed)) * OverSpeedDec;
            if(Form.Center.distanceTo(Target) < 100){
                TargetThrottle *= Form.Center.distanceTo(Target) / 100;
            }
            if(Form.Center.distanceTo(Target) > 500){
                TargetThrottle += (Form.Center.distanceTo(Target) - 500) / 100;
            }
            if(TargetThrottle > 7.5 && Afterburner == true && AfterburnerTimer.Current > 0){
                Afterburner = true;
            }else if(TargetThrottle > 10 && AfterburnerTimer.Current * 2 > AfterburnerTimer.Max){
                Afterburner = true;
            }else{
                Afterburner = false;
            }
            TargetThrottle = Math.min(TargetThrottle, 10);
            
            if(Form.Center.distanceTo(DecisionTarget.Form.Center) > 1000){
                TargetThrottle = 10;
            }
            
            if(Speed == 0){
                return;
            }
            
            
            
            
            if(Firing){
                
                pointToTarget();
            }else{
                Point3DM Moving = Momentum.copy();
                Moving.normalize();
                
                Point3DM Difference = ControlTarget.subtract(Form.Center);
                Difference.normalize();
                double Theta = Math.toDegrees(Math.acos(GUI.trigCap(Moving.dotProduct(Difference))));
                if(Theta < 10 && Momentum.length() > 50){
                    //cruiseToTarget();
                    pointToTarget();
                }else{
                    pointToTarget();
                }
            }
    
        }
        public void cruiseToTarget(){
            Point3DM DCap = ControlTarget.subtract(Form.Center);
            Point3DM G = World.getGravity();
            DCap.normalize();
            Point3DM H = G.subtract(DCap.multiply(G.dotProduct(DCap)));
            double LMag = Math.sqrt(GUI.square(Thrust) - GUI.square(H.length()));
            Point3DM L = DCap.multiply(LMag);
            L.add(H, ControlTarget);
            Form.Center.add(ControlTarget, ControlTarget);
            pointToTarget();
            
        }
        public void pointToTarget(){
            
            
            
            //Init Points
            final Point3DM WAxis = new Point3DM();
            final Point3DM MAxis = new Point3DM();
            final Point3DM LAxis = new Point3DM();
            final Point3DM Difference = new Point3DM();
            final Point3DM DiffSubM = new Point3DM();
            final Point3DM DiffSubW = new Point3DM();
            
            Point3DM Target = ControlTarget;
         
            Form.Rotation.getOriginalAxis('y', WAxis);
            
            Form.Rotation.getOriginalAxis('x', MAxis);
       
            
            WAxis.crossProduct(MAxis, LAxis);
            LAxis.normalize();
            Target.subtract(Form.Center, Difference);
            
            
            MAxis.multiply(Difference.dotProduct(MAxis), DiffSubM);
            Difference.subtract(DiffSubM, DiffSubM);
            
            
            WAxis.multiply(Difference.dotProduct(WAxis), DiffSubW);
            Difference.subtract(DiffSubW, DiffSubW);
            
            double AngleToTurn = Math.toDegrees(Math.acos(Math.max(-1, Math.min(1, Difference.dotProduct(MAxis) / (Difference.length())))));
            
            
            //Roll
       
            
            double RollAmount = Math.toDegrees(Math.atan2(DiffSubM.dotProduct(WAxis) / DiffSubM.length(), DiffSubM.dotProduct(LAxis) / DiffSubM.length()));
            
            if(RollAmount > 180){RollAmount = 360 - RollAmount;}
            if(RollAmount < 0){RollAmount += 180;}else{RollAmount -= 180;}
                
            AIMouseTarget.x = GUI.trigCap(-Math.toRadians(RollAmount) / RollSpeedMultiplier / Math.sqrt(Speed) * RollResponsiveness);

            
            //Pitch
            
            double PitchAngle = Math.toDegrees(Math.acos(GUI.trigCap(DiffSubW.dotProduct(MAxis) / DiffSubW.length())));
         
            AIMouseTarget.y = Math.atan(PitchAngle);
        
        }
        public void checkEvasion(){
            int NearBullets = 0;
            boolean Homing = false;
            for(int i = 0; i < Projectiles.size(); i++){
                Projectile P = Projectiles.pr(i);
                if(P.Form.Center.distanceTo(Form.Center) < P.LastPosition.distanceTo(Form.Center)){
                    if(P.Form.Center.distanceTo(Form.Center) < DodgeImpactRadius && P.LastPosition.distanceTo(Form.Center) > DodgeImpactRadius){
                        NearBullets++;
                    }
                    if(P.Homing && P.Form.Center.distanceTo(Form.Center) / P.Speed < DodgeImpactTime && P.LastPosition.distanceTo(Form.Center) / P.Speed > DodgeImpactTime){
                        Homing = true;
                    }
                }
            }
            
            if(NearBullets >= 2 && ((!Evading && EvasionTCounter.Current >= 0) || Homing)){
                
                initEvasion();
            }
        }
        public void initEvasion(){
            EvasionTCounter.setTo(EvasionTCounter.Max);
            Utils3DM.getRandomUnit(EvasionTarget);
            EvasionTarget.multiply(1000000, EvasionTarget);
            EvasionTarget.add(Form.Center, EvasionTarget);
           
            final Point3DM Projection = new Point3DM();
            final Point3DM Face = new Point3DM();
            Form.Rotation.getOriginalAxis('x', Face);
            EvasionTarget.subtract(Form.Center, Projection);
            double DotProduct = Projection.dotProduct(Face);
            if(DotProduct > 0){
                Face.multiply(DotProduct, Face);
                EvasionTarget.add(Face, EvasionTarget);
                EvasionTarget.add(Face, EvasionTarget);
            }
            Evading = true;
          //  Firing[1] = false;
        }
        public void AIEvasion(){
            checkEvasion();
            if(EvasionTCounter.Current < 0){
                if(EvasionTCounter.checkBounds() >= 0){
                    EvasionTCounter.increment(-1.0/FramesPerSecond);
                }else{
                    EvasionTCounter.setTo(EvasionTCounter.Max);
                }
            }
       //     if(this==Player)System.out.println(EvasionTCounter);
        }
        public void AIAvoidStall(){
            double DAoA = Math.toDegrees(AngleOfAttack);
            double DSA = Math.toDegrees(StallAngle);
            
            if(DAoA > DSA - 1){
                //System.out.println("AVOIDING STALL^");
                AIMouseTarget.y = AIMouseTarget.y * (-GUI.square(DAoA - DSA + 1)  + 1);
            }
        }
        public void AIMouseMovement(){
            
            MouseHorizontal = GUI.trigCap(AIMouseTarget.x);
            
            MouseVertical = GUI.trigCap(AIMouseTarget.y);
            
        }
        public void cycleShot(){
            int i = Guns.lastIndexOf(SelectedGun);
            i = (i + 1 ) % Guns.size();
            SelectedGun = Guns.gun(i);
            
        }
        public void printNow(Graphics2D Painter, ObserverM Viewer){
            Form.printNow(Painter, Viewer);
            if(Afterburner){
                Trails[Trails.length - 2].printNow(Painter, Viewer);
                Trails[Trails.length - 1].printNow(Painter, Viewer);
            }
            for(int i = 0; i < Trails.length - 2; i++){
                Trails[i].printNow(Painter, Viewer);
            }
            
        }
        public void printNow(Graphics2D Painter, ObserverM Viewer, Color Tint){
            Form.printNow(Painter, Viewer, Tint);
            if(Afterburner){
                Trails[Trails.length - 2].printNow(Painter, Viewer, Tint);
                Trails[Trails.length - 1].printNow(Painter, Viewer, Tint);
            }
            for(int i = 0; i < Trails.length - 2; i++){
                Trails[i].printNow(Painter, Viewer, Tint);
            }
            
        }
        public void print(ObserverM Viewer){
            Form.print(Viewer);
            if(Afterburner){
                Trails[Trails.length - 2].print(Viewer);
                Trails[Trails.length - 1].print(Viewer);
            }
            for(int i = 0; i < Trails.length - 2; i++){
                Trails[i].print(Viewer);
            }
            
        }

    }
    double FramesPerSecond = 60;
    Timer Stopwatch;
    TimeKeeper Time = new TimeKeeper(100);
    JCanvas Area;
    double UnPausedTime;
    int MouseX;
    int MouseY;
    int MouseDX;
    int MouseDY;
    Robot MyRobot;
    boolean TwistLeft;
    boolean TwistRight;
    boolean TwistUp;
    boolean TwistForward;
    boolean ClickingL;
    boolean ClickingR;

    boolean SpaceDown;
    boolean ThrottleUp;
    boolean ThrottleDown;
    
    Plane Floor;
    TextScroller TickerTape = new TextScroller(4, 5);
    ObserverM View;
    Point3DM TowPoint = new Point3DM(0, 0, 0);
    double TowDistance = 25;
    
    int Facing; //0 - Chase / 1 - Cockpit
    int Direction = 1; //1 - DirectionCount Compass Rotations /  -1 - up
    int DirectionCount = 8;
    double TurnIncrement = 225;// in degrees / second
    double CurrentDirection = 1;
    double CurrentPitch = 0;
    
    Shape3DM Grid;
    Craft Player; 
    
    ProjectileType MachineGunShot;
    ProjectileType MissileShot;
    ProjectileType CannonShot;
    Gun MachineGun;
    Gun MissileLauncher;
    Gun Cannon;
    Gun[] GunList;
    
    
    
    int StarCount = 100;
    double DebrisSpeedMultiplier = 0.1;
    
    Objective WinCondition;
    Objective LoseCondition;
    FlatHash PlaneTable = new FlatHash(20);

    Sprite3DM[] Stars = new Sprite3DM[StarCount];
    
    ObjectVector Doodads = new ObjectVector(); //Doodads
    ObjectVector Sprites = new ObjectVector(); //Sprites
    GeneralVector Planes = new GeneralVector(); //Planes
    ObjectVector Vehicles = new ObjectVector(); //Vehicles
    ObjectVector Projectiles = new ObjectVector(); //Projectiles
    ObjectVector Trails = new ObjectVector(); //Trails
    ObjectVector NavPoints = new ObjectVector(); //Nav Points
    
    ObjectVector VehicleStore = new ObjectVector(); //Delayed Spawn Vehicles
    ObjectVector[] DoodadStore = {new ObjectVector(), new ObjectVector(), new ObjectVector()}; //Out of Use Doodads
    ObjectVector ShortTrailStore = new ObjectVector(); //Out of use trails
    ObjectVector LongTrailStore = new ObjectVector(); //Out of use Homing trails
    long FrameCounter;
    
    public void init(){
        
        View = new ObserverM(0, 0, 3, new Rotation3DM(), Area.Painter);
        View.setArea(0, 0, Area.W, Area.H, 1.0);
        try{
            MyRobot = new Robot();
        }catch(Exception e){System.out.println("Robot Creation Error"); System.exit(0);}
        
        
        Grid = new Shape3DM(false, Color.GREEN.darker(), 0, 0, 0);
        Utils3DM.setPoints(Grid, 'g', 50, 50, 500);
        
       
       
        initGUI();
        initShots();
        initStars();
        initLevel(LevelPath);
        
        initViewpoint(Player);
        
    //    Structure S = new Structure("Thing", Player.Form.Center.subtract(new Point3DM(-100, -100, 0)), 2, 1200, 3);
     //   Vehicles.add(S);
   //     System.out.println(Thing.FurthestDistance);
    }
    
    
    public void initShots(){
        //MachineGun Shot
        //Speed 1050m/s
        //HitRadius = 4
        //Duration = 2s
        {//                                 Speed      CR  FC DUR   ROT  SPREAD DMG FRAGS
        MachineGunShot = new ProjectileType(1050 + 150, 2, 0.5, 2.0, true, 0.005, 0.5, 1);
        MachineGunShot.setTrail(0.04, 3);
        Shape3DM Form = new Shape3DM(false, Color.red, 0, 0, 0);
        Utils3DM.setPoints(Form, 'p', 0.3, 0.9, 0);
        Form.Rotation.rotateAround('y', Math.toRadians(90));
        Utils3DM.center(Form, 'l');
        MachineGunShot.setShape(Form);
        MachineGunShot.setAIData(1450, 0, 0);
        for(int i = 0; i < MachineGunShot.Form.Points.length; i++){
            Form.Rotation.transform(MachineGunShot.Form.Points[i], MachineGunShot.Form.Points[i]);
        }
        MachineGunShot.Form.Rotation.identify();

        MachineGunShot.FireSound = (WaveSound)Base.SoundMap.get("Shot - MachineGun");;
        
        
        }
        
        //Missile Shot
        //Speed 650m/s
        //HitRadius = 5
        //Duration = 3s
        {
        MissileShot = new ProjectileType(600 + 150, 3, 5, 5.0, true, 0.1, 5.0, 3);
        MissileShot.setTrail(1, -1);
        Shape3DM Form = new Shape3DM(false, Color.yellow, 0, 0, 0);
        Utils3DM.setPoints(Form, 'p', 2.5, 7, 0);
        Form.Rotation.rotateAround('y', Math.toRadians(-90));
        Utils3DM.center(Form, 'l');
        MissileShot.setShape(Form);
        for(int i = 0; i < MissileShot.Form.Points.length; i++){
            Form.Rotation.transform(MissileShot.Form.Points[i], MissileShot.Form.Points[i]);
        }
        MissileShot.Form.Rotation.identify();
        MissileShot.setHoming(Math.toRadians(6), 0.35);
        MissileShot.setAIData(2500, 750, 2);
        
        MissileShot.FireSound = (WaveSound)Base.SoundMap.get("Shot - Missile");
        
        }
        //Cannon Shot
        {
        CannonShot = new ProjectileType(1500 + 150, 6, 10, 3.0, true, 0.002, 5.0, 3);
        CannonShot.setTrail(0.5, 1);
        Shape3DM Form = new Shape3DM(false, Color.red, 0, 0, 0);
        Utils3DM.setPoints(Form, 'o', 6, 2, 2);
        Utils3DM.center(Form, 'p');
        CannonShot.setShape(Form);
        CannonShot.setAIData(1500, 0, 1);
        CannonShot.FireSound = (WaveSound)Base.SoundMap.get("Shot - Cannon");
        }
        //MachineGun
        //MaxBurst 2s
        //ReloadTime 8s
        MachineGun = new Gun("Machine Gun", null, null, null, MachineGunShot, -1337, 20, 2.5);
        
        //MissileLauncher
        //MaxBurst 2 shots
        //ReloadTime 12.5s
        MissileLauncher = new Gun("Missile", null, null, null, MissileShot, -1337, 10, 0.5);
        //Cannon
        //MaxBurst 1 shot
        //Reload Time 10s
        Cannon = new Gun("Cannon", null, null, null, CannonShot, -1337, 10, 1);
        
        
        GunList = new Gun[]{MachineGun, Cannon, MissileLauncher};
    }
    public void initLevel(String Path){
        try{
            
            initGround();
            System.out.println("Loading Level Data from " + Path);
            DataArray InputData = new DataArray();
            FileInputStream F = new FileInputStream(Path);

            InputData.ensureCapacity(F.available());
            System.out.println("Available: " + F.available());
            F.read(InputData.Buffer, 0, F.available());
            InputData.reset();
            
            //Read Planes
            System.out.println("Loading Planes");
            int N = InputData.readInt(); 
            for(int i = 0; i < N; i++){
                Point3DM[] NewPlane = new Point3DM[InputData.readInt()];
                for(int j = 0; j < NewPlane.length; j++){
                    NewPlane[j] = new Point3DM(0, 0, 0);
                    NewPlane[j].fromBareBytes(InputData);
                }
             //   System.out.println(i + " " + Arrays.toString(NewPlane));
                Planes.add(new Plane(NewPlane));

            }
            
            //Init PlaneTable
            System.out.println("Setting up Plane Graph");
            for(int i = 0; i < Planes.size(); i++){
                if(!Planes.pl(i).Finite){
                    PlaneTable.addObjectToAll(Planes.pl(i));
                    continue;
                }
                double[] Data = new double[Planes.pl(i).Form.PointCount * 2];
                for(int j = 0; j < Planes.pl(i).Form.PointCount; j++){
                    Data[2*j] = Planes.pl(i).Form.ActualPoints[j].POSX;
                    Data[2*j + 1] = Planes.pl(i).Form.ActualPoints[j].POSY;
                }
                PlaneTable.addObject(Planes.pl(i), Data);
            }
            PlaneTable.process();
            System.out.println(PlaneTable);
            System.out.println("Planes Loaded");
            //Read Units
            System.out.println("Loading Units");
            N = InputData.readInt();
            for(int i = 0; i < N; i++){
                char Type = InputData.readChar();
                if(Type == 'c'){ //if it is a craft
                    Point3DM P = new Point3DM(0, 0, 0); P.fromBareBytes(InputData); //Read Position
                    Color Tint = new Color(InputData.readInt()); //Read Color
                    int Side = getSide(Tint);
                    
                    //Read Guns
                    String Guns = "9";
                    int M = InputData.readInt();
                    for(int j = 0; j < M; j++){
                        if(InputData.readBoolean()){
                            Guns += "" + j;
                        }
                    }
                    
                    //Read Data
                    int[] DataToRead = new int[InputData.readInt()];
                    for(int j = 0; j < DataToRead.length; j++){
                        DataToRead[j] = InputData.readInt();
                    }
                    
                    //Create Craft
                    P.POSZ = DataToRead[3];
                    
                    Craft C = new Craft(getFaction(Side) + " " + (Side == -1 ? "" : getGreek(Side, Craft.class)), P, Side , Integer.parseInt(Guns), FlightWindow.CraftDatas[DataToRead[0]]);
                    
                    if(P.POSZ != 0){
                        C.Momentum.POSX = 50;
                    }
                    if(C.Side == -1){
                        C.Side = 0;
                        Player = C;
                    }
                    
                    //Fix Data
                    C.Hitpoints = new Counter(0, DataToRead[1], DataToRead[1]);
                    C.initAI(DataToRead[2]);
                    C.Form.Center.POSZ = DataToRead[3];
                    if(DataToRead[4] == 0){
                        VehicleStore.add(C);
                    }else{
                        VehicleStore.add(C);
                        C.Hitpoints.Current = 0;
                        C.Hitpoints.BaseIncrement = DataToRead[4];
                    }
                    
                }else if(Type == 't'){ //if it is a turret
                    
                    Point3DM P = new Point3DM(0, 0, 0); P.fromBareBytes(InputData); //Read Position
                    Color Tint = new Color(InputData.readInt()); //Read Color
                    int Side = getSide(Tint);
                    
                    //Read Guns
                    int[] DataToRead = new int[InputData.readInt()];
                
                    for(int j = 0; j < DataToRead.length; j++){
                        DataToRead[j] = InputData.readBoolean() ? 1 : 0;
                    }
                    int Gun = -1;
                    for(int j = 0; j < DataToRead.length; j++){
                        if(DataToRead[j]!=0){
                            Gun = j;
                        }
                    }
                    
                    //Read Data
                    DataToRead = new int[InputData.readInt()];
                    for(int j = 0; j < DataToRead.length; j++){
                        DataToRead[j] = InputData.readInt();
                    }
                    
                    Gun[] Guns = {MachineGun, Cannon, MissileLauncher};
                    
                    //Create Vehicle
                    P.POSZ = DataToRead[3];
                    Vehicle V;
                    if(Gun >= 0){
                        V = new Turret(getFaction(Side) + " Turret " + getGreek(Side, Turret.class), P, Side, DataToRead[0] + 1, Guns[Gun]);
                    }else{
                        V = new Structure(getFaction(Side) + " Structure " + getGreek(Side, Structure.class), P, Side, 0 , DataToRead[2]);
                    }
                    
                    //Fix Data
                    V.Hitpoints = new Counter(0, DataToRead[1], DataToRead[1]);
                    
                    V.initAI(DataToRead[2]);
                    
                    if(DataToRead[4] == 0){
                        VehicleStore.add(V);
                    }else{
                        VehicleStore.add(V);
                        V.Hitpoints.Current = 0;
                        V.Hitpoints.BaseIncrement = DataToRead[4];
                    }
                }
                
                
               
            }
            System.out.println(VehicleStore.size() + " Units Loaded");
            //Read Orders
            System.out.println("Orders Loaded");
            for(int i = 0; i < N; i++){
                int O = InputData.readInt();
                char C = InputData.readChar();
                if(!(VehicleStore.ve(i) instanceof Craft)){continue;}
                Craft V = (Craft)VehicleStore.ve(i);
                switch(C){
                    case ' ': break;
                    case 'v':{
                        
                        int Index = InputData.readInt();
                        
                        V.orderVehicle(O, VehicleStore.ve(Index));
                        }break;
                    case 'p':{
                        
                        Point3DM P = new Point3DM(0, 0, 0); P.fromBareBytes(InputData);
                        V.orderPosition(O, P);
                        }break;
                }
            }
            System.out.println("Orders Loaded");
            //Read NavPoints
            
            N = InputData.readInt();
            System.out.println("Loading "+N + " NavPoints");
            for(int i = 0; i < N; i++){
                Point3DM P = new Point3DM(0, 0, 0); P.fromBareBytes(InputData);
                P.POSZ = 1000;
                System.out.println(P);
                NavPoints.add(new Position3DM(P, InputData.readInt(), i));
                NavPoints.lastElement().Name = "Nav Point " + GUI.getGreek(i);
                
            }
            Object[] NP = NavPoints.toArray();
            Arrays.sort(NP);
            NavPoints.clear();
            for(int i = 0; i < NP.length; i++){
                NavPoints.add((Object3DM)NP[i]);
            }
            System.out.println("NavPoints Loaded");
            //Read Objectives
            System.out.println("Loading Objectives");
            char C = InputData.readChar();
            WinCondition = makeObjective(C);
            WinCondition.fromBytes(InputData);
            C = InputData.readChar();
            LoseCondition = makeObjective(C);
            LoseCondition.fromBytes(InputData);
            System.out.println("Objectives Loaded");
            
            //Enable starting units
            System.out.println("Activating Starting Units");
            
            for(int i = VehicleStore.size() - 1; i >= 0; i--){

                Vehicle V = VehicleStore.ve(i);
                if(V.Hitpoints.Current > V.Hitpoints.BaseIncrement){
      
                    VehicleStore.remove(V);
                    Vehicles.add(V);
                  
                }
            }
           
            System.out.println(Vehicles.size() + " Units Activated");
            System.out.println(VehicleStore.size() + " Units in VehicleStore");
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void initGround(){
        Point3DM[] PV = new Point3DM[4];
        PV[0] = new Point3DM(-1000000, -1000000, 0);
        PV[1] = new Point3DM(1000000, -1000000, 0);
        PV[2] = new Point3DM(1000000, 1000000, 0);
        PV[3] = new Point3DM(-1000000, 1000000, 0);
        Floor = new Plane(PV);
        
        Floor.Finite = false;
        Floor.Form.Tint = Color.green.darker();
        Planes.add(Floor);   
    }
    /*
    public void initPlanes(){
        
        //Ground Plane
        
        
        int[][] Data = GameData.TerrainData;
        for(int i = 0; i < Data.length; i++){
            int[] A = Data[i];
            if(A[0] == 0){
                makePyramid(A[1], A[2], A[3], A[4]);
            }else if(A[0] == 1){
                makeRing(A[1], A[2], A[3], A[4], A[5]);
            }
        }
       
        
        
    }*/
    public void initStars(){
        for(int i = 0; i < StarCount; i++){
            Point3DM StarPosition = new Point3DM(0, 0, 0);
            do{
                StarPosition.setLocation(2.2* Math.random() - 1.1, 2.2 * Math.random() - 1.1, 2.2 * Math.random() - 1.1);
            }while(StarPosition.length() > 1 || StarPosition.POSZ <= 0);
            
            StarPosition.normalize();
            StarPosition.multiply(1000000000.0, StarPosition);
           
            Stars[i] = new Sprite3DM(new Line2D.Double(0, 0, 0, 0), StarPosition, Color.white);
           
            
        }
    }
    Container ContentArea;
    String LevelPath;
    int Index = -1;;
    GameState ExitState;
    public FlightState(FlightWindow F, String DataPath, int I, GameState ES){
        
        Base = F;
        LevelPath = DataPath;
        Index = I;
        ExitState = ES;
        //BASIC INITIALIZATION
        
        Stopwatch = new Timer((int)(10), this);
        Stopwatch.stop();
        //SET CONTENT PANE
      
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
       
        Area = F.Area;
 
        Area.setBounds(0, 0, X, Y);
     //   Area.setBounds(ScreenSize.width / 2 - X / 2, ScreenSize.height / 2 - Y / 2, X, Y);
        Area.init(BufferedImage.TYPE_BYTE_INDEXED);
        //System.out.println(this.getIgnoreRepaint());
      
        Area.Painter.setClip(0, 0, Area.W, Area.H);
        
        init();
 
        //this.pack();   
        
    }

            
    public void togglePlayerNav(){
        if(Player.DecisionTarget != null && Player.DecisionTarget.Exists){
            Player.DecisionTarget = NavPoints.ps((NavPoints.indexOf(Player.DecisionTarget) + 1) % NavPoints.size());
        }else{
            Player.DecisionTarget = NavPoints.ps(0);
        }
    }
    public void togglePlayerTarget(int Side){
        int Count = Vehicles.size()+1;
        do{
            if(Player.DecisionTarget != null && Player.DecisionTarget.Exists){
                Player.DecisionTarget = Vehicles.ve((Vehicles.indexOf(Player.DecisionTarget) + 1) % Vehicles.size());                    
                
            }else{
                Player.DecisionTarget = Vehicles.ve(0);
                
            }
            Count--;
            if(Player.DecisionTarget.Side == Player.Side && !(Player.DecisionTarget instanceof Craft)){
                continue;
            }
        }while((Player.DecisionTarget.Side != Side || Player.DecisionTarget.Form.Center.distanceTo(Player.Form.Center) > TargetRange) && Count > 0);
        if(Count == 0){
            Player.DecisionTarget = null;
        }
        
    }
    int TargetRange = 800000;
    public void nearestCrosshairTarget(){
        double Distance = TargetRange;
        Point2D.Double P = new Point2D.Double();
        Point3DM P3 = new Point3DM(0, 0, 0);
        for(int i = 0; i < Vehicles.size(); i++){
            Vehicle V = Vehicles.ve(i);
            if(V.Form.Center.subtract(View.POS).dotProduct(View.LookVector) < 0){
                continue;
            }
            V.Form.Center.center(View, P3);
            P3.toScreen(View, P);
            if(GUI.pythagoras(P.x - X /2, P.y - Y / 2) < 15){
                
                if(V.Form.Center.distanceTo(Player.Form.Center) < Distance && V != Player){
                    Distance = V.Form.Center.distanceTo(Player.Form.Center);
                    Player.DecisionTarget = V;
                }
            }
        }
    }
    public void nearestEnemyTarget(){
        int Count = Vehicles.size() + 1;
        double Distance = TargetRange;
        for(int i = 0; i < Vehicles.size(); i++){
            if(Vehicles.ve(i).Side != Player.Side && Vehicles.ve(i).Form.Center.distanceTo(Player.Form.Center) < Distance){
                Distance = Vehicles.ve(i).Form.Center.distanceTo(Player.Form.Center);
                Player.DecisionTarget = Vehicles.ve(i);
            }
        }
    }
    public void orderScatter(){
        for(int i = 0; i < Vehicles.size(); i++){
            if(Vehicles.ve(i) instanceof Craft && Vehicles.ve(i).Side == Player.Side && Vehicles.ve(i) != null){
                ((Craft)Vehicles.ve(i)).orderBlank(0);
                TickerTape.addLine(Vehicles.ve(i).Name + ": Scattering");
            }
        }
    }
    public void orderCancel(){
        for(int i = 0; i < Vehicles.size(); i++){
            if(Vehicles.ve(i) instanceof Craft && Vehicles.ve(i).Side == Player.Side && Vehicles.ve(i) != null){
                ((Craft)Vehicles.ve(i)).orderBlank(-1);
                TickerTape.addLine(Vehicles.ve(i).Name + ": Disregarding Last Order");
            }
        }
    }
    public void orderFollow(){
        int k = 1;
        for(int i = 0; i < Vehicles.size(); i++){
            if(Vehicles.ve(i) instanceof Craft && Vehicles.ve(i).Side == Player.Side && Vehicles.ve(i) != null){
                ((Craft)Vehicles.ve(i)).orderVehicle(4, Player);
                TickerTape.addLine(Vehicles.ve(i).Name + ": Following " + Player.Name);
                
                if(k<0){k--; k/=-1;}else{k/=-1;};
                
            }
        }
    }
    public void orderReturn(){
        for(int i = 0; i < Vehicles.size(); i++){
            if(Vehicles.ve(i) instanceof Craft && Vehicles.ve(i).Side == Player.Side && Vehicles.ve(i) != null){
                ((Craft)Vehicles.ve(i)).orderPosition(2, Player.Form.Center);
                TickerTape.addLine(Vehicles.ve(i).Name + ": Returning to " + Player.Name);
            }
        }
    }
    public void orderHold(){
        for(int i = 0; i < Vehicles.size(); i++){
            if(Vehicles.ve(i) instanceof Craft && Vehicles.ve(i).Side == Player.Side && Vehicles.ve(i) != null){
                ((Craft)Vehicles.ve(i)).orderPosition(1, Vehicles.ve(i).Form.Center);
                TickerTape.addLine(Vehicles.ve(i).Name + ": Holding Position");
            }
        }
    }
    
    public void orderAttack(){
        for(int i = 0; i < Vehicles.size(); i++){
            if(Vehicles.ve(i) instanceof Craft && Vehicles.ve(i).Side == Player.Side && Vehicles.ve(i) != null){
                if(Player.DecisionTarget instanceof Vehicle && Player.DecisionTarget.Side != Player.Side && Player.DecisionTarget.Exists){
                
                    ((Craft)Vehicles.ve(i)).orderVehicle(4, (Vehicle)Player.DecisionTarget);
                    TickerTape.addLine(Vehicles.ve(i).Name + ": Attacking " + ((Vehicle)Player.DecisionTarget).Name);
                }else if(Player.DecisionTarget instanceof Position3DM){
                    ((Craft)Vehicles.ve(i)).orderPosition(3, Player.DecisionTarget.Form.Center);
                    TickerTape.addLine(Vehicles.ve(i).Name + ": Assaulting " + ((Position3DM)Player.DecisionTarget).Name);
                }
            }
            
        }
    }
    public boolean Paused = false;
    public void doStuff(){
    	for(int i = 0; i < Vehicles.size(); i++){
    		if(Vehicles.ve(i) instanceof Craft && Vehicles.ve(i).Side == Player.Side){
    			System.out.println(Arrays.toString(((Craft)Vehicles.ve(i)).Path.toArray()));
    		}	
    	}	
    }
    public void keyPressed(KeyEvent Event){
        switch(Event.getKeyCode()){
            case 112: orderFollow(); break; //F1
            case 113: orderReturn(); break; //F2
            case 114: orderHold(); break; //F3
            case 115: orderAttack(); break; //F4
            case 116: orderCancel(); break; //F5
            case 117: orderScatter(); break; //F6
            case 80: Paused = !Paused; break; //P
            case 65: TwistLeft = true; break; //A
            case 83: TwistUp = true; break; //S
            case 68: TwistRight = true; break; //D
            case 87: TwistForward = true; break; //W

            case 38: break;//up
            case 40: break;//down
            case 37: break;//left
            case 39: break;//right
            case 12: break; //numpad center
            
            case 16: Player.Afterburner = true; break;//LeftShift
            
            case 77: Base.addState(new MapState(this, Base, Area, false));
            
            case 69: togglePlayerTarget(2); playSound("BEEPPURE"); break; //E
            case 82: nearestEnemyTarget(); playSound("BEEPPURE"); break; //R
            case 84: nearestCrosshairTarget(); playSound("BEEPPURE"); break; //T
            case 70: togglePlayerTarget(0); playSound("BEEPPURE"); break; //F
            case 78: togglePlayerNav(); playSound("BEEPPURE"); break; //N
            
            case 67: if(Player.UseAI){Player.UseAI = false; Player.Afterburner = false;}else{Player.UseAI = true;} break; // C
            case 86:  break; //V
            case 88: doStuff(); break; //X
          
            case 27: openMenu(); break;
           
            case 32: SpaceDown = true; break; // 
            default: System.out.println(Event.getKeyCode());
            
        }
        
        
        switch(Event.getKeyCode()){
            case 192:{ // ` key
                Player.TargetThrottle = 0;
            }break;
            case 49: case 50: case 51: case 52: case 53: case 54: case 55: case 56: case 57:{ // number keys
                double A = (Event.getKeyChar() - 48) * 10.0 / 10;
                Player.TargetThrottle = (float)A;
            }break;
            case 48:{ // 0 key
                Player.TargetThrottle = 10;
            }break;
           
            
            
        } 
        
        //System.out.println(User.HAngle + "\t" + User.VAngle + "\t" + User.RAngle);
    }
    public void openMenu(){
    	
    	ExitState.Background = new BufferedImage(Area.Picture.getWidth(), Area.Picture.getHeight(), Area.Picture.getType());
    	Color C = new Color(0, 0, 0, 160);
    	Area.Painter.setColor(C);
    	Area.Painter.fillRect(0, 0, X, Y);
    	Area.Picture.copyData(ExitState.Background.getRaster());
    	
    	Base.addState(ExitState);
    	
    }
    public void exit(){
        
        Stopwatch.stop();
        Time.DiscountDelay = true;
    }
    public void enter(){
        Stopwatch.start();
    }
    public void keyReleased(KeyEvent Event){
        switch(Event.getKeyCode()){
            
            case 65: TwistLeft = !true; break; //A
            case 83: TwistUp = !true; break; //S
            case 68: TwistRight = !true; break; //D
            case 87: TwistForward = !true; break; //W
            case 81: Player.RudderLeft = !true; break; //Q
            case 69: Player.RudderRight = !true; break; //E
            
            case 16: Player.Afterburner = !true; break;//LeftShift
            
 //           case 45: ThrottleDown = !true; break; //-
//            case 61: ThrottleUp = !true; break; //=
            
            case 32: SpaceDown = !true; break; //
            
            //default: System.out.println(Event.getKeyCode());
            
            //case 27: System.exit(0); break;//ESC
        }

    }
 
    
    public void keyTyped(KeyEvent Event){
        
        
    }
    
    public Doodad createDebris(int Duration, Point3DM Position, double Momentum, Color Tint){
        int K = GUI.randomInt(0, 2);
        if(DoodadStore[K].size() == 0){
            char A = ' '; 
            switch(GUI.randomInt(1, 3)){
                case 1: A = 't'; break;
                case 2: A = 'p'; break;
                case 3: A = 'c'; break;
            }
            Point3DM RandomUnit = Utils3DM.getRandomUnit();
            Doodad Debris = new Doodad(1, A, Duration, Position.add(RandomUnit), RandomUnit.multiply((Math.random() + 1) / 2 * Momentum), Tint, Math.random() * 1.5 + 0.5);

            return Debris;
        }else{
            Doodad Debris = (Doodad)DoodadStore[K].lastElement();
            DoodadStore[K].removeLastElement();
            Debris.Duration.Current = Duration;
            Position.copyTo(Debris.Form.Center);
            Point3DM RandomUnit = Utils3DM.getRandomUnit();
            Debris.Form.Center.add(RandomUnit, Debris.Form.Center);
            Debris.Form.Tint = Tint;
            
            Debris.Momentum = Utils3DM.getRandomUnit();
            Debris.Momentum.multiply(Momentum, Debris.Momentum);
            return Debris;
        }
    }
    public void alignViewpoint(Craft Target){
          
        double Difference = GUI.angleDiff(CurrentDirection, 360 / DirectionCount * GUI.cap(0, Math.abs(Direction) - 1, DirectionCount - 1));

        if(Math.abs(Difference) < 360.0 / DirectionCount / 5){

            if(TwistLeft){
            //    Direction = Math.abs(Direction);
                Direction = ((Math.abs(Direction) + 1 + DirectionCount - 1) % DirectionCount + 1) * GUI.sign(Direction);

            }
            if(TwistRight){
             //  Direction = Math.abs(Direction);
               Direction = ((Math.abs(Direction) - 1 + DirectionCount - 1) % DirectionCount + 1) * GUI.sign(Direction);

            }
        }


        //
        if(Difference > 0){
            CurrentDirection += Math.min(Math.abs(Difference), TurnIncrement * 1.0 / FramesPerSecond);
        }
        if(Difference < 0){
            CurrentDirection -= Math.min(Math.abs(Difference), TurnIncrement * 1.0 / FramesPerSecond);
        }

        Difference = CurrentPitch - (Direction < 0 ? 45 : 0);
        if(Difference == 0){
            if(TwistUp){
                 Direction = -Direction;
            }
        }
        if(TwistForward){
            Direction = 1;
        }

        if(Difference > 0){
            CurrentPitch -= Math.min(Math.abs(Difference), TurnIncrement / 2 * 1.0 / FramesPerSecond);
        }
        if(Difference < 0){
            CurrentPitch += Math.min(Math.abs(Difference), TurnIncrement / 2 * 1.0 / FramesPerSecond);
        }
        //
      //  System.out.println(Direction);
        final Point3DM Movement = new Point3DM();
        Target.Form.Center.subtract(TowPoint, Movement);
        Movement.normalize();   
        Movement.multiply(-TowDistance, TowPoint);
        TowPoint.add(Target.Form.Center, TowPoint);
        Rotation3DM Rot = new Rotation3DM();
        Rotation3DM Pit = new Rotation3DM();
        Rot.rotateAround('z', Math.toRadians(CurrentDirection));
        Pit.rotateAround('y', Math.toRadians(CurrentPitch));
        View.Rotation = Target.Form.Rotation.copy();
        Rotation3DM.multiply(View.Rotation, Rot, View.Rotation);
        Rotation3DM.multiply(View.Rotation, Pit, View.Rotation);
        View.setLookVector();
        if(true)
        {
            switch(Facing){
                case 0:{
                    TowPoint.copyTo(View.POS);

                    View.POS.subtract(Target.Form.Center, View.POS);
                    Target.Form.Rotation.inverseTransform(View.POS, View.POS);
                    Pit.transform(View.POS, View.POS);
                    Rot.transform(View.POS, View.POS);

                    Target.Form.Rotation.transform(View.POS, View.POS);
                    View.POS.add(Target.Form.Center, View.POS);
                    }
                    break;
                case 1:{



                    Target.Form.Center.copyTo(View.POS);


                    }
                    break;




            }




              //  View.Rotation.inverse(View.InverseRotation);
                View.setLookVector();
        }
    }
    public void initViewpoint(Craft Target){
        
        View.POS = new Point3DM(-TowDistance, 0, 0);
        TowPoint = new Point3DM(-TowDistance, 0, 0);
        
        
        Player.Form.Rotation.copyTo(View.Rotation);
        //View.Rotation = Player.Form.Rotation;
        
        //View.Rotation.inverse(View.InverseRotation);
        
        View.Rotation.transform(View.POS, View.POS); 
        View.Rotation.transform(TowPoint, TowPoint); 
        View.POS.add(Target.Form.Center, View.POS);
        TowPoint.add(Target.Form.Center, TowPoint);
        
        View.setLookVector();
        
    }
    public void setMouseControls(){
        
        double MX = -(MouseX - Area.W / 2.0) / (Area.W / 2);
        double MY = (MouseY - Area.H / 2.0) / (Area.H / 2);
      //  System.out.println(MX + "\t" + MY);
        Player.MouseVertical = MY;
        Player.MouseHorizontal = MX;
        
    }
    public void throttleControls(){
        if(ThrottleUp){
            Player.Throttle = ((float)(Player.Throttle + 5.0 / FramesPerSecond));
        }
        if(ThrottleDown){
            Player.Throttle = ((float)(Player.Throttle - 5.0 / FramesPerSecond));
        }
    }
    
    public void actionPerformed(ActionEvent Event){

     //   System.out.println(Utils3DM.countPoints());
     //   System.out.println(Utils3DM.statPoints());
        //Count Frames
        FrameCounter++;
        Time.doTime();
        FramesPerSecond = Time.FramesPerSecond;
        if(!Paused)UnPausedTime += 1 / FramesPerSecond;
        
        //Black out Canvas
        Area.Painter = Area.Picture.createGraphics();
        Area.Painter.setColor(Color.black);
        Area.Painter.fillRect(0, 0, Area.W, Area.H);
        
      
        //Move Vehicle Store
        
        for(int i = 0; i < VehicleStore.size(); i++){
            Vehicle V = VehicleStore.ve(i);
            V.Hitpoints.increment(1.0 / FramesPerSecond);
            if(V.Hitpoints.Current > V.Hitpoints.BaseIncrement){
                V.Hitpoints.Current = V.Hitpoints.Max;
                VehicleStore.remove(V);
                Vehicles.add(V);
                System.out.println("Vehicle Activated: " + VehicleStore.size() + " left");
            }
            
        }
        //Move Crafts
        for(int i = 0; i < Vehicles.size(); i++){
            if(!Paused){
                Vehicles.ve(i).move();
            }
        }
        
        //Move Projectiles
        for(int i = Projectiles.size() - 1; i >= 0; i--){
            if(!Paused){
                Projectiles.pr(i).move();
            }
            
        }
        
        //Move Sprites
        for(int i = Sprites.size() - 1; i >= 0; i--){
            if(!Paused){
                Sprites.ts(i).move();
            }
            if(Sprites.ts(i).Duration.checkBounds() < 0){
                Sprites.removeElementAt(i);
            }
        }
        
        //Move Doodads
        for(int i = Doodads.size() - 1; i >= 0; i--){
            if(!Paused){
                Doodads.d(i).move();
            }
            if(Doodads.d(i).Duration.checkBounds() < 0){
                Doodads.removeElementAt(i);
            }
        }
        
        //Calculate 3D Points
        for(int i = Planes.size() - 1; i >= 0; i--){
            if(!Planes.pl(i).Form.Static){
                Planes.pl(i).Form.calcPoints();
            }
        }
        for(int i = 0; i < Vehicles.size(); i++){
            if(!Vehicles.ve(i).Form.Static){
                Vehicles.ve(i).Form.calcPoints();
            }
            if(!Paused){
                if(Vehicles.ve(i) instanceof Craft){
                    Vehicles.cr(i).updateTrails();
                }
            }
            if(Vehicles.ve(i) == Player){
                
            }
            
        }
        
        for(int i = Projectiles.size() - 1; i >= 0; i--){
            if(!Projectiles.pr(i).Form.Static){
                Projectiles.pr(i).Form.calcPoints();
            }
        }
        for(int i = Doodads.size() - 1; i >= 0; i--){
            if(!Doodads.d(i).Form.Static){
                Doodads.d(i).Form.calcPoints();
            }
        }
        
        //Move Trails
    //   System.out.println(Trails.size() + "\t" + Projectiles.size());
        for(int i = Trails.size() - 1; i >= 0; i--){
            if(!Paused){
                Trails.tr(i).chase((float)FramesPerSecond);
                if(Trails.tr(i).finished()){
                    Trail T = Trails.tr(i);
                    if(T.Owner instanceof Projectile){
                        Projectile P = (Projectile)T.Owner;
                        P.Type.Store.add(P);
                    }
                    
                    if(T.Form.PointCount == 3){
                        
                        ShortTrailStore.add(T);
                    }else if(T.Form.PointCount == 8){
                        LongTrailStore.add(T);
                    }
                    Trails.removeElementAt(i);
                    
                }
            }
        }
        
        
        //Accept User Input
        if(!Player.UseAI){
            setMouseControls();
            throttleControls(); 
            Player.manualFire();
        }
        
        //Do AI Controls
        for(int i = 0; i < Vehicles.size(); i++){
            
            if(Vehicles.ve(i).UseAI){
                
                Vehicles.ve(i).doAI();
            }

        }
        //Align Camera to Player
        alignViewpoint(Player);
        
        //Center Grid on Player
        shiftGrid(Player.Form.Center);
        Grid.calcPoints();

        //CheckCollision
        
        Point3DM ImpactSpot;
        for(int i = Projectiles.size() - 1; i >= 0; i--){
            if(Projectiles.size() <= i){continue;}
            Projectile P = Projectiles.pr(i);
            for(int j = 0; j < Vehicles.size(); j++){
                Vehicle V = Vehicles.ve(j);
                ImpactSpot = P.checkCollision(V);
                if(ImpactSpot != null){
                
                    P.doCollision(V, ImpactSpot);
                }
            }
            Vector PossiblePlanes = PlaneTable.testPoint(P.Form.Center.POSX, P.Form.Center.POSY, P.Form.Center.distanceTo(P.LastPosition));
            //Object[] PossiblePlanes = PlaneTable.testPoint(P.Form.Center.POSX, P.Form.Center.POSY);
            for(int j = 0; j < PossiblePlanes.size(); j++){
                Plane Pl = (Plane)PossiblePlanes.get(j);
                ImpactSpot = P.checkCollision(Pl);
                if(ImpactSpot != null){

                    P.doCollision(Pl, ImpactSpot);
                }
                
            
            }
        }
        Point3DM[] TempPoints = {new Point3DM(0, 0, 0), new Point3DM(0, 0, 0), new Point3DM(0, 0, 0)};
        for(int i = Doodads.size() - 1; i >= 0; i--){
            Doodad D = Doodads.d(i);
            Vector PossiblePlanes = PlaneTable.testPoint(D.Form.Center.POSX, D.Form.Center.POSY);
            
            for(int j = 0; j < PossiblePlanes.size(); j++){

                    
                ImpactSpot = ((Plane)PossiblePlanes.get(j)).checkCollision(Doodads.d(i));
                if(ImpactSpot != null){
                    ((Plane)PossiblePlanes.get(j)).doCollision(Doodads.d(i), ImpactSpot);
                }
            
            }
        }
        
        for(int j = 0; j < Vehicles.size(); j++){
            Vehicle V = Vehicles.ve(j);
            
            Vector PossiblePlanes = PlaneTable.testPoint(V.Form.Center.POSX, V.Form.Center.POSY, V.CollisionRadius);

                
            for(int i = 0; i < PossiblePlanes.size(); i++){

                ImpactSpot = ((Plane)PossiblePlanes.get(i)).checkCollision(Vehicles.ve(j));
                if(ImpactSpot != null){   
                    ((Plane)PossiblePlanes.get(i)).doCollision(Vehicles.ve(j), ImpactSpot);

                }
            }
            
        }
        
        //Print Everything
        
        print(); 
        
        //Check Mission Status
        
        EndCounter.increment(EndCounter.BaseIncrement * 1.0 / FramesPerSecond);
        if(EndCounter.checkBounds() == 0){
            System.out.println("Ending");
            double R = EndCounter.Current / EndCounter.Max;
            Area A = new Area(new Rectangle(0, 0, X, Y));
            A.subtract(new Area(new Rectangle2D.Double(R * X/2, R * Y/2, (1-R) * X, (1-R) * Y)));
            Area.Painter.setClip(A);
            Area.Painter.setColor(Color.black);
            Area.Painter.fillRect(0, 0, X, Y);
            
            Area.printLine(X/2, Y/2, "Mission " + Result, 40, Color.green, 'c');
            Area.Painter.setClip(null);
        }else if(EndCounter.checkBounds() > 0){
            Area.Painter.setColor(Color.black);
            Area.Painter.fillRect(0, 0, X, Y);
            Area.printLine(X/2, Y/2, "Mission " + Result, 40, Color.green, 'c');
        }
        checkMissionAccomplished();
        //Print Canvas to Screen
     //   System.out.println(MouseX + "\t" + MouseY);
        
                            
        Area.paintComponent(null); 
    }
    public void print(){
        //Background Printing
        for(int i = 0; i < Stars.length; i++){
 
            Stars[i].printNow(Area.Painter, View);
        }
        

        //3D Stuff Printing

        
        Grid.printNow(Area.Painter, View);
        Planes.pl(0).Form.printNow(Area.Painter, View);
        for(int i = 0; i < Vehicles.size(); i++){
            Vehicles.ve(i).print(View);

        }
        
        for(int i = 0; i < Trails.size(); i++){
          
            Trails.tr(i).print(View);
            
        }
     
        for(int i = Planes.size() - 1; i > 0; i--){
            Planes.pl(i).print(View);
  
        }
        
        for(int i = 0; i < Projectiles.size(); i++){
            Projectiles.pr(i).print(View);
           
        }
        
        for(int i = 0; i < Sprites.size(); i++){
            Sprites.ts(i).print(View);
        }
        
        for(int i = 0; i < Doodads.size(); i++){
            Doodads.d(i).print(View);

        }
        Planes.pl(0).Form.printNow(Area.Painter, View);
        View.printAll();
        //GUI Stuff Printing

        
        drawCursor();
        drawGUI();
        drawBorder();
        Area.printLine(100, 100, ((int)(Time.DelayList.average() * 10) / 10.0) + "", 12, Color.green, 'c');
    }
    boolean Finished = false;
    String Result;
    Counter EndCounter = new Counter(0, 5, 0);
    public void prepareToEnd(){
        //WinCondition.disable();
       // LoseCondition.disable();
        
        Finished = true;
        EndCounter.setBaseIncrement(1);
    }
    public void checkMissionAccomplished(){
     //   System.out.println(EndCounter);
        if(WinCondition.checkComplete() == 2){
            prepareToEnd();
            Result = "Accomplished";
            TickerTape.addLine("Mission Accomplished: " + WinCondition.getText());
        }
        if(WinCondition.checkComplete() > 0){
            System.out.println("WIN");
        }
        if(LoseCondition.checkComplete() == 2){
            Result = "Failed";
            prepareToEnd();
            TickerTape.addLine("Mission Failed: " + LoseCondition.getText());
        }
        if(LoseCondition.checkComplete() > 0){
            System.out.println("LOSE");
        }
    }
    public void GUITest(){
    	if(!Player.UseAI && Player.DecisionTarget != null){
    		
	    //	Player.Path = Player.find(Player.Form.Center, Player.DecisionTarget.Form.Center, 1000000000, new HashSet<Plane>(), 9); 
    //		if(Player.Path != null){
   // 			Player.Path.insertElementAt(Player.Form.Center, 0);
   // 			Player.process(Player.Path);
   // 		}
    		
      		
      	}
      	for(int i = 0; i < Vehicles.size(); i++){
      		Vehicle V = Vehicles.ve(i);
      		if(V instanceof Craft && ((Craft)V).Path != null && V.Side == Player.Side){
      			View.print(((Craft)V).Path, Color.cyan);
      		}
      	}
	                
    }
    public void GUIBase(){
        
        
        //Crosshairs
        Area.Painter.setColor(Color.green);
        Area.Painter.setStroke(Area.DashedStroke);
        
        int CenterGap = 20;
        Area.Painter.drawLine(Area.W / 2, 0, Area.W / 2, Area.H / 2 - CenterGap);
        Area.Painter.drawLine(Area.W / 2, Area.H / 2 + CenterGap, Area.W / 2, Area.H);
        
        Area.Painter.drawLine(0-2 , Area.H / 2, Area.W / 2 - CenterGap, Area.H / 2);
        Area.Painter.drawLine(Area.W / 2 + CenterGap, Area.H / 2, Area.W, Area.H / 2);
        
        Area.Painter.setStroke(Area.NormalStroke);
        CenterGap /= 2;
        Area.Painter.drawLine(Area.W / 2 - CenterGap, Area.H / 2 - CenterGap, Area.W / 2 - CenterGap / 2, Area.H / 2 - CenterGap / 2);
        Area.Painter.drawLine(Area.W / 2 + CenterGap, Area.H / 2 + CenterGap, Area.W / 2 + CenterGap / 2, Area.H / 2 + CenterGap / 2);
        Area.Painter.drawLine(Area.W / 2 + CenterGap, Area.H / 2 - CenterGap, Area.W / 2 + CenterGap / 2, Area.H / 2 - CenterGap / 2);
        Area.Painter.drawLine(Area.W / 2 - CenterGap, Area.H / 2 + CenterGap, Area.W / 2 - CenterGap / 2, Area.H / 2 + CenterGap / 2);
		
    }
    public void GUISpeed(){
        //Speed & Throttle Indicators
        Area.Painter.drawRect((int)(0.9 * Area.W), (int)(0.5 * Area.H) - 75, 16, 150);
        Area.Painter.drawRect((int)(0.9 * Area.W), (int)(0.5 * Area.H + 75 - Player.getThrust() / Player.Thrust * 10 * 15), 16, (int)(Player.getThrust() / Player.Thrust * 10 * 15));
        Area.Painter.drawLine((int)(0.9 * Area.W + 3), (int)(0.5 * Area.H + Math.min(75, 75 - Player.TargetThrottle * 15 + 3)), (int)(0.9 * Area.W + 13), (int)(0.5 * Area.H + Math.min(75, 75 - Player.TargetThrottle * 15 + 3)));
        
        //Area.Painter.drawLine((int)(0.9 * X + 3), (int)(0.5 * Y + Math.min(75, 75 - Player.getThrust() / Player.Thrust * 10 * 15 + 3)), (int)(0.9 * X + 3), (int)(0.5 * Y + 75));
        if(Player.Afterburner){
            Area.Painter.setColor(Color.red);
        }
        Area.Painter.drawLine((int)(0.9 * Area.W + 3), (int)(0.5 * Area.H + Math.min(75, 75 - Player.getThrust() / Player.Thrust * 10 * 15 * Player.AfterburnerTimer.Current / Player.AfterburnerTimer.Max + 3)), (int)(0.9 * Area.W + 3), (int)(0.5 * Area.H + 75));
        Area.Painter.setColor(Color.green);
        
        Area.printLine((int)(0.9 * Area.W + 8), (int)(0.5 * Area.H + 90), Math.round(Player.Momentum.length()) + "m/s", 12, Color.green, 'c');
        Area.printLine((int)(0.9 * Area.W + 20), (int)(0.5 * Area.H + 15), Math.round(Player.Acceleration.length()) + "m/s/s", 12, Color.green, 'l');
    }
    public void GUIAltitude(){
        //Altitude, Vert Momentum, Vert Acceleration
        Area.Painter.drawLine((int)(0.1 * Area.W - 8), (int)(0.5 * Area.H) - 75, (int)(0.1 * Area.W - 8), (int)(0.5 * Area.H) + 75);
        Area.Painter.drawLine((int)(0.1 * Area.W - 8), (int)(0.5 * Area.H) - 75, (int)(0.1 * Area.W - 16), (int)(0.5 * Area.H - 75 + 8));
        Area.Painter.drawLine((int)(0.1 * Area.W - 8), (int)(0.5 * Area.H) - 75, (int)(0.1 * Area.W - 00), (int)(0.5 * Area.H - 75 + 8));
        
        Area.printLine((int)(0.1 * Area.W - 8), (int)(0.5 * Area.H + 90), Math.round(Player.Form.Center.POSZ) + "m", 12, Color.green, 'c');
        Area.printLine((int)(0.1 * Area.W - 0), (int)(0.5 * Area.H + 15), Math.round(Player.Momentum.POSZ) + "m/s", 12, Color.green, 'l');
        Area.printLine((int)(0.1 * Area.W - 0), (int)(0.5 * Area.H - 15), Math.round(Player.Acceleration.POSZ) + "m/s/s", 12, Color.green, 'l');
        
    }
    public void GUILift(){
        //AoA & Lift Indicator
        
        Area.Painter.drawLine((int)(0.1 * Area.W - 0 - 30), (int)(0.68 * Area.H), (int)(0.1 * Area.W - 0 + 30), (int)(0.68 * Area.H));
        if(Player.Momentum.length() > 1){
            Area.Painter.drawLine((int)(0.1 * Area.W + 30 - 60 * Math.cos(Player.AngleOfAttack * -2)), (int)(0.68 * Area.H + 60 * Math.sin(Player.AngleOfAttack * -2)), (int)(0.1 * Area.W + 30), (int)(0.68 * Area.H));
            Area.printLine((int)(0.1 * Area.W - 30 - 5), (int)(0.68 * Area.H), "" + Math.round(10 * Math.toDegrees(Player.AngleOfAttack)) / 10.0, 12, Color.green, 'r');
            int Angle = (int)(Math.toDegrees(Player.AngleOfAttack * -2));
            
            if(Angle < 0){Angle++;}
            Area.Painter.drawArc((int)(0.1 * Area.W + 30 - 40), (int)(0.68 * Area.H - 40), 80, 80, 180, Angle);
        }else{
            Area.printLine((int)(0.1 * Area.W - 30 - 5), (int)(0.68 * Area.H), "0.0", 12, Color.green, 'r');
        }
        if(Player.Lift != 0){
            Area.Painter.drawLine((int)(0.1 * Area.W + 30 + 5), (int)(0.68 * Area.H - Player.Lift / Player.Mass * 0.3), (int)(0.1 * Area.W + 30 + 5), (int)(0.68 * Area.H + Player.Lift / Player.Mass * 0.3));
            Area.Painter.drawLine((int)(0.1 * Area.W + 30 + 5), (int)(0.68 * Area.H - Player.Lift / Player.Mass * 0.3), (int)(0.1 * Area.W + 30 + 5 + 5), (int)(0.68 * Area.H - Player.Lift / Player.Mass * 0.3 + (Player.Lift >= 0 ? 5 : -5)));
            Area.Painter.drawLine((int)(0.1 * Area.W + 30 + 5), (int)(0.68 * Area.H - Player.Lift / Player.Mass * 0.3), (int)(0.1 * Area.W + 30 + 5 - 5), (int)(0.68 * Area.H - Player.Lift / Player.Mass * 0.3 + (Player.Lift >= 0 ? 5 : -5)));
        }
    }
    public void GUITargeting(){
        //Highlight Target
        
        int PointerLength = 50;
        if(Player.DecisionTarget != null && Player.DecisionTarget.Exists){
            //Check if Point needs to be pointed too
            final Point3DM Target = new Point3DM();
            final Point3DM ObserverVector = new Point3DM();
            Player.DecisionTarget.Form.Center.copyTo(Target);
            Target.subtract(View.POS, ObserverVector);
            boolean Behind = ObserverVector.dotProduct(View.LookVector) < 0;
            Target.center(View, Target);
            Point2D.Double FlatPoint = new Point2D.Double();
            Target.toScreen(View, FlatPoint);
            
            //Extend Point to edge of screen
            if(!(FlatPoint.x < Area.W && FlatPoint.y < Area.H && FlatPoint.x > 0 && FlatPoint.y > 0 && !Behind)){
                FlatPoint.x -= Area.W / 2; FlatPoint.y -= Area.H / 2;
                if(Behind){
                    FlatPoint.x = -FlatPoint.x;
                    FlatPoint.y = -FlatPoint.y;
                }
                double Grad = FlatPoint.y / FlatPoint.x;
                double Gradi = FlatPoint.x / FlatPoint.y;
                FlatPoint.x *= 10000; FlatPoint.y *= 10000;
             //   System.out.println(FlatPoint);
                if(FlatPoint.x < -Area.W / 2){
                    FlatPoint.y = FlatPoint.y - (FlatPoint.x + Area.W / 2) / Gradi;
                    FlatPoint.x = -Area.W / 2;
                }else if(FlatPoint.x > Area.W / 2){
                    FlatPoint.y = FlatPoint.y - (FlatPoint.x - Area.W / 2) / Gradi;
                    FlatPoint.x = Area.W / 2;
                }
             //   System.out.println(FlatPoint);
                if(FlatPoint.y < -Area.H / 2){
                    FlatPoint.x = FlatPoint.x - (FlatPoint.y + Area.H / 2) * Gradi;
                    FlatPoint.y = -Area.H / 2;
                }else if(FlatPoint.y > Area.H / 2){
                    FlatPoint.x = FlatPoint.x - (FlatPoint.y - Area.H / 2) * Gradi;
                    FlatPoint.y = Area.H / 2;
                }
                double Angle = Math.atan2(FlatPoint.y, FlatPoint.x);
                
                double PointerWidth = Math.toRadians(15);
                Point2D.Double PointA = new Point2D.Double((FlatPoint.x + Area.W / 2), (FlatPoint.y + Area.H / 2));
                Point2D.Double PointB = new Point2D.Double(PointA.x - PointerLength * Math.cos(Angle + PointerWidth), PointA.y - PointerLength * Math.sin(Angle + PointerWidth));
                Point2D.Double PointC = new Point2D.Double(PointA.x - PointerLength * Math.cos(Angle - PointerWidth), PointA.y - PointerLength * Math.sin(Angle - PointerWidth));
                Area.Painter.drawLine((int)PointA.x, (int)PointA.y, (int)PointB.x, (int)PointB.y);
                Area.Painter.drawLine((int)PointA.x, (int)PointA.y, (int)PointC.x, (int)PointC.y);
                Area.Painter.drawLine((int)PointB.x, (int)PointB.y, (int)PointC.x, (int)PointC.y);
            //    Area.Painter.fillRect((int)FlatPoint.x + X / 2 - 50, (int)FlatPoint.y + Y / 2 - 50, 100, 100);
            }else{
                //Circle Target
                Area.Painter.drawOval((int)(FlatPoint.x - PointerLength / 4), (int)(FlatPoint.y - PointerLength / 4), PointerLength / 2, PointerLength / 2);   
            }
        }
        //Draw Lead
        
        if(Player.DecisionTarget != null && Player.DecisionTarget.Exists && Player.SelectedGun != null && Player.DecisionTarget.Momentum.length() > 0 && Player.DecisionTarget != Player){
            final Point3DM Target = new Point3DM();;
            final Point3DM TransformedTarget = new Point3DM();
            final Point3DM ObserverVector = new Point3DM();
            final Point3DM JudgedTarget = new Point3DM();
            
            Point2D.Double FlatPoint = new Point2D.Double();
            
            Player.DecisionTarget.Form.Center.copyTo(Target);
            
            Point3DM Lead = Player.judgeShot(Player.DecisionTarget, Player.SelectedGun.Shot.Momentum.POSX);
            
            if(Lead != null)Lead.copyTo(Target);
            Target.add(Player.DecisionTarget.Form.Center, Target);
            Target.add(View.POS, Target);
            Target.subtract(Player.Form.Center, Target);
            
            Target.subtract(View.POS, ObserverVector);
            
            boolean Behind = ObserverVector.dotProduct(View.LookVector) < 0;
            
            if(!Behind && !Target.equals(Player.DecisionTarget.Form.Center)){
                Target.center(View, TransformedTarget);
                FlatPoint.setLocation(0, 0);
                TransformedTarget.toScreen(View, FlatPoint);
                Area.Painter.drawOval((int)FlatPoint.x - 8, (int)FlatPoint.y - 8, 16, 16);
            }
            
            //AITarget
            
        }
        if(Player.UseAI){
            
            final Point3DM Target = new Point3DM();
            final Point3DM TransformedTarget = new Point3DM();
            final Point3DM ObserverVector = new Point3DM();
            Point2D.Double FlatPoint = new Point2D.Double();
            
            Player.ControlTarget.subtract(View.POS, ObserverVector);
            boolean Behind = ObserverVector.dotProduct(View.LookVector) < 0;

            if(!Behind){
                Player.ControlTarget.center(View, TransformedTarget);
                FlatPoint.setLocation(0, 0);
                TransformedTarget.toScreen(View, FlatPoint);
                Area.Painter.drawRect((int)FlatPoint.x - 6, (int)FlatPoint.y - 6, 12, 12);
                Area.printLine((int)FlatPoint.x, (int)FlatPoint.y + 1, Character.toUpperCase(Player.Evading && Player.State != 't' ? 'e' : Player.State) + "", 10, Player.State == 'a' && Player.AState == 'a' ? Color.red : Color.green, 'c');
                Area.Painter.setColor(Color.green);
            }

        }
    }
    public Color getColor(double Percent){
        int Green = (int)Math.max(0, Math.min(255, 2 * 255 * Percent));
        int Red = (int)Math.max(0, Math.min(255, 510 + (-2 * 255 * Percent)));
        Color HPColor = new Color(Red, Green, 55);
        return HPColor;
    }
    public void GUIHitpoints(){
        int Length = 100;
        int Width = 12;
        int Gap = 5;
       
      //  System.out.println(Player.Hitpoints);
        
        Area.Painter.drawRect((int)(0.9 * Area.W - Length + 15), (int)(0.75 * Area.H + -2 * (Width + Gap)), Length, Width);
        Area.Painter.drawRect((int)(0.9 * Area.W - Length + 15), (int)(0.75 * Area.H + -2 * (Width + Gap)), (int)(Length * Player.Hitpoints.Current / Player.Hitpoints.Max), Width);
        Area.Painter.setColor(getColor(Player.Hitpoints.Current / Player.Hitpoints.Max));
        Area.Painter.drawRect((int)(0.9 * Area.W - Length + 15) + (int)(Length * Player.Hitpoints.Current / Player.Hitpoints.Max) - 2, (int)(0.75 * Area.H + -2 * (Width + Gap)) + 2, 0, Width - 4);
        Area.printLine((int)(0.9 * Area.W - Length + 14 + Length / 2), (int)(0.75 * Area.H + (-2 + 0.5) * (Width + Gap)) - 1, "Hitpoints", 10, Area.Painter.getColor(), 'c');
    }
    public void GUIWeapons(){
        //List Weapons public void printLine(int INTX, int INTY, String Text, int Font, Color FontColor, char Alignment){
        int Length = 100;
        int Width = 12;
        int Gap = 5;
        for(int i = 0; i < Player.Guns.size(); i++){
            Area.Painter.drawRect((int)(0.9 * Area.W - Length + 15), (int)(0.75 * Area.H + i * (Width + Gap)), Length, Width);
            Area.Painter.drawRect((int)(0.9 * Area.W - Length + 15), (int)(0.75 * Area.H + i * (Width + Gap)), (int)(Length * Player.Guns.gun(i).ReloadTimer.Current / Player.Guns.gun(i).ReloadTimer.Max), Width);
            Area.Painter.drawRect((int)(0.9 * Area.W - Length + 15) + (int)(Length * Player.Guns.gun(i).ReloadTimer.Current / Player.Guns.gun(i).ReloadTimer.Max) - 2, (int)(0.75 * Area.H + i * (Width + Gap)) + 2, 0, Width - 4);
            Area.printLine((int)(0.9 * Area.W - Length + 15 + 1), (int)(0.75 * Area.H + (i + 0.5) * (Width + Gap) - 1), Player.Guns.gun(i).Name, 10, Color.green, 'l');
            if(Player.Guns.gun(i) == Player.SelectedGun){
                Area.Painter.drawRect((int)(0.9 * Area.W - Length + 15 - 2), (int)(0.75 * Area.H + i * (Width + Gap) - 2), Length + 4, Width + 4);
            }
        }
    }
    
    
    Point Middle;
    int Width = 72;
    Point3DM Temp1 = new Point3DM(0, 0, 0);
    Point3DM Temp2 = new Point3DM(0, 0, 0);
    Point3DM Temp3 = new Point3DM(0, 0, 0);
    AffineTransform RollRot = new AffineTransform();
    GeneralPath GroundShape = new GeneralPath();
    double TranslateConstant = (Width) / (Math.PI / 2);
    int GapWidth = 5;
    Ellipse2D Circle = new Ellipse2D.Float();
    String[] Directions = {"N", "|", "NE", "|", "E", "|", "SE", "|", "S", "|", "SW", "|", "W", "|", "NW", "|"};
    public void GUIHorizon(){
        
        
        
        
        Circle.setFrame(Middle.x - Width / 2, Middle.y - Width / 2, Width, Width);
        //Area.Painter.drawRect(Middle.x - Width / 2, Middle.y - Width / 2, Width, Width);
        Area.Painter.draw(Circle);
        Area.Painter.drawLine(Middle.x - 4, Middle.y + 4, Middle.x + 4, Middle.y - 4);
        Area.Painter.drawLine(Middle.x + 4, Middle.y + 4, Middle.x - 4, Middle.y - 4);
        Player.Form.Rotation.getOriginalAxis('x', Temp1);
        double Angle = Math.PI / 2 - Math.acos(GUI.trigCap(Temp1.dotProduct(Utils3DM.Vectork)));
        double Pitch = Angle;
        
        Temp1.crossProduct(Utils3DM.Vectork, Temp2);
        Temp2.crossProduct(Temp1, Temp3);
        Temp3.normalize();
        Player.Form.Rotation.getOriginalAxis('z', Temp1);
        Angle = Math.atan2(GUI.trigCap(Temp1.dotProduct(Temp2)), GUI.trigCap(Temp1.dotProduct(Temp3)));
        double Roll = Angle;
      //  System.out.println(Math.toDegrees(Pitch) + "\t" + Math.toDegrees(Roll));
        Area.ClippedPainter.setClip(Circle);
      
        RollRot.setToTranslation(0, 0);
        
        RollRot.rotate(Roll, Middle.x, Middle.y);
        RollRot.translate(0, TranslateConstant * Pitch);
        RollRot.translate(Middle.x, Middle.y);
            
        
        Area.ClippedPainter.draw(RollRot.createTransformedShape(GroundShape));
        Area.Painter.drawLine(Middle.x + Width / 2, Middle.y, Middle.x + Width / 2 + 3, Middle.y);
        Area.Painter.drawLine(Middle.x - Width / 2, Middle.y, Middle.x - Width / 2 - 3, Middle.y);
        Area.printLine(Middle.x + Width / 2 + 5, Middle.y, "" + ((int)(Math.toDegrees(Pitch) * 10)) / 10.0, 12, Color.green, 'l');
        Area.Painter.drawLine(Middle.x, Middle.y - Width / 2, Middle.x, Middle.y - Width / 2 - 3);
        Area.Painter.drawLine(Middle.x, Middle.y + Width / 2, Middle.x, Middle.y + Width / 2 + 3);
        
        
        Point3DM Forward = new Point3DM();
        Player.Form.Rotation.getOriginalAxis('x', Forward);
        double Theta = Math.toDegrees(Math.atan2(Forward.dotProduct(Utils3DM.Vectori), Forward.dotProduct(Utils3DM.Vectorj)));
        int CompassDegreeWidth = 150;
        int CompassPixelWidth = 90;
        
        for(int i = 0; i < Directions.length; i++){
            int pX = (int)((360.0 / Directions.length * i - Theta) / CompassDegreeWidth * CompassPixelWidth);
            int mX = (int)((360.0 / Directions.length * (i - Directions.length) - Theta) / CompassDegreeWidth * CompassPixelWidth);
         //  System.out.println(pX);
            if(Math.abs(pX) < CompassPixelWidth/2){
                Area.printLine(Middle.x + pX, Middle.y - Width / 2 - 5, Directions[i], 12, Color.green, 'b');
                Area.Painter.drawLine(Middle.x + pX, Middle.y - Width / 2 - 3, Middle.x + pX, Middle.y - Width / 2 - 1);
            }else if(Math.abs(mX) < CompassPixelWidth/2){
                Area.printLine(Middle.x + mX, Middle.y - Width / 2 - 5, Directions[i], 12, Color.green, 'b');
                Area.Painter.drawLine(Middle.x + mX, Middle.y - Width / 2 - 3, Middle.x + mX, Middle.y - Width / 2 - 1);
            }
        }

       // Area.printLine(Middle.x, Middle.y - Width / 2 - 5, "" + ((int)(Theta * 10)) / 10.0, 12, Color.green, 'c');
    }
    ObserverM GUIView;
    public void GUIData(){
        
        
      //  GUIView = View.copy();
        View.copyStatsTo(GUIView);
        Area.Painter.setColor(Color.green);
        int Length = 100;
        int Height = 200;
        int Width = 12;
        int Gap = 5;
        int pX = (int)(0.9 * Area.W - Length + 15);
        int pY = (int)(0.15 * Area.H);
        //Area.Painter.drawRect(pX, pY, Length, Height);
        GUIView.setArea(pX, pY, pX + Length, pY + Length, 1);
        
        if(Player.DecisionTarget != null && Player.DecisionTarget.Exists){
            if(Player.DecisionTarget instanceof Vehicle){
                Vehicle Target = (Vehicle)Player.DecisionTarget;
                Point3DM Diff = Target.Form.Center.subtract(View.POS);
                Diff.normalize();
                Rotation3DM R = new Rotation3DM();
                double Theta = Math.atan2(Diff.dotProduct(Utils3DM.Vectorj), Diff.dotProduct(Utils3DM.Vectori));
                double Alpha = Math.asin(Diff.dotProduct(Utils3DM.Vectork));

                GUIView.Rotation.rotateAround('y', Alpha);
                R.rotateAround('z', Theta);
                Rotation3DM.multiply(R, GUIView.Rotation, GUIView.Rotation);
                GUIView.setMag(Target.Form.Center.distanceTo(GUIView.POS) / Target.CollisionRadius / 3);
                GUIView.setLookVector();


                Area.Painter.drawRect(pX, pY + Height / 2 + Width + Gap, Length, Width);
                Area.Painter.drawRect(pX, pY + Height / 2 + Width + Gap, (int)(Length * Target.Hitpoints.Current / Target.Hitpoints.Max), Width);

                Color C = getColor(Target.Hitpoints.Current / Target.Hitpoints.Max);
                Area.Painter.setColor(C);
                Target.printNow(Area.Painter, GUIView, C);
                Area.Painter.setColor(Color.green);
                Area.printLine(pX + Length / 2, pY + Height / 2 + Width - 10, getDistance(Player.Form.Center.distanceTo(Target.Form.Center)), 10, Color.green, 't');
                Area.printLine(pX + Length / 2, pY + Height / 2 + Width + Gap, Target.Name, 10, Color.green, 't');
                Area.Painter.drawRect(pX + (int)(Length * Target.Hitpoints.Current / Target.Hitpoints.Max) - 2, pY + Height / 2 + Width + Gap + 2, 0, Width - 4);
                Area.printLine(pX + Length / 2, pY + Height / 2 + Width + Gap + Gap + 10, Target.getTask(), 10, Color.green, 't');
                if(Target instanceof Craft)Area.printLine(pX + Length / 2, pY + Height / 2 + Width + Gap + Gap + 20, ""+((Craft)Target).State, 10, Color.green, 't');
            }else if(Player.DecisionTarget instanceof Position3DM){
                Position3DM Target = (Position3DM)Player.DecisionTarget;
                Area.Painter.drawRect(pX, pY + Height / 2 + Width + Gap, Length, Width);
                Area.printLine(pX + Length/2, pY + Height / 2 * 1/3 - 2, "Nav Point", 14, Color.green, 'c');
                int Font = 14;
                Area.printLine(pX + Length/2, pY + Height / 2 * 2/3 + 2, GUI.getGreek(NavPoints.indexOf(Player.DecisionTarget)), Font, Color.green, 'c');
                Area.printLine(pX + Length / 2, pY + Height / 2 + Width - 10, getDistance(Target.Form.Center.distanceTo(Player.Form.Center)), 10, Color.green, 't');
                Point PA = new Point(pX + Length / 2, pY + Height / 2 * 1/3 + Font/2);
                Point PB = new Point(pX + Length / 2 - (int)(14/Math.sqrt(2)), pY + Height / 2 * 2/3 - Font/2 - 1);
                Point PC = new Point(pX + Length / 2 + (int)(14/Math.sqrt(2)), pY + Height / 2 * 2/3 - Font/2 - 1);
                Area.Painter.drawLine(PA.x, PA.y, PB.x, PB.y);
                Area.Painter.drawLine(PC.x, PC.y, PB.x, PB.y);
                Area.Painter.drawLine(PC.x, PC.y, PA.x, PA.y);
            }
            
            
        }
        
        
    }
    public void initGUI(){
        GUIView = View.copy();
        Middle  = new Point((int)(0.1 * Area.W - 8), (int)(0.7 * Area.H + 80));
        for(int i = 0; i < 5; i++){
            GroundShape.moveTo(-Width/(2 + i), GapWidth * i);
            GroundShape.lineTo(Width / (2 + i) + 1, GapWidth * i);
        }
        for(int i = 0; i < 15; i++){
            int INTA;
            int INTB;
            do{
                INTA = -GUI.randomInt(0, (int)(Width));
                INTB = GUI.randomInt(-Width / 2, Width / 2);
            }while(Math.sqrt(GUI.square(-INTB) + GUI.square(INTA + Width)) < Width / 2);
            
            GroundShape.moveTo(INTB - 2, INTA);
            GroundShape.lineTo(INTB, INTA - 2);
            GroundShape.lineTo(INTB + 2, INTA);
        }
        for(int i = 0; i < 20; i++){
            int INTA;
            int INTB;
            do{
                INTA = -GUI.randomInt(Width / 2, Width * 3 / 2);
                INTB = GUI.randomInt(-Width / 2, Width / 2);
            }while(Math.sqrt(GUI.square(-INTB) + GUI.square(INTA + Width)) > Width / 2 && Math.sqrt(GUI.square(-INTB) + GUI.square(INTA + Width)) > 8);
            double Theta = Math.atan2((INTA + Width), (0 - INTB));
            
            GroundShape.moveTo((float)(INTB + 2 * Math.sqrt(2) * Math.sin(Theta + Math.PI / 4 - Math.PI)), (float)(INTA + 2 * Math.sqrt(2)* Math.cos(Theta + Math.PI / 4 - Math.PI)));
            GroundShape.lineTo(INTB, INTA);
            GroundShape.lineTo((float)(INTB + 2 * Math.sqrt(2) * Math.sin(Theta - Math.PI / 4)), (float)(INTA + 2 * Math.sqrt(2) * Math.cos(Theta - Math.PI / 4)));
        }
        //Draw top cross
        GroundShape.moveTo(-4, -(Width - 4));
        GroundShape.lineTo(4, -(Width + 4));
        GroundShape.moveTo(4, -(Width - 4));
        GroundShape.lineTo(-4, -(Width + 4));
        //Draw bottom cross
        GroundShape.moveTo(-4, (Width - 4));
        GroundShape.lineTo(4, (Width + 4));
        GroundShape.moveTo(4, (Width - 4));
        GroundShape.lineTo(-4, (Width + 4));
        
        
        
    }
    public void GUITickerTape(){
        TickerTape.decrement((float)(1.0/FramesPerSecond));
        
        int pX = X *1/4; int pY = Y*3/4;
        int H = Y / 8; int W = X / 2;
        
        for(int i = 0; i < TickerTape.getLineCount(); i++){
            Area.printLine(X / 2, pY + i * H / TickerTape.getMaxLines() + 3, TickerTape.getLine(TickerTape.getLineCount()-1 - i), 16, Color.green, 't');
        }
        Area.Painter.setColor(Color.green);
      //  Area.Painter.drawRect(pX, pY, W, H);
    }
    public void drawGUI(){
        GUITest();
        GUIBase();
        GUISpeed();
        GUIAltitude();
        GUILift();
        GUITargeting();
        GUIWeapons();
        GUIHorizon();
        GUIHitpoints();
        GUIData();
        GUITickerTape();
    }
    
    public void drawCursor(){
        Area.Painter.setColor(Color.white);
        double PX;
        double PY;
        if(Player.UseAI){
            PX = Player.MouseHorizontal * (Area.W / 2.0) + Area.W / 2.0;
            PY = Player.MouseVertical * (Area.H / 2.0) + Area.H / 2.0; 
            
                
            
            Area.printLine((int)PX, (int)PY + 1, Character.toUpperCase(Player.Evading && Player.State != 't' ? 'e' : Player.State) + "", 12, Player.State == 'a' && Player.AState == 'a' ? Color.red : Color.green, 'c');
        }else{
            PX = MouseX;
            PY = MouseY;
            
        }
        Area.Painter.setColor(Color.green);
        Area.Painter.drawRect((int)(PX - 10), (int)(PY - 10), 20, 20);
    }
    
    public void centerMouse(){
        final Dimension ScreenSize = GUI.getScreenSize();
        int TargetX = ScreenSize.width / 2;
        int TargetY = ScreenSize.height / 2; 
        MyRobot.mouseMove(TargetX, TargetY);
        
    }
    
    public void mousePressed(MouseEvent Event){
        if(Event.getButton() == 1){
            ClickingL = true;
            Player.cycleShot();
            playSound("KLICK"); 
        }
        if(Event.getButton() == 3){
            ClickingR = true;
            centerMouse(); 
        }
        
        
        
      
    }
    public void mouseReleased(MouseEvent Event){
        if(Event.getButton() == 1){
            ClickingL = false;
            
        }
        if(Event.getButton() == 3){
            ClickingR = false;
        }
    }
    public void mouseClicked(MouseEvent Event){
        if(Event.getButton() == 1){
            
        }
        if(Event.getButton() == 3){
            
        }
    }

    public void mouseEntered(MouseEvent Event){
    }
    public void mouseExited(MouseEvent Event){
            
         
        
    }
    
    public void mouseMoved(MouseEvent Event){
        
        MouseDX = Event.getX() - MouseX;
        MouseDY = Event.getY() - MouseY;
        MouseX = Event.getX();
        MouseY = Event.getY();
        
       // setMouseControls();
    }
    public void mouseDragged(MouseEvent Event){
        MouseDX = Event.getX() - MouseX;
        MouseDY = Event.getY() - MouseY;
        MouseX = Event.getX();
        MouseY = Event.getY();
       // setMouseControls();
    }
    
    public void shiftGrid(Point3DM Target){
        int INTX = (int)GUI.round(Target.POSX, 1.0/Grid.ArgC);
        int INTY = (int)GUI.round(Target.POSY, 1.0/Grid.ArgC);
        boolean ReRotate = false;
        if(Grid.Center.POSX != INTX || Grid.Center.POSY != INTY){
            ReRotate = true;
        }
        if(ReRotate){
            Grid.Center.POSX = INTX;
            Grid.Center.POSY = INTY;
            
        }
    }
    
    public void mouseWheelMoved(MouseWheelEvent WheelEvent) {
        Player.TargetThrottle -= WheelEvent.getWheelRotation();
        Player.TargetThrottle = Math.max(0, Math.min(10, Player.TargetThrottle));
        
    }
    
    Color[] SideTints = {Color.cyan, Color.yellow, Color.red, Color.magenta};
    public Color getColor(int Side){
        switch(Side){
            case -1: return Color.cyan;
            case 0: return Color.cyan; 
            case 1: return Color.yellow; 
            case 2: return Color.red; 
            default: throw new IllegalArgumentException("NO SUCH SIDE");
        }
    }
    public void playSound(WaveSound Sound, Point3DM Position, double Mod){
        double Modifier = Mod / Position.distanceTo(View.POS);
        Sound.play(Modifier);        
    }
    public void playSound(WaveSound Sound){    
        Sound.play();
    }
    public void playSound(String S){
        try{
            playSound((WaveSound)Base.SoundMap.get(S));
        }catch(NullPointerException e){
            System.out.println("No Such Sound: " + S);
        }
    }
    public void playSound(String S, Point3DM Position, double Mod){
        try{
            playSound((WaveSound)Base.SoundMap.get(S), Position, Mod);
        }catch(NullPointerException e){
            System.out.println("No Such Sound: " + S);
        }
    }
    public int getSide(Color C){
        for(int i = 0; i < SideTints.length; i++){
            if(C.equals(SideTints[i])){
                return i;
            }
        }
        return -1;
    }   
    int GreekCounter = 0;
    public String getGreek(){
        return GUI.getGreek(GreekCounter++);
    }
    public String getGreek(int Side, Class Type){
        int A = 0;
        for(int i = 0; i < VehicleStore.size(); i++){
            if(VehicleStore.ve(i).Side == Side && VehicleStore.ve(i).getClass() == Type && VehicleStore.ve(i) != Player){
                A++;
            }
        }
        return GUI.getGreek(A);
    }
    public String getFaction(int A){
        switch(A){
            case -1: return "Player";
            case 0: return "Ally";
            case 1: return "Target";
            case 2: return "Enemy";
            default: return "Unknown";
        }
    }
    public String getDistance(double A){
        int LogTen = (int)Math.floor(Math.log10(A));
        if(LogTen > 3){
            return GUI.round(A/1000, 10) + "km";
        }else if(LogTen <= 3 && LogTen >= 1){
            return GUI.round(A, 10) + "m";
        }else{
            return "";
        }
 
    }
    public boolean mightIntersectPlane(Point3DM A, Point3DM B){
        Vector PossiblePlanes = PlaneTable.testArea(A.POSX, A.POSY, B.POSX, B.POSY);
        for(int i = 0; i < PossiblePlanes.size(); i++){
            if(((Plane)PossiblePlanes.get(i)).checkCollision(A, B) != null){
                return true;
            }
        }
       
        return false;
    }
    public Vehicle scanForEnemies(double Range, int S, Point3DM Center){
        double MinDistance = Range;
        Vehicle CurrentTarget = null;
        for(int i = 0; i < Vehicles.size(); i++){
            Vehicle Target = Vehicles.ve(i);
            if(Target.Side != S && Target.Form.Center.distanceTo(Center) <= MinDistance){
                CurrentTarget = Target;
                MinDistance = Target.Form.Center.distanceTo(Center);
            }

        }
        return CurrentTarget;
    }
    
}
