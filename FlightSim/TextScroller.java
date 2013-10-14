import java.util.*;

public class TextScroller {
    class TextLine{
        String Text;
        float Counter;
        TextLine(String T, float C){
            Text = T;
            Counter = C;
        }
    }
    private Vector TextLines = new Vector();
    
    private int MaxLines;
    private float LineDuration;
    public TextScroller(int LineCount, float Duration){
        MaxLines = LineCount;
        LineDuration = Duration;
    }
    public void addLine(String S){
        if(TextLines.size() >= MaxLines){
            TextLines.removeElementAt(0);
        }
        TextLines.add(new TextLine(S, LineDuration));
        
    }
    public void decrement(float S){
        for(int i = TextLines.size()-1; i >= 0; i--){
            ((TextLine)TextLines.elementAt(i)).Counter -= S;
            if(((TextLine)TextLines.elementAt(i)).Counter <= 0){
                TextLines.remove(TextLines.elementAt(i));
            }
        }
    }
    public String getLine(int A){
        if(A > MaxLines || A > TextLines.size()){
            throw new ArrayIndexOutOfBoundsException("No such line in textscroller");
        }
        
        return ((TextLine)TextLines.elementAt(A)).Text;
    }
    public int getMaxLines(){
        return MaxLines;
    }
    public int getLineCount(){
        return TextLines.size();
    }
    
}
