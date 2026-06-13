package org.zeith.hammerlib.abstractions.props;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class KeyMap
{
	public static final KeyMap EMPTY = new KeyMap(Collections::emptyMap);
	
	protected final Map<Key<?>, Object> values;
	
	public KeyMap(Supplier<Map<Key<?>, Object>> mapSupplier)
	{
		this.values = mapSupplier.get();
	}
	
	public boolean isEmpty()
	{
		return this.values.isEmpty();
	}
	
	public <T> Optional<T> opt(Key<T> key)
	{
		return Optional.ofNullable(get(key));
	}
	
	public <T> T get(Key<T> key)
	{
		return (T) values.get(key);
	}
	
	public <T> T getOrDefault(Key<T> key, T defaultValue)
	{
		return (T) values.getOrDefault(key, defaultValue);
	}
	
	public <T> T getOrSupply(Key<T> key, Supplier<T> defaultValue)
	{
		Object t = values.get(key);
		return t != null ? (T) t : defaultValue.get();
	}
	
	public <T> T computeIfAbsent(Key<T> key, Supplier<T> defaultValue)
	{
		return (T) values.computeIfAbsent(key, k -> defaultValue.get());
	}
	
	public <T> T put(Key<T> key, T value)
	{
		return (T) values.put(key, value);
	}
	
	public void putAll(KeyMap other)
	{
		this.values.putAll(other.values);
	}
	
	public <T> T remove(Key<T> key)
	{
		return (T) this.values.remove(key);
	}
	
	public <T> KeyMap with(Key<T> key, T value)
	{
		put(key, value);
		return this;
	}
	
	public KeyMap withAll(KeyMap other)
	{
		this.values.putAll(other.values);
		return this;
	}
	
	public <T> KeyMap withExcept(Key<T> key)
	{
		remove(key);
		return this;
	}
	
	public static KeyMap createHash()
	{
		return new KeyMap(HashMap::new);
	}
	
	public static KeyMap createConcurrentHash()
	{
		return new KeyMap(ConcurrentHashMap::new);
	}
	
	public static KeyMap createHash(int initialCapacity)
	{
		return new KeyMap(() -> new HashMap<>(initialCapacity));
	}
	
	public static KeyMap createConcurrentHash(int initialCapacity)
	{
		return new KeyMap(() -> new ConcurrentHashMap<>(initialCapacity));
	}
}