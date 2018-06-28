package com.zeitheron.hammercore.utils;

import java.util.Map.Entry;

/**
 * Basic implementation of java {@link Entry}
 */
public class ArrayEntry<K, V> implements Entry<K, V>
{
	protected K key;
	protected V value;
	
	public ArrayEntry(K key, V value)
	{
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey()
	{
		return key;
	}
	
	@Override
	public V getValue()
	{
		return value;
	}
	
	@Override
	public V setValue(V value)
	{
		return this.value = value;
	}
}