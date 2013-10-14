import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

public class WaveSound extends Thread{
    
    
    public static void main(String[] args) throws IOException{
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Start");
        
        String[] Names = FileHandler.readFile(WorkingDirectory + "\\FlightSound\\SoundData.txt");
        String[] Thresholds = new String[Names.length];
        String[] Bases = new String[Names.length];
        
        System.out.println(Names.length);
        for(int i = 0; i < Names.length; i++){
            WaveSound MySound = new WaveSound(WorkingDirectory + "\\FlightSound\\" + Names[i] + ".wav");
            System.out.println(Names[i]);
            for(int j = 0; j < 80; j+=1){
                
                System.out.println("-" + j + " decibels");
                MySound.playDeci(-j);
                String S = stdin.readLine();
                if(S.length() > 0){
                    if(Bases[i] == null){
                        
                        Bases[i] = ""+j;
                        System.out.println(Names[i] + " Base at " + Bases[i] + " decibels");
                    }else if(Thresholds[i] == null){
                        
                        Thresholds[i] = ""+j;
                        System.out.println(Names[i] + " threshold at " + Thresholds[i] + " decibels");
                        break;
                    }else{
                        
                    }
                }
            }
        }
        String[] Output = new String[Names.length];
        for(int i = 0; i < Names.length; i++){
            Output[i] = "";
            Output[i] += Names[i];
            Output[i] += "\t"+Bases[i];
            Output[i] += "\t"+Thresholds[i];
            
        }
        FileHandler.writeFile(WorkingDirectory + "\\FlightSound\\SoundData.txt", Output);
        System.out.println("End");
    }


    
    private static final String WorkingDirectory = System.getProperty("user.dir");
    private static final int BufferSize = 1024;
    private static final Stack<byte[]> Store = new Stack<byte[]>();

    
    public final String FileName;
    public final File SoundFile;
    public float BaseDecibels;
    public float CutOff;
    public float MaxDecibels;
    public float MinDecibels;
    
    private AudioFormat audioFormat;
    private final DataLine.Info info;
    
    public String toString(){
        return "WaveSound[" + FileName + " | Base Decibel Offset = " + BaseDecibels + " | Attenuation Cutoff = " + CutOff;
    }
    public WaveSound(String F, double DB){
        this(F, DB, -1000000000);
    }
    public WaveSound(String F, double DB, double CT){
        this(F);
        BaseDecibels = (float)DB;
        CutOff = (float)CT;
        try{
            SourceDataLine DataLine = null;
            
            DataLine = (SourceDataLine) AudioSystem.getLine(info);
            DataLine.open(audioFormat);
            
            
            
            FloatControl FC = (FloatControl)DataLine.getControl(FloatControl.Type.MASTER_GAIN);
            
            MinDecibels = FC.getMinimum();
            MaxDecibels = FC.getMaximum();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        
        
    }
    public WaveSound(String F){
        FileName = F;
        SoundFile = new File(FileName);
        
        try{
            audioFormat = AudioSystem.getAudioInputStream(SoundFile).getFormat();
        }
        catch (Exception e){
            audioFormat = null;
            e.printStackTrace();
            System.exit(1);
        }
        info = new DataLine.Info(SourceDataLine.class, audioFormat);
    }
    public void play(){
        new Player().start();
    }
    public void play(double IntensityMod){

        float Value = (float)GUI.cap(MinDecibels, BaseDecibels + 10*Math.log10(IntensityMod), MaxDecibels);
     
        if(Value < CutOff)return;

        new Player(Value).start();
    }
    
    public void playDeci(double DeciMod){
        float Value = (float)GUI.cap(MinDecibels, (BaseDecibels + DeciMod), MaxDecibels);
        if(Value < CutOff)return;
        new Player(Value).start();
    }
    class Player extends Thread{
        float DecibelMod = 0;
        AudioInputStream AudioStream;
        public Player(){   }
        public Player(float DBM){  DecibelMod = DBM; }
        
        public void run(){
            try{AudioStream = AudioSystem.getAudioInputStream(SoundFile);}
            catch (Exception e){e.printStackTrace();System.exit(1);}
            SourceDataLine DataLine = null;
            
            try{DataLine = (SourceDataLine) AudioSystem.getLine(info);
                DataLine.open(audioFormat);}
            catch (Exception e){
                e.printStackTrace();System.exit(1);}
            
            FloatControl FC = (FloatControl)DataLine.getControl(FloatControl.Type.MASTER_GAIN);
            
            FC.setValue(DecibelMod);
            
            
            DataLine.start();
            int	nBytesRead = 0;
            byte[] BufferData;
            if(Store.size() == 0){
                BufferData = new byte[BufferSize];
            }else{
                BufferData = Store.pop();
            }
            
            while (nBytesRead != -1){
                
                try{
                    nBytesRead = AudioStream.read(BufferData, 0, BufferData.length);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                if (nBytesRead >= 0){
                    int	nBytesWritten = DataLine.write(BufferData, 0, nBytesRead);
                }

            }
            DataLine.drain();
            DataLine.close();

        }
    }
}