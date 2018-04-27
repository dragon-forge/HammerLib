package com.endie.lib.utils.streaming;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.endie.lib.utils.function.ObjToBoolFunction;

public class IterWrapper<T> implements Iterator<T>, Enumeration<T>
{
	public final BooleanSupplier hasMore;
	public final Supplier<T> next;
	
	public static <T> IterWrapper<T> of(T... ts)
	{
		return of(Arrays.stream(ts));
	}
	
	public static <T> IterWrapper<T> of(Stream<T> st)
	{
		return of(st.iterator());
	}
	
	public static <T> IterWrapper<T> of(Iterable<T> it)
	{
		return of(it.iterator());
	}
	
	public static <T> IterWrapper<T> of(Iterator<T> it)
	{
		if(it instanceof IterWrapper)
			return (IterWrapper<T>) it;
		return new IterWrapper<T>(it::hasNext, it::next);
	}
	
	public static <T> IterWrapper<T> of(Enumeration<T> en)
	{
		if(en instanceof IterWrapper)
			return (IterWrapper<T>) en;
		return new IterWrapper<T>(en::hasMoreElements, en::nextElement);
	}
	
	public IterWrapper(BooleanSupplier hasMore, Supplier<T> next)
	{
		this.hasMore = hasMore;
		this.next = next;
	}
	
	@Override
	public boolean hasNext()
	{
		return hasMore.getAsBoolean();
	}
	
	@Override
	public T next()
	{
		return next.get();
	}
	
	@Override
	public boolean hasMoreElements()
	{
		return hasNext();
	}
	
	@Override
	public T nextElement()
	{
		return next();
	}
}