package org.zeith.hammerlib.util.java.tuples;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple10<A, B, C, D, E, F, G, H, I, J>
		implements ITuple
{
	protected A a;
	protected B b;
	protected C c;
	protected D d;
	protected E e;
	protected F f;
	protected G g;
	protected H h;
	protected I i;
	protected J j;
	
	public Tuple10(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.g = g;
		this.h = h;
		this.i = i;
		this.j = j;
	}
	
	public A a()
	{
		return a;
	}
	
	public B b()
	{
		return b;
	}
	
	public C c()
	{
		return c;
	}
	
	public D d()
	{
		return d;
	}
	
	public E e()
	{
		return e;
	}
	
	public F f()
	{
		return f;
	}
	
	public G g()
	{
		return g;
	}
	
	public H h()
	{
		return h;
	}
	
	public I i()
	{
		return i;
	}
	
	public J j()
	{
		return j;
	}
	
	public Tuple1<A> strip9R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<J> strip9L()
	{
		return Tuples.immutable(j);
	}
	
	public Tuple2<A, B> strip8R()
	{
		return Tuples.immutable(a, b);
	}
	
	public Tuple2<I, J> strip8L()
	{
		return Tuples.immutable(i, j);
	}
	
	public Tuple3<A, B, C> strip7R()
	{
		return Tuples.immutable(a, b, c);
	}
	
	public Tuple3<H, I, J> strip7L()
	{
		return Tuples.immutable(h, i, j);
	}
	
	public Tuple4<A, B, C, D> strip6R()
	{
		return Tuples.immutable(a, b, c, d);
	}
	
	public Tuple4<G, H, I, J> strip6L()
	{
		return Tuples.immutable(g, h, i, j);
	}
	
	public Tuple5<A, B, C, D, E> strip5R()
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public Tuple5<F, G, H, I, J> strip5L()
	{
		return Tuples.immutable(f, g, h, i, j);
	}
	
	public Tuple6<A, B, C, D, E, F> strip4R()
	{
		return Tuples.immutable(a, b, c, d, e, f);
	}
	
	public Tuple6<E, F, G, H, I, J> strip4L()
	{
		return Tuples.immutable(e, f, g, h, i, j);
	}
	
	public Tuple7<A, B, C, D, E, F, G> strip3R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public Tuple7<D, E, F, G, H, I, J> strip3L()
	{
		return Tuples.immutable(d, e, f, g, h, i, j);
	}
	
	public Tuple8<A, B, C, D, E, F, G, H> strip2R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h);
	}
	
	public Tuple8<C, D, E, F, G, H, I, J> strip2L()
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j);
	}
	
	public Tuple9<A, B, C, D, E, F, G, H, I> strip1R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public Tuple9<B, C, D, E, F, G, H, I, J> strip1L()
	{
		return Tuples.immutable(b, c, d, e, f, g, h, i, j);
	}
	
	
	public <K> Tuple11<A, B, C, D, E, F, G, H, I, J, K> add(K k)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public <K, L> Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> add(K k, L l)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public <K, L, M> Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(K k, L l, M m)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public <K, L, M, N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(K k, L l, M m, N n)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <K, L, M, N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(K k, L l, M m, N n, O o)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <K, L, M, N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(K k, L l, M m, N n, O o, P p)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <K, L, M, N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(K k, L l, M m, N n, O o, P p, Q q)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <K, L, M, N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <K, L, M, N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public @Override int arity()
	{
		return 10;
	}
	
	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h, i, j);
	}
	
	public Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> mutable()
	{
		return new Tuple10.Mutable10<>(a, b, c, d, e, f, g, h, i, j);
	}
	
	public Tuple10<A, B, C, D, E, F, G, H, I, J> immutable()
	{
		return this;
	}
	
	public Tuple10<A, B, C, D, E, F, G, H, I, J> copy()
	{
		return new Tuple10<>(a, b, c, d, e, f, g, h, i, j);
	}
	
	public @Override String toString()
	{
		return "Tuple10" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}
	
	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h, i, j);
	}
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple10 tuple)) return false;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}
	
	public static class Mutable10<A, B, C, D, E, F, G, H, I, J>
			extends Tuple10<A, B, C, D, E, F, G, H, I, J>
	{
		public Mutable10(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
		{
			super(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> mutable()
		{
			return this;
		}
		
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> copy()
		{
			return new Tuple10.Mutable10<>(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override Tuple10<A, B, C, D, E, F, G, H, I, J> immutable()
		{
			return new Tuple10<>(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override String toString()
		{
			return "Tuple10.Mutable10" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setC(C c)
		{
			this.c = c;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setD(D d)
		{
			this.d = d;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setE(E e)
		{
			this.e = e;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setF(F f)
		{
			this.f = f;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setG(G g)
		{
			this.g = g;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setH(H h)
		{
			this.h = h;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setI(I i)
		{
			this.i = i;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setJ(J j)
		{
			this.j = j;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip9R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<J> strip9L()
		{
			return Tuples.mutable(j);
		}
		
		public @Override Tuple2.Mutable2<A, B> strip8R()
		{
			return Tuples.mutable(a, b);
		}
		
		public @Override Tuple2.Mutable2<I, J> strip8L()
		{
			return Tuples.mutable(i, j);
		}
		
		public @Override Tuple3.Mutable3<A, B, C> strip7R()
		{
			return Tuples.mutable(a, b, c);
		}
		
		public @Override Tuple3.Mutable3<H, I, J> strip7L()
		{
			return Tuples.mutable(h, i, j);
		}
		
		public @Override Tuple4.Mutable4<A, B, C, D> strip6R()
		{
			return Tuples.mutable(a, b, c, d);
		}
		
		public @Override Tuple4.Mutable4<G, H, I, J> strip6L()
		{
			return Tuples.mutable(g, h, i, j);
		}
		
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip5R()
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override Tuple5.Mutable5<F, G, H, I, J> strip5L()
		{
			return Tuples.mutable(f, g, h, i, j);
		}
		
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip4R()
		{
			return Tuples.mutable(a, b, c, d, e, f);
		}
		
		public @Override Tuple6.Mutable6<E, F, G, H, I, J> strip4L()
		{
			return Tuples.mutable(e, f, g, h, i, j);
		}
		
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip3R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override Tuple7.Mutable7<D, E, F, G, H, I, J> strip3L()
		{
			return Tuples.mutable(d, e, f, g, h, i, j);
		}
		
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip2R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h);
		}
		
		public @Override Tuple8.Mutable8<C, D, E, F, G, H, I, J> strip2L()
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j);
		}
		
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip1R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override Tuple9.Mutable9<B, C, D, E, F, G, H, I, J> strip1L()
		{
			return Tuples.mutable(b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K> Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> add(K k)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override <K, L> Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> add(K k, L l)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override <K, L, M> Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(K k, L l, M m)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override <K, L, M, N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(K k, L l, M m, N n)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override <K, L, M, N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(K k, L l, M m, N n, O o)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override <K, L, M, N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(K k, L l, M m, N n, O o, P p)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <K, L, M, N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(K k, L l, M m, N n, O o, P p, Q q)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override <K, L, M, N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(K k, L l, M m, N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
	}
}