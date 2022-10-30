package org.zeith.hammerlib.util.java;

import java.util.Map.Entry;

/**
 * Immutable implementation of java {@link Entry}
 */
public class ImmutableEntry<K, V>
		implements Entry<K, V>
{
	protected final K key;
	protected final V value;
	
	public ImmutableEntry(K key, V value)
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
		throw new UnsupportedOperationException();
	}
}