package com.pengu.hammercore.vec;

public class Mat4
{
	float[] mat;
	
	public Mat4()
	{
		this.loadIdentity();
	}
	
	public Mat4 loadIdentity()
	{
		this.mat = new float[16];
		this.mat[15] = 1.0f;
		this.mat[10] = 1.0f;
		this.mat[5] = 1.0f;
		this.mat[0] = 1.0f;
		return this;
	}
	
	public Vector3 translate(Vector3 vec)
	{
		float x = (float) (vec.x * this.mat[0] + vec.y * this.mat[1] + vec.z * this.mat[2] + this.mat[3]);
		float y = (float) (vec.x * this.mat[4] + vec.y * this.mat[5] + vec.z * this.mat[6] + this.mat[7]);
		float z = (float) (vec.x * this.mat[8] + vec.y * this.mat[9] + vec.z * this.mat[10] + this.mat[11]);
		vec.x = x;
		vec.y = y;
		vec.z = z;
		return vec;
	}
	
	public static Mat4 rotationMat(double angle, Vector3 axis)
	{
		axis = axis.copy().normalize();
		float x = (float) axis.x;
		float y = (float) axis.y;
		float z = (float) axis.z;
		float cos = (float) Math.cos(angle *= 0.0174532925);
		float ocos = 1.0f - cos;
		float sin = (float) Math.sin(angle);
		Mat4 rotmat = new Mat4();
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