package com.zeitheron.hammercore.lib.zlib.utils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class PriorityList<T>
		extends AbstractList<T>
{
	private List<T> sorted = new ArrayList<>();
	private ToIntFunction<T> prio;

	public void setPriority(Function<T, Integer> i)
	{
		prio = i::apply;
	}

	public void setPriority(ToIntFunction<T> i)
	{
		prio = i;
	}

	@Override
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

	/**
	 * @return The inverted list.
	 */
	public PriorityList<T> invert()
	{
		PriorityList<T> pl = new PriorityList<>();
		pl.prio = prio;
		for(int i = sorted.size() - 1; i >= 0; --i)
			pl.sorted.add(sorted.get(i));
		return pl;
	}

	public static <T> PriorityList<T> sort(List<T> src, Function<T, Integer> priority)
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

	public static class PriorityHolder<T>
	{
		public final T val;
		public final int prior;

		public PriorityHolder(T value, int priority)
		{
			this.val = value;
			this.prior = priority;
		}

		public static <K> Function<PriorityHolder<K>, Integer> sorter()
		{
			return holder -> holder.prior;
		}
	}
}