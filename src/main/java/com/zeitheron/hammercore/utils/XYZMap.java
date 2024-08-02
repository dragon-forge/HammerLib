package com.zeitheron.hammercore.utils;

import net.minecraft.util.math.BlockPos;

import java.util.*;

public class XYZMap<T>
{
	public final Map<BlockPos, T> VALUES;
	
	public XYZMap(Map<BlockPos, T> values)
	{
		this.VALUES = values;
	}
	
	public XYZMap()
	{
		this(new HashMap<>());
	}
	
	public T getOnPos(int x, int y, int z)
	{
		return getOnPos(new BlockPos(x, y, z));
	}
	
	public T getOnPos(BlockPos pos)
	{
		return VALUES.get(pos.toImmutable());
	}
	
	public T setOnPos(int x, int y, int z, T v)
	{
		return setOnPos(new BlockPos(x, y, z), v);
	}
	
	public T setOnPos(BlockPos pos, T v)
	{
		return VALUES.put(pos.toImmutable(), v);
	}
	
	public BlockPos[] toKeyArray()
	{
		while(true)
		{
			try
			{
				return VALUES.keySet().toArray(new BlockPos[0]);
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
		}
	}
	
	public void clear()
	{
		VALUES.clear();
	}
}