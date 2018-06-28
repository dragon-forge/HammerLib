package com.zeitheron.hammercore.lib.zlib.utils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2IntFunction;

public class PriorityList<T> extends AbstractList<T>
{
	private List<T> sorted = new ArrayList<>();
	private Object2IntFunction<T> prio;
	
	public void setPriority(Object2IntFunction<T> i)
	{
		prio = i;
	}
	
	public boolean add(T elem)
	{
		int p = prio.getInt(elem);
		
		if(sorted.isEmpty())
			return sorted.add(elem);
		
		for(int i = 0; i < sorted.size(); ++i)
		{
			int cp = prio.getInt(sorted.get(i));
			
			if(p >= cp)
				continue;
			
			sorted.add(i, elem);
			
			return true;
		}
		
		return sorted.add(elem);
	}
	
	/** Inverts the list. */
	public PriorityList<T> invert()
	{
		PriorityList<T> pl = new PriorityList<>();
		pl.prio = prio;
		for(int i = sorted.size() - 1; i >= 0; --i)
			pl.sorted.add(sorted.get(i));
		return pl;
	}
	
	public static <T> PriorityList<T> sort(List<T> src, Object2IntFunction<T> priority)
	{
		PriorityList<T> pl = new PriorityList<>();
		pl.setPriority(priority);
		for(T i : src)
			pl.add(i);
		return pl;
	}
	
	@Override
	public T get(int index)
	{
		return sorted.get(index);
	}
	
	@Override
	public int size()
	{
		return sorted.size();
	}
}