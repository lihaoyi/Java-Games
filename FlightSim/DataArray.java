import java.io.*;
import java.util.zip.*;

public class DataArray implements DataInput, DataOutput{
    byte[] Buffer;
    
    int Count = 0;
    boolean Expand;
    public void compress(Deflater D){
        D.setInput((byte[])Buffer.clone(), 0, Count);
        D.finish();
        Count = 0;
        for(;;){
            Count = Count + D.deflate(Buffer, Count, Buffer.length - Count);
            if(D.finished()){break;}
            doubleSize();
        }
        D.reset();
    }
    public void decompress(Inflater I) throws DataFormatException{
        I.setInput((byte[])Buffer.clone(), 0, Count);
       // System.out.println(Count);
        Count = 0;
     //   System.out.print(I.getRemaining() + " " + Count + " |");
        for(;;){
            Count = Count + I.inflate(Buffer, Count, Buffer.length - Count);
       //     System.out.print(I.getRemaining() + " " + Count + " |");
            if(I.getRemaining() == 0){break;}
            doubleSize();
        }
        

        I.reset();
    }
    public String toBits(){
        StringBuffer S = new StringBuffer();
        S.append("[");
        for(int i = Count - 1; i >= 0; i--){
            
            for(int j = 8-1; j >= 0; j--){
                S.append(((Buffer[i] & 1 << j) != 0) ? "1" : "0");
            }
             if(i != 0){
                S.append(" ");
            }
        }
        S.append("]");
        return new String(S);
        
    }
    public String toString(){
        StringBuffer S = new StringBuffer();
        for(int i = 0; i < Count; i++){
            if(i > 0)S.append(" ");
            S.append(Buffer[i]);
        }
        return new String(S);
    }
    public DataArray(){
        Buffer = new byte[64];
        Expand = true;
    }
    public DataArray(int BufferLength){
        Buffer = new byte[BufferLength];
        Expand = true;
    }
    public DataArray(int BufferLength, boolean E){
        Buffer = new byte[BufferLength];
        Expand = E;
    }
    
    
    public void getDataFrom(byte[] Target){
        Count = 0;
        Buffer = Target;
    }
    
    public void backtrack(int A){
        if(A > Count){
            throw new IllegalArgumentException("CANNOT BACKTRACK PAST ZERO");
        }   
        Count -= A;
    }
    public int skipBytes(int A){
        if(A + Count > Buffer.length){
            throw new IllegalArgumentException("INSUFFICIENT BYTES TO SKIP PAST");
        }
        Count += A;
        return A;
    }
    public void reset(){
        Count = 0;
    }
    
    private void checkExpand(int More){
        while(More + Count >= Buffer.length){
            byte[] NewBuffer = new byte[Buffer.length * 2];
            System.arraycopy(Buffer, 0, NewBuffer, 0, Buffer.length);
            Buffer = NewBuffer;
        }
    }
    private void doubleSize(){
        byte[] NewBuffer = new byte[Buffer.length * 2];
            System.arraycopy(Buffer, 0, NewBuffer, 0, Buffer.length);
            Buffer = NewBuffer;
    }
    public void ensureCapacity(int MaxLength){
        while(MaxLength > Buffer.length){
            byte[] NewBuffer = new byte[Buffer.length * 2];
            System.arraycopy(Buffer, 0, NewBuffer, 0, Buffer.length);
            Buffer = NewBuffer;
        }
    }
    
    
    public void write(byte[] Source){
        writeBytes(Source, 0, Source.length);
    }
    public void write(byte[] Source, int Offset, int wLength) throws java.io.IOException {
        writeBytes(Source, Offset, wLength);
    }
    public void writeBytes(byte[] Source, int Offset, int wLength){
        checkExpand(wLength);
        System.arraycopy(Source, Offset, Buffer, Count, wLength);
        Count += wLength;
    }
    public void readBytes(byte[] Source, int Offset, int wLength){
        System.arraycopy(Buffer, Count, Source, Offset, wLength);
        Count += wLength;
    }
    public void readFully(byte[] Target, int Offset, int wLength) throws java.io.IOException {
        System.arraycopy(Buffer, Count, Target, Offset, wLength);
        Count += Target.length;
    }
    public void readFully(byte[] Target){
        System.arraycopy(Buffer, Count, Target, 0, Target.length);
        Count += Target.length;
    }
     
    public void write(int Input){
        writeByte((byte)Input);
    }
    public void writeByte(int Input){
        writeByte((byte)Input);
    }
    public void writeByte(byte Input){
        checkExpand(1);
        Buffer[Count++] = Input;
    }
    
    public byte readByte(){
        return Buffer[Count++];
        
    }
    
    
    public void writeBoolean(boolean Input){
        checkExpand(1);
        Buffer[Count++] = (Input == true ? (byte)1 : 0);
        
    }
    public boolean readBoolean(){
        return (Buffer[Count++] == 1);
        
    }
    
    
    public void writeShort(int Input){
        writeShort((short)Input);
    }
    public void writeShort(short Input){   
        checkExpand(2);
        Buffer[Count++] = (byte)((Input >>> 8) & 0xFF);
        Buffer[Count++] = (byte)((Input >>> 0) & 0xFF);
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
    
    
    public void writeChar(int Input){
        writeChar((char)Input);
    }
    public void writeChar(char Input){
        writeShort((short)Input);
    }
    public char readChar(){
        return (char)readShort();
    }
    
    
    public void writeBytes(String Input){
        writeString(Input);
    }
    public void writeChars(String Input){
        writeString(Input);
    }
    public void writeUTF(String Input){
        writeString(Input);
    }
    public void writeString(String Input){
        checkExpand(4 + Input.length() * 2);
        writeInt(Input.length());
        for(int i = 0; i < Input.length(); i++){
            writeShort((short)Input.charAt(i));
        }
    }
    public String readUTF(){
        return readString();
    }
    public String readLine(){
        return readString();
    }
    public String readString(){
        StringBuffer S = new StringBuffer(readInt());
        for(int i = 0; i < S.capacity(); i++){
            S.append((char)readShort());
        }
        return new String(S);
    }
    
    public int readUnsignedByte(){
        throw new IllegalArgumentException("UNDEFINED OPERATION");
    }
    
    public int readUnsignedShort(){
        throw new IllegalArgumentException("UNDEFINED OPERATION");
    }
}