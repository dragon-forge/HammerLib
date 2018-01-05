package com.pengu.hammercore.glelwjgl;

public class GLEContext
{
	public static final String VERSION = new String("$Id: GLEContext.java,v 1.1 1998/05/03 16:18:47 descarte Exp descarte $");
	private int joinStyle = 1;
	protected int ncp = 0;
	protected double[][] contour = null;
	protected double[][] contourNormal = null;
	protected double[] up = null;
	protected int npoints = 0;
	protected double[][] pointArray = null;
	protected float[][] colourArray = null;
	protected double[][][] xformArray = null;
	
	protected final int getJoinStyle()
	{
		return this.joinStyle;
	}
	
	protected final void setJoinStyle(int style)
	{
		this.joinStyle = style;
	}
}