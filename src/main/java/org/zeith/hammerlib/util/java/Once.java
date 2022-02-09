package org.zeith.hammerlib.util.java;

import com.google.common.util.concurrent.Runnables;

import java.util.concurrent.Callable;

public class Once
{
	private Runnable call;

	public static Once run(Runnable r)
	{
		return new Once(r);
	}

	public static <T> TypeOnce<T> call(Callable<T> r)
	{
		return new TypeOnce<>(r);
	}

	public Once(Runnable run)
	{
		call = run;
	}

	public Object call()
	{
		if(call != null)
			call.run();
		call = null;
		return null;
	}

	public static class TypeOnce<T>
			extends Once
	{
		private Callable<T> callable;

		public TypeOnce(Callable<T> run)
		{
			super(Runnables.doNothing());
			this.callable = run;
		}

		@Override
		public T call()
		{
			if(callable != null)
				try
				{
					T v = callable.call();
					callable = null;
					return v;
				} catch(Exception e)
				{
					throw new RuntimeException(e);
				}
			return null;
		}
	}
}