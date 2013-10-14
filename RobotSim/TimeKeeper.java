import javax.swing.Timer;
import java.awt.event.ActionListener;
public class TimeKeeper {
    
    boolean CountTime = true;
    double FramesPerSecond;
    double TotalTime;
    double PreviousTime;
    long FrameCounter;
    NumberQueue DelayList;
    boolean DiscountDelay = false;
    long Temp = System.currentTimeMillis();
    
    public void doTime(){
        if(Temp == 0)Temp = System.currentTimeMillis();    
      
        if(DiscountDelay){
            DiscountDelay = false;
        }else{
            DelayList.add(System.currentTimeMillis() - Temp);
        }
        FramesPerSecond = 1000.0 / DelayList.average();
        System.out.println((int)(DelayList.average() * 10) / 10.0);
        PreviousTime = TotalTime;
        if(CountTime)TotalTime = TotalTime + 1.0 / FramesPerSecond;
        Temp = System.currentTimeMillis();
    }
    
    public TimeKeeper(int QueueLength){

        FramesPerSecond = 0;
        DelayList = new NumberQueue(QueueLength);
        
    }
    
}
