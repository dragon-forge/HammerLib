package org.zeith.hammerlib.util.java.tuples;

import java.util.stream.Stream;
import java.util.stream.Collectors;

public class Tuple1<A> implements ITuple
{
	protected A a;
	
	public Tuple1(A a)
	{
		this.a = a;
	}

	public A a() { return a; }
	
	
	public <B> Tuple2<A, B> add(B b) { return Tuples.immutable(a, b); }
	public <B, C> Tuple3<A, B, C> add(B b, C c) { return Tuples.immutable(a, b, c); }
	public <B, C, D> Tuple4<A, B, C, D> add(B b, C c, D d) { return Tuples.immutable(a, b, c, d); }
	public <B, C, D, E> Tuple5<A, B, C, D, E> add(B b, C c, D d, E e) { return Tuples.immutable(a, b, c, d, e); }
	public <B, C, D, E, F> Tuple6<A, B, C, D, E, F> add(B b, C c, D d, E e, F f) { return Tuples.immutable(a, b, c, d, e, f); }
	public <B, C, D, E, F, G> Tuple7<A, B, C, D, E, F, G> add(B b, C c, D d, E e, F f, G g) { return Tuples.immutable(a, b, c, d, e, f, g); }
	public <B, C, D, E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> add(B b, C c, D d, E e, F f, G g, H h) { return Tuples.immutable(a, b, c, d, e, f, g, h); }
	public <B, C, D, E, F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> add(B b, C c, D d, E e, F f, G g, H h, I i) { return Tuples.immutable(a, b, c, d, e, f, g, h, i); }
	public <B, C, D, E, F, G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(B b, C c, D d, E e, F f, G g, H h, I i, J j) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j); }
	public <B, C, D, E, F, G, H, I, J, K> Tuple11<A, B, C, D, E, F, G, H, I, J, K> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k); }
	public <B, C, D, E, F, G, H, I, J, K, L> Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l); }
	public <B, C, D, E, F, G, H, I, J, K, L, M> Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
	public <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	
	public @Override int arity()
	{
		return 1;
	}

	public @Override Stream<?> stream()
	{
		return Stream.of(a);
	}

	public Tuple1.Mutable1<A> mutable()
	{
		return new Tuple1.Mutable1<>(a);
	}

	public Tuple1<A> immutable()
	{
		return this;
	}

	public Tuple1<A> copy()
	{
		return new Tuple1<>(a);
	}

	public @Override String toString()
	{
		return "Tuple1" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}

	public @Override int hashCode()
	{
		return java.util.Objects.hash(a);
	}

	public @Override @SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple1 tuple)) return false;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}

	public static class Mutable1<A>
			extends Tuple1<A>
	{
		public Mutable1(A a)
		{
			super(a);
		}

		public @Override Tuple1.Mutable1<A> mutable()
		{
			return this;
		}

		public @Override Tuple1.Mutable1<A> copy()
		{
		    return new Tuple1.Mutable1<>(a);
		}

		public @Override Tuple1<A> immutable()
		{
			return new Tuple1<>(a);
		}

		public @Override String toString()
 	    {
 	        return "Tuple1.Mutable1" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
 	    }

		public Mutable1<A> setA(A a) { this.a = a; return this; }
	
	
		public @Override <B> Tuple2.Mutable2<A, B> add(B b) { return Tuples.mutable(a, b); }
		public @Override <B, C> Tuple3.Mutable3<A, B, C> add(B b, C c) { return Tuples.mutable(a, b, c); }
		public @Override <B, C, D> Tuple4.Mutable4<A, B, C, D> add(B b, C c, D d) { return Tuples.mutable(a, b, c, d); }
		public @Override <B, C, D, E> Tuple5.Mutable5<A, B, C, D, E> add(B b, C c, D d, E e) { return Tuples.mutable(a, b, c, d, e); }
		public @Override <B, C, D, E, F> Tuple6.Mutable6<A, B, C, D, E, F> add(B b, C c, D d, E e, F f) { return Tuples.mutable(a, b, c, d, e, f); }
		public @Override <B, C, D, E, F, G> Tuple7.Mutable7<A, B, C, D, E, F, G> add(B b, C c, D d, E e, F f, G g) { return Tuples.mutable(a, b, c, d, e, f, g); }
		public @Override <B, C, D, E, F, G, H> Tuple8.Mutable8<A, B, C, D, E, F, G, H> add(B b, C c, D d, E e, F f, G g, H h) { return Tuples.mutable(a, b, c, d, e, f, g, h); }
		public @Override <B, C, D, E, F, G, H, I> Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> add(B b, C c, D d, E e, F f, G g, H h, I i) { return Tuples.mutable(a, b, c, d, e, f, g, h, i); }
		public @Override <B, C, D, E, F, G, H, I, J> Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> add(B b, C c, D d, E e, F f, G g, H h, I i, J j) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j); }
		public @Override <B, C, D, E, F, G, H, I, J, K> Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L> Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M> Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
		public @Override <B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	}
}