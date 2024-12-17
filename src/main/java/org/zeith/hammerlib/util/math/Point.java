package org.zeith.hammerlib.util.math;

import org.zeith.hammerlib.util.java.tuples.Tuple2;

import java.util.stream.Stream;

public class Point
		extends Tuple2<Float, Float>
{
	public static final Point ZERO = new Point(0, 0);
	
	public Point(float x, float y)
	{
		super(x, y);
	}
	
	public Point(double x, double y)
	{
		this((float) x, (float) y);
	}
	
	public Point offset(float x, float y)
	{
		return new Point(x() + x, y() + y);
	}
	
	public float x()
	{
		return a;
	}
	
	public float y()
	{
		return b;
	}
	
	public void setX(float x)
	{
		this.a = x;
	}
	
	public void setY(float y)
	{
		this.b = y;
	}
	
	@Override
	public Stream<Float> stream()
	{
		return Stream.of(a, b);
	}
}