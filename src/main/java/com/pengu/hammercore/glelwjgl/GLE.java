package com.pengu.hammercore.glelwjgl;

public interface GLE
{
	public static final String VERSION = new String("$Id: GLE.java,v 1.3 1998/05/02 12:06:39 descarte Exp descarte $");
	public static final int SUMMARY = 0;
	public static final int VERBOSE = 1;
	public static final int TUBE_JN_RAW = 1;
	public static final int TUBE_JN_ANGLE = 2;
	public static final int TUBE_JN_CUT = 3;
	public static final int TUBE_JN_ROUND = 4;
	public static final int TUBE_JN_MASK = 15;
	public static final int TUBE_JN_CAP = 16;
	public static final int TUBE_NORM_FACET = 256;
	public static final int TUBE_NORM_EDGE = 512;
	public static final int TUBE_NORM_PATH_EDGE = 1024;
	public static final int TUBE_NORM_MASK = 3840;
	public static final int TUBE_CONTOUR_CLOSED = 4096;
	public static final int GLE_TEXTURE_ENABLE = 65536;
	public static final int GLE_TEXTURE_STYLE_MASK = 255;
	public static final int GLE_TEXTURE_VERTEX_FLAT = 1;
	public static final int GLE_TEXTURE_NORMAL_FLAT = 2;
	public static final int GLE_TEXTURE_VERTEX_CYL = 3;
	public static final int GLE_TEXTURE_NORMAL_CYL = 4;
	public static final int GLE_TEXTURE_VERTEX_SPH = 5;
	public static final int GLE_TEXTURE_NORMAL_SPH = 6;
	public static final int GLE_TEXTURE_VERTEX_MODEL_FLAT = 7;
	public static final int GLE_TEXTURE_NORMAL_MODEL_FLAT = 8;
	public static final int GLE_TEXTURE_VERTEX_MODEL_CYL = 9;
	public static final int GLE_TEXTURE_NORMAL_MODEL_CYL = 10;
	public static final int GLE_TEXTURE_VERTEX_MODEL_SPH = 11;
	public static final int GLE_TEXTURE_NORMAL_MODEL_SPH = 12;
	
	public int gleGetJoinStyle();
	
	public void gleSetJoinStyle(int var1);
	
	public void gleTextureMode(int var1);
	
	public void glePolyCylinder(int var1, double[][] var2, float[][] var3, double var4, float var6, float var7) throws GLEException;
	
	public void glePolyCone(int var1, double[][] var2, float[][] var3, double[] var4, float var5, float var6) throws GLEException;
	
	public void gleExtrusion(int var1, double[][] var2, double[][] var3, double[] var4, int var5, double[][] var6, float[][] var7) throws GLEException;
	
	public void gleTwistExtrusion(int var1, double[][] var2, double[][] var3, double[] var4, int var5, double[][] var6, float[][] var7, double[] var8) throws GLEException;
	
	public void gleSuperExtrusion(int var1, double[][] var2, double[][] var3, double[] var4, int var5, double[][] var6, float[][] var7, double[][][] var8) throws GLEException;
	
	public void gleSpiral(int var1, double[][] var2, double[][] var3, double[] var4, double var5, double var7, double var9, double var11, double[][] var13, double[][] var14, double var15, double var17) throws GLEException;
	
	public void gleLathe(int var1, double[][] var2, double[][] var3, double[] var4, double var5, double var7, double var9, double var11, double[][] var13, double[][] var14, double var15, double var17) throws GLEException;
	
	public void gleHelicoid(double var1, double var3, double var5, double var7, double var9, double[][] var11, double[][] var12, double var13, double var15) throws GLEException;
	
	public void gleToroid(double var1, double var3, double var5, double var7, double var9, double[][] var11, double[][] var12, double var13, double var15) throws GLEException;
	
	public void gleScrew(int var1, double[][] var2, double[][] var3, double[] var4, double var5, double var7, double var9) throws GLEException;
}