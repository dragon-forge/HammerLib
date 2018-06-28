package com.zeitheron.hammercore.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListUtils
{
	public static <T> ArrayList<T> randomizeList(List<T> list, Random rand)
	{
		ArrayList<T> sublist = new ArrayList<>();
		ArrayList<T> clist = new ArrayList<>(list);
		
		while(!clist.isEmpty())
		{
			int index = rand.nextInt(clist.size());
			T t = clist.remove(index);
			sublist.add(t);
		}
		
		clist.clear();
		clist = null;
		return sublist;
	}
	
	public static <T> T random(List<T> list, Random rand)
	{
		if(list.isEmpty())
			return null;
		return list.get(rand.nextInt(list.size()));
	}
}