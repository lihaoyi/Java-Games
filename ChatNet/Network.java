import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.awt.*;
import java.awt.event.*;
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
        System.out.println("SCAN STARTED");
        for(int i = Start; i < Start + Length; i++){
            ScanThreads[i - Start].start();
        }
            
        
            //wait until notified by scanports
        System.out.println("AWAITING RESULTS");
        try{
            synchronized(this){
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
            
            UtilsNet.pause(this, 10);
            
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
class DataArray{
    byte[] Buffer;
    byte[] Length = new byte[4];
    int Count = 0;
    boolean Expand;
    public DataArray(int BufferLength, boolean E){
        Buffer = new byte[BufferLength];
        Expand = E;
    }
    public int getLengthInt(){
        return (int)(
        ((Length[0] & 0xFF) << 24) + 
        ((Length[1] & 0xFF) << 16) +
        ((Length[2] & 0xFF) <<  8) + 
        ((Length[3] & 0xFF) <<  0)
        );
    }   
    public byte[] getLengthBytes(){
        Length[0] = (byte)((Count >>> 24) & 0xFF);
        Length[1] = (byte)((Count >>> 16) & 0xFF);
        Length[2] = (byte)((Count >>>  8) & 0xFF);
        Length[3] = (byte)((Count >>>  0) & 0xFF);
        return Length;
    }
    public void backtrack(int A){
        if(A > Count){
            throw new IllegalArgumentException("CANNOT BACKTRACK PAST ZERO");
        }   
        Count -= A;
    }
    public void reset(){
        Count = 0;
    }
    public void writeBoolean(boolean Input){
        Buffer[Count++] = (Input == true ? (byte)1 : 0);
        
    }
    public void checkExpand(int More){
        while(More + Count >= Buffer.length){
            byte[] NewBuffer = new byte[Buffer.length * 2];
            System.arraycopy(Buffer, 0, NewBuffer, 0, Buffer.length);
            Buffer = NewBuffer;
        }
    }
    public void ensureCapacity(int MaxLength){
        while(MaxLength > Buffer.length){
            byte[] NewBuffer = new byte[Buffer.length * 2];
            System.arraycopy(Buffer, 0, NewBuffer, 0, Buffer.length);
            Buffer = NewBuffer;
        }
    }
    public boolean readBoolean(){
        return (Buffer[Count++] == 1);
        
    }
    public void writeByte(byte Input){
        checkExpand(1);
        Buffer[Count++] = Input;
        
        
    }
    public byte readByte(){
        return Buffer[Count++];
        
    }
    public void writeShort(short Input){   
        checkExpand(2);
        Buffer[Count++] = (byte)((Input >>> 8) & 0xFF);
        Buffer[Count++] = (byte)((Input >>> 0) & 0xFF);
    }
    public void writeShort(int Input){
        writeShort((short)Input);
    }
    public short readShort(){
        return (short)(
        ((Buffer[Count++] & 0xFF) << 8) + 
        ((Buffer[Count++] & 0xFF) << 0)
        );
        
    }
    public void writeInt(int Input){
        checkExpand(4);
        Buffer[Count++] = (byte)((Input >>> 24) & 0xFF);
        Buffer[Count++] = (byte)((Input >>> 16) & 0xFF);
        Buffer[Count++] = (byte)((Input >>>  8) & 0xFF);
        Buffer[Count++] = (byte)((Input >>>  0) & 0xFF);
    }
    public int readInt(){
        return (int)(
        ((Buffer[Count++] & 0xFF) << 24) + 
        ((Buffer[Count++] & 0xFF) << 16) +
        ((Buffer[Count++] & 0xFF) <<  8) + 
        ((Buffer[Count++] & 0xFF) <<  0)
        );
    }
    public void writeLong(long Input){
        checkExpand(8);
        Buffer[Count++] = (byte)((Input >>> 56) & 0xFF);
        Buffer[Count++] = (byte)((Input >>> 48) & 0xFF);
        Buffer[Count++] = (byte)((Input >>> 40) & 0xFF);
        Buffer[Count++] = (byte)((Input >>> 32) & 0xFF);
        Buffer[Count++] = (byte)((Input >>> 24) & 0xFF);
        Buffer[Count++] = (byte)((Input >>> 16) & 0xFF);
        Buffer[Count++] = (byte)((Input >>>  8) & 0xFF);
        Buffer[Count++] = (byte)((Input >>>  0) & 0xFF);
    }
    public long readLong(){
        return (long)(
        ((Buffer[Count++] & 0xFF) << 56) + 
        ((Buffer[Count++] & 0xFF) << 48) +
        ((Buffer[Count++] & 0xFF) << 40) + 
        ((Buffer[Count++] & 0xFF) << 32) +
        ((Buffer[Count++] & 0xFF) << 24) + 
        ((Buffer[Count++] & 0xFF) << 16) +
        ((Buffer[Count++] & 0xFF) <<  8) + 
        ((Buffer[Count++] & 0xFF) <<  0)
        );
    }
    public void writeFloat(float Input){
        checkExpand(4);
        int I = Float.floatToRawIntBits(Input);
        Buffer[Count++] = (byte)((I >>> 24) & 0xFF);
        Buffer[Count++] = (byte)((I >>> 16) & 0xFF);
        Buffer[Count++] = (byte)((I >>>  8) & 0xFF);
        Buffer[Count++] = (byte)((I >>>  0) & 0xFF);
    }
    public float readFloat(){
        return Float.intBitsToFloat(
            ((Buffer[Count++] & 0xFF) << 24) + 
            ((Buffer[Count++] & 0xFF) << 16) +
            ((Buffer[Count++] & 0xFF) <<  8) + 
            ((Buffer[Count++] & 0xFF) <<  0)
        );
    }
    public void writeDouble(double Input){
        checkExpand(8);
        long I = Double.doubleToRawLongBits(Input);
        Buffer[Count++] = (byte)((I >>> 56) & 0xFF);
        Buffer[Count++] = (byte)((I >>> 48) & 0xFF);
        Buffer[Count++] = (byte)((I >>> 40) & 0xFF);
        Buffer[Count++] = (byte)((I >>> 32) & 0xFF);
        Buffer[Count++] = (byte)((I >>> 24) & 0xFF);
        Buffer[Count++] = (byte)((I >>> 16) & 0xFF);
        Buffer[Count++] = (byte)((I >>>  8) & 0xFF);
        Buffer[Count++] = (byte)((I >>>  0) & 0xFF);
    }
    public double readDouble(){
        return Double.longBitsToDouble(
            ((long)(Buffer[Count++] & 0xFF) << 56) + 
            ((long)(Buffer[Count++] & 0xFF) << 48) +
            ((long)(Buffer[Count++] & 0xFF) << 40) + 
            ((long)(Buffer[Count++] & 0xFF) << 32) +
            ((long)(Buffer[Count++] & 0xFF) << 24) + 
            ((long)(Buffer[Count++] & 0xFF) << 16) +
            ((long)(Buffer[Count++] & 0xFF) <<  8) + 
            ((long)(Buffer[Count++] & 0xFF) <<  0)
        );
    }
    public void writeChar(char Input){
        writeShort((short)Input);
    }
    public char readChar(){
        return (char)readShort();
    }
    public void writeString(String Input){
        checkExpand(4 + Input.length() * 2);
        writeInt(Input.length());
        for(int i = 0; i < Input.length(); i++){
            writeShort((short)Input.charAt(i));
        }
    }
    public String readString(){
        StringBuffer S = new StringBuffer(readInt());
        for(int i = 0; i < S.capacity(); i++){
            S.append((char)readShort());
        }
        return new String(S);
    }
}
class DataLink{
    DatagramSocket LinkSocket;
    DatagramPacket LinkPacket;
    
    DataOutputStream Output;
    DataInputStream Input;
    int LocalPort;
    int TargetPort;
    
    InetAddress Target;
    public DataLink(int tLocalPort, InetAddress TargetAddress, int tTargetPort) throws IOException{
        LocalPort = tLocalPort;
        TargetPort = tTargetPort;
        Target = TargetAddress;
        
        LinkPacket = new DatagramPacket(null, 0, Target, TargetPort);
        LinkSocket = new DatagramSocket(LocalPort);
    }
    
    public void close() throws IOException{
        LinkSocket.close();
        Output.close();
        Input.close();
        
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
}
