package org.zeith.hammerlib.util;

import net.minecraft.core.Direction;
import net.minecraft.util.*;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.*;

import java.util.*;
import java.util.function.Consumer;

public class AABBUtils
{
	public static double EQUALITY_TOLERANCE = 1.0E-4;
	
	public static boolean notAlmostEqual(double a, double b)
	{
		return Math.abs(a - b) >= EQUALITY_TOLERANCE;
	}
	
	public static boolean almostEqual(double a, double b)
	{
		return Math.abs(a - b) < EQUALITY_TOLERANCE;
	}
	
	public static Vec3 randomPosWithin(AABB aabb, Random rng)
	{
		return new Vec3(
				Mth.lerp(rng.nextDouble(), aabb.minX, aabb.maxX),
				Mth.lerp(rng.nextDouble(), aabb.minY, aabb.maxY),
				Mth.lerp(rng.nextDouble(), aabb.minZ, aabb.maxZ)
		);
	}
	
	public static Vec3 randomPosWithin(AABB aabb, RandomSource rng)
	{
		return new Vec3(
				Mth.lerp(rng.nextDouble(), aabb.minX, aabb.maxX),
				Mth.lerp(rng.nextDouble(), aabb.minY, aabb.maxY),
				Mth.lerp(rng.nextDouble(), aabb.minZ, aabb.maxZ)
		);
	}
	
	private static final Rotation[] ROTATIONS = Rotation.values();
	
	public static AABB rotateNorthBox(AABB aabb, Direction rotationIn)
	{
		int d2d = rotationIn.get2DDataValue();
		if(d2d < 0)
			throw new IllegalStateException("Unable to get Y-rotated facing of " + rotationIn);
		return rotate(aabb, ROTATIONS[(d2d + 2) % 4]);
	}
	
	public static AABB rotate(AABB aabb, Rotation rotationIn)
	{
		return switch(rotationIn)
		{
			case CLOCKWISE_90 -> // east
					new AABB(1 - aabb.maxZ, aabb.minY, aabb.minX, 1 - aabb.minZ, aabb.maxY, aabb.maxX);
			case CLOCKWISE_180 -> // south
					new AABB(1 - aabb.maxX, aabb.minY, 1 - aabb.minZ, 1 - aabb.minX, aabb.maxY, 1 - aabb.maxZ);
			case COUNTERCLOCKWISE_90 -> // west
					new AABB(aabb.minZ, aabb.minY, 1 - aabb.minX, aabb.maxZ, aabb.maxY, 1 - aabb.maxX);
			default -> aabb; // north
		};
	}
	
	public static AABB normalize(AABB aabb)
	{
		return aabb.move(-aabb.minX, -aabb.minY, -aabb.minZ);
	}
	
	public static AABB lerp(AABB prev, AABB cur, float pv)
	{
		return new AABB(
				Mth.lerp(pv, prev.minX, cur.minX),
				Mth.lerp(pv, prev.minY, cur.minY),
				Mth.lerp(pv, prev.minZ, cur.minZ),
				Mth.lerp(pv, prev.maxX, cur.maxX),
				Mth.lerp(pv, prev.maxY, cur.maxY),
				Mth.lerp(pv, prev.maxZ, cur.maxZ)
		);
	}
	
	public static AABB extrudeGravity(AABB aabb, float ySpeed)
	{
		float ay = Math.abs(ySpeed);
		return aabb.inflate(
						-ay * (aabb.maxX - aabb.minX) / 2,
						ay * (aabb.maxY - aabb.minY) / 2,
						-ay * (aabb.maxZ - aabb.minZ) / 2
				)
				.move(0, -ySpeed * (aabb.maxY - aabb.minY) / 2, 0);
	}
	
	public static void cut(AABB box, AABB knife, List<AABB> intoList)
	{
		cut(box, knife, intoList::add);
	}
	
	public static void cut(AABB box, AABB knife, Consumer<AABB> intoList)
	{
		if(!box.intersects(knife))
		{
			intoList.accept(box);
			return;
		}
		
		AABB inter = box.intersect(knife);
		
		// When cutting does not happen on lowest Y of the box, add floor
		if(notAlmostEqual(box.minY, inter.minY))
			intoList.accept(new AABB(box.minX, box.minY, box.minZ, box.maxX, inter.minY, box.maxZ));
		
		// When cutting does not happen on highest Y of the box, add ceiling
		if(notAlmostEqual(box.maxY, inter.maxY))
			intoList.accept(new AABB(box.minX, inter.maxY, box.minZ, box.maxX, box.maxY, box.maxZ));
		
		// Positive X and Z boxes
		{
			boolean eaX = notAlmostEqual(box.maxX, inter.maxX), eaZ = notAlmostEqual(box.maxZ, inter.maxZ);
			if(eaX)
				intoList.accept(new AABB(inter.maxX, inter.minY, box.minZ, box.maxX, inter.maxY, inter.maxZ));
			if(eaZ)
				intoList.accept(new AABB(box.minX, inter.minY, inter.maxZ, inter.maxX, inter.maxY, box.maxZ));
			if(eaX && eaZ)
				intoList.accept(new AABB(inter.maxX, inter.minY, inter.maxZ, box.maxX, inter.maxY, box.maxZ));
		}
		
		// Negative X and Z boxes
		{
			boolean eaZ = notAlmostEqual(box.minZ, inter.minZ), eaX = notAlmostEqual(box.minX, inter.minX);
			if(eaZ)
				intoList.accept(new AABB(inter.minX, inter.minY, box.minZ, inter.maxX, inter.maxY, inter.minZ));
			if(eaX)
				intoList.accept(new AABB(box.minX, inter.minY, inter.minZ, inter.minX, inter.maxY, inter.maxZ));
			if(eaZ || eaX)
				intoList.accept(new AABB(box.minX, inter.minY, box.minZ, inter.minX, inter.maxY, inter.minZ));
		}
	}
	
	@Deprecated
	public static Vec3 getCenter(AABB aabb)
	{
//		return new Vec3(aabb.minX + (aabb.maxX - aabb.minX) * 0.5D, aabb.minY + (aabb.maxY - aabb.minY) * 0.5D, aabb.minZ + (aabb.maxZ - aabb.minZ) * 0.5D);
		return aabb.getCenter();
	}
}