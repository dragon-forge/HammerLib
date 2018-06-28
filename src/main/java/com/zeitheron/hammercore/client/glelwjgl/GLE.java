package com.zeitheron.hammercore.client.glelwjgl;

/**
 * $Id: GLE.java,v 1.3 1998/05/02 12:06:39 descarte Exp descarte $
 *
 * Copyright (c)1998 Arcane Technologies Ltd. <http://www.arcana.co.uk>
 *
 * OpenGL GLE Tubing/Extrusion library variables and methods declarations
 *
 * $Log: GLE.java,v $ Revision 1.3 1998/05/02 12:06:39 descarte Added copyright
 * information and minor documentation clean-up.
 *
 * Revision 1.2 1998/05/01 22:25:25 descarte Corrected value assigned to
 * TUBE_JN_CUT.
 *
 * Revision 1.1 1998/04/30 19:23:51 descarte Initial revision
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

/**
 * Encapsulation of the routines supplied by the GLE Tubing and Extrusion
 * library. All the constants defined with GLE are defined here also.
 * <P>
 * 
 * @version $Id: GLE.java,v 1.3 1998/05/02 12:06:39 descarte Exp descarte $
 * @author Alligator Descartes
 *         &lt;<A HREF="http://www.arcana.co.uk">http://www.arcana.co.uk</A>&gt;
 */
public interface GLE
{
	
	/** Version information */
	public static final String VERSION = new String("$Id: GLE.java,v 1.3 1998/05/02 12:06:39 descarte Exp descarte $");
	
	/** Summary execution mode of extended pipelines */
	public static final int SUMMARY = 0;
	
	/** Verbose execution mode of extended pipelines */
	public static final int VERBOSE = 1;
	
	/**
	 * Draw polycylinders, polycones, extrusions &amp;c with no special
	 * treatment of the extrusion ends
	 */
	public static final int TUBE_JN_RAW = 0x01;
	
	/**
	 * Draw polyclyinders, polycones, extrusions &amp;c by extending the
	 * different segments until the butt into each other with an angular style
	 */
	public static final int TUBE_JN_ANGLE = 0x02;
	
	/**
	 * Draw polycylinders, polycones, extrusions &amp;c by joining together the
	 * different segments and slicing off the joint at half the angle between
	 * the segments. A cap is drawn. Note that the slicing plane runs through
	 * the origin of the contour coordinate system. Thus, the amount of slice
	 * can be varied by offsetting the contour with respect to the origin.
	 * <P>
	 * Note that when two segments meet at a shallow angle, the cut join style
	 * will potentially shave off a whole lot of the contour leading to
	 * ``surprising'' results.
	 */
	public static final int TUBE_JN_CUT = 0x3;
	
	/**
	 * Joints will be rounded. Strictly speaking, the part of the joint above
	 * the origin will be rounded. The part below the origin will come together
	 * in an angular join.
	 */
	public static final int TUBE_JN_ROUND = 0x04;
	
	/**
	 * Mask bits. This can be used to mask off the bit field that defines the
	 * join style.
	 */
	public static final int TUBE_JN_MASK = 0x0f;
	
	/**
	 * If this is set, a cap will be drawn at each end of the extrusion
	 */
	public static final int TUBE_JN_CAP = 0x10;
	
	/**
	 * A normal vector is generated per facet. Useful for having an extrusion
	 * have a ``faceted'' look.
	 */
	public static final int TUBE_NORM_FACET = 0x100;
	
	/**
	 * Normal vectors are generated so that they lie along edges. Useful for
	 * making angular things look rounded under lighting. For example, when
	 * extruding a hexagon and using this flag, the hexagonal extrusion will
	 * look ( more like a ) smooth perfectly round cylinder, rather than a
	 * six-sided shape.
	 */
	public static final int TUBE_NORM_EDGE = 0x200;
	
	/**
	 * Normal vectors are generated so that they both lie on edges, and so that
	 * they interpolate between neighbouring segments. Useful for drawing
	 * ``spaghetti'' -- extrusions that follow a spline path. Because the spline
	 * path must be ``tessellated'' into small straight segments, each segment
	 * will look straight unless this flag is set.
	 */
	public static final int TUBE_NORM_PATH_EDGE = 0x400;
	
	/**
	 * A mask useful for masking out the ``norm'' bits
	 */
	public static final int TUBE_NORM_MASK = 0xf00;
	
	/**
	 * If this bit is set, the contour will be treated as a ``closed'' contour,
	 * where the last point connects back up to the first. It is useful to set
	 * this flag when drawing closed shapes ( such as extruded cylinders,
	 * star-shapes, I-Beams &amp;c. When drawing open extrusions, e.g.,
	 * corrugated sheet metal ), you DON'T want to set this flag!
	 */
	public static final int TUBE_CONTOUR_CLOSED = 0x1000;
	
	/**
	 * If this bit is set the texturing is enabled. If this bit is NOT set, then
	 * automatic texture coordinate generation is disabled.
	 */
	public static final int GLE_TEXTURE_ENABLE = 0x10000;
	
	/**
	 * Bitmask containing the texture styles
	 */
	public static final int GLE_TEXTURE_STYLE_MASK = 0xff;
	
	/**
	 * Uses the vertex' ``x'' coordinate as the texture ``u'' coordinate and the
	 * accumulated segment length as the ``v'' coordinate.
	 */
	public static final int GLE_TEXTURE_VERTEX_FLAT = 1;
	
	/**
	 * Uses the normal vector's ``x'' coordinate as the texture ``u'' coordinate
	 * and the accumulated segment length as the ``v'' coordinate
	 */
	public static final int GLE_TEXTURE_NORMAL_FLAT = 2;
	
	/**
	 * Uses u = phi / ( 2 * PI ) = arctan ( vy / vx ) / ( 2 * PI ) as the
	 * texture ``u'' coordinate and the accumulated segment length as the ``v''
	 * coordinate. In the above equation, ``vx'' and ``vy'' stand for the
	 * vertex' x and y coordinates
	 */
	public static final int GLE_TEXTURE_VERTEX_CYL = 3;
	
	/**
	 * Uses u = phi / ( 2 * PI ) = arctan ( ny / nx ) / ( 2 * PI ) as the
	 * texture ``u'' coordinate and the accumulated segment length as the ``v''
	 * coordinate. In the above equation, ``vx'' and ``vy'' stand for the
	 * normal's x and y coordinates
	 */
	public static final int GLE_TEXTURE_NORMAL_CYL = 4;
	
	/**
	 * Uses u = phi / ( 2 * PI ) = arctan ( vy / vx ) / ( 2 * PI ) as the
	 * texture ``u'' coordinate and v = theta / PI = ( 1.0 - arccos( vz ) ) / PI
	 * as the texture ``v'' coordinate. In the above equation, ``vx'', ``vy''
	 * and ``vz'' stand for the vertex' x, y and z coordinates
	 */
	public static final int GLE_TEXTURE_VERTEX_SPH = 5;
	
	/**
	 * Uses u = phi / ( 2 * PI ) = arctan ( ny / nx ) / ( 2 * PI ) as the
	 * texture ``u'' coordinate and v = theta / PI = ( 1.0 - arccos( nz ) ) / PI
	 * as the texture ``v'' coordinate. In the above equation, ``nx'', ``ny''
	 * and ``nz'' stand for the normal's x, y and z coordinates
	 */
	public static final int GLE_TEXTURE_NORMAL_SPH = 6;
	
	/**
	 * Behaves as GLE_TEXTURE_VERTEX_FLAT except that the untransformed vertices
	 * are used. As a result, textures tend to ``stick'' to the extrusion
	 * according to the extrusion's local surface coordinates rather than
	 * according to real-space coordinates. This will, in general, provide the
	 * correct style of texture mapping when affine transforms are being applied
	 * to the contour since the coordinates used are those prior to the affine
	 * transform.
	 */
	public static final int GLE_TEXTURE_VERTEX_MODEL_FLAT = 7;
	
	/**
	 * Behaves as GLE_TEXTURE_NORMAL_FLAT except that the untransformed vertices
	 * are used. As a result, textures tend to ``stick'' to the extrusion
	 * according to the extrusion's local surface coordinates rather than
	 * according to real-space coordinates. This will, in general, provide the
	 * correct style of texture mapping when affine transforms are being applied
	 * to the contour since the coordinates used are those prior to the affine
	 * transform.
	 */
	public static final int GLE_TEXTURE_NORMAL_MODEL_FLAT = 8;
	
	/**
	 * Behaves as GLE_TEXTURE_VERTEX_CYL except that the untransformed vertices
	 * are used. As a result, textures tend to ``stick'' to the extrusion
	 * according to the extrusion's local surface coordinates rather than
	 * according to real-space coordinates. This will, in general, provide the
	 * correct style of texture mapping when affine transforms are being applied
	 * to the contour since the coordinates used are those prior to the affine
	 * transform.
	 */
	public static final int GLE_TEXTURE_VERTEX_MODEL_CYL = 9;
	
	/**
	 * Behaves as GLE_TEXTURE_NORMAL_CYL except that the untransformed vertices
	 * are used. As a result, textures tend to ``stick'' to the extrusion
	 * according to the extrusion's local surface coordinates rather than
	 * according to real-space coordinates. This will, in general, provide the
	 * correct style of texture mapping when affine transforms are being applied
	 * to the contour since the coordinates used are those prior to the affine
	 * transform.
	 */
	public static final int GLE_TEXTURE_NORMAL_MODEL_CYL = 10;
	
	/**
	 * Behaves as GLE_TEXTURE_VERTEX_SPH except that the untransformed vertices
	 * are used. As a result, textures tend to ``stick'' to the extrusion
	 * according to the extrusion's local surface coordinates rather than
	 * according to real-space coordinates. This will, in general, provide the
	 * correct style of texture mapping when affine transforms are being applied
	 * to the contour since the coordinates used are those prior to the affine
	 * transform.
	 */
	public static final int GLE_TEXTURE_VERTEX_MODEL_SPH = 11;
	
	/**
	 * Behaves as GLE_TEXTURE_NORMAL_SPH except that the untransformed vertices
	 * are used. As a result, textures tend to ``stick'' to the extrusion
	 * according to the extrusion's local surface coordinates rather than
	 * according to real-space coordinates. This will, in general, provide the
	 * correct style of texture mapping when affine transforms are being applied
	 * to the contour since the coordinates used are those prior to the affine
	 * transform.
	 */
	public static final int GLE_TEXTURE_NORMAL_MODEL_SPH = 12;
	
	/**
	 * Returns the current join style for connected extruded segments
	 * <P>
	 */
	public int gleGetJoinStyle();
	
	/**
	 * Sets the current join style for connected extruded segments.
	 * <P>
	 * 
	 * @param style
	 *            The new join style
	 */
	public void gleSetJoinStyle(int style);
	
	/**
	 * Sets the current texture coordinate generation mode
	 * <P>
	 * 
	 * @param mode
	 *            Bitwise OR of texture flags
	 */
	public void gleTextureMode(int mode);
	
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
	public void glePolyCylinder(int npoints, double[][] pointArray, float[][] colourArray, double radius) throws GLEException;
	
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
	public void glePolyCone(int npoints, double[][] pointArray, float[][] colourArray, double[] radiusArray) throws GLEException;
	
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
	public void gleExtrusion(int ncp, double[][] contour, double[][] contourNormal, double[] up, int npoints, double[][] pointArray, float[][] colourArray) throws GLEException;
	
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
	public void gleTwistExtrusion(int ncp, double[][] contour, double[][] contourNormal, double[] up, int npoints, double[][] pointArray, float[][] colourArray, double[] twistArray) throws GLEException;
	
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
	public void gleSuperExtrusion(int ncp, double[][] contour, double[][] contourNormal, double[] up, int npoints, double[][] pointArray, float[][] colourArray, double[][][] xformArray) throws GLEException;
	
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
	public void gleSpiral(int ncp, double[][] contour, double[][] contourNormal, double[] up, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta) throws GLEException;
	
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
	public void gleLathe(int ncp, double[][] contour, double[][] contourNormal, double[] up, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta) throws GLEException;
	
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
	 * @see com.hermetica.gle.GLE#gleSpiral
	 */
	public void gleHelicoid(double rToroid, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta) throws GLEException;
	
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
	 * @see com.hermetica.gle.GLE#gleSpiral
	 */
	public void gleToroid(double rToroid, double startRadius, double drdTheta, double startZ, double dzdTheta, double[][] startTransform, double[][] dTransformdTheta, double startTheta, double sweepTheta) throws GLEException;
	
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
	public void gleScrew(int ncp, double[][] contour, double[][] contourNormal, double[] up, double startz, double endz, double twist) throws GLEException;
}
