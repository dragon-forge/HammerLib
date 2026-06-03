package com.zeitheron.hammercore.utils.java;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.*;
import java.util.function.IntConsumer;

public final class FuturesUtil
{
	
	private FuturesUtil() {}
	
	public static <T> CompletableFuture<T> firstSuccessfulInOrder(List<? extends CompletableFuture<? extends T>> futures, IntConsumer indexCbq)
	{
		CompletableFuture<T> result = new CompletableFuture<>();
		
		if(futures == null || futures.isEmpty())
		{
			result.completeExceptionally(new NoSuchElementException("No futures provided"));
			return result;
		}
		
		int n = futures.size();
		AtomicBoolean done = new AtomicBoolean(false);
		AtomicReferenceArray<Throwable> errors = new AtomicReferenceArray<>(n);
		AtomicReferenceArray<Object> values = new AtomicReferenceArray<>(n);
		
		for(int i = 0; i < n; i++)
		{
			final int index = i;
			
			futures.get(i).whenCompleteAsync((value, err) ->
			{
				if(done.get()) return;
				
				if(err == null)
				{
					values.set(index, value);
					
					// only accept if all earlier ones are either failed OR already completed unsuccessfully
					for(int j = 0; j < index; j++)
					{
						if(errors.get(j) == null && values.get(j) == null)
						{
							// earlier one still pending → cannot decide yet
							return;
						}
					}
					
					if(done.compareAndSet(false, true))
					{
						result.complete(value);
						if(indexCbq != null) indexCbq.accept(index);
					}
				} else
				{
					errors.set(index, err);
					
					// if all failed
					boolean allFailed = true;
					for(int j = 0; j < n; j++)
					{
						if(values.get(j) != null || errors.get(j) == null)
						{
							allFailed = false;
							break;
						}
					}
					
					if(allFailed && done.compareAndSet(false, true))
					{
						result.completeExceptionally(new NoSuchElementException("No future completed successfully"));
					}
				}
			});
		}
		
		return result;
	}
}