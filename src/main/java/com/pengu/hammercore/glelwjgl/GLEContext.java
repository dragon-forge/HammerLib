/**
 * $Id: GLEContext.java,v 1.1 1998/05/03 16:18:47 descarte Exp descarte $
 *
 * Copyright (c)1998 Arcane Technologies Ltd. <http://www.arcana.co.uk>
 *
 * OpenGL GLE Tubing/Extrusion library GLE context
 *
 * $Log: GLEContext.java,v $
 *
 * This software is Copyright (c)1998 Arcane Technologies Ltd. and is released
 * under the ``Artistic'' licence which is available in the source distribution.
 * If this license is not present, you have an unofficial release of this
 * software. The official release may be downloaded from Arcane Technologies
 * Ltd. WWW site at:
 *
 * http://www.arcana.co.uk/products/shapeshifter
 *
 */

package com.pengu.hammercore.glelwjgl;

/**
 * Encapsulation of the information required by GLE to handle multiple GLE
 * ``contexts''.
 * <P>
 * 
 * @version $Id: GLEContext.java,v 1.1 1998/05/03 16:18:47 descarte Exp descarte
 *          $
 * @author Alligator Descartes
 *         &lt;<A HREF="http://www.arcana.co.uk">http://www.arcana.co.uk</A>&gt;
 */
public class GLEContext
{
	
	/** Version information */
	public static final String VERSION = new String("$Id: GLEContext.java,v 1.1 1998/05/03 16:18:47 descarte Exp descarte $");
	
	/** The current join style to be used in any extrusions */
	private int joinStyle = GLE.TUBE_JN_RAW;
	
	/** Various state variables used in extrusions */
	/** @@Make these private and add accessor methods */
	protected int ncp = 0;
	protected double[][] contour = null;
	protected double[][] contourNormal = null;
	protected double[] up = null;
	protected int npoints = 0;
	protected double[][] pointArray = null;
	protected float[][] colourArray = null;
	protected double[][][] xformArray = null;
	
	/** Returns the current join style */
	protected final int getJoinStyle()
	{
		return joinStyle;
	}
	
	/** Sets the join style */
	protected final void setJoinStyle(int style)
	{
		joinStyle = style;
	}
	
}
