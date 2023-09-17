package org.zeith.hammerlib.util;

import net.minecraft.util.*;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.*;

import java.util.*;
import java.util.function.Consumer;

public class AABBUtils
{
	public static boolean almostEqual(double a, double b)
	{
		return Math.abs(a - b) < 1.0E-4;
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

	public static AABB rotate(AABB aabb, Rotation rotationIn)
	{
		return switch(rotationIn)
		{
			default -> aabb;
			case CLOCKWISE_90 -> new AABB(-aabb.minX, aabb.minY, aabb.minZ, -aabb.maxX, aabb.maxY, aabb.maxZ);
			case CLOCKWISE_180 -> new AABB(-aabb.minX, aabb.minY, -aabb.minZ, -aabb.maxX, aabb.maxY, -aabb.maxZ);
			case COUNTERCLOCKWISE_90 -> new AABB(aabb.minX, aabb.minY, -aabb.minZ, aabb.maxX, aabb.maxY, -aabb.maxZ);
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
		if(!almostEqual(box.minY, inter.minY))
			intoList.accept(new AABB(box.minX, box.minY, box.minZ, box.maxX, inter.minY, box.maxZ));

		// When cutting does not happen on highest Y of the box, add ceiling
		if(!almostEqual(box.maxY, inter.maxY))
			intoList.accept(new AABB(box.minX, inter.maxY, box.minZ, box.maxX, box.maxY, box.maxZ));

		// Positive X and Z boxes
		{
			boolean ea1, ea2;

			if(ea1 = !almostEqual(box.maxX, inter.maxX))
			{
				intoList.accept(new AABB(inter.maxX, inter.minY, box.minZ, box.maxX, inter.maxY, inter.maxZ));
			}

			if(ea2 = !almostEqual(box.maxZ, inter.maxZ))
			{
				intoList.accept(new AABB(box.minX, inter.minY, inter.maxZ, inter.maxX, inter.maxY, box.maxZ));
			}

			if(ea1 && ea2)
				intoList.accept(new AABB(inter.maxX, inter.minY, inter.maxZ, box.maxX, inter.maxY, box.maxZ));
		}

		// Negative X and Z boxes
		{
			boolean ea1, ea2;

			if(ea1 = !almostEqual(box.minZ, inter.minZ))
			{
				intoList.accept(new AABB(inter.minX, inter.minY, box.minZ, inter.maxX, inter.maxY, inter.minZ));
			}

			if(ea2 = !almostEqual(box.minX, inter.minX))
			{
				intoList.accept(new AABB(box.minX, inter.minY, inter.minZ, inter.minX, inter.maxY, inter.maxZ));
			}

			if(ea1 || ea2)
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