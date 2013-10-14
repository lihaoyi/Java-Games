import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.Timer;
import java.util.*;
import javax.sound.sampled.*;
import javax.sound.midi.*;
import java.lang.Character;
import java.lang.reflect.*;
import javax.imageio.*;
import java.applet.*;
import java.net.*;
public class GUI{
    /*
    public static double Speed = 1 / 1;
    public static int X = 800;
    public static int Y = 600;
    public static int TankImageType = BufferedImage.TYPE_INT_RGB;
    public static int TankStartingLevel;
    public static boolean Time;
    public static boolean Border;
    
    */

    public static void close(String Message, String Title){
        JOptionPane.showMessageDialog(null, Message, Title, JOptionPane.PLAIN_MESSAGE); 
        System.exit(0);
    }
    /*
    public static Vector Sounds = new Vector();
    public static Vector Names = new Vector();
    public static void playSound(boolean Play, String Path, int LoopCount, int Multiplier){
        try{
            for(int i = 0; i < Names.size(); i++){
                if(((String)Names.elementAt(i)).equals(Path)){
                    if(Play){
                         Applet.newAudioClip((URL)Sounds.elementAt(i)).play();
                    }
                    return;
                }
            }
            URL U = new File(System.getProperty("user.dir"), "Sounds\\" + Path).toURL();
            Sounds.add(U);
            Names.add(Path);
          //  System.out.println(A);
            if(Play){
                Applet.newAudioClip(U).play();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    */
    public static void playSound(boolean Play, String filename, int LoopCount, int multiplier){
        Clip Clip = null;

        for(int i = 0; i < multiplier; i++){
        
        try{
                AudioInputStream source = AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir"), "Sounds\\" + filename));
                DataLine.Info clipInfo = new DataLine.Info(Clip.class, source.getFormat());
                Clip = (Clip)AudioSystem.getLine(clipInfo);
                Clip.open(source);
            }catch(Exception e){}
            if(Clip == null){
                return;
            }
            if(Play){
            
                Clip.loop(LoopCount);    
            
            }
        }
    }
    public static void playSound(String filename){
        try {
        Sequence sequence = MidiSystem.getSequence(new File(System.getProperty("user.dir"), "Sounds\\" + filename));   
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.start();
        }catch(Exception e){}
    }
    public static Image getImage(String filename){
        Image picture;
        try{
            picture = Toolkit.getDefaultToolkit().getImage("Pictures\\" + filename);
        }catch(Exception e){System.out.println("error"); return null;}
        return picture;
    }
    
    public static Object doubleArray(Object[] Target){
        Object NewArray = Array.newInstance(Target[0].getClass(), Target.length * 2);
        System.arraycopy(Target, 0, NewArray, 0, Target.length);
        return NewArray;
    }
    private static Dimension ScreenSize;
    public static Dimension getScreenSize(){
        if(ScreenSize == null){
            ScreenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();   
        }
        return ScreenSize;
    }
    public static int randomInt(int INTA, int INTB){
        return ((int)Math.ceil(Math.random() * (Math.max(INTA, INTB) - Math.min(INTA, INTB) + 1) + Math.min(INTA, INTB) - 1));
    }   
    public static double difference(double DoubleA, double DoubleB){
        return Math.abs(Math.max(DoubleA,  DoubleB) - Math.min(DoubleA, DoubleB));
    }
    public static int numpadY(int INTY, int SWITCH){
        switch(SWITCH){
            case 1: case 2: case 3: INTY = INTY + 1; break;
            case 4: case 5: case 6: INTY = INTY;     break;
            case 7: case 8: case 9: INTY = INTY - 1; break;
        }
        return INTY;
    }
    public static int numpadX(int INTX, int SWITCH){
        switch(SWITCH){
            case 1: case 4: case 7: INTX = INTX - 1; break;
            case 2: case 5: case 8: INTX = INTX;     break;
            case 3: case 6: case 9: INTX = INTX + 1; break;
        }
        return INTX;
    }
    public static int numpadRotate(int FACING, int SWITCH){
        for(int i = 0; i < Math.abs(SWITCH); i++){
            switch(FACING){
            case 7: return (SWITCH < 0 ? 8 : 4); case 8: return (SWITCH < 0 ? 9 : 7); case 9: return (SWITCH < 0 ? 6 : 8);
            
            case 4: return (SWITCH < 0 ? 7 : 1);                                      case 6: return (SWITCH < 0 ? 3 : 9);
        
            case 1: return (SWITCH < 0 ? 4 : 2); case 2: return (SWITCH < 0 ? 1 : 3); case 3: return (SWITCH < 0 ? 2 : 6);
            default: return FACING;
        }
        }
        return FACING;
    }
    public static double square(double INPUT){
        return INPUT * INPUT;
    }
    public static double length(Line2D L){
        return Math.sqrt(square(L.getX1() - L.getX2()) + square(L.getY1() - L.getY2()));
    }
    public static double trigCap(double Current){
        if(Current < -1){
            return -1;
        }
        if(Current > 1){
            return 1;
        }
        return Current;
    }
    public static int cap(int Min, int Current, int Max){
    	if(Min > Max){
    		int Temp = Min;
    		Min = Max;
    		Max = Temp;
    	}
    		
        if(Current < Min){
            return Min;
        }
        
        if(Current > Max){
            return Max;
        }
        return Current;
    }
    public static void checkNaN(double D){
    	if(D != D){
    		throw new IllegalArgumentException("NaN OH NOES");	
    	}	
    }
    public static double cap(double Min, double Current, double Max){
    	if(Min > Max){
    		double Temp = Min;
    		Min = Max;
    		Max = Temp;
    	}
        if(Current < Min){
            return Min;
        }
        
        if(Current > Max){
            return Max;
        }
        return Current;
    }
    public static char getRandomAxis(){
        double R = Math.random();
        if(R <= 0.333333){
            return 'x';
        }
        if(R > 0.666666){
            return 'y';
        }
        return 'z';
    }
    public static int sign(double DoubleA){
        if(DoubleA < 0){
            return -1;
        }else if(DoubleA > 0){
            return 1;
        }else{
            return 0;
        }
    }
    private static String[] Greek = {"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda", "Mu", "Nu", "Xi", "Omnicron", "Pi", "Rho", "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega"};
    public static String getGreek(int A){
    
        return Greek[A % Greek.length];
    }
    private static String[] AlphaNumerals = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "J", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public static String alphaNumeral(int A){
        return AlphaNumerals[A % AlphaNumerals.length];
    }
    public static String returnBlanks(int Count){
        String Blanks = "";
        for(int i = 0; i < Count; i++){
            Blanks += " ";
        }
        return Blanks;
    }
    public static String processNumber(int LB, double Count, int RB){
        String A = "" + Count;
        A = A.replace('.', '/');
        String[] Bits = A.split("/");
        if(Bits.length > 0){
            return returnBlanks(LB - Bits[0].length()) + Count + returnBlanks(RB - Bits[1].length());
        }else{
            return returnBlanks(LB + RB);
        }
    }
    public static String processNumber(int LB, String Count, int RB){
        String A = Count;
        A = A.replace('.', '/');
        String[] Bits = A.split("/");
        if(Bits.length > 0){
            return returnBlanks(LB - Bits[0].length()) + Count + returnBlanks(RB - Bits[1].length());
        }else{
            return returnBlanks(LB + RB);
        }
    }    
    
    public static double simplify(double Angle){
        while(Angle < 0 || Angle > 360){
            if(Angle < 0){Angle = 360 + Angle;}
            if(Angle > 360){Angle = Angle - 360;}
        }
        return Angle;
    }
    public static double moveToward(double StartValue, double Increment, double Target){
        if(StartValue == Target){
            
        }else if(StartValue > Target){
            StartValue = StartValue - Increment;
            if(StartValue < Target){
                StartValue = Target;
            }
            
        }else if(StartValue < Target){
            StartValue = StartValue + Increment;
            if(StartValue > Target){
                StartValue = Target;
            }
        }
        return StartValue;
    }
    public static double angleDiff(double AngleA, double AngleB){
        while(AngleA < 0 || AngleA > 360){
            if(AngleA < 0){AngleA = 360 + AngleA ;}
            if(AngleA > 360){AngleA = AngleA - 360;}
        }
        while(AngleB < 0 || AngleB > 360){
            if(AngleB < 0){AngleB = 360 + AngleB ;}
            if(AngleB > 360){AngleB = AngleB - 360;}
        }
            double ArcA = Math.max(AngleA, AngleB) - Math.min(AngleA, AngleB);
            double ArcB = 360 - Math.max(AngleA, AngleB) + Math.min(AngleA, AngleB);
                
                if(ArcA > ArcB){
                    if(AngleB < AngleA){
                        return ArcB;
                    }else{
                        return -ArcB;
                    }
                }
                if(ArcA < ArcB){
                    if(AngleB < AngleA){
                        return -ArcA;
                    }else{
                        return ArcA;
                    }
                }
            
            return ArcB;
    }
    public static double angleDiffR(double AngleA, double AngleB){
        double TwoPi = 2*Math.PI;
        while(AngleA < 0 || AngleA > TwoPi){
            if(AngleA < 0){AngleA = TwoPi + AngleA ;}
            if(AngleA > TwoPi){AngleA = AngleA - TwoPi;}
        }
        while(AngleB < 0 || AngleB > TwoPi){
            if(AngleB < 0){AngleB = TwoPi + AngleB ;}
            if(AngleB > TwoPi){AngleB = AngleB - TwoPi;}
        }
            double ArcA = Math.max(AngleA, AngleB) - Math.min(AngleA, AngleB);
            double ArcB = TwoPi - Math.max(AngleA, AngleB) + Math.min(AngleA, AngleB);
                
                if(ArcA > ArcB){
                    if(AngleB < AngleA){
                        return ArcB;
                    }else{
                        return -ArcB;
                    }
                }
                if(ArcA < ArcB){
                    if(AngleB < AngleA){
                        return -ArcA;
                    }else{
                        return ArcA;
                    }
                }
            
            return ArcB;
    }
    public static double pythagoras(double A, double B){
        return Math.sqrt(A*A + B*B);
    }
    public static Color blackenColor(Color OldColor, double PercentFading){
        PercentFading = Math.max(0, Math.min(1, PercentFading));
        return new Color((int)(OldColor.getRed() * PercentFading), (int)(OldColor.getGreen() * PercentFading), (int)(OldColor.getBlue() * PercentFading));
    }
    
    
    public static Color fadeColor(Color OldColor, double PercentFading){
        PercentFading = Math.max(0, Math.min(1, PercentFading));
        return new Color(OldColor.getRed(), OldColor.getGreen(), OldColor.getBlue(), (int)(PercentFading * 255));
    }
    
    public static Color mix(Color ColorA, Color ColorB, double PercentA){
        PercentA = Math.max(Math.min(PercentA, 1), 0);
        double Red = (ColorA.getRed() * PercentA) + ColorB.getRed() * (1 - PercentA);
        double Green = (ColorA.getGreen() * PercentA) + ColorB.getGreen() * (1 - PercentA);
        double Blue = (ColorA.getBlue() * PercentA) + ColorB.getBlue() * (1 - PercentA);
        return new Color((int)Red, (int)Green, (int)Blue);
    }
    public static double log(int Base, double Value){
        return Math.log(Value) / Math.log(Base);
        
    }
    public static AffineTransform inverse(AffineTransform Transformation){
        try{
            return Transformation.createInverse();
        }catch(NoninvertibleTransformException e){
            return Transformation;
        }
    }
    public static double round(double Value, double Divisor){
        Value = (int)(Value * Divisor) / Divisor;
        return Value;
    }
    public static int getFirstNonZero(int[] A){
        for(int i = 0; i < A.length; i++){
            if(A[i] != 0){
                return i;
            }
        }
        return -1;
    }
    public static int stringTogether(int[] A){
        String S = "9";
        for(int i = 0; i < A.length; i++){
            S += A;
        }
        return Integer.parseInt(S);
    }
    
    public static double newtonRaphson(double A, double B, double C, double D, double E, double Start, int Iterations, double Allowance){//where Ax^4 + Bx^3 + Cx^2 + Dx + E = 0
        double dA = 4*A;
        double dB = 3*B;
        double dC = 2*C;
        double dD = D;
        
        double X = Start;
        double PercentageChange = 0;
        for(int i = 0; i < Iterations; i++){
            PercentageChange = X;
            X = X - (A*X*X*X*X + B*X*X*X + C*X*X + D*X + E) / (dA*X*X*X + dB*X*X + dC*X + dD);
            PercentageChange = Math.abs(1 - X / PercentageChange);
        }
        
        if(PercentageChange < Allowance){
            return X;
        }else{
            return 0.0/0.0;
        }
        
        /*
        double Alpha = -3.0 / 8.0 * (B/A)*(B/A) + C/A;
        double Beta = 1.0/8*(B/A)*(B/A)*(B/A) - B/A*C/A/2 + D/A;
        double Gamma = -3.0/256*(B/A)*(B/A)*(B/A)*(B/A) + C/A*B/A*B/A/16 - B/A*D/A/4 + E/A;
        
        if(Beta == 0){
            for(int s = -1; s <= 1; s+=2){
                for(int t = -1; t <= 1; t+=2){
                    double X = -B/(4*A) + s * Math.sqrt((-Alpha + t*Math.sqrt(Alpha*Alpha - 4*Gamma))/2);
                    System.out.println("Alpha " + Alpha);
                    System.out.println("Beta " + Beta);
                    System.out.println("Gamma " + Gamma);
                    System.out.println("X"+X);
                    if(X == X){
                        i++;
                        QuarticSolution[i] = X;
                    }
                }
            }
            QuarticSolution[0] = i;
            return QuarticSolution;
        }
        double P = -1.0/12*Alpha*Alpha - Gamma;
        double Q = -1.0/180*Alpha*Alpha*Alpha + 1.0/3*Alpha*Gamma - 1.0/8*Beta*Beta;
        double R = Q/2 + Math.sqrt(1.0/4*Q*Q + 1.0/27*P*P*P);
        double U = Math.pow(R, 1.0/3);
        double y = -5.0/6.0 * Alpha - U + (U == 0 ? 0 : P / (3*U));
        double W = Math.sqrt(Alpha + 2*y);
        
        
        
        for(int s = -1; s <= 1; s+=2){
            for(int t = -1; t <= 1; t+=2){
                System.out.println("Alpha " + Alpha);
                System.out.println("Beta " + Beta);
                System.out.println("Gamma " + Gamma);
                System.out.println("P " + P);
                System.out.println("Q " + Q);
                System.out.println("R " + R);
                System.out.println("U " + U);
                System.out.println("y " + y);
                System.out.println("W " + W);
                double X = -B / (4 * A) + (s*W + t*Math.sqrt(-(3*Alpha + 2*y + s*2*Beta/W)))/2;
                System.out.println("X " + X);
                if(X == X){
                    i++;
                    QuarticSolution[i] = X;
                }
                
            }
        }
        QuarticSolution[0] = i;
        return QuarticSolution;
         **/
    }
}
