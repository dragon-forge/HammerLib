package org.zeith.hammerlib.util.java;

import javax.annotation.Nonnull;
import java.util.*;

public class RoundRobinList<E>
		extends ArrayList<E>
{
	private int lastIndex;
	
	public RoundRobinList()
	{
	}
	
	public RoundRobinList(int initialCapacity)
	{
		super(initialCapacity);
	}
	
	public RoundRobinList(@Nonnull Collection<? extends E> c)
	{
		super(c);
	}
	
	public E next()
	{
		E e = get(lastIndex);
		skip(1);
		return e;
	}
	
	public E prev()
	{
		backward(1);
		return get(lastIndex);
	}
	
	public void skip(int steps)
	{
		setPos(lastIndex + steps);
	}
	
	public void backward(int steps)
	{
		int s = lastIndex - steps;
		while(s < 0)
			s += size();
		setPos(s);
	}
	
	public int getPos()
	{
		return lastIndex;
	}
	
	public void setPos(int i)
	{
		lastIndex = Math.abs(i) % size();
	}
	
	public static <T> RoundRobinList<T> of(T... arr)
	{
		return new RoundRobinList<>(List.of(arr));
	}
	
	public static <T> RoundRobinList<T> copyOf(Collection<? extends T> coll)
	{
		return new RoundRobinList<>(coll);
	}
}