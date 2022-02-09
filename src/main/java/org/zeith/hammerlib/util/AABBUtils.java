package org.zeith.hammerlib.util;

import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class AABBUtils
{
	public static boolean almostEqual(double a, double b)
	{
		return a - b > -1.0E-4 && a - b < 1.0E-4;
	}

	public static Vec3 randomPosWithin(AABB aabb, Random rng)
	{
		double x = aabb.minX + (aabb.maxX - aabb.minX) * rng.nextDouble();
		double y = aabb.minY + (aabb.maxY - aabb.minY) * rng.nextDouble();
		double z = aabb.minZ + (aabb.maxZ - aabb.minZ) * rng.nextDouble();
		return new Vec3(x, y, z);
	}

	public static AABB rotate(AABB aabb, Rotation rotationIn)
	{
		switch(rotationIn)
		{
			case NONE:
			default:
				return aabb;
			case CLOCKWISE_90:
				return new AABB(-aabb.minX, aabb.minY, aabb.minZ, -aabb.maxX, aabb.maxY, aabb.maxZ);
			case CLOCKWISE_180:
				return new AABB(-aabb.minX, aabb.minY, -aabb.minZ, -aabb.maxX, aabb.maxY, -aabb.maxZ);
			case COUNTERCLOCKWISE_90:
				return new AABB(aabb.minX, aabb.minY, -aabb.minZ, aabb.maxX, aabb.maxY, -aabb.maxZ);
		}
	}

	public static AABB normalize(AABB aabb)
	{
		return aabb.move(-aabb.minX, -aabb.minY, -aabb.minZ);
	}

	public static AABB partial(AABB prev, AABB cur, float partialTicks)
	{
		double x1 = prev.minX + (cur.minX - prev.minX) * partialTicks;
		double y1 = prev.minY + (cur.minY - prev.minY) * partialTicks;
		double z1 = prev.minZ + (cur.minZ - prev.minZ) * partialTicks;

		double x2 = prev.maxX + (cur.maxX - prev.maxX) * partialTicks;
		double y2 = prev.maxY + (cur.maxY - prev.maxY) * partialTicks;
		double z2 = prev.maxZ + (cur.maxZ - prev.maxZ) * partialTicks;

		return new AABB(x1, y1, z1, x2, y2, z2);
	}

	public static AABB extrudeGravity(AABB aabb, float ySpeed)
	{
		float ay = Math.abs(ySpeed);
		return aabb.inflate(-ay * (aabb.maxX - aabb.minX) / 2, ay * (aabb.maxY - aabb.minY) / 2, -ay * (aabb.maxZ - aabb.minZ) / 2).move(0, -ySpeed * (aabb.maxY - aabb.minY) / 2, 0);
	}

	public static void cut(AABB box, AABB knife, List<AABB> intoList)
	{
		if(!box.intersects(knife))
		{
			intoList.add(box);
			return;
		}

		AABB inter = box.intersect(knife);

		// When cutting does not happen on lowest Y of the box, add floor
		if(!almostEqual(box.minY, inter.minY))
			intoList.add(new AABB(box.minX, box.minY, box.minZ, box.maxX, inter.minY, box.maxZ));

		// When cutting does not happen on highest Y of the box, add ceiling
		if(!almostEqual(box.maxY, inter.maxY))
			intoList.add(new AABB(box.minX, inter.maxY, box.minZ, box.maxX, box.maxY, box.maxZ));

		// Positive X and Z boxes
		{
			boolean ea1, ea2;

			if(ea1 = !almostEqual(box.maxX, inter.maxX))
			{
				intoList.add(new AABB(inter.maxX, inter.minY, box.minZ, box.maxX, inter.maxY, inter.maxZ));
			}

			if(ea2 = !almostEqual(box.maxZ, inter.maxZ))
			{
				intoList.add(new AABB(box.minX, inter.minY, inter.maxZ, inter.maxX, inter.maxY, box.maxZ));
			}

			if(ea1 && ea2)
				intoList.add(new AABB(inter.maxX, inter.minY, inter.maxZ, box.maxX, inter.maxY, box.maxZ));
		}

		// Negative X and Z boxes
		{
			boolean ea1, ea2;

			if(ea1 = !almostEqual(box.minZ, inter.minZ))
			{
				intoList.add(new AABB(inter.minX, inter.minY, box.minZ, inter.maxX, inter.maxY, inter.minZ));
			}

			if(ea2 = !almostEqual(box.minX, inter.minX))
			{
				intoList.add(new AABB(box.minX, inter.minY, inter.minZ, inter.minX, inter.maxY, inter.maxZ));
			}

			if(ea1 || ea2)
				intoList.add(new AABB(box.minX, inter.minY, box.minZ, inter.minX, inter.maxY, inter.minZ));
		}
	}

	public static Vec3 getCenter(AABB aabb)
	{
		return new Vec3(aabb.minX + (aabb.maxX - aabb.minX) * 0.5D, aabb.minY + (aabb.maxY - aabb.minY) * 0.5D, aabb.minZ + (aabb.maxZ - aabb.minZ) * 0.5D);
	}
}