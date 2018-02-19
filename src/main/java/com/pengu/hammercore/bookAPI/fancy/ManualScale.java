package com.pengu.hammercore.bookAPI.fancy;

public class ManualScale
{
	private static long start = System.currentTimeMillis();
	private static float prev = 1;
	private static float target = 1;
	
	public static float get()
	{
		long maxTransition = 250L;
		
		float curr = target;
		if(System.currentTimeMillis() - start < maxTransition)
			curr = prev + (target - prev) * ((System.currentTimeMillis() - start) % maxTransition) / maxTransition;
		
		return curr;
	}
	
	public static void set(float scale)
	{
		prev = get();
		target = scale;
		start = System.currentTimeMillis();
	}
}