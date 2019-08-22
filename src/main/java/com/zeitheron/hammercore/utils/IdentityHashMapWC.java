package com.zeitheron.hammercore.utils;

import java.util.IdentityHashMap;

public class IdentityHashMapWC<K, V> extends IdentityHashMap<K, V>
{
	public final IdentityHashMap<K, V> constants = new IdentityHashMap<>();
	
	public V putConstant(K k, V v)
	{
		return this.constants.put(k, v);
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		return super.containsKey(key) || constants.containsKey(key);
	}
	
	@Override
	public boolean containsValue(Object value)
	{
		return super.containsValue(value) || constants.containsValue(value);
	}
	
	@Override
	public V get(Object key)
	{
		return constants.containsKey(key) ? constants.get(key) : super.get(key);
	}
}