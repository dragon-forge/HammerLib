package com.pengu.hammercore.glelwjgl;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class matrix
{
	public static final String VERSION = new String("$Id: matrix.java,v 1.2 1998/05/05 23:31:09 descarte Exp descarte $");
	
	private static final double[][] ROTX_CS(double cosine, double sine)
	{
		double[][] m = new double[4][4];
		m[0][0] = 1.0;
		m[0][1] = 0.0;
		m[0][2] = 0.0;
		m[0][3] = 0.0;
		m[1][0] = 0.0;
		m[1][1] = cosine;
		m[1][2] = sine;
		m[1][3] = 0.0;
		m[2][0] = 0.0;
		m[2][1] = -sine;
		m[2][2] = cosine;
		m[2][3] = 0.0;
		m[3][0] = 0.0;
		m[3][1] = 0.0;
		m[3][2] = 0.0;
		m[3][3] = 1.0;
		return m;
	}
	
	private static final double[][] ROTY_CS(double cosine, double sine)
	{
		double[][] m = new double[4][4];
		m[0][0] = cosine;
		m[0][1] = 0.0;
		m[0][2] = -sine;
		m[0][3] = 0.0;
		m[1][0] = 0.0;
		m[1][1] = 1.0;
		m[1][2] = 0.0;
		m[1][3] = 0.0;
		m[2][0] = sine;
		m[2][1] = 0.0;
		m[2][2] = cosine;
		m[2][3] = 0.0;
		m[3][0] = 0.0;
		m[3][1] = 0.0;
		m[3][2] = 0.0;
		m[3][3] = 1.0;
		return m;
	}
	
	private static final double[][] ROTZ_CS(double cosine, double sine)
	{
		double[][] m = new double[4][4];
		m[0][0] = cosine;
		m[0][1] = sine;
		m[0][2] = 0.0;
		m[0][3] = 0.0;
		m[1][0] = -sine;
		m[1][1] = cosine;
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
		DoubleBuffer mbuffer = BufferUtils.createDoubleBuffer((int) 16);
		mbuffer.put(new double[] { m[0][0], m[0][1], m[0][2], m[0][3], m[1][0], m[1][1], m[1][2], m[1][3], m[2][0], m[2][1], m[2][2], m[2][3], m[3][0], m[3][1], m[3][2], m[3][3] });
		mbuffer.flip();
		return mbuffer;
	}
	
	public static final double[][] urotx_cs_d(double cosine, double sine)
	{
		return matrix.ROTX_CS(cosine, sine);
	}
	
	public static final void rotx_cs_d(double cosine, double sine)
	{
		GL11.glMultMatrix((DoubleBuffer) matrix.getBufferedMatrix(matrix.urotx_cs_d(cosine, sine)));
	}
	
	public static final double[][] uroty_cs_d(double cosine, double sine)
	{
		return matrix.ROTX_CS(cosine, sine);
	}
	
	public static final void roty_cs_d(double cosine, double sine)
	{
		GL11.glMultMatrix((DoubleBuffer) matrix.getBufferedMatrix(matrix.uroty_cs_d(cosine, sine)));
	}
	
	public static final double[][] urotz_cs_d(double cosine, double sine)
	{
		return matrix.ROTX_CS(cosine, sine);
	}
	
	public static final void rotz_cs_d(double cosine, double sine)
	{
		GL11.glMultMatrix((DoubleBuffer) matrix.getBufferedMatrix(matrix.urotz_cs_d(cosine, sine)));
	}
	
	public static final double[][] urot_cs_d(double cosine, double sine, char axis)
	{
		switch(axis)
		{
		case 'X':
		case 'x':
		{
			return matrix.urotx_cs_d(cosine, sine);
		}
		case 'Y':
		case 'y':
		{
			return matrix.uroty_cs_d(cosine, sine);
		}
		case 'Z':
		case 'z':
		{
			return matrix.urotz_cs_d(cosine, sine);
		}
		}
		return null;
	}
	
	public static final void rot_cs_d(double cosine, double sine, char axis)
	{
		GL11.glMultMatrix((DoubleBuffer) matrix.getBufferedMatrix(matrix.urot_cs_d(cosine, sine, axis)));
	}
	
	public static final double[][] urot_prince_d(double theta, char axis)
	{
		return matrix.urot_cs_d(Math.cos(theta), Math.sin(theta), axis);
	}
	
	public static final void rot_prince_d(double theta, char axis)
	{
		GL11.glMultMatrix((DoubleBuffer) matrix.getBufferedMatrix(matrix.urot_prince_d(theta, axis)));
	}
	
	public static final void rot_axis_d(double omega, double[] axis)
	{
		GL11.glMultMatrix((DoubleBuffer) matrix.getBufferedMatrix(matrix.urot_axis_d(omega, axis)));
	}
	
	public static final void rot_about_axis_d(double angle, double[] axis)
	{
		GL11.glMultMatrix((DoubleBuffer) matrix.getBufferedMatrix(matrix.urot_about_axis_d(angle, axis)));
	}
	
	public static final void rot_omega_d(double[] axis)
	{
		GL11.glMultMatrix((DoubleBuffer) matrix.getBufferedMatrix(matrix.urot_omega_d(axis)));
	}
	
	public static final double[][] urot_axis_d(double omega, double[] axis)
	{
		double[][] m = new double[4][4];
		if(axis.length != 3)
			throw new GLEException("Length of axis parameter != 3. This is not a valid vector!");
		double tmp = omega / 2.0;
		double s = Math.sin(tmp);
		double c = Math.cos(tmp);
		double ssq = s * s;
		double csq = c * c;
		double d = csq - ssq;
		m[2][2] = d;
		m[1][1] = d;
		m[0][0] = d;
		double[] arrd = m[0];
		arrd[0] = arrd[0] + (ssq *= 2.0) * axis[0] * axis[0];
		double[] arrd2 = m[1];
		arrd2[1] = arrd2[1] + ssq * axis[1] * axis[1];
		double[] arrd3 = m[2];
		arrd3[2] = arrd3[2] + ssq * axis[2] * axis[2];
		double d2 = axis[0] * axis[1] * ssq;
		m[1][0] = d2;
		m[0][1] = d2;
		double d3 = axis[1] * axis[2] * ssq;
		m[2][1] = d3;
		m[1][2] = d3;
		double d4 = axis[2] * axis[0] * ssq;
		m[0][2] = d4;
		m[2][0] = d4;
		double cts = 2.0 * c * s;
		tmp = cts * axis[2];
		double[] arrd4 = m[0];
		arrd4[1] = arrd4[1] + tmp;
		double[] arrd5 = m[1];
		arrd5[0] = arrd5[0] - tmp;
		tmp = cts * axis[0];
		double[] arrd6 = m[1];
		arrd6[2] = arrd6[2] + tmp;
		double[] arrd7 = m[2];
		arrd7[1] = arrd7[1] - tmp;
		tmp = cts * axis[1];
		double[] arrd8 = m[2];
		arrd8[0] = arrd8[0] + tmp;
		double[] arrd9 = m[0];
		arrd9[2] = arrd9[2] - tmp;
		m[3][0] = 0.0;
		m[3][1] = 0.0;
		m[3][2] = 0.0;
		m[2][3] = 0.0;
		m[1][3] = 0.0;
		m[0][3] = 0.0;
		m[3][3] = 1.0;
		return m;
	}
	
	public static final double[][] urot_about_axis_d(double angle, double[] axis)
	{
		double[][] m = null;
		double[] ax = new double[3];
		double ang = angle;
		if(axis.length != 3)
			throw new GLEException("Length of axis parameter != 3. This is not a valid vector!");
		ang *= 0.017453292519943295;
		double len = axis[0] * axis[0] + axis[1] * axis[1] + axis[2] * axis[2];
		if(len != 1.0)
		{
			len = 1.0 / Math.sqrt(len);
			ax[0] = axis[0] * len;
			ax[1] = axis[1] * len;
			ax[2] = axis[2] * len;
			m = matrix.urot_axis_d(ang, ax);
		} else
			m = matrix.urot_axis_d(ang, axis);
		return m;
	}
	
	public static final double[][] urot_omega_d(double[] axis)
	{
		double[][] m = null;
		double[] ax = new double[3];
		double len = axis[0] * axis[0] + axis[1] * axis[1] + axis[2] * axis[2];
		len = 1.0 / Math.sqrt(len);
		ax[0] = axis[0] * len;
		ax[1] = axis[1] * len;
		ax[2] = axis[2] * len;
		return matrix.urot_axis_d(len, ax);
	}
	
	public static final double[] VEC_ZERO()
	{
		double[] vtmp = new double[3];
		vtmp[2] = 0.0;
		vtmp[1] = 0.0;
		vtmp[0] = 0.0;
		return vtmp;
	}
	
	public static final double[] VEC_NORMALIZE(double[] v)
	{
		double[] vtmp = new double[3];
		double vlen = matrix.VEC_LENGTH(v);
		if(vlen != 0.0)
		{
			vlen = 1.0 / vlen;
			vtmp[0] = v[0] * vlen;
			vtmp[1] = v[1] * vlen;
			vtmp[2] = v[2] * vlen;
		}
		return vtmp;
	}
	
	public static final double[] VEC_REFLECT(double[] v, double[] n)
	{
		double[] vtmp = new double[3];
		double dot = matrix.VEC_DOT_PRODUCT(v, n);
		vtmp[0] = v[0] - 2.0 * dot * n[0];
		vtmp[1] = v[1] - 2.0 * dot * n[1];
		vtmp[2] = v[2] - 2.0 * dot * n[2];
		return vtmp;
	}
	
	public static final double[] VEC_COPY_2(double[] v)
	{
		double[] vtmp = new double[3];
		vtmp[0] = v[0];
		vtmp[1] = v[1];
		return vtmp;
	}
	
	public static final double[] VEC_COPY(double[] v)
	{
		double[] vtmp = new double[] { v[0], v[1], v[2] };
		return vtmp;
	}
	
	public static final double VEC_LENGTH_2(double[] v)
	{
		double length = v[0] * v[0] + v[1] * v[1];
		return length;
	}
	
	public static final double VEC_LENGTH(double[] v)
	{
		double length = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
		return length;
	}
	
	public static final double[] VEC_SCALE(double scale, double[] v)
	{
		double[] vtmp = new double[] { scale * v[0], scale * v[1], scale * v[2] };
		return vtmp;
	}
	
	public static final double[] VEC_CROSS_PRODUCT(double[] v1, double[] v2)
	{
		double[] vtmp = new double[] { v1[1] * v2[2] - v1[2] * v2[1], v1[2] * v2[0] - v1[0] * v2[2], v1[0] * v2[1] - v1[1] * v2[0] };
		return vtmp;
	}
	
	public static final double VEC_DOT_PRODUCT(double[] v1, double[] v2)
	{
		double dot = 0.0;
		if(v1.length != 3 || v2.length != 3)
			throw new GLEException("Length of v1 or v2 != 3. Invalid vectors!");
		dot = v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
		return dot;
	}
	
	public static final double[] VEC_PERP(double[] v, double[] n)
	{
		double[] vtmp = new double[3];
		double dot = matrix.VEC_DOT_PRODUCT(v, n);
		if(v.length != 3 || n.length != 3)
			throw new GLEException("Length of v or n !=3. Invalid vectors!");
		vtmp[0] = v[0] - dot * n[0];
		vtmp[1] = v[1] - dot * n[1];
		vtmp[2] = v[2] - dot * n[2];
		return vtmp;
	}
	
	public static final double[] VEC_DIFF(double[] v2, double[] v1)
	{
		double[] vtmp = new double[3];
		if(v1.length != 3 || v2.length != 3)
			throw new GLEException("Length of v1 or v2 != 3. Invalid vectors!");
		vtmp[0] = v2[0] - v1[0];
		vtmp[1] = v2[1] - v1[1];
		vtmp[2] = v2[2] - v1[2];
		return vtmp;
	}
	
	public static final double[] VEC_SUM(double[] v1, double[] v2)
	{
		double[] vtmp = new double[3];
		if(v1.length != 3 || v2.length != 3)
			throw new GLEException("Length of v1 or v2 != 3. Invalid vectors!");
		vtmp[0] = v2[0] + v1[0];
		vtmp[1] = v2[1] + v1[1];
		vtmp[2] = v2[2] + v1[2];
		return vtmp;
	}
	
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
	
	public static final double[][] COPY_MATRIX_2X2(double[][] a)
	{
		double[][] b = new double[2][2];
		b[0][0] = a[0][0];
		b[0][1] = a[0][1];
		b[1][0] = a[1][0];
		b[1][1] = a[1][1];
		return b;
	}
	
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
	
	public static final double[][] MATRIX_PRODUCT_2X2(double[][] a, double[][] b)
	{
		double[][] c = new double[2][2];
		c[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0];
		c[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1];
		c[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0];
		c[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1];
		return c;
	}
	
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
	
	public static final double[] MAT_DOT_VEC_2X3(double[][] m, double[] v)
	{
		double[] vtmp = new double[] { m[0][0] * v[0] + m[0][1] * v[1] + m[0][2], m[1][0] * v[0] + m[1][1] * v[1] + m[1][2], 0.0 };
		return vtmp;
	}
	
	public static final double[] MAT_DOT_VEC_3X3(double[][] m, double[] v)
	{
		double[] vtmp = new double[] { m[0][0] * v[0] + m[0][1] * v[1] + m[0][2] * v[2], m[1][0] * v[0] + m[1][1] * v[1] + m[1][2] * v[2], m[2][0] * v[0] + m[2][1] * v[1] + m[2][2] * v[2] };
		return vtmp;
	}
	
	public static final double[] NORM_XFORM_2X2(double[][] m, double[] v)
	{
		double len = 0.0;
		double[] p = new double[3];
		if(m[0][1] != 0.0 || m[1][0] != 0.0 || m[0][0] != m[1][1])
		{
			p[0] = m[1][1] * v[0] - m[1][0] * v[1];
			p[1] = (-m[0][1]) * v[0] + m[0][0] * v[1];
			len = p[0] * p[0] + p[1] * p[1];
			len = 1.0 / Math.sqrt(len);
			double[] arrd = p;
			arrd[0] = arrd[0] * len;
			double[] arrd2 = p;
			arrd2[1] = arrd2[1] * len;
		} else
			p = matrix.VEC_COPY_2(v);
		return p;
	}
	
	public static final double[][] uview_direction_d(double[] v21, double[] up)
	{
		double sine;
		double[][] amat = null;
		double[][] bmat = null;
		double[][] cmat = null;
		double[] v_hat_21 = new double[3];
		double[] v_xy = new double[3];
		double[] up_proj = new double[3];
		double[] tmp = new double[3];
		double[][] m = null;
		v_hat_21 = matrix.VEC_COPY(v21);
		double len = matrix.VEC_LENGTH(v_hat_21);
		if(len != 0.0)
		{
			len = 1.0 / len;
			v_hat_21 = matrix.VEC_SCALE(len, v_hat_21);
			sine = Math.sqrt(1.0 - v_hat_21[2] * v_hat_21[2]);
			amat = matrix.ROTY_CS(-v_hat_21[2], -sine);
		} else
			amat = matrix.IDENTIFY_MATRIX_4X4();
		v_xy[0] = v21[0];
		v_xy[1] = v21[1];
		v_xy[2] = 0.0;
		len = matrix.VEC_LENGTH(v_xy);
		if(len != 0.0)
		{
			len = 1.0 / len;
			v_xy = matrix.VEC_SCALE(len, v_xy);
			bmat = matrix.ROTZ_CS(v_xy[0], v_xy[1]);
			cmat = matrix.MATRIX_PRODUCT_4X4(amat, bmat);
		} else
			cmat = matrix.COPY_MATRIX_4X4(amat);
		up_proj = matrix.VEC_PERP(up, v_hat_21);
		len = matrix.VEC_LENGTH(up_proj);
		if(len != 0.0)
		{
			len = 1.0 / len;
			up_proj = matrix.VEC_SCALE(len, up_proj);
			tmp[0] = cmat[1][0];
			tmp[1] = cmat[1][1];
			tmp[2] = cmat[1][2];
			double cosine = matrix.VEC_DOT_PRODUCT(tmp, up_proj);
			tmp[0] = cmat[0][0];
			tmp[1] = cmat[0][1];
			tmp[2] = cmat[0][2];
			sine = matrix.VEC_DOT_PRODUCT(tmp, up_proj);
			amat = matrix.ROTZ_CS(cosine, -sine);
			m = matrix.MATRIX_PRODUCT_4X4(amat, cmat);
		} else
			m = matrix.COPY_MATRIX_4X4(cmat);
		return m;
	}
	
	public static final double[][] uviewpoint_d(double[] v1, double[] v2, double[] up)
	{
		double[] v_hat_21 = null;
		double[][] trans_mat = null;
		double[][] rot_mat = null;
		double[][] m = null;
		v_hat_21 = matrix.VEC_DIFF(v2, v1);
		rot_mat = matrix.uview_direction_d(v_hat_21, up);
		trans_mat = matrix.IDENTIFY_MATRIX_4X4();
		trans_mat[3][0] = v1[0];
		trans_mat[3][1] = v1[1];
		trans_mat[3][2] = v1[2];
		m = matrix.MATRIX_PRODUCT_4X4(rot_mat, trans_mat);
		return m;
	}
}