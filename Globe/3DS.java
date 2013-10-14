import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.imageio.*;

import java.nio.*;

class Utils3DS{
    //Base Vectors
    static final Point3DS Origin = new Point3DS(0, 0, 0);
    static final Point3DS Vectori = new Point3DS(1, 0, 0);
    static final Point3DS Vectorj = new Point3DS(0, 1, 0);
    static final Point3DS Vectork = new Point3DS(0, 0, 1);

    
    //Polygon Calculating Normal Vectors
    private static final Point3DS DirectionA = new Point3DS();
    private static final Point3DS DirectionB = new Point3DS();
    public static Point3DS getNormal(Point3DS A, Point3DS B, Point3DS C){
    	C.subtract(A, DirectionA);
    	B.subtract(A, DirectionB);
    	Point3DS N = DirectionA.cross(DirectionB);
    	N.normalize();
    	return N;
    }
    
    public static Point3DS getRandomUnit(){
        Point3DS Position = new Point3DS();
        do{
            Position.setLocation(2 * Math.random() - 1, 2 * Math.random() - 1, 2 * Math.random() - 1);
        }while(Position.length() > 1 || Position.length() == 0);
        Position.divide(Position.length(), Position);

        return Position;

    }
    public static float getR(int RGB){
    	return (RGB & 0xff0000) * 1.0f / 0xff0000;	
    }
    public static float getG(int RGB){
    	return (RGB & 0xff00) * 1.0f / 0xff00;	
    }
    public static float getB(int RGB){
    	return (RGB & 0xff) * 1.0f / 0xff;	
    }
    
    public static int scaleColor(int RGB, double R){
        
        return	((RGB & 0xff0000) * R > 0xff0000 ? 0xff0000 : ((int)((RGB & 0xff0000) * R) & 0xff0000)) +
    			((RGB & 0xff) * R> 0xff ? 0xff : ((int)((RGB & 0xff) * R) & 0xff)) + 
    			((RGB & 0xff00) * R > 0xff00 ? 0xff00 : ((int)((RGB & 0xff00) * R) & 0xff00));
    }
    public static int scaleColor(int RGB, double[] R){ //scale is in double[]{R, G, B}
    	//return 255 << 24 | ((int)(((RGB >> 16) & 0xFF) * Scale[0]) << 16 & 255 << 16) | ((int)(((RGB >> 8) & 0xFF) * Scale[1]) << 8 & 255 << 8) | ((int)(((RGB >> 0) & 0xFF) * Scale[2]) & 255);
    	return	((RGB & 0xff0000) * R[2] > 0xff0000 ? 0xff0000 : ((int)((RGB & 0xff0000) * R[2]) & 0xff0000)) +
    			((RGB & 0xff) * R[0] > 0xff ? 0xff : ((int)((RGB & 0xff) * R[0]) & 0xff)) + 
    			((RGB & 0xff00) * R[1] > 0xff00 ? 0xff00 : ((int)((RGB & 0xff00) * R[1]) & 0xff00));
    		//
    		// + ;
    	//return ((int)Math.min(255, (RGB >> 0) * Scale[2]) << 0) + ((int)Math.min(255, (RGB >> 8) * Scale[1]) << 8) + ((int)Math.min(255, (RGB >> 16) * Scale[0]) << 16);
    }

    public static void setPoints(Shape3DS Target, char Shape, double ArgA, double ArgB, double ArgC){
    
        
        Vector<Polygon3DS> Polygons = new Vector<Polygon3DS>();
        int PolygonCount = 0;
        
        switch(Shape){
            case 'f':{
                Target.initArrays((int)(ArgA * ArgB));
                
                Polygon3DS P = new Polygon3DS(3);
                PolygonCount++;
                
                for(int i = 1; i < ArgA; i++){
                    for(int j = 1; j < ArgB; j++){
                        
                    }
                }
                
                
                
                
            }break;
            case 'c':{
                Target.initArrays(8);
                
                
                for(double i = -ArgA/2; i <= ArgA/2; i+=ArgA){
                    for(double j = -ArgB/2; j <= ArgB/2; j+=ArgB){
                        for(double k = -ArgC/2; k <= ArgC/2; k+=ArgC){
                            Target.BasePoints.add(new Point3DS(i, j, k));
                        }
                    }
                }
                PolygonCount = 6;
                
                Target.BasePoints.toArray(Target.Points);
                
                for(int i = 0; i < PolygonCount; i++){
                    Polygons.add(new Polygon3DS(4));
                    Polygons.get(i).Texture = new FlatTexture3DS(Color.getColor(null, GUI.randomInt(0, Integer.MAX_VALUE)));
                }
                Target.Polygons = new Polygon3DS[PolygonCount];
                Polygons.toArray(Target.Polygons);
                
                addLinks(Target.Polygons[0], new int[]{0, 1, 5, 4}, Target);
                addLinks(Target.Polygons[1], new int[]{0, 4, 6, 2}, Target);
                addLinks(Target.Polygons[2], new int[]{5, 7, 6, 4}, Target);
                addLinks(Target.Polygons[3], new int[]{7, 3, 2, 6}, Target);
                addLinks(Target.Polygons[4], new int[]{3, 7, 5, 1}, Target);
                addLinks(Target.Polygons[5], new int[]{1, 0, 2, 3}, Target);
                

            }break;
            case 't':{
                Target.initArrays(4);
                
                Target.BasePoints.add(new Point3DS(25, -20, 0));
                Target.BasePoints.add(new Point3DS(25, 0, 8));
                Target.BasePoints.add(new Point3DS(25, 20, 0));
                Target.BasePoints.add(new Point3DS(25, 0, -8));
                Target.BasePoints.toArray(Target.Points);
                
                PolygonCount = 1;
                
                Target.Polygons = new Polygon3DS[PolygonCount];
                
                Polygons.add(new Polygon3DS(4));
                Polygons.toArray(Target.Polygons);
                
                addLinks(Target.Polygons[0], new int[]{0, 1, 2, 3}, Target);
                
                
            }break;
            
            
        }
        Target.finalizeData();
        
    }
    public static void addPolygon(Polygon3DS Target, Shape3DS Base){
        
        for(int i = 0; i < Target .ActualPoints.length; i++){
            if(true){
                
            }
        }
            
        
    }
    
    public static void addLinks(Polygon3DS Target, int[] Links, Shape3DS Base){
    	Target.acceptPoints(Links, Base);
        
        
    }
}
class Shape3DS{
    Vector<Point3DS> BasePoints = new Vector<Point3DS>();
    Point3DS[] Points; // Base Points before rotation and translation; Points relative to the object
    Point3DS[] ActualPoints; //Actual Points after rotation and translation; Points in 3D Space
    Point3DS[] TransformedPoints; //Imaginary Points centered on an arbitrary Viewpoint; Points relative to Viewer
    
    
    Polygon3DS[] Polygons; //list of polygons each comprising a set of points
    
    private boolean[] ClipTest = new boolean[4]; //Preprocessed data to easily tell what planes of the clipping frustrum the polygons do not need to be tested against
    private boolean[] BackClipTest = new boolean[1]; //preprocessed data to easily tell whether polygons need to be tested against the near clipping plane
    
    int PointCount; //number of points
        
    
    Point3DS Center; //center of object around which rotations transform points; combined with Rotation to map Points to ActualPoints
    Point3DS TransformedCenter;
    
    double MaxDistance; //the maximum distance from the Center of the Shape3DS of any of it's constituent points; used for early culling
    
    Rotation3DS Rotation = new Rotation3DS(); //the rotation which transforms this shape around the Center; combined with Center to map Points to ActualPoints
    boolean Visible = true; //whether this object is visible; switch to toggle visibility
    private boolean OutOfFrustum; //whether this object is entirely outside the view frustrum: calculated during preClip and used for culling
    
    public void finalizeData(){ //fixes pre-calculated data and initializes variables in Shape3DS after points/planes have been defined
        for(int i = 0; i < Points.length; i++){
            MaxDistance = Math.max(MaxDistance, Points[i].length());
        }
        
        
        
    }
    public void initArrays(int tPointCount){
        PointCount = tPointCount;
        Points = new Point3DS[PointCount];
        ActualPoints = new Point3DS[PointCount];
        TransformedPoints = new Point3DS[PointCount];
        for(int i = 0; i < PointCount; i++){
        	Points[i] = new Point3DS();
            ActualPoints[i] = new Point3DS();
            TransformedPoints[i] = new Point3DS();
        }
    }
    private Comparator<Point3DS> PointSorter = new Comparator<Point3DS>(){
		public int compare(Point3DS A, Point3DS B){
			if(A.POSX != B.POSX){return Double.compare(A.POSX, B.POSX);	}
			if(A.POSY != B.POSY){return Double.compare(A.POSY, B.POSY);	}
			if(A.POSZ != B.POSZ){return Double.compare(A.POSZ, B.POSZ);	}
			return 0;
		}	
		public boolean equals(Point3DS A, Point3DS B){
			return A.POSX == B.POSX && A.POSY == B.POSY && A.POSZ == B.POSZ;	
		}
		
	};
    public void acceptPolygons(Vector<Polygon3DS> V){ //fleshes out an empty object from a set of polygons. forms point lists from polygon's points and links polygons to respective points
    	Polygons = new Polygon3DS[V.size()];
    	V.toArray(Polygons);
    	Set<Point3DS> PointSet = new TreeSet<Point3DS>(PointSorter);
    	for(int i = 0; i < Polygons.length; i++){
    		for(int j = 0; j < Polygons[i].ActualPoints.length; j++){
    			for(int k = 0; k < 4; k++)
    			PointSet.add(Polygons[i].ActualPoints[j]);
    		}
    	}
    	
    	initArrays(PointSet.size());
    	System.out.println(PointSet.size());
    	PointSet.toArray(ActualPoints);
    	for(int i = 0; i < Polygons.length; i++){
    		for(int j = 0; j < Polygons[i].ActualPoints.length; j++){
    			int k = Arrays.binarySearch(ActualPoints, Polygons[i].ActualPoints[j], PointSorter);
    			Polygons[i].ActualPoints[j] = ActualPoints[k];
    			Polygons[i].TransformedPoints[j] = TransformedPoints[k];	
    			
    		}
    	}
    	center();
    	finalizeData();
    }
    public void center(){
    	Point3DS Sum = new Point3DS();
    	for(int i = 0; i < ActualPoints.length; i++){
    		Sum.add(ActualPoints[i], Sum);		
    	}	
    	Sum.divide(PointCount, Sum);
    	for(int i = 0; i < ActualPoints.length; i++){
    		ActualPoints[i].subtract(Sum, Points[i]);
    	}	
    	Center = Sum;
    }
    public Shape3DS(){
        Center = new Point3DS(0, 0, 0);
       	TransformedCenter = new Point3DS();
            
    }
    public Shape3DS( double tPOSX, double tPOSY, double tPOSZ){
        
        Center = new Point3DS(tPOSX, tPOSY, tPOSZ);
       	TransformedCenter = new Point3DS();
    }
    public Shape3DS(Color tTint, Point3DS tPosition){
       
        Center = new Point3DS(tPosition.POSX, tPosition.POSY, tPosition.POSZ);
        TransformedCenter = new Point3DS();
    }
    public final void calcPoints(){ //called to update ActualPoints when Points, Center or Rotation have changed; usually called every frame
        for(int i = 0; i < PointCount; i++){  
        	
            Points[i].rotate(Rotation, ActualPoints[i]);
            ActualPoints[i].add(Center, ActualPoints[i]);
        }
        
        for(int i = 0; i < Polygons.length; i++){
            Polygons[i].update();
            
        	
        
        }
       
    }

   
    public final void preClip(Observer3DS Viewer){ //preprocessing to quickly discard frustrum planes constituent polygons can ignore; also tests OutOfFrustrum to cull whole objects.
        OutOfFrustum = false;
        
        //System.out.println("\t" + MaxDistance);
        Point3DS TempPoint = new Point3DS();
        TransformedCenter.copyTo(TempPoint);
        TempPoint.POSX -= Viewer.NearClip;
        double DotProduct = Viewer.BackFrustum.dotProduct(TempPoint);
      //  System.out.println(DotProduct);
        if(DotProduct > MaxDistance){
            BackClipTest[0] = false;
        }else if(DotProduct < -MaxDistance){
            OutOfFrustum = true;
            return;
        }else{
        	BackClipTest[0] = true;		
        }
        
        for(int i = 0; i < 4; i++){
        	
            DotProduct = Viewer.Frustum[i].dotProduct(TransformedCenter);
          //  System.out.println(DotProduct);
            if(DotProduct > MaxDistance){
                ClipTest[i] = false;
            }else if(DotProduct < -MaxDistance){
                OutOfFrustum = true;
                return;
            }else{
            	ClipTest[i] = true;
            }
        }
        
    }
 
    
 

    public boolean behindObserver(Observer3DS Viewer){ //checks to see if every point in Eye-Space is behind Eye; used for culling
        for(int i = 0; i < PointCount; i++){
        	if(TransformedPoints[i].POSX > 0){
        		return false;
        	}	
        }
        return true;
        
    }
    public void center(Observer3DS Viewer){ //centers every point, mapping from Space to Viewer's Eye-Space
    	for(int i = 0; i < PointCount; i++){
    		ActualPoints[i].center(Viewer, TransformedPoints[i]);	
    	}	
    	Center.center(Viewer, TransformedCenter);
    }
   
    public final void submit(Observer3DS Viewer){ //Pre-calculates some values and culls object/polygons before passing remainder to Viewer; identical to calling submit on individual Polygon3DS' except for whole shape centering and culling
    	//Actual Points are Calculated somewhere before this
    	center(Viewer);
    	
    	if(behindObserver(Viewer)){
    
            return;
        }		
        preClip(Viewer);
        if(OutOfFrustum){
        	//System.out.println("B");
            return;
        }
     //   System.out.println(Arrays.toString(BackClipTest));
     //   System.out.println(Arrays.toString(ClipTest));

        for(int i = 0; i < Polygons.length; i++){
        	Polygons[i].update();
        	if(Polygons[i].Normal.dotProduct(Polygons[i].Centroid.subtract(Viewer.Position)) < 0){
        		Viewer.BackFacing++;
				//back facing
	    		continue;	
	    	}
	    	Polygons[i].center(Viewer);
	    	if(Polygons[i].behindObserver(Viewer)){
	    		//behind test
	            continue;
	        }
	    	
        	Polygons[i].ClipTest = ClipTest;
        	Polygons[i].BackClipTest = BackClipTest;
        	Viewer.accept(Polygons[i]);	
        }
    }
    public final void print(Observer3DS Viewer){//prints the whole shape immediately; identical to printing individual polygons immediately
        //Actual Points are Calculated somewhere before this
        
        
        for(int i = 0; i < Polygons.length; i++){
        	Polygons[i].print(Viewer);
        	
        }

     
      

    }
}
class Renderer3DS{
	
}
class Polygon3DS{
	
	
	
    //Plane Equation Data
    //Ax + By + C = Depth
    
    private static double A;
    private static double B;
    private static double C;
    
    //static shared variables used in rendering
    
    private static Point3DS[] ClippedPoints = fill3D(20); //linked-list of points where clipping takes place: TransformedPoints are entered and then Clipped to the view frustrum
    private static int[] LinkArray = filli(20, -1); //links in ClippedPoints
    private static int ClippedCount; //final number of points remaining after clipping is complete
    
    
   // private static Point[] FlatPoints = fill2(20); //array of pixel-accurate screen positions of ClippedPoints; Linked-List is unravelled into Array
    private static Point2D.Double[] ExactFlatPoints = fill2D(20); //array of decimel precision screen positions of ClippedPoints; Linked-List is unravelled into Array
    private static float[] FlatDepths = new float[20]; //array of Depth values for ExactFlatPoints
    
    private static float DepthX; //Extreme case of depth; used to store shallowest depth on polygon
    
    
    
    
    Point3DS[] ActualPoints; //vertices in 3D space; can be independent points or pointers to points in a Shape3DS
    Point3DS[] TransformedPoints; //vertices in eye space; can be independent points or pointers to points in a Shape3DS
    
    int PointCount; //number of points
    
    Point3DS Centroid; //centroid, defined as average position of all points
    Point3DS Normal; //normal to plane

    Texture3DS Texture; //texture of plane
    
    
    boolean[] ClipTest; //data transferred from owning Shape3DS describing which frustrum planes clipping needs to be done against; null otherwise
    boolean[] BackClipTest; //data transferred from owning Shape3DS describing which frustrum planes clipping needs to be done against; null otherwise
    

    private static Point3DS[] fill3D(int N){
    	Point3DS[] Array = new Point3DS[N];
    	for(int i = 0; i < N; i++){
    		Array[i] = new Point3DS();	
    	}
    	return Array;
    }
    private static int[] filli(int N, int V){
    	int[] Array = new int[N];
    	for(int i = 0; i < N; i++){
    		Array[i] = V;
    	}	
    	return Array;
    }
    private static Point2D.Double[] fill2D(int N){
    	Point2D.Double[] Array = new Point2D.Double[N];
    	for(int i = 0; i < N; i++){
    		Array[i] = new Point2D.Double();	
    	}
    	return Array;	
    }
    private static Point[] fill2(int N){
    	Point[] Array = new Point[N];
    	for(int i = 0; i < N; i++){
    		Array[i] = new Point();	
    	}
    	return Array;	
    }

    
    public Polygon3DS(int tPointCount){ //generate blank Polygon3DS with space for a number of points
        PointCount = tPointCount;
        ActualPoints = new Point3DS[PointCount];
       	TransformedPoints = new Point3DS[PointCount];
        for(int i = 0; i < PointCount; i++){
            ActualPoints[i] = new Point3DS();
			TransformedPoints[i] = new Point3DS();
        }
       	Centroid = new Point3DS();
        Normal = new Point3DS();
		update();
		Texture = new FlatTexture3DS(Color.white);
    }
    public Polygon3DS(Point3DS[] PointList, Color C){ //generates a full Polygon3DS using the target points
    	PointCount = PointList.length;
    	ActualPoints = new Point3DS[PointCount];
    	TransformedPoints = new Point3DS[PointCount];
    	for(int i = 0; i < PointCount; i++){
    		ActualPoints[i] = PointList[i].copy();
    		TransformedPoints[i] = new Point3DS();
    	}
    	Centroid = new Point3DS();
    	Normal = new Point3DS();
    	update();
    	Texture = new FlatTexture3DS(C);
    	
    }
    public void acceptPoints(int[] Links, Shape3DS Base){ //sets the polygon to comprise a set of points from the target shape
    	for(int i = 0; i < Links.length; i++){
            TransformedPoints[i] = Base.TransformedPoints[Links[i]];
            ActualPoints[i] = Base.ActualPoints[Links[i]];
        }
    }
    //public void print(Observer3DS Viewer, boolean BackClipTest, boolean[] ClipTest){
	
    private void toClip(){ //prepares the ClippedPoints for clipping; copies data from TransformedPoints and initialized LinkArray into simple sequential order
        for(int i = 0; i < PointCount; i++){
            TransformedPoints[i].copyTo(ClippedPoints[i]);
            LinkArray[i] = i + 1;
        }
        for(int i = PointCount; i < LinkArray.length; i++){
            LinkArray[i] = -1;
        }
        LinkArray[PointCount - 1] = 0;
        ClippedCount = PointCount;
        
    }
    private int getFirstEmptySlot(){ //get first null entry in ClippedPoints; used to store additional points generated by clipping
        for(int i = 0; i < LinkArray.length; i++){
            if(LinkArray[i] == -1){
                return i;
            }
        }
        return -1;
    }
    
    
   	private static Point3DS ExitPoint = new Point3DS(); 
    private static Point3DS ExitPointB = new Point3DS();
    private static Point3DS EnterPoint = new Point3DS();
    private static Point3DS EnterPointB = new Point3DS();
    private static Point3DS CalcPointA = new Point3DS();;
    private static Point3DS CalcPointB = new Point3DS();
    private void clip(Observer3DS Viewer, Point3DS Frustum, double XIntercept){ //clips this Polygon3DS against the plane defined by the normal Frustrum and offset by XIntercept in the x direction
	    int ExitIndex;
	    int ExitIndexB;
	    int EnterIndex;
	    int EnterIndexB;
	    boolean Repeat;
	    boolean AllRightSide;
	    boolean AllWrongSide;
	    boolean BIn;
	    boolean AIn;
      //  
      
        for(int i = 0; i < ClippedPoints.length; i++){
            ClippedPoints[i].POSX -= XIntercept;
        }
        ExitIndex = -1;
        ExitIndexB = -1;
        EnterIndex = -1;
        EnterIndexB = -1;
        Repeat = false;
        AllRightSide = true;
        AllWrongSide = true;
        BIn = ClippedPoints[0].dotProduct(Frustum) > 0;
        AIn = false;
        for(int i = 0; !(ExitIndex != -1 && EnterIndex != -1 || i == 0 && Repeat == true); i = LinkArray[i], Repeat = true){
            
            
            
            Point3DS PA = ClippedPoints[i];
            Point3DS PB = ClippedPoints[LinkArray[i]];
            AIn = BIn;
            BIn = PB.dotProduct(Frustum) > 0;
            
            if(!BIn && AIn){
                ExitIndex = i;
                ExitIndexB = LinkArray[i];
                PA.copyTo(ExitPoint);
                PB.copyTo(ExitPointB);
            }else if(!AIn && BIn){
                EnterIndex = i;
                EnterIndexB = LinkArray[i];
                PA.copyTo(EnterPoint);
                PB.copyTo(EnterPointB);
            }
            
            AllRightSide = AllRightSide && AIn;
            AllWrongSide = AllWrongSide && !AIn;
            
  
            
        }
        if(AllWrongSide){
            
            ClippedCount = 0;
         
            //System.out.println("OFFSCREEN");
         //   LinkArray[0] = -1;
        }else if(AllRightSide){
            
        }else{
            
            //insert two new points
            int j = 0;
            for(int i = ExitIndexB; i != EnterIndexB; i = j){
                j = LinkArray[i];
                LinkArray[i] = -1;
                ClippedCount--;
            }
            
            ClippedCount = ClippedCount + 2;
            int ExitStore = getFirstEmptySlot();
            LinkArray[ExitStore] = 0;
            int EnterStore = getFirstEmptySlot();
            LinkArray[ExitIndex] = ExitStore;
            LinkArray[ExitStore] = EnterStore;
            LinkArray[EnterStore] = EnterIndexB;
         
            //Calculate Clipping
            double DX;
            
            Point3DS PA;
            Point3DS PB;
            double T;
            
            PA = EnterPoint; //Before Entering Frustum
            PB = EnterPointB; //After Entering Frustum
            
            PB.subtract(PA, CalcPointA);
            T = PB.dotProduct(Frustum) / CalcPointA.dotProduct(Frustum);
            PA.multiply(T, CalcPointA);
            PB.multiply(1 - T, CalcPointB);
            CalcPointA.add(CalcPointB, CalcPointA);
            CalcPointA.copyTo(ClippedPoints[EnterStore]);
            
       
            
            PA = ExitPoint; //Before Exiting Frustum
            PB = ExitPointB; //After Exiting Frustum
            
            PB.subtract(PA, CalcPointA);
            T = PB.dotProduct(Frustum) / CalcPointA.dotProduct(Frustum);
            PA.multiply(T, CalcPointA);
            PB.multiply(1 - T, CalcPointB);
            CalcPointA.add(CalcPointB, CalcPointA);
            CalcPointA.copyTo(ClippedPoints[ExitStore]);

        }
        
        for(int i = 0; i < ClippedPoints.length; i++){
            ClippedPoints[i].POSX += XIntercept;
        }

    }
    private void pointify(Observer3DS Viewer){ //converts the polygon's clipped Point3DS into flat Point2D.Double

        int Current = 0;
        DepthX = 0;
        for(int i = 0; i < ClippedCount; i++){
        //    ClippedPoints[Current].toScreen(Viewer, FlatPoints[i]);
            ClippedPoints[Current].toScreen(Viewer, ExactFlatPoints[i]);
           // FlatPoints[i].x = GUI.cap(Viewer.MinX, FlatPoints[i].x, Viewer.MaxX); 
           // FlatPoints[i].y = GUI.cap(Viewer.MinY, FlatPoints[i].y, Viewer.MaxY);
            
            
            //X and Y ranges from 0 to MaxValue + 1 due to
            //the rasterizer ignoring the last pixel
          //  System.out.println(FlatPoints[i]);
            
            FlatDepths[i] = (int)(Integer.MAX_VALUE / ClippedPoints[Current].POSX * Viewer.NearClip);
          //  FlatDepths[i] = 10000000-(float)ClippedPoints[Current].POSX*10000;
          //  System.out.println(FlatDepths[i]);
            DepthX = Math.max(DepthX, FlatDepths[i]);
            Current = LinkArray[Current]; 
               
       
        }

    }
    //Polygon Points go around clockwise from the PoV of the camera
    //Back Face Culling should cull counterclockwise polygons
    private void rasterize(Observer3DS Viewer){ //rasterizes the polygon via the scanline algorithm
      
        
        int Y1 = Integer.MAX_VALUE; //Top Y coordinate of flat polygon
        int Y2 = Integer.MIN_VALUE; //Bottom Y coordinate of flat polygon
        int MinPoint = 0; //index of top vertex of flat polygon; point to begin scanline from
        
        double LM = 0; //fraction M in line defined by two points on left in x = my+c
        double LC = 0; //C value of line defined by two points on left in x = my + c
        double RM = 0; //fraction M in line defined by two points on right in x = my+c
        double RC = 0; //C value of line defined by two points on right in x = my + c

        
        int X1;
        int X2;
        
        
        //find MinPoint to start scanline and Max to terminate scanline
        
        for(int i = 0; i < ClippedCount; i++){
            Y2 = ((int)ExactFlatPoints[i].y > Y2 ? (int)ExactFlatPoints[i].y : Y2);
            MinPoint = ((int)ExactFlatPoints[i].y < (int)ExactFlatPoints[MinPoint].y ? i : MinPoint);
            
        }
        
        
        
        int LP = MinPoint; //current point on left side of polygon
        int LN = MinPoint; //next point on left side of polygon

        int RP = MinPoint; //current point on right side of polygon
        int RN = MinPoint; //next point on right side of polygon
        
        
        int PixelCoord; //single integer coordinate of pixel calculated from x and y values
        
        

        Y1 = (int)ExactFlatPoints[MinPoint].y;
        
        for(int y = Y1; y < Y2; y++){ //going line by line from y coordinate of MinPoint down to Max
            
            while(y == (int)ExactFlatPoints[LN].y){ //if reached next point then both this point and next point are shifted one down; while is in case of multiple points on same scanline
                LP = LN;
                LN = (LN + 1 + ClippedCount) % ClippedCount;           
            }
            
			LM = (ExactFlatPoints[LN].x - ExactFlatPoints[LP].x) * 1.0 / (ExactFlatPoints[LN].y - ExactFlatPoints[LP].y);

            LC = ExactFlatPoints[LP].x - LM * ExactFlatPoints[LP].y; 

            
            while(y == (int)ExactFlatPoints[RN].y){//if reached next point then both this point and next point are shifted one down; while is in case of multiple points on same scanline
                RP = RN;
                RN = (RN - 1 + ClippedCount) % ClippedCount;
            }
            
  			RM = (ExactFlatPoints[RN].x - ExactFlatPoints[RP].x) * 1.0 / (ExactFlatPoints[RN].y - ExactFlatPoints[RP].y);
            RC = ExactFlatPoints[RP].x - RM * ExactFlatPoints[RP].y; 

			//X2 always greater than X1 due to back face culling criteria
			X1 = (int)GUI.cap(ExactFlatPoints[LN].x, (y * LM + LC), ExactFlatPoints[LP].x);
            X2 = (int)GUI.cap(ExactFlatPoints[RN].x, (y * RM + RC), ExactFlatPoints[RP].x);
           
  	//		if(Print)System.out.println(y + "\t" + X1 + "\t" + X2 + "\t" + Viewer.Filled);

            scanLine(Viewer, y, X1, X2);
     		
     		
        }
        
    }
    
 	public void scanLine(Observer3DS Viewer, int y, int X1, int X2){
 		float Depth = (float)(A*(X1) + B*y + C); //starting depth for scanline
 		int PixelCoord = y * Viewer.Width + X1; //starting pixelcoordinate for scanline
 		
        for(int x = X1; x < X2; x++, Depth += A, PixelCoord++){
        	int D = (int)(Depth > DepthX ? DepthX : Depth);
           	Viewer.Scanned++;
           	if(D > Viewer.ZBuffer[PixelCoord]){
           		Viewer.Filled++;
           		Viewer.PointerBuffer[PixelCoord] = this;
           		Viewer.TargetBuffer[PixelCoord] = Texture.getColor(Viewer, x, y, Viewer.NearClip * Integer.MAX_VALUE / Depth); //inverse of Depth calculation from POSX coordinate
           		Viewer.ZBuffer[PixelCoord] = D;
           	}
       	}	
 	}
 	//Polygon Calculating Normal Vectors
    private static final Point3DS DirectionA = new Point3DS();
    private static final Point3DS DirectionB = new Point3DS();
    public void update(){ //updates the polygons Normal and Centroid
        ActualPoints[0].subtract(ActualPoints[1], DirectionA);
        ActualPoints[1].subtract(ActualPoints[2], DirectionB);
        DirectionA.crossProduct(DirectionB, Normal);
       // System.out.println(ActualPoints[0] + "\t" + ActualPoints[1]);
        Normal.normalize();
        
        Centroid.set(0, 0, 0);
        for(int i = 0; i < PointCount; i++){
        	Centroid.add(ActualPoints[i], Centroid);	
        }
        Centroid.divide(PointCount, Centroid);
        
    }
    
    private void updateEquation(){ //calculates the equation of plane for the polygon from first three points
        final double Z12 = (FlatDepths[0] - FlatDepths[1]);
        final double Y12 = (ExactFlatPoints[0].y - ExactFlatPoints[1].y);
        final double X12 = (ExactFlatPoints[0].x - ExactFlatPoints[1].x);
        final double Z32 = (FlatDepths[2] - FlatDepths[1]);
        final double Y32 = (ExactFlatPoints[2].y - ExactFlatPoints[1].y);
        final double X32 = (ExactFlatPoints[2].x - ExactFlatPoints[1].x);
        
        B = ((Z32 * X12 - Z12 * X32) / (Y32 * X12 - Y12 * X32));
        A = ((Z32 * Y12 - Z12 * Y32) / (X32 * Y12 - X12 * Y32));
        C = (FlatDepths[1] - A * ExactFlatPoints[1].x - B * ExactFlatPoints[1].y);
        
        
        
    }
    public void prepareTexture(Observer3DS Viewer){
    	Texture.prepareTexture(Viewer, this, ShadeFactor);
    }
    public double[] ShadeFactor = new double[3];
    public void updateTint(Observer3DS Viewer){ //calculates the tint of the polygon
       // if(!printable()){
       //    return;
        //}
        
        
        ShadeFactor[0] = 0;
        ShadeFactor[1] = 0;
        ShadeFactor[2] = 0;
        //Directional Lighting
        for(int i = 1; i < Viewer.LightVector.length; i++){ 
        	for(int j = 0; j < 3; j++){
        		ShadeFactor[j] += Viewer.LightIntensity[i][j] * Math.max(Normal.dotProduct(Viewer.LightVector[i]), 0);
        	}
            
        }
        //Ambient Lighting
        for(int j = 0; j < 3; j++){
    		ShadeFactor[j] += Viewer.LightIntensity[0][j];
    	}
    }
	
	public void center(Observer3DS Viewer){ //centers the polygon's points, storing the eye-space coordinates into TransformedPoints
		for(int i = 0; i < PointCount; i++){
    		ActualPoints[i].center(Viewer, TransformedPoints[i]);	
    	}	
    }
    public boolean behindObserver(Observer3DS Viewer){ //checks to see if all points are behind the eye; used for culling
    	for(int i = 0; i < PointCount; i++){
    		if(TransformedPoints[i].POSX > 0){
    			return false;
    		}
    	}
    	return true;
    }
   	public void submit(Observer3DS Viewer){ //submits the polygon to the viewer: updated & centered
   		update();
   		
   		center(Viewer);
   		
    	if(behindObserver(Viewer)){
            return;
        }
  
       // preClip(Viewer); 
        //System.out.println("!#");
        ClipTest = null;
        BackClipTest = null;
    	Viewer.accept(this);	
        
    }   
 	public void complete(Observer3DS Viewer){ //completes printing the polygon; separate from submit to allow for reshuffling before printing
		//Clip Polygons
        toClip();
        
        if(BackClipTest == null || BackClipTest[0]){
            clip(Viewer, Viewer.BackFrustum, Viewer.NearClip);
        }
        
        for(int j = 0; j < Viewer.Frustum.length; j++){
            if(ClipTest == null || ClipTest[j]){
                clip(Viewer, Viewer.Frustum[j], 0);
            }
        }
        
        //Color
        updateTint(Viewer);
		prepareTexture(Viewer);
        //Finish
        pointify(Viewer);
        updateEquation();
        
        rasterize(Viewer);
      
	}
	public void print(Observer3DS Viewer){ //prints the polygon now
		submit(Viewer);
		complete(Viewer);
    }
    public String toString(){
    	String Value = "";
    	Value += super.toString() + "\t"  + PointCount + "\t" + Arrays.toString(ActualPoints);
    	return Value;
    }
}
class Point3DS{
    double POSX;
    double POSY;
    double POSZ;

    //Shape3DS Base;

    public final Point3DS copy(){
    	Point3DS Point = new Point3DS(POSX, POSY, POSZ);
        return Point;
    }
    
    
    public final void copyTo(Point3DS Target){
        Target.POSX = POSX;
        Target.POSY = POSY;
        Target.POSZ = POSZ;
    }
    public final void copyFrom(Point3DS Target){
        POSX = Target.POSX;
        POSY = Target.POSY;
        POSZ = Target.POSZ;
    }
    public Point3DS(){
        
    }
    
    public Point3DS(double tPOSX, double tPOSY, double tPOSZ){
        POSX = tPOSX;
        POSY = tPOSY;
        POSZ = tPOSZ;
    }
   
    public final void set(double x, double y, double z){
    	POSX = x;
    	POSY = y;
    	POSZ = z;	
    }
    public final void normalize(){
        double L = Math.sqrt(POSX * POSX + POSY * POSY + POSZ * POSZ);
        POSX = POSX / L;
        POSY = POSY / L;
        POSZ = POSZ / L;
    }
    public final void normalize(double D){
    	double L = Math.sqrt(POSX * POSX + POSY * POSY + POSZ * POSZ) / D;
        POSX = POSX / L;
        POSY = POSY / L;
        POSZ = POSZ / L;
    }

    public final Point3DS add(Point3DS Target){
        return new Point3DS(POSX + Target.POSX, POSY + Target.POSY, POSZ + Target.POSZ);
    }
    public final Point3DS add(Point3DS Target, Point3DS Result){
        Result.POSX = POSX + Target.POSX;
        Result.POSY = POSY + Target.POSY;
        Result.POSZ = POSZ + Target.POSZ;
        return Result;
    }
    public final Point3DS subtract(Point3DS Target){
        return new Point3DS(POSX - Target.POSX, POSY - Target.POSY, POSZ - Target.POSZ);
    }
    public final Point3DS subtract(Point3DS Target, Point3DS Result){
        Result.POSX = POSX - Target.POSX;
        Result.POSY = POSY - Target.POSY;
        Result.POSZ = POSZ - Target.POSZ;
        return Result;
    }
    public final Point3DS multiply(double Multiple, Point3DS Target){
        Target.setLocation(POSX * Multiple, POSY * Multiple, POSZ * Multiple);
        return Target;
    }
    public final Point3DS divide(double Multiple, Point3DS Target){
        Target.setLocation(POSX / Multiple, POSY / Multiple, POSZ / Multiple);
        return Target;
    }
    public final Point3DS multiply(double Multiple){
        return new Point3DS(POSX * Multiple, POSY * Multiple, POSZ * Multiple);
    }
    public final Point3DS divide(double Multiple){
        return new Point3DS(POSX / Multiple, POSY / Multiple, POSZ / Multiple);
    }
    public final double dotProduct(Point3DS Other){
        return POSX * Other.POSX + POSY * Other.POSY + POSZ * Other.POSZ;
    }
    public final double dot(Point3DS Other){
        return POSX * Other.POSX + POSY * Other.POSY + POSZ * Other.POSZ;
    }
    public final Point3DS crossProduct(Point3DS Other){
        return new Point3DS(POSY * Other.POSZ - POSZ * Other.POSY, POSZ * Other.POSX - POSX * Other.POSZ, POSX * Other.POSY - POSY * Other.POSX);
    }
    public final Point3DS cross(Point3DS Other){
        return new Point3DS(POSY * Other.POSZ - POSZ * Other.POSY, POSZ * Other.POSX - POSX * Other.POSZ, POSX * Other.POSY - POSY * Other.POSX);
    }
    public final void crossProduct(Point3DS Other, Point3DS Target){
        Target.POSX = POSY * Other.POSZ - POSZ * Other.POSY;
        Target.POSY = POSZ * Other.POSX - POSX * Other.POSZ;
        Target.POSZ = POSX * Other.POSY - POSY * Other.POSX;
    }
    public final void cross(Point3DS Other, Point3DS Target){
        Target.POSX = POSY * Other.POSZ - POSZ * Other.POSY;
        Target.POSY = POSZ * Other.POSX - POSX * Other.POSZ;
        Target.POSZ = POSX * Other.POSY - POSY * Other.POSX;
    }
    public final double distanceTo(Point3DS Target){
        return Math.sqrt((Target.POSX - POSX) * (Target.POSX - POSX) +(Target.POSY - POSY) * (Target.POSY - POSY) + (Target.POSZ - POSZ) * (Target.POSZ - POSZ));
    }
    public final double distanceToSquare(Point3DS Target){
        return (Target.POSX - POSX) * (Target.POSX - POSX) +(Target.POSY - POSY) * (Target.POSY - POSY) + (Target.POSZ - POSZ) * (Target.POSZ - POSZ);
    }
    public final double flatDistanceTo(Point3DS Target){
        return Math.sqrt((Target.POSX - POSX) * (Target.POSX - POSX) +(Target.POSY - POSY) * (Target.POSY - POSY));
    }
    public final double length(){
        return Math.sqrt(POSX * POSX + POSY * POSY + POSZ * POSZ);
    }
	public double getLattitude(){
		return Math.atan(POSZ / Math.hypot(POSX, POSY));
	}
	public double getLongitude(){
		return Math.atan2(POSY, POSX);
	}
    public final void setToMidpoint(Point3DS A, Point3DS B){
        POSX = (A.POSX + B.POSX) / 2;
        POSY = (A.POSY + B.POSY) / 2;
        POSZ = (A.POSZ + B.POSZ) / 2;
    }

    public final void setLocation(double X, double Y, double Z){
        POSX = X;
        POSY = Y;
        POSZ = Z;
    }


    public final void rotate(Rotation3DS R, Point3DS Point){

        R.transform(this, Point);

    }
    public final void rotate(Point3DS Center, Rotation3DS R, Point3DS Point){

        Point.POSX = this.POSX - Center.POSX;
        Point.POSY = this.POSY - Center.POSY;
        Point.POSZ = this.POSZ - Center.POSZ;
        R.transform(Point, Point);
        Point.POSX = Point.POSX + Center.POSX;
        Point.POSY = Point.POSY + Center.POSY;
        Point.POSZ = Point.POSZ + Center.POSZ;

    }

    public final void center(Observer3DS Viewer, Point3DS Point){
        Point.POSX = this.POSX - Viewer.Position.POSX;
        Point.POSY = this.POSY - Viewer.Position.POSY;
        Point.POSZ = this.POSZ - Viewer.Position.POSZ;
        Viewer.Rotation.inverseTransform(Point, Point);

  //      if(Point.POSX < 0.01 && Point.POSX > -0.01){
  //          Point.POSX = 0.001;
   //     }
    }

    private static double Temp;
    public final void toScreen(Observer3DS Viewer, Point Target){
        Temp = Viewer.HalfY * Viewer.Magnification / POSX;
        Target.x = (int)(POSY * Temp + Viewer.CenterX);
        Target.y = (int)(-POSZ * Temp + Viewer.CenterY);
    }
    public final void fromScreen(Observer3DS Viewer, Point Source, double Depth){
    	POSX = Depth;
    	Temp = Viewer.HalfY * Viewer.Magnification / POSX;
    	POSY = (Source.x - Viewer.CenterX) / Temp;
    	POSZ = -(Source.y - Viewer.CenterY) / Temp;
    }
    public final void toScreen(Observer3DS Viewer, Point2D.Float Target){
        Temp = Viewer.HalfY * Viewer.Magnification / POSX;
        Target.x = (float)(POSY * Temp + Viewer.CenterX);
        Target.y = (float)(-POSZ * Temp + Viewer.CenterY);
    }
    public final void fromScreen(Observer3DS Viewer, Point.Float Source, double Depth){
    	POSX = Depth;
    	Temp = Viewer.HalfY * Viewer.Magnification / POSX;
    	POSY = (Source.x - Viewer.CenterX) / Temp;
    	POSZ = -(Source.y - Viewer.CenterY) / Temp;
    }
    public final void toScreen(Observer3DS Viewer, Point2D.Double Target){
        Temp = Viewer.HalfY * Viewer.Magnification / POSX;
        Target.x = (POSY * Temp + Viewer.CenterX);
        Target.y = (-POSZ * Temp + Viewer.CenterY);
    }
    public final void fromScreen(Observer3DS Viewer, Point2D.Double Source, double Depth){
    	POSX = Depth;
    	Temp = Viewer.HalfY * Viewer.Magnification / POSX;
    	POSY = (Source.x - Viewer.CenterX) / Temp;
    	POSZ = -(Source.y - Viewer.CenterY) / Temp;
    }
	
	public boolean equals(Object O){
		Point3DS P = (Point3DS)O;
		
		return P.POSX == POSX && P.POSY == POSY && P.POSZ == POSZ;
	}
    public String toString(){
        return "Point: " + "\t" + POSX + "\t" + POSY + "\t" + POSZ;

    }

}
class Image3DS{
	BufferedImage Picture;
	int Height;
	int Width;
	public Image3DS(BufferedImage Target){
		Picture = new BufferedImage(Target.getWidth(), Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Height = Picture.getHeight();
    	Width = Picture.getWidth();
    	
    	Graphics2D Painter = Picture.createGraphics();
		Painter.setComposite(AlphaComposite.Clear);
		Painter.fillRect(0, 0, Width, Height);
		
		Painter.setComposite(AlphaComposite.SrcOver);
		Painter.drawImage(Target, 0, 0, null);
		
    	
    	
	}
	public Image3DS(String TargetAddress){
		try{
				
			BufferedImage Target = ImageIO.read(new File(TargetAddress));
			Picture = new BufferedImage(Target.getWidth(), Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Height = Picture.getHeight();
    		Width = Picture.getWidth();
			
			Graphics2D Painter = Picture.createGraphics();
			Painter.setComposite(AlphaComposite.Clear);
			Painter.fillRect(0, 0, Width, Height);
			
			Painter.setComposite(AlphaComposite.SrcOver);
			Painter.drawImage(Target, 0, 0, null);
			
    	
    		
		}catch(IOException e){
			throw new IllegalArgumentException("Image Reading Error: " + e);	
		}
		
		
	}
	public void compound(Image3DS Target){
		compound(Target.Picture);
	}
	public void compound(String Target){
		try{
			
			//Painter.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.7f));
			BufferedImage TargetI = ImageIO.read(new File(Target));
			
			compound(TargetI);
		}catch(Exception e){
			throw new IllegalArgumentException("Image Reading Error: " + e);	
		}
	}
	public void compound(BufferedImage Target){
		Graphics2D Painter = Picture.createGraphics();
		Painter.setComposite(AlphaComposite.SrcOver);
		Painter.drawImage(Target, 0, 0, Width, Height, null, null);
	}
	
}
abstract class Texture3DS{
	boolean Shaded = true;
	
	public abstract int getColor(Observer3DS Viewer, int x, int y, double Depth);
	
	public abstract void prepareTexture(Observer3DS Viewer, Polygon3DS Target, double[] ShadeFactor);
}
class DepthTexture3DS extends Texture3DS{
	int WhiteRGB = Color.white.getRGB();
	double Multiple = 1;
	
	public DepthTexture3DS(double Fudge){
		Multiple = Fudge;
	}
	public void prepareTexture(Observer3DS Viewer, Polygon3DS Target, double[] ShadeFactor){
		
	}		
	public int getColor(Observer3DS Viewer, int x, int y, double Depth){
		return Utils3DS.scaleColor(WhiteRGB, Multiple/Depth);
	}
}
class FlatTexture3DS extends Texture3DS{
	Color Tint;
	int TintRGB;
	
	int ShadedTintRGB;
	public FlatTexture3DS(Color C){
		Tint = C;
		TintRGB = C.getRGB();	
	}
	final public int getColor(Observer3DS Viewer, int x, int y, double Depth){
		if(Shaded){
			return ShadedTintRGB;
		}else{
			return TintRGB;
		}
	}
	public void prepareTexture(Observer3DS Viewer, Polygon3DS Target, double[] ShadeFactor){
		ShadedTintRGB = Utils3DS.scaleColor(TintRGB, ShadeFactor);
	}
	
}
class ImageTexture3DS extends Texture3DS{
	Image3DS Image;
	int[] Data;
	//Pegs the corners of the 3D Polygon to 3 points on image; other points are interpolated
	Point PegA;
	Point PegB;
	Point PegC;
	Point3DS P3A;
	Point3DS P3B;
	Point3DS P3C;
	double[] ColorScale;
	
	public ImageTexture3DS(Image3DS Source){
		Image = Source;
		init();	
	}
	
	public ImageTexture3DS(BufferedImage Target){
		Image = new Image3DS(Target);
		
		init();
	}
	public ImageTexture3DS(String TargetAddress){
		//Image I = Toolkit.getDefaultToolkit().createImage(TargetAddress);
		Image = new Image3DS(TargetAddress);		
		init();	
	}
	private void init(){

    	Data = ((DataBufferInt)Image.Picture.getRaster().getDataBuffer()).getBankData()[0];	
    	
	}
	private static Matrix3DS TextureMapper = new Matrix3DS(); //3x3 [[a, b, c][e, f, g][0, 0, 1]] matrix for mapping from Y, Z eye-space to x, y texture space
	private static Point3DS TempABC = new Point3DS();
	private static Point3DS TempEFG = new Point3DS();
	
	char SwapPlane;
	public void prepareTexture(Observer3DS Viewer, Polygon3DS Target, double[] ShadeFactor){
		
		P3A = Target.TransformedPoints[0]; 
		P3B = Target.TransformedPoints[1];
		P3C = Target.TransformedPoints[Target.TransformedPoints.length-1];	
		
	
		
		ColorScale = ShadeFactor;

		//prepare the image-to-eyespace transform
		double[][] M = TextureMapper.Matrix;
		
		Point3DS TransformedNormal = new Point3DS();
		Viewer.Rotation.inverseTransform(Target.Normal, TransformedNormal);
		
		//plane
		double Xd = Math.abs(TransformedNormal.dot(Utils3DS.Vectori));
		double Yd = Math.abs(TransformedNormal.dot(Utils3DS.Vectorj));
		double Zd = Math.abs(TransformedNormal.dot(Utils3DS.Vectork));
		if(Xd > Yd && Xd > Zd){ //if x is biggest
			SwapPlane = 'x';
			M[0][0] = P3A.POSY; M[0][1] = P3A.POSZ; M[0][2] = 1;
			M[1][0] = P3B.POSY; M[1][1] = P3B.POSZ; M[1][2] = 1;
			M[2][0] = P3C.POSY; M[2][1] = P3C.POSZ; M[2][2] = 1;
		}else if(Yd > Zd){ //if y is bigger (and x not biggest so y is biggest)
			SwapPlane = 'y';
			M[0][0] = P3A.POSZ; M[0][1] = P3A.POSX; M[0][2] = 1;
			M[1][0] = P3B.POSZ; M[1][1] = P3B.POSX; M[1][2] = 1;
			M[2][0] = P3C.POSZ; M[2][1] = P3C.POSX; M[2][2] = 1;	
		}else{ //else if z is biggest
			SwapPlane = 'z';
			M[0][0] = P3A.POSX; M[0][1] = P3A.POSY; M[0][2] = 1;
			M[1][0] = P3B.POSX; M[1][1] = P3B.POSY; M[1][2] = 1;
			M[2][0] = P3C.POSX; M[2][1] = P3C.POSY; M[2][2] = 1;	
		}

		TextureMapper.inverse();
		
		TempABC.set(PegA.x, PegB.x, PegC.x);
		TempEFG.set(PegA.y, PegB.y, PegC.y);
		TextureMapper.transform(TempABC, TempABC);
		TextureMapper.transform(TempEFG, TempEFG);
		M[0][0] = TempABC.POSX; M[0][1] = TempABC.POSY; M[0][2] = TempABC.POSZ;
		M[1][0] = TempEFG.POSX; M[1][1] = TempEFG.POSY; M[1][2] = TempEFG.POSZ;
		M[2][0] = 0;			M[2][1] = 0;			M[2][2] = 1;
		
		
		
		
	}
	
	
	//whole method inlined for speed
	//original method comprised converting x, y and z into POSX POSY POSZ eye-space, 
	//choosing 2 out of the 3 to serve as coordinates and transforming them to texture
	//space with the matrix prepared in prepareTexture. returns value of pixel in texture 
	//space as color
	final public int getColor(Observer3DS Viewer, int x, int y, double Depth){ 
		
		double POSY=0;
		double POSZ=0;	
	
		switch(SwapPlane){
			case 'x':
			POSY = (x - Viewer.CenterX) / (Viewer.HalfY * Viewer.Magnification / Depth);
			POSZ = -(y - Viewer.CenterY) / (Viewer.HalfY * Viewer.Magnification / Depth);
			break;
			case 'y':
			POSY = -(y - Viewer.CenterY) / (Viewer.HalfY * Viewer.Magnification / Depth);
			POSZ = Depth;
			break;
			case 'z':
			POSY = Depth;
			POSZ = (x - Viewer.CenterX) / (Viewer.HalfY * Viewer.Magnification / Depth);
			break;
		}
		double[][] Matrix = TextureMapper.Matrix;
		x = (int)(Matrix[0][0] * POSY + Matrix[0][1] * POSZ + Matrix[0][2]); 
        y = (int)(Matrix[1][0] * POSY + Matrix[1][1] * POSZ + Matrix[1][2]);

		
		if(Shaded){
			double[] R = ColorScale;
			int RGB = Data[(x%Image.Width + Image.Width) % Image.Width  + Image.Width * ((y%Image.Height + Image.Height) % Image.Height)];
			return	((RGB & 0xff0000) * R[2] > 0xff0000 ? 0xff0000 : ((int)((RGB & 0xff0000) * R[2]) & 0xff0000)) +
    				((RGB & 0xff) * R[0] > 0xff ? 0xff : ((int)((RGB & 0xff) * R[0]) & 0xff)) + 
    				((RGB & 0xff00) * R[1] > 0xff00 ? 0xff00 : ((int)((RGB & 0xff00) * R[1]) & 0xff00));
		
		}else{
			
			return Data[(x%Image.Width + Image.Width) % Image.Width  + Image.Width * ((y%Image.Height + Image.Height) % Image.Height)];
		}

		
	
		
		
		
	}
	public void peg(){
		int H = Image.Height;
		int W = Image.Width;
		PegA = new Point(GUI.randomInt(0, W), GUI.randomInt(0, H));
		PegB = new Point(GUI.randomInt(0, W), GUI.randomInt(0, H));
		PegC = new Point(GUI.randomInt(0, W), GUI.randomInt(0, H));
	}
	public void peg(Point A, Point B, Point C){
		PegA = A;
		PegB = B;
		PegC = C;	
	}

	
	
}
class Matrix3DS{
	double[][] Matrix = new double[3][3];	
	protected static double[][] TempMatrix = new double[3][3];
    protected static Point3DS TempPoint = Utils3DS.Origin.copy();
    
    public Matrix3DS(){
    	Matrix[0][0] = 1;	
    	Matrix[1][1] = 1;
    	Matrix[2][2] = 1;
    }
    public void print(){
        System.out.println();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                System.out.print(Matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
    public final void identify(){
        Matrix[0][0] = 1; Matrix[0][1] = 0; Matrix[0][2] = 0;
        Matrix[1][0] = 0; Matrix[1][1] = 1; Matrix[1][2] = 0;
        Matrix[2][0] = 0; Matrix[2][1] = 0; Matrix[2][2] = 1;
     
    }
    public final void createFromPoints(Point3DS A, Point3DS B, Point3DS C){
        Matrix[0][0] = A.POSX; Matrix[0][1] = A.POSY; Matrix[0][2] = A.POSZ;
        Matrix[1][0] = B.POSX; Matrix[1][1] = B.POSY; Matrix[1][2] = B.POSZ;
        Matrix[2][0] = C.POSX; Matrix[2][1] = C.POSY; Matrix[2][2] = C.POSZ;
  
    }
    
    public void inverse(){
    	double[][] A = Matrix;
    	double[][] T = TempMatrix;
    	
    	if(A.length != 3 || A[0].length != 3 || A[1].length != 3 || A[2].length != 3){
    		throw new IllegalArgumentException("Matrix not 3x3");	
    	}
    	
    	double det = 0;
    	for(int i = 0; i < 3; i++){
    		det += A[0][i] * A[1][(i+1)%3] * A[2][(i+2)%3];
    	}
    	for(int i = 0; i < 3; i++){
    		det -= A[0][i] * A[1][(i-1+3)%3] * A[2][(i-2+3)%3];
    	}
    	for(int i = 0; i < 3; i++){
    		for(int j = 0; j < 3; j++){
    			T[i][j] = A[(i-1+3)%3][(j-1+3)%3] * A[(i+1+3)%3][(j+1+3)%3] - A[(i-1+3)%3][(j+1+3)%3] * A[(i+1+3)%3][(j-1+3)%3];
    		}	
    	}
    	for(int i = 0; i < 3; i++){
    		for(int j = 0; j < 3; j++){
    			A[j][i] = T[i][j] / det;
    		}	
    	}
    
    }
    public Matrix3DS copy(){
        Matrix3DS Target = new Rotation3DS();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Target.Matrix[i][j] = Matrix[i][j];
            }
        }
        return Target;
    }
    public final void transform(Point3DS Start, Point3DS End){
        TempPoint.POSX = Matrix[0][0] * Start.POSX + Matrix[0][1] * Start.POSY + Matrix[0][2] * Start.POSZ; 
        TempPoint.POSY = Matrix[1][0] * Start.POSX + Matrix[1][1] * Start.POSY + Matrix[1][2] * Start.POSZ;
        TempPoint.POSZ = Matrix[2][0] * Start.POSX + Matrix[2][1] * Start.POSY + Matrix[2][2] * Start.POSZ;
        End.POSX = TempPoint.POSX;
        End.POSY = TempPoint.POSY;
        End.POSZ = TempPoint.POSZ;
    }  
    public static void multiply(Matrix3DS a, Matrix3DS b, Matrix3DS End){
        double[][] A = a.Matrix;
        double[][] B = b.Matrix;
        a.TempMatrix[0][0] = A[0][0] * B[0][0] + A[0][1] * B[1][0] + A[0][2] * B[2][0];
        a.TempMatrix[1][0] = A[1][0] * B[0][0] + A[1][1] * B[1][0] + A[1][2] * B[2][0];
        a.TempMatrix[2][0] = A[2][0] * B[0][0] + A[2][1] * B[1][0] + A[2][2] * B[2][0];
        
        a.TempMatrix[0][1] = A[0][0] * B[0][1] + A[0][1] * B[1][1] + A[0][2] * B[2][1];
        a.TempMatrix[1][1] = A[1][0] * B[0][1] + A[1][1] * B[1][1] + A[1][2] * B[2][1];
        a.TempMatrix[2][1] = A[2][0] * B[0][1] + A[2][1] * B[1][1] + A[2][2] * B[2][1];
        
        a.TempMatrix[0][2] = A[0][0] * B[0][2] + A[0][1] * B[1][2] + A[0][2] * B[2][2];
        a.TempMatrix[1][2] = A[1][0] * B[0][2] + A[1][1] * B[1][2] + A[1][2] * B[2][2];
        a.TempMatrix[2][2] = A[2][0] * B[0][2] + A[2][1] * B[1][2] + A[2][2] * B[2][2];
        
        End.Matrix[0][0] = a.TempMatrix[0][0]; End.Matrix[0][1] = a.TempMatrix[0][1]; End.Matrix[0][2] = a.TempMatrix[0][2];
        
        End.Matrix[1][0] = a.TempMatrix[1][0]; End.Matrix[1][1] = a.TempMatrix[1][1]; End.Matrix[1][2] = a.TempMatrix[1][2];
        
        End.Matrix[2][0] = a.TempMatrix[2][0]; End.Matrix[2][1] = a.TempMatrix[2][1]; End.Matrix[2][2] = a.TempMatrix[2][2];
      
    }
}
class Rotation3DS extends Matrix3DS{
	
	//these are provided by superclass
	
    //double[][] Matrix = new double[3][3];	
	//protected static double[][] TempMatrix = new double[3][3];
    //protected static Point3DS TempPoint = Utils3DS.Origin.copy();
    
    
   
    //public Rotation3DS(String Temp){
        
   // }
    public Rotation3DS(){
        Matrix[0][0] = 1;
        Matrix[1][1] = 1;
        Matrix[2][2] = 1;
     
        
    }
    
    
    public void inverse(){ //simple transpose operation; roughly 4 times faster than full 3x3 matrix inversion
        
    
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                TempMatrix[i][j] = Matrix[j][i];
            }
        }
                        
                    
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Matrix[i][j] = TempMatrix[i][j];
            }
        } 
    }
    public void inverse(Rotation3DS Target){ //simple transpose operation; roughly 4 times faster than full 3x3 matrix inversion
        
    
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                TempMatrix[i][j] = Matrix[j][i];
            }
        }
                        
                    
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Target.Matrix[i][j] = TempMatrix[i][j];
            }
        } 
    }
  
    public final void getOriginalAxis(char Axis, Point3DS Target){
        switch(Axis){
            case 'x':
                Target.POSX = Matrix[0][0]; 
                Target.POSY = Matrix[1][0];
                Target.POSZ = Matrix[2][0];
                break;
            case 'y':
                                            Target.POSX = Matrix[0][1]; 
                                            Target.POSY = Matrix[1][1]; 
                                            Target.POSZ = Matrix[2][1];
                break;
            case 'z':
                                                                        Target.POSX = Matrix[0][2]; 
                                                                        Target.POSY = Matrix[1][2]; 
                                                                        Target.POSZ = Matrix[2][2];
                break;
        }
    }
    public final void rotateAround(char Switch, double Angle){
        switch(Switch){
            case 'z':
                Matrix[0][0] = Math.cos(Angle); Matrix[0][1] = -Math.sin(Angle);    Matrix[0][2] = 0;
                Matrix[1][0] = -Matrix[0][1];   Matrix[1][1] = Matrix[0][0];        Matrix[1][2] = 0;
                Matrix[2][0] = 0;               Matrix[2][1] = 0;                   Matrix[2][2] = 1;
                break;
            case 'y':
                Matrix[0][0] = Math.cos(Angle); Matrix[0][1] = 0;                   Matrix[0][2] = -Math.sin(Angle);
                Matrix[1][0] = 0;               Matrix[1][1] = 1;                   Matrix[1][2] = 0;
                Matrix[2][0] = -Matrix[0][2];   Matrix[2][1] = 0;                   Matrix[2][2] = Matrix[0][0];
                
                break;
            case 'x':
                Matrix[0][0] = 1;               Matrix[0][1] = 0;                   Matrix[0][2] = 0;
                Matrix[1][0] = 0;               Matrix[1][1] = Math.cos(Angle);     Matrix[1][2] = -Math.sin(Angle);
                Matrix[2][0] = 0;               Matrix[2][1] = -Matrix[1][2];       Matrix[2][2] = Matrix[1][1];
                 
                break;
        }
    }
    public final Rotation3DS copy(){
        Rotation3DS Target = new Rotation3DS();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Target.Matrix[i][j] = Matrix[i][j];
            }
        }
        return Target;
    }
    public final void copy(Rotation3DS Target){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Target.Matrix[i][j] = Matrix[i][j];
            }
        }
    }
    public final void inverseTransform(Point3DS Start, Point3DS End){
        TempPoint.POSX = Matrix[0][0] * Start.POSX + Matrix[1][0] * Start.POSY + Matrix[2][0] * Start.POSZ; 
        TempPoint.POSY = Matrix[0][1] * Start.POSX + Matrix[1][1] * Start.POSY + Matrix[2][1] * Start.POSZ;
        TempPoint.POSZ = Matrix[0][2] * Start.POSX + Matrix[1][2] * Start.POSY + Matrix[2][2] * Start.POSZ;
        End.POSX = TempPoint.POSX;
        End.POSY = TempPoint.POSY;
        End.POSZ = TempPoint.POSZ;
    }
    
    private static Point3DS M = new Point3DS(0, 0, 0);
    private static Point3DS T = new Point3DS(0, 0, 0);
    private static Point3DS Z = new Point3DS(0, 0, 0);
    private static Point3DS Y = new Point3DS(0, 0, 0);
    private static Rotation3DS R = new Rotation3DS();
    private static Rotation3DS S = new Rotation3DS();
    private static Rotation3DS Ri = new Rotation3DS();
    
    public static void getRotationBetween(Point3DS m, Point3DS t, double MaxRadians, Rotation3DS Target){
        m.copyTo(M);
        M.normalize();
        t.copyTo(T);
        T.normalize();
        M.crossProduct(T, Z);
        Z.normalize();
        Z.crossProduct(M, Y);
        Y.normalize();
            
        double Angle = T.dotProduct(M);
        if(Angle < -1) Angle = -1;
        if(Angle > 1) Angle = 1;
        Angle = Math.acos(Angle);
        if(Angle < -MaxRadians) Angle = -MaxRadians;
        if(Angle > MaxRadians) Angle = MaxRadians;
        
        
            
        
        R.createFromPoints(M, Y, Z);
        R.inverse(Ri); 
        S.rotateAround('z', Angle);
            
        Target.identify();
        Rotation3DS.multiply(R, Target, Target);
        Rotation3DS.multiply(S, Target, Target);
        Rotation3DS.multiply(Ri, Target, Target);
            
     	
    }
    
    
    
}

class Observer3DS{
    Point3DS Position;
    Rotation3DS Rotation;
    

    
    int MaxX;
    int MaxY;
    
    
    int Width; //Width of Screen
    int Height; //Height of Screen
    int PixelCount = -1; //Number of Pixels

    int HalfX; //Half Width of Screen
    int HalfY; //Half Height of Screen
    int CenterX; //Center of Screen
    int CenterY; //Center of Screen
    
    double Magnification = 1.00; //Magnification

    

    
    double ZGradient;
    double YGradient;
    double DGradient;
    
    Point3DS[] Frustum = new Point3DS[4];
    Point3DS BackFrustum;
    double NearClip = 0.1;
    
    
    
    Point3DS[] LightVector;
    
    float[][] LightIntensity; //first element is the ambient
    
    
    
    Point3DS LookVector = new Point3DS();
    
    Graphics2D Painter;
    

    int BufferPixelCount;
    int[] ZBuffer;
    private int[] BlankIntArray;
    private Object[] BlankPointerArray;
    Polygon3DS[] PointerBuffer;
    
    int[] TargetBuffer;
    
    int Scanned;
    int Filled;
    int BackFacing;
    private BufferedImage Picture;
    ArrayList<Polygon3DS> Printables = new ArrayList<Polygon3DS>();
  
  	public Polygon3DS getPixelOwner(int x, int y){
  		return PointerBuffer[x + y * Width];	
  	}  
    public void accept(Polygon3DS P){
    	Printables.add(P);	
    }
    Comparator<Polygon3DS> SorterA = new Comparator<Polygon3DS>(){
    	public int compare(Polygon3DS A, Polygon3DS B){
    		return Double.compare(A.TransformedPoints[0].POSX, B.TransformedPoints[0].POSX);
    	}
    	public boolean equals(Polygon3DS A, Polygon3DS B){
    		throw new IllegalArgumentException("unsupported operation");
    	}
    };

    public void printAll(){
    	Collections.sort(Printables, SorterA); //sort so closer objects are printed first;

		for(int i = 0; i < Printables.size(); i++){
    		Printables.get(i).complete(this);	
		}	
    
    }
    
    public void setImage(BufferedImage P){
    	Picture = P;
    	DataBufferInt DB = ((DataBufferInt)P.getRaster().getDataBuffer());
    	
    	setBuffer(DB.getBankData()[0], P.getWidth(), P.getHeight());
    }
    public void setBuffer(int[] TargetB, int W, int H){
        BufferPixelCount = W * H;
        TargetBuffer = TargetB;
        ZBuffer = new int[BufferPixelCount];
        BlankIntArray = new int[BufferPixelCount];
        BlankPointerArray = new Object[BufferPixelCount];
        PointerBuffer = new Polygon3DS[BufferPixelCount];

    }
    public void blankOut(){
        
		System.out.println(Filled + "\t" + Scanned + "\t" + Printables.size());        
		Printables.clear();
        
        Scanned = 0;
        Filled = 0;
        System.arraycopy(BlankIntArray, 0, ZBuffer, 0, BufferPixelCount);  
        System.arraycopy(BlankIntArray, 0, TargetBuffer, 0, BufferPixelCount);  
        System.arraycopy(BlankPointerArray, 0, PointerBuffer,0, BufferPixelCount);  
		

    }
    public void initLights(int Count){
        LightVector = new Point3DS[Count+1];
     
        LightIntensity = new float[Count+1][3];
        for(int i = 0; i < Count+1; i++){
            LightVector[i] = new Point3DS();
        }
    }
    public void setAmbient(double[] Intensities){
    	setLight(-1, Intensities, Utils3DS.Origin);	
    }
    public void setLight(int i, double[] Intensities, Point3DS Direction){
    	for(int j = 0; j < 3; j++){
    		LightIntensity[i+1][j] = (float)Intensities[j];
    	}
    	Direction.copyTo(LightVector[i+1]);
    	LightVector[i+1].normalize();
    }
    
    public void setArea(int eX, int eY, double M){

        
        Height = eY;
        Width = eX;

        PixelCount = Width * Height;
        CenterX = (eX) / 2;
        CenterY = (eY) / 2;
        HalfX = Width / 2;
        HalfY = Height / 2;
  
        Magnification = M;

        ZGradient = 1/M;
        YGradient = 1/M * Width / Height;
        DGradient = Math.sqrt(GUI.square(ZGradient) + GUI.square(YGradient));
        float N = 1 - 0.00001f;
        Frustum[0].setLocation(ZGradient, 0, N);
        Frustum[1].setLocation(ZGradient, 0, -N);
        Frustum[2].setLocation(YGradient, N, 0);
        Frustum[3].setLocation(YGradient, -N, 0);
        for(int i = 0; i < 4; i++){
            Frustum[i].normalize();
            //System.out.println(Frustum[i]);
        }
    }
    public Observer3DS(double tX, double tY, double tZ, Rotation3DS tR){
        Position = new Point3DS(tX, tY, tZ);

        Rotation = tR;
        for(int i = 0; i < 4; i++){
            Frustum[i] = new Point3DS();
        }
        BackFrustum = new Point3DS(1, 0, 0);
        //Rotation.inverse(InverseRotation);
    }
    
    public final void setLookVector(){
        Rotation.getOriginalAxis('x', LookVector);
    }
        
}