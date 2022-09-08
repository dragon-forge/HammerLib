package org.zeith.hammerlib.util.java.collections;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

public class UnmodifiableConcatCollection<E>
		implements Collection<E>, Serializable
{
	@java.io.Serial
	private static final long serialVersionUID = 1820017752578914078L;
	
	final Collection<Collection<? extends E>> cs;
	
	public UnmodifiableConcatCollection(Collection<Collection<? extends E>> c)
	{
		if(c == null)
			throw new NullPointerException();
		this.cs = c;
	}
	
	@Override
	public int size()
	{
		return cs.stream().mapToInt(Collection::size).sum();
	}
	
	@Override
	public boolean isEmpty()
	{
		return cs.stream().allMatch(Collection::isEmpty);
	}
	
	@Override
	public boolean contains(Object o)
	{
		return cs.stream().anyMatch(c -> c.contains(o));
	}
	
	@Override
	public Object[] toArray()
	{
		return cs.stream().flatMap(Collection::stream).toArray();
	}
	
	@Override
	public <T> T[] toArray(T[] a)
	{
		return cs.stream().flatMap(Collection::stream).toList().toArray(a);
	}
	
	@Override
	public <T> T[] toArray(IntFunction<T[]> f)
	{
		return cs.stream().flatMap(Collection::stream).toArray(f);
	}
	
	@Override
	public String toString()
	{
		return cs.toString();
	}
	
	@Override
	public Iterator<E> iterator()
	{
		Iterator<Collection<? extends E>> itrGlob = cs.iterator();
		
		return new Iterator<E>()
		{
			private Iterator<? extends E> i = itrGlob.next().iterator();
			
			private Iterator<? extends E> getIterator()
			{
				if(!i.hasNext() && itrGlob.hasNext())
					i = itrGlob.next().iterator();
				return i;
			}
			
			@Override
			public boolean hasNext()
			{
				return getIterator().hasNext();
			}
			
			@Override
			public E next()
			{
				return getIterator().next();
			}
			
			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void forEachRemaining(Consumer<? super E> action)
			{
				// Use backing collection version
				i.forEachRemaining(action);
				itrGlob.forEachRemaining(c -> c.iterator().forEachRemaining(action));
			}
		};
	}
	
	public boolean add(E e)
	{
		throw new UnsupportedOperationException();
	}
	
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException();
	}
	
	public boolean containsAll(Collection<?> coll)
	{
		return cs.stream().toList().containsAll(coll);
	}
	
	public boolean addAll(Collection<? extends E> coll)
	{
		throw new UnsupportedOperationException();
	}
	
	public boolean removeAll(Collection<?> coll)
	{
		throw new UnsupportedOperationException();
	}
	
	public boolean retainAll(Collection<?> coll)
	{
		throw new UnsupportedOperationException();
	}
	
	public void clear()
	{
		throw new UnsupportedOperationException();
	}
	
	// Override default methods in Collection
	@Override
	public void forEach(Consumer<? super E> action)
	{
		cs.stream().flatMap(Collection::stream).forEach(action);
	}
	
	@Override
	public boolean removeIf(Predicate<? super E> filter)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Spliterator<E> spliterator()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Stream<E> stream()
	{
		return cs.stream().flatMap(Collection::stream);
	}
	
	@Override
	public Stream<E> parallelStream()
	{
		return cs.parallelStream().flatMap(Collection::parallelStream);
	}
}