
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

class RibbonClientGame extends JFrame implements KeyListener, ActionListener{
    
    final int X = 800;
    final int Y = 600;
    Dimension ScreenSize = GUI.getScreenSize();
    

    Timer Stopwatch = new Timer(20, this);
    double FramesPerSecond = 50;
    TimeKeeper Time = new TimeKeeper(50, false);
    JCanvas Area;
    
    Link UpLink;

    Color Tint;
    boolean Left;
    boolean Right;
    boolean Forward;
    boolean Back;
    int Offset = 11000;
    DataArray InputData = new DataArray(32, true);
    DataArray OutputData = new DataArray(128, true);
    public void init(){
        String TargetHost = JOptionPane.showInputDialog(null, "CHOOSE HOST");
        InetAddress Target = null;

        try{
            Target = InetAddress.getByName(TargetHost);
        }catch(Exception e){
            System.out.println("ATTEMPT TO GET ADDRESS FAILED " + e);
            System.exit(0);
        }
       
        System.out.println("Attempting to connect to " + Target);
        
        for(int i = 1; i <= 5; i++){
            Scanner MyScanner = new Scanner(Target, Offset, 4);
            Socket S = MyScanner.scan(5000);
            System.out.println("SCAN " + i + " COMPLETE");
            System.out.println(S);
            if(S != null){
                try{
                    UpLink = new Link(S);
                    System.out.println("CONNECTING USING PORT " + S.getPort());
                    return;
                }catch(Exception e){
                    System.out.println("LINKAGE FAILED " + e);
                }
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
        this.show();
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
            while(UpLink.Input.available() > 0){
                while(UpLink.Input.available() > 5000){
                    UpLink.Input.read(InputData.Length, 0, 4);        
                    UpLink.Input.skip(InputData.getLengthInt());
                }
                UpLink.Input.read(InputData.Length, 0, 4);        
                System.out.println("TAKING" + InputData.getLengthInt());
                
                InputData.ensureCapacity(InputData.getLengthInt());
                UpLink.Input.read(InputData.Buffer, 0, InputData.getLengthInt());    
            }
            
            InputData.reset();
            
            int Count = InputData.readInt();

            for(int i = 0; i < Count; i++){
                APrinter.fromBytes(InputData);
                APrinter.print(Area.Painter);
            }
            Count = InputData.readInt();
            for(int i = 0; i < Count; i++){
                SPrinter.fromBytes(InputData);
                SPrinter.print(Area.Painter);
            }
            Tint = new Color(InputData.readInt());
        }catch(IOException e){
            System.out.println("PRINT ERROR" + e);
            System.exit(0);
        }
    }
    
    public void sendOutput(){
        try{
            OutputData.reset();
            OutputData.writeBoolean(Forward);
            OutputData.writeBoolean(Back);
            OutputData.writeBoolean(Left);
            OutputData.writeBoolean(Right);
            UpLink.Output.write(OutputData.getLengthBytes(), 0, 4);
            UpLink.Output.write(OutputData.Buffer, 0, OutputData.Count);
            
        }catch(IOException e){
            System.out.println(e);
            System.exit(0);
        }
    }
    public void actionPerformed(ActionEvent e){
 
        System.out.println();
        FramesPerSecond = Time.FramesPerSecond;

        
        sendOutput();
        printInput();
            

        Area.Painter.setColor(Color.white);
        Area.Painter.drawRect(1, 1, X - 3, Y - 3);
        Area.Painter.drawRect(3, 3, X - 7, Y - 7);
        Area.Painter.setColor(Tint);
        Area.Painter.drawRect(0, 0, X - 1, Y - 1);
        Area.Painter.drawRect(2, 2, X - 5, Y - 5);
        Area.paintComponent(this.getGraphics());
        
        Time.doTime();
    }

}


