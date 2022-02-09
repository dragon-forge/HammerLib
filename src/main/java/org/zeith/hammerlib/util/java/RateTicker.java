package org.zeith.hammerlib.util.java;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

public class RateTicker
		implements BooleanSupplier
{
	private final long timerMS;

	private boolean called;
	private long lastCall;

	public RateTicker(long timer, TimeUnit unit)
	{
		this.timerMS = TimeUnit.MILLISECONDS.convert(timer, unit);
	}

	@Override
	public boolean getAsBoolean()
	{
		long now = System.currentTimeMillis();

		if(now - lastCall > timerMS || !called)
		{
			called = true;
			lastCall = System.currentTimeMillis();
			return true;
		}

		return false;
	}
}