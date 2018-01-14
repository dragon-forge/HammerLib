package com.pengu.hammercore.client.particle.def.lightbolt;

public class LBMat4
{
	float[] mat;
	
	public LBMat4()
	{
		this.loadIdentity();
	}
	
	public LBMat4 loadIdentity()
	{
		this.mat = new float[16];
		final float[] mat = this.mat;
		final int n = 0;
		final float[] mat2 = this.mat;
		final int n2 = 5;
		final float[] mat3 = this.mat;
		final int n3 = 10;
		final float[] mat4 = this.mat;
		final int n4 = 15;
		final float n5 = 1.0f;
		mat3[n3] = (mat4[n4] = n5);
		mat[n] = (mat2[n2] = n5);
		return this;
	}
	
	public LBVector3 translate(final LBVector3 vec)
	{
		final float x = vec.x * this.mat[0] + vec.y * this.mat[1] + vec.z * this.mat[2] + this.mat[3];
		final float y = vec.x * this.mat[4] + vec.y * this.mat[5] + vec.z * this.mat[6] + this.mat[7];
		final float z = vec.x * this.mat[8] + vec.y * this.mat[9] + vec.z * this.mat[10] + this.mat[11];
		vec.x = x;
		vec.y = y;
		vec.z = z;
		return vec;
	}
	
	public static LBMat4 rotationMat(double angle, LBVector3 axis)
	{
		axis = axis.copy().normalize();
		final float x = axis.x;
		final float y = axis.y;
		final float z = axis.z;
		angle *= 0.0174532925;
		final float cos = (float) Math.cos(angle);
		final float ocos = 1.0f - cos;
		final float sin = (float) Math.sin(angle);
		final LBMat4 rotmat = new LBMat4();
		rotmat.mat[0] = x * x * ocos + cos;
		rotmat.mat[1] = y * x * ocos + z * sin;
		rotmat.mat[2] = x * z * ocos - y * sin;
		rotmat.mat[4] = x * y * ocos - z * sin;
		rotmat.mat[5] = y * y * ocos + cos;
		rotmat.mat[6] = y * z * ocos + x * sin;
		rotmat.mat[8] = x * z * ocos + y * sin;
		rotmat.mat[9] = y * z * ocos - x * sin;
		rotmat.mat[10] = z * z * ocos + cos;
		rotmat.mat[15] = 1.0f;
		return rotmat;
	}
}