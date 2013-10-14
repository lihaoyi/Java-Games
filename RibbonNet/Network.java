
import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.zip.*;
class UtilsNet{
    public static void pause(Object O, int Delay){
        try{
            synchronized(O){
                O.wait(Delay);
            }
        }catch(InterruptedException e){
            System.out.println(O + " INTERRUPTED " + e);
        }
    }
    public static void newWaiter(int Port, NetworkVector Waiters){
        Parallel Waiter = new Parallel(Port);
        Waiter.start();
        Waiters.add(Waiter);
        System.out.println("NEW WAITER AT " + Port);
    }
    static byte[] Length = new byte[4];
    public static int getLengthInt(){
        return (int)(
        ((Length[0] & 0xFF) << 24) + 
        ((Length[1] & 0xFF) << 16) +
        ((Length[2] & 0xFF) <<  8) + 
        ((Length[3] & 0xFF) <<  0)
        );
    }   
    public static byte[] getLengthBytes(int Count){
        Length[0] = (byte)((Count >>> 24) & 0xFF);
        Length[1] = (byte)((Count >>> 16) & 0xFF);
        Length[2] = (byte)((Count >>>  8) & 0xFF);
        Length[3] = (byte)((Count >>>  0) & 0xFF);
        return Length;
    }
    public static void preparePacket(DatagramPacket P, DataArray Input){
        P.setData(Input.Buffer, 0, Input.Count);
    }
    public static void preparePacket(DatagramPacket P, Client C){
        P.setSocketAddress(C.FullAddress);
    }
    public static Deflater Compressor = new Deflater(Deflater.BEST_COMPRESSION);
    public static Inflater Decompressor = new Inflater();
    
}
class Client implements ActionListener{
    InetAddress Address;
    int Port;
    SocketAddress FullAddress;
    int Index;
    Object Pointer;
    Timer TimeoutTimer;
    boolean TimedOut;
    public Client(int I, InetAddress A, int P, int TimeOut){
        Index = I;
        Address = A;
        Port = P;
        FullAddress = new InetSocketAddress(A, P);
        TimeoutTimer = new Timer(TimeOut, this);
        TimeoutTimer.setRepeats(false);
    }
    public Client(InetAddress A, int P, int TimeOut){
        this(0, A, P, TimeOut);
    }
    public Client(InetAddress A, int P){
        this(0, A, P, -1);
    }
    public boolean isTimedOut(){
        return TimedOut;
    }
    public void countdown(){
        TimeoutTimer.restart();
        
    }
    public void actionPerformed(ActionEvent E){
        TimedOut = true;
    }
    public String toString(){
        return Address + ":" + Port + ":" + TimeoutTimer.getDelay();
    }
}



interface UDPListener{
    abstract void receiveUDP(DataHandler D);
        
    
    
}
class DataHandler implements Runnable{
    UDPListener Base;
    DatagramSocket DS;
    DatagramPacket Receiver;
    DatagramPacket Sender;
    int Port;
    boolean Active;
    public DataHandler(UDPListener B) throws SocketException{
        Base = B;
        DS = new DatagramSocket();
        Port = DS.getLocalPort();
        Receiver = new DatagramPacket(new byte[8192], 0);
        Receiver.setLength(8192);
        Sender = new DatagramPacket(new byte[8192], 0);
        Receiver.setLength(8192);
        Active = true;
    }
    
    public DataHandler(UDPListener B, int P) throws SocketException{
        Base = B;
        Port = P;
        DS = new DatagramSocket(Port);
        Receiver = new DatagramPacket(new byte[8192], 0);
        Receiver.setLength(8192);
        Sender = new DatagramPacket(new byte[8192], 0);
        Receiver.setLength(8192);
        Active = true;
    }
    public void send() throws IOException{
        DS.send(Sender);
    }
    public void start(){
        new Thread(this).start();
    }
    public void close(){
        Active = false;
        DS.close();
    }
    public void run(){
        while(Active){
            try{
           //     System.out.println(Receiver.getLength());
                //System.out.println("WAITING FOR INPUT");
                DS.receive(Receiver);
                if(Active){
                    Base.receiveUDP(this);
                }
                
                
            }catch(IOException e){}
            
        }
    }
    
}





class Parallel implements Runnable{
    int Port;
    Socket S;

    
    public Parallel(int P){
        Port = P;

    }
    public Thread start(){
        Thread T = new Thread(this);
        T.start();
        return T;
    }
    public void run(){
        try{
            
            S = null;
            System.out.println("STARTING WAITER: " + Port);
            ServerSocket SS = new ServerSocket(Port);
            Socket TempS = SS.accept();
            SS.close();
            
            
            int Result = TempS.getInputStream().read();
            if(Result == 1){
                System.out.println("GOT CONNECTION: " + Port);
                S = TempS;
                S.setTcpNoDelay(!true); 
            }
            if(Result == 0){
                System.out.println("GOT PING: " + Port);
                TempS.close();
                start();
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
        System.out.println("WAITER DONE " + Port);
    }
    
}

class Scanner{
    int Start;
    int Length;
    InetAddress Address;
    Socket S;
    ScanPort[] ScanPorts;
    Thread[] ScanThreads;
    int TimeOut;
    
    public Scanner(InetAddress Add, int SPort, int PortL){
        Start = SPort;
        Length = PortL;
        Address = Add;
        ScanPorts = new ScanPort[PortL];
        ScanThreads = new Thread[PortL];
        for(int i = Start; i < Start + Length; i++){
            ScanPorts[i - Start] = new ScanPort(this, Address, i);
        }
    } 
    public Socket scan(int TO){
        TimeOut = TO;
        
        S = null;
        //prepare ports and begin scan
        for(int i = Start; i < Start + Length; i++){
            ScanPorts[i - Start].reset();
            ScanThreads[i - Start] = new Thread(ScanPorts[i - Start]);
            
        }   
        
        
            
        
            
        
        try{
            synchronized(this){
                for(int i = Start; i < Start + Length; i++){
                    ScanThreads[i - Start].start();
                }
                System.out.println("SCAN STARTED");
                this.wait(TimeOut);
            }
        }catch(InterruptedException e){
            System.out.println("INTERRUPTED EXCEPTION " + e);
        }
        
        
        
   
        //return S. either working socket or null
        System.out.println("FINISHED SCANNING");
        return S;
    }

}

class ScanPort implements Runnable{
    Scanner Base;
    InetAddress Target;
    int Port;
    boolean Done = false;
    
    public ScanPort(Scanner B, InetAddress T, int P){
        Base = B;
        Target = T;
        Port = P;
    }
    public void reset(){
        Done = false;
    }
    public void run(){
        try{
            
            //UtilsNet.pause(this, 10);
            
            System.out.println("SCANNING PORT " + Port);
            //try connecting
            Socket S = new Socket();
            S.connect(new InetSocketAddress(Target, Port), Base.TimeOut);
            
            Done = true;
            if(Base.S != null){//if you have connected but someone else has connected first
                System.out.println("CLOSING PORT " + Port);
                S.getOutputStream().write(0);
                S.close();    
            }else if(S.isConnected()){//if you have connected first
                Base.S = S;
                System.out.println("RETURNING PORT " + Port);
                
                S.getOutputStream().write(1);
                synchronized(Base){//unblock scanner
                    Base.notify();
                    System.out.println("BASE NOTIFIED");
                }
            }
        }catch(IOException e){//if you have failed to connect
            
            Done = true;
            System.out.println("LINK FAILED AT PORT " + Port + " " + e);
             
            //if everybody else has failed and you fail
            for(int i = 0; i < Base.ScanPorts.length; i++){  
                if(!Base.ScanPorts[i].Done){
                    return;
                }
            }
            
            //unblock scanner
            synchronized(Base){
                Base.notify();
            }
        }
        
    }
}


class Link{
    Socket Connection;
    OutputStream Output;
    InputStream Input;
    Object Pointer; //Arbitrary pointer to associate socket with object
    public void close(){ //Closes socket after it is finished 
        try{
            Connection.close();
            Output.close();
            Input.close();
        }catch(IOException e){
            System.out.println("ERROR CLOSING LINK: " + e);
        }
    }
    public Link(InetAddress Address, int Port) throws IOException{ //tries to create link from target address
        
        Connection = new Socket(Address, Port);
        Connection.setTcpNoDelay(true);
        
        Output = Connection.getOutputStream();
        
        Input = Connection.getInputStream();
    }
    public Link(Socket C) throws IOException{ //Tries to create link from socket (use with Class Parallel to get socket)
        
        Connection = C;
        Connection.setTcpNoDelay(true);
        Output = Connection.getOutputStream();
        
        Input = Connection.getInputStream();
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
    public Parallel parallel(int A){
        return (Parallel)elementAt(A);
    }
    public Thread thread(int A){
        return (Thread)elementAt(A);
    }
    public ScanPort scanPort(int A){
        return (ScanPort)elementAt(A);
    }
    public Client client(int A){
        return (Client)elementAt(A);
    }
}
