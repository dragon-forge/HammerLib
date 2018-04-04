package com.endie.lib.fast.lists;

import java.util.List;

public interface IntList extends List<Integer>
{
	static IntList of(int... data)
	{
		IntList bl = new IntArrayList();
		for(int b : data)
			bl.addInt(b);
		return bl;
	}
	
	default int getInt(int index)
	{
		return get(index);
	}
	
	default boolean addInt(int index)
	{
		return add(index);
	}
	
	default int findInt(int i)
	{
		return indexOf(i);
	}
}