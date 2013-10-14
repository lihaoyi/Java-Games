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
import javax.imageio.*;
public class GameLibrary{
    
    public static double Speed = 1 / 1;
    public static int X = 800;
    public static int Y = 600;
    public static int TankImageType = BufferedImage.TYPE_INT_RGB;
    public static int TankStartingLevel;
    public static boolean Time;
    public static boolean Border;
    public static void getParameters(String[] yo){
        
        
      
        try{
            Speed = Double.parseDouble(yo[0]); 
        }catch(Exception e){
            Speed = 1;
        }
        
        try{
            if(yo[1].equals("byte")){
                TankImageType = BufferedImage.TYPE_BYTE_INDEXED;
            }else if(yo[1].equals("short")){
               TankImageType = BufferedImage.TYPE_USHORT_555_RGB;
            }else if(yo[1].equals("int")){
               TankImageType = BufferedImage.TYPE_INT_RGB; 
            }
        }catch(Exception e){
            TankImageType = BufferedImage.TYPE_INT_RGB;
        }
        
        try{
            int INTA = Integer.parseInt(yo[2]);
            TankStartingLevel = INTA;
        }catch(Exception e){
            TankStartingLevel = 1;
        }
        
        try{
            Time = yo[3].equals("true");
        }catch(Exception e){
            Time = false;
        }
        try{
            Border = !yo[4].equals("Off");
        }catch(Exception e){
            Border = true;
        }
        //test();
    }
    
    
    
    public static void main(String[] yo){
        
        
        getParameters(yo);
        
        String[] OPTIONS = {"Exit", "Asteroids", "AstroLander", "Turmites", "Snake", "Pong", "Bricks", "Tetris", "Tanks", "Circuit", "Tanks3D", "NewTest", "Discover", "Queens",  "AntiAfk", "Klotski"};
        String[] LANDEROPTIONS = {"Original Version", "New Version"};

                int INTA = JOptionPane.showOptionDialog(null, "Select the program to run.", "Games by LHY", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, OPTIONS, null);

                switch(INTA){
                    default: System.exit(0);
                    case 1: new Asteroids(); break;
                    case 2: new Astrolander((JOptionPane.showOptionDialog(null, "Run Original Version of Lander or New Version?", "", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, LANDEROPTIONS, null) == 0)); break;
                    case 3: new Turmites(); break;
                    case 4: new Snake(); break;
                    case 5: new Pong(); break;
                    case 6: new Bricks(); break;
                    case 7: new Tetris(); break;
                    case 8: new Tanks(); break;
                    case 9: new Circuit(); break;
                    case 10: new Test(); break;
                    case 11: new NewTest(); break;
                    case 12: new Discover(); break;
                    case 13: new Queens(); break;
                    case 14: new CTimer(); break;
                    case 15: new Klotski(); break;
                }
            
        
    }
}

class Asteroids extends JFrame implements KeyListener, ActionListener{
    
    final int X = GUI.X;
    final int Y = GUI.Y;
    
    
    class Projectile{
        int SHOTS = 100;         
        double POSY;
        double POSX;
        double YMOMENTUM;
        double XMOMENTUM;
        double SPEED;
        int FACINGANGLE;
        boolean CONTROLABLE;
        boolean EXITED = false;
        int IMAGE;
        int TYPE;
        int SIZE = 3;
        Shape Image;
        public Object clone(){
            Projectile thing = new Projectile(SPEED, POSY, POSX, YMOMENTUM, XMOMENTUM, FACINGANGLE, CONTROLABLE, IMAGE, TYPE);
            thing.SIZE = SIZE;
            return thing;
        }
        public Projectile(double tSPEED, double tPOSY, double tPOSX, double tYMOMENTUM, double tXMOMENTUM, int tFACINGANGLE, boolean tCONTROLABLE, int tIMAGE, int tTYPE){
            SPEED = tSPEED;
            POSY = tPOSY;
            POSX = tPOSX;
            YMOMENTUM = tYMOMENTUM;
            XMOMENTUM = tXMOMENTUM;
            FACINGANGLE = tFACINGANGLE;
            CONTROLABLE = tCONTROLABLE;
            IMAGE = tIMAGE;
            TYPE = tTYPE;
        }
        public void fillOldImage(){
            try{
                Area.Painter.setColor(Color.black);
                Area.Painter.fill(Image);
                Area.Painter.draw(Image);
            }catch(NullPointerException e){}
              
        }
        public void move(){
            
            if(this.CONTROLABLE == true){
            if(LEFT == true){
                FACINGANGLE = FACINGANGLE - 7;
            }
            if(RIGHT == true){  
                FACINGANGLE = FACINGANGLE + 7;
            }
            if(FORWARD == true){
                YMOMENTUM = YMOMENTUM + SPEED * Math.sin(Math.toRadians(FACINGANGLE));
                XMOMENTUM = XMOMENTUM + SPEED * Math.cos(Math.toRadians(FACINGANGLE));
            }
            if(BACK == true){
                YMOMENTUM = YMOMENTUM - SPEED * Math.sin(Math.toRadians(FACINGANGLE));
                XMOMENTUM = XMOMENTUM - SPEED * Math.cos(Math.toRadians(FACINGANGLE));
            }
            }
            POSY = POSY + YMOMENTUM / 2;
            POSX = POSX + XMOMENTUM / 2;
            if(FACINGANGLE > 360){
                FACINGANGLE = FACINGANGLE - 360;
            }
            if(FACINGANGLE < 0){
                FACINGANGLE = FACINGANGLE + 360;
            }
            if(POSY > Y){
                POSY = POSY - Y;
                EXITED = true;
            }
            if(POSY < 0){
                POSY = POSY + Y;
                EXITED = true;
            }
            if(POSX > X){
                POSX = POSX - X;
                EXITED = true;
            }
            if(POSX < 0){
                POSX = POSX + X;
                EXITED = true;
            }
            
            
            if(IMAGE == 1){
                Image = new GeneralPath();
                ((GeneralPath)(Image)).moveTo((float)POSX, (float)POSY);
                ((GeneralPath)(Image)).lineTo((float)(POSX + 7  * Math.cos(Math.toRadians(FACINGANGLE - 127.5))), (float)(POSY + 7 * Math.sin(Math.toRadians(FACINGANGLE - 127.5))));
                ((GeneralPath)(Image)).lineTo((float)(POSX + 7  * Math.cos(Math.toRadians(FACINGANGLE + 127.5))), (float)(POSY + 7 * Math.sin(Math.toRadians(FACINGANGLE + 127.5))));
                ((GeneralPath)(Image)).lineTo((float)(POSX + 15  * Math.cos(Math.toRadians(FACINGANGLE))), (float)(POSY + 15 * Math.sin(Math.toRadians(FACINGANGLE))));
                ((GeneralPath)(Image)).lineTo((float)(POSX + 7  * Math.cos(Math.toRadians(FACINGANGLE - 127.5))), (float)(POSY + 7 * Math.sin(Math.toRadians(FACINGANGLE - 127.5))));
            }else if(IMAGE == 2){
                Image = new GeneralPath();
                ((GeneralPath)(Image)).moveTo((float)POSX, (float)POSY);
                ((GeneralPath)(Image)).lineTo((float)(POSX + 5  * Math.cos(Math.toRadians(FACINGANGLE))), (float)(POSY + 5 * Math.sin(Math.toRadians(FACINGANGLE))));
                ((GeneralPath)(Image)).closePath();
            }else{
                Image = new Rectangle2D.Double();
                ((Rectangle2D.Double)(Image)).setRect(POSX, POSY, (double)10 * SIZE, (double)10 * SIZE);
            }
            Area.Painter.setColor(Color.white);
            Area.Painter.fill(Image);
            Area.Painter.draw(Image);
        }
    }
    
    Projectile Ship = new Projectile(0.5, Y / 2, X / 2, 0, 0, 90, true, 1, 1);
    Vector Asteroids = new Vector();
    Vector Shots = new Vector();
    
    boolean LEFT;
    boolean RIGHT;
    boolean FORWARD;
    boolean BACK;

    
    
    Timer Stopwatch = new Timer((int)(15 * GUI.Speed), this);
    Background Area;
    public void createAsteroids(){
        for(int i = 0; i < 10; i++){
            Projectile ASTEROID = new Projectile(1, 0, 0, Math.random() * 5 - 2.5, Math.random() * 5 - 2.5, 0, false, 0, 0);
            switch(GUI.randomInt(0, 1)){
                case 0: ASTEROID.POSY = 0; ASTEROID.POSX = GUI.randomInt(0, X); break;
                case 1: ASTEROID.POSX = 0; ASTEROID.POSY = GUI.randomInt(0, X); break;
            }
            Asteroids.addElement(ASTEROID);
        }
    }
    public Asteroids(){
        //BASIC INITIALIZATION
        super("Window");
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new Background();
        contentArea.add(Area);
        Stopwatch.start();
        this.addKeyListener(this);
        createAsteroids();
        
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }

    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){
   //     System.out.println(e.getKeyChar() + "\t" + LEFT + "\t" + RIGHT + "\t" + FORWARD + "\t" + BACK);
        switch(e.getKeyCode()){
            case 37: LEFT = true; break;
            case 39: RIGHT = true; break;
            case 38: FORWARD = true; break;
            case 40: BACK = true; break;

        }
    }
    public void keyReleased(KeyEvent e){
        
        switch(e.getKeyCode()){
            case 37: LEFT = false; break;
            case 39: RIGHT = false; break;
            case 38: FORWARD = false; break;
            case 40: BACK = false; break;
        }
    }
 
    
    public void keyTyped(KeyEvent e){
        if(Ship.SHOTS < 10){
            return;
        }else{
            Ship.SHOTS = Ship.SHOTS - 10;
        }
        switch(e.getKeyChar()){
            //Projectile Ship = new Projectile(Y / 2, X / 2, 0, 0, 90, true, 1, 1);
            case ' ': Projectile SHOT = new Projectile(15, Ship.POSY, Ship.POSX, Ship.YMOMENTUM, Ship.XMOMENTUM, Ship.FACINGANGLE, false, 2, 0);
                      SHOT.YMOMENTUM = Ship.YMOMENTUM + SHOT.SPEED * Math.sin(Math.toRadians(SHOT.FACINGANGLE));
                      SHOT.XMOMENTUM = Ship.XMOMENTUM + SHOT.SPEED * Math.cos(Math.toRadians(SHOT.FACINGANGLE));
                      Shots.add(SHOT);
                   // System.out.println(((Projectile)(Shots.elementAt(Shots.size() - 1))).POSY + "\t" + ((Projectile)(Shots.elementAt(Shots.size() - 1))).POSX);
        }
        
    }
   
    public void actionPerformed(ActionEvent e){
        Ship.SHOTS = Math.min(100, Ship.SHOTS + 1);
        Ship.fillOldImage();
        for(int i = 0; i < Shots.size(); i++){
            ((Projectile)Shots.elementAt(i)).fillOldImage();
        }
        for(int i = 0; i < Asteroids.size(); i++){
            ((Projectile)Asteroids.elementAt(i)).fillOldImage();
        }
        Ship.move();
        for(int i = 0; i < Shots.size(); i++){
            ((Projectile)(Shots.elementAt(i))).move();
            if(((Projectile)(Shots.elementAt(i))).EXITED == true){
                Area.Painter.setColor(Color.black);
                Area.Painter.fill(((Projectile)Shots.elementAt(i)).Image);
                Area.Painter.draw(((Projectile)Shots.elementAt(i)).Image);
                Shots.remove(i);
                
            }  
        }
        for(int i = 0; i < Asteroids.size(); i++){
            ((Projectile)(Asteroids.elementAt(i))).move();
        }
        for(int j = 0; j < Asteroids.size(); j++){
            if((Ship.Image).intersects((Rectangle2D)(((Projectile)Asteroids.elementAt(j)).Image)) == true){
                Stopwatch.stop();
                
                GUI.close("Your ship hit an Asteroid!", "Crash!");
                
                
                return;
            
                
            }
        }
        for(int i = 0; i < Shots.size(); i++){
            for(int j = 0; j < Asteroids.size(); j++){
                try{
                    if(((Projectile)Shots.elementAt(i)).Image.intersects((Rectangle2D)(((Projectile)Asteroids.elementAt(j)).Image))){
                        if(((Projectile)Asteroids.elementAt(j)).SIZE > 1){
                            Projectile ASTEROID = ((Projectile)Asteroids.elementAt(j));
                            Projectile SHOT = ((Projectile)Shots.elementAt(i));
                            splitAsteroid(ASTEROID, SHOT);
                            
                        }else{
                            Area.Painter.setColor(Color.black);
                            Area.Painter.fill(((Projectile)Asteroids.elementAt(j)).Image);
                            Area.Painter.draw(((Projectile)Asteroids.elementAt(j)).Image);
                            Asteroids.remove(j);
                            
                        }
                        Area.Painter.setColor(Color.black);
                        Area.Painter.fill(((Projectile)Shots.elementAt(i)).Image);
                        Area.Painter.draw(((Projectile)Shots.elementAt(i)).Image);
                        Shots.remove(i);
                    }
                }catch(Exception n){}
            }
        }
        if(Asteroids.size() == 0){
            Stopwatch.stop();
            
            GUI.close("You have destroyed all the Asteroids!.", "Success!");
            
            
            return;
            
        }
        Area.paintComponent(this.getGraphics());
    }
    
    public void splitAsteroid(Projectile ASTEROID, Projectile SHOT){
            ASTEROID.SIZE--;
            Projectile NEWASTEROID = (Projectile)ASTEROID.clone();
            double ANGLE = Math.toDegrees(Math.atan(ASTEROID.YMOMENTUM / ASTEROID.XMOMENTUM));
            double SPEED = ASTEROID.YMOMENTUM * Math.sin(Math.toRadians(ANGLE));
            double XMOD = SPEED * Math.cos(Math.toRadians(ANGLE + 30));
            double YMOD = SPEED * Math.sin(Math.toRadians(ANGLE + 30));
            ASTEROID.XMOMENTUM += XMOD;
            ASTEROID.YMOMENTUM += YMOD;
            XMOD = SPEED * Math.cos(Math.toRadians(ANGLE - 30));
            YMOD = SPEED * Math.sin(Math.toRadians(ANGLE - 30));
            NEWASTEROID.XMOMENTUM += XMOD;
            NEWASTEROID.YMOMENTUM += YMOD;
            NEWASTEROID.XMOMENTUM += SHOT.XMOMENTUM / (NEWASTEROID.SIZE * 2 + 3);
            NEWASTEROID.YMOMENTUM += SHOT.YMOMENTUM / (NEWASTEROID.SIZE * 2 + 3);
            ASTEROID.XMOMENTUM += SHOT.XMOMENTUM / (ASTEROID.SIZE * 2 + 3);
            ASTEROID.YMOMENTUM += SHOT.YMOMENTUM / (ASTEROID.SIZE * 2 + 3);
            Asteroids.add(NEWASTEROID);                
    }
    public void paint(){
        ((Graphics2D)this.getGraphics()).drawImage(Area.Picture, null, 0, 0);
        
    }
    class Background extends JPanel{
        BufferedImage Picture = new BufferedImage(X, Y, BufferedImage.TYPE_BYTE_GRAY);
        Graphics painter = Picture.getGraphics();
        Graphics2D Painter = Picture.createGraphics();
        public void paintComponent(Graphics paint){
            try{
                Graphics2D Paint = (Graphics2D)(paint);
                Painter.setColor(Color.black);
                Paint.drawImage(Picture, null, 0, 0);
            }catch(NullPointerException e){}
        }
    }

}
class Astrolander extends JFrame implements KeyListener, ActionListener, MouseListener{
    
    final int X = GUI.X;
    final int Y = GUI.Y;
    final int POINTCOUNT = 20;
    final double MAXLANDINGSPEED = 3;
    final double MAXLANDINGANGLE = 10;
    final int STARTINGFUEL = 1000;
    final boolean ORIGINAL;
    class Projectile{
        int SHOTS = 100;         
        double POSY;
        double POSX;
        double YMOMENTUM;
        double XMOMENTUM;
        double SPEED;
        int FACINGANGLE;
        

        int FUEL = STARTINGFUEL;
        Line2D.Float[] LINES = new Line2D.Float[3];
        Point2D.Float POINTA;
        Point2D.Float POINTB;
        Point2D.Float POINTC;
        Point2D.Float POINTAB;
        Point2D.Float POINTAC;
        Shape Image;
        Shape OldImage;
        
        public Projectile(double tSPEED, double tPOSY, double tPOSX, double tYMOMENTUM, double tXMOMENTUM, int tFACINGANGLE){
            SPEED = tSPEED;
            POSY = tPOSY;
            POSX = tPOSX;
            YMOMENTUM = tYMOMENTUM;
            XMOMENTUM = tXMOMENTUM;
            FACINGANGLE = tFACINGANGLE;

        }
        public void move(){
            if(ORIGINAL){
                if((LEFT == true) && (FUEL > 0)){
                    YMOMENTUM = YMOMENTUM + SPEED * Math.sin(Math.toRadians(FACINGANGLE + 45));
                    XMOMENTUM = XMOMENTUM + SPEED * Math.cos(Math.toRadians(FACINGANGLE + 45));
                    FUEL = FUEL - 1;
                }
                if((RIGHT == true) && (FUEL > 0)){  
                    YMOMENTUM = YMOMENTUM + SPEED * Math.sin(Math.toRadians(FACINGANGLE - 45));
                    XMOMENTUM = XMOMENTUM + SPEED * Math.cos(Math.toRadians(FACINGANGLE - 45));
                    FUEL = FUEL - 1;
                }
               
                if((BACK == true) && (FUEL > 0)){
                    YMOMENTUM = YMOMENTUM + SPEED * Math.sin(Math.toRadians(FACINGANGLE));
                    XMOMENTUM = XMOMENTUM + SPEED * Math.cos(Math.toRadians(FACINGANGLE));
                    FUEL = FUEL - 1;
                }
            }else{
                if((LEFT == true)){
                    FACINGANGLE = FACINGANGLE - 5;
                }
                if((RIGHT == true)){  
                    FACINGANGLE = FACINGANGLE + 5;
                }
                if((FORWARD == true) && (FUEL > 0)){
                    YMOMENTUM = YMOMENTUM + SPEED * Math.sin(Math.toRadians(FACINGANGLE));
                    XMOMENTUM = XMOMENTUM + SPEED * Math.cos(Math.toRadians(FACINGANGLE));
                    FUEL = FUEL - 1;
                }   
                
            }
            POSY = POSY + YMOMENTUM / 2;
            POSX = POSX + XMOMENTUM / 2;
            YMOMENTUM = YMOMENTUM + 0.2;
            if(FACINGANGLE > 360){
                FACINGANGLE = FACINGANGLE - 360;
            }
            if(FACINGANGLE < 0){
                FACINGANGLE = FACINGANGLE + 360;
            }
                OldImage = Image;
                Image = null;
                Image = new GeneralPath();
                ((GeneralPath)(Image)).moveTo((float)POSX, (float)POSY);
                POINTA = new Point2D.Float((float)(POSX + 7  * Math.cos(Math.toRadians(FACINGANGLE - 127.5))), (float)(POSY + 7 * Math.sin(Math.toRadians(FACINGANGLE - 127.5))));
                ((GeneralPath)(Image)).lineTo(POINTA.x, POINTA.y);
                POINTB = new Point2D.Float((float)(POSX + 7  * Math.cos(Math.toRadians(FACINGANGLE + 127.5))), (float)(POSY + 7 * Math.sin(Math.toRadians(FACINGANGLE + 127.5))));
                ((GeneralPath)(Image)).lineTo(POINTB.x, POINTB.y);
                POINTC = new Point2D.Float((float)(POSX + 15  * Math.cos(Math.toRadians(FACINGANGLE))), (float)(POSY + 15 * Math.sin(Math.toRadians(FACINGANGLE))));
                ((GeneralPath)(Image)).lineTo(POINTC.x, POINTC.y);
                ((GeneralPath)(Image)).lineTo(POINTA.x, POINTA.y);
                POINTAB = new Point2D.Float((POINTA.x + POINTC.x) / 2, (POINTA.y + POINTC.y) / 2);
                POINTAC = new Point2D.Float((POINTA.x + POINTC.x) / 2, (POINTA.y + POINTC.y) / 2);
                LINES[0] = new Line2D.Float(POINTA, POINTB);
                LINES[1] = new Line2D.Float(POINTA, POINTC);
                LINES[2] = new Line2D.Float(POINTB, POINTC);
        }
    }
    
    public Shape drawFlame(Projectile Ship, int Angle){
        GeneralPath Flame = new GeneralPath();
        int FlameWidth = 15;
        Flame.moveTo((float)(Ship.POSX + 5 * Math.cos(Math.toRadians(Angle + 180))), (float)(Ship.POSY +  5 * Math.sin(Math.toRadians(Angle + 180))));
        Flame.lineTo((float)(Ship.POSX + 15 * Math.cos(Math.toRadians(Angle + 180 + FlameWidth))), (float)(Ship.POSY +  15 * Math.sin(Math.toRadians(Angle + 180 + FlameWidth)))); 
        Flame.moveTo((float)(Ship.POSX + 25 * Math.cos(Math.toRadians(Angle + 180))), (float)(Ship.POSY +  25 * Math.sin(Math.toRadians(Angle + 180))));
        Flame.lineTo((float)(Ship.POSX + 15 * Math.cos(Math.toRadians(Angle + 180 - FlameWidth))), (float)(Ship.POSY +  15 * Math.sin(Math.toRadians(Angle + 180 - FlameWidth)))); 
        FlameWidth = 10;
        Flame.moveTo((float)(Ship.POSX + 10 * Math.cos(Math.toRadians(Angle + 180))), (float)(Ship.POSY +  10 * Math.sin(Math.toRadians(Angle + 180))));
        Flame.lineTo((float)(Ship.POSX + 15 * Math.cos(Math.toRadians(Angle + 180 + FlameWidth))), (float)(Ship.POSY +  15 * Math.sin(Math.toRadians(Angle + 180 + FlameWidth)))); 
        Flame.moveTo((float)(Ship.POSX + 20 * Math.cos(Math.toRadians(Angle + 180))), (float)(Ship.POSY +  20 * Math.sin(Math.toRadians(Angle + 180))));
        Flame.lineTo((float)(Ship.POSX + 15 * Math.cos(Math.toRadians(Angle + 180 - FlameWidth))), (float)(Ship.POSY +  15 * Math.sin(Math.toRadians(Angle + 180 - FlameWidth)))); 
        Flame.closePath();
        return Flame;
    }
    
    Projectile Ship = new Projectile(0.5, 10, X / 2, 0, 0, 270);
    GeneralPath Ground = new GeneralPath();
    GeneralPath SHAPE = new GeneralPath();
    
    Point2D.Float[] POINTS = new Point2D.Float[POINTCOUNT];
    Line2D.Float[] LINES = new Line2D.Float[POINTCOUNT];
    boolean LEFT;
    boolean RIGHT;
    boolean FORWARD;
    boolean BACK;
    int COUNT = -1;
    
    
    Timer Stopwatch = new Timer((int)(GUI.Speed * 15), this);
    Background Area;
    public void makeGround(){
        
        int FLATLINE = 10;
        int CLIFF = 5;
        int CLIFFTWO = 8;
        
        do{
            FLATLINE = GUI.randomInt(2, 17);
        }while(FLATLINE > 7 && FLATLINE < 13);
        
        do{
            CLIFF = GUI.randomInt(1, 18);
        }while(CLIFF == FLATLINE || CLIFF == FLATLINE + 1);
        
        do{
           CLIFFTWO = GUI.randomInt(1, 18); 
        }while(CLIFFTWO == FLATLINE || CLIFFTWO == FLATLINE + 1 || CLIFFTWO == CLIFF - 1 || CLIFFTWO == CLIFF || CLIFFTWO == CLIFF + 1);
        Ground.moveTo(X, Y);
        Ground.lineTo(0, Y);
        POINTS[0] = new Point2D.Float(0, 3 * Y / 4);
        for(int i = 1; i < POINTCOUNT - 1; i++){
            if(i == FLATLINE){
                POINTS[i] = new Point2D.Float((float)(X * ((float)i / POINTCOUNT) + GUI.randomInt(10, 15)), (float)(POINTS[i - 1].y));
            }else if(i == FLATLINE + 1){
                POINTS[i] = new Point2D.Float((float)(X * ((float)i / POINTCOUNT) + GUI.randomInt(-5, -10)), (float)(POINTS[i - 1].y));
            }else if(i == CLIFF){
                POINTS[i] = new Point2D.Float((float)(X * ((float)i / POINTCOUNT) + GUI.randomInt(-10, 10)), (float)(POINTS[i - 1].y + GUI.randomInt(-150, -175)));
            }else if(i == CLIFFTWO){
                POINTS[i] = new Point2D.Float((float)(X * ((float)i / POINTCOUNT) + GUI.randomInt(-10, 10)), (float)(POINTS[i - 1].y + GUI.randomInt(125, 150)));
            }else{
                POINTS[i] = new Point2D.Float((float)(X * ((float)i / POINTCOUNT) + GUI.randomInt(-10, 10)), (float)(POINTS[i - 1].y +  GUI.randomInt(-50, 50)));
            }
            if(POINTS[i].y > Y - 10){
                POINTS[i].y = Y - GUI.randomInt(10, 50);
            }
            
        }
        POINTS[POINTCOUNT - 1] = new Point2D.Float(X, POINTS[POINTCOUNT - 2].y + GUI.randomInt(10, 20));
        /*
        COUNT++; POINTS[COUNT] = new Point2D.Float(0, 520);
        COUNT++; POINTS[COUNT] = new Point2D.Float(85, 584);
        COUNT++; POINTS[COUNT] = new Point2D.Float(163, 582);
        COUNT++; POINTS[COUNT] = new Point2D.Float(206, 460);
        COUNT++; POINTS[COUNT] = new Point2D.Float(292, 587);
        COUNT++; POINTS[COUNT] = new Point2D.Float(343, 282);
        COUNT++; POINTS[COUNT] = new Point2D.Float(481, 319);
        COUNT++; POINTS[COUNT] = new Point2D.Float(522, 242);
        COUNT++; POINTS[COUNT] = new Point2D.Float(600, 441);
        COUNT++; POINTS[COUNT] = new Point2D.Float(646, 573);
        COUNT++; POINTS[COUNT] = new Point2D.Float(803, 499);
        COUNT++; POINTS[COUNT] = new Point2D.Float(913, 595);
        COUNT++; POINTS[COUNT] = new Point2D.Float(980, 623);
        COUNT++; POINTS[COUNT] = new Point2D.Float(994, 575);
         */
        COUNT = POINTCOUNT;
        for(int i = 0; i < COUNT; i++){
            try{
                if(i + 1 < COUNT){
                    LINES[i] = new Line2D.Float(POINTS[i], POINTS[i + 1]);
                }else{
                    LINES[i] = new Line2D.Float();
                }
            }catch(NullPointerException e){}
            try{
            Ground.lineTo(POINTS[i].x, POINTS[i].y);
            }catch(NullPointerException e){}
        }
        SHAPE.moveTo(X, 0);
        SHAPE.lineTo(0, 0);
            for(int i = 0; i < COUNT; i++){
                try{
                    SHAPE.lineTo(POINTS[i].x, POINTS[i].y);
                }catch(NullPointerException e){}
            }   
            SHAPE.closePath();
        Ground.lineTo(X, Y);
        Ground.closePath();
    }
    public Astrolander(boolean tORIGINAL){
        //BASIC INITIALIZATION
        super("Window");
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new Background();
        contentArea.add(Area);
        Stopwatch.start();
        this.addKeyListener(this);
        this.addMouseListener(this);
        makeGround();
        ORIGINAL = tORIGINAL;
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }

    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){
   //     System.out.println(e.getKeyChar() + "\t" + LEFT + "\t" + RIGHT + "\t" + FORWARD + "\t" + BACK);
        switch(e.getKeyCode()){
            case 37: LEFT = true; break;
            case 39: RIGHT = true; break;
            case 38: FORWARD = true; break;
            case 40: BACK = true; break;
            
        }
    }
    public void keyReleased(KeyEvent e){
        
        switch(e.getKeyCode()){
            case 37: LEFT = false; break;
            case 39: RIGHT = false; break;
            case 38: FORWARD = false; break;
            case 40: BACK = false; break;
        }
    }
 
    
    public void keyTyped(KeyEvent e){
        
        
    }
    public boolean goodAngle(double SHIPBASEANGLE, double GROUNDANGLE, double SHIPANGLE){
        boolean BOOLA = ((GROUNDANGLE < 180 + MAXLANDINGANGLE && GROUNDANGLE > 180 - MAXLANDINGANGLE) || (GROUNDANGLE < MAXLANDINGANGLE || GROUNDANGLE > 360 - MAXLANDINGANGLE));
        boolean BOOLB = (SHIPANGLE > 270 - MAXLANDINGANGLE && SHIPANGLE < 270 + MAXLANDINGANGLE);
        
        if((BOOLA == true) && (BOOLB == true) && (Math.abs(SHIPBASEANGLE) < MAXLANDINGANGLE || Math.abs(SHIPBASEANGLE) > 360 - MAXLANDINGANGLE)){
            return true;
        }
        if((BOOLA == true) && (BOOLB == true) && (360 + Math.min(SHIPBASEANGLE, GROUNDANGLE) - Math.max(SHIPBASEANGLE, GROUNDANGLE) < 20)){
            return true;
        }
        return false;
    }
    public double speed(double XMomentum, double YMomentum){
        return Math.sqrt(XMomentum * XMomentum + YMomentum * YMomentum);
    }
    public void actionPerformed(ActionEvent e){
        Ship.SHOTS = Math.min(100, Ship.SHOTS + 1);
        Ship.move();
        if(Ship.POSY > Y){
            
            
            Stopwatch.stop();
            return;
        }
        Area.paintComponent(this.getGraphics());
        
        for(int i = 0; i < Ship.LINES.length; i++){
            for(int j = 0; j < COUNT; j++){
                if(Ship.LINES[i].intersectsLine(LINES[j])){
                    Rectangle2D.Float RECT = (Rectangle2D.Float)Ship.LINES[0].getBounds2D();
                    double ANGLE = Math.toDegrees(Math.atan((double)(RECT.height / RECT.width)));
                    Rectangle2D.Float OTHERRECT = (Rectangle2D.Float)LINES[j].getBounds2D();
                    double OTHERANGLE = Math.toDegrees(Math.atan((double)(OTHERRECT.height / OTHERRECT.width)));
                    if((goodAngle(ANGLE, OTHERANGLE, Ship.FACINGANGLE) == true) && (speed(Ship.XMOMENTUM, Ship.YMOMENTUM) < MAXLANDINGSPEED)){
                        
                        Stopwatch.stop();
                        GUI.close("You have landed successfully.", "Success!");
                        
                        return;
            
                    }else{
                        Stopwatch.stop();
                        GUI.close("You have crashed your lander.", "Crash!");
                        return;
                    }
                }
            }
        }
    }
    public void mousePressed(MouseEvent Event){

    }
    public void mouseReleased(MouseEvent Event){

    }
    public void mouseClicked(MouseEvent Event){
        System.out.println(Event.getX() + "\t" + Event.getY());
    }
    public void mouseEntered(MouseEvent Event){
    }
    public void mouseExited(MouseEvent Event){

    }
    public void paint(){
        ((Graphics2D)this.getGraphics()).drawImage(Area.Picture, null, 0, 0);
    }
    
    class Background extends JPanel{
        int COUNT = 100;
        Shape[] OldFlame = new Shape[3];
        BufferedImage Picture = new BufferedImage(X, Y, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D Painter = Picture.createGraphics();
        public void paintComponent(Graphics paint){
            try{
            //System.out.println(Ship.FACINGANGLE + "\t" + Ship.SPEED + "\t" + Ship.POSY + "\t" + Ship.POSX + "\t");
            
            Graphics2D Paint = (Graphics2D)(paint);
            Painter.setColor(Color.black);
            if(COUNT > 0){
                Painter.fill(SHAPE);
                COUNT--;
            }
            
            int temp = ((int)((speed(Ship.XMOMENTUM, Ship.YMOMENTUM) * 100)));
            double printSpeed = (((double)(temp)) / 100);
            
            GeneralPath Image = new GeneralPath();
            int POSY = 60;
            int POSX = 115;
            
            Image.moveTo((float)(POSX + 7  * Math.cos(Math.toRadians(Ship.FACINGANGLE - 127.5))), (float)(POSY + 7 * Math.sin(Math.toRadians(Ship.FACINGANGLE - 127.5))));
            Image.lineTo((float)(POSX + 7  * Math.cos(Math.toRadians(Ship.FACINGANGLE + 127.5))), (float)(POSY + 7 * Math.sin(Math.toRadians(Ship.FACINGANGLE + 127.5))));
            Image.lineTo((float)(POSX + 15  * Math.cos(Math.toRadians(Ship.FACINGANGLE))), (float)(POSY + 15 * Math.sin(Math.toRadians(Ship.FACINGANGLE))));
            Image.closePath();
            Rectangle2D.Float FuelBox = new Rectangle2D.Float(20, 60, 65, 15);
            Rectangle2D.Float Fuel = new Rectangle2D.Float(20, 60, Math.max(1, Ship.FUEL) * 65 / STARTINGFUEL, 15);
            
            Painter.fillRect(0, 0, 200, 100);
            Painter.setColor(printSpeed < MAXLANDINGSPEED ? Color.green : Color.white);
            Painter.drawString("Speed: " + printSpeed, 20, 50);
            
            if(ORIGINAL){
            }else{
                Painter.setColor(goodAngle(0, 0, Ship.FACINGANGLE) == true ? Color.green : Color.white);
                Painter.draw(Image);
            }
            Painter.setColor(Color.green);
            Painter.draw(Fuel);
            Painter.setColor(Color.white);
            Painter.draw(FuelBox);
            try{
                Painter.setColor(Color.black);
                Painter.fill(Ship.OldImage);
       //         Painter.draw(Ship.OldImage);
            }catch(NullPointerException e){}    
            //Painter.fillRect(0, 0,  X, Y);
            
        //    Painter.draw(Ship.Image);
            Painter.setColor(Color.white);
            Painter.fill(Ground);
        //    Painter.draw(Ground);
            //drawFlame(Projectile Ship, int Angle)
            if(ORIGINAL == true){
                for(int i = 0; i < 3; i++){
                    try{
                        Painter.setColor(Color.black);
                        Painter.draw(OldFlame[i]);
                        OldFlame[0] = null;
                    }catch(NullPointerException e){}
                }
                if(BACK == true){
                    Painter.setColor(Color.red);
                    Painter.draw(drawFlame(Ship, Ship.FACINGANGLE));
                    OldFlame[0] = drawFlame(Ship, Ship.FACINGANGLE);
                }
                if(LEFT == true){
                    Painter.setColor(Color.red);
                    Painter.draw(drawFlame(Ship, Ship.FACINGANGLE + 45));
                    OldFlame[1] = drawFlame(Ship, Ship.FACINGANGLE + 45);
                }
                if(RIGHT == true){
                    Painter.setColor(Color.red);
                    Painter.draw(drawFlame(Ship, Ship.FACINGANGLE - 45));
                    OldFlame[2] = drawFlame(Ship, Ship.FACINGANGLE - 45);
                }
            }else{
                try{
                    Painter.setColor(Color.black);
                    Painter.draw(OldFlame[0]);
                    OldFlame[0] = null;
                }catch(NullPointerException e){}
                if(FORWARD == true){
                    Painter.setColor(Color.red);
                    Painter.draw(drawFlame(Ship, Ship.FACINGANGLE));
                    OldFlame[0] = drawFlame(Ship, Ship.FACINGANGLE);
                }
            }
            try{
                Painter.setColor(Color.white);
                Painter.fill(Ship.Image);
            }catch(NullPointerException e){}
            Paint.drawImage(Picture, null, 0, 0);
         }catch(NullPointerException e){}   
        }
    }
    
}
class Turmites extends JFrame implements KeyListener, ActionListener, MouseListener{
    //colors
    //black - 0
    //white - 1
    //blue - 2
    //red - 3
    //green - 4
    final int X = GUI.X;
    final int Y = GUI.Y;
    final int[][] Screen = new int[X][Y];
    class TurmiteVector extends Vector{
        public Turmite turmiteAt(int index){
            return (Turmite)(this.elementAt(index));
        }
        public TurmiteVector(int INTA){
            super(INTA);
            
        }
    }
    
    class Reaction{
            int NewState;
            int Turn;
            int NewColor;
            public Reaction(int tNewState, int tTurn, int tNewColor){
                NewState = tNewState;
                Turn = tTurn;
                NewColor = tNewColor;
            }
        }
    
    public Reaction R(int NewState, int Turn, int NewColor){
                return new Reaction(NewState, Turn, NewColor);
    }
                                 //  BLACK      WHITE
    Reaction[][][] Reactions = {
                                {//0 Line Drawer (Pattern Making)
                                 {R(0, -1, 1),   R(1, 3, 0)},   //STATE 0
                                 {R(1, -1, 1),   R(0, 3, 0)}    //STATE 1
                                }
                                ,
                                {//1 Line Drawer (Slow Collision Recovery)
                                 {R(0, 1, 1),   R(1, -1, 1)},  //STATE 0
                                 {R(1, 1, 1),   R(0, 1, 1)}    //STATE 1
                                }
                                , 
                                {//2 Line Drawer (Super Bouncy)
                                 {R(0, 1, 1),   R(1, -3, 0)},   //STATE 0
                                 {R(1, -1, 1),   R(0, 3, 0)}    //STATE 1
                                }
                                ,
                                {//3 Diamond Drawer (Crystal)
                                 {R(1, 2, 1),   R(0, -2, 1)},  //STATE 0
                                 {R(1, -2, 1),   R(0, 2, 0)}    //STATE 1
                                }
                                ,
                                {//4 Diamond Drawer (Unstable)
                                 {R(1, 0, 1),   R(1, -2, 0)},  //STATE 0
                                 {R(2, 2, 1),   R(2, 2, 1)},  //STATE 1
                                 {R(0, 4, 1),   R(0, 0, 1)}   //STATE 2
                                }
                                ,
                                {//5 Diamond Drawer (Stable)
                                 {R(0, 2, 1),   R(1, 4, 1)},  //STATE 0
                                 {R(0, 2, 1),   R(0, 2, 1)}    //STATE 1
                                }
                                ,
                                {//6 Line Drawer (Thin Diagonal)
                                 {R(0, -3, 1),   R(1, 2, 1)},  //S  TATE 0
                                 {R(1, 2, 1),   R(0, -3, 1)}    //STATE 1
                                }
                                ,
                                {//7 Line Drawer (Fast Thin Bouncy)
                                 {R(1, 1, 1),   R(1, -1, 1)},  //STATE 0
                                 {R(2, 2, 1),   R(2, -2, 1)},  //STATE 1
                                 {R(0, -3, 1),   R(0, 3, 1)}   //STATE 2
                                }
                                ,
                                {//8 Projectile (Large)
                                 {R(0, 3, 1),   R(0, -3, 0)}    //STATE 1
                                }
                                ,
                                {//9 Line Drawer (Double Sided)
                                 {R(0, 1, 1),   R(1, -2, 1)},  //STATE 0
                                 {R(0, 1, 1),   R(2, 1, 1)},  //STATE 1
                                 {R(0, 1, 1),   R(0, 1, 1)}   //STATE 2
                                }
                                ,
                                {//10 Diamond Drawer (With Spiral)
                                 {R(1, 2, 1),   R(0, -2, 0)},  //STATE 0
                                 {R(1, -2, 1),   R(0, 2, 0)}    //STATE 1
                                }
                                ,
                                {//11 Diamond Drawer (Split)
                                 {R(0, -2, 1),   R(1, 4, 0)},  //STATE 0
                                 {R(0, -2, 1),   R(1, 0, 1)}  //STATE 1
                                }
                                ,
                                {//12 Line Drawer (Serrated)
                                 {R(0, 1, 1),   R(1, -3, 0)},  //STATE 0
                                 {R(1, -1, 1),   R(0, 3, 1)}  //STATE 1
                                }
                                ,
                                {//13 Line Drawer (Ant)
                                 {R(0, 2, 1),   R(0, -2, 0)}  //STATE 0
                                }
                                ,
                                {//14 Line Drawer (Hooked)
                                 {R(1, 1, 0),   R(0, 3, 0)},  //STATE 0
                                 {R(1, 1, 1),   R(0, -2, 0)}  //STATE 1
                                }
                                ,
                                {//15 Line Drawer (Worm)
                                 {R(1, -1, 1),   R(0, -1, 1)},  //STATE 0
                                 {R(2, -2, 1),   R(0, 0, 0)},  //STATE 1
                                 {R(2, -1, 1),   R(2, 0, 0)}  //STATE 2
                                }
                                ,
                                {//16 Projectile (Small)
                                 {R(2, 3, 1),   R(2, -3, 0)},  //STATE 0
                                 {R(0, -3, 0),   R(0, 2, 1)},  //STATE 1
                                 {R(0, 0, 0),   R(1, 1, 1)}  //STATE 2
                                }
                                ,
                                {//17 Diamond Drawer (Stable Tilted)
                                 {R(0, 2, 1),   R(1, 4, 1)},  //STATE 0
                                 {R(0, 2, 1),   R(1, 0, 1)}    //STATE 1
                                }
                                ,
                                {//18 Star Drawer
                                 {R(1, 3, 0),   R(0, 0, 1)},  //STATE 0
                                 {R(0, 4, 1),   R(1, 0, 1)},  //STATE 1
                                }
                                ,
                                {//19 Line Drawer (Double Sided Serrated)
                                 {R(1, 3, 0),   R(0, 0, 1)},  //STATE 0
                                 {R(0, 4, 1),   R(1, 0, 1)},  //STATE 1
                                }
                                ,
                                {//20 Triangle Drawer (Outline)
                                 {R(1, 3, 1),   R(1, -3, 1)},  //STATE 0
                                 {R(0, -2, 0),   R(1, 0, 0)},  //STATE 1
                                }
                                ,
                                {//21 Triangle Drawer (Double)
                                 {R(1, 2, 1),   R(0, 0, 1)},  //STATE 0
                                 {R(1, 1, 1),   R(0, -3, 1)},  //STATE 1
                                }
                                ,
                                {//22 Line Drawer (Wide Translucent)
                                 {R(1, -1, 1),   R(1, 3, 0)},  //STATE 0
                                 {R(0, 0, 0),   R(0, 0, 0)},  //STATE 1
                                }
                                ,
                                {//23 Line Drawer (Segmented)
                                 {R(0, 1, 1),   R(1, 2, 0)},  //STATE 0
                                 {R(0, -2, 1),   R(1, -2, 0)},  //STATE 1
                                }
                                ,
                                {//24 Line Drawer (Zig Zag)
                                 {R(0, -2, 1),   R(1, 1, 0)},  //STATE 0
                                 {R(0, 2, 0),   R(1, 1, 0)},  //STATE 1
                                }
                                ,
                                {//25 Line Drawer (Double Sided Fat)
                                 {R(1, 1, 0),   R(0, -3, 1)},  //STATE 0
                                 {R(2, -3, 1),   R(2, 1, 1)},  //STATE 1
                                 {R(1, 2, 1),   R(1, -1, 1)}  //STATE 2
                                }
                                ,
                                {//26 Line Drawer (Wide Elaborate)
                                 
                                 {R(0, -3, 1),   R(1, -1, 0)},  //STATE 1
                                 {R(0, 1, 1),   R(1, -3, 1)}  //STATE 2
                                }
                                ,
                                {//27 Diamond Drawer (Circle Spiky)
                                 {R(2, 1, 1),   R(1, 3, 1)},  //STATE 0
                                 {R(2, -2, 1),   R(2, 3, 1)},  //STATE 1
                                 {R(1, 1, 1),   R(2, -2, 0)}  //STATE 2
                                }
                                ,
                                {//28 Diamond Drawer (Thin)
                                 {R(1, 2, 0),   R(0, 3, 1)},  //STATE 0
                                 {R(2, -2, 1),   R(0, 2, 0)},  //STATE 1
                                 {R(1, 0, 0),   R(2, -1, 1)}  //STATE 2
                                }
                                ,
                                {//29 Dash Drawer (Small Double Slow)
                                 
                                 {R(0, 2, 1),   R(0, 0, 0)}  //STATE 2
                                }
                                ,
                                {//30 Line Drawer (Zig Zag)
                                 {R(2, 3, 1),   R(0, 2, 0)},  //STATE 0
                                 {R(1, -2, 1),   R(2, 1, 0)},  //STATE 1
                                 {R(1, 2, 0),   R(0, -1, 1)}  //STATE 2
                                }
                                ,
                                {//31 Diamond Drawer (Thin)
                                 {R(1, 2, 0),   R(0, 0, 0)},  //STATE 0
                                 {R(0, -1, 1),   R(0, 3, 0)},  //STATE 1
                                }
                                ,
                                {//32 Line Drawer (Zig Zag)
                                 {R(2, 3, 1),   R(0, 2, 0)},  //STATE 0
                                 {R(1, -2, 1),   R(2, 1, 0)},  //STATE 1
                                 {R(1, 2, 0),   R(0, -1, 1)}  //STATE 2
                                }
                                ,
                                {//33 Diamond Drawer (Cross)
                                 {R(2, 2, 1),   R(2, 3, 1)},  //STATE 0
                                 {R(2, -3, 0),   R(1, -1, 1)},  //STATE 1
                                 {R(0, -3, 1),   R(0, 3, 0)}  //STATE 2
                                }
                                ,
                                {//34 Line Drawer (Circle)
                                 {R(1, 1, 1),   R(0, 0, 0)},  //STATE 0
                                 {R(2, 2, 1),   R(2, -1, 0)},  //STATE 1
                                 {R(0, -2, 0),   R(1, -3, 1)}  //STATE 2
                                }
                                ,
                                {//35 Line Drawer (Fuzzy)
                                 {R(2, -1, 1),   R(1, -1, 0)},  //STATE 0
                                 {R(3, 0, 1),   R(1, 2, 0)},  //STATE 1
                                 {R(3, -1, 0),   R(0, 1, 1)},  //STATE 0
                                 {R(2, -1, 1),   R(1, 2, 0)},  //STATE 1
                                }
                                ,
                                {//36 Line Drawer (Double Dotted)
                                 {R(2, 3, 1),   R(0, -1, 0)},  //STATE 0
                                 {R(3, 1, 1),   R(0, -1, 0)},  //STATE 1
                                 {R(2, 3, 0),   R(3, 1, 1)},  //STATE 0
                                 {R(2, -3, 1),   R(0, -2, 0)},  //STATE 1
                                }
                                ,
                                {//37 Line Drawer (Pokey)
                                 {R(3, 3, 0),   R(1, 1, 1)},  //STATE 0
                                 {R(2, 0, 0),   R(1, 3, 0)},  //STATE 1
                                 {R(3, 0, 1),   R(0, 2, 0)},  //STATE 0
                                 {R(0, -1, 1),   R(1, -3, 1)},  //STATE 1
                                }
                                ,
                                {//37 Projectile (Tiny)
                                 {R(3, -2, 0),   R(1, -2, 0)},  //STATE 0
                                 {R(2, 4, 0),   R(1, -3, 1)},  //STATE 1
                                 {R(0, -1, 1),   R(3, -1, 0)},  //STATE 0
                                 {R(1, -3, 1),   R(3, -4, 0)},  //STATE 1
                                }
                               };
                               
    public void randomize(){
        Reaction[][] REACTIONS = Reactions[Reactions.length - 1];
        for(int i = 0; i < REACTIONS.length; i++){
            for(int j = 0; j < 2; j++){
                int INTA = GUI.randomInt(0, REACTIONS.length - 1);
                int INTB = GUI.randomInt(4, -4);
                int INTC = GUI.randomInt(0, 1);
                System.out.print(INTA + "\t" + INTB + "\t" + INTC + "\t\t");
                REACTIONS[i][j] = R(INTA, INTB, INTC);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }
    public void clear(){
        Turmites.clear();
        Graphics2D Painter = (Graphics2D)(Area.Picture.getGraphics());
        Painter.setColor(Color.black);
        Painter.fill(new Rectangle2D.Double(0, 0, Area.Picture.getWidth(), Area.Picture.getHeight()));
        for(int i = 0; i < Screen.length; i++){
            for(int j = 0; j < Screen[i].length; j++){
                Screen[i][j] = 0;
            }
        }
    }
    class Turmite{
        
        
        int POSX;
        int POSY;
        int FACING;
        int TYPE;
        int STATE = 0;
        
        
        public Turmite(int tPOSX, int tPOSY, int tFACING, int tTYPE){
            POSY = tPOSY;
            POSX = tPOSX;
            FACING = tFACING;
            TYPE = tTYPE;
        }
        public void move(){
            moveY();
            moveX();
            if(POSX == Screen.length){
                POSX = POSX - Screen.length;
            }
            if(POSX < 0){
                POSX = POSX + Screen.length;
            }
            if(POSY == Screen[0].length){
                POSY = POSY - Screen[0].length;
            }
            if(POSY < 0){
                POSY = POSY + Screen[0].length;
            }
            Reaction REACTION = Reactions[TYPE][STATE][Screen[POSX][POSY]];
            STATE = REACTION.NewState;
            rotate(REACTION.Turn);
            Screen[POSX][POSY] = REACTION.NewColor;
            
            Color COLOR = new Color(0);
            switch(REACTION.NewColor){
                case 0: COLOR = Color.black; break;
                case 1: COLOR = Color.white; break;
                case 2: COLOR = Color.blue; break;
                case 3: COLOR = Color.red; break;
                case 4: COLOR = Color.green; break;
            }
            if(Area.Picture.getRGB(POSX, POSY) != COLOR.getRGB()){
                Area.Picture.setRGB(POSX, POSY, COLOR.getRGB());
            }
        }
        public void moveY(){
            switch(FACING){
                case 1: case 2: case 3: POSY = POSY + 1; break;
                case 4: case 5: case 6: POSY = POSY;     break;
                case 7: case 8: case 9: POSY = POSY - 1; break;
            }
        }
        public void moveX(){
            switch(FACING){
                case 1: case 4: case 7: POSX = POSX - 1; break;
                case 2: case 5: case 8: POSX = POSX;     break;
                case 3: case 6: case 9: POSX = POSX + 1; break;
            }
        }
        public void rotate(int SWITCH){
            for(int i = 0; i < Math.abs(SWITCH); i++){
               turn((SWITCH < 0 ? false : true));
            }
 
        }
        public void turn(boolean SWITCH){
            int TEMP = FACING;
            switch(TEMP){
                case 7: FACING = (SWITCH ? 8 : 4); break; case 8: FACING = (SWITCH ? 9 : 7); break; case 9: FACING = (SWITCH ? 6 : 8); break; 
        
                case 4: FACING = (SWITCH ? 7 : 1); break;                                           case 6: FACING = (SWITCH ? 3 : 9); break; 
        
                case 1: FACING = (SWITCH ? 4 : 2); break; case 2: FACING = (SWITCH ? 1 : 3); break; case 3: FACING = (SWITCH ? 2 : 6); break; 
            }   

        }

    }
    TurmiteVector Turmites = new TurmiteVector(100);
    Timer Stopwatch = new Timer((int)(GUI.Speed * 0), this);
    Background Area;
    int ReleaseDirection = 2;
    int CurrentTermite = Reactions.length - 1;
    public void init(){
        for(int i = 0; i < Screen.length; i++){
            for(int j = 0; j < Screen[i].length; j++){
                Screen[i][j] = 0;
            } 
        }   
    }
    public void switchTermite(){
        String[] Options = new String[Reactions.length];
        for(int i = 0; i < Reactions.length; i++){
            Options[i] = "" + i;
        }
        int INTA = Math.max(0, JOptionPane.showOptionDialog(null, "Select the Turmite you with to use.", "Turmites", 0, JOptionPane.PLAIN_MESSAGE, null, Options, null));
        CurrentTermite = INTA;
        //showOptionDialog(Component parentComponent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue)
    }
    public Turmites(){
        //BASIC INITIALIZATION
        super("Window");
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new Background();
        contentArea.add(Area);
        init();
        Stopwatch.start();
        this.addKeyListener(this);
        this.addMouseListener(this);
        
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }

    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){

    }
    public void keyReleased(KeyEvent e){
        

    }
 
    
    public void keyTyped(KeyEvent e){
        switch(e.getKeyChar()){
            case ' ': switchTermite(); break;
            case 'r': randomize(); break;
            case 'c': clear(); break;
        }
    }


    public void actionPerformed(ActionEvent e){
        Area.paintComponent(this.getGraphics());
        for(int i = 0; i < Turmites.size(); i++){
            Turmites.turmiteAt(i).move();
        }
    }
    public void mousePressed(MouseEvent Event){

    }
    public void mouseReleased(MouseEvent Event){
    }
    public void mouseClicked(MouseEvent Event){
        if(Event.getButton() == 1){
            int TEMPX = Event.getX();
            int TEMPY = Event.getY();
            Turmites.add(new Turmite(TEMPX, TEMPY, ReleaseDirection, CurrentTermite));
        }else if(Event.getButton() == 3){
            switch(ReleaseDirection){
                                                    case 8: ReleaseDirection = 6; break;
        
                case 4: ReleaseDirection = 8; break;                                      case 6: ReleaseDirection = 2; break; 
        
                                                    case 2: ReleaseDirection = 4; break;
            }  
            
        }
    }
    public void mouseEntered(MouseEvent Event){
    }
    public void mouseExited(MouseEvent Event){

    }
    public void paint(){
        ((Graphics2D)this.getGraphics()).drawImage(Area.Picture, null, 0, 0);
    }
    
    class Background extends JPanel{
        
        
        BufferedImage Picture = new BufferedImage(X, Y, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D Painter = Picture.createGraphics();
        
        public void paintComponent(Graphics paint){
            Graphics2D Paint = (Graphics2D)(paint);
            //System.out.println(Ship.FACINGANGLE + "\t" + Ship.SPEED + "\t" + Ship.POSY + "\t" + Ship.POSX + "\t");
            
          try{
            Paint.drawImage(Picture, null, 0, 0);
          }catch(NullPointerException e){}
        }
    }
    
}

class Snake extends JFrame implements KeyListener, ActionListener{
    
    final int X = GUI.X;
    final int Y = GUI.Y;
    final int PIXEL = 10;
    final int APPLEDURATION = 15 * 25;
    Position[][] Map = new Position[X / PIXEL][Y / PIXEL];
    Timer Stopwatch = new Timer((int)(GUI.Speed * 50), this);
    Background Area;
    
    int Length = 10;
    int Direction = 6;
    int POSX = 20;
    int POSY = 20;
    boolean Active = true;
    boolean Test = true;
    class Position{
        int Duration = 0;
        Color Coloring = Color.black;
        boolean Passable = true;
        int Bonus = 0;
        char Shape;
        public Position(int tDuration, Color tColoring, boolean tPassable, int tBonus, char tShape){
            Duration = tDuration;
            Coloring = tColoring;
            Passable = tPassable;
            Bonus = tBonus;
            Shape = tShape;
        }


        public void update(){
            if(Duration > 0){
                Duration = Math.max(0, Duration - 1);
                if(Duration == 0){
                    Coloring = new Color(0);
                    Passable = true;
                    Bonus = 0;
                    Shape = 'r';
                }else if(Bonus > 0 && Duration < 75){
                    if(Coloring.equals(Color.red)){
                        Coloring = new Color(Coloring.getRed() / 2, Coloring.getGreen() / 2, Coloring.getBlue() / 2);
                    }else if(Coloring.equals(Color.yellow)){
                        Coloring = new Color(Coloring.getRed() / 2, Coloring.getGreen() / 2, Coloring.getBlue() / 2);
                    }
                }
            }
        }
    }
    
    public void makeMap(){
        for(int i = 0; i < Map.length; i++){
            for(int j = 0; j < Map[i].length; j++){
                if(i <= 0 || i >= Map.length - 1 || j <= 3 || j >= Map[i].length - 1){
                    
                    Area.paintPixel(i, j, Color.white , 'r');
                    Map[i][j] = new Position(0, Color.white, false, 0, 'r');
                }else{
                    Area.paintPixel(i, j, Color.black, 'r');
                    Map[i][j] = new Position(0, Color.black, true, 0, 'r');
                }
            }
        }
 
       
    }
    
    public Snake(){
        //BASIC INITIALIZATION
        super("Window");
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
                
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new Background();
        makeMap();
        contentArea.add(Area);
        //Stopwatch.start();
        this.addKeyListener(this);

        Stopwatch.start();
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }

    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){
        try{
                switch(e.getKeyCode()){
                    case 38: if(Direction != 2 && Active == true)Direction = 8; Active = false; break;
                    case 39: if(Direction != 4 && Active == true)Direction = 6; Active = false; break;
                    case 37: if(Direction != 6 && Active == true)Direction = 4; Active = false; break;
                    case 40: if(Direction != 8 && Active == true)Direction = 2; Active = false; break;
                    case 32: if(Stopwatch.isRunning()){Stopwatch.stop();}else{Stopwatch.start();}
                }
        }catch(NumberFormatException u){}
    }
    public void keyReleased(KeyEvent e){
 
    }
 
    
    public void keyTyped(KeyEvent e){
        
    }

    public int appleCount(){
        int INTA = 0;
        for(int i = 0; i < Map.length; i++){
            for(int j = 0; j < Map[i].length; j++){
                if((Map[i][j].Bonus > 0)){
                    INTA = INTA + 1;  
                }
            }
        }
        return INTA;
    }
    public void updateMap(){
        for(int i = 0; i < Map.length; i++){
            for(int j = 0; j < Map[i].length; j++){
                Map[i][j].update();
                Area.paintPixel(i, j, Map[i][j].Coloring, Map[i][j].Shape);
                
            }
        }
    
    }
    public void move(){
        POSX = GUI.numpadX(POSX, Direction);
        POSY = GUI.numpadY(POSY, Direction);
        Active = true;
        if((Map[POSX][POSY].Passable == false)){
            if((Map[POSX][POSY].Duration == 0)){
                Stopwatch.stop();
                GUI.close("You have run into a wall.", "");
                return;
            }else{
                Stopwatch.stop();
                GUI.close("You have run into yourself.", "");
                return;
            }
            
        }else{
            if(Map[POSX][POSY].Bonus > 0){
                addLength(Map[POSX][POSY].Bonus);
            }
            Map[POSX][POSY] = new Position(Length, Color.white, false, 0, 'r');
        }
    }
    public void addLength(int INTA){
        for(int i = 0; i < Map.length; i++){
            for(int j = 0; j < Map[i].length; j++){
                if((Map[i][j].Passable != true) && (Map[i][j].Duration > 0)){
                    Map[i][j].Duration = Map[i][j].Duration + INTA;
                }
            }
        }
        Length = Length + INTA;
    
    }
    public void addApples(){
        if((GUI.randomInt(1, 100) >= 90 + appleCount()) && (GUI.randomInt(1, 1 + appleCount()) == 1)){
            int INTX = GUI.randomInt(0, Map.length - 1);
            int INTY = GUI.randomInt(0, Map[INTX].length - 1);
            if(Map[INTX][INTY].Coloring == Color.black){
                if(GUI.randomInt(1, 20) == 20){
                    Map[INTX][INTY] = new Position(APPLEDURATION, Color.yellow, true, 5, 'c');
                }else{
                    Map[INTX][INTY] = new Position(APPLEDURATION, Color.red, true, 2, 'c');
                }
            }
        }
    }
    public void actionPerformed(ActionEvent e){
        move();
        addApples();
        updateMap();
        Area.paintComponent(this.getGraphics());
        if(Test){
            Test = false;
            Stopwatch.stop();
        }
    }
    
   
    
    public void paint(){
        
        
    }
    class Background extends JPanel{
        
        BufferedImage Picture = new BufferedImage(X, Y, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics Painter = Picture.getGraphics();
        
        public void paintPixel(int INTX, int INTY, Color COLOR, char SHAPE){
            Painter.setColor(COLOR);
            switch(SHAPE){
                case 'r': Painter.fillRect(INTX * PIXEL, INTY * PIXEL, PIXEL, PIXEL); break;
                case 'c': Painter.fillOval(INTX * PIXEL, INTY * PIXEL, PIXEL, PIXEL); break;
            }
            
        }
        
        public void paintComponent(Graphics paint){
            
            try{
                Graphics2D Painter = Picture.createGraphics();
                Graphics2D Paint = (Graphics2D)(paint);
            
                Painter.setColor(Color.black);
            
                Paint.drawImage(Picture, null, 0, 0);
            }catch(NullPointerException e){}
        }
    }

}
class Pong extends JFrame implements KeyListener, ActionListener{
    
    final int X = GUI.X;
    final int Y = GUI.Y;
    
    final int TopBorderWidth = 31;
    final int BottomBorderWidth = 5;
    
    public void bounce(boolean First){
        GUI.playSound(First, "TAH.wav", 0, 2);
    
    }
    public void cheer(boolean First, int Switch){
        GUI.playSound(First, "CHEERS" + Switch + ".wav", 0, 2);
        
    }
    class Padel{
        int POSX;
        int POSY;
        int Thickness;
        int Length;
        
        Rectangle2D.Float Face;
        Rectangle2D.Float CatchFace;
        Rectangle2D.Float UpSide;
        Rectangle2D.Float DownSide;
        
        int Speed;
        public Padel(int tPOSX, int tPOSY, int tThickness, int tLength, int tSpeed){
            POSX = tPOSX;
            POSY = tPOSY;
            Thickness = tThickness;
            Length = tLength;
            Speed = tSpeed;
            
            Face = new Rectangle2D.Float();
            CatchFace = new Rectangle2D.Float();
            UpSide = new Rectangle2D.Float();
            DownSide = new Rectangle2D.Float();
        }
        public void move(char Side){
            Area.Painter.setColor(Color.black);
            Area.Painter.fill(Face);
            Area.Painter.fill(UpSide);
            Area.Painter.fill(DownSide);
            Area.Painter.setColor(Color.white);
            switch(Side){
                case 'r': if(UpRight == true){
                                POSY = Math.max(POSY - Speed, TopBorderWidth);     
                          }
                          if(DownRight == true){
                                POSY = Math.min(POSY + Speed, Y - Length - BottomBorderWidth);
                          }
                          
                          Face.setRect(POSX, POSY, 1, Length);
                          CatchFace.setRect(POSX + 1, POSY + 1, 4, Length - 1);
                          UpSide.setRect(POSX, POSY, Thickness, 1);
                          DownSide.setRect(POSX, POSY + Length, Thickness, 1);
                          break;
                case 'l': if(UpLeft == true){
                                POSY =  Math.max(POSY = POSY - Speed, TopBorderWidth);     
                          }
                          if(DownLeft == true){
                                POSY = Math.min(POSY + Speed, Y - Length - BottomBorderWidth);       
                          }
                          
                          Face.setRect(POSX + Thickness, POSY, 1, Length);
                          CatchFace.setRect(POSX + Thickness - 1, POSY + 1, 4, Length - 1);
                          UpSide.setRect(POSX, POSY, Thickness, 1);
                          DownSide.setRect(POSX, POSY + Length, Thickness, 1);
                          break;
                }
            
            POSY = Math.max(POSY, 31);
            POSY = Math.min(POSY, Y - Length - 5);
            Area.Painter.fill(Face);
            Area.Painter.fill(UpSide);
            Area.Painter.fill(DownSide);
            

        }
            
            
        
    }
    
    boolean UpRight;
    boolean DownRight;
    boolean UpLeft;
    boolean DownLeft;
    
    double XMomentum = 4 * (GUI.randomInt(0, 1) == 0 ? -1 : 1);
    double YMomentum = 4 * (GUI.randomInt(0, 1) == 0 ? -1 : 1);
    double BallX = X / 2;
    double BallY = Y / 2;
    int BallWidth = 10;
    int BallHeight = 10;
    
    int BounceCounter = 0;
    int RespawnCounter = -1;
    
    int LeftScore = 0;
    int RightScore = 0;
    Ellipse2D.Float Ball = new Ellipse2D.Float((float)BallX, (float)BallY, BallWidth, BallHeight);
    
    Padel LeftPadel = new Padel(40, (Y + 75) / 2, 5, 75, 8);
    Padel RightPadel = new Padel(X - 40, (Y + 75) / 2, 5, 75, 8 - 1);
    Timer Stopwatch = new Timer((int)(GUI.Speed * 10), this);
    Background Area;
    
    int AICounter = 0;
    
    public double checkYAI(){
        double PadelX = RightPadel.POSX;
        double Distance = PadelX - BallX;
        double Gradient = YMomentum / XMomentum;
        double EndingY = Distance * Gradient + BallY;
        return EndingY;
    }
    
    public void moveAI(){
    
        double TargetY = checkYAI();
        if((GUI.difference(RightPadel.POSX, BallX) < XMomentum * 45 + 25) && (XMomentum > 0)){
            if(TargetY > RightPadel.POSY + (RightPadel.Length / 2)){
                if((GUI.difference(TargetY, RightPadel.POSY + (RightPadel.Length / 2)) > RightPadel.Length / 2 - 10)){
                    DownRight = true;
                }else{
                    DownRight = false;
                }
                UpRight = false;
            }else if(TargetY < RightPadel.POSY + (RightPadel.Length / 2)){
                if((GUI.difference(TargetY, RightPadel.POSY + (RightPadel.Length / 2)) > RightPadel.Length / 2 - 10)){
                    UpRight = true;
                }else{
                    UpRight = false;
                }
                DownRight = false;
            }
        }
    }
    public void moveBall(){
        BallX = BallX + XMomentum;
        BallY = BallY + YMomentum;
        Area.Painter.setColor(Color.black);
        Area.Painter.fill(Ball);
        Ball.setFrame(BallX, BallY, BallWidth, BallHeight);
        Area.Painter.setColor(Color.white);
        Area.Painter.fill(Ball);
        if(BallY < TopBorderWidth){
            bounce(true);
            YMomentum = Math.abs(YMomentum);
            
        }
        if(BallY > Y - BottomBorderWidth - BallHeight){
            bounce(true);
            YMomentum = -Math.abs(YMomentum);
            
        }
        if(BounceCounter == 0){
            if(Ball.intersects(LeftPadel.Face) || Ball.intersects(LeftPadel.CatchFace)){
                bounce(true);
                XMomentum = -XMomentum;
                BounceCounter = 50;
                XMomentum = XMomentum + 0.2;
                if(UpLeft == true){
                    YMomentum = YMomentum - 1;
                }
                if(DownLeft == true){
                    YMomentum = YMomentum + 1;
                }
                
            }
            if(Ball.intersects(RightPadel.Face) || Ball.intersects(RightPadel.CatchFace)){
                bounce(true);
                XMomentum = -XMomentum;
                BounceCounter = 50;
                if(UpRight == true){
                    YMomentum = YMomentum - 1;
                }
                if(DownRight == true){
                    YMomentum = YMomentum + 1;
                }
                
            }
        }
        if(Ball.intersects(LeftPadel.DownSide) || Ball.intersects(RightPadel.DownSide)){
            bounce(true);
            YMomentum = Math.abs(YMomentum);
            
        }
        if(Ball.intersects(RightPadel.UpSide) || Ball.intersects(LeftPadel.UpSide)){
            bounce(true);
            YMomentum = -Math.abs(YMomentum);
            
        }
        if(BallX < 0 - 100){
            addScore('r');
        }
        if(BallX + BallWidth > X + 100){
            addScore('l');
        }
    }
    public void addScore(char Scorer){
        
        if(Scorer == 'l'){
            LeftScore = LeftScore + 1;
            cheer(true, 1);
        }
        if(Scorer == 'r'){
            RightScore = RightScore + 1;
            cheer(true, 2);
        }
        BallX = X / 2;
        BallY = Y / 2;
        XMomentum = 0;
        YMomentum = 0;
        UpRight = false;
        DownRight = false;
        UpLeft = false;
        DownLeft = false;
        LeftPadel.POSY = (Y - LeftPadel.Length) / 2;
        RightPadel.POSY = (Y - RightPadel.Length) / 2;
        RespawnCounter = 0;
    }
    public void initSounds(){
        bounce(false);
        cheer(false, 1);
        cheer(false, 2);
        
    }
    public Pong(){
        //BASIC INITIALIZATION
        super("Window");
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new Background();
        contentArea.add(Area);
        this.addKeyListener(this);
        initSounds();
        
        Stopwatch.start();
        
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }

    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){
   //     System.out.println(e.getKeyChar() + "\t" + LEFT + "\t" + RIGHT + "\t" + FORWARD + "\t" + BACK);
        
        switch(e.getKeyCode()){
            case 38: UpLeft = true; break;
            case 40: DownLeft = true; break;

        }
    }
    public void keyReleased(KeyEvent e){
        switch(e.getKeyCode()){
            case 38: UpLeft = false; break;
            case 40: DownLeft = false; break;
        }
    }
 
    
    public void keyTyped(KeyEvent e){

    }
    
    public void actionPerformed(ActionEvent e){
        AICounter = AICounter + 1;
        if(AICounter % (int)(GUI.difference(RightPadel.POSX, BallX) / 15 + Math.abs(YMomentum) / 3 + 1) == 0){
            moveAI();
            AICounter = 0;
        }
        LeftPadel.move('l');
        RightPadel.move('r');
        moveBall();
        
        BounceCounter = Math.max(BounceCounter - 1, 0);
        Area.paintComponent(this.getGraphics());
        if(RespawnCounter != -1){
            RespawnCounter = RespawnCounter + 1;
        }
        if(RespawnCounter == 100){
            RespawnCounter = -1;
            
                XMomentum = 3 * (GUI.randomInt(0, 1) == 0 ? -1 : 1);
                YMomentum = 3 * (GUI.randomInt(0, 1) == 0 ? -1 : 1);

        }
        
    }
    
    
    public void paint(){
        ((Graphics2D)this.getGraphics()).drawImage(Area.Picture, null, 0, 0);
        
    }
    class Background extends JPanel{
        BufferedImage Picture = new BufferedImage(X, Y, BufferedImage.TYPE_BYTE_GRAY);
        Graphics painter = Picture.getGraphics();
        Graphics2D Painter = Picture.createGraphics();
        
        public void paintComponent(Graphics paint){
            
            
            Graphics2D Paint = (Graphics2D)(paint);
            

            //Painter.fill(LeftPadel.Shape);
            //Painter.fill(RightPadel.Shape);
            Painter.setColor(Color.black);
            Painter.fillRect(5 * X / 10 - 20, Y / 8 + 5, 30, 10);
            Painter.fillRect(5 * X / 10 + 20, Y / 8 + 5, 30, 10);
            Painter.fillRect(5 * X / 10 - 10, Y / 8 - 10, 35, 10);
            Painter.setColor(Color.white);
            Painter.drawString("Score", 5 * X / 10 - 10, Y / 8);
            Painter.drawString(LeftScore + "", 5 * X / 10 - 20, Y / 8 + 15);
            Painter.drawString(RightScore + "", 5 * X / 10 + 20, Y / 8 + 15);
            try{
                Paint.drawImage(Picture, null, 0, 0);
            }catch(NullPointerException e){}
        }
    }

}
class Bricks extends JFrame implements KeyListener, ActionListener{
    
    final int X = GUI.X;
    final int Y = GUI.Y;
    
    final int TopBorderWidth = 31;
    final int RightBorderWidth = 175;
    final int LeftBorderWidth = 175;
    final int BrickWidth = 50;
    final int BrickHeight = 15;
    final int ColumnWidth = X - RightBorderWidth - LeftBorderWidth;
    int BallsLeft = 3;
    public void brickSound(){
        GUI.playSound(true, "TIK.wav", 0, 2);
    }
    public void bounceSound(){
        GUI.playSound(true, "TINK.wav", 0, 2);
    }
    class BrickVector extends Vector{
        public Brick element(int INTA){
            return (Brick)elementAt(INTA);
            
        }
        
    }
    
    class Brick{
        int POSX;
        int POSY;
        int Thickness;
        int Length;
        int RightLeftBounceCounter;
        int UpDownBounceCounter;
        
        Rectangle2D.Float RightSide;
        Rectangle2D.Float UpSide;
        Rectangle2D.Float DownSide;
        Rectangle2D.Float LeftSide;
        Rectangle2D.Float InSide;
        boolean Moveable = false;
        int Speed;
        Color Tint = new Color(0, 0, 0);
        public Brick(int tPOSX, int tPOSY, int tThickness, int tLength, int tSpeed, boolean tMoveable, Color tTint){
            POSX = tPOSX;
            POSY = tPOSY;
            Thickness = tThickness;
            Length = tLength;
            Speed = tSpeed;
            Moveable = tMoveable;
            
            RightSide = new Rectangle2D.Float();
            UpSide = new Rectangle2D.Float();
            DownSide = new Rectangle2D.Float();
            LeftSide = new Rectangle2D.Float();
            InSide = new Rectangle2D.Float();
            Tint = tTint;
        }
        public void checkIntersect(){
            if((Ball.intersects(RightSide) || Ball.intersects(LeftSide)) && RightLeftBounceCounter == 0){
                if(Moveable == true){
                    XMomentum = XMomentum + (Right == true ? 3 : 0);
                    XMomentum = XMomentum - (Left == true ? 3 : 0);
                    bounceSound();
                }else{
                    brickSound();   
                }
                if(Ball.intersects(RightSide)){
                    XMomentum = Math.abs(XMomentum);
                }else if(Ball.intersects(LeftSide)){
                    XMomentum = -Math.abs(XMomentum);    
                }
                RightLeftBounceCounter = 25;
                if(Moveable == false){
                    Bricks.remove(this);
                    Area.Painter.setColor(Color.black);
                    Area.Painter.draw(RightSide);
                    Area.Painter.draw(LeftSide);
                    Area.Painter.draw(UpSide);
                    Area.Painter.draw(DownSide);
                    Area.Painter.fill(InSide);
                    Area.Painter.setColor(Color.white);
                }
            }
            if((Ball.intersects(UpSide) || Ball.intersects(DownSide)) && UpDownBounceCounter == 0){
                if(Moveable == true){
                    XMomentum = XMomentum + (Right == true ? 1 : 0);
                    XMomentum = XMomentum - (Left == true ? 1 : 0);
                    bounceSound();
                }else{
                    brickSound();   
                }
                if(Ball.intersects(UpSide)){
                    YMomentum = -Math.abs(YMomentum);
                }else if(Ball.intersects(DownSide)){
                    YMomentum = Math.abs(YMomentum);
                }
                UpDownBounceCounter = 25;
                if(Moveable == false){
                    Bricks.remove(this);
                    Area.Painter.setColor(Color.black);
                    Area.Painter.draw(RightSide);
                    Area.Painter.draw(LeftSide);
                    Area.Painter.draw(UpSide);
                    Area.Painter.draw(DownSide);
                    Area.Painter.fill(InSide);
                    Area.Painter.setColor(Color.white);
                }
            }
            
        }
        public void move(){
            Area.Painter.setColor(Color.black);
            Area.Painter.draw(RightSide);
            Area.Painter.draw(LeftSide);
            Area.Painter.draw(UpSide);
            Area.Painter.draw(DownSide);
            Area.Painter.fill(InSide);
            
            UpDownBounceCounter = Math.max(UpDownBounceCounter - 1, 0);
            RightLeftBounceCounter = Math.max(RightLeftBounceCounter - 1, 0);
            if(Left == true){
                POSX = Math.max(POSX - Speed, 0 + LeftBorderWidth);     
            }
            if(Right == true){
                POSX = Math.min(POSX + Speed, X - RightBorderWidth - Thickness);
            }
                          
            RightSide.setRect(POSX + Thickness, POSY, 0.1, Length);
            UpSide.setRect(POSX, POSY, Thickness, 0.1);
            DownSide.setRect(POSX, POSY + Length, Thickness, 0.1);
            LeftSide.setRect(POSX, POSY, 0.1, Length);
            InSide.setRect(POSX, POSY, Thickness, Length);
                
            if(Speed == 0){
                Area.Painter.setColor(new Color(Tint.getRed() / 2, Tint.getGreen() / 2, Tint.getBlue() / 2));
                Area.Painter.fill(InSide);
                Area.Painter.setColor(Tint);
                Area.Painter.drawLine(POSX + 1, POSY + 1, POSX + Thickness - 2, POSY + 1);
                Area.Painter.drawLine(POSX + 1, POSY + 1, POSX + 1, POSY + Length - 2);
            }else{
                Area.Painter.setColor(new Color(Tint.getRed() / 2, Tint.getGreen() / 2, Tint.getBlue() / 2));
                Area.Painter.fill(InSide);
                Area.Painter.setColor(Tint);
                Area.Painter.drawLine(POSX, POSY, POSX + Thickness, POSY);
                Area.Painter.drawLine(POSX, POSY, POSX, POSY + Length);
                Area.Painter.drawLine(POSX + Thickness, POSY, POSX + Thickness, POSY + Length);
                Area.Painter.drawLine(POSX, POSY + Length, POSX + Thickness, POSY + Length);
            
            }
           // 
            
            

        }
            
            
        
    }
    
    boolean Left;
    boolean Right;

    
    double XMomentum = 3.5 * (GUI.randomInt(0, 1) == 1 ? 1 : -1);
    double YMomentum = 3.5 * (GUI.randomInt(0, 1) == 1 ? -1 : -1);
    double BallX = X / 2;
    double BallY = Y / 2;
    int BallWidth = 10;
    int BallHeight = 10;
    
    int GlobalBounceCounter = 0;
    int RespawnCounter = -1;
    
    BrickVector Bricks = new BrickVector();
    Ellipse2D.Float Ball = new Ellipse2D.Float((float)BallX, (float)BallY, BallWidth, BallHeight);
    
    Brick Padel = new Brick(X / 2, Y - 40, 75, 5, 8, true, Color.white);
    Timer Stopwatch = new Timer((int)(GUI.Speed * 10), this);
    Background Area;
    public void resetBall(){
        Left = false;
        Right = false;
        Padel.POSX = X / 2 - Padel.Thickness / 2;
        XMomentum = 0;
        YMomentum = 0;
        BallX = X / 2 - BallWidth / 2;
        BallY = Padel.POSY - 10;
        RespawnCounter = 100;
    }
    public void relaunchBall(){
        YMomentum = -3.5;
        XMomentum = 3.5 * (GUI.randomInt(0, 1) == 1 ? 1 : -1);
    }
    public void fillGap(){
        Area.Painter.setColor(Color.black);
        Area.Painter.fill(Ball);
    }
    public void moveBall(){
        BallX = BallX + XMomentum;
        BallY = BallY + YMomentum;
        
        Ball.setFrame(BallX, BallY, BallWidth, BallHeight);
        Area.Painter.setColor(Color.white);
        Area.Painter.fill(Ball);
        if(BallY < TopBorderWidth && GlobalBounceCounter == 0){
            YMomentum = Math.abs(YMomentum);
            bounceSound();
        }
        if(BallX > X - RightBorderWidth - BallWidth && GlobalBounceCounter == 0){
            XMomentum = -Math.abs(XMomentum);
            bounceSound();
        }
        if(BallX < 0 + LeftBorderWidth && GlobalBounceCounter == 0){
            XMomentum = Math.abs(XMomentum);
            bounceSound();
        }
        if(BallY > Y){
            XMomentum = 0;
            YMomentum = 0;
            BallY = -1;
            BallsLeft = BallsLeft - 1;
            if(BallsLeft < 0){
                Stopwatch.stop();
                GUI.close("You have lost your last ball.", "");
                return;
            }else{
                resetBall();
            }
        }
        if(Bricks.size() == 0){
            GUI.playSound(true, "CHEERS2.wav", 0, 2);
            Stopwatch.stop();

            GUI.close("You destroyed all the bricks.", "");
            
            
            return;
        }
    }
    public void makeBricks(){
        for(int i = 0; i < ColumnWidth / BrickWidth; i++){
            for(int j = 3; j < 15; j++){
                
                    int color = GUI.randomInt(1, 8);
                    
                    Color Coloring = Color.white;
                    
                    switch(color){
                        case 1: Coloring = Color.green; break;
                        case 2: Coloring = Color.blue; break;
                        case 3: Coloring = Color.cyan; break;
                        case 4: Coloring = Color.magenta; break;
                        case 5: Coloring = Color.orange; break;
                        case 6: Coloring = Color.red; break;
                        case 7: Coloring = Color.white; break;
                        case 8: Coloring = Color.yellow; break;
                        
                    }
                    
                    
                    Bricks.add(new Brick(LeftBorderWidth + i * BrickWidth, TopBorderWidth + j * BrickHeight, BrickWidth, BrickHeight, 0, false, Coloring));

            }
        }
        
    }
    public void initSounds(){
        GUI.playSound(false, "TINK.wav", 0, 2);
        GUI.playSound(false, "TOK.wav", 0, 2);   
        GUI.playSound(false, "CHEERS2.wav", 0, 2);
        
    }
    
    public Bricks(){
        //BASIC INITIALIZATION
        super("Window");
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new Background();
        contentArea.add(Area);
        makeBricks();
        initSounds();
        
        Stopwatch.start();
        this.addKeyListener(this);
    
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }

    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){
        //System.out.println(e.getKeyChar() + "\t" + Left + "\t" + Right);
        
        switch(e.getKeyCode()){
            case 37: Left = true; break;
            case 39: Right = true; break;
        }
    }
    public void keyReleased(KeyEvent e){
        switch(e.getKeyCode()){
            case 37: Left = false; break;
            case 39: Right = false; break;
        }
    }

    public void keyTyped(KeyEvent e){

    }
    
    public void actionPerformed(ActionEvent e){
        fillGap();
        GlobalBounceCounter = Math.max(GlobalBounceCounter - 1, 0);
        if(RespawnCounter > 0){
            XMomentum = 0;
            YMomentum = 0;
            RespawnCounter--;
            Left = false;
            Right = false;
        }else if(RespawnCounter == 0){
            RespawnCounter = -1;
            relaunchBall();
        }
        Padel.move();
        
        moveBall();
        for(int i = 0; i < Bricks.size(); i++){
            try{
                Bricks.element(i).checkIntersect();
                Bricks.element(i).move();
            }catch(ArrayIndexOutOfBoundsException f){}
            
        }
        Padel.checkIntersect();
        
        
        Area.paintComponent(this.getGraphics());
        
        
    }
    
    
    public void paint(){
        ((Graphics2D)this.getGraphics()).drawImage(Area.Picture, null, 0, 0);
        
    }
    class Background extends JPanel{
        BufferedImage Picture = new BufferedImage(X, Y, BufferedImage.TYPE_USHORT_555_RGB);
        Graphics painter = Picture.getGraphics();
        Graphics2D Painter = Picture.createGraphics();
        
        public void paintComponent(Graphics paint){
            Painter.setColor(Color.white);
            Painter.drawRect(0, 0, LeftBorderWidth - 1, Y);
            Painter.drawRect(X - RightBorderWidth + 1, 0, RightBorderWidth - 1, Y);
            Painter.drawString("Balls:", X - RightBorderWidth / 2 - 20, 4 * Y / 5);
            
            for(int i = 1; i <= 3; i++){
                Painter.setColor(Color.black);
                Painter.fillOval(X - RightBorderWidth / 2 - 40 + i * 15, 4 * Y / 5 + 10, 10, 10);
                if(i <= BallsLeft){
                    Painter.setColor(Color.white);
                    Painter.fillOval(X - RightBorderWidth / 2 - 40 + i * 15, 4 * Y / 5 + 10, 10, 10);   
                }
            }
            Graphics2D Paint = (Graphics2D)(paint);
            

            //Painter.fill(LeftPadel.Shape);
            //Painter.fill(RightPadel.Shape);
            try{
                Paint.drawImage(Picture, null, 0, 0);
            }catch(NullPointerException e){}
        }
    }

}

class Tetris extends JFrame implements KeyListener, ActionListener{
    
    final int X = GUI.X;
    final int Y = GUI.Y;
    final int BlockWidth = 20;
    final int Width = 13;
    final int Height = Y / BlockWidth;
    final int LeftBorder = (X - BlockWidth * Width) / 2;
    final int ShapeSize = 7;
    int SoundCounter = 0;
    Position[][] Map = new Position[Width][Height];
    Timer Stopwatch = new Timer((int)(GUI.Speed * 15), this);
    Background Area;
    int Counter = 0;
    int LinesCleared = 0;
    LoopingMidi Music = new LoopingMidi("Tetris.mid", 0);
    Timer MoveTimer = new Timer((int)(GUI.Speed * 60), new ActionListener(){
    	public void actionPerformed(ActionEvent evt) {
            try{
	        if(Left == true){
                    MovingBlock.move(-1, 0, true);
                }
                if(Right == true){
                    MovingBlock.move(1, 0, true);
                }
                if(Down == true){
                    MovingBlock.move(0, 1, true);
                }
            }catch(NullPointerException e){}
    	}    
    });
    public void moveSound(){
        if(SoundCounter == 0){
            GUI.playSound(true, "MOVPIECE.wav", 0, 2);
            GUI.playSound(true, "MOVPIECE.wav", 0, 2);
            SoundCounter = 3;
        }
    }
    public void printSquare(int POSX, int POSY, Color Tint){
        int INTX = LeftBorder + POSX * BlockWidth;
        int INTY = POSY * BlockWidth;
        Area.Painter.setColor(new Color(Tint.getRed() / 2, Tint.getGreen() / 2, Tint.getBlue() / 2));
        Area.Painter.fillRect(INTX, INTY, BlockWidth, BlockWidth);
        Area.Painter.setColor(Tint);
        Area.Painter.drawRect(INTX + 1, INTY + 1, BlockWidth - 2, BlockWidth - 2);
    }
    class Position{
        Color Tint;
        boolean Filled;
        boolean Final;
        public Position(){
            Tint = Color.black;
            Filled = false;
            Final = false;
        }
        public void setPosition(Color tTint, boolean tFilled, boolean tFinal){
            Tint = tTint;
            Filled = tFilled;
            Final = tFinal;
        }
    }
    class Block{
        int[][] Shape;
        int Rotation;
        int POSY;
        int POSX;
        int INTX = 0;
        int INTY = 0;
        int Direction = 2;
        boolean Rotateable;
        Color Tint = new Color(0);
        public void imprint(){
            for(int i = 0; i < ShapeSize; i++){
                for(int j = 0; j < ShapeSize; j++){
                    if(Shape[i][j] == 1 || Shape[i][j] == 2){
                        if(j - INTY + POSY < 1){
                            Stopwatch.stop();
                            GUI.close("You lose", "");
                            return;
                        }   
                        Map[i - INTX + POSX][j - INTY + POSY].setPosition(Tint, true, true);
                        
                    }
                }
            }
            
            checkLines();
        }
        public void rotate(int Rotation, boolean Primary){
            if(Primary == true){
                this.print(Color.black);
            }
            moveSound();
            int[][] Temp = new int[ShapeSize][ShapeSize];
            for(int i = 0; i < ShapeSize; i++){
                for(int j = 0; j < ShapeSize; j++){
                    if(Shape[i][j] == 1 || Shape[i][j] == 2){
                        int TEMPX = i - INTX;
                        int TEMPY = j - INTY;
                        if(Rotation == 1){
                            Temp[-TEMPY + INTX][TEMPX + INTY] = Shape[i][j];
                        }else{
                            Temp[TEMPY + INTX][-TEMPX + INTY] = Shape[i][j];
                        }
                    }
                }
            }
            Shape = Temp;
            if(checkOverlap() && (Primary == true)){
                rotate(-Rotation, false);
            }
            if(Primary == true){
                this.print(Tint);
            }
        }
        public boolean checkOverlap(){
            for(int i = 0; i < ShapeSize; i++){
                for(int j = 0; j < ShapeSize; j++){
                    if(Shape[i][j] == 1 || Shape[i][j] == 2){
                        try{
                            if(Map[i - INTX + POSX][j - INTY + POSY].Filled == true){
                                return true;
                            }
                        }catch(ArrayIndexOutOfBoundsException e){return true;}
                    }
                }
            }
            return false;
        }
        public Block(int tShape){
            POSX = Map.length / 2;
            POSY = 1;
            
            switch(tShape){
                case 1:{int[][] Temp = {{0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 1, 0, 0, 0},
                                        {0, 0, 1, 2, 1, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0}};
                                        Shape = Temp;
                                        Rotateable = true;
                                        Tint = Color.red;
                                        break;}
                case 2:{int[][] Temp = {{0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 1, 1, 0, 0},
                                        {0, 0, 0, 2, 0, 0, 0},
                                        {0, 0, 0, 1, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0}};
                                        Shape = Temp;
                                        Rotateable = true;
                                        Tint = Color.blue;
                                        break;}
                case 3:{int[][] Temp = {{0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 1, 1, 0, 0, 0},
                                        {0, 0, 0, 2, 0, 0, 0},
                                        {0, 0, 0, 1, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0}};
                                        Shape = Temp;
                                        Rotateable = true;
                                        Tint = Color.green;
                                        break;}
                case 4:{int[][] Temp = {{0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 1, 0, 0, 0},
                                        {0, 0, 0, 2, 0, 0, 0},
                                        {0, 0, 0, 1, 0, 0, 0},
                                        {0, 0, 0, 1, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0}};
                                        Shape = Temp;
                                        Rotateable = true;
                                        Tint = Color.yellow;
                                        break;}
                case 5:{int[][] Temp = {{0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 1, 1, 0, 0, 0},
                                        {0, 0, 1, 2, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0}};
                                        Shape = Temp;
                                        Rotateable = false;
                                        Tint = Color.white;
                                        break;}
                case 6:{int[][] Temp = {{0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 1, 1, 0, 0, 0},
                                        {0, 0, 0, 2, 1, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0}};
                                        Shape = Temp;
                                        Rotateable = true;
                                        Tint = Color.magenta;
                                        break;}
                case 7:{int[][] Temp = {{0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 1, 1, 0, 0},
                                        {0, 0, 1, 2, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0},
                                        {0, 0, 0, 0, 0, 0, 0}};
                                        Shape = Temp;
                                        Rotateable = true;
                                        Tint = Color.cyan;
                                        break;}
            }
            for(int i = 0; i < ShapeSize; i++){
                for(int j = 0; j < ShapeSize; j++){
                    if(Shape[i][j] == 2){
                        INTX = i;
                        INTY = j;
                    }
                }
            }
        }
        public void print(Color Tint){
            
            for(int i = 0; i < ShapeSize; i++){
                for(int j = 0; j < ShapeSize; j++){
                    if(Shape[i][j] == 1 || Shape[i][j] == 2){
                        printSquare(i - INTX + POSX, j - INTY + POSY, Tint);
                    }
                }
            }
            
        }
        public void move(int MoveX, int MoveY, boolean Manuel){
            boolean BOOLA = false;
            print(Color.black);
            POSY = POSY + MoveY;
            POSX = POSX + MoveX;
            if(Manuel == true){
                moveSound();
            }
            if(checkOverlap() == true){
                POSY = POSY - MoveY;
                POSX = POSX - MoveX;
            }
            
            print(Tint);
        }
        public boolean checkCollision(){
            for(int i = 0; i < ShapeSize; i++){
                for(int j = 0; j < ShapeSize; j++){
                    if(Shape[i][j] == 1 || Shape[i][j] == 2){
                        try{
                            if((Shape[i][j + 1] != 1) && (Map[i - INTX + POSX][j - INTY + POSY + 1].Filled == true)){
                                return true;
                            }
                        }catch(ArrayIndexOutOfBoundsException e){return true;
                        }//catch(NullPointerException f){}
                    }
                }
            }
            return false;
        }
        
    }
    Block MovingBlock;
    Block NextBlock;
    int Speed = 0;
    boolean Left;
    boolean Right;
    boolean Down;
    public void initMap(){
        for(int i = 0; i < Map.length; i++){
            for(int j = 0; j < Map[i].length; j++){
                Map[i][j] = new Position();
            }
        }
    }
    public boolean checkLine(int Line){
        for(int j = 0; j < Width; j++){
            if(Map[j][Line].Filled == false){
                return false;
            }
        }
        return true;
    }
    public void clearLine(int Line){
        for(int i = 0; i < Width; i++){
            Map[i][Line] = new Position();
        }
        
        LinesCleared = LinesCleared + 1;
        
    }
    public void checkLines(){
        for(int i = 0; i < Height; i++){
            
            
            if(checkLine(i) == true){
                clearLine(i);
                moveDown(i);
            }
        }
    }
    public void moveLine(int Line){
        for(int i = 0; i < Width; i++){
            try{
                Map[i][Line].setPosition(Map[i][Line - 1].Tint, Map[i][Line - 1].Filled, Map[i][Line - 1].Final);
                Map[i][Line - 1] = new Position();
                printLine(Line - 1);
                printLine(Line);
            }catch(ArrayIndexOutOfBoundsException e){}
        }
    }
    public void printLine(int Line){
        for(int i = 0; i < Width; i++){
            printSquare(i, Line, Map[i][Line].Tint);
        }
    }
    public void moveDown(int Level){
        for(int j = Level; j >= 0; j--){
            moveLine(j);
        }
    }
    public void drawBorders(){
       
        Area.Painter.setColor(Color.white);
        Area.Painter.drawRect(0, 0, LeftBorder - 1, Y);
        Area.Painter.drawRect(X - LeftBorder + 1, 0, LeftBorder - 1, Y);
        Area.Painter.drawRect(LeftBorder, Height * BlockWidth, X - 2 * LeftBorder, Y - Height * BlockWidth);
    }
    public void initSounds(){
        GUI.playSound(false, "MOVPIECE.wav", 0, 2);
    }

    public Tetris(){
        
        //BASIC INITIALIZATION
        super("Window");
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
                
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new Background();
        initMap();
        drawBorders();
        initSounds();
        Music.play('s');
        contentArea.add(Area);
        MoveTimer.start();
        Stopwatch.start();
        this.addKeyListener(this);

        
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }

    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){
        try{
                switch(e.getKeyCode()){
                    case 32: if(MovingBlock.Rotateable)MovingBlock.rotate(1, true); ; break;
                    case 39: Right = true;/* MovingBlock.move(1, 0);*/ break; 
                    case 37: Left = true;/* MovingBlock.move(-1, 0);*/ break;
                    case 40: Down = true;/* MovingBlock.move(0, 1);*/ break;
                    
                }
        }catch(NullPointerException u){}
    }
    public void keyReleased(KeyEvent e){
        try{
                switch(e.getKeyCode()){

                    case 39:  Right = false; break;
                    case 37:  Left = false; break;
                    case 40:  Down = false; break;
                    
                }
        }catch(NullPointerException u){}
        
    }
 
    
    public void keyTyped(KeyEvent e){

        
    }

   
   
    public void actionPerformed(ActionEvent e){
        /*
        try{
            if(loop.sequencer.isRunning() == false){
                loop.sequencer.start();
            }
        }catch(Exception f){}
         */
        SoundCounter = Math.max(SoundCounter - 1, 0);
        if(NextBlock == null){
            NextBlock = new Block(GUI.randomInt(1, 7));
        }
        if(MovingBlock == null){
            NextBlock.print(Color.black);
            MovingBlock = NextBlock;
            MovingBlock.POSX = Map.length / 2;
            MovingBlock.POSY = 0;
            NextBlock = new Block(GUI.randomInt(1, 7));
            NextBlock.POSY = 10;
            NextBlock.POSX = 19;
            NextBlock.print(NextBlock.Tint);
        }
        
        
        
        
        Counter = Counter + 1;
        if(Counter % (20 - LinesCleared / 10) == 0){
            if(MovingBlock.checkCollision() == true){
                MovingBlock.imprint();
                MovingBlock = null;
            }else{
                MovingBlock.move(0, 1, false);
            }
            
            Counter = 0;
        }
        
        Area.paintComponent(this.getGraphics());
    }
    
   
    
    public void paint(){
        ((Graphics2D)this.getGraphics()).drawImage(Area.Picture, null, 0, 0);
        
    }
   
    class Background extends JPanel{
        
        BufferedImage Picture = new BufferedImage(X, Y, BufferedImage.TYPE_USHORT_555_RGB);
        Graphics Painter = Picture.getGraphics();
        

        public void paintComponent(Graphics paint){
            try{
                Graphics2D Painter = Picture.createGraphics();
                Graphics2D Paint = (Graphics2D)(paint);
                Area.Painter.setColor(Color.black);
                Area.Painter.fillRect((int)(LeftBorder * 1.3 + BlockWidth * Width), 90, 125, 15);
                Area.Painter.setColor(Color.white);
                Area.Painter.drawString("Lines Cleared: " + LinesCleared, (int)(LeftBorder * 1.3 + BlockWidth * Width), 100);
                Area.Painter.drawString("Next Block:",  (int)(LeftBorder * 1.35 + BlockWidth * Width), 150);
                Paint.drawImage(Picture, null, 0, 0);
            }catch(NullPointerException e){}
        }
    }

}


class Tanks extends JFrame implements KeyListener, ActionListener, MouseListener, MouseMotionListener{
    
    final int TopBorderWidth = 26;
    final int X = 1024 - TopBorderWidth;
    final int Y = 768;
    
    
    int MapX = X * 6;
    int MapY = Y * 6;
    int ShellImpactCounter = 0;
    int SalvageMulti = 15;
    String[] GreekAlphabet = {"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Lambda", "Mu", "Sigma", "Tau", "Omega"};
    public void shellFire(){
        GUI.playSound(true, "Explosion2.wav", 0, 1);
    }
    public void shellImpact(){
        if(ShellImpactCounter > 0){return;}
        GUI.playSound(true, "Explosion1.wav", 0, 1);
        ShellImpactCounter = 5;
    }
    public void tankExplode(){
        GUI.playSound(true, "EXPLODE.wav", 0, 1);
    }
    public void collideSound(){
        GUI.playSound(true, "GirderImpact.wav", 0, 2);  
    }
    public void collectSound(){
        GUI.playSound(true, "OilDrumImpact.wav", 0, 2);  
    }
    class Display{
        int POSY;
        int POSX;
        double EllipseLength;
        Tank DisplayedTank;
        GeneralPath TurretImage;
        GeneralPath BaseImage;
        Shape Highlight;
        
        boolean MaxInfo;
        boolean Display = true;
        
        
        public Display(int tPOSY, int tPOSX, boolean tMaxInfo){ 
            POSY = tPOSY;
            POSX = tPOSX;
            POSY = POSY + 35;
            MaxInfo = tMaxInfo;
        }
        public void update(Tank tDisplayedTank){
            DisplayedTank = tDisplayedTank;
            TurretImage = (GeneralPath)(DisplayedTank.TurretImage.clone());
            BaseImage = (GeneralPath)(DisplayedTank.BaseImage.clone());
            EllipseLength = DisplayedTank.Length * 3;
            //Tank Image
            AffineTransform MoveImage = new AffineTransform();
            double Xmove = -DisplayedTank.POSX + POSX + 140;
            double Ymove = -DisplayedTank.POSY + POSY + 16;
            MoveImage.setToTranslation(Xmove, Ymove);
            TurretImage.transform(MoveImage);
            BaseImage.transform(MoveImage);
            if(MaxInfo == false){
            //Highlights
  
            Highlight = new Ellipse2D.Double();
            ((Ellipse2D.Double)Highlight).setFrame(DisplayedTank.POSX - CenterX  - EllipseLength / 2, DisplayedTank.POSY - CenterY - EllipseLength / 2, EllipseLength, EllipseLength);
            
            
            }
            if(DisplayedTank.DeathTimer > 0 || (!Friends.contains(DisplayedTank) && !Enemies.contains(DisplayedTank))){
                Display = false;
                if(MaxInfo == false){
                    switchTarget(Enemies);
                }else{
                    switchTarget(Friends);
                }
            }else{
                Display = true;
            }
            
        }
        public Shape getPointer(){
            if(DisplayedTank == Player){
                return new GeneralPath();
            }
            if(GUI.difference(CenterX + X / 2, DisplayedTank.POSX) < X / 2 + EllipseLength / 2 && GUI.difference(CenterY + Y / 2, DisplayedTank.POSY) < Y / 2 + EllipseLength / 2){
                return new GeneralPath();
            }
            double YDiff = DisplayedTank.POSY - Player.POSY;
            double XDiff = DisplayedTank.POSX - Player.POSX;
            double XPos = X / 2;
            double YPos = Y / 2;
            
            
                //Intersects top/bottom
                YPos = (YDiff > 0 ? Y / 2 : - Y / 2);
                XPos = (XDiff / YDiff) * (YPos);
            
            if(Math.abs(XPos) > X / 2){
                //Intersects sides    
                XPos = (XDiff > 0 ? X / 2 : - X / 2);
                YPos = (YDiff / XDiff) * (XPos);
            }
            Ellipse2D.Double Pointer = new Ellipse2D.Double(XPos - EllipseLength / 2 + X / 2, YPos - EllipseLength / 2 + Y / 2, EllipseLength, EllipseLength);
            return Pointer;
        }
        public void drawCross(Graphics2D Painter, int INTX, int INTY){
            Painter.setColor(Color.white);
            Painter.fillRect(INTX + 4 + 25, INTY , 2, 10);
            Painter.fillRect(INTX + 25, INTY + 4, 10, 2);
            
        }
        public void printShot(int XDis, int YDis, Graphics2D Painter, Shot Type){
            
            if(Type == GrapeShot){
                Painter.drawOval(XDis + 10, 12 + YDis, 6, 6);
                Painter.drawRect(XDis + 5, 20 + YDis, 3, 3);
                Painter.drawOval(XDis + 16, 4 + YDis, 5, 5);
                Painter.drawRect(XDis + 21, 11 + YDis, 6, 6);
                Painter.drawOval(XDis + 20, 23 + YDis, 4, 4);
                Painter.drawRect(XDis + 7, 2 + YDis, 5, 5);
                if(DisplayedTank.MoreFragmentBonus > 0){
                    drawCross(Painter, XDis, YDis);
                    Area.printLine(XDis + 40, YDis + 11, DisplayedTank.MoreFragmentBonus + "", 8, Color.white);
                }
                 
            }else if(Type == VelocityShot){
                GeneralPath RangeShellImage = new GeneralPath();
                RangeShellImage.moveTo(XDis + 10, 30 + YDis);
                RangeShellImage.lineTo(XDis + 20, 30 + YDis);
                RangeShellImage.lineTo(XDis + 20, 20 + YDis);
                RangeShellImage.lineTo(XDis + 18, 15 + YDis);
                RangeShellImage.lineTo(XDis + 18, 8 + YDis);
                RangeShellImage.lineTo(XDis + 15, 0 + YDis);
                RangeShellImage.lineTo(XDis + 12, 8 + YDis);
                RangeShellImage.lineTo(XDis + 12, 15 + YDis);
                RangeShellImage.lineTo(XDis + 10, 20 + YDis);
                RangeShellImage.closePath();
                Painter.draw(RangeShellImage);
                if(DisplayedTank.HigherDamageBonus > 0){
                    drawCross(Painter, XDis, YDis);
                    Area.printLine(XDis + 40, YDis + 11, DisplayedTank.HigherDamageBonus + "", 8, Color.white);
                }
    
            }else if(Type == NormalShot){
                GeneralPath NormalShellImage = new GeneralPath();
                NormalShellImage.moveTo(XDis + 10, 28 + YDis);
                NormalShellImage.lineTo(XDis + 20, 28 + YDis);
                NormalShellImage.lineTo(XDis + 20, 12 + YDis);
                NormalShellImage.lineTo(XDis + 15, 2 + YDis);
                NormalShellImage.lineTo(XDis + 10, 12 + YDis);        
                NormalShellImage.closePath();
                Painter.draw(NormalShellImage);
                if(DisplayedTank.FasterReloadBonus > 0){
                    drawCross(Painter, XDis, YDis);
                    Area.printLine(XDis + 40, YDis + 11, DisplayedTank.FasterReloadBonus + "", 8, Color.white);
                }
            
            } 
        }
        public void print(Graphics2D Painter){
            if(!Display){
                return;
            }
            double PercentHP = (float)DisplayedTank.Hitpoints / (float)DisplayedTank.MaxHitpoints;
            int Green = (int)Math.max(0, Math.min(255, 2 * 255 * PercentHP));
            int Red = (int)Math.max(0, Math.min(255, 510 + (-2 * 255 * PercentHP)));
            Color HPColor = new Color(Red, Green, 55);
            if(MaxInfo == true){
                //Make HP Bar
                Painter.setColor(HPColor);
                Painter.drawRect(POSX + 11, POSY + 21, (int)(98 * (double)(PercentHP)), 8);
                Painter.setColor(Color.white);
                Painter.drawRect(POSX + 10, POSY + 20, 100, 10);
                //Draw ReactiveArmor
                if(DisplayedTank.ReactiveArmorBonus > 0){
                    Painter.drawRect(POSX + 12, POSY + 22, (int)(96 * Math.min(1, (double)(DisplayedTank.ReactiveArmorBonus) / (SalvageMulti + 5))), 6);
                    drawCross(Painter, POSX + 70, POSY + 26);
                    Area.printLine(POSX + 110, POSY + 45, "" + DisplayedTank.ReactiveArmorBonus, 8, Color.white);
                }
                //Make Reload bar
                Painter.drawRect(POSX + 50, POSY, 60, 10);
                Painter.drawRect(POSX + 51, POSY + 1, (int)(58 * ((double)(DisplayedTank.MaxReloadTimer - DisplayedTank.ReloadTimer) / DisplayedTank.MaxReloadTimer)), 8);
                //Make Name
                Painter.drawString(DisplayedTank.Name, POSX + 10, POSY + 10);
                printShot(POSX + 120, POSY, Painter, DisplayedTank.ShotType);
                
            }else{
                //Make HP Bar
                Painter.setColor(HPColor);
                Painter.drawRect(POSX + 11, POSY + 21, (int)(98 * (double)(PercentHP)), 8);
                Painter.setColor(Color.white);
                Painter.drawRect(POSX + 10, POSY + 20, 100, 10);
                //Make Name
                Painter.drawString(DisplayedTank.Name, POSX + 10, POSY + 10);
                //Make Image
                Painter.setColor(HPColor);
                try{
                    Painter.draw(TurretImage);
                    Painter.draw(BaseImage);
                }catch(NullPointerException e){}
                //Make Highlight
                Painter.draw(getPointer());
                Painter.draw(Highlight);
                //Print Distance
                String Distance = "" + (int)Math.sqrt(Math.pow(DisplayedTank.POSY - Player.POSY, 2) + Math.pow(DisplayedTank.POSX - Player.POSX, 2));
                Area.printLine(POSX + 140, POSY + 50, Distance, 10, Color.white);
            }
            
        }
    }
    
    class Projectile{
        double POSY;
        double POSX;
        GeneralPath Image;
        Point2D.Double[] Corner = new Point2D.Double[4];
        double XMomentum;
        double YMomentum;
        double Angle;
        double Length = 1;
        double Width = 1;
        int Duration;
        int MaxDuration;
        double Damage;
        boolean Hit = false;
        
        Color Tint = Color.white;
        Tank Launcher;
        
        
        public void remove(){
            Shots.remove(this);
            this.print(Color.black);
            Image = null;
            Corner = null;
            
        }
        public double getSpeed(){
            return Math.sqrt(XMomentum * XMomentum + YMomentum * YMomentum);
        }
        
        public Projectile(double tPOSY, double tPOSX, double tXMomentum, double tYMomentum, int tDuration, double tDamage, Tank tLauncher){
            POSY = tPOSY;
            POSX = tPOSX;
            Launcher = tLauncher;
            
            Duration = tDuration;
            MaxDuration = Duration;
            XMomentum = tXMomentum;
            YMomentum = tYMomentum;
            Angle = Math.toDegrees(Math.atan2(YMomentum, XMomentum));
            
            Damage = tDamage;
            Length = Length * Damage;
            Image = new GeneralPath();
            for(int i = 0; i < Corner.length; i++){
                Corner[i] = new Point2D.Double();
            }
            Corner[0].setLocation(POSX + Length * Math.cos(Math.toRadians(Angle)), POSY + Length * Math.sin(Math.toRadians(Angle)));
            Corner[1].setLocation(POSX + Width * Math.cos(Math.toRadians(Angle + 90)), POSY + Width * Math.sin(Math.toRadians(Angle + 90)));
            Corner[2].setLocation(POSX + Length * Math.cos(Math.toRadians(Angle + 180)), POSY + Length * Math.sin(Math.toRadians(Angle + 180)));
            Corner[3].setLocation(POSX + Width * Math.cos(Math.toRadians(Angle + 270)), POSY + Width * Math.sin(Math.toRadians(Angle + 270)));
            makeImage();
        }
        public Projectile(Tank tLauncher, Shot Type){
            Launcher = tLauncher;
            XMomentum = (Launcher.ShotSpeed + Math.random() * Type.SpreadSpeed * 2 - Type.SpreadSpeed) * Type.SpeedMulti * Math.cos(Math.toRadians(Launcher.TurretAngle + Math.random() * Type.SpreadAngle * 2 - Type.SpreadAngle));
            YMomentum = (Launcher.ShotSpeed + Math.random() * Type.SpreadSpeed * 2 - Type.SpreadSpeed) * Type.SpeedMulti * Math.sin(Math.toRadians(Launcher.TurretAngle + Math.random() * Type.SpreadAngle * 2 - Type.SpreadAngle));
            POSY = Launcher.FiringHole.y - getSpeed() * Math.sin(Math.toRadians(Launcher.TurretAngle));
            POSX = Launcher.FiringHole.x - getSpeed() * Math.cos(Math.toRadians(Launcher.TurretAngle));;
            
            
            Duration = (int)(Launcher.ShotDuration * Type.DurationMulti);
            MaxDuration = Duration;
            
            
            Angle = Launcher.TurretAngle;
            
            Damage = Type.Damage * Launcher.Damage;
            Length = Length * Type.LengthMulti;
            Width = Width * Type.WidthMulti;
            Image = new GeneralPath();
            for(int i = 0; i < Corner.length; i++){
                Corner[i] = new Point2D.Double();
            }
            initImage();
            makeImage();
        }
        public void initImage(){
            Corner[0].setLocation(POSX + Length * Math.cos(Math.toRadians(Angle)), POSY + Length * Math.sin(Math.toRadians(Angle)));
            Corner[1].setLocation(POSX + Width * Math.cos(Math.toRadians(Angle + 90)), POSY + Width * Math.sin(Math.toRadians(Angle + 90)));
            Corner[2].setLocation(POSX + Length * Math.cos(Math.toRadians(Angle + 180)), POSY + Length * Math.sin(Math.toRadians(Angle + 180)));
            Corner[3].setLocation(POSX + Width * Math.cos(Math.toRadians(Angle + 270)), POSY + Width * Math.sin(Math.toRadians(Angle + 270)));
        }
        public void move(){
            Duration = Math.max(0, Duration - 1);
            
            if(Duration < 20){
                XMomentum = XMomentum * 1 / 3;
                YMomentum = YMomentum * 1 / 3;
                for(int i = 0; i < Corner.length; i++){
                    Corner[i].x += XMomentum; Corner[i].y += YMomentum;
                }
                makeImage();
            }else{
                TankVector AllUnits = new TankVector();
                AllUnits.addAll(Friends);
                AllUnits.addAll(Enemies);
                int Divisor = (int)(getSpeed() / 8);
                for(int j = 0; j < Divisor; j++){
                    POSY = POSY + YMomentum / Divisor;
                    POSX = POSX + XMomentum / Divisor;
                    for(int i = 0; i < Corner.length; i++){
                        Corner[i].x += XMomentum / Divisor; Corner[i].y += YMomentum / Divisor;
                    }
                    for(int i = 0; i < Obstacles.size(); i++){
                        if(this.checkIntersect(Obstacles.element(i))){
                            Obstacles.element(i).Hitpoints = Obstacles.element(i).Hitpoints - this.Damage;
                            this.Duration = 20;
                            shellImpact();
                        }
                    }
                    for(int i = 0; i < AllUnits.size(); i++){
                        AllUnits.element(i).checkIntersect(this);
                    }
                    makeImage();
                    
                    
                }
                
            }
            if(Duration < 20){
                switch(GUI.randomInt(1, 2)){
                    case 1: Tint = Color.red; break;
                    case 2: Tint = Color.orange; break;  
                }
            }
            
            print(Tint);
            if(Duration == 0){
                remove();
            }
        }
        
        public void makeImage(){

            Image.reset();
            if(Duration < 20){
                //Length = 20;
                Image.moveTo((float)POSX , (float)POSY);
                for(int i = 0; i < ((Width * 2 + Length / 2)) * ((Width * 2 + Length / 2)) ; i++){
                    double Angle = GUI.randomInt(0, 360);
                    double Distance = GUI.randomInt(0, (int)((Width * 3 + Length / 3) * 3));
                    Image.lineTo((float)(Distance * Math.cos(Math.toRadians(Angle)) + POSX), (float)(Distance * Math.sin(Math.toRadians(Angle)) + POSY));
                }
                Image.closePath();
            }else{
                /*
                 */
                
                Image.moveTo((float)(Corner[0].x), (float)(Corner[0].y));
                Image.lineTo((float)(Corner[1].x), (float)(Corner[1].y));
                Image.lineTo((float)(Corner[2].x), (float)(Corner[2].y));
                Image.lineTo((float)(Corner[3].x), (float)(Corner[3].y));
                Image.closePath();
            }
        }
        public void print(Color Tint){
            try{
                if(Math.abs(POSY - (CenterY + Y / 2)) < Y / 2 + Length && Math.abs(POSX - (CenterX + X / 2)) < X / 2 + Length){
                    Area.ScreenPainter.setColor(Tint);
                    Area.ScreenPainter.draw(Center.createTransformedShape(Image));
                }
            }catch(Exception e){}
        }
        public boolean checkIntersect(Obstacle obstacle){
            if(Duration < 20){return false;}
            if(obstacle.DeathTimer > 0){return false;}
            if(obstacle.TopImage.contains(Corner[0]) || obstacle.TopImage.contains(Corner[2])){
                return true;
            }
            return false;
        }
    }
    
    
    class Shot{
        double Damage;
        double SpeedMulti;
        int Quantity = 1;
        double DurationMulti = 1;
        double SpreadAngle;
        double SpreadSpeed;
        double LengthMulti;
        double WidthMulti;
        int ReloadIncrement = 6;
        Shape ShotIcon;
        String Name;
        public Shot(String tName, double tDamage, double tSpeedMulti, double tDurationMulti, int tReloadMulti, double tLengthMulti, double tWidthMulti){
            DurationMulti = tDurationMulti;
            Damage = tDamage;
            SpeedMulti = tSpeedMulti;
            LengthMulti = tLengthMulti;
            WidthMulti = tWidthMulti;
            ReloadIncrement = tReloadMulti;
            Name = tName;
        }
        public void addSpread(int tQuantity, double tSpreadAngle, double tSpreadSpeed){
            Quantity = tQuantity;
            Damage = Damage / Quantity;
            SpreadAngle = tSpreadAngle;
            SpreadSpeed = tSpreadSpeed;
        }
    }
    class Salvage{
        double POSX;
        double POSY;
        
        int FasterReloadBonus;
        int MoreFragmentBonus;
        int HigherDamageBonus;
        int ReactiveArmorBonus;
        
        Shape[] Images = new Shape[8];
        public Salvage(double tPOSX, double tPOSY, int tFasterReloadBonus, int tMoreFragmentBonus, int tHigherDamageBonus, int tReactiveArmorBonus){
            POSY = tPOSY;
            POSX = tPOSX;
            FasterReloadBonus = tFasterReloadBonus;
            MoreFragmentBonus = tMoreFragmentBonus;
            HigherDamageBonus = tHigherDamageBonus;
            ReactiveArmorBonus = tReactiveArmorBonus;
            makeImage();
        }
        public void makeImage(){
            double Distance = GUI.randomInt(0, 15);
            for(int i = 1; i <= Images.length; i++){
                double Angle = GUI.randomInt(0, 360);
                Distance = GUI.randomInt(0, 15);
                int INTX = (int)(Distance * Math.cos(Math.toRadians(Angle)) + POSX);
                int INTY = (int)(Distance * Math.sin(Math.toRadians(Angle)) + POSY);
                int Size = GUI.randomInt(5, 7);
                switch(GUI.randomInt(0, 1)){
                    case 0:
                        Images[i - 1] = new Ellipse2D.Double(INTX - Size, INTY - Size, Size, Size);
                    break;
                    case 1:
                        Images[i - 1] = new Rectangle2D.Double(INTX - Size, INTY - Size, Size, Size);
                    break;
                }
            }
            Images[0] = new Ellipse2D.Double(POSX - 15, POSY - 15, 30, 30);
        }
        public void print(){
            for(int i = 0; i < Images.length; i++){
                if(i > 0){
                    Area.ScreenPainter.setColor(Color.black);
                    Area.ScreenPainter.fill(Center.createTransformedShape(Images[i]));
                }
                Area.ScreenPainter.setColor(Color.white);
                Area.ScreenPainter.draw(Center.createTransformedShape(Images[i]));
            }
           // System.out.println((int)(POSX - CenterX) + "\t" + (int)(POSY - CenterY) + "\t\t" + (X / 2) + "\t" + (Y / 2));
        }
        public void remove(){
            
            Salvages.remove(this);
        }
    }
     class Obstacle{
        double Hitpoints;
        double POSY;
        double POSX;
        LineVector Lines = new LineVector();
        LineVector TopLines = new LineVector();
        Shape Image;
        Shape TopImage;
        int DeathTimer = -1;
        double SizeMulti;
        double Rotation;
        public Obstacle(double tPOSX, double tPOSY, double tHitpoints, char ImageType, double tSizeMulti, double tRotation){
            
            POSY = tPOSY;
            POSX = tPOSX;
            Hitpoints = tHitpoints;
            SizeMulti = tSizeMulti;
            Rotation = tRotation;
            
            switch(ImageType){
                case 's':
                    Image = new Rectangle2D.Double(POSX - SizeMulti / 2, POSY - SizeMulti / 2, SizeMulti, SizeMulti);  
                    TopImage = new Rectangle2D.Double(POSX - SizeMulti * 2 / 8, POSY - SizeMulti * 2 / 8, SizeMulti * 2 / 4, SizeMulti * 2 / 4);
                    break;
                case 'r':
                    Image = new GeneralPath();
                    ((GeneralPath)Image).moveTo((float)POSX + 10, (float)POSY + 10);
                    ((GeneralPath)Image).lineTo((float)POSX - 50, (float)POSY + 10);
                    ((GeneralPath)Image).lineTo((float)POSX - 50, (float)POSY - 50);
                    ((GeneralPath)Image).lineTo((float)POSX - 30, (float)POSY - 50);
                    ((GeneralPath)Image).lineTo((float)POSX - 30, (float)POSY + 50);
                    ((GeneralPath)Image).lineTo((float)POSX + 10, (float)POSY + 50);
                    ((GeneralPath)Image).closePath();
                    TopImage = new GeneralPath();
                    ((GeneralPath)TopImage).moveTo((float)POSX, (float)POSY);
                    //Image = new Rectangle2D.Double(POSX - SizeMulti / 2, POSY - SizeMulti / 2, SizeMulti, SizeMulti);  
                    //TopImage = new Rectangle2D.Double(POSX - SizeMulti * 2 / 8, POSY - SizeMulti * 2 / 8, SizeMulti * 2 / 4, SizeMulti * 2 / 4);
                    break;
            }
            Image = setupImage(Image, Lines);
            TopImage = setupImage(TopImage, TopLines);
        }
        public Shape setupImage(Shape SHAPE, LineVector LINES){
            AffineTransform Transform = new AffineTransform();
            Transform.setToRotation(Math.toRadians(Rotation), POSX, POSY);
            SHAPE = Transform.createTransformedShape(SHAPE);
            PathIterator Path = SHAPE.getPathIterator(null);
            
            Vector Point = new Vector();
            while(true){
                float[] Points = new float[6];
                
                
                Path.currentSegment(Points);
                Path.next();
                if(Points[0] != 0 || Points[1] != 0){
                    Point.add(new Point2D.Double(Points[0], Points[1]));
                }
                if(Path.isDone()){ 
                    break;
                }
            }
            for(int i = 0; i < Point.size(); i++){
                Point2D.Double PointA = (Point2D.Double)(Point.get(i));
                Point2D.Double PointB = (Point2D.Double)(Point.get((i - 1 < 0 ? Point.size() - 1 : i - 1)));
                LINES.add(new Line2D.Double(PointA.x, PointA.y, PointB.x, PointB.y));
            }   
            return SHAPE;
        }
        public void explode(){
            DeathTimer = 50;
            tankExplode();
        }
        public void print(){
            if(Hitpoints <= 0 && DeathTimer == -1){
                explode();
            }
            if(DeathTimer > 0){
                DeathTimer--;
                Lines.clear();
                Image = new GeneralPath();
                GeneralPath Path = (GeneralPath)(Image);
                Path.reset();
                Path.moveTo((float)POSX, (float)POSY);
                for(int i = 0; i < 30; i++){
                    int Angle = GUI.randomInt(0, 360);
                    int Distance = GUI.randomInt(0, (int)SizeMulti / 2);
                    
                    Path.lineTo((float)(POSX + Distance * Math.cos(Math.toRadians(Angle))), (float)(POSY + Distance * Math.sin(Math.toRadians(Angle))));
                }
                Path.closePath();
                Area.ScreenPainter.setColor((GUI.randomInt(0, 1) == 0 ? Color.red : Color.yellow));
                Area.ScreenPainter.draw(Center.createTransformedShape(Image));
            }else if(DeathTimer == 0){
                Obstacles.remove(this);
            }else if(DeathTimer < 0){
                Area.ScreenPainter.setColor(Level[CurrentLevel].Background);
                Area.ScreenPainter.fill(Center.createTransformedShape(Image));
                Area.ScreenPainter.setColor(Color.white);
               // Area.ScreenPainter.draw(Center.createTransformedShape(Image));
                for(int i = 0; i < Lines.size(); i++){
                    Area.ScreenPainter.drawLine((int)Lines.element(i).x1 - CenterX, (int)Lines.element(i).y1 - CenterY, (int)Lines.element(i).x2 - CenterX, (int)Lines.element(i).y2 - CenterY);
                }
                
            }
        }
        public void printTop(){
            if(DeathTimer > 0){
                DeathTimer--;
                TopLines.clear();
                TopImage = new GeneralPath();
                GeneralPath Path = (GeneralPath)(Image);
                Path.reset();
                Path.moveTo((float)POSX, (float)POSY);
                for(int i = 0; i < 30; i++){
                    int Angle = GUI.randomInt(0, 360);
                    int Distance = GUI.randomInt(0, (int)SizeMulti / 2);
                    Path.lineTo((float)(POSX + Distance * Math.cos(Math.toRadians(Angle))), (float)(POSY + Distance * Math.sin(Math.toRadians(Angle))));
                }
                Path.closePath();
                Area.ScreenPainter.setColor((GUI.randomInt(0, 1) == 0 ? Color.red : Color.yellow));
                Area.ScreenPainter.draw(Center.createTransformedShape(TopImage));
            
            }else if(DeathTimer < 0){
                Area.ScreenPainter.setColor(Level[CurrentLevel].Background);
                Area.ScreenPainter.fill(Center.createTransformedShape(TopImage));
                Area.ScreenPainter.setColor(Color.white);
                for(int i = 0; i < TopLines.size(); i++){
                    Area.ScreenPainter.drawLine((int)TopLines.element(i).x1 - CenterX, (int)TopLines.element(i).y1 - CenterY, (int)TopLines.element(i).x2 - CenterX, (int)TopLines.element(i).y2 - CenterY);
                }
                
            }
            
        }
        
    }
    class Tank{
        double POSY;
        double POSX;
        GeneralPath BaseImage;
        GeneralPath TurretImage;
        
        Point2D.Double[] Corner = new Point2D.Double[4];
        Point2D.Double[] TCorner = new Point2D.Double[4];
        Point2D.Double[] BCorner = new Point2D.Double[4];
        Line2D.Double[] Line = new Line2D.Double[4];
        Point2D.Double FiringHole = new Point2D.Double();
        int ReloadTimer;
        int MaxReloadTimer = 480;
        int ReloadIncrement;
        double Speed;
        double Angle;
        double TurretAngle;
        double MaxSpeed;
        double Length = 15;
        double TurretWidth = Length / 2 / Math.sqrt(2);
        double ShotSpeed = 12.5;
        
        String Name;
        int ShotDuration = 125;
        int DeathTimer = -1;
        int Damage = 1;
        double MaxHitpoints = 10;
        double Hitpoints = MaxHitpoints;
        Color Tint = Color.white;
        boolean Alive = true;
        char FlagType;
        Shot ShotType;
        int FasterReloadBonus;
        int MoreFragmentBonus;
        int HigherDamageBonus;
        int ReactiveArmorBonus;
        AffineTransform RotateBase = new AffineTransform();
        AffineTransform RotateTurret = new AffineTransform();
        
        boolean IsPlayer;
        boolean LEFT;
        boolean RIGHT;
        boolean FORWARD;
        boolean BACK; 
        boolean TLEFT;
        boolean TRIGHT;
        
        double Wobble;
        int ReverseCount = 0;
        char ReverseDirection = (GUI.randomInt(0, 1) == 0 ? 'l' : 'r');
        int Orientation = (GUI.randomInt(0, 1) == 1 ? 1 : -1);
        TankVector Targets;
        Tank Target;
        double IdealDistance = 275;
        int AILevel;

        int FiringDelay = 0;
        Order Order = new Order(this);
        TankVector Allies = null;
        boolean TargetHasReactiveArmor = false;
            
        class Order{
            Tank Target;
            Tank Owner;
            char Command;
            int Value;
            double POSX;
            public Order(Tank tOwner){
                Owner = tOwner;
            }
            public void setToAttack(Tank TargetTank){
                if(TargetTank == Target && Command == 'a'){
                    Value = Value + 1000;
                }else{
                    Target = TargetTank;
                    Value = 1000;
                    Command = 'a';
                }
            }
            public void setToFollow(Tank TargetTank){
                if(TargetTank == Target && Command == 'f'){
                    Value = -Value;
                }else{
                    Target = TargetTank;
                    Value = GUI.randomInt(1, Allies.size());
                    Command = 'f';
                }
            }
            public void setToHold(){
                if(Command == 'h'){
                    Value = -Value;
                }else{
                    Target = new Tank(Owner.POSX, Owner.POSY);
                    Value = 1;
                    Command = 'h';
                }
                System.out.println(Owner);
            }
        }
        public void explode(){
            this.DeathTimer = 25;
            Speed = 0;
            if(MoreFragmentBonus > 0 || HigherDamageBonus > 0 || ReactiveArmorBonus < 0 || FasterReloadBonus > 0){
                Salvages.add(new Salvage(POSX - 10, POSY - 10, FasterReloadBonus, MoreFragmentBonus, HigherDamageBonus, (ReactiveArmorBonus < 0 ? SalvageMulti + 5 : 0)));
            }
            tankExplode();
        }
        public Tank(double tPOSX, double tPOSY){
            POSX = tPOSX;
            POSY = tPOSY;
        }
        public Tank copy(Tank tank){
            
            Tank NewTank = new Tank(tank.Name, tank.POSX, tank.POSY, tank.Angle, tank.MaxSpeed, (int)tank.MaxHitpoints, tank.AILevel, tank.Targets, tank.FlagType, (int)tank.IdealDistance);
            NewTank.FasterReloadBonus = tank.FasterReloadBonus;
            NewTank.HigherDamageBonus = tank.HigherDamageBonus;
            NewTank.MoreFragmentBonus = tank.MoreFragmentBonus;
            NewTank.ReactiveArmorBonus = tank.ReactiveArmorBonus;
            return NewTank;
        }
        public Tank(String tName, double tPOSX, double tPOSY, double tAngle, double tMaxSpeed, int MaxHp, int tAILevel, TankVector tTargets, char tFlagType, int tIdealDistance){
            Name = tName;
            AILevel = tAILevel;
            POSX = tPOSX;
            POSY = tPOSY;
            Angle = tAngle;
            TurretAngle = Angle;
            MaxSpeed = tMaxSpeed;
            MaxHitpoints = MaxHp;
            Hitpoints = MaxHitpoints;
            BaseImage = new GeneralPath();
            TurretImage = new GeneralPath();
            ShotType = NormalShot;
            Targets = tTargets;
            Target = this;
            FlagType = tFlagType;
            IdealDistance = tIdealDistance;
            for(int i = 0; i < Corner.length; i++){
                Corner[i] = new Point2D.Double();
                TCorner[i] = new Point2D.Double();
                BCorner[i] = new Point2D.Double();
            }
            for(int i = 0; i < Line.length; i++){
                Line[i] = new Line2D.Double();
            }
            if(Targets == Enemies){
                Allies = Friends;
            }else if(Targets == Friends){
                Allies = Enemies;
            }
            FiringHole = new Point2D.Double();
            makeImage();
        }

        public void fire(){
            if(ReloadTimer != 0){return;}
            Projectile[] NewShots = new Projectile[0];
            if(ShotType != null){
                NewShots = shot(ShotType); 
            }else{
                NewShots = shot(NormalShot); 
            }
            if(ShotType == NormalShot){
                FasterReloadBonus = Math.max(0, FasterReloadBonus - 1);
            }else if(ShotType == GrapeShot){
                MoreFragmentBonus = Math.max(0, MoreFragmentBonus - 1);
            }else if(ShotType == VelocityShot){
                HigherDamageBonus = Math.max(0, HigherDamageBonus - 1);
            }
            ReloadTimer = MaxReloadTimer;
            for(int i = 0; i < NewShots.length; i++){
                Shots.add(NewShots[i]);
            }
            shellFire();
            if(!IsPlayer){
                FiringDelay = GUI.randomInt(180, 200);
            }

        }
        public Projectile[] shot(Shot Type){
            
            int Count = Type.Quantity;
            if(MoreFragmentBonus > 0 && Type == GrapeShot){
                Count = Count * 2;
            }
            Projectile[] Shots = new Projectile[Count];
            for(int i = 0; i < Count; i++){
                Shots[i] = new Projectile(this, ShotType);
                if(HigherDamageBonus > 0 && Type == VelocityShot){
                    Shots[i].Damage = Shots[i].Damage * 2;
                    Shots[i].Length = Shots[i].Length + 2;
                    Shots[i].XMomentum = Shots[i].XMomentum * 6 / 5;
                    Shots[i].YMomentum = Shots[i].YMomentum * 6 / 5;
                    Shots[i].initImage();
                }
            }
            ReloadIncrement = Type.ReloadIncrement;
            if(FasterReloadBonus > 0 && Type == NormalShot){
                ReloadIncrement = ReloadIncrement * 2; 
            }
            return Shots;

        }
       
        public void updateTarget(){
            if(!Targets.contains(Target)){
                Target = this;
            }
            for(int i = 0; i < Targets.size(); i++){
                if(getPriority(Targets.element(i)) > getPriority(Target)){
                    if(Targets.element(i) == Target){
                        
                    }else{
                        TargetHasReactiveArmor = false;
                    }
                    Target = Targets.element(i);
                }
            }
            
        }
        public double getPriority(Tank Enemy){
            if(Enemy == this){
                return Double.NEGATIVE_INFINITY;
            }else{
                double Distance = - Math.sqrt(Math.pow(this.POSX - Enemy.POSX, 2) + Math.pow(this.POSY - Enemy.POSY, 2));
                double OrderPriority = (Enemy == Order.Target && (Order.Command == 'a') ? Order.Value : 0);
                double Inertia = (Enemy == Target ? 100 : 0);
                return Distance + OrderPriority + Inertia;
            }
        }
        public void doAI(){
            if(AILevel < 0){
                return;
            }
            updateTarget();
            boolean Stop = false;
            boolean Reverse = false;
            double YDistance;
            double XDistance;
            
            YDistance = Target.POSY - this.POSY;
            XDistance = Target.POSX - this.POSX;
            
            double DistanceToTarget = Math.sqrt(XDistance * XDistance + YDistance * YDistance);
            ShotType = selectShot(DistanceToTarget);
            double ShotSpeedMulti = (ShotType == null ? 1 : ShotType.SpeedMulti);
            int AngleMod = 0;
            if(ReloadTimer == 0 && FiringDelay < 15){
                switch(AILevel){
                    case 0:
                        //Very bad accuracy when Stationary
                        AngleMod = GUI.randomInt(-10, 10);
                        YDistance = YDistance + GUI.randomInt(-80, 80);
                        XDistance = XDistance + GUI.randomInt(-80, 80);
                        YDistance = YDistance + GUI.randomInt(-5, 5) * Target.Speed;
                        XDistance = XDistance + GUI.randomInt(-5, 5) * Target.Speed;
                    break;
                    case 1:
                        //Bad accuracy when moving
                        AngleMod = GUI.randomInt(-5, 5);
                        YDistance = YDistance + GUI.randomInt(-10, 10) * Target.Speed;
                        XDistance = XDistance + GUI.randomInt(-10, 10) * Target.Speed;
                    break;
                    case 2:
                        //Partially compensates for targets motion
                        AngleMod = GUI.randomInt(-5, 5);
                        YDistance = YDistance + Target.Speed * Math.sin(Math.toRadians(Target.Angle)) * DistanceToTarget / (ShotSpeed * ShotSpeedMulti) / 3;
                        XDistance = XDistance + Target.Speed * Math.cos(Math.toRadians(Target.Angle)) * DistanceToTarget / (ShotSpeed * ShotSpeedMulti) / 3;
                        YDistance = YDistance + GUI.randomInt(-10, 10) * Target.Speed;
                        XDistance = XDistance + GUI.randomInt(-10, 10) * Target.Speed;
                    break;
                    case 3:
                        //Fully compensates for targets motion
                        AngleMod = GUI.randomInt(-5, 5);
                        YDistance = YDistance + Target.Speed * Math.sin(Math.toRadians(Target.Angle)) * DistanceToTarget / (ShotSpeed * ShotSpeedMulti) * 2 / 3;
                        XDistance = XDistance + Target.Speed * Math.cos(Math.toRadians(Target.Angle)) * DistanceToTarget / (ShotSpeed * ShotSpeedMulti) * 2 / 3;
                        YDistance = YDistance + GUI.randomInt(-10, 10) * Target.Speed;
                        XDistance = XDistance + GUI.randomInt(-10, 10) * Target.Speed;
                    break;
                } 
            }
            DistanceToTarget = Math.sqrt(XDistance * XDistance + YDistance * YDistance);
           
            double TargetAngle = Math.toDegrees(Math.atan2(YDistance, XDistance));
            TargetAngle = TargetAngle + AngleMod;
            if(TargetAngle < 0){TargetAngle = 360 + TargetAngle;}else if(TargetAngle > 360){TargetAngle = TargetAngle - 360;}
            if(ReloadTimer == 0){
                FiringDelay = Math.max(0, FiringDelay - ReloadIncrement);
            }
            if(Math.abs(GUI.angleDiff(TargetAngle, TurretAngle)) > 1.25 && ReloadTimer == 0){
                if(GUI.angleDiff(TargetAngle, TurretAngle) > 0){
                    TRIGHT = false;
                    TLEFT = true;
                }else{
                    TRIGHT = true;
                    TLEFT = false;
                }
                
            }else{
                
                if(FiringDelay == 0){
                    if(checkCrossFire(Allies) == false){
                        
                        if(ShotType != null){
                            //ShotType = SHOTA;
                            this.fire();    
                        }
                    }
                }
                    
                
                TRIGHT = false;
                TLEFT = false;
            }
            
            
            
            
            
            if(Order.Command == 'f'){
                double Angle = ((double)Order.Value / Math.max(3, Allies.size())) * 360 - Order.Target.Angle - 90;
                
                double DisplacementX = IdealDistance * Math.cos(Math.toRadians(Angle));
                double DisplacementY = IdealDistance * Math.sin(Math.toRadians(Angle));
                YDistance = Order.Target.POSY - this.POSY + DisplacementX;
                XDistance = Order.Target.POSX - this.POSX + DisplacementY;
            }else if(Order.Command == 'h'){
                YDistance = Order.Target.POSY - this.POSY;
                XDistance = Order.Target.POSX - this.POSX;
            }else{
                YDistance = Target.POSY - this.POSY;
                XDistance = Target.POSX - this.POSX;
            }
            
            DistanceToTarget = Math.sqrt(XDistance * XDistance + YDistance * YDistance);
            TargetAngle = Math.toDegrees(Math.atan2(YDistance, XDistance));
            double TempIdealDistance = IdealDistance;
            
            if(Order.Command == 'f' || Order.Command == 'h'){
                TempIdealDistance = (Order.Value < 0 ? -1 : IdealDistance / 2);
            }
            if(Order.Command == 'f' || Order.Command == 'h'){
                
                if(DistanceToTarget < 50 && TempIdealDistance < 0){
                    Stop = true;
                }
            }
            switch(AILevel){
                case 0:
                if(DistanceToTarget < TempIdealDistance * 0.75){
                    TargetAngle = TargetAngle - 180;
                }else if(DistanceToTarget > TempIdealDistance * 1.25){
                    TargetAngle = TargetAngle;
                }else{
                    Stop = true;
                }
                break; 
                case 1:
                if(DistanceToTarget > TempIdealDistance * 2){
                    TargetAngle = TargetAngle;
                }else if(DistanceToTarget > TempIdealDistance){
                    TargetAngle = TargetAngle + Orientation * (90 - Math.max((DistanceToTarget - TempIdealDistance) / 2, 90));
                }else if(DistanceToTarget < TempIdealDistance){
                    TargetAngle = TargetAngle + Orientation * (90 + Math.max((TempIdealDistance - DistanceToTarget), 90));
                }   
                if(DistanceToTarget < TempIdealDistance + 50 && DistanceToTarget > TempIdealDistance - 50){
                    Stop = true;
                }
                break;
                default:
                if(DistanceToTarget > TempIdealDistance * 2){
                    TargetAngle = TargetAngle;
                }else if(DistanceToTarget > TempIdealDistance){
                    TargetAngle = TargetAngle + Orientation * (90 - Math.max((DistanceToTarget - TempIdealDistance) / 2, 90));
                }else if(DistanceToTarget < TempIdealDistance){
                    TargetAngle = TargetAngle + Orientation * (90 + Math.max((TempIdealDistance - DistanceToTarget), 90));
                }
                break;
            }
            if(Order.Command == 'h' || Order.Command == 'f' && Order.Value < 0){
                TargetAngle = Math.toDegrees(Math.atan2(YDistance, XDistance));
                if(DistanceToTarget < 100 && Math.abs(GUI.angleDiff(TargetAngle, Angle)) < 5){
                    Stop = true;
                }
                if(DistanceToTarget < 100 && Math.abs(GUI.angleDiff(TargetAngle, Angle)) > 175){
                    Stop = true;
                    Reverse = true;
                }
            }
            if(TargetAngle < 0){TargetAngle = 360 + TargetAngle;}else if(TargetAngle > 360){TargetAngle = TargetAngle - 360;}

            
            if(ReverseCount == 0){
                FORWARD = true;
                BACK = false;
                
                if(Math.abs(GUI.angleDiff(TargetAngle, Angle)) > 0.75){
                    if(GUI.angleDiff(TargetAngle, Angle) > 0){
                        RIGHT = false;
                        LEFT = true;
                    }else{
                        RIGHT = true;
                        LEFT = false;
                    }
                    
                }else{
                    RIGHT = false;
                    LEFT = false;
                }
                
                if(Stop == true){
                    FORWARD = false;
                    BACK = false;
                    RIGHT = false;
                    LEFT = false;
                }
                if(Reverse == true){
                    FORWARD = false;
                    BACK = true;
                }
            }else if(ReverseCount > 0){
                FORWARD = false;
                BACK = true;
                if(ReverseCount > 30 && ReverseCount < 90){
                    if(ReverseDirection == 'r'){
                        RIGHT = true;
                        LEFT = false;
                    }else{
                        RIGHT = false;
                        LEFT = true;
                    }
                }else{
                    RIGHT = false;
                    LEFT = false;
                }
            }else if(ReverseCount < 0){
                FORWARD = true;
                BACK = false;
                if(ReverseCount < -30 && ReverseCount > -90){
                    if(ReverseDirection == 'r'){
                        RIGHT = true;
                        LEFT = false;
                    }else{
                        RIGHT = false;
                        LEFT = true;
                    }
                }else{
                    RIGHT = false;
                    LEFT = false;
                }
            }
           // checkFriendlyFire();
        }
        public Shot selectShot(double Distance){
            int GrapeShotValue = 100;
            int NormalShotValue = 100;
            int VelocityShotValue = 100;
            int Multi;
            if(Allies == Enemies){Multi = 1;
            }else{Multi = 0;}
            
            if(FasterReloadBonus > 2 * SalvageMulti * Multi){
                NormalShotValue = NormalShotValue + 75;
            }
            if(MoreFragmentBonus > SalvageMulti * Multi){
                GrapeShotValue = GrapeShotValue + 75;
            }
            if(HigherDamageBonus > SalvageMulti * Multi){
                VelocityShotValue = VelocityShotValue + 75;
            }
            if(Distance > 500){
                GrapeShotValue = GrapeShotValue - 50;
            }
            if(Distance < 500){
                VelocityShotValue = VelocityShotValue - 50;
            }
            if(Distance > 1000){
                NormalShotValue = NormalShotValue - 50;
            }
            if(TargetHasReactiveArmor){
                NormalShotValue = NormalShotValue - 100;
                VelocityShotValue = VelocityShotValue - 100;
                GrapeShotValue = GrapeShotValue + 100;
            }
            
            if(Distance > NormalShot.SpeedMulti * this.ShotSpeed * this.ShotDuration * NormalShot.DurationMulti){
                NormalShotValue = 0;
            }
            if(Distance > GrapeShot.SpeedMulti * this.ShotSpeed * this.ShotDuration * GrapeShot.DurationMulti){
                GrapeShotValue = 0;
            }
            if(Distance > VelocityShot.SpeedMulti * this.ShotSpeed * this.ShotDuration * VelocityShot.DurationMulti){
                VelocityShotValue = 0;
            }
            int MaxValue = Math.max(Math.max(NormalShotValue, VelocityShotValue), GrapeShotValue);
            if(MaxValue == 0){
                return null;
            }else if(MaxValue == NormalShotValue){
                return NormalShot;
            }else if(MaxValue == GrapeShotValue){
                return GrapeShot;
            }else if(MaxValue == VelocityShotValue){
                return VelocityShot;
            }
            return null;
        }
        public boolean checkCrossFire(TankVector Targets){
            double TargetAngle = Math.toDegrees(Math.atan2(POSY - Target.POSY, POSX - Target.POSX));
            
            double Distance = Math.sqrt(Math.pow(POSY - Target.POSY, 2) + Math.pow(POSX - Target.POSX, 2)); 
            for(int i = 0; i < Targets.size(); i++){
                if(Targets.element(i) == this){
                    continue;
                }
                double AllyDistance = Math.sqrt(Math.pow(POSY - Targets.element(i).POSY, 2) + Math.pow(POSX - Targets.element(i).POSX, 2)); 
                if(AllyDistance < Distance){
                    if(Math.abs(GUI.angleDiff(Math.toDegrees(Math.atan2(POSY - Targets.element(i).POSY, POSX - Targets.element(i).POSX)), TargetAngle)) < Math.toDegrees(Math.atan(Targets.element(i).Length / AllyDistance)) + 1){
                        return true;
                    }
                }
            }   
            return false;
        }
        public boolean checkIntersect(Salvage Target){
            if(this.DeathTimer > 0){return false;}
            if(Math.sqrt(Math.pow(POSY - Target.POSY, 2) + Math.pow(POSX - Target.POSX, 2)) < 15){
                String Message = "";
                boolean Bonus = false;
                if(Target.FasterReloadBonus > 0){
                    Message = Message + "Normal Shot";
                    Bonus = true;
                }
                if(Target.MoreFragmentBonus > 0){
                    Message = Message + "Grape Shot";
                    if(Message.length() > 0){Message = Message + ", ";}
                    Bonus = true;
                }
                if(Target.HigherDamageBonus > 0){
                    Message = Message + "Range Shot";
                    if(Message.length() > 0){Message = Message + ", ";}
                    Bonus = true;
                }
                if(Bonus){
                    Message = Message + " Bonus";
                }
                if(Target.ReactiveArmorBonus > 0){
                    if(Message.length() > 0){Message = Message + ", ";}
                    Message = Message + "Reactive Armor Retrofit";
                }
                if(this == Player){
                    Messages.add(new TempText(X / 2, Y / 2, 's', "Power Up: " + Message, 50, Color.white, 100));
                    collectSound();
                }
                this.MoreFragmentBonus = this.MoreFragmentBonus + Target.MoreFragmentBonus;
                this.FasterReloadBonus = this.FasterReloadBonus + Target.FasterReloadBonus;
                this.HigherDamageBonus = this.HigherDamageBonus + Target.HigherDamageBonus;
                this.ReactiveArmorBonus = this.ReactiveArmorBonus + Target.ReactiveArmorBonus;
                Target.remove();
                return true;
            }
            return false;
        }
        public boolean checkIntersect(Projectile Shot){
            if(this.DeathTimer > 0){return false;}
            if(GUI.difference(this.POSX, Shot.POSX) +  GUI.difference(this.POSY, Shot.POSY) > Length * Length){
                return false;
            }
            
                if(Shot.Duration > 20 && Shot.Launcher != this && (BaseImage.contains(Shot.Corner[0].x, Shot.Corner[0].y) || BaseImage.contains(Shot.Corner[2].x, Shot.Corner[2].y))){
                    Shot.Hit = true; 
                    
                    shellImpact();
                    if(ReactiveArmorBonus > 0){
                        ReactiveArmorBonus = Math.max(0, ReactiveArmorBonus - 1);
                        if(ReactiveArmorBonus == 0){
                            ReactiveArmorBonus = -1;
                        }
                        Shot.Width = Shot.Width + 1;
                        if(this == Shot.Launcher.Target){
                            Shot.Launcher.TargetHasReactiveArmor = true;
                        }
                        //System.out.println("Reactive " + this.ReactiveArmorBonus);
                    }else{
                        
                        if(this == Shot.Launcher.Target){
                            Shot.Launcher.TargetHasReactiveArmor = false;
                        }
                        Hitpoints = Hitpoints - Shot.Damage;
                        slow(Shot.Damage);
                        
                    }
                    Shot.Duration = 20;
                    
                    
                    return true;
                    
                }
            
            return false;
        }
        public Line2D.Double checkIntersect(Tank tank){
            if(this.DeathTimer > 0){return null;}
            if(GUI.difference(this.POSX, tank.POSX) > Length + tank.Length || GUI.difference(this.POSY, tank.POSY) > Length + tank.Length){
                return null;
            }
            
            if(tank == this){return null;}
            for(int i = 0; i < tank.Line.length; i++){
                for(int j = 0; j < this.Line.length; j++){
                    if(tank.Line[i].intersectsLine(this.Line[j])){
                        
                        return this.Line[j];
                        
                    }
                }
            }
            return null;
        }
        public Line2D.Double checkIntersect(Obstacle obstacle){
            if(obstacle.DeathTimer > 0){return null;}
            for(int i = 0; i < obstacle.Lines.size(); i++){
                for(int j = 0; j < this.Line.length; j++){
                    if(obstacle.Lines.element(i).intersectsLine(this.Line[j])){
                        return obstacle.Lines.element(i);
                    }
                }
            }
            return null;
        }
        public void controls(){
            double Increment = 1.5;
            if(LEFT == true){
                Angle = Angle - Increment;
                TurretAngle = TurretAngle - Increment;
            }
            if(RIGHT == true){
                Angle = Angle + Increment;
                TurretAngle = TurretAngle + Increment;
            }
            if(TLEFT == true){
                TurretAngle = TurretAngle - (1 + Increment);
            }
            if(TRIGHT == true){
                TurretAngle = TurretAngle + (1 + Increment);
            }
            if(FORWARD == true){
                Speed = Speed + 0.05;
            }
            if(BACK == true){
                Speed = Speed - 0.05;
            }
            if(Angle > 360){Angle = Angle - 360;}
            if(Angle < 0){Angle = 360 + Angle;}
            if(TurretAngle > 360){TurretAngle = TurretAngle - 360;}
            if(TurretAngle < 0){TurretAngle = 360 + TurretAngle;}
            slow(0.025);
            Speed = Math.min(MaxSpeed, Speed);
            Speed = Math.max(-MaxSpeed, Speed);
            
        }
        
        public void slow(double Amount){
            if(Speed > 0){
                Speed = Math.max(Speed - Amount, 0);
            }else if(Speed < 0){
                Speed = Math.min(Speed + Amount, 0);
            }else{
                Speed = Speed;
            }
        }
        public void remove(){
            Enemies.remove(this);
            Friends.remove(this);
            Targets = null;
            Target = null;
            Corner = null;
            Alive = false;
        }
        public void move(){
            
            if(Hitpoints <= 0 && DeathTimer < 0){this.explode();}
            if(DeathTimer > 0){DeathTimer = DeathTimer - 1;}
            else if(DeathTimer == 0){remove();}
            else if(DeathTimer > 0){
                switch(GUI.randomInt(1, 3)){
                    case 1: Tint = Color.red; break;
                    case 2: Tint = Color.orange; break;
                    case 3: Tint = Color.yellow; break;
                    
                }
            }
            ReloadTimer = Math.max(0, ReloadTimer - ReloadIncrement);
            if(ReverseCount > 0){
                ReverseCount = Math.max(0, ReverseCount - 1);
            }else if(ReverseCount < 0){
                ReverseCount = Math.min(0, ReverseCount + 1);
            }
            
            for(int i = 0; i < Obstacles.size(); i++){
                if(this.checkIntersect(Obstacles.element(i)) != null){
                    collide(this.checkIntersect(Obstacles.element(i)));
                    Obstacles.element(i).Hitpoints = Obstacles.element(i).Hitpoints - Math.abs(Speed);
                    slow(1.5);
                }
            }
            
            if(POSX - X / 4 < 0){collide(new Line2D.Double(0, MapY, 0, 0));}
            else if(POSX + X / 4 > MapX){collide(new Line2D.Double(MapX, MapY, MapX, 0));}
            if(POSY - Y / 4 < 0){collide(new Line2D.Double(MapX, 0, 0, 0));}
            else if(POSY + Y / 4 > MapY){collide(new Line2D.Double(MapX, MapY, 0, MapY));}
            
            controls();
            POSY = POSY + Speed * Math.sin(Math.toRadians(Angle));
            POSX = POSX + Speed * Math.cos(Math.toRadians(Angle));
            RotateBase = AffineTransform.getRotateInstance(Math.toRadians(Angle), POSX, POSY);
            RotateTurret = AffineTransform.getRotateInstance(Math.toRadians(TurretAngle), POSX, POSY);
            makeImage();
            
            printBase(DeathTimer < 0 ? Color.white : GUI.randomInt(0, 1) == 1 ? Color.red : Color.yellow);
            
        }
        public double getLineAngle(Line2D.Double Line){;
            double LineAngle = Math.atan2(Line.y1-Line.y2, Line.x1-Line.x2);
            return LineAngle;
        }
        public void collide(Line2D.Double Line){
            collide(new Tank(POSX, POSY), Line);   
        }
        public void collide(Tank Target, Line2D.Double Line){
            Orientation = -Orientation;
            this.slow(1);
            Target.slow(1);
            
            Speed = -Speed;
            Target.Orientation = -Target.Orientation;
            Target.Speed = -Target.Speed;
            
            if(ReverseCount <= 0){
                ReverseCount = 120;
            }else if(ReverseCount > 0){
                ReverseCount = -120;
            }
            if(Target.ReverseCount <= 0){
                Target.ReverseCount = 120;
            }else if(Target.ReverseCount > 0){
                Target.ReverseCount = -120;
            }
            /*
            if(Target.POSX > this.POSX){
                Target.POSX++;
                this.POSX--;
            }else if(Target.POSX < this.POSX){
                Target.POSX--;
                this.POSX++;
            }
            */
            double CollideAngle = getLineAngle(Line) + Math.PI / 2 * Line.relativeCCW(POSX, POSY);
            POSX = POSX + 1 * Math.cos(CollideAngle);
            POSY = POSY + 1 * Math.sin(CollideAngle);
            if(Target.POSY > this.POSY){
                Target.POSY++;
                this.POSY--;
            }else if(Target.POSY < this.POSY){
                Target.POSY--;
                this.POSY++;
            }
            if(GUI.randomInt(0, 1) == 0){
                Target.ReverseDirection = 'r';
                this.ReverseDirection = 'l';
            }else{
                Target.ReverseDirection = 'l';
                this.ReverseDirection = 'r';
                
            }
            if(Order.Command == 'f' && Target.Order.Command == 'f'){
                if(GUI.randomInt(0, 1) == 0){
                    Order.Value = GUI.sign(Order.Value) * GUI.randomInt(1, Allies.size());
                }else{
                    Target.Order.Value = GUI.sign(Target.Order.Value) * GUI.randomInt(1, Allies.size());
                }
            }else if(Order.Command == 'f'){
                Order.Value = GUI.sign(Order.Value) * GUI.randomInt(1, Allies.size());
            }
            
            this.IdealDistance = this.IdealDistance + GUI.randomInt(-20, 20);
            if(this.IdealDistance > 300){this.IdealDistance = 225 + (this.IdealDistance - 300);
            }else if(this.IdealDistance < 225){this.IdealDistance = 300 - this.IdealDistance + 225;}
            Target.IdealDistance = Target.IdealDistance + GUI.randomInt(-20, 20);
            if(Target.IdealDistance > 300){Target.IdealDistance = 225 + (Target.IdealDistance - 300);
            }else if(Target.IdealDistance < 225){Target.IdealDistance = 300 - Target.IdealDistance + 225;}
            
            collideSound();
        }
        public void makeImage(){
            double NewAngle = 0;
            BaseImage.reset();
            TurretImage.reset();
            
            if(DeathTimer < 0){
                //26 pixels long
                //16 pixels wide
                double DiagLength = Length;
                NewAngle = Math.toRadians(Angle - 30);
                //double INTX = DiagLength * Math.cos(NewAngle);
                //double INTY = DiagLength * Math.sin(NewAngle);
                Corner[0].setLocation(POSX + 13, POSY + 7.5);
                Corner[1].setLocation(POSX - 13, POSY + 7.5);
                Corner[2].setLocation(POSX - 13, POSY - 7.5);
                Corner[3].setLocation(POSX + 13, POSY - 7.5);
                for(int i = 0; i < Corner.length; i++){
                    RotateBase.transform(Corner[i], Corner[i]);
                }
                Line[0].setLine(Corner[0], Corner[1]);
                Line[1].setLine(Corner[1], Corner[2]);
                Line[2].setLine(Corner[2], Corner[3]);
                Line[3].setLine(Corner[3], Corner[0]);
                BaseImage.moveTo((float)(Corner[0].x), (float)(Corner[0].y));
                for(int i = 1; i <= 3; i++){
                    BaseImage.lineTo((float)(Corner[i].x), (float)(Corner[i].y));
                }
                BaseImage.closePath();
                makeTurret();
            }else{
                BaseImage.moveTo((float)POSX, (float)POSY);
                TurretImage.moveTo((float)POSX, (float)POSY);
                for(int i = 0; i < 30; i++){
                    double Angle = Math.random() * 2 * Math.PI;
                    double Distance = GUI.randomInt(0, 25);
                    BaseImage.lineTo((float)(Distance * Math.cos(Angle) + POSX), (float)(Distance * Math.sin(Angle) + POSY));
                }
                for(int i = 0; i < 30; i++){
                    double Angle = Math.random() * 2 * Math.PI;
                    double Distance = GUI.randomInt(0, 25);
                    TurretImage.lineTo((float)(Distance * Math.cos(Angle) + POSX), (float)(Distance * Math.sin(Angle) + POSY));
                }
            }
            
            
        }
        public void makeTurret(){
            double NewAngle = 0;
            
            NewAngle = Math.toRadians(TurretAngle - 45);
            
            TCorner[0].setLocation(POSX + TurretWidth, POSY + TurretWidth);
            TCorner[1].setLocation(POSX - TurretWidth, POSY + TurretWidth);
            TCorner[2].setLocation(POSX - TurretWidth, POSY - TurretWidth);
            TCorner[3].setLocation(POSX + TurretWidth, POSY - TurretWidth);
            
            for(int i = 0; i < TCorner.length; i++){
                RotateTurret.transform(TCorner[i], TCorner[i]);
            }
            
            
            TurretImage.moveTo((float)(TCorner[0].x), (float)(TCorner[0].y));
            
            for(int i = 1; i <= 3; i++){
                TurretImage.lineTo((float)(TCorner[i].x), (float)(TCorner[i].y));
            }
            TurretImage.closePath();
            
            
            double Width = TurretWidth;
            double PercentWidth = 0.25 * Width;
            double PercentLength = 2.5 * Width;
            
            BCorner[0].setLocation(POSX + Width, POSY - PercentWidth);
            BCorner[1].setLocation(POSX + Width, POSY + PercentWidth);
            BCorner[2].setLocation(POSX + PercentLength + Width, POSY + PercentWidth);
            BCorner[3].setLocation(POSX + PercentLength + Width, POSY - PercentWidth);
            
            for(int i = 0; i < BCorner.length; i++){
                RotateTurret.transform(BCorner[i], BCorner[i]);
            }
            FiringHole.setLocation((BCorner[2].x + BCorner[3].x) / 2, (BCorner[2].y + BCorner[3].y) / 2);
            TurretImage.moveTo((float)(BCorner[0].x), (float)(BCorner[0].y));
            
            for(int i = 1; i <= 3; i++){
                TurretImage.lineTo((float)(BCorner[i].x), (float)(BCorner[i].y));
                
            }
            TurretImage.closePath();
            
            
            
             
        }
        
        public void printBase(Color Tint){
            if(Math.abs(POSY - (CenterY + Y / 2)) < Y / 2 + Length && Math.abs(POSX - (CenterX + X / 2)) < X / 2 + Length){
                Area.ScreenPainter.setColor(Color.black);
                Area.ScreenPainter.fill(Center.createTransformedShape(BaseImage));
                printBaseColors();
                Area.ScreenPainter.setColor(Tint);
                Area.ScreenPainter.draw(Center.createTransformedShape(BaseImage));
               
            }
            
        }
        public void printBaseColors(){
            
            
        }
        public void printTurret(Color Tint){
            
            if(Math.abs(POSY - (CenterY + Y / 2)) < Y / 2 + Length && Math.abs(POSX - (CenterX + X / 2)) < X / 2 + Length){
                Area.ScreenPainter.setColor(Color.black);
                Area.ScreenPainter.fill(Center.createTransformedShape(TurretImage));
                if(DeathTimer < 0){
                    printFlag();
                }
                Area.ScreenPainter.setColor(Tint);
                Area.ScreenPainter.draw(Center.createTransformedShape(TurretImage)); 
                
            }
            
        }
        public void printStripes(){
            for(int k = 1; k <= AILevel + 1; k++){
                int i = 2 * k;
                int j = 8 - i;
                double DX = (TCorner[3].x * i + TCorner[2].x * j) / 8;
                double DY = (TCorner[3].y * i + TCorner[2].y * j) / 8;
                Point2D.Double PointA = new Point2D.Double(DX, DY);
                DX = (TCorner[2].x * j + TCorner[1].x * i) / 8;
                DY = (TCorner[2].y * j + TCorner[1].y * i) / 8;
                Point2D.Double PointB = new Point2D.Double(DX, DY);
                Area.ScreenPainter.drawLine((int)PointA.x - CenterX, (int)PointA.y - CenterY, (int)PointB.x - CenterX, (int)PointB.y - CenterY);
            }
        }
        public void printFlag(){
            switch(FlagType){
                case 'b': Area.ScreenPainter.setColor(Color.green); break;
                case 'r': Area.ScreenPainter.setColor(Color.red); break;
            }
            double INTX = 0;
            double INTY = 0;
            for(int i = 0; i < 2; i++){
                INTX = BCorner[2].x - (3 + i) * Math.cos(Math.toRadians(TurretAngle));
                INTY = BCorner[2].y - (3 + i) * Math.sin(Math.toRadians(TurretAngle));
                Point2D.Double PointA = new Point2D.Double(INTX, INTY);
                INTX = BCorner[3].x - (3 + i) * Math.cos(Math.toRadians(TurretAngle));
                INTY = BCorner[3].y - (3 + i) * Math.sin(Math.toRadians(TurretAngle));
                Point2D.Double PointB = new Point2D.Double(INTX, INTY);
                Area.ScreenPainter.drawLine((int)PointA.x - CenterX, (int)PointA.y - CenterY, (int)PointB.x - CenterX, (int)PointB.y - CenterY);
            }
            //Area.ScreenPainter.drawOval((int)(POSX - 2 - CenterX), (int)(POSY - 2 - CenterY), 4, 4);   
            printStripes();
        }
    }
    class TempText{
        String Text;
        int Duration;
        int FadeStart = 50;
        int FontSize;
        int POSX;
        int POSY;
        char Placement;
        Color FontColor;
        public TempText(int tPOSX, int tPOSY, char tPlacement, String tText, int tFontSize, Color tFontColor, int tDuration){
            Text = tText;
            POSX = tPOSX;
            POSY = tPOSY;
            Placement = tPlacement;
            FontSize = tFontSize;
            Duration = tDuration;
            FontColor = tFontColor;
        }
        public void print(){
            Duration--;
            if(Duration == 0){
                Messages.remove(this);
            }
            Area.printLine(POSX + (Placement == 'b' ? - CenterX : 0), POSY + (Placement == 'b' ? - CenterY : 0), Text, FontSize, GUI.mix(FontColor, Level[CurrentLevel].Background, (double)Duration / FadeStart));
        }
    }

    class ProjectileVector extends Vector{
        public Projectile element(int INTA){
            return (Projectile)(elementAt(INTA));
        }
    }
   class TankVector extends Vector{
        public Tank element(int INTA){
            return (Tank)(elementAt(INTA));
        }
    }
    class LineVector extends Vector{
        public Line2D.Double element(int INTA){
            return (Line2D.Double)(elementAt(INTA));
        }
    }
    class SalvageVector extends Vector{
        public Salvage element(int INTA){
            return (Salvage)(elementAt(INTA));
        }
    }
    class TempTextVector extends Vector{
        public TempText element(int INTA){
            return (TempText)(elementAt(INTA));
        }
    }
    class ObstacleVector extends Vector{
        public Obstacle element(int INTA){
            return (Obstacle)(elementAt(INTA));
        }
    }
    class LevelData{
        int MapWidth;
        int MapHeight;
        int EnemyTanks;
        int FriendlyTanks;
        int[] EnemyCount = new int[4];
        int[] FriendlyCount = new int[4];
        int[] EnemyList = new int[4];
        int[] FriendlyList = new int[4];
        Color Background;
        Color[] Colors = new Color[3];
        double[] ColorConc = new double[Colors.length];
        public void setColors(Color ColorA, double ValueA, Color ColorB, double ValueB, Color ColorC, double ValueC, Color tBackground){
            Background = tBackground;
            Colors[0] = ColorA;
            Colors[1] = ColorB;
            Colors[2] = ColorC;
            ColorConc[0] = ValueA;
            ColorConc[1] = ValueB;
            ColorConc[2] = ValueC;
        }
        public LevelData(int tMapWidth, int tMapHeight, int tEnemyTanks){
            MapWidth = tMapWidth;
            MapHeight = tMapHeight;
            EnemyTanks = tEnemyTanks;
        }
        public void resetTanks(){
            for(int i = 0; i < 4; i++){
                EnemyList[i] = EnemyCount[i];
                FriendlyList[i] = FriendlyCount[i];
            }
        }
        public void setEnemyAI(int AI0, int AI1, int AI2, int AI3){
            EnemyList[0] = EnemyCount[0] = AI0;
            EnemyList[1] = EnemyCount[1] = AI1;
            EnemyList[2] = EnemyCount[2] = AI2;
            EnemyList[3] = EnemyCount[3] = AI3;
        }
        public void setFriendlyAI(int AI0, int AI1, int AI2, int AI3){
            FriendlyList[0] = FriendlyCount[0] = AI0;
            FriendlyList[1] = FriendlyCount[1] = AI1;
            FriendlyList[2] = FriendlyCount[2] = AI2;
            FriendlyList[3] = FriendlyCount[3] = AI3;
            for(int i = 0; i < 4; i++){
                FriendlyTanks = FriendlyTanks + FriendlyList[i];
            }
        }
        public int getEnemy(){
            if(EnemyList[0] == 0 && EnemyList[1] == 0 && EnemyList[2] == 0 && EnemyList[3] == 0){
                return -1;
            }
            while(true){
                int INTA = GUI.randomInt(0, 3);
                if(EnemyList[INTA] > 0){
                    EnemyList[INTA] = EnemyList[INTA] - 1;
                    return INTA;
                }
            }
        }
        public int getFriendly(){
            if(FriendlyList[0] == 0 && FriendlyList[1] == 0 && FriendlyList[2] == 0 && FriendlyList[3] == 0){
                return -1;
            }
            while(true){
                int INTA = GUI.randomInt(0, 3);
                if(FriendlyList[INTA] > 0){
                    FriendlyList[INTA] = FriendlyList[INTA] - 1;
                    return INTA;
                }
            }
        }
    }

    Timer Stopwatch = new Timer((int)(10 * GUI.Speed), this);
    Timer TransitionStopwatch = new Timer((int)(25 * GUI.Speed), this);
    Background Area;
    TankVector Enemies = new TankVector();
    TankVector Friends = new TankVector();
    ProjectileVector Shots = new ProjectileVector();
    
    SalvageVector Salvages = new SalvageVector();
    ObstacleVector Obstacles = new ObstacleVector();
    TempTextVector Messages = new TempTextVector();
    Tank Player;
    Display PlayerDisplay = new Display(10, 20, true);
    Display TargetDisplay = new Display(10, X - 190, false);
    LineVector Borders = new LineVector();
    Tank Target;
    Tank PlayerTemplate;
    double MouseAngle;
    Shape RangeShellImage;
    Shape NormalShellImage;
    Shot NormalShot;
    Shot GrapeShot;
    Shot VelocityShot;
    int EnemyCount;
    int FriendlyCount;
    
    char State = 't';
    int CenterX = 0;
    int CenterY = 0;
    AffineTransform Center = new AffineTransform();
    
    LevelData[] Level = new LevelData[6];
    int CurrentLevel = -1;
    public void followMouse(){
        if(MouseAngle < 0){MouseAngle = 360 + MouseAngle;}else if(MouseAngle > 360){MouseAngle = MouseAngle - 360;}
        double ArcA = Math.max(MouseAngle, Player.TurretAngle) - Math.min(MouseAngle, Player.TurretAngle);
        double ArcB = 360 - Math.max(MouseAngle, Player.TurretAngle) + Math.min(MouseAngle, Player.TurretAngle);
        
        if(Math.abs(GUI.angleDiff(MouseAngle, Player.TurretAngle)) > 1.25){
            if(GUI.angleDiff(MouseAngle, Player.TurretAngle) > 0){
                    Player.TRIGHT = !true;
                    Player.TLEFT = !false;
                }else{
                    Player.TRIGHT = !false;
                    Player.TLEFT = !true;
                }
        }else{
            Player.TRIGHT = false;
            Player.TLEFT = false;
        }
    }    
    public void switchTarget(TankVector Units){
        int INTA = Units.indexOf(Target);
        if(INTA == -1){
            if(Units.size() >= 1){
                Target = Units.element(0);
            }else{
                
            }
        }else if(INTA < Units.size() - 1){
            Target = Units.element(INTA + 1);
        }else{
            Target = Units.element(0);
            
        }
    }
   //public void addSpread(int tQuantity, double tSpreadAngle, double tSpreadSpeed){
    //public Shot(double tDamage, double tSpeedMulti, double tReloadMulti, double tLengthMulti, double tWidthMulti){
    public void paintPattern(int Switch){
        double[] Xs = new double[3];
        double[] Ys = new double[3];
        for(int i = 0; i < 3; i++){
            Xs[i] = MapX / 2 + Math.cos(Math.toRadians(120 * i + 30)) * 3000;
            Ys[i] = MapY / 2 + Math.sin(Math.toRadians(120 * i + 30)) * 3000;
        }
        double INTX = MapX / 2;
        double INTY = MapY / 2;
        
        for(int j = 0; j < 3; j++){
            Color COLORA = Level[Switch].Colors[j];
            for(int i = 0; i < 500000 * Level[Switch].ColorConc[j]; i++){
                int Target = GUI.randomInt(0, 2);
                INTX = (INTX + Xs[Target]) / 2;
                INTY = (INTY + Ys[Target]) / 2;
                try{
                    Area.Picture.setRGB((int)INTX, (int)INTY, (COLORA.getRGB()));
                }catch(Exception e){}
            }
        }
    }
    public void paintBackground(int Switch){
        Area.Painter.setColor(Level[Switch].Background);
        Area.Painter.fillRect(0, 0, MapX, MapY);
        
        for(int j = 0; j < Level[Switch].Colors.length; j++){
            for(int i = 0; i < (double)200000 * Level[Switch].ColorConc[j]; i++){
                int INTX = GUI.randomInt(1, MapX - 1);
                int INTY = GUI.randomInt(1, MapY - 1);
                Area.Picture.setRGB(INTX, INTY, Level[Switch].Colors[j].getRGB());
            }
        }
        
    }
    public void init(){
        
        NormalShot = new Shot("Normal Shot", 1, 1.25, 1, 6, 3, 1);
        GrapeShot = new Shot("Grape Shot", 1.5, 1, 0.75, 4, 1, 1);
        GrapeShot.addSpread(10, 4, 2);
        VelocityShot = new Shot("Range Shot", 1, 2, 1, 4, 6, 1);
        Player = new Tank("Player", MapX / 2, MapY / 2, 90, 2.25, 12, 2, Enemies, 'b', 200);
        CurrentLevel = Math.max(0, Math.min(Level.length - 2, GUI.TankStartingLevel - 1));
        Player.IsPlayer = true;
        Level[1] = new LevelData(MapX, MapY, 1); Level[1].setEnemyAI(2, 1, 0, 0); Level[1].setFriendlyAI(0, 1, 0, 0); Level[1].setColors(Color.yellow, 0.15, Color.green, 0.70, Color.orange, 0.15, Color.black);
        Level[2] = new LevelData(MapX, MapY, 2); Level[2].setEnemyAI(2, 2, 1, 0); Level[2].setFriendlyAI(0, 0, 1, 0); Level[2].setColors(Color.yellow, 0.25, Color.green, 0.50, Color.orange, 0.25, Color.black);
        Level[3] = new LevelData(MapX, MapY, 2); Level[3].setEnemyAI(2, 3, 2, 0); Level[3].setFriendlyAI(0, 0, 2, 0); Level[3].setColors(Color.yellow, 0.40, Color.green, 0.20, Color.orange, 0.40, Color.black);
        Level[4] = new LevelData(MapX, MapY, 2); Level[4].setEnemyAI(0, 4, 3, 2); Level[4].setFriendlyAI(0, 0, 1, 1); Level[4].setColors(Color.yellow, 0.30, Color.green, 0.40, Color.white , 0.30, Color.black);
        Level[5] = new LevelData(MapX, MapY, 2); Level[5].setEnemyAI(0, 2, 5, 2); Level[5].setFriendlyAI(0, 0, 0, 2); Level[5].setColors(Color.yellow, 0.10, Color.white, 0.50, Color.green , 0.40, Color.black);
        
        
        endLevel();
        
    }
    public void endLevel(){
        //Stopwatch.stop();
        Stopwatch.stop();
        System.gc();
        TransitionStopwatch.start();
        CombatTimer = 0;
        Area.paintComponent((Graphics2D)this.getGraphics());
        
        if(State != 'r'){
            CurrentLevel++;
            State = 't';
        }else{
            State = 'r';
        }
        
            
        
        
    }
    public void startLevel(){
        CombatTimer = 0;
        Level[CurrentLevel].resetTanks();
        initLevel();
        resetPlayer();
        TransitionStopwatch.stop();
        Stopwatch.start();
        Target = Enemies.element(0);
        State = 'c';
        try{
            for(int i = 0; i < Friends.size(); i++){Friends.element(i).Order.setToFollow(Player);}
        }catch(Exception e){}
    }
    public void resetPlayer(){
        
        Friends.remove(Player);
        if(!Friends.contains(Player)){
            if(State == 't'){
                PlayerTemplate = Player.copy(Player);
                PlayerTemplate.POSX = MapX / 2;
                PlayerTemplate.POSY = MapY / 2;
                Player = PlayerTemplate.copy(PlayerTemplate);
            }else{
                Player = PlayerTemplate.copy(PlayerTemplate);
            }
            Friends.add(Player);
            Player.IsPlayer = true;
        }else{
        
            Player.DeathTimer = -1;
            Player.POSX = MapX / 2;
            Player.POSY = MapY / 2;
            Player.Hitpoints = Player.MaxHitpoints;
            Player.ReloadTimer = 0;
            Player.Speed = 0;
            Player.Angle = 90;
            Player.TurretAngle = 90;
            Player.FORWARD = false;
            Player.BACK = false;
            Player.LEFT = false;
            Player.RIGHT = false;
        }
    }
    
        
   
    public void initLevel(){
        //public TempText(int tPOSX, int tPOSY, char tPlacement, String tText, int tFontSize, Color tFontColor, int tDuration){
        
        EnemyCount = 0;
        FriendlyCount = 0;
        resetPlayer();
        Target = Player;
        
        Friends.clear();
        Enemies.clear();
        Shots.clear();
        Salvages.clear();
        Friends.add(Player);
        //MapX = Level[CurrentLevel].MapWidth;
        //MapY = Level[CurrentLevel].MapWidth;
        Area.Painter.setColor(Color.green);
        Area.Painter.drawRect(X / 4, Y / 4, MapX - X / 4 - X / 4, MapY - Y / 4 - Y / 4);
        Area.Painter.setColor(Color.green);
        Area.Painter.drawRect(X / 4 - 10, Y / 4 - 10, MapX - X / 4 - X / 4 + 20, MapY - Y / 4 - Y / 4 + 20);
        int Count = 0;
        
        while(true){
            

            if(Count == Level[CurrentLevel].EnemyTanks){
                break;
            }
            int INTA = Level[CurrentLevel].getEnemy();
            if(INTA < 0){break;
            }else{
                int TempX = (GUI.randomInt(0, 1) == 0 ? MapX - 500 : 500);
                int TempY = GUI.randomInt(500, MapY - 500);
                Tank TANKA = new Tank("Enemy " + GreekAlphabet[EnemyCount % GreekAlphabet.length], TempX, TempY, 90, 2.25, 8, INTA, Friends, 'r', GUI.randomInt(225, 275));
                if(GUI.randomInt(1, 3) == 1){
                    switch(GUI.randomInt(1, 4)){
                        case 1: TANKA.FasterReloadBonus = SalvageMulti * 4; break; 
                        case 2: TANKA.MoreFragmentBonus = SalvageMulti * 2; break; 
                        case 3: TANKA.HigherDamageBonus = SalvageMulti * 2; break; 
                        case 4: TANKA.ReactiveArmorBonus = SalvageMulti + 5; break; 
                    }
                }
                Enemies.add(TANKA);
                EnemyCount++;
                Count++;
            }
            
        }
        
        Count = 0;
        while(true){
            int INTA = Level[CurrentLevel].getFriendly();
           
            if(INTA < 0){break;
            }else{
                Count++;
                int Angle = Count * (360 / Level[CurrentLevel].FriendlyTanks);
                double TempX = (200 * Math.cos(Math.toDegrees(Angle))) + MapX / 2;
                double TempY = (200 * Math.sin(Math.toDegrees(Angle))) + MapY / 2;
                Tank TANKA = new Tank("Ally " + GreekAlphabet[FriendlyCount % GreekAlphabet.length], TempX, TempY, 90, 2.25, 10, INTA, Enemies, 'b', GUI.randomInt(225, 275));
                FriendlyCount++;
                Friends.add(TANKA);
                
                
            }
        }
        //for(int i = 0; i < Friends.size(); i++){Friends.element(i).Order.setToFollow(Player);}
        
    }
    public void initSounds(){
        GUI.playSound(false, "Explosion1.wav", 0, 1);
        GUI.playSound(false, "Explosion2.wav", 0, 1);
        GUI.playSound(false, "Explode.wav", 0, 1);
        GUI.playSound(false, "GirderImpact.wav", 0, 1);
        GUI.playSound(false, "OilDrumImpact.wav", 0, 1);
    }
    
    public Tanks(){
        //BASIC INITIALIZATION
        super("Window");
        this.setSize(X, Y + TopBorderWidth);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        
        Area = new Background();
        Area.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        contentArea.add(Area);
        init();
        initSounds();
        TransitionStopwatch.start();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        
        this.show();
        
        //this.pack();   
    }
   
   
    public void changeShot(){
        if(Player.ShotType == NormalShot){
            Player.ShotType = GrapeShot;
        }else if(Player.ShotType == GrapeShot){
            Player.ShotType = VelocityShot;
        }else if(Player.ShotType == VelocityShot){
            Player.ShotType = NormalShot;
        }
        
        
    }
    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){
        switch(State){
            case 'c':
                switch(e.getKeyCode()){
                    case 65: Player.LEFT = true; break;
                    case 68: Player.RIGHT = true; break;
                    case 87: Player.FORWARD = true; break;
                    case 83: Player.BACK = true; break;
                    case 37: Player.TLEFT = true; break;
                    case 39: Player.TRIGHT = true; break;
                //  default: System.out.println(e.getKeyCode());

          
                }
                break;
        }
    }
    public void keyReleased(KeyEvent e){
       switch(State){
           case 'c':
                switch(e.getKeyCode()){
                    case 65: Player.LEFT = false; break;
                    case 68: Player.RIGHT = false; break;
                    case 87: Player.FORWARD = false; break;
                    case 83: Player.BACK = false; break;
                    case 37: Player.TLEFT = false; break;
                    case 39: Player.TRIGHT = false; break;
                }
        }
    }
 
    
    public void keyTyped(KeyEvent e){
        switch(State){
            case 'c':
                switch(e.getKeyChar()){

                    case ' ': Player.fire(); break;
                    case 'e': switchTarget(Enemies); break;
                    case 'f': switchTarget(Friends); break;
                    case '1': for(int i = 0; i < Friends.size(); i++){Friends.element(i).Order.setToAttack(Target);} break;
                    case '2': for(int i = 0; i < Friends.size(); i++){Friends.element(i).Order.setToFollow(Player);} break;
                    case '3': if(Friends.contains(Target)){Target.Order.setToHold();} break;
                 //   case 'q': Player.ReactiveArmorBonus = 20; break;
                //    case 'r': Obstacles.add(new Obstacle(Player.POSX, Player.POSY + 100, 30, 'r', 50, 45));
                    
                }
                break;
            case 't': case 'r':
                switch(e.getKeyChar()){
                    default: 
                        if(RestartCounter > 20){
                            if(CurrentLevel == Level.length){
                                System.out.println("You Win");
                            }else{
                                startLevel();
                                RestartCounter = 0;
                            }
                        }
                }
                break;
        }
        
    }
    public void mousePressed(MouseEvent Event){
        switch(State){
            case 'c':
                switch(Event.getButton()){
                    case 1: Player.fire(); break;
                    case 3: changeShot(); break;
                }
            break;
            
        }
    }
    public void mouseReleased(MouseEvent Event){}
    public void mouseClicked(MouseEvent Event){
        
    }
    boolean MouseActive;
    public void mouseEntered(MouseEvent Event){MouseActive = true;}
    public void mouseExited(MouseEvent Event){MouseActive = false;}
    public void mouseMoved(MouseEvent Event){
        MouseAngle = - Math.toDegrees(Math.atan2(Event.getX() + CenterX - Player.POSX, Event.getY() + CenterY - Player.POSY - TopBorderWidth)) + 90;
        
    }
    public void mouseDragged(MouseEvent Event){}
    
    public void retryLevel(){
        State = 'r';
        endLevel();
    }
    int RestartCounter = 0;
    long CombatTimer = 0;
    
    public void actionPerformed(ActionEvent e){
        
        //System.out.println(InCombat);
        ShellImpactCounter = Math.max(ShellImpactCounter - 1, 0);
        switch(State){
            case 'c':
                
                CombatTimer++;
                
                Area.ScreenPainter.drawImage(Area.Picture.getSubimage(CenterX, CenterY, X, Y), null, 0, 0);
                
                if(MouseActive){
                    followMouse();
                }
        
       
                for(int i = 0; i < Salvages.size(); i++){
                    Salvages.element(i).print();
                }


                TankVector AllUnits = new TankVector();
                AllUnits.addAll(Friends);
                AllUnits.addAll(Enemies);
                for(int i = 0; i < AllUnits.size(); i++){
                    
                    if(!AllUnits.element(i).IsPlayer){
                        AllUnits.element(i).doAI();
                    }
                    AllUnits.element(i).move(); 
                    if(AllUnits.element(i).DeathTimer < 0){
                        for(int j = 0; j < Salvages.size(); j++){
                            try{
                                AllUnits.element(i).checkIntersect(Salvages.element(j));
                            }catch(ArrayIndexOutOfBoundsException h){}
                        }
                    }
                }
                
                for(int i = 0; i < AllUnits.size(); i++){
                    for(int j = i; j < AllUnits.size(); j++){
                        Line2D.Double LineA = AllUnits.element(i).checkIntersect(AllUnits.element(j));
                        if(LineA != null){
                            AllUnits.element(i).collide(AllUnits.element(j), LineA);
                        }
                    }
                }
                for(int i = 0; i < Obstacles.size(); i++){
                    Obstacles.element(i).print();
                }
                for(int i = 0; i < Shots.size(); i++){
                    if(Shots.element(i).Duration >= 20){
                        Shots.element(i).move();
                    }
                }
                
                for(int i = 0; i < AllUnits.size(); i++){
                    AllUnits.element(i).printTurret((AllUnits.element(i).DeathTimer >= 0 ? Color.red : Color.white));
                }
                
                for(int i = 0; i < Obstacles.size(); i++){
                    Obstacles.element(i).printTop();
                }
                for(int i = 0; i < Shots.size(); i++){
                    if(Shots.element(i).Duration < 20){
                        Shots.element(i).move();
                    }
                }
                if(Enemies.size() < Level[CurrentLevel].EnemyTanks){
                    int INTA = Level[CurrentLevel].getEnemy();
                    if(INTA < 0){
                        //System.out.println("Next");
                        if(Enemies.size() == 0 && CombatTimer > 0){
                            //System.out.println("NEXT LEVEL");
                           CombatTimer = -100;
                            
                        }   
                    }else{
                    // System.out.println("Making Tank" + "\t" + Enemies.size() + "\t" + Level[CurrentLevel].EnemyTanks + "\t" + CurrentLevel);
                        int TempX = (GUI.randomInt(0, 1) == 0 ? MapX - 500 : 500);
                        int TempY = GUI.randomInt(500, MapY - 500);
                        Tank TANKA = new Tank("Enemy " + GreekAlphabet[EnemyCount % GreekAlphabet.length], TempX, TempY, 90, 2.25, 8, INTA, Friends, 'r', GUI.randomInt(225, 300)); 
                        if(GUI.randomInt(1, 3) == 1){
                            switch(GUI.randomInt(1, 4)){
                                case 1: TANKA.FasterReloadBonus = SalvageMulti * 4; break; 
                                case 2: TANKA.MoreFragmentBonus = SalvageMulti * 2; break; 
                                case 3: TANKA.HigherDamageBonus = SalvageMulti * 2; break; 
                                case 4: TANKA.ReactiveArmorBonus = SalvageMulti + 5; break; 
                            }
                        }
                        EnemyCount++;
                        Enemies.add(TANKA);
                        /*
                        for(int i = 0; i < Level[CurrentLevel].EnemyList.length; i++){
                            System.out.println(Level[CurrentLevel].EnemyList[i]);
                        }*/
                        System.out.println();
                    }
           // System.out.println(TempX + "\t" + TempY);
                }
                for(int i = 0; i < Messages.size(); i++){
                    Messages.element(i).print();
                }
                PlayerDisplay.update(Player);
                TargetDisplay.update(Target);
                
                Area.paintComponent((Graphics2D)(this.getGraphics()));
                if(!Friends.contains(Player) && State == 'c'){
                    retryLevel();
                    //this.dispose();
                    //GUI.main(null);
            
            
                    return;
                }
                if(CombatTimer == -1){
                    endLevel();
                }
                break;
            case 't':
                RestartCounter++;
                Area.paintComponent((Graphics2D)this.getGraphics());
                if(RestartCounter == 10){
                    try{
                        paintBackground(CurrentLevel);
                        paintPattern(CurrentLevel);
                    }catch(Exception f){}
                }
                break;
            case 'r':
                RestartCounter++;
                Area.paintComponent((Graphics2D)this.getGraphics());
                break;
        }
    }
    

   
    class Background extends JPanel{
        BufferedImage Picture = new BufferedImage(MapX, MapY, BufferedImage.TYPE_BYTE_INDEXED);//TYPE_3BYTE_BGR TYPE_USHORT_565_RGB
        BufferedImage Screen = new BufferedImage(X, Y, GUI.TankImageType);
        Graphics2D Painter = Picture.createGraphics();
        Graphics2D ScreenPainter = Screen.createGraphics();
        double AvTime = 0;
        int Count = 0;
        long Temp = System.currentTimeMillis();
        public void printLine(int INTX, int INTY, String Text, int Font, Color FontColor){
            
            Font FONT = Area.ScreenPainter.getFont();
            ScreenPainter.setFont(FONT.deriveFont((float)Font));
            FontMetrics Metrics = ScreenPainter.getFontMetrics();
            ScreenPainter.setColor(FontColor);
            ScreenPainter.drawString(Text, INTX - Metrics.stringWidth(Text) / 2, INTY - Metrics.getHeight() / 2);
            ScreenPainter.setFont(FONT);    
            
            
        }
        
        public void paintComponent(Graphics2D Paint){
            
            try{
                switch(State){
                    case 'c':
                        if(Count == 0){Temp = System.currentTimeMillis();}
            
            
                        
                        CenterX = ((int)(Math.min(MapX - X, Math.max(Player.POSX - X / 2, 0))));
                        CenterY = ((int)(Math.min(MapY - Y, Math.max(Player.POSY - Y / 2, 0))));
                        Center.setToTranslation(-CenterX, -CenterY);
            
                        PlayerDisplay.print(ScreenPainter);
                        TargetDisplay.print(ScreenPainter);
                        int INTA = (int)((X / 2) * (1 - (double)Math.abs(CombatTimer) / 100));
                        int INTB = (int)((Y / 2) * (1 - (double)Math.abs(CombatTimer) / 100));
                        ScreenPainter.setColor(Color.black);
                        ScreenPainter.fillRect(0, 0, INTA, Y);
                        ScreenPainter.fillRect(X - INTA, 0, INTA, Y);
                        ScreenPainter.fillRect(INTA, 0, X - 2 * INTA, INTB);
                        ScreenPainter.fillRect(INTA, Y - INTB, X - 2 * INTA, INTB);
                        Paint.drawImage(Screen, 0, TopBorderWidth, null);
                        AvTime = (AvTime * Count + System.currentTimeMillis() - Temp) / (++Count);
                        if(GUI.Time){
                            System.out.println(AvTime);
                        }
                        Temp = System.currentTimeMillis();
                    break;
                    case 't':
                        ScreenPainter.setColor(Color.black);
                        ScreenPainter.fillRect(0, 0, X, Y);
                        
                        
                        
                        if(CurrentLevel < Level.length){
                            printLine(X / 2, Y / 2, "Level " + CurrentLevel, 100, Color.white);
                            if(RestartCounter > 10){
                                printLine(X / 2, Y / 2, "Press any key to continue.", 20, GUI.blackenColor(Color.white, ((double)RestartCounter - 10) / 200));
                            }
                        }else{
                            printLine(X / 2, Y / 2, "You Win!", 100, Color.white);
                        }
                        
                        
                        Paint.drawImage(Area.Screen, 0, TopBorderWidth, null);
                    break;
                    case 'r':
                        ScreenPainter.setColor(Color.black);  
                        ScreenPainter.fillRect(0, 0, X, Y);
                        ScreenPainter.setColor(Color.white);
                        printLine(X / 2, Y / 2, "You Died", 100, Color.white);
                        if(RestartCounter > 10){
                            printLine(X / 2, Y / 2, "Press any key to restart.", 20, GUI.blackenColor(Color.white, ((double)RestartCounter - 10) / 200));
                        }
                        Paint.drawImage(Area.Screen, 0, TopBorderWidth, null);
                        
                        break;
                    }
            }catch(Exception e){}
        }
        
    }
 
}
class Circuit extends JFrame implements KeyListener, ActionListener, MouseListener, MouseMotionListener{
    //colors
    //black - 0
    //white - 1
    //blue - 2
    //red - 3
    //green - 4
    final int X = GUI.X;
    final int Y = GUI.Y;
    final int[][] Screen = new int[X][Y];
   
    class Point{
        double POSX;
        double POSY;
        boolean State;
        char Use;
        GeneralVector Links = new GeneralVector();
        Object Owner;
        public Point(double tPOSX, double tPOSY, char tUse, Object tOwner){
            POSX = tPOSX;
            POSY = tPOSY;
            Use = tUse;
            Owner = tOwner;
        }
        public void addLink(Point Connection){
            Links.add(Connection);
        }
    }
    class Gate implements CircuitComponent{
        GeneralVector Inputs = new GeneralVector();
        GeneralVector Links = new GeneralVector();
        Point Output;
        char Type;
        Shape Image;
        boolean[] TruthTable = new boolean[(int)Math.pow(2, Inputs.size())];
        double POSX;
        double POSY;
        public Gate(double tPOSX, double tPOSY, char tType){
            POSX = tPOSX;
            POSY = tPOSY;
            Type = tType;
            switch(Type){
                case 'a': {boolean[] tTruthTable = {
                            false,
                            false,
                            false,
                            true
                            }; TruthTable = tTruthTable; break;}
                case 'o': {boolean[] tTruthTable = {
                            false,
                            true,
                            true,
                            true
                            }; TruthTable = tTruthTable; break;}
                case 'n': {boolean[] tTruthTable = {
                            true,
                            false
                            }; TruthTable = tTruthTable; break;}
                
                
            }
            
            switch(Type){
                case 'o':
                case 'a':   Inputs.add(new Point(POSX - 10, POSY - 7.5, 'i', this));
                            Inputs.add(new Point(POSX - 10, POSY + 7.5, 'i', this));
                            Output = new Point(POSX + 20, POSY, 'o', this);
                            break;
                case 'n':   Inputs.add(new Point(POSX - 10, POSY, 'i', this));
                            Output = new Point(POSX + 10, POSY, 'o', this);
                
                        
            }
            Image = getShape(Type, POSX, POSY);
            Area.Painter.setColor(Color.white);
            Area.Painter.draw(Image);
            Area.Painter.setColor(Color.black);
            Area.Painter.fillOval((int)Output.POSX - 2, (int)Output.POSY - 2, 4, 4);
            for(int i = 0; i < Inputs.size(); i++){
                Area.Painter.setColor(Color.black);
                Area.Painter.fillRect((int)Inputs.point(i).POSX - 2, (int)Inputs.point(i).POSY - 2, 4, 4);
                Area.Painter.setColor(Color.white);
                Area.Painter.drawRect((int)Inputs.point(i).POSX - 2, (int)Inputs.point(i).POSY - 2, 4, 4);
            }
            Area.Painter.setColor(Color.black);
            Area.Painter.drawOval((int)Output.POSX - 2, (int)Output.POSY - 2, 4, 4);
        }
       
        public void linkTo(Wire WIRE){
            Output.addLink(WIRE.Start);
            Links.add(WIRE);
            WIRE.Start.State = this.Output.State;
            Fronts.add(new Front(WIRE, this.Output.State, 'i'));
        }
        
        public void move(){
           
            int Result = 0;
            for(int i = 0; i < Inputs.size(); i++){
                if(Inputs.point(i).State == true){
                    Result = Result + (int)Math.pow(2, i);
                }
                
            }
            if(Output.State != TruthTable[Result]){
                Output.State = TruthTable[Result];
                for(int i = 0; i < Links.size(); i++){
                    Fronts.add(new Front(Links.wire(i), Output.State, 'i'));
                }
            }
            for(int i = 0; i < Inputs.size(); i++){
                if(Inputs.point(i).State){
                    Area.Painter.setColor(Color.red);
                }else{
                    Area.Painter.setColor(Color.white);
                }
                Area.Painter.drawOval((int)Inputs.point(i).POSX - 2, (int)Inputs.point(i).POSY - 2, 4, 4);
            }
            if(Output.State){
                Area.Painter.setColor(Color.red);
            }else{
                Area.Painter.setColor(Color.white);
            }
            Area.Painter.drawOval((int)Output.POSX - 2, (int)Output.POSY - 2, 4, 4);
        }
        public void remove(){
            Gates.remove(this);
            Area.Painter.setColor(Color.black);
            Area.Painter.draw(this.Image);
            for(int i = 0; i < Inputs.size(); i++){             
                Area.Painter.fillRect((int)Inputs.point(i).POSX - 2, (int)Inputs.point(i).POSY - 2, 4, 4);
                Area.Painter.drawRect((int)Inputs.point(i).POSX - 2, (int)Inputs.point(i).POSY - 2, 4, 4);
            }
            Area.Painter.drawOval((int)Output.POSX - 2, (int)Output.POSY - 2, 4, 4); 
            for(int i = 0; i < Links.size(); i++){
                Fronts.add(new Front(Links.wire(i), false, 'i'));
            }
            for(int i = 0; i < Wires.size(); i++){
                for(int j = 0; j < Inputs.size(); j++){   
                    if(Wires.wire(i).End.Links.contains(Inputs.point(j))){
                        Wires.wire(i).End.Links.remove(Inputs.point(j));
                    }
                }
                
            }
        }
        public void setStart(boolean State){
            
        }
    }
    class Wire implements CircuitComponent{
        Point Start;
        Point End;
        double Angle;
        public void setStart(boolean State){
            if(State != Start.State){
                Fronts.add(new Front(this, State, 'i'));
                Start.State = State;
            }
        }
        public Wire(double SX, double SY, double EX, double EY){
            Start = new Point(SX, SY, 'i', this);
            End = new Point(EX, EY, 'o', this);
            Angle = Math.toDegrees(Math.atan2(EY - SY, EX - SX));
            Area.Painter.setColor(OFF);
            
            Fronts.add(new Front(this, false, 'i'));
            
            //Area.Painter.drawLine((int)SX, (int)SY, (int)EX, (int)EY);
        }
        public void linkTo(Wire WIRE){
            End.addLink(WIRE.Start);
            WIRE.Start.State = this.End.State;
            Fronts.add(new Front(WIRE, this.End.State, 'i'));
        }   
        public void remove(){
            Wires.remove(this);
            
            
            Start.Links.clear();
            Front Destroyer = new Front(this, false, 'i');
            Destroyer.Destroyer = true;
            Fronts.add(Destroyer);
            for(int i = 0; i < Wires.size(); i++){
                if(Wires.wire(i).End.Links.contains(Start)){
                    Wires.wire(i).End.Links.remove(Start);
                }
            }
            for(int i = 0; i < Gates.size(); i++){
                Gates.gate(i).Links.remove(this);
                if(Gates.gate(i).Output.Links.contains(Start)){
                    Gates.gate(i).Output.Links.remove(Start);
                }
            }
        }
    }
    interface CircuitComponent{
        public void setStart(boolean State);
        
    }
    class Front{
        double POSX;
        double POSY;
        Wire CurrentWire;
        boolean State;
        boolean Destroyer;
        int Direction;
        public Front(Wire tCurrentWire, boolean tState, char tDirection){
            CurrentWire = tCurrentWire;
            State = tState;
            if(tDirection == 'i'){
                POSX = CurrentWire.Start.POSX;
                POSY = CurrentWire.Start.POSY;
            }else{
                POSX = CurrentWire.End.POSX;
                POSY = CurrentWire.End.POSY;
            }
            if(tDirection == 'o'){
                Direction = -1;
            }else{
                Direction = 1;
            }
        }
        public void move(){
            if(POSX < CurrentWire.End.POSX){
                POSX = Math.min(CurrentWire.End.POSX, POSX + Direction * Math.cos(Math.toRadians(CurrentWire.Angle)));
            }else if(POSX > CurrentWire.End.POSX){
                POSX = Math.max(CurrentWire.End.POSX, POSX + Direction * Math.cos(Math.toRadians(CurrentWire.Angle)));
            }
            if(POSY < CurrentWire.End.POSY){
                POSY = Math.min(CurrentWire.End.POSY, POSY + Direction * Math.sin(Math.toRadians(CurrentWire.Angle)));
            }else if(POSY > CurrentWire.End.POSY){
                POSY = Math.max(CurrentWire.End.POSY, POSY + Direction * Math.sin(Math.toRadians(CurrentWire.Angle)));
            }
            try{
                if(!Destroyer){
                    Area.Picture.setRGB((int)POSX, (int)POSY, (State == true ? ON.getRGB() : OFF.getRGB()));
                }else{
                    Area.Picture.setRGB((int)POSX, (int)POSY, 0);
                }
            }catch(Exception e){}
            if(POSX == CurrentWire.End.POSX && POSY == CurrentWire.End.POSY){
                CurrentWire.End.State = this.State;
                end();
            }
        }
        public void end(){
            Fronts.remove(this);
            if(this.Destroyer == true){
                Wires.remove(CurrentWire);
            }
            for(int i = 0; i < CurrentWire.End.Links.size(); i++){
                ((CircuitComponent)CurrentWire.End.Links.point(i).Owner).setStart(this.State);
                CurrentWire.End.Links.point(i).State = this.State;
            }
            CurrentWire = null;
            
        }
        
    }
    class GeneralVector extends Vector{
        public Wire wire(int INTA){
            return (Wire)elementAt(INTA);
        }
        public Front front(int INTA){
            return (Front)elementAt(INTA);
        }
        public Point point(int INTA){
            return (Point)elementAt(INTA);
        }
        public Gate gate(int INTA){
            return (Gate)elementAt(INTA);
        }
        
    }
    Double[] MouseBuffer = new Double[4];
    Color ON = Color.red;
    Color OFF = Color.white;
    Background Area;
    char GateBrush = ' ';
    Timer Stopwatch = new Timer(0, this);
    double MouseX;
    double MouseY;
    GeneralVector Fronts = new GeneralVector();
    GeneralVector Wires = new GeneralVector();
    GeneralVector Gates = new GeneralVector();
     public Shape getShape( char Type, double POSX, double POSY){
            switch(Type){
                case 'a':
                    GeneralPath AndImage = new GeneralPath();
                    AndImage.moveTo((int)POSX - 10, (int)POSY - 10);
                    AndImage.lineTo((int)POSX - 10, (int)POSY + 10);
                    AndImage.lineTo((int)POSX + 10, (int)POSY + 10);
                    AndImage.quadTo((int)POSX + 30, (int)POSY, (int)POSX + 10, (int)POSY - 10);
                    AndImage.closePath();
                    return AndImage;
           
                case 'o':
                    GeneralPath Image = new GeneralPath();
                    Image.moveTo((float)POSX + 20, (float)POSY);
                    Image.quadTo((float)POSX + 5, (float)POSY - 9f, (float)POSX - 10, (float)POSY - 10);
                    Image.lineTo((float)POSX, (float)POSY);
                    Image.lineTo((float)POSX - 10, (float)POSY + 10);
                    Image.quadTo((float)POSX + 5, (float)POSY + 9f, (float)POSX + 20, (float)POSY);
                    return Image;
                    
                case 'n':
                    GeneralPath NotImage = new GeneralPath();
                    NotImage.moveTo((int)POSX - 10, (int)POSY + 10);
                    NotImage.lineTo((int)POSX - 10, (int)POSY - 10);
                    NotImage.lineTo((int)POSX + 10, (int)POSY + 0);
                    NotImage.closePath();
                    return NotImage;
                    
                
            }
            return null;
        }
    public void makeLine(){
        Wire NewWire = new Wire(MouseBuffer[0].doubleValue(), MouseBuffer[1].doubleValue(), MouseBuffer[2].doubleValue(), MouseBuffer[3].doubleValue());
        
        
        for(int i = 0; i < Wires.size(); i++){
            if(Math.sqrt(Math.pow(Wires.wire(i).Start.POSX - NewWire.End.POSX, 2) + Math.pow(Wires.wire(i).Start.POSY - NewWire.End.POSY, 2)) < 7.5){
                NewWire.linkTo(Wires.wire(i));
            }
            if(Math.sqrt(Math.pow(Wires.wire(i).End.POSX - NewWire.Start.POSX, 2) + Math.pow(Wires.wire(i).End.POSY - NewWire.Start.POSY, 2)) < 7.5){
                Wires.wire(i).linkTo(NewWire);
            }
        }
        for(int i = 0; i < Gates.size(); i++){
            if(Math.sqrt(Math.pow(Gates.gate(i).Output.POSX - NewWire.Start.POSX, 2) + Math.pow(Gates.gate(i).Output.POSY - NewWire.Start.POSY, 2)) < 7.5){
                Gates.gate(i).linkTo(NewWire);
            }
            for(int j = 0; j < Gates.gate(i).Inputs.size(); j++){
                if(Math.sqrt(Math.pow(Gates.gate(i).Inputs.point(j).POSX - NewWire.End.POSX, 2) + Math.pow(Gates.gate(i).Inputs.point(j).POSY - NewWire.End.POSY, 2)) < 7.5){
                    NewWire.End.addLink(Gates.gate(i).Inputs.point(j));
                }
            }
        }
        Wires.add(NewWire);
    }
    public void init(){
        for(int i = 1; i <= 10; i++){
            Wires.add(new Wire(0, (Y * i + 2) / 11, X / 8, Y * i / 11));
        }
        
 
    }
    public Circuit(){
        
        //BASIC INITIALIZATION
        super("Window");
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new Background();
        contentArea.add(Area);
        
        Stopwatch.start();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        init();
        
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }

    //EVENT TRIGGERS
    public void keyPressed(KeyEvent e){
        switch(e.getKeyCode()){
            case 27: for(int i = 0; i < MouseBuffer.length; i++){
                        MouseBuffer[i] = null;
                     }
                     break;
            case 67: Area.Painter.setColor(Color.black);
                     Area.Painter.fillRect(0, 0, X, Y);
                     Wires.clear();
                     Gates.clear();
                     Fronts.clear();
                     init();
            //default:System.out.println(e.getKeyCode());
            
        }
        
    }
    public void keyReleased(KeyEvent e){
        

    }
 
    
    public void keyTyped(KeyEvent e){
        switch(e.getKeyChar()){
            case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
            int INTA = Integer.parseInt("" + e.getKeyChar());
                Fronts.add(new Front(Wires.wire(INTA), !Wires.wire(INTA).Start.State, 'i')); 
                Wires.wire(INTA).Start.State = !Wires.wire(INTA).Start.State; 
                break;
            
            case 'w': GateBrush = 'w'; Area.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR)); break;
            case 'a': GateBrush = 'a'; Area.setCursor(new Cursor(Cursor.HAND_CURSOR));break;
            case 'o': GateBrush = 'o'; Area.setCursor(new Cursor(Cursor.HAND_CURSOR));break;
            case 'n': GateBrush = 'n'; Area.setCursor(new Cursor(Cursor.HAND_CURSOR));break;
            case 'd': GateBrush = 'd'; Area.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        
    }

    public void setCursor(){
        Area.ScreenPainter.setColor(Color.white);
        switch(GateBrush){
            
            case 'n': case 'o': case 'a':
                Area.ScreenPainter.draw(getShape(GateBrush, MouseX, MouseY));
                break;
            case 'w':
                if(MouseBuffer[0] != null){
                    Area.ScreenPainter.drawLine((int)MouseBuffer[0].doubleValue(), (int)MouseBuffer[1].doubleValue(), (int)MouseX, (int)MouseY);
                }
        }
        
    }
    public void actionPerformed(ActionEvent e){
        if(print){
            Area.ScreenPainter.drawImage(Area.Picture, null, 0, 0);
        }
        setCursor();
        for(int i = 0; i < Fronts.size(); i++){
            if(Fronts.front(i).State == false){
                Fronts.front(i).move();
            }
        }
        for(int i = 0; i < Fronts.size(); i++){
            if(Fronts.front(i).State == true){
                Fronts.front(i).move();
            }
        }
        for(int i = 0; i < Gates.size(); i++){
            Gates.gate(i).move();
        }
        Area.paintComponent(this.getGraphics());
    }
    public void mouseMoved(MouseEvent Event){
        MouseX = Event.getX();
        MouseY = Event.getY();
        
        
    }
    public void mouseDragged(MouseEvent Event){
    }
    public void mousePressed(MouseEvent Event){
        switch(GateBrush){
            case 'w':
            if(MouseBuffer[0] == null){
                MouseBuffer[0] = new Double(Event.getX());
                MouseBuffer[1] = new Double(Event.getY());
            }else{
                MouseBuffer[2] = new Double(Event.getX());
                MouseBuffer[3] = new Double(Event.getY());
                makeLine();
                switch(Event.getButton()){
                    case 1: 
                        for(int i = 0; i < MouseBuffer.length; i++){
                            MouseBuffer[i] = null;
                        }
                        break;
                    case 3:
                        MouseBuffer[0] = MouseBuffer[2];
                        MouseBuffer[1] = MouseBuffer[3];
                        MouseBuffer[2] = null;
                        MouseBuffer[3] = null;
                        break;
                }
                
            }
            
            break;
            case 'a': case 'o': case 'n': Gates.add(new Gate(Event.getX(), Event.getY(), GateBrush));
            for(int i = 0; i < MouseBuffer.length; i++){
                MouseBuffer[i] = null;
            }
            break;
            case 'd':
                for(int i = 0; i < Gates.size(); i++){
                    if(Gates.gate(i).Image.contains(Event.getX(), Event.getY())){
                        Gates.gate(i).remove();
                    }
                }
                for(int i = 0; i < Wires.size(); i++){
                    Line2D.Double LineA = new Line2D.Double(Wires.wire(i).Start.POSX, Wires.wire(i).Start.POSY, Wires.wire(i).End.POSX, Wires.wire(i).End.POSY);
                    if(LineA.ptSegDist(Event.getX(), Event.getY()) < 5){
                        Wires.wire(i).remove();
                    }
                }
        }
        
    }
    public void mouseReleased(MouseEvent Event){
    }
    public void mouseClicked(MouseEvent Event){
        
    }
    public void mouseEntered(MouseEvent Event){
    }
    public void mouseExited(MouseEvent Event){

    }
    public void paint(){
        ((Graphics2D)this.getGraphics()).drawImage(Area.Picture, null, 0, 0);
    }
    boolean print = false;
    class Background extends JPanel{
        
        
        BufferedImage Picture = new BufferedImage(X, Y, BufferedImage.TYPE_USHORT_555_RGB);
        Graphics2D Painter = Picture.createGraphics();
        
        BufferedImage Screen = new BufferedImage(X, Y, BufferedImage.TYPE_INT_RGB);
        Graphics2D ScreenPainter = Screen.createGraphics();
        float AvTime = 0;
        long Temp = 0;
        int Count;
        public void paintComponent(Graphics paint){
            Graphics2D Paint = (Graphics2D)(paint);
            //System.out.println(Ship.FACINGANGLE + "\t" + Ship.SPEED + "\t" + Ship.POSY + "\t" + Ship.POSX + "\t");
            Count++;
            AvTime = (AvTime + System.currentTimeMillis() - Temp) / Count;
            
            //System.out.println(AvTime);
            Temp = System.currentTimeMillis();
            print = !print;
            if(print){
                try{
                    
                    Paint.drawImage(Screen, null, 0, 0);
                }catch(Exception e){}
            }
        }
    }
    
}
    
            
 

class Test extends JFrame implements KeyListener, ActionListener, MouseListener, MouseMotionListener{
    
    final int TopBorderWidth = 26;
    final int X = 1024 - TopBorderWidth;
    final int Y = 768;
    WaveSound shellFire = new WaveSound("Explosion2.wav");
    public void shellFire(){
        GUI.playSound(true, "Explosion2.wav", 0, 1);
    }
    public void softShellFire(){
        GUI.playSound(true, "Explosion4.wav", 0, 1);
    }
    public void shellImpact(){
        GUI.playSound(true, "Explosion1.wav", 0, 1);
    }
    public void softShellImpact(){
        GUI.playSound(true, "Explosion5.wav", 0, 1);
    }
    public void tankExplode(){
        GUI.playSound(true, "EXPLODE.wav", 0, 1);
    }
    public void collideSound(){
        GUI.playSound(true, "GirderImpact.wav", 0, 2);  
    }
    public void rocketRelease(){
        GUI.playSound(true, "ROCKETRELEASE.wav", 0, 2);  
    }
    public void collectSound(){
        GUI.playSound(true, "OilDrumImpact.wav", 0, 2);  
    }
    public void homingSound(){
        GUI.playSound(true, "WEAPONHOMING.wav", 0, 2);  
    }
    public void playHoming(){
        if(HomingSoundCounter == 0){
            homingSound();
            HomingSoundCounter = 40;
        }
   
    }
    int HomingSoundCounter = 0;
    public void firingSound(char Switch){
        switch(Switch){
            case 'C': shellFire(); break;
            case 'c': softShellFire(); break;
            case 'R': rocketRelease(); break;
            case 'r': rocketRelease(); break;
            
            case 'I': shellImpact(); break;
            case 'i': softShellImpact(); break;
            case 'X': tankExplode(); break;
            case 'x': tankExplode(); break;
            
            case 'h': homingSound(); break;
        }
    }
    class GeneralVector extends Vector{
        public Shot3D shot3D(int INTA){
            return (Shot3D)elementAt(INTA);
        }
        public Tank3D tank3D(int INTA){
            return (Tank3D)elementAt(INTA);
        }
        public Obstacle3D obstacle3D(int INTA){
            return (Obstacle3D)elementAt(INTA);
        }
        public Fragment3D fragment3D(int INTA){
            return (Fragment3D)elementAt(INTA);
        }
        public Aircraft3D aircraft3D(int INTA){
            return (Aircraft3D)elementAt(INTA);
        }
        public Gun gun(int INTA){
            return (Gun)elementAt(INTA);
        }
        public PowerUp3D powerup3D(int INTA){
            return (PowerUp3D)elementAt(INTA);
        }
    }
    class Object3D{
        Point3D Center;
        Shape3D Form;
        double Speed;
        double MaxSpeed;
        double Friction;
        public void collide(Point3D CollisionPoint, Object3D Collider, boolean Continue){
        }
    }
    class Obstacle3D extends Object3D{
        
        public Obstacle3D(Point3D Loc, double HAngle, Point3D Size){
            Form = new Shape3D(true, Color.white, Loc.POSX, Loc.POSY, Loc.POSZ);
            Utils3D.setPoints(Form, 'c', Size.POSX, Size.POSY, Size.POSZ);
            Center = Loc;
            Form.rotate();
        }
        public void collide(Point3D CollisionPoint, Object3D Collider, boolean Continue){
            if(Continue){
                Collider.collide(CollisionPoint, this, false);
            }
           
        }
        public Point3D checkIntersect(Shape3D Target){
            for(int i = 0; i < 8; i++){
                if(Target.rectContains(Form.ActualPoints[i], true)){
                    return Form.ActualPoints[i];
                }
            }
            return null;
        }
        public Point3D checkBaseIntersect(Shape3D Target){
            for(int i = 0; i < 8; i++, i++){
                if(Target.rectContains(Form.ActualPoints[i], false)){
                    return Form.ActualPoints[i];
                }
            }
            return null;
        }
        
        
        public void print(Graphics2D Painter, Observer Viewer){
            printShape(Form, Painter, Viewer);
        }
    }
    class Fragment3D extends Object3D{
        double XMomentum;
        double YMomentum;
        double ZMomentum;
        int Spin;
        int Duration;
        double Gravity;
        public Fragment3D(Point3D Location, double tSpeed, double tFriction, double tGravity, int tDuration){
            
            Center = new Point3D(Location.POSX, Location.POSY, Location.POSZ);
            Form = new Shape3D(false, Color.white, Center.POSX, Center.POSY, Center.POSZ);
            Speed = tSpeed;
           
            Duration = tDuration;
            Gravity = tGravity;
            Friction = tFriction;
            
        }
        public void initMomentum(){
            double INTZ = Speed * Math.sin(Form.VAngle);
            double HMagnitude = Math.cos(Form.VAngle);
            double INTX = Speed * HMagnitude * Math.cos(Form.HAngle);
            double INTY = Speed * HMagnitude * Math.sin(Form.HAngle);
            XMomentum = INTX;
            YMomentum = INTY;
            ZMomentum = INTZ;
            
        }
        public void moveVector(double mX, double mY, double mZ){
            Center.POSX = Center.POSX + XMomentum;
            Center.POSY = Center.POSY + YMomentum;
            Center.POSZ = Center.POSZ + ZMomentum;
            Form.Center.POSX = Center.POSX;
            Form.Center.POSY = Center.POSY;
            Form.Center.POSZ = Center.POSZ;
        }
        public void move(){
            Duration--;
            if(Duration <= 0){Fragments.remove(this);}
            
            moveVector(XMomentum, YMomentum, ZMomentum);
            if(Spin > 0){
                Form.increment('r', (double)Spin / 2, TurnIncrTransform[Spin]);
                Form.increment('h', (double)Spin / 2, TurnIncrTransform[Spin]);
            }else{
                Form.increment('r', (double)Spin / 2, TurnDecrTransform[-Spin]);
                Form.increment('h', (double)Spin / 2, TurnDecrTransform[-Spin]);
            }
            if(Center.POSZ < 0){
                Center.POSZ = 0;
                ZMomentum = -ZMomentum * 0.9;
                if(Spin > 0){
                    if(GUI.randomInt(0, 1) == 1){
                        Spin = Spin - 1;
                    }
                }
                if(Spin < 0){
                    if(GUI.randomInt(0, 1) == 1){
                        Spin = Spin + 1;
                    }
                }
            }
            ZMomentum = ZMomentum - Gravity;
            ZMomentum = ZMomentum * (1 - Friction);
            YMomentum = YMomentum * (1 - Friction);
            XMomentum = XMomentum * (1 - Friction);
            
            
            
        }
        public void print(Graphics2D Painter, Observer Viewer){
            printShape(Form, Painter, Viewer);
        }
    }
    class PowerUp3D extends Object3D{
        ShotType AmmoType;
        int AmmoBonus;
        int ArmorBonus;
        
        double XMomentum;
        double YMomentum;
        double ZMomentum;
        double Friction = 0.005;
        public PowerUp3D(){
            this(Utils3D.Origin);
        }
        public PowerUp3D(Point3D Position){
            Center = Position.copy();
            Form = new Shape3D(false, Color.white, Position.POSX, Position.POSY, Position.POSZ);
            Utils3D.setPoints(Form, 'c', 10, 10, 10);
            Form.finalizeLinks();
            for(int i = 0; i < Form.Points.length; i++){
                Form.Points[i].rotate(new AffineTransform(), AffineTransform.getRotateInstance(Math.atan(1 / Math.sqrt(2))), AffineTransform.getRotateInstance(Math.toRadians(45)), Form.Points[i]);
            }
        }
        public void checkCollision(Tank3D Collider){
            if(Collider.Dead){return;}
            if(Collider != Player){return;}
            if(this.Center.distanceTo(Collider.Center) < 15){
                PowerUps.remove(this);
                powerUp(Collider);
            }
        }
        public void checkCollision(Obstacle3D Collider){
            if(Collider.Form.rectContains(Center, true)){
                Point3D Diff = Center.subtract(Collider.Form.Center);;
                
            }
               
            
        }
        public void move(){
            Form.increment('h', 1, TurnIncrTransform[2]);
            Center.POSX += XMomentum;
            Center.POSY += YMomentum;
            Center.POSZ += ZMomentum;
            addFriction();
            ZMomentum -= 0.01;
            Form.Center = Center.copy();
            for(int i = 0; i < Form.ActualPoints.length; i++){
                if(Form.ActualPoints[i].POSZ < 0){
                    Center.POSZ = Center.POSZ + Math.abs(Form.ActualPoints[i].POSZ);
                    Form.Center = Center.copy();
                    ZMomentum = Math.abs(ZMomentum);
                    for(int j = 0; j < 10; j++){
                        addFriction();
                    }
                }
            }
        }
        public void addFriction(){
            XMomentum = XMomentum * (1 - Friction);
            YMomentum = YMomentum * (1 - Friction);
            ZMomentum = ZMomentum * (1 - Friction);
            XMomentum = GUI.moveToward(XMomentum, Friction * XMomentum * 0.025 / (XMomentum + YMomentum + ZMomentum), 0);
            YMomentum = GUI.moveToward(YMomentum, Friction * YMomentum * 0.025 / (XMomentum + YMomentum + ZMomentum), 0);
            ZMomentum = GUI.moveToward(ZMomentum, Friction * ZMomentum * 0.025 / (XMomentum + YMomentum + ZMomentum), 0);
            
        }
        public void addWeaponBonus(ShotType WeaponType, int Quantity){
            AmmoType = WeaponType;
            AmmoBonus = Quantity;
        }
        public void addArmorBonus(int Quantity){
            ArmorBonus = Quantity;
        }
        public void powerUp(Tank3D Target){
            collectSound();
            if(AmmoType != null){
                boolean TEMP = false;
                for(int i = 0; i < Target.Guns.size(); i++){
                    if(Target.Guns.gun(i).ShellType == AmmoType){
                        TEMP = true;
                        Target.Guns.gun(i).Ammo += AmmoBonus;
                    }
                }
                if(TEMP == false){
                    Target.Guns.add(new Gun(Target, AmmoType.StandardName, AmmoType, AmmoBonus));
                }
            }
            Target.Hitpoints = Math.min(Target.MaxHitpoints, Math.max(0, Target.Hitpoints + ArmorBonus));
        }
        public void print(Graphics2D Painter, Observer Viewer){
            printShape(Form, Painter, Viewer);
        }
    }
    class Shot3D extends Object3D{
        double XMomentum;
        double YMomentum;
        double ZMomentum;
        int Duration;
        Tank3D Launcher;
        int FragmentCount;
        int FragDurationMod;
        double Damage;
        int Impact;
        boolean Hit = false;
        int SpeedMulti;
        ShotType BaseShellType;
        
        double HomingIncrement;
        Tank3D HomingTarget;
        int HomingDelay;
        double HomingArea;
        double MaxHomingDistance;
        boolean IsHoming;
        
        char ImpactSound;
        
        public Shot3D(Tank3D tLauncher, Point3D tCenter, ShotType ShellType){
            Form = ShellType.Form.copy();
            Form.Static = false;
            Center = tCenter.copy();
            Form.Center = Center.copy();
            Launcher = tLauncher;
            Damage = ShellType.Damage;
            Impact = ShellType.Impact;
            Friction = 0;
            BaseShellType = ShellType;
            Speed = ShellType.Speed;
            MaxSpeed = Speed;
            SpeedMulti = ShellType.SpeedMulti;
            
            Duration = ShellType.Duration;
            FragmentCount = ShellType.FragmentCount;
            FragDurationMod = ShellType.FragDurationMod;
            
            HomingIncrement = ShellType.HomingIncrement;
            HomingTarget = Launcher.Order.FiringTarget;
            HomingDelay = ShellType.HomingDelay;
            HomingArea = ShellType.HomingArea;
            MaxHomingDistance = ShellType.MaxHomingDistance;
            
            if(HomingIncrement != 0){
                Form.Tint = Color.yellow;
            }else{
                Form.Tint = Color.red;
            }
            ImpactSound = ShellType.ImpactSound;
            Form.setAngles(-tLauncher.BarrelForm.HAngle, tLauncher.BarrelForm.VAngle, tLauncher.BarrelForm.RAngle);
            initMomentum();
            
            
        }
        
        public Shot3D(Tank3D tLauncher, Point3D tCenter, int tFragments, double tSpeed, double tDamage, int tImpact){
            Damage = tDamage;
            Impact = tImpact;
            FragmentCount = tFragments;
            Launcher = tLauncher;
            Form = new Shape3D(false, Color.red, tCenter.POSX, tCenter.POSY, tCenter.POSZ);
            Form.setAngles(Launcher.BarrelForm.HAngle, Launcher.BarrelForm.VAngle, 0);
            Center = new Point3D(tCenter.POSX, tCenter.POSY, tCenter.POSZ);
            Friction = 0;
            MaxSpeed = tSpeed;
            Speed = tSpeed;
            Duration = 100;
            initMomentum();
        }
        public boolean checkIntersect(Shape3D Target){
       
            if(Target.rectContains(Center, true)){
                return true;
            }else{
                return false;
            }
        }
        
        public void remove(Object3D Target){
            Shots.remove(this);
            Hit = true;

   
            if(Target == Player){
                
                firingSound(ImpactSound);
                firingSound(ImpactSound);
            }else if(User.POS.distanceTo(Center) < 500){
                firingSound(ImpactSound);
            }else if(User.POS.distanceTo(Center) < 1000){
                if(Character.isLowerCase(ImpactSound)){
                }else{
                    firingSound(Character.toLowerCase(ImpactSound));
                }
            }
            
            boolean HitTarget;
            Color OtherColor;
            if(Target == null){
                OtherColor = Color.red;
                HitTarget = false;
            }else{
                OtherColor = Target.Form.Tint;
                HitTarget = true;
            } 
                
            for(int i = 0; i < FragmentCount / (HitTarget ? 1 : 2); i++){
                double NewSpeed;
                if(HitTarget){
                    NewSpeed = Speed * 1 * (Math.random() + 0.5);
                }else{
                    NewSpeed = Speed * 0.75 * (Math.random() / 2 + 0.5);
                }
                Fragment3D Fragment = new Fragment3D(Center, NewSpeed, 0.01, 0.04, (int)(FragDurationMod + 100 + 100 * ((double)i / FragmentCount)) + GUI.randomInt(-10, 10));
                Fragment.Spin = GUI.randomInt(-10, 10);
                Fragment.Form.setAngles(GUI.randomInt(0, 360), GUI.randomInt(0, 360), GUI.randomInt(0, 360));
                Fragment.initMomentum();
                if(!HitTarget){
                    Fragment.XMomentum += this.XMomentum / 2;
                    Fragment.YMomentum += this.YMomentum / 2;
                    Fragment.ZMomentum += this.ZMomentum / 2;
                }
                switch(GUI.randomInt(1, 3)){
                    case 1: Fragment.Form.Tint = OtherColor; break;
                    case 2: Fragment.Form.Tint = Color.red; break;
                    case 3: Fragment.Form.Tint = Form.Tint; break;
                }
                    
                    
                double Size = Math.random() + 0.5;
                switch(GUI.randomInt(1, 5)){
                    
                    case 1: Utils3D.setPoints(Fragment.Form, 'c', Size, Size, Size); break;
                    case 2: case 3:Utils3D.setPoints(Fragment.Form, 't', Size * 1.2, Size * 1.2, Size * 1.2); break;
                    case 4: case 5:Utils3D.setPoints(Fragment.Form, 'p', Size * 1.4, 1, 0); break;
                }
                Utils3D.center(Fragment.Form, 'p');
                Fragments.add(Fragment);
            } 
            
        }
        public void checkCollision(){
            
            for(int i = 0; i < Tanks.size(); i++){
        
                if(Tanks.tank3D(i) == Launcher){continue;}
                if (Tanks.tank3D(i).checkIntersect(this)){
                    remove(Tanks.tank3D(i));
                    Tanks.tank3D(i).hit(this);
                    
                }
            }
            for(int i = 0; i < Obstacles.size(); i++){
                if(checkIntersect(Obstacles.obstacle3D(i).Form)){
                    remove(Obstacles.obstacle3D(i));
                    
                }
            }
        }
        public void move(){
            boolean Homing = IsHoming;
            if(HomingIncrement != 0 && HomingDelay == 0 && Duration != BaseShellType.Duration){
                home();
            }
            if(!Homing && IsHoming){
                playHoming();
            }
            HomingDelay = Math.max(HomingDelay - 1, 0);
            for(int k = 0; k < SpeedMulti; k++){
                Duration--;
                if(Duration <= 0){
                    remove(null);
                    return;
                }
                if(Center.POSZ < 0){
                    remove(null);
                    return;
                }
                if(!Hit){
                    checkCollision();
                }
                moveVector();
                if(k % 3 == 2){
                    print(Area.Painter, User);
                }
            }
           
            
            
            
            //System.out.\(Form.HAngle + "\t" + Form.VAngle);
            
        }
        public void home(){
            if(HomingTarget == null){
                return;
            }
            if(HomingIncrement == 0){
                return;
            }
            if(Center.distanceTo(HomingTarget.Center) > MaxHomingDistance){
                return;
            }
            Point3D MoveVector = new Point3D(XMomentum, YMomentum, ZMomentum);
            Point3D TargetVector = HomingTarget.Center.subtract(Center);
            double MDT = MoveVector.dotProduct(TargetVector);
            
            //double Angle = Math.toDegrees(Math.acos(MDT / Math.sqrt(MoveVector.dotProduct(MoveVector)) / Math.sqrt(TargetVector.dotProduct(TargetVector))));
            double Angle = Math.toDegrees(Math.acos(MDT / Speed / Math.sqrt(TargetVector.dotProduct(TargetVector))));
            //System.out.println(MoveVector.dotProduct(MoveVector) + "\t" + Speed * Speed);
            //System.out.println(Angle);
            if(Angle > HomingArea){
                return;
            }
            if(Angle < HomingIncrement / 2){
                return;
            }
            IsHoming = true; 
            
            Point3D TurningVector = TargetVector.subtract( MoveVector.multiply( MDT / (Speed * Speed) ) );
            MoveVector = MoveVector.multiply(Math.cos(Math.toRadians(HomingIncrement))).add(TurningVector.divide(Math.sqrt(TurningVector.dotProduct(TurningVector))).multiply(Speed).multiply(Math.sin(Math.toRadians(HomingIncrement))));
            double Normalize = Speed / Math.sqrt(MoveVector.dotProduct(MoveVector));
           
                XMomentum = MoveVector.POSX * Normalize;
                YMomentum = MoveVector.POSY * Normalize;
                ZMomentum = MoveVector.POSZ * Normalize;
           
            Form.setAngles(Math.toDegrees(Math.atan2(MoveVector.POSY, MoveVector.POSX)), Math.toDegrees(Math.atan2(MoveVector.POSZ,  Math.sqrt(MoveVector.POSX * MoveVector.POSX + MoveVector.POSY * MoveVector.POSY))), Form.RAngle);
            
            
        }
        
        public void initMomentum(){
            double INTZ = Speed * Math.sin(Math.toRadians(Form.VAngle));
            double HMagnitude = Math.cos(Math.toRadians(Form.VAngle));
            double INTX = Speed * HMagnitude * Math.cos(Math.toRadians(Form.HAngle));
            double INTY = Speed * HMagnitude * Math.sin(Math.toRadians(Form.HAngle));
            XMomentum = INTX;
            YMomentum = INTY;
            ZMomentum = INTZ;
            
        }
        public void moveVector(){
            Center.moveVector(XMomentum, YMomentum, ZMomentum, Center);
            Form.Center.POSX = Center.POSX;
            Form.Center.POSY = Center.POSY;
            Form.Center.POSZ = Center.POSZ;
            
        }
        public void print(Graphics2D Painter, Observer Viewer){
            printShape(Form, Painter, Viewer);
        }
    }
    class Aircraft3D extends Tank3D{
        Shape3D TailForm;
        float BL = 8;
        float BW = 6;
        float TL = 12;
        float TW = 2;
        float RL = 36;
        float RW = 3;
        

        
        public Aircraft3D(String tName, Point3D Loc, int tAILevel, char Config){
            MinBarrelHeight = -45;
            MaxBarrelHeight = 30;
            DamageMod = 0.5;
            Name = tName;
            AILevel = tAILevel;
            MaxSpeed = 1.5;
            Friction = MoveIncrement / 12;
            
            initForm(Loc);
            initGuns(Config);
            Flying = true;
            Hitpoints = 6;
            MaxHitpoints = 6;
            MinIdealDistance = 100;
            MaxIdealDistance = 200;
            
            
            Order = new Objective(this);
            Order.askToHold();
            
            
            
        }
        public Point3D[][] getFiringHole(char Switch){
            switch(Switch){
                case 'g':{
                    Point3D[][] FiringHole = {{BarrelForm.ActualPoints[5], BarrelForm.ActualPoints[7]}};
                    return FiringHole;}
                case 'l':{
                    Point3D[][] FiringHole = {{TurretForm.ActualPoints[5], TurretForm.ActualPoints[7]}};
                    return FiringHole;}
                case 's':{
                    Point3D[][] FiringHole = {{TurretForm.ActualPoints[1], TurretForm.ActualPoints[2]}};
                    return FiringHole;}
            }
            return null;
        }
        public void initGuns(char Config){
            switch(Config){
                case 'a':
                    Guns.add(new Gun(this, "Gattling Gun", HeliShot, -1));
                    Guns.add(new Gun(this, "Cannon", WeakShot, 4));
                    break;
                case 'b':
                    Guns.add(new Gun(this, "Gattling Gun", HeliShot, -1));
                    Guns.add(new Gun(this, "Homing Rocket", HomeShot, 2));
                    break;
                case 'c':
                    Guns.add(new Gun(this, "Gattling Gun", HeliShot, -1));
                    Guns.add(new Gun(this, "Homing Rockets", AAirShot, 24));
                    break;
            }
        }
        public void initForm(Point3D Loc){
            Center = new Point3D(Loc.POSX, Loc.POSY, Loc.POSZ);
            Form = new Shape3D(false, Color.white, Loc.POSX, Loc.POSY, Loc.POSZ);
            TurretForm = new Shape3D(false, Color.white, Loc.POSX, Loc.POSY, Loc.POSZ);
            BarrelForm = new Shape3D(false, Color.white, TurretForm.Center.POSX, TurretForm.Center.POSY, TurretForm.Center.POSZ);
            TailForm = new Shape3D(false, Color.white, TurretForm.Center.POSX, TurretForm.Center.POSY, TurretForm.Center.POSZ);
            
            Utils3D.setPoints(Form, 't', RL, RW, 0.5);
          
            for(int i = 0; i < Form.Points.length; i++){   
                
                Form.Points[i].rotate(AffineTransform.getRotateInstance(Math.toRadians(90)), TurnIncrTransform[0], TurnIncrTransform[0], Form.Points[i]);
            }
            
            
            
            Utils3D.setPoints(Form, 't', RL, RW, 0.5);
            Utils3D.setPoints(Form, 'p', 2, 1.5, 0);
            Utils3D.setPoints(TurretForm, 'c', BL, BW, BW);
            Utils3D.setPoints(TailForm, 't', TL, TW, TW);
            Utils3D.setPoints(BarrelForm, 'c', 2, 1.5, 1.5);
            for(int i = Form.Points.length - 5; i < Form.Points.length; i++){
                Form.Points[i].POSZ = Form.Points[i].POSZ - 1;
            }
            for(int i = 0; i < Form.Points.length; i++){
                Form.Points[i].POSZ = Form.Points[i].POSZ - 0.4;
            }
            for(int i = 0; i < TailForm.Points.length; i++){
                TailForm.Points[i].POSX = TailForm.Points[i].POSX - BL / 2 - TL / 2;
            }
            for(int i = 0; i < BarrelForm.Points.length; i++){
                BarrelForm.Points[i].POSX = BarrelForm.Points[i].POSX + BL / 2;
            }
        }
        
        public void makeFragments(){
            for(int i = 0; i < 5; i++){
                
                Fragment3D Fragment = new Fragment3D(Center, Math.random() * 6, 0.015, 0.03, GUI.randomInt(300, 350));
                Fragment.Spin = GUI.randomInt(-10, 10);
                Fragment.Form.setAngles(GUI.randomInt(0, 360), GUI.randomInt(0, 360), GUI.randomInt(0, 360));
                Fragment.initMomentum();
                addTanksMomentum(Fragment);
               
                Fragment.Form.Tint = (Form.Tint);
                switch(i){
                    case 0: Fragment.Form = BarrelForm; break;
                    case 1: Fragment.Form = TurretForm; break;
                    case 2: Fragment.Form = TailForm; break;
                    case 3: Fragment.Form = Form; break;
                    case 4: Utils3D.setPoints(Fragment.Form, 'c', BL / 2, BW, BH); break;
                    
                }
                Utils3D.center(Fragment.Form, 'p');
                Fragments.add(Fragment);
            }
        }
        
        public Point3D checkCollision(Shape3D Target){
            return checkBaseIntersect(Target);
        }
        public boolean checkRotorHit(Shot3D Shot){
            
            if(Shot.Center.distanceToSquare(Form.Center) < RL * RL / 2 / 2 && Math.abs(Shot.Center.POSZ - Form.Center.POSZ) < 1){
                return true;
            }
            return false;
        }
        public boolean checkIntersect(Shot3D Shot){
            if(checkRotorHit(Shot)){
                return true;
            }
            
            
            if(TurretForm.rectContains(Shot.Center, true)){
                return true;
            }
            return false;
            
        }
        public Point3D checkBaseIntersect(Shape3D Target){    
            double Distance = (Target.MaxDistance + TurretForm.MaxDistance);
            if(TurretForm.Center.distanceToSquare(Target.Center) > Distance * Distance){return null;}
            for(int i = 0; i < 8; i++, i++){
                if(Target.rectContains(TurretForm.ActualPoints[i], true)){
                    return TurretForm.ActualPoints[i];
                }
            }
            return null;
        }
        public Point3D checkIntersect(Shape3D Target){    
            double Distance = (Target.MaxDistance + TurretForm.MaxDistance);
            if(TurretForm.Center.distanceToSquare(Target.Center) > Distance * Distance){return null;}
            for(int i = 0; i < 8; i++){

                if(Target.rectContains(TurretForm.ActualPoints[i], true)){
                    return TurretForm.ActualPoints[i];
                }
            }
            return null;
        }
        
        public void controls(){
            if(Controls.Forward){  Speed = Speed + MoveIncrement;}
            if(Controls.Back){  Speed = Speed - MoveIncrement;}
            Speed = Math.max(Math.min(Speed, MaxSpeed), -MaxSpeed);
            if(Controls.Left){  Form.increment('h', 1, TurnDecrTransform[2]); TailForm.increment('h', 1, TurnDecrTransform[2]); TurretForm.increment('h', -1, TurnDecrTransform[2]); BarrelForm.increment('h', -1, TurnDecrTransform[2]); Controls.TargetHAngle = Controls.TargetHAngle + 1;}
            if(Controls.Right){  Form.increment('h', -1, TurnIncrTransform[2]); TailForm.increment('h', -1, TurnIncrTransform[2]); TurretForm.increment('h', 1, TurnIncrTransform[2]); BarrelForm.increment('h', 1, TurnIncrTransform[2]); Controls.TargetHAngle = Controls.TargetHAngle - 1;}
            
            for(int i = 0; i < 2; i++){
                double MaxDiff;
                AffineTransform IncrTransform;
                AffineTransform DecrTransform;
                if(i == 0){
                    MaxDiff = 0.5;
                    IncrTransform = TurnIncrTransform[2];
                    DecrTransform = TurnDecrTransform[2];
                }else{
                    MaxDiff = 0.25;
                    IncrTransform = TurnIncrTransform[1];
                    DecrTransform = TurnDecrTransform[1];
                }
                double AngleDiff = 2 * MaxDiff;
                Controls.TLeft = false;
                Controls.TRight = false;
                Controls.TUp = false;
                Controls.TDown = false;
                if(Math.abs(GUI.angleDiff(Controls.TargetHAngle, TurretForm.HAngle)) > MaxDiff){
                    if(GUI.angleDiff(Controls.TargetHAngle, TurretForm.HAngle) < 0){
                        Controls.TLeft = true;
                    }else{
                        Controls.TRight = true;
                    }
                }
                if(Math.abs(GUI.angleDiff(Controls.TargetVAngle, BarrelForm.VAngle)) > MaxDiff){
                    if(GUI.angleDiff(Controls.TargetVAngle, BarrelForm.VAngle) < 0){
            
                        Controls.TUp = true;
                    }else{
                
                        Controls.TDown = true;
                    }
                }
                if(Controls.TLeft && Controls.TRight){
                }else if(Controls.TLeft){
                    TurretForm.increment('h', -AngleDiff, DecrTransform);
                    BarrelForm.increment('h', -AngleDiff, DecrTransform);
                    TailForm.increment('h', -AngleDiff, DecrTransform);
                }else if(Controls.TRight){
                    TurretForm.increment('h', AngleDiff, IncrTransform);
                    BarrelForm.increment('h', AngleDiff, IncrTransform);
                    TailForm.increment('h', AngleDiff, IncrTransform);
                }
                
                
                if(Controls.TUp && BarrelForm.VAngle < MaxBarrelHeight - AngleDiff){
                    BarrelForm.increment('v', -AngleDiff, IncrTransform);
                }
                if(Controls.TDown && BarrelForm.VAngle > MinBarrelHeight + AngleDiff){
                    BarrelForm.increment('v', AngleDiff, DecrTransform);
                }
            }
        }
        public void spinRotor(){
          
            for(int i = 0; i < Form.Points.length; i++){
                if(Speed > 0){
                    Form.Points[i].rotate(TurnIncrTransform[15 + (int)(Math.min(10, Speed * 50 / 8))], TurnIncrTransform[0], TurnIncrTransform[0], Form.Points[i]);
                }else{
                    Form.Points[i].rotate(TurnDecrTransform[15 + (int)(Math.min(10, -Speed * 50 / 8))], TurnIncrTransform[0], TurnIncrTransform[0], Form.Points[i]);
                }
                
            }
        }
        public void move(){
            decrementCounters();
            doAI();
            spinRotor();
            
            
            controls();
            if(Speed > 0){Speed = Math.max(0, Speed - Friction);}
            if(Speed < 0){Speed = Math.min(0, Speed + Friction);}
            
            if(XMomentum > 0){XMomentum = Math.max(0, XMomentum - 0.001);}
            else if(XMomentum < 0){XMomentum = Math.min(0, XMomentum + 0.001);}
            if(YMomentum > 0){YMomentum = Math.max(0, YMomentum - 0.001);}
            else if(YMomentum < 0){YMomentum = Math.min(0, YMomentum + 0.001);}
            
            XMomentum = XMomentum * (1 - 2 * Friction);
            YMomentum = YMomentum * (1 - 2 * Friction);
            moveDirection();
            
        }
        public void moveDirection(){
            Center.moveDirection(Form.HAngle, Form.VAngle, Speed, Center);
            Center.moveVector(XMomentum, YMomentum, 0, Center);
            Form.Center.POSX = Center.POSX;
            Form.Center.POSY = Center.POSY;
            Form.Center.POSZ = Center.POSZ + BW / 2 + TW + 0.5;
            TurretForm.Center.POSX = Center.POSX;
            TurretForm.Center.POSY = Center.POSY;
            TurretForm.Center.POSZ = Center.POSZ;
            BarrelForm.Center.POSX = TurretForm.Center.POSX;
            BarrelForm.Center.POSY = TurretForm.Center.POSY;
            BarrelForm.Center.POSZ = TurretForm.Center.POSZ;
            TailForm.Center.POSX = TurretForm.Center.POSX;
            TailForm.Center.POSY = TurretForm.Center.POSY;
            TailForm.Center.POSZ = TurretForm.Center.POSZ;
        }
        public void print(Graphics2D Painter, Observer Viewer){
            printShape(Form, Painter, Viewer);
            printShape(TurretForm, Painter, Viewer);
            printShape(BarrelForm, Painter, Viewer);
            printShape(TailForm, Painter, Viewer);
        }
    }
    class MammothTank3D extends Tank3D{
        float BH = 4.5f;
        float BL = 23;
        float BW = 18;
        float TH = 4;
        float TW = 12;
        float RL = 15;
        float RW = 1.25f;
        public MammothTank3D(String tName, Point3D Loc, int tAILevel, char Config){
            MaxHitpoints = 10;
            Hitpoints = 10;
            Name = tName;
            AILevel = tAILevel;
            MaxSpeed = 0.7;
            Friction = MoveIncrement / 5;
            initForm(Loc);
            initMaxDistance();
            initGuns(Config);
            Order = new Objective(this);
            
            Order.askToHold();
        }
        public Point3D[][] getFiringHole(char Switch){
            
            switch(Switch){
                case 'g':{
                    Point3D[][] FiringHole = {{BarrelForm.ActualPoints[5], BarrelForm.ActualPoints[7]}, {BarrelForm.ActualPoints[13], BarrelForm.ActualPoints[15]}};
                    return FiringHole;}
                case 'l':{
                    Point3D[][] FiringHole = {{TurretForm.ActualPoints[1], TurretForm.ActualPoints[2]}};
                    return FiringHole;}
                case 's':{
                    Point3D[][] FiringHole = {{TurretForm.ActualPoints[1], TurretForm.ActualPoints[2]}};
                    return FiringHole;}
            }
            return null;
            
        }
        public void initGuns(char Config){
            switch(Config){
                
                case 'a': 
                    Guns.add(new Gun(this, "Main Guns", WeakShot, -1));
                    break;
            }
        }
        public void initForm(Point3D Loc){
            Center = new Point3D(Loc.POSX, Loc.POSY, Loc.POSZ + BH / 2);
            Form = new Shape3D(false, Color.white, Loc.POSX, Loc.POSY, Loc.POSZ + BH / 2);
            TurretForm = new Shape3D(false, Color.white, Loc.POSX + 2, Loc.POSY, Loc.POSZ + TH / 2 + BH / 2);
            BarrelForm = new Shape3D(false, Color.white, TurretForm.Center.POSX, TurretForm.Center.POSY, TurretForm.Center.POSZ + BH / 2 + TH / 2);
            Utils3D.setPoints(Form, 'c',  BL, BW, BH);
            Utils3D.setPoints(TurretForm, 'c', TW, TW, TH);
            Utils3D.setPoints(BarrelForm, 'c', RL, RW, RW);
            Utils3D.setPoints(BarrelForm, 'c', RL, RW, RW);
            for(int i = 0; i < BarrelForm.Points.length; i++){
                BarrelForm.Points[i].moveVector(RL / 2 + TW / 2, 0, 0, BarrelForm.Points[i]);
            }
            for(int i = 0; i < BarrelForm.Points.length / 2; i++){
                BarrelForm.Points[i].moveVector(0, TW / 5, 0, BarrelForm.Points[i]);
                BarrelForm.Points[i + BarrelForm.Points.length / 2].moveVector(0, -TW / 5, 0, BarrelForm.Points[i + BarrelForm.Points.length / 2]);
            }
        }
      
        public Point3D[] getFiringHoles(){
            Point3D HoleA = BarrelForm.ActualPoints[5];
            Point3D HoleB = BarrelForm.ActualPoints[7];
            Point3D[] FiringHoles = new Point3D[2];
            FiringHoles[0] = HoleA.add(HoleB).divide(2);
            HoleA = BarrelForm.ActualPoints[5 + 8];
            HoleB = BarrelForm.ActualPoints[7 + 8];
            FiringHoles[1] = HoleA.add(HoleB).divide(2);;
            return FiringHoles;
        }
        public void makeFragments(){
            for(int i = 0; i < 8; i++){
                
                Fragment3D Fragment = new Fragment3D(Center, Math.random() * 5, 0.015, 0.03, GUI.randomInt(300, 350));
                Fragment.Spin = GUI.randomInt(-10, 10);
                Fragment.Form.setAngles(GUI.randomInt(0, 360), GUI.randomInt(0, 360), GUI.randomInt(0, 360));
                Fragment.initMomentum();
                addTanksMomentum(Fragment);
               
                Fragment.Form.Tint = (Form.Tint);
                switch(i){
                    case 0: case 1: Utils3D.setPoints(Fragment.Form, 'c', BarrelForm.ArgA, BarrelForm.ArgB, BarrelForm.ArgC); break;
                    case 2: Fragment.Form = TurretForm; break;
                    case 3: case 4: Utils3D.setPoints(Fragment.Form, 'c', BL / 2, BW, BH); break;
                    case 5: case 6: Utils3D.setPoints(Fragment.Form, (i == 5 ? 't' : 'p'), GUI.randomInt(2, 5), GUI.randomInt(2, 5), GUI.randomInt(2, 5)); break;
                    case 7: Utils3D.setPoints(Fragment.Form, 'c', 7, 8, 9); break;
                }
                Utils3D.center(Fragment.Form, 'p');
                Fragments.add(Fragment);
            }
        }
       
    }
    
    class Objective{
        Tank3D FiringTarget;
        int FiringTargetPriority;
        
        Tank3D MovingTarget;
        Point2D.Double TargetOffset;
        boolean StopAtTarget;
        boolean OffensiveMovement;
        
        
        Tank3D Owner;
        
        
        
        public Objective(Tank3D tOwner){
            Owner = tOwner;
            TargetOffset = new Point2D.Double(0, 0);
        }
        public void askToAttack(Tank3D TargetTank){
            TargetOffset = new Point2D.Double(0, 0);
            FiringTargetPriority = 0;
            OffensiveMovement = true;
            StopAtTarget = false;
            FiringTarget = TargetTank;
            MovingTarget = TargetTank;
        }
        public void setToAttack(Tank3D TargetTank){
            
            if(FiringTargetPriority == 0){
                FiringTarget = TargetTank; 
                FiringTargetPriority = 750;
                
            }else if(FiringTargetPriority == 750){
                TargetOffset = new Point2D.Double(0, 0);
                MovingTarget = FiringTarget;
                OffensiveMovement = true;
                StopAtTarget = false;
                FiringTargetPriority = 20000;
            }
        }
        public void setToFollow(Tank3D TargetTank){
            StopAtTarget = true;
            TargetOffset = new Point2D.Double(GUI.randomInt(90, 270), Owner.IdealDistance * 0.75);
            MovingTarget = TargetTank;
            OffensiveMovement = false;
            FiringTargetPriority = 0;
        }
        public void clear(){
            TargetOffset = new Point2D.Double(0, 0);
            StopAtTarget = false;
            TargetOffset = null;
            MovingTarget = null;
            FiringTarget = null;
            OffensiveMovement = false;
        }
        public void setToHold(){
            if(MovingTarget == Owner){
                StopAtTarget = true;
                TargetOffset = new Point2D.Double(0, 0);
                FiringTargetPriority = 0;
            }else{
                FiringTargetPriority = 0;
                TargetOffset = new Point2D.Double(90, Owner.IdealDistance / 2);
                MovingTarget = Owner;
                StopAtTarget = false;
                OffensiveMovement = false;
            }
        }
        public void askToHold(){
            MovingTarget = Owner;
            StopAtTarget = true;
            OffensiveMovement = true;
            TargetOffset = new Point2D.Double(0, 0);
            FiringTargetPriority = 0;
        }
    }
    class Tank3D extends Object3D{
        class Commands{
            boolean Forward;
            boolean Back;
            boolean Left;
            boolean Right;
            boolean TUp;
            boolean TDown;
            boolean TLeft;
            boolean TRight;
            double TargetHAngle;
            double TargetVAngle;
        }
        Shape3D TurretForm;
        Shape3D BarrelForm;
     
        
        GeneralVector Allies;
        GeneralVector Targets;
        
        Commands Controls = new Commands();
        float BH = 4;
        float BL = 20;
        float BW = 15;
        float TH = 4;
        float TW = 9;
        float RL = 12;
        float RW = 1;
        float MinBarrelHeight = -15;
        float MaxBarrelHeight = 75;
        float MaxDistance;
        
        
        String Name;
        
        GeneralVector Guns = new GeneralVector();
        Gun CurrentGun;
        double DamageMod = -0.1;
        
        boolean Flying = false;
        double Hitpoints = 6;
        double MaxHitpoints = 6;
        boolean Dead = false;
        double XMomentum;
        double YMomentum;
        
        PowerUp3D Loot;
        
        int IdealDistance = 200;
        int MinIdealDistance = 150;
        int MaxIdealDistance = 300;
        int Orientation = 1;
        int ReverseCounter = 0;
        int StopCounter = 0;
        int AILevel;
        double RandomSpeedOffset;

        
        Objective Order;
        
        
        float HRandOffset;
        float VRandOffset;
        
        
        
        public Tank3D(){}
       
        public Tank3D(String tName, Point3D Loc, int tAILevel, char Config){
            
            Name = tName;
            AILevel = tAILevel;
            MaxSpeed = 0.9;
            Friction = MoveIncrement / 5;
            initForm(Loc);
            initMaxDistance();
            initGuns(Config);
            
            Order = new Objective(this);
            Order.askToHold();
            
        }
        public void addLoot(PowerUp3D lewt){
            Loot = lewt;
        }
        public void initGuns(char Config){
            
            Point3D[][] FiringHole = {{BarrelForm.ActualPoints[5], BarrelForm.ActualPoints[7]}};
            switch(Config){
                case 'a':
                Guns.add(new Gun(this, "Main Gun", TankShot, -1));
                Guns.add(new Gun(this, "Gattling Gun", MiniShot, -1));
                break;
                case 'b':
                Guns.add(new Gun(this, "Main Gun", WeakShot, -1));
                Guns.add(new Gun(this, "Gattling Gun", HeliShot, -1));
                break;
                case 's':
                Guns.add(new Gun(this, "Main Gun", TankShot, -1));
                Guns.add(new Gun(this, "Gattling Gun", HeliShot, -1));
                break;
            }
            CurrentGun = Guns.gun(0);
        }
        public void initForm(Point3D Loc){
            Center = new Point3D(Loc.POSX, Loc.POSY, Loc.POSZ + BH / 2);
            Form = new Shape3D(false, Color.white, Loc.POSX, Loc.POSY, Loc.POSZ + BH / 2);
            TurretForm = new Shape3D(false, Color.white, Loc.POSX, Loc.POSY, Loc.POSZ + TH / 2 + BH / 2);
            BarrelForm = new Shape3D(false, Color.white, TurretForm.Center.POSX, TurretForm.Center.POSY, TurretForm.Center.POSZ + BH / 2 + TH / 2);
            Utils3D.setPoints(Form, 'c',  BL, BW, BH);
            Utils3D.setPoints(TurretForm, 'c', TW, TW, TH);
            Utils3D.setPoints(BarrelForm, 'c', RL, RW, RW);
            for(int i = 0; i < BarrelForm.Points.length; i++){
                BarrelForm.Points[i].moveVector(RL / 2 + TW / 2, 0, 0, BarrelForm.Points[i]);
            }
        }
        public void initMaxDistance(){
            for(int i = 0; i < Form.Points.length; i++){
                MaxDistance = Math.max(MaxDistance, (float)Form.Points[i].distanceTo(Center));
            }
            for(int i = 0; i < TurretForm.Points.length; i++){
                MaxDistance = Math.max(MaxDistance, (float)TurretForm.Points[i].distanceTo(Center));
            }
        }
        public Point3D checkCollision(Shape3D Target){
            return checkBaseIntersect(Target);
        }
        public boolean checkIntersect(Shot3D Shot){
            if(Shot.Center.distanceToSquare(Form.Center) > MaxDistance * MaxDistance){return false;}
            if(Form.rectContains(Shot.Center, true)){
                return true;
            }
            if(TurretForm.rectContains(Shot.Center, true)){
                return true;
            }
            return false;
            
        }
        public Point3D checkBaseIntersect(Shape3D Target){
            double Distance = (Target.MaxDistance + MaxDistance);
            if(Form.Center.distanceToSquare(Target.Center) > Distance * Distance){return null;}
            for(int i = 0; i < 8; i++, i++){

                if(Target.rectContains(Form.ActualPoints[i], false)){
                    return Form.ActualPoints[i];
                }
            }
            return null;
        }
        public Point3D checkIntersect(Shape3D Target){
            double Distance = (Target.MaxDistance + MaxDistance);
            if(Form.Center.distanceToSquare(Target.Center) > Distance * Distance){return null;}
            for(int i = 0; i < 8; i++){
                if(Target.rectContains(Form.ActualPoints[i], true)){
                    return Form.ActualPoints[i];
                }else if(Target.rectContains(TurretForm.ActualPoints[i], true)){
                    return TurretForm.ActualPoints[i];
                }
            }
            return null;
        }
        
        public void collide(Point3D CollisionPoint, Object3D Collider, boolean Continue){
            if(this.Form.Center.distanceTo(Player.Form.Center) < 200 && CollideShakeCounter < 2){
                collideSound();
            }
            if(this == Player){ 
                collideSound();
            }
            if(Order.TargetOffset != null){
                Order.TargetOffset.x = GUI.randomInt(90, 270);
            }
            Orientation = (GUI.randomInt(0, 1) == 0 ? -1 : 1);
            if(ReverseCounter <= 0 ){
                ReverseCounter = 200;
            }else if(ReverseCounter > 0){
                ReverseCounter = -200;
            }
            if(Continue){
                Collider.collide(CollisionPoint, this, false);
            }
            double XMovement = 0.2 * Math.abs(Speed * Math.cos(Form.HAngle) - Collider.Speed * Math.cos(Collider.Form.HAngle));
            double YMovement = 0.2 * Math.abs(Speed * Math.sin(Form.HAngle) - Collider.Speed * Math.sin(Collider.Form.HAngle));
            
            double Angle = Math.atan2(-Collider.Center.POSY + CollisionPoint.POSY, -Collider.Center.POSX + CollisionPoint.POSX);
            XMomentum = XMomentum * 0.8;
            YMomentum = YMomentum * 0.8;
            XMomentum = XMomentum + 0.25 * (Math.cos(Angle));
            YMomentum = YMomentum + 0.25 * (Math.sin(Angle));
            
            if(Center.POSX < Collider.Center.POSX){
                Center.POSX = Center.POSX - XMovement;
            }else if(Center.POSX > Collider.Center.POSX){
                Center.POSX = Center.POSX + XMovement;
            }else if(Center.POSY < Collider.Center.POSY){
                Center.POSY = Center.POSY - YMovement;
            }else if(Center.POSY > Collider.Center.POSY){
                Center.POSY = Center.POSY + YMovement;
            }
            Speed = Speed / 2;
            IdealDistance = GUI.randomInt(MinIdealDistance, MaxIdealDistance);
        }
        public void hit(Shot3D Impacter){
            Orientation = (GUI.randomInt(0, 1) == 0 ? -1 : 1);
            IdealDistance = GUI.randomInt(MinIdealDistance, MaxIdealDistance);
            Hitpoints -= Math.max(Impacter.Damage + DamageMod, 0);
            
            
            resetRandomOffset(Impacter.Impact);
            XMomentum = XMomentum + Impacter.XMomentum / 5 * Impacter.Damage;
            YMomentum = YMomentum + Impacter.YMomentum / 5 * Impacter.Damage;
            if(Hitpoints <= 0){
                explode();
            }
            if(this == Player){
                shakeCamera((int)(16 * Impacter.Damage), Impacter.Impact);
            }
        }
        public void dropLoot(){
            if(Loot != null){
                Loot.Center = Center.copy();
                
                PowerUps.add(Loot);
                
            }
        }
        public void makeFragments(){
            for(int i = 0; i < 5; i++){
                
                Fragment3D Fragment = new Fragment3D(Center, Math.random() * 5, 0.015, 0.03, GUI.randomInt(300, 350));
                Fragment.Spin = GUI.randomInt(-10, 10);
                Fragment.Form.setAngles(GUI.randomInt(0, 360), GUI.randomInt(0, 360), GUI.randomInt(0, 360));
                Fragment.initMomentum();
                addTanksMomentum(Fragment);
               
                Fragment.Form.Tint = (Form.Tint);
                switch(i){
                    case 0: Fragment.Form = BarrelForm; break;
                    case 1: Fragment.Form = TurretForm; break;
                    case 2: case 3: Utils3D.setPoints(Fragment.Form, 'c', BL / 2, BW, BH); break;
                    case 4: Utils3D.setPoints(Fragment.Form, 't', GUI.randomInt(2, 5), GUI.randomInt(2, 5), GUI.randomInt(2, 5)); break;
                }
                Utils3D.center(Fragment.Form, 'p');
                Fragments.add(Fragment);
            }
            
        }
        public void explode(){
            dropLoot();
            tankExplode();
            Friends.remove(this);
            Enemies.remove(this);
            Dead = true;
            
            makeFragments();
            
        }
        public void resetRandomOffset(float Randomness){
            HRandOffset = (float)(Math.random() - 0.5) * Randomness;
            VRandOffset = (float)(Math.random() - 0.25) * Randomness / 2; 
            RandomSpeedOffset = Math.random() + 0.25;
       
        }
        public void setFiringDelay(Point3D TargetPoint){
            
            if(CurrentGun.ShellType == HeliShot || CurrentGun.ShellType == MiniShot || CurrentGun.ShellType == AAirShot){
                if(CurrentGun.ReloadTimer + CurrentGun.ShellType.ReloadIncrement * CurrentGun.ShellType.MinFiringDelay < CurrentGun.ShellType.ReloadDecrement){
                    CurrentGun.FiringDelay = (int)((CurrentGun.ShellType.MaxReloadTime - CurrentGun.ReloadTimer) / CurrentGun.ShellType.ReloadIncrement + GUI.randomInt(10, 25) * (4 - AILevel) + (TurretForm.Center.distanceTo(TargetPoint) / 10));
                    if(CurrentGun.ShellType == AAirShot){
                        CurrentGun.FiringDelay += 20;
                    }
                }else{
                    CurrentGun.FiringDelay = 0;
                }
            }else if(CurrentGun.ShellType == TankShot || CurrentGun.ShellType == WeakShot || CurrentGun.ShellType == HomeShot){
                CurrentGun.FiringDelay = GUI.randomInt(10, 25) * (4 - AILevel) + (int)(TurretForm.Center.distanceTo(TargetPoint) / 10);
                if(CurrentGun.ShellType == HomeShot){
                    CurrentGun.FiringDelay += 20;
                }
            }
        }
        public void findTarget(){
            Tank3D NearestTarget = null;
            
            
            for(int i = 0; i < Targets.size(); i++){
                
                   
                
                if(NearestTarget == null){
                    if(!Targets.tank3D(i).Dead){
                        NearestTarget = Targets.tank3D(i);
                    }
                }else{
                    if(Center.distanceTo(Targets.tank3D(i).Center) < Center.distanceTo(NearestTarget.Center)){
                        if(!Targets.tank3D(i).Dead){
                            NearestTarget = Targets.tank3D(i);
                        }
                    }
                }
            }
            if(NearestTarget == null){Order.askToHold(); return;}
            
                
            
            double IncumbantDistance = (Order.FiringTarget == null ? 1000000 : this.Center.distanceTo(Order.FiringTarget.Center));
            double CandidateDistance = this.Center.distanceTo(NearestTarget.Center);
            
            if(Order.FiringTarget == null || Order.FiringTarget.Dead || CandidateDistance < IncumbantDistance - Order.FiringTargetPriority){
                Order.FiringTarget = NearestTarget;
                Order.FiringTargetPriority = 0;
                if(Order.OffensiveMovement){
                    Order.askToAttack(NearestTarget);
                }
            }
            if(Order.MovingTarget == null || Order.MovingTarget.Dead){
                if(Order.FiringTarget != null && !Order.FiringTarget.Dead){
                    Order.askToAttack(NearestTarget);
                }
            }
        }
        public void chooseGun(){
            
            double Distance;
            double MaxPriority = 0;
            /*if(Allies == Friends){
                    System.out.println();
            }*/
            try{
                Distance = TurretForm.Center.distanceTo(Order.FiringTarget.Center);
                
            }catch(NullPointerException e){
                return;
            }
            for(int i = 0; i < Guns.size(); i++){
                double INTA = 0;
               
                INTA = INTA - Distance / (Guns.gun(i).ShellType.Speed * Guns.gun(i).ShellType.SpeedMulti);
               
                double INTB = (Guns.gun(i).ShellType.Damage + Order.FiringTarget.DamageMod) * ((float)Guns.gun(i).ShellType.MaxReloadTime / Guns.gun(i).ShellType.ReloadDecrement) * 10000;
             //   if(Allies == Friends)System.out.println(Guns.gun(i).ShellType.Name + "\t" + INTB);
                INTA = INTA + INTB;
                
                if(Guns.gun(i).ShellType.Range < Distance){
                    INTA = 0;
                }
                if(INTB < 3000){
                    INTA = 0;
                }
                if(Guns.gun(i).ReloadTimer < Guns.gun(i).ShellType.ReloadDecrement){
                    INTA = 0;
                }
                if(Guns.gun(i).FiringDelay != 0){
                    INTA = 0;
                }
                /*if(Allies == Friends){
                    System.out.println((Guns[i].ShellType.Damage + Order.FiringTarget.DamageMod) + "\t" + INTB);
                }*/
                if(INTA > MaxPriority){
                    MaxPriority = INTA;
                    CurrentGun = Guns.gun(i);
                }
            }
           
        }
        public Point3D[][] getFiringHole(char Switch){
            
            switch(Switch){
                case 'g':{
                    Point3D[][] FiringHole = {{BarrelForm.ActualPoints[5], BarrelForm.ActualPoints[7]}};
                    return FiringHole;}
                case 'l':{
                    Point3D[][] FiringHole = {{TurretForm.ActualPoints[1], TurretForm.ActualPoints[4], TurretForm.ActualPoints[2], TurretForm.ActualPoints[5]}};
                    return FiringHole;}
                case 's':{
                    Point3D[][] FiringHole = {{TurretForm.ActualPoints[3], TurretForm.ActualPoints[2]}};
                    return FiringHole;}
            }
            return null;
            
        }
        public void doAI(){
            
            if(this == Player){return;}
            if(AILevel < 0){return;}
            if(FrameCounter % 2 == 0){return;}
            boolean Stop = false;
            boolean Reverse = false;
            boolean Forward = false;
            boolean Brake = false;
            findTarget();
            chooseGun();
            Point2D.Double Angles = new Point2D.Double();
            //Turret
            Point3D TargetPoint = new Point3D(0, 0, 0);
       
            if(Order.FiringTarget != null && CurrentGun != null){
                double Distance = GUI.round(TurretForm.Center.distanceTo(Order.FiringTarget.Form.Center), 1.0/50);
                TargetPoint.setLocation(Order.FiringTarget.Form.Center.POSX, Order.FiringTarget.Form.Center.POSY, Order.FiringTarget.Form.Center.POSZ);
                if(AILevel == 2){
                    double BulletSpeed = CurrentGun.ShellType.Speed * CurrentGun.ShellType.SpeedMulti; 
                    TargetPoint.POSX = TargetPoint.POSX  + RandomSpeedOffset * Order.FiringTarget.Speed * Distance / BulletSpeed * Math.cos(Math.toRadians(Order.FiringTarget.Form.HAngle));
                    TargetPoint.POSY = TargetPoint.POSY + RandomSpeedOffset * Order.FiringTarget.Speed * Distance / BulletSpeed * Math.sin(Math.toRadians(Order.FiringTarget.Form.HAngle));
                }
            }
            for(int i = 0; i < Guns.size(); i++){
                if(Guns.gun(i).ReloadTimer == Guns.gun(i).ShellType.MaxReloadTime){
                    Guns.gun(i).FiringDelay = Math.max(0, Guns.gun(i).FiringDelay - 1);
                }
                
            }
            if(CurrentGun != null && CurrentGun.ReloadTimer >= CurrentGun.ShellType.ReloadDecrement){
                if(Order.FiringTarget != null){
                    Angles = TurretForm.Center.angleTo(TargetPoint);
                }else{
                    Angles.setLocation(TurretForm.HAngle, BarrelForm.VAngle);
                }

                Controls.TargetHAngle = 180-Angles.x + HRandOffset * Order.FiringTarget.Speed;
                Controls.TargetVAngle = Angles.y + VRandOffset* Order.FiringTarget.Speed;
                
                if(Math.abs(GUI.angleDiff(TurretForm.HAngle, Controls.TargetHAngle)) < 1 && Math.abs(GUI.angleDiff(BarrelForm.VAngle, Controls.TargetVAngle)) < 1 && Order.FiringTarget != null){
                    if(CurrentGun.FiringDelay == 0){
                        if(TurretForm.Center.distanceTo(Order.FiringTarget.Center) < CurrentGun.ShellType.Range && CurrentGun != null){
                            
                            CurrentGun.fire();
                            setFiringDelay(TargetPoint);
                            
                            resetRandomOffset(3 - AILevel);
                            if(AILevel == 0){
                                StopCounter = GUI.randomInt(20, 50);
                            }
                        }
                    }else{
                        
                        if(AILevel == 0 && CurrentGun.FiringDelay < 50 && CurrentGun.FiringDelay > 5){
                            Brake = true;
                        }
                        
                    }
                    Controls.TLeft = false;
                    Controls.TRight = false;
                }
                
            }
            
            
            //Movement
       
            double TargetAngle;
            
            if(!Order.StopAtTarget){
                double TotalHAngle = Math.toRadians(Order.MovingTarget.Form.HAngle + Order.TargetOffset.x);
                TargetPoint.setLocation(Order.MovingTarget.Center.POSX + Order.TargetOffset.y * Math.cos(TotalHAngle), Order.MovingTarget.Center.POSY + Order.TargetOffset.y * Math.sin(TotalHAngle), Center.POSZ);
                Angles = Center.angleTo(TargetPoint);
                
                TargetAngle = Angles.x + 180;
                double DistanceToTarget = Center.distanceTo(TargetPoint);
                
                int TempIdealDistance = IdealDistance;
                if(DistanceToTarget > TempIdealDistance){
                    TargetAngle = TargetAngle + Orientation * (90 - Math.min(Math.max((DistanceToTarget - TempIdealDistance) / 2, 0), 90));
                }else if(DistanceToTarget < TempIdealDistance){
                    TargetAngle = TargetAngle + Orientation * (90 + Math.min(Math.max(TempIdealDistance - DistanceToTarget, 0), 90));
                }
            }else{
                double TotalHAngle = Math.toRadians(Order.MovingTarget.Form.HAngle + Order.TargetOffset.x);
                TargetPoint.setLocation(Order.MovingTarget.Center.POSX + Order.TargetOffset.y * Math.cos(TotalHAngle), Order.MovingTarget.Center.POSY + Order.TargetOffset.y * Math.sin(TotalHAngle), Center.POSZ);
                Angles = Center.angleTo(TargetPoint);
                TargetAngle = Angles.x + 180;
                
                if(Center.distanceTo(TargetPoint) < 25){
                    Brake = true;
                }
            }
            
            if(ReverseCounter == 0 && StopCounter <= 0){
                Controls.Forward = true;
                Controls.Back = false;
                if(Math.abs(GUI.angleDiff(TargetAngle, Form.HAngle)) > 0.5){
                    if(GUI.angleDiff(TargetAngle, Form.HAngle) < 0){
                        Controls.Left = false;
                        Controls.Right = true;
                    }else{
                        Controls.Right = false;
                        Controls.Left = true;
                    }
                }else{
                    Controls.Right = false;
                    Controls.Left = false;
                }
                if(Stop){
                    Controls.Forward = false;
                    Controls.Back = false;
                    Controls.Left = false;
                    Controls.Right = false;
                }else if(Brake){
                    Controls.Left = false;
                    Controls.Right = false;
                    if(Speed > 0){
                        Controls.Back = true;
                        Controls.Forward = false;
                    }else{
                        Controls.Back = !true;
                        Controls.Forward = !false;
                    }
                }else if(Reverse){
                    Controls.Back = true;
                    Controls.Forward = false;
                    Controls.Left = false;
                    Controls.Right = false;
                }else if(Forward){
                    Controls.Back = false;
                    Controls.Forward = true;
                    Controls.Left = false;
                    Controls.Right = false;
                }
            }else if(StopCounter > 0){
                Controls.Back = false;
                Controls.Forward = false;
                Controls.Left = false;
                Controls.Right = false;
            }else if(ReverseCounter != 0){
             
                if(ReverseCounter > 0){
                    Controls.Back = true;
                    Controls.Forward = false;
                }else{
                    Controls.Back = false;
                    Controls.Forward = true;
                    
                }
                
                if(Math.abs(ReverseCounter) > 100 && Math.abs(ReverseCounter) < 175){
                    if(Orientation > 0){
                        Controls.Left = true;
                        Controls.Right = false;
                    }else{
                        Controls.Left = false;
                        Controls.Right = true;
                    }
                }else{
                    Controls.Left = false;
                    Controls.Right = false;
                }
                
            }
            
        }
        public void decrementCounters(){
            StopCounter = Math.max(StopCounter - 1, 0);
            if(ReverseCounter < 0){ReverseCounter++;}
            if(ReverseCounter > 0){ReverseCounter--;}
            for(int i = 0; i < Guns.size(); i++){
                Guns.gun(i).MinFiringDelayCounter = Math.max(0, Guns.gun(i).MinFiringDelayCounter - 1);
                Guns.gun(i).ReloadTimer = Math.min(Guns.gun(i).ShellType.MaxReloadTime, Guns.gun(i).ReloadTimer + Guns.gun(i).ShellType.ReloadIncrement);
            }
        }
        public void addTanksMomentum(Fragment3D Fragment){
            Fragment.XMomentum += Speed * 2 * Math.cos(Math.toRadians(Form.HAngle));
            Fragment.YMomentum += Speed * 2 * Math.sin(Math.toRadians(Form.HAngle));
            
        }
        public void move(){
         
            
            
            doAI();
            
            decrementCounters();
            
            controls();
            if(Speed > 0){Speed = Math.max(0, Speed - Friction);}
            else if(Speed < 0){Speed = Math.min(0, Speed + Friction);}
            
            XMomentum = XMomentum * (1 - 5 * Friction);
            YMomentum = YMomentum * (1 - 5 * Friction);
            
            if(XMomentum > 0){XMomentum = Math.max(0, XMomentum - 0.001);}
            else if(XMomentum < 0){XMomentum = Math.min(0, XMomentum + 0.001);}
            if(YMomentum > 0){YMomentum = Math.max(0, YMomentum - 0.001);}
            else if(YMomentum < 0){YMomentum = Math.min(0, YMomentum + 0.001);}
            
            
            moveDirection();
            
        }
        public Point3D[] getFiringHoles(){
            Point3D HoleA = BarrelForm.ActualPoints[5];
            Point3D HoleB = BarrelForm.ActualPoints[7];
            Point3D[] FiringHoles = new Point3D[1];
            FiringHoles[0] = new Point3D((HoleA.POSX + HoleB.POSX) / 2, (HoleA.POSY + HoleB.POSY) / 2, (HoleA.POSZ + HoleB.POSZ) / 2);
            return FiringHoles;
        }
        
        
        public void moveDirection(){
            Center.moveDirection(Form.HAngle, Form.VAngle, Speed, Center);
            Center.moveVector(XMomentum, YMomentum, 0, Center);
            Form.Center.POSX = Center.POSX;
            Form.Center.POSY = Center.POSY;
            Form.Center.POSZ = Center.POSZ;
            TurretForm.Center.POSX = Center.POSX;
            TurretForm.Center.POSY = Center.POSY;
            TurretForm.Center.POSZ = Center.POSZ + BH / 2  + TH / 2;
            BarrelForm.Center.POSX = TurretForm.Center.POSX;
            BarrelForm.Center.POSY = TurretForm.Center.POSY;
            BarrelForm.Center.POSZ = TurretForm.Center.POSZ;
        }
        public void controls(){
            if(Controls.Forward){  Speed = Speed + MoveIncrement;}
            if(Controls.Back){  Speed = Speed - MoveIncrement;}
            Speed = Math.max(Math.min(Speed, MaxSpeed), -MaxSpeed);
            if(Controls.Left){  Form.increment('h', 1, TurnDecrTransform[2]); TurretForm.increment('h', -1, TurnDecrTransform[2]); BarrelForm.increment('h', -1, TurnDecrTransform[2]); Controls.TargetHAngle = Controls.TargetHAngle + 1;}
            if(Controls.Right){  Form.increment('h', -1, TurnIncrTransform[2]); TurretForm.increment('h', 1, TurnIncrTransform[2]); BarrelForm.increment('h', 1, TurnIncrTransform[2]); Controls.TargetHAngle = Controls.TargetHAngle - 1;}
            
            for(int i = 0; i < 2; i++){
                double MaxDiff;
                AffineTransform IncrTransform;
                AffineTransform DecrTransform;
                if(i == 0){
                    MaxDiff = 0.5;
                    IncrTransform = TurnIncrTransform[2];
                    DecrTransform = TurnDecrTransform[2];
                }else{
                    MaxDiff = 0.25;
                    IncrTransform = TurnIncrTransform[1];
                    DecrTransform = TurnDecrTransform[1];
                }
                double AngleDiff = 2 * MaxDiff;
                Controls.TLeft = false;
                Controls.TRight = false;
                Controls.TUp = false;
                Controls.TDown = false;
                if(Math.abs(GUI.angleDiff(Controls.TargetHAngle, TurretForm.HAngle)) > MaxDiff){
                    if(GUI.angleDiff(Controls.TargetHAngle, TurretForm.HAngle) < 0){
                        Controls.TLeft = true;
                    }else{
                        Controls.TRight = true;
                    }
                }
                if(Math.abs(GUI.angleDiff(Controls.TargetVAngle, BarrelForm.VAngle)) > MaxDiff){
                    if(GUI.angleDiff(Controls.TargetVAngle, BarrelForm.VAngle) < 0){
            
                        Controls.TUp = true;
                    }else{
                
                        Controls.TDown = true;
                    }
                }
                if(Controls.TLeft && Controls.TRight){
                }else if(Controls.TLeft){
                    TurretForm.increment('h', -AngleDiff, DecrTransform);
                    BarrelForm.increment('h', -AngleDiff, DecrTransform);
                }else if(Controls.TRight){
                    TurretForm.increment('h', AngleDiff, IncrTransform);
                    BarrelForm.increment('h', AngleDiff, IncrTransform);
                }
                
                
                if(Controls.TUp && BarrelForm.VAngle < MaxBarrelHeight - AngleDiff){
                    BarrelForm.increment('v', -AngleDiff, IncrTransform);
                }
                if(Controls.TDown && BarrelForm.VAngle > MinBarrelHeight + AngleDiff){
                    BarrelForm.increment('v', AngleDiff, DecrTransform);
                }
            }
        }
        
        
        public void print(Graphics2D Painter, Observer Viewer){
            printShape(Form, Painter, Viewer);
            
            printShape(TurretForm, Painter, Viewer);
            printShape(BarrelForm, Painter, Viewer);
        }
    }
    class Gun{
        Point3D[][] FiringPoints;
        int ReloadTimer;
        ShotType ShellType;
        int MinFiringDelayCounter;
        int FiringDelay = 0;
        Tank3D Owner;
        int Ammo;
        char FiringSound;
        String Name;
        public Gun(Tank3D Tank, String TName, ShotType Shell, int tAmmo){
            this(Tank, TName, Shell, tAmmo, Shell.FiringSound);
        }
        public Gun(Tank3D Tank, String TName, ShotType Shell, int tAmmo, char Sound){
            this(Tank, TName, Shell, Tank.getFiringHole(Shell.FiringSpot), tAmmo, Sound);
        }
        public Gun(Tank3D Tank, String TName, ShotType Shell, Point3D[][] FiringPoint, int tAmmo, char Sound){
            Name = TName;
            Owner = Tank;
            ShellType = Shell;
            FiringPoints = FiringPoint;
            Ammo = tAmmo;
            FiringSound = Sound;
        }
        public void fire(){
            
            if(ReloadTimer < ShellType.ReloadDecrement || MinFiringDelayCounter > 0 || Ammo == 0){
                return;
            }else{
                
                ReloadTimer = ReloadTimer - ShellType.ReloadDecrement;
            }
            
            MinFiringDelayCounter = ShellType.MinFiringDelay;
            
            
            Point3D AverageHole = new Point3D(0, 0, 0);

            double Damage = 0;
            for(int i = 0; i < FiringPoints.length; i++){
                
                Point3D Point = new Point3D(0, 0, 0);
                for(int j = 0; j < FiringPoints[i].length; j++){
                    Point.POSX += FiringPoints[i][j].POSX;
                    Point.POSY += FiringPoints[i][j].POSY;
                    Point.POSZ += FiringPoints[i][j].POSZ;

                }
                Point.POSX /= FiringPoints[i].length;
                Point.POSY /= FiringPoints[i].length;
                Point.POSZ /= FiringPoints[i].length;
                if(ShellType.RandomLaunchOffset != 0){
                    while(true){
                        double POSX = (Math.random() * 2 - 1) * ShellType.RandomLaunchOffset;
                        double POSY = (Math.random() * 2 - 1) * ShellType.RandomLaunchOffset;
                        double POSZ = (Math.random() * 2 - 1) * ShellType.RandomLaunchOffset;
                        if(POSX * POSX + POSY * POSY + POSZ * POSZ <= ShellType.RandomLaunchOffset * ShellType.RandomLaunchOffset){
                            Point.moveVector(POSX, POSY, POSZ, Point);
                            break;
                        }
                    }
                }
                if(Owner == Player){
 
                }
                Shot3D LastShot = new Shot3D(Owner, Point, ShellType);
                Damage = Damage + LastShot.Damage;
                AverageHole.POSX += Point.POSX;
                AverageHole.POSY += Point.POSY;
                AverageHole.POSZ += Point.POSZ;
            
                Shots.add(LastShot);
                
                Ammo--;
                if(Ammo == 0){
                    Owner.Guns.remove(this);
                    break;
                }
                
                
            }
            AverageHole.POSX /= FiringPoints.length;
            AverageHole.POSY /= FiringPoints.length;
            AverageHole.POSZ /= FiringPoints.length;
            if(AverageHole.distanceTo(Player.Center) < 500){
                firingSound(FiringSound);
            }else if(AverageHole.distanceTo(Player.Center) < 1000){
                if(Character.isLowerCase(FiringSound)){
                    
                }else{
                    firingSound(Character.toLowerCase(FiringSound));
                }
            }
        }
    }
    class Display{
        int POSY;
        int POSX;
        double EllipseLength;
        Tank3D DisplayedTank;
        GeneralPath TurretImage;
        GeneralPath BaseImage;
        Shape Highlight;
        
        boolean MaxInfo;
        boolean Display = true;
        
        
        public Display(int tPOSX, int tPOSY, boolean tMaxInfo){ 
            POSY = tPOSY;
            POSX = tPOSX;
            POSY = POSY + 35;
            MaxInfo = tMaxInfo;
        }
        public void update(Tank3D tDisplayedTank){
            DisplayedTank = tDisplayedTank;
            
        }
       
        public void printCircle(Graphics2D Painter, Observer Viewer){
            Point3D ActualPoint = new Point3D(DisplayedTank.Center.POSX, DisplayedTank.Center.POSY, DisplayedTank.Center.POSZ);
            Point2D.Double Point = new Point2D.Double();
            ActualPoint.center(User, ActualPoint);
            Painter.setColor(getColor(DisplayedTank.Hitpoints / DisplayedTank.MaxHitpoints));
            int Size = Math.min(200, Math.max(10, (int)(10000 / User.POS.distanceTo(DisplayedTank.Center))));
            ActualPoint.toScreen(Viewer, Point);
            if(ActualPoint.POSX > 0 && Point.x <= Viewer.ScreenX + 5 && Point.x >= -5 && Point.y <= Viewer.ScreenY + 5 && Point.y >= -5){
                Point.y = Point.y - Size / 4;
                Painter.drawLine((int)Point.x - Size / 2, (int)Point.y + Size / 2, (int)Point.x + Size / 2, (int)Point.y + Size / 2);
                Painter.drawLine((int)Point.x + Size / 2, (int)Point.y + Size / 2, (int)Point.x, (int)Point.y - Size / 2);
                Painter.drawLine((int)Point.x - Size / 2, (int)Point.y + Size / 2, (int)Point.x, (int)Point.y - Size / 2);
                
            }
        }
        
        public Color getColor(double Percent){
            int Green = (int)Math.max(0, Math.min(255, 2 * 255 * Percent));
            int Red = (int)Math.max(0, Math.min(255, 510 + (-2 * 255 * Percent)));
            Color HPColor = new Color(Red, Green, 55);
            return HPColor;
        }
        public void print(Graphics2D Painter){
            if(DisplayedTank == null){
                
                return;
            }
            if(DisplayedTank.Dead == true){
                
                switchTarget(DisplayedTank.Allies);
                return;
            }
            
            if(!Display){
                return;
            }
            
            double PercentHP = DisplayedTank.Hitpoints / DisplayedTank.MaxHitpoints;
            Color HPColor = getColor(PercentHP);
            if(MaxInfo == true){
                //Make HP Bar
                Painter.setColor(HPColor);
                Painter.drawRect(POSX + 11, POSY + 21, (int)(148 * (double)(PercentHP)), 8);
                Painter.setColor(Color.white);
                Painter.drawRect(POSX + 10, POSY + 20, 150, 10);
                //Draw ReactiveArmor
                
                //Make Reload bar
                for(int i = 0; i < DisplayedTank.Guns.size(); i++){

                    Painter.setColor(Color.white);
                    
                    Painter.drawRect(POSX + 50, POSY - 20 * i, 110, 10);
                    if(DisplayedTank.Guns.gun(i) == DisplayedTank.CurrentGun){
                        Painter.setColor(getColor((float)DisplayedTank.Guns.gun(i).ReloadTimer / DisplayedTank.Guns.gun(i).ShellType.MaxReloadTime));
                    }else{ 
                        Painter.setColor(Color.white);
                    }
                    Painter.drawRect(POSX + 51, POSY + 1 - 20 * i, (int)(108 * ((double)(DisplayedTank.Guns.gun(i).ReloadTimer) / DisplayedTank.Guns.gun(i).ShellType.MaxReloadTime)), 8);
                    if(DisplayedTank.Guns.gun(i).Ammo > 0){
                        Area.printLine(POSX + 140, POSY + 16 - 20 * i, DisplayedTank.Guns.gun(i).Ammo + "", 10, Color.white, 'l');
                    }
                    Painter.setColor(Color.white);
                    Area.printLine(POSX + 51, POSY + 16 - 20 * i, DisplayedTank.Guns.gun(i).Name + "", 10, Color.white, 'l');
                }
                Painter.setColor(Color.white);
                //Make Name
                Painter.drawString("Tank", POSX + 10, POSY + 10);
                //Show Speed;
                Painter.drawString("Speed: " + (double)(int)(DisplayedTank.Speed * 500) / 10, POSX + 10, POSY + 50);
            }else{
                if(DisplayedTank == Player){
                    return;
                }
                printCircle(Painter, User);
                //Make HP Bar
                Painter.setColor(HPColor);
                Painter.drawRect(POSX + 11, POSY + 21, (int)(148 * (double)(PercentHP)), 8);
                Painter.setColor(Color.white);
                Painter.drawRect(POSX + 10, POSY + 20, 150, 10);
                //Make Name
                Painter.drawString(DisplayedTank.Name, POSX + 10, POSY + 10);
                
                //Show Distance
                Painter.drawString("Distance: " + (double)(int)(DisplayedTank.Center.distanceTo(Player.Center) * 10) / 10, POSX + 10, POSY + 50);
                
                //Show Orders
                if(DisplayedTank.Allies == Friends){
                    /*try{
                        Painter.drawString("FiringTarget " + DisplayedTank.Order.FiringTarget.Name, POSX + 10, POSY + 110);
                    }catch(Exception e){}
                    try{
                        Painter.drawString("MovingTarget " + DisplayedTank.Order.MovingTarget.Name, POSX + 10, POSY + 130);
                    }catch(Exception e){}*/
                    if(DisplayedTank.Order.FiringTarget != null){
                        Painter.drawString("Attacking " + DisplayedTank.Order.FiringTarget.Name, POSX + 10, POSY + 70);
                    }
                    if(DisplayedTank.Order.MovingTarget == DisplayedTank && !DisplayedTank.Order.OffensiveMovement){
                        Painter.drawString("Holding Position", POSX + 10, POSY + 90);
                    }else if(DisplayedTank.Order.MovingTarget != DisplayedTank.Order.FiringTarget){
                        Painter.drawString("Following " + DisplayedTank.Order.MovingTarget.Name, POSX + 10, POSY + 90);
                    } 
                    /*
                    try{
                        Painter.drawString("Firing Delay: " + DisplayedTank.CurrentGun.FiringDelay, POSX + 10, POSY + 110);
                    
                        Painter.drawString("ReloadTimer: " + DisplayedTank.CurrentGun.ReloadTimer, POSX + 10, POSY + 130);
                    }catch(Exception e){}*/
                }else{ 
                    
                   
                    
                }
            }
            
        }
    }
    class ShotType{
        String Name;
        String StandardName;
        double Speed;
        int SpeedMulti;
        double Damage;
        int FragmentCount;
        int Duration;
        double Range;
        Shape3D Form;
        double HMod;
        double VMod;
        double RMod;
        int MaxReloadTime;
        int ReloadIncrement;
        int ReloadDecrement;
        int Impact;
        int FragDurationMod;
        int MinFiringDelay;
        double HomingIncrement;
        int HomingDelay;
        double HomingArea;
        double MaxHomingDistance;
        char FiringSpot;
        char ImpactSound;
        char FiringSound;
        double RandomLaunchOffset;
        public ShotType(String tName, String tSName, char FiringSpott){
            Name = tName;
            StandardName = tSName;
            FiringSpot = FiringSpott;
            Form = new Shape3D(true, Color.red, 0, 0, 0);
            
        }
        public void setLaunchOffset(double tOffset){
            RandomLaunchOffset = tOffset;
        }
        public void setSpeed(double tSpeed, int tSpeedMulti){
            Speed = tSpeed;
            SpeedMulti = tSpeedMulti;
        }
        public void setDamage(double tDamage, int tImpact){
            Damage = tDamage;
            Impact = tImpact;
        }
        public void setFragments(int tFragments, int tDurationMod){
            FragDurationMod = tDurationMod;
            FragmentCount = tFragments;
        }
        public void setRange(double tRange){
            Duration = (int)(tRange / (Speed));
            Range = Duration * Speed;
        }
        public void setAngles(double H, double V, double R){
            HMod = H;
            VMod = V;
            RMod = R;
            for(int i = 0; i < Form.Points.length; i++){
                Form.Points[i].rotate(AffineTransform.getRotateInstance(Math.toRadians(H)), AffineTransform.getRotateInstance(Math.toRadians(V)), AffineTransform.getRotateInstance(Math.toRadians(R)), Form.Points[i]);
            }   
        }
        public void setReload(int Incr, int Decr, int Max, int MinDelay){
            MinFiringDelay = MinDelay;
            ReloadIncrement = Incr;
            ReloadDecrement = Decr;
            MaxReloadTime = Max;
        }
        public void setHoming(double tHomingIncrement, int tHomingDelay, double tHomingArea, int HomingDistance){
            HomingIncrement = tHomingIncrement;
            HomingDelay = tHomingDelay;
            HomingArea = tHomingArea;
            MaxHomingDistance = HomingDistance;
        }
        public void setSound(char FireSound, char ImpactingSound){
            ImpactSound = ImpactingSound;
            FiringSound = FireSound;
        }
    }
    Display PlayerDisplay = new Display(800, 600, true);
    Display TargetDisplay = new Display(100, 600, false);
    class Shake{
        int ShakeMagnitude;
        int ShakeDuration;
        int RShake;
        int VShake;
        int HShake;
        public Shake(double Duration, double Magnitude, int R, int V, int H){
            ShakeMagnitude = (int)Magnitude;
            ShakeDuration = (int)Duration;
            RShake = R;
            VShake = V;
            HShake = H;
        }
    }
    
    Timer Stopwatch = new Timer((int)(GUI.Speed * 10), this);
    Background Area;
    GeneralVector Shots = new GeneralVector();
    GeneralVector Enemies = new GeneralVector();
    GeneralVector Friends = new GeneralVector();
    GeneralVector Tanks = new GeneralVector();
    GeneralVector Obstacles = new GeneralVector();
    GeneralVector Fragments = new GeneralVector();
    GeneralVector PowerUps = new GeneralVector();
    
    Obstacle3D Cube;
    Shape3D Grid;
    
    ShotType TankShot;
    ShotType HeliShot;
    ShotType WeakShot;
    ShotType MiniShot;
    ShotType HomeShot;
    ShotType AAirShot;
    
    final String[] GreekAlphabet = {"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Lambda", "Mu", "Sigma", "Tau", "Omega"};
    int EnemyCount = 0;
    
    Observer User;
    double ViewerZOffset = 11;
    double ViewerXOffset = 45;
    Tank3D Player;
    final double MoveIncrement = 0.025;
    final int FadeStartDistance = 1200;
    final int FadeFinishDistance = 2000;
    int CollideShakeCounter;
    Vector Shakes = new Vector();
    boolean LookLeft;
    boolean LookRight;
    char ViewType = 'o';
    AffineTransform[] TurnIncrTransform = new AffineTransform[31];
    
    AffineTransform[] TurnDecrTransform = new AffineTransform[31];
    
    int LookLeftCounter;
    int LookRightCounter;
    double MouseX;
    double MouseY;
    int Wave = 0;
    
    boolean Clicking;
    
    int RadarCenterX = 110;
    int RadarCenterY = 110 + TopBorderWidth;
    int RadarSize = 150;
    int RadarDistance = RadarSize / 2;
    int RadarRange = 500;
    
    long FrameCounter;
    public String getGreekAlphabet(){
        if(EnemyCount >= GreekAlphabet.length){
            EnemyCount = 0;
        }
        return GreekAlphabet[EnemyCount++];
        
        
    }
    public void printShape(Shape3D Form, Graphics2D Painter, Observer Viewer){
        Form.print(Painter, Viewer, Form.Tint);
 
    }
    public void shakeCamera(int INTA, int INTB){
        
        Shakes.add(new Shake(INTA * 2, INTB, 0, 0, 0));
    }
    public void switchTarget(GeneralVector Units){
        int INTA;
        if(Player.Dead || Units.size() == 0){
            Player.Order.FiringTarget = null;
            TargetDisplay.DisplayedTank = null;
            return;
        }
        try{
            INTA = Units.indexOf(TargetDisplay.DisplayedTank);
        }catch(Exception e){
            INTA = -1;
        }
        if(INTA == -1){
            
            TargetDisplay.DisplayedTank = Units.tank3D(0);
            Player.Order.FiringTarget = Units.tank3D(0);
        }else if(INTA < Units.size()){
            try{
                TargetDisplay.DisplayedTank = Units.tank3D(INTA + 1);
                Player.Order.FiringTarget = Units.tank3D(INTA + 1);
            }catch(Exception e){
                TargetDisplay.DisplayedTank = Units.tank3D(0);
                Player.Order.FiringTarget = Units.tank3D(0);
            }
        }
    }
    public Point3D getRandomPos(Point3D Center, int Distance, int Height){
        double Angle = Math.random() * 2 * Math.PI;
        double POSX = Distance * Math.cos(Angle) + Center.POSX;
        double POSY = Distance * Math.sin(Angle) + Center.POSY;
        return new Point3D(POSX, POSY, Height + Center.POSZ);
    }
    public void spawnWave(){
        Wave++;
        Point3D Center = getRandomPos(Utils3D.Origin, GUI.randomInt(1300, 1500), 0);
        switch(Wave){
            case 1:{
                for(int i = 0; i < 1; i++){
                    Tank3D Enemy = new Tank3D("Enemy Tank " + getGreekAlphabet(), getRandomPos(Center, GUI.randomInt(100, 200), 0), 0, (GUI.randomInt(0, 1) == 0 ? 'a' : 'b'));
                    
                    Enemies.add(Enemy);
                    Enemy.Targets = Friends; Enemy.Allies = Enemies;
                }
                
            }break;
            case 2:{
                for(int i = 0; i < 2; i++){
                    Tank3D Enemy = new Tank3D("Enemy Tank " + getGreekAlphabet(), getRandomPos(Center, GUI.randomInt(100, 200), 0), 1, (i == 0 ? 'a' : 'b'));
                    
                    Enemies.add(Enemy);
                    Enemy.Targets = Friends; Enemy.Allies = Enemies;
                }
                Enemies.tank3D(0).Order.setToFollow(Enemies.tank3D(1));
                {
                    PowerUp3D PowerUp = new PowerUp3D();
                    PowerUp.addArmorBonus(5);
                    Enemies.tank3D(0).addLoot(PowerUp);
                }
                {   
                    PowerUp3D PowerUp = new PowerUp3D();
                    PowerUp.addWeaponBonus(AAirShot, 25);
                    Enemies.tank3D(1).addLoot(PowerUp);
                }
            }break;
            case 3:{
                for(int i = 0; i < 3; i++){
                    Aircraft3D Enemy = new Aircraft3D("Enemy Aircraft " + getGreekAlphabet(), getRandomPos(Center, GUI.randomInt(100, 200), GUI.randomInt(40, 60)), i, (i == 2 ? 'b' : (i == 3 ? 'c' : 'a')));
                    
                    Enemies.add(Enemy);
                    Enemy.Targets = Friends; Enemy.Allies = Enemies;
                }
                Enemies.tank3D(2).Order.setToFollow(Enemies.tank3D(1));
                Enemies.tank3D(1).Order.setToFollow(Enemies.tank3D(0));
                PowerUp3D PowerUp = new PowerUp3D();
                PowerUp.addWeaponBonus(HomeShot, 4);
                Enemies.tank3D(0).addLoot(PowerUp);
            }break;
            case 4:{
                for(int i = 0; i < 2; i++){
                    Aircraft3D Enemy = new Aircraft3D("Enemy Aircraft " + getGreekAlphabet(), getRandomPos(Center, GUI.randomInt(100, 200), GUI.randomInt(40, 60)), 1, (i == 0 ? 'c' : 'a'));
                    
                    Enemies.add(Enemy);
                    Enemy.Targets = Friends; Enemy.Allies = Enemies;
                }
                for(int i = 0; i < 1; i++){
                    Tank3D Enemy = new MammothTank3D("Enemy Tank " + getGreekAlphabet(), getRandomPos(Center, GUI.randomInt(100, 200), 0), 2, 'a');
                    
                    Enemies.add(Enemy);
                    Enemy.Targets = Friends; Enemy.Allies = Enemies;
                }
                Enemies.tank3D(0).Order.setToFollow(Enemies.tank3D(2));
                Enemies.tank3D(1).Order.setToFollow(Enemies.tank3D(2));
                PowerUp3D PowerUp = new PowerUp3D();
                PowerUp.addArmorBonus(5);
                Enemies.tank3D(2).addLoot(PowerUp);
                PowerUp = new PowerUp3D();
                PowerUp.addWeaponBonus(HomeShot, 8);
                Enemies.tank3D(0).addLoot(PowerUp);
                PowerUp = new PowerUp3D();
                PowerUp.addWeaponBonus(AAirShot, 40);
                Enemies.tank3D(1).addLoot(PowerUp);
            }break;
            default:{
                int INTA = GUI.randomInt(0, 2);
                for(int i = 0; i < INTA; i++){
                    Tank3D Enemy = new Tank3D("Enemy Tank " + getGreekAlphabet(), getRandomPos(Center, GUI.randomInt(100, 200), 0), GUI.randomInt(1, 2), (i == 0 ? 'a' : 'b'));
                    Enemies.add(Enemy);
                    Enemy.Targets = Friends; Enemy.Allies = Enemies;
                }
                for(int i = INTA; i < 2; i++){
                    Aircraft3D Enemy = new Aircraft3D("Enemy Aircraft " + getGreekAlphabet(), getRandomPos(Center, GUI.randomInt(100, 200), GUI.randomInt(40, 60)), 2, (i == 1 ? (GUI.randomInt(0, 1) == 0 ? 'b' : 'c') : 'a'));
  
                    Enemies.add(Enemy);
                    Enemy.Targets = Friends; Enemy.Allies = Enemies;
                }
                Enemies.tank3D(0).Order.setToFollow(Enemies.tank3D(1));
                if(Wave % 2 == 0){
                    PowerUp3D PowerUp = new PowerUp3D();
                    if(Wave % 4 == 1){
                        
                        PowerUp.addWeaponBonus(HomeShot, 6);
                    }else{
                        PowerUp.addArmorBonus(5);
                    }
                    Enemies.tank3D(0).addLoot(PowerUp);
                }
            }break;
        }
    }
    public void initShots(){
        TankShot = new ShotType("TankShot", "Main Cannon", 'g'); TankShot.setLaunchOffset(0.0);
        TankShot.setSpeed(3.0, 5); TankShot.setDamage(1.0, 3); TankShot.setHoming(0.0, 0, 0.0, 0);
        TankShot.setFragments(10, 25); TankShot.setRange(1500); TankShot.setReload(5, 720, 720, 10);
        TankShot.setSound('C', 'I');
        Utils3D.setPoints(TankShot.Form, 'c', 2, 2, 2); TankShot.Form.finalizeLinks(); TankShot.setAngles(0, 0, 0); 
        
        HeliShot = new ShotType("HeliShot", "Gattling Cannon", 'l'); HeliShot.setLaunchOffset(1.0);
        HeliShot.setSpeed(4.0, 4); HeliShot.setDamage(0.2, 1); HeliShot.setHoming(0.0, 0, 0.0, 0);
        HeliShot.setFragments(2, -50); HeliShot.setRange(800); HeliShot.setReload(6, 180, 720, 10);
        HeliShot.setSound('c', 'i');
        Utils3D.setPoints(HeliShot.Form, 'p', 1.5, 3, 0); TankShot.Form.finalizeLinks(); HeliShot.setAngles(0, -90, 0); 
        
        WeakShot = new ShotType("WeakShot", "Main Cannon", 'g'); WeakShot.setLaunchOffset(0.0);
        WeakShot.setSpeed(3.0, 5); WeakShot.setDamage(0.8, 2); WeakShot.setHoming(0.0, 0, 0.0, 0);
        WeakShot.setFragments(6, 25); WeakShot.setRange(1500); WeakShot.setReload(5, 720, 720, 10);
        WeakShot.setSound('C', 'I');
        Utils3D.setPoints(WeakShot.Form, 'c', 1.75, 1.75, 1.75); WeakShot.Form.finalizeLinks(); WeakShot.setAngles(0, 0, 0); 
        
        MiniShot = new ShotType("MiniShot", "Gattling Cannon", 'l'); MiniShot.setLaunchOffset(1.0);
        MiniShot.setSpeed(4.0, 4); MiniShot.setDamage(0.15, 1); MiniShot.setHoming(0.0, 0, 0.0, 0);
        MiniShot.setFragments(2, -50); MiniShot.setRange(800); MiniShot.setReload(6, 180, 720, 10);
        MiniShot.setSound('c', 'i');
        Utils3D.setPoints(MiniShot.Form, 'p', 1.5, 3, 0); MiniShot.Form.finalizeLinks(); MiniShot.setAngles(0, -90, 0); 
        
        HomeShot = new ShotType("HomeShot", "Anti-Tank Missiles", 's'); HomeShot.setLaunchOffset(0.0);
        HomeShot.setSpeed(3.0, 3); HomeShot.setDamage(2.0, 3); HomeShot.setHoming(0.4, 30, 60.0, 1000);
        HomeShot.setFragments(12, 50); HomeShot.setRange(2000); HomeShot.setReload(2, 720, 720, 10);
        HomeShot.setSound('R', 'I');
        Utils3D.setPoints(HomeShot.Form, 'p', 3, 9, 0); HomeShot.Form.finalizeLinks(); HomeShot.setAngles(0, -90, 0);    
        
        AAirShot = new ShotType("AAirShot", "AA Rockets", 's'); AAirShot.setLaunchOffset(4.0);
        AAirShot.setSpeed(3.0, 3); AAirShot.setDamage(0.2, 2); AAirShot.setHoming(1.5, 0, 150.0, 750);
        AAirShot.setFragments(4, 0); AAirShot.setRange(750); AAirShot.setReload(4, 120, 720, 5);
        AAirShot.setSound('r', 'i');
        Utils3D.setPoints(AAirShot.Form, 'p', 2, 6, 0); AAirShot.Form.finalizeLinks(); AAirShot.setAngles(0, -90, 0); 
        
    }
    public Point3D getRandomPositionInCircle(Point3D Center, int Radius, double Height){
        double POSX = GUI.randomInt(-Radius, Radius);
        double POSY = GUI.randomInt(-Radius, Radius);
        if(Radius * Radius >= POSX * POSX + POSY * POSY){
            return new Point3D(POSX + Center.POSX, POSY + Center.POSY, Height + Center.POSZ);
        }else{
            return getRandomPositionInCircle(Center, Radius, Height);
        }
        
    }
    public void init(){
        initShots();

        for(int i = 0; i < TurnIncrTransform.length; i++){
            TurnIncrTransform[i] = AffineTransform.getRotateInstance(Math.toRadians((double)i * 0.5));
            TurnDecrTransform[i] = GUI.inverse(TurnIncrTransform[i]);
        }
        for(int j = 0; j < 5; j++){
            for(int i = 0; i < 4; i++){
                
                
                int Size = GUI.randomInt(30, 90);
                Point3D Center = getRandomPositionInCircle(Utils3D.Origin, 900, Size / 2);
                Obstacles.add(new Obstacle3D(Center, Math.random() * 360, new Point3D(Size, Size, Size)));
            }
        }
        
        Grid = new Shape3D(true, Color.green.darker(), 0, 0, 0);
        
        Utils3D.setPoints(Grid, 'g', 16, 16, 60);
        
        
        Player = new Tank3D("Player", new Point3D(0, 0, 0), -1, 's');
       // Player.Guns.add(new Gun(Player, "AA Rockets", AAirShot, 60));
        
        
        

        Player.Hitpoints = 10;
        Player.MaxHitpoints = 10;
        
        Tank3D Assistant = new Tank3D("Ally", new Point3D(0, 100, 0), 1, 'a');
        Assistant.Order.setToFollow(Player);
        Assistant.Targets = Enemies; Assistant.Allies = Friends;

        Friends.add(Assistant);
        Friends.add(Player);
        
        
        User = new Observer(0, 0, ViewerZOffset + Player.BH + Player.TH / 2, 0, 0, 0);
        
        User.Magnification = 1.5;
        User.setArea(X, Y);
        
        PlayerDisplay.update(Player);
        TargetDisplay.update(null);
    }
    
    public void initSounds(){
        GUI.playSound(false, "Explosion1.wav", 0, 1);
        GUI.playSound(false, "Explosion2.wav", 0, 1);
        GUI.playSound(false, "Explode.wav", 0, 1);
        GUI.playSound(false, "GirderImpact.wav", 0, 1);
        GUI.playSound(false, "ShotGunFire.wav", 0, 1);
        GUI.playSound(false, "Ricochet.wav", 0, 1);
    }
    public Test(){
        //BASIC INITIALIZATION
        super("Window"); 
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new Background();
        contentArea.add(Area);
        
        Area.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        init();
        initSounds();
        Stopwatch.start();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        if(!GUI.Border){
            setUndecorated(true); 
            getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        }
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }
    public void mouseTarget(){ 
        
        
        double HAngle = 0;
        double VAngle = 0;
        Point2D.Double MousePoint = new Point2D.Double(MouseX, MouseY);
  
   //     if(MousePoint.y > User.HalfY){
        if(User.POS.POSZ - Player.TurretForm.Center.POSZ == 0){
            Point2D.Double TempPoint = User.getAngle(MousePoint);                       
            HAngle = TempPoint.x;
            VAngle = TempPoint.y;
        }else if(TargetDisplay.DisplayedTank != null){
            Point2D.Double TempPoint = User.getGradient(MousePoint);
            
            double TargetZ = TargetDisplay.DisplayedTank.Center.POSZ;
            double DiagLength = (User.POS.POSZ - TargetZ) / TempPoint.y;
            double TargetX = -DiagLength / Math.sqrt((1 + TempPoint.x * TempPoint.x));
            if(TargetX > 0){
                    double TargetY = (TargetX) * TempPoint.x;
                    TargetX = TargetX - ViewerXOffset + User.Zoom;  
                    Point2D.Double Angles = new Point3D(0, 0, Player.TurretForm.Center.POSZ).angleTo(new Point3D(TargetX, TargetY, TargetZ));
                    HAngle = Angles.x - User.HAngle + 180;
                    VAngle = Angles.y + User.VAngle;
                
                }else{
                    
                    TempPoint = User.getGradient(MousePoint);
                    
                    DiagLength = User.POS.POSZ / TempPoint.y;
                    TargetX = -DiagLength / Math.sqrt((1 + TempPoint.x * TempPoint.x));
                    if(TargetX > 0){
                        double TargetY = (TargetX) * TempPoint.x;
                        TargetX = TargetX - ViewerXOffset + User.Zoom;  
                        TargetZ = 0;
                        Point2D.Double Angles = new Point3D(0, 0, Player.TurretForm.Center.POSZ).angleTo(new Point3D(TargetX, TargetY, TargetZ));
                        HAngle = Angles.x - User.HAngle + 180;
                        VAngle = Angles.y + User.VAngle;
                    }else{
                        TempPoint = User.getAngle(MousePoint);
                        HAngle = TempPoint.x;
                        VAngle = TempPoint.y;                        
                    }
                
                    
                    
                    
                    
                }
        }else{
            Point2D.Double TempPoint = User.getGradient(MousePoint);
            
            double DiagLength = User.POS.POSZ / TempPoint.y;
            double TargetX = -DiagLength / Math.sqrt((1 + TempPoint.x * TempPoint.x));
            if(TargetX > 0){
                double TargetY = (TargetX) * TempPoint.x;
                TargetX = TargetX - ViewerXOffset + User.Zoom;  
                double TargetZ = 0;
                Point2D.Double Angles = new Point3D(0, 0, Player.TurretForm.Center.POSZ).angleTo(new Point3D(TargetX, TargetY, TargetZ));
                HAngle = Angles.x - User.HAngle + 180;
                VAngle = Angles.y + User.VAngle;
            }else{
                TempPoint = User.getAngle(MousePoint);
                HAngle = TempPoint.x;
                VAngle = TempPoint.y;                        
            }
            
            
        }

                
                
                
            

     //   }else{
            
            
       
//       }
        Player.Controls.TargetVAngle = VAngle;
        Player.Controls.TargetHAngle = HAngle;
        
        
        
    }
    public void shake(char Switch, Shake Stats){
        int INTA = 0;
        int Shake = 0;
        int Divisor = 0;
        int ShakeDuration = (int)Stats.ShakeDuration;
        int ShakeMagnitude = (int)Stats.ShakeMagnitude;
        switch(Switch){
            case 'r': Shake = Stats.RShake; Divisor = 0; break;
            case 'v': Shake = Stats.VShake; Divisor = 1; break;
            case 'h': Shake = Stats.HShake; Divisor = 2; break;
        }
        if(Math.abs(Shake) >= (ShakeDuration - 1) * 20){
            if(Shake > 0){
                INTA = Math.max(-Shake, -10);
            }else{
                INTA = Math.min(-Shake, 10);
            } 
    
        }else{
            if(ShakeDuration % 6 == Divisor){
                while(INTA == 0){
                
                    INTA = GUI.randomInt(-ShakeMagnitude, ShakeMagnitude);
                }
            }else{
                INTA = 0;
            }
        }
        //System.out.print(ShakeDuration + "\t" + RShake + " + " + INTA + " = ");
        if(Shake < -ShakeMagnitude * ShakeMagnitude && INTA < 0 || Shake > ShakeMagnitude * ShakeMagnitude && INTA > 0){
            INTA = -INTA;
        }
        if(INTA >= 0){
            User.increment(Switch, (double)INTA / 2, TurnIncrTransform[INTA]);   
        }else{
            User.increment(Switch, (double)INTA / 2, TurnDecrTransform[-INTA]);
        }
        Shake = Shake + INTA;
        //System.out.println(RShake);
        switch(Switch){
            case 'r': Stats.RShake = Shake; break;
            case 'v': Stats.VShake = Shake; break;
            case 'h': Stats.HShake = Shake; break;
        }
        
    }
    public void cameraControls(){
        if(LookLeftCounter == 0 && LookLeft)LookLeftCounter = LookLeftCounter + 30;
        if(LookRightCounter == 0 && LookRight)LookRightCounter = LookRightCounter + 30;
        for(int i = 0; i < Shakes.size(); i++){
            shake('r', (Shake)Shakes.elementAt(i));    
            shake('v', (Shake)Shakes.elementAt(i));
            shake('h', (Shake)Shakes.elementAt(i));
            ((Shake)Shakes.elementAt(i)).ShakeDuration--;
            if(((Shake)Shakes.elementAt(i)).ShakeDuration == 0){ 
                Shakes.remove(i);
            }
        }
        
        if(Player.Controls.Left){  User.increment('h', -1, TurnDecrTransform[2]);}
        if(Player.Controls.Right){  User.increment('h', 1, TurnIncrTransform[2]);}
        

        
        User.POS.POSX = Player.Center.POSX;
        User.POS.POSY = Player.Center.POSY;
            
    
        User.POS.POSZ = ViewerZOffset + Player.BH + Player.TH / 2 + Player.Center.POSZ;
        
       
        
        
        User.POS.moveDirection(User.HAngle - 180, 0, ViewerXOffset, User.POS);
       
        
        if(LookLeftCounter > 0){
            User.increment('h', -3, TurnDecrTransform[6]); 
        }
        if(LookRightCounter > 0){
            User.increment('h', 3, TurnIncrTransform[6]); 
        }

    }
    public void shiftGrid(){
        int INTX = (int)GUI.round(Player.Center.POSX, 1.0/60);
        int INTY = (int)GUI.round(Player.Center.POSY, 1.0/60);
        boolean ReRotate = false;
        if(Grid.Center.POSX != INTX || Grid.Center.POSY != INTY){
            ReRotate = true;
        }
        if(ReRotate){
            Grid.Center.POSX = INTX;
            Grid.Center.POSY = INTY;
            Grid.rotate();
        }
    }
   
    public void move(){
        
       
        
        //System.out.println(User.HAngle + "\t" + User.VAngle + "\t" + User.RAngle);
       // System.out.println(User.POS.POSX + "\t" + User.POS.POSY + "\t" + User.POS.POSZ);
    }
    public void keyPressed(KeyEvent Event){
        
        switch(Event.getKeyCode()){
            case 87: Player.Controls.Forward = true; break;
            case 83: Player.Controls.Back = true; break;
            case 65: Player.Controls.Left = true; break;
            case 68: Player.Controls.Right = true; break;
            
            
            case 81: LookLeft = true; break; 
            case 69: LookRight = true; break; 
            case 27: System.exit(0); break;
            //default: System.out.println(Event.getKeyCode());
        }
        
        //System.out.println(User.HAngle + "\t" + User.VAngle + "\t" + User.RAngle);
    }
    public void keyReleased(KeyEvent Event){
        switch(Event.getKeyCode()){
            case 87: Player.Controls.Forward = !true; break;
            case 83: Player.Controls.Back = !true; break;
            case 65: Player.Controls.Left = !true; break;
            case 68: Player.Controls.Right = !true; break;
            case 81: LookLeft = !true; break; 
            case 69: LookRight = !true; break; 
            
            
        }

    }
 
    
    public void keyTyped(KeyEvent Event){
        
        switch(Event.getKeyChar()){
            case 'z':
        
                switch(ViewType){
                    case 'o':
                        ViewerXOffset = 0;
                        ViewerZOffset = -Player.TH / 2;
                        ViewType = 'n';
                        break;

                    case 'n':
                        ViewerXOffset = 30;
                        ViewerZOffset = 11 - Player.TH / 2;
                        ViewType = 'o';
                        break;
                }
                break;
            case 'r':
                switchTarget(Enemies);
                break;
            case 't':
                switchTarget(Friends);
                break;
            case '1':
                if(TargetDisplay.DisplayedTank != null && !TargetDisplay.DisplayedTank.Dead){
                    for(int i = 0; i < Friends.size(); i++){
                        Friends.tank3D(i).Order.setToAttack(TargetDisplay.DisplayedTank);
                    }
                }
                break;
            case '2':
                for(int i = 0; i < Friends.size(); i++){
                    
                    Friends.tank3D(i).Order.setToFollow(Player);
                }
                break;
            case '3':
                for(int i = 0; i < Friends.size(); i++){
                    Friends.tank3D(i).Order.setToHold();
                    
                }
                break;
            case 'x':{
              
                
            }
        }
        
    }
    
    public void checkCollision(){
        
        Point3D Point;
        for(int i = 0; i < Tanks.size(); i++){
            for(int j = 0; j < PowerUps.size(); j++){
                PowerUps.powerup3D(j).checkCollision(Tanks.tank3D(i));
            }
            for(int j = 0; j < Obstacles.size(); j++){
                Point = Tanks.tank3D(i).checkCollision(Obstacles.obstacle3D(j).Form);
                if(Point != null){
                    Tanks.tank3D(i).collide(Point, Obstacles.obstacle3D(j), true);
                }
                if(!Tanks.tank3D(i).Flying){
                    Point = Obstacles.obstacle3D(j).checkBaseIntersect(Tanks.tank3D(i).Form);
                    if(Point != null){
                        Tanks.tank3D(i).collide(Point, Obstacles.obstacle3D(j), true);
                    }
                }
            }
            for(int j = 0; j < Tanks.size(); j++){
                if(j == i)continue;
                if(Tanks.tank3D(i).Flying != Tanks.tank3D(j).Flying){continue;}
                Point = Tanks.tank3D(i).checkCollision(Tanks.tank3D(j).Form);
                if(Point != null){
                    Tanks.tank3D(i).collide(Point, Tanks.tank3D(j), true);
                }
            }
            
        }
        
    }
    
    public void actionPerformed(ActionEvent Event){
        FrameCounter++;
        if(Clicking){
            if(Player.CurrentGun != null){
                Player.CurrentGun.fire();
            }
        }
        
        Area.Painter.setColor(Color.black);
        Area.Painter.fillRect(0, 0, X, Y);
        shiftGrid();
        
        Grid.print(Area.Painter, User, Grid.Tint);
        
            
        Tanks.clear();
        Tanks.addAll(Enemies);
        Tanks.addAll(Friends);
        CollideShakeCounter = Math.max(0, CollideShakeCounter - 1);
        HomingSoundCounter = Math.max(0, HomingSoundCounter - 1);
        LookLeftCounter = Math.max(0, LookLeftCounter - 1);
        LookRightCounter = Math.max(0, LookRightCounter - 1);
        for(int i = 0; i < Tanks.size(); i++){
          
            Tanks.tank3D(i).move();
            
        }
        cameraControls();
        mouseTarget();
        for(int i = 0; i < Tanks.size(); i++){
            Tanks.tank3D(i).print(Area.Painter, User);
        }
        
        for(int i = 0; i < Shots.size(); i++){
            
            Shots.shot3D(i).move();
        } 
        for(int i = 0; i < PowerUps.size(); i++){
            PowerUps.powerup3D(i).print(Area.Painter, User);
            PowerUps.powerup3D(i).move();
        }
        for(int i = 0; i < Fragments.size(); i++){
            Fragments.fragment3D(i).print(Area.Painter, User);
            Fragments.fragment3D(i).move();
            
        } 
        for(int i = 0; i < Obstacles.size(); i++){
            Obstacles.obstacle3D(i).print(Area.Painter, User);
        }
        checkCollision();

        if(Enemies.size() == 0 && !Player.Dead){
            spawnWave();
        }

        
        
            
        makeDisplay();
        Area.paintComponent(this.getGraphics()); 
    }
    public void switchGun(){
        int INTA = 0;
        
        if(Player.CurrentGun == null){
        }else{
            INTA = Player.Guns.indexOf(Player.CurrentGun);
        }
            
        
        INTA++;
        if(INTA < Player.Guns.size()){
            Player.CurrentGun = Player.Guns.gun(INTA);
        }else if(Player.Guns.size() > 0){
            Player.CurrentGun = Player.Guns.gun(0);
        }else{
            Player.CurrentGun = null;
        }
    }
    public void mousePressed(MouseEvent Event){
        if(Event.getButton() == 1){
            Clicking = true;
        }
        if(Event.getButton() == 3){
            switchGun();
        }
        
        
        
      
    }
    public void mouseReleased(MouseEvent Event){
        if(Event.getButton() == 1){
            Clicking = false;
        }
        
    }
    public void mouseClicked(MouseEvent Event){
        
    }
    public void mouseEntered(MouseEvent Event){
    }
    public void mouseExited(MouseEvent Event){

    }
    public void mouseMoved(MouseEvent Event){
        MouseX = Event.getX();
        MouseY = Event.getY();
    }
    public void mouseDragged(MouseEvent Event){
        MouseX = Event.getX();
        MouseY = Event.getY();
    }
    
    public void printRadar(Graphics2D Painter){
        Painter.setColor(Color.white);
        Painter.drawOval(RadarCenterX - RadarSize / 2, RadarCenterY - RadarSize /2, RadarSize, RadarSize);
        
        Painter.drawRect(RadarCenterX - 3, RadarCenterY - 4, 6, 8);
        Point2D.Double PointA = new Point2D.Double(RadarCenterX, RadarCenterY);
        
        double Angle = User.HAngle - 45 - Player.Form.HAngle;

        AffineTransform Rotate = GUI.inverse(Player.Form.HRotation);
        Point2D.Double PointB = new Point2D.Double(RadarCenterX + RadarDistance * Math.cos(Math.toRadians(Angle)), RadarCenterY + RadarDistance * Math.sin(Math.toRadians(Angle)));
        Painter.drawLine((int)PointA.x, (int)PointA.y, (int)PointB.x, (int)PointB.y);
        Angle = Angle - 90;
        PointB.setLocation(RadarCenterX + RadarDistance * Math.cos(Math.toRadians(Angle)), RadarCenterY + RadarDistance * Math.sin(Math.toRadians(Angle)));
        Painter.drawLine((int)PointA.x, (int)PointA.y, (int)PointB.x, (int)PointB.y);
        
        
        Rotate.rotate(Math.toRadians(-90));
        
        
        Painter.setColor(Color.yellow);
        double XDistance;
        double YDistance;
        double ActualDistance;
        Point2D.Double Point = new Point2D.Double();
        Tanks.clear();
        Tanks.addAll(Friends);
        Tanks.addAll(Enemies);
        
        for(int i = 0; i < Tanks.size(); i++){
            if(Tanks.tank3D(i) == Player){
                continue;
            }

            XDistance = Tanks.tank3D(i).Center.POSX - Player.Center.POSX;
            YDistance = Tanks.tank3D(i).Center.POSY - Player.Center.POSY;
            ActualDistance = Math.sqrt(XDistance * XDistance + YDistance * YDistance);
            if(ActualDistance > RadarRange){
                XDistance = XDistance / ActualDistance * RadarRange;
                YDistance = YDistance / ActualDistance * RadarRange;
            }
            Point.setLocation(XDistance * RadarDistance / RadarRange, YDistance * RadarDistance / RadarRange);
            
            Rotate.transform(Point, Point);
            if(Tanks.tank3D(i) == TargetDisplay.DisplayedTank){
                Painter.setColor(Color.green);
                Painter.drawRect((int)Point.x - 4 + RadarCenterX, (int)Point.y - 4 + RadarCenterY, 8, 8);
                
            }
            if(Tanks.tank3D(i).Allies == Friends){
                Painter.setColor(Color.cyan);
            }else{
                Painter.setColor(Color.yellow);
            }
            Painter.fillOval((int)Point.x - 3 + RadarCenterX, (int)Point.y - 3 + RadarCenterY, 6, 6);
        }
        Painter.setColor(Color.red);
        for(int i = 0; i < Shots.size(); i++){

            XDistance = Shots.shot3D(i).Center.POSX - Player.Center.POSX;
            YDistance = Shots.shot3D(i).Center.POSY - Player.Center.POSY;
            ActualDistance = Math.sqrt(XDistance * XDistance + YDistance * YDistance);
            if(ActualDistance > RadarRange){
                continue;
            }
            Point.setLocation(XDistance * RadarDistance / RadarRange, YDistance * RadarDistance / RadarRange);
            
            Rotate.transform(Point, Point);
            Painter.fillOval((int)Point.x - 2 + RadarCenterX, (int)Point.y - 2 + RadarCenterY, 4, 4);
        }
        Painter.setColor(Color.white);
        for(int i = 0; i < Obstacles.size(); i++){
            
            XDistance = Obstacles.obstacle3D(i).Center.POSX - Player.Center.POSX;
            YDistance = Obstacles.obstacle3D(i).Center.POSY - Player.Center.POSY;
            ActualDistance = Math.sqrt(XDistance * XDistance + YDistance * YDistance);
            if(ActualDistance > RadarRange){
                continue;
            }
            Point.setLocation(XDistance * RadarDistance / RadarRange, YDistance * RadarDistance / RadarRange);
            
            Rotate.transform(Point, Point);
            int ObjectSize = (int)(Obstacles.obstacle3D(i).Form.ArgA * 0.07);
            Painter.drawRect((int)Point.x - ObjectSize + RadarCenterX, (int)Point.y - ObjectSize + RadarCenterY, ObjectSize * 2, ObjectSize * 2);
        }
        Painter.setColor(Color.green);
        for(int i = 0; i < PowerUps.size(); i++){
            XDistance = PowerUps.powerup3D(i).Center.POSX - Player.Center.POSX;
            YDistance = PowerUps.powerup3D(i).Center.POSY - Player.Center.POSY;
            ActualDistance = Math.sqrt(XDistance * XDistance + YDistance * YDistance);
            if(ActualDistance > RadarRange){
                continue;
            }
            Point.setLocation(XDistance * RadarDistance / RadarRange, YDistance * RadarDistance / RadarRange);
            
            Rotate.transform(Point, Point);
            Area.printLine((int)Point.x + RadarCenterX, (int)Point.y + RadarCenterY, "P", 12, Color.green, 'c');
        }
    }
    public void makeDisplay(){
        if(!Player.Dead){
            PlayerDisplay.print(Area.Painter);
            TargetDisplay.print(Area.Painter);
            printRadar(Area.Painter);
        }
    }
    class Background extends JPanel{
        
        
        BufferedImage Picture = new BufferedImage(X, Y, GUI.TankImageType);
        Graphics2D Painter = Picture.createGraphics();
        double AvTime;
        int Count = 1;
        long Temp = System.currentTimeMillis();
        
        public void printLine(int INTX, int INTY, String Text, int Font, Color FontColor, char Alignment){
            
            Font FONT = Painter.getFont();
            Painter.setFont(FONT.deriveFont((float)Font));
            FontMetrics Metrics = Painter.getFontMetrics();
            Painter.setColor(FontColor);
            switch(Alignment){
                case 'c':
                    Painter.drawString(Text, INTX - Metrics.stringWidth(Text) / 2, INTY - Metrics.getHeight() / 2);
                    break;
                case 't':
                    Painter.drawString(Text, INTX - Metrics.stringWidth(Text) / 2, INTY);
                    break;
                case 'b':
                    Painter.drawString(Text, INTX - Metrics.stringWidth(Text) / 2, INTY - Metrics.getHeight());
                    break;
                case 'l':
                    Painter.drawString(Text, INTX, INTY - Metrics.getHeight() / 2);
                    break;
                case 'r':
                    Painter.drawString(Text, INTX - Metrics.stringWidth(Text), INTY - Metrics.getHeight() / 2);
                    break;
            }
            Painter.setFont(FONT);    
            
            
        }
        public void paintComponent(Graphics paint){
            
            AvTime = (AvTime * Count + System.currentTimeMillis() - Temp) / (++Count);
            
            System.out.println(AvTime);
            
            Temp = System.currentTimeMillis();
            //   Graphics2D Paint = (Graphics2D)(paint);
            
            //System.out.println(Ship.FACINGANGLE + "\t" + Ship.SPEED + "\t" + Ship.POSY + "\t" + Ship.POSX + "\t");
            
            try{
                
                //Paint.drawImage(Picture, null, 0, 0);
                paint.drawImage(Picture, 0, 0, Color.white, null);
            }catch(NullPointerException e){}
        }
    }
    
}

class NewTest extends JFrame implements KeyListener, ActionListener, MouseListener, MouseMotionListener, MouseWheelListener{
    
    final int TopBorderWidth = 0;
    final int X = 1024 - TopBorderWidth;
    final int Y = 768;
    final Dimension ScreenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    static class World{
        
        public static double getGravity(){
            return 9.81;
        }
        static double K = 1.402; //Adiabetic Index
        
        public static double getSpeedofSound(double h){
            return Math.sqrt(K * R * getAirTemperature(h) / M);
        }
        static double TO = 288; //Temperature at Sea Level
        static double L = -0.0065; //Rate of Temperature Decrease
        public static double getAirTemperature(double h){
            
            return TO + L*h;
        }
        static double PO = 101325; //Pressure at Sea Level
        
        
        static double R = 8.314; //Universal Gas Constant
        static double M = 0.02896; //Molecular Weight of Dry Air
        static double G = 9.81; //Acceleration due to Gravity
        static double Konstant= - (G * M) / (R * L);
    
        public static double airDensity(double h){
            
            double P = PO * Math.pow(1 + L*h / TO, Konstant);
            double Rho = (P*M)/(R*getAirTemperature(h));
            return Rho;
        }
        
    }
    class Object3DM{
        Shape3DM Form;
        Point3DM Momentum;
        Point3DM Acceleration;
        double CollisionRadius;
    }
    
    class GeneralVector extends Vector{
        public Doodad doodad(int INTA){
            return (Doodad)elementAt(INTA);
        }
        public Plane plane(int INTA){
            return (Plane)elementAt(INTA);
        }
        public Craft craft(int INTA){
            return (Craft)elementAt(INTA);
        }
        public Sprite3D sprite3D(int INTA){
            return (Sprite3D)elementAt(INTA);
        }
        public TempSprite tempSprite(int INTA){
            return (TempSprite)elementAt(INTA);
        }
        public Projectile projectile(int INTA){
            return (Projectile)elementAt(INTA);
        }
        public Gun gun(int INTA){
            return (Gun)elementAt(INTA);
        }
        public Trail trail(int INTA){
            return (Trail)elementAt(INTA);
        }
    }
    class Plane{
        Shape3DM Form;
        Color Tint = Color.white;
        Point3DM Normal;
        double FurthestDistance;
        double BounceFactor = 0.0;
        boolean Finite = true;
        //double[] CollConst = new double[3];
        
        Point3DM[] MidPoints; //Mid Point of Point N+1 and Point N
        Point3DM[] MidNormals; //Outward facing unit parallel to plane perpendicular to line of points N+1 and N
        Point3DM[] EdgeUnits; //Unit Vector from Point N+1 to Point N
        
        
        public Plane(Point3DM[] Corners){
            Form = new Shape3DM();
            MidPoints = new Point3DM[Corners.length];
            MidNormals = new Point3DM[Corners.length];
            EdgeUnits = new Point3DM[Corners.length];
            
            for(int i = 0; i < Corners.length; i++){
                Corners[i].Links = new PointVectorM();
                Corners[i].Links.add(Corners[(i + 1) % Corners.length]);
                Form.BasePoints.add(Corners[i]);
                Corners[i].Base = Form;

                
                
            }
            
            Form.finalizeLinks();
            for(int i = 0; i < Form.Points.length; i++){
                Form.ActualPoints[i] = Form.Points[i].copy();
            }
            Form.Center = new Point3DM(0, 0, 0);
          
            for(int i = 0; i < Form.Points.length; i++){
                Form.Center.add(Form.Points[i], Form.Center);
            }
            
            Form.Center.divide(Form.Points.length, Form.Center);
            
            Utils3DM.center(Form, 'p');
            
            calculateNormal();
            calculateCollisionConstants();
            getFurthestDistance();

        }
        public void calculateCollisionConstants(){
            for(int i = 0; i < Form.ActualPoints.length; i++){
                EdgeUnits[i] = Form.ActualPoints[i].subtract(Form.ActualPoints[(i + 1) % Form.ActualPoints.length]);
                EdgeUnits[i].normalize();
            }
            for(int i = 0; i < Form.ActualPoints.length; i++){
                
                MidPoints[i] = Form.ActualPoints[i].add(Form.ActualPoints[(i + 1) % MidPoints.length]);
                MidPoints[i].divide(2, MidPoints[i]);
                
            }
            for(int i = 0; i < Form.ActualPoints.length; i++){
                
                MidNormals[i] = EdgeUnits[i].crossProduct(Normal);
                MidNormals[i].normalize();
            }
    
        }
        public void doCollision(Doodad Target, Point3DM ImpactSpot, Point3DM[] TempPoints){
            Point3DM EffectiveNormal = TempPoints[0];
            Point3DM Difference = TempPoints[1];
            Target.Form.Center.subtract(ImpactSpot, Difference);
            Difference.copyCoords(EffectiveNormal);
            EffectiveNormal.normalize();
            
            //
            if(Difference.dotProduct(Target.Momentum) < 0){
                EffectiveNormal.multiply(EffectiveNormal.dotProduct(Target.Momentum), Difference);
                Difference.multiply(Target.BounceFactor + 1.0, Difference);
                Target.Momentum.subtract(Difference, Target.Momentum);
            }   
            
        }
        public void doCollision(Craft Target, Point3DM ImpactSpot){
            Point3DM Temp = Target.TempPoints[0];
            Point3DM EffectiveNormal = Target.TempPoints[1];
            Target.Form.Center.subtract(ImpactSpot, EffectiveNormal);
            EffectiveNormal.normalize();
            
            //Push Craft away from Point of Collision
            
            Target.Form.Center.subtract(ImpactSpot, Temp);
            
            EffectiveNormal.multiply(Math.max(0, Temp.length() - Target.CollisionRadius), Temp);
     //       Target.Form.Center.add(Temp, Target.Form.Center);
            
            
            //Cut out movement towards Point of Collision
            if(ImpactSpot.subtract(Target.Form.Center).dotProduct(Target.Momentum) > 0){
                EffectiveNormal.multiply(EffectiveNormal.dotProduct(Target.Momentum), Temp);
                Temp.multiply(BounceFactor + 1.0, Temp);
             
                double RemovedSpeed = Temp.length();
                Target.Momentum.subtract(Temp, Target.Momentum);
             //   System.out.println(Temp.length());
                for(int i = 2; i < RemovedSpeed; i += 5){
                    if(GUI.randomInt(1, 3) == 3){
                        Doodad Debris = createDebris(GUI.randomInt(1, 15), ImpactSpot.add(Utils3DM.getRandomUnit().multiply(Math.random() * 2)), Math.random() * Target.Momentum.length() * 5, (Math.random() > 0.5 ? this.Form.Tint : Target.Form.Tint), 1);
                        Doodads.add(Debris);
                    }
                }
            }else{
                
            }
             
        }
        
        public Point3DM checkCollision(Object3DM Target, Point3DM[] TempPoints){
            if(Target.Form == null){
                System.out.println(12);
                return null;
                
            }
            
            Point3DM Center = Target.Form.Center;
            double CollisionRadius = Target.CollisionRadius;
            Point3DM SurfaceImage = TempPoints[0];
            Point3DM Difference = TempPoints[1];
            
            //Check Bounding Sphere for Triangle
            Center.subtract(Form.Center, Difference);
            if(Difference.length() > CollisionRadius + FurthestDistance){ 
                return null;
            }
            
            //Gets perpendicular projection of center of craft onto infinite plane
            Normal.multiply(Difference.dotProduct(Normal), SurfaceImage);
            Center.subtract(SurfaceImage, SurfaceImage);
            
            //Calculates distance of craft to perpendicular plane
            Center.subtract(SurfaceImage, Difference);
            
            if(Difference.length() > CollisionRadius){
                return null;
            }
            //If plane is infinite, return collision
            if(!Finite){
                return SurfaceImage;
            }
            
            //If close enough, check to see if it collides with the limited plane
            Point3DM P = SurfaceImage;
            Point3DM Temp = TempPoints[2];
            
            boolean Out = false;
            for(int i = 0; i < MidPoints.length; i++){
                MidPoints[i].subtract(P, Difference);
                
                if(Difference.dotProduct(MidNormals[i]) > 0) Out = true;
            }
            
            if(Out == false){
                
                return SurfaceImage.copy();
                
            }
            
            
            
          
            
            //Otherwise check to see if it collides with the edges
            for(int a = 0; a < MidPoints.length; a++){
                SurfaceImage = Utils3DM.lineSphereCollision(Form.ActualPoints[a], Form.ActualPoints[(a + 1) % MidPoints.length], Center, CollisionRadius);
                if(SurfaceImage != null){
                  
                    return SurfaceImage.copy();
                }
                
            }
            //Lastly, check to see if it collides with corners
            for(int a = 0; a < MidPoints.length; a++){
                Center.subtract(Form.ActualPoints[a], Difference);
                if(Difference.length() <= CollisionRadius){
                    return Form.ActualPoints[a].copy();
                }
            }
            
            return null;
        }
        public void calculateNormal(){
            Normal = (Form.Points[1].subtract(Form.Points[0])).crossProduct(Form.Points[2].subtract(Form.Points[0]));
            Normal.normalize();
            
        }
        public void getFurthestDistance(){
            for(int i = 0; i < Form.Points.length; i++){
                FurthestDistance = Math.max(FurthestDistance, Form.Points[i].length());
            }
   
        }
        public void print(Graphics2D Painter, ObserverM Viewer){
            Form.print(Painter, Viewer);
        }
    }
    class Gun{
        Object3DM Base;
        Point3DM[] POS;
        Rotation3D Facing;
        
        String Name;
        
        ProjectileType Shot;
        int Ammo;
        
        Counter ReloadTimer;
        
        
        Counter CooldownCounter;
        public Gun copy(Object3DM base, Point3DM[] pos, Rotation3D facing){
            Gun G = new Gun(Name, base, pos, facing, Shot, Ammo, ReloadTimer.Max, ReloadTimer.BaseIncrement);
            return G;
            
        }
        public Gun(String N, Object3DM base, Point3DM[] pos, Rotation3D facing, ProjectileType shot, int ammo, double maxr, double rr){
           Name = N;
           Base = base;
           POS = pos;
           Facing = facing;
           Shot = shot;
           Ammo = ammo;
           ReloadTimer = new Counter(0, maxr, 0);
           ReloadTimer.setBaseIncrement(rr);
           CooldownCounter = new Counter(0, 0.05, 0.05);
        }
        public void reload(){
            ReloadTimer.increment(ReloadTimer.BaseIncrement * 1.0 / FramesPerSecond);
            ReloadTimer.cap();
            
            if(CooldownCounter.checkBounds() < 1){
                CooldownCounter.increment(1.0 / FramesPerSecond);
            }
            
        }
        public Projectile fire(Object3DM Target){
            if(CooldownCounter.Current < CooldownCounter.Max){
                return null;
            }
            if(ReloadTimer.Current < Shot.FireCost){
                return null;
            }
            Projectile NewShot = null;
            for(int i = 0; i < POS.length; i++){
                if(Ammo > 0 || Ammo == -1337){
                    if(Ammo > 0){
                        Ammo--;
                    }
                    NewShot = Shot.createProjectile(Base, POS[i], Facing, Target);
                    Projectiles.add(NewShot);
                    
                    
                }
            }
            CooldownCounter.increment(-0.1);
            ReloadTimer.increment(-Shot.FireCost);
            return NewShot;
        }
    }
    
    class ProjectileType extends Object3DM{
        //Object3D Stuff
        //Shape3DM Form;
        //Point3DM Momentum;
        //double CollisionRadius;
        
        double Duration;
        boolean Rotating;
        double FireCost;
        double Spread;
        double Damage;
        
        boolean Homes;
        double TurnPerSecond;
        double HomingDelay;
        
        double MaxEffectiveRange;
        double MinEffectiveRange;
        int AIPrefMod;
        
        int Fragments;
        
        double TrailTime;
        int Attachment;
        public ProjectileType(double M, double CR, double FC, double D, boolean R, double S, double DMG, int Fragment){
            Duration = D;
            Momentum = new Point3DM(M, 0, 0);
            CollisionRadius = CR;
            Rotating = R;
            FireCost = FC;
            Spread = S;
            Damage = DMG;
            Fragments = Fragment;
            
        }
        public void setTrail(double TT, int Hook){
            TrailTime = TT;
            Attachment = Hook;
        }
        
        public void setAIData(double MER, double MER2, int AIPreference){
            MaxEffectiveRange = MER;
            MinEffectiveRange = MER2;
            AIPrefMod = AIPreference;
        }
        public void setHoming(double TPS, double SD){
            TurnPerSecond = TPS;
            Homes = true;
            HomingDelay = SD; 
        }
        public void setShape(Shape3DM F){
            Form = F.copy();
        }
        public Projectile createProjectile(Object3DM Base, Point3DM Position, Rotation3D Facing, Object3DM Target){
            Point3DM Direction = new Point3DM(0, 0, 0);
            Facing.getOriginalAxis('x', Direction);
            Projectile P = new Projectile(Base, Form.copy(), Position, Direction.multiply(Momentum.POSX), Duration, Form.Tint);
            
            P.Damage = Damage;
            P.Momentum.add(Utils3DM.getRandomUnit().multiply(Spread * Momentum.POSX), P.Momentum);
            P.Homing = Homes;
            P.Target = Target;
            P.MaxTurn = TurnPerSecond;
            P.Fragments = Fragments;
            P.HomingDelay = new Counter(0, HomingDelay, HomingDelay);
            
            Rotation3D.multiply(Base.Form.Rotation, P.Form.Rotation, P.Form.Rotation);
            P.Form.calcPoints();
            P.BulletTrail = new Trail((P.Homing ? 8 : 3), (Attachment == -1 ? P.Form.Center : P.Form.ActualPoints[Attachment]), P.Form.Tint, TrailTime);
            
            Trails.add(P.BulletTrail);
            return P;
            
        }
    }
    class Projectile  extends Object3DM{
        //Object3D Stuff
        //Shape3DM Form;
        //Point3DM Momentum;
        //double CollisionRadius;
        
        Trail BulletTrail;
        Point3DM LastPosition;
        double Damage = 0;
        double Speed;
        
        Counter Duration;
        boolean Homing;
        double MaxTurn;
        Counter HomingDelay;
        Rotation3D Turn = new Rotation3D();
        
        
        int Fragments;
        
        Object3DM Target;
        
        Object Base;
        public Projectile(Object B, Shape3DM form, Point3DM Center, Point3DM V, double D, Color T){
            Base = B;
            
            if(form == null){Form = new Shape3DM(false, Color.white, Center.POSX, Center.POSY, Center.POSZ);
            }else{Form = form; Form.Center = Center.copy();}
            
            LastPosition = new Point3DM(Center.POSX, Center.POSY, Center.POSZ);
            
            Form.Tint = T;
            
            Momentum = V.copyCoords();
            Speed = Momentum.length();
            Duration = new Counter(0, D, D);
        
            CollisionRadius = 4; //Affects Craft Collision but not Plane
            
        }
        public void setForm(char Switch, double ArgA, double ArgB, double ArgC){
            Utils3DM.setPoints(Form, Switch, ArgA, ArgB, ArgC);
            if(Switch == 'p'){
                Form.Rotation.rotateAround('y', Math.toRadians(-90));
            }
        }
        public void turnProjectile(){
          //  System.out.println(Momentum);
            
            
            
            
            
            
            Point3DM TargetDirection = Target.Form.Center.subtract(Form.Center);
            Point3DM Lead = projectileJudgeLead();
            if(Lead != null){
                TargetDirection.add(Lead, TargetDirection);
            }
           // if(Base == Player)System.out.println(projectileJudgeLead().length());
            Rotation3D.getRotationBetween(Momentum, TargetDirection, MaxTurn * 1.0 / FramesPerSecond, Turn);
            Rotation3D.multiply(Turn, Form.Rotation, Form.Rotation);
            
            Form.Rotation.getOriginalAxis('x', Momentum);
            Momentum.multiply(Speed, Momentum);
           
        }
        public Point3DM projectileJudgeLead(){
            if(Target.Momentum.length() == 0 || Speed == 0){
                return Utils3DM.Origin;
            }
            
            Point3DM TargetUnitMovement = new Point3DM(0, 0, 0);
            Point3DM DifferenceUnit = new Point3DM(0, 0, 0);
            Target.Momentum.copyCoords(TargetUnitMovement);
            
            double TargetSpeed = TargetUnitMovement.length();
            TargetUnitMovement.normalize();
            Target.Form.Center.subtract(Form.Center, DifferenceUnit);
            
            double Distance = DifferenceUnit.length(); 
            DifferenceUnit.normalize();
            
            double Angle = Math.PI - Math.acos(GUI.trigCap(TargetUnitMovement.dotProduct(DifferenceUnit)));
            double DiffAngle = Math.asin(Math.sin(Angle) * TargetSpeed / Speed);
            double Lead = Distance * Math.sin(DiffAngle) / Math.sin(Angle + DiffAngle);
            if(Double.isNaN(Lead)){
                System.out.println("SPOIL2" + Speed / TargetSpeed * Math.sin(Angle));
                return null;
                
            }
            
            return TargetUnitMovement.multiply(Lead);
            
            
        }
        public void move(){
            
           
            Form.Center.copyCoords(LastPosition);
            if(HomingDelay.checkBounds() != -1){
                HomingDelay.increment(-1.0 / FramesPerSecond);
            }
            if(Homing && Target != null && HomingDelay.checkBounds() < 0){
                turnProjectile();
            }
            
            Form.Center.add(Momentum.divide(FramesPerSecond), Form.Center);
       //    System.out.println(this + "\t" + Form.Center);
            
      //   System.out.println(Form.Center + "\t" + Duration);
            Duration.increment(-1.0 / FramesPerSecond);
        }
        public Point3DM checkCollision(Plane Target){
            Point3DM A = Target.Form.Center.subtract(Form.Center);
            Point3DM B = Target.Form.Center.subtract(LastPosition);
            Point3DM Temp = new Point3DM(0, 0, 0);
            if(A.dotProduct(Target.Normal) / B.dotProduct(Target.Normal) > 0){
                
                return null;
                
            }else{
                
                Target.Normal.subtract(Target.Normal.multiply(B.dotProduct(Target.Normal)), B);
                Form.Center.add(B, B);
                if(!Target.Finite){
                    return B;
                }
                Target.MidPoints[0].subtract(B, A);
                Temp.POSX = A.dotProduct(Target.MidNormals[0]);
                Target.MidPoints[1].subtract(B, A);
                Temp.POSY = A.dotProduct(Target.MidNormals[1]);
                Target.MidPoints[2].subtract(B, A);
                Temp.POSZ = A.dotProduct(Target.MidNormals[2]);

                if(Temp.POSX <= 0 && Temp.POSY <= 0 && Temp.POSZ <= 0){
                    return B;
                }
            }
            
            return null;
        }
        public void doCollision(Plane Target, Point3DM ImpactSpot){
            Duration.setTo(0);
            Homing = false;
            
         //   System.out.println("BOOM PLANESHOT");
          //  Explosions.add(new TempSprite(new Sprite3D(Form, ImpactSpot, Tint), 1, Utils3DM.Origin));
            for(int i = 0; i < Fragments; i++){
                Doodad Debris = createDebris(GUI.randomInt(1, 15), ImpactSpot, Momentum.length() * DebrisSpeedMultiplier, (Math.random() > 0.5 ? this.Form.Tint : Target.Form.Tint), 1);
                
                Doodads.add(Debris);
            }
            //Momentum = Utils3DM.Origin;
        }
        public Point3DM checkCollision(Object3DM Target){
            if(Target == Base){
                return null;
            }
            Point3DM Impact = Utils3DM.lineSphereCollision(Form.Center, LastPosition, Target.Form.Center, Target.CollisionRadius + CollisionRadius);
            
            return Impact;
        }
        public void doCollision(Craft Target, Point3DM ImpactSpot){
            Duration.setTo(0);
            Homing = false;
            for(int i = 0; i < Fragments; i++){
                Doodad Debris = createDebris(GUI.randomInt(1, 15), ImpactSpot, Math.random() * (Target.Momentum.subtract(Momentum)).length() * DebrisSpeedMultiplier, (Math.random() > 0.5 ? this.Form.Tint : Target.Form.Tint), 1);
                Doodads.add(Debris);
            }
            //Momentum = Utils3DM.Origin;
            
         //   Explosions.add(new TempSprite(new Sprite3D(Form, ImpactSpot, Tint), 1, Utils3DM.Origin));
            
            //System.out.println("BOOM HEADSHOT");
        }
        public void print(Graphics2D Painter, ObserverM View){
            Form.print(Painter, View);
        }
    }
    
    class TempSprite{
        Sprite3D Form;
        
        Point3DM Momentum;
        Counter Duration;
        
        public TempSprite(Sprite3D F, double D, Point3DM M){
            Form = F;
            Duration = new Counter(0, D, D);
            Momentum = M.copyCoords();

        }
        public void move(){
            Form.ActualCenter.add(Momentum.divide(FramesPerSecond), Form.ActualCenter);
            Duration.increment(-1.0 / FramesPerSecond);
        }
        public void print(Graphics2D Painter, ObserverM Viewer){
            
            Form.print(Painter, Viewer);
        }
    }

    class Doodad extends Object3DM{
        //Object3D Stuff
        //Shape3DM Form;
        //Point3DM Momentum;
        //double CollisionRadius;
        
        Counter Duration;
        boolean Gravity;
        Color Tint;
        
        double BounceFactor = 1.0;
        public Doodad(boolean G, Shape3DM S, double Dur, Point3DM Pos, Point3DM Mom, Color T, double Size){
            Gravity = G;
            Form = S.copy();
            CollisionRadius = Size;
            Duration = new Counter(0, Dur, Dur);
            Momentum = Mom.copy();
            Tint = T;
        }
        public Doodad(boolean G, char F, double Dur, Point3DM Pos, Point3DM Mom, Color T, double Size){
            Gravity = G;
            Form = new Shape3DM(false, Color.white, Pos.POSX, Pos.POSY, Pos.POSZ);
            switch(F){
                case 't':
                    Utils3DM.setPoints(Form, 't', 1 * Size, 1 * Size, 1 * Size);
                    break;
                case 'p':
                    Utils3DM.setPoints(Form, 'p', 1 * Size, 1, 0);
                case 'c':
                    Utils3DM.setPoints(Form, 'c', 1 * Size, 1 * Size, 1 * Size);
                break;
            }
            CollisionRadius = Size;
            Duration = new Counter(0, Dur, Dur);
            Momentum = Mom.copy();
            Tint = T;
        }
        public void move(){
            if(Gravity){
                Momentum.add(Utils3DM.Vectork.multiply(-10.0/FramesPerSecond), Momentum);
             //   System.out.println(Momentum);
            }
            Form.Center.add(Momentum.divide(FramesPerSecond), Form.Center);
            Duration.increment(-1.0 / FramesPerSecond);
            
        }
       
        public void print(Graphics2D Painter, ObserverM Viewer){
            Form.print(Painter, Viewer);
        }
    }
    class Trail{
        Shape3DM Form;
        Counter TrailCounter;
        Point3DM Target;
        int TrailLength;
        boolean Active;
        Counter DeathCounter;
        public Trail(int PointCount, Point3DM Targ, Color T, double TotalDuration){
            Form = new Shape3DM(true, T, Targ.POSX, Targ.POSY, Targ.POSZ);
            Utils3DM.setPoints(Form, 'h', PointCount, 0, 0);
            DeathCounter = new Counter(0, 3, 3);
            for(int i = 0; i < PointCount; i++){
                Form.Center.copyCoords(Form.ActualPoints[i]);
            }
            TrailLength = PointCount;
            Form.MaxDistance = 0;
            TrailCounter = new Counter(0, TotalDuration / PointCount, TotalDuration / PointCount);
            Target = Targ;
            Active = true;
            
        }
        public void reset(){
            for(int i = 0; i < TrailLength; i++){
                Target.copyCoords(Form.ActualPoints[i]);
            }
          //  TrailLength = Form.PointCount;
        }
        public boolean finished(){
            if(DeathCounter.checkBounds() < 0){
                return true;
            }
            return false;
        }
        public void disable(){
            Active = false;
        }
        public void enable(){
            if(!Active)reset();
            Active = true;
            
        }
        public void chase(){
            
            Target.copyCoords(Form.ActualPoints[0]); //First Point
            
            
            TrailCounter.increment(-1.0 / FramesPerSecond);
            
            if(TrailCounter.checkBounds() >= 0){
                
                double LastDistance =(TrailCounter.Current) / (TrailCounter.Current + 1.0/FramesPerSecond);
             
                Form.ActualPoints[TrailLength - 1].subtract(Form.ActualPoints[TrailLength - 2], Form.ActualPoints[TrailLength - 1]);
               
                Form.ActualPoints[TrailLength - 1].multiply(LastDistance, Form.ActualPoints[TrailLength - 1]);
                
                Form.ActualPoints[TrailLength - 1].add(Form.ActualPoints[TrailLength - 2], Form.ActualPoints[TrailLength - 1]);
               
                return;
            }
              
            TrailCounter.Current = TrailCounter.Max;
            for(int i = TrailLength - 1; i > 0; i--){
                Form.ActualPoints[i - 1].copyCoords(Form.ActualPoints[i]);   
            }
            Target.copyCoords(Form.Center);
            Form.MaxDistance = Form.Center.distanceTo(Form.ActualPoints[TrailLength - 1]);
                
            if(Form.MaxDistance == 0){
                DeathCounter.increment(-1);
            }
            
        }
        public void print(Graphics2D Painter, ObserverM Viewer){
            if(Form.MaxDistance > -0.01 && Active){
                Form.print(Painter, Viewer);
            }
        }
        
    }
    /*\
     *f-16
     *Dimensions 14.5m X 9.5m X 5m
     *Weight 8000kg-12000kg (10000kg average)
     *Max Speed 2150km/h = 600m/s
     *Thrust 24000lb = 110000N 
     *Acceleration 110000N / 10000kg = 11m/s/s
     *Rate of Climb 15420M/minute = 250m/s
     *Cruising speed: 577 m/h = 256 m/s
    \*/

    class Craft extends Object3DM{
        
        //Object3D Stuff
        //Shape3DM Form;
        //Point3DM Momentum;
        //double CollisionRadius;
        
        
        //Point3DM Acceleration;
        
        //Extra Graphics
        Trail[] Trails;
        
        Point3DM[] AfterburnerPoint;
        
  //      Shape3DM RightWingTrail;
     //   Shape3DM LeftWingTrail;
        
        
        //Data Constants
        double Mass = 10000; //Kilograms
        double Thrust = 90000; //Newtons
        double AfterburnerBonus = 0.6; //Fraction of Thrust
        double Gravity = 10; //Meters/Second/Second
        double MaxSpeed = 650; //Meters/Second
        double CruiseSpeed = 400; //Meters/Second
        double WingArea = 28; //Square Meters
        double WingSpan = 9.8; //Meters
        double AspectRatio = WingSpan * WingSpan / WingArea;
        
        //Controls
        
        double Throttle;
        double TargetThrottle;
        double ThrottleIncrement = 5;
        
        boolean Afterburner;
        Gun SelectedGun;
        boolean Telemetry = false;
        
        //Limits and Constants
        
        Counter AfterburnerTimer = new Counter(0, 10, 10);
        
        double HeightAboveGround = 4;
        double MaxElevatorControl = Math.toRadians(25);
        double StandardSpeed = 240;
        
        
        double StallAngle = Math.toRadians(20); //Angle at which CL starts dropping
        double MaxCL = 1.6; //Maximum Coefficient of Lift
        double StallSpeed = 0.8; //Rate at which lift drops to 0 across AoA
        
        double InducedDragE = 0.9084; //Divisor fudge factor for induced drag
        
        double CoefficientOfParasiticDrag = 0.0208; //Coefficient of drag
        
        double RollSpeedMultiplier = Math.toRadians(Math.sqrt(90)); //Roll angle at ideal speed at sea level per second
        double GroundDragMultiplier = 5; //not used
        double TurnSpeedMultiplier = 0.75; //Multiplier to increase or decrease Alignment Speed
        
        int TrailLength = 8; //Length of trails in sections
        double FlightCeiling = 15000; //Maximum engine height
        Counter TrailCounter = new Counter(0, 0.5 / TrailLength, 0.5 / TrailLength);
        //Calculated Values
        
        double Speed; //Absolute value of momentum
        double Mach; //Speed in terms of the Speed of Sound
        double AngleOfAttack; //Angle between facing of craft and movement in crafts plane of symmetry
        double CoefficientOfLift; //Coefficient of the lift generated
        
        double CoefficientOfInducedDrag; //Coefficient of lift induced drag
        double Lift; //Final Lift value
        
        
        //Keyboard Controls
        boolean Forward;
        boolean Back;
        boolean Left;
        boolean Right;
        boolean RudderLeft;
        boolean RudderRight;
        
        //Mouse Controls
        double MouseVertical;
        double MouseHorizontal;
        double ElevatorControl = 0;
        //Weapons
        GeneralVector Guns = new GeneralVector();
        
        //AI
        Object3DM DecisionTarget; //Whatever AI is targetting
        char State; //What AI is doing
        double MinimumSafeHeight = 400; //Height below which AI turns up
        double MaximumSafeHeight = 9600; //Height above which AI turns down
        double HeightReflectionTime = 400; //Height from border across which AIs target is reflected across.
        double IdealSpeed = 240; //Ideal speed AI aims to achieve
        double OverSpeedDec = 0.2; //Rate at which AI moves throttle to 0 or 10 across Speed
        
        
        //Attack Data
        char AState = 'a'; //Assuming AI is attacking, attack or back off
        double TurnDistance = 250; //Distance at which AI turns away
        double ResumeDistance = 1250; //Distance at which AI turns back
        Counter RetreatTimer = new Counter(0, 20, 0); //Counter to prevent AI from retreating indefinately
      
        Counter AttackTimer = new Counter(0, 30, 0); //Counter to prevent AI from attacking indefinately
        boolean Firing[] = new boolean[2];; //Whether or not target is unreliable
        
        //Firing Data
        double AngleToFire = Math.toRadians(1); //Angle between sights and target which AI will fire
        Point3DM AccelerationLead = new Point3DM(0, 0, 0); //AI compensating for targets acceleration
        
        
        Counter Fudge = new Counter(-0.5, 0.5, 0);
        
        double FudgeDecayMod = 0.015;
        Projectile TrackedShot;
        
        //Evasion Data
        Point3DM EvasionTarget = new Point3DM(0, 0, 0);
        Counter EvasionTCounter = new Counter(-12, 6, 6);
        
        
        
        double EvadeTriggerRadius = 30;
        boolean Evading = false;
        double EvasionIncrement = 3;
        //Follow Data
        double CircleDistance;
        
        //Control Data
        
        Point3DM ControlTarget = new Point3DM(0, 0, 0);
        Point2D.Double AIMouseTarget = new Point2D.Double(0, 0);
        
        boolean UseAI = false;
        
        double MaxAngleTurn = 1.5;
        double RollResponsiveness = 1;
        //double MaxRollSpeedAngle = 1.5;
        
        
        //Temp Calculation Points
        Point3DM[] TempPoints = new Point3DM[6];
        Rotation3D[] TempRotations = new Rotation3D[6];
        //Max Speeds
        //~600 9
        //~565 8
        //~530 7
        //~490 6
        //~450 5
        //~400 4
        //~345 3
        //~280 2
        //~200 1
        public void setOrder(Object3DM Target, char St){
            DecisionTarget = Target;
            State = St;
        }
        public void initAI(){
            UseAI = true;
            Fudge.setBaseIncrement(0.1);
        }
        public Craft(Point3DM Position){
            for(int i = 0; i < TempPoints.length; i++){
                TempPoints[i] = new Point3DM(0, 0, 0);
            }
            for(int i = 0; i < TempRotations.length; i++){
                TempRotations[i] = new Rotation3D();
            }
            Form = new Shape3DM(false, Color.white, Position.POSX, Position.POSY, Position.POSZ);
            Acceleration = new Point3DM(0, 0, 0);
            
            Utils3DM.setPoints(Form, 'p', 0, 0, 0);
            
            Form.Points[0].setLocation(15, 0, 0);
            Form.Points[1].setLocation(0, 5, 0);
            Form.Points[2].setLocation(0, -5, 0);
            Form.Points[3].setLocation(0, 0, 3);
            
            /*
            Utils3DM.setPoints(Form, 's', 0, 0, 0);
            
            Form.Points[0].setLocation(15, 0, 0);
            Form.Points[1].setLocation(0, 5, 0);
            Form.Points[2].setLocation(0, -5, 0);
            Form.Points[3].setLocation(0, -2, 2);
            Form.Points[4].setLocation(0, 2, 2);
            
            
            */
            Utils3DM.center(Form, 'p');
            
            
            Trails = new Trail[4];
            
            //RightWingTrail = new Shape3DM(true, Color.white, 0, 0, 0);
            //LeftWingTrail = new Shape3DM(true, Color.white, 0, 0, 0);
        //    Utils3DM.setPoints(RightWingTrail, 'h', TrailLength, 0, 0);
        //    Utils3DM.setPoints(LeftWingTrail, 'h', TrailLength, 0, 0);
            
            
            Momentum = new Point3DM(0, 0, 0);
            Form.Rotation.rotateAround('z', Math.toRadians(90));
            groundEffect();
            Form.calcPoints();
            
            initTrails();
        //    updateTrails();
            
            
            AfterburnerTimer.setBaseIncrement(0.25);
            
            CollisionRadius = 4;
            for(int i = 0; i < Firing.length; i++){
                Firing[i] = true;
            }
            
            Guns.add(MachineGun.copy(this, new Point3DM[] {Form.ActualPoints[1], Form.ActualPoints[2]}, Form.Rotation));
            Guns.add(Cannon.copy(this, new Point3DM[] {Form.ActualPoints[1], Form.ActualPoints[2]}, Form.Rotation));
            SelectedGun = Guns.gun(0);
           // Guns.add(new Gun(this, new Point3DM[] {Form.ActualPoints[1], Form.ActualPoints[2]}, Form.Rotation, MachineGun, -1337, 30, 5));
        }
        public void initTrails(){
            Trails[0] = new Trail(7, Form.ActualPoints[1], Color.white, 0.5);
            Trails[1] = new Trail(7, Form.ActualPoints[2], Color.white, 0.5);
            
            
            AfterburnerPoint = new Point3DM[2];
            AfterburnerPoint[0] = new Point3DM(0, 0, 0);
            AfterburnerPoint[1] = new Point3DM(0, 0, 0);
            AfterburnerPoint[0].setToMidpoint(Form.Center, Form.ActualPoints[1]);
            AfterburnerPoint[1].setToMidpoint(Form.Center, Form.ActualPoints[2]);
            
            Trails[2] = new Trail(8, AfterburnerPoint[0], Color.orange, 0.6);
            Trails[3] = new Trail(8, AfterburnerPoint[1], Color.orange, 0.6);
        }
 
        public void updateTrails(){
            AfterburnerPoint[0].setToMidpoint(Form.Center, Form.ActualPoints[1]);
            AfterburnerPoint[1].setToMidpoint(Form.Center, Form.ActualPoints[2]);
            for(int i = 0; i < Trails.length; i++){
                Trails[i].chase();
            }
            
        }
        
        public void moveWeapons(){
            for(int i = 0; i < Guns.size(); i++){
                Guns.gun(i).reload();
                
            }
        }
        public void move(){
            Acceleration.POSX = 0;
            Acceleration.POSY = 0;
            Acceleration.POSZ = 0;
            
            moveWeapons();
            controls();
            
            setAngleOfAttack();
            
            setCoefficientOfLift();
            
            throttle();
            
            
            
            lift();
            
       
            
            drag();
            
            inducedDrag();
            
            gravity();
            
            groundEffect();
            
            
            Acceleration.divide(FramesPerSecond, TempPoints[0]);
            Momentum.add(TempPoints[0], Momentum);
            Momentum.divide(FramesPerSecond, TempPoints[1]);
            Form.Center.add(TempPoints[1], Form.Center);
            Speed = Momentum.length();
            Mach = Speed / World.getSpeedofSound(Form.Center.POSZ);
            
            
           
            if(Speed> 0){
                alignCraft();
            }
            
           // System.out.println();
        }
        public void setAngleOfAttack(){
            if(Speed > 0){
                Momentum.copyCoords(TempPoints[0]);
               
                Form.Rotation.getOriginalAxis('y', TempPoints[1]);
                TempPoints[1].multiply(TempPoints[0].dotProduct(TempPoints[1]), TempPoints[2]);
                TempPoints[0].subtract(TempPoints[2], TempPoints[0]);
            
                TempPoints[0].normalize();
            
            
            
                Form.Rotation.getOriginalAxis('z', TempPoints[1]);
                double DotProduct = -TempPoints[0].dotProduct(TempPoints[1]);
                DotProduct = GUI.trigCap(DotProduct);
                

            
                AngleOfAttack = Math.asin(DotProduct);
              
            }
        }
        public void setCoefficientOfLift(){
        		
            CoefficientOfLift = (MaxCL * AngleOfAttack / StallAngle); 
            if(AngleOfAttack > StallAngle){
                CoefficientOfLift = -MaxCL * GUI.square((Math.toDegrees(AngleOfAttack - StallAngle)) * StallSpeed) + MaxCL;
                
                CoefficientOfLift = Math.max(0, CoefficientOfLift);                
            } else
            if(AngleOfAttack < -StallAngle){
                CoefficientOfLift = MaxCL * GUI.square((Math.toDegrees(AngleOfAttack + StallAngle)) * StallSpeed) - MaxCL;
                CoefficientOfLift = Math.min(0, CoefficientOfLift);                
            }
            //Correct for supersonic flight
            
            if(Mach > 0.8){
                double CCL = 0.6 * Math.sin(10 * Mach + 6) + 1.9 - 0.6;
                if(Mach > 1 && (CCL > 0.5 || Mach > 1.4)){
                    CCL = 0.5;
                }
                CoefficientOfLift = CoefficientOfLift * CCL;
            }
            //System.out.println(CoefficientOfLift);
                //
            //if(this==Player)System.out.println(CoefficientOfLift+"\t"+Math.toDegrees(AngleOfAttack));
        }
        public void alignCraft(){
            if(Speed > 1){
                Point3DM Facing = TempPoints[0];
                Facing.setLocation(1, 0, 0);
                TempRotations[4].rotateAround('y', ElevatorControl);
                TempRotations[4].transform(Facing, Facing);
                Form.Rotation.transform(Facing, Facing);
            
                double Angle = Momentum.dotProduct(Facing) / Speed;
                Angle = GUI.trigCap(Angle);
                Angle = Math.acos(Angle);
                double PercentClosedPerSecond = Speed * Speed / StandardSpeed * World.airDensity(Form.Center.POSZ) * TurnSpeedMultiplier;
                PercentClosedPerSecond = PercentClosedPerSecond / (1 + PercentClosedPerSecond);
                Angle = Angle * (1 - Math.pow(1-PercentClosedPerSecond, 1.0 / FramesPerSecond));
                
                Rotation3D.getRotationBetween(Facing, Momentum, Angle, TempRotations[4]);
                Rotation3D.multiply(TempRotations[4], Form.Rotation, Form.Rotation);
            }
            
               
            
        }
        
        
        public void inducedDrag(){
           //Cdi =    CL²   / (p e AR)
           //Di = Cdi x S x ½rV2
            if(Speed > 0){

                CoefficientOfInducedDrag =  CoefficientOfLift * CoefficientOfLift / Math.PI / InducedDragE / AspectRatio;
                
                
                //Correct for supersonic flight
                if(Mach > 0.8){
                    CoefficientOfInducedDrag = CoefficientOfInducedDrag * (1 + 2.5 * (Mach - 0.8));
                }
                
                //
                double InducedDrag = CoefficientOfInducedDrag * WingArea * 0.5 * getAirDensity() * Speed * Speed;
                if(Telemetry && this==Player)System.out.println("Di"+InducedDrag / Mass);
                Momentum.copyCoords(TempPoints[0]);
                TempPoints[0].normalize();
                TempPoints[0].multiply(-InducedDrag / Mass, TempPoints[0]);
                Acceleration.add(TempPoints[0], Acceleration);
         
            }

        }
        public void drag(){
            //Drag = Coefficient * Wing Area * 1/2(Air Density)(Velocity Squared)
            if(Speed > 0){
                
                
                //Correct for supersonic flight
                double RealCD = CoefficientOfParasiticDrag;
                
                if(Mach > 1.1){
                    RealCD = 2.5 * RealCD * (1 + (Mach - 1.2) * (-0.3));
                }else if(Mach > 0.8){
                    RealCD = RealCD * (1 + (Mach - 0.8) * (5));
                }
                
                //
                Momentum.copyCoords(TempPoints[0]);
                TempPoints[0].normalize();
                double Drag = RealCD * WingArea * 0.5 * getAirDensity() * Speed * Speed;
                if(Telemetry && this==Player)System.out.println("Dp"+Drag / Mass + "\t" + RealCD);
                TempPoints[0].multiply(-Drag / Mass, TempPoints[0]);
                Acceleration.add(TempPoints[0], Acceleration);
           //     System.out.println("Drag"+Drag);
            }
            /*
            
           
            
            
            Temp = Temp.multiply(Momentum.length());
            
            Acceleration.add(Temp, Acceleration);
            
             */
        }
        public double getAirDensity(){
          
            return World.airDensity(Form.Center.POSZ);
        }
        public void lift(){
            //Lift = Coefficient * Wing Area * 1/2(Air Densicty)(Velocity Squared)
            
            if(Speed > 0){
                double Velocity = 0;
                Point3DM Temp = TempPoints[0];
                Form.Rotation.getOriginalAxis('y', Temp);
                
                Point3DM MomentumCopy = TempPoints[1];
                Momentum.copyCoords(MomentumCopy);
                Temp.multiply(Momentum.dotProduct(Temp), Temp);
                MomentumCopy.subtract(Temp, MomentumCopy);
                
                Velocity = MomentumCopy.length();
                
                
                Lift = CoefficientOfLift * WingArea * 0.5 * getAirDensity() * (Velocity) * (Velocity) * 1;
                
                if(Telemetry && this==Player)System.out.println("Lift"+Lift / Mass + "\t" + CoefficientOfLift);
                if(Telemetry && this==Player)System.out.println("Velocity" + Speed);

                Form.Rotation.getOriginalAxis('y', Temp);
                
                Momentum.crossProduct(Temp, MomentumCopy);
                
                MomentumCopy.normalize();
               // MomentumCopy.multiply(10, ShapeX.Center);
                //ShapeX.Center.add(Form.Center, ShapeX.Center);
                //Temp.multiply(10, ShapeY.Center);
                //ShapeY.Center.add(Form.Center, ShapeY.Center);
                MomentumCopy.multiply(Lift / Mass, MomentumCopy);
                //System.out.println(Temp);
                //System.out.println(MomentumCopy);
                Acceleration.add(MomentumCopy, Acceleration); 
                
            }
           
            
        }
        public void gravity(){
            
            
                TempPoints[0].setLocation(0, 0, -1);
                TempPoints[0].multiply(World.getGravity(), TempPoints[0]);
            

                Acceleration.add(TempPoints[0], Acceleration);
                if(Telemetry && this==Player)System.out.println("G"+World.getGravity());
            
        }
        public void groundEffect(){
            if(Form.Center.POSZ <= HeightAboveGround){
                Form.Center.POSZ = HeightAboveGround;
                Momentum.POSZ = Math.max(Momentum.POSZ, 0);
                Acceleration.POSZ = Math.max(Acceleration.POSZ, 0);
                if(Speed > 0){
                    Utils3DM.Vectork.multiply(Momentum.dotProduct(Utils3DM.Vectork));
                    
                    Momentum.subtract(TempPoints[0], TempPoints[0]);
                    TempPoints[0].normalize();
                    TempPoints[0].multiply(-Math.min(GroundDragMultiplier, TempPoints[0].length()), TempPoints[0]);
                    Acceleration.add(TempPoints[0], Acceleration);

                }
            }
            
        }
        public double getThrust(){
            
            if(Form.Center.POSZ > FlightCeiling){
                return 0;
            }else{
                

                return Thrust * Throttle / 10;
                
            }
        }
        public void throttle(){
            if(Throttle > 0){
                if(Throttle > 10){ 
                    Throttle = 10;
                }
                if(Throttle < 0){
                    Throttle = 0;
                }
                
                Form.Rotation.getOriginalAxis('x', TempPoints[0]);
                
                TempPoints[0].multiply(getThrust(), TempPoints[0]);
                //if(this==Player)System.out.println(AfterburnerTimer.Current);
                if(Afterburner && AfterburnerTimer.checkBounds() >= 0){
                    
                    
                    Trails[2].enable();
                    Trails[3].enable();
                    AfterburnerTimer.increment(-1.0 / FramesPerSecond);
                    AfterburnerTimer.cap();
                    TempPoints[0].multiply(1 + AfterburnerBonus, TempPoints[0]);
                }else if(!Afterburner){
                    Trails[2].disable();
                    Trails[3].disable();
                    
                    AfterburnerTimer.increment(AfterburnerTimer.BaseIncrement / FramesPerSecond);
                    AfterburnerTimer.cap();
                }else{
                    Trails[2].disable();
                    Trails[3].disable();
                }
                
                if(Telemetry && this==Player)System.out.println("Thr"+TempPoints[0].length() / Mass);
                Acceleration.add(TempPoints[0].divide(Mass), Acceleration);
            }
        }
        public void controls(){
            MouseHorizontal = GUI.trigCap(MouseHorizontal);
            MouseVertical = GUI.trigCap(MouseVertical);
            
            //Horizontal Roll Control
            
            
            TempRotations[0].rotateAround('x', -MouseHorizontal * RollSpeedMultiplier * Math.sqrt(Momentum.length()) * World.airDensity(Form.Center.POSZ) * 1.0 / FramesPerSecond);
          //  Rotation3D.multiply(View.Rotation, TempRotations[0], View.Rotation);
            Rotation3D.multiply(Form.Rotation, TempRotations[0], Form.Rotation);
            
            //Elevator Control
            ElevatorControl = MaxElevatorControl * -MouseVertical;
            
            //Throttle Control
            if(Throttle < TargetThrottle){
                Throttle = Math.min(TargetThrottle, Throttle + ThrottleIncrement / FramesPerSecond);
            }else if(Throttle > TargetThrottle){
                Throttle = Math.max(TargetThrottle, Throttle - ThrottleIncrement / FramesPerSecond);
            }
            
        }
        
        public void AITrackShots(){
            if(TrackedShot == null){return;}
            if(TrackedShot.Duration.Current <= 0 || DecisionTarget.Form.Center.subtract(TrackedShot.Form.Center).dotProduct(TrackedShot.Momentum) < 0){
                if(DecisionTarget.Momentum.dotProduct(TrackedShot.Form.Center.subtract(DecisionTarget.Form.Center)) < 0){
                    //shot was behind
                    Fudge.increment(Fudge.BaseIncrement);
                }else{
                    //shot was in front
                    Fudge.increment(-Fudge.BaseIncrement);
                }
                Fudge.cap();
//                AIHandleTrackedShot(TrackedShot);
                TrackedShot = null;
            }
            
        }
        public boolean isFiring(){
            for(int i = 0; i < Firing.length; i++){
                if(!Firing[i]){
                    return false;
                }
            }
            return true;
        }
        public void AIDecisions(){
            Point3DM Difference = TempPoints[0];
            Point3DM Target = TempPoints[1];
            DecisionTarget.Form.Center.copyCoords(Target);
            Point3DM TempPoint = TempPoints[2];
            
            Form.Center.subtract(Target, Difference);
            
            Firing[0] = true;
            Firing[1] = true;
            Fudge.increment(-GUI.sign(Fudge.Current) * FudgeDecayMod * 1.0 / FramesPerSecond);
            switch((Evading ? 'e' : State)){
                case 't': //Take Off
                    Form.Center.add(Momentum, ControlTarget);
                    break;
                case 'e': //Evading Target
                    EvasionTCounter.increment(-1.0/FramesPerSecond);
                    EvasionTarget.copyCoords(ControlTarget);
                    
                    
                    if(EvasionTCounter.Current < 0){
                        Evading = false;
                    }
                    Firing[1] = false;
                    break;
                case 'a': // Attacking Target
                    
                    if(AState == 'a'){ //if Attacking
                        
                        Target.copyCoords(ControlTarget); //Set Target
                        
                        
                        if(isFiring()){
                            
                            TempPoint = AIJudgeLead(DecisionTarget, SelectedGun.Shot.Momentum.POSX);
                        
                        
                            if(TempPoint == null){ 
                                System.out.println("MOMENTUMSPOIL");
                                
                            }else{
                                TempPoint.multiply(1.0 + Fudge.Current, TempPoint);
                                ControlTarget.add(TempPoint, ControlTarget);
                            }
                            if(SelectedGun.Shot.Homes){TempPoint = Utils3DM.Origin.copyCoords();}
                            else{TempPoint = AIFudgeLead(DecisionTarget, ControlTarget, SelectedGun.Shot.Momentum.POSX);}
                            if(TempPoint == null){
                                System.out.println("ACCELERATIONSPOIL");
                            }else{
                                ControlTarget.add(TempPoint, ControlTarget);
                            }
                        }
                        if(Difference.length() < ResumeDistance){
                            AttackTimer.increment(1.0 / FramesPerSecond);
                        }
                        if(Difference.length() < TurnDistance || AttackTimer.Current > AttackTimer.Max){ //If too close
                            System.out.println("RETREATING");//begin retreat 
                            beginRetreat(TempPoint);
                        }
                        
                    }else if(AState == 'r'){ //if Retreating
                        //Count Retreat Time
                        RetreatTimer.increment(1.0 / FramesPerSecond);
                        
                        if(RetreatTimer.Current> RetreatTimer.Max){//If retreating for too long
                            System.out.println("RETREATING");
                            //begin retreat
                            beginRetreat(TempPoint);
                        }
                        
                        if(Difference.length() > ResumeDistance ){ //if far enough
                            //attack
                            
                            System.out.println("ATTACKING");
                            AState = 'a';
                            Firing[0] = false;
                            AttackTimer.increment(-AttackTimer.Max);
                            RetreatTimer.increment(-RetreatTimer.Max);
                        }
                    }
                    
                    break;
                case 'f':
                    
                    break;
                
            }
            
            //Avoid Crashing
            
            if(ControlTarget.POSZ < MinimumSafeHeight && Form.Center.POSZ < MinimumSafeHeight + HeightReflectionTime){ControlTarget.POSZ = Math.min(MinimumSafeHeight + HeightReflectionTime, MinimumSafeHeight * 2 - ControlTarget.POSZ); Firing[0] = false;}
            if(ControlTarget.POSZ > MaximumSafeHeight && Form.Center.POSZ > MaximumSafeHeight - HeightReflectionTime){ControlTarget.POSZ = Math.max(MaximumSafeHeight - HeightReflectionTime, MaximumSafeHeight * 2 - ControlTarget.POSZ); Firing[0] = false;}
        //    System.out.println(Form.Center.POSZ);
            if(Form.Center.POSZ < MinimumSafeHeight){
                Form.Rotation.getOriginalAxis('x', ControlTarget);
                double Diagonal = Math.sqrt(ControlTarget.POSX * ControlTarget.POSX + ControlTarget.POSY * ControlTarget.POSY);
                ControlTarget.POSZ = Diagonal; 
                ControlTarget.normalize();
                ControlTarget.multiply(1000, ControlTarget);
                //System.out.println(ControlTarget);
                ControlTarget.add(Form.Center, ControlTarget);
                Firing[0] = false;
            }
           // System.out.println(AState);;
            
        }
       public void beginRetreat(Point3DM TempPoint){
            this.Form.Center.copyCoords(ControlTarget);
            TempPoint = Utils3DM.getRandomUnit();
            TempPoint.multiply(ResumeDistance * 10000, TempPoint);
            if(GUI.randomInt(1, 4) == 1){
                
                TempPoint.POSX /= 10;
                TempPoint.POSY /= 10;
                if(Math.random() * Form.Center.POSZ / 1000 > 2.5){
                    TempPoint.POSZ = -Math.abs(TempPoint.POSZ);
                }
            }
            ControlTarget.add(TempPoint, ControlTarget);
        
            AState = 'r';
            AttackTimer.increment(-AttackTimer.Max);
            RetreatTimer.increment(-RetreatTimer.Max);
        }
        public void manualFire(){
            if(this == Player && SpaceDown){
                Player.SelectedGun.fire(DecisionTarget);
            }
        }
        public void AIPickWeapon(){
            double Distance = ControlTarget.subtract(this.Form.Center).length();
            int Stickiness = 10; //Reluctance to change weapons
            int Total = 100; //Total Preference
            int[] GunPriorities = new int[Guns.size()];
            for(int i = 0; i < GunPriorities.length; i++){
                GunPriorities[i] = Total;
                
                GunPriorities[i] += Guns.gun(i).Shot.AIPrefMod;
                GunPriorities[i] = (int)(GunPriorities[i] * Guns.gun(i).ReloadTimer.Current / Guns.gun(i).ReloadTimer.Max);
                
                
                if(Guns.gun(i) == SelectedGun){
                    GunPriorities[i] = GunPriorities[i] + Stickiness;
                }
                
                if(Guns.gun(i).Shot.MaxEffectiveRange < Distance || Guns.gun(i).Shot.MinEffectiveRange > Distance){
                    GunPriorities[i] = 0;
                }
                
                if(Guns.gun(i).Shot.FireCost > Guns.gun(i).ReloadTimer.Current){
                    GunPriorities[i] = 0;
                }
            }
            int Max = 0;
            for(int i = 0; i < GunPriorities.length; i++){
                if(GunPriorities[i] > Max){
                    Max = GunPriorities[i];
                    SelectedGun = Guns.gun(i);
                }
            }
            
        }
        public void AIJudgeFire(){
            if(this.State != 'a' || this.AState != 'a'){
                return;
            }
            for(int i = 0; i < Firing.length; i++){
                if(!Firing[i]){
                    return;
                }
            }
            Point3DM TempPoint = TempPoints[0];
            Point3DM Difference = TempPoints[1];
            ControlTarget.subtract(Form.Center, Difference);
          
            Form.Rotation.getOriginalAxis('x', TempPoint);
            
            if(Math.acos(GUI.trigCap(TempPoint.dotProduct(Difference) / Difference.length())) < AngleToFire){//If close enough fire
                
                    
                Projectile NewShot = SelectedGun.fire(DecisionTarget);
                if(TrackedShot == null && NewShot != null){
                    TrackedShot = NewShot;
                }
            }
        }
        public Point3DM AIFudgeLead(Object3DM Target, Point3DM MomentumPosition, double ProjectileSpeed){
            if(Target.Acceleration.length() == 0){return Utils3DM.Origin;}
            Point3DM Difference = TempPoints[0];
            MomentumPosition.subtract(Form.Center, Difference);
            double A = Target.Acceleration.dotProduct(Target.Acceleration) / 4;
            double B = 2 * Target.Acceleration.dotProduct(Difference) - ProjectileSpeed * ProjectileSpeed;
            double C = Difference.dotProduct(Difference);
            double Determinant = B*B - 4*A*C;
            if(Determinant < 0){
                return Utils3DM.Origin;
            }else{
                Determinant = Math.sqrt(Determinant);
                double TSquareP = (-B + Determinant) / (2 * A);
                double TSquareM = (-B - Determinant) / (2 * A);
                
                if(TSquareM > 0){
                    return Target.Acceleration.multiply(TSquareM / 2);
                    
                }else if(TSquareP > 0){
                    return Target.Acceleration.multiply(TSquareP/ 2);
                }else{
                    return Utils3DM.Origin;
                }
            }
            
        }
        public Point3DM AIJudgeLead(Object3DM Target, double ProjectileSpeed){
            if(Target.Momentum.length() == 0 || ProjectileSpeed == 0){
                return Utils3DM.Origin;
            }
            //}if(this==Player)System.out.println(AccelerationLead);
            Point3DM TargetUnitMovement = TempPoints[4];
            Point3DM DifferenceUnit = TempPoints[5];
            
            Target.Momentum.copyCoords(TargetUnitMovement);
            //TargetUnitMovement.subtract(this.Momentum);
            double TargetSpeed = TargetUnitMovement.length();
            TargetUnitMovement.normalize();
            Target.Form.Center.subtract(Form.Center, DifferenceUnit);
            
            double Distance = DifferenceUnit.length(); 
            DifferenceUnit.normalize();
            
            double Angle = Math.PI - Math.acos(GUI.trigCap(TargetUnitMovement.dotProduct(DifferenceUnit)));
            double DiffAngle = Math.asin(Math.sin(Angle) * TargetSpeed / ProjectileSpeed);
            double Lead = Distance * Math.sin(DiffAngle) / Math.sin(Angle + DiffAngle);
            if(Double.isNaN(Lead)){
                System.out.println("SPOIL1" +ProjectileSpeed / TargetSpeed * Math.sin(Angle));
                return null;
                
            }
            
            return TargetUnitMovement.multiply(Lead);
            
            
        }
        public void AIControls(){
            //Init Points
            Point3DM WAxis = TempPoints[0];
            Point3DM MAxis = TempPoints[1];
            Point3DM LAxis = TempPoints[2];
            Point3DM Difference = TempPoints[3];
            Point3DM DiffSubM = TempPoints[4];
            Point3DM DiffSubW = TempPoints[5];
            Point3DM Target = ControlTarget;
            
            Form.Rotation.getOriginalAxis('y', WAxis);
            
            Form.Rotation.getOriginalAxis('x', MAxis);
       
            
            WAxis.crossProduct(MAxis, LAxis);
            LAxis.normalize();
            Difference = Target.subtract(Form.Center);
            
            
            MAxis.multiply(Difference.dotProduct(MAxis), DiffSubM);
            Difference.subtract(DiffSubM, DiffSubM);
            
            
            WAxis.multiply(Difference.dotProduct(WAxis), DiffSubW);
            Difference.subtract(DiffSubW, DiffSubW);
            
            double AngleToTurn = Math.acos(Math.max(-1, Math.min(1, Difference.dotProduct(MAxis) / (Difference.length()))));
          
            //Throttle
            if(Speed < IdealSpeed){
                TargetThrottle = 10;
            }else{
                double D = Speed - IdealSpeed;
                D = D * OverSpeedDec;
                TargetThrottle = 10 - (int)(D);
            }
            if(Speed < 200 && Afterburner == true && AfterburnerTimer.Current > 0){
                Afterburner = true;
            }else if(Speed < 180 && AfterburnerTimer.Current * 2 > AfterburnerTimer.Max){
                Afterburner = true;
            }else{
                Afterburner = false;
            }
            
            if(Speed == 0){
                return;
            }
            //Roll
       
            
            double RollAmount = Math.toDegrees(Math.atan2(DiffSubM.dotProduct(WAxis) / DiffSubM.length(), DiffSubM.dotProduct(LAxis) / DiffSubM.length()));
            
            if(RollAmount > 180){RollAmount = 360 - RollAmount;}
            if(RollAmount < 0){RollAmount += 180;}else{RollAmount -= 180;}
                
            AIMouseTarget.x = GUI.trigCap(-Math.toRadians(RollAmount) / RollSpeedMultiplier / Math.sqrt(Speed) * RollResponsiveness);
            //AIMouseTarget.x = AIMouseTarget.x * Math.min(MaxRollSpeedAngle, Math.toDegrees(AngleToTurn)) / MaxRollSpeedAngle;
            
       //     System.out.println(RollAmount);
         //   System.out.println(MouseHorizontal);
            
            
            
            
            
            
            //Pitch
            
            double PitchAngle = Math.toDegrees(Math.acos(GUI.trigCap(DiffSubW.dotProduct(MAxis) / DiffSubW.length())));
         
            if(Difference.dotProduct(LAxis) < 0){
                
                AIMouseTarget.y = 1.0 * Math.min(MaxAngleTurn, PitchAngle) / MaxAngleTurn;
            }else{
                
                AIMouseTarget.y = -1.0 * Math.min(MaxAngleTurn, PitchAngle) / MaxAngleTurn;
            }
            
            
            
     //       if(DAoA < -DSA + 1){
    //            System.out.println("AVOIDING STALLv");
     //           AIMouseTarget.y = AIMouseTarget.y * (-GUI.square(DAoA + DSA -1) + 1);
       //     }
        }
        public void checkEvasion(){
            int NearBullets = 0;
            for(int i = 0; i < Projectiles.size(); i++){
                if(Projectiles.projectile(i).Form.Center.distanceTo(Form.Center) < EvadeTriggerRadius && Projectiles.projectile(i).Base != this){
                    NearBullets++;
                }   
            }
            if(NearBullets >= 2 && !Evading && EvasionTCounter.Current >= 0 ){
                
                initEvasion();
            }
        }
        public void initEvasion(){
            EvasionTCounter.setTo(EvasionTCounter.Max);
            Utils3DM.getRandomUnit(EvasionTarget);
            EvasionTarget.multiply(1000000, EvasionTarget);
            EvasionTarget.add(Form.Center, EvasionTarget);
           
            Point3DM Projection = TempPoints[0];
            Point3DM Face = TempPoints[1];
            Form.Rotation.getOriginalAxis('x', Face);
            EvasionTarget.subtract(Form.Center, Projection);
            double DotProduct = Projection.dotProduct(Face);
            if(DotProduct > 0){
                Face.multiply(DotProduct, Face);
                EvasionTarget.add(Face, EvasionTarget);
                EvasionTarget.add(Face, EvasionTarget);
            }
            Evading = true;
          //  Firing[1] = false;
        }
        public void AIEvasion(){
            checkEvasion();
            if(EvasionTCounter.Current < 0){
                if(EvasionTCounter.checkBounds() >= 0){
                    EvasionTCounter.increment(-1.0/FramesPerSecond);
                }else{
                    EvasionTCounter.setTo(EvasionTCounter.Max);
                }
            }
       //     if(this==Player)System.out.println(EvasionTCounter);
        }
        public void AIAvoidStall(){
            double DAoA = Math.toDegrees(AngleOfAttack);
            double DSA = Math.toDegrees(StallAngle);
            
            if(DAoA > DSA - 1){
                //System.out.println("AVOIDING STALL^");
                AIMouseTarget.y = AIMouseTarget.y * (-GUI.square(DAoA - DSA + 1)  + 1);
            }
        }
        public void AIMouseMovement(){
            
            MouseHorizontal = GUI.trigCap(AIMouseTarget.x);
            
            MouseVertical = GUI.trigCap(AIMouseTarget.y);
            
        }
        public void cycleShot(){
            int i = Guns.lastIndexOf(SelectedGun);
            i = (i + 1 ) % Guns.size();
            SelectedGun = Guns.gun(i);
            
        }
        public void print(Graphics2D Painter, ObserverM Viewer){
            Form.print(Painter, Viewer);
            
           // System.out.println(GUI.round(Form.Center.subtract(Form.ActualPoints[1]).length(), 0.1) + "\t" + Form.Center + "\t" + Form.ActualPoints[1]);
        
            for(int i = 0; i < Trails.length; i++){
                Trails[i].print(Painter, Viewer);
            }
            //fSystem.out.println(GUI.round(Form.Center.subtract(Form.ActualPoints[1]).length(), 0.1));

        }
    }
    double FramesPerSecond = 1000.0 / 9;
    Timer Stopwatch;
    JCanvas Area;
   
    int MouseX;
    int MouseY;
    int MouseDX;
    int MouseDY;
    Robot MyRobot;
    boolean TwistLeft;
    boolean TwistRight;
    boolean TwistUp;
    boolean TwistForward;
    boolean ClickingL;
    boolean ClickingR;

    boolean SpaceDown;
    boolean ThrottleUp;
    boolean ThrottleDown;
    
    
    ObserverM View = new ObserverM(0, 0, 3, new Rotation3D());
    Point3DM TowPoint = new Point3DM(0, 0, 0);
    double TowDistance = 25;
    
    int Facing; //0 - Chase / 1 - Cockpit
    int Direction = 1; //1 - DirectionCount Compass Rotations /  -1 - up
    int DirectionCount = 8;
    double TurnIncrement = 225;// in degrees / second
    double CurrentDirection = 1;
    double CurrentPitch = 0;
    
    Shape3DM Grid;
    Craft Player; 
    
    ProjectileType MachineGunShot;
    ProjectileType MissileShot;
    ProjectileType CannonShot;
    Gun MachineGun;
    Gun MissileLauncher;
    Gun Cannon;
    
    
    int StarCount = 100;
    double DebrisSpeedMultiplier = 0.1;
    
    Sprite3D[] Stars = new Sprite3D[StarCount];
    
    GeneralVector Doodads = new GeneralVector(); //Doodads
    GeneralVector Sprites = new GeneralVector(); //Sprites
    
    GeneralVector Planes = new GeneralVector(); //Planes
    GeneralVector Crafts = new GeneralVector(); //Crafts
    GeneralVector Projectiles = new GeneralVector(); //Projectiles
    GeneralVector Trails = new GeneralVector(); //Trails
    
    long FrameCounter;
    NumberQueue DelayList = new NumberQueue(50);
    public void init(){
        initShots();
        View.setArea(0, 0, Area.W, Area.H, 1.2);
        try{
            MyRobot = new Robot();
        }catch(Exception e){System.out.println("Robot Creation Error"); System.exit(0);}
        Player = new Craft(Utils3DM.Origin);
        Crafts.add(Player);
        Player.Guns.add(MissileLauncher.copy(Player, new Point3DM[] {Player.Form.ActualPoints[1]}, Player.Form.Rotation));
        
        Grid = new Shape3DM(false, Color.GREEN.darker(), 0, 0, 0);
        Utils3DM.setPoints(Grid, 'g', 50, 50, 100);
        
        initViewpoint(Player);
        initGUI();
    
        initStars();
        initPlanes();
        test();
        
   //     System.out.println(Thing.FurthestDistance);
    }
    
    
    public void initShots(){
        //MachineGun Shot
        //Speed 1050m/s
        //HitRadius = 4
        //Duration = 2s
        {//                                 Speed      CR  FC DUR   ROT  SPREAD DMG FRAGS
        MachineGunShot = new ProjectileType(1050 + 150, 2, 1, 3.0, true, 0.015, 1.0, 1);
        MachineGunShot.setTrail(0.06, 3);
        Shape3DM Form = new Shape3DM(false, Color.red, 0, 0, 0);
        Utils3DM.setPoints(Form, 'p', 1, 1, 0);
        Form.Rotation.rotateAround('y', Math.toRadians(90));
        Utils3DM.center(Form, 'l');
        MachineGunShot.setShape(Form);
        MachineGunShot.setAIData(1450, 0, 0);
        for(int i = 0; i < MachineGunShot.Form.Points.length; i++){
            Form.Rotation.transform(MachineGunShot.Form.Points[i], MachineGunShot.Form.Points[i]);
        }
        MachineGunShot.Form.Rotation.identify();
        }
        
        //Missile Shot
        //Speed 650m/s
        //HitRadius = 5
        //Duration = 3s
        {
        MissileShot = new ProjectileType(600 + 150, 3, 5, 5.0, true, 0.1, 5.0, 3);
        MissileShot.setTrail(1, -1);
        Shape3DM Form = new Shape3DM(false, Color.yellow, 0, 0, 0);
        Utils3DM.setPoints(Form, 'p', 2.5, 7, 0);
        Form.Rotation.rotateAround('y', Math.toRadians(-90));
        Utils3DM.center(Form, 'l');
        MissileShot.setShape(Form);
        for(int i = 0; i < MissileShot.Form.Points.length; i++){
            Form.Rotation.transform(MissileShot.Form.Points[i], MissileShot.Form.Points[i]);
        }
        MissileShot.Form.Rotation.identify();
        MissileShot.setHoming(Math.toRadians(20), 0.5);
        MissileShot.setAIData(2500, 750, 2);
        }
        //Cannon Shot
        {
        CannonShot = new ProjectileType(1500 + 150, 2, 10, 3.0, true, 0.01, 5.0, 3);
        CannonShot.setTrail(0.2, 1);
        Shape3DM Form = new Shape3DM(false, Color.red, 0, 0, 0);
        Utils3DM.setPoints(Form, 'o', 6, 2, 2);
        Utils3DM.center(Form, 'p');
        CannonShot.setShape(Form);
        CannonShot.setAIData(1500, 0, 1);
        
        }
        //MachineGun
        //MaxBurst 2s
        //ReloadTime 8s
        MachineGun = new Gun("Machine Gun", null, null, null, MachineGunShot, -1337, 20, 2.5);
        
        //MissileLauncher
        //MaxBurst 2 shots
        //ReloadTime 12.5s
        MissileLauncher = new Gun("Missile", null, null, null, MissileShot, -1337, 10, 0.5);
        //Cannon
        //MaxBurst 1 shot
        //Reload Time 10s
        Cannon = new Gun("Cannon", null, null, null, CannonShot, -1337, 10, 1);
        
    }
    public void test(){
        Craft NewCraft2 = new Craft(new Point3DM(0, -500, 2000));
        
        NewCraft2.Trails[0].Form.Tint = Color.cyan;
        NewCraft2.Trails[1].Form.Tint = Color.cyan;
        
        NewCraft2.initAI();
        
        NewCraft2.State = 'a';
        
        Craft NewCraft = new Craft(new Point3DM(0, 500, 2000));
        NewCraft.initAI();
        
        NewCraft.Trails[0].Form.Tint = Color.red;
        NewCraft.Trails[1].Form.Tint = Color.red;
        
        NewCraft.State = 'a';
        
        
        
        
        
        
        NewCraft.DecisionTarget = NewCraft2;
        NewCraft2.DecisionTarget = NewCraft;
        
        Crafts.add(NewCraft);
        Crafts.add(NewCraft2);
        
        
        Player.State = 'a';
        
    }
    public void initPlanes(){
        Point3DM[] PV = new Point3DM[3];
        
        for(int i = 0; i < 4; i++){
            
            PV[0] = new Point3DM(0, 2000, 1000);
            PV[1] = new Point3DM(Math.sin(i * Math.PI/2) * 1000, Math.cos(i * Math.PI/2) * 1000 + 2000, 0);
            PV[2] = new Point3DM(Math.sin((i + 1) * Math.PI/2) * 1000, Math.cos((i + 1) * Math.PI/2) * 1000 + 2000, 0);
            Planes.add(new Plane(PV));
            
        }
        PV = new Point3DM[4];
        PV[0] = new Point3DM(500, -2000, 2000);
        PV[1] = new Point3DM(-500, -2000, 2000);
        PV[2] = new Point3DM(-500, -2000, 1000);
        PV[3] = new Point3DM(500, -2000, 1000);
        
        Planes.add(new Plane(PV));
        
        PV = new Point3DM[3];
        PV[0] = new Point3DM(2000, -500, 1500);
        PV[1] = new Point3DM(2000, -500, 500);
        PV[2] = new Point3DM(2000, 500, 500);
        Planes.add(new Plane(PV));
        
        PV[0] = new Point3DM(-2000, -500, 1500);
        PV[1] = new Point3DM(-2000, -500, 500);
        PV[2] = new Point3DM(-2000, 500, 500);
        Planes.add(new Plane(PV));
        
        //Ground Plane
        PV = new Point3DM[4];
        PV[0] = new Point3DM(-1000000, -1000000, 0);
        PV[1] = new Point3DM(1000000, -1000000, 0);
        PV[2] = new Point3DM(1000000, 1000000, 0);
        PV[3] = new Point3DM(-1000000, 1000000, 0);
        Plane Floor = new Plane(PV);
        
        Floor.Finite = false;
        Floor.Form.Tint = Color.green.darker();
        Planes.add(Floor);
        
    }
    public void initStars(){
        for(int i = 0; i < StarCount; i++){
            Point3DM StarPosition = new Point3DM(0, 0, 0);
            do{
                StarPosition.setLocation(2.2* Math.random() - 1.1, 2.2 * Math.random() - 1.1, 2.2 * Math.random() - 1.1);
            }while(StarPosition.length() > 1 || StarPosition.POSZ <= 0);
            
            StarPosition.normalize();
            StarPosition.multiply(1000000000.0, StarPosition);
           
            Stars[i] = new Sprite3D(new Line2D.Double(0, 0, 0, 0), StarPosition, Color.white);
           
            
        }
    }
    Container ContentArea;
    public NewTest(){
        //BASIC INITIALIZATION
        super("Window"); 
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        Stopwatch = new Timer((int)(0), this);
        Stopwatch.stop();
        //SET CONTENT PANE
        ContentArea = getContentPane();
        ContentArea.setLayout(null);
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new JCanvas();
        Area.setBounds(0, 0, X, Y);
        Area.init();
        ContentArea.add(Area);
        Area.Painter.setClip(0, 0, Area.W, Area.H);
        Area.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        this.setLocation(ScreenSize.width / 2 - X / 2, ScreenSize.height / 2 - Y / 2);
        init();
        
        Area.addMouseWheelListener(this);
        this.addKeyListener(this);
        Area.addMouseListener(this);
        Area.addMouseMotionListener(this);
       // if(!GUI.Border){
        setUndecorated(true); 
       // getRootPane().setWindowDecorationStyle(JRootPane.WARNING_DIALOG);
        //}
        //ADD CONTENT PANE AND PACK
        this.setContentPane(ContentArea);
        this.show();
        Stopwatch.start();
        //this.pack();   
    }
    public void togglePlayerTarget(){
        if(Player.DecisionTarget == Crafts.craft(1)){
            Player.DecisionTarget = Crafts.craft(2);
        }else{
            Player.DecisionTarget = Crafts.craft(1);
        } 
        Player.AState = 'a';
    }
    public void toggleNPCTarget(){
        if(Crafts.craft(1).DecisionTarget == Player){
            Crafts.craft(1).DecisionTarget = Crafts.craft(2); 
            Crafts.craft(2).DecisionTarget = Crafts.craft(1);
        }else{
            Crafts.craft(1).DecisionTarget = Player; 
            Crafts.craft(2).DecisionTarget = Player;
        }
    }
    
    public void keyPressed(KeyEvent Event){
        switch(Event.getKeyCode()){
            
            
            case 65: TwistLeft = true; break; //A
            case 83: TwistUp = true; break; //S
            case 68: TwistRight = true; break; //D
            case 87: TwistForward = true; break; //W
            case 81: Player.RudderLeft = true; break; //Q
            case 69: Player.RudderRight = true; break; //E
            
            case 16: Player.Afterburner = true; break;//LeftShift
            
            case 82: togglePlayerTarget(); break; //R
            case 84: Player.initEvasion(); break; //T
            case 89: break; //Y
            case 67: if(Player.UseAI){Player.UseAI = false;}else{Player.UseAI = true; Player.DecisionTarget = Crafts.craft(2);} break; // C
            case 86:  break; //V
            case 88: toggleNPCTarget(); break; //X
            case 90:  break; //Z
            //Facing = (Facing + 1) % 2;
            //case 45: ThrottleDown = true; break; //-
            //case 61: ThrottleUp = true; break; //=
            
            case 27: System.exit(0); break;
           
            case 32: SpaceDown = true; break; // 
            default: System.out.println(Event.getKeyCode());
            
        }
        
        
        switch(Event.getKeyCode()){
            case 192:{ // ` key
                Player.TargetThrottle = 0;
            }break;
            case 49: case 50: case 51: case 52: case 53: case 54: case 55: case 56: case 57:{ // number keys
                double A = (Event.getKeyChar() - 48) * 10.0 / 10;
                Player.TargetThrottle = A;
            }break;
            case 48:{ // 0 key
                Player.TargetThrottle = 10;
            }break;
           
            
            
        } 
        
        //System.out.println(User.HAngle + "\t" + User.VAngle + "\t" + User.RAngle);
    }
    public void keyReleased(KeyEvent Event){
        switch(Event.getKeyCode()){
            
            case 65: TwistLeft = !true; break; //A
            case 83: TwistUp = !true; break; //S
            case 68: TwistRight = !true; break; //D
            case 87: TwistForward = !true; break; //W
            case 81: Player.RudderLeft = !true; break; //Q
            case 69: Player.RudderRight = !true; break; //E
            
            case 16: Player.Afterburner = !true; break;//LeftShift
            
 //           case 45: ThrottleDown = !true; break; //-
//            case 61: ThrottleUp = !true; break; //=
            
            case 32: SpaceDown = !true; break; //
            
            //default: System.out.println(Event.getKeyCode());
            
            case 27: System.exit(0); break;//ESC
        }

    }
 
    
    public void keyTyped(KeyEvent Event){
        
        
    }
    public Doodad createDebris(int Duration, Point3DM Position, double Momentum, Color Tint, double Size){
        char A = ' '; 
        switch(GUI.randomInt(1, 3)){
            case 1: A = 't'; break;
            case 2: A = 'p'; break;
            case 3: A = 'c'; break;
        }
        Point3DM RandomUnit = Utils3DM.getRandomUnit();
        Doodad Debris = new Doodad(true, A, Duration, Position.add(RandomUnit), RandomUnit.multiply(Momentum), Tint, Size);
        
        return Debris;
    }
    public void alignViewpoint(Craft Target){
          
            double Difference = GUI.angleDiff(CurrentDirection, 360 / DirectionCount * GUI.cap(0, Math.abs(Direction) - 1, DirectionCount - 1));
          
            if(Math.abs(Difference) < 360.0 / DirectionCount / 5){
          
                if(TwistLeft){
                //    Direction = Math.abs(Direction);
                    Direction = ((Math.abs(Direction) - 1 + DirectionCount - 1) % DirectionCount + 1) * GUI.sign(Direction);
          
                }
                if(TwistRight){
                 //  Direction = Math.abs(Direction);
                   Direction = ((Math.abs(Direction) + 1 + DirectionCount - 1) % DirectionCount + 1) * GUI.sign(Direction);
          
                }
            }
            
            
            //
            if(Difference > 0){
                CurrentDirection += Math.min(Math.abs(Difference), TurnIncrement * 1.0 / FramesPerSecond);
            }
            if(Difference < 0){
                CurrentDirection -= Math.min(Math.abs(Difference), TurnIncrement * 1.0 / FramesPerSecond);
            }
            
            Difference = CurrentPitch - (Direction < 0 ? 45 : 0);
            if(Difference == 0){
                if(TwistUp){
                     Direction = -Direction;
                }
            }
            if(TwistForward){
                Direction = 1;
            }
            
            if(Difference > 0){
                CurrentPitch -= Math.min(Math.abs(Difference), TurnIncrement / 2 * 1.0 / FramesPerSecond);
            }
            if(Difference < 0){
                CurrentPitch += Math.min(Math.abs(Difference), TurnIncrement / 2 * 1.0 / FramesPerSecond);
            }
            //
          //  System.out.println(Direction);
            Point3DM Movement = Target.TempPoints[0];
            Target.Form.Center.subtract(TowPoint, Movement);
            Movement.normalize();   
            Movement.multiply(-TowDistance, TowPoint);
            TowPoint.add(Target.Form.Center, TowPoint);
            Rotation3D Rot = new Rotation3D();
            Rotation3D Pit = new Rotation3D();
            Rot.rotateAround('z', Math.toRadians(CurrentDirection));
            Pit.rotateAround('y', Math.toRadians(CurrentPitch));
            View.Rotation = Target.Form.Rotation.copy();
            Rotation3D.multiply(View.Rotation, Rot, View.Rotation);
            Rotation3D.multiply(View.Rotation, Pit, View.Rotation);
            View.setLookVector();
            if(true)
            {
                switch(Facing){
                    case 0:{
                        TowPoint.copyCoords(View.POS);
              
                        
                        
                        
                        
                        View.POS.subtract(Target.Form.Center, View.POS);
                        Target.Form.Rotation.inverseTransform(View.POS, View.POS);
                        Pit.transform(View.POS, View.POS);
                        Rot.transform(View.POS, View.POS);
                        
                        Target.Form.Rotation.transform(View.POS, View.POS);
                        View.POS.add(Target.Form.Center, View.POS);
                        }
                        break;
                    case 1:{
                        
      
                       
                        Target.Form.Center.copyCoords(View.POS);
                        
                        
                        }
                        break;
                   
                        
                        
                    
                }
                
                    
                        
                    
                  //  View.Rotation.inverse(View.InverseRotation);
                    View.setLookVector();
            }
    }
    public void initViewpoint(Craft Target){
        
        View.POS = new Point3DM(-TowDistance, 0, 0);
        TowPoint = new Point3DM(-TowDistance, 0, 0);
        
        
        Player.Form.Rotation.copy(View.Rotation);
        //View.Rotation = Player.Form.Rotation;
        
        //View.Rotation.inverse(View.InverseRotation);
        
        View.Rotation.transform(View.POS, View.POS); 
        View.Rotation.transform(TowPoint, TowPoint); 
        View.POS.add(Target.Form.Center, View.POS);
        TowPoint.add(Target.Form.Center, TowPoint);
        View.setLookVector();
        
    }
    public void setMouseControls(){
        
        double MX = (MouseX - Area.W / 2.0) / (Area.W / 2);
        double MY = (MouseY - Area.H / 2.0) / (Area.H / 2);
      //  System.out.println(MX + "\t" + MY);
        Player.MouseVertical = MY;
        Player.MouseHorizontal = MX;
        
    }
    public void throttleControls(){
        if(ThrottleUp){
            Player.Throttle = Player.Throttle + 5.0 / FramesPerSecond;
        }
        if(ThrottleDown){
            Player.Throttle = Player.Throttle - 5.0 / FramesPerSecond;
        }
    }
  
    public void actionPerformed(ActionEvent Event){
        //Count Frames
        FrameCounter++;
        doTime();
        //Black out Canvas
        Area.Painter.setColor(Color.black);
        Area.Painter.fillRect(0, 0, Area.W, Area.H);

        
        
        //Move Crafts
        for(int i = 0; i < Crafts.size(); i++){
            Crafts.craft(i).move();
        }
     
        //Move Projectiles
        for(int i = 0; i < Projectiles.size(); i++){
            Projectiles.projectile(i).move();
            if(Projectiles.projectile(i).Duration.checkBounds() < 0){
                Projectiles.remove(i);
             
            }
        }
        
        //Move Sprites
        for(int i = Sprites.size() - 1; i >= 0; i--){
            Sprites.tempSprite(i).move();
            if(Sprites.tempSprite(i).Duration.checkBounds() < 0){
                Sprites.remove(i);
            }
        }
        
        //Move Doodads
        for(int i = Doodads.size() - 1; i >= 0; i--){
            Doodads.doodad(i).move();
            if(Doodads.doodad(i).Duration.checkBounds() < 0){
                Doodads.remove(i);
            }
        }
        //Calculate 3D Points
        for(int i = 0; i < Planes.size(); i++){
            if(!Planes.plane(i).Form.Static){
                Planes.plane(i).Form.calcPoints();
            }
        }
        for(int i = 0; i < Crafts.size(); i++){
            if(!Crafts.craft(i).Form.Static){
                Crafts.craft(i).Form.calcPoints();
            }
            Crafts.craft(i).updateTrails();
            if(Crafts.craft(i) == Player){
                
            }
            
        }
        
        for(int i = 0; i < Projectiles.size(); i++){
            if(!Projectiles.projectile(i).Form.Static){
                Projectiles.projectile(i).Form.calcPoints();
            }
        }
        for(int i = 0; i < Doodads.size(); i++){
            if(!Doodads.doodad(i).Form.Static){
                Doodads.doodad(i).Form.calcPoints();
            }
        }
//        System.out.println(Trails.size());
        for(int i = 0; i < Trails.size(); i++){
            Trails.trail(i).chase();
            if(Trails.trail(i).finished()){
                Trails.remove(Trails.trail(i));
            }
        }
        //Accept User Input
        if(!Player.UseAI){
            setMouseControls();
            throttleControls(); 
            Player.manualFire();
        }
        
        //Do AI Controls
        for(int i = 0; i < Crafts.size(); i++){
            if(Crafts.craft(i).UseAI){
                
                Crafts.craft(i).AITrackShots();
                Crafts.craft(i).AIPickWeapon();
                Crafts.craft(i).AIEvasion();
                Crafts.craft(i).AIDecisions();
                
                Crafts.craft(i).AIControls();
                
                Crafts.craft(i).AIAvoidStall();
                Crafts.craft(i).AIJudgeFire();
                Crafts.craft(i).AIMouseMovement();
            }
        }
        //Align Camera to Player
        alignViewpoint(Player);
        
        //Center Grid on Player
        shiftGrid(Player.Form.Center);
        Grid.calcPoints();
        
        //CheckCollision
        
        Point3DM ImpactSpot;
        for(int i = 0; i < Projectiles.size(); i++){
            for(int j = 0; j < Planes.size(); j++){
                ImpactSpot = Projectiles.projectile(i).checkCollision(Planes.plane(j));
                if(ImpactSpot != null){
                    Projectiles.projectile(i).doCollision(Planes.plane(j), ImpactSpot);
                }
            }
            for(int j = 0; j < Crafts.size(); j++){
                ImpactSpot = Projectiles.projectile(i).checkCollision(Crafts.craft(j));
                if(ImpactSpot != null){
                
                    Projectiles.projectile(i).doCollision(Crafts.craft(j), ImpactSpot);
                }
            }
        }
        Point3DM[] TempPoints = {new Point3DM(0, 0, 0), new Point3DM(0, 0, 0), new Point3DM(0, 0, 0)};
        for(int i = 0; i < Doodads.size(); i++){
            for(int j = 0; j < Planes.size(); j++){
                ImpactSpot = Planes.plane(j).checkCollision(Doodads.doodad(i), TempPoints);
                if(ImpactSpot != null){
                    Planes.plane(j).doCollision(Doodads.doodad(i), ImpactSpot, TempPoints);
                }
            }
        }
        
        for(int j = 0; j < Crafts.size(); j++){
            for(int i = 0; i < Planes.size(); i++){
            
                ImpactSpot = Planes.plane(i).checkCollision(Crafts.craft(j), Crafts.craft(j).TempPoints);
                if(ImpactSpot != null){   
                    Planes.plane(i).doCollision(Crafts.craft(j), ImpactSpot);
                
                }
            }
        }
        //Background Printing
        for(int i = 0; i < Stars.length; i++){
 
            Stars[i].print(Area.Painter, View);
        }
        
        
        //3D Stuff Printing

        
        Grid.print(Area.Painter, View, Grid.Tint);
        
        for(int i = 0; i < Crafts.size(); i++){
            Crafts.craft(i).print(Area.Painter, View);
        }
        for(int i = 0; i < Trails.size(); i++){
            Trails.trail(i).print(Area.Painter, View);
        }
     
        for(int i = 0; i < Planes.size(); i++){
            Planes.plane(i).print(Area.Painter, View);
        }
        
        for(int i = 0; i < Projectiles.size(); i++){
            Projectiles.projectile(i).print(Area.Painter, View);
        }
        
        for(int i = 0; i < Sprites.size(); i++){
            Sprites.tempSprite(i).print(Area.Painter, View);
        }
        
        for(int i = 0; i < Doodads.size(); i++){
            Doodads.doodad(i).print(Area.Painter, View);
        }
        //GUI Stuff Printing
        
        drawCursor();
        drawGUI();
        
        //Print Canvas to Screen
     //   System.out.println(MouseX + "\t" + MouseY);
        Area.paintComponent(ContentArea.getGraphics());
        
    }
    public void GUIBase(){
        //Border
        Area.Painter.setColor(Color.white);
        Area.Painter.drawRect(1, 1, Area.W - 3, Area.H - 3);
        Area.Painter.drawRect(3, 3, Area.W - 7, Area.H - 7);
        
        //Crosshairs
        Area.Painter.setColor(Color.green);
        Area.Painter.setStroke(Area.DashedStroke);
        Area.Painter.drawLine(Area.W / 2, 0 - 3, Area.W / 2, Area.H);
        Area.Painter.drawLine(0 - 5 , Area.H / 2, Area.W, Area.H / 2);
        Area.Painter.setStroke(Area.NormalStroke);

    }
    public void GUISpeed(){
        //Speed & Throttle Indicators
        Area.Painter.drawRect((int)(0.9 * Area.W), (int)(0.5 * Area.H) - 75, 16, 150);
        Area.Painter.drawRect((int)(0.9 * Area.W), (int)(0.5 * Area.H + 75 - Player.getThrust() / Player.Thrust * 10 * 15), 16, (int)(Player.getThrust() / Player.Thrust * 10 * 15));
        Area.Painter.drawLine((int)(0.9 * Area.W + 3), (int)(0.5 * Area.H + Math.min(75, 75 - Player.TargetThrottle * 15 + 3)), (int)(0.9 * Area.W + 13), (int)(0.5 * Area.H + Math.min(75, 75 - Player.TargetThrottle * 15 + 3)));
        
        //Area.Painter.drawLine((int)(0.9 * X + 3), (int)(0.5 * Y + Math.min(75, 75 - Player.getThrust() / Player.Thrust * 10 * 15 + 3)), (int)(0.9 * X + 3), (int)(0.5 * Y + 75));
        if(Player.Afterburner){
            Area.Painter.setColor(Color.red);
        }
        Area.Painter.drawLine((int)(0.9 * Area.W + 3), (int)(0.5 * Area.H + Math.min(75, 75 - Player.getThrust() / Player.Thrust * 10 * 15 * Player.AfterburnerTimer.Current / Player.AfterburnerTimer.Max + 3)), (int)(0.9 * Area.W + 3), (int)(0.5 * Area.H + 75));
        Area.Painter.setColor(Color.green);
        
        Area.printLine((int)(0.9 * Area.W + 8), (int)(0.5 * Area.H + 105), Math.round(Player.Momentum.length()) + "m/s", 12, Color.green, 'c');
        Area.printLine((int)(0.9 * Area.W + 20), (int)(0.5 * Area.H), Math.round(Player.Acceleration.length()) + "m/s/s", 12, Color.green, 'l');
    }
    public void GUIAltitude(){
        //Altitude, Vert Momentum, Vert Acceleration
        Area.Painter.drawLine((int)(0.1 * Area.W - 8), (int)(0.5 * Area.H) - 75, (int)(0.1 * Area.W - 8), (int)(0.5 * Area.H) + 75);
        Area.Painter.drawLine((int)(0.1 * Area.W - 8), (int)(0.5 * Area.H) - 75, (int)(0.1 * Area.W - 16), (int)(0.5 * Area.H - 75 + 8));
        Area.Painter.drawLine((int)(0.1 * Area.W - 8), (int)(0.5 * Area.H) - 75, (int)(0.1 * Area.W - 00), (int)(0.5 * Area.H - 75 + 8));
        
        Area.printLine((int)(0.1 * Area.W - 8), (int)(0.5 * Area.H + 105), Math.round(Player.Form.Center.POSZ) + "m", 12, Color.green, 'c');
        Area.printLine((int)(0.1 * Area.W - 0), (int)(0.5 * Area.H + 15 + 12), Math.round(Player.Momentum.POSZ) + "m/s", 12, Color.green, 'l');
        Area.printLine((int)(0.1 * Area.W - 0), (int)(0.5 * Area.H - 15 + 12), Math.round(Player.Acceleration.POSZ) + "m/s/s", 12, Color.green, 'l');
        
    }
    public void GUILift(){
        //AoA & Lift Indicator
        
        Area.Painter.drawLine((int)(0.1 * Area.W - 0 - 30), (int)(0.7 * Area.H), (int)(0.1 * Area.W - 0 + 30), (int)(0.7 * Area.H));
        if(Player.Momentum.length() > 1){
            Area.Painter.drawLine((int)(0.1 * Area.W + 30 - 60 * Math.cos(Player.AngleOfAttack * -2)), (int)(0.7 * Area.H + 60 * Math.sin(Player.AngleOfAttack * -2)), (int)(0.1 * Area.W + 30), (int)(0.7 * Area.H));
            Area.printLine((int)(0.1 * Area.W - 30 - 5), (int)(0.7 * Area.H + 12), "" + Math.round(10 * Math.toDegrees(Player.AngleOfAttack)) / 10.0, 12, Color.green, 'r');
            int Angle = (int)(Math.toDegrees(Player.AngleOfAttack * -2));
            
            if(Angle < 0){Angle++;}
            Area.Painter.drawArc((int)(0.1 * Area.W + 30 - 40), (int)(0.7 * Area.H - 40), 80, 80, 180, Angle);
        }else{
            Area.printLine((int)(0.1 * Area.W - 30 - 5), (int)(0.7 * Area.H + 12), "0.0", 12, Color.green, 'r');
        }
        if(Player.Lift != 0){
            Area.Painter.drawLine((int)(0.1 * Area.W + 30 + 5), (int)(0.7 * Area.H - Player.Lift / Player.Mass * 0.3), (int)(0.1 * Area.W + 30 + 5), (int)(0.7 * Area.H + Player.Lift / Player.Mass * 0.3));
            Area.Painter.drawLine((int)(0.1 * Area.W + 30 + 5), (int)(0.7 * Area.H - Player.Lift / Player.Mass * 0.3), (int)(0.1 * Area.W + 30 + 5 + 5), (int)(0.7 * Area.H - Player.Lift / Player.Mass * 0.3 + (Player.Lift >= 0 ? 5 : -5)));
            Area.Painter.drawLine((int)(0.1 * Area.W + 30 + 5), (int)(0.7 * Area.H - Player.Lift / Player.Mass * 0.3), (int)(0.1 * Area.W + 30 + 5 - 5), (int)(0.7 * Area.H - Player.Lift / Player.Mass * 0.3 + (Player.Lift >= 0 ? 5 : -5)));
        }
    }
    public void GUITargeting(){
        //Highlight Target
        
        int PointerLength = 50;
        if(Player.DecisionTarget != null){
            //Check if Point needs to be pointed too
            Point3DM Target = Player.TempPoints[0];
            Point3DM ObserverVector = Player.TempPoints[1];
            Player.DecisionTarget.Form.Center.copyCoords(Target);
            Target.subtract(View.POS, ObserverVector);
            boolean Behind = ObserverVector.dotProduct(View.LookVector) < 0;
            Target.center(View, Target);
            Point2D.Double FlatPoint = new Point2D.Double();
            Target.toScreen(View, FlatPoint);
            
            //Extend Point to edge of screen
            if(!(FlatPoint.x < Area.W && FlatPoint.y < Area.H && FlatPoint.x > 0 && FlatPoint.y > 0 && !Behind)){
                FlatPoint.x -= Area.W / 2; FlatPoint.y -= Area.H / 2;
                if(Behind){
                    FlatPoint.x = -FlatPoint.x;
                    FlatPoint.y = -FlatPoint.y;
                }
                double Grad = FlatPoint.y / FlatPoint.x;
                double Gradi = FlatPoint.x / FlatPoint.y;
                FlatPoint.x *= 10000; FlatPoint.y *= 10000;
             //   System.out.println(FlatPoint);
                if(FlatPoint.x < -Area.W / 2){
                    FlatPoint.y = FlatPoint.y - (FlatPoint.x + Area.W / 2) / Gradi;
                    FlatPoint.x = -Area.W / 2;
                }else if(FlatPoint.x > Area.W / 2){
                    FlatPoint.y = FlatPoint.y - (FlatPoint.x - Area.W / 2) / Gradi;
                    FlatPoint.x = Area.W / 2;
                }
             //   System.out.println(FlatPoint);
                if(FlatPoint.y < -Area.H / 2){
                    FlatPoint.x = FlatPoint.x - (FlatPoint.y + Area.H / 2) * Gradi;
                    FlatPoint.y = -Area.H / 2;
                }else if(FlatPoint.y > Area.H / 2){
                    FlatPoint.x = FlatPoint.x - (FlatPoint.y - Area.H / 2) * Gradi;
                    FlatPoint.y = Area.H / 2;
                }
                double Angle = Math.atan2(FlatPoint.y, FlatPoint.x);
                
                double PointerWidth = Math.toRadians(15);
                Point2D.Double PointA = new Point2D.Double((FlatPoint.x + Area.W / 2), (FlatPoint.y + Area.H / 2));
                Point2D.Double PointB = new Point2D.Double(PointA.x - PointerLength * Math.cos(Angle + PointerWidth), PointA.y - PointerLength * Math.sin(Angle + PointerWidth));
                Point2D.Double PointC = new Point2D.Double(PointA.x - PointerLength * Math.cos(Angle - PointerWidth), PointA.y - PointerLength * Math.sin(Angle - PointerWidth));
                Area.Painter.drawLine((int)PointA.x, (int)PointA.y, (int)PointB.x, (int)PointB.y);
                Area.Painter.drawLine((int)PointA.x, (int)PointA.y, (int)PointC.x, (int)PointC.y);
                Area.Painter.drawLine((int)PointB.x, (int)PointB.y, (int)PointC.x, (int)PointC.y);
            //    Area.Painter.fillRect((int)FlatPoint.x + X / 2 - 50, (int)FlatPoint.y + Y / 2 - 50, 100, 100);
            }else{
                //Circle Target
                Area.Painter.drawOval((int)(FlatPoint.x - PointerLength / 4), (int)(FlatPoint.y - PointerLength / 4), PointerLength / 2, PointerLength / 2);   
            }
        }
        //Draw Lead
        
        if(Player.DecisionTarget != null){
            Point3DM Target = Player.TempPoints[0];
            Point3DM TransformedTarget = Player.TempPoints[1];
            Point3DM ObserverVector = Player.TempPoints[2];
            Point2D.Double FlatPoint = new Point2D.Double();
            
            Player.DecisionTarget.Form.Center.copyCoords(Target);
            Point3DM Lead = Player.AIJudgeLead(Player.DecisionTarget, Player.SelectedGun.Shot.Momentum.POSX);
            if(Lead != null){
                Target.add(Lead, Target);
            }
            //MomentumLead
            Target.subtract(View.POS, ObserverVector);
            boolean Behind = ObserverVector.dotProduct(View.LookVector) < 0;
            
            if(!Behind){
                Target.center(View, TransformedTarget);
                FlatPoint.setLocation(0, 0);
                TransformedTarget.toScreen(View, FlatPoint);
                Area.Painter.drawOval((int)FlatPoint.x - 8, (int)FlatPoint.y - 8, 16, 16);
            }
            Target.copyCoords(TransformedTarget);
            TransformedTarget.add(Player.AIFudgeLead(Player.DecisionTarget,TransformedTarget, Player.SelectedGun.Shot.Momentum.POSX), Target);
            //MomentumLead
            Target.subtract(View.POS, ObserverVector);
            Behind = ObserverVector.dotProduct(View.LookVector) < 0;
            
            if(!Behind){
                Target.center(View, TransformedTarget);
                FlatPoint.setLocation(0, 0);
                TransformedTarget.toScreen(View, FlatPoint);
                Area.Painter.drawOval((int)FlatPoint.x - 6, (int)FlatPoint.y - 6, 12, 12);
            }
            
            //AITarget
            if(Player.UseAI){
                Player.ControlTarget.subtract(View.POS, ObserverVector);
                Behind = ObserverVector.dotProduct(View.LookVector) < 0;
            
                if(!Behind){
                    Player.ControlTarget.center(View, TransformedTarget);
                    FlatPoint.setLocation(0, 0);
                    TransformedTarget.toScreen(View, FlatPoint);
                    Area.Painter.drawRect((int)FlatPoint.x - 6, (int)FlatPoint.y - 6, 12, 12);
                }
                
                
            }
        }
    }
    public void GUIWeapons(){
        //List Weapons public void printLine(int INTX, int INTY, String Text, int Font, Color FontColor, char Alignment){
        int Length = 100;
        int Width = 12;
        int Gap = 5;
        for(int i = 0; i < Player.Guns.size(); i++){
            Area.Painter.drawRect((int)(0.9 * Area.W - Length + 15), (int)(0.75 * Area.H + i * (Width + Gap)), Length, Width);
            Area.Painter.drawRect((int)(0.9 * Area.W - Length + 15), (int)(0.75 * Area.H + i * (Width + Gap)), (int)(Length * Player.Guns.gun(i).ReloadTimer.Current / Player.Guns.gun(i).ReloadTimer.Max), Width);
            Area.Painter.drawRect((int)(0.9 * Area.W - Length + 15) + (int)(Length * Player.Guns.gun(i).ReloadTimer.Current / Player.Guns.gun(i).ReloadTimer.Max) - 2, (int)(0.75 * Area.H + i * (Width + Gap)) + 2, 0, Width - 4);
            Area.printLine((int)(0.9 * Area.W - Length + 15 + 1), (int)(0.75 * Area.H + i * (Width + Gap) + Width + Gap), Player.Guns.gun(i).Name, 10, Color.green, 'l');
            if(Player.Guns.gun(i) == Player.SelectedGun){
                Area.Painter.drawRect((int)(0.9 * Area.W - Length + 15 - 2), (int)(0.75 * Area.H + i * (Width + Gap) - 2), Length + 4, Width + 4);
            }
        }
    }
    
    
    Point Middle;
    int Width = 72;
    Point3DM Temp1 = new Point3DM(0, 0, 0);
    Point3DM Temp2 = new Point3DM(0, 0, 0);
    Point3DM Temp3 = new Point3DM(0, 0, 0);
    AffineTransform RollRot = new AffineTransform();
    GeneralPath GroundShape = new GeneralPath();
    double TranslateConstant = (Width) / (Math.PI / 2);
    int GapWidth = 5;
    Ellipse2D Circle = new Ellipse2D.Float();
    public void GUIHorizon(){
        
        
        
        
        Circle.setFrame(Middle.x - Width / 2, Middle.y - Width / 2, Width, Width);
        //Area.Painter.drawRect(Middle.x - Width / 2, Middle.y - Width / 2, Width, Width);
        Area.Painter.draw(Circle);
        Area.Painter.drawLine(Middle.x - 4, Middle.y + 4, Middle.x + 4, Middle.y - 4);
        Area.Painter.drawLine(Middle.x + 4, Middle.y + 4, Middle.x - 4, Middle.y - 4);
        Player.Form.Rotation.getOriginalAxis('x', Temp1);
        double Angle = Math.PI / 2 - Math.acos(GUI.trigCap(Temp1.dotProduct(Utils3DM.Vectork)));
        double Pitch = Angle;
        
        Temp1.crossProduct(Utils3DM.Vectork, Temp2);
        Temp2.crossProduct(Temp1, Temp3);
        Temp3.normalize();
        Player.Form.Rotation.getOriginalAxis('z', Temp1);
        Angle = Math.atan2(GUI.trigCap(Temp1.dotProduct(Temp2)), GUI.trigCap(Temp1.dotProduct(Temp3)));
        double Roll = Angle;
      //  System.out.println(Math.toDegrees(Pitch) + "\t" + Math.toDegrees(Roll));
        Area.ClippedPainter.setClip(Circle);
      
        RollRot.setToTranslation(0, 0);
        
        RollRot.rotate(Roll, Middle.x, Middle.y);
        RollRot.translate(0, TranslateConstant * Pitch);
        RollRot.translate(Middle.x, Middle.y);
        
        
        Area.ClippedPainter.draw(RollRot.createTransformedShape(GroundShape));
        Area.Painter.drawLine(Middle.x + Width / 2, Middle.y, Middle.x + Width / 2 + 2, Middle.y);
        Area.Painter.drawLine(Middle.x - Width / 2, Middle.y, Middle.x - Width / 2 - 2, Middle.y);
        Area.printLine(Middle.x + Width / 2 + 5, Middle.y + 12, "" + ((int)(Math.toDegrees(Pitch) * 10)) / 10.0, 12, Color.green, 'l');
    }
    public void initGUI(){
        Middle  = new Point((int)(0.1 * Area.W - 8), (int)(0.7 * Area.H + 80));
        for(int i = 0; i < 5; i++){
            GroundShape.moveTo(-Width/(2 + i), GapWidth * i);
            GroundShape.lineTo(Width / (2 + i) + 1, GapWidth * i);
        }
        for(int i = 0; i < 15; i++){
            int INTA;
            int INTB;
            do{
                INTA = -GUI.randomInt(0, (int)(Width));
                INTB = GUI.randomInt(-Width / 2, Width / 2);
            }while(Math.sqrt(GUI.square(-INTB) + GUI.square(INTA + Width)) < Width / 2);
            
            GroundShape.moveTo(INTB - 2, INTA);
            GroundShape.lineTo(INTB, INTA - 2);
            GroundShape.lineTo(INTB + 2, INTA);
        }
        for(int i = 0; i < 20; i++){
            int INTA;
            int INTB;
            do{
                INTA = -GUI.randomInt(Width / 2, Width * 3 / 2);
                INTB = GUI.randomInt(-Width / 2, Width / 2);
            }while(Math.sqrt(GUI.square(-INTB) + GUI.square(INTA + Width)) > Width / 2 && Math.sqrt(GUI.square(-INTB) + GUI.square(INTA + Width)) > 8);
            double Theta = Math.atan2((INTA + Width), (0 - INTB));
            
            GroundShape.moveTo((float)(INTB + 2 * Math.sqrt(2) * Math.sin(Theta + Math.PI / 4 - Math.PI)), (float)(INTA + 2 * Math.sqrt(2)* Math.cos(Theta + Math.PI / 4 - Math.PI)));
            GroundShape.lineTo(INTB, INTA);
            GroundShape.lineTo((float)(INTB + 2 * Math.sqrt(2) * Math.sin(Theta - Math.PI / 4)), (float)(INTA + 2 * Math.sqrt(2) * Math.cos(Theta - Math.PI / 4)));
        }
        //Draw top cross
        GroundShape.moveTo(-4, -(Width - 4));
        GroundShape.lineTo(4, -(Width + 4));
        GroundShape.moveTo(4, -(Width - 4));
        GroundShape.lineTo(-4, -(Width + 4));
        //Draw bottom cross
        GroundShape.moveTo(-4, (Width - 4));
        GroundShape.lineTo(4, (Width + 4));
        GroundShape.moveTo(4, (Width - 4));
        GroundShape.lineTo(-4, (Width + 4));
    }
    public void drawGUI(){
        
        GUIBase();
        GUISpeed();
        GUIAltitude();
        GUILift();
        GUITargeting();
        GUIWeapons();
        GUIHorizon();
         
        
    }
    
    public void drawCursor(){
        Area.Painter.setColor(Color.white);
        double PX;
        double PY;
        if(Player.UseAI){
            PX = Player.MouseHorizontal * (Area.W / 2.0) + Area.W / 2.0;
            PY = Player.MouseVertical * (Area.H / 2.0) + Area.H / 2.0; 
        }else{
            PX = MouseX;
            PY = MouseY;
            
        }
        
        Area.Painter.drawRect((int)(PX - 10), (int)(PY - 10), 20, 20);
    }
    
    public void centerMouse(){
        int TargetX = this.getX() + Area.AX + Area.W / 2;
        int TargetY = this.getY() + Area.AY + Area.H / 2; 
        MyRobot.mouseMove(TargetX, TargetY);
        
    }
    
    public void mousePressed(MouseEvent Event){
        if(Event.getButton() == 1){
            ClickingL = true;
            Player.cycleShot();
            
        }
        if(Event.getButton() == 3){
            ClickingR = true;
            centerMouse(); 
        }
        
        
        
      
    }
    public void mouseReleased(MouseEvent Event){
        if(Event.getButton() == 1){
            ClickingL = false;
            
        }
        if(Event.getButton() == 3){
            ClickingR = false;
        }
    }
    public void mouseClicked(MouseEvent Event){
        if(Event.getButton() == 1){
            
        }
        if(Event.getButton() == 3){
            
        }
    }

    public void mouseEntered(MouseEvent Event){
    }
    public void mouseExited(MouseEvent Event){
        
           MyRobot.mouseMove(MouseX + this.getX() + Area.AX - GUI.sign(MouseDX), MouseY + this.getY() + Area.AY - GUI.sign(MouseDY));
        
    }
    
    public void mouseMoved(MouseEvent Event){
        
        MouseDX = Event.getX() - MouseX;
        MouseDY = Event.getY() - MouseX;
        MouseX = Event.getX();
        MouseY = Event.getY();
        
       // setMouseControls();
    }
    public void mouseDragged(MouseEvent Event){
        MouseDX = Event.getX() - MouseX;
        MouseDY = Event.getY() - MouseX;
        MouseX = Event.getX();
        MouseY = Event.getY();
       // setMouseControls();
    }
    
    public void shiftGrid(Point3DM Target){
        int INTX = (int)GUI.round(Target.POSX, 1.0/100);
        int INTY = (int)GUI.round(Target.POSY, 1.0/100);
        boolean ReRotate = false;
        if(Grid.Center.POSX != INTX || Grid.Center.POSY != INTY){
            ReRotate = true;
        }
        if(ReRotate){
            Grid.Center.POSX = INTX;
            Grid.Center.POSY = INTY;
            
        }
    }
    
    public void mouseWheelMoved(java.awt.event.MouseWheelEvent WheelEvent) {
        Player.TargetThrottle -= WheelEvent.getWheelRotation();
        Player.TargetThrottle = Math.max(0, Math.min(10, Player.TargetThrottle));
        
    }
    public void doTime(){
        System.out.println((int)(DelayList.average() * 100)/100.0);
        DelayList.add(System.currentTimeMillis() - Temp);
        FramesPerSecond = 1000.0 / DelayList.average();
        Temp = System.currentTimeMillis();
    }
    long Temp = System.currentTimeMillis();
    
    
}

    class Discover extends JFrame implements KeyListener, ActionListener, MouseListener, MouseMotionListener{
    
    final int TopBorderWidth = 26;
    final int X = 600 - TopBorderWidth;
    final int Y = 480;
    
    class GeneralVector extends Vector{
        public Node node(int INTA){
            return (Node)elementAt(INTA);
        }
    }
    class Node{
        int[] MinStepsToReach;
        int Color;
        boolean Finish;
        int POSX;
        int POSY;
        int NearestFinish = -1;
        public Node(int X, int Y, int StepCount, int C){
            POSX = X;
            POSY = Y;
            MinStepsToReach = new int[StepCount];
            Color = C;
            for(int i = 0; i < MinStepsToReach.length; i++){
                MinStepsToReach[i] = -1;
            }
        }
    }
    int[][] Map =    {   
                        {0, 0, 0, 1, 1, 1, 0, 1, 0, 1},//1
                        {1, 1, 1, 1, 0, 0, 0, 1, 0, 1},//2
                        {1, 0, 0, 0, 0, 1, 1, 1, 1, 0},//3
                        {0, 1, 1, 1, 0, 1, 0, 1, 1, 1},//4
                        {0, 0, 1, 1, 1, 1, 1, 1, 1, 0},//5
                        
                        {1, 0, 0, 1, 0, 0, 1, 1, 1, 1},//6
                        {1, 0, 1, 0, 0, 0, 1, 0, 1, 0},//7
                        {0, 0, 1, 0, 1, 1, 1, 0, 1, 1},//8
                        {1, 0, 0, 0, 1, 0, 0, 0, 1, 0},//9
                        {0, 1, 1, 1, 1, 1, 1, 0, 1, 0},//10
                        
                        {0, 1, 1, 0, 0, 0, 1, 0, 0, 1},//11
                        {1, 1, 0, 1, 0, 0, 1, 1, 0, 1},//12
                        {1, 1, 0, 1, 1, 1, 1, 0, 0, 0},//13
                        {0, 1, 0, 1, 1, 1, 0, 0, 0, 1},//14
                        {0, 1, 1, 0, 0, 1, 0, 1, 1, 1} //15
        
                    };//purple = 0 green = 1
    int[] Pattern = {1,  0, 0};
    Node[][] InnerMap = new Node[Map.length][Map[0].length];
    Timer Stopwatch = new Timer((int)(GUI.Speed * 100), this);
    Background Area;
   
    double MouseX;
    double MouseY;


    boolean Forward;
    boolean Back;
    boolean Right;
    boolean Left;
    
    int MaxPathLength = -1;
    GeneralVector Path = new GeneralVector();
    GeneralVector Result = new GeneralVector();
    public void saveResult(){
        Result.clear();
        Result.addAll(Path);
        System.out.println(Result.size());
        C = C + B;
        B = 0;
    }
    public void findPath(Node Start){
        initDistances();
        MaxPathLength = Map.length * Map[0].length;
        find(Start, 0, 1);
        System.out.println("C" + C);
    }
    int C;
    int B;
    
    public void initDistances(){
        long T = System.currentTimeMillis();
        for(int i = 0; i < InnerMap.length; i++){
            for(int j = 0; j < InnerMap[i].length; j++){
                Node Current = InnerMap[i][j];
                int Distance = 10000;
                for(int ii = 0; ii < InnerMap.length; ii++){
                    for(int jj = 0; jj < InnerMap[ii].length; jj++){
                        if(InnerMap[ii][jj].Finish){
                            Distance = Math.min(Distance, Math.abs(InnerMap[ii][jj].POSX - InnerMap[i][j].POSX) + Math.abs(InnerMap[ii][jj].POSY - InnerMap[i][j].POSY));
                        }
                    }
                }
                InnerMap[i][j].NearestFinish = Distance;
            }
        }
        
    }
    public String find(Node Current, int PatternCounter, int CurrentDuration){
        if(Path.contains(Current)){
            return "F";
        }
        B++;
        Path.add(Current);
        print(Area.Painter);
        Area.paintComponent(this.getGraphics());
        if(Pattern[PatternCounter] != Current.Color){
            Path.removeElementAt(Path.size() - 1);
            return "F";
        }
        if(CurrentDuration + Current.NearestFinish >= MaxPathLength && MaxPathLength != -1){
            Path.removeElementAt(Path.size() - 1);
            return "F";
        }
        
        if(Current.MinStepsToReach[PatternCounter] != -1 && Current.MinStepsToReach[PatternCounter] > CurrentDuration){
            Path.removeElementAt(Path.size() - 1);
            return "F";
        }
        
        if(Current.Finish == true){
            
            saveResult();
            Path.remove(Current);
            MaxPathLength = CurrentDuration;
            return "F";
            
        }   
            System.out.println(CurrentDuration + "\t" + Current.NearestFinish);
        
        
        //System.out.println(PatternCounter + "\t" + CurrentDuration + "\t" + Current.MinStepsToReach[PatternCounter] + "\t" + MaxPathLength);
     
        for(int i = 0; i < 4; i++){
            Node NextNode;
            try{
                switch(i){
                    case 2: if(Current.POSY - 1 < 0)continue; NextNode = InnerMap[Current.POSY][Current.POSX - 1]; break;
                    case 1: if(Current.POSY + 1 > 14)continue; NextNode = InnerMap[Current.POSY][Current.POSX + 1]; break;
                    case 3: if(Current.POSX - 1 < 0)continue; NextNode = InnerMap[Current.POSY - 1][Current.POSX]; break;
                    case 0: if(Current.POSX + 1 > 14)continue;  NextNode = InnerMap[Current.POSY + 1][Current.POSX]; break;
                    default: NextNode = Current; System.out.println("ERROR"); System.exit(0);
                }
                
            }catch(ArrayIndexOutOfBoundsException e){
                continue;
            }
            
            
            try{Thread.sleep(10);}catch(Exception e){}
           
            String S = find(NextNode, (PatternCounter + 1 == Pattern.length ? 0 : PatternCounter + 1), CurrentDuration + 1);
            
            
        }
        
        Path.removeElementAt(Path.size() - 1);
        return "F";
        
    }
    public void initMap(){
        for(int i = 0; i < Map.length; i++){
            for(int j = 0; j < Map[i].length; j++){
                InnerMap[i][j] = new Node(j, i, Pattern.length, Map[i][j]);
                if(i == Map.length - 1){
                    InnerMap[i][j].Finish = true;
                
                }
            }
        }
    }
    public void print(Graphics2D Painter){
        Painter.setColor(Color.black);
        Painter.fillRect(0, 0, X, Y);
        for(int i = 0; i < Map.length; i++){
            for(int j = 0; j < Map[i].length; j++){
                if(InnerMap[i][j].Finish){
                    Painter.setColor(Color.blue);
                    Painter.fillRect(j * 10 + 100, i * 10 + 100, 10, 10);
                    Painter.fillRect(j * 10 + 300, i * 10 + 100, 10, 10);
                }
                switch(Map[i][j]){
                    case 0: Painter.setColor(Color.magenta); break;
                    case 1: Painter.setColor(Color.green); break;
                }
                
                Painter.fillOval(j * 10 + 100, i * 10 + 100, 10, 10);
                Painter.fillOval(j * 10 + 300, i * 10 + 100, 10, 10);
                
            }
        }
        
        for(int i = 0; i < Path.size(); i++){
            
            Painter.setColor(Color.white);
            
            Painter.drawRect(Path.node(i).POSX * 10 + 100, Path.node(i).POSY * 10 + 100, 10, 10);
            if(i != 0){
                Painter.setColor(Color.red);
                Painter.drawLine(Path.node(i - 1).POSX * 10 + 105, Path.node(i - 1).POSY * 10 + 105, Path.node(i).POSX * 10 + 105, Path.node(i).POSY * 10 + 105);
            }
        }
        for(int i = 0; i < Result.size(); i++){
            Painter.setColor(Color.white);
            
            Painter.drawRect(Result.node(i).POSX * 10 + 300, Result.node(i).POSY * 10 + 100, 10, 10);
            if(i != 0){
                Painter.setColor(Color.red);
                Painter.drawLine(Result.node(i - 1).POSX * 10 + 305, Result.node(i - 1).POSY * 10 + 105, Result.node(i).POSX * 10 + 305, Result.node(i).POSY * 10 + 105);
            }
        }
        Painter.setColor(Color.white);
        Painter.drawString("Number of Steps: " + Result.size(), + 300, Map.length * 10 + 150);
    }
    public void init(){
        initMap();

    }
    public Discover(){
        //BASIC INITIALIZATION
        super("Window"); 
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new Background();
        contentArea.add(Area);
        
        
        init();
        
        Stopwatch.start();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        if(!GUI.Border){
            setUndecorated(true); 
            getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        }
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }
   
    public void keyPressed(KeyEvent Event){
        switch(Event.getKeyCode()){
            case 87: Forward = true; break;
            case 83: Back = true; break;
            case 65: Left = true; break;
            case 68: Right = true; break;
            
            

            case 27: System.exit(0); break;
            //default: System.out.println(Event.getKeyCode());
        }
        findPath(InnerMap[0][4]);
        //System.out.println(User.HAngle + "\t" + User.VAngle + "\t" + User.RAngle);
    }
    public void keyReleased(KeyEvent Event){
        switch(Event.getKeyCode()){
            case 87: Forward = !true; break;
            case 83: Back = !true; break;
            case 65: Left = !true; break;
            case 68: Right = !true; break;
            

            case 27: System.exit(0); break;
            //default: System.out.println(Event.getKeyCode());
        }

    }
 
    
    public void keyTyped(KeyEvent Event){
        
        switch(Event.getKeyChar()){
            
        }
        
    }
    
   
    
    public void actionPerformed(ActionEvent Event){
       
    
        Area.Painter.setColor(Color.black);
        Area.Painter.fillRect(0, 0, X, Y);
        Area.Painter.setColor(Color.white);
        print(Area.Painter);
        
        Area.paintComponent(this.getGraphics());
       
    }
  
    public void mousePressed(MouseEvent Event){
       
  
      
    }
    public void mouseReleased(MouseEvent Event){
       
    }
    public void mouseClicked(MouseEvent Event){
        
    }
    public void mouseEntered(MouseEvent Event){
    }
    public void mouseExited(MouseEvent Event){

    }
    public void mouseMoved(MouseEvent Event){
        MouseX = Event.getX();
        MouseY = Event.getY();
    }
    public void mouseDragged(MouseEvent Event){
        MouseX = Event.getX();
        MouseY = Event.getY();
    }
    
    class Background extends JPanel{
        
        
        BufferedImage Picture = new BufferedImage(X, Y, GUI.TankImageType);
        Graphics2D Painter = Picture.createGraphics();
        
        public void paintComponent(Graphics paint){
            

 
         
            try{
                
                //Paint.drawImage(Picture, null, 0, 0);
                paint.drawImage(Picture, 0, 0, Color.white, null);
            }catch(NullPointerException e){System.out.println(e);}
        }
    }
    
}
class Queens extends JFrame implements KeyListener, ActionListener, MouseListener, MouseMotionListener{
    
    final int TopBorderWidth = 26;
    final int X = 600 - TopBorderWidth;
    final int Y = 480;
    
    class GeneralVector extends Vector{
        
    }
    int NumberofQueens = 8;
    

        
                   
    
    Timer Stopwatch = new Timer((int)(GUI.Speed * 100), this);
    Background Area;

    public void solve(){
     
    }
    static int All = (int)Math.pow(2, 8) - 1 ;
    static int Count = 0;
    public static void Try(int Columns, int RowsA, int RowsB){
        if(Columns == All){
            Count++;
        }else{
            int Pos =  All & ~(Columns | RowsA | RowsB);

            while (Pos != 0) {
                    // X = GetFirstPos(Pos); 
                    int X = Pos & -Pos;
                    Pos = Pos & ~X;
                    Try(Columns | X, (RowsA | X) >> 1, (RowsB | X) << 1);
            }
        }
    }
    public void init(){
        
            
                int A = 10;
                int B = 1;
                int C = A & B;
    }
    public Queens(){
        
        //BASIC INITIALIZATION
        super("Window"); 
        Try(0, 0, 0);
        System.out.println(Count);
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new Background();
        contentArea.add(Area);
        
        
        init();
        
        Stopwatch.start();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        if(!GUI.Border){
            setUndecorated(true); 
            getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        }
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }
   
    public void keyPressed(KeyEvent Event){

        //System.out.println(User.HAngle + "\t" + User.VAngle + "\t" + User.RAngle);
    }
    public void keyReleased(KeyEvent Event){
        switch(Event.getKeyCode()){
 //           case 87: Forward = !true; break;
   //         case 83: Back = !true; break;
     //       case 65: Left = !true; break;
       //     case 68: Right = !true; break;
            

         //   case 27: System.exit(0); break;
            //default: System.out.println(Event.getKeyCode());
        }

    }
 
    
    public void keyTyped(KeyEvent Event){
        
        switch(Event.getKeyChar()){
            
        }
        
    }
    
   
    
    public void actionPerformed(ActionEvent Event){
       
    
        Area.Painter.setColor(Color.black);
        Area.Painter.fillRect(0, 0, X, Y);
        Area.Painter.setColor(Color.white);
        print(Area.Painter);

        Area.paintComponent(this.getGraphics());
       
    }
  
    public void mousePressed(MouseEvent Event){
       
  
      
    }
    public void mouseReleased(MouseEvent Event){
       
    }
    public void mouseClicked(MouseEvent Event){
        
    }
    public void mouseEntered(MouseEvent Event){
    }
    public void mouseExited(MouseEvent Event){

    }
    public void mouseMoved(MouseEvent Event){
   //     MouseX = Event.getX();
  //      MouseY = Event.getY();
    }
    public void mouseDragged(MouseEvent Event){
   //     MouseX = Event.getX();
   //     MouseY = Event.getY();
    }
    
    class Background extends JPanel{
        
        
        BufferedImage Picture = new BufferedImage(X, Y, GUI.TankImageType);
        Graphics2D Painter = Picture.createGraphics();
        
        public void paintComponent(Graphics paint){
            

 
         
            try{
                
                //Paint.drawImage(Picture, null, 0, 0);
                paint.drawImage(Picture, 0, 0, Color.white, null);
            }catch(NullPointerException e){System.out.println(e);}
        }
    }
    
}

class CTimer extends JFrame implements ActionListener{
    
    final int TopBorderWidth = 26;
    final int X = 240;
    final int Y = 180;

  

    
    Timer Stopwatch = new Timer((int)(10), this);
    
 

    int ButtonPress = 65;
    int Pressed;
    int Unpress;

    Robot MyRobot;
    
    public void init(){
        
        try{
   
            MyRobot = new Robot();
            
         //   doStuff();
        }catch(Exception e){
            System.out.println(e);
        }
       
        
    }
    public CTimer(){
        //BASIC INITIALIZATION
        super("Window"); 
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //ADD CONTENT PANE AND PACK
     
        this.show();
        Stopwatch.start();
        init();
        //this.pack();   
    }
   
 
    
   
   
    public void actionPerformed(ActionEvent Event){
        

        
            
            if(Pressed == 0){
		ButtonPress = (Math.random() > 0.5 ? 65 : 68);
                MyRobot.keyPress(ButtonPress);
                Pressed = 12000 + GUI.randomInt(100, 500) * GUI.randomInt(2, 10);
                Unpress = Pressed - GUI.randomInt(100, 300);
         //       System.out.println("P " + Pressed);
            }else if(Pressed == Unpress){
                MyRobot.keyRelease(ButtonPress);
            //    System.out.println("U " + Pressed);
                Pressed--;
            }else{
            //    System.out.println(Pressed);
                Pressed--;
            }
        
    }


}
class Klotski extends JFrame implements KeyListener, ActionListener, MouseListener, MouseMotionListener{
    
    final int TopBorderWidth = 26;
    final int X = 600 - TopBorderWidth;
    final int Y = 480;
    final int[] DX = {-1, 0, 1, 0};
    final int[] DY = {0, 1, 0, -1};
    final Piece OutOfBounds = new Piece();
    class PointV extends Point{
        public PointV(int A, int B){
            super(A, B);
        }
        public PointV add(PointV Target){
            return new PointV(x + Target.x, y + Target.y);
        }
        
        
    }
    class State{
        Piece[] Chunk;
        int Width = 4;
        int Height = 5;
        Piece[][] Map;
        String FlatData = "";
        public State copy(){
            State NewState = new State();
            NewState.Chunk = new Piece[Chunk.length];
            for(int i = 0; i < NewState.Chunk.length; i++){
                NewState.Chunk[i] = Chunk[i].copy();
            }
            NewState.Map = new Piece[Map.length][Map[0].length];
            
            NewState.updateMap();
            return NewState;
        }
        public State(){
        }
        public State(String Name){
            
            Chunk = new Piece[10];
            Piece Q1 = new Piece(new PointV(1, 0), 'q', 0);
            Piece V1 = new Piece(new PointV(0, 0), 'v', 1);
            Piece V2 = new Piece(new PointV(0, 2), 'v', 2);
            Piece V3 = new Piece(new PointV(3, 0), 'v', 3);
            Piece V4 = new Piece(new PointV(3, 2), 'v', 4);
            Piece H1 = new Piece(new PointV(1, 2), 'h', 5);
            Piece S1 = new Piece(new PointV(0, 4), 's', 6);
            Piece S2 = new Piece(new PointV(1, 3), 's', 7);
            Piece S3 = new Piece(new PointV(2, 3), 's', 8);
            Piece S4 = new Piece(new PointV(3, 4), 's', 9);
            Chunk[0] = Q1;
            Chunk[1] = V1;
            Chunk[2] = V2;
            Chunk[3] = V3;
            Chunk[4] = V4;
            Chunk[5] = H1;
            Chunk[6] = S1;
            Chunk[7] = S2;
            Chunk[8] = S3;
            Chunk[9] = S4;
            Map = new Piece[Width][Height];
            
            updateMap();
        }
        public void updateFlatData(){
            FlatData = "";
            for(int i = 0; i < Width; i++){
                for(int j = 0; j < Height; j++){
                    if(Map[i][j] != null){
                        FlatData += Map[i][j].Shape;
                    }else{
                        FlatData += " ";
                    }   
                }
            }
        }
        public Piece getPoint(Point P){
            if(P.x < 0 || P.y < 0 || P.x >= Map.length || P.y >= Map[P.x].length){
                return OutOfBounds;
            }else{
                return Map[P.x][P.y];
            }
        }
        public void updateMap(){
            for(int i = 0; i < Map.length; i++){
                for(int j = 0; j < Map[i].length; j++){
                    Map[i][j] = null;
                }
            }
            for(int i = 0; i < Chunk.length; i++){
                for(int j = 0; j < Chunk[i].Part.length; j++){
                 //   System.out.println();
                //    System.out.println(Map.length);
                    Map[Chunk[i].Part[j].x + Chunk[i].Position.x][Chunk[i].Part[j].y + Chunk[i].Position.y] = Chunk[i];
                }
            }
            updateFlatData();
        }
        public State checkMove(Piece Target, int Direction){
            
            
            updateMap();
            PointV Offset = new PointV(DX[Direction], DY[Direction]);
            for(int i = 0; i < Target.Part.length; i++){
                Piece Owner = getPoint(Target.Part[i].add(Target.Position).add(Offset));
                if(Owner == OutOfBounds || (Owner != Target && Owner != null)){
                    return null;
                }
            }
            State NewState = copy();
            
            NewState.Chunk[Target.Index].Position.x += DX[Direction];
            NewState.Chunk[Target.Index].Position.y += DY[Direction];
           // System.out.println(Target.Index + "\t" + Direction);
            NewState.updateMap();
            
            return NewState;
        }
        
        
    }
    class Piece{
        PointV Position;
        PointV[] Part;
        Color Tint;
        char Shape;
        int Index;
    
        public Piece copy(){
            Piece NewPiece = new Piece();
            NewPiece.Position = ((PointV)this.Position.clone());
            NewPiece.Part = new PointV[Part.length];
            for(int i = 0; i < Part.length; i++){
                NewPiece.Part[i] = new PointV(0, 0);
                NewPiece.Part[i].setLocation(Part[i].x, Part[i].y);
            }
            NewPiece.Shape = Shape;
            NewPiece.Tint = Tint;
            NewPiece.Index = Index;
            return NewPiece;
        }
        public Piece(){
        }
        public Piece(PointV Pos, char Type, int index){
            Index = index;
            Position = ((PointV)Pos.clone());
            Shape = Type;
            switch(Type){
                case 'v':
                    Part = new PointV[2];
                    Part[0] = new PointV(0, 0);
                    Part[1] = new PointV(0, 1);
                    Tint = Color.green;
                    break;
                case 'h':
                    Part = new PointV[2];
                    Part[0] = new PointV(0, 0);
                    Part[1] = new PointV(1, 0);
                    Tint = Color.blue;
                    break;
                case 's':
                    Part = new PointV[1];
                    Part[0] = new PointV(0, 0);
                    Tint = Color.yellow;
                    break;
                case 'q':
                    Part = new PointV[4];
                    Part[0] = new PointV(0, 0);
                    Part[1] = new PointV(0, 1);
                    Part[2] = new PointV(1, 0);
                    Part[3] = new PointV(1, 1);
                    Tint = Color.red;
                    break;
            }
        }
        
        
    }
    
    class StateVector extends Vector{
        public State state(int A){
            return (State)elementAt(A);
            
        }
        public State lastState(){
            return (State)lastElement();
        }
    }
    class Covered{
        String Data;
        int Time;
        public Covered(String D, int T){
            Data = D;
            Time = T;
        }
    }

    Timer Stopwatch = new Timer((int)(GUI.Speed * 100), this);
    Background Area;
   
    double MouseX;
    double MouseY;


    boolean Forward;
    boolean Back;
    boolean Right;
    boolean Left;
    State Base = new State("Normal");
    StateVector CurrentSolution = new StateVector();
    Vector CoveredStates = new Vector();
    int StepCount;
    int MinimumSteps = 100;
    public void init(){
        
        System.out.println(Base.FlatData);
    }
    public void begin(){
        StateVector Chain = new StateVector();
        Chain.add(Base);
        search(Chain);   
    }
    public Klotski(){
        //BASIC INITIALIZATION
        super("Window"); 
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new Background();
        contentArea.add(Area);
        
        
        init();
        this.setLocation(100, 100);
     //   Stopwatch.start();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        if(!GUI.Border){
            setUndecorated(true); 
         //   getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        }
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.show();
        //this.pack();   
    }
    public int beenHereFaster(State Current, int Steps){
        for(int i = 0; i < CoveredStates.size(); i++){
            Covered C = ((Covered)CoveredStates.elementAt(i));
            if(C.Data.equals(Current.FlatData) || (C.Data.substring(0, 5).equals(Current.FlatData.substring(15, 20)) && C.Data.substring(5, 10).equals(Current.FlatData.substring(10, 15)))){
                if(C.Time <= Steps){
                    return -1;
                }else{
                    return i;
                }
            }
        }
        return -2;
    }
    public boolean search(StateVector Path){

        State Current = Path.lastState();

        if(Current.Chunk[0].Position.x == 1 && Current.Chunk[0].Position.y == 3){
            if(MinimumSteps == -1 || Path.size() < MinimumSteps){
                CurrentSolution.clear();
                for(int i = 0; i < Path.size(); i++){
                    CurrentSolution.add(Path.state(i).copy());
                    MinimumSteps = CurrentSolution.size();
                }
            }
            try{
                    Thread.sleep(100000000);
            }catch(Exception e){}
            System.out.println("PATH FOUND");
            Path.remove(Path.lastElement());
            
            return true;
        }
        int A = beenHereFaster(Current, Path.size());
        if(A == -1){
            //System.out.println("BEEN HERE BEFORE");
            Path.remove(Path.lastElement());
            return false;
        }
        
        if(Path.size() >= MinimumSteps && MinimumSteps > 0){
            Path.remove(Path.lastElement());
       //     System.out.println("TOO FAR");
            return false;
        }
        printState(Current, 1, 1); 
        Area.paintComponent(this.getGraphics());
        StepCount++;
        System.out.println(Path.size() + "\t" + CoveredStates.size());
        
        if(A == -2){
            CoveredStates.add(new Covered(Current.FlatData, Path.size()));
        }else{
            ((Covered)CoveredStates.elementAt(A)).Time = Path.size();
        }
        boolean Win = false;
        for(int i = 0; i < Current.Chunk.length; i++){
            for(int j = 0; j < 4; j++){
                State New = Current.checkMove(Current.Chunk[i], j);
                
                if(New != null){
                 
                    Path.add(New);
                    boolean Temp = search(Path);
                    
                    Win = (Temp ? true : Win);
                    
                }
                
            }
        }   
        Path.remove(Path.lastElement());
     //   System.out.println("NO MORE POSSIBILITIES"); 
        
        return Win;
        
        
        
    }
    public void keyPressed(KeyEvent Event){
        switch(Event.getKeyCode()){
            case 32: begin(); System.out.println(CoveredStates.size() + "\t" + StepCount); break;
          /*  case 32: printState(Base, 1, 1);
                State K = Base.copy();
                printState(K, 5, 1);
                Area.paintComponent(this.getGraphics());
                System.out.println(K.FlatData);
                CoveredStates.add(new Covered(Base.FlatData, 10));
                System.out.println(beenHereFaster(K, 9));
                break;*/
          /*  case 32:
                for(int i = 0; i < Base.Chunk.length; i++){
                    for(int j = 0; j < 4; j++){
                        State New = Base.checkMove(Base.Chunk[i], j);
                
                        if(New != null){
                            System.out.println(i + "\t" + j);
                    
                        }
                
                    }
                }  
                
                break;*/
            case 87: Forward = true; break;
            case 83: Back = true; break;
            case 65: Left = true; break;
            case 68: Right = true; break;
            
            

            case 27: System.exit(0); break;
            default: System.out.println(Event.getKeyCode());
        }

        //System.out.println(User.HAngle + "\t" + User.VAngle + "\t" + User.RAngle);
    }
    public void keyReleased(KeyEvent Event){
        switch(Event.getKeyCode()){
            case 87: Forward = !true; break;
            case 83: Back = !true; break;
            case 65: Left = !true; break;
            case 68: Right = !true; break;
            

            case 27: System.exit(0); break;
            //default: System.out.println(Event.getKeyCode());
        }

    }
 
    
    public void keyTyped(KeyEvent Event){
        
        switch(Event.getKeyChar()){
            
        }
        
    }
    
    public void printState(State Target, int XS, int YS){
        //Target.updateMap();
        for(int i = 0; i < Target.Map.length; i++){
            for(int j = 0; j < Target.Map[i].length; j++){
                if(Target.Map[i][j] != null){
                    Area.Painter.setColor(Target.Map[i][j].Tint);
                }else{
                    Area.Painter.setColor(Color.white);
                }
                Area.Painter.fillRect((XS + i) * 50, (YS + j) * 50, 50, 50);
            }
        }
    }
    
    public void actionPerformed(ActionEvent Event){
       
    
        Area.Painter.setColor(Color.black);
        Area.Painter.fillRect(0, 0, X, Y);
        Area.Painter.setColor(Color.white);
        printState(Base, 1, 1);
        
        Area.paintComponent(this.getGraphics());
       
    }
  
    public void mousePressed(MouseEvent Event){
       
  
      
    }
    public void mouseReleased(MouseEvent Event){
       
    }
    public void mouseClicked(MouseEvent Event){
        
    }
    public void mouseEntered(MouseEvent Event){
    }
    public void mouseExited(MouseEvent Event){

    }
    public void mouseMoved(MouseEvent Event){
        MouseX = Event.getX();
        MouseY = Event.getY();
    }
    public void mouseDragged(MouseEvent Event){
        MouseX = Event.getX();
        MouseY = Event.getY();
    }
    
    class Background extends JPanel{
        
        
        BufferedImage Picture = new BufferedImage(X, Y, GUI.TankImageType);
        Graphics2D Painter = Picture.createGraphics();
        
        public void paintComponent(Graphics paint){
            

 
         
            try{
                
                //Paint.drawImage(Picture, null, 0, 0);
                paint.drawImage(Picture, 0, 0, Color.white, null);
            }catch(NullPointerException e){System.out.println("P" + e);}
        }
    }
    
}