package org.zeith.hammerlib.util.mcf.shapes;

import com.google.common.collect.*;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.zeith.hammerlib.util.java.TableHelper;

import java.util.function.BiFunction;

public class RotatedShapeCache<T>
{
	private final Table<Direction, T, VoxelShape> cache;
	private final BiFunction<Direction, T, VoxelShape> compute;
	
	public RotatedShapeCache(Table<Direction, T, VoxelShape> cache, BiFunction<Direction, T, VoxelShape> compute)
	{
		this.cache = cache;
		this.compute = compute;
	}
	
	public static <T> RotatedShapeCache<T> hashBased(BiFunction<Direction, T, VoxelShape> compute)
	{
		return new RotatedShapeCache<>(HashBasedTable.create(4, 12), compute);
	}
	
	public static <T> RotatedShapeCache<T> hashBasedSynchronized(BiFunction<Direction, T, VoxelShape> compute)
	{
		return new RotatedShapeCache<>(Tables.synchronizedTable(HashBasedTable.create(4, 12)), compute);
	}
	
	public void clear()
	{
		cache.clear();
	}
	
	public VoxelShape getShape(Direction rotation, T data)
	{
		return TableHelper.computeIfAbsent(cache, rotation, data, compute);
	}
}