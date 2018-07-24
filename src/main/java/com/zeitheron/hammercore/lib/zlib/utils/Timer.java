package com.zeitheron.hammercore.lib.zlib.utils;

public class Timer
{
	private static final long NS_PER_SECOND = 1000000000;
	private static final long MAX_NS_PER_UPDATE = 1000000000;
	private static final int MAX_TICKS_PER_UPDATE = 100;
	private float ticksPerSecond;
	private long lastTime;
	public int ticks;
	public float partialTime;
	public float timeScale = 1.0f;
	public float fps = 0.0f;
	public float passedTime = 0.0f;
	
	public Timer(float ticksPerSecond)
	{
		this.ticksPerSecond = ticksPerSecond;
		this.lastTime = System.nanoTime();
	}
	
	public void advanceTime()
	{
		long now = System.nanoTime();
		long passedNs = now - this.lastTime;
		this.lastTime = now;
		if(passedNs < 0)
			passedNs = 0;
		if(passedNs > MAX_NS_PER_UPDATE)
			passedNs = MAX_NS_PER_UPDATE;
		if(passedNs == 0L)
			return;
		this.fps = NS_PER_SECOND / passedNs;
		this.passedTime += (float) passedNs * this.timeScale * this.ticksPerSecond / 1.0E9f;
		this.ticks = (int) this.passedTime;
		if(this.ticks > MAX_TICKS_PER_UPDATE)
			this.ticks = MAX_TICKS_PER_UPDATE;
		this.passedTime -= (float) this.ticks;
		this.partialTime = this.passedTime;
	}
	
	public void resetTimer()
	{
		this.lastTime = System.nanoTime();
	}
}