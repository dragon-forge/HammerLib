package com.pengu.hammercore.common.utils;

import net.minecraft.util.math.BlockPos;

public class StrPos extends BlockPos
{
	public StrPos(int x, int y, int z)
	{
		super(x, y, z);
	}
	
	@Override
	public String toString()
	{
		return toStr(this);
	}
	
	public static String toStr(BlockPos pos)
	{
		return pos.getX() + "/" + pos.getY() + "/" + pos.getZ();
	}
	
	public static BlockPos fromStr(String str)
	{
		String[] pars = str.split("/");
		return new BlockPos(Integer.parseInt(pars[0]), Integer.parseInt(pars[1]), Integer.parseInt(pars[2]));
	}
}