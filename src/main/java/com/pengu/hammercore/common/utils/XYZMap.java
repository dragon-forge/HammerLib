package com.pengu.hammercore.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.math.BlockPos;

public class XYZMap<T>
{
	public final Map<Long, T> VALUES = new HashMap<Long, T>();
	
	public T getOnPos(int x, int y, int z)
	{
		return VALUES.get(new BlockPos(x, y, z).toLong());
	}
	
	public T getOnPos(BlockPos pos)
	{
		return getOnPos(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public T setOnPos(int x, int y, int z, T v)
	{
		return VALUES.put(new BlockPos(x, y, z).toLong(), v);
	}
	
	public void setOnPos(BlockPos pos, T v)
	{
		setOnPos(pos.getX(), pos.getY(), pos.getZ(), v);
	}
	
	public BlockPos[] toKeyArray()
	{
		while(true)
		{
			try
			{
				List<BlockPos> decoded = new ArrayList<BlockPos>();
				for(Long l : VALUES.keySet())
					decoded.add(BlockPos.fromLong(l));
				return decoded.toArray(new BlockPos[0]);
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
		}
	}
}