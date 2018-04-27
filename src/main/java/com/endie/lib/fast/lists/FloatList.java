package com.endie.lib.fast.lists;

import java.util.List;

public interface FloatList extends List<Float>
{
	static FloatList of(float... data)
	{
		FloatList bl = new FloatArrayList();
		for(float b : data)
			bl.addFloat(b);
		return bl;
	}
	
	default float getFloat(int index)
	{
		return get(index);
	}
	
	default boolean addFloat(float index)
	{
		return add(index);
	}
	
	default int findFloat(float i)
	{
		return indexOf(i);
	}
}