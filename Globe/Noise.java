import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.Color;
import java.io.*;
public class Noise{
	
	public static void main(String[] args) throws IOException{
		DetPerlin2D MyNoise = new DetPerlin2D(8, 1, 101, 127.5, 127.5, 100);
		BufferedImage B;
		BufferedImage A;
		B = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);	
		A = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);	
		
		
		MyNoise.makeNoise(0, 0, 800, 600);
		long L = System.currentTimeMillis();
		for(int i = 0; i < 800; i++){
			for(int j = 0; j < 600; j++){
				int RGB = (int)MyNoise.get(i, j);
				Color C = new Color(RGB, RGB, RGB);
				B.setRGB(i, j, C.getRGB());
			}	
		}
		MyNoise = new DetPerlin2D(8, 1, 111230, 127.5, 127.5, 100);
		int Offset = 0;
		MyNoise.makeNoise(0 + Offset, 0 + Offset, 800 + Offset, 600 + Offset);
		for(int i = 0; i < 800; i++){
			for(int j = 0; j < 600; j++){
				int RGB = (int)MyNoise.get(i + Offset, j + Offset);
				Color C = new Color(RGB, RGB, RGB);
				A.setRGB(i, j, C.getRGB());
			}	
		}
		L -= System.currentTimeMillis();
		System.out.println(-L);
		ImageIO.write(B, "bmp", new File("OutputB.bmp"));
		ImageIO.write(A, "bmp", new File("OutputA.bmp"));
		

	}
	public static double linearInterpolate(double X1, double X2, double Y1, double Y2, double Xf){
		if(Xf > X2 || Xf < X1){
			throw new IllegalArgumentException("cannot interpolate out of range");	
		}
		return Y1 + (Y2 - Y1) / (X2 - X1) * (Xf - X1);
			
	}
	public static double cosInterpolate(double X1, double X2, double Y1, double Y2, double Xf){
		if(Xf > X2 || Xf < X1){
			throw new IllegalArgumentException("cannot interpolate out of range");	
		}

		double w = Math.PI / (X1 - X2);
		double k = -X2 * w;
		
		
		return ((Y2 - Y1) / 2) * Math.cos(w * Xf + k) + ((Y2 + Y1) / 2);	
	}
	public static double cubicInterpolate(double X1, double X2, double Y1, double Y2, double Xf){
		if(Xf > X2 || Xf < X1){
			throw new IllegalArgumentException("cannot interpolate out of range");	
		}
		
		double Xt = (Xf - X1) / (X2 - X1);
		
		return (2 * Xt*Xt*Xt - 3 * Xt*Xt) * (Y1 - Y2) + Y1;	
	}	
	public static int index(int X, int Y){ // maps every X and Y point on to a unique index
    	int R = Math.abs(X) + Math.abs(Y) + 1;
    	int E = 2*R*R - 6*R + 5;
    	if(Y >= 0){
    		return E + X + R - 1;	
    	}else if(Y < 0){
    		return E + R + R + R - X -3;
    	
    	}
    	return E;
    }
}

class Noise1D{
	float Amplitude;
	float Average;
	float Wavelength;
	
	float[] NoisePoints;
	
	Random Generator;
	
	public Noise1D(Random Gen, double Amp, double Ave, double Wave){
		Generator = Gen; Amplitude = (float)Amp; Average = (float)Ave; Wavelength = (float)Wave;	
	}	
	public void makeNoise(double Range){
		NoisePoints = new float[(int)((Range + 1) / Wavelength)];
		makeNoise(NoisePoints);
	}
	public void makeNoise(float[] Target){
		for(int i = 0; i < Target.length; i++){
			Target[i] = 2*(Generator.nextFloat()-0.5f) * Amplitude + Average;
		}
	}
	public double get(double x){ //not bounds safe
		int n1 = (int)(x / Wavelength);
		int n2 = (int)(x / Wavelength + 1);
		double X1 = n1 * Wavelength;
		double X2 = n2 * Wavelength;
		double Y1 = NoisePoints[n1];
		double Y2 = NoisePoints[n2];
		
		return Noise.cubicInterpolate(X1, X2, Y1, Y2, x);
		
	}
	
	
}
class DetNoise2D extends Noise2D{
	//loat Amplitude;
	//float Average;
	//float Wavelength;
	//Random Generator;
	//float[][] NoisePoints;

	
	double Xp; double Yp; //lowest corner coords
	double Xf; double Yf; //highest corner coords
	int Xn; int Yn; //number of noise points
	int Xb; int Yb; //coordinates of lower corner normalized by wavelength
	
	int Seed;
	public DetNoise2D(int S, double Amp, double Ave, double Wave){
		super(S, Amp, Ave, Wave);
	//	Generator = new Random();
		Seed = S;
	}
	
	public void makeNoise(double Xs, double Ys, double Xe, double Ye){
		Xp = (int)Math.floor(Xs / Wavelength) * Wavelength;
		Yp = (int)Math.floor(Ys / Wavelength) * Wavelength;
		
		Xf = (int)Math.ceil(Xe / Wavelength) * Wavelength;
		Yf = (int)Math.ceil(Ye / Wavelength) * Wavelength;
		
		
		Xn = (int)Math.ceil(Xe / Wavelength + 1) - (int)Math.floor(Xs / Wavelength);
		Yn = (int)Math.ceil(Ye / Wavelength + 1) - (int)Math.floor(Ys / Wavelength);
		
		
		xR = Xn; yR = Yn;
		Xb = (int)Math.floor(Xs / Wavelength);
		Yb = (int)Math.floor(Ys / Wavelength);
		
	//	System.out.println("P\t" + Xp + "\t" + Yp);
	//	System.out.println("F\t" + Xf + "\t" + Yf);
	//	System.out.println("B\t" + Xb + "\t" + Yb);
	//	System.out.println("N\t" + Xn + "\t" + Yn);
		
		NoisePoints = new float[Xn][Yn];
		
		for(int i = 0; i < Xn; i++){
			for(int j = 0; j < Yn; j++){		
				int Xr = i + Xb; int Yr = j + Yb;
				Generator.setSeed(Noise.index(Xr, Yr) + Seed);
				Generator.setSeed(Generator.nextLong());
				NoisePoints[i][j] = 2*(Generator.nextFloat()-0.5f) * Amplitude + Average;
			}	
		}
	}
	public void makeNoise(double xRv, double yRv){
		makeNoise(0, 0, xRv, yRv);	
	}
	public double get(double x, double y){
		if(x < Xp || x > Xf || y < Yp || y > Yf){ //check bounds
			throw new IllegalArgumentException("coordinates out of prepared range");	
		}
		
		x -= Xp; y -= Yp; //converting x and y to local coordinates
		
		return super.get(x, y); //return as normal
		
		
	}
}

class Noise2D{
	float Amplitude;
	float Average;
	float Wavelength;
	
	float[][] NoisePoints;
	int xR;
	int yR;
	
	Random Generator;
	public Noise2D(int Seed, double Amp, double Ave, double Wave){
		Generator = new Random(Seed); Amplitude = (float)Amp; Average = (float)Ave; Wavelength = (float)Wave;	
		
	}	
	public void makeNoise(double xRv, double yRv){
		
		NoisePoints = new float[(int)(xRv / Wavelength) + 2][(int)(yRv / Wavelength) + 2];
		for(int i = 0; i < NoisePoints.length; i++){
			for(int j = 0; j < NoisePoints[i].length; j++){
				NoisePoints[i][j] = 2*(Generator.nextFloat()-0.5f) * Amplitude + Average;
			}	
		}
		xR = NoisePoints.length;
		yR = NoisePoints[0].length;
	}
	public double get(double x, double y){
		int nX = (int)(x / Wavelength);	
		int mX = nX + 1;
		int nY = (int)(y / Wavelength);
		int mY = nY + 1;
		
		double Y1 = Noise.cubicInterpolate(nX * Wavelength, mX * Wavelength, NoisePoints[((nX%xR)+xR)%xR][((nY%yR)+yR)%yR], NoisePoints[((mX%xR)+xR)%xR][((nY%yR)+yR)%yR], x);
		double Y2 = Noise.cubicInterpolate(nX * Wavelength, mX * Wavelength, NoisePoints[((nX%xR)+xR)%xR][((mY%yR)+yR)%yR], NoisePoints[((mX%xR)+xR)%xR][((mY%yR)+yR)%yR], x);
		
		double Result = Noise.cubicInterpolate(nY * Wavelength, mY * Wavelength, Y1, Y2, y);
		
		return Result;
	}
}
class DetPerlin2D extends Perlin2D{
	
	public DetPerlin2D(int D, double S, int Seed, double Amp, double Ave, double Wave){
		super(D, S, Seed, Amp, Ave, Wave);	
	}
	public void initNoise(){
		double Multiplier = (1 - Math.pow(2, -Smoothness));
		for(int i = 0; i < Depth; i++){
			Parts[i] = new DetNoise2D(Seed, Amplitude / Math.pow(2, Smoothness * i) * Multiplier, 0, Wavelength / (1 << i));
		}
	}
	public void makeNoise(double xR, double yR){
		for(int i = 0; i < Depth; i++){
			((DetNoise2D)Parts[i]).makeNoise(0, 0, xR, yR);	
		}	
		Prepared = true;
	}
	public void makeNoise(double Xs, double Ys, double xR, double yR){
		for(int i = 0; i < Depth; i++){
			((DetNoise2D)Parts[i]).makeNoise(Xs, Ys, xR, yR);	
		}	
		Prepared = true;
	}
}
class Perlin2D extends Noise2D{
	//double Amplitude;
	//double Average;
	//double Wavelength;
	
	double Smoothness;
	int Depth;
	
	
	Noise2D[] Parts;
	Random Generator;
	boolean Prepared;
	int Seed;
	public Perlin2D(int D, double S, int tSeed, double Amp, double Ave, double Wave){
		super(tSeed, Amp, Ave, Wave);
		Seed = tSeed;
		
		Depth = D;
		Smoothness = S;
		Parts = new Noise2D[Depth];
		
		
		
		initNoise();
		
	}
	public void initNoise(){
		double Multiplier = (1 - Math.pow(2, -Smoothness));
		for(int i = 0; i < Depth; i++){
			Parts[i] = new Noise2D(10, Amplitude / Math.pow(2, Smoothness * i) * Multiplier, 0, Wavelength / (1 << i));
		}
	}
	public void makeNoise(double xR, double yR){
		for(int i = 0; i < Depth; i++){
			Parts[i].makeNoise(xR, yR);	
		}	
		Prepared = true;
	}
	public double get(double x, double y){
		if(!Prepared){throw new IllegalArgumentException("Perlin was not prepared");}
		double Result = Average;
		for(int i = 0; i < Depth; i++){
			Result += Parts[i].get(x, y);	
		}	
		
		return Result;
	}
		
	
	
}

/*
class DetPerlin2D extends DetNoise2D{
	//double Amplitude;
	//double Average;
	//double Wavelength;
	
	double Smoothness;
	int Depth;
	
	
	DetNoise2D[] Parts;
	
	boolean Prepared;
	public DetPerlin2D(int D, double S, int Seed, double Amp, double Ave, double Wave){
		super(0, Amp, Ave, Wave);
		
		
		Depth = D;
		Smoothness = S;
		Parts = new DetNoise2D[Depth];
		double Multiplier = (1 - Math.pow(2, -Smoothness));
		for(int i = 0; i < Depth; i++){
			Parts[i] = new DetNoise2D(10, Amp / Math.pow(2, Smoothness * i) * Multiplier, 0, Wave / (1 << i));
		}
	}
	public void makeNoise(double Xs, double Ys, double xR, double yR){
		for(int i = 0; i < Depth; i++){
			Parts[i].makeNoise(Xs, Ys, xR, yR);	
		}	
		Prepared = true;
	}
	public double get(double x, double y){
		if(!Prepared){throw new IllegalArgumentException("Perlin was not prepared");}
		double Result = Average;
		for(int i = 0; i < Depth; i++){
			Result += Parts[i].get(x, y);	
		}	
		
		return Result;
	}
		
	
	
}*/