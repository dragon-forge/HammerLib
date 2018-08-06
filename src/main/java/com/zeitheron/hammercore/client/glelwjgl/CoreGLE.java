/**
 * $Id: CoreGLE.java,v 1.5 1998/05/20 00:19:43 descarte Exp descarte $
 *
 * Copyright (c)1998 Arcane Technologies Ltd. <http://www.arcana.co.uk>
 *
 * OpenGL GLE Tubing/Extrusion method implementations.
 *
 * $Log: CoreGLE.java,v $ Revision 1.5 1998/05/20 00:19:43 descarte Updated
 * tesselator code to use the new double[] method for gluTessVertex() instead of
 * performing translation to float[] manually.
 *
 * Revision 1.4 1998/05/05 23:30:38 descarte First complete version of the pure
 * Java GLE code.
 *
 * Revision 1.3 1998/05/02 12:11:03 descarte Fixed documentation typo.
 *
 * Revision 1.2 1998/05/02 12:06:52 descarte Added copyright information.
 *
 * Revision 1.1 1998/05/02 00:53:38 descarte Initial revision
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
package com.zeitheron.hammercore.client.glelwjgl;

import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glIsEnabled;
import static org.lwjgl.opengl.GL11.glMultMatrix;
import static org.lwjgl.opengl.GL11.glNormal3d;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLUtessellator;
import org.lwjgl.util.glu.GLUtessellatorCallback;

/**
 * Implementation of the routines exposed by the GLE library. The methods in
 * this class interface with the GLE library perform the execution of the
 * underlying extrusion code.
 * <P>
 *
 * @version $Id: CoreGLE.java,v 1.5 1998/05/20 00:19:43 descarte Exp descarte $
 * @author Alligator Descartes
 *         &lt;<A HREF="http://www.arcana.co.uk">http://www.arcana.co.uk</A>&gt;
 */
public class CoreGLE implements GLE
{
	
	/**
	 * Version information
	 */
	public static final String VERSION = new String("$Id: CoreGLE.java,v 1.5 1998/05/20 00:19:43 descarte Exp descarte $");
	/**
	 * GLE version information for Win32 DLL loading
	 */
	private static final String GLE_VERSION = new String("095");
	/**
	 * The current GLE context
	 */
	private final GLEContext context_ = new GLEContext();
	/**
	 * Default number of segments to tesselate into
	 */
	private int _POLYCYL_TESS = 20;
	/**
	 * The number of pieces round caps are tesselated into
	 */
	private int __ROUND_TESS_PIECES = 5;
	/**
	 * OpenGL pipelines
	 */
	// private static GL gl_ = null;
	// private static CoreGL coregl_ = new CoreGL();
	// private static TraceGL tracegl_ = new TraceGL( coregl_ );
	private static GLU glu_ = null;
	// private static CoreGLU coreglu_ = new CoreGLU();
	// private static TraceGLU traceglu_ = new TraceGLU( coreglu_ );
	/**
	 * Constructor
	 */
	private tessellCallBack tessCallback = new tessellCallBack(glu_);
	
	public CoreGLE()
	{
		// tracegl_.setMode( TraceGLE.VERBOSE );
		// gl_ = coregl_;
		// gl_ = tracegl_;
		// glu_ = coreglu_;
	}
	
	/**
	 * Returns the current join style for connected extruded segments
	 * 
	 * @return the join style
	 */
	@Override
	public int gleGetJoinStyle()
	{
		return context_.getJoinStyle();
	}
	
	/**
	 * Sets the current join style for connected extruded segments.
	 * <P>
	 *
	 * @param style
	 *            The new join style
	 */
	@Override
	public void gleSetJoinStyle(int style)
	{
		context_.setJoinStyle(style);
	}
	
	/**
	 * Sets the current texture coordinate generation mode
	 * <P>
	 *
	 * @param mode
	 *            Bitwise OR of texture flags
	 */
	@Override
	public void gleTextureMode(int mode)
	{
	}
	
	/**
	 * Generates a polycone used in glePolyCylinder() and glePolyCone()
	 */
	private void gen_polycone(int npoints, double[][] pointArray, float[][] colourArray, double radius, double[][][] xformArray)
	{
		int savedStyle;
		double[][] circle = new double[_POLYCYL_TESS][2];
		double[][] norm = new double[_POLYCYL_TESS][2];
		double c, s;
		double[] v21 = new double[3];
		double len = 0;
		double[] up = new double[3];
		int i;
		
		/**
		 * This if statement forces this routine into double-duty for both the
		 * polycone and the polycylinder routines
		 */
		if(xformArray != null)
		{
			radius = 1.0;
		}
		s = Math.sin(2.0 * Math.PI / _POLYCYL_TESS);
		c = Math.cos(2.0 * Math.PI / _POLYCYL_TESS);
		
		norm[0][0] = 1.0;
		norm[0][1] = 0.0;
		circle[0][0] = radius;
		circle[0][1] = 0.0;
		
		/**
		 * Draw a norm using recursion relations
		 */
		for(i = 1; i < _POLYCYL_TESS; i++)
		{
			norm[i][0] = norm[i - 1][0] * c - norm[i - 1][1] * s;
			norm[i][1] = norm[i - 1][0] * s + norm[i - 1][1] * c;
			circle[i][0] = radius * norm[i][0];
			circle[i][1] = radius * norm[i][1];
		}
		
		/**
		 * Avoid degenerate vectors
		 */
		/**
		 * First, find a non-zero length segment
		 */
		i = 0;
		i = intersect.FIND_NON_DEGENERATE_POINT(i, npoints, len, v21, pointArray);
		len = matrix.VEC_LENGTH(v21);
		/* System.err.println( "v21: ( " + v21[0] + ", " + v21[1] + ", " +
		 * v21[2] + " )" ); */
		if(i == npoints)
		{
			return;
		}
		
		/* next, check to see if this segment lies along x-axis */
		if((v21[0] == 0.0) && (v21[2] == 0.0))
		{
			up[0] = up[1] = up[2] = 1.0;
		} else
		{
			up[0] = up[2] = 0.0;
			up[1] = 1.0;
		}
		
		/* save the current join style */
		savedStyle = gleGetJoinStyle();
		gleSetJoinStyle(savedStyle | TUBE_CONTOUR_CLOSED);
		
		/* if lighting is not turned on, don't send normals. MMODE is a good
		 * indicator of whether lighting is active */
		if(!glIsEnabled(GL_LIGHTING))
		{
			gleSuperExtrusion(_POLYCYL_TESS, circle, null, up, npoints, pointArray, colourArray, xformArray);
		} else
		{
			gleSuperExtrusion(_POLYCYL_TESS, circle, norm, up, npoints, pointArray, colourArray, xformArray);
		}
		
		/* restore the join style */
		gleSetJoinStyle(savedStyle);
	}
	
	/**
	 * Draws a polycylinder, specified as a polyline
	 * <P>
	 *
	 * @param npoints
	 *            The number of points in the polyline
	 * @param pointArray
	 *            An array of points specified as ( x, y, z )
	 * @param colourArray
	 *            An array of colors at each polyline vertex specified as RGB
	 * @param radius
	 *            The radius of the polycylinder
	 */
	@Override
	public void glePolyCylinder(int npoints, double[][] pointArray, float[][] colourArray, double radius) throws GLEException
	{
		
		gen_polycone(npoints, pointArray, colourArray, radius, null);
	}
	
	/**
	 * Draws a polycone specified as a polyline with radii
	 * <P>
	 *
	 * @param npoints
	 *            The number of points in the polyline
	 * @param pointArray
	 *            An array of points specified as ( x, y, z )
	 * @param colourArray
	 *            An array of colours at each polyline vertex specified as RGB
	 * @param radiusArray
	 *            An array containing the radii of the cone at each polyline
	 *            vertex
	 */
	@Override
	public void glePolyCone(int npoints, double[][] pointArray, float[][] colourArray, double[] radiusArray) throws GLEException
	{
		double[][][] xforms = new double[npoints][2][3];
		for(int i = 0; i < npoints; i++)
		{
			xforms[i][0][0] = radiusArray[i];
			xforms[i][0][1] = 0.0;
			xforms[i][0][2] = 0.0;
			xforms[i][1][0] = 0.0;
			xforms[i][1][1] = radiusArray[i];
			xforms[i][1][2] = 0.0;
		}
		gen_polycone(npoints, pointArray, colourArray, 1.0, xforms);
	}
	
	/**
	 * Extrudes an arbitrary 2D contour along an arbitrary 3D path.
	 * <P>
	 *
	 * @param ncp
	 *            The number of contour points
	 * @param contour
	 *            An array containing the points forming the 2D contour
	 *            specified as ( x, y )
	 * @param contourNormal
	 *            An array containing the normals for each contour point
	 * @param up
	 *            The up vector for the contour
	 * @param npoints
	 *            The number of points in the 3D polyline
	 * @param pointArray
	 *            An array containing the vertices for the 3D polyline specified
	 *            as ( x, y, z )
	 * @param colourArray
	 *            An array containing the colours at each polyline vertex
	 *            specified as RGB
	 */
	@Override
	public void gleExtrusion(int ncp, double[][] contour, double[][] contourNormal, double[] up, int npoints, double[][] pointArray, float[][] colourArray) throws GLEException
	{
		
		gleSuperExtrusion(ncp, contour, contourNormal, up, npoints, pointArray, colourArray, null);
	}
	
	/**
	 * Extrudes an arbitrary 2D contour along a 3D path specifying local
	 * rotations ( twists ) as the contour is extruded.
	 * <P>
	 *
	 * @param ncp
	 *            The number of contour points
	 * @param contour
	 *            An array containing the points forming the 2D contour
	 *            specified as ( x, y )
	 * @param contourNormal
	 *            An array containing the normals for each contour point
	 * @param up
	 *            The up vector for the contour
	 * @param npoints
	 *            The number of points in the 3D polyline
	 * @param pointArray
	 *            An array containing the vertices for the 3D polyline specified
	 *            as ( x, y, z )
	 * @param colourArray
	 *            An array containing the colours at each polyline vertex
	 *            specified as RGB
	 * @param twistArray
	 *            An array containing the twists to be applied at each contour
	 *            point ( in degrees )
	 */
	@Override
	public void gleTwistExtrusion(int ncp, double[][] contour, double[][] contourNormal, double[] up, int npoints, double[][] pointArray, float[][] colourArray, double[] twistArray) throws GLEException
	{
		
		double[][][] xforms = new double[npoints][2][3];
		double angle = 0.0;
		double si = 0.0, co = 0.0;
		
		for(int j = 0; j < npoints; j++)
		{
			angle = Math.PI / 180.0 * twistArray[j];
			si = Math.sin(angle);
			co = Math.cos(angle);
			xforms[j][0][0] = co;
			xforms[j][0][1] = -si;
			xforms[j][0][2] = 0.0;
			xforms[j][1][0] = si;
			xforms[j][1][1] = co;
			xforms[j][1][2] = 0.0;
		}
		gleSuperExtrusion(ncp, contour, contourNormal, up, npoints, pointArray, colourArray, xforms);
	}
	
	/**
	 * Extrudes an arbitrary 2D contour along a 3D path specifying local affine
	 * transformations as the contour is extruded.
	 * <P>
	 *
	 * @param ncp
	 *            The number of contour points
	 * @param contour
	 *            An array containing the points forming the 2D contour
	 *            specified as ( x, y )
	 * @param contourNormal
	 *            An array containing the normals for each contour point
	 * @param up
	 *            The up vector for the contour
	 * @param npoints
	 *            The number of points in the 3D polyline
	 * @param pointArray
	 *            An array containing the vertices for the 3D polyline specified
	 *            as ( x, y, z )
	 * @param colourArray
	 *            An array containing the colours at each polyline vertex
	 *            specified as RGB
	 * @param xformArray
	 *            An array containing the affine transformations to be applied
	 *            at each contour point ( in degrees )
	 */
	@Override
	public void gleSuperExtrusion(int ncp, double[][] contour, double[][] contourNormal, double[] up, int npoints, double[][] pointArray, float[][] colourArray, double[][][] xformArray) throws GLEException
	{
		
		/**
		 * Store the state for this extrusion
		 */
		context_.ncp = ncp;
		context_.contour = contour;
		context_.contourNormal = contourNormal;
		context_.up = up;
		context_.npoints = npoints;
		context_.pointArray = pointArray;
		context_.colourArray = colourArray;
		context_.xformArray = xformArray;
		
		switch(gleGetJoinStyle() & TUBE_JN_MASK)
		{
		case TUBE_JN_RAW:
		{
			extrusion_raw_join(ncp, contour, contourNormal, up, npoints, pointArray, colourArray, xformArray);
			break;
		}
		case TUBE_JN_ANGLE:
		{
			extrusion_angle_join(ncp, contour, contourNormal, up, npoints, pointArray, colourArray, xformArray);
			break;
		}
		case TUBE_JN_CUT:
		case TUBE_JN_ROUND:
		{
			/**
			 * This routine is used for both cut and round styles
			 */
			extrusion_round_or_cut_join(ncp, contour, contourNormal, up, npoints, pointArray, colourArray, xformArray);
			break;
		}
		default:
		{
			throw new GLEException("Join style is complete rubbish!");
		}
		}
	}
	
	/**
	 * Sweep an arbitrary contour along a helical path.
	 * <P>
	 *
	 * @param ncp
	 *            The number of contour points
	 * @param contour
	 *            An array containing the points forming the 2D contour
	 *            specified as ( x, y )
	 * @param contourNormal
	 *            An array containing the normals for each contour point
	 * @param up
	 *            The up vector for the contour
	 * @param startRadius
	 *            The spiral starts in XY plane
	 * @param drdTheta
	 *            The change in radius per revolution
	 * @param startZ
	 *            The starting Z value
	 * @param dzdTheta
	 *            The change in Z per revolution
	 * @param startTransform
	 *            The starting contour affine transformation
	 * @param dTransformdTheta
	 *            The tangent change transformation per revolution
	 * @param startTheta
	 *            The start angle in XY plane
	 * @param sweepTheta
	 *            The number of degrees to spiral around
	 */
	@Override
	public void gleSpiral(int ncp, double[][] contour, double[][] contourNormal, double[] up, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta) throws GLEException
	{
		
		int npoints = (int) ((_POLYCYL_TESS / 360.0) * Math.abs(sweepTheta) + 4);
		double[][] points = null;
		double[][][] xforms = null;
		double delta = 0;
		double deltaAngle = 0;
		double cdelta = 0, sdelta = 0;
		double sprev = 0, cprev = 0;
		double scurr = 0, ccurr = 0;
		double[][] mA = new double[2][2];
		double[][] mB = new double[2][2];
		double[][] run = new double[2][2];
		double[] deltaTrans = new double[2];
		double[] trans = new double[2];
		
		points = new double[npoints][3];
		if(startTransform == null)
		{
			xforms = null;
		} else
		{
			xforms = new double[npoints][2][3];
		}
		
		/**
		 * Compute delta angle based on number of points
		 */
		deltaAngle = (Math.PI / 180.0) * sweepTheta / (npoints - 3);
		startTheta *= Math.PI / 180.0;
		startTheta -= deltaAngle;
		
		/**
		 * Initialize factors
		 */
		cprev = Math.cos(startTheta);
		sprev = Math.sin(startTheta);
		
		cdelta = Math.cos(deltaAngle);
		sdelta = Math.sin(deltaAngle);
		
		/**
		 * Renormalize different factors
		 */
		delta = deltaAngle / (2.0 * Math.PI);
		dzdTheta *= delta;
		drdTheta *= delta;
		
		/**
		 * Remember the first point is hidden, so back-step
		 */
		startZ -= dzdTheta;
		startRadius -= drdTheta;
		
		/**
		 * Draw spiral path using recursion relations for sine, cosine
		 */
		for(int i = 0; i < npoints; i++)
		{
			points[i][0] = startRadius * cprev;
			points[i][1] = startRadius * sprev;
			points[i][2] = startZ;
			
			startZ += dzdTheta;
			startRadius += drdTheta;
			ccurr = cprev * cdelta - sprev * sdelta;
			scurr = cprev * sdelta + sprev * cdelta;
			cprev = ccurr;
			sprev = scurr;
		}
		
		/**
		 * If there's a deformation matrix specified, then a deformation path
		 * must be generated also
		 */
		if(startTransform != null)
		{
			if(dTransformdTheta == null)
			{
				for(int i = 0; i < npoints; i++)
				{
					xforms[i][0][0] = startTransform[0][0];
					xforms[i][0][1] = startTransform[0][1];
					xforms[i][0][2] = startTransform[0][2];
					xforms[i][1][0] = startTransform[1][0];
					xforms[i][1][1] = startTransform[1][1];
					xforms[i][1][2] = startTransform[1][2];
				}
			} else
			{
				/**
				 * if there is a differential matrix specified, treat it a a
				 * tangent (algebraic, infinitessimal) matrix. We need to
				 * project it into the group of real 2x2 matricies. (Note that
				 * the specified matrix is affine. We treat the translation
				 * components linearly, and only treat the 2x2 submatrix as an
				 * algebraic tangenet).
				 *
				 * For exponentiaition, we use the well known approx: exp(x) =
				 * lim (N->inf) (1+x/N) ** N and take N=32.
				 */
				/**
				 * Initialize translation and delta translation
				 */
				deltaTrans[0] = delta * dTransformdTheta[0][2];
				deltaTrans[1] = delta * dTransformdTheta[1][2];
				trans[0] = startTransform[0][2];
				trans[1] = startTransform[1][2];
				
				/**
				 * Prepare the tangent matrix
				 */
				delta /= 32.0;
				mA[0][0] = 1.0 + delta * dTransformdTheta[0][0];
				mA[0][1] = delta * dTransformdTheta[0][1];
				mA[1][0] = delta * dTransformdTheta[1][0];
				mA[1][1] = 1.0 + delta * dTransformdTheta[1][1];
				
				/**
				 * Compute exponential of matrix
				 */
				mB = matrix.MATRIX_PRODUCT_2X2(mA, mA);
				mA = matrix.MATRIX_PRODUCT_2X2(mB, mB);
				mB = matrix.MATRIX_PRODUCT_2X2(mA, mA);
				mA = matrix.MATRIX_PRODUCT_2X2(mB, mB);
				mB = matrix.MATRIX_PRODUCT_2X2(mA, mA);
				
				/**
				 * Initialize running matrix
				 */
				run = matrix.COPY_MATRIX_2X2(startTransform);
				
				/**
				 * Remember the first point is hidden
				 */
				xforms[0][0][0] = startTransform[0][0];
				xforms[0][0][1] = startTransform[0][1];
				xforms[0][0][2] = startTransform[0][2];
				xforms[0][1][0] = startTransform[1][0];
				xforms[0][1][1] = startTransform[1][1];
				xforms[0][1][2] = startTransform[1][2];
				
				for(int j = 0; j < npoints; j++)
				{
					xforms[j][0][0] = run[0][0];
					xforms[j][0][1] = run[0][1];
					xforms[j][1][0] = run[1][0];
					xforms[j][1][1] = run[1][1];
					
					/**
					 * Integrate to get exponential matrix. ( Note that the
					 * group action is a left-action -- i.e., multiply on the
					 * left ( not the right )
					 */
					mA = matrix.MATRIX_PRODUCT_2X2(mB, run);
					run = matrix.COPY_MATRIX_2X2(mA);
					
					xforms[j][0][2] = trans[0];
					xforms[j][1][2] = trans[1];
					
					trans[0] += deltaTrans[0];
					trans[1] += deltaTrans[1];
				}
			}
		}
		
		/**
		 * Save the current join style
		 */
		int saveStyle = gleGetJoinStyle();
		int style = saveStyle;
		
		/**
		 * Allow only angle joins ( for performance reasons ). The idea is that
		 * if the tesselation is fine enough, then an angle join should be
		 * sufficient to get the desired visual quality. A raw join would look
		 * terrible, an cut join would leave garbage everywhere, and a round
		 * join will over-tesselate (and thus should be avoided for performance
		 * reasons).
		 */
		style &= ~TUBE_JN_MASK;
		style |= TUBE_JN_ANGLE;
		gleSetJoinStyle(style);
		
		/**
		 * Do the extrusion
		 */
		gleSuperExtrusion(ncp, contour, contourNormal, up, npoints, points, null, xforms);
		
		/**
		 * Restore the join style
		 */
		gleSetJoinStyle(saveStyle);
	}
	
	/**
	 * Sweep an arbitrary contour along a helical path. The sweep will be
	 * performed as a shear along the z-axis so that the orientation of the
	 * contour is displaced, rather than translated, as the contour is swept.
	 * <P>
	 *
	 * @param ncp
	 *            The number of contour points
	 * @param contour
	 *            An array containing the points forming the 2D contour
	 *            specified as ( x, y )
	 * @param contourNormal
	 *            An array containing the normals for each contour point
	 * @param up
	 *            The up vector for the contour
	 * @param startRadius
	 *            The spiral starts in XY plane
	 * @param drdTheta
	 *            The change in radius per revolution
	 * @param startZ
	 *            The starting Z value
	 * @param dzdTheta
	 *            The change in Z per revolution
	 * @param startTransform
	 *            The starting contour affine transformation
	 * @param dTransformdTheta
	 *            The tangent change transformation per revolution
	 * @param startTheta
	 *            The start angle in XY plane
	 * @param sweepTheta
	 *            The number of degrees to spiral around
	 */
	@Override
	public void gleLathe(int ncp, double[][] contour, double[][] contourNormal, double[] up, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta) throws GLEException
	{
		
		double[] localup = new double[3];
		double len = 0.0;
		double[] trans = new double[2];
		double[][] start = new double[2][3], delt = new double[2][3];
		
		/**
		 * Because the spiral always starts on the axis, and proceeds in the
		 * positive y direction, we can see that valid up-vectors must lie in
		 * the x-z plane. Therefore, we make sure we have a valid up vector by
		 * projecting it onto the x-z plane, and normalizing.
		 */
		if(up != null)
		{
			if(up[1] != 0.0)
			{
				localup[0] = up[0];
				localup[1] = 0.0;
				localup[2] = up[2];
				
				len = matrix.VEC_LENGTH(localup);
				if(len != 0.0)
				{
					len = 1.0 / len;
					localup[0] *= len;
					localup[2] *= len;
					localup = matrix.VEC_SCALE(len, localup);
				} else
				{
					localup[0] = 0.0;
					localup[2] = 1.0;
				}
			} else
			{
				localup = matrix.VEC_COPY(up);
			}
		} else
		{
			localup[0] = 0.0;
			localup[2] = 1.0;
		}
		
		/**
		 * the dzdtheta derivative and the drdtheta derivative form a vector in
		 * the x-z plane. dzdtheta is the z component, and drdtheta is the x
		 * component. We need to convert this vector into the local coordinate
		 * system defined by the up vector. We do this by applying a 2D rotation
		 * matrix.
		 */
		trans[0] = localup[2] * drdTheta - localup[0] * dzdTheta;
		trans[1] = localup[0] * drdTheta + localup[2] * dzdTheta;
		
		/**
		 * now, add this translation vector into the affine xform
		 */
		if(startTransform != null)
		{
			if(dTransformdTheta != null)
			{
				delt = matrix.COPY_MATRIX_2X3(dTransformdTheta);
				delt[0][2] += trans[0];
				delt[1][2] += trans[1];
			} else
			{
				/**
				 * Hmm - the transforms don't exist
				 */
				delt[0][0] = 0.0;
				delt[0][1] = 0.0;
				delt[0][2] = trans[0];
				delt[1][0] = 0.0;
				delt[1][1] = 0.0;
				delt[1][2] = trans[1];
			}
			gleSpiral(ncp, contour, contourNormal, up, startRadius, 0.0, startZ, 0.0, startTransform, delt, startTheta, sweepTheta);
		} else
		{
			/**
			 * Hmm - the transforms don't exist
			 */
			start[0][0] = 1.0;
			start[0][1] = 0.0;
			start[0][2] = 0.0;
			start[1][0] = 0.0;
			start[1][1] = 1.0;
			start[1][2] = 0.0;
			
			delt[0][0] = 0.0;
			delt[0][1] = 0.0;
			delt[0][2] = trans[0];
			delt[1][0] = 0.0;
			delt[1][1] = 0.0;
			delt[1][2] = trans[1];
			gleSpiral(ncp, contour, contourNormal, up, startRadius, 0.0, startZ, 0.0, start, delt, startTheta, sweepTheta);
		}
	}
	
	/**
	 * Generalized Torus. Similar to gleSpiral() except contour is a circle.
	 * Uses gleSpiral() to draw.
	 * <P>
	 *
	 * @param rToroid
	 *            The circle contour radius
	 * @param startRadius
	 *            The spiral starts in XY plane
	 * @param drdTheta
	 *            The change in radius per revolution
	 * @param startZ
	 *            The starting Z value
	 * @param dzdTheta
	 *            The change in Z per revolution
	 * @param startTransform
	 *            The starting contour affine transformation
	 * @param dTransformdTheta
	 *            The tangent change transformation per revolution
	 * @param startTheta
	 *            The start angle in XY plane
	 * @param sweepTheta
	 *            The number of degrees to spiral around
	 * @see GLE#gleSpiral(int, double[][], double[][], double[], double, double, double, double, double[][], double[][], double, double)
	 */
	@Override
	public void gleHelicoid(double rToroid, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta) throws GLEException
	{
		super_helix(rToroid, startRadius, drdTheta, startZ, dzdTheta, startTransform, dTransformdTheta, startTheta, sweepTheta, "Spiral");
	}
	
	/**
	 * Generalized Torus. Similar to gleLathe() except contour is a circle. Uses
	 * gleLathe() to draw.
	 * <P>
	 *
	 * @param rToroid
	 *            The circle contour radius
	 * @param startRadius
	 *            The spiral starts in XY plane
	 * @param drdTheta
	 *            The change in radius per revolution
	 * @param startZ
	 *            The starting Z value
	 * @param dzdTheta
	 *            The change in Z per revolution
	 * @param startTransform
	 *            The starting contour affine transformation
	 * @param dTransformdTheta
	 *            The tangent change transformation per revolution
	 * @param startTheta
	 *            The start angle in XY plane
	 * @param sweepTheta
	 *            The number of degrees to spiral around
	 * @see GLE#gleSpiral(int, double[][], double[][], double[], double, double, double, double, double[][], double[][], double, double)
	 */
	@Override
	public void gleToroid(double rToroid, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta) throws GLEException
	{
		super_helix(rToroid, startRadius, drdTheta, startZ, dzdTheta, startTransform, dTransformdTheta, startTheta, sweepTheta, "Lathe");
	}
	
	/**
	 * Draws screw-type shapes. Takes a contour and extrudes it along the z-axis
	 * from a start z-value of <B>startz</B> to and end z-value of <B>endz</B>.
	 * During the extrusion, it will spin the contour along the contour origin
	 * by <B>twist</B> degrees.
	 * <P>
	 *
	 * @param ncp
	 *            The number of contour points
	 * @param contour
	 *            An array containing the vertex data for the 2D contour
	 *            specified as ( x, y )
	 * @param contourNormal
	 *            An array containing the normal data for the 2D contour
	 * @param up
	 *            The up vector for the contour
	 * @param startz
	 *            The start z-value for the segment
	 * @param endz
	 *            The end z-value for the segment
	 * @param twist
	 *            The number of rotations to be applied to the contour
	 */
	@Override
	public void gleScrew(int ncp, double[][] contour, double[][] contourNormal, double[] up, double startz, double endz, double twist) throws GLEException
	{
		
		int numsegs = (int) (Math.abs(twist / 18.0)) + 4;
		double[][] path = new double[numsegs][3];
		double[] twarr = new double[numsegs];
		double delta = 0.0, currz = 0.0, currang = 0.0, delang = 0.0;
		
		/**
		 * Fill in the extrusion array and the twist array uniformly
		 */
		delta = (endz - startz) / (numsegs - 3);
		currz = startz - delta;
		delang = twist / (numsegs - 3);
		currang = -delang;
		
		for(int i = 0; i < numsegs; i++)
		{
			path[i][0] = 0.0;
			path[i][1] = 0.0;
			path[i][2] = currz;
			currz += delta;
			twarr[i] = currang;
			currang += delang;
		}
		
		/**
		 * Do the extrusion
		 */
		gleTwistExtrusion(ncp, contour, contourNormal, up, numsegs, path, null, twarr);
	}
	
	/**
	 * Super-helicoid primitive method
	 */
	private final void super_helix(double rToroid, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta, String callback)
	{
		
		double[][] circle = new double[_POLYCYL_TESS][2];
		double[][] norm = new double[_POLYCYL_TESS][2];
		double c = 0.0, s = 0.0;
		double[] up = new double[3];
		
		/**
		 * Initialize sine and cosing for circle recursion equations
		 */
		s = Math.sin(2.0 * Math.PI / _POLYCYL_TESS);
		c = Math.cos(2.0 * Math.PI / _POLYCYL_TESS);
		
		norm[0][0] = 1.0;
		norm[0][1] = 0.0;
		circle[0][0] = rToroid;
		circle[0][1] = 0.0;
		
		/**
		 * Draw a norm using recursion relations
		 */
		for(int i = 1; i < _POLYCYL_TESS; i++)
		{
			norm[i][0] = norm[i - 1][0] * c - norm[i - 1][1] * s;
			norm[i][1] = norm[i - 1][0] * s + norm[i - 1][1] * c;
			circle[i][0] = rToroid * norm[i][0];
			circle[i][1] = rToroid * norm[i][1];
		}
		
		/**
		 * Make up vector point along x axis
		 */
		up[1] = up[2] = 0.0;
		up[0] = 1.0;
		
		/**
		 * Save and set the current join style
		 */
		int saveStyle = gleGetJoinStyle();
		int style = saveStyle;
		style |= TUBE_CONTOUR_CLOSED;
		style |= TUBE_NORM_PATH_EDGE;
		gleSetJoinStyle(style);
		
		/**
		 * Do the extrusion
		 */
		if(!glIsEnabled(GL_LIGHTING))
		{
			if(callback.equals("Spiral"))
			{
				gleSpiral(_POLYCYL_TESS, circle, null, up, startRadius, drdTheta, startZ, dzdTheta, startTransform, dTransformdTheta, startTheta, sweepTheta);
			} else
			{
				if(callback.equals("Lathe"))
				{
					gleLathe(_POLYCYL_TESS, circle, null, up, startRadius, drdTheta, startZ, dzdTheta, startTransform, dTransformdTheta, startTheta, sweepTheta);
				} else
				{
					throw new GLEException("Specified callback " + callback + " is not registered. Use either ``Spiral'' or ``Lathe''");
				}
			}
		} else
		{
			if(callback.equals("Spiral"))
			{
				gleSpiral(_POLYCYL_TESS, circle, norm, up, startRadius, drdTheta, startZ, dzdTheta, startTransform, dTransformdTheta, startTheta, sweepTheta);
			} else
			{
				if(callback.equals("Lathe"))
				{
					gleLathe(_POLYCYL_TESS, circle, norm, up, startRadius, drdTheta, startZ, dzdTheta, startTransform, dTransformdTheta, startTheta, sweepTheta);
				} else
				{
					throw new GLEException("Specified callback " + callback + " is not registered. Use either ``Spiral'' or ``Lathe''");
				}
			}
		}
		
		/**
		 * Restore the join style
		 */
		gleSetJoinStyle(saveStyle);
	}
	
	/**
	 * -- Begin ex_raw.c --
	 */
	
	/* The following routine is, in principle, very simple: all that it does is
	 * normalize the up vector, and makes sure that it is perpendicular to the
	 * initial polyline segment.
	 *
	 * In fact, this routine gets awfully complicated because: a) the first few
	 * segements of the polyline might be degenerate, b) up vecotr may be
	 * parallel to first few segments of polyline, c) etc. */
	private double[] up_sanity_check(double[] up, /* up vector for contour */
	        int npoints, /* numpoints in poly-line */
	        double[][] pointArray)
	{ /* polyline */
		
		double len = 0;
		double[] diff = null;
		double[] vtmp = null;
		
		/**
		 * now, right off the bat, we should make sure that the up vector is in
		 * fact perpendicular to the polyline direction
		 */
		diff = matrix.VEC_DIFF(pointArray[1], pointArray[0]);
		len = matrix.VEC_LENGTH(diff);
		if(len == 0.0)
		{
			/* loop till we find something that ain't of zero length */
			for(int i = 1; i < npoints - 2; i++)
			{
				diff = matrix.VEC_DIFF(pointArray[i + 1], pointArray[i]);
				len = matrix.VEC_LENGTH(diff);
				if(len != 0.0)
				{
					break;
				}
			}
		}
		
		/* normalize diff to be of unit length */
		len = 1.0 / len;
		diff = matrix.VEC_SCALE(len, diff);
		
		/* we want to take only perpendicular component of up w.r.t. the initial
		 * segment */
		vtmp = matrix.VEC_PERP(up, diff);
		
		len = matrix.VEC_LENGTH(vtmp);
		if(len == 0.0)
		{
			/* This error message should go through "official" error
			 * interface */
			System.err.println("Extrusion: Warning: ");
			System.err.println("contour up vector parallel to tubing direction");
			
			/* do like the error message says ... */
			vtmp = matrix.VEC_COPY(diff);
		}
		return vtmp;
	}
	
	/**
	 * Extrusion joining routines
	 */
	private final void extrusion_raw_join(int ncp, double[][] contour, double[][] contourNormal, double[] up, int npoints, double[][] pointArray, float[][] colourArray, double[][][] xformArray)
	{
		int i = 0, j = 0;
		int inext = 0;
		double[][] m = null;
		double len = 0;
		double[] diff = new double[3];
		double[] bi_0 = new double[3];
		double[] yup = new double[3];
		double[] nrmv = new double[3];
		boolean no_norm = (contourNormal == null), no_cols = (colourArray == null), no_xform = (xformArray == null);
		double[][] mem_anchor = null;
		double[][] front_loop = null;
		double[][] back_loop = null;
		double[][] front_norm = null;
		double[][] back_norm = null;
		double[][] tmp = null;
		
		/* System.err.println( "extrusion_raw_join(): no_norm: " + no_norm +
		 * "\tno_cols: " + no_cols + "\tno_xform: " + no_xform ); */
		
		nrmv[0] = nrmv[1] = 0.0;
		/**
		 * Used for drawing end caps
		 */
		/**
		 * Allocate loop arrays for transformations
		 */
		if(!no_xform)
		{
			front_loop = new double[ncp][3];
			back_loop = new double[ncp][3];
			front_norm = new double[ncp][3];
			back_norm = new double[ncp][3];
		}
		
		/* By definition, the contour passed in has its up vector pointing in
		 * the y direction */
		if(up == null)
		{
			yup[0] = 0.0;
			yup[1] = 1.0;
			yup[2] = 0.0;
		} else
		{
			yup = matrix.VEC_COPY(up);
		}
		up = matrix.VEC_COPY(yup);
		
		/* ========== "up" vector sanity check ========== */
		// System.err.println( "yup1 ( " + yup[0] + ", " + yup[1] + ", " +
		// yup[2] + " )" );
		yup = up_sanity_check(up, npoints, pointArray);
		// System.err.println( "yup2 ( " + yup[0] + ", " + yup[1] + ", " +
		// yup[2] + " )" );
		
		/* ignore all segments of zero length */
		i = 1;
		inext = i;
		inext = intersect.FIND_NON_DEGENERATE_POINT(inext, npoints, len, diff, pointArray);
		len = matrix.VEC_LENGTH(diff);
		
		/* first time through, get the loops */
		if(!no_xform)
		{
			for(j = 0; j < ncp; j++)
			{
				front_loop[j] = matrix.MAT_DOT_VEC_2X3(xformArray[inext - 1], contour[j]);
				front_loop[j][2] = 0.0;
			}
			if(!no_norm)
			{
				for(j = 0; j < ncp; j++)
				{
					front_norm[j] = matrix.NORM_XFORM_2X2(xformArray[inext - 1], contourNormal[j]);
					front_norm[j][2] = 0.0;
					back_norm[j][2] = 0.0;
				}
			}
		}
		
		/* draw tubing, not doing the first segment */
		while(inext < npoints - 1)
		{
			
			/* get the two bisecting planes */
			// System.err.println( "i: " + i + "\tinext: " + inext );
			bi_0 = intersect.bisecting_plane(pointArray[i - 1], pointArray[i], pointArray[inext]);
			// System.err.println( "bi_0: ( " + bi_0[0] + ", " + bi_0[1] + ", "
			// + bi_0[2] + " )" );
			
			/* reflect the up vector in the bisecting plane */
			yup = matrix.VEC_REFLECT(yup, bi_0);
			/* System.err.println( "VEC_REFLECT( " + yup[0] + ", " + yup[1] + ",
			 * " + yup[2] + " )" ); */
			
			/* rotate so that z-axis points down v2-v1 axis, and so that origen
			 * is at v1 */
			m = matrix.uviewpoint_d(pointArray[i], pointArray[inext], yup);
			DoubleBuffer mbuffer = BufferUtils.createDoubleBuffer(16);
			mbuffer.put(new double[] { m[0][0], m[0][1], m[0][2], m[0][3], m[1][0], m[1][1], m[1][2], m[1][3], m[2][0], m[2][1], m[2][2], m[2][3], m[3][0], m[3][1], m[3][2], m[3][3] });
			
			mbuffer.flip();
			glPushMatrix();
			glMultMatrix(mbuffer);
			
			/**
			 * There are six different cases we can have for presence and/or
			 * absecnce of colors and normals, and for interpretation of
			 * normals. The blechy set of nested if statements below branch to
			 * each of the six cases
			 */
			if(no_xform)
			{
				if(no_cols)
				{
					if(no_norm)
					{
						draw_raw_segment_plain(ncp, contour, inext, len);
					} else
					{
						if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
						{
							draw_raw_segment_facet_n(ncp, contour, contourNormal, inext, len);
						} else
						{
							draw_raw_segment_edge_n(ncp, contour, contourNormal, inext, len);
						}
					}
				} else
				{
					if(no_norm)
					{
						draw_raw_segment_color(ncp, contour, colourArray, inext, len);
					} else
					{
						if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
						{
							draw_raw_segment_c_and_facet_n(ncp, contour, colourArray, contourNormal, inext, len);
						} else
						{
							draw_raw_segment_c_and_edge_n(ncp, contour, colourArray, contourNormal, inext, len);
						}
					}
				}
			} else
			{
				/* else -- there are scales and offsets to deal with */
				for(j = 0; j < ncp; j++)
				{
					back_loop[j] = matrix.MAT_DOT_VEC_2X3(xformArray[inext], contour[j]);
					back_loop[j][2] = -len;
					front_loop[j][2] = 0.0;
				}
				
				if(!no_norm)
				{
					for(j = 0; j < ncp; j++)
					{
						back_norm[j] = matrix.NORM_XFORM_2X2(xformArray[inext], contourNormal[j]);
					}
				}
				
				if(no_cols)
				{
					if(no_norm)
					{
						draw_segment_plain(ncp, front_loop, back_loop, inext, len);
					} else
					{
						if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
						{
							draw_binorm_segment_facet_n(ncp, front_loop, back_loop, front_norm, back_norm, inext, len);
						} else
						{
							draw_binorm_segment_edge_n(ncp, front_loop, back_loop, front_norm, back_norm, inext, len);
						}
					}
					
					if((gleGetJoinStyle() & TUBE_JN_CAP) == TUBE_JN_CAP)
					{
						nrmv[2] = 1.0;
						glNormal3d(nrmv[0], nrmv[1], nrmv[2]);
						draw_front_contour_cap(ncp, front_loop);
						nrmv[2] = -1.0;
						glNormal3d(nrmv[0], nrmv[1], nrmv[2]);
						draw_back_contour_cap(ncp, back_loop);
					}
				} else
				{
					if(no_norm)
					{
						draw_segment_color(ncp, front_loop, back_loop, colourArray[inext - 1], colourArray[inext], inext, len);
					} else
					{
						if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
						{
							draw_binorm_segment_c_and_facet_n(ncp, front_loop, back_loop, front_norm, back_norm, colourArray[inext - 1], colourArray[inext], inext, len);
						} else
						{
							draw_binorm_segment_c_and_edge_n(ncp, front_loop, back_loop, front_norm, back_norm, colourArray[inext - 1], colourArray[inext], inext, len);
						}
					}
					
					if((gleGetJoinStyle() & TUBE_JN_CAP) == TUBE_JN_CAP)
					{
						glColor3f(colourArray[inext - 1][0], colourArray[inext - 1][1], colourArray[inext - 1][2]);
						nrmv[2] = 1.0;
						glNormal3d(nrmv[0], nrmv[1], nrmv[2]);
						draw_front_contour_cap(ncp, front_loop);
						
						glColor3f(colourArray[inext][0], colourArray[inext][1], colourArray[inext][2]);
						nrmv[2] = -1.0;
						glNormal3d(nrmv[0], nrmv[1], nrmv[2]);
						draw_back_contour_cap(ncp, back_loop);
					}
				}
			}
			
			/* pop this matrix, do the next set */
			glPopMatrix();
			
			/* flop over transformed loops */
			tmp = front_loop;
			front_loop = back_loop;
			back_loop = tmp;
			tmp = front_norm;
			front_norm = back_norm;
			back_norm = tmp;
			
			i = inext;
			/* ignore all segments of zero length */
			inext = intersect.FIND_NON_DEGENERATE_POINT(inext, npoints, len, diff, pointArray);
			len = matrix.VEC_LENGTH(diff);
		}
		
		/* free previously allocated memory, if any */
	}
	
	/**
	 * This routine draws a segment of raw-join-style tubing. Essentially, we
	 * assume that the proper transformations have already been performed to
	 * properly orient the tube segment -- our only task left is to render PLAIN
	 * - NO COLOUR, NO NORMAL
	 */
	private final void draw_raw_segment_plain(int ncp, double[][] contour, int inext, double len)
	{
		int j;
		double[] point = new double[3];
		
		// System.err.println( "draw_raw_segment_plain()" );
		
		/* draw the tube segment */
		glBegin(GL_TRIANGLE_STRIP);
		for(j = 0; j < ncp; j++)
		{
			point[0] = contour[j][0];
			point[1] = contour[j][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]); /* , j, FRONT); */
			
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]); /* , j, BACK); */
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			/* connect back up to first point of contour */
			point[0] = contour[0][0];
			point[1] = contour[0][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]); /* , 0, FRONT); */
			
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]); /* 0, BACK); */
		}
		glEnd();
		
		/* draw the endcaps, if the join style calls for it */
		if((gleGetJoinStyle() & TUBE_JN_CAP) == TUBE_JN_CAP)
		{
			/* draw the front cap */
			draw_raw_style_end_cap(ncp, contour, 0.0, true);
			
			/* draw the back cap */
			draw_raw_style_end_cap(ncp, contour, -len, false);
		}
	}
	
	/**
	 * This routine draws a segment of raw-join-style tubing. Essentially, we
	 * assume that the proper transformations have already been performed to
	 * properly orient the tube segment -- our only task left is to render
	 *
	 * COLOUR -- DRAW ONLY COLOUR!
	 */
	private final void draw_raw_segment_color(int ncp, double[][] contour, float[][] color_array, int inext, double len)
	{
		
		double[] point = new double[3];
		
		/* draw the tube segment */
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp; j++)
		{
			point[0] = contour[j][0];
			point[1] = contour[j][1];
			point[2] = 0.0;
			glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
			glVertex3d(point[0], point[1], point[2]); /* , j, FRONT); */
			
			point[2] = -len;
			glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
			glVertex3d(point[0], point[1], point[2]); /* , j, BACK); */
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			/* connect back up to first point of contour */
			point[0] = contour[0][0];
			point[1] = contour[0][1];
			point[2] = 0.0;
			
			glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
			glVertex3d(point[0], point[1], point[2]); /* , 0, FRONT); */
			
			point[2] = -len;
			glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
			glVertex3d(point[0], point[1], point[2]); /* , 0, BACK); */
		}
		
		glEnd();
		
		/* draw the endcaps, if the join style calls for it */
		if((gleGetJoinStyle() & TUBE_JN_CAP) == TUBE_JN_CAP)
		{
			
			/* draw the front cap */
			glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
			draw_raw_style_end_cap(ncp, contour, 0.0, true);
			
			/* draw the back cap */
			glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
			draw_raw_style_end_cap(ncp, contour, -len, false);
		}
	}
	
	/**
	 * This routine draws a segment of raw-join-style tubing. Essentially, we
	 * assume that the proper transformations have already been performed to
	 * properly orient the tube segment -- our only task left is to render
	 *
	 *
	 */
	private final void draw_raw_segment_edge_n(int ncp, double[][] contour, double[][] cont_normal, int inext, double len)
	{
		
		double[] point = new double[3];
		double[] norm = new double[3];
		
		// System.err.println( "draw_raw_segment_edge_n" );
		
		/* draw the tube segment */
		norm[2] = 0.0;
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp; j++)
		{
			norm[0] = cont_normal[j][0];
			norm[1] = cont_normal[j][1];
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[0] = contour[j][0];
			point[1] = contour[j][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , j, FRONT);
			 */
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]); /* , j, BACK); */
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			/* connect back up to first point of contour */
			norm[0] = cont_normal[0][0];
			norm[1] = cont_normal[0][1];
			norm[2] = 0.0;
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[0] = contour[0][0];
			point[1] = contour[0][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]); /* , 0, FRONT); */
			
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]); /* , 0, BACK); */
		}
		glEnd();
		
		/* draw the endcaps, if the join style calls for it */
		if((gleGetJoinStyle() & TUBE_JN_CAP) == TUBE_JN_CAP)
		{
			/* draw the front cap */
			norm[0] = norm[1] = 0.0;
			norm[2] = 1.0;
			glNormal3d(norm[0], norm[1], norm[2]);
			draw_raw_style_end_cap(ncp, contour, 0.0, true);
			
			/* draw the back cap */
			norm[2] = -1.0;
			glNormal3d(norm[0], norm[1], norm[2]);
			draw_raw_style_end_cap(ncp, contour, -len, false);
		}
	}
	
	/**
	 * This routine draws a segment of raw-join-style tubing. Essentially, we
	 * assume that the proper transformations have already been performed to
	 * properly orient the tube segment -- our only task left is to render
	 *
	 * C_AND_EDGE_N -- DRAW EDGE NORMALS AND COLORS
	 */
	private final void draw_raw_segment_c_and_edge_n(int ncp, double[][] contour, float[][] color_array, double[][] cont_normal, int inext, double len)
	{
		double[] point = new double[3];
		double[] norm = new double[3];
		
		/* draw the tube segment */
		norm[2] = 0.0;
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp; j++)
		{
			glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
			
			norm[0] = cont_normal[j][0];
			norm[1] = cont_normal[j][1];
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[0] = contour[j][0];
			point[1] = contour[j][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , j, FRONT);
			 */
			glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , j, BACK);
			 */
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			/* connect back up to first point of contour */
			glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
			
			norm[0] = cont_normal[0][0];
			norm[1] = cont_normal[0][1];
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[0] = contour[0][0];
			point[1] = contour[0][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , 0, FRONT);
			 */
			glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
			norm[0] = cont_normal[0][0];
			norm[1] = cont_normal[0][1];
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , 0, BACK);
			 */
		}
		glEnd();
		
		/* draw the endcaps, if the join style calls for it */
		if((gleGetJoinStyle() & TUBE_JN_CAP) == TUBE_JN_CAP)
		{
			
			/* draw the front cap */
			glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
			norm[0] = norm[1] = 0.0;
			norm[2] = 1.0;
			glNormal3d(norm[0], norm[1], norm[2]);
			draw_raw_style_end_cap(ncp, contour, 0.0, true);
			
			/* draw the back cap */
			glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
			norm[2] = -1.0;
			glNormal3d(norm[0], norm[1], norm[2]);
			draw_raw_style_end_cap(ncp, contour, -len, false);
		}
	}
	
	/**
	 * This routine draws a segment of raw-join-style tubing. Essentially, we
	 * assume that the proper transformations have already been performed to
	 * properly orient the tube segment -- our only task left is to render
	 *
	 * FACET_N -- DRAW ONLY FACET NORMALS
	 */
	private final void draw_raw_segment_facet_n(int ncp, double[][] contour, double[][] cont_normal, int inext, double len)
	{
		double[] point = new double[3];
		double[] norm = new double[3];
		
		/* draw the tube segment */
		norm[2] = 0.0;
		
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp - 1; j++)
		{
			/* facet normals require one normal per four vertices */
			norm[0] = cont_normal[j][0];
			norm[1] = cont_normal[j][1];
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[0] = contour[j][0];
			point[1] = contour[j][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , j, FRONT);
			 */
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , j, BACK);
			 */
			point[0] = contour[j + 1][0];
			point[1] = contour[j + 1][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , j+1, FRONT);
			 */
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , j+1, BACK);
			 */
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			/* connect back up to first point of contour */
			norm[0] = cont_normal[ncp - 1][0];
			norm[1] = cont_normal[ncp - 1][1];
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[0] = contour[ncp - 1][0];
			point[1] = contour[ncp - 1][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , ncp-1, FRONT);
			 */
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , ncp-1, BACK);
			 */
			point[0] = contour[0][0];
			point[1] = contour[0][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , 0, FRONT);
			 */
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , 0, BACK);
			 */
		}
		glEnd();
		
		/* draw the endcaps, if the join style calls for it */
		if((gleGetJoinStyle() & TUBE_JN_CAP) == TUBE_JN_CAP)
		{
			
			/* draw the front cap */
			norm[0] = norm[1] = 0.0;
			norm[2] = 1.0;
			glNormal3d(norm[0], norm[1], norm[2]);
			draw_raw_style_end_cap(ncp, contour, 0.0, true);
			
			/* draw the back cap */
			norm[2] = -1.0;
			glNormal3d(norm[0], norm[1], norm[2]);
			draw_raw_style_end_cap(ncp, contour, -len, false);
		}
	}
	
	/**
	 * This routine draws a segment of raw-join-style tubing. Essentially, we
	 * assume that the proper transformations have already been performed to
	 * properly orient the tube segment -- our only task left is to render
	 *
	 * C_AND_FACET_N -- DRAW FACET NORMALS AND COLOURS
	 */
	private final void draw_raw_segment_c_and_facet_n(int ncp, double[][] contour, float[][] color_array, double[][] cont_normal, int inext, double len)
	{
		
		double[] point = new double[3];
		double[] norm = new double[3];
		
		/* draw the tube segment */
		norm[2] = 0.0;
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp - 1; j++)
		{
			/* facet normals require one normal per four vertices; However, we
			 * have to respecify normal each time at each vertex so that the
			 * lighting equation gets triggered. (V3F does NOT automatically
			 * trigger the lighting equations -- it only triggers when there is
			 * a local light) */
			
			glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
			
			norm[0] = cont_normal[j][0];
			norm[1] = cont_normal[j][1];
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[0] = contour[j][0];
			point[1] = contour[j][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , j, FRONT);
			 */
			glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
			glNormal3d(norm[0], norm[1], norm[2]);
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , j, BACK);
			 */
			glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[0] = contour[j + 1][0];
			point[1] = contour[j + 1][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , j+1, FRONT);
			 */
			glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
			glNormal3d(norm[0], norm[1], norm[2]);
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , j+1, BACK);
			 */
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			/* connect back up to first point of contour */
			point[0] = contour[ncp - 1][0];
			point[1] = contour[ncp - 1][1];
			point[2] = 0.0;
			glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
			
			norm[0] = cont_normal[ncp - 1][0];
			norm[1] = cont_normal[ncp - 1][1];
			glNormal3d(norm[0], norm[1], norm[2]);
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , ncp-1, FRONT);
			 */
			glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , ncp-1, BACK);
			 */
			glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
			
			norm[0] = cont_normal[0][0];
			norm[1] = cont_normal[0][1];
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[0] = contour[0][0];
			point[1] = contour[0][1];
			point[2] = 0.0;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , 0, FRONT);
			 */
			glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
			glNormal3d(norm[0], norm[1], norm[2]);
			
			point[2] = -len;
			glVertex3d(point[0], point[1], point[2]);
			/**
			 * , 0, BACK);
			 */
		}
		
		glEnd();
		
		/* draw the endcaps, if the join style calls for it */
		if((gleGetJoinStyle() & TUBE_JN_CAP) == TUBE_JN_CAP)
		{
			/* draw the front cap */
			glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
			norm[0] = norm[1] = 0.0;
			norm[2] = 1.0;
			glNormal3d(norm[0], norm[1], norm[2]);
			draw_raw_style_end_cap(ncp, contour, 0.0, true);
			
			/* draw the back cap */
			glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
			norm[2] = -1.0;
			glNormal3d(norm[0], norm[1], norm[2]);
			draw_raw_style_end_cap(ncp, contour, -len, false);
		}
	}
	
	/**
	 * This routine does what it says: It draws the end-caps for the "raw" join
	 * style.
	 */
	private final void draw_raw_style_end_cap(int ncp, double[][] contour, double zval, boolean frontwards)
	{
		
		GLUtessellator tobj = GLU.gluNewTess();
		tobj.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
		tobj.gluTessCallback(GLU.GLU_TESS_VERTEX, tessCallback);// glVertex3dv);
		tobj.gluTessCallback(GLU.GLU_TESS_BEGIN, tessCallback);// beginCallback);
		tobj.gluTessCallback(GLU.GLU_TESS_END, tessCallback);// endCallback);
		tobj.gluTessCallback(GLU.GLU_TESS_ERROR, tessCallback);// errorCallback);
		
		// glu_.gluBeginPolygon( tobj );
		tobj.gluTessBeginPolygon(null);
		tobj.gluTessBeginContour();
		
		/* draw the loop counter clockwise for the front cap */
		if(frontwards)
		{
			for(int j = 0; j < ncp; j++)
			{
				double[] vertex = new double[3];
				vertex[0] = contour[j][0];
				vertex[1] = contour[j][1];
				vertex[2] = zval;
				// glu_.gluTessVertex( tobj, vertex, vertex );
				tobj.gluTessVertex(vertex, 0, vertex);
			}
		} else
		{
			/* the sense of the loop is reversed for backfacing culling */
			for(int j = ncp - 1; j > -1; j--)
			{
				double[] vertex = new double[3];
				vertex[0] = contour[j][0];
				vertex[1] = contour[j][1];
				vertex[2] = zval;
				// glu_.gluTessVertex( tobj, vertex, vertex );
				tobj.gluTessVertex(vertex, 0, vertex);
			}
		}
		
		tobj.gluTessEndContour();
		tobj.gluTessEndPolygon();
		tobj.gluDeleteTess();
	}
	
	/**
	 * This routine does what it says: It draws a counter-clockwise cap from a
	 * 3D contour loop list
	 */
	private final void draw_front_contour_cap(int ncp, double[][] contour)
	{
		
		GLUtessellator tobj = GLU.gluNewTess();
		tobj.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
		tobj.gluTessCallback(GLU.GLU_TESS_VERTEX, tessCallback);// glVertex3dv);
		tobj.gluTessCallback(GLU.GLU_TESS_BEGIN, tessCallback);// beginCallback);
		tobj.gluTessCallback(GLU.GLU_TESS_END, tessCallback);// endCallback);
		tobj.gluTessCallback(GLU.GLU_TESS_ERROR, tessCallback);// errorCallback);
		
		// glu_.gluBeginPolygon( tobj );
		tobj.gluTessBeginPolygon(null);
		tobj.gluTessBeginContour();
		
		for(int j = 0; j < ncp; j++)
		{
			// glu_.gluTessVertex( tobj, contour[j], contour[j] );
			tobj.gluTessVertex(contour[j], 0, contour[j]);
		}
		// glu_.gluEndPolygon( tobj );
		// glu_.gluDeleteTess( tobj );
		tobj.gluTessEndContour();
		tobj.gluTessEndPolygon();
		tobj.gluDeleteTess();
	}
	
	/* This routine does what it says: It draws a clockwise cap from a 3D
	 * contour loop list */
	private final void draw_back_contour_cap(int ncp, double[][] contour)
	{
		
		GLUtessellator tobj = GLU.gluNewTess();
		tobj.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_POSITIVE);
		tobj.gluTessCallback(GLU.GLU_TESS_VERTEX, tessCallback);// glVertex3dv);
		tobj.gluTessCallback(GLU.GLU_TESS_BEGIN, tessCallback);// beginCallback);
		tobj.gluTessCallback(GLU.GLU_TESS_END, tessCallback);// endCallback);
		tobj.gluTessCallback(GLU.GLU_TESS_ERROR, tessCallback);// errorCallback);
		
		// glu_.gluBeginPolygon( tobj );
		tobj.gluTessBeginPolygon(null);
		tobj.gluTessBeginContour();
		
		/* draw the end cap */
		/* draw the loop clockwise for the back cap */
		/* the sense of the loop is reversed for backfacing culling */
		for(int j = ncp - 1; j > -1; j--)
		{
			// glu_.gluTessVertex( tobj, contour[j], contour[j] );
			tobj.gluTessVertex(contour[j], 0, contour[j]);
		}
		// glu_.gluEndPolygon( tobj );
		// glu_.gluDeleteTess( tobj );
		tobj.gluTessEndContour();
		tobj.gluTessEndPolygon();
		tobj.gluDeleteTess();
	}
	
	/**
	 * -- End ex_raw.c --
	 */
	/**
	 * -- Begin ex_angle.c --
	 */
	/* Algorithmic trivia:
	 *
	 * There is a slight bit of trivia which the super-duper exacto coder needs
	 * to know about the code in this module. It is this:
	 *
	 * This module attempts to correctly treat contour normal vectors by
	 * applying the inverse transpose of the 2D contour affine transformation to
	 * the 2D contour normals. This is perfectly correct, when applied to the
	 * "raw" join style. However, if the affine transform has a strong
	 * rotational component, AND the join style is angle or cut, then the normal
	 * vectors would continue to rotate as the intersect point is extrapolated.
	 *
	 * The extrapolation of the inverse-transpose matrix to the intersection
	 * point is not done. This would appear to be overkill for most situations.
	 * The viewer might possibly detect an artifact of the failure to do this
	 * correction IF all three of the following criteria were met: 1) The affine
	 * xform has a strong rotational component, 2) The angle between two
	 * succesive segments is sharp (greater than 15 or 30 degrees). 3) The join
	 * style is angle or cut.
	 *
	 * However, I beleive that it is highly unlikely that the viewer will detect
	 * any artifacts. The reason I beleive this is that a strong rotational
	 * component will twist a segment so strongly that the more visible artifact
	 * will be that a segment is composed of triangle strips. As the user
	 * attempts to minimize the tesselation artifacts by shortening segments,
	 * then the rotational component will decrease in proportion, and the
	 * lighting artifact will fall away.
	 *
	 * To summarize, there is a slight inexactness in this code. The author of
	 * the code beleives that this inexactness results in miniscule errors in
	 * every situation.
	 *
	 * Linas Vepstas March 1993 */
	private final void extrusion_angle_join(int ncp, double[][] contour, double[][] cont_normal, double[] up, int npoints, double[][] point_array, float[][] color_array, double[][][] xform_array)
	{
		int i = 0, j = 0;
		int inext = 0, inextnext = 0;
		double[][] m = new double[4][4];
		double len = 0;
		double len_seg = 0;
		double[] diff = new double[3];
		double[] bi_0 = new double[3],
		        bi_1 = new double[3]; /* bisecting plane */
		double[] bisector_0 = new double[3],
		        bisector_1 = new double[3]; /* bisecting plane */
		double[] end_point_0 = new double[3], end_point_1 = new double[3];
		double[] origin = new double[3], neg_z = new double[3];
		double[] yup = new double[3]; /* alternate up vector */
		double[][] front_loop, back_loop; /* contours in 3D */
		double[][] norm_loop;
		double[][] front_norm, /* contour normals in 3D */
		        back_norm, tmp;
		boolean first_time;
		
		/**
		 * By definition, the contour passed in has its up vector pointing in
		 * the y direction
		 */
		if(up == null)
		{
			yup[0] = 0.0;
			yup[1] = 1.0;
			yup[2] = 0.0;
		} else
		{
			yup = matrix.VEC_COPY(up);
		}
		
		/* ========== "up" vector sanity check ========== */
		yup = up_sanity_check(yup, npoints, point_array);
		
		/* the origin is at the origin */
		origin[0] = 0.0;
		origin[1] = 0.0;
		origin[2] = 0.0;
		
		/* and neg_z is at neg z */
		neg_z[0] = 0.0;
		neg_z[1] = 0.0;
		neg_z[2] = 1.0;
		
		/* ignore all segments of zero length */
		i = 1;
		inext = i;
		inext = intersect.FIND_NON_DEGENERATE_POINT(inext, npoints, len, diff, point_array);
		len = matrix.VEC_LENGTH(diff);
		len_seg = len; /* store for later use */
		
		/* get the bisecting plane */
		bi_0 = intersect.bisecting_plane(point_array[0], point_array[1], point_array[inext]);
		
		/* reflect the up vector in the bisecting plane */
		yup = matrix.VEC_REFLECT(yup, bi_0);
		
		/* malloc the storage we'll need for relaying changed contours to the
		 * drawing routines. */
		/* mem_anchor = malloc (2 * 3 * ncp * sizeof(double) + 2 * 3 * ncp *
		 * sizeof(gleDouble)); */
		front_loop = new double[ncp][3];
		back_loop = new double[ncp][3];
		front_norm = new double[ncp][3];
		back_norm = new double[ncp][3];
		norm_loop = front_norm;
		
		/* may as well get the normals set up now */
		if(cont_normal != null)
		{
			if(xform_array == null)
			{
				for(j = 0; j < ncp; j++)
				{
					norm_loop[j][0] = cont_normal[j][0];
					norm_loop[j][1] = cont_normal[j][1];
					norm_loop[j][2] = 0.0;
				}
			} else
			{
				for(j = 0; j < ncp; j++)
				{
					front_norm[j] = matrix.NORM_XFORM_2X2(xform_array[inext - 1], cont_normal[j]);
					front_norm[j][2] = 0.0;
					back_norm[j][2] = 0.0;
				}
			}
		}
		
		first_time = true;
		/* draw tubing, not doing the first segment */
		while(inext < npoints - 1)
		{
			inextnext = inext;
			/* ignore all segments of zero length */
			inextnext = intersect.FIND_NON_DEGENERATE_POINT(inextnext, npoints, len, diff, point_array);
			len = matrix.VEC_LENGTH(diff);
			
			/* get the next bisecting plane */
			bi_1 = intersect.bisecting_plane(point_array[i], point_array[inext], point_array[inextnext]);
			
			/**
			 * rotate so that z-axis points down v2-v1 axis, and so that origen
			 * is at v1
			 */
			m = matrix.uviewpoint_d(point_array[i], point_array[inext], yup);
			
			DoubleBuffer mbuffer = BufferUtils.createDoubleBuffer(16);
			
			mbuffer.put(new double[] { m[0][0], m[0][1], m[0][2], m[0][3], m[1][0], m[1][1], m[1][2], m[1][3], m[2][0], m[2][1], m[2][2], m[2][3], m[3][0], m[3][1], m[3][2], m[3][3] });
			
			mbuffer.flip();
			glPushMatrix();
			glMultMatrix(mbuffer);
			
			/* rotate the bisecting planes into the local coordinate system */
			bisector_0 = matrix.MAT_DOT_VEC_3X3(m, bi_0);
			bisector_1 = matrix.MAT_DOT_VEC_3X3(m, bi_1);
			
			neg_z[2] = -len_seg;
			
			/* draw the tube */
			/* --------- START OF TMESH GENERATION -------------- */
			for(j = 0; j < ncp; j++)
			{
				
				/* if there are normals, and there are either affine xforms, OR
				 * path-edge normals need to be drawn, then compute local
				 * coordinate system normals. */
				if(cont_normal != null)
				{
					
					/**
					 * set up the back normals. (The front normals we inherit
					 * from previous pass through the loop)
					 */
					if(xform_array != null)
					{
						/* do up the normal vectors with the inverse
						 * transpose */
						back_norm[j] = matrix.NORM_XFORM_2X2(xform_array[inext], cont_normal[j]);
					}
					
					/**
					 * Note that if the xform array is null, then normals are
					 * constant, and are set up outside of the loop.
					 */
					
					/* if there are normal vectors, and the style calls for it,
					 * then we want to project the normal vectors into the
					 * bisecting plane. (This style is needed to make toroids,
					 * etc. look good: Without this, segmentation artifacts show
					 * up under lighting. */
					if((gleGetJoinStyle() & TUBE_NORM_PATH_EDGE) == TUBE_NORM_PATH_EDGE)
					{
						/**
						 * Hmm, if no affine xforms, then we haven't yet set
						 * back vector. So do it.
						 */
						if(xform_array == null)
						{
							back_norm[j][0] = cont_normal[j][0];
							back_norm[j][1] = cont_normal[j][1];
						}
						
						/**
						 * now, start with a fresh normal (z component equal to
						 * zero), project onto bisecting plane (by computing
						 * perpendicular componenet to bisect vector, and
						 * renormalize (since projected vector is not of unit
						 * length
						 */
						front_norm[j][2] = 0.0;
						front_norm[j] = matrix.VEC_PERP(front_norm[j], bisector_0);
						front_norm[j] = matrix.VEC_NORMALIZE(front_norm[j]);
						
						back_norm[j][2] = 0.0;
						back_norm[j] = matrix.VEC_PERP(back_norm[j], bisector_1);
						back_norm[j] = matrix.VEC_NORMALIZE(back_norm[j]);
					}
				}
				
				/* Next, we want to define segements. We find the endpoints of
				 * the segments by intersecting the contour with the bisecting
				 * plane. If there is no local affine transform, this is easy.
				 *
				 * If there is an affine tranform, then we want to remove the
				 * torsional component, so that the intersection points won't
				 * get twisted out of shape. We do this by applying the local
				 * affine transform to the entire coordinate system. */
				if(xform_array == null)
				{
					end_point_0[0] = contour[j][0];
					end_point_0[1] = contour[j][1];
					
					end_point_1[0] = contour[j][0];
					end_point_1[1] = contour[j][1];
				} else
				{
					/* transform the contour points with the local xform */
					end_point_0 = matrix.MAT_DOT_VEC_2X3(xform_array[inext - 1], contour[j]);
					end_point_1 = matrix.MAT_DOT_VEC_2X3(xform_array[inext - 1], contour[j]);
				}
				
				end_point_0[2] = 0.0;
				end_point_1[2] = -len_seg;
				
				/* The two end-points define a line. Intersect this line against
				 * the clipping plane defined by the PREVIOUS tube segment. */
				front_loop[j] = intersect.INNERSECT(origin, /* point on
				                                             * intersecting
				                                             * plane */
				        bisector_0, /* normal vector to plane */
				        end_point_0, /* point on line */
				        end_point_1); /* another point on the line */
				
				/* The two end-points define a line. Intersect this line against
				 * the clipping plane defined by the NEXT tube segment. */
				
				/* if there's an affine coordinate change, be sure to use it */
				if(xform_array != null)
				{
					/* transform the contour points with the local xform */
					end_point_0 = matrix.MAT_DOT_VEC_2X3(xform_array[inext], contour[j]);
					end_point_1 = matrix.MAT_DOT_VEC_2X3(xform_array[inext], contour[j]);
					// System.err.println( "end_point_0: ( " + end_point_0[0] +
					// ", " + end_point_0[1] + ", " + end_point_0[2] + " )" );
					// System.err.println( "end_point_1: ( " + end_point_1[0] +
					// ", " + end_point_1[1] + ", " + end_point_1[2] + " )" );
				}
				end_point_0[2] = 0.0;
				end_point_1[2] = -len_seg;
				
				back_loop[j] = intersect.INNERSECT(neg_z, /* point on
				                                           * intersecting
				                                           * plane */
				        bisector_1, /* normal vector to plane */
				        end_point_0, /* point on line */
				        end_point_1); /* another point on the line */
			}
			
			/* --------- END OF TMESH GENERATION -------------- */
			
			/* v^v^v^v^v^v^v^v^v BEGIN END CAPS v^v^v^v^v^v^v^v^v^v^v^v */
			
			/**
			 * if end caps are required, draw them. But don't draw any but the
			 * very first and last caps
			 */
			if((gleGetJoinStyle() & TUBE_JN_CAP) == TUBE_JN_CAP)
			{
				if(first_time)
				{
					if(color_array != null)
					{
						glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
					}
					first_time = false;
					draw_angle_style_front_cap(ncp, bisector_0, front_loop);
				}
				if(inext == npoints - 2)
				{
					if(color_array != null)
					{
						glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
					}
					draw_angle_style_back_cap(ncp, bisector_1, back_loop);
				}
			}
			/* v^v^v^v^v^v^v^v^v END END CAPS v^v^v^v^v^v^v^v^v^v^v^v */
			
			/* |||||||||||||||||| START SEGMENT DRAW |||||||||||||||||||| */
			/**
			 * There are six different cases we can have for presence and/or
			 * absecnce of colors and normals, and for interpretation of
			 * normals. The blechy set of nested if statements below branch to
			 * each of the six cases
			 */
			if((xform_array == null) && (!((gleGetJoinStyle() & TUBE_NORM_PATH_EDGE) == TUBE_NORM_PATH_EDGE)))
			{
				if(color_array == null)
				{
					if(cont_normal == null)
					{
						draw_segment_plain(ncp, front_loop, back_loop, inext, len_seg);
					} else
					{
						if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
						{
							draw_segment_facet_n(ncp, front_loop, back_loop, norm_loop, inext, len_seg);
						} else
						{
							draw_segment_edge_n(ncp, front_loop, back_loop, norm_loop, inext, len_seg);
						}
					}
				} else
				{
					if(cont_normal == null)
					{
						draw_segment_color(ncp, front_loop, back_loop, color_array[inext - 1], color_array[inext], inext, len_seg);
					} else
					{
						if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
						{
							draw_segment_c_and_facet_n(ncp, front_loop, back_loop, norm_loop, color_array[inext - 1], color_array[inext], inext, len_seg);
						} else
						{
							draw_segment_c_and_edge_n(ncp, front_loop, back_loop, norm_loop, color_array[inext - 1], color_array[inext], inext, len_seg);
						}
					}
				}
			} else
			{
				if(color_array == null)
				{
					if(cont_normal == null)
					{
						draw_segment_plain(ncp, front_loop, back_loop, inext, len_seg);
					} else
					{
						if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
						{
							draw_binorm_segment_facet_n(ncp, front_loop, back_loop, front_norm, back_norm, inext, len_seg);
						} else
						{
							draw_binorm_segment_edge_n(ncp, front_loop, back_loop, front_norm, back_norm, inext, len_seg);
						}
					}
				} else
				{
					if(cont_normal == null)
					{
						draw_segment_color(ncp, front_loop, back_loop, color_array[inext - 1], color_array[inext], inext, len_seg);
					} else
					{
						if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
						{
							draw_binorm_segment_c_and_facet_n(ncp, front_loop, back_loop, front_norm, back_norm, color_array[inext - 1], color_array[inext], inext, len_seg);
						} else
						{
							draw_binorm_segment_c_and_edge_n(ncp, front_loop, back_loop, front_norm, back_norm, color_array[inext - 1], color_array[inext], inext, len_seg);
						}
					}
				}
			}
			/* |||||||||||||||||| END SEGMENT DRAW |||||||||||||||||||| */
			
			/* pop this matrix, do the next set */
			glPopMatrix();
			
			/* bump everything to the next vertex */
			len_seg = len;
			i = inext;
			inext = inextnext;
			bi_0 = matrix.VEC_COPY(bi_1);
			
			/* trade norm loops */
			tmp = front_norm;
			front_norm = back_norm;
			back_norm = tmp;
			
			/* reflect the up vector in the bisecting plane */
			yup = matrix.VEC_REFLECT(yup, bi_0);
		}
	}
	
	/**
	 * Draws an angle style front cap
	 */
	private final void draw_angle_style_front_cap(int ncp, double[] bi, double[][] point_array)
	{
		GLUtessellator tobj = GLU.gluNewTess();
		tobj.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
		tobj.gluTessCallback(GLU.GLU_TESS_VERTEX, tessCallback);// glVertex3dv);
		tobj.gluTessCallback(GLU.GLU_TESS_BEGIN, tessCallback);// beginCallback);
		tobj.gluTessCallback(GLU.GLU_TESS_END, tessCallback);// endCallback);
		tobj.gluTessCallback(GLU.GLU_TESS_ERROR, tessCallback);// errorCallback);
		
		if(bi[2] < 0.0)
		{
			bi = matrix.VEC_SCALE(-1.0, bi);
		}
		
		glNormal3d(bi[0], bi[1], bi[2]);
		// glu_.gluBeginPolygon( tobj );
		// tobj.gluBeginPolygon();
		tobj.gluTessBeginPolygon(null);
		tobj.gluTessBeginContour();
		for(int j = 0; j < ncp; j++)
		{
			// glu_.gluTessVertex( tobj, point_array[j], point_array[j] );
			tobj.gluTessVertex(point_array[j], 0, point_array[j]);
		}
		
		tobj.gluTessEndContour();
		tobj.gluTessEndPolygon();
		tobj.gluDeleteTess();
	}
	
	/**
	 * Draws an angle style back cap
	 */
	private final void draw_angle_style_back_cap(int ncp, double[] bi, double[][] point_array)
	{
		
		GLUtessellator tobj = GLU.gluNewTess();
		tobj.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
		tobj.gluTessCallback(GLU.GLU_TESS_VERTEX, tessCallback);// glVertex3dv);
		tobj.gluTessCallback(GLU.GLU_TESS_BEGIN, tessCallback);// beginCallback);
		tobj.gluTessCallback(GLU.GLU_TESS_END, tessCallback);// endCallback);
		tobj.gluTessCallback(GLU.GLU_TESS_ERROR, tessCallback);// errorCallback);
		
		if(bi[2] > 0.0)
		{
			bi = matrix.VEC_SCALE(-1.0, bi);
		}
		
		glNormal3d(bi[0], bi[1], bi[2]);
		
		tobj.gluTessBeginPolygon(null);
		tobj.gluTessBeginContour();
		for(int j = ncp - 1; j >= 0; j--)
		{
			
			tobj.gluTessVertex(point_array[j], 0, point_array[j]);
		}
		
		tobj.gluTessEndContour();
		tobj.gluTessEndPolygon();
		tobj.gluDeleteTess();
	}
	
	/**
	 * -- End ex_angle.c --
	 */
	/**
	 * -- Begin segment.c --
	 */
	private final void draw_segment_plain(int ncp, double[][] front_contour, double[][] back_contour, int inext, double len)
	{
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp; j++)
		{
			glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
			glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
			glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
		}
		glEnd();
	}
	
	private final void draw_segment_color(int ncp, double[][] front_contour, double[][] back_contour, float[] color_last, float[] color_next, int inext, double len)
	{
		
		/* draw the tube segment */
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp; j++)
		{
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			/* connect back up to first point of contour */
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
		}
		
		glEnd();
	}
	
	private final void draw_segment_edge_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] norm_cont, int inext, double len)
	{
		
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp; j++)
		{
			glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
			glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
			glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			glNormal3d(norm_cont[0][0], norm_cont[0][1], norm_cont[0][2]);
			glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
			glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
		}
		glEnd();
	}
	
	private final void draw_segment_c_and_edge_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] norm_cont, float[] color_last, float[] color_next, int inext, double len)
	{
		
		// System.err.println( "draw_segment_c_and_edge_n()" );
		
		glBegin(GL_TRIANGLE_STRIP);
		
		for(int j = 0; j < ncp; j++)
		{
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
			glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
			glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glNormal3d(norm_cont[0][0], norm_cont[0][1], norm_cont[0][2]);
			glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glNormal3d(norm_cont[0][0], norm_cont[0][1], norm_cont[0][2]);
			glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
		}
		glEnd();
	}
	
	private final void draw_segment_facet_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] norm_cont, int inext, double len)
	{
		
		// System.err.println( "draw_segment_facet_n()" );
		
		/* draw the tube segment */
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp - 1; j++)
		{
			glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
			glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
			/**
			 * , j, FRONT);
			 */
			glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
			/**
			 * , j, BACK);
			 */
			glVertex3d(front_contour[j + 1][0], front_contour[j + 1][1], front_contour[j + 1][2]);
			/**
			 * , j+1, FRONT);
			 */
			glVertex3d(back_contour[j + 1][0], back_contour[j + 1][1], back_contour[j + 1][2]);
			/**
			 * , j+1, BACK);
			 */
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			/* connect back up to first point of contour */
			glNormal3d(norm_cont[ncp - 1][0], norm_cont[ncp - 1][1], norm_cont[ncp - 1][2]);
			glVertex3d(front_contour[ncp - 1][0], front_contour[ncp - 1][1], front_contour[ncp - 1][2]);
			/**
			 * , ncp-1, FRONT);
			 */
			glVertex3d(back_contour[ncp - 1][0], back_contour[ncp - 1][1], back_contour[ncp - 1][2]);
			/**
			 * , ncp-1, BACK);
			 */
			glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
			/**
			 * , 0, FRONT);
			 */
			glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]); /* ,
			                                                                         * 0,
			                                                                         * BACK
			                                                                         * )
			                                                                         * ; */
		}
		glEnd();
	}
	
	private final void draw_segment_c_and_facet_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] norm_cont, float[] color_last, float[] color_next, int inext, double len)
	{
		/**
		 * Note about this code: At first, when looking at this code, it appears
		 * to be really dumb: the N3F() call appears to be repeated multiple
		 * times, for no apparent purpose. It would seem that a performance
		 * improvement would be gained by stripping it out. !DONT DO IT! When
		 * there are no local lights or viewers, the V3F() subroutine does not
		 * trigger a recalculation of the lighting equations. However, we MUST
		 * trigger lighting, since otherwise colors come out wrong. Trigger
		 * lighting by doing an N3F call.
		 */
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp - 1; j++)
		{
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
			glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
			glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
			
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
			glVertex3d(front_contour[j + 1][0], front_contour[j + 1][1], front_contour[j + 1][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glNormal3d(norm_cont[j][0], norm_cont[j][1], norm_cont[j][2]);
			glVertex3d(back_contour[j + 1][0], back_contour[j + 1][1], back_contour[j + 1][2]);
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glNormal3d(norm_cont[ncp - 1][0], norm_cont[ncp - 1][1], norm_cont[ncp - 1][2]);
			glVertex3d(front_contour[ncp - 1][0], front_contour[ncp - 1][1], front_contour[ncp - 1][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glNormal3d(norm_cont[ncp - 1][0], norm_cont[ncp - 1][1], norm_cont[ncp - 1][2]);
			glVertex3d(back_contour[ncp - 1][0], back_contour[ncp - 1][1], back_contour[ncp - 1][2]);
			
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glNormal3d(norm_cont[ncp - 1][0], norm_cont[ncp - 1][1], norm_cont[ncp - 1][2]);
			glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glNormal3d(norm_cont[ncp - 1][0], norm_cont[ncp - 1][1], norm_cont[ncp - 1][2]);
			glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
		}
		glEnd();
	}
	
	/**
	 * This routine draws a segment with normals specified at each end.
	 */
	private final void draw_binorm_segment_edge_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] front_norm, double[][] back_norm, int inext, double len)
	{
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp; j++)
		{
			glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
			glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
			glNormal3d(back_norm[j][0], back_norm[j][1], back_norm[j][2]);
			glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			glNormal3d(front_norm[0][0], front_norm[0][1], front_norm[0][2]);
			glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
			glNormal3d(back_norm[0][0], back_norm[0][1], back_norm[0][2]);
			glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
		}
		glEnd();
	}
	
	private final void draw_binorm_segment_c_and_edge_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] front_norm, double[][] back_norm, float[] color_last, float[] color_next, int inext, double len)
	{
		
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp; j++)
		{
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
			glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glNormal3d(back_norm[j][0], back_norm[j][1], back_norm[j][2]);
			glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glNormal3d(front_norm[0][0], front_norm[0][1], front_norm[0][2]);
			glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glNormal3d(back_norm[0][0], back_norm[0][1], back_norm[0][2]);
			glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
		}
		glEnd();
	}
	
	/**
	 * This routine draws a piece of the round segment with psuedo-facet
	 * normals. I say "psuedo-facet" because the resulting object looks much,
	 * much better than real facet normals, and is what the round join style was
	 * meant to accomplish for face normals.
	 */
	private final void draw_binorm_segment_facet_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] front_norm, double[][] back_norm, int inext, double len)
	{
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp - 1; j++)
		{
			glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
			glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
			
			glNormal3d(back_norm[j][0], back_norm[j][1], back_norm[j][2]);
			glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
			
			glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
			glVertex3d(front_contour[j + 1][0], front_contour[j + 1][1], front_contour[j + 1][2]);
			
			glNormal3d(back_norm[j][0], back_norm[j][1], back_norm[j][2]);
			glVertex3d(back_contour[j + 1][0], back_contour[j + 1][1], back_contour[j + 1][2]);
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			glNormal3d(front_norm[ncp - 1][0], front_norm[ncp - 1][1], front_norm[ncp - 1][2]);
			glVertex3d(front_contour[ncp - 1][0], front_contour[ncp - 1][1], front_contour[ncp - 1][2]);
			
			glNormal3d(back_norm[ncp - 1][0], back_norm[ncp - 1][1], back_norm[ncp - 1][2]);
			glVertex3d(back_contour[ncp - 1][0], back_contour[ncp - 1][1], back_contour[ncp - 1][2]);
			
			glNormal3d(front_norm[ncp - 1][0], front_norm[ncp - 1][1], front_norm[ncp - 1][2]);
			glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
			
			glNormal3d(back_norm[ncp - 1][0], back_norm[ncp - 1][1], back_norm[ncp - 1][2]);
			glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
		}
		glEnd();
	}
	
	private final void draw_binorm_segment_c_and_facet_n(int ncp, double[][] front_contour, double[][] back_contour, double[][] front_norm, double[][] back_norm, float[] color_last, float[] color_next, int inext, double len)
	{
		
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j < ncp - 1; j++)
		{
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
			glVertex3d(front_contour[j][0], front_contour[j][1], front_contour[j][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glNormal3d(back_norm[j][0], back_norm[j][1], back_norm[j][2]);
			glVertex3d(back_contour[j][0], back_contour[j][1], back_contour[j][2]);
			
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glNormal3d(front_norm[j][0], front_norm[j][1], front_norm[j][2]);
			glVertex3d(front_contour[j + 1][0], front_contour[j + 1][1], front_contour[j + 1][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glNormal3d(back_norm[j][0], back_norm[j][1], back_norm[j][2]);
			glVertex3d(back_contour[j + 1][0], back_contour[j + 1][1], back_contour[j + 1][2]);
		}
		
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glNormal3d(front_norm[ncp - 1][0], front_norm[ncp - 1][1], front_norm[ncp - 1][2]);
			glVertex3d(front_contour[ncp - 1][0], front_contour[ncp - 1][1], front_contour[ncp - 1][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glNormal3d(back_norm[ncp - 1][0], back_norm[ncp - 1][1], back_norm[ncp - 1][2]);
			glVertex3d(back_contour[ncp - 1][0], back_contour[ncp - 1][1], back_contour[ncp - 1][2]);
			
			glColor3f(color_last[0], color_last[1], color_last[2]);
			glNormal3d(front_norm[ncp - 1][0], front_norm[ncp - 1][1], front_norm[ncp - 1][2]);
			glVertex3d(front_contour[0][0], front_contour[0][1], front_contour[0][2]);
			
			glColor3f(color_next[0], color_next[1], color_next[2]);
			glNormal3d(back_norm[ncp - 1][0], back_norm[ncp - 1][1], back_norm[ncp - 1][2]);
			glVertex3d(back_contour[0][0], back_contour[0][1], back_contour[0][2]);
		}
		glEnd();
	}
	
	/**
	 * -- End segment.c --
	 */
	/**
	 * -- Begin ex_cut_round.c --
	 */
	private final void extrusion_round_or_cut_join(int ncp, double[][] contour, double[][] cont_normal, double[] up, int npoints, double[][] point_array, float[][] color_array, double[][][] xform_array)
	{
		int i = 0, j = 0;
		int inext = 0, inextnext = 0;
		double[][] m = new double[4][4];
		double tube_len = 0, seg_len = 0;
		double[] diff = new double[3];
		double[] bi_0 = new double[3], bi_1 = new double[3];
		double[] bisector_0 = new double[3], bisector_1 = new double[3];
		double[] cut_0 = new double[3], cut_1 = new double[3];
		double[] lcut_0 = new double[3], lcut_1 = new double[3];
		boolean valid_cut_0 = false, valid_cut_1 = false;
		double[] end_point_0 = new double[3], end_point_1 = new double[3];
		double[] torsion_point_0 = new double[3], torsion_point_1 = new double[3];
		double[] isect_point = new double[3];
		double[] origin = new double[3];
		double[] neg_z = new double[3];
		double[] yup = new double[3];
		double[][] front_cap = null;
		double[][] back_cap = null;
		double[][] front_loop = null;
		double[][] back_loop = null;
		double[][] front_norm = null;
		double[][] back_norm = null;
		double[][] norm_loop = null;
		double[][] tmp = null;
		boolean[] front_is_trimmed = null;
		boolean[] back_is_trimmed = null;
		float[] front_color = null;
		float[] back_color = null;
		boolean join_style_is_cut = false;
		double dot = 0;
		boolean first_time = true;
		double[] cut_vec = null;
		String cap_callback = null;
		String tmp_cap_callback = null;
		
		if((gleGetJoinStyle() & TUBE_JN_CUT) == TUBE_JN_CUT)
		{
			join_style_is_cut = true;
			cap_callback = new String("cut");
		} else
		{
			join_style_is_cut = false;
			cap_callback = new String("round");
		}
		
		/**
		 * By definition, the contour passed in has its up vector pointing in
		 * the y direction
		 */
		if(up == null)
		{
			yup[0] = 0.0;
			yup[1] = 1.0;
			yup[2] = 0.0;
		} else
		{
			yup = matrix.VEC_COPY(up);
		}
		
		/* ========== "up" vector sanity check ========== */
		yup = up_sanity_check(yup, npoints, point_array);
		
		/* the origin is at the origin */
		origin[0] = 0.0;
		origin[1] = 0.0;
		origin[2] = 0.0;
		
		/* and neg_z is at neg z */
		neg_z[0] = 0.0;
		neg_z[1] = 0.0;
		neg_z[2] = 1.0;
		
		/**
		 * Allocate the data areas to store the end-caps and loops
		 */
		front_norm = new double[ncp][3];
		back_norm = new double[ncp][3];
		front_loop = new double[ncp][3];
		back_loop = new double[ncp][3];
		front_cap = new double[ncp][3];
		back_cap = new double[ncp][3];
		front_is_trimmed = new boolean[ncp];
		back_is_trimmed = new boolean[ncp];
		
		/* |-|-|-|-|-|-|-|-| SET UP FOR FIRST SEGMENT |-|-|-|-|-|-|-| */
		
		/* ignore all segments of zero length */
		i = 1;
		inext = i;
		inext = intersect.FIND_NON_DEGENERATE_POINT(inext, npoints, seg_len, diff, point_array);
		seg_len = matrix.VEC_LENGTH(diff);
		tube_len = seg_len; /* store for later use */
		
		/* may as well get the normals set up now */
		if(cont_normal != null)
		{
			if(xform_array == null)
			{
				norm_loop = front_norm;
				back_norm = norm_loop;
				for(j = 0; j < ncp; j++)
				{
					norm_loop[j][0] = cont_normal[j][0];
					norm_loop[j][1] = cont_normal[j][1];
					norm_loop[j][2] = 0.0;
				}
			} else
			{
				for(j = 0; j < ncp; j++)
				{
					front_norm[j] = matrix.NORM_XFORM_2X2(xform_array[inext - 1], cont_normal[j]);
					front_norm[j][2] = 0.0;
					back_norm[j][2] = 0.0;
				}
			}
		} else
		{
			front_norm = back_norm = norm_loop = null;
		}
		
		/* get the bisecting plane */
		bi_0 = intersect.bisecting_plane(point_array[i - 1], point_array[i], point_array[inext]);
		
		/* compute cutting plane */
		valid_cut_0 = intersect.CUTTING_PLANE(cut_0, point_array[i - 1], point_array[i], point_array[inext]);
		
		/* reflect the up vector in the bisecting plane */
		yup = matrix.VEC_REFLECT(yup, bi_0);
		
		/* |-|-|-|-|-|-|-|-| START LOOP OVER SEGMENTS |-|-|-|-|-|-|-| */
		
		/* draw tubing, not doing the first segment */
		while(inext < npoints - 1)
		{
			
			inextnext = inext;
			/* ignore all segments of zero length */
			inextnext = intersect.FIND_NON_DEGENERATE_POINT(inextnext, npoints, seg_len, diff, point_array);
			seg_len = matrix.VEC_LENGTH(diff);
			
			/* get the far bisecting plane */
			bi_1 = intersect.bisecting_plane(point_array[i], point_array[inext], point_array[inextnext]);
			
			/* compute cutting plane */
			valid_cut_1 = intersect.CUTTING_PLANE(cut_1, point_array[i], point_array[inext], point_array[inextnext]);
			
			/* rotate so that z-axis points down v2-v1 axis, and so that origen
			 * is at v1 */
			m = matrix.uviewpoint_d(point_array[i], point_array[inext], yup);
			DoubleBuffer mbuffer = BufferUtils.createDoubleBuffer(16);
			mbuffer.put(new double[] { m[0][0], m[0][1], m[0][2], m[0][3], m[1][0], m[1][1], m[1][2], m[1][3], m[2][0], m[2][1], m[2][2], m[2][3], m[3][0], m[3][1], m[3][2], m[3][3] });
			
			mbuffer.flip();
			glPushMatrix();
			glMultMatrix(mbuffer);
			
			/* rotate the cutting planes into the local coordinate system */
			lcut_0 = matrix.MAT_DOT_VEC_3X3(m, cut_0);
			lcut_1 = matrix.MAT_DOT_VEC_3X3(m, cut_1);
			
			/* rotate the bisecting planes into the local coordinate system */
			bisector_0 = matrix.MAT_DOT_VEC_3X3(m, bi_0);
			bisector_1 = matrix.MAT_DOT_VEC_3X3(m, bi_1);
			
			neg_z[2] = -tube_len;
			
			/* draw the tube */
			/* --------- START OF TMESH GENERATION -------------- */
			for(j = 0; j < ncp; j++)
			{
				
				/* set up the endpoints for segment clipping */
				if(xform_array == null)
				{
					end_point_0 = matrix.VEC_COPY_2(contour[j]);
					end_point_1 = matrix.VEC_COPY_2(contour[j]);
					torsion_point_0 = matrix.VEC_COPY_2(contour[j]);
					torsion_point_1 = matrix.VEC_COPY_2(contour[j]);
				} else
				{
					/* transform the contour points with the local xform */
					end_point_0 = matrix.MAT_DOT_VEC_2X3(xform_array[inext - 1], contour[j]);
					torsion_point_0 = matrix.MAT_DOT_VEC_2X3(xform_array[inext], contour[j]);
					end_point_1 = matrix.MAT_DOT_VEC_2X3(xform_array[inext], contour[j]);
					torsion_point_1 = matrix.MAT_DOT_VEC_2X3(xform_array[inext - 1], contour[j]);
					
					/* if there are normals and there are affine xforms, then
					 * compute local coordinate system normals. Set up the back
					 * normals. (The front normals we inherit from previous pass
					 * through the loop). */
					if(cont_normal != null)
					{
						/* do up the normal vectors with the inverse
						 * transpose */
						back_norm[j] = matrix.NORM_XFORM_2X2(xform_array[inext], cont_normal[j]);
					}
				}
				end_point_0[2] = 0.0;
				torsion_point_0[2] = 0.0;
				end_point_1[2] = -tube_len;
				torsion_point_1[2] = -tube_len;
				
				/* The two end-points define a line. Intersect this line against
				 * the clipping plane defined by the PREVIOUS tube segment. */
				
				/* if this and the last tube are co-linear, don't cut the angle
				 * if you do, a divide by zero will result. This and last tube
				 * are co-linear when the cut vector is of zero length */
				if(valid_cut_0 && join_style_is_cut)
				{
					isect_point = intersect.INNERSECT(origin, /* point on
					                                           * intersecting
					                                           * plane */
					        lcut_0, /* normal vector to plane */
					        end_point_0, /* point on line */
					        end_point_1); /* another point on the line */
					
					/* determine whether the raw end of the extrusion would have
					 * been cut, by checking to see if the raw and is on the far
					 * end of the half-plane defined by the cut vector. If the
					 * raw end is not "cut", then it is "trimmed". */
					if(lcut_0[2] < 0.0)
					{
						lcut_0 = matrix.VEC_SCALE(-1.0, lcut_0);
					}
					dot = lcut_0[0] * end_point_0[0];
					dot += lcut_0[1] * end_point_0[1];
					
					front_loop[j] = matrix.VEC_COPY(isect_point);
				} else
				{
					/* actual value of dot not interseting; need only be
					 * positive so that if test below failes */
					dot = 1.0;
					front_loop[j] = matrix.VEC_COPY(end_point_0);
				}
				
				isect_point = intersect.INNERSECT(origin, /* point on
				                                           * intersecting
				                                           * plane */
				        bisector_0, /* normal vector to plane */
				        end_point_0, /* point on line */
				        torsion_point_1); /* another point on the line */
				
				/* trim out interior of intersecting tube */
				/* ... but save the untrimmed version for drawing the endcaps */
				/* ... note that cap contains valid data ONLY when is_trimmed is
				 * TRUE. */
				if((dot <= 0.0) || (isect_point[2] < front_loop[j][2]))
				{
					/* if ((dot <= 0.0) || (front_loop[3*j+2] > 0.0)) { */
					front_cap[j] = matrix.VEC_COPY(front_loop[j]);
					front_loop[j] = matrix.VEC_COPY(isect_point);
					front_is_trimmed[j] = true;
				} else
				{
					front_is_trimmed[j] = false;
				}
				
				/* if intersection is behind the end of the segment, truncate to
				 * the end of the segment Note that coding front_loop [3*j+2] =
				 * -tube_len; doesn't work when twists are involved, */
				if(front_loop[j][2] < -tube_len)
				{
					front_loop[j] = matrix.VEC_COPY(end_point_1);
				}
				
				/* --------------------------------------------------- */
				/* The two end-points define a line. We did one endpoint above.
				 * Now do the other.Intersect this line against the clipping
				 * plane defined by the NEXT tube segment. */
				
				/* if this and the last tube are co-linear, don't cut the angle
				 * if you do, a divide by zero will result. This and last tube
				 * are co-linear when the cut vector is of zero length */
				if(valid_cut_1 && join_style_is_cut)
				{
					isect_point = intersect.INNERSECT(neg_z, /* point on
					                                          * intersecting
					                                          * plane */
					        lcut_1, /* normal vector to plane */
					        end_point_1, /* point on line */
					        end_point_0); /* another point on the line */
					
					if(lcut_1[2] > 0.0)
					{
						lcut_1 = matrix.VEC_SCALE(-1.0, lcut_1);
					}
					dot = lcut_1[0] * end_point_1[0];
					dot += lcut_1[1] * end_point_1[1];
					
					back_loop[j] = matrix.VEC_COPY(isect_point);
				} else
				{
					/* actual value of dot not interseting; need only be
					 * positive so that if test below failes */
					dot = 1.0;
					back_loop[j] = matrix.VEC_COPY(end_point_1);
				}
				
				isect_point = intersect.INNERSECT(neg_z, /* point on
				                                          * intersecting
				                                          * plane */
				        bisector_1, /* normal vector to plane */
				        torsion_point_0, /* point on line */
				        end_point_1); /* another point on the line */
				
				/* cut out interior of intersecting tube */
				/* ... but save the uncut version for drawing the endcaps */
				/* ... note that cap contains valid data ONLY when is _trimmed
				 * is TRUE. */
				/* if ((dot <= 0.0) || (back_loop[3*j+2] < -tube_len)) { */
				if((dot <= 0.0) || (isect_point[2] > back_loop[j][2]))
				{
					back_cap[j] = matrix.VEC_COPY(back_loop[j]);
					back_loop[j] = matrix.VEC_COPY(isect_point);
					back_is_trimmed[j] = true;
				} else
				{
					back_is_trimmed[j] = false;
				}
				
				/* if intersection is behind the end of the segment, truncate to
				 * the end of the segment Note that coding back_loop [3*j+2] =
				 * 0.0; doesn't work when twists are involved, */
				if(back_loop[j][2] > 0.0)
				{
					back_loop[j] = matrix.VEC_COPY(end_point_0);
				}
			}
			
			/* --------- END OF TMESH GENERATION -------------- */
			
			/* |||||||||||||||||| START SEGMENT DRAW |||||||||||||||||||| */
			/* There are six different cases we can have for presence and/or
			 * absecnce of colors and normals, and for interpretation of
			 * normals. The blechy set of nested if statements below /*
			 * |||||||||||||||||| START SEGMENT DRAW |||||||||||||||||||| */
			/* There are six different cases we can have for presence and/or
			 * absecnce of colors and normals, and for interpretation of
			 * normals. The blechy set of nested if statements below branch to
			 * each of the six cases */
			if(xform_array == null)
			{
				if(color_array == null)
				{
					if(cont_normal == null)
					{
						draw_segment_plain(ncp, front_loop, back_loop, inext, seg_len);
					} else
					{
						if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
						{
							draw_segment_facet_n(ncp, front_loop, back_loop, norm_loop, inext, seg_len);
						} else
						{
							draw_segment_edge_n(ncp, front_loop, back_loop, norm_loop, inext, seg_len);
						}
					}
				} else
				{
					if(cont_normal == null)
					{
						draw_segment_color(ncp, front_loop, back_loop, color_array[inext - 1], color_array[inext], inext, seg_len);
					} else
					{
						if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
						{
							draw_segment_c_and_facet_n(ncp, front_loop, back_loop, norm_loop, color_array[inext - 1], color_array[inext], inext, seg_len);
						} else
						{
							draw_segment_c_and_edge_n(ncp, front_loop, back_loop, norm_loop, color_array[inext - 1], color_array[inext], inext, seg_len);
						}
					}
				}
			} else
			{
				if(color_array == null)
				{
					if(cont_normal == null)
					{
						draw_segment_plain(ncp, front_loop, back_loop, inext, seg_len);
					} else
					{
						if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
						{
							draw_binorm_segment_facet_n(ncp, front_loop, back_loop, front_norm, back_norm, inext, seg_len);
						} else
						{
							draw_binorm_segment_edge_n(ncp, front_loop, back_loop, front_norm, back_norm, inext, seg_len);
						}
					}
				} else
				{
					if(cont_normal == null)
					{
						draw_segment_color(ncp, front_loop, back_loop, color_array[inext - 1], color_array[inext], inext, seg_len);
					} else
					{
						if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
						{
							draw_binorm_segment_c_and_facet_n(ncp, front_loop, back_loop, front_norm, back_norm, color_array[inext - 1], color_array[inext], inext, seg_len);
						} else
						{
							draw_binorm_segment_c_and_edge_n(ncp, front_loop, back_loop, front_norm, back_norm, color_array[inext - 1], color_array[inext], inext, seg_len);
						}
					}
				}
			}
			
			/* |||||||||||||||||| END SEGMENT DRAW |||||||||||||||||||| */
			
			/* v^v^v^v^v^v^v^v^v BEGIN END CAPS v^v^v^v^v^v^v^v^v^v^v^v */
			
			/* if end caps are required, draw them. But don't draw any but the
			 * very first and last caps */
			if(first_time)
			{
				first_time = false;
				tmp_cap_callback = cap_callback;
				cap_callback = new String("null");
				if((gleGetJoinStyle() & TUBE_JN_CAP) == 1)
				{
					if(color_array != null)
					{
						glColor3f(color_array[inext - 1][0], color_array[inext - 1][1], color_array[inext - 1][2]);
					}
					draw_angle_style_front_cap(ncp, bisector_0, front_loop);
				}
			}
			/* v^v^v^v^v^v^v^v^v END END CAPS v^v^v^v^v^v^v^v^v^v^v^v */
			
			/* $$$$$$$$$$$$$$$$ BEGIN -1, FILLET & JOIN DRAW
			 * $$$$$$$$$$$$$$$$$ */
			/* Now, draw the fillet triangles, and the join-caps. */
			if(color_array != null)
			{
				front_color = color_array[inext - 1];
				back_color = color_array[inext];
			} else
			{
				front_color = null;
				back_color = null;
			}
			
			if(cont_normal == null)
			{
				/* the flag valid-cut is true if the cut vector has a valid
				 * value (i.e. if a degenerate case has not occured). */
				if(valid_cut_0)
				{
					cut_vec = lcut_0;
				} else
				{
					cut_vec = null;
				}
				draw_fillets_and_join_plain(ncp, front_loop, front_cap, front_is_trimmed, origin, bisector_0, front_color, back_color, cut_vec, true, cap_callback);
				
				/* v^v^v^v^v^v^v^v^v BEGIN END CAPS v^v^v^v^v^v^v^v^v^v^v^v */
				if(inext == npoints - 2)
				{
					if((gleGetJoinStyle() & TUBE_JN_CAP) == 1)
					{
						if(color_array != null)
						{
							glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
						}
						draw_angle_style_back_cap(ncp, bisector_1, back_loop);
						cap_callback = new String("null");
					}
				} else
				{
					/* restore ability to draw cap */
					cap_callback = tmp_cap_callback;
				}
				/* v^v^v^v^v^v^v^v^v END END CAPS v^v^v^v^v^v^v^v^v^v^v^v */
				
				/* the flag valid-cut is true if the cut vector has a valid
				 * value (i.e. if a degenerate case has not occured). */
				if(valid_cut_1)
				{
					cut_vec = lcut_1;
				} else
				{
					cut_vec = null;
				}
				draw_fillets_and_join_plain(ncp, back_loop, back_cap, back_is_trimmed, neg_z, bisector_1, back_color, front_color, cut_vec, false, cap_callback);
			} else
			{
				
				/* the flag valid-cut is true if the cut vector has a valid
				 * value (i.e. if a degenerate case has not occured). */
				if(valid_cut_0)
				{
					cut_vec = lcut_0;
				} else
				{
					cut_vec = null;
				}
				draw_fillets_and_join_n_norms(ncp, front_loop, front_cap, front_is_trimmed, origin, bisector_0, front_norm, front_color, back_color, cut_vec, true, cap_callback);
				
				/* v^v^v^v^v^v^v^v^v BEGIN END CAPS v^v^v^v^v^v^v^v^v^v^v^v */
				if(inext == npoints - 2)
				{
					if((gleGetJoinStyle() & TUBE_JN_CAP) == 1)
					{
						if(color_array != null)
						{
							glColor3f(color_array[inext][0], color_array[inext][1], color_array[inext][2]);
						}
						draw_angle_style_back_cap(ncp, bisector_1, back_loop);
						cap_callback = new String("null");
					}
				} else
				{
					/* restore ability to draw cap */
					cap_callback = tmp_cap_callback;
				}
				/* v^v^v^v^v^v^v^v^v END END CAPS v^v^v^v^v^v^v^v^v^v^v^v */
				
				/* the flag valid-cut is true if the cut vector has a valid
				 * value (i.e. if a degenerate case has not occured). */
				if(valid_cut_1)
				{
					cut_vec = lcut_1;
				} else
				{
					cut_vec = null;
				}
				draw_fillets_and_join_n_norms(ncp, back_loop, back_cap, back_is_trimmed, neg_z, bisector_1, back_norm, back_color, front_color, cut_vec, false, cap_callback);
			}
			
			/* $$$$$$$$$$$$$$$$ END FILLET & JOIN DRAW $$$$$$$$$$$$$$$$$ */
			
			/* pop this matrix, do the next set */
			glPopMatrix();
			
			/* slosh stuff over to next vertex */
			tmp = front_norm;
			front_norm = back_norm;
			back_norm = tmp;
			
			tube_len = seg_len;
			i = inext;
			inext = inextnext;
			bi_0 = matrix.VEC_COPY(bi_1);
			cut_0 = matrix.VEC_COPY(cut_1);
			valid_cut_0 = valid_cut_1;
			
			/* reflect the up vector in the bisecting plane */
			yup = matrix.VEC_REFLECT(yup, bi_0);
		}
		/* |-|-|-|-|-|-|-|-| END LOOP OVER SEGMENTS |-|-|-|-|-|-|-| */
	}
	
	private final void draw_fillets_and_join_plain(int ncp, double[][] trimmed_loop, double[][] untrimmed_loop, boolean[] is_trimmed, double[] bis_origin, double[] bis_vector, float[] front_color, float[] back_color, double[] cut_vector, boolean face, String cap_callback)
	{
		int istop = 0;
		int icnt = 0, icnt_prev = 0, iloop = 0;
		double[][] cap_loop = null;
		double[] sect = new double[3];
		double[] tmp_vec = new double[3];
		int save_style = 0;
		boolean was_trimmed = false;
		
		cap_loop = new double[ncp + 3][3];
		
		// System.err.println( "draw_fillets_and_join_plain()" );
		
		/* If the first point is trimmed, keep going until one is found that is
		 * not trimmed, and start join there. */
		icnt = 0;
		iloop = 0;
		if(!is_trimmed[0])
		{
			/* if the first point on the contour isn't trimmed, go ahead and
			 * drop an edge down to the bisecting plane, (thus starting the
			 * join). (Only need to do this for cut join, its bad if done for
			 * round join). (Also, do this only for open contours; leads to bugs
			 * when done for closed contours). */
			if(((gleGetJoinStyle() & TUBE_JN_CUT) == TUBE_JN_CUT) && (!((save_style & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)))
			{
				tmp_vec = matrix.VEC_SUM(trimmed_loop[0], bis_vector);
				sect = intersect.INNERSECT(bis_origin, bis_vector, trimmed_loop[0], tmp_vec);
				cap_loop[iloop] = matrix.VEC_COPY(sect);
				iloop++;
			}
			cap_loop[iloop] = matrix.VEC_COPY(trimmed_loop[0]);
			iloop++;
			icnt_prev = icnt;
			icnt++;
		} else
		{
			/* else, loop until an untrimmed point is found */
			was_trimmed = true;
			while(is_trimmed[icnt])
			{
				icnt_prev = icnt;
				icnt++;
				if(icnt >= ncp)
				{
					return; /* oops - everything was trimmed */
				}
			}
		}
		
		/* Start walking around the end cap. Every time the end loop is trimmed,
		 * we know we'll need to draw a fillet triangle. In addition, after
		 * every pair of visibility changes, we draw a cap. */
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			istop = ncp;
		} else
		{
			istop = ncp - 1;
		}
		
		/* save the join style, and disable a closed contour. Need to do this so
		 * partial contours don't close up. */
		save_style = gleGetJoinStyle();
		gleSetJoinStyle(save_style & ~TUBE_CONTOUR_CLOSED);
		
		for(; icnt_prev < istop; icnt_prev++, icnt++, icnt %= ncp)
		{
			/* There are four interesting cases for drawing caps and fillets: 1)
			 * this & previous point were trimmed. Don't do anything, advance
			 * counter. 2) this point trimmed, previous not -- draw fillet, and
			 * draw cap. 3) this point not trimmed, previous one was -- compute
			 * intersection point, draw fillet with it, and save point for cap
			 * contour. 4) this & previous point not trimmed -- save for
			 * endcap. */
			
			/* Case 1 -- noop, just advance pointers */
			if(is_trimmed[icnt_prev] && is_trimmed[icnt])
			{
			}
			
			/* Case 2 -- Hah! first point! compute intersect & draw fillet! */
			if(is_trimmed[icnt_prev] && !is_trimmed[icnt])
			{
				
				/* important note: the array "untrimmed" contains valid
				 * untrimmed data ONLY when is_trim is TRUE. Otherwise, only
				 * "trim" containes valid data */
				
				/* compute intersection */
				sect = intersect.INNERSECT(bis_origin, bis_vector, untrimmed_loop[icnt_prev], trimmed_loop[icnt]);
				
				/* Draw Fillet */
				draw_fillet_triangle_plain(trimmed_loop[icnt_prev], trimmed_loop[icnt], sect, face, front_color, back_color);
				
				cap_loop[iloop] = matrix.VEC_COPY(sect);
				iloop++;
				cap_loop[iloop] = matrix.VEC_COPY(trimmed_loop[icnt]);
				iloop++;
			}
			/* Case 3 -- add to collection of points */
			if(!is_trimmed[icnt_prev] && !is_trimmed[icnt])
			{
				cap_loop[iloop] = matrix.VEC_COPY(trimmed_loop[icnt]);
				iloop++;
			}
			
			/* Case 4 -- Hah! last point! draw fillet & draw cap! */
			if(!is_trimmed[icnt_prev] && is_trimmed[icnt])
			{
				was_trimmed = true;
				
				/* important note: the array "untrimmed" contains valid
				 * untrimmed data ONLY when is_trim is TRUE. Otherwise, only
				 * "trim" containes valid data */
				
				/* compute intersection */
				sect = intersect.INNERSECT(bis_origin, bis_vector, trimmed_loop[icnt_prev], untrimmed_loop[icnt]);
				
				/* Draw Fillet */
				draw_fillet_triangle_plain(trimmed_loop[icnt_prev], trimmed_loop[icnt], sect, face, front_color, back_color);
				
				cap_loop[iloop] = matrix.VEC_COPY(sect);
				iloop++;
				
				/* draw cap */
				if(iloop >= 3)
				{
					if(cap_callback.equals("cut"))
					{
						draw_cut_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, null, face);
					} else
					{
						if(cap_callback.equals("round"))
						{
							draw_round_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, null, face);
						}
					}
				}
				/* reset cap counter */
				iloop = 0;
			}
		}
		
		/* now, finish up in the same way that we started. If the last point of
		 * the contour is visible, drop an edge to the bisecting plane, thus
		 * finishing the join, and then, draw the join! */
		
		icnt--; /* decrement to make up for loop exit condititons */
		icnt += ncp;
		icnt %= ncp;
		if((!is_trimmed[icnt]) && (iloop >= 2))
		{
			
			tmp_vec = matrix.VEC_SUM(trimmed_loop[icnt], bis_vector);
			sect = intersect.INNERSECT(bis_origin, bis_vector, trimmed_loop[icnt], tmp_vec);
			cap_loop[iloop] = matrix.VEC_COPY(sect);
			iloop++;
			
			/* if nothing was ever trimmed, then we want to draw the cap the way
			 * the user asked for it -- closed or not closed. Therefore, reset
			 * the closure flag to its original state. */
			if(!was_trimmed)
			{
				gleSetJoinStyle(save_style);
			}
			
			/* draw cap */
			if(cap_callback.equals("cut"))
			{
				draw_cut_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, null, face);
			} else
			{
				if(cap_callback.equals("round"))
				{
					draw_round_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, null, face);
				}
			}
		}
		
		/* rest to the saved style */
		gleSetJoinStyle(save_style);
	}
	
	private final void draw_fillets_and_join_n_norms(int ncp, double[][] trimmed_loop, double[][] untrimmed_loop, boolean[] is_trimmed, double[] bis_origin, double[] bis_vector, double[][] normals, float[] front_color, float[] back_color, double[] cut_vector, boolean face, String cap_callback)
	{
		
		int istop = 0;
		int icnt = 0, icnt_prev = 0, iloop = 0;
		double[][] cap_loop = null, norm_loop = null;
		double[] sect = new double[3];
		double[] tmp_vec = new double[3];
		int save_style = 0;
		boolean was_trimmed = false;
		
		cap_loop = new double[ncp + 3][3];
		norm_loop = new double[ncp + 3][3];
		
		/* If the first point is trimmed, keep going until one is found that is
		 * not trimmed, and start join there. */
		icnt = 0;
		iloop = 0;
		if(!is_trimmed[0])
		{
			/* if the first point on the contour isn't trimmed, go ahead and
			 * drop an edge down to the bisecting plane, (thus starting the
			 * join). (Only need to do this for cut join, its bad if done for
			 * round join). (Also, leads to bugs when done for closed contours
			 * ... do this only if contour is open). */
			if(((gleGetJoinStyle() & TUBE_JN_CUT) == TUBE_JN_CUT) && (!((save_style & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)))
			{
				tmp_vec = matrix.VEC_SUM(trimmed_loop[0], bis_vector);
				sect = intersect.INNERSECT(bis_origin, bis_vector, trimmed_loop[0], tmp_vec);
				cap_loop[iloop] = matrix.VEC_COPY(sect);
				norm_loop[iloop] = matrix.VEC_COPY(normals[0]);
				iloop++;
			}
			cap_loop[iloop] = matrix.VEC_COPY(trimmed_loop[0]);
			norm_loop[iloop] = matrix.VEC_COPY(normals[0]);
			iloop++;
			icnt_prev = icnt;
			icnt++;
		} else
		{
			/* else, loop until an untrimmed point is found */
			was_trimmed = true;
			while(is_trimmed[icnt])
			{
				icnt_prev = icnt;
				icnt++;
				if(icnt >= ncp)
				{
					return; /* oops - everything was trimmed */
				}
			}
		}
		
		/* Start walking around the end cap. Every time the end loop is trimmed,
		 * we know we'll need to draw a fillet triangle. In addition, after
		 * every pair of visibility changes, we draw a cap. */
		if((gleGetJoinStyle() & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)
		{
			istop = ncp;
		} else
		{
			istop = ncp - 1;
		}
		
		/* save the join style, and disable a closed contour. Need to do this so
		 * partial contours don't close up. */
		save_style = gleGetJoinStyle();
		gleSetJoinStyle(save_style & ~TUBE_CONTOUR_CLOSED);
		
		for(; icnt_prev < istop; icnt_prev++, icnt++, icnt %= ncp)
		{
			
			/* There are four interesting cases for drawing caps and fillets: 1)
			 * this & previous point were trimmed. Don't do anything, advance
			 * counter. 2) this point trimmed, previous not -- draw fillet, and
			 * draw cap. 3) this point not trimmed, previous one was -- compute
			 * intersection point, draw fillet with it, and save point for cap
			 * contour. 4) this & previous point not trimmed -- save for
			 * endcap. */
			
			/* Case 1 -- noop, just advance pointers */
			if(is_trimmed[icnt_prev] && is_trimmed[icnt])
			{
			}
			
			/* Case 2 -- Hah! first point! compute intersect & draw fillet! */
			if(is_trimmed[icnt_prev] && !is_trimmed[icnt])
			{
				
				/* important note: the array "untrimmed" contains valid
				 * untrimmed data ONLY when is_trim is TRUE. Otherwise, only
				 * "trim" containes valid data */
				
				/* compute intersection */
				sect = intersect.INNERSECT(bis_origin, bis_vector, untrimmed_loop[icnt_prev], trimmed_loop[icnt]);
				
				/* Draw Fillet */
				draw_fillet_triangle_n_norms(trimmed_loop[icnt_prev], trimmed_loop[icnt], sect, face, front_color, back_color, normals[icnt_prev], normals[icnt]);
				cap_loop[iloop] = matrix.VEC_COPY(sect);
				norm_loop[iloop] = matrix.VEC_COPY(normals[icnt_prev]);
				iloop++;
				cap_loop[iloop] = matrix.VEC_COPY(trimmed_loop[icnt]);
				norm_loop[iloop] = matrix.VEC_COPY(normals[icnt]);
				iloop++;
			}
			
			/* Case 3 -- add to collection of points */
			if(!is_trimmed[icnt_prev] && !is_trimmed[icnt])
			{
				cap_loop[iloop] = matrix.VEC_COPY(trimmed_loop[icnt]);
				norm_loop[iloop] = matrix.VEC_COPY(normals[icnt]);
				iloop++;
			}
			
			/* Case 4 -- Hah! last point! draw fillet & draw cap! */
			if(!is_trimmed[icnt_prev] && is_trimmed[icnt])
			{
				was_trimmed = true;
				
				/* important note: the array "untrimmed" contains valid
				 * untrimmed data ONLY when is_trim is TRUE. Otherwise, only
				 * "trim" containes valid data */
				
				/* compute intersection */
				sect = intersect.INNERSECT(bis_origin, bis_vector, trimmed_loop[icnt_prev], untrimmed_loop[icnt]);
				
				/* Draw Fillet */
				draw_fillet_triangle_n_norms(trimmed_loop[icnt_prev], trimmed_loop[icnt], sect, face, front_color, back_color, normals[icnt_prev], normals[icnt]);
				
				cap_loop[iloop] = matrix.VEC_COPY(sect);
				
				/* OK, maybe phong normals are wrong, but at least facet normals
				 * will come out OK. */
				if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
				{
					norm_loop[iloop] = matrix.VEC_COPY(normals[icnt_prev]);
				} else
				{
					norm_loop[iloop] = matrix.VEC_COPY(normals[icnt]);
				}
				iloop++;
				
				/* draw cap */
				if(iloop >= 3)
				{
					if(cap_callback.equals("cut"))
					{
						draw_cut_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, norm_loop, face);
					} else
					{
						if(cap_callback.equals("round"))
						{
							draw_round_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, norm_loop, face);
						}
					}
				}
				
				/* reset cap counter */
				iloop = 0;
			}
		}
		
		/* now, finish up in the same way that we started. */
		
		icnt--; /* decrement to make up for loop exit condititons */
		icnt += ncp;
		icnt %= ncp;
		if((!is_trimmed[icnt]) && (iloop >= 2))
		{
			
			/* If the last point of the contour is visible, drop an edge to the
			 * bisecting plane, thus finishing the join. Note that doing this
			 * leads to bugs if done for closed contours ... do this only if
			 * contour is open. */
			if(((gleGetJoinStyle() & TUBE_JN_CUT) == TUBE_JN_CUT) && (!((save_style & TUBE_CONTOUR_CLOSED) == TUBE_CONTOUR_CLOSED)))
			{
				tmp_vec = matrix.VEC_SUM(trimmed_loop[icnt], bis_vector);
				sect = intersect.INNERSECT(bis_origin, bis_vector, trimmed_loop[icnt], tmp_vec);
				cap_loop[iloop] = matrix.VEC_COPY(sect);
				norm_loop[iloop] = matrix.VEC_COPY(normals[icnt]);
				iloop++;
			}
			
			/* if nothing was ever trimmed, then we want to draw the cap the way
			 * the user asked for it -- closed or not closed. Therefore, reset
			 * the closure flag to its original state. */
			if(!was_trimmed)
			{
				gleSetJoinStyle(save_style);
				
			}
			/* draw cap */
			if(cap_callback.equals("cut"))
			{
				draw_cut_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, norm_loop, face);
			} else
			{
				if(cap_callback.equals("round"))
				{
					draw_round_style_cap_callback(iloop, cap_loop, front_color, cut_vector, bis_vector, norm_loop, face);
				}
			}
		}
		
		/* rest to the saved style */
		gleSetJoinStyle(save_style);
	}
	
	/* This little routine draws the little idd-biddy fillet triangle with the
	 * right color, normal, etc.
	 *
	 * HACK ALERT -- there are two aspects to this routine/interface that are
	 * "unfinished". 1) the third point of the triangle should get a color thats
	 * interpolated beween the front and back color. The interpolant is not
	 * currently being computed. The error introduced by not doing this should
	 * be tiny and/or non-exitant in almost all expected uses of this code.
	 *
	 * 2) additional normal vectors should be supplied, and these should be
	 * interpolated to fit. Currently, this is not being done. As above, the
	 * expected error of not doing this should be tiny and/or non-existant in
	 * almost all expected uses of this code. */
	private static void draw_fillet_triangle_plain(double[] va, double[] vb, double[] vc, boolean face, float[] front_color, float[] back_color)
	{
		
		if(front_color != null)
		{
			glColor3f(front_color[0], front_color[1], front_color[2]);
		}
		glBegin(GL_TRIANGLE_STRIP);
		if(face)
		{
			glVertex3d(va[0], va[1], va[2]);
			glVertex3d(vb[0], vb[1], vb[2]);
		} else
		{
			glVertex3d(vb[0], vb[1], vb[2]);
			glVertex3d(va[0], va[1], va[2]);
		}
		glVertex3d(vc[0], vc[1], vc[2]);
		glEnd();
		
	}
	
	/* This little routine draws the little idd-biddy fillet triangle with the
	 * right color, normal, etc.
	 *
	 * HACK ALERT -- there are two aspects to this routine/interface that are
	 * "unfinished". 1) the third point of the triangle should get a color thats
	 * interpolated beween the front and back color. The interpolant is not
	 * currently being computed. The error introduced by not doing this should
	 * be tiny and/or non-exitant in almost all expected uses of this code.
	 *
	 * 2) additional normal vectors should be supplied, and these should be
	 * interpolated to fit. Currently, this is not being done. As above, the
	 * expected error of not doing this should be tiny and/or non-existant in
	 * almost all expected uses of this code. */
	private final void draw_fillet_triangle_n_norms(double[] va, double[] vb, double[] vc, boolean face, float[] front_color, float[] back_color, double[] na, double[] nb)
	{
		
		if(front_color != null)
		{
			glColor3f(front_color[0], front_color[1], front_color[2]);
		}
		
		glBegin(GL_TRIANGLE_STRIP);
		if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
		{
			glNormal3d(na[0], na[1], na[2]);
			if(face)
			{
				glVertex3d(va[0], va[1], va[2]);
				glVertex3d(vb[0], vb[1], vb[2]);
			} else
			{
				glVertex3d(vb[0], vb[1], vb[2]);
				glVertex3d(va[0], va[1], va[2]);
			}
			glNormal3d(vc[0], vc[1], vc[2]);
		} else
		{
			if(face)
			{
				glNormal3d(na[0], na[1], na[2]);
				glVertex3d(va[0], va[1], va[2]);
				glNormal3d(nb[0], nb[1], nb[2]);
				glVertex3d(vb[0], vb[1], vb[2]);
			} else
			{
				glNormal3d(nb[0], nb[1], nb[2]);
				glVertex3d(vb[0], vb[1], vb[2]);
				glNormal3d(na[0], na[1], na[2]);
				glVertex3d(va[0], va[1], va[2]);
				glNormal3d(nb[0], nb[1], nb[2]);
			}
			glVertex3d(vc[0], vc[1], vc[2]);
		}
		glEnd();
	}
	
	/* This subroutine draws a flat cap, to close off the cut ends of the
	 * cut-style join. Properly handles concave endcaps. <P> This has been
	 * converted from the DELICATE_TESSELATOR section from the original source
	 * code since Mesa isn't quite as robust as the SGI tesselators. */
	private final void draw_cut_style_cap_callback(int iloop, double[][] cap, float[] face_color, double[] cut_vector, double[] bisect_vector, double[][] norms, boolean frontwards)
	{
		
		int i;
		double[] previous_vertex = null;
		double[] first_vertex = null;
		boolean is_colinear = false;
		
		GLUtessellator tobj = GLU.gluNewTess();
		tobj.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
		tobj.gluTessCallback(GLU.GLU_TESS_VERTEX, tessCallback);// glVertex3dv);
		tobj.gluTessCallback(GLU.GLU_TESS_BEGIN, tessCallback);// beginCallback);
		tobj.gluTessCallback(GLU.GLU_TESS_END, tessCallback);// endCallback);
		tobj.gluTessCallback(GLU.GLU_TESS_ERROR, tessCallback);// errorCallback);
		
		if(face_color != null)
		{
			glColor3f(face_color[0], face_color[1], face_color[2]);
		}
		
		if(frontwards)
		{
			
			/* if lighting is on, specify the endcap normal */
			if(cut_vector != null)
			{
				/* if normal pointing in wrong direction, flip it. */
				if(cut_vector[2] < 0.0)
				{
					cut_vector = matrix.VEC_SCALE(-1.0, cut_vector);
				}
				glNormal3d(cut_vector[0], cut_vector[1], cut_vector[2]);
			}
			// glu_.gluBeginPolygon( tobj );
			tobj.gluTessBeginPolygon(null);
			tobj.gluTessBeginContour();
			
			first_vertex = null;
			previous_vertex = cap[iloop - 1];
			for(i = 0; i < iloop - 1; i++)
			{
				is_colinear = intersect.COLINEAR(previous_vertex, cap[i], cap[i + 1]);
				if(!is_colinear)
				{
					// glu_.gluTessVertex( tobj, cap[i], cap[i] );
					tobj.gluTessVertex(cap[i], 0, cap[i]);
					previous_vertex = cap[i];
					if(first_vertex == null)
					{
						first_vertex = previous_vertex;
					}
				}
			}
			
			if(first_vertex == null)
			{
				first_vertex = cap[0];
			}
			is_colinear = intersect.COLINEAR(previous_vertex, cap[iloop - 1], first_vertex);
			if(!is_colinear)
			{
				// glu_.gluTessVertex( tobj, cap[iloop - 1], cap[iloop - 1] );
				tobj.gluTessVertex(cap[iloop - 1], 0, cap[iloop - 1]);
			}
			
			// glu_.gluEndPolygon( tobj );
			tobj.gluTessEndContour();
			tobj.gluTessEndPolygon();
		} else
		{
			/* if lighting is on, specify the endcap normal */
			if(cut_vector != null)
			{
				/* if normal pointing in wrong direction, flip it. */
				if(cut_vector[2] > 0.0)
				{
					cut_vector = matrix.VEC_SCALE(-1.0, cut_vector);
				}
				glNormal3d(cut_vector[0], cut_vector[1], cut_vector[2]);
			}
			/* the sense of the loop is reversed for backfacing culling */
			// glu_.gluBeginPolygon( tobj );
			tobj.gluTessBeginPolygon(null);
			tobj.gluTessBeginContour();
			
			first_vertex = null;
			previous_vertex = cap[0];
			for(i = iloop - 1; i > 0; i--)
			{
				is_colinear = intersect.COLINEAR(previous_vertex, cap[i], cap[i - 1]);
				if(!is_colinear)
				{
					// glu_.gluTessVertex( tobj, cap[i], cap[i] );
					tobj.gluTessVertex(cap[i], 0, cap[i]);
					previous_vertex = cap[i];
					if(first_vertex == null)
					{
						first_vertex = previous_vertex;
					}
				}
			}
			
			if(first_vertex == null)
			{
				first_vertex = cap[iloop - 1];
			}
			is_colinear = intersect.COLINEAR(previous_vertex, cap[0], first_vertex);
			if(!is_colinear)
			{
				// glu_.gluTessVertex( tobj, cap[0], cap[0] );
				tobj.gluTessVertex(cap[0], 0, cap[0]);
			}
			
			// glu_.gluEndPolygon( tobj );
			tobj.gluTessEndContour();
			tobj.gluTessEndPolygon();
		}
		// glu_.gluDeleteTess( tobj );
		tobj.gluDeleteTess();
	}
	
	/**
	 * -- End ex_cut_round.c --
	 */
	/**
	 * -- Begin round_cap.c --
	 */
	private final void draw_round_style_cap_callback(int ncp, double[][] cap, float[] face_color, double[] cut, double[] bi, double[][] norms, boolean frontwards)
	{
		
		double[] axis = new double[3];
		double[] xycut = new double[3];
		double theta = 0.0;
		double[][] last_contour = null, next_contour = null;
		double[][] last_norm = null, next_norm = null;
		double[] cap_z = null;
		double[][] tmp = null;
		int i = 0, j = 0, k = 0;
		double[][] m = new double[4][4];
		
		if(face_color != null)
		{
			glColor3f(face_color[0], face_color[1], face_color[2]);
		}
		
		/* ------------ start setting up rotation matrix ------------- */
		/* if the cut vector is NULL (this should only occur in a degenerate
		 * case), then we can't draw anything. return. */
		if(cut == null)
		{
			return;
		}
		
		/* make sure that the cut vector points inwards */
		if(cut[2] > 0.0)
		{
			cut = matrix.VEC_SCALE(-1.0, cut);
		}
		
		/* make sure that the bi vector points outwards */
		if(bi[2] < 0.0)
		{
			bi = matrix.VEC_SCALE(-1.0, bi);
		}
		
		/* determine the axis we are to rotate about to get bi-contour. Note
		 * that the axis will always lie in the x-y plane */
		axis = matrix.VEC_CROSS_PRODUCT(cut, bi);
		
		/* reverse the cut vector for the back cap -- need to do this to get
		 * angle right */
		if(!frontwards)
		{
			cut = matrix.VEC_SCALE(-1.0, cut);
		}
		
		/* get angle to rotate by -- arccos of dot product of cut with cut
		 * projected into the x-y plane */
		xycut[0] = 0.0;
		xycut[1] = 0.0;
		xycut[2] = 1.0;
		xycut = matrix.VEC_PERP(cut, xycut);
		xycut = matrix.VEC_NORMALIZE(xycut);
		theta = matrix.VEC_DOT_PRODUCT(xycut, cut);
		
		theta = Math.acos(theta);
		
		/* we'll tesselate round joins into a number of teeny pieces */
		theta /= __ROUND_TESS_PIECES;
		
		/* get the matrix */
		m = matrix.urot_axis_d(theta, axis);
		
		/* ------------ done setting up rotation matrix ------------- */
		
		/* This malloc is a fancy version of: last_contour = (double *) malloc
		 * (3*ncp*sizeof(double); next_contour = (double *) malloc
		 * (3*ncp*sizeof(double); */
		last_contour = new double[ncp][3];
		next_contour = new double[ncp][3];
		cap_z = new double[ncp];
		last_norm = new double[ncp][3];
		next_norm = new double[ncp][3];
		
		/* make first copy of contour */
		if(frontwards)
		{
			for(j = 0; j < ncp; j++)
			{
				last_contour[j][0] = cap[j][0];
				last_contour[j][1] = cap[j][1];
				last_contour[j][2] = cap_z[j] = cap[j][2];
			}
			
			if(norms != null)
			{
				for(j = 0; j < ncp; j++)
				{
					last_norm[j] = matrix.VEC_COPY(norms[j]);
				}
			}
		} else
		{
			/* in order for backfacing polygon removal to work correctly, have
			 * to have the sense in which the joins are drawn to be reversed for
			 * the back cap. This can be done by reversing the order of the
			 * contour points. Normals are a bit trickier, since the reversal is
			 * off-by-one for facet normals as compared to edge normals. */
			for(j = 0; j < ncp; j++)
			{
				k = ncp - j - 1;
				last_contour[k][0] = cap[j][0];
				last_contour[k][1] = cap[j][1];
				last_contour[k][2] = cap_z[k] = cap[j][2];
			}
			
			if(norms != null)
			{
				if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
				{
					for(j = 0; j < ncp - 1; j++)
					{
						k = ncp - j - 2;
						last_norm[k] = matrix.VEC_COPY(norms[j]);
					}
				} else
				{
					for(j = 0; j < ncp; j++)
					{
						k = ncp - j - 1;
						last_norm[k] = matrix.VEC_COPY(norms[j]);
					}
				}
			}
		}
		
		/* &&&&&&&&&&&&&& start drawing cap &&&&&&&&&&&&& */
		
		for(i = 0; i < __ROUND_TESS_PIECES; i++)
		{
			for(j = 0; j < ncp; j++)
			{
				next_contour[j][2] -= cap_z[j];
				last_contour[j][2] -= cap_z[j];
				next_contour[j] = matrix.MAT_DOT_VEC_3X3(m, last_contour[j]);
				next_contour[j][2] += cap_z[j];
				last_contour[j][2] += cap_z[j];
			}
			
			if(norms != null)
			{
				for(j = 0; j < ncp; j++)
				{
					next_norm[j] = matrix.MAT_DOT_VEC_3X3(m, last_norm[j]);
				}
			}
			
			/* OK, now render it all */
			if(norms == null)
			{
				draw_segment_plain(ncp, next_contour, last_contour, 0, 0.0);
			} else
			{
				if((gleGetJoinStyle() & TUBE_NORM_FACET) == TUBE_NORM_FACET)
				{
					draw_binorm_segment_facet_n(ncp, next_contour, last_contour, next_norm, last_norm, 0, 0.0);
				} else
				{
					draw_binorm_segment_edge_n(ncp, next_contour, last_contour, next_norm, last_norm, 0, 0.0);
				}
			}
			/* swap contours */
			tmp = next_contour;
			next_contour = last_contour;
			last_contour = tmp;
			
			tmp = next_norm;
			next_norm = last_norm;
			last_norm = tmp;
		}
		/* &&&&&&&&&&&&&& end drawing cap &&&&&&&&&&&&& */
	}
	
	/**
	 * -- End round_cap.c --
	 */
	//////////// tess callback class
	/* Tessellator callback implemenation with all the callback routines. YOu
	 * could use GLUtesselatorCallBackAdapter instead. But */
	class tessellCallBack implements GLUtessellatorCallback
	{
		
		// private GLU glu;
		public tessellCallBack(GLU glu)
		{
			// this.glu = glu;
		}
		
		@Override
		public void begin(int type)
		{
			glBegin(type);
		}
		
		@Override
		public void end()
		{
			glEnd();
		}
		
		@Override
		public void vertex(Object vertexData)
		{
			double[] pointer;
			if(vertexData instanceof double[])
			{
				pointer = (double[]) vertexData;
				if(pointer.length == 6)
				{
					glColor3d(pointer[3], pointer[4], pointer[5]);
				}
				glVertex3d(pointer[0], pointer[1], pointer[2]);
			}
			
		}
		
		@Override
		public void vertexData(Object vertexData, Object polygonData)
		{
		}
		
		/* combineCallback is used to create a new vertex when edges intersect.
		 * coordinate location is trivial to calculate, but weight[4] may be
		 * used to average color, normal, or texture coordinate data. In this
		 * program, color is weighted. */
		@Override
		public void combine(double[] coords, Object[] data, //
		        float[] weight, Object[] outData)
		{
			double[] vertex = new double[6];
			int i;
			
			vertex[0] = coords[0];
			vertex[1] = coords[1];
			vertex[2] = coords[2];
			for(i = 3; i < 6/* 7OutOfBounds from C! */; i++)
			{
				vertex[i] = weight[0] //
				        * ((double[]) data[0])[i] + weight[1] * ((double[]) data[1])[i] + weight[2] * ((double[]) data[2])[i] + weight[3] * ((double[]) data[3])[i];
			}
			outData[0] = vertex;
		}
		
		@Override
		public void combineData(double[] coords, Object[] data, //
		        float[] weight, Object[] outData, Object polygonData)
		{
		}
		
		@Override
		public void error(int errnum)
		{
			String estring;
			
			estring = GLU.gluErrorString(errnum);
			System.err.println("Tessellation Error: " + estring);
			System.exit(0);
		}
		
		@Override
		public void beginData(int type, Object polygonData)
		{
		}
		
		@Override
		public void endData(Object polygonData)
		{
		}
		
		@Override
		public void edgeFlag(boolean boundaryEdge)
		{
		}
		
		@Override
		public void edgeFlagData(boolean boundaryEdge, Object polygonData)
		{
		}
		
		@Override
		public void errorData(int errnum, Object polygonData)
		{
		}
	}// tessellCallBack
}
