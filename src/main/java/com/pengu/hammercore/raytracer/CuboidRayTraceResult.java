package com.pengu.hammercore.raytracer;

import com.pengu.hammercore.vec.Vector3;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CuboidRayTraceResult extends DistanceRayTraceResult
{
	public IndexedCuboid6 cuboid6;
	
	public CuboidRayTraceResult(Entity entity, Vector3 hit, IndexedCuboid6 cuboid, double dist)
	{
		super(entity, hit, cuboid.data, dist);
		this.cuboid6 = cuboid;
	}
	
	public CuboidRayTraceResult(Vector3 hit, BlockPos pos, EnumFacing side, IndexedCuboid6 cuboid, double dist)
	{
		super(hit, pos, side, cuboid.data, dist);
		this.cuboid6 = cuboid;
	}
	
	public CuboidRayTraceResult(Vector3 hit, EnumFacing side, IndexedCuboid6 cuboid6, double dist)
	{
		super(hit, side, cuboid6.data, dist);
		this.cuboid6 = cuboid6;
	}
	
	public DistanceRayTraceResult getAsTraceResult()
	{
		return new DistanceRayTraceResult(new Vector3(hitVec), sideHit, hitInfo, dist);
	}
	
	@Override
	public String toString()
	{
		return super.toString().replace("}", "") + ", cuboid=" + cuboid6.toString() + "}";
	}
}