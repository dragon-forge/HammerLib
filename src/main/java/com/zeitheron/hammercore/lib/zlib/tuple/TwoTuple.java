package com.zeitheron.hammercore.lib.zlib.tuple;

import java.util.Arrays;

public class TwoTuple<P1, P2>
{
	protected P1 p1;
	protected P2 p2;
	
	public TwoTuple(P1 p1, P2 p2)
	{
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public P1 get1()
	{
		return this.p1;
	}
	
	public P2 get2()
	{
		return this.p2;
	}
	
	public boolean isAtomic()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + Arrays.asList(p1, p2);
	}
	
	public static class Atomic<P1, P2> extends TwoTuple<P1, P2>
	{
		public Atomic(P1 p1, P2 p2)
		{
			super(p1, p2);
		}
		
		@Override
		public boolean isAtomic()
		{
			return true;
		}
		
		public void set(P1 p1, P2 p2)
		{
			this.p1 = p1;
			this.p2 = p2;
		}
		
		public void set1(P1 p1)
		{
			this.p1 = p1;
		}
		
		public void set2(P2 p2)
		{
			this.p2 = p2;
		}
	}
}