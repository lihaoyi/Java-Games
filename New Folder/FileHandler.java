import java.io.*;
import java.util.*;

public class FileHandler{
    
    File Target;
    String Address;
    BufferedReader FileRead;
    
    BufferedWriter FileWrite;
    public FileHandler(String Add, boolean Read, boolean Write){ //Constructor
        try{
            if(Read)FileRead = new BufferedReader(new FileReader(Add));
            if(Write)FileWrite = new BufferedWriter(new FileWriter(Add));
            Address = Add;
        }catch(Exception E){
            System.out.println(E + "CONSTRUCTOR");
            System.exit(0);
        }
    }
    
    
    public static void writeFile(String FileName, Object[] Input){ //Write to arbitrary file; List; Independent
        try{
            BufferedWriter Writer = new BufferedWriter(new FileWriter(FileName));
            write(Writer, Input);
            Writer.close();
        }catch(Exception e){System.out.println(e + "A"); System.exit(0);}
    }
    public void writeFile(Object Input){ //Write to this  file; String; Cumalative
        try{
            write(FileWrite, Input);
        }catch(Exception e){
            System.out.println(e + "AA");
        }
    }
    public void writeFile(Object[] Input){ //Write to this file; List; Cumalative
        try{
            write(FileWrite, Input);
        }catch(Exception e){System.out.println(e + "B"); System.exit(0);}
    }
    public void closeWrite(){ //Close this writing stream
        try{
            FileWrite.close();
        }catch(Exception e){System.out.println(e + "C"); System.exit(0);}    
    }
    public void openWrite(){ //Opens new write stream
        try{
            FileWrite = new BufferedWriter(new FileWriter(Address));
        }catch(Exception e){System.out.println(e + "D"); System.exit(0);}    
    }
    public static void write(BufferedWriter Writer, Object Input){
        try{
            Writer.write("" + Input);
            Writer.newLine();
            Writer.flush();
        }catch(Exception e){
            System.out.println(e + "DD");
        }
    }
    public static void write(BufferedWriter Writer, Object[] Input){ //Writing method; List of strings; Base Method
        try{
            
            for(int i = 0; i < Input.length; i++){
                Writer.write("" + Input[i]);
                Writer.newLine();
                
            }
            Writer.flush();
        }catch(Exception e){
            System.out.println(e + "DDD");
        }
    }
    public static void writeFile(String FileName, Object[][] Input, String Spacer){ //Writing Method; Grid of strings; Independent
        String[] FinalInput = new String[Input.length];
        for(int i = 0; i < Input.length; i++){
            String Line = "";
            for(int j = 0; j < Input[i].length; j++){
                if(j > 0){
                    Line = Line + Spacer;
                }
                Line = Line + Input[i][j];
                
            }
            FinalInput[i] = Line;
        }
        
        
        writeFile(FileName, FinalInput);
    }
    public static void writeFile(String FileName, double[][] Input, String Spacer){ //Writing Method; Grid of Doubles; Independent
        String[] FinalInput = new String[Input.length];
        for(int i = 0; i < Input.length; i++){
            String Line = "";
            for(int j = 0; j < Input[i].length; j++){
                if(j > 0){
                    Line = Line + Spacer;
                }
                Line = Line + Input[i][j];
                
            }
            FinalInput[i] = Line;
        }
        
        
        writeFile(FileName, FinalInput);
    }
    public static void writeFile(String FileName, int[][] Input, String Spacer){ //Writing Method; Grid of Doubles; Independent
        String[] FinalInput = new String[Input.length];
        for(int i = 0; i < Input.length; i++){
            String Line = "";
            for(int j = 0; j < Input[i].length; j++){
                if(j > 0){
                    Line = Line + Spacer;
                }
                Line = Line + Input[i][j];
                
            }
            FinalInput[i] = Line;
        }
        
        
        writeFile(FileName, FinalInput);
    }
    public static String[] readFile(String FileName){ //Read arbitrary file; List; Independent
        try{
            BufferedReader Reader = new BufferedReader(new FileReader(FileName));
            String[] Output = read(Reader);
            Reader.close();
            return Output;
            
        }catch(Exception e){System.out.println(e + "EE"); System.exit(0); return null;}
    }
    public String[] readFile(){ //Read this file; List; Cumulative
        try{
            return read(FileRead);
        }catch(Exception e){System.out.println(e + "E"); System.exit(0); return null;}
    }
    public void closeRead(){ //Close read stream
        try{
            FileRead.close();
        }catch(Exception e){System.out.println(e + "F"); System.exit(0);}
    }
    public void openRead(){ //Opens new read stream
        try{
            FileRead = new BufferedReader(new FileReader(Address));
        }catch(Exception e){System.out.println(e + "G"); System.exit(0);}    
    }
    static Vector InputLines = new Vector();
    public static String[] read(BufferedReader FileIn){ //Read list of strings; Independent
        try{
            
            
            InputLines.clear();
            
            while(true){
                String Line = FileIn.readLine();
                if(Line != null){
                    InputLines.add((String)Line);
                }else{
                    break;
                }
            }
            String[] InputData = new String[InputLines.size()];
            for(int i = 0; i < InputLines.size(); i++){
                InputData[i] = (String)InputLines.elementAt(i);
            }
            
            return InputData;
        }catch(Exception e){
            System.out.println("EXCEPTION: " + e);
            System.exit(0);
            return null;
        }
        
    }
    public static String[][] readFile(String FileName, String Spacer){ //Read arbitrary file; Grid of strings; Independent
        String[] Output = readFile(FileName);
        String[][] FinalOutput = new String[Output.length][];
        for(int i = 0; i < Output.length; i++){
            FinalOutput[i] = Output[i].split(Spacer);
        }
        return FinalOutput;
    }
    public static int[][] readInts(String FileName, String Spacer){ //Read arbitrary file; Grid of doubles; Independent
        String[][] MyInput = readFile(FileName, Spacer);
        int[][] MyOutput = new int[MyInput.length][];
        for(int i = 0; i < MyInput.length; i++){
            MyOutput[i] = new int[MyInput[i].length];
            for(int j = 0; j < MyInput[i].length; j++){
                
                MyOutput[i][j] = Integer.parseInt(MyInput[i][j]);
            }
        }
        return MyOutput;
    }
    public static double[][] readNumbers(String FileName, String Spacer){ //Read arbitrary file; Grid of doubles; Independent
        String[][] MyInput = readFile(FileName, Spacer);
        double[][] MyOutput = new double[MyInput.length][];
        for(int i = 0; i < MyInput.length; i++){
            MyOutput[i] = new double[MyInput[i].length];
            for(int j = 0; j < MyInput[i].length; j++){
                MyOutput[i][j] = Double.parseDouble(MyInput[i][j]);
            }
        }
        return MyOutput;
    }
    
    
    
}
