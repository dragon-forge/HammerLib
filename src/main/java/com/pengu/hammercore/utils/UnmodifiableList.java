package com.pengu.hammercore.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class UnmodifiableList<E> implements List<E>
{
	private final List<E> parent;
	
	public UnmodifiableList(List<E> parent)
	{
		this.parent = parent;
	}
	
	@Override
	public int size()
	{
		return parent.size();
	}
	
	@Override
	public boolean isEmpty()
	{
		return parent.isEmpty();
	}
	
	@Override
	public boolean contains(Object o)
	{
		return parent.contains(o);
	}
	
	@Override
	public Iterator<E> iterator()
	{
		return parent.iterator();
	}
	
	@Override
	public E[] toArray()
	{
		return (E[]) parent.toArray(new Object[0]);
	}
	
	@Override
	public <T> T[] toArray(T[] a)
	{
		return parent.toArray(a);
	}
	
	@Override
	public boolean add(E e)
	{
		throw new UnsupportedOperationException("Unable to modify an unmodifiable list!");
	}
	
	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException("Unable to modify an unmodifiable list!");
	}
	
	@Override
	public boolean containsAll(Collection<?> c)
	{
		return parent.containsAll(c);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		throw new UnsupportedOperationException("Unable to modify an unmodifiable list!");
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		throw new UnsupportedOperationException("Unable to modify an unmodifiable list!");
	}
	
	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("Unable to modify an unmodifiable list!");
	}
	
	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("Unable to modify an unmodifiable list!");
	}
	
	@Override
	public void clear()
	{
		throw new UnsupportedOperationException("Unable to modify an unmodifiable list!");
	}
	
	@Override
	public E get(int index)
	{
		return parent.get(index);
	}
	
	@Override
	public E set(int index, E element)
	{
		throw new UnsupportedOperationException("Unable to modify an unmodifiable list!");
	}
	
	@Override
	public void add(int index, E element)
	{
		throw new UnsupportedOperationException("Unable to modify an unmodifiable list!");
	}
	
	@Override
	public E remove(int index)
	{
		throw new UnsupportedOperationException("Unable to modify an unmodifiable list!");
	}
	
	@Override
	public int indexOf(Object o)
	{
		return parent.indexOf(o);
	}
	
	@Override
	public int lastIndexOf(Object o)
	{
		return parent.lastIndexOf(o);
	}
	
	@Override
	public ListIterator<E> listIterator()
	{
		return parent.listIterator();
	}
	
	@Override
	public ListIterator<E> listIterator(int index)
	{
		return parent.listIterator(index);
	}
	
	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		return parent.subList(fromIndex, toIndex);
	}
	
	@Override
	public String toString()
	{
		return parent + "";
	}
}