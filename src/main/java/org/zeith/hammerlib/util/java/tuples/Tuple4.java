package org.zeith.hammerlib.util.java.tuples;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple4<A, B, C, D>
		implements ITuple
{
	protected A a;
	protected B b;
	protected C c;
	protected D d;
	
	public Tuple4(A a, B b, C c, D d)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
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
	
	public Tuple1<A> strip3R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<D> strip3L()
	{
		return Tuples.immutable(d);
	}
	
	public Tuple2<A, B> strip2R()
	{
		return Tuples.immutable(a, b);
	}
	
	public Tuple2<C, D> strip2L()
	{
		return Tuples.immutable(c, d);
	}
	
	public Tuple3<A, B, C> strip1R()
	{
		return Tuples.immutable(a, b, c);
	}
	
	public Tuple3<B, C, D> strip1L()
	{
		return Tuples.immutable(b, c, d);
	}
	
	
	public <E> Tuple5<A, B, C, D, E> add(E e)
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public <E, F> Tuple6<A, B, C, D, E, F> add(E e, F f)
	{
		return Tuples.immutable(a, b, c, d, e, f);
	}
	
	public <E, F, G> Tuple7<A, B, C, D, E, F, G> add(E e, F f, G g)
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public <E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> add(E e, F f, G g, H h)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h);
	}
	
	public <E, F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> add(E e, F f, G g, H h, I i)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public <E, F, G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(E e, F f, G g, H h, I i, J j)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j);
	}
	
	public <E, F, G, H, I, J, K> Tuple11<A, B, C, D, E, F, G, H, I, J, K> add(E e, F f, G g, H h, I i, J j, K k)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public <E, F, G, H, I, J, K, L> Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> add(E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public <E, F, G, H, I, J, K, L, M> Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public <E, F, G, H, I, J, K, L, M, N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <E, F, G, H, I, J, K, L, M, N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <E, F, G, H, I, J, K, L, M, N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public @Override int arity()
	{
		return 4;
	}
	
	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d);
	}
	
	public Tuple4.Mutable4<A, B, C, D> mutable()
	{
		return new Tuple4.Mutable4<>(a, b, c, d);
	}
	
	public Tuple4<A, B, C, D> immutable()
	{
		return this;
	}
	
	public Tuple4<A, B, C, D> copy()
	{
		return new Tuple4<>(a, b, c, d);
	}
	
	public @Override String toString()
	{
		return "Tuple4" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}
	
	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d);
	}
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple4 tuple)) return false;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}
	
	public static class Mutable4<A, B, C, D>
			extends Tuple4<A, B, C, D>
	{
		public Mutable4(A a, B b, C c, D d)
		{
			super(a, b, c, d);
		}
		
		public @Override Tuple4.Mutable4<A, B, C, D> mutable()
		{
			return this;
		}
		
		public @Override Tuple4.Mutable4<A, B, C, D> copy()
		{
			return new Tuple4.Mutable4<>(a, b, c, d);
		}
		
		public @Override Tuple4<A, B, C, D> immutable()
		{
			return new Tuple4<>(a, b, c, d);
		}
		
		public @Override String toString()
		{
			return "Tuple4.Mutable4" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
		}
		
		public Mutable4<A, B, C, D> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable4<A, B, C, D> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public Mutable4<A, B, C, D> setC(C c)
		{
			this.c = c;
			return this;
		}
		
		public Mutable4<A, B, C, D> setD(D d)
		{
			this.d = d;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip3R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<D> strip3L()
		{
			return Tuples.mutable(d);
		}
		
		public @Override Tuple2.Mutable2<A, B> strip2R()
		{
			return Tuples.mutable(a, b);
		}
		
		public @Override Tuple2.Mutable2<C, D> strip2L()
		{
			return Tuples.mutable(c, d);
		}
		
		public @Override Tuple3.Mutable3<A, B, C> strip1R()
		{
			return Tuples.mutable(a, b, c);
		}
		
		public @Override Tuple3.Mutable3<B, C, D> strip1L()
		{
			return Tuples.mutable(b, c, d);
		}
		
		public @Override <E> Tuple5.Mutable5<A, B, C, D, E> add(E e)
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override <E, F> Tuple6.Mutable6<A, B, C, D, E, F> add(E e, F f)
		{
			return Tuples.mutable(a, b, c, d, e, f);
		}
		
		public @Override <E, F, G> Tuple7.Mutable7<A, B, C, D, E, F, G> add(E e, F f, G g)
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override <E, F, G, H> Tuple8.Mutable8<A, B, C, D, E, F, G, H> add(E e, F f, G g, H h)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h);
		}
		
		public @Override <E, F, G, H, I> Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> add(E e, F f, G g, H h, I i)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override <E, F, G, H, I, J> Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> add(E e, F f, G g, H h, I i, J j)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <E, F, G, H, I, J, K> Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> add(E e, F f, G g, H h, I i, J j, K k)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override <E, F, G, H, I, J, K, L> Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> add(E e, F f, G g, H h, I i, J j, K k, L l)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M> Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(E e, F f, G g, H h, I i, J j, K k, L l, M m)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override <E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
	}
}