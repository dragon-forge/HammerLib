/**
 * $Id: matrix.java,v 1.2 1998/05/05 23:31:09 descarte Exp descarte $
 *
 * Copyright (c)1998 Arcane Technologies Ltd. <http://www.arcana.co.uk>
 *
 * OpenGL GLE Tubing/Extrusion library matrix handling methods
 *
 * $Log: matrix.java,v $ Revision 1.2 1998/05/05 23:31:09 descarte Added all the
 * methods required for extruding shapes.
 *
 * Revision 1.1 1998/05/03 16:18:47 descarte Initial revision
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

import static org.lwjgl.opengl.GL11.glMultMatrix;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

/**
 * Miscellaneous matrix operations used by the GLE Tubing and Extrusion library.
 * <P>
 *
 * @version $Id: matrix.java,v 1.2 1998/05/05 23:31:09 descarte Exp descarte $
 * @author Alligator Descartes
 *         &lt;<A HREF="http://www.arcana.co.uk">http://www.arcana.co.uk</A>&gt;
 */
public class matrix
{
	
	/**
	 * Version information
	 */
	public static final String VERSION = new String("$Id: matrix.java,v 1.2 1998/05/05 23:31:09 descarte Exp descarte $");
	
	/**
	 * OpenGL pipeline used internally by GLE
	 */
	// private static CoreGL coregl_ = new CoreGL();
	// private static GL gl_ = coregl_;
	/**
	 * -- begin rot.h --
	 */
	/**
	 * Creates a rotation matrix about the x-axis
	 */
	private static final double[][] ROTX_CS(double cosine, double sine)
	{
		double[][] m = new double[4][4];
		m[0][0] = 1.0;
		m[0][1] = 0.0;
		m[0][2] = 0.0;
		m[0][3] = 0.0;
		
		m[1][0] = 0.0;
		m[1][1] = (cosine);
		m[1][2] = (sine);
		m[1][3] = 0.0;
		
		m[2][0] = 0.0;
		m[2][1] = -(sine);
		m[2][2] = (cosine);
		m[2][3] = 0.0;
		
		m[3][0] = 0.0;
		m[3][1] = 0.0;
		m[3][2] = 0.0;
		m[3][3] = 1.0;
		return m;
	}
	
	/**
	 * Creates a rotation matrix around the y-axis
	 */
	private static final double[][] ROTY_CS(double cosine, double sine)
	{
		double[][] m = new double[4][4];
		m[0][0] = (cosine);
		m[0][1] = 0.0;
		m[0][2] = -(sine);
		m[0][3] = 0.0;
		
		m[1][0] = 0.0;
		m[1][1] = 1.0;
		m[1][2] = 0.0;
		m[1][3] = 0.0;
		
		m[2][0] = (sine);
		m[2][1] = 0.0;
		m[2][2] = (cosine);
		m[2][3] = 0.0;
		
		m[3][0] = 0.0;
		m[3][1] = 0.0;
		m[3][2] = 0.0;
		m[3][3] = 1.0;
		
		return m;
	}
	
	/**
	 * Creates a rotation matrix around the z-axis
	 */
	private static final double[][] ROTZ_CS(double cosine, double sine)
	{
		double[][] m = new double[4][4];
		m[0][0] = (cosine);
		m[0][1] = (sine);
		m[0][2] = 0.0;
		m[0][3] = 0.0;
		
		m[1][0] = -(sine);
		m[1][1] = (cosine);
		m[1][2] = 0.0;
		m[1][3] = 0.0;
		
		m[2][0] = 0.0;
		m[2][1] = 0.0;
		m[2][2] = 1.0;
		m[2][3] = 0.0;
		
		m[3][0] = 0.0;
		m[3][1] = 0.0;
		m[3][2] = 0.0;
		m[3][3] = 1.0;
		
		return m;
	}
	
	private static DoubleBuffer getBufferedMatrix(double[][] m)
	{
		DoubleBuffer mbuffer = BufferUtils.createDoubleBuffer(16);
		mbuffer.put(new double[] { m[0][0], m[0][1], m[0][2], m[0][3], m[1][0], m[1][1], m[1][2], m[1][3], m[2][0], m[2][1], m[2][2], m[2][3], m[3][0], m[3][1], m[3][2], m[3][3] });
		
		mbuffer.flip();
		return mbuffer;
	}
	
	/**
	 * -- end of rot.h --
	 */
	/**
	 * -- begin rot_prince.c --
	 */
	/**
	 * Creates a matrix that represents rotation about the x-axis
	 */
	public static final double[][] urotx_cs_d(double cosine, double sine)
	{
		return ROTX_CS(cosine, sine);
	}
	
	/**
	 * Creates and loads a matrix that represents rotation about the x-axis
	 */
	public static final void rotx_cs_d(double cosine, double sine)
	{
		
		glMultMatrix(getBufferedMatrix(urotx_cs_d(cosine, sine)));
	}
	
	/**
	 * Creates a matrix that represents rotation about the y-axis
	 */
	public static final double[][] uroty_cs_d(double cosine, double sine)
	{
		return ROTX_CS(cosine, sine);
	}
	
	/**
	 * Creates and loads a matrix that represents rotation about the y-axis
	 */
	public static final void roty_cs_d(double cosine, double sine)
	{
		glMultMatrix(getBufferedMatrix(uroty_cs_d(cosine, sine)));
	}
	
	/**
	 * Creates a matrix that represents rotation about the z-axis
	 */
	public static final double[][] urotz_cs_d(double cosine, double sine)
	{
		return ROTX_CS(cosine, sine);
	}
	
	/**
	 * Creates and loads a matrix that represents rotation about the z-axis
	 */
	public static final void rotz_cs_d(double cosine, double sine)
	{
		glMultMatrix(getBufferedMatrix(urotz_cs_d(cosine, sine)));
	}
	
	/**
	 * Creates a matrix that represents rotation about the given axis
	 */
	public static final double[][] urot_cs_d(double cosine, double sine, char axis)
	{
		switch(axis)
		{
		case 'x':
		case 'X':
		{
			return urotx_cs_d(cosine, sine);
		}
		case 'y':
		case 'Y':
		{
			return uroty_cs_d(cosine, sine);
		}
		case 'z':
		case 'Z':
		{
			return urotz_cs_d(cosine, sine);
		}
		}
		return null;
	}
	
	/**
	 * Creates and loads a matrix that represents rotation about the given axis
	 */
	public static final void rot_cs_d(double cosine, double sine, char axis)
	{
		glMultMatrix(getBufferedMatrix(urot_cs_d(cosine, sine, axis)));
	}
	
	/**
	 * Generates a rotation matrix for rotation about principal axis; note that
	 * angle is measured in radians ( divide by 180, multiply by PI to convert
	 * from degrees )
	 */
	public static final double[][] urot_prince_d(double theta, char axis)
	{
		return urot_cs_d(Math.cos(theta), Math.sin(theta), axis);
	}
	
	/**
	 * Generates and loads a rotation matrix for rotation about principal axis;
	 * note that angle is measured in radians ( divide by 180, multiply by PI to
	 * convert from degrees )
	 */
	public static final void rot_prince_d(double theta, char axis)
	{
		glMultMatrix(getBufferedMatrix(urot_prince_d(theta, axis)));
	}
	
	/**
	 * -- End of rot_prince.c --
	 */
	/**
	 * -- Begin rotate.c --
	 */
	/**
	 * Generates and loads a rotation about axis matrix
	 */
	public static final void rot_axis_d(double omega, double[] axis)
	{
		glMultMatrix(getBufferedMatrix(urot_axis_d(omega, axis)));
	}
	
	/**
	 *
	 */
	public static final void rot_about_axis_d(double angle, double[] axis)
	{
		glMultMatrix(getBufferedMatrix(urot_about_axis_d(angle, axis)));
	}
	
	/**
	 *
	 */
	public static final void rot_omega_d(double[] axis)
	{
		glMultMatrix(getBufferedMatrix(urot_omega_d(axis)));
	}
	
	/**
	 * -- End of rotate.c --
	 */
	/**
	 * -- Begin urotate.c --
	 */
	/**
	 *
	 */
	public static final double[][] urot_axis_d(double omega, double[] axis)
	{
		
		double[][] m = new double[4][4];
		double c, s, ssq, csq, cts;
		double tmp;
		
		if(axis.length != 3)
		{
			throw new GLEException("Length of axis parameter != 3. This is not a valid vector!");
		}
		
		/* The formula coded up below can be derived by using the homomorphism
		 * between SU(2) and O(3), namely, that the 3x3 rotation matrix R is
		 * given by t.R.v = S(-1) t.v S where t are the Pauli matrices (similar
		 * to Quaternions, easier to use) v is an arbitrary 3-vector and S is a
		 * 2x2 hermitian matrix: S = exp ( i omega t.axis / 2 )
		 *
		 * (Also, remember that computer graphics uses the transpose of R).
		 *
		 * The Pauli matrices are:
		 *
		 * tx = (0 1) ty = (0 -i) tz = (1 0) (1 0) (i 0) (0 -1)
		 *
		 * Note that no error checking is done -- if the axis vector is not of
		 * unit length, you'll get strange results. */
		tmp = omega / 2.0f;
		s = Math.sin(tmp);
		c = Math.cos(tmp);
		
		ssq = s * s;
		csq = c * c;
		
		m[0][0] = m[1][1] = m[2][2] = csq - ssq;
		
		ssq *= 2.0;
		
		/**
		 * on-diagonal entries
		 */
		m[0][0] += ssq * axis[0] * axis[0];
		m[1][1] += ssq * axis[1] * axis[1];
		m[2][2] += ssq * axis[2] * axis[2];
		
		/**
		 * off-diagonal entries
		 */
		m[0][1] = m[1][0] = axis[0] * axis[1] * ssq;
		m[1][2] = m[2][1] = axis[1] * axis[2] * ssq;
		m[2][0] = m[0][2] = axis[2] * axis[0] * ssq;
		
		cts = 2.0 * c * s;
		
		tmp = cts * axis[2];
		m[0][1] += tmp;
		m[1][0] -= tmp;
		
		tmp = cts * axis[0];
		m[1][2] += tmp;
		m[2][1] -= tmp;
		
		tmp = cts * axis[1];
		m[2][0] += tmp;
		m[0][2] -= tmp;
		
		/* homogeneous entries */
		m[0][3] = m[1][3] = m[2][3] = m[3][2] = m[3][1] = m[3][0] = 0.0;
		m[3][3] = 1.0;
		
		return m;
	}
	
	/**
	 *
	 */
	public static final double[][] urot_about_axis_d(double angle, double[] axis)
	{
		
		double[][] m = null;
		double len;
		double[] ax = new double[3];
		double ang = angle;
		
		if(axis.length != 3)
		{
			throw new GLEException("Length of axis parameter != 3. This is not a valid vector!");
		}
		
		ang *= Math.PI / 180.0;
		
		/* renormalize axis vector, if needed */
		len = axis[0] * axis[0] + axis[1] * axis[1] + axis[2] * axis[2];
		
		/* we can save some machine instructions by normalizing only if needed.
		 * The compiler should be able to schedule in the if test "for free". */
		if(len != 1.0)
		{
			len = 1.0 / Math.sqrt(len);
			ax[0] = axis[0] * len;
			ax[1] = axis[1] * len;
			ax[2] = axis[2] * len;
			m = urot_axis_d(ang, ax);
		} else
		{
			m = urot_axis_d(ang, axis);
		}
		
		return m;
	}
	
	/**
	 *
	 */
	public static final double[][] urot_omega_d(double[] axis)
	{
		
		double[][] m = null;
		double len;
		double[] ax = new double[3];
		
		/* normalize axis vector */
		len = axis[0] * axis[0] + axis[1] * axis[1] + axis[2] * axis[2];
		
		len = 1.0 / Math.sqrt(len);
		ax[0] = axis[0] * len;
		ax[1] = axis[1] * len;
		ax[2] = axis[2] * len;
		
		/* the amount of rotation is equal to the length, in radians */
		return urot_axis_d(len, ax);
	}
	
	/**
	 * -- End urotate.c --
	 */
	/**
	 * -- Begin vvector.h --
	 */
	/**
	 * Returns a new vector with all coefficients set to 0
	 */
	public static final double[] VEC_ZERO()
	{
		double[] vtmp = new double[3];
		vtmp[0] = vtmp[1] = vtmp[2] = 0;
		return vtmp;
	}
	
	/**
	 * Returns a new vector as the given vector normalized
	 */
	public static final double[] VEC_NORMALIZE(double[] v)
	{
		double[] vtmp = new double[3];
		double vlen = VEC_LENGTH(v);
		if(vlen != 0.0)
		{
			vlen = 1.0 / vlen;
			vtmp[0] = v[0] * vlen;
			vtmp[1] = v[1] * vlen;
			vtmp[2] = v[2] * vlen;
		}
		return vtmp;
	}
	
	/**
	 * Vector reflection. Takes vector v and reflects it against reflector n.
	 * Returns the new reflected vector
	 */
	public static final double[] VEC_REFLECT(double[] v, double[] n)
	{
		double[] vtmp = new double[3];
		double dot = VEC_DOT_PRODUCT(v, n);
		vtmp[0] = v[0] - 2.0 * dot * n[0];
		vtmp[1] = v[1] - 2.0 * dot * n[1];
		vtmp[2] = v[2] - 2.0 * dot * n[2];
		
		return vtmp;
	}
	
	/**
	 * Returns a new vector as a copy of the given vector
	 */
	public static final double[] VEC_COPY_2(double[] v)
	{
		double[] vtmp = new double[3];
		vtmp[0] = v[0];
		vtmp[1] = v[1];
		return vtmp;
	}
	
	/**
	 * Returns a new vector as a copy of the given vector
	 */
	public static final double[] VEC_COPY(double[] v)
	{
		double[] vtmp = new double[3];
		vtmp[0] = v[0];
		vtmp[1] = v[1];
		vtmp[2] = v[2];
		return vtmp;
	}
	
	/**
	 * Returns the length of the given vector
	 */
	public static final double VEC_LENGTH_2(double[] v)
	{
		double length = v[0] * v[0] + v[1] * v[1];
		return length;
	}
	
	/**
	 * Returns the length of the given vector
	 */
	public static final double VEC_LENGTH(double[] v)
	{
		double length = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
		return length;
	}
	
	/**
	 * Returns a new vector as a scaled version of the given one
	 */
	public static final double[] VEC_SCALE(double scale, double[] v)
	{
		double[] vtmp = new double[3];
		vtmp[0] = scale * v[0];
		vtmp[1] = scale * v[1];
		vtmp[2] = scale * v[2];
		return vtmp;
	}
	
	/**
	 * Calculates the cross product of the two given vectors
	 */
	public static final double[] VEC_CROSS_PRODUCT(double[] v1, double[] v2)
	{
		double[] vtmp = new double[3];
		vtmp[0] = v1[1] * v2[2] - v1[2] * v2[1];
		vtmp[1] = v1[2] * v2[0] - v1[0] * v2[2];
		vtmp[2] = v1[0] * v2[1] - v1[1] * v2[0];
		return vtmp;
	}
	
	/**
	 * Calculates the dot product of the two given vectors
	 */
	public static final double VEC_DOT_PRODUCT(double[] v1, double[] v2)
	{
		double dot = 0;
		if(v1.length != 3 || v2.length != 3)
		{
			throw new GLEException("Length of v1 or v2 != 3. Invalid vectors!");
		}
		
		dot = v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
		return dot;
	}
	
	/**
	 * Calculates vector parallel to the given vectors
	 */
	public static final double[] VEC_PERP(double[] v, double[] n)
	{
		double[] vtmp = new double[3];
		double dot = VEC_DOT_PRODUCT(v, n);
		
		if(v.length != 3 || n.length != 3)
		{
			throw new GLEException("Length of v or n !=3. Invalid vectors!");
		}
		
		vtmp[0] = v[0] - dot * n[0];
		vtmp[1] = v[1] - dot * n[1];
		vtmp[2] = v[2] - dot * n[2];
		
		return vtmp;
	}
	
	/**
	 * Returns a new vector containing the difference between the two given
	 * vectors
	 */
	public static final double[] VEC_DIFF(double[] v2, double[] v1)
	{
		double[] vtmp = new double[3];
		
		if(v1.length != 3 || v2.length != 3)
		{
			throw new GLEException("Length of v1 or v2 != 3. Invalid vectors!");
		}
		
		vtmp[0] = v2[0] - v1[0];
		vtmp[1] = v2[1] - v1[1];
		vtmp[2] = v2[2] - v1[2];
		
		return vtmp;
	}
	
	/**
	 * Returns a new vector created from the sum of the two given vectors
	 */
	public static final double[] VEC_SUM(double[] v1, double[] v2)
	{
		double[] vtmp = new double[3];
		
		if(v1.length != 3 || v2.length != 3)
		{
			throw new GLEException("Length of v1 or v2 != 3. Invalid vectors!");
		}
		
		vtmp[0] = v2[0] + v1[0];
		vtmp[1] = v2[1] + v1[1];
		vtmp[2] = v2[2] + v1[2];
		
		return vtmp;
	}
	
	/**
	 * Initializes a 3x3 matrix
	 */
	public static final double[][] IDENTIFY_MATRIX_3X3()
	{
		double[][] m = new double[3][3];
		
		m[0][0] = 1.0;
		m[0][1] = 0.0;
		m[0][2] = 0.0;
		
		m[1][0] = 0.0;
		m[1][1] = 1.0;
		m[1][2] = 0.0;
		
		m[2][0] = 0.0;
		m[2][1] = 0.0;
		m[2][2] = 1.0;
		
		return m;
	}
	
	/**
	 * Initializes a 4x4 matrix
	 */
	public static final double[][] IDENTIFY_MATRIX_4X4()
	{
		double[][] m = new double[4][4];
		
		m[0][0] = 1.0;
		m[0][1] = 0.0;
		m[0][2] = 0.0;
		m[0][3] = 0.0;
		
		m[1][0] = 0.0;
		m[1][1] = 1.0;
		m[1][2] = 0.0;
		m[1][3] = 0.0;
		
		m[2][0] = 0.0;
		m[2][1] = 0.0;
		m[2][2] = 1.0;
		m[2][3] = 0.0;
		
		m[3][0] = 0.0;
		m[3][1] = 0.0;
		m[3][2] = 0.0;
		m[3][3] = 1.0;
		
		return m;
	}
	
	/**
	 * Returns a new 2x2 matrix that is a copy of the given 2x2 matrix
	 */
	public static final double[][] COPY_MATRIX_2X2(double[][] a)
	{
		double[][] b = new double[2][2];
		
		b[0][0] = a[0][0];
		b[0][1] = a[0][1];
		
		b[1][0] = a[1][0];
		b[1][1] = a[1][1];
		
		return b;
	}
	
	/**
	 * Returns a new 2x3 matrix that is a copy of the given 2x3 matrix
	 */
	public static final double[][] COPY_MATRIX_2X3(double[][] a)
	{
		double[][] b = new double[2][3];
		
		b[0][0] = a[0][0];
		b[0][1] = a[0][1];
		b[0][2] = a[0][2];
		
		b[1][0] = a[1][0];
		b[1][1] = a[1][1];
		b[1][2] = a[1][2];
		
		return b;
	}
	
	/**
	 * Returns a new 4x4 matrix that is a copy of the given 4x4 matrix
	 */
	public static final double[][] COPY_MATRIX_4X4(double[][] a)
	{
		double[][] b = new double[4][4];
		
		b[0][0] = a[0][0];
		b[0][1] = a[0][1];
		b[0][2] = a[0][2];
		b[0][3] = a[0][3];
		
		b[1][0] = a[1][0];
		b[1][1] = a[1][1];
		b[1][2] = a[1][2];
		b[1][3] = a[1][3];
		
		b[2][0] = a[2][0];
		b[2][1] = a[2][1];
		b[2][2] = a[2][2];
		b[2][3] = a[2][3];
		
		b[3][0] = a[3][0];
		b[3][1] = a[3][1];
		b[3][2] = a[3][2];
		b[3][3] = a[3][3];
		
		return b;
	}
	
	/**
	 * Returns a new matrix as the product of the two given matrices
	 */
	public static final double[][] MATRIX_PRODUCT_2X2(double[][] a, double[][] b)
	{
		
		double[][] c = new double[2][2];
		
		c[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0];
		c[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1];
		
		c[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0];
		c[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1];
		
		return c;
	}
	
	/**
	 * Returns a new matrix as the product of the two given matrices
	 */
	public static final double[][] MATRIX_PRODUCT_4X4(double[][] a, double[][] b)
	{
		double[][] c = new double[4][4];
		
		c[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0] + a[0][2] * b[2][0] + a[0][3] * b[3][0];
		c[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1] + a[0][2] * b[2][1] + a[0][3] * b[3][1];
		c[0][2] = a[0][0] * b[0][2] + a[0][1] * b[1][2] + a[0][2] * b[2][2] + a[0][3] * b[3][2];
		c[0][3] = a[0][0] * b[0][3] + a[0][1] * b[1][3] + a[0][2] * b[2][3] + a[0][3] * b[3][3];
		
		c[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0] + a[1][2] * b[2][0] + a[1][3] * b[3][0];
		c[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1] + a[1][2] * b[2][1] + a[1][3] * b[3][1];
		c[1][2] = a[1][0] * b[0][2] + a[1][1] * b[1][2] + a[1][2] * b[2][2] + a[1][3] * b[3][2];
		c[1][3] = a[1][0] * b[0][3] + a[1][1] * b[1][3] + a[1][2] * b[2][3] + a[1][3] * b[3][3];
		
		c[2][0] = a[2][0] * b[0][0] + a[2][1] * b[1][0] + a[2][2] * b[2][0] + a[2][3] * b[3][0];
		c[2][1] = a[2][0] * b[0][1] + a[2][1] * b[1][1] + a[2][2] * b[2][1] + a[2][3] * b[3][1];
		c[2][2] = a[2][0] * b[0][2] + a[2][1] * b[1][2] + a[2][2] * b[2][2] + a[2][3] * b[3][2];
		c[2][3] = a[2][0] * b[0][3] + a[2][1] * b[1][3] + a[2][2] * b[2][3] + a[2][3] * b[3][3];
		
		c[3][0] = a[3][0] * b[0][0] + a[3][1] * b[1][0] + a[3][2] * b[2][0] + a[3][3] * b[3][0];
		c[3][1] = a[3][0] * b[0][1] + a[3][1] * b[1][1] + a[3][2] * b[2][1] + a[3][3] * b[3][1];
		c[3][2] = a[3][0] * b[0][2] + a[3][1] * b[1][2] + a[3][2] * b[2][2] + a[3][3] * b[3][2];
		c[3][3] = a[3][0] * b[0][3] + a[3][1] * b[1][3] + a[3][2] * b[2][3] + a[3][3] * b[3][3];
		
		return c;
	}
	
	/**
	 * Affine matrix times vector. The matrix is assumed to be an affine matrix
	 * with last two entries representing a translation
	 */
	public static final double[] MAT_DOT_VEC_2X3(double[][] m, double[] v)
	{
		double[] vtmp = new double[3];
		vtmp[0] = m[0][0] * v[0] + m[0][1] * v[1] + m[0][2];
		vtmp[1] = m[1][0] * v[0] + m[1][1] * v[1] + m[1][2];
		vtmp[2] = 0.0;
		return vtmp;
	}
	
	/**
	 * Matrix times vector
	 */
	public static final double[] MAT_DOT_VEC_3X3(double[][] m, double[] v)
	{
		double[] vtmp = new double[3];
		
		vtmp[0] = m[0][0] * v[0] + m[0][1] * v[1] + m[0][2] * v[2];
		vtmp[1] = m[1][0] * v[0] + m[1][1] * v[1] + m[1][2] * v[2];
		vtmp[2] = m[2][0] * v[0] + m[2][1] * v[1] + m[2][2] * v[2];
		
		return vtmp;
	}
	
	/**
	 * transform normal vector by inverse transpose of matrix and then
	 * renormalize the vector
	 *
	 * This macro computes inverse transpose of matrix m, and multiplies vector
	 * v into it, to yeild vector p Vector p is then normalized.
	 */
	public static final double[] NORM_XFORM_2X2(double[][] m, double[] v)
	{
		double len = 0;
		double[] p = new double[3];
		
		/**
		 * do nothing if off-diagonals are zero and diagonals are equal
		 */
		if((m[0][1] != 0.0) || (m[1][0] != 0.0) || (m[0][0] != m[1][1]))
		{
			p[0] = m[1][1] * v[0] - m[1][0] * v[1];
			p[1] = -m[0][1] * v[0] + m[0][0] * v[1];
			
			len = p[0] * p[0] + p[1] * p[1];
			len = 1.0 / Math.sqrt(len);
			p[0] *= len;
			p[1] *= len;
		} else
		{
			p = VEC_COPY_2(v);
		}
		
		return p;
	}
	
	/**
	 * -- End vvector.h --
	 */
	/**
	 * -- Begin view.c --
	 */
	/**
	 * The uviewdirection subroutine computes and returns a 4x4 rotation matrix
	 * that puts the negative z axis along the direction v21 and puts the y axis
	 * along the up vector.
	 *
	 * Note that this code is fairly tolerant of "weird" paramters. It
	 * normalizes when necessary, it does nothing when vectors are of zero
	 * length, or are co-linear. This code shouldn't croak, no matter what the
	 * user sends in as arguments.
	 */
	public static final double[][] uview_direction_d(double[] v21, double[] up)
	{
		double[][] amat = null;
		double[][] bmat = null;
		double[][] cmat = null;
		double[] v_hat_21 = new double[3];
		double[] v_xy = new double[3];
		double sine, cosine;
		double len;
		double[] up_proj = new double[3];
		double[] tmp = new double[3];
		double[][] m = null;
		
		/**
		 * Find the unit vector that points in the v21 direction
		 */
		v_hat_21 = VEC_COPY(v21);
		len = VEC_LENGTH(v_hat_21);
		if(len != 0.0)
		{
			len = 1.0 / len;
			v_hat_21 = VEC_SCALE(len, v_hat_21);
			
			/**
			 * Rotate z in the xz-plane until same latitude
			 */
			sine = Math.sqrt(1.0 - v_hat_21[2] * v_hat_21[2]);
			amat = ROTY_CS(-v_hat_21[2], (-sine));
		} else
		{
			amat = IDENTIFY_MATRIX_4X4();
		}
		
		// System.err.println( "amat: " + amat[0][0] + ", " + amat[0][1] + ", "
		// + amat[0][2] );
		
		/* project v21 onto the xy plane */
		v_xy[0] = v21[0];
		v_xy[1] = v21[1];
		v_xy[2] = 0.0;
		len = VEC_LENGTH(v_xy);
		
		/* rotate in the x-y plane until v21 lies on z axis --- but of course,
		 * if its already there, do nothing */
		if(len != 0.0)
		{
			/* want xy projection to be unit vector, so that sines/cosines pop
			 * out */
			len = 1.0 / len;
			v_xy = VEC_SCALE(len, v_xy);
			
			/* rotate the projection of v21 in the xy-plane over to the x
			 * axis */
			bmat = ROTZ_CS(v_xy[0], v_xy[1]);
			
			/* concatenate these together */
			cmat = MATRIX_PRODUCT_4X4(amat, bmat);
		} else
		{
			/**
			 * no-op -- vector is already in correct position
			 */
			cmat = COPY_MATRIX_4X4(amat);
		}
		
		/* up vector really should be perpendicular to the x-form direction --
		 * Use up a couple of cycles, and make sure it is, just in case the user
		 * blew it. */
		up_proj = VEC_PERP(up, v_hat_21);
		// System.err.println( "up_proj: " + up_proj[0] + ", " + up_proj[1] + ",
		// " + up_proj[2] + " )" );
		len = VEC_LENGTH(up_proj);
		if(len != 0.0)
		{
			/* normalize the vector */
			len = 1.0 / len;
			up_proj = VEC_SCALE(len, up_proj);
			
			/* compare the up-vector to the y-axis to get the cosine of the
			 * angle */
			tmp[0] = cmat[1][0];
			tmp[1] = cmat[1][1];
			tmp[2] = cmat[1][2];
			cosine = VEC_DOT_PRODUCT(tmp, up_proj);
			
			/* compare the up-vector to the x-axis to get the sine of the
			 * angle */
			tmp[0] = cmat[0][0];
			tmp[1] = cmat[0][1];
			tmp[2] = cmat[0][2];
			sine = VEC_DOT_PRODUCT(tmp, up_proj);
			
			/* rotate to align the up vector with the y-axis */
			amat = ROTZ_CS(cosine, -sine);
			
			/* This xform, although computed last, acts first */
			m = MATRIX_PRODUCT_4X4(amat, cmat);
		} else
		{
			/* error condition: up vector is indeterminate (zero length) -- do
			 * nothing */
			m = COPY_MATRIX_4X4(cmat);
		}
		return m;
	}
	
	/**
	 *
	 */
	public static final double[][] uviewpoint_d(double[] v1, double[] v2, double[] up)
	{
		
		double[] v_hat_21 = null;
		double[][] trans_mat = null;
		double[][] rot_mat = null;
		double[][] m = null;
		
		/**
		 * Find the vector that points in the v21 direction
		 */
		v_hat_21 = VEC_DIFF(v2, v1);
		
		/* System.err.println( "v_hat_21: ( " + v_hat_21[0] + ", " + v_hat_21[1]
		 * + ", " + v_hat_21[2] + " )" ); */
		
		/**
		 * Compute rotation matrix that takes -z axis to the v21 axis, and y to
		 * the up dierction
		 */
		rot_mat = uview_direction_d(v_hat_21, up);
		
		/* build matrix that translates the origin to v1 */
		trans_mat = IDENTIFY_MATRIX_4X4();
		trans_mat[3][0] = v1[0];
		trans_mat[3][1] = v1[1];
		trans_mat[3][2] = v1[2];
		
		/* concatenate the matrices together */
		m = MATRIX_PRODUCT_4X4(rot_mat, trans_mat);
		
		return m;
	}
	/**
	 * -- End view.c --
	 */
}
