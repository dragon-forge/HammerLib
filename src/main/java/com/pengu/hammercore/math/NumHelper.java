package com.pengu.hammercore.math;

import java.util.List;

public class NumHelper
{
	public static int[] toPrim(List<Integer> ints)
	{
		ints.removeIf(i -> i == null);
		int[] a = new int[ints.size()];
		for(int i = 0; i < ints.size(); ++i)
			a[i] = ints.get(i).intValue();
		return a;
	}
}