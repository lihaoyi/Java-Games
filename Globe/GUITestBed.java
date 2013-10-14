import javax.imageio.*;
import java.io.*;
import java.util.*;
import java.awt.Toolkit;
import java.awt.*;
import java.math.BigInteger;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

public class GUITestBed{
    
    public static void main(String[] Args) throws IOException{
     
        
        
        long k = 1000L;

        new GUITest();
        long OldTime = System.currentTimeMillis();
        
       
        long NewTime = System.currentTimeMillis();
   
     
        
    }
    static void function(int A, int B, int C, double s){
        
    }
    
}

class GUITest extends JFrame implements KeyListener, ActionListener, MouseListener, MouseWheelListener{
    
    final int X = 1024;
    final int Y = 768;
    
    
   
    
    boolean LEFT;
    boolean RIGHT;
    boolean FORWARD;
    boolean BACK;
    //Point[] Points = new Point[5];
    //Point[] Points = {new Point(300, 100), new Point(400, 100), new Point(450, 120), new Point(300, 200), new Point(200, 200)};
    int PointCount = 0;
    
    
    javax.swing.Timer Stopwatch = new javax.swing.Timer((int)(0), this);
    JCanvas Area;
    Observer3DS View = new Observer3DS(-150, 0, 0, new Rotation3DS());
    long FrameCounter;
    double FramesPerSecond;
    NumberQueue DelayList = new NumberQueue(50);
    Shape3DS[] Cube = new Shape3DS[1];
    int nWidth = 10;
    double Width = 0.1;
    Point3DS[][] Grid = new Point3DS[nWidth * 2 + 1][nWidth * 2 + 1];
    Polygon3DS Water;
    Vector<Polygon3DS> GridPolygons = new Vector<Polygon3DS>();
    
    Shape3DS Earth = new Shape3DS();
    Image3DS GrassImage = new Image3DS("grass4.jpg");
    Image3DS TestImage = new Image3DS("test.png");
    Image3DS WaterImage = new Image3DS("water.png");
    Image3DS EarthImage = new Image3DS("earth.jpg");
    Image3DS CloudImage = new Image3DS("clouds.png");
    Random R = new Random(110);
    
    int Precision = 10;
    Vector<Polygon3DS> SphereSides = new Vector<Polygon3DS>();
    
    
    Perlin2D NoiseMaker = new Perlin2D(7, 1, 1021214, 5, 0, 5);
    Shape3DS Land = new Shape3DS();
    public void makeGrid(){
    	NoiseMaker.makeNoise(Width * 2, Width * 2);
    	for(int i = 0; i < Grid.length; i++){
        	for(int j = 0; j < Grid[i].length; j++){
        		Grid[i][j] = new Point3DS((i-nWidth) * Width / nWidth, (j-nWidth) * Width / nWidth, -10);	
        		//System.out.println(i*Width + "\t" + j*Width);
        		Grid[i][j].POSZ += NoiseMaker.get(i * Width / nWidth, j * Width / nWidth);
        	}	
        }
        
        for(int i = 1; i < Grid.length; i++){
       		for(int j = 1; j < Grid.length; j++){
       			Polygon3DS P1 = new Polygon3DS(new Point3DS[]{Grid[i][j], Grid[i][j-1], Grid[i-1][j]}, Color.red);
       			Polygon3DS P2 = new Polygon3DS(new Point3DS[]{Grid[i-1][j], Grid[i][j-1], Grid[i-1][j-1]}, Color.green);
       			
       			ImageTexture3DS GrassTexture = new ImageTexture3DS(GrassImage);
       			GrassTexture.peg(new Point(0, 0), new Point(512, 0), new Point(512, 512));
       			//GrassTexture.Shaded = false;
       			P1.Texture = GrassTexture;
       			GrassTexture = new ImageTexture3DS(GrassImage);
       			GrassTexture.peg(new Point(0, 0), new Point(512, 0), new Point(512, 512));
       			//GrassTexture.Shaded = false;
       			P2.Texture = GrassTexture;
       			
       			GridPolygons.add(P1);
       			GridPolygons.add(P2);
       		}	
       	}
       	Land.acceptPolygons(GridPolygons);
    }

    public void makeSphere(){
    	float R = 5;
    	Point3DS[] SP = new Point3DS[4];
    	
		
		
    	SP[0] = new Point3DS(R, R, R);
    	SP[1] = new Point3DS(-R, -R, R);
    	SP[2] = new Point3DS(-R, R, -R);
		SP[3] = new Point3DS(R, -R, -R);
    	for(int i = 0; i < SP.length; i++){
    		SP[i].normalize(R);	
    	}
    	
		int D = 6;
		
    	sphereRecurse(SP[0], SP[1], SP[2], D, R);
    	sphereRecurse(SP[1], SP[3], SP[2], D, R);
    	sphereRecurse(SP[2], SP[3], SP[0], D, R);
    	sphereRecurse(SP[3], SP[1], SP[0], D, R);
    	
    	//System.out.println(SphereSides.size());
    	
    	Earth.acceptPolygons(SphereSides);
    	System.out.println("Accepted");
    	System.out.println("SIDES " + SphereSides.size());
      System.out.println("MIN MAX");
      System.out.println(min + " " + max);
    }
    double max = 0;
    double min = 0;
    public Point sphereConvert(Point3DS P, int W, int H){
    	Point Pf = new Point();
    	Pf.x = W/2-(int)(W * P.getLongitude() / 2 / Math.PI);
    	Pf.y = H/2 -(int)(H * P.getLattitude() / Math.PI);
      min = Math.min(min, Pf.y);
      max = Math.max(max, Pf.y);
      
    	return Pf;
    }
    public void sphereSubmit(Point3DS pA, Point3DS pB, Point3DS pC){
    	Polygon3DS P = new Polygon3DS(new Point3DS[]{pA, pB, pC}, Color.green);	
		ImageTexture3DS ITS = new ImageTexture3DS(EarthImage);
		Point Pa = sphereConvert(pA, EarthImage.Width, EarthImage.Height);
		Point Pb = sphereConvert(pB, EarthImage.Width, EarthImage.Height);
		Point Pc = sphereConvert(pC, EarthImage.Width, EarthImage.Height);
		
      
		//this part is to prevent edge pieces from wrapping around the sphere the wrong way
		if(Pa.x < EarthImage.Width / 4){
			if(Pb.x > EarthImage.Width * 3/4)Pb.x -= EarthImage.Width;	
			if(Pc.x > EarthImage.Width * 3/4)Pc.x -= EarthImage.Width;
		}
		if(Pa.x > EarthImage.Width * 3/4){
			if(Pb.x < EarthImage.Width / 4)Pb.x += EarthImage.Width;	
			if(Pc.x < EarthImage.Width / 4)Pc.x += EarthImage.Width;
		}

    //  FlatTexture3DS ITS = new FlatTexture3DS(Color.white);
		ITS.peg(Pa, Pb, Pc);
		// ITS.Shaded = false;
		P.Texture = ITS;
		//P.Texture.Shaded = false;
		SphereSides.add(P);
	}
    public void sphereRecurse(Point3DS pA, Point3DS pB, Point3DS pC, int D, float R){
    	if(D == 0){
    		sphereSubmit(pA, pB, pC);
    		return;
    	}
    	Point3DS pD = pA.add(pB).divide(2);	
    	Point3DS pE = pB.add(pC).divide(2);
    	Point3DS pF = pC.add(pA).divide(2);
    	
    	pD.normalize(R);
    	pE.normalize(R);
    	pF.normalize(R);
    	
    	sphereRecurse(pA, pD, pF, D-1, R);
    	sphereRecurse(pD, pB, pE, D-1, R);
    	sphereRecurse(pF, pD, pE, D-1, R);
    	sphereRecurse(pF, pE, pC, D-1, R);
    }
    public void makeCubes(){
	 	for(int i = 0; i < Cube.length; i++){
	      //  Cube[i] = new Shape3DS(false, GUI.randomInt(4, 8), GUI.randomInt(-1, 1), GUI.randomInt(-1, 1));
	        Cube[i] = new Shape3DS(Color.red, new Point3DS(0, 0, 0));
	        Utils3DS.setPoints(Cube[i], 'c', 6, 6, 6);
	        //Cube[i].Center.add(Utils3DS.getRandomUnit().multiply(3), Cube[i].Center);
	        for(int j = 0; j < Cube[i].Polygons.length; j++){
	        	ImageTexture3DS IT = new ImageTexture3DS(TestImage);
				IT.peg(new Point(10, 10), new Point(10, 1190), new Point(1590, 10));
	        	Cube[i].Polygons[j].Texture = IT;	
	        }
	    }
	}
    public void init(){
    	
    	EarthImage.compound(CloudImage);
        

        View.setImage(Area.Picture);
        View.setArea(X, Y, 10);
        View.initLights(2);
        View.setLight(0, new double[]{0.5, 0.5, 0.5}, new Point3DS(1, 0, 0));
        View.setLight(1, new double[]{0.5, 0.5, 0.5}, new Point3DS(1, 0, 0));
        View.setAmbient(new double[]{0.01, 0.01, 0.01});
        
        View.Painter = Area.Painter;
        
        makeGrid();
       
        makeCubes();
        Water = new Polygon3DS(new Point3DS[]{new Point3DS(-10, -10, -10), new Point3DS(-10, 10, -10), new Point3DS(10, 10, -10), new Point3DS(10, -10, -10)}, Color.blue);
     	Water.Texture = new ImageTexture3DS("water.png");
     	//Water.Texture.Shaded = false;
     	((ImageTexture3DS)Water.Texture).peg(new Point(0, 0), new Point(0, 1024), new Point(1024, 1024));
        
        makeSphere();
     //   P[0].ActualPoints[0].setLocation(4, 10, 0);
     //   P[0].ActualPoints[1].setLocation(4, 0, 4);
     //   P[0].ActualPoints[2].setLocation(4, -10, 0);
     //   P[0].ActualPoints[3].setLocation(4, 0, -4);
        Turner.rotateAround('z', -Math.PI / 2.7); 
        Rotation3DS.multiply(Turner, Earth.Rotation, Earth.Rotation);
    }
    public GUITest(){
        //BASIC INITIALIZATION
        super("Window");
        this.setSize(X, Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //SET CONTENT PANE
        Container contentArea = getContentPane();
        contentArea.setLayout(null);
        //LAYOUT MANAGER
        //ADD ITEMS TO CONTENT PANE
        Area = new JCanvas();
        Area.setBounds(0, 0, X, Y);
        Area.init(BufferedImage.TYPE_INT_RGB);
        contentArea.add(Area);
        this.setLocation(1280 / 2 - X / 2, 1024 / 2 - Y / 2);
        this.addKeyListener(this);
        Area.addMouseListener(this);
        this.setUndecorated(true);
        Area.addMouseWheelListener(this);
        //ADD CONTENT PANE AND PACK
        this.setContentPane(contentArea);
        this.setVisible(true);
        init();
        Stopwatch.start();
        //this.pack();   
    }

    //EVENT TRIGGERS
    Rotation3DS Turner = new Rotation3DS();
    double Momentum = 0;
    public void keyPressed(KeyEvent e){
   //     System.out.println(e.getKeyChar() + "\t" + LEFT + "\t" + RIGHT + "\t" + FORWARD + "\t" + BACK);
        switch(e.getKeyCode()){
            case 37: LEFT = true; break;
            case 39: RIGHT = true; break;
            case 38: FORWARD = true; break;
            case 40: BACK = true; break;

        }
    	switch(e.getKeyCode()){
            case 37: System.out.println("L"); Momentum += 0.1;  break;
            case 39: System.out.println("R"); Momentum -= 0.1; break;
            case 38: View.Position.add(View.LookVector.divide(0.2), View.Position); break;
            case 40: View.Position.subtract(View.LookVector.divide(0.2), View.Position); break;

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
    	System.out.println(e.getKeyCode());
        
    }
   	
    public void actionPerformed(ActionEvent e){
        doTime();
        View.blankOut();
        Area.Painter.setColor(Color.black);
        Area.Painter.fillRect(0, 0, X, Y);
        View.blankOut();
        View.setLookVector();
        
        //GridPolygons.size(); i++){
        //	GridPolygons.get(i).submit(View);	
       // }
       	//Land.calcPoints();
       //	Land.submit(View);
       	
        //System.out.println(Water.Normal.dotProduct(View.LookVector));
        
        
    //    Water.submit(View);
        
        //for(int i = 0; i < SphereSides.size(); i++){
       // 	SphereSides.get(i).submit(View);	
        //}
        Turner.rotateAround('z', Momentum / FramesPerSecond); 
        Rotation3DS.multiply(Turner, Earth.Rotation, Earth.Rotation);
        Earth.calcPoints();
        Earth.submit(View);
      
        for(int i = 0; i < Cube.length; i++){
        	Cube[i].calcPoints();
     	//	Cube[i].submit(View);   		
     	
    	}
        
        
    	
		View.printAll();
		
        Area.paintComponent(null);

      //  }
    }
    
    int RotCount = 0;
    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
      //  Area.Painter.setColor(Color.black);
          //  Area.Painter.fillRect(0, 0, 10000, 10000);
        if(mouseEvent.getButton() == 1){
            Rotation3DS New = new Rotation3DS();
            Turner.rotateAround('z', -2 / FramesPerSecond);
            if(BACK){
                for(int i = 0; i < Cube.length; i++){
                    Rotation3DS.multiply(Turner, Cube[i].Rotation, Cube[i].Rotation);
                }
            }else{
                Rotation3DS.multiply(Turner, View.Rotation, View.Rotation);
                Turner.transform(View.Position, View.Position);
            }
            System.out.println(View.Position);
          //  View.Rotation.print();
            RotCount--;
        }else{
            
            Turner.rotateAround('z', 2 / FramesPerSecond);
            if(BACK){
                for(int i = 0; i < Cube.length; i++){
                    Rotation3DS.multiply(Turner, Cube[i].Rotation, Cube[i].Rotation);
                }
            }else{
                Rotation3DS.multiply(Turner, View.Rotation, View.Rotation);
                Turner.transform(View.Position, View.Position);
            }
           // View.Rotation.print();
            RotCount++;
        }/*
        Polygon3DS P = Cube[0].Polygons[0];//5 front 2 back
        System.out.println((P.A * mouseEvent.getX() + P.B * mouseEvent.getY() + P.C));
        System.out.println(P.FlatDepths[4] + "\t" + P.A + "\t" + P.B + "\t" + P.C);
       */
        //double Depth = (A*PX + B*PY + C);
    }    
   
    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }    

    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    	
    }
    
    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
    }
    public void doTime(){
        System.out.println((int)(DelayList.average() * 100)/100.0);
        DelayList.add(System.currentTimeMillis() - Temp);
        FramesPerSecond = 1000.0 / DelayList.average();
        Temp = System.currentTimeMillis();
    }
    long Temp = System.currentTimeMillis();
    public void mouseWheelMoved(java.awt.event.MouseWheelEvent mouseWheelEvent) {
        Area.Painter.setColor(Color.black);
      //  Area.Painter.fillRect(0, 0, 10000, 10000);
        if(mouseWheelEvent.getWheelRotation() == 1){
            Rotation3DS New = new Rotation3DS();
            New.rotateAround('y', -Math.PI / 32);
            if(BACK){
               for(int i = 0; i < Cube.length; i++){
                    Rotation3DS.multiply(New, Cube[i].Rotation, Cube[i].Rotation);
                }
            }else{
                Rotation3DS.multiply(New, View.Rotation, View.Rotation);
                
                New.transform(View.Position, View.Position);
            }
       //     View.Rotation.print();
        //    RotCount--;
        }else{
            Rotation3DS New = new Rotation3DS();
            New.rotateAround('y', Math.PI / 32);
            if(BACK){
                for(int i = 0; i < Cube.length; i++){
                    Rotation3DS.multiply(New, Cube[i].Rotation, Cube[i].Rotation);
                }
            }else{
                Rotation3DS.multiply(New, View.Rotation, View.Rotation);
                New.transform(View.Position, View.Position);
            }
        //    View.Rotation.print();
         //   RotCount++;
        }
    }
    
}