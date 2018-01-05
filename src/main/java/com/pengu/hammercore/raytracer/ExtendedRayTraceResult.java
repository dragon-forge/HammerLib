package com.pengu.hammercore.raytracer;

import com.pengu.hammercore.vec.Vector3;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class ExtendedRayTraceResult extends RayTraceResult implements Comparable<ExtendedRayTraceResult>
{
	/**
	 * The square distance from the start of the raytrace.
	 */
	public double dist;
	
	public ExtendedRayTraceResult(Entity entity, Vector3 hit, Object data, double dist)
	{
		super(entity, hit.vec3());
		setData(data);
		this.dist = dist;
	}
	
	public ExtendedRayTraceResult(Vector3 hit, int side, BlockPos pos, Object data, double dist)
	{
		super(hit.vec3(), EnumFacing.values()[side], pos);
		setData(data);
		this.dist = dist;
	}
	
	public ExtendedRayTraceResult(Vector3 hit, EnumFacing side, BlockPos pos, Object data, double dist)
	{
		super(hit.vec3(), side, pos);
		setData(data);
		this.dist = dist;
	}
	
	public void setData(Object data)
	{
		if(data instanceof Integer)
			subHit = (Integer) data;
		hitInfo = data;
	}
	
	@Override
	public int compareTo(ExtendedRayTraceResult o)
	{
		return dist == o.dist ? 0 : dist < o.dist ? -1 : 1;
	}
}