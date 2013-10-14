
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
public class RibbonClient {
    
    /** Creates a new instance of RibbonClient */
    public static void main(String[] args) {
        new RibbonClientGame();
    }
    
}

class RibbonClientGame extends JFrame implements KeyListener, ActionListener, UDPListener{
    
    final int X = 800;
    final int Y = 600;
    Dimension ScreenSize = GUI.getScreenSize();
    

    Timer Stopwatch = new Timer(20, this);
    double FramesPerSecond = 50;
    TimeKeeper Time = new TimeKeeper(50, false);
    JCanvas Area;
    


    Color Tint;
    boolean Left;
    boolean Right;
    boolean Forward;
    boolean Back;
    
    DataHandler Handler;
    Client UpLink;
    
    int Index = -1;
    
    int Offset = 11000;
    int BaseTimeout = 3000;
    DataArray InputData = new DataArray(32, true);
    DataArray OutputData = new DataArray(128, true);
    
    public void init(){
        String TargetHost = JOptionPane.showInputDialog(null, "CHOOSE HOST", "www.techcreation.sg");
        try{
            
            UpLink = new Client(0, InetAddress.getByName(TargetHost), Offset, BaseTimeout);
        }catch(UnknownHostException e){
            System.out.println("CANNOT FIND HOST : " + e);
        }
        for(int i = 1; i < 10; i++){
            try{
                Handler = new DataHandler(this);
                Handler.start();
                UpLink.countdown();
                return;
            }catch(SocketException e){
                System.out.println("ERROR: CANNOT MAKE LISTENER " + e);
            }
        }
        
        
        System.out.println("UNABLE TO CONNECT");
        System.exit(0);
        
        
    }
  
    
    
    public RibbonClientGame(){
        //BASIC INITIALIZATION
        super("Ribbon Client");
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

      //  initSockets();
        init();
        Stopwatch.start();
        
        //ADD CONTENT PANE AND PACK
        this.setUndecorated(true);
        this.setContentPane(contentArea);
        this.setVisible(true);
        //this.pack();   
    }

    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){
        switch(e.getKeyCode()){
            case 38: Forward = true; break; //u
            case 40: Back = true; break; //d
            case 37: Left = true; break; //l
            case 39: Right = true; break; //r

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
    
 
    Snake SPrinter = new Snake(null, 0, 0, 0, new Point2D.Float(), Color.black);
    Apple APrinter = new Apple(null, new Point2D.Float(), 0, 0, Color.black);
    public void printInput(){
        try{
            Area.Painter.setColor(Color.black);
            Area.Painter.fillRect(0, 0, X, Y);
            Area.Painter.setColor(Color.white);
            Area.Painter.drawRect(1, 1, X - 3, Y - 3);
            Area.Painter.drawRect(3, 3, X - 7, Y - 7);
            Area.Painter.setColor(Tint);
            Area.Painter.drawRect(0, 0, X - 1, Y - 1);
            Area.Painter.drawRect(2, 2, X - 5, Y - 5);
            
            InputData.reset();
            InputData.writeBytes(Handler.Receiver.getData(), 0, Handler.Receiver.getLength());
            UpLink.countdown();
            try{
                InputData.decompress(UtilsNet.Decompressor);
            }catch(Exception O){}
            
            
            
            InputData.reset();
            
            
            int Count = InputData.readInt(); //Apple Count
            System.out.println(Count);
            for(int i = 0; i < Count; i++){
                APrinter.fromBytes(InputData); //Apples
                APrinter.print(Area.Painter); //Print
            }
            Count = InputData.readInt(); //Snake Count
            for(int i = 0; i < Count; i++){
                SPrinter.fromBytes(InputData); //Snakes
                SPrinter.print(Area.Painter); //Print
            }
            Index = InputData.readInt(); //Index
            Tint = new Color(InputData.readInt()); //Color
            
        }catch(IOException e){
            System.out.println("PRINT ERROR" + e);
            System.exit(0);
        }
    }
    public void killTimeout(){
        if(UpLink.isTimedOut()){
            System.exit(0);
        }
    }
    public void sendOutput(){
        
        try{
            OutputData.reset();
            OutputData.writeInt(Index);
            OutputData.writeBoolean(Forward);
            OutputData.writeBoolean(Back);
            OutputData.writeBoolean(Left);
            OutputData.writeBoolean(Right);
            

            
            DatagramPacket Packet = Handler.Sender;
            UtilsNet.preparePacket(Packet, OutputData);
            UtilsNet.preparePacket(Packet, UpLink);
            
            //System.out.println("SENDING OUTPUT " + Packet.getLength());
            Handler.send();
            
            
            
 
        }catch(IOException e){
            System.out.println(e);
            System.exit(0);
        }
    }
    public void paint(Graphics G){
        
    }
    public void receiveUDP(DataHandler D){
        printInput();
        Area.paintComponent(this.getGraphics());
    }
    public void actionPerformed(ActionEvent e){
        
        
        FramesPerSecond = Time.FramesPerSecond;

        killTimeout();
        sendOutput();
        System.out.println("A");





        Time.doTime();
        
    }

}


