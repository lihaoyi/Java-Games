import java.util.*;
public class FlatHash<T>{
    double POSX;
    double POSY;
    double Width;
    int Count;
    int ObjectNumber;



    private final Object[][][] Objects;
    
    private final Set<T> ReturnValues = new HashSet<T>();



    //Temporary Initialization Storage Variables
    private Vector ObjectList = new Vector();
    private Vector[][] TempGrid;
    private Vector<double[]> Collisions = new Vector<double[]>();

    boolean Finalized;
    public FlatHash(int C){
        Count = C;
        TempGrid = new Vector[Count][Count];
        for(int i = 0; i < Count; i++){
            for(int j = 0; j < Count; j++){
                TempGrid[i][j] = new Vector();
            }
        }
        Objects = new Object[C][C][];
    }
    public String toString(){
        return "HashMap From [" + POSX + ", " + POSY + "] Spacing: " + Width + " GridLength: " + Count + " Objects: " + ObjectNumber + "Finalized: " + Finalized;
    }
    public void addObject(Object A, double[] Data){ //Data = array of XY coordinates for each point {x1, y1, x2, y2...}
        if(Finalized){
            throw new IllegalArgumentException("Attempt to modify while Hash is Finalized");
        }
    	if(Data.length % 2 != 0){
    		throw new IllegalArgumentException("Data does not fit X-Y pair template");
    	}
        ObjectList.add(A);
        Collisions.add(Data);
        ObjectNumber++;
    }
    public void addObjectToAll(Object A){
        if(Finalized){
            throw new IllegalArgumentException("Attempt to modify while Hash is Finalized");
        }
        ObjectNumber++;
        for(int i = 0; i < Count; i++){
            for(int j = 0; j < Count; j++){
                TempGrid[i][j].add(A);
            }
        }
    }
    public Collection<T> testArea(double XA, double YA, double XB, double YB){
        if(!Finalized){
            throw new IllegalArgumentException("Attempt to read before Hash is Finalized");
        }
        ReturnValues.clear();
        if(XA > XB){double T = XB; XB = XA; XA = T;}
        if(YA > YB){double T = YB; YB = YA; YA = T;}
        int iX = (int)Math.max(Math.floor((XA - POSX) / Width), 0);
        int iY = (int)Math.max(Math.floor((YA - POSY) / Width), 0);

        int aX = (int)Math.min(Math.floor((XB - POSX) / Width), Count-1);
        int aY = (int)Math.min(Math.floor((YB - POSY) / Width), Count-1);

        for(int x = iX; x <= aX; x++){
            for(int y = iY; y <= aY; y++){

                for(int i = 0; i < Objects[x][y].length; i++){
                    ReturnValues.add((T)Objects[x][y][i]);
                }
            }
        }
     //   System.out.println(tX + " " + tY + "\t" + iX + " " + iY + "\t" + aX + " " + aY);
        
        return ReturnValues;
    }
    public Collection<T> testPoint(double X, double Y, double R){

        if(!Finalized){
            throw new IllegalArgumentException("Attempt to read before Hash is Finalized");
        }
        ReturnValues.clear();

        int tX = (int)Math.floor((X - POSX) / Width);
        int tY = (int)Math.floor((Y - POSY) / Width);
        int iX = (int)Math.max(Math.floor((X - R - POSX) / Width), 0);
        int iY = (int)Math.max(Math.floor((Y - R - POSY) / Width), 0);
        int aX = (int)Math.min(Math.floor((X + R - POSX) / Width), Count-1);
        int aY = (int)Math.min(Math.floor((Y + R - POSY) / Width), Count-1);
        int N = 0;
        for(int x = iX; x <= aX; x++){
            for(int y = iY; y <= aY; y++){

                for(int i = 0; i < Objects[x][y].length; i++){
                    ReturnValues.add((T)Objects[x][y][i]);
                }
            }
        }
     //   System.out.println(tX + " " + tY + "\t" + iX + " " + iY + "\t" + aX + " " + aY);
        
        return ReturnValues;
    }
    public Collection<T> testPoint(double X, double Y){


        if(!Finalized){
            throw new IllegalArgumentException("Attempt to read before Hash is Finalized");
        }
        ReturnValues.clear();
        int tX = (int)Math.floor((X - POSX) / Width);
        int tY = (int)Math.floor((Y - POSY) / Width);
        if(tX < 0 || tY < 0 || tX >= Count || tY >= Count){


        }else{
            for(int i = 0; i < Objects[tX][tY].length; i++){
                ReturnValues.add((T)Objects[tX][tY][i]);
            }
        }
        return ReturnValues;
    }
    public void process(){

        if(Finalized){
            throw new IllegalArgumentException("Attempt to process when already finalized");
        }
        Finalized = true;


        //set bounds
        double MaxX = Double.MIN_VALUE; double MinX = Double.MAX_VALUE;
        double MaxY = Double.MIN_VALUE; double MinY = Double.MAX_VALUE;
        System.out.println("MIN: " + MinX + "\t" + MinY);

        for(int i = 0; i < ObjectList.size(); i++){
            double[] Data = (double[])Collisions.elementAt(i);
            for(int j = 0; j < Data.length; j+= 2){
                double X = Data[j];
                double Y = Data[j+1];
                MaxX = Math.max(X, MaxX);
                MaxY = Math.max(Y, MaxY);
                MinX = Math.min(X, MinX);
                MinY = Math.min(Y, MinY);
            }
        }
        System.out.println("MIN: " + MinX + "\t" + MinY);
        if(MaxX - MinX > MaxY - MinY){
            Width = (MaxX - MinX) / Count;
        }else{
            Width = (MaxY - MinY) / Count;
        }
        POSX = MinX;
        POSY = MinY;




        //fill grid
        for(int i = 0; i < ObjectList.size(); i++){
            int MiX = Integer.MAX_VALUE; int MiY = Integer.MAX_VALUE;
            int MaX = Integer.MIN_VALUE; int MaY = Integer.MIN_VALUE;
            double[] Data = (double[])Collisions.elementAt(i);
            for(int j = 0; j < Data.length; j+= 2){
                int X = (int)Math.floor((Data[j] - POSX) / Width);
                int Y = (int)Math.floor((Data[j+1] - POSY) / Width);
                MiX = Math.min(X, MiX);
                MaX = Math.max(X, MaX);
                MiY = Math.min(Y, MiY);
                MaY = Math.max(Y, MaY);
            }
            MaX = GUI.cap(0, MaX, Count-1); MiX = GUI.cap(0, MiX, Count-1);
            MaY = GUI.cap(0, MaY, Count-1); MiY = GUI.cap(0, MiY, Count-1);

            for(int x = MiX; x <= MaX; x++){
                for(int y = MiY; y <= MaY; y++){
                    TempGrid[x][y].add(ObjectList.elementAt(i));
                }
            }

        }
        for(int i = 0; i < Count; i++){
            for(int j = 0; j < Count; j++){
                Objects[i][j] = TempGrid[i][j].toArray();
            }
        }
        Collisions = null; TempGrid = null; ObjectList = null;
        System.out.println("FLATASH " + this.Width);
    }

}