package org.zeith.hammerlib.util.java.tuples;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>
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
	protected K k;
	protected L l;
	protected M m;
	
	public Tuple13(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
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
		this.k = k;
		this.l = l;
		this.m = m;
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
	
	public K k()
	{
		return k;
	}
	
	public L l()
	{
		return l;
	}
	
	public M m()
	{
		return m;
	}
	
	public Tuple1<A> strip12R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<M> strip12L()
	{
		return Tuples.immutable(m);
	}
	
	public Tuple2<A, B> strip11R()
	{
		return Tuples.immutable(a, b);
	}
	
	public Tuple2<L, M> strip11L()
	{
		return Tuples.immutable(l, m);
	}
	
	public Tuple3<A, B, C> strip10R()
	{
		return Tuples.immutable(a, b, c);
	}
	
	public Tuple3<K, L, M> strip10L()
	{
		return Tuples.immutable(k, l, m);
	}
	
	public Tuple4<A, B, C, D> strip9R()
	{
		return Tuples.immutable(a, b, c, d);
	}
	
	public Tuple4<J, K, L, M> strip9L()
	{
		return Tuples.immutable(j, k, l, m);
	}
	
	public Tuple5<A, B, C, D, E> strip8R()
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public Tuple5<I, J, K, L, M> strip8L()
	{
		return Tuples.immutable(i, j, k, l, m);
	}
	
	public Tuple6<A, B, C, D, E, F> strip7R()
	{
		return Tuples.immutable(a, b, c, d, e, f);
	}
	
	public Tuple6<H, I, J, K, L, M> strip7L()
	{
		return Tuples.immutable(h, i, j, k, l, m);
	}
	
	public Tuple7<A, B, C, D, E, F, G> strip6R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public Tuple7<G, H, I, J, K, L, M> strip6L()
	{
		return Tuples.immutable(g, h, i, j, k, l, m);
	}
	
	public Tuple8<A, B, C, D, E, F, G, H> strip5R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h);
	}
	
	public Tuple8<F, G, H, I, J, K, L, M> strip5L()
	{
		return Tuples.immutable(f, g, h, i, j, k, l, m);
	}
	
	public Tuple9<A, B, C, D, E, F, G, H, I> strip4R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public Tuple9<E, F, G, H, I, J, K, L, M> strip4L()
	{
		return Tuples.immutable(e, f, g, h, i, j, k, l, m);
	}
	
	public Tuple10<A, B, C, D, E, F, G, H, I, J> strip3R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j);
	}
	
	public Tuple10<D, E, F, G, H, I, J, K, L, M> strip3L()
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m);
	}
	
	public Tuple11<A, B, C, D, E, F, G, H, I, J, K> strip2R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public Tuple11<C, D, E, F, G, H, I, J, K, L, M> strip2L()
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> strip1R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public Tuple12<B, C, D, E, F, G, H, I, J, K, L, M> strip1L()
	{
		return Tuples.immutable(b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	
	public <N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(N n)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(N n, O o)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(N n, O o, P p)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(N n, O o, P p, Q q)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public @Override int arity()
	{
		return 13;
	}
	
	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> mutable()
	{
		return new Tuple13.Mutable13<>(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> immutable()
	{
		return this;
	}
	
	public Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> copy()
	{
		return new Tuple13<>(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public @Override String toString()
	{
		return "Tuple13" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}
	
	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple13 tuple)) return false;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}
	
	public static class Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M>
			extends Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>
	{
		public Mutable13(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
		{
			super(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> mutable()
		{
			return this;
		}
		
		public @Override Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> copy()
		{
			return new Tuple13.Mutable13<>(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> immutable()
		{
			return new Tuple13<>(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override String toString()
		{
			return "Tuple13.Mutable13" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setC(C c)
		{
			this.c = c;
			return this;
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setD(D d)
		{
			this.d = d;
			return this;
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setE(E e)
		{
			this.e = e;
			return this;
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setF(F f)
		{
			this.f = f;
			return this;
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setG(G g)
		{
			this.g = g;
			return this;
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setH(H h)
		{
			this.h = h;
			return this;
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setI(I i)
		{
			this.i = i;
			return this;
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setJ(J j)
		{
			this.j = j;
			return this;
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setK(K k)
		{
			this.k = k;
			return this;
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setL(L l)
		{
			this.l = l;
			return this;
		}
		
		public Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> setM(M m)
		{
			this.m = m;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip12R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<M> strip12L()
		{
			return Tuples.mutable(m);
		}
		
		public @Override Tuple2.Mutable2<A, B> strip11R()
		{
			return Tuples.mutable(a, b);
		}
		
		public @Override Tuple2.Mutable2<L, M> strip11L()
		{
			return Tuples.mutable(l, m);
		}
		
		public @Override Tuple3.Mutable3<A, B, C> strip10R()
		{
			return Tuples.mutable(a, b, c);
		}
		
		public @Override Tuple3.Mutable3<K, L, M> strip10L()
		{
			return Tuples.mutable(k, l, m);
		}
		
		public @Override Tuple4.Mutable4<A, B, C, D> strip9R()
		{
			return Tuples.mutable(a, b, c, d);
		}
		
		public @Override Tuple4.Mutable4<J, K, L, M> strip9L()
		{
			return Tuples.mutable(j, k, l, m);
		}
		
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip8R()
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override Tuple5.Mutable5<I, J, K, L, M> strip8L()
		{
			return Tuples.mutable(i, j, k, l, m);
		}
		
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip7R()
		{
			return Tuples.mutable(a, b, c, d, e, f);
		}
		
		public @Override Tuple6.Mutable6<H, I, J, K, L, M> strip7L()
		{
			return Tuples.mutable(h, i, j, k, l, m);
		}
		
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip6R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override Tuple7.Mutable7<G, H, I, J, K, L, M> strip6L()
		{
			return Tuples.mutable(g, h, i, j, k, l, m);
		}
		
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip5R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h);
		}
		
		public @Override Tuple8.Mutable8<F, G, H, I, J, K, L, M> strip5L()
		{
			return Tuples.mutable(f, g, h, i, j, k, l, m);
		}
		
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip4R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override Tuple9.Mutable9<E, F, G, H, I, J, K, L, M> strip4L()
		{
			return Tuples.mutable(e, f, g, h, i, j, k, l, m);
		}
		
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> strip3R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override Tuple10.Mutable10<D, E, F, G, H, I, J, K, L, M> strip3L()
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> strip2R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override Tuple11.Mutable11<C, D, E, F, G, H, I, J, K, L, M> strip2L()
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> strip1R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override Tuple12.Mutable12<B, C, D, E, F, G, H, I, J, K, L, M> strip1L()
		{
			return Tuples.mutable(b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override <N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(N n)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override <N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(N n, O o)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override <N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(N n, O o, P p)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(N n, O o, P p, Q q)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override <N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override <N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override <N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override <N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override <N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override <N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
	}
}