package com.zeitheron.hammercore.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LimitedList<E> extends ArrayList<E>
{
	public final int capacity;
	public final boolean removeIfFull;
	
	public LimitedList(int capacity, boolean removeIfFull)
	{
		this.capacity = capacity;
		this.removeIfFull = removeIfFull;
	}
	
	@Override
	public boolean add(E e)
	{
		if(size() >= capacity)
			if(removeIfFull)
				remove(0);
			else
				return false;
		return super.add(e);
	}
	
	@Override
	public void add(int index, E element)
	{
		if(size() >= capacity)
			if(removeIfFull)
				remove(0);
			else
				return;
		super.add(index, element);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		if(removeIfFull && size() + c.size() >= capacity)
			removeRange(0, size() + c.size() - capacity + 1);
		
		List<E> nc0 = new ArrayList<>();
		List<E> nc1 = new ArrayList<>();
		nc1.addAll(c);
		while(size() + nc1.size() < capacity && !nc1.isEmpty())
			nc0.add(nc1.remove(nc1.size() - 1));
		return super.addAll(nc0);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		if(removeIfFull && size() + c.size() >= capacity)
			removeRange(0, size() + c.size() - capacity + 1);
		
		List<E> nc0 = new ArrayList<>();
		List<E> nc1 = new ArrayList<>();
		nc1.addAll(index, c);
		while(size() + nc1.size() < capacity && !nc1.isEmpty())
			nc0.add(nc1.remove(nc1.size() - 1));
		return super.addAll(nc0);
	}
}