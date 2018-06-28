package com.zeitheron.hammercore.lib.zlib.utils;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("serial")
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
		if(this.size() >= this.capacity)
			if(this.removeIfFull)
				this.remove(0);
			else
				return false;
		return super.add(e);
	}
	
	@Override
	public void add(int index, E element)
	{
		if(this.size() >= this.capacity)
			if(this.removeIfFull)
				this.remove(0);
			else
				return;
		super.add(index, element);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		if(this.removeIfFull && this.size() + c.size() >= this.capacity)
			this.removeRange(0, this.size() + c.size() - this.capacity + 1);
		ArrayList<E> nc0 = new ArrayList<>();
		ArrayList<E> nc1 = new ArrayList<>();
		nc1.addAll(c);
		while(this.size() + nc1.size() < this.capacity && !nc1.isEmpty())
			nc0.add(nc1.remove(nc1.size() - 1));
		return super.addAll(nc0);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		if(this.removeIfFull && this.size() + c.size() >= this.capacity)
			this.removeRange(0, this.size() + c.size() - this.capacity + 1);
		ArrayList<E> nc0 = new ArrayList<>();
		ArrayList<E> nc1 = new ArrayList<>();
		nc1.addAll(index, c);
		while(this.size() + nc1.size() < this.capacity && !nc1.isEmpty())
			nc0.add(nc1.remove(nc1.size() - 1));
		return super.addAll(nc0);
	}
}
