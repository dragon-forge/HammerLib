package org.zeith.hammerlib.util.java.tuples;

import java.util.stream.Stream;
import java.util.stream.Collectors;

public class Tuple7<A, B, C, D, E, F, G> implements ITuple
{
	protected A a;
	protected B b;
	protected C c;
	protected D d;
	protected E e;
	protected F f;
	protected G g;
	
	public Tuple7(A a, B b, C c, D d, E e, F f, G g)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.g = g;
	}

	public A a() { return a; }
	public B b() { return b; }
	public C c() { return c; }
	public D d() { return d; }
	public E e() { return e; }
	public F f() { return f; }
	public G g() { return g; }
	
	public Tuple1<A> strip6R() { return Tuples.immutable(a); }
	public Tuple1<G> strip6L() { return Tuples.immutable(g); }

	public Tuple2<A, B> strip5R() { return Tuples.immutable(a, b); }
	public Tuple2<F, G> strip5L() { return Tuples.immutable(f, g); }

	public Tuple3<A, B, C> strip4R() { return Tuples.immutable(a, b, c); }
	public Tuple3<E, F, G> strip4L() { return Tuples.immutable(e, f, g); }

	public Tuple4<A, B, C, D> strip3R() { return Tuples.immutable(a, b, c, d); }
	public Tuple4<D, E, F, G> strip3L() { return Tuples.immutable(d, e, f, g); }

	public Tuple5<A, B, C, D, E> strip2R() { return Tuples.immutable(a, b, c, d, e); }
	public Tuple5<C, D, E, F, G> strip2L() { return Tuples.immutable(c, d, e, f, g); }

	public Tuple6<A, B, C, D, E, F> strip1R() { return Tuples.immutable(a, b, c, d, e, f); }
	public Tuple6<B, C, D, E, F, G> strip1L() { return Tuples.immutable(b, c, d, e, f, g); }

	
	public <H> Tuple8<A, B, C, D, E, F, G, H> add(H h) { return Tuples.immutable(a, b, c, d, e, f, g, h); }
	public <H, I> Tuple9<A, B, C, D, E, F, G, H, I> add(H h, I i) { return Tuples.immutable(a, b, c, d, e, f, g, h, i); }
	public <H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(H h, I i, J j) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j); }
	public <H, I, J, K> Tuple11<A, B, C, D, E, F, G, H, I, J, K> add(H h, I i, J j, K k) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k); }
	public <H, I, J, K, L> Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> add(H h, I i, J j, K k, L l) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l); }
	public <H, I, J, K, L, M> Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(H h, I i, J j, K k, L l, M m) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
	public <H, I, J, K, L, M, N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(H h, I i, J j, K k, L l, M m, N n) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
	public <H, I, J, K, L, M, N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(H h, I i, J j, K k, L l, M m, N n, O o) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
	public <H, I, J, K, L, M, N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(H h, I i, J j, K k, L l, M m, N n, O o, P p) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
	public <H, I, J, K, L, M, N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
	public <H, I, J, K, L, M, N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
	public <H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
	public <H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
	public <H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
	public <H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
	public <H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
	public <H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
	public <H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
	public <H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	
	public @Override int arity()
	{
		return 7;
	}

	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g);
	}

	public Tuple7.Mutable7<A, B, C, D, E, F, G> mutable()
	{
		return new Tuple7.Mutable7<>(a, b, c, d, e, f, g);
	}

	public Tuple7<A, B, C, D, E, F, G> immutable()
	{
		return this;
	}

	public Tuple7<A, B, C, D, E, F, G> copy()
	{
		return new Tuple7<>(a, b, c, d, e, f, g);
	}

	public @Override String toString()
	{
		return "Tuple7" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}

	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g);
	}

	public @Override @SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple7 tuple)) return false;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}

	public static class Mutable7<A, B, C, D, E, F, G>
			extends Tuple7<A, B, C, D, E, F, G>
	{
		public Mutable7(A a, B b, C c, D d, E e, F f, G g)
		{
			super(a, b, c, d, e, f, g);
		}

		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> mutable()
		{
			return this;
		}

		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> copy()
		{
		    return new Tuple7.Mutable7<>(a, b, c, d, e, f, g);
		}

		public @Override Tuple7<A, B, C, D, E, F, G> immutable()
		{
			return new Tuple7<>(a, b, c, d, e, f, g);
		}

		public @Override String toString()
 	    {
 	        return "Tuple7.Mutable7" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
 	    }

		public Mutable7<A, B, C, D, E, F, G> setA(A a) { this.a = a; return this; }
		public Mutable7<A, B, C, D, E, F, G> setB(B b) { this.b = b; return this; }
		public Mutable7<A, B, C, D, E, F, G> setC(C c) { this.c = c; return this; }
		public Mutable7<A, B, C, D, E, F, G> setD(D d) { this.d = d; return this; }
		public Mutable7<A, B, C, D, E, F, G> setE(E e) { this.e = e; return this; }
		public Mutable7<A, B, C, D, E, F, G> setF(F f) { this.f = f; return this; }
		public Mutable7<A, B, C, D, E, F, G> setG(G g) { this.g = g; return this; }
	
		public @Override Tuple1.Mutable1<A> strip6R() { return Tuples.mutable(a); }
		public @Override Tuple1.Mutable1<G> strip6L() { return Tuples.mutable(g); }
		public @Override Tuple2.Mutable2<A, B> strip5R() { return Tuples.mutable(a, b); }
		public @Override Tuple2.Mutable2<F, G> strip5L() { return Tuples.mutable(f, g); }
		public @Override Tuple3.Mutable3<A, B, C> strip4R() { return Tuples.mutable(a, b, c); }
		public @Override Tuple3.Mutable3<E, F, G> strip4L() { return Tuples.mutable(e, f, g); }
		public @Override Tuple4.Mutable4<A, B, C, D> strip3R() { return Tuples.mutable(a, b, c, d); }
		public @Override Tuple4.Mutable4<D, E, F, G> strip3L() { return Tuples.mutable(d, e, f, g); }
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip2R() { return Tuples.mutable(a, b, c, d, e); }
		public @Override Tuple5.Mutable5<C, D, E, F, G> strip2L() { return Tuples.mutable(c, d, e, f, g); }
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip1R() { return Tuples.mutable(a, b, c, d, e, f); }
		public @Override Tuple6.Mutable6<B, C, D, E, F, G> strip1L() { return Tuples.mutable(b, c, d, e, f, g); }
	
		public @Override <H> Tuple8.Mutable8<A, B, C, D, E, F, G, H> add(H h) { return Tuples.mutable(a, b, c, d, e, f, g, h); }
		public @Override <H, I> Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> add(H h, I i) { return Tuples.mutable(a, b, c, d, e, f, g, h, i); }
		public @Override <H, I, J> Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> add(H h, I i, J j) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j); }
		public @Override <H, I, J, K> Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> add(H h, I i, J j, K k) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k); }
		public @Override <H, I, J, K, L> Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> add(H h, I i, J j, K k, L l) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l); }
		public @Override <H, I, J, K, L, M> Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(H h, I i, J j, K k, L l, M m) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
		public @Override <H, I, J, K, L, M, N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(H h, I i, J j, K k, L l, M m, N n) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
		public @Override <H, I, J, K, L, M, N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(H h, I i, J j, K k, L l, M m, N n, O o) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
		public @Override <H, I, J, K, L, M, N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(H h, I i, J j, K k, L l, M m, N n, O o, P p) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
		public @Override <H, I, J, K, L, M, N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
		public @Override <H, I, J, K, L, M, N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
		public @Override <H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
		public @Override <H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
		public @Override <H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
		public @Override <H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
		public @Override <H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
		public @Override <H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override <H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
		public @Override <H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	}
}