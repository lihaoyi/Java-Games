import javax.sound.sampled.*;
import java.io.*;
import java.util.*;
public class WaveSound{
    String FileName;
    File SoundFile;
    double BaseDecibels;
    double CutOff;
    byte[] Data;
    AudioFormat Format;
    LinkedList<SourceDataLine> Store = new LinkedList<SourceDataLine>();

    public WaveSound(String fileName, double DB, double cutOff){
        FileName = fileName;
        BaseDecibels = DB;
        CutOff = -1000;
        SoundFile = new File(FileName);
        try{
            AudioInputStream AIS = AudioSystem.getAudioInputStream(SoundFile);
            Data = new byte[AIS.available()];
            Format = AIS.getFormat();
            AIS.read(Data);
        }catch(Exception e){e.printStackTrace();}
    }
    public WaveSound(String FileName, double DB){
        this(FileName, DB, -10000000);
    }
    public WaveSound(String FileName){
        this(FileName, 0);
    }
    public void play(){
        play(1);
    }
    public void play(final double Intensity){
        System.out.println(Store.size());
        float FinalVolume = (float)(BaseDecibels + 10*Math.log10(Intensity));
        if(FinalVolume < CutOff) return;
        try{
            
            final SourceDataLine L;
            if(Store.size() == 0 || Store.get(0).isActive()){
                L = (SourceDataLine)AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, Format));
                L.open(Format, Data.length);
            }else{
                L = Store.poll();
            }
               
            FloatControl VolumeControl = (FloatControl)L.getControl(FloatControl.Type.MASTER_GAIN);
            VolumeControl.setValue(FinalVolume);
         
            L.start();
            L.write(Data, 0, L.available());
            Store.add(L);
            
            
        }catch(Exception e){e.printStackTrace();}
    }
}
