package com.endie.lib.utils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import com.endie.lib.utils.function.ObjToIntFunction;

public class PriorityList<T> extends AbstractList<T>
{
	private List<T> sorted = new ArrayList<>();
	private ObjToIntFunction<T> prio;
	
	public void setPriority(ObjToIntFunction<T> i)
	{
		prio = i;
	}
	
	public boolean add(T elem)
	{
		int p = prio.applyAsInt(elem);
		
		if(sorted.isEmpty())
			return sorted.add(elem);
		
		for(int i = 0; i < sorted.size(); ++i)
		{
			int cp = prio.applyAsInt(sorted.get(i));
			
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
	
	public static <T> PriorityList<T> sort(List<T> src, ObjToIntFunction<T> priority)
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