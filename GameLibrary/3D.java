import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
class PointVector extends Vector{
    public Point3D point3D(int INTA){
        return (Point3D)elementAt(INTA);
    }     
    public Point2D.Double point2D(int INTA){
        return (Point2D.Double)elementAt(INTA);
    }
        
      
}
class ShapeVector extends Vector{
    public Shape3D shape3D(int INTA){
        return (Shape3D)elementAt(INTA);
    }
    
}
class Utils3D{
        static Point3D Origin = new Point3D(0, 0, 0);
        
        public static void setPoints(Shape3D CurrentShape, char Switch, double ArgA, double ArgB, double ArgC){
            CurrentShape.ArgA = ArgA;
            CurrentShape.ArgB = ArgB;
            CurrentShape.ArgC = ArgC;
            switch(Switch){
                case 'g': //Grid
                    PointVector GPoints = new PointVector();
                    double Max = Math.min(ArgA, ArgB);
                    
                    for(int i = -(int)(ArgA / 2); i <= ArgA / 2; i++){
                        Point3D PointA = new Point3D(CurrentShape, i * ArgC, -ArgB * ArgC * 3 / 2, 0);
                        Point3D PointB = new Point3D(CurrentShape, i * ArgC, ArgB * ArgC * 3 / 2, 0);
                        PointA.Links.add(PointB);
                        //PointB.Links.add(PointA);
                        GPoints.add(PointA);
                        GPoints.add(PointB);
                    }
            
                    for(int i = -(int)(ArgB / 2); i <= ArgB / 2; i++){
                        Point3D PointA = new Point3D(CurrentShape, -ArgA * ArgC * 3 / 2, i * ArgC, 0);
                        Point3D PointB = new Point3D(CurrentShape, ArgA * ArgC * 3 / 2, i * ArgC, 0);
                        PointA.Links.add(PointB);
                        //PointB.Links.add(PointA);
                        GPoints.add(PointA);
                        GPoints.add(PointB);            
                    }
                    for(int i = (int)(Max / 2); i <= Max * 3 / 2; i++){
                        int j = (int)(2 * Max - i);
                        Point3D PointA = new Point3D(CurrentShape, j * ArgC, i * ArgC, 0);
                        Point3D PointB = new Point3D(CurrentShape, -j * ArgC, i * ArgC, 0);
                        Point3D PointC = new Point3D(CurrentShape, -j * ArgC, -i * ArgC, 0);
                        Point3D PointD = new Point3D(CurrentShape, j * ArgC, -i * ArgC, 0);
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
                    break;
                    
                case 'c': //Cuboid
                    PointVector CPoints = new PointVector();
                    for(double i = -ArgA/2; i <= ArgA/2; i+=ArgA){
                        for(double j = -ArgB/2; j <= ArgB/2; j+=ArgB){
                            for(double k = -ArgC/2; k <= ArgC/2; k+=ArgC){
                                CPoints.add(new Point3D(CurrentShape, (double)i, j, k));
                            }
                        }
                    }
                    for(int i = 0; i < CPoints.size(); i++){
                        for(int j = i; j < CPoints.size(); j++){
                            int INTA = 0;
                            if(CPoints.point3D(i).POSX == CPoints.point3D(j).POSX)INTA++;
                            if(CPoints.point3D(i).POSY == CPoints.point3D(j).POSY)INTA++;
                            if(CPoints.point3D(i).POSZ == CPoints.point3D(j).POSZ)INTA++;        
                            if(INTA == 2){
                                try{
                                    CPoints.point3D(i).Links.add(CPoints.point3D(j));
                                   //     CPoints.point3D(j).Links.add(CPoints.point3D(i));
                               }catch(Exception e){}
                            }
                        }    
                    }
                    CurrentShape.BasePoints.addAll(CPoints);
                    
                    break;
                case 't': //Triangular Prism
                    PointVector TPoints = new PointVector();
                    for(int i = -1; i <= 1; i++, i++){
                        Point3D PointA = new Point3D(CurrentShape, ArgA / 2 * i, -ArgB / 2, 0);
                        Point3D PointB = new Point3D(CurrentShape, ArgA / 2 * i, ArgB / 2, 0);
                        Point3D PointC = new Point3D(CurrentShape, ArgA / 2 * i, 0, ArgC);
                        PointA.Links.add(PointB);
                        PointB.Links.add(PointC);
                        PointC.Links.add(PointA);
                        TPoints.add(PointA);
                        TPoints.add(PointB);
                        TPoints.add(PointC);
                    }
                    for(int i = 0; i < 3; i++){
                        TPoints.point3D(i).Links.add(TPoints.point3D(i + 3));
                    }
                
                    CurrentShape.BasePoints.addAll(TPoints);
                    break;
                case 'p': //Pyramid
                    PointVector PPoints = new PointVector();
                    Point3D PointA = new Point3D(CurrentShape, 0, 1 * ArgA / Math.sqrt(3), 0);
                    Point3D PointB = new Point3D(CurrentShape, -0.5 * ArgA, -0.5 * ArgA / Math.sqrt(3), 0);
                    Point3D PointC = new Point3D(CurrentShape, 0.5 * ArgA, -0.5 * ArgA / Math.sqrt(3), 0);
                    Point3D PointD = new Point3D(CurrentShape, 0, 0, Math.sqrt(2f / 3) * ArgA * ArgB);
                    
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
                    break;
            }
            CurrentShape.finalizeLinks();
 
        }
        public static void center(Shape3D Form, char Switch){
            
            double[] Values = new double[Form.Points.length];
            Point3D Center = new Point3D(0, 0, 0);
            double TotalLength = 0;
            switch(Switch){
                case 'p':
                    for(int i = 0; i < Form.Points.length; i++){
                        Center.moveVector(Form.Points[i].POSX, Form.Points[i].POSY, Form.Points[i].POSZ, Center);
                    }   
                    break;
                case 'l':
                    for(int i = 0; i < Form.Points.length; i++){
                        for(int j = 0; j < Form.Points[i].LinkIndex.length; j++){
                            Point3D Bisector = new Point3D(Form.Points[i].POSX + Form.Points[Form.Points[i].LinkIndex[j]].POSX / 2, Form.Points[i].POSY + Form.Points[Form.Points[i].LinkIndex[j]].POSY / 2, Form.Points[i].POSZ + Form.Points[Form.Points[i].LinkIndex[j]].POSZ/ 2);
                            double Distance = Form.Points[i].distanceTo(Form.Points[Form.Points[i].LinkIndex[j]]);
                            Center.moveVector(Bisector.POSX * Distance, Bisector.POSY * Distance, Bisector.POSZ * Distance, Center);
                            TotalLength += Distance;
                        }
                    }
                    break;
            }
            Center.POSX = Center.POSX / Form.Points.length;
            Center.POSY = Center.POSY / Form.Points.length;
            Center.POSZ = Center.POSZ / Form.Points.length;
            for(int i = 0; i < Form.Points.length; i++){
                Form.Points[i].moveVector(-Center.POSX, -Center.POSY, -Center.POSZ, Form.Points[i]);
            }
        }
    }
    class Shape3D{
        
        

        PointVector BasePoints = new PointVector();
        Point3D[] Points;
        Point3D[] ActualPoints;
        Point3D[] TransformedPoints;
        Point2D.Double[] FlatPoints;
        boolean Static;
        
        Color Tint;
        Point3D Center;
        Point3D ObserverVector = new Point3D(0, 0, 0);
        double HAngle;
        double VAngle;
        double RAngle;
        double ArgA;
        double ArgB;
        double ArgC;
        double MaxDistance;
        
        AffineTransform HRotation = new AffineTransform();
        AffineTransform VRotation = new AffineTransform();
        AffineTransform RRotation = new AffineTransform();
        AffineTransform IHRotation = new AffineTransform();
        AffineTransform IVRotation = new AffineTransform();
        AffineTransform IRRotation = new AffineTransform();
        public void move(double MoveAngleH, double MoveAngleV, double Magnitude){
            Center.POSZ = Center.POSZ + Magnitude * Math.sin(Math.toRadians(MoveAngleV));
            double HMagnitude = Magnitude * Math.cos(Math.toRadians(MoveAngleV));
            Center.POSX = Center.POSX + HMagnitude * Math.cos(Math.toRadians(MoveAngleH));
            Center.POSY = Center.POSY + HMagnitude * Math.sin(Math.toRadians(MoveAngleH));
        }
        public void moveTo(double tX, double tY, double tZ){
            Center.POSZ = tZ;
            Center.POSY = tY;
            Center.POSX = tX;
            
        }
        public boolean checkForBehind(Observer Viewer){
            Center.subtract(Viewer.POS, ObserverVector);
            if(ObserverVector.dotProduct(Viewer.LookVector) + MaxDistance < 0){
                
                return true;
            }else{
                return false;
            }
        }
        public void increment(char Switch, double Increment, AffineTransform Transform){
            switch(Switch){
                case 'h': HAngle = HAngle - Increment;
                          HRotation.concatenate(Transform);
                          IHRotation.concatenate(GUI.inverse(Transform));
                          break;
                case 'v': VAngle = VAngle - Increment;
                          VRotation.concatenate(Transform);
                          IVRotation.concatenate(GUI.inverse(Transform));
                          break;
                case 'r': RAngle = RAngle - Increment;
                          RRotation.concatenate(Transform);
                          IRRotation.concatenate(GUI.inverse(Transform));
                          break;
            }
        }
        
        final public boolean rectContains(Point3D Point, boolean CompareHeight){
            if(Point.distanceToSquare(Center) > MaxDistance * MaxDistance){
                return false;
            }
            Point3D TempPoint = new Point3D(0, 0, 0);
            Point.moveVector(-Center.POSX, -Center.POSY, -Center.POSZ, TempPoint);
            
            TempPoint.rotate(IHRotation, IVRotation, IRRotation, TempPoint);
            
            if(TempPoint.POSX <= Points[0].POSX){return false;}
            if(TempPoint.POSX >= Points[6].POSX){return false;}
            if(TempPoint.POSY <= Points[0].POSY){return false;}
            if(TempPoint.POSY >= Points[6].POSY){return false;}
            if(CompareHeight){
                if(TempPoint.POSZ >= Points[1].POSZ){return false;}
                if(TempPoint.POSZ <= Points[0].POSZ){return false;}
            }
            return true;
        }
        public Shape3D copy(){
            Shape3D Target = new Shape3D();
            Target.Points = new Point3D[Points.length];
            Target.ActualPoints = new Point3D[Points.length];
            Target.TransformedPoints = new Point3D[Points.length];
            Target.FlatPoints = new Point2D.Double[FlatPoints.length];
            for(int i = 0; i < Points.length; i++){
                Target.BasePoints.add(BasePoints.point3D(i).copy());
                Target.Points[i] = Points[i].copy();
                Target.ActualPoints[i] = ActualPoints[i].copy();
                Target.TransformedPoints[i] = TransformedPoints[i].copy();
                Target.FlatPoints[i] = new Point2D.Double();
            }
            Target.Center = Center.copy();
            Target.Static = Static;
            Target.Tint = Tint;
            Target.HAngle = HAngle;
            Target.VAngle = VAngle;
            Target.RAngle = RAngle;
            Target.ArgA = ArgA;
            Target.ArgB = ArgB;
            Target.ArgC = ArgC;
            Target.MaxDistance = MaxDistance;
            Target.HRotation = new AffineTransform(HRotation);
            Target.VRotation = new AffineTransform(VRotation);
            Target.RRotation = new AffineTransform(RRotation);
            Target.IHRotation = new AffineTransform(IHRotation);
            Target.IVRotation = new AffineTransform(IVRotation);
            Target.IRRotation = new AffineTransform(IRRotation);
            return Target;
        }
        public Shape3D(){
            
        }
        public Shape3D(boolean tStatic, Color tTint, double tPOSX, double tPOSY, double tPOSZ){
            Static = tStatic;
            Tint = tTint;
            Center = new Point3D(tPOSX, tPOSY, tPOSZ);
            
        }
        final public void setHAngle(double aH){
            if(HAngle != aH){
                HRotation = AffineTransform.getRotateInstance(Math.toRadians(aH));
                IHRotation = AffineTransform.getRotateInstance(Math.toRadians(-aH));
                HAngle = aH;
            }
            
        }
        final public void setVAngle(double aV){
            if(VAngle != aV){
                VRotation = AffineTransform.getRotateInstance(Math.toRadians(aV));
                IVRotation = AffineTransform.getRotateInstance(Math.toRadians(-aV));
                VAngle = aV;
            }
            
        }
        final public void setRAngle(double aR){
            if(RAngle != aR){
                RRotation = AffineTransform.getRotateInstance(Math.toRadians(aR));
                IRRotation = AffineTransform.getRotateInstance(Math.toRadians(-aR));
                RAngle = aR;
            }
            
            
        }
        final public void setAngles(double aH, double aV, double aR){
            if(HAngle != aH){
                HRotation = AffineTransform.getRotateInstance(Math.toRadians(aH));
                IHRotation = AffineTransform.getRotateInstance(Math.toRadians(-aH));
                HAngle = aH;
            }
            
            if(VAngle != aV){
                VRotation = AffineTransform.getRotateInstance(Math.toRadians(aV));
                IVRotation = AffineTransform.getRotateInstance(Math.toRadians(-aV));
                VAngle = aV;
            }
            
            if(RAngle != aR){
                RRotation = AffineTransform.getRotateInstance(Math.toRadians(aR));
                IRRotation = AffineTransform.getRotateInstance(Math.toRadians(-aR));
                RAngle = aR;
            }
            
        }
        
        final public void center(Observer Viewer){
            
            for(int i = 0; i < ActualPoints.length; i++){
                ActualPoints[i].center(Viewer, TransformedPoints[i]);
            }
              
        }
       
        final public void rotate(){
            
            for(int i = 0; i < Points.length; i++){
                //RotatedPoints.add(Points.point3D(i));
                 Points[i].rotate(HRotation, VRotation, RRotation, ActualPoints[i]);
                 ActualPoints[i].moveVector(Center.POSX, Center.POSY, Center.POSZ, ActualPoints[i]);
                
            }
        }
        final public void linkPoints(Graphics2D Painter, Observer Viewer, int A, int B){
            
            
            if(TransformedPoints[A].POSX < 0 && TransformedPoints[B].POSX < 0){
                return;
            }
            if(TransformedPoints[A].POSX > 0 && TransformedPoints[B].POSX > 0){
                Painter.drawLine((int)FlatPoints[A].x, (int)FlatPoints[A].y, (int)FlatPoints[B].x, (int)FlatPoints[B].y);            
                return;
            }
            if(TransformedPoints[A].POSX > 0 && TransformedPoints[B].POSX < 0){ 
                Painter.drawLine((int)FlatPoints[A].x, (int)FlatPoints[A].y, (int)(FlatPoints[A].x - (FlatPoints[B].x - FlatPoints[A].x) * 256), (int)(FlatPoints[A].y - (FlatPoints[B].y - FlatPoints[A].y) * 256));
                return;
            }
            if(TransformedPoints[A].POSX < 0 && TransformedPoints[B].POSX > 0){
                Painter.drawLine((int)FlatPoints[B].x, (int)FlatPoints[B].y, (int)(FlatPoints[B].x - (FlatPoints[A].x - FlatPoints[B].x) * 256), (int)(FlatPoints[B].y - (FlatPoints[A].y - FlatPoints[B].y) * 256));
                return;
            }
            
        }
        final public void flatten(Observer Viewer){
            
            for(int i = 0; i < TransformedPoints.length; i++){
                 TransformedPoints[i].toScreen(Viewer, FlatPoints[i]);
            }
        }
        public void print(Graphics2D Painter, Observer Viewer, Color PrintColor){
            if(checkForBehind(Viewer)){return;}
            if(!Static){
                rotate();
            }
            center(Viewer);
            flatten(Viewer);
            Painter.setColor(PrintColor);    
            
            for(int i = 0; i < TransformedPoints.length; i++){
                for(int j = 0; j < TransformedPoints[i].LinkIndex.length; j++){
                    linkPoints(Painter, Viewer, i, TransformedPoints[i].LinkIndex[j]);
                }
            }
        }
        public void finalizeLinks(){
            for(int i = 0; i < BasePoints.size(); i++){
                BasePoints.point3D(i).finalizeLinks();
            }
            for(int i = 0; i < BasePoints.size(); i++){
                MaxDistance = Math.max(BasePoints.point3D(i).distanceTo(Utils3D.Origin), MaxDistance);
            }
            Points = new Point3D[BasePoints.size()];
            ActualPoints = new Point3D[BasePoints.size()];
            TransformedPoints = new Point3D[BasePoints.size()];
            FlatPoints = new Point2D.Double[BasePoints.size()];
            for(int i = 0; i < BasePoints.size(); i++){
                Points[i] = BasePoints.point3D(i);
                ActualPoints[i] = new Point3D(0, 0, 0);
                TransformedPoints[i] = new Point3D(0, 0, 0);
                FlatPoints[i] = new Point2D.Double();
            }
            rotate();
        }
    }
    class Point3D{
        double POSX;
        double POSY;
        double POSZ;
        PointVector Links = new PointVector();
        Shape3D Base;
        int[] LinkIndex = new int[0];
        public Point3D copy(){
            Point3D Point = new Point3D(Base, POSX, POSY, POSZ);
            Point.LinkIndex = new int[LinkIndex.length];
            for(int i = 0; i < LinkIndex.length; i++){
                Point.LinkIndex[i] = LinkIndex[i];
            }
            return Point;
        }
        public Point3D(double tPOSX, double tPOSY, double tPOSZ){
            POSX = tPOSX;
            POSY = tPOSY;
            POSZ = tPOSZ;
        }
        public Point3D(Shape3D tBase, double tPOSX, double tPOSY, double tPOSZ){
            Base = tBase;
            POSX = tPOSX;
            POSY = tPOSY;
            POSZ = tPOSZ;
        }
        public double length(){
            return Math.sqrt(POSX * POSX + POSY * POSY + POSZ * POSZ);
        }
        public Point3D add(Point3D Target){
            return new Point3D(POSX + Target.POSX, POSY + Target.POSY, POSZ + Target.POSZ);
        }
        public Point3D add(Point3D Target, Point3D Result){
            Result.POSX = POSX + Target.POSX;
            Result.POSY = POSY + Target.POSY;
            Result.POSZ = POSZ + Target.POSZ;
            return Result;
        }
        public Point3D subtract(Point3D Target){
            return new Point3D(POSX - Target.POSX, POSY - Target.POSY, POSZ - Target.POSZ);
        }
        public Point3D subtract(Point3D Target, Point3D Result){
            Result.POSX = POSX - Target.POSX;
            Result.POSY = POSY - Target.POSY;
            Result.POSZ = POSZ - Target.POSZ;
            return Result;
        }
        
        public Point3D multiply(double Multiple){
            return new Point3D(POSX * Multiple, POSY * Multiple, POSZ * Multiple);
        }
        public Point3D divide(double Multiple){
            return new Point3D(POSX / Multiple, POSY / Multiple, POSZ / Multiple);
        }
        public double dotProduct(Point3D Other){
            return POSX * Other.POSX + POSY * Other.POSY + POSZ * Other.POSZ;
        }
        public Point3D crossProduct(Point3D Other){
            return new Point3D(POSY * Other.POSZ - POSZ * Other.POSY, POSZ * Other.POSX - POSX * Other.POSZ, POSX * Other.POSY - POSY * Other.POSX);
        }
        public void setLocation(double X, double Y, double Z){
            POSX = X;
            POSY = Y;
            POSZ = Z;
        }
        public void finalizeLinks(){
            
            LinkIndex = new int[Links.size()];
            for(int i = 0; i < Links.size(); i++){
                LinkIndex[i] = Base.BasePoints.indexOf(Links.point3D(i));
            }
          //  Links.clear();
        }
        final public void moveVector(double mX, double mY, double mZ, Point3D Target){
            Target.Base = Base;
            Target.POSX = POSX + mX;
            Target.POSY = POSY + mY;
            Target.POSZ = POSZ + mZ;
            Target.LinkIndex = LinkIndex;
            //return Point;
        }
        final public void moveDirection(double MoveAngleH, double MoveAngleV, double Magnitude, Point3D Target){
           
            Target.POSZ = POSZ + Magnitude * Math.sin(Math.toRadians(MoveAngleV));
            double HMagnitude = Magnitude * Math.cos(Math.toRadians(MoveAngleV));
            Target.POSX = POSX + HMagnitude * Math.cos(Math.toRadians(MoveAngleH));
            Target.POSY = POSY + HMagnitude * Math.sin(Math.toRadians(MoveAngleH));
            
            Target.Base = Base;
            Target.LinkIndex = LinkIndex;
            
        }
        final public void rotate(AffineTransform HRotate, AffineTransform VRotate, AffineTransform RRotate, Point3D Point){
            Point.Base = Base;
            Point2D.Double TempPoint = new Point2D.Double();
            
            
            TempPoint.x = POSY;
            TempPoint.y = POSZ;
            RRotate.transform(TempPoint, TempPoint);
            Point.POSY = TempPoint.x;
            Point.POSZ = TempPoint.y;
            
            TempPoint.x = POSX;
            TempPoint.y = Point.POSZ;
            VRotate.transform(TempPoint, TempPoint);
            Point.POSX = TempPoint.x;
            Point.POSZ = TempPoint.y;
            TempPoint.x = Point.POSX;
            TempPoint.y = Point.POSY;
            HRotate.transform(TempPoint, TempPoint);
            Point.POSX = TempPoint.x;
            Point.POSY = TempPoint.y;
            Point.LinkIndex = LinkIndex;
        }
        final public void rotate(Point3D Center, AffineTransform HRotate, AffineTransform VRotate, AffineTransform RRotate, Point3D Point){
            Point.Base = Base;
            Point.POSX = POSX - Center.POSX;
            Point.POSY = POSY - Center.POSY;
            Point.POSZ = POSZ - Center.POSZ;
            Point2D.Double TempPoint = new Point2D.Double();
            
            
            TempPoint.x = Point.POSY;
            TempPoint.y = Point.POSZ;
            RRotate.transform(TempPoint, TempPoint);
            Point.POSY = TempPoint.x;
            Point.POSZ = TempPoint.y;
            
            TempPoint.x = Point.POSX;
            TempPoint.y = Point.POSZ;
            VRotate.transform(TempPoint, TempPoint);
            Point.POSX = TempPoint.x;
            Point.POSZ = TempPoint.y;
            
            TempPoint.x = Point.POSX;
            TempPoint.y = Point.POSY;            
            HRotate.transform(TempPoint, TempPoint);
            Point.POSX = TempPoint.x;
            Point.POSY  = TempPoint.y;
             
            Point.POSX = Point.POSX + Center.POSX;
            Point.POSY = Point.POSY + Center.POSY;
            Point.POSZ = Point.POSZ + Center.POSZ;
            
            Point.LinkIndex = LinkIndex;
          //  return Point;
        }
        final public void center(Observer Viewer, Point3D Point){
            Point.Base = Base;
            
            Point2D.Double Pos = new Point2D.Double();
            
            Pos.x = POSX - Viewer.POS.POSX;
            Pos.y = POSY - Viewer.POS.POSY;
            Viewer.IHRotation.transform(Pos, Pos);
            Point.POSX = Pos.x;
            Point.POSY = Pos.y;
            
            Pos.x = Point.POSX;
            Pos.y = POSZ - Viewer.POS.POSZ;
            Viewer.IVRotation.transform(Pos, Pos);
            Point.POSZ = Pos.y;
            Point.POSX = Pos.x;
            
            Pos.x = Point.POSY;
            Pos.y = Point.POSZ;
            Viewer.IRRotation.transform(Pos, Pos);
            Point.POSZ = Pos.y;
            Point.POSY = Pos.x;
            
            
            if(Point.POSX < 0.01 && Point.POSX > -0.01){
                Point.POSX = 0.0001;
            }
            
                
                
            
            Point.LinkIndex = LinkIndex;
          //  return Point;
           
        }
        final public void toScreen(Observer Viewer, Point2D.Double Target){
            double Temp = 1 / POSX * Viewer.HalfYMag;
            
            
            Target.x = POSY * Temp + Viewer.HalfX;
            Target.y = -POSZ * Temp + Viewer.HalfY;
            
        }
        public double distanceTo(Point3D Target){
            return Math.sqrt((Target.POSX - POSX) * (Target.POSX - POSX) +(Target.POSY - POSY) * (Target.POSY - POSY) + (Target.POSZ - POSZ) * (Target.POSZ - POSZ));
        }
        public double distanceToSquare(Point3D Target){
            return (Target.POSX - POSX) * (Target.POSX - POSX) +(Target.POSY - POSY) * (Target.POSY - POSY) + (Target.POSZ - POSZ) * (Target.POSZ - POSZ);
        }
        public Point2D.Double angleTo(Point3D Target){
           double HAngle = Math.toDegrees(Math.atan2(POSY - Target.POSY, POSX - Target.POSX));
           double VAngle = -Math.toDegrees(Math.atan2((POSZ - Target.POSZ),  Math.sqrt((POSY - Target.POSY) * (POSY - Target.POSY) + (POSX - Target.POSX) * (POSX - Target.POSX))));
           return new Point2D.Double(HAngle, VAngle);
        }
        public String toString(){
            return "Point: " + "\t" + POSX + "\t" + POSY + "\t" + POSZ;
            
        }
       
    }
    class Observer{
        Point3D POS;
        double VAngle;
        double HAngle;
        double RAngle;
        int ScreenX;
        int ScreenY;
        int HalfX;
        int HalfY;
        double Magnification = 1.15;
        int HalfXMag;
        int HalfYMag;
        double Zoom = 0;
        Point3D LookVector = new Point3D(0, 0, 0);
        AffineTransform HRotation = new AffineTransform();
        AffineTransform VRotation = new AffineTransform();
        AffineTransform RRotation = new AffineTransform();
        AffineTransform IHRotation = new AffineTransform();
        AffineTransform IVRotation = new AffineTransform();
        AffineTransform IRRotation = new AffineTransform();
        public void setArea(int sX, int sY){
            ScreenY = sY;
            ScreenX = sX;
            HalfX = ScreenX / 2;
            HalfY = ScreenY / 2;
            HalfXMag = (int)(HalfX * Magnification);
            HalfYMag = (int)(HalfY * Magnification);
        }
        public Observer(double tX, double tY, double tZ, double aH, double aV, double aR){
            POS = new Point3D(tX, tY, tZ);
            VAngle = aV;
            HAngle = aH;
            RAngle = aR;
            HRotation.rotate(Math.toRadians(aH));
            VRotation.rotate(Math.toRadians(aV));
            RRotation.rotate(Math.toRadians(aR));
            IHRotation.rotate(Math.toRadians(-aH));
            IVRotation.rotate(Math.toRadians(-aV));
            IRRotation.rotate(Math.toRadians(-aR));
        }
        final public void setHAngle(double aH){
            if(HAngle != aH){
                HRotation.setToRotation(Math.toRadians(aH));
                IHRotation.setToRotation(Math.toRadians(-aH));
                HAngle = aH;
                setLookVector();
            }
        }
        final public void setVAngle(double aV){
            if(VAngle != aV){
                VRotation.setToRotation(Math.toRadians(aV));
                IVRotation.setToRotation(Math.toRadians(-aV));
                VAngle = aV;
                setLookVector();
            }
        }
        final public void setRAngle(double aR){
            if(RAngle != aR){
                RRotation.setToRotation(Math.toRadians(aR));
                IRRotation.setToRotation(Math.toRadians(-aR));
                RAngle = aR;
                setLookVector();
            }
        }
        final public void setAngles(double aH, double aV, double aR){
            if(HAngle != aH){
                HRotation.setToRotation(Math.toRadians(aH));
                IHRotation.setToRotation(Math.toRadians(-aH));
                HAngle = aH;
            }
            if(VAngle != aV){
                VRotation.setToRotation(Math.toRadians(aV));
                IVRotation.setToRotation(Math.toRadians(-aV));
                VAngle = aV;
            }
            
            
            
            if(RAngle != aR){
                RRotation.setToRotation(Math.toRadians(aR));
                IRRotation.setToRotation(Math.toRadians(-aR));
                RAngle = aR;
            }
            setLookVector();
            
        }
        final public void setLookVector(){
            LookVector.POSX = 1;
            LookVector.POSY = 0;
            LookVector.POSZ = 0;
            LookVector.rotate(HRotation, VRotation, RRotation, LookVector);
        }
        final public void increment(char Switch, double Increment, AffineTransform Transform){
            switch(Switch){
                case 'h': HAngle = HAngle + Increment;
                          HRotation.concatenate(Transform);
                          IHRotation.concatenate(GUI.inverse(Transform));
                          setLookVector();
                          break;
                case 'v': VAngle = VAngle + Increment;
                          VRotation.concatenate(Transform);
                          IVRotation.concatenate(GUI.inverse(Transform));
                          setLookVector();
                          break;
                case 'r': RAngle = RAngle + Increment;
                          RRotation.concatenate(Transform);
                          IRRotation.concatenate(GUI.inverse(Transform));
                          setLookVector();
                          break;
            }
        }
        public Point2D.Double getAngle(Point2D.Double Point){
            
            
            Point2D.Double Temp = new Point2D.Double(Point.x - HalfX, Point.y - HalfY);
            RRotation.transform(Temp, Temp);
            double HA = -Math.toDegrees(Math.atan((Temp.x) / HalfYMag)) - HAngle;
            //double DiagLength = Math.sqrt(1 + Math.pow(Math.tan(Math.toRadians(HA + HAngle)), 2));
            double DiagLength = 1 / Math.cos(Math.toRadians(HA + HAngle));
            double VA = Math.toDegrees(Math.atan2((Temp.y) / HalfYMag, DiagLength)) - VAngle;
            
            Temp = new Point2D.Double(HA, -VA);
            
            return Temp;
        }
        public Point2D.Double getGradient(Point2D.Double Point){
            
            Point2D.Double Temp = new Point2D.Double(Point.x - HalfX, Point.y - HalfY);
            RRotation.transform(Temp, Temp);
            
            double HG = (Temp.x) / HalfYMag;
            //double DiagLength = Math.sqrt(1 + Math.pow(Math.tan(Math.toRadians(HA + HAngle)), 2));
            double VG = (Temp.y) / HalfYMag / Math.sqrt(1 + HG * HG);
           

            
            Temp = new Point2D.Double(-HG, -VG);
            return Temp;
        }
        
    }
    