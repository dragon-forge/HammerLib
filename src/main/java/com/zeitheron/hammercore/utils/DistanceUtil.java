package com.zeitheron.hammercore.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Has some utility for calculating distances, fastest face to go to, etc.
 */
public class DistanceUtil
{
	/**
	 * Gets THE fastest way that you should go from point A to point B
	 */
	public static EnumFacing getFastestWay(BlockPos from, BlockPos to)
	{
		EnumFacing rec = EnumFacing.DOWN;
		double prevDist = Double.POSITIVE_INFINITY;
		
		for(EnumFacing f : EnumFacing.VALUES)
		{
			double currDist = from.offset(f).getDistance(to.getX(), to.getY(), to.getZ());
			
			if(currDist <= prevDist)
			{
				prevDist = currDist;
				rec = f;
			}
		}
		
		return rec.getOpposite();
	}
	
	private static final Set<EnumFacing> used = new HashSet<EnumFacing>();
	private static final Map<EnumFacing, Double> facesMapped = new HashMap<EnumFacing, Double>();
	
	/**
	 * Gets a {@link EnumFacing}[6] array containing sorted ways from fastest
	 * to, well, never
	 */
	public static EnumFacing[] sortByDistance(BlockPos from, BlockPos to)
	{
		EnumFacing[] map = new EnumFacing[6];
		
		EnumFacing fast = getFastestWay(from, to);
		map[0] = fast;
		
		if(fast == EnumFacing.EAST)
		{
			map[1] = EnumFacing.UP;
			map[2] = EnumFacing.DOWN;
			map[3] = EnumFacing.SOUTH;
			map[4] = EnumFacing.NORTH;
			map[5] = fast.getOpposite();
		}
		
		if(fast == EnumFacing.WEST)
		{
			map[1] = EnumFacing.UP;
			map[2] = EnumFacing.DOWN;
			map[3] = EnumFacing.SOUTH;
			map[4] = EnumFacing.NORTH;
			map[5] = fast.getOpposite();
		}
		
		if(fast == EnumFacing.SOUTH)
		{
			map[1] = EnumFacing.UP;
			map[2] = EnumFacing.DOWN;
			map[3] = EnumFacing.EAST;
			map[4] = EnumFacing.WEST;
			map[5] = fast.getOpposite();
		}
		
		if(fast == EnumFacing.NORTH)
		{
			map[1] = EnumFacing.UP;
			map[2] = EnumFacing.DOWN;
			map[3] = EnumFacing.EAST;
			map[4] = EnumFacing.WEST;
			map[5] = fast.getOpposite();
		}
		
		if(fast == EnumFacing.UP)
		{
			map[1] = EnumFacing.SOUTH;
			map[2] = EnumFacing.NORTH;
			map[3] = EnumFacing.EAST;
			map[4] = EnumFacing.WEST;
			map[5] = fast.getOpposite();
		}
		
		if(fast == EnumFacing.DOWN)
		{
			map[1] = EnumFacing.SOUTH;
			map[2] = EnumFacing.NORTH;
			map[3] = EnumFacing.EAST;
			map[4] = EnumFacing.WEST;
			map[5] = fast.getOpposite();
		}
		
		return map;
	}
}