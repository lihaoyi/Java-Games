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

/*
 *REQUIRED CLASSES:
 *Shape3DM
 *Point3DM
 *Rotation3D
 *ObserverM
 *Utils3DM
 *PointVector
 *
 *Counter
 *NumberQueue
 *GUI
 *FileHandler
 */
public class RobotSim{
    public static void main(String[] args){
        new RobotWindow();
    }
}
class RobotWindow extends JFrame implements KeyListener, ActionListener, MouseListener, MouseMotionListener, MouseWheelListener{
    int X = 800;
    int Y = 640;
    String CurrentDirectory = System.getProperty("user.dir");
    final Dimension ScreenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    
    double[][] UsedData;
                                //T    1   2   3

    Vector WheelLog = new Vector();
                               //T     dX  dY  dTHETA

    Vector SpeedLog = new Vector();
    class Trail{
        Shape3DM Form;
        Counter TrailCounter;
        Point3DM Target;
        int TrailLength;
        boolean Active;
        Counter DeathCounter;
        public Trail(int PointCount, Point3DM Targ, Color T, double TotalDuration){
            Form = new Shape3DM(true, T, Targ.POSX, Targ.POSY, Targ.POSZ);
            Utils3DM.setPoints(Form, 'h', PointCount, 0, 0);
            DeathCounter = new Counter(0, 3, 3);
            for(int i = 0; i < PointCount; i++){
                Form.Center.copyCoords(Form.ActualPoints[i]);
            }
            TrailLength = PointCount;
            Form.MaxDistance = 0;
            TrailCounter = new Counter(0, TotalDuration / PointCount, TotalDuration / PointCount);
            Target = Targ;
            Active = true;
            
        }
        public void reset(){
            for(int i = 0; i < TrailLength; i++){
                Target.copyCoords(Form.ActualPoints[i]);
            }
            
          //  TrailLength = Form.PointCount;
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
        public void chase(){
            
            Target.copyCoords(Form.ActualPoints[0]); //First Point
            TrailCounter.increment(-1.0 / FramesPerSecond);
            if(TrailCounter.checkBounds() >= 0){
                double LastDistance =(TrailCounter.Current) / (TrailCounter.Current + 1.0/FramesPerSecond);
                Form.ActualPoints[TrailLength - 1].subtract(Form.ActualPoints[TrailLength - 2], Form.ActualPoints[TrailLength - 1]);
                Form.ActualPoints[TrailLength - 1].multiply(LastDistance, Form.ActualPoints[TrailLength - 1]);
                Form.ActualPoints[TrailLength - 1].add(Form.ActualPoints[TrailLength - 2], Form.ActualPoints[TrailLength - 1]);
                return;
            }
              
            TrailCounter.Current = TrailCounter.Max;
            for(int i = TrailLength - 1; i > 0; i--){
                Form.ActualPoints[i - 1].copyCoords(Form.ActualPoints[i]);   
            }
            Target.copyCoords(Form.Center);
            Form.MaxDistance = Form.Center.distanceTo(Form.ActualPoints[TrailLength - 1]);
                
            if(Form.MaxDistance == 0){
                DeathCounter.increment(-1);
            }
            
        }
        public void print(Graphics2D Painter, ObserverM Viewer){
            if(Form.MaxDistance > -0.01 && Active){
                Form.print(Painter, Viewer);
            }
        }
        
    }
    class GeneralVector extends Vector{
        
    }
    class Thruster{
        Point3DM Position;
        Point3DM Direction;
        Counter Strength;
        double Acceleration;
        public Thruster(){
            Position = new Point3DM(0, 0, 0);
            Direction = new Point3DM(0, 0, 0);
        }
        public void setLimits(double Max, double ds){
            Strength = new Counter(-Max, Max, 0);
            Acceleration = ds;
        }
        public void moveToward(double Target){
            double Increment = Acceleration / FramesPerSecond;
            boolean Bigger = Target > Strength.Current;
            
            if(Bigger){
                Strength.increment(Increment);
                if(Strength.Current > Target){
                    Strength.Current = Target;
                }
            }else{
                Strength.increment(-Increment);
                if(Strength.Current < Target){
                    Strength.Current = Target;
                }
            }
            
            
            Strength.cap();
        }
    }
    class Obstacle{
        Shape3DM Form;
        Point2D.Double[] Footprint;
        Line2D.Double[] Borders;
        
        public Point2D.Double tryCollision(Mobile Target){
            for(int i = 0; i < Target.Borders.length; i++){
                for(int j = 0; j < Borders.length; j++){
                    if(Target.Borders[i].intersectsLine(Borders[j])){
                        double A = Target.Borders[i].ptLineDist(Borders[j].getP1());
                        double B = Target.Borders[i].ptLineDist(Borders[j].getP2());
                        Point2D.Double IPoint = new Point2D.Double();
                        Point3DM P = new Point3DM(Target.Borders[i].getX1() - Target.Borders[i].getX2(),
                                                  Target.Borders[i].getY1() - Target.Borders[i].getY2(), 0);
                        System.out.println("COLLIDE RARGH");
                        P = P.crossProduct(Utils3DM.Vectork);
                        P.multiply(P.dotProduct(new Point3DM(Target.Form.Center.POSX - Target.Borders[i].getX2(), Target.Form.Center.POSY - Target.Borders[i].getY2(), 0)), P);
                        int D = -Target.Borders[i].relativeCCW(Target.Form.Center.POSX, Target.Form.Center.POSY);
                        P.multiply(0.02 / P.length(), P);
                        Target.Form.Center.add(P, Target.Form.Center);
                    }
                }
            }
            
            return null;
        }
        public Obstacle(Shape3DM F){
            Form = F;
            switch(F.Form){
                case 'c':
                    Footprint = new Point2D.Double[4];
                    Footprint[0] = new Point2D.Double(Form.ActualPoints[0].POSX, Form.ActualPoints[0].POSY);
                    Footprint[1] = new Point2D.Double(Form.ActualPoints[2].POSX, Form.ActualPoints[2].POSY);
                    Footprint[2] = new Point2D.Double(Form.ActualPoints[6].POSX, Form.ActualPoints[6].POSY);
                    Footprint[3] = new Point2D.Double(Form.ActualPoints[4].POSX, Form.ActualPoints[4].POSY);
                    Borders = new Line2D.Double[4];
                    
                    break;
                case 't':
                    Footprint = new Point2D.Double[4];
                    Footprint[0] = new Point2D.Double(Form.ActualPoints[0].POSX, Form.ActualPoints[0].POSY);
                    Footprint[1] = new Point2D.Double(Form.ActualPoints[1].POSX, Form.ActualPoints[1].POSY);
                    Footprint[2] = new Point2D.Double(Form.ActualPoints[2].POSX, Form.ActualPoints[2].POSY);
                    Borders = new Line2D.Double[3];
                    break;
            }       
            for(int i = 0; i < Borders.length; i++){
                Borders[i] = new Line2D.Double(Footprint[i], Footprint[(i + 1) % Borders.length]);
            }
        }
    }
    class Mobile{
        Shape3DM Form;
        Point2D.Double[] Footprint = new Point2D.Double[3];
        Line2D.Double[] Borders = new Line2D.Double[3];
        int ThrusterCount = 3;
        Thruster[] Wheel = new Thruster[ThrusterCount];
        Point3DM[] Pivot = new Point3DM[ThrusterCount];
        public Mobile(Point3DM Position, double SideLength, double Thickness, double WheelSpeed){
            Form = new Shape3DM(false, Color.white, Position.POSX, Position.POSY, Position.POSZ);
            Utils3DM.setPoints(Form, 't', Thickness, SideLength, Math.sqrt(3) * SideLength / 2);
            Form.Rotation.rotateAround('y', Math.toRadians(90));
            for(int i = 0; i < Form.Points.length; i++){
                Form.Rotation.transform(Form.Points[i], Form.Points[i]);
            }
            Utils3DM.center(Form, 'p');
            Form.Rotation = new Rotation3D();
            for(int i = 0; i < Footprint.length; i++){
                Footprint[i] = new Point2D.Double();
                Borders[i] = new Line2D.Double();
            }
            for(int i = 0; i < ThrusterCount; i++){
                Wheel[i] = new Thruster();
                Wheel[i].setLimits(WheelSpeed, WheelSpeed);
                Pivot[i] = new Point3DM(0, 0, 0);
            }
            
            updateThrusters();
        }
        public double getAngle(){
            Point3DM Direction = new Point3DM(0, 0, 0);
            MyRobot.Form.Rotation.getOriginalAxis('x', Direction);
            return Math.atan2(-Direction.dotProduct(Utils3DM.Vectorj), Direction.dotProduct(Utils3DM.Vectori));
        }
        public Point3DM getPivot(Thruster[] UsedWheel, double[] Status){
            Point3DM FinalPivot = new Point3DM(0, 0, 0);
            double FinalStrength = 0;
            for(int i = 0; i < ThrusterCount; i++){
                double Strength = UsedWheel[i].Strength.Max * Status[i + 1] / (Pivot[i].flatDistanceTo(UsedWheel[i].Position));
                FinalPivot.add(Pivot[i].multiply(Strength), FinalPivot);
                FinalStrength = FinalStrength + Strength;
            }
            
            FinalPivot.divide(FinalStrength, FinalPivot);
            return FinalPivot;
        }
        public Rotation3D getRotation(double FinalStrength, double[] Status){
            Rotation3D Slide = new Rotation3D();
            Slide.rotateAround('z', getRotationSpeed(FinalStrength) * Status[0]);
            return Slide;
        }
        public double getRotationSpeed(double FinalStrength){
            return -FinalStrength;
        }
        public void doRotation(Point3DM FinalPivot, Rotation3D Slide){
            Form.Center.subtract(FinalPivot, Form.Center);
            Slide.transform(Form.Center, Form.Center);
            Form.Center.add(FinalPivot, Form.Center);
            Rotation3D.multiply(Slide, Form.Rotation, Form.Rotation);
        }
        public double getFinalStrength(Thruster[] UsedWheel, double[] Status){
            double FinalStrength = 0;
            for(int i = 0; i < ThrusterCount; i++){
                
                FinalStrength = FinalStrength + UsedWheel[i].Strength.Max * Status[i + 1] / (Pivot[i].flatDistanceTo(UsedWheel[i].Position));
            }   
            return FinalStrength;
        }
        public void updateFootprint(){
            for(int i = 0; i < Footprint.length; i++){
                Footprint[i].x = Form.ActualPoints[i].POSX;
                Footprint[i].y = Form.ActualPoints[i].POSY;
            }
            for(int i = 0; i < 3; i++){
                Borders[i].setLine(Footprint[i], Footprint[(i + 1) % 3]);
            }   
        }
        public Point3DM moveIncrement(double[] Status){
            
            double FinalStrength = getFinalStrength(Wheel, Status);
            MyRobot.updateThrusters();
            if(Math.abs(FinalStrength) < 0.00001){
                Point3DM Translation = new Point3DM(0, 0, 0);
                for(int i = 0; i < ThrusterCount; i++){ 
                    Translation.add(Wheel[i].Direction.multiply(Wheel[i].Strength.Max * Status[i + 1] * 2 / 3), Translation);
                }
                Form.Center.add(Translation.multiply(Status[0]), Form.Center);
                Rotation3D Slide = new Rotation3D(); 
                Slide.rotateAround('z', -FinalStrength * Status[0]);
                Rotation3D.multiply(Slide, Form.Rotation, Form.Rotation);
                return new Point3DM(-Translation.POSX, -Translation.POSY, -FinalStrength);
            }else{
                Point3DM FinalPivot = getPivot(Wheel, Status);

                Rotation3D Slide = getRotation(FinalStrength, Status);
                doRotation(FinalPivot, Slide);
                
                Point3DM InstaV = Form.Center.subtract(FinalPivot).crossProduct(Utils3DM.Vectork).multiply(FinalStrength);
                return new Point3DM(-InstaV.POSX, -InstaV.POSY, FinalStrength);
                
            }
            
        }
        
        public void updateThrusters(){
            Form.calcPoints();
            for(int i = 0; i < ThrusterCount; i++){
                Wheel[i].Position.setToMidpoint(Form.ActualPoints[i], Form.ActualPoints[(i + 1) % ThrusterCount]);
                Form.ActualPoints[i].subtract(Form.ActualPoints[(i + 1) % ThrusterCount], Wheel[i].Direction);
                Wheel[i].Direction.normalize();
                Form.ActualPoints[(i + 2) % ThrusterCount].copyCoords(Pivot[i]);
                
            }
        }
    }
    
    
    //GUI STUFF
    JCanvas Area;
    JPanel TopArea = new JPanel();
    JPanel BottomArea = new JPanel();
    
    JLabel ViewLabel = new JLabel("View");
    JButton TrackButton = new JButton("Track");
    
    JButton ViewButton = new JButton("Top");
    JButton DefaultButton = new JButton("Default");
    JButton TrailButton = new JButton("Trail");
  
    
    
    JButton LoadObjectButton = new JButton("Load");
    JButton SaveObjectButton = new JButton("Save");
    
    JLabel ObjectLabel = new JLabel("Objects");
    
    JButton AddObjectButton = new JButton("Add");
    JButton RemoveObjectButton = new JButton("Remove");
    
    JLabel ShapeLabel = new JLabel("Shape");
    JComboBox ShapeComboBox = new JComboBox(new String[]{"Box", "Triangle", "Wall"});
    
    
    JLabel SimLabel = new JLabel("Simulator");
    JButton ResetButton = new JButton("Reset");
    JButton ObstacleButton = new JButton("Objects");
    
    
    JLabel InputLabel = new JLabel("Input / Output");
    JComboBox TypeComboBox = new JComboBox(new String[]{"Wheel", "Velocity"});
    JComboBox FormatComboBox = new JComboBox(new String[]{"Loaded", "Streaming"});
    JButton LoadButton = new JButton("Load");
    JButton OutputButton = new JButton("Output");
    
    JLabel[][] Telemetry = new JLabel[3][3];
    
    
    //Time Considerations
    TimeKeeper Time = new TimeKeeper(20);
    double FramesPerSecond = 1000.0 / 20;
    
    Timer Stopwatch = new Timer((int)(20), this);
    long FrameCounter;
  
    //Settings
    char InputType = ' '; //w wheels v velocities
    
    char InputStreaming = 'n'; //y yes n no
    char CameraType = 'x'; //x fixed f follow
    char TrailActive = 'y'; //y yes n no
    //boolean CountTime = false;
    
    
    //File Settings
    String[][] Stats;
    
    
    //IO Streamers
    FileHandler StreamLoader;
    
    FileHandler OutputVStreamer;
    FileHandler OutputPStreamer;
    FileHandler OutputWStreamer;
    
    
    //O Data
    double[] InstantWheelSpeed = new double[3];
    double[] InstantRobotPosition = new double[3];
    double[] InstantRobotSpeed = new double[3];
    double[][] LastOutput = new double[3][4];
    
   //3DM Objects 
    
    Point3DM IsoCameraOffset = new Point3DM(-2, 0, 2);
    Point3DM TopCameraOffset = new Point3DM(0, 0, 200);
    Point3DM CameraOffset = IsoCameraOffset;
    
    ObserverM IsoView = new ObserverM(-2, 0, 2, new Rotation3D());
    ObserverM TopView = new ObserverM(-0, 0, 200, new Rotation3D());
    ObserverM View = IsoView;
    
    Shape3DM Grid;
    Trail[] Trails = new Trail[3];
   
    Mobile MyRobot;
    Vector ShapesToPrint = new Vector();
    Vector Obstacles = new Vector();
    Shape3DM NewObstacle = new Shape3DM(false, Color.white, 0, 0, 0);
    public void toggleObstacleButtons(boolean T){
        T = !T;


        System.out.println("A");
        TypeComboBox.setVisible(T);
        FormatComboBox.setVisible(T);
        LoadButton.setVisible(T);
        OutputButton.setVisible(T);
        ObjectLabel.setVisible(!T);
        AddObjectButton.setVisible(!T);
        RemoveObjectButton.setVisible(!T);
        LoadObjectButton.setVisible(!T);
        SaveObjectButton.setVisible(!T);
        ShapeComboBox.setVisible(!T);
        ShapeLabel.setVisible(!T);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Telemetry[i][j].setVisible(T);
            }
        }

        
    }
    public void reset(){
        //Stop Counter
        Time.CountTime = false;
        
        //Reset Variables
        Time.TotalTime = 0;
        Time.PreviousTime = 0;
        for(int i = 0; i < InstantRobotSpeed.length; i++){
            InstantRobotSpeed[i] = 0;
        }
        for(int i = 0; i < InstantRobotPosition.length; i++){
            InstantRobotPosition[i] = 0;
        }
        for(int i = 0; i < InstantWheelSpeed.length; i++){
            InstantWheelSpeed[i] = 0;
        }
        
        //Reset Robot
        Utils3DM.Origin.copyCoords(MyRobot.Form.Center);
        MyRobot.Form.Rotation.identify();
        
        //Reset Output
        resetOutputs();
        
        //Reset InputStream
        if(InputStreaming == 'n'){
            
        }else{
            UsedData[0] = new double[]{0, 0, 0, 0};
            StreamLoader.closeRead();
            
            StreamLoader.closeWrite();
            StreamLoader = null;
        }
        
        //Activate GUI
        enableIO(true);
        ObstacleButton.setEnabled(true);
    }
    public void enableIO(boolean T){
        LoadButton.setEnabled(T);
        TypeComboBox.setEnabled(T);
        FormatComboBox.setEnabled(T);
        OutputButton.setEnabled(T);
    }
    public void load(){
        Stopwatch.stop();
        Time.DiscountDelay = true;
        InputType = (TypeComboBox.getSelectedIndex() == 0 ? 'w' : 'v');
            
        InputStreaming = (FormatComboBox.getSelectedIndex() == 0 ? 'n' : 'y');
        for(int i = 0; i < 3; i++){
            LastOutput[i][0] = -1;
            LastOutput[i][1] = 1.0/0;
            LastOutput[i][2] = 1.0/0;
            LastOutput[i][3] = 1.0/0;
        }
        
        try{
            if(InputStreaming == 'n'){
                
                String[] LoadArray = null;       
                String InputAddress = (InputType == 'v' ? Stats[7][1] : Stats[8][1]);
                if(StreamLoader != null){
                    InputAddress = StreamLoader.Address;
                }
                String InputFile = FilePicker.userGetFile("Select File", InputAddress, this);
                if(InputFile == null)throw new Exception();
                LoadArray = FileHandler.readFile(InputFile);   
                UsedData = arrayExpand(LoadArray);
                Time.CountTime = true;
            }else{
                String InputAddress = (InputType == 'v' ? Stats[9][1] : Stats[10][1]);
                if(StreamLoader != null){
                    InputAddress = StreamLoader.Address;
                }
                String InputFile = FilePicker.userGetFile("Select File", InputAddress, this);
                if(InputFile == null)throw new Exception();
                StreamLoader = new FileHandler(InputFile, true, true);
                StreamLoader.closeWrite();
                UsedData = new double[1][4];
  
            }
            enableIO(false);
            ObstacleButton.setEnabled(false);
            resetOutputs();
            
        }catch(Exception e){System.out.println("LOADERROR" + e);}
        Stopwatch.start();
        
    }
    public void resetOutputs(){
        
        OutputVStreamer.closeWrite();
        OutputVStreamer = new FileHandler(OutputVStreamer.Address, false, true);
        
        OutputPStreamer.closeWrite();
        OutputPStreamer = new FileHandler(OutputPStreamer.Address, false, true);
        
        OutputWStreamer.closeWrite();
        OutputWStreamer = new FileHandler(OutputWStreamer.Address, false, true);
    }

    public void init(){
        
        Time.CountTime = false;
        //Load .ini
        Stats = FileHandler.readFile("RobotStats.txt", "\t");        
        
        //Create Robot
        MyRobot = new Mobile(new Point3DM(0, 0, 0), Double.parseDouble(Stats[0][1]), Double.parseDouble(Stats[1][1]), Double.parseDouble(Stats[2][1]));
        ShapesToPrint.add(MyRobot.Form);
        
        //Create Grid
        Grid = new Shape3DM(true, Color.GREEN.darker(), 0, 0, 0);
        ShapesToPrint.add(Grid);
        Utils3DM.setPoints(Grid, 'g', 20, 20, 0.4);
        //Create Trails
        for(int i = 0; i < 3; i++){
            Trails[i] = new Trail(10, MyRobot.Form.ActualPoints[i], Color.red, Double.parseDouble(Stats[3][1]));
            
            ShapesToPrint.add(Trails[i].Form);
            switch(i){
                case 2: Trails[i].Form.Tint = Color.red; break;
                case 0: Trails[i].Form.Tint = Color.green; break;
                case 1: Trails[i].Form.Tint = GUI.mix(Color.cyan, Color.blue, 0.5); break;
            }
        }
        
        //Create Observers
        IsoView.setArea(0, 0, X, Y-100, 1.0);
        TopView.setArea(0, 0, X, Y-100, 75);
        IsoView.Rotation.rotateAround('y', Math.toRadians(-25));
        TopView.Rotation.rotateAround('y', Math.toRadians(-90));
        
        //Create Output Streams
        OutputVStreamer = new FileHandler(Stats[11][1], false, true);
        OutputPStreamer = new FileHandler(Stats[12][1], false, true);
        OutputWStreamer = new FileHandler(Stats[13][1], false, true);
        
        
        
        
        
        
        
    }
    
    
    public void initButtons(Container ContentArea){
        
        TopArea.setLayout(null);
        BottomArea.setLayout(null);
        TopArea.setBounds(0, 0, X, Y-100);
        BottomArea.setBounds(0, Y-100, X, 100);
        BottomArea.setBackground(Color.gray);
        ContentArea.add(TopArea);
        ContentArea.add(BottomArea);
        
        Area = new JCanvas();
        Area.setBounds(0, 0, X, Y - 100);
        Area.init(BufferedImage.TYPE_BYTE_INDEXED);
        Area.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
      //  Area.updatePicture();
        
        TopArea.add(Area);
        
        BottomArea.add(ViewLabel);
        ViewLabel.setBounds(4, 4, 88 + 4 + 88, 26);
        ViewLabel.setHorizontalAlignment(JTextField.CENTER);
        

        BottomArea.add(TrackButton);
        TrackButton.addActionListener(this);
        TrackButton.setBounds(4, 34, 88, 26);
        TrackButton.setToolTipText("Causes the camera to follow the robot");
        
        BottomArea.add(DefaultButton);
        DefaultButton.addActionListener(this);
        DefaultButton.setBounds(4 + 88 + 4, 64, 88, 26);
        DefaultButton.setToolTipText("Resets view to default settings");
        
        BottomArea.add(TrailButton);
        TrailButton.addActionListener(this);
        TrailButton.setBounds(96, 34, 88, 26);
        TrailButton.setToolTipText("Toggles movement trails on robot");
        
        BottomArea.add(ViewButton);
        ViewButton.addActionListener(this);
        ViewButton.setBounds(4, 64, 88, 26);
        ViewButton.setToolTipText("Switches between top-down and isometric view");

        
        
        
        BottomArea.add(SimLabel);
        SimLabel.setBounds(188, 4, 88 + 4, 26);
        SimLabel.setHorizontalAlignment(JTextField.CENTER);
        
        BottomArea.add(ResetButton);
        ResetButton.addActionListener(this);
        ResetButton.setBounds(188, 34, 88, 26);
        ResetButton.setToolTipText("Resets the simulation");
        
        BottomArea.add(ObstacleButton);
        ObstacleButton.addActionListener(this);
        ObstacleButton.setBounds(188, 64, 88, 26);
        ObstacleButton.setToolTipText("Edit obstacles");
        
        BottomArea.add(InputLabel);
        InputLabel.setBounds(188 + 88 + 4, 4, 88 + 4 + 88, 26);
        InputLabel.setHorizontalAlignment(JTextField.CENTER);
        
        
        BottomArea.add(FormatComboBox);
        FormatComboBox.setBounds(188 + 88 + 4, 34, 88, 26);
        FormatComboBox.setToolTipText("Selects whether input is pre-loaded or streaming");
        
        BottomArea.add(TypeComboBox);
        TypeComboBox.setBounds(188 + 88 + 4 + 88 + 4, 34, 88, 26);
        TypeComboBox.setToolTipText("Selects whether input controls wheel speeds or the robots velocity");
        
        BottomArea.add(LoadButton);
        LoadButton.setBounds(188 + 88 + 4, 64, 88, 26);
        LoadButton.addActionListener(this);
        LoadButton.setToolTipText("Select input file to use and begin simulation");
        
        BottomArea.add(OutputButton);
        OutputButton.setBounds(188 + 88 + 4 + 88 + 4, 64, 88, 26);
        OutputButton.addActionListener(this);
        OutputButton.setToolTipText("Modify output file streams");
        
        BottomArea.add(LoadObjectButton);
        LoadObjectButton.setBounds(188 + 88 + 4, 34, 88, 26);
        LoadObjectButton.addActionListener(this);
        LoadObjectButton.setToolTipText("Load a set of objects to the scene");
        
        BottomArea.add(SaveObjectButton);
        SaveObjectButton.setBounds(188 + 88 + 4 + 88 + 4, 34, 88, 26);
        SaveObjectButton.addActionListener(this);
        SaveObjectButton.setToolTipText("Save the set of objects on the scene");
        
        
        BottomArea.add(ObjectLabel);
        ObjectLabel.setBounds(188 + 88 + 4 + 88 + 4 + 88 + 4, 4, 88 + 4 + 88, 26);
        ObjectLabel.setHorizontalAlignment(JTextField.CENTER);
        
       
        BottomArea.add(AddObjectButton);
        AddObjectButton.addActionListener(this);
        AddObjectButton.setBounds(188 + 88 + 4 + 88 + 4 + 88 + 4, 34, 88, 26);
        AddObjectButton.setToolTipText("Add an object to the scene");

        BottomArea.add(RemoveObjectButton);
        RemoveObjectButton.addActionListener(this);
        RemoveObjectButton.setBounds(188 + 88 + 4 + 88 + 4 + 88 + 4 + 88 + 4, 34, 88, 26);
        RemoveObjectButton.setToolTipText("Remove an object from the scene");
        
        BottomArea.add(ShapeLabel);
        ShapeLabel.setBounds(188 + 88 + 4 + 88 + 4 + 88 + 4 + 88 + 4 + 88 + 4, 4, 88, 26);
        ShapeLabel.setHorizontalAlignment(JTextField.CENTER);
        
        BottomArea.add(ShapeComboBox);
        ShapeComboBox.addActionListener(this);
        ShapeComboBox.setBounds(188 + 88 + 4 + 88 + 4 + 88 + 4 + 88 + 4 + 88 + 4, 34, 88, 26);
        ShapeComboBox.setToolTipText("Select shape of obstacle to add");
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Telemetry[i][j] = new JLabel("ARR");
                Telemetry[i][j].setFont(new Font("COURIER", Font.BOLD, 14));
                Telemetry[i][j].setHorizontalAlignment(JTextField.RIGHT);
                Telemetry[i][j].setBounds(4 + (5 + i) * 92, 4 + (j) * 30, 88, 26);
                BottomArea.add(Telemetry[i][j]);
            }
        }
        toggleObstacleButtons(false);
    }
    
    public RobotWindow(){
        //BASIC INITIALIZATION
        super("Window"); 
        Stopwatch.stop();
        
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        contentArea.setLayout(null);
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        
        
        this.setLocation(ScreenSize.width / 2 - X / 2, ScreenSize.height / 2 - Y / 2);
        init();
        initButtons(contentArea);
        
        this.addKeyListener(this);
        Area.addMouseListener(this);
        Area.addMouseMotionListener(this);
        Area.addMouseWheelListener(this);
       // if(!GUI.Border){
        setUndecorated(true); 
       // getRootPane().setWindowDecorationStyle(JRootPane.WARNING_DIALOG);
        //}
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        Stopwatch.start();
        
        //this.pack();   
    }
    public void keyPressed(KeyEvent Event){
        switch(Event.getKeyCode()){
            case 81: break; //Q
            case 87: break; //W
            case 69: break; //E
            
            case 65: break; //A
            case 83: break; //S
            case 68: break; //D
            
            case 90: break; //Z
            case 88: break; //X
            case 67: if(CameraType == 'x')CameraType = 'f'; else CameraType = 'x'; break; //C
            
            
            case 86: break; //V
            case 45: break; //-
            case 61: break; //=
            case 27: break; //ESC
            
            case 32: break; //SPACE 
            
            
        }
        

    }
    public void keyReleased(KeyEvent Event){
        switch(Event.getKeyCode()){
            case 81: break; //Q
            case 87: break; //W
            case 69: break; //E
            
            case 65: break; //A
            case 83: break; //S
            case 68: break; //D
            
            case 90: break; //Z
            case 88: break; //X
            case 67: break; //C
            
            
            case 86: break; //V
            case 45: break; //-
            case 61: break; //=
            case 27: exit(); break; //ESC
            
            case 32: break; //SPACE 
            default: System.out.println(Event.getKeyCode());
        }

    }
    public double[][] arrayExpand(String[] Input){
        String[][] ExpandedInput = new String[Input.length][];
        double[][] UsedArray = new double[Input.length][4];
        for(int i = 0; i < Input.length; i++){
            ExpandedInput[i] = Input[i].split("\t");
        }
        for(int i = 0; i < ExpandedInput.length; i++){
            for(int j = 0; j < ExpandedInput[i].length; j++){
                if(ExpandedInput[i][j].equals("^")){
                    UsedArray[i][j] = UsedArray[i-1][j];
                }else{
                    UsedArray[i][j] = Double.parseDouble(ExpandedInput[i][j]);
                }
            }
        }
        return UsedArray ;
    }
    public String[] arrayCompress(double[][] Array){
        for(int i = 0; i < Array.length; i++){
            for(int j = 0; j < Array[i].length; j++){
                Array[i][j] = GUI.round(Array[i][j], 100);
            }
        }
        Vector Output = new Vector();
        for(int i = 0; i < Array.length; i++){
            Output.add(Array[i][0] + "\t" + (i != 0 && Array[i][1] == Array[i-1][1] ? "^" : ""+Array[i][1]) + "\t" + (i != 0 && Array[i][2] == Array[i-1][2] ? "^" : ""+Array[i][2]) + "\t" + (i != 0 && Array[i][3] == Array[i-1][3] ? "^" : ""+Array[i][3]));
        }
        String[] FinalOutput = new String[Output.size()];
        Output.toArray(FinalOutput);
        return FinalOutput;
    }
    public void exit(){
        Stopwatch.stop();
        Time.CountTime = false;
        OutputVStreamer.closeWrite();
        OutputPStreamer.closeWrite();
        OutputWStreamer.closeWrite();
        
        
   
        System.exit(0);
    }
    
    public void keyTyped(KeyEvent Event){
        
        
        
    }

  
   
    public double[] setArray(){
        //System.out.println(InputStreaming);
        if(InputStreaming == 'n'){//if not streaming use loaded array
            if(Time.CountTime){
            
                int j;
                for(j = 0; j < UsedData.length && UsedData[j][0] < Time.PreviousTime; j++){}
            
                if(j > 0){
                
                    return new double[]{Time.TotalTime - Time.PreviousTime, UsedData[j-1][1], UsedData[j-1][2], UsedData[j-1][3]};
                }else{
                    return new double[]{Time.TotalTime - Time.PreviousTime, 0, 0, 0};
                }
            }else{
                return null;
            }
        }else if(StreamLoader != null){ //if streaming then grab from file
            
            String[] RawInput = StreamLoader.readFile();
            if(RawInput.length != 0){Time.CountTime = true;}
            double[] Input;
            //System.out.println(RawInput.length);
            if(RawInput.length == 0){
                
                Input = ((double[][])UsedData.clone())[0];
                Input[0] = Time.TotalTime;
            }else{
                
                
                RawInput = RawInput[RawInput.length - 1].split("\t");
                Input = new double[RawInput.length];
                System.out.println(RawInput.length);
                for(int i = 0; i < RawInput.length; i++){
                    if(RawInput[i].equals("^")){
                        Input[i] = UsedData[0][i];
                    }else{
                        Input[i] = Double.parseDouble(RawInput[i]);
                    }
                }
                UsedData[0] = Input;
                Input[0] = Time.TotalTime;
                
            }
            return Input;
        }
        return null;
    }
    
    public boolean isEqual(double[] ArrayA, double[] ArrayB){
        if(ArrayA.length != ArrayB.length){
            return false;
        }
        for(int i = 1; i < ArrayA.length; i++){
            if(GUI.round(ArrayA[i], 1000) != GUI.round(ArrayB[i], 1000)){
                return false;
            }
        }
        return true;
    }
             
    public void moveWheels(double[] Status){
      
        
        Status[0] = 1.0 / FramesPerSecond;
        Point3DM Info = MyRobot.moveIncrement(Status);
        for(int i = 0; i < 3; i++){InstantWheelSpeed[i] = Status[i + 1];}
        
        double[] Update = {Time.TotalTime, Info.POSX, Info.POSY, Info.POSZ};
        for(int i = 0; i < 3; i++){InstantRobotSpeed[i] = Update[i + 1];}
        
        if(SpeedLog.size() == 0 || !isEqual(Update, (double[])SpeedLog.lastElement())){
            SpeedLog.add(Update);
        }
    }                                 // 0  1  2  3
    public void moveRobot(double[] Status){ // DT DX DY DTHETA             STATUS
                                         // DT WHEEL0 WHEEL1 WHEEL2     OUTPUT
        double[] Output = new double[4];
      
        for(int j = 0; j < 4; j++){
            Output[j] = Output[j] + Status[j];
        }
        
        Status = Output;
        for(int i = 0; i < 3; i++){InstantRobotSpeed[i] = Status[i + 1];}
    
        Output = new double[4];
        Point3DM Movement = new Point3DM(Status[1], Status[2], 0);
        for(int i = 0; i < 3; i++){
            Output[i + 1] = -MyRobot.Wheel[i].Direction.dotProduct(Movement);
                
            Output[i + 1] = Output[i + 1] + Status[3] * MyRobot.Wheel[i].Position.flatDistanceTo(MyRobot.Form.Center);
                
            Output[i + 1] = Output[i + 1] / MyRobot.Wheel[i].Strength.Max;
        }
 
        
     //   for(int i = 0;  i < Output.length; i++)System.out.println(Output[i]);
        Output[0] = 1.0 / FramesPerSecond;
        for(int i = 0; i < 3; i++){InstantWheelSpeed[i] = Output[i + 1];}
        MyRobot.moveIncrement(Output);
        Output[0] = Time.TotalTime;
        double[] Update = Output;
        //if(WheelLog.size() != 0){
//            System.out.println(!isEqual(Update, (double[])WheelLog.lastElement()));
  //      }
        if(WheelLog.size() == 0 || !isEqual(Update, (double[])WheelLog.lastElement())){
            WheelLog.add(Update);
        }
        
    }
    
    public void outputStream(double[] Data, double[] LastOutputData, FileHandler OutputStream){
        double[] Output = new double[4];
        Output[0] = GUI.round(Time.TotalTime, 100);
        Output[1] = GUI.round(Data[0], 100);
        Output[2] = GUI.round(Data[1], 100);
        Output[3] = GUI.round(Data[2], 100);
     
        String FinalOutput = "";
        FinalOutput += Output[0] + "\t";
        FinalOutput += (Output[1] == LastOutputData[1] ? "^" : Output[1] + "") + "\t";
        FinalOutput += (Output[2] == LastOutputData[2] ? "^" : Output[2] + "") + "\t";
        FinalOutput += (Output[3] == LastOutputData[3] ? "^" : Output[3] + "") + "\t";
        
        if(!(Output[1] == LastOutputData[1] && Output[2] == LastOutputData[2] && Output[3] == LastOutputData[3])){
            OutputStream.writeFile(FinalOutput);
        }
        for(int i = 0; i < 4; i++){
            LastOutputData[i] = Output[i];
        }
        
    }
    public void updateRobotPosition(){
        
        InstantRobotPosition[0] = MyRobot.Form.Center.POSX;
        InstantRobotPosition[1] = MyRobot.Form.Center.POSY;
        InstantRobotPosition[2] = MyRobot.getAngle();
    }
    public void actionPerformed(ActionEvent Event){
        if(Event.getSource() == Stopwatch){
            //System.out.println("A");
            //Count Frames
            Time.doTime();
            FramesPerSecond = Time.FramesPerSecond;;
            //Black out Canvas
            
            Area.Painter.clearRect(0, 0, Area.W, Area.H);
            Area.Painter.setColor(Color.white);
            Area.Painter.drawRect(2, 2, View.ScreenX - 4, View.ScreenY - 4);
            Area.Painter.drawRect(4, 4, View.ScreenX - 8, View.ScreenY - 8);
            //Do Movement
            
            MyRobot.updateThrusters();
            Point3DM Center = MyRobot.Form.Center.copy();
            double[] Array = setArray();
            if(Time.CountTime){
                
                if(InputType == 'w')moveWheels(Array);
                if(InputType == 'v')moveRobot(Array);
                Center.subtract(MyRobot.Form.Center, Center);
                updateRobotPosition();
                for(int i = 0; i < Obstacles.size(); i++){
                    ((Obstacle)Obstacles.elementAt(i)).tryCollision(MyRobot);
                }
                outputStream(InstantRobotSpeed, LastOutput[0], OutputVStreamer);
                outputStream(InstantRobotPosition, LastOutput[1], OutputPStreamer);
                outputStream(InstantWheelSpeed, LastOutput[2], OutputWStreamer);
            }
            //Update Footprint of robot
            MyRobot.updateFootprint();
            //Update Camera
            moveCamera(MyRobot.Form.Center);
            //Calculate 3D Points
            
            for(int i = 0; i < ShapesToPrint.size(); i++){
                if(((Shape3DM)ShapesToPrint.elementAt(i)).Static == false){
                    ((Shape3DM)ShapesToPrint.elementAt(i)).calcPoints();
                }
            }
            for(int i = 0; i < Trails.length; i++){
                Trails[i].chase();
            }
            //GUI Stuff
            updateTelemetry();
            drawGUI();
            //Accept User Input

            //Do AI Controls
        
            //setPosition();
            //Align Camera to Player
       
            //Center Grid on Player
 
            //CheckCollision
        
            //Background Printing
     
        
        
            //3D Stuff Printing

            for(int i = 0; i < ShapesToPrint.size(); i++){
                ((Shape3DM)ShapesToPrint.elementAt(i)).print(Area.Painter, View);
            }
        
        
        
            //Print Canvas to Screen
            Area.paintComponent(this.getGraphics());
        }else if(Event.getSource() == TrackButton){
            if(CameraType == 'f'){
                CameraType = 'x';
            }else{
                CameraType = 'f';
            }
        }else if(Event.getSource() == TrailButton){
            for(int i = 0; i < 3; i++){
                Trails[i].Form.Visible = !Trails[2].Form.Visible;
            }
        }else if(Event.getSource() == DefaultButton){
            CameraType = 'f';
            moveCamera(Utils3DM.Origin);
            CameraType = 'x';
            View = IsoView;
            CameraOffset = IsoCameraOffset;
        }else if(Event.getSource() == ViewButton){
            
            if(View == TopView){
                View = IsoView;
                CameraOffset = IsoCameraOffset;
            }else{
                View = TopView;
                CameraOffset = TopCameraOffset;
            }
        }else if(Event.getSource() == ResetButton){
            reset();
            
        }else if(Event.getSource() == LoadButton){
            load();
        }else if(Event.getSource() == OutputButton){
            modifyOutputStream();
        }else if(Event.getSource() == ObstacleButton){
            if(ObstacleButton.getText().equals("Cancel")){
                enableIO(true);
                ObstacleButton.setText("Objects");
                
                NewObstacle.Visible = false;
                toggleObstacleButtons(false);
            }else{
                NewObstacle.Visible = true;
                prepareToPlace();
                enableIO(false);
                ObstacleButton.setText("Cancel");
                toggleObstacleButtons(true);
            }
        }else if(Event.getSource() == ShapeComboBox){
            
            setPoints(NewObstacle, nameToChar(ShapeComboBox.getSelectedItem() + ""), 1, 1, 1);
        }
    }
    public char nameToChar(String Name){
        if(Name.equals("Box")){
            return 'c';
        }
        if(Name.equals("Triangle")){
            return 't';
        }
        if(Name.equals("Wall")){
            return 'w';
        }
        return ' ';
    }
    public void setPoints(Shape3DM Sh, char Ch, double A, double B, double C){
        NewObstacle.BasePoints.clear();
        if(Ch == 'w'){
            B = 0;
            Ch = 'c';
        }
        Utils3DM.setPoints(Sh, Ch, A, B, C);
        
        if(Ch == 't'){
            Sh.Points[2].POSZ /= 2 / Math.sqrt(3);
            Sh.Points[5].POSZ /= 2 / Math.sqrt(3);
            Rotation3D R = new Rotation3D();
            R.rotateAround('y', Math.PI / 2);
            for(int i = 0; i < Sh.Points.length; i++){
                R.transform(Sh.Points[i], Sh.Points[i]);
            }
            
        }
        Utils3DM.center(Sh, 'p');
        double MinZ = 0;
        for(int i = 0; i < Sh.Points.length; i++){
            if(Sh.Points[i].POSZ < MinZ){
                MinZ = Sh.Points[i].POSZ;
            }   
        }
        for(int i = 0; i < Sh.Points.length; i++){
            Sh.Points[i].POSZ -= MinZ;
        }
    }
    public void prepareToPlace(){
        ShapesToPrint.remove(NewObstacle);
        if(NewObstacle == null){
            NewObstacle = new Shape3DM(false, Color.white, 0, 0, 0);
        }else{
            NewObstacle = new Shape3DM(false, Color.white, NewObstacle.Center.POSX, NewObstacle.Center.POSY, NewObstacle.Center.POSZ);
        }
        System.out.println(ShapeComboBox.getSelectedItem().toString().charAt(0));
        
        
        setPoints(NewObstacle, nameToChar(ShapeComboBox.getSelectedItem() + ""), 1, 1, 1);
        
           
        NewObstacle.MaxDistance = 1.0/0;
        ShapesToPrint.add(NewObstacle);
        
    }
    public void handleOutputStream(FileHandler Streamer, FileHandler OtherStreamerA, FileHandler OtherStreamerB){
        String FilePath = FilePicker.userGetFile("Input File Path", Streamer.Address, this);
        //new FileWriter(FilePath);
        if(FilePath == null){
            System.out.println("Error: No File selected");
        }else if(FilePath.equals(OtherStreamerB.Address) || FilePath.equals(OtherStreamerA.Address)){
            System.out.println("Error: File already used for output stream.");

        }else{
                            
        Streamer = new FileHandler(FilePath, false, true);
        }
    }
    public void modifyOutputStream(){
        int OutputChoice = JOptionPane.showOptionDialog(this, "Output #1:  " + OutputWStreamer.Address + "\nOutput #2:  " + OutputVStreamer.Address + "\nOutput #3:  " + OutputPStreamer.Address + "\n\nSelect output path to edit:", "Output", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"(1) Wheels", "(2) Velocities", "(3) Position"}, null);
            if(OutputChoice != -1){
                try{
                    switch(OutputChoice){
                        case 0:{
                            handleOutputStream(OutputWStreamer, OutputVStreamer, OutputPStreamer);
                            }break;
                        case 1:{
                            handleOutputStream(OutputVStreamer, OutputWStreamer, OutputPStreamer);
                            }break;
                        case 2:{
                            handleOutputStream(OutputPStreamer, OutputVStreamer, OutputWStreamer);
                            }break;
                        
                    }
               }catch(Exception e){
                    System.out.println("Error: " + e);
            }
        }
    }
    public void drawGUI(){
        //public void printLine(int INTX, int INTY, String Text, int Font, Color FontColor, char Alignment){
        Area.printLine(30, 50, "T = " + GUI.processNumber(4, GUI.round(Time.TotalTime, 100) , 3), 14, Color.green, 'l');
        Area.printLine(30, 75, "V = " + GUI.processNumber(4, GUI.round(Math.sqrt(GUI.square(InstantRobotSpeed[0]) + GUI.square(InstantRobotSpeed[1])), 100) , 3), 14, Color.green, 'l');
    }
    public void updateTelemetry(){
        for(int i = 0; i< 3; i++){
            Telemetry[0][i].setText("W" + (i + 1) + GUI.processNumber(3, GUI.round(InstantWheelSpeed[i], 100), 3));
            
        }
        Telemetry[1][0].setText("Vx" + GUI.processNumber(3, GUI.round(InstantRobotSpeed[0], 100), 3));
        Telemetry[1][1].setText("Vy" + GUI.processNumber(3, GUI.round(InstantRobotSpeed[1], 100), 3));
        Telemetry[1][2].setText("Vt" + GUI.processNumber(3, GUI.round(InstantRobotSpeed[2], 100), 3));
        
        Telemetry[2][0].setText("X" + GUI.processNumber(3, GUI.round(InstantRobotPosition[0], 100), 3));
        Telemetry[2][1].setText("Y" + GUI.processNumber(3, GUI.round(InstantRobotPosition[1], 100), 3));
        Telemetry[2][2].setText("T" + GUI.processNumber(3, GUI.round(InstantRobotPosition[2], 100), 3));
    }
    
    public void moveCamera(Point3DM Anchor){
        if(CameraType == 'f'){
            
            CameraOffset.add(Anchor, View.POS);
            
                
        }else{
            
        }
    }
    public void mousePressed(MouseEvent Event){
        if(ObstacleButton.getText().equals("Cancel")){
            if(Event.getButton() == 1){
                Point3DM Target = View.getScreenVector(Event.getX(), Event.getY());
                Obstacles.add(new Obstacle(NewObstacle));
                ShapesToPrint.add(NewObstacle);
                prepareToPlace();
            }
            if(Event.getButton() == 3){
                Rotation3D R = new Rotation3D();
                R.rotateAround('z', Math.PI * 15 / 180);
                Rotation3D.multiply(R, NewObstacle.Rotation, NewObstacle.Rotation);
            }
        }
        
        
        
      
    }
    public void mouseReleased(MouseEvent Event){
        if(Event.getButton() == 1){
  
        }
        if(Event.getButton() == 3){

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
        Point3DM Target = View.getScreenVector(Event.getX(), Event.getY());
        
        NewObstacle.Center.POSX = View.POS.POSX - Target.POSX / Target.POSZ * View.POS.POSZ;
        NewObstacle.Center.POSY = View.POS.POSY - Target.POSY / Target.POSZ * View.POS.POSZ;
            
        
        
       // setMouseControls(); 
    }
    public void mouseDragged(MouseEvent Event){
       // Area.setBounds(0, 0, Event.getX(), Event.getY());
       // Area.updatePicture();
        //View.setArea(0, 0, Event.getX(), Event.getY(), View.Magnification);
   
       // setMouseControls();
    }
    public void mouseWheelMoved(java.awt.event.MouseWheelEvent WheelEvent) {
        setPoints(NewObstacle, NewObstacle.Form, NewObstacle.ArgA * (1 + 0.1 * WheelEvent.getWheelRotation()), NewObstacle.ArgB * (1 + + 0.1 * WheelEvent.getWheelRotation()), NewObstacle.ArgC * (1 + + 0.1 * WheelEvent.getWheelRotation()));
    } 

       
    
}