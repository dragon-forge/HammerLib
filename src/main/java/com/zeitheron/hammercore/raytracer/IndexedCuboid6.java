package com.zeitheron.hammercore.raytracer;

import com.zeitheron.hammercore.utils.math.vec.Cuboid6;

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