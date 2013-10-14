import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

class PointVectorM extends Vector{
    public Point3DM point3DM(int INTA){   
        return (Point3DM)elementAt(INTA);
    }
}
class ShapeVectorM extends Vector{
    public Shape3DM shape3DM(int INTA){
        return (Shape3DM)elementAt(INTA);
    }
}
class Utils3DM{
        static final Point3DM Origin = new Point3DM(0, 0, 0);
        static final Point3DM Vectori = new Point3DM(1, 0, 0);
        static final Point3DM Vectorj = new Point3DM(0, 1, 0);
        static final Point3DM Vectork = new Point3DM(0, 0, 1);
        static final Point3DM TempA = new Point3DM(0, 0, 0);
        static final Point3DM TempB = new Point3DM(0, 0, 0);
        static final Point3DM TempC = new Point3DM(0, 0, 0);
        static final Point3DM TempD = new Point3DM(0, 0, 0);
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
        
        public static Point3DM lineSphereCollision(Point3DM LineA, Point3DM LineB, Point3DM SphereCenter, double Radius){
                Point3DM Difference = TempA;
                Point3DM LineUnits = TempB;
                Point3DM Temp = TempC;
                Point3DM SurfaceImage = TempD;
                
                LineA.subtract(SphereCenter, Difference);
                
                LineA.subtract(LineB, LineUnits);
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
            switch(Switch){
                case 'g':{ //Grid 
                    PointVectorM GPoints = new PointVectorM();
                    double Max = Math.min(ArgA, ArgB);
                    
                    for(int i = -(int)(ArgA / 2); i <= ArgA / 2; i++){
                        Point3DM PointA = new Point3DM(CurrentShape, i * ArgC, -ArgB * ArgC * 3 / 2, 0);
                        Point3DM PointB = new Point3DM(CurrentShape, i * ArgC, ArgB * ArgC * 3 / 2, 0);
                        PointA.Links.add(PointB);
                        //PointB.Links.add(PointA);
                        GPoints.add(PointA);
                        GPoints.add(PointB);
                    }
            
                    for(int i = -(int)(ArgB / 2); i <= ArgB / 2; i++){
                        Point3DM PointA = new Point3DM(CurrentShape, -ArgA * ArgC * 3 / 2, i * ArgC, 0);
                        Point3DM PointB = new Point3DM(CurrentShape, ArgA * ArgC * 3 / 2, i * ArgC, 0);
                        PointA.Links.add(PointB);
                        //PointB.Links.add(PointA);
                        GPoints.add(PointA);
                        GPoints.add(PointB);            
                    }
                    for(int i = (int)(Max / 2); i <= Max * 3 / 2; i++){
                        int j = (int)(2 * Max - i);
                        Point3DM PointA = new Point3DM(CurrentShape, j * ArgC, i * ArgC, 0);
                        Point3DM PointB = new Point3DM(CurrentShape, -j * ArgC, i * ArgC, 0);
                        Point3DM PointC = new Point3DM(CurrentShape, -j * ArgC, -i * ArgC, 0);
                        Point3DM PointD = new Point3DM(CurrentShape, j * ArgC, -i * ArgC, 0);
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
                    PointVectorM OPoints = new PointVectorM();
                    Point3DM PointF = new Point3DM(CurrentShape, ArgA, 0, 0);
                    Point3DM PointB = new Point3DM(CurrentShape, -ArgA, 0, 0);
                    Point3DM PointL = new Point3DM(CurrentShape, 0, ArgB, 0);
                    Point3DM PointR = new Point3DM(CurrentShape, 0, -ArgB, 0);
                    Point3DM PointU = new Point3DM(CurrentShape, 0, 0, ArgC);
                    Point3DM PointD = new Point3DM(CurrentShape, 0, 0, -ArgC);
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
                    CurrentShape.BasePoints.addAll(OPoints);
                    }break;
                case 'c':{ //Cuboid Length/Width/Height
                    PointVectorM CPoints = new PointVectorM();
                    for(double i = -ArgA/2; i <= ArgA/2; i+=ArgA){
                        for(double j = -ArgB/2; j <= ArgB/2; j+=ArgB){
                            for(double k = -ArgC/2; k <= ArgC/2; k+=ArgC){
                                CPoints.add(new Point3DM(CurrentShape, (double)i, j, k));
                            }
                        }
                    }
                    for(int i = 0; i < CPoints.size(); i++){
                        for(int j = i; j < CPoints.size(); j++){
                            int INTA = 0;
                            if(CPoints.point3DM(i).POSX == CPoints.point3DM(j).POSX)INTA++;
                            if(CPoints.point3DM(i).POSY == CPoints.point3DM(j).POSY)INTA++;
                            if(CPoints.point3DM(i).POSZ == CPoints.point3DM(j).POSZ)INTA++;        
                            if(INTA == 2){
                                try{
                                    CPoints.point3DM(i).Links.add(CPoints.point3DM(j));
                                        //CPoints.point3DM(j).Links.add(CPoints.point3DM(i));
                               }catch(Exception e){}
                            }
                        }    
                    }
                    CurrentShape.BasePoints.addAll(CPoints);
                    
                    }break;
                    
                case 't':{ //Triangular Prism Length/Width/Height
                    PointVectorM TPoints = new PointVectorM();
                    for(int i = -1; i <= 1; i++, i++){
                        Point3DM PointA = new Point3DM(CurrentShape, ArgA / 2 * i, -ArgB / 2, 0);
                        Point3DM PointB = new Point3DM(CurrentShape, ArgA / 2 * i, ArgB / 2, 0);
                        Point3DM PointC = new Point3DM(CurrentShape, ArgA / 2 * i, 0, ArgC);
                        PointA.Links.add(PointB);
                        PointB.Links.add(PointC);
                        PointC.Links.add(PointA);
                        TPoints.add(PointA);
                        TPoints.add(PointB);
                        TPoints.add(PointC);
                    }
                    for(int i = 0; i < 3; i++){
                        TPoints.point3DM(i).Links.add(TPoints.point3DM(i + 3));
                    }
                
                    CurrentShape.BasePoints.addAll(TPoints);
                    }break;
                case 's':{//Square Pyramid BaseX/BaseY/Height
                    PointVectorM SPoints = new PointVectorM();
                    Point3DM PointA = new Point3DM(CurrentShape, 0, 0, ArgC);
                    Point3DM PointB = new Point3DM(CurrentShape, ArgB/2, ArgC/2, 0);
                    Point3DM PointC = new Point3DM(CurrentShape, -ArgB/2, ArgC/2, 0);
                    Point3DM PointD = new Point3DM(CurrentShape, -ArgB/2, -ArgC/2, 0);
                    Point3DM PointE = new Point3DM(CurrentShape, ArgB/2, -ArgC/2, 0);
                    
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
                    
                    
                }
                case 'p':{ //Tetrahedron SizeOfBase/Height/nil
                    PointVectorM PPoints = new PointVectorM();
                    Point3DM PointA = new Point3DM(CurrentShape, 0, 			1 * ArgA / Math.sqrt(3), 	0);
                    Point3DM PointB = new Point3DM(CurrentShape, -0.5 * ArgA, 		-0.5 * ArgA / Math.sqrt(3),	0);
                    Point3DM PointC = new Point3DM(CurrentShape, 0.5 * ArgA, 		-0.5 * ArgA / Math.sqrt(3), 	0);
                    Point3DM PointD = new Point3DM(CurrentShape, 0, 			0, 				Math.sqrt(11.0/12) * ArgA * ArgB);
                    
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
                }break;
                case 'd':{ //Dot nil/nil/nil
                    PointVectorM PPoints = new PointVectorM();
                    Point3DM PointA = new Point3DM(CurrentShape, 0, 0, 0);
                    PointA.Links.add(PointA);
                    CurrentShape.BasePoints.add(PointA);
                
                }break;
                case 'l':{ //Line Length/nil/nil
                    PointVectorM PPoints = new PointVectorM();
                    Point3DM PointA = new Point3DM(CurrentShape, 0.5 * ArgA, 0, 0);
                    Point3DM PointB = new Point3DM(CurrentShape, -0.5 * ArgA, 0, 0);
                    PointA.Links.add(PointB);
                    CurrentShape.BasePoints.add(PointA);
                    CurrentShape.BasePoints.add(PointB);
                }
                case 'h':{ //Chain Count/nil/nil
                    PointVectorM PPoints = new PointVectorM();
                    for(int i = 0; i < ArgA; i++){
                        PPoints.add(new Point3DM(CurrentShape, 0, 0, 0));
                        CurrentShape.BasePoints.add(PPoints.point3DM(i));
                    }
                    for(int i = 0; i < ArgA - 1; i++){
                        PPoints.point3DM(i).Links.add(PPoints.point3DM(i + 1));
                        
                    }
                }
            }
            CurrentShape.finalizeLinks();
 
        }
        public static void center(Shape3DM Form, char Switch){
            
            double[] Values = new double[Form.Points.length];
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
                            double Distance = Math.sqrt(D.POSX * D.POSX + D.POSY * D.POSY + D.POSZ * D.POSZ);
                            TotalLength += Distance;
                            Center.add(D.multiply(Distance), Center);
                        }
                    }
                    Center.divide(TotalLength, Center);
                    /*
                     
                    for(int i = 0; i < Form.Points.length; i++){
                        for(int j = 0; j < Form.Points[i].LinkIndex.length; j++){
                            Point3DM Bisector = new Point3DM(Form.Points[i].POSX + Form.Points[Form.Points[i].LinkIndex[j]].POSX / 2, Form.Points[i].POSY + Form.Points[Form.Points[i].LinkIndex[j]].POSY / 2, Form.Points[i].POSZ + Form.Points[Form.Points[i].LinkIndex[j]].POSZ/ 2);
                            double Distance = Form.Points[i].distanceTo(Form.Points[Form.Points[i].LinkIndex[j]]);
                            Center.moveVector(Bisector.POSX * Distance, Bisector.POSY * Distance, Bisector.POSZ * Distance, Center);
                            TotalLength += Distance;
                        }
                    }*/
                    break;
            }
            
            Center.divide(Form.Points.length, Center);
            
            for(int i = 0; i < Form.Points.length; i++){
                Form.Points[i].subtract(Center, Form.Points[i]);
            }
        }
    }
   
class Sprite3D{
    Shape Image;
    Shape FinalImage;
    AffineTransform Transformer;
    Point3DM ActualCenter;
    Point3DM TransformedCenter;
    Point2D.Double ScreenCenter;
    Color Tint;
    
    public Sprite3D(Shape I, Point3DM Center, Color T){
        Image = I;
        ActualCenter = Center.copyCoords();
        TransformedCenter = ActualCenter.copyCoords();
        ScreenCenter = new Point2D.Double();
        Transformer = new AffineTransform();
        Tint = T;
    }
    public void setLocation(Point3DM Target){
        Target.copyCoords(ActualCenter);
    }
    public void center(ObserverM View){
        ActualCenter.center(View, TransformedCenter);
        TransformedCenter.toScreen(View, ScreenCenter);
      
    }
    public boolean checkForBehind(ObserverM View){
        ActualCenter.subtract(View.POS, TransformedCenter);
        return (TransformedCenter.dotProduct(View.LookVector) < 0);
        
    }
    public void setFinalImage(double Scale, Point2D.Double Position){
        
        Transformer.setToTranslation(Position.x, Position.y);
        

        Transformer.scale(Scale, Scale);

        FinalImage = Transformer.createTransformedShape(Image);
        
    }
    public void print(Graphics2D Painter, ObserverM View){
        if(checkForBehind(View)){
            return;
        }
        center(View);
      
        double Scale = View.HalfYMag / View.LookVector.dotProduct(ActualCenter.subtract(View.POS));
        
        setFinalImage(Scale, ScreenCenter);
        Painter.setColor(Tint);
        Painter.draw(FinalImage);
    }
     
}   
class Rotation3D{
    double[][] Matrix = new double[3][3];
    double[][] TempMatrix = new double[3][3];
    Point3DM TempPoint = Utils3DM.Origin.copyCoords();
    
   
    public Rotation3D(String Temp){
        
    }
    public Rotation3D(){
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
    public final void createFromPoints(Point3DM A, Point3DM B, Point3DM C){
        Matrix[0][0] = A.POSX; Matrix[0][1] = A.POSY; Matrix[0][2] = A.POSZ;
        Matrix[1][0] = B.POSX; Matrix[1][1] = B.POSY; Matrix[1][2] = B.POSZ;
        Matrix[2][0] = C.POSX; Matrix[2][1] = C.POSY; Matrix[2][2] = C.POSZ;
  
    }
    public final void inverse(Rotation3D Target){
        
    
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
    public final Rotation3D copy(){
        Rotation3D Target = new Rotation3D();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Target.Matrix[i][j] = Matrix[i][j];
            }
        }
        return Target;
    }
    public final void copy(Rotation3D Target){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Target.Matrix[i][j] = Matrix[i][j];
            }
        }
    }
    public final void inverseTransform(Point3DM Start, Point3DM End){
        TempPoint.POSX = Matrix[0][0] * Start.POSX + Matrix[1][0] * Start.POSY + Matrix[2][0] * Start.POSZ; 
        TempPoint.POSY = Matrix[0][1] * Start.POSX + Matrix[1][1] * Start.POSY + Matrix[2][1] * Start.POSZ;
        TempPoint.POSZ = Matrix[0][2] * Start.POSX + Matrix[1][2] * Start.POSY + Matrix[2][2] * Start.POSZ;
        End.POSX = TempPoint.POSX;
        End.POSY = TempPoint.POSY;
        End.POSZ = TempPoint.POSZ;
    }
    public final void transform(Point3DM Start, Point3DM End){
        TempPoint.POSX = Matrix[0][0] * Start.POSX + Matrix[0][1] * Start.POSY + Matrix[0][2] * Start.POSZ; 
        TempPoint.POSY = Matrix[1][0] * Start.POSX + Matrix[1][1] * Start.POSY + Matrix[1][2] * Start.POSZ;
        TempPoint.POSZ = Matrix[2][0] * Start.POSX + Matrix[2][1] * Start.POSY + Matrix[2][2] * Start.POSZ;
        End.POSX = TempPoint.POSX;
        End.POSY = TempPoint.POSY;
        End.POSZ = TempPoint.POSZ;
    }  
    static Point3DM M = new Point3DM(0, 0, 0);
    static Point3DM T = new Point3DM(0, 0, 0);
    static Point3DM Z = new Point3DM(0, 0, 0);
    static Point3DM Y = new Point3DM(0, 0, 0);
    static Rotation3D R = new Rotation3D();
    static Rotation3D S = new Rotation3D();
    static Rotation3D Ri = new Rotation3D();
    
    public static void getRotationBetween(Point3DM m, Point3DM t, double MaxRadians, Rotation3D Target){
        m.copyCoords(M);
        M.normalize();
        t.copyCoords(T);
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
        Rotation3D.multiply(R, Target, Target);
        Rotation3D.multiply(S, Target, Target);
        Rotation3D.multiply(Ri, Target, Target);
            
     
    }
    
    public static void multiply(Rotation3D a, Rotation3D b, Rotation3D End){
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
    
    class Shape3DM{
        
        

        PointVectorM BasePoints = new PointVectorM();
        Point3DM[] Points; // Base Points before rotation and translation; Points relative to the object
        Point3DM[] ActualPoints; //Actual Points after rotation and translation; Points in 3D Space
        Point3DM[] TransformedPoints; //Imaginary Points centered on an arbitrary Viewpoint; Points relative to Viewer
        Point2D.Double[] FlatPoints; //Imaginary Points flattened onto screen of Viewer
        boolean Static; //Whether or not objects rotation and position moves; Affects whether or not ActualPoints are re-computed
        
        int PointCount;
        
        Color Tint;
        Point3DM Center;
        Point3DM ObserverVector = new Point3DM(0, 0, 0);
        double ArgA;
        double ArgB;
        double ArgC;
        double MaxDistance;
        
        Rotation3D Rotation = new Rotation3D();
        boolean Visible = true;
        
        public final boolean checkForBehind(ObserverM Viewer){
            Center.subtract(Viewer.POS, ObserverVector);
            return (ObserverVector.dotProduct(Viewer.LookVector) + MaxDistance < 0);
            
        }
        
        public final Shape3DM copy(){
            Shape3DM Target = new Shape3DM();
            Target.Points = new Point3DM[Points.length];
            Target.ActualPoints = new Point3DM[Points.length];
            Target.TransformedPoints = new Point3DM[Points.length];
            Target.FlatPoints = new Point2D.Double[FlatPoints.length];
            for(int i = 0; i < Points.length; i++){
                Target.BasePoints.add(BasePoints.point3DM(i).copy());
                Target.Points[i] = Points[i].copy();
                Target.ActualPoints[i] = ActualPoints[i].copy();
                Target.TransformedPoints[i] = TransformedPoints[i].copy();
                Target.FlatPoints[i] = new Point2D.Double();
            }
            
            Target.Center = Center.copyCoords();
            Target.Static = Static;
            Target.Tint = Tint;

            Target.ArgA = ArgA;
            Target.ArgB = ArgB;
            Target.ArgC = ArgC;
            Target.MaxDistance = MaxDistance;
            Target.Rotation = Rotation.copy();
            Target.PointCount = PointCount;
            return Target;
        }
        public Shape3DM(){
            Center = new Point3DM(0, 0, 0);
            Tint = Color.white;
            Static = true;
            
        }
        public Shape3DM(boolean tStatic, Color tTint, double tPOSX, double tPOSY, double tPOSZ){
            Static = tStatic;
            Tint = tTint;
            Center = new Point3DM(tPOSX, tPOSY, tPOSZ);
            
        }


        public final void center(ObserverM Viewer){
            
            for(int i = 0; i < PointCount; i++){
                ActualPoints[i].center(Viewer, TransformedPoints[i]);
            }
              
        }
       
        public final void calcPoints(){
            
            for(int i = 0; i < PointCount; i++){
                
                 Points[i].rotate(Rotation, ActualPoints[i]);
                
                 ActualPoints[i].add(Center, ActualPoints[i]);
                
            }
        }
        public final void linkPoints(Graphics2D Painter, int A, int B){
            double C = TransformedPoints[A].POSX;
            double D = TransformedPoints[B].POSX;
            
            if(C < 0 && D < 0){
                return;
            }
            if(C > 0 && D > 0){
                Painter.drawLine((int)FlatPoints[A].x, (int)FlatPoints[A].y, (int)FlatPoints[B].x, (int)FlatPoints[B].y);            
                return;
            }
            if(C > 0 && D < 0){ 
                Painter.drawLine((int)FlatPoints[A].x, (int)FlatPoints[A].y, (int)(FlatPoints[A].x - (FlatPoints[B].x - FlatPoints[A].x) * 1024), (int)(FlatPoints[A].y - (FlatPoints[B].y - FlatPoints[A].y) * 1024));
                return;
            }
            if(C < 0 && D > 0){
                Painter.drawLine((int)FlatPoints[B].x, (int)FlatPoints[B].y, (int)(FlatPoints[B].x - (FlatPoints[A].x - FlatPoints[B].x) * 1024), (int)(FlatPoints[B].y - (FlatPoints[A].y - FlatPoints[B].y) * 1024));
                return;
            }
            
        }
        public final void flatten(ObserverM Viewer){
            
            for(int i = 0; i < PointCount; i++){
                 TransformedPoints[i].toScreen(Viewer, FlatPoints[i]);
            }
        }
        public final void print(Graphics2D Painter, ObserverM Viewer){
            if(!Visible){return;}
            if(checkForBehind(Viewer)){return;}
          
            center(Viewer);
            flatten(Viewer);
            Painter.setColor(Tint);    
            Painter.setClip(Viewer.POSX, Viewer.POSY, Viewer.ScreenX, Viewer.ScreenY);
            for(int i = 0; i < PointCount; i++){
                
                for(int j = 0; j < Points[i].LinkIndex.length; j++){
                    
                    linkPoints(Painter, i, Points[i].LinkIndex[j]);
                }
            }
            Painter.setClip(null);
        }
        public final void print(Graphics2D Painter, ObserverM Viewer, Color PrintColor){
            
            if(!Visible){return;}
            if(checkForBehind(Viewer)){return;}
           
            center(Viewer);
            flatten(Viewer);
            Painter.setColor(PrintColor);    
            Painter.setClip(Viewer.POSX, Viewer.POSY, Viewer.ScreenX, Viewer.ScreenY);
            for(int i = 0; i < PointCount; i++){
                
                for(int j = 0; j < Points[i].LinkIndex.length; j++){
                    
                    linkPoints(Painter, i, Points[i].LinkIndex[j]);
                }
            }
            Painter.setClip(null);
        }
        public final void finalizeLinks(){
            for(int i = 0; i < BasePoints.size(); i++){
                BasePoints.point3DM(i).finalizeLinks();
            }
            for(int i = 0; i < BasePoints.size(); i++){
                MaxDistance = Math.max(BasePoints.point3DM(i).distanceTo(Utils3DM.Origin), MaxDistance);
            }
            Points = new Point3DM[BasePoints.size()];
            ActualPoints = new Point3DM[BasePoints.size()];
            TransformedPoints = new Point3DM[BasePoints.size()];
            FlatPoints = new Point2D.Double[BasePoints.size()];
            for(int i = 0; i < BasePoints.size(); i++){
                
                Points[i] = BasePoints.point3DM(i);
                ActualPoints[i] = new Point3DM(0, 0, 0);
                TransformedPoints[i] = new Point3DM(0, 0, 0);
                FlatPoints[i] = new Point2D.Double();
            }
            PointCount = BasePoints.size();
            calcPoints();
        }
    }    
    class Point3DM{
        double POSX;
        double POSY;
        double POSZ;
        PointVectorM Links;

        Shape3DM Base;
        int[] LinkIndex;
        public final Point3DM copy(){
            Point3DM Point = new Point3DM(Base, POSX, POSY, POSZ);
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
        public final Point3DM copyCoords(){
            Point3DM Point = new Point3DM(POSX, POSY, POSZ);
            return Point;
        }
        public final void copyCoords(Point3DM Target){
            Target.POSX = POSX;
            Target.POSY = POSY;
            Target.POSZ = POSZ;
        }
        public final void copy(Point3DM Target){
            Target.POSX = POSX;
            Target.POSY = POSY;
            Target.POSZ = POSZ;
            Target.LinkIndex = new int[LinkIndex.length];
            for(int i = 0; i < LinkIndex.length; i++){
                Target.LinkIndex[i] = LinkIndex[i];
            }
            
        }
        public Point3DM(double tPOSX, double tPOSY, double tPOSZ){
            POSX = tPOSX;
            POSY = tPOSY;
            POSZ = tPOSZ;
        }
        public Point3DM(Shape3DM tBase, double tPOSX, double tPOSY, double tPOSZ){
            Links = new PointVectorM();
            Base = tBase;
            POSX = tPOSX;
            POSY = tPOSY;
            POSZ = tPOSZ;
        }
        public final void normalize(){
            double L = Math.sqrt(POSX * POSX + POSY * POSY + POSZ * POSZ);
            POSX = POSX / L;
            POSY = POSY / L;
            POSZ = POSZ / L;
        }
        
        public final Point3DM add(Point3DM Target){
            return new Point3DM(POSX + Target.POSX, POSY + Target.POSY, POSZ + Target.POSZ);
        }
        public final Point3DM add(Point3DM Target, Point3DM Result){
            Result.POSX = POSX + Target.POSX;
            Result.POSY = POSY + Target.POSY;
            Result.POSZ = POSZ + Target.POSZ;
            return Result;
        }
        public final Point3DM subtract(Point3DM Target){
            return new Point3DM(POSX - Target.POSX, POSY - Target.POSY, POSZ - Target.POSZ);
        }
        public final Point3DM subtract(Point3DM Target, Point3DM Result){
            Result.POSX = POSX - Target.POSX;
            Result.POSY = POSY - Target.POSY;
            Result.POSZ = POSZ - Target.POSZ;
            return Result;
        }
        public final Point3DM multiply(double Multiple, Point3DM Target){
            Target.setLocation(POSX * Multiple, POSY * Multiple, POSZ * Multiple);
            return Target;
        }
        public final Point3DM divide(double Multiple, Point3DM Target){
            Target.setLocation(POSX / Multiple, POSY / Multiple, POSZ / Multiple);
            return Target;
        }
        public final Point3DM multiply(double Multiple){
            return new Point3DM(POSX * Multiple, POSY * Multiple, POSZ * Multiple);
        }
        public final Point3DM divide(double Multiple){
            return new Point3DM(POSX / Multiple, POSY / Multiple, POSZ / Multiple);
        }
        public final double dotProduct(Point3DM Other){
            return POSX * Other.POSX + POSY * Other.POSY + POSZ * Other.POSZ;
        }
        public final Point3DM crossProduct(Point3DM Other){
            return new Point3DM(POSY * Other.POSZ - POSZ * Other.POSY, POSZ * Other.POSX - POSX * Other.POSZ, POSX * Other.POSY - POSY * Other.POSX);
        }
        public final void crossProduct(Point3DM Other, Point3DM Target){
            Target.POSX = POSY * Other.POSZ - POSZ * Other.POSY;
            Target.POSY = POSZ * Other.POSX - POSX * Other.POSZ;
            Target.POSZ = POSX * Other.POSY - POSY * Other.POSX;
        }
        public final double distanceTo(Point3DM Target){
            return Math.sqrt((Target.POSX - POSX) * (Target.POSX - POSX) +(Target.POSY - POSY) * (Target.POSY - POSY) + (Target.POSZ - POSZ) * (Target.POSZ - POSZ));
        }
        public final double distanceToSquare(Point3DM Target){
            return (Target.POSX - POSX) * (Target.POSX - POSX) +(Target.POSY - POSY) * (Target.POSY - POSY) + (Target.POSZ - POSZ) * (Target.POSZ - POSZ);
        }
        public final double flatDistanceTo(Point3DM Target){
            return Math.sqrt((Target.POSX - POSX) * (Target.POSX - POSX) +(Target.POSY - POSY) * (Target.POSY - POSY));
        }
        public final double length(){
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
        public final void finalizeLinks(){
            
            LinkIndex = new int[Links.size()];
            for(int i = 0; i < Links.size(); i++){
                LinkIndex[i] = Base.BasePoints.indexOf(Links.point3DM(i));
            }
       
        }
       
        public final void rotate(Rotation3D R, Point3DM Point){
       
            R.transform(this, Point);
      
        }
        public final void rotate(Point3DM Center, Rotation3D R, Point3DM Point){
          
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
            Target.x = POSY * Temp + Viewer.CenterX;
            Target.y = -POSZ * Temp + Viewer.CenterY;
            
        }
        

        public String toString(){
            return "Point: " + "\t" + POSX + "\t" + POSY + "\t" + POSZ;
            
        }
       
    }
    class ObserverM{
        Point3DM POS;
        
        int POSX; //X of top left corner
        int POSY; //Y of top left corner
        int ScreenX; //Width of Screen
        int ScreenY; //Height of Screen
        int HalfX; //Half Width of Screen
        int HalfY; //Half Height of Screen
        int CenterX; //Center of Screen
        int CenterY; //Center of Screen
        double Magnification = 1.00; //Magnification
        int HalfXMag; //Half Width with Magnification
        int HalfYMag; //Half Height with Magnification
        double Zoom = 0;
        Point3DM LookVector = new Point3DM(0, 0, 0);
        Rotation3D Rotation = new Rotation3D();
     //   Rotation3D InverseRotation = new Rotation3D();
        public void setArea(int bX, int bY, int eX, int eY, double M){
            POSX = bX;
            POSY = bY;
            ScreenY = eY - bY;
            ScreenX = eX - bX;
            CenterX = (bX + eX) / 2;
            CenterY = (bY + eY) / 2;
            HalfX = ScreenX / 2;
            HalfY = ScreenY / 2;
            Magnification = M;
            HalfXMag = (int)(HalfX * Magnification);
            HalfYMag = (int)(HalfY * Magnification);
        }
        public ObserverM(double tX, double tY, double tZ, Rotation3D tR){
            POS = new Point3DM(tX, tY, tZ);
       
            Rotation = tR;
            //Rotation.inverse(InverseRotation);
        }
        
        public final void setLookVector(){
            Rotation.getOriginalAxis('x', LookVector);
        }
        
        
    }