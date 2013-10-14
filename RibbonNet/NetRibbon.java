
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
public class NetRibbon{
    public static void main(String[] args){
        
            
            
        new RibbonGame();
        
    }

}
class SnakeVector extends Vector{
    public Point2D.Float point(int A){
        return (Point2D.Float)elementAt(A);
    }
    public Snake snake(int A){
        return (Snake)elementAt(A);
    }
    public Apple apple(int A){
        return (Apple)elementAt(A);
    }
}
class Snake{

    SnakeVector Points = new SnakeVector();
    SnakeVector Normals = new SnakeVector();
    Point2D.Float BackNormal = new Point2D.Float();
    Point[] LeftPoints = new Point[16];
    Point[] RightPoints = new Point[16];
    
    
    float Width = 5;
    float ActualLength;
    float PointStep = 5;
    byte HeadLength = 2;
    
    float Length;
    float Speed;
    float TurboSpeed;
    float Theta;
    
    int N;
    int WaveCounter = 0;
    boolean Left;
    boolean Right;
    boolean Forward;
    boolean Back;

    boolean Dead = false;
    float TurnSpeed = (float)Math.PI / 1.5f;
    
    SnakeVector Home;
    Color Tint;
    public Snake(SnakeVector home, float length, float speed, Rectangle Boundary, Point2D.Float Position, Color tint){
        this(home, length, speed, (float)Math.atan2(Boundary.height / 2 - Position.y, Boundary.width / 2 - Position.x), Position, tint);
    }
    public Snake(SnakeVector home, float length, float speed, float theta, Point2D.Float Position, Color tint){
        super();
        Home = home;
        Length = length;
        Speed = speed;
        TurboSpeed = 0.3f * Speed;
        Theta = theta;
        Tint = tint;
        Points.add(Position);
        Points.add(Position.clone());
        Normals.add(new Point2D.Float());
        Normals.add(new Point2D.Float());
        Points.point(0).x += 0.1 * Math.cos(Theta);
        Points.point(0).y += 0.1 * Math.sin(Theta);
        for(int i = 0; i < LeftPoints.length; i++){
            LeftPoints[i] = new Point();
            RightPoints[i] = new Point();
        }
        N = Points.size() - 1;
    }
    public void ensureCapacity(){
       while(N >= LeftPoints.length){
            LeftPoints = (Point[])GUI.doubleArray(LeftPoints);
            RightPoints = (Point[])GUI.doubleArray(RightPoints);
            for(int j = 0; j < LeftPoints.length; j++){
                LeftPoints[j] = (LeftPoints[j] == null ? new Point() : LeftPoints[j]);
                RightPoints[j] = (RightPoints[j] == null ? new Point() : RightPoints[j]);
            }
            
        } 
    }
    public void kill(){
        Dead = true;
        Speed = 0;
        TurboSpeed = 0;
    }
    public void move(double FramesPerSecond){
      //  Turn = GUI.cap(-1, Turn, 1);
        Theta += TurnSpeed / FramesPerSecond * ((Left ? -1 : 0) + (Right ? 1 : 0));
        
        float DS = Speed;
        if(Back){DS -= TurboSpeed;}
        if(Forward){DS += TurboSpeed;};
        DS /= FramesPerSecond;
        
        WaveCounter += DS;
        Points.point(0).x += (float)(DS * Math.cos(Theta));
        Points.point(0).y += (float)(DS * Math.sin(Theta));
        
        
        

        if(Points.point(0).distance(Points.point(1)) >= PointStep){
            Points.insertElementAt(Points.point(0).clone(), 0);
            Normals.insertElementAt(Normals.point(0).clone(), 0);
            Points.point(0).x += 0.1 * Math.cos(Theta);
            Points.point(0).y += 0.1 * Math.sin(Theta);
        }
        

        
        if(Dead){
            Length -= 8;
        }
        shorten();
        N = Points.size() - 1;
        calcLength();
        if(Length <= 0){
            Home.remove(this);
        }
      //  printPoints();
    }
    
    public void thicken(){
        
        ensureCapacity();
        
        for(int i = 1; i < Points.size(); i++){
            float DX = Points.point(i).x - Points.point(i - 1).x;
            float DY = Points.point(i).y - Points.point(i - 1).y;
            float FX = DY / (float)Math.sqrt(DX * DX + DY * DY) * Width /* (float)Math.sin((PointStep * i + WaveCounter) / Math.PI / 8)*/;
            float FY = -DX / (float)Math.sqrt(DX * DX + DY * DY) * Width /* (float)Math.sin((PointStep * i + WaveCounter) / Math.PI / 8)*/;
            Normals.point(i).x = (float)(FX / GUI.pythagoras(FX, FY));
            Normals.point(i).y = (float)(FY / GUI.pythagoras(FX, FY));
            
            LeftPoints[i].x = (int)(Points.point(i).x + FX);
            LeftPoints[i].y = (int)(Points.point(i).y + FY);
            RightPoints[i].x = (int)(Points.point(i).x - FX);
            RightPoints[i].y = (int)(Points.point(i).y - FY);
            if(i == 1){
                i--;

                LeftPoints[i].x = (int)(Points.point(i).x + FX);
                LeftPoints[i].y = (int)(Points.point(i).y + FY);
                RightPoints[i].x = (int)(Points.point(i).x - FX);
                RightPoints[i].y = (int)(Points.point(i).y - FY);
                i++;
            }
        }
        BackNormal.x = (float)(RightPoints[N].x - RightPoints[N - 1].x / GUI.pythagoras(RightPoints[N].x - RightPoints[N - 1].x, RightPoints[N].y - RightPoints[N - 1].y));
        BackNormal.y = (float)(RightPoints[N].y - RightPoints[N - 1].y / GUI.pythagoras(RightPoints[N].x - RightPoints[N - 1].x, RightPoints[N].y - RightPoints[N - 1].y));
    }
    public void calcLength(){
        ActualLength = 0;
        for(int i = 1; i < Points.size(); i++){
            ActualLength += Points.point(i).distance(Points.point(i - 1));
        }
    }
    public void printPoints(){
        for(int j = 0; j < Points.size(); j++){
            System.out.println(Points.point(j));
        }
    }
    public void shorten(){
        calcLength();
        float LengthDefect = ActualLength - Length;
        if(LengthDefect > 0){
            if(Points.size() > 2 && LengthDefect > Points.point(Points.size() - 2).distance(Points.point(Points.size() - 1))){
                Points.removeElementAt(Points.size() - 1);
                Normals.removeElementAt(Points.size() - 1);
                shorten();
            }else{
                
                float DX = Points.point(Points.size() - 2).x  - Points.point(Points.size() - 1).x;
                float DY = Points.point(Points.size() - 2).y  - Points.point(Points.size() - 1).y;
                
                float Multiplier = (float)(LengthDefect / Math.sqrt(DX * DX + DY * DY));
                Points.point(Points.size() - 1).x += DX * Multiplier;
                Points.point(Points.size() - 1).y += DY * Multiplier;
            }
        }
        return;
    }
    public boolean checkCollision(Rectangle Target){
        
        
        if(!Target.contains(LeftPoints[0]) || !Target.contains(RightPoints[0])){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean checkCollision(Snake Target){

        Line2D.Float LineA = new Line2D.Float();
        Line2D.Float LineB = new Line2D.Float();
        LineA.setLine(LeftPoints[0], RightPoints[0]);
        
        for(int j = 0; j < Math.min(HeadLength, N / 2); j++){
            int K = 0;
            if(j != 0){
                K = (j % 2 == 0 ? j / 2 : (j + 1) / 2);
                LineA.setLine(LeftPoints[K], LeftPoints[K - 1]);
            }
            for(int i = 1; i <= Target.N; i++){
                if(Target == this && Math.abs(i - K) <= 2){
                    continue;
                }
                LineB.setLine(Target.RightPoints[i], Target.RightPoints[i - 1]);
                if(LineB.intersectsLine(LineA) && GUI.length(LineA) > 0 && GUI.length(LineB) > 0){
               //     System.out.println(LineB.getP1() + "\t" + LineB.getP2() + "\n" + LineA.getP1() + "\t" + LineA.getP2());
               //     System.out.println("RI" + i);

                    return true;

                }
                LineB.setLine(Target.LeftPoints[i], Target.LeftPoints[i - 1]);
                if(LineB.intersectsLine(LineA) && GUI.length(LineA) > 0 && GUI.length(LineB) > 0){
               //     System.out.println(LineB.getP1() + "\t" + LineB.getP2() + "\n" + LineA.getP1() + "\t" + LineA.getP2());
                  //  System.out.println("LI" + i);

                    return true;
                }

            }
            LineB.setLine(Target.LeftPoints[Target.N], Target.RightPoints[Target.N]);
            if(LineB.intersectsLine(LineA) && GUI.length(LineA) > 0 && GUI.length(LineB) > 0){
             //   System.out.println("BI");
                return true;
            }
            LineB.setLine(Target.LeftPoints[0], Target.RightPoints[0]);
            if(Target != this && LineB.intersectsLine(LineA) && GUI.length(LineA) > 0 && GUI.length(LineB) > 0){
             //   System.out.println("BI");
                return true;
            }
            
        }
        return false;
    }
    public void printHalf(Graphics2D Painter){
        Painter.setColor(Tint);
        for(int i = 1; i <= N; i++){
            
            Painter.drawLine((int)Points.point(i).x / 2, (int)Points.point(i).y / 2, (int)Points.point(i - 1).x / 2, (int)Points.point(i - 1).y / 2);
            //Painter.drawLine((int)Points.point(i).x, (int)Points.point(i).y, (int)Points.point(i - 1).x, (int)Points.point(i - 1).y);
        }
    }
    public void print(Graphics2D Painter){
        Painter.setColor(Tint);
        Painter.drawLine(LeftPoints[0].x, LeftPoints[0].y, RightPoints[0].x, RightPoints[0].y);
        Painter.drawLine(LeftPoints[N].x, LeftPoints[N].y, RightPoints[N].x, RightPoints[N].y);
        for(int i = 1; i <= N; i++){
            Painter.drawLine(LeftPoints[i].x, LeftPoints[i].y, LeftPoints[i - 1].x, LeftPoints[i - 1].y);
            Painter.drawLine(RightPoints[i].x, RightPoints[i].y, RightPoints[i - 1].x, RightPoints[i - 1].y);
            //Painter.drawLine((int)Points.point(i).x, (int)Points.point(i).y, (int)Points.point(i - 1).x, (int)Points.point(i - 1).y);
        }
    }
    public void toBytes(DataArray B){
        B.writeInt(Tint.getRGB());
        B.writeInt(N);
        Point CPL;
        Point LPL;
        CPL = LeftPoints[0];
        Point CPR;
        Point LPR;
        CPR = RightPoints[0];
        
        B.writeShort(CPL.x);
        B.writeShort(CPL.y);
        B.writeShort(CPR.x);
        B.writeShort(CPR.y);
        
        for(int i = 1; i <= N; i++){
            if(i % 2 == 0 && i != N)continue;
            LPL = CPL;
            CPL = LeftPoints[i];
            LPR = CPR;
            CPR = RightPoints[i];
            
            B.writeByte((byte)(CPL.x - LPL.x));
            B.writeByte((byte)(CPL.y - LPL.y));
            B.writeByte((byte)(CPR.x - LPR.x));
            B.writeByte((byte)(CPR.y - LPR.y));
            
            
        }
        
        
  
    }
    public void fromBytes(DataArray B) throws IOException{
        Tint = Color.getColor("", B.readInt());
        N = B.readInt();
        ensureCapacity();
        LeftPoints[0].x = B.readShort();
        LeftPoints[0].y = B.readShort();
        RightPoints[0].x = B.readShort();
        RightPoints[0].y = B.readShort();
        int K = 1;
        byte[] Data = new byte[4];
        for(int i = 1; i <= N; i++){
            if(i % 2 == 0 && i != N)continue;
            
            
            LeftPoints[K].x = B.readByte() + LeftPoints[K - 1].x;
            LeftPoints[K].y = B.readByte() + LeftPoints[K - 1].y;
            RightPoints[K].x = B.readByte() + RightPoints[K - 1].x;
            RightPoints[K].y = B.readByte() + RightPoints[K - 1].y;
            K++;
        }
        N = K - 1 ;
        
        
  
        
    }


        
        
  
        
    
}
class Apple{
    Point2D.Float Position;
    Point2D.Float Velocity;
    Point2D.Float NextPosition;
    Color Tint;
    int Points;
    int Radius = 4;
    SnakeVector Home;
    public void toBytes(DataArray B){
        B.writeInt(Tint.getRGB());
        B.writeFloat(Position.x);
        B.writeFloat(Position.y);
    }
    public void fromBytes(DataArray B){
        Tint = Color.getColor("", B.readInt());
        Position.x = B.readFloat();
        Position.y = B.readFloat();
    }
    public Apple(SnakeVector home, int Level, Rectangle Boundary){
        Home = home;
        int V = 0;
        switch(Level){
            case 1:
                Tint = Color.red;
                Points = 15;
                V = 0;
                break;
            case 2:
                Tint = Color.blue;
                Points = 45;
                V = 50;
                break;
            case 3:
                Tint = Color.cyan;
                Points = 75;
                V = 100;
                break;
        }
        Velocity = new Point2D.Float((GUI.randomInt(0, 1) * 2 - 1) * V, (GUI.randomInt(0, 1) * 2 - 1) * V);
        Position = new Point2D.Float(GUI.randomInt(Radius + Boundary.x, Boundary.width + Boundary.x - Radius), GUI.randomInt(Radius + Boundary.y, Boundary.height + Boundary.y - Radius));
        NextPosition = new Point2D.Float(Position.x, Position.y);
    }
    public Apple(SnakeVector home, Point2D.Float Pos, float V, int points, Color tint){
        Home = home;
        Position = Pos;
        Velocity = new Point2D.Float((GUI.randomInt(0, 1) * 2 - 1) * V, (GUI.randomInt(0, 1) * 2 - 1) * V);
        Tint = tint;
        Points = points;
        NextPosition = new Point2D.Float(Pos.x, Pos.y);
    }
    public void move(float FramesPerSecond){
        
        Position.x += Velocity.x / FramesPerSecond;
        Position.y += Velocity.y / FramesPerSecond;
        NextPosition.x = Position.x + Velocity.x / FramesPerSecond;
        NextPosition.y = Position.y + Velocity.y / FramesPerSecond;
    }
    public void kill(){
        Home.remove(this);
    }
    public void tryBoundaries(Rectangle Boundary){
        if(Position.x - Radius < Boundary.x){
            Velocity.x = Math.abs(Velocity.x);
        }
        if(Position.y - Radius < Boundary.y){
            Velocity.y = Math.abs(Velocity.y);
        }
        if(Position.x + Radius > Boundary.width + Boundary.x){
            Velocity.x = -Math.abs(Velocity.x);
        }
        if(Position.y + Radius > Boundary.height + Boundary.y){
            Velocity.y = -Math.abs(Velocity.y);
        }
    }
    public void tryEat(Snake Target){
        
        if(Line2D.Float.ptSegDist(Target.LeftPoints[0].x, Target.LeftPoints[0].y, Target.RightPoints[0].x, Target.RightPoints[0].y, Position.x, Position.y) < Radius){
            Home.remove(this);
            Target.Length += Points;
        }
        if(Line2D.Float.ptSegDist(Target.LeftPoints[0].x, Target.LeftPoints[0].y, Target.RightPoints[0].x, Target.RightPoints[0].y, NextPosition.x, NextPosition.y) < Radius){
            Home.remove(this);
            Target.Length += Points;
        }
        for(int i = 0; i < Target.HeadLength; i++){
            if(Line2D.Float.ptSegDist(Target.LeftPoints[i].x, Target.LeftPoints[i].y, Target.LeftPoints[i + 1].x, Target.LeftPoints[i+1].y, Position.x, Position.y) < Radius){
                Home.remove(this);
                Target.Length += Points;
            }
            if(Line2D.Float.ptSegDist(Target.RightPoints[i].x, Target.RightPoints[i].y, Target.RightPoints[i+1].x, Target.RightPoints[i+1].y, Position.x, Position.y) < Radius){
                Home.remove(this);
                Target.Length += Points;
            }
            if(Line2D.Float.ptSegDist(Target.LeftPoints[i].x, Target.LeftPoints[i].y, Target.LeftPoints[i + 1].x, Target.LeftPoints[i+1].y, NextPosition.x, NextPosition.y) < Radius){
                Home.remove(this);
                Target.Length += Points;
            }
            if(Line2D.Float.ptSegDist(Target.RightPoints[i].x, Target.RightPoints[i].y, Target.RightPoints[i+1].x, Target.RightPoints[i+1].y, NextPosition.x, NextPosition.y) < Radius){
                Home.remove(this);
                Target.Length += Points;
            }
        }
    }
    public void tryCollision(Snake Target){
        
        Line2D.Float TargetLine = new Line2D.Float();
        float Distance = -1;
        int Index = -1;
        //Check Left
        for(int i = Target.HeadLength; i < Target.Points.size(); i++){
            //TargetLine.setLine(Target.LeftPoints[i - 1], Target.LeftPoints[i]);
            double D = Line2D.ptSegDist(Target.LeftPoints[i - 1].x, Target.LeftPoints[i - 1].y, Target.LeftPoints[i].x, Target.LeftPoints[i].y, Position.x, Position.y);
            if(D < Radius && (D < Distance || Index == -1)){
                Index = i;
                Distance = (float)D;
            }
            D = Line2D.ptSegDist(Target.LeftPoints[i - 1].x, Target.LeftPoints[i - 1].y, Target.LeftPoints[i].x, Target.LeftPoints[i].y, NextPosition.x, NextPosition.y);
            if(D < Radius && (D < Distance || Index == -1)){
                Index = i;
                Distance = (float)D;
            }
        }
        //Check Right
        for(int i = Target.HeadLength; i < Target.Points.size(); i++){
            //TargetLine.setLine(Target.RightPoints[i - 1], Target.RightPoints[i]);
            double D = Line2D.ptSegDist(Target.RightPoints[i - 1].x, Target.RightPoints[i - 1].y, Target.RightPoints[i].x, Target.RightPoints[i].y, Position.x, Position.y);
            if(D < Radius && (D < Distance || Index == -1)){
                Index = i;
                Distance = (float)D;
            }
            D = Line2D.ptSegDist(Target.RightPoints[i - 1].x, Target.RightPoints[i - 1].y, Target.RightPoints[i].x, Target.RightPoints[i].y, NextPosition.x, NextPosition.y);
            if(D < Radius && (D < Distance || Index == -1)){
                Index = i;
                Distance = (float)D;
            }
        }
        //Check Back
        double D = Line2D.ptSegDist(Target.RightPoints[Target.N].x, Target.RightPoints[Target.N].y, Target.LeftPoints[Target.N].x, Target.LeftPoints[Target.N].y, Position.x, Position.y);
        if(D < Radius && (D < Distance || Index == -1)){
            Index = -2;
            Distance = (float)D;
        }
        D = Line2D.ptSegDist(Target.RightPoints[Target.N].x, Target.RightPoints[Target.N].y, Target.LeftPoints[Target.N].x, Target.LeftPoints[Target.N].y, NextPosition.x, NextPosition.y);
        if(D < Radius && (D < Distance || Index == -1)){
            Index = -2;
            Distance = (float)D;
        }
        
        if(Index != -1){
          //  System.out.println("BOUNCE");
            float x;
            float y;
            if(Index == -2){
                TargetLine.setLine(Target.Points.point(Target.N), Target.Points.point(Target.N));
                x = Target.BackNormal.x;
                y = Target.BackNormal.y;
            }else{
                TargetLine.setLine(Target.Points.point(Index - 1), Target.Points.point(Index));
                x = Target.Normals.point(Index).x;
                y = Target.Normals.point(Index).y;
            }
            

            float Dot = x * Velocity.x + y * Velocity.y;
          //  System.out.println(Dot);
            //Dot = (float)GUI.cap(-Math.sqrt(Velocity.x * Velocity.x + Velocity.y * Velocity.y), Dot, 0);
            Velocity.x -= 2 * x * Dot; Velocity.y -= 2 * y * Dot;
            
            
        }
    }
    public void printHalf(Graphics2D Painter){
        Painter.setColor(Tint);
        Painter.drawRect((int)Position.x / 2 - Radius/2, (int)Position.y / 2 - Radius/2, Radius, Radius);
        
    }
    public void print(Graphics2D Painter){
        Painter.setColor(Tint);
        Painter.drawLine((int)Position.x - Radius, (int)Position.y - Radius, (int)Position.x + Radius, (int)Position.y + Radius);
        Painter.drawLine((int)Position.x + Radius, (int)Position.y - Radius, (int)Position.x - Radius, (int)Position.y + Radius);
    }
    
}

class RibbonGame implements KeyListener, ActionListener, UDPListener{
    
    final int X = 400;
    final int Y = 300;
    
    //Dimension ScreenSize = GUI.getScreenSize();

    Timer Stopwatch = new Timer(20, this);
    float FramesPerSecond = 50;
    TimeKeeper Time = new TimeKeeper(50, false);
    JCanvas Area;
    
    boolean Paused = false;
    
    Rectangle Boundary = new Rectangle(0, 0, X, Y);
    
    
    SnakeVector Snakes = new SnakeVector();
    SnakeVector Apples = new SnakeVector();
   
    
    int Offset = 11000;
    
    int TimeoutDelay = 3000;
    DataHandler Handler;
    
    NetworkVector Clients = new NetworkVector();
    
    DataArray InputData = new DataArray(128, true);
    DataArray OutputData = new DataArray(10, true);
    
    public void init(){       
        try{
            Handler = new DataHandler(this, Offset);
        }catch(SocketException e){
            System.out.println("ERROR: CANNOT MAKE LISTENER " + e);
        }
        Handler.start();
    
        
    }
    
    public RibbonGame(){
        //BASIC INITIALIZATION
        
      //  super("Ribbon Host");
        
           
           
        //this.setSize(X, Y);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setResizable(false);
        //SET CONTENT PANE
        //Container contentArea = getContentPane();
        //contentArea.setLayout(null);
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        //Area = new JCanvas(); 
        //Area.setBounds(0, 0, X, Y);
        
     //   this.setLocation(ScreenSize.width / 2 - X / 2, ScreenSize.height / 2 - Y / 2);
        //contentArea.add(Area);
        //Area.init(BufferedImage.TYPE_INT_RGB);
        Boundary.setBounds(0 + 4, 0 + 4, 800 - 8, 600 - 8);
        
        
        
        //ADD CONTENT PANE AND PACK
    //    this.setUndecorated(true);
      //  this.setContentPane(contentArea);
        //this.setVisible(true);  
     
        //this.addKeyListener(this);

         //  initSockets();
           init();
           Stopwatch.start();
    }

    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){

        switch(e.getKeyCode()){
            case 32:  
                System.out.println();
                System.out.println("BYTES STREAMED: " + OutputData.Count + "\t");
                System.out.println("MS/FRAME: " + (int)(1000.0/FramesPerSecond) + "\t");
                for(int i = 0; i < Clients.size(); i++){
                    System.out.println(Clients.client(i));
                }
                System.out.println();

                break;
            case 10: 
                Paused = !Paused;
                break;
            

        }
        
    }
    public void keyReleased(KeyEvent e){

    }
 
    
    public void keyTyped(KeyEvent e){

    }
    
 
    
    

    public Snake newSnake(int Index){
        
        Snake S = new Snake(Snakes, 150, 55, Boundary, new Point2D.Float(GUI.randomInt(10, 800-10), GUI.randomInt(10, 600-10)), ColorIndex[Index % ColorIndex.length]);
        
        Snakes.add(S);   
        return S;
    }
    public void getControls(){
        
    }
    Color[] ColorIndex = {Color.red, Color.blue, Color.green, Color.yellow, Color.cyan, Color.magenta};
    public void doOutput(DataHandler Sender){
        
        DataArray DO = OutputData;
        DO.reset();
        DO.writeInt(Apples.size()); //Apple Count
        for(int i = 0; i < Apples.size(); i++){
            Apples.apple(i).toBytes(DO); //Apples
        }
        DO.writeInt(Snakes.size()); //Snake Count
        for(int i = 0; i < Snakes.size(); i++){
            Snakes.snake(i).toBytes(DO); //Snakes
        }
        for(int i = 0; i < Clients.size(); i++){
            DO.writeInt(Clients.client(i).Index); //Index
            DO.writeInt(((Snake)Clients.client(i).Pointer).Tint.getRGB()); //Colour
            System.out.println(DO.Count);
            DO.compress(UtilsNet.Compressor);
            DatagramPacket Packet = Sender.Sender;
            System.out.println(DO.Count);
            UtilsNet.preparePacket(Packet, DO);
            
            UtilsNet.preparePacket(Packet, Clients.client(i));
            
            
            try{
                //System.out.println("SENDING OUTPUT " + Packet.getLength());
                Handler.send();
                
            }catch(IOException e){
                //System.out.println("SENDING FAILED " + e);
            }
            
            try{
                DO.decompress(UtilsNet.Decompressor);
            }catch(Exception e){
                System.out.println("EXCEPTION");
            }
            DO.backtrack(4);
            DO.backtrack(4);
        }
    }
    public void killTimeouts(){
        for(int i = Clients.size() - 1; i >= 0; i--){
            Client C = Clients.client(i);
            if(C.isTimedOut()){
                System.out.println("CLIENT TIMED OUT " + C.Address + "\t" + C.Port);
                Clients.remove(C);
                ((Snake)C.Pointer).kill();
            }
        }
    }
    public void handleInput(DatagramPacket Packet){
        InputData.getDataFrom(Packet.getData());
        
        int Index = InputData.readInt();
        
        for(int i = 0; i < Clients.size(); i++){
            
            Client C = Clients.client(i);
          
            //if(Packet.getSocketAddress().equals(C.FullAddress)){
            if(Packet.getAddress().equals(C.Address) && Packet.getPort() == C.Port){
                C.countdown();
                
                Snake S = (Snake)C.Pointer;
                
                S.Forward = InputData.readBoolean();
                S.Back = InputData.readBoolean();
                S.Left = InputData.readBoolean();
                S.Right = InputData.readBoolean();
                
                return;
            }
        }
        
        System.out.println("NEW CLIENT FOUND " + Packet.getAddress() + "\t" + Packet.getPort());
        
        for(Index = 0;; Index++){
            boolean Y = false;
            for(int j = 0; j < Clients.size(); j++){
                if(Index == Clients.client(j).Index){
                    Y = true;   
                }
            }   
            
            if(!Y){
                break;
            }
        }
        
        
        
        
        Client NewClient = new Client(Index, Packet.getAddress(), Packet.getPort(), TimeoutDelay);
        NewClient.Pointer = newSnake(Index);
        Clients.add(NewClient);
        //send back index to new client
        
    }
    public void receiveUDP(DataHandler D){
        handleInput(D.Receiver);
    }
    public void actionPerformed(ActionEvent e){
        
            
            

    //    Area.Painter.setColor(Color.black);
    //    Area.Painter.fillRect(0, 0, X, Y);
        FramesPerSecond = (float)Time.FramesPerSecond;



        getControls();


        if(!Paused){
            if(GUI.randomInt(1, 5) > Apples.size()){
                Apple B = new Apple(Apples, GUI.randomInt(1, 3), Boundary);
                Apples.add(B);
            }
            for(int i = 0; i < Snakes.size(); i++){        
                Snakes.snake(i).move(FramesPerSecond);

            }
            for(int i = 0; i < Snakes.size(); i++){        
                Snakes.snake(i).thicken();
            }
            for(int i = 0; i < Apples.size(); i++){
                Apples.apple(i).move(FramesPerSecond);
            }
            for(int i = 0; i < Apples.size(); i++){
                Apples.apple(i).tryBoundaries(Boundary);
                for(int j = 0; j < Snakes.size(); j++){
                    Apples.apple(i).tryCollision(Snakes.snake(j));
                }
            }
            for(int j = 0; j < Snakes.size(); j++){
                for(int i = Apples.size() - 1; i >= 0; i--){;
                    Apples.apple(i).tryEat(Snakes.snake(j));
                }
            }
            for(int i = 0; i < Snakes.size(); i++){
                if(Snakes.snake(i).checkCollision(Boundary)){
                    Snakes.snake(i).kill();
                }
            }
            for(int i = 0; i < Snakes.size(); i++){
                for(int j = 0; j < Snakes.size(); j++){
                    Point CollisionData = null;

                    if(Snakes.snake(i).checkCollision(Snakes.snake(j))){
                        Snakes.snake(i).kill();
                    }

                }
            }
        }
        /*
        for(int i = 0; i < Snakes.size(); i++){

            Snakes.snake(i).printHalf(Area.Painter);
        }
        for(int j = 0; j < Apples.size(); j++){
            Apples.apple(j).printHalf(Area.Painter);
        }
        Area.Painter.setColor(Color.white);
        Area.Painter.drawRect(1, 1, X - 3, Y - 3);
      */
        killTimeouts();
        doOutput(Handler);

        //Area.paintComponent(this.getGraphics());
        Time.doTime();
        
    }
    //Time Considerations
}

