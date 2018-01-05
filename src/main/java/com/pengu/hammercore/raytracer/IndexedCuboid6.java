package com.pengu.hammercore.raytracer;

import com.pengu.hammercore.vec.Cuboid6;

public class IndexedCuboid6 extends Cuboid6
{
	public Object data;
	
	public IndexedCuboid6(Object data, Cuboid6 cuboid)
	{
		super(cuboid);
		this.data = data;
	}
	
	@Override
	public IndexedCuboid6 copy()
	{
		return new IndexedCuboid6(data, this);
	}
}