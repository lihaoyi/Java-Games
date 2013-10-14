

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
public class ChatClient {
    
    
    public static void main(String[] args) {
        new ChatUser();
    }
    
}

class ChatUser extends JFrame implements KeyListener, ActionListener{
    
    int X = 400;
    int Y = 400;
    Dimension ScreenSize = GUI.getScreenSize();
    

    Timer Stopwatch = new Timer(20, this);
    
    Link UpLink;
    String Label;
    
    int Offset = 11000;
    DataArray InputData = new DataArray(32, true);
    DataArray OutputData = new DataArray(128, true);
    boolean ClearChat;
    
    
    JTextArea MainTextArea = new JTextArea("");
    JScrollPane MainTextPane = new JScrollPane(MainTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    JTextArea TypeTextArea = new JTextArea("");
    JScrollPane TypeTextPane = new JScrollPane(TypeTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    JTextArea ListTextArea = new JTextArea("");
    JScrollPane ListTextPane = new JScrollPane(ListTextArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    JButton SendButton = new JButton("SEND");
    
    
    public void initComponents(){
        Container C = this.getContentPane();
        C.setBackground(Color.gray);
         

        
        Insets I = this.getInsets();
        System.out.println(I);
        X = X - I.left - I.right;
        Y = Y - I.top - I.bottom;

        int B = 10;
        int Boundary = 270;
        
        MainTextArea.setBounds(B, B, X - 2*B - (Y - Boundary), Boundary - 2*B);
        MainTextPane.setBounds(B, B, X - 2*B - (Y - Boundary), Boundary - 2*B);
        C.add(MainTextPane);
        
        
        TypeTextArea.setBounds(B, Boundary + B, X - 2*B - (Y - Boundary), Y - Boundary - 2*B);
        TypeTextPane.setBounds(B, Boundary + B, X - 2*B - (Y - Boundary), Y - Boundary - 2*B);        
        TypeTextArea.addKeyListener(this);
        C.add(TypeTextPane);
        
        ListTextArea.setBounds(X - (Y - Boundary) + B, B, (Y - Boundary) - 2*B, Boundary - 2*B);     
        ListTextPane.setBounds(X - (Y - Boundary) + B, B, (Y - Boundary) - 2*B, Boundary - 2*B);     
        C.add(ListTextPane);
       
        SendButton.setBounds(X - (Y - Boundary) + B, Boundary + B, (Y - Boundary) - 2*B, (Y - Boundary) - 2*B);
        SendButton.addActionListener(this);
        C.add(SendButton);
        
        Font StandardFont = new Font("COURIER", Font.PLAIN, 12);
        MainTextArea.setFont(StandardFont);
        TypeTextArea.setFont(StandardFont);
        ListTextArea.setFont(StandardFont);
       
    }
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
                    String MyLabel = null;
                    do{
                         MyLabel = JOptionPane.showInputDialog(null, "SELECT LABEL");
                    }while(MyLabel == null || MyLabel.length() == 0);
                    Label = MyLabel;
                    
                    OutputData.reset();
                    
                    OutputData.writeString(Label);  
                    UpLink.Output.write(OutputData.getLengthBytes(), 0, 4);
                    UpLink.Output.write(OutputData.Buffer, 0, OutputData.Count);
                    UpLink.Output.flush();
                    OutputData.reset();
                    
                    return;
                }catch(Exception e){
                    System.out.println("LINKAGE FAILED " + e);
                }
            }
            
        }
        System.out.println("UNABLE TO CONNECT");
        System.exit(0);
        
        
    }
  
    
    
    public ChatUser(){
        //BASIC INITIALIZATION
        super("Chat User");
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        contentArea.setLayout(null);
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
    
        this.setLocation(ScreenSize.width / 2 - X / 2, ScreenSize.height / 2 - Y / 2);
        this.addKeyListener(this);

      //  initSockets();
        init();
        Stopwatch.start();
        
        //ADD CONTENT PANE AND PACK
        
        
        this.show();
        initComponents();
        this.setContentPane(contentArea);
        //this.pack();   
    }

    //EVENT TRIGGERS
    boolean CTRL = false;
    public void keyPressed(KeyEvent e){
        System.out.println(e.getKeyCode());
        if(e.getKeyCode() == 10){
            if(CTRL){
                TypeTextArea.append("\n");

            }else{
                sendOutput();
            }
        }
        if(e.getKeyCode() == 17){
            CTRL = true;
        }
        
    }
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode() == 17){
            CTRL = !true;
        }
    }
 
    
    public void keyTyped(KeyEvent e){

    }
    
 
    public void printInput(){
        try{
            
            while(UpLink.Input.available() > 0){
                
                System.out.println("RECIEVING");
                
                InputData.reset();
                UpLink.Input.read(InputData.Length, 0, 4);        
                InputData.ensureCapacity(InputData.getLengthInt());
                
                UpLink.Input.read(InputData.Buffer, 0, InputData.getLengthInt());    
                boolean Changed = InputData.readBoolean();
                if(Changed){
                    
                    int Count = InputData.readInt();
                    if(Count != 0){
                        ListTextArea.setText("");
                    }
                    for(int i = 0; i < Count; i++){
                        ListTextArea.append(InputData.readString());
                    }

                    MainTextArea.setText(InputData.readString());
                }
            }   
                
        }catch(IOException e){
            System.out.println("IOEXCEPTION " + e);
        }
    }
    public void sendOutput(){
        try{
            OutputData.reset();
            OutputData.writeString(TypeTextArea.getText());
            
            UpLink.Output.write(OutputData.getLengthBytes(), 0, 4);
            UpLink.Output.write(OutputData.Buffer, 0, OutputData.Count);
            ClearChat = true;
        }catch(IOException e){
            System.out.println(e);
            System.exit(0);
        }
    }
    public void actionPerformed(ActionEvent e){
    
        if(e.getSource() == Stopwatch){
            if(ClearChat){
                TypeTextArea.setText("");
                ClearChat = false;
            }
        
        //  sendOutput();
            printInput();
        }else if(e.getSource() == SendButton){
            sendOutput();
        }
       
    }

}


