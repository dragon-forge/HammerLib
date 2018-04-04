package com.endie.lib.fast.lists;

import java.util.List;

public interface ByteList extends List<Byte>
{
	static ByteList of(byte... data)
	{
		ByteList bl = new ByteArrayList();
		for(byte b : data)
			bl.addByte(b);
		return bl;
	}
	
	default byte getByte(int index)
	{
		return get(index);
	}
	
	default boolean addByte(byte index)
	{
		return add(index);
	}
	
	default int findByte(byte i)
	{
		return indexOf(i);
	}
}