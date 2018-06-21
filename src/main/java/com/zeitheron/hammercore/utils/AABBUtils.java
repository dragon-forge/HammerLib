package com.zeitheron.hammercore.utils;

import java.util.Random;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AABBUtils
{
	public static Vec3d randomPosWithin(AxisAlignedBB aabb, Random rng)
	{
		double x = aabb.minX + (aabb.maxX - aabb.minX) * rng.nextDouble();
		double y = aabb.minY + (aabb.maxY - aabb.minY) * rng.nextDouble();
		double z = aabb.minZ + (aabb.maxZ - aabb.minZ) * rng.nextDouble();
		return new Vec3d(x, y, z);
	}
	
	public static AxisAlignedBB rotate(AxisAlignedBB aabb, Rotation rotationIn)
	{
		switch(rotationIn)
		{
		case NONE:
		default:
			return aabb;
		case CLOCKWISE_90:
			return new AxisAlignedBB(-aabb.minX, aabb.minY, aabb.minZ, -aabb.maxX, aabb.maxY, aabb.maxZ);
		case CLOCKWISE_180:
			return new AxisAlignedBB(-aabb.minX, aabb.minY, -aabb.minZ, -aabb.maxX, aabb.maxY, -aabb.maxZ);
		case COUNTERCLOCKWISE_90:
			return new AxisAlignedBB(aabb.minX, aabb.minY, -aabb.minZ, aabb.maxX, aabb.maxY, -aabb.maxZ);
		}
	}
	
	public static AxisAlignedBB normalize(AxisAlignedBB aabb)
	{
		return aabb.offset(-aabb.minX, -aabb.minY, -aabb.minZ);
	}
}