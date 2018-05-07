package com.endie.lib.utils;

import javax.annotation.Nullable;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Vec2D
{
	public static final Vec2D ZERO = new Vec2D(0.0D, 0.0D);
	
	/** X coordinate of Vec2D */
	public final double x;
	/** Y coordinate of Vec2D */
	public final double y;
	
	public Vec2D(double xIn, double yIn)
	{
		if(xIn == -0.0D)
			xIn = 0.0D;
		if(yIn == -0.0D)
			yIn = 0.0D;
		this.x = xIn;
		this.y = yIn;
	}
	
	/**
	 * Returns a new vector with the result of the specified vector minus this.
	 */
	public Vec2D subtractReverse(Vec2D vec)
	{
		return new Vec2D(vec.x - this.x, vec.y - this.y);
	}
	
	/**
	 * Normalizes the vector to a length of 1 (except if it is the zero vector)
	 */
	public Vec2D normalize()
	{
		double d0 = (double) MathHelper.sqrt(this.x * this.x + this.y * this.y);
		return d0 < 1.0E-4D ? ZERO : new Vec2D(this.x / d0, this.y / d0);
	}
	
	public double dotProduct(Vec2D vec)
	{
		return this.x * vec.x + this.y * vec.y;
	}
	
	public Vec2D subtract(Vec2D vec)
	{
		return subtract(vec.x, vec.y);
	}
	
	public Vec2D subtract(double x, double y)
	{
		return addVector(-x, -y);
	}
	
	public Vec2D add(Vec2D vec)
	{
		return addVector(vec.x, vec.y);
	}
	
	/**
	 * Adds the specified x,y,z vector components to this vector and returns the
	 * resulting vector. Does not change this vector.
	 */
	public Vec2D addVector(double x, double y)
	{
		return new Vec2D(this.x + x, this.y + y);
	}
	
	/**
	 * Euclidean distance between this and the specified vector, returned as
	 * double.
	 */
	public double distanceTo(Vec2D vec)
	{
		double d0 = vec.x - this.x;
		double d1 = vec.y - this.y;
		return (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
	}
	
	/**
	 * Euclidean distance between this and the specified vector, returned as
	 * double.
	 */
	public double distanceTo(double x, double y)
	{
		double d0 = x - this.x;
		double d1 = y - this.y;
		return (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
	}
	
	/**
	 * The square of the Euclidean distance between this and the specified
	 * vector.
	 */
	public double squareDistanceTo(Vec2D vec)
	{
		double d0 = vec.x - this.x;
		double d1 = vec.y - this.y;
		return d0 * d0 + d1 * d1;
	}
	
	public double squareDistanceTo(double xIn, double yIn)
	{
		double d0 = xIn - this.x;
		double d1 = yIn - this.y;
		return d0 * d0 + d1 * d1;
	}
	
	public Vec2D scale(double scale)
	{
		return new Vec2D(this.x * scale, this.y * scale);
	}
	
	/**
	 * Returns the length of the vector.
	 */
	public double lengthVector()
	{
		return (double) MathHelper.sqrt(lengthSquared());
	}
	
	public double lengthSquared()
	{
		return this.x * this.x + this.y * this.y;
	}
	
	/**
	 * Returns a new vector with x value equal to the second parameter, along
	 * the line between this vector and the passed in vector, or null if not
	 * possible.
	 */
	@Nullable
	public Vec2D getIntermediateWithXValue(Vec3d vec, double x)
	{
		double d0 = vec.x - this.x;
		double d1 = vec.y - this.y;
		
		if(d0 * d0 < 1.0000000116860974E-7D)
		{
			return null;
		} else
		{
			double d3 = (x - this.x) / d0;
			return d3 >= 0.0D && d3 <= 1.0D ? new Vec2D(this.x + d0 * d3, this.y + d1 * d3) : null;
		}
	}
	
	/**
	 * Returns a new vector with y value equal to the second parameter, along
	 * the line between this vector and the passed in vector, or null if not
	 * possible.
	 */
	@Nullable
	public Vec2D getIntermediateWithYValue(Vec3d vec, double y)
	{
		double d0 = vec.x - this.x;
		double d1 = vec.y - this.y;
		
		if(d1 * d1 < 1.0000000116860974E-7D)
		{
			return null;
		} else
		{
			double d3 = (y - this.y) / d1;
			return d3 >= 0.0D && d3 <= 1.0D ? new Vec2D(this.x + d0 * d3, this.y + d1 * d3) : null;
		}
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		} else if(!(obj instanceof Vec2D))
		{
			return false;
		} else
		{
			Vec2D vec3d = (Vec2D) obj;
			
			if(Double.compare(vec3d.x, this.x) != 0)
				return false;
			else
				return Double.compare(vec3d.y, this.y) == 0;
		}
	}
	
	public int hashCode()
	{
		long j = Double.doubleToLongBits(this.x);
		int i = (int) (j ^ j >>> 32);
		j = Double.doubleToLongBits(this.y);
		i = 31 * i + (int) (j ^ j >>> 32);
		return i;
	}
	
	public String toString()
	{
		return "(" + this.x + ", " + this.y + ")";
	}
}