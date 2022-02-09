package org.zeith.hammerlib.util.java;

import java.util.ArrayList;

public class RoundRobinList<E>
		extends ArrayList<E>
{
	private int lastIndex;

	public E next()
	{
		E e = get(lastIndex);
		skip(1);
		return e;
	}

	public void skip(int steps)
	{
		setPos(lastIndex + steps);
	}

	public void backward(int steps)
	{
		int s = lastIndex - steps;
		while(s < 0)
			s += size();
		setPos(s);
	}

	public int getPos()
	{
		return lastIndex;
	}

	public void setPos(int i)
	{
		lastIndex = Math.abs(i) % size();
	}
}