package org.zeith.hammerlib.util.java.tuples;

import java.util.stream.Stream;
import java.util.stream.Collectors;

public class Tuple11<A, B, C, D, E, F, G, H, I, J, K> implements ITuple
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
	
	public Tuple11(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
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
	}

	public A a() { return a; }
	public B b() { return b; }
	public C c() { return c; }
	public D d() { return d; }
	public E e() { return e; }
	public F f() { return f; }
	public G g() { return g; }
	public H h() { return h; }
	public I i() { return i; }
	public J j() { return j; }
	public K k() { return k; }
	
	public Tuple1<A> strip10R() { return Tuples.immutable(a); }
	public Tuple1<K> strip10L() { return Tuples.immutable(k); }

	public Tuple2<A, B> strip9R() { return Tuples.immutable(a, b); }
	public Tuple2<J, K> strip9L() { return Tuples.immutable(j, k); }

	public Tuple3<A, B, C> strip8R() { return Tuples.immutable(a, b, c); }
	public Tuple3<I, J, K> strip8L() { return Tuples.immutable(i, j, k); }

	public Tuple4<A, B, C, D> strip7R() { return Tuples.immutable(a, b, c, d); }
	public Tuple4<H, I, J, K> strip7L() { return Tuples.immutable(h, i, j, k); }

	public Tuple5<A, B, C, D, E> strip6R() { return Tuples.immutable(a, b, c, d, e); }
	public Tuple5<G, H, I, J, K> strip6L() { return Tuples.immutable(g, h, i, j, k); }

	public Tuple6<A, B, C, D, E, F> strip5R() { return Tuples.immutable(a, b, c, d, e, f); }
	public Tuple6<F, G, H, I, J, K> strip5L() { return Tuples.immutable(f, g, h, i, j, k); }

	public Tuple7<A, B, C, D, E, F, G> strip4R() { return Tuples.immutable(a, b, c, d, e, f, g); }
	public Tuple7<E, F, G, H, I, J, K> strip4L() { return Tuples.immutable(e, f, g, h, i, j, k); }

	public Tuple8<A, B, C, D, E, F, G, H> strip3R() { return Tuples.immutable(a, b, c, d, e, f, g, h); }
	public Tuple8<D, E, F, G, H, I, J, K> strip3L() { return Tuples.immutable(d, e, f, g, h, i, j, k); }

	public Tuple9<A, B, C, D, E, F, G, H, I> strip2R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i); }
	public Tuple9<C, D, E, F, G, H, I, J, K> strip2L() { return Tuples.immutable(c, d, e, f, g, h, i, j, k); }

	public Tuple10<A, B, C, D, E, F, G, H, I, J> strip1R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j); }
	public Tuple10<B, C, D, E, F, G, H, I, J, K> strip1L() { return Tuples.immutable(b, c, d, e, f, g, h, i, j, k); }

	
	public <L> Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> add(L l) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l); }
	public <L, M> Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(L l, M m) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
	public <L, M, N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(L l, M m, N n) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
	public <L, M, N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(L l, M m, N n, O o) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
	public <L, M, N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(L l, M m, N n, O o, P p) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
	public <L, M, N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(L l, M m, N n, O o, P p, Q q) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
	public <L, M, N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(L l, M m, N n, O o, P p, Q q, R r) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
	public <L, M, N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(L l, M m, N n, O o, P p, Q q, R r, S s) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
	public <L, M, N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
	public <L, M, N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
	public <L, M, N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
	public <L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
	public <L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
	public <L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
	public <L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	
	public @Override int arity()
	{
		return 11;
	}

	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h, i, j, k);
	}

	public Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> mutable()
	{
		return new Tuple11.Mutable11<>(a, b, c, d, e, f, g, h, i, j, k);
	}

	public Tuple11<A, B, C, D, E, F, G, H, I, J, K> immutable()
	{
		return this;
	}

	public Tuple11<A, B, C, D, E, F, G, H, I, J, K> copy()
	{
		return new Tuple11<>(a, b, c, d, e, f, g, h, i, j, k);
	}

	public @Override String toString()
	{
		return "Tuple11" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}

	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h, i, j, k);
	}

	public @Override @SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple11 tuple)) return false;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}

	public static class Mutable11<A, B, C, D, E, F, G, H, I, J, K>
			extends Tuple11<A, B, C, D, E, F, G, H, I, J, K>
	{
		public Mutable11(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
		{
			super(a, b, c, d, e, f, g, h, i, j, k);
		}

		public @Override Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> mutable()
		{
			return this;
		}

		public @Override Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> copy()
		{
		    return new Tuple11.Mutable11<>(a, b, c, d, e, f, g, h, i, j, k);
		}

		public @Override Tuple11<A, B, C, D, E, F, G, H, I, J, K> immutable()
		{
			return new Tuple11<>(a, b, c, d, e, f, g, h, i, j, k);
		}

		public @Override String toString()
 	    {
 	        return "Tuple11.Mutable11" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
 	    }

		public Mutable11<A, B, C, D, E, F, G, H, I, J, K> setA(A a) { this.a = a; return this; }
		public Mutable11<A, B, C, D, E, F, G, H, I, J, K> setB(B b) { this.b = b; return this; }
		public Mutable11<A, B, C, D, E, F, G, H, I, J, K> setC(C c) { this.c = c; return this; }
		public Mutable11<A, B, C, D, E, F, G, H, I, J, K> setD(D d) { this.d = d; return this; }
		public Mutable11<A, B, C, D, E, F, G, H, I, J, K> setE(E e) { this.e = e; return this; }
		public Mutable11<A, B, C, D, E, F, G, H, I, J, K> setF(F f) { this.f = f; return this; }
		public Mutable11<A, B, C, D, E, F, G, H, I, J, K> setG(G g) { this.g = g; return this; }
		public Mutable11<A, B, C, D, E, F, G, H, I, J, K> setH(H h) { this.h = h; return this; }
		public Mutable11<A, B, C, D, E, F, G, H, I, J, K> setI(I i) { this.i = i; return this; }
		public Mutable11<A, B, C, D, E, F, G, H, I, J, K> setJ(J j) { this.j = j; return this; }
		public Mutable11<A, B, C, D, E, F, G, H, I, J, K> setK(K k) { this.k = k; return this; }
	
		public @Override Tuple1.Mutable1<A> strip10R() { return Tuples.mutable(a); }
		public @Override Tuple1.Mutable1<K> strip10L() { return Tuples.mutable(k); }
		public @Override Tuple2.Mutable2<A, B> strip9R() { return Tuples.mutable(a, b); }
		public @Override Tuple2.Mutable2<J, K> strip9L() { return Tuples.mutable(j, k); }
		public @Override Tuple3.Mutable3<A, B, C> strip8R() { return Tuples.mutable(a, b, c); }
		public @Override Tuple3.Mutable3<I, J, K> strip8L() { return Tuples.mutable(i, j, k); }
		public @Override Tuple4.Mutable4<A, B, C, D> strip7R() { return Tuples.mutable(a, b, c, d); }
		public @Override Tuple4.Mutable4<H, I, J, K> strip7L() { return Tuples.mutable(h, i, j, k); }
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip6R() { return Tuples.mutable(a, b, c, d, e); }
		public @Override Tuple5.Mutable5<G, H, I, J, K> strip6L() { return Tuples.mutable(g, h, i, j, k); }
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip5R() { return Tuples.mutable(a, b, c, d, e, f); }
		public @Override Tuple6.Mutable6<F, G, H, I, J, K> strip5L() { return Tuples.mutable(f, g, h, i, j, k); }
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip4R() { return Tuples.mutable(a, b, c, d, e, f, g); }
		public @Override Tuple7.Mutable7<E, F, G, H, I, J, K> strip4L() { return Tuples.mutable(e, f, g, h, i, j, k); }
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip3R() { return Tuples.mutable(a, b, c, d, e, f, g, h); }
		public @Override Tuple8.Mutable8<D, E, F, G, H, I, J, K> strip3L() { return Tuples.mutable(d, e, f, g, h, i, j, k); }
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip2R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i); }
		public @Override Tuple9.Mutable9<C, D, E, F, G, H, I, J, K> strip2L() { return Tuples.mutable(c, d, e, f, g, h, i, j, k); }
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> strip1R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j); }
		public @Override Tuple10.Mutable10<B, C, D, E, F, G, H, I, J, K> strip1L() { return Tuples.mutable(b, c, d, e, f, g, h, i, j, k); }
	
		public @Override <L> Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> add(L l) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l); }
		public @Override <L, M> Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(L l, M m) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
		public @Override <L, M, N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(L l, M m, N n) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
		public @Override <L, M, N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(L l, M m, N n, O o) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
		public @Override <L, M, N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(L l, M m, N n, O o, P p) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
		public @Override <L, M, N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(L l, M m, N n, O o, P p, Q q) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
		public @Override <L, M, N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(L l, M m, N n, O o, P p, Q q, R r) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
		public @Override <L, M, N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(L l, M m, N n, O o, P p, Q q, R r, S s) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
		public @Override <L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
		public @Override <L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
		public @Override <L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
		public @Override <L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
		public @Override <L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override <L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
		public @Override <L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	}
}