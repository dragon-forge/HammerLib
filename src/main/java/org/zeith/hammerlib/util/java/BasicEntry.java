package org.zeith.hammerlib.util.java;

import java.util.Map.Entry;

/**
 * Basic implementation of java {@link Entry}
 */
public class BasicEntry<K, V>
		implements Entry<K, V>
{
	protected K key;
	protected V value;
	
	public BasicEntry(K key, V value)
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
	
	public ImmutableViewEntry<K, V> immutableView()
	{
		return new ImmutableViewEntry<>(this);
	}
	
	public static final class ImmutableViewEntry<K, V>
			implements Entry<K, V>
	{
		final BasicEntry<K, V> basic;
		
		private ImmutableViewEntry(BasicEntry<K, V> basic)
		{
			this.basic = basic;
		}
		
		@Override
		public K getKey()
		{
			return basic.getKey();
		}
		
		@Override
		public V getValue()
		{
			return basic.getValue();
		}
		
		@Override
		public V setValue(V value)
		{
			throw new UnsupportedOperationException();
		}
	}
}