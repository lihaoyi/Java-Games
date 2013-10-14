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
import java.net.*;

public class MultiPong{
    public static void main(String[] args){
        
            
            
        new MPong();
        
    }
    
}

class MPong extends JFrame implements KeyListener, ActionListener{
    
    final int X = GUI.X;
    final int Y = GUI.Y;
    
    
    public void bounce(boolean First){
        GUI.playSound(First, "TAH.wav", 0, 2);
        if(Host){
            SoundOutput += 'b';
        }
    }
    public void cheer(boolean First, int Switch){
        GUI.playSound(First, "CHEERS" + Switch + ".wav", 0, 2);
        if(Host && Switch == 1){
            SoundOutput += 'c';
        }
        if(Host && Switch == 2){
            SoundOutput += 'C';
        }
    }

    boolean Up;
    boolean Down;
    boolean Left;
    boolean Right;
    

    JCanvas Area;
    
    boolean Host;
 
    //Host Stuff
    
    Parallel[] Waiters = new Parallel[5];
    NetworkVector Links = new NetworkVector();
    PointVector Points = new PointVector();

    
    String SoundOutput = "";
    //Server Sockets:   10000->infinity
    //SquareCountXYXYXY
    //Client Stuff
    Link ClientLink;
    InetAddress Target;
    //Client Sockets:   20000->infinity
    public void init(int Index, Socket S){
        
        Points.add(new Point2D.Double());
        try{
            Link C = new Link(S);
            Links.add(C);
        }catch(IOException e){
            System.out.println("IOEXCEPTION: " + e);
        }
    }
    public void initSounds(){
        bounce(false);
        cheer(false, 1);
        cheer(false, 2);
        
    }
    public void initSockets(){
        int A = JOptionPane.showConfirmDialog(null, "Host?");
        Host = A == 0;
        if(Host){
            for(int i = 0; i < Waiters.length; i++){
                Waiters[i] = new Parallel(this, 10000 + i, i);
                new Thread(Waiters[i]).start();
            }
            this.setTitle("HOST");
        }else if(!Host){
            
            String T = JOptionPane.showInputDialog(this, "Select Host");
            if(T == null){
                System.exit(0);
            }
            try{
                Target = InetAddress.getByName(T);
            }catch(UnknownHostException e){
                System.out.println("UNKNOWN HOST");
            }
            for(int i = 0; i < 5; i++){
                try{
                    ClientLink = new Link(new Socket(Target, 10000 + i));
                    
                    
                    break;
                }catch(Exception e){
                    System.out.println("SOCKET FAILED: " + e);
                }
            }
            
            try{
                Thread.sleep(1000);
                
            }catch(Exception e){}
        }
        
    }
    

    public MPong(){
        //BASIC INITIALIZATION
        super("Window");
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new JCanvas(); 
        Area.setBounds(0, 0, X, Y);
        Area.init(BufferedImage.TYPE_INT_RGB);
        contentArea.add(Area);
        
        this.addKeyListener(this);
        initSounds();

        initSockets();
        Stopwatch.start();
        
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }

    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){
   //     System.out.println(e.getKeyChar() + "\t" + LEFT + "\t" + RIGHT + "\t" + FORWARD + "\t" + BACK);
        System.out.println(e.getKeyCode());
        switch(e.getKeyCode()){
            case 38: Up = true; break;
            case 40: Down = true; break;
            case 37: Left = true; break;
            case 39: Right = true; break;

        }
        
    }
    public void keyReleased(KeyEvent e){
        switch(e.getKeyCode()){
            case 38: Up = !true; break;
            case 40: Down = !true; break;
            case 37: Left = !true; break;
            case 39: Right = !true; break;

        }
    }
 
    
    public void keyTyped(KeyEvent e){

    }
    
 
    public void doOutput(){

        
    }
    public void feedControls(){
  
    }
    public void getControls(){

    }
    public void actionPerformed(ActionEvent e){
        Area.Painter.setColor(Color.black);
        Area.Painter.fillRect(0, 0, X, Y);
      //  System.out.println("GO");
        if(Host){
            
            for(int i = 0; i < Links.size(); i++){
                DataInputStream Input = Links.link(i).Input;
                try{
                    while(Input.available() > 0){
                        Points.point2D(i).x += Input.readDouble();
                        Points.point2D(i).y += Input.readDouble();
                    }
                }catch(Exception E){
                    System.out.println("READING EXCEPTION: " + E);
                    
                    Points.removeElementAt(i);
                    Links.link(i).close();
                    Links.removeElementAt(i);
                }
            }
            for(int i = 0; i < Links.size(); i++){
                try{
                    DataOutputStream Output = Links.link(i).Output;
                    Output.writeInt(Points.size());
                    for(int j = 0; j < Points.size(); j++){
                        Output.writeDouble(Points.point2D(j).x);
                        Output.writeDouble(Points.point2D(j).y);
                    }
                    Output.flush();
                    
                }catch(IOException E){
                    System.out.println("WRITING EXCEPTION: " + E);
                    Points.removeElementAt(i);
                    Links.link(i).close();
                    Links.removeElementAt(i);
                    
                }
                
            }
        }else{
            try{
                double V = (Up ? 1 : 0) + (Down ? -1 : 0);
                double H = (Right ? 1 : 0) + (Left ? -1 : 0);
                ClientLink.Output.writeDouble(H);
                ClientLink.Output.writeDouble(V);
                ClientLink.Output.flush();
            }catch(IOException E){
                System.out.println("WRITING EXCEPTION: " + E);
            }
            try{
                while(ClientLink.Input.available() > 0){
                
                    int K = ClientLink.Input.readInt();
                    for(int i = 0; i < K; i++){
                        if(i >= Points.size()){
                            Points.add(new Point2D.Double());
                        }
                        Points.point2D(i).setLocation(ClientLink.Input.readDouble(), ClientLink.Input.readDouble());
                    }   
                }
            }catch(Exception E){
                System.out.println("READING EXCEPTION: " + E);
            }
            
        }
       // System.out.println(Area.Painter);
        System.out.println(Area.Picture.getRGB(100, 100));
        for(int i = 0; i < Points.size(); i++){
            if(Points.point2D(i) != null){
                Area.Painter.setColor(Color.green);
                Area.Painter.fillRect((int)(X / 2 - 10 + Points.point2D(i).x), (int)(Y / 2 - 10 + Points.point2D(i).y), 20, 20);
                Area.Painter.setColor(Color.black);
            } 
        }
        Area.paintComponent(this.getGraphics());

        doTime();
    }
    //Time Considerations
    boolean CountTime = true;
    double FramesPerSecond = 1000.0 / 20;
    double TotalTime;
    double PreviousTime;
    Timer Stopwatch = new Timer((int)(GUI.Speed * 20), this);
    long FrameCounter;
    NumberQueue DelayList = new NumberQueue(20);
    boolean DiscountDelay = false;
    long Temp = System.currentTimeMillis();
    public void doTime(){
        if(Temp == 0)Temp = System.currentTimeMillis();
      
            
      
        if(DiscountDelay){
            DiscountDelay = false;
        }else{
            DelayList.add(System.currentTimeMillis() - Temp);
        }
        FramesPerSecond = 1000.0 / DelayList.average();
        System.out.println(DelayList.average());
        PreviousTime = TotalTime;
        if(CountTime)TotalTime = TotalTime + 1.0 / FramesPerSecond;
        Temp = System.currentTimeMillis();
    }
}
class Parallel implements Runnable{
    MPong Host;
    int Port;
    Socket S;
    int Index;
    public Parallel(MPong H, int P, int I){
        Host = H;
        Port = P;
        Index = I;
    }
    public void run(){
        try{
            System.out.println("Starting Thread");
            ServerSocket SS = new ServerSocket(Port);
            S = SS.accept();
            
           
            Host.init(Index, S);
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
}

class Link{
    Socket Connection;
    DataOutputStream Output;
    DataInputStream Input;
    public void close(){
        try{
            Connection.close();
            Output.close();
            Input.close();
        }catch(IOException e){
            System.out.println("ERROR CLOSING LINK: " + e);
        }
    }
    public Link(Socket C) throws IOException{
        Connection = C;
        Connection.setTcpNoDelay(false);
        Output = new DataOutputStream(Connection.getOutputStream());
        Input = new DataInputStream(Connection.getInputStream());
    }
}
class PointVector extends Vector{
    public Point2D.Double point2D(int A){
        return (Point2D.Double)elementAt(A);
    }
}
class NetworkVector extends Vector{
    public DataOutputStream dataOutputStream(int A){
        return (DataOutputStream)elementAt(A);
    }
    public DataInputStream dataInputStream(int A){
        return (DataInputStream)elementAt(A);
    }
    public InetAddress inetAddress(int A){
        return (InetAddress)elementAt(A);
    }
    public Link link(int A){
        return (Link)elementAt(A);
    }
}