package com.zeitheron.hammercore.utils.java;

public class BoolArray32
		implements Comparable<BoolArray32>
{
	private int values;
	
	public static boolean get(int values, int pos)
	{
		return (values & (1 << pos)) != 0;
	}
	
	public static int set(int values, int pos, boolean value)
	{
		int mask = 1 << pos;
		return (values & ~mask) | (value ? mask : 0);
	}
	
	public static int setTrue(int values, int... positions)
	{
		for(int position : positions) values = set(values, position, true);
		return values;
	}
	
	public boolean get(int pos)
	{
		return (values & (1 << pos)) != 0;
	}
	
	public void set(int pos, boolean value)
	{
		int mask = 1 << pos;
		values = (values & ~mask) | (value ? mask : 0);
	}
	
	@Override
	public int compareTo(BoolArray32 b2)
	{
		return countBits(b2.values & values);
	}
	
	public static int countBits(int v)
	{
		v = v - ((v >>> 1) & 0x55555555);
		v = (v & 0x33333333) + ((v >>> 2) & 0x33333333);
		return ((v + (v >>> 4) & 0xF0F0F0F) * 0x1010101) >>> 24;
	}
}