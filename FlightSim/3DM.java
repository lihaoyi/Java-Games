import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;



class Utils3DM{

    static final Point3DM Origin = new Point3DM(0, 0, 0);
    static final Point3DM Vectori = new Point3DM(1, 0, 0);
    static final Point3DM Vectorj = new Point3DM(0, 1, 0);
    static final Point3DM Vectork = new Point3DM(0, 0, 1);
    
    public static Point3DM getNormal(Point3DM A, Point3DM B, Point3DM C){
    	return C.subtract(A).cross(B.subtract(A));	
    }
    public static void getRandomUnit(Point3DM Target){
        Point3DM Position = Target;
        do{
            Position.setLocation(2 * Math.random() - 1, 2 * Math.random() - 1, 2 * Math.random() - 1);
        }while(Position.length() > 1 || Position.length() == 0);
        Position.divide(Position.length(), Position);


    }
    public static Point3DM getRandomUnit(){
        Point3DM Position = new Point3DM(0, 0, 0);
        do{
            Position.setLocation(2 * Math.random() - 1, 2 * Math.random() - 1, 2 * Math.random() - 1);
        }while(Position.length() > 1 || Position.length() == 0);
        Position.divide(Position.length(), Position);

        return Position;

    }
    static final Point3DM TempA = new Point3DM(0, 0, 0);
    static final Point3DM TempB = new Point3DM(0, 0, 0);
    static final Point3DM TempC = new Point3DM(0, 0, 0);
    static final Point3DM TempD = new Point3DM(0, 0, 0);
    public static Point3DM lineSphereCollision(Point3DM LineA, Point3DM LineB, Point3DM SphereCenter, double Radius){
        Point3DM Difference = TempA;
        Point3DM LineUnits = TempB;
        Point3DM Temp = TempC;
        Point3DM SurfaceImage = TempD;

		
        LineA.subtract(SphereCenter, Difference);

        LineA.subtract(LineB, LineUnits);
        if(LineUnits.length() == 0){
        	if(SphereCenter.distanceTo(LineA) < Radius){
        		return LineA.copy();
        	}else{
				return null;        		
    		}	
        }
        LineUnits.normalize();

        LineUnits.multiply(LineUnits.dotProduct(Difference), Difference);
        LineA.subtract(Difference, SurfaceImage);
        SurfaceImage.subtract(SphereCenter, Difference);

        if(Difference.length() > Radius){
            return null;
        }else{
            LineA.subtract(SurfaceImage, Difference);
            LineB.subtract(SurfaceImage, Temp);
            if(Difference.dotProduct(Temp) < 0){
                return SurfaceImage;
            }else{
                return null;
            }
        }
    }
    public static void setPoints(Shape3DM CurrentShape, char Switch, double ArgA, double ArgB, double ArgC){
        CurrentShape.ArgA = ArgA;
        CurrentShape.ArgB = ArgB;
        CurrentShape.ArgC = ArgC;
        CurrentShape.Form = Switch;
        switch(Switch){
            case 'g':{ //Grid 
                Vector<Point3DMShape> GPoints = new Vector<Point3DMShape>();
                double Max = Math.min(ArgA, ArgB);

                for(int i = -(int)(ArgA / 2); i <= ArgA / 2; i++){
                    Point3DMShape PointA = new Point3DMShape(CurrentShape, i * ArgC, -ArgB * ArgC * 3 / 2, 0);
                    Point3DMShape PointB = new Point3DMShape(CurrentShape, i * ArgC, ArgB * ArgC * 3 / 2, 0);
                    PointA.Links.add(PointB);
                    //PointB.Links.add(PointA);
                    GPoints.add(PointA);
                    GPoints.add(PointB);
                }

                for(int i = -(int)(ArgB / 2); i <= ArgB / 2; i++){
                    Point3DMShape PointA = new Point3DMShape(CurrentShape, -ArgA * ArgC * 3 / 2, i * ArgC, 0);
                    Point3DMShape PointB = new Point3DMShape(CurrentShape, ArgA * ArgC * 3 / 2, i * ArgC, 0);
                    PointA.Links.add(PointB);
                    //PointB.Links.add(PointA);
                    GPoints.add(PointA);
                    GPoints.add(PointB);            
                }
                for(int i = (int)(Max / 2); i <= Max * 3 / 2; i++){
                    int j = (int)(2 * Max - i);
                    Point3DMShape PointA = new Point3DMShape(CurrentShape, j * ArgC, i * ArgC, 0);
                    Point3DMShape PointB = new Point3DMShape(CurrentShape, -j * ArgC, i * ArgC, 0);
                    Point3DMShape PointC = new Point3DMShape(CurrentShape, -j * ArgC, -i * ArgC, 0);
                    Point3DMShape PointD = new Point3DMShape(CurrentShape, j * ArgC, -i * ArgC, 0);
                    PointA.Links.add(PointB);
                    PointB.Links.add(PointC);
                    PointC.Links.add(PointD);
                    PointD.Links.add(PointA);
                    GPoints.add(PointA);
                    GPoints.add(PointB);
                    GPoints.add(PointC);
                    GPoints.add(PointD);

                }
                CurrentShape.BasePoints.addAll(GPoints);
                }break;
            case 'o':{ //Octahedron Length/Width/Height 
                Vector<Point3DMShape> OPoints = new Vector<Point3DMShape>();
                Point3DMShape PointF = new Point3DMShape(CurrentShape, ArgA, 0, 0);
                Point3DMShape PointB = new Point3DMShape(CurrentShape, -ArgA, 0, 0);
                Point3DMShape PointL = new Point3DMShape(CurrentShape, 0, ArgB, 0);
                Point3DMShape PointR = new Point3DMShape(CurrentShape, 0, -ArgB, 0);
                Point3DMShape PointU = new Point3DMShape(CurrentShape, 0, 0, ArgC);
                Point3DMShape PointD = new Point3DMShape(CurrentShape, 0, 0, -ArgC);
                PointF.Links.add(PointL);
                PointF.Links.add(PointR);
                PointF.Links.add(PointU);
                PointF.Links.add(PointD);

                PointU.Links.add(PointL);
                PointU.Links.add(PointR);
                PointU.Links.add(PointB);

                PointD.Links.add(PointL);
                PointD.Links.add(PointR);
                PointD.Links.add(PointB);

                PointB.Links.add(PointL);
                PointB.Links.add(PointR);

                OPoints.add(PointF);
                OPoints.add(PointB);
                OPoints.add(PointL);
                OPoints.add(PointR);
                OPoints.add(PointU);
                OPoints.add(PointD);
                
                CurrentShape.Planes.add(new int[]{1, 2, 4});
                CurrentShape.Planes.add(new int[]{2, 0, 4});
                CurrentShape.Planes.add(new int[]{0, 3, 4});
                CurrentShape.Planes.add(new int[]{3, 1, 4});
                CurrentShape.Planes.add(new int[]{1, 2, 5});
                CurrentShape.Planes.add(new int[]{2, 0, 5});
                CurrentShape.Planes.add(new int[]{0, 3, 5});
                CurrentShape.Planes.add(new int[]{3, 1, 5});
                
                CurrentShape.BasePoints.addAll(OPoints);
                }break;
            case 'c':{ //Cuboid Length/Width/Height
                Vector<Point3DMShape> CPoints = new Vector<Point3DMShape>();
                for(double i = -0.5; i <= 0.5; i+=1){
                    for(double j = -0.5; j <= 0.5; j+=1){
                        for(double k = -0.5; k <= 0.5; k+=1){
                            CPoints.add(new Point3DMShape(CurrentShape, i * ArgA, j * ArgB, k * ArgC));
                        }
                    }
                }
                for(int i = 0; i < CPoints.size(); i++){
                    for(int j = i; j < CPoints.size(); j++){
                        int INTA = 0;
                        if(CPoints.get(i).POSX == CPoints.get(j).POSX)INTA++;
                        if(CPoints.get(i).POSY == CPoints.get(j).POSY)INTA++;
                        if(CPoints.get(i).POSZ == CPoints.get(j).POSZ)INTA++;        
                        if(INTA == 2){
                            try{
                                CPoints.get(i).Links.add(CPoints.get(j));
                                    //CPoints.point3DM(j).Links.add(CPoints.point3DM(i));
                           }catch(Exception e){}
                        }
                    }    
                }
                
                CurrentShape.Planes.add(new int[]{0, 1, 5, 4});
                CurrentShape.Planes.add(new int[]{2, 3, 7, 6});
                CurrentShape.Planes.add(new int[]{0, 2, 6, 4});
                CurrentShape.Planes.add(new int[]{1, 3, 7, 5});
                CurrentShape.Planes.add(new int[]{4, 6, 7, 5});
                CurrentShape.Planes.add(new int[]{0, 2, 3, 1});
                
                
                CurrentShape.BasePoints.addAll(CPoints);

                }break;

            case 't':{ //Triangular Prism Length/Width/Height
                Vector<Point3DMShape> TPoints = new Vector<Point3DMShape>();
                for(int i = -1; i <= 1; i++, i++){
                    Point3DMShape PointA = new Point3DMShape(CurrentShape, ArgA / 2 * i, -ArgB / 2, 0);
                    Point3DMShape PointB = new Point3DMShape(CurrentShape, ArgA / 2 * i, ArgB / 2, 0);
                    Point3DMShape PointC = new Point3DMShape(CurrentShape, ArgA / 2 * i, 0, ArgC);
                    PointA.Links.add(PointB);
                    PointB.Links.add(PointC);
                    PointC.Links.add(PointA);
                    TPoints.add(PointA);
                    TPoints.add(PointB);
                    TPoints.add(PointC);
                }
                for(int i = 0; i < 3; i++){
                    TPoints.get(i).Links.add(TPoints.get(i + 3));
                }
				CurrentShape.Planes.add(new int[]{0, 1, 2});
				CurrentShape.Planes.add(new int[]{3, 4, 5});
				CurrentShape.Planes.add(new int[]{1, 5, 4, 2});
				CurrentShape.Planes.add(new int[]{2, 4, 3, 0});
				CurrentShape.Planes.add(new int[]{0, 3, 5, 1});
                CurrentShape.BasePoints.addAll(TPoints);
                }break;
            case 'r':{ //Generic Prism Sides/Width/Length
                Vector<Point3DMShape> RPoints = new Vector<Point3DMShape>();
                int Sides = (int)ArgA;
               // System.out.println("S:" + Sides + "\t" + ArgB);
               	int[] Top = new int[Sides];
                int[] Bot = new int[Sides];
                for(int i = 0; i < Sides; i++){
                    double Theta = Math.PI * 2 * i / Sides;
                    Point3DMShape A = new Point3DMShape(CurrentShape, ArgB * Math.cos(Theta), ArgB * Math.sin(Theta), -ArgC / 2);
                    Point3DMShape B = new Point3DMShape(CurrentShape, ArgB * Math.cos(Theta), ArgB * Math.sin(Theta), ArgC / 2);
                    Top[i] = 2*i;
                    Bot[i] = 2*i+1;
                //    System.out.println(A);
                    A.Links.add(B);
                    RPoints.add(A);
                    RPoints.add(B);
                }
                int Size = RPoints.size();
                for(int j = 0; j < Sides; j++){
                    int i = j * 2;
                    RPoints.get(i).Links.add(RPoints.get((i + Size - 2) % Size));
                    RPoints.get((i + 1 + Size) % Size).Links.add(RPoints.get((i + Size - 2 + 1) % Size));
                    CurrentShape.Planes.add(new int[]{i, (i + Size - 2) % Size, (i + Size - 2 + 1) % Size, (i + 1 + Size) % Size});
                }
                CurrentShape.Planes.add(Top);
                CurrentShape.Planes.add(Bot);
                CurrentShape.BasePoints.addAll(RPoints);
            }break;
            case 's':{//Square Pyramid BaseX/BaseY/Height
                Vector<Point3DMShape> SPoints = new Vector<Point3DMShape>();
                Point3DMShape PointA = new Point3DMShape(CurrentShape, 0, 0, ArgC);
                Point3DMShape PointB = new Point3DMShape(CurrentShape, ArgB/2, ArgC/2, 0);
                Point3DMShape PointC = new Point3DMShape(CurrentShape, -ArgB/2, ArgC/2, 0);
                Point3DMShape PointD = new Point3DMShape(CurrentShape, -ArgB/2, -ArgC/2, 0);
                Point3DMShape PointE = new Point3DMShape(CurrentShape, ArgB/2, -ArgC/2, 0);

                PointA.Links.add(PointB);
                PointA.Links.add(PointC);
                PointA.Links.add(PointD);
                PointA.Links.add(PointE);

                PointB.Links.add(PointC);
                PointB.Links.add(PointE);
                PointD.Links.add(PointC);
                PointD.Links.add(PointE);

                CurrentShape.BasePoints.add(PointA);
                CurrentShape.BasePoints.add(PointB);
                CurrentShape.BasePoints.add(PointC);
                CurrentShape.BasePoints.add(PointD);
                CurrentShape.BasePoints.add(PointE);

				CurrentShape.Planes.add(new int[]{1, 2, 3, 4});
				CurrentShape.Planes.add(new int[]{0, 1, 2});
				CurrentShape.Planes.add(new int[]{0, 2, 3});
				CurrentShape.Planes.add(new int[]{0, 3, 4});
				CurrentShape.Planes.add(new int[]{0, 4, 1});
            }break;
            case 'p':{ //Tetrahedron SizeOfBase/Height/nil
                Vector<Point3DMShape> PPoints = new Vector<Point3DMShape>();
                Point3DMShape PointA = new Point3DMShape(CurrentShape, 0, 			1 * ArgA / Math.sqrt(3), 	0);
                Point3DMShape PointB = new Point3DMShape(CurrentShape, -0.5 * ArgA, 		-0.5 * ArgA / Math.sqrt(3),	0);
                Point3DMShape PointC = new Point3DMShape(CurrentShape, 0.5 * ArgA, 		-0.5 * ArgA / Math.sqrt(3), 	0);
                Point3DMShape PointD = new Point3DMShape(CurrentShape, 0, 			0, 				Math.sqrt(11.0/12) * ArgA * ArgB);

                PointA.Links.add(PointB);
                PointB.Links.add(PointC);
                PointC.Links.add(PointA);
                PointD.Links.add(PointA);
                PointD.Links.add(PointB);
                PointD.Links.add(PointC);
                CurrentShape.BasePoints.add(PointA);
                CurrentShape.BasePoints.add(PointB);
                CurrentShape.BasePoints.add(PointC);
                CurrentShape.BasePoints.add(PointD);
                
                CurrentShape.Planes.add(new int[]{0, 1, 2});
				CurrentShape.Planes.add(new int[]{3, 0, 2});
				CurrentShape.Planes.add(new int[]{3, 2, 1});
				CurrentShape.Planes.add(new int[]{3, 1, 0});
            }break;
            case 'd':{ //Dot nil/nil/nil
                Vector<Point3DMShape> PPoints = new Vector<Point3DMShape>();
                Point3DMShape PointA = new Point3DMShape(CurrentShape, 0, 0, 0);
                PointA.Links.add(PointA);
                CurrentShape.BasePoints.add(PointA);

            }break;
            case 'l':{ //Line Length/nil/nil
                Vector<Point3DMShape> PPoints = new Vector<Point3DMShape>();
                Point3DMShape PointA = new Point3DMShape(CurrentShape, 0.5 * ArgA, 0, 0);
                Point3DMShape PointB = new Point3DMShape(CurrentShape, -0.5 * ArgA, 0, 0);
                PointA.Links.add(PointB);
                CurrentShape.BasePoints.add(PointA);
                CurrentShape.BasePoints.add(PointB);
            }break;
            case 'h':{ //Chain Count/nil/nil
                Vector<Point3DMShape> PPoints = new Vector<Point3DMShape>();
                for(int i = 0; i < ArgA; i++){
                    PPoints.add(new Point3DMShape(CurrentShape, 0, 0, 0));
                    CurrentShape.BasePoints.add(PPoints.get(i));
                }
                for(int i = 0; i < ArgA - 1; i++){
                    PPoints.get(i).Links.add(PPoints.get(i + 1));

                }
            }break;
        }
        CurrentShape.finalizeLinks();

    }
    public static void centerStatic(Shape3DM Form, char Switch){ //Points don't move relative to center
        
        Point3DM Center = new Point3DM(0, 0, 0);
        double TotalLength = 0;
        switch(Switch){
            case 'p':
                for(int i = 0; i < Form.Points.length; i++){
                    Center.add(Form.Points[i], Center);
                }   
                break;
            case 'l':

                for(int i = 0; i < Form.Points.length; i++){
                    for(int j = 0; j < Form.Points[i].LinkIndex.length; j++){
                        Point3DM A = Form.Points[i];
                        Point3DM B = Form.Points[Form.Points[i].LinkIndex[j]];
                        Point3DM MidPoint = A.add(B).divide(2);
                        Point3DM D = new Point3DM(A.POSX - B.POSX, A.POSY - B.POSY, A.POSZ - B.POSZ);
                        double Distance = D.length();
                        TotalLength += Distance;
                        Center.add(MidPoint.multiply(Distance), Center);
                    }
                }
                Center.divide(TotalLength, Center);
                break;
            default:
                throw new IllegalArgumentException();
        }
        Form.Center.add(Center, Form.Center);
        Form.calcPoints();
    }
    public static double pathLength(Vector<Point3DM> Path){
        double L = 0;
        for(int i = 1; i < Path.size(); i++){
            L += Path.get(i).distanceTo(Path.get(i-1));
        }
        return L;
    }
    public static double pathLength(Point3DM[] Path){
        double L = 0;
        for(int i = 1; i < Path.length; i++){
            L += Path[i].distanceTo(Path[i-1]);
        }
        return L;
    }
    public static void center(Shape3DM Form, char Switch){//Points don't move relative to Space
		if(Form.PointCount == 0){
			return;
		}
       
        Point3DM Center = new Point3DM(0, 0, 0);
        double TotalLength = 0;
        switch(Switch){
            case 'p':
                for(int i = 0; i < Form.Points.length; i++){
                    Center.add(Form.Points[i], Center);
                }   
                break;
            case 'l':

                for(int i = 0; i < Form.Points.length; i++){
                    for(int j = 0; j < Form.Points[i].LinkIndex.length; j++){
                        Point3DM A = Form.Points[i];
                        Point3DM B = Form.Points[Form.Points[i].LinkIndex[j]];
                        Point3DM MidPoint = A.add(B).divide(2);
                        Point3DM D = new Point3DM(A.POSX - B.POSX, A.POSY - B.POSY, A.POSZ - B.POSZ);
                        double Distance = D.length();
                        TotalLength += Distance;
                        Center.add(MidPoint.multiply(Distance), Center);
                    }
                }
                Center.divide(TotalLength, Center);
                break;
            default:
                throw new IllegalArgumentException();
        }
		
        Center.divide(Form.Points.length, Center);
        Center.add(Form.Center, Form.Center);
        Form.MaxDistance = 0;
        
        for(int i = 0; i < Form.Points.length; i++){
            Form.Points[i].subtract(Center, Form.Points[i]);
            
        }

        for(int i = 0; i < Form.BasePoints.size(); i++){
            Form.MaxDistance = Math.max(Form.BasePoints.get(i).distanceTo(Utils3DM.Origin), Form.MaxDistance);
        }
    }
 
    static Store<Rotation3DM> RS = new Store<Rotation3DM>();
    public static Rotation3DM getRotation(){
   //     System.out.println(RS.Pile.size());
        Rotation3DM R = RS.get();
        if(R == null){
            R = new Rotation3DM();
        }
        return R;
    }
    public static void putRotation(Rotation3DM R){
        RS.put(R);
    }
    
}
class Store<E>{
    Stack<E> Pile = new Stack<E>();
    public E get(){
        if(Pile.size() > 0){
            return Pile.pop();
        }else{
            return null;
        }
    }
    public void put(E Thing){
        Pile.add(Thing);
    }
} 
abstract class Printable3DM{
    Point3DM Center;
    Color Tint;
    protected Point3DM TransformedCenter;
    double MaxDistance;
    double Dmax;
    double Dmin;
    abstract void print(ObserverM View);
    abstract void printFinish(ObserverM View);
    abstract void printNow(Graphics2D Painter, ObserverM View);
    abstract void printNow(Graphics2D Painter, ObserverM View, Color Tint);
    abstract void getRepresentativePlane(Vector<Point3DM> V);
    abstract double getDepth();
}

class Sprite3DM extends Printable3DM{
    Shape Image;
    private Shape FinalImage;
    AffineTransform Transformer;
    
    boolean Scaling = true;
    private Point2D.Double ScreenCenter;

    public void getRepresentativePlane(Vector<Point3DM> V){
    	V.clear();
    	V.add(Center);
    	Point3DM New = new Point3DM(0.001, 0.01, 0.01);
    	New.add(Center, New);
    	V.add(New);
    	New = new Point3DM(0.001, -0.01, -0.01);
    	New.add(Center, New);
    	V.add(New);
    }
    public Sprite3DM(Shape I, Point3DM C, Color T){
        Image = I;
        Center = C.copy();
        TransformedCenter = Center.copy();
        ScreenCenter = new Point2D.Double();
        Transformer = new AffineTransform();
        Tint = T;
    }
    public final void setLocation(Point3DM Target){
        Target.copyTo(Center);
    }
    public final void center(ObserverM View){
        Center.center(View, TransformedCenter);
        TransformedCenter.toScreen(View, ScreenCenter);
      	Dmax = TransformedCenter.POSX;
      	Dmin = TransformedCenter.POSX;
    }
    public final boolean checkForBehind(ObserverM View){
        Center.subtract(View.POS, TransformedCenter);
        return (TransformedCenter.dotProduct(View.LookVector) < 0);
        
    }
    private final void setFinalImage(double Scale, Point2D.Double Position){
        
        Transformer.setToIdentity();
       
        
        Transformer.translate((int)Position.x, (int)Position.y);
        if(Scaling)Transformer.scale(Scale, Scale);
        //else Transformer.scale(1, 1);
        
        FinalImage = Transformer.createTransformedShape(Image);
        
    }
    public final void print(ObserverM View){
        if(checkForBehind(View)){
            return;
        }
        center(View);
        double Scale = View.HalfYMag / View.LookVector.dotProduct(Center.subtract(View.POS));
        
        setFinalImage(Scale, ScreenCenter);
    }
    public final void printFinish(ObserverM View){
        Graphics2D Painter = View.Painter;
        
        Painter.setColor(Tint);
        
        Painter.draw(FinalImage);
    }
    public final void printNow(Graphics2D Painter, ObserverM View){
        if(checkForBehind(View)){
            return;
        }
        center(View);
      
        double Scale = View.HalfYMag / View.LookVector.dotProduct(Center.subtract(View.POS));
        
        setFinalImage(Scale, ScreenCenter);
        Painter.setColor(Tint);
        
        Painter.draw(FinalImage);
    }
    public final void printNow(Graphics2D Painter, ObserverM View, Color PrintColor){
        if(checkForBehind(View)){
            return;
        }
        center(View);
      
        double Scale = View.HalfYMag / View.LookVector.dotProduct(Center.subtract(View.POS));
        
        setFinalImage(Scale, ScreenCenter);
        Painter.setColor(PrintColor);
        
        Painter.draw(FinalImage);
    }
    public final int compareTo(Object O){
        return Double.compare(Center.POSX, ((Printable3DM)O).getDepth());
    }
    public final double getDepth(){
        return TransformedCenter.POSX;
    }
    //public void print(Graphics2D Painter, ObserverM View){
        
    //}
     
}   

    
    class Shape3DM extends Printable3DM{
        
        
    
        final Vector<Point3DMShape> BasePoints = new Vector<Point3DMShape>();
        public Point3DMShape[] Points; // Base Points before rotation and translation; Points relative to the object
        Point3DMShape[] ActualPoints; //Actual Points after rotation and translation; Points in 3D Space
        private Point3DM[] TransformedPoints; //Imaginary Points centered on an arbitrary Viewpoint; Points relative to Viewer
        private Point2D.Double[] FlatPoints; //Imaginary Points flattened onto screen of Viewer
        boolean Static; //Whether or not objects rotation and position moves; Affects whether or not ActualPoints are re-computed
        
        int PointCount;
        Vector<int[]> Planes = new Vector<int[]>();
        
        final private Point2D.Double FlatCenter = new Point2D.Double();
        
        final private Point3DM ObserverVector = new Point3DM(0, 0, 0);
        char Form;
        double ArgA;
        double ArgB;
        double ArgC;
        

		
        final Rotation3DM Rotation = new Rotation3DM();
        boolean Visible = true;
        
        public final boolean checkForBehind(ObserverM Viewer){
            Center.subtract(Viewer.POS, ObserverVector);
            return (ObserverVector.dotProduct(Viewer.LookVector) + MaxDistance < 0);
            
        }
        public final void getRepresentativePlane(Vector<Point3DM> V){
        	V.clear();
        	if(Planes.size() == 1){
	        	for(int i = 0; i < Planes.get(0).length; i++){
    	    		V.add(ActualPoints[Planes.get(0)[i]]);	
        		}
        	}else{
        		V.add(Center);
		    	Point3DM New = new Point3DM(0.1, 0.01, 0.001);
		    	New.add(Center, New);
		    	V.add(New);
		    	New = new Point3DM(0.1, -0.01, -0.001);
		    	New.add(Center, New);
		    	V.add(New);
        	}
        }
        public final Shape3DM copy(){
        	
            Shape3DM Target = new Shape3DM();
            Target.Points = new Point3DMShape[Points.length];
            Target.ActualPoints = new Point3DMShape[Points.length];
            Target.TransformedPoints = new Point3DM[Points.length];
            Target.FlatPoints = new Point2D.Double[FlatPoints.length];
            for(int i = 0; i < Points.length; i++){
                Target.BasePoints.add(BasePoints.get(i).copy());
                Target.Points[i] = Points[i].copy();
                Target.ActualPoints[i] = ActualPoints[i].copy();
                Target.TransformedPoints[i] = TransformedPoints[i].copy();
                Target.FlatPoints[i] = new Point2D.Double();
            }
            
            Center.copyTo(Target.Center);
            Target.Static = Static;
            Target.Tint = Tint;

            Target.ArgA = ArgA;
            Target.ArgB = ArgB;
            Target.ArgC = ArgC;
            Target.MaxDistance = MaxDistance;
            Rotation.copyTo(Target.Rotation);
            Target.PointCount = PointCount;
            return Target;
        }
        public Shape3DM(int PC){
            PointCount = PC;
            Center = new Point3DM(0, 0, 0);
            Tint = Color.white;
            Static = true;
            TransformedCenter = new Point3DM();
            TransformedPoints = new Point3DM[PC];
            FlatPoints = new Point2D.Double[PC];
            Points = new Point3DMShape[PC];
            ActualPoints = new Point3DMShape[PC];
            for(int i = 0; i < PC; i++){
                TransformedPoints[i] = new Point3DM(0, 0, 0);
                FlatPoints[i] = new Point2D.Double();
                Points[i] = new Point3DMShape(this, 0, 0, 0);
                ActualPoints[i] = new Point3DMShape(this, 0, 0, 0);
            }
            
        }
        public Shape3DM(){
        	TransformedCenter = new Point3DM();
            Center = new Point3DM(0, 0, 0);
            Tint = Color.white;
            Static = true;
            ActualPoints = new Point3DMShape[0];
            TransformedPoints = new Point3DMShape[0];
            FlatPoints = new Point2D.Double[0];
        }
        public Shape3DM(boolean tStatic, Color tTint, double tPOSX, double tPOSY, double tPOSZ){
        	TransformedCenter = new Point3DM();
            Static = tStatic;
            Tint = tTint;
            Center = new Point3DM(tPOSX, tPOSY, tPOSZ);
            
        }


        public final void center(ObserverM Viewer){
            Dmax = Integer.MIN_VALUE;
            Dmin = Integer.MAX_VALUE;
            for(int i = 0; i < PointCount; i++){
                ActualPoints[i].center(Viewer, TransformedPoints[i]);
                Dmax = Math.max(TransformedPoints[i].POSX, Dmax);
                Dmin = Math.min(TransformedPoints[i].POSX, Dmin);
            }
            
        }
        public final void centerCenter(ObserverM Viewer){
            Center.center(Viewer, TransformedCenter);
        }
        public final void calcPoints(){
            
            for(int i = 0; i < PointCount; i++){
                
                 Points[i].rotate(Rotation, ActualPoints[i]);
                
                 ActualPoints[i].add(Center, ActualPoints[i]);
                
            }
        }
        
        public final void linkPoints(Graphics2D Painter, int A, int B){
        
            double dA = TransformedPoints[A].POSX;
            double dB = TransformedPoints[B].POSX;
            
            if(dA < 0 && dB < 0){
                return;
            }
            if(dA > 0 && dB > 0){
                Painter.drawLine((int)FlatPoints[A].x, (int)FlatPoints[A].y, (int)FlatPoints[B].x, (int)FlatPoints[B].y);            
                return;
            }
            if(dA > 0 && dB < 0){ 
            	double Multiple = Math.min(Integer.MAX_VALUE / Math.abs(FlatPoints[B].x - FlatPoints[A].x), Integer.MAX_VALUE / Math.abs(FlatPoints[B].y - FlatPoints[A].y));
                Painter.drawLine((int)FlatPoints[A].x, (int)FlatPoints[A].y, (int)(FlatPoints[A].x - (FlatPoints[B].x - FlatPoints[A].x) * Multiple), (int)(FlatPoints[A].y - (FlatPoints[B].y - FlatPoints[A].y) * Multiple));
                return;
            }
            if(dA < 0 && dB > 0){
            	double Multiple = Math.min(Integer.MAX_VALUE / Math.abs(FlatPoints[A].x - FlatPoints[B].x), Integer.MAX_VALUE / Math.abs(FlatPoints[A].y - FlatPoints[B].y));
                Painter.drawLine((int)FlatPoints[B].x, (int)FlatPoints[B].y, (int)(FlatPoints[B].x - (FlatPoints[A].x - FlatPoints[B].x) * Multiple), (int)(FlatPoints[B].y - (FlatPoints[A].y - FlatPoints[B].y) * Multiple));
                return;
            }
            
        }
        
        public final void fillPolygons(ObserverM Viewer){
        	if(Planes.size() != 1){
        		return;
        	}
        	Viewer.Painter.setColor(Color.black);
        	for(int i = 0; i < Planes.size(); i++){
        		int[] P = Planes.get(i);
        		Polygon Poly = new Polygon();
        		for(int j = 0; j < P.length; j++){
        			int A = P[j];
        			int B = P[(j+1)%P.length];
        			double dA = TransformedPoints[A].POSX;		
        			double dB = TransformedPoints[B].POSX;
        			if(dA > 0 && dB > 0){
        				Poly.addPoint((int)FlatPoints[B].x, (int)FlatPoints[B].y);
        			}
        			if(dA > 0 && dB < 0){
        				double Multiple = Math.min(Integer.MAX_VALUE / Math.abs(FlatPoints[B].x - FlatPoints[A].x), Integer.MAX_VALUE / Math.abs(FlatPoints[B].y - FlatPoints[A].y));
        				Poly.addPoint((int)(FlatPoints[A].x - (FlatPoints[B].x - FlatPoints[A].x) * Multiple), (int)(FlatPoints[A].y - (FlatPoints[B].y - FlatPoints[A].y) * Multiple));
        			}
        			if(dA < 0 && dB > 0){
        				double Multiple = Math.min(Integer.MAX_VALUE / Math.abs(FlatPoints[A].x - FlatPoints[B].x), Integer.MAX_VALUE / Math.abs(FlatPoints[A].y - FlatPoints[B].y));
                		
                		Poly.addPoint((int)(FlatPoints[B].x - (FlatPoints[A].x - FlatPoints[B].x) * Multiple), (int)(FlatPoints[B].y - (FlatPoints[A].y - FlatPoints[B].y) * Multiple));
                		Poly.addPoint((int)FlatPoints[B].x, (int)FlatPoints[B].y);
        			}	
        		}	
        		Viewer.Painter.fill(Poly);
        	}
        	
        }
        public final void flatten(ObserverM Viewer){
            
            for(int i = 0; i < PointCount; i++){
                 TransformedPoints[i].toScreen(Viewer, FlatPoints[i]);
            }
        }
        
      
       	public final void print(ObserverM Viewer){
            if(!Visible){return;}
            if(checkForBehind(Viewer)){return;}

            centerCenter(Viewer);
            Viewer.PrintingShapes.add(this);
            
        }
        public final void printFinish(ObserverM Viewer){
            Graphics2D Painter = Viewer.Painter;
               
            
            if(Math.abs(MaxDistance / TransformedCenter.POSX) * Viewer.Magnification > Viewer.PixelSize){
            	center(Viewer);
            	flatten(Viewer);	    
                
                
                
              //  fillPolygons(Viewer);
                Painter.setClip(Viewer.PX, Viewer.PY, Viewer.ScreenX, Viewer.ScreenY);
                
             
                Painter.setColor(Tint); 
                for(int i = 0; i < PointCount; i++){

                    for(int j = 0; j < ActualPoints[i].LinkIndex.length; j++){

                        linkPoints(Painter, i, ActualPoints[i].LinkIndex[j]);
                    }
                }
                Painter.setClip(null);
            }else{
              //  System.out.println("A");
                TransformedCenter.toScreen(Viewer, FlatCenter);
                Painter.setColor(Tint); 
                Painter.drawLine((int)FlatCenter.x, (int)FlatCenter.y, (int)FlatCenter.x, (int)FlatCenter.y);
            }
        }
        public final void printNow(Graphics2D Painter, ObserverM Viewer){
            printNow(Painter, Viewer, Tint);
        }
        public final void printNow(Graphics2D Painter, ObserverM Viewer, Color PrintColor){
            
            if(!Visible){return;}
            if(checkForBehind(Viewer)){return;}
           
            centerCenter(Viewer);
            
            
             
            Painter.setColor(PrintColor);   
            if(Math.abs(MaxDistance / TransformedCenter.POSX) * Viewer.Magnification > Viewer.PixelSize){
                
                center(Viewer);
                flatten(Viewer);
                
                Painter.setClip(Viewer.PX, Viewer.PY, Viewer.ScreenX, Viewer.ScreenY);
                

                
                
                for(int i = 0; i < PointCount; i++){

                    for(int j = 0; j < ActualPoints[i].LinkIndex.length; j++){

                        linkPoints(Painter, i, ActualPoints[i].LinkIndex[j]);
                    }
                }
                Painter.setClip(null);
            }else{
              //  System.out.println("A");
                TransformedCenter.toScreen(Viewer, FlatCenter);
                
                Painter.drawLine((int)FlatCenter.x, (int)FlatCenter.y, (int)FlatCenter.x, (int)FlatCenter.y);
            }
        }
        public final void finalizeLinks(){
            for(int i = 0; i < BasePoints.size(); i++){
                BasePoints.get(i).finalizeLinks();
                
            }
            for(int i = 0; i < BasePoints.size(); i++){
                MaxDistance = Math.max(BasePoints.get(i).distanceTo(Utils3DM.Origin), MaxDistance);
            }
            Points = new Point3DMShape[BasePoints.size()];
            ActualPoints = new Point3DMShape[BasePoints.size()];
            TransformedPoints = new Point3DM[BasePoints.size()];
            FlatPoints = new Point2D.Double[BasePoints.size()];
            for(int i = 0; i < BasePoints.size(); i++){
                
                Points[i] = BasePoints.get(i);
                
                ActualPoints[i] = new Point3DMShape(this, 0, 0, 0);
                ActualPoints[i].LinkIndex = Points[i].LinkIndex;
                TransformedPoints[i] = new Point3DM(0, 0, 0);
                FlatPoints[i] = new Point2D.Double();
            }
            PointCount = BasePoints.size();
            calcPoints();
        }
        public void toBytes(DataOutput Output) throws IOException{
            Output.writeInt(Tint.getRGB()); //1 
            Center.toBareBytes(Output); //2
            Output.writeFloat((float)MaxDistance); //3
            Output.writeShort((short)PointCount); //4
            for(int i = 0; i < PointCount; i++){ //5
                ActualPoints[i].toBytes(Output);
            }
        }
        public void fromBytes(DataInput Input) throws IOException{
            Tint = new Color(Input.readInt()); //1
            Center.fromBareBytes(Input); //2
            MaxDistance = Input.readFloat(); //3
            PointCount = Input.readShort(); //4
           // System.out.println(PointCount);
            if(ActualPoints.length < PointCount){
                Point3DMShape[] NewPoints = new Point3DMShape[PointCount];
                System.arraycopy(ActualPoints, 0, NewPoints, 0, ActualPoints.length);
                ActualPoints = NewPoints;
                NewPoints = new Point3DMShape[PointCount];
                System.arraycopy(TransformedPoints, 0, NewPoints, 0, TransformedPoints.length);
                TransformedPoints = NewPoints;
                Point2D.Double[] FPoints = new Point2D.Double[PointCount];
                System.arraycopy(FlatPoints, 0, FPoints, 0, FlatPoints.length);
                FlatPoints = FPoints;
              //  System.out.println("A:"+ActualPoints.length);
                for(int i = ActualPoints.length - 1; i >= 0 && ActualPoints[i] == null; i--){
                  //  System.out.println("i" + i);
                    ActualPoints[i] = new Point3DMShape(this, 0, 0, 0);
                    TransformedPoints[i] = new Point3DM(0, 0, 0);
                    FlatPoints[i] = new Point2D.Double();
                }
            }
            for(int i = 0; i < PointCount; i++){ //5
                ActualPoints[i].fromBytes(Input);
            }
        }
      
        
        public double getDepth(){
            return TransformedCenter.POSX;
        }
    }    
    
class Rotation3DM{
    final private double[][] Matrix = new double[3][3];
    static final private double[][] TempMatrix = new double[3][3];
    static private Point3DM TempPoint = Utils3DM.Origin.copy();
    
    public Rotation3DM(double[][] M){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Matrix[i][j] = M[i][j];
            }
        }
        
    }
    public Rotation3DM(String Temp){
        
    }
    public Rotation3DM(){
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
    public final void checkNaN(){
    	for(int i = 0; i < 3; i++){
    		for(int j = 0; j < 3; j++){
    			if(Matrix[i][j] != Matrix[i][j]){
    				throw new IllegalArgumentException("NaN Created");	
    			}	
    		}	
    	}	
    }
    public final void identify(){
        Matrix[0][0] = 1; Matrix[0][1] = 0; Matrix[0][2] = 0;
        Matrix[1][0] = 0; Matrix[1][1] = 1; Matrix[1][2] = 0;
        Matrix[2][0] = 0; Matrix[2][1] = 0; Matrix[2][2] = 1;
     
    }
    public final void createFromPoints(Point3DM A, Point3DM B, Point3DM C){
    	
        Matrix[0][0] = A.POSX; Matrix[0][1] = A.POSY; Matrix[0][2] = A.POSZ;
        Matrix[1][0] = B.POSX; Matrix[1][1] = B.POSY; Matrix[1][2] = B.POSZ;
        Matrix[2][0] = C.POSX; Matrix[2][1] = C.POSY; Matrix[2][2] = C.POSZ;
  		checkNaN();
    }
    public final void inverse(Rotation3DM Target){
        
    
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
        checkNaN();
    }
  
    public final void getOriginalAxis(char Axis, Point3DM Target){
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
    	if(Angle != Angle){
    		throw new IllegalArgumentException("NaN Passed");	
    	}
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
        checkNaN();
    }
    public final Rotation3DM copy(){
    	checkNaN();
        Rotation3DM Target = new Rotation3DM();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Target.Matrix[i][j] = Matrix[i][j];
            }
        }
        return Target;
    }
    public final void copyTo(Rotation3DM Target){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Target.Matrix[i][j] = Matrix[i][j];
            }
        }
        Target.checkNaN();
    }
    public final void inverseTransform(Point3DM Start, Point3DM End){
    	checkNaN();
        TempPoint.POSX = Matrix[0][0] * Start.POSX + Matrix[1][0] * Start.POSY + Matrix[2][0] * Start.POSZ; 
        TempPoint.POSY = Matrix[0][1] * Start.POSX + Matrix[1][1] * Start.POSY + Matrix[2][1] * Start.POSZ;
        TempPoint.POSZ = Matrix[0][2] * Start.POSX + Matrix[1][2] * Start.POSY + Matrix[2][2] * Start.POSZ;
        End.POSX = TempPoint.POSX;
        End.POSY = TempPoint.POSY;
        End.POSZ = TempPoint.POSZ;
        End.checkNaN();
    }
    public final void transform(Point3DM Start, Point3DM End){
    	checkNaN();
        TempPoint.POSX = Matrix[0][0] * Start.POSX + Matrix[0][1] * Start.POSY + Matrix[0][2] * Start.POSZ; 
        TempPoint.POSY = Matrix[1][0] * Start.POSX + Matrix[1][1] * Start.POSY + Matrix[1][2] * Start.POSZ;
        TempPoint.POSZ = Matrix[2][0] * Start.POSX + Matrix[2][1] * Start.POSY + Matrix[2][2] * Start.POSZ;
        End.POSX = TempPoint.POSX;
        End.POSY = TempPoint.POSY;
        End.POSZ = TempPoint.POSZ;
        End.checkNaN();
    }  
    static private Point3DM M = new Point3DM(0, 0, 0);
    static private Point3DM T = new Point3DM(0, 0, 0);
    static private Point3DM Z = new Point3DM(0, 0, 0);
    static private Point3DM Y = new Point3DM(0, 0, 0);
    static private Rotation3DM R = new Rotation3DM();
    static private Rotation3DM S = new Rotation3DM();
    static private Rotation3DM Ri = new Rotation3DM();
    
    public static void getRotationBetween(Point3DM m, Point3DM t, double MaxRadians, Rotation3DM Target){
        if((m.crossProduct(t).length()) == 0){
            Target.identify();
            return;
        }
        m.copyTo(M);
        M.normalize();
        t.copyTo(T);
        T.normalize();
        M.crossProduct(T, Z);
        Z.normalize();
        Z.crossProduct(M, Y);
        Y.normalize();
            
        double Angle = T.dotProduct(M);
        Angle = GUI.trigCap(Angle);
        Angle = Math.acos(Angle);
        if(Angle < -MaxRadians) Angle = -MaxRadians;
        if(Angle > MaxRadians) Angle = MaxRadians;
        
        
            
        
        R.createFromPoints(M, Y, Z);
        R.inverse(Ri); 
        S.rotateAround('z', Angle);
            
        Target.identify();
        Rotation3DM.multiply(R, Target, Target);
        Rotation3DM.multiply(S, Target, Target);
        Rotation3DM.multiply(Ri, Target, Target);
        Target.checkNaN();
     
    }
    
    public static void multiply(Rotation3DM a, Rotation3DM b, Rotation3DM End){
    	a.checkNaN();
		b.checkNaN();
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
      
  		End.checkNaN();
    }
    public void toBytes(DataOutput Output) throws IOException{
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Output.writeFloat((float)Matrix[i][j]);//1
            }
        }
    }
    public void fromBytes(DataInput Input) throws IOException{
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Matrix[i][j] = Input.readFloat(); //1
            }
        }
    }
}

class Point3DMShape extends Point3DM{
    Vector<Point3DM> Links;
    Shape3DM Base;
    int[] LinkIndex;
    
    public Point3DMShape(Shape3DM tBase, double tPOSX, double tPOSY, double tPOSZ){
        Links = new Vector();
        Base = tBase;
        POSX = tPOSX;
        POSY = tPOSY;
        POSZ = tPOSZ;
    }
    
    public Point3DMShape copy(){
        Point3DMShape Point = new Point3DMShape(Base, POSX, POSY, POSZ);
        if(LinkIndex != null){
            Point.LinkIndex = new int[LinkIndex.length];
            for(int i = 0; i < LinkIndex.length; i++){
                Point.LinkIndex[i] = LinkIndex[i];
            }
        }else{
            Point.LinkIndex = null;
        }
        return Point;
    }
    public void copy(Point3DMShape Target){
        Target.POSX = POSX;
        Target.POSY = POSY;
        Target.POSZ = POSZ;
        Target.LinkIndex = new int[LinkIndex.length];
        for(int i = 0; i < LinkIndex.length; i++){
            Target.LinkIndex[i] = LinkIndex[i];
        }

    }
    public void finalizeLinks(){

        LinkIndex = new int[Links.size()];
        for(int i = 0; i < Links.size(); i++){
            LinkIndex[i] = Base.BasePoints.indexOf(Links.get(i));
        }

    }
    public void toBytes(DataOutput Output) throws IOException{
        if(LinkIndex == null){
            Output.writeInt(0);
        }else{
            Output.writeInt(LinkIndex.length); //1
            for(int i = 0; i < LinkIndex.length; i++){
                Output.writeInt(LinkIndex[i]); //2
            }
        }
        Output.writeFloat((float)POSX); //3
        Output.writeFloat((float)POSY); //4
        Output.writeFloat((float)POSZ); //5
    }
    public void fromBytes(DataInput Input) throws IOException{
        int L = Input.readInt(); //1
        if(LinkIndex == null || LinkIndex.length != L){
            LinkIndex = new int[L];
        }
        for(int i = 0; i < L; i++){
            LinkIndex[i] = Input.readInt(); //2
        }
        POSX = Input.readFloat(); //3
        POSY = Input.readFloat(); //4
        POSZ = Input.readFloat(); //5
    }
}
class Point3DM{
    double POSX;
    double POSY;
    double POSZ;
    
    public final void zero(){
        POSX = 0; POSY = 0; POSZ = 0;
    }
    public final boolean equals(Point3DM Target){
        return (this.POSX == Target.POSX) && (this.POSY == Target.POSY) && (this.POSZ == Target.POSZ);
    }
    public Point3DM copy(){
        Point3DM Point = new Point3DM(POSX, POSY, POSZ);
        
        return Point;
    }
    public final void copyTo(Point3DM Target){
        Target.POSX = POSX;
        Target.POSY = POSY;
        Target.POSZ = POSZ;
        Target.checkNaN();
    }
    public final void copyFrom(Point3DM Target){
        POSX = Target.POSX;
        POSY = Target.POSY;
        POSZ = Target.POSZ;

		checkNaN();
    }
    public Point3DM(){
        POSX = 0;
        POSY = 0;
        POSZ = 0;
        checkNaN();
    }
    public Point3DM(double tPOSX, double tPOSY, double tPOSZ){
        POSX = tPOSX;
        POSY = tPOSY;
        POSZ = tPOSZ;
        checkNaN();
    }
    public final void checkNaN(){
    	if(POSX != POSX || POSY != POSY || POSZ != POSZ){
    		throw new IllegalArgumentException("NaN Created");	
    	}	
    }
    public final Point3DM normalize(double D){
        double L = Math.sqrt(POSX * POSX + POSY * POSY + POSZ * POSZ) / D;
        POSX = POSX / L;
        POSY = POSY / L;
        POSZ = POSZ / L;
        checkNaN();
        return this;
    }
    public final Point3DM normalize(){
        double L = Math.sqrt(POSX * POSX + POSY * POSY + POSZ * POSZ);
        POSX = POSX / L;
        POSY = POSY / L;
        POSZ = POSZ / L;
        checkNaN();
        return this;
    }

    public final Point3DM add(Point3DM Target){
    	Point3DM New = new Point3DM(POSX + Target.POSX, POSY + Target.POSY, POSZ + Target.POSZ);
    	New.checkNaN();
        return New;
    }
    public final Point3DM add(Point3DM Target, Point3DM Result){
        Result.POSX = POSX + Target.POSX;
        Result.POSY = POSY + Target.POSY;
        Result.POSZ = POSZ + Target.POSZ;
        checkNaN();
        return Result;
    }
    public final Point3DM subtract(Point3DM Target){
    	Point3DM New = new Point3DM(POSX - Target.POSX, POSY - Target.POSY, POSZ - Target.POSZ);
    	New.checkNaN();
        return New;
    }
    public final Point3DM subtract(Point3DM Target, Point3DM Result){
        Result.POSX = POSX - Target.POSX;
        Result.POSY = POSY - Target.POSY;
        Result.POSZ = POSZ - Target.POSZ;
        checkNaN();
        return Result;
    }
    public final Point3DM multiply(double Multiple, Point3DM Target){
        Target.setLocation(POSX * Multiple, POSY * Multiple, POSZ * Multiple);
        Target.checkNaN();
        return Target;
    }
    public final Point3DM divide(double Multiple, Point3DM Target){
        Target.setLocation(POSX / Multiple, POSY / Multiple, POSZ / Multiple);
        Target.checkNaN();
        return Target;
    }
    public final Point3DM multiply(double Multiple){
    	Point3DM New = new Point3DM(POSX * Multiple, POSY * Multiple, POSZ * Multiple);
    	New.checkNaN();
        return New;
    }
    public final Point3DM divide(double Multiple){
    	Point3DM New = new Point3DM(POSX / Multiple, POSY / Multiple, POSZ / Multiple);
    	New.checkNaN();
        return New;
    }
    public final double dotProduct(Point3DM Other){
        return POSX * Other.POSX + POSY * Other.POSY + POSZ * Other.POSZ;
    }
    public final double dot(Point3DM Other){
        return dotProduct(Other);
    }   
    public final Point3DM crossProduct(Point3DM Other){
    	Point3DM New = new Point3DM(POSY * Other.POSZ - POSZ * Other.POSY, POSZ * Other.POSX - POSX * Other.POSZ, POSX * Other.POSY - POSY * Other.POSX);
    	New.checkNaN();
        return New;
    }
    public final Point3DM cross(Point3DM Other){
        return crossProduct(Other);
    }
    public final void crossProduct(Point3DM Other, Point3DM Target){
        Target.POSX = POSY * Other.POSZ - POSZ * Other.POSY;
        Target.POSY = POSZ * Other.POSX - POSX * Other.POSZ;
        Target.POSZ = POSX * Other.POSY - POSY * Other.POSX;
        Target.checkNaN();
    }
    public final double distanceTo(Point3DM Target){
    	checkNaN();
    	Target.checkNaN();
        return Math.sqrt((Target.POSX - POSX) * (Target.POSX - POSX) + (Target.POSY - POSY) * (Target.POSY - POSY) + (Target.POSZ - POSZ) * (Target.POSZ - POSZ));
    }
    public final double distanceToSquare(Point3DM Target){
    	checkNaN();
    	Target.checkNaN();
        return (Target.POSX - POSX) * (Target.POSX - POSX) +(Target.POSY - POSY) * (Target.POSY - POSY) + (Target.POSZ - POSZ) * (Target.POSZ - POSZ);
    }
    public final double flatDistanceTo(Point3DM Target){
    	checkNaN();
    	Target.checkNaN();
        return Math.sqrt((Target.POSX - POSX) * (Target.POSX - POSX) +(Target.POSY - POSY) * (Target.POSY - POSY));
    }
    
    public final double length(){
    	checkNaN();
        return Math.sqrt(POSX * POSX + POSY * POSY + POSZ * POSZ);
    }

    public final void setToMidpoint(Point3DM A, Point3DM B){
        POSX = (A.POSX + B.POSX) / 2;
        POSY = (A.POSY + B.POSY) / 2;
        POSZ = (A.POSZ + B.POSZ) / 2;
    }

    public final void setLocation(double X, double Y, double Z){
        POSX = X;
        POSY = Y;
        POSZ = Z;
    }
    

    public final void rotate(Rotation3DM R, Point3DM Point){

        R.transform(this, Point);

    }
    public final void rotate(Point3DM Center, Rotation3DM R, Point3DM Point){

        Point.POSX = this.POSX - Center.POSX;
        Point.POSY = this.POSY - Center.POSY;
        Point.POSZ = this.POSZ - Center.POSZ;
        R.transform(Point, Point);
        Point.POSX = Point.POSX + Center.POSX;
        Point.POSY = Point.POSY + Center.POSY;
        Point.POSZ = Point.POSZ + Center.POSZ;
		
    }

    public final void center(ObserverM Viewer, Point3DM Point){
        Point.POSX = this.POSX - Viewer.POS.POSX;
        Point.POSY = this.POSY - Viewer.POS.POSY;
        Point.POSZ = this.POSZ - Viewer.POS.POSZ;
        Viewer.Rotation.inverseTransform(Point, Point);

        if(Point.POSX < 0.01 && Point.POSX > -0.01){
            Point.POSX = 0.001;
        }
       
    }
	
    double Temp;
    public final void toScreen(ObserverM Viewer, Point2D.Double Target){
        Temp = Viewer.HalfYMag / POSX;
        Target.x = -POSY * Temp + Viewer.CenterX;
        Target.y = -POSZ * Temp + Viewer.CenterY;

    }


    public final String toString(){
        return "Point: " + "\t" + GUI.round(POSX, 100) + "\t " + GUI.round(POSY, 100) + "\t " + GUI.round(POSZ, 100);

    }
    public void toBytes(DataOutput Output) throws IOException{
        
        Output.writeFloat((float)POSX); //3
        Output.writeFloat((float)POSY); //4
        Output.writeFloat((float)POSZ); //5
    }
    public void toBareBytes(DataOutput Output) throws IOException{
        Output.writeFloat((float)POSX); //1
        Output.writeFloat((float)POSY); //2
        Output.writeFloat((float)POSZ); //3
    }
    public void fromBareBytes(DataInput Input) throws IOException{
        POSX = Input.readFloat(); //1
        POSY = Input.readFloat(); //2 
        POSZ = Input.readFloat(); //3
    }
    public void fromBytes(DataInput Input) throws IOException{
        
        POSX = Input.readFloat(); //3
        POSY = Input.readFloat(); //4
        POSZ = Input.readFloat(); //5
    }
}
class ObserverM{
    Point3DM POS;

    int PX; //X of top left corner
    int PY; //Y of top left corner
    int ScreenX; //Width of Screen
    int ScreenY; //Height of Screen
    int HalfX; //Half Width of Screen
    int HalfY; //Half Height of Screen
    int CenterX; //Center of Screen
    int CenterY; //Center of Screen
    double Magnification = 1.00; //Magnification
    int HalfXMag; //Half Width with Magnification
    int HalfYMag; //Half Height with Magnification
    
    float PixelSize = 1.0f/640;
    Point3DM LookVector = new Point3DM(0, 0, 0);
    Rotation3DM Rotation = new Rotation3DM();
 //   Rotation3DM InverseRotation = new Rotation3D();
    
    Shape3DM LineShape = new Shape3DM(1000);
    Graphics2D Painter;
    Vector<Printable3DM> PrintingShapes = new Vector<Printable3DM>();
    public ObserverM copy(){
        ObserverM O = new ObserverM(POS.POSX, POS.POSY, POS.POSZ, Rotation.copy(), Painter);
        O.PX = PX;          O.PY = PY;
        O.ScreenX = ScreenX;    O.ScreenY = ScreenY;
        O.HalfX = HalfX;        O.HalfY = HalfY;        
        O.CenterX = CenterX;    O.CenterY = CenterY;
        O.HalfXMag = HalfXMag;  O.HalfYMag = HalfYMag;
        O.Magnification = Magnification;
        LookVector.copyTo(O.LookVector);
        POS.copyTo(O.POS);
        O.LineShape = new Shape3DM(10);
        O.Painter = Painter ;
        return O;
    }
    public void setGraphics(Graphics2D G){
        Painter = G;
    }
    public void copy(ObserverM O){
        O.PX = PX;          O.PY = PY;
        O.ScreenX = ScreenX;    O.ScreenY = ScreenY;
        O.HalfX = HalfX;        O.HalfY = HalfY;        
        O.CenterX = CenterX;    O.CenterY = CenterY;
        O.HalfXMag = HalfXMag;  O.HalfYMag = HalfYMag;
        O.Magnification = Magnification;
        POS.copyTo(O.POS);
        LookVector.copyTo(O.LookVector);
        Painter = O.Painter;
    }
    public void copyStatsTo(ObserverM O){
        LookVector.copyTo(O.LookVector);
        POS.copyTo(O.POS);
        Rotation.copyTo(O.Rotation);
    }
    Vector<Point3DM> PA = new Vector<Point3DM>();
    Vector<Point3DM> PB = new Vector<Point3DM>();
    Comparator<Printable3DM> DepthSort = new Comparator<Printable3DM>(){
    	/*public int compare(Printable3DM A, Printable3DM B){
	    	return Double.compare(A.Center.distanceTo(POS), B.Center.distanceTo(POS));
    	}*/
    	
    	public int compare(Printable3DM A, Printable3DM B){
    		if(A == B)return 0;
    //		B.getRepresentativePlane(PB);
	//		A.getRepresentativePlane(PA);
    //		Point3DM NA = Utils3DM.getNormal(PA.get(0), PA.get(1), PA.get(2));
   // 		Point3DM NB = Utils3DM.getNormal(PB.get(0), PB.get(1), PB.get(2));
    //		NA.normalize();
    	//	NB.normalize();
    	//	if(false && A.Center.distanceTo(B.Center) > A.MaxDistance + B.MaxDistance){ //if they are not entangled, return the closer one
    			return Double.compare(A.Center.distanceTo(POS), B.Center.distanceTo(POS));	
    	//	}else{ //if they are entangled
    			//check B with respect to A
    		/*	{	
    				double DEye = POS.subtract(PA.get(0)).dot(NA);
    			
	    			double Max = Integer.MIN_VALUE;
	    			double Min = Integer.MAX_VALUE;
	    			for(int i = 0; i < PB.size(); i++){
	    				double D = PB.get(i).subtract(PA.get(0)).dot(NA);
	    				if(D*D < 0.1)D = 0;
	    				Max = Math.max(Max, D);
	    				Min = Math.min(Min, D);
	    			}
	    			if(Min == Integer.MAX_VALUE){throw new IllegalArgumentException();}
	    			
	    			if(DEye * Min >= 0 && DEye * Max >= 0){ //Eye on same side of A as nearest and furthest points on B
	    				return 1; //B closer than A;
	    			}else if(DEye * Min <= 0 && DEye * Max <= 0){ //Eye on opposite side of A as nearest and furthest points on B
	    				return -1; //A closer than B;
	    			}
    			}
    			//check A with respect to B
    			{	
    				double DEye = POS.subtract(PB.get(0)).dot(NB);
    				
	    			double Max = Integer.MIN_VALUE;
	    			double Min = Integer.MAX_VALUE;
	    			for(int i = 0; i < PA.size(); i++){
	    				double D = PA.get(i).subtract(PB.get(0)).dot(NB);
	    				if(D*D < 0.1)D = 0;
	    				Max = Math.max(Max, D);
	    				Min = Math.min(Min, D);
	    			}
	    			if(Min == Integer.MAX_VALUE){throw new IllegalArgumentException();}
	    			if(DEye * Min >= 0 && DEye * Max >= 0){ //Eye on same side of B as nearest and furthest points on A
	    				return -1; //A closer than B;
	    			}else if(DEye * Min <= 0 && DEye * Max <= 0){ //Eye on opposite side of B as nearest and furthest points on A
	    				return 1; //B closer than A;
	    			}
    			}
    			
    			//if still cannot decide fall back on distance
    			//return Double.compare(A.Center.distanceTo(POS), B.Center.distanceTo(POS));
    			return 0;
    			*/
    		
    	//	}
    	}
    	public boolean equals(Printable3DM A, Printable3DM B){
    		throw new IllegalArgumentException();
    		//return compare(A, B) == 0;	
    	}
	};

    public void printAll(){

        Collections.sort(PrintingShapes, DepthSort);
        //Collections.sort(PrintingShapes, DepthSort);

        for(int i = PrintingShapes.size()-1; i >= 0; i--){
            PrintingShapes.get(i).printFinish(this);
        }

        PrintingShapes.clear();
    }
    public void print(Point3DM[] ActualPoints, Color Tint){
        for(int i = 0; i < ActualPoints.length; i++){
            ActualPoints[i].copyTo(LineShape.ActualPoints[i]);

        }
        LineShape.Tint = Tint;
        LineShape.PointCount = ActualPoints.length;
        printLineShape();

    }
    public void print(Vector<Point3DM> ActualPoints, Color Tint){
        for(int i = 0; i < ActualPoints.size(); i++){
            ActualPoints.get(i).copyTo(LineShape.ActualPoints[i]);

        }
        LineShape.Tint = Tint;
        LineShape.PointCount = ActualPoints.size();;
        printLineShape();
    }
    public void printLineShape(){
      
        LineShape.ActualPoints[0].LinkIndex = new int[]{};
        for(int i = 1; i < LineShape.PointCount; i++){
            LineShape.ActualPoints[i].LinkIndex = new int[]{i - 1};
        }
        LineShape.MaxDistance = Double.POSITIVE_INFINITY;
        LineShape.Points[0].LinkIndex = new int[]{};
        
        LineShape.printNow(Painter, this);
    }
    public void setArea(int bX, int bY, int eX, int eY, double M){
        PX = bX;
        PY = bY;
        ScreenY = eY - bY;
        ScreenX = eX - bX;
        CenterX = (bX + eX) / 2;
        CenterY = (bY + eY) / 2;
        HalfX = ScreenX / 2;
        HalfY = ScreenY / 2;
        Magnification = M;
        HalfXMag = (int)(HalfX * Magnification);
        HalfYMag = (int)(HalfY * Magnification);
        PixelSize = 0.5f / HalfYMag;
    }
    public void setMag(double M){
        Magnification = M;
        HalfXMag = (int)(HalfX * Magnification);
        HalfYMag = (int)(HalfY * Magnification);
        PixelSize = 0.5f / HalfYMag;
    }
    public ObserverM(double tX, double tY, double tZ, Rotation3DM tR, Graphics2D G){

        POS = new Point3DM(tX, tY, tZ);
        Painter = G;
        Rotation = tR;
        //Rotation.inverse(InverseRotation);
    }
    public final Point3DM getScreenVector(int PX, int PY){
        Point3DM NewVector = new Point3DM(0, 0, 0);
        NewVector.POSX = 1;
        NewVector.POSY = (PX - CenterX) * 1.0 / HalfYMag;
        NewVector.POSZ = -(PY - CenterY) * 1.0 / HalfYMag;
        NewVector.normalize();
        Rotation.transform(NewVector, NewVector);
        return NewVector;
    }
    public final void setLookVector(){
        Rotation.getOriginalAxis('x', LookVector);
    }
    public void toBytes(DataOutput Output, Vector<Shape3DM> Shapes) throws IOException{
        POS.toBareBytes(Output); //1
        Rotation.toBytes(Output); //2
        for(int i = Shapes.size() - 1; i >= 0; i--){
            if(Shapes.get(i).checkForBehind(this)){
                Shapes.removeElementAt(i);
            }
        }
        Output.writeInt(Shapes.size()); //3
        for(int i = 0; i < Shapes.size(); i++){ //4
            Shapes.get(i).toBytes(Output);
        }

        
    }
    public void fromBytes(DataInput Input, Vector<Shape3DM> Shapes) throws IOException{
        POS.fromBareBytes(Input); //1
        Rotation.fromBytes(Input); //2
        setLookVector();
        int ShapeCount = Input.readInt(); //3
        for(int i = 0; i < ShapeCount; i++){ //4
            if(i >= Shapes.size()){
                Shapes.add(new Shape3DM());
            }
            Shapes.get(i).fromBytes(Input);
        }
        
        
    }


}