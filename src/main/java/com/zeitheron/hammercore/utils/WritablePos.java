package com.zeitheron.hammercore.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class WritablePos
		extends BlockPos
{
	public WritablePos(int x, int y, int z)
	{
		super(x, y, z);
	}

	public WritablePos(double x, double y, double z)
	{
		super(x, y, z);
	}

	public WritablePos(Entity source)
	{
		super(source);
	}

	public WritablePos(Vec3d vec)
	{
		super(vec);
	}

	public WritablePos(Vec3i source)
	{
		super(source);
	}

	public WritablePos(String str)
	{
		this(str.split(","));
	}

	WritablePos(String[] pars)
	{
		super(Integer.parseInt(pars[0]), Integer.parseInt(pars[1]), Integer.parseInt(pars[2]));
	}

	@Override
	public String toString()
	{
		return toStr(this);
	}

	public static String toStr(BlockPos pos)
	{
		return pos.getX() + "," + pos.getY() + "," + pos.getZ();
	}

	public static String toStr(Vec3i pos)
	{
		return pos.getX() + "," + pos.getY() + "," + pos.getZ();
	}

	public static BlockPos fromStr(String str)
	{
		return new WritablePos(str);
	}
}