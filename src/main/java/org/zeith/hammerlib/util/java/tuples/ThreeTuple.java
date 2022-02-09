package org.zeith.hammerlib.util.java.tuples;

public class ThreeTuple<A, B, C>
{
	private A a;
	private B b;
	private C c;

	public ThreeTuple(A a, B b, C c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public A getA()
	{
		return this.a;
	}

	public B getB()
	{
		return this.b;
	}

	public C getC()
	{
		return this.c;
	}
}