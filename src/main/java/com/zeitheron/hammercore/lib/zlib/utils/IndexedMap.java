package com.zeitheron.hammercore.lib.zlib.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This map uses ArrayLists to store key-value pairs. It does not support
 * element removal from {@link #keySet()}, {@link #entrySet()}
 */
public class IndexedMap<K, V> implements Map<K, V>, Serializable
{
	private static final long serialVersionUID = 3667362897170201432L;
	
	private final List<K> keys;
	private final List<V> values;
	
	private IndexedMap(List<K> keys, List<V> values)
	{
		this.keys = keys;
		this.values = values;
	}
	
	public IndexedMap()
	{
		this(new ArrayList<>(), new ArrayList<>());
	}
	
	@Override
	public int size()
	{
		return keys.size();
	}
	
	@Override
	public boolean isEmpty()
	{
		return keys.isEmpty();
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		return keys.contains(key);
	}
	
	@Override
	public boolean containsValue(Object value)
	{
		return values.contains(value);
	}
	
	@Override
	public V get(Object key)
	{
		if(containsKey(key))
			return values.get(keys.indexOf(key));
		return null;
	}
	
	public K getKey(V value)
	{
		if(containsValue(value))
			return keys.get(values.indexOf(value));
		return null;
	}
	
	public K getKey(int index)
	{
		if(index < 0 || index >= keys.size())
			return null;
		return keys.get(index);
	}
	
	public V getValue(int index)
	{
		if(index < 0 || index >= values.size())
			return null;
		return values.get(index);
	}
	
	public int indexOf(K key)
	{
		return keys.indexOf(key);
	}
	
	public List<K> getKeys()
	{
		return keys;
	}
	
	public List<V> getValues()
	{
		return values;
	}
	
	@Override
	public V put(K key, V value)
	{
		if(containsKey(key))
		{
			int i = keys.indexOf(key);
			V pval = values.get(i);
			keys.set(i, key);
			values.set(i, value);
			return pval;
		} else
		{
			keys.add(key);
			values.add(value);
		}
		
		return null;
	}
	
	@Override
	public V remove(Object key)
	{
		if(containsKey(key))
		{
			int i = keys.indexOf(key);
			keys.remove(i);
			return values.remove(i);
		}
		return null;
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		for(K key : m.keySet())
			put(key, m.get(key));
	}
	
	@Override
	public void clear()
	{
		keys.clear();
		values.clear();
	}
	
	@Override
	public Set<K> keySet()
	{
		return new LinkedHashSet<>(keys);
	}
	
	@Override
	public Collection<V> values()
	{
		return values;
	}
	
	@Override
	public Set<Entry<K, V>> entrySet()
	{
		Set<Entry<K, V>> set = new LinkedHashSet<>();
		for(K key : keys)
			set.add(new ArrayEntry<K, V>(key, values.get(keys.indexOf(key))));
		return set;
	}
	
	public IndexedMap<V, K> inverseLinked()
	{
		return new IndexedMap<>(values, keys);
	}
	
	public static class ArrayEntry<K, V> implements Map.Entry<K, V>
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
			return this.key;
		}
		
		@Override
		public V getValue()
		{
			return this.value;
		}
		
		@Override
		public V setValue(V value)
		{
			this.value = value;
			return this.value;
		}
	}
}