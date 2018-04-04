package com.endie.lib.fast.lists;

import java.util.List;

public interface BoolList extends List<Boolean>
{
	static BoolList of(boolean... data)
	{
		BoolList bl = new BoolArrayList();
		for(boolean b : data)
			bl.addBoolean(b);
		return bl;
	}
	
	default boolean getBool(int index)
	{
		return get(index);
	}
	
	default boolean addBoolean(boolean index)
	{
		return add(index);
	}
}