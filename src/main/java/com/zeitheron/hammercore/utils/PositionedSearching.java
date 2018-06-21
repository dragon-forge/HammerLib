package com.zeitheron.hammercore.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.util.math.BlockPos;

public class PositionedSearching<T>
{
	public List<T> located = new ArrayList<>();
	
	public Class<T> tileType;
	
	public Function<BlockPos, T> getter;
	public Predicate<T> valid;
	
	public PositionedSearching(Function<BlockPos, T> getter, Predicate<T> valid, Class<T> tileType)
	{
		this.getter = getter;
		this.valid = valid;
		this.tileType = tileType;
	}
	
	public BlockPos center;
	public int radiusX, radiusY, radiusZ;
	
	List<BlockPos> check = new ArrayList<>();
	BlockPos current;
	
	public void setRadius(int radius)
	{
		setRadius(radius, radius, radius);
	}
	
	public void setRadius(int radiusX, int radiusY, int radiusZ)
	{
		this.radiusX = radiusX;
		this.radiusY = radiusY;
		this.radiusZ = radiusZ;
		bakeCheckList();
	}
	
	public void bakeCheckList()
	{
		check.clear();
		if(center != null && radiusX > 0 && radiusY > 0 && radiusZ > 0)
			for(int x = -radiusX; x <= radiusX; ++x)
				for(int y = -radiusY; y <= radiusY; ++y)
					for(int z = -radiusZ; z <= radiusZ; ++z)
						check.add(center.add(x, y, z));
	}
	
	public void setCenter(BlockPos center)
	{
		this.center = center;
		bakeCheckList();
	}
	
	public void update(int searches)
	{
		located.removeIf(valid.negate());
		for(int i = 0; i < searches; ++i)
			continueSearch();
	}
	
	public void continueSearch()
	{
		if(check.isEmpty())
			return;
		int fo = check.indexOf(current);
		if(fo == -1)
			current = check.get(0);
		
		T i = getter.apply(current);
		if(i != null && !located.contains(i))
			located.add(i);
		
		current = check.get((fo + 1) % check.size());
	}
}