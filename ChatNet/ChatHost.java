
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
public class ChatHost{
    public static void main(String[] args){
        new ChatServer();
    }
    
}
class ChatServer implements KeyListener, ActionListener{
    
    int X = 320;
    int Y = 320;
    
    Dimension ScreenSize = GUI.getScreenSize();

    Timer Stopwatch = new Timer(20, this);

    int Offset = 11000;
    NetworkVector Waiters = new NetworkVector();
    int PortCount = 10;

    NetworkVector Links = new NetworkVector();
    DataArray InputData = new DataArray(128, true);
    DataArray OutputData = new DataArray(10, true);
    
    JTextArea MainTextArea = new JTextArea("");
    JScrollPane MainTextPane = new JScrollPane(MainTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
  
    
    public void init(){       
        for(int i = 0; i < PortCount; i++){
            UtilsNet.newWaiter(i + Offset, Waiters);
        }
        
    }
  
    
    public ChatServer(){
      
        init();
        
        Stopwatch.start();
        
        //ADD CONTENT PANE AND PACK
     
        
    }

    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){

        switch(e.getKeyCode()){
            case 32:  //Space
            
                break;
            case 10:  //Enter
                
                break;
            

        }
        
    }
    public void keyReleased(KeyEvent e){

    }
 
    
    public void keyTyped(KeyEvent e){

    }
    
 
    public void doOutput(){
        
        OutputData.reset();
        DataArray DO = OutputData;
        DO.writeBoolean(Changed);
        if(Changed){
            DO.writeInt(Links.size());
            for(int i = 0; i < Links.size(); i++){
                DO.writeString((String)Links.link(i).Pointer + "\n");
            }
            DO.writeString(MainTextArea.getText());    
        }
        
        
        for(int k = Links.size() - 1; k >= 0; k--){
            try{
                
                if(Links.link(k).Output == null){
                    continue;
                }
                
                //print buffers length
                Links.link(k).Output.write(OutputData.getLengthBytes(), 0, 4);  
               
                //print buffer
                Links.link(k).Output.write(OutputData.Buffer, 0, OutputData.Count);
                //flush writer
                Links.link(k).Output.flush();
      
                
            }catch(IOException e){
                System.out.println(e);
                if(e instanceof SocketException){
                    Link DeadLink = Links.link(k);
                    killLink(DeadLink);
                    UtilsNet.newWaiter(DeadLink.Connection.getLocalPort(), Waiters);
                    MainTextArea.append(DeadLink.Pointer + " has left.\n");
                    Changed = true;
                }
                
    
            }
        }
        
    
    }
    
    public void killLink(Link Target){
        Target.close();
        Links.remove(Target);
        
        System.out.println("KILLED LINK AT " + Target.Connection.getLocalPort());
    }

    public void getInputs(){
        for(int i = 0; i < Links.size(); i++){
            try{
                while(Links.link(i).Input.available() > 0){
                    Changed = true;
                    Link L = Links.link(i);
                    
                    
                    InputData.reset();
                    //read and ensure sufficient length of buffer
                    L.Input.read(InputData.Length, 0, 4);
                    InputData.ensureCapacity(InputData.getLengthInt());
                    //read to buffer
                    L.Input.read(InputData.Buffer, 0, InputData.getLengthInt());
                    
                    //transfer from buffer to Text
                    
                    String[] Chunks = InputData.readString().split("\n");
                    for(int j = 0; j < Chunks.length; j++){
                        
                        MainTextArea.append((j == 0 ? L.Pointer + ": ": GUI.returnBlanks(((String)L.Pointer).length() + 2)) + Chunks[j] + "\n");
                    }
                    
                }
                
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }
    Color[] ColorIndex = {Color.red, Color.blue, Color.green, Color.yellow, Color.cyan, Color.magenta};
    public void handleThreads(){
        
        for(int i = 0; i < Waiters.size(); i++){//for every waiter
            try{
                Parallel Waiter = Waiters.parallel(i);
                if(Waiter.S != null){//if new link is 
                    
                    Link L = null;
                    try{//try to establish link and remove waiter
                        L = new Link(Waiter.S);
                        System.out.println("LINK CONNECTED "+ Waiter.Port);
                        
                        Waiters.remove(Waiter);
                        Links.add(L);
                        
                        InputData.reset();
                        L.Input.read(InputData.Length, 0, 4);
                        InputData.ensureCapacity(InputData.getLengthInt());
                        L.Input.read(InputData.Buffer);
                        L.Pointer = InputData.readString();
                        
                        Changed = true;
                    }catch(IOException e){
                        System.out.println("LINKING FAILED " + Waiter.Port + "\t" + e);
                    }

                    
                }
            }catch(Exception e){
                System.out.println(e);
            }
        }

    }
    boolean Changed;
    public void actionPerformed(ActionEvent e){
  
        Changed = false;

        getInputs();
        handleThreads();
        
    
        doOutput();
    
 
    }
    //Time Considerations
}

