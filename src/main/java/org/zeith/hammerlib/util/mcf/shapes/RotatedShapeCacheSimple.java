package org.zeith.hammerlib.util.mcf.shapes;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;
import java.util.function.Function;

public class RotatedShapeCacheSimple
{
	private final Map<Direction, VoxelShape> cache;
	private final Function<Direction, VoxelShape> compute;
	
	public RotatedShapeCacheSimple(Map<Direction, VoxelShape> cache, Function<Direction, VoxelShape> compute)
	{
		this.cache = cache;
		this.compute = compute;
	}
	
	public static RotatedShapeCacheSimple hashBased(Function<Direction, VoxelShape> compute)
	{
		return new RotatedShapeCacheSimple(new HashMap<>(4), compute);
	}
	
	public static RotatedShapeCacheSimple hashBasedSynchronized(Function<Direction, VoxelShape> compute)
	{
		return new RotatedShapeCacheSimple(Collections.synchronizedMap(new HashMap<>(4)), compute);
	}
	
	public void clear()
	{
		cache.clear();
	}
	
	public VoxelShape getShape(Direction rotation)
	{
		return cache.computeIfAbsent(rotation, compute);
	}
}