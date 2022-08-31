package org.zeith.hammerlib.util.java;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A reference to a variable that may be get(required) or set(optional)
 */
public class DirectStorage<T>
{
	final Consumer<T> set;
	final Supplier<T> get;
	
	public DirectStorage(Consumer<T> set, Supplier<T> get)
	{
		this.set = set;
		this.get = get;
	}
	
	public void set(T val)
	{
		if(set != null)
			set.accept(val);
	}
	
	public T get()
	{
		return get.get();
	}
	
	public static <T> DirectStorage<T> create(Consumer<T> set, Supplier<T> get)
	{
		return new DirectStorage<>(set, get);
	}
	
	public static <T> DirectStorage<T> readonly(Supplier<T> get)
	{
		return new DirectStorage<>(null, get);
	}
	
	public static <T> DirectStorage<T> constant(T var)
	{
		return new DirectStorage<>(null, () -> var);
	}
	
	public static <T> DirectStorage<T> allocate()
	{
		AtomicReference<T> ref = new AtomicReference<>();
		return create(ref::set, ref::get);
	}
	
	public static <T> DirectStorage<T> allocate(T intitial)
	{
		AtomicReference<T> ref = new AtomicReference<>(intitial);
		return create(ref::set, ref::get);
	}
	
	@Override
	public String toString()
	{
		return "DirectStorage{" + get() + "}";
	}
}