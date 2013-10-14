import javax.swing.Timer;
import java.awt.event.ActionListener;
public class TimeKeeper {
    
    boolean CountTime = true;
    double FramesPerSecond;
    double DeltaT;
    double TotalTime;
    double PreviousTime;
    long FrameCounter;
    NumberQueue DelayList;
    boolean DiscountDelay = true;
    long Temp = System.currentTimeMillis();
    boolean Print = true;
    public void doTime(){
        if(Temp == 0)Temp = System.currentTimeMillis();    
      
        if(DiscountDelay){
            DiscountDelay = false;
        }else{
            DelayList.add(System.currentTimeMillis() - Temp);
        }
        DeltaT = DelayList.average() / 1000;
        FramesPerSecond = 1.0 / DeltaT;
        if(Print)System.out.println((int)(DelayList.average() * 10) / 10.0);
        PreviousTime = TotalTime;
        if(CountTime)TotalTime = TotalTime + 1.0 / FramesPerSecond;
        Temp = System.currentTimeMillis();
    }
    public TimeKeeper(int QueueLength, boolean print){
        Print = print;
        FramesPerSecond = 30;
        DeltaT = 1.0/30;
        DelayList = new NumberQueue(QueueLength);
    }
    public TimeKeeper(int QueueLength){

        FramesPerSecond = 30;
        DeltaT = 1.0/30;
        DelayList = new NumberQueue(QueueLength);
        
    }
    
}
