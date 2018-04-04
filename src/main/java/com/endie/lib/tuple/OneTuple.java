package com.endie.lib.tuple;

import java.util.Arrays;

public class OneTuple<P1>
{
	protected P1 p1;
	
	public OneTuple(P1 p1)
	{
		this.p1 = p1;
	}
	
	public P1 get()
	{
		return this.p1;
	}
	
	public boolean isAtomic()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + Arrays.asList(p1);
	}
	
	public static class Atomic<P1> extends OneTuple<P1>
	{
		public Atomic(P1 p1)
		{
			super(p1);
		}
		
		@Override
		public boolean isAtomic()
		{
			return true;
		}
		
		public void set(P1 p1)
		{
			this.p1 = p1;
		}
		
		public void set1(P1 p1)
		{
			this.p1 = p1;
		}
	}
}