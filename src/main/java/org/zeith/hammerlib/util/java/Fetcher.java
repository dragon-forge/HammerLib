package org.zeith.hammerlib.util.java;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class Fetcher<T>
		implements Supplier<T>
{
	private final Supplier<T> fetch;
	
	private final BooleanSupplier shouldRefetch;
	
	private boolean cached = false;
	private T cache;
	
	public Fetcher(Supplier<T> fetch, BooleanSupplier shouldRefetch)
	{
		this.fetch = fetch;
		this.shouldRefetch = shouldRefetch;
	}
	
	@Override
	public T get()
	{
		if(!cached || shouldRefetch.getAsBoolean())
		{
			cache = fetch.get();
			cached = true;
		}
		return cache;
	}
	
	private static final BooleanSupplier ALWAYS_FALSE = () -> false;
	
	public static <T> Fetcher<T> fetchOnce(Supplier<T> origin)
	{
		return new Fetcher<>(origin, ALWAYS_FALSE);
	}
	
	public static <T> Fetcher<T> refetchAtRate(Supplier<T> origin, long refetchTime, TimeUnit refetchUnit)
	{
		return new Fetcher<>(origin, new RateTicker(refetchTime, refetchUnit));
	}
}