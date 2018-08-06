/**
 * $Id: intersect.java,v 1.1 1998/05/05 23:31:21 descarte Exp descarte $
 *
 * Copyright (c)1998 Arcane Technologies Ltd. <http://www.arcana.co.uk>
 *
 * OpenGL GLE Tubing/Extrusion library intersection calculations
 *
 * $Log: intersect.java,v $ Revision 1.1 1998/05/05 23:31:21 descarte Initial
 * revision
 *
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

/**
 * Miscellaneous intersection and degenerate vector operations used by the GLE
 * Tubing and Extrusion library.
 * <P>
 * 
 * @version $Id: intersect.java,v 1.1 1998/05/05 23:31:21 descarte Exp descarte
 *          $
 * @author Alligator Descartes
 *         &lt;<A HREF="http://www.arcana.co.uk">http://www.arcana.co.uk</A>&gt;
 */
public class intersect
{
	
	/** Version information */
	public static final String VERSION = new String("$Id: intersect.java,v 1.1 1998/05/05 23:31:21 descarte Exp descarte $");
	
	/** Degenerate tolerance */
	private static final double DEGENERATE_TOLERANCE = 0.000002;
	
	/**
	 * This macro is used in several places to cycle through a series of points
	 * to find the next non-degenerate point in a series
	 * 
	 * @param index
	 *            int
	 * @param npoints
	 *            int
	 * @param len
	 *            double
	 * @param diff
	 *            double[]
	 * @param point_array
	 *            double[][]
	 * @return int
	 */
	public static final int FIND_NON_DEGENERATE_POINT(int index, int npoints, double len, double[] diff, double[][] point_array)
	{
		
		double slen;
		double[] summa = null;
		int i = index;
		double tlen = len;
		double[] tdiff = null;
		
		do
		{
			/* get distance to next point */
			/* System.err.println( "FIND_NON_DEGENERATE_POINT " + i );
			 * System.err.println( "pointArray[i+1]: " + point_array[i+1][0] +
			 * ", " + point_array[i+1][1] + ", " + point_array[i+1][2] );
			 * System.err.println( "pointArray[i]: " + point_array[i][0] + ", "
			 * + point_array[i][1] + ", " + point_array[i][2] ); */
			tdiff = matrix.VEC_DIFF(point_array[i + 1], point_array[i]);
			// System.err.println( "tdiff( " + tdiff[0] + ", " + tdiff[1] + ", "
			// + tdiff[2] + " )" );
			diff[0] = tdiff[0];
			diff[1] = tdiff[1];
			diff[2] = tdiff[2];
			tlen = matrix.VEC_LENGTH(diff);
			len = tlen;
			summa = matrix.VEC_SUM(point_array[i + 1], point_array[i]);
			slen = matrix.VEC_LENGTH(summa);
			slen *= DEGENERATE_TOLERANCE;
			i++;
		} while((tlen <= slen) && (i < npoints - 1));
		return i;
	}
	
	/* The macro and subroutine INTERSECT are designed to compute the
	 * intersection of a line (defined by the points v1 and v2) and a plane
	 * (defined as plane which is normal to the vector n, and contains the point
	 * p). Both return the point sect, which is the point of interesection.
	 *
	 * This MACRO attemps to be fairly robust by checking for a divide by
	 * zero. */
	
	/* HACK ALERT The intersection parameter t has the nice property that if
	 * t>1, then the intersection is "in front of" p1, and if t<0, then the
	 * intersection is "behind" p2. Unfortunately, as the intersecting plane and
	 * the line become parallel, t wraps through infinity -- i.e. t can become
	 * so large that t becomes "greater than infinity" and comes back as a
	 * negative number (i.e. winding number hopped by one unit). We have no way
	 * of detecting this situation without adding gazzillions of lines of code
	 * of topological algebra to detect the winding number; and this would be
	 * incredibly difficult, and ruin performance.
	 * 
	 * Thus, we've installed a cheap hack for use by the "cut style" drawing
	 * routines. If t proves to be a large negative number (more negative than
	 * -5), then we assume that t was positive and wound through infinity. This
	 * makes most cuts look good, without introducing bogus cuts at infinity. */
	/* ========================================================== */
	public static final double[] INNERSECT(double[] p, double[] n, double[] v1, double[] v2)
	{
		
		double deno = 0, numer = 0, t = 0, omt = 0;
		boolean valid = false;
		double[] sect = new double[3];
		
		deno = (v1[0] - v2[0]) * n[0];
		deno += (v1[1] - v2[1]) * n[1];
		deno += (v1[2] - v2[2]) * n[2];
		
		if(deno == 0.0)
		{
			valid = false;
			n = matrix.VEC_COPY(v1);
		} else
		{
			valid = true;
			numer = (p[0] - v2[0]) * n[0];
			numer += (p[1] - v2[1]) * n[1];
			numer += (p[2] - v2[2]) * n[2];
			
			t = numer / deno;
			omt = 1.0 - t;
			
			/** if ( t < -5.0 valid = 2; HACK ALERT See above */
			
			/** if t is HUGE, then plane and line are almost co-planar */
			if((1.0 < t * DEGENERATE_TOLERANCE) || (-1.0 > t * DEGENERATE_TOLERANCE))
			{
				valid = false;
			}
			
			sect[0] = t * v1[0] + omt * v2[0];
			sect[1] = t * v1[1] + omt * v2[1];
			sect[2] = t * v1[2] + omt * v2[2];
		}
		return sect;
	}
	
	/* The macro and subroutine BISECTING_PLANE compute a normal vecotr that
	 * describes the bisecting plane between three points (v1, v2 and v3). This
	 * bisecting plane has the following properties: 1) it contains the point v2
	 * 2) the angle it makes with v21 == v2 - v1 is equal to the angle it makes
	 * with v32 == v3 - v2 3) it is perpendicular to the plane defined by v1,
	 * v2, v3.
	 *
	 * Having input v1, v2, and v3, it returns a vector n. Note that n is NOT
	 * normalized (is NOT of unit length).
	 * 
	 * The subroutine returns a value indicating if the specified inputs
	 * represented a degenerate case. Valid is TRUE if the computed intersection
	 * is valid, else it is FALSE. */
	public static final double[] bisecting_plane(double[] v1, double[] v2, double[] v3)
	{
		boolean valid = false;
		double[] v21 = null, v32 = null;
		double len21, len32;
		double dot;
		double[] n = new double[3];
		
		v21 = matrix.VEC_DIFF(v2, v1);
		v32 = matrix.VEC_DIFF(v3, v2);
		
		len21 = matrix.VEC_LENGTH(v21);
		len32 = matrix.VEC_LENGTH(v32);
		
		if(len21 <= DEGENERATE_TOLERANCE * len32)
		{
			if(len32 == 0.0)
			{
				/* all three points lie ontop of one-another */
				n = matrix.VEC_ZERO();
				valid = false;
			} else
			{
				/* return a normalized copy of v32 as bisector */
				len32 = 1.0 / len32;
				n = matrix.VEC_SCALE(len32, v32);
				valid = true;
			}
		} else
		{
			valid = true;
			if(len32 <= DEGENERATE_TOLERANCE * len21)
			{
				/* return a normalized copy of v21 as bisector */
				len21 = 1.0 / len21;
				n = matrix.VEC_SCALE(len21, v21);
			} else
			{
				/* normalize v21 to be of unit length */
				len21 = 1.0 / len21;
				v21 = matrix.VEC_SCALE(len21, v21);
				
				/* normalize v32 to be of unit length */
				len32 = 1.0 / len32;
				v32 = matrix.VEC_SCALE(len32, v32);
				
				dot = matrix.VEC_DOT_PRODUCT(v32, v21);
				
				/* if dot == 1 or -1, then points are colinear */
				if((dot >= (1.0 - DEGENERATE_TOLERANCE)) || (dot <= (-1.0 + DEGENERATE_TOLERANCE)))
				{
					n = matrix.VEC_COPY(v21);
				} else
				{
					/* go do the full computation */
					n[0] = dot * (v32[0] + v21[0]) - v32[0] - v21[0];
					n[1] = dot * (v32[1] + v21[1]) - v32[1] - v21[1];
					n[2] = dot * (v32[2] + v21[2]) - v32[2] - v21[2];
					
					/**
					 * if above if-test's passed, n should NEVER be of zero
					 * length
					 */
					n = matrix.VEC_NORMALIZE(n);
				}
			}
		}
		return n;
	}
	
	/* This macro computes the plane perpendicular to the the plane defined by
	 * three points, and whose normal vector is givven as the difference between
	 * the two vectors ...
	 * 
	 * (See way below for the "math" model if you want to understand this. The
	 * comments about relative errors above apply here.) */
	public static final boolean CUTTING_PLANE(double[] n, double[] v1, double[] v2, double[] v3)
	{
		
		double[] v21 = new double[3];
		double[] v32 = new double[3];
		double len21 = 0, len32 = 0;
		double lendiff = 0;
		boolean valid = false;
		double[] vtmp = matrix.VEC_COPY(n);
		
		v21 = matrix.VEC_DIFF(v2, v1);
		v32 = matrix.VEC_DIFF(v3, v2);
		
		len21 = matrix.VEC_LENGTH(v21);
		len32 = matrix.VEC_LENGTH(v32);
		
		if(len21 <= DEGENERATE_TOLERANCE * len32)
		{
			if(len32 == 0.0)
			{
				vtmp = matrix.VEC_ZERO();
				valid = false;
			} else
			{
				len32 = 1.0 / len32;
				vtmp = matrix.VEC_SCALE(len32, v32);
				valid = true;
			}
		} else
		{
			valid = true;
			
			if(len32 <= DEGENERATE_TOLERANCE * len21)
			{
				len21 = 1.0 / len21;
				vtmp = matrix.VEC_SCALE(len21, v21);
			} else
			{
				len21 = 1.0 / len21;
				v21 = matrix.VEC_SCALE(len21, v21);
				
				len32 = 1.0 / len32;
				v32 = matrix.VEC_SCALE(len32, v32);
				
				vtmp = matrix.VEC_DIFF(v21, v32);
				lendiff = matrix.VEC_LENGTH(vtmp);
				
				if(lendiff < DEGENERATE_TOLERANCE)
				{
					vtmp = matrix.VEC_ZERO();
					valid = false;
				} else
				{
					lendiff = 1.0 / lendiff;
					vtmp = matrix.VEC_SCALE(lendiff, vtmp);
				}
			}
		}
		n[0] = vtmp[0];
		n[1] = vtmp[1];
		n[2] = vtmp[2];
		return valid;
	}
	
	/**
	 * Returns true if the three points are colinear, else false. Note that if
	 * any two points are degenerate, then they are colinear. We are careful to
	 * make sure that the comparison is unit-less, i.e. that the lengths of the
	 * individual segments is normalized. By re-arrangement, avoid overhead of
	 * two square roots and two divides.
	 * 
	 * @param v1
	 *            double[]
	 * @param v2
	 *            double[]
	 * @param v3
	 *            double[]
	 * @return boolean
	 */
	public static boolean COLINEAR(double[] v1, double[] v2, double[] v3)
	{
		double[] v21 = new double[3];
		double[] v32 = new double[3];
		boolean rv = false;
		double len21 = 0, len32 = 0, dot = 0;
		
		v21 = matrix.VEC_DIFF(v2, v1);
		v32 = matrix.VEC_DIFF(v3, v2);
		
		len21 = matrix.VEC_DOT_PRODUCT(v21, v21);
		len32 = matrix.VEC_DOT_PRODUCT(v32, v32);
		rv = (len32 <= (DEGENERATE_TOLERANCE * len21));
		rv = rv || (len21 <= (DEGENERATE_TOLERANCE * len32));
		dot = matrix.VEC_DOT_PRODUCT(v21, v32);
		rv = rv || ((len21 * len32 - dot * dot) <= (len21 * len32 * DEGENERATE_TOLERANCE * DEGENERATE_TOLERANCE));
		
		return rv;
	}
}