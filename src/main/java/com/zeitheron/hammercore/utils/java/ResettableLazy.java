package com.zeitheron.hammercore.utils.java;

import java.util.function.*;

public class ResettableLazy<T>
{
	public static <T> ResettableLazy<T> of()
	{
		return new ResettableLazy<>((Supplier<T>) null);
	}
	
	public static <T> ResettableLazy<T> of(final T value)
	{
		return new ResettableLazy<T>(value);
	}
	
	public static <T> ResettableLazy<T> of(final Supplier<T> provider)
	{
		return new ResettableLazy<T>(provider);
	}
	
	private final Object lock = new Object();
	private T value;
	private Boolean initialized;
	private final Supplier<T> provider;
	
	private ResettableLazy(final T value)
	{
		this.value = value;
		this.initialized = true;
		this.provider = () -> value;
	}
	
	private ResettableLazy(final Supplier<T> provider)
	{
		this.value = null;
		this.initialized = false;
		this.provider = provider;
	}
	
	public void reset()
	{
		this.value = null;
		this.initialized = false;
	}
	
	public T get()
	{
		synchronized(lock)
		{
			if(!initialized && provider != null)
			{
				initialized = true;
				this.value = provider.get();
			}
			
			return value;
		}
	}
	
	public void ifPresent(final Consumer<T> consumer)
	{
		synchronized(lock)
		{
			if(!initialized)
				return;
			
			consumer.accept(this.value);
		}
	}
	
	public <R> ResettableLazy<R> map(Function<T, R> mapper)
	{
		synchronized(lock)
		{
			return of(() -> mapper.apply(get()));
		}
	}
	
	public T orElse(T elseValue)
	{
		synchronized(lock)
		{
			if(!initialized)
				return elseValue;
			
			return value;
		}
	}
}
