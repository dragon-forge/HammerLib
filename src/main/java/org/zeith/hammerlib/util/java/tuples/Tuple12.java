package org.zeith.hammerlib.util.java.tuples;

import java.util.stream.Stream;
import java.util.stream.Collectors;

public class Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> implements ITuple
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
	
	public Tuple12(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
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
	public L l() { return l; }
	
	public Tuple1<A> strip11R() { return Tuples.immutable(a); }
	public Tuple1<L> strip11L() { return Tuples.immutable(l); }

	public Tuple2<A, B> strip10R() { return Tuples.immutable(a, b); }
	public Tuple2<K, L> strip10L() { return Tuples.immutable(k, l); }

	public Tuple3<A, B, C> strip9R() { return Tuples.immutable(a, b, c); }
	public Tuple3<J, K, L> strip9L() { return Tuples.immutable(j, k, l); }

	public Tuple4<A, B, C, D> strip8R() { return Tuples.immutable(a, b, c, d); }
	public Tuple4<I, J, K, L> strip8L() { return Tuples.immutable(i, j, k, l); }

	public Tuple5<A, B, C, D, E> strip7R() { return Tuples.immutable(a, b, c, d, e); }
	public Tuple5<H, I, J, K, L> strip7L() { return Tuples.immutable(h, i, j, k, l); }

	public Tuple6<A, B, C, D, E, F> strip6R() { return Tuples.immutable(a, b, c, d, e, f); }
	public Tuple6<G, H, I, J, K, L> strip6L() { return Tuples.immutable(g, h, i, j, k, l); }

	public Tuple7<A, B, C, D, E, F, G> strip5R() { return Tuples.immutable(a, b, c, d, e, f, g); }
	public Tuple7<F, G, H, I, J, K, L> strip5L() { return Tuples.immutable(f, g, h, i, j, k, l); }

	public Tuple8<A, B, C, D, E, F, G, H> strip4R() { return Tuples.immutable(a, b, c, d, e, f, g, h); }
	public Tuple8<E, F, G, H, I, J, K, L> strip4L() { return Tuples.immutable(e, f, g, h, i, j, k, l); }

	public Tuple9<A, B, C, D, E, F, G, H, I> strip3R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i); }
	public Tuple9<D, E, F, G, H, I, J, K, L> strip3L() { return Tuples.immutable(d, e, f, g, h, i, j, k, l); }

	public Tuple10<A, B, C, D, E, F, G, H, I, J> strip2R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j); }
	public Tuple10<C, D, E, F, G, H, I, J, K, L> strip2L() { return Tuples.immutable(c, d, e, f, g, h, i, j, k, l); }

	public Tuple11<A, B, C, D, E, F, G, H, I, J, K> strip1R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k); }
	public Tuple11<B, C, D, E, F, G, H, I, J, K, L> strip1L() { return Tuples.immutable(b, c, d, e, f, g, h, i, j, k, l); }

	
	public <M> Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(M m) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
	public <M, N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(M m, N n) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
	public <M, N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(M m, N n, O o) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
	public <M, N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(M m, N n, O o, P p) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
	public <M, N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(M m, N n, O o, P p, Q q) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
	public <M, N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(M m, N n, O o, P p, Q q, R r) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
	public <M, N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(M m, N n, O o, P p, Q q, R r, S s) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
	public <M, N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(M m, N n, O o, P p, Q q, R r, S s, T t) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
	public <M, N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(M m, N n, O o, P p, Q q, R r, S s, T t, U u) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
	public <M, N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
	public <M, N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
	public <M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
	public <M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
	public <M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	
	public @Override int arity()
	{
		return 12;
	}

	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l);
	}

	public Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> mutable()
	{
		return new Tuple12.Mutable12<>(a, b, c, d, e, f, g, h, i, j, k, l);
	}

	public Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> immutable()
	{
		return this;
	}

	public Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> copy()
	{
		return new Tuple12<>(a, b, c, d, e, f, g, h, i, j, k, l);
	}

	public @Override String toString()
	{
		return "Tuple12" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}

	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h, i, j, k, l);
	}

	public @Override @SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple12 tuple)) return false;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}

	public static class Mutable12<A, B, C, D, E, F, G, H, I, J, K, L>
			extends Tuple12<A, B, C, D, E, F, G, H, I, J, K, L>
	{
		public Mutable12(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
		{
			super(a, b, c, d, e, f, g, h, i, j, k, l);
		}

		public @Override Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> mutable()
		{
			return this;
		}

		public @Override Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> copy()
		{
		    return new Tuple12.Mutable12<>(a, b, c, d, e, f, g, h, i, j, k, l);
		}

		public @Override Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> immutable()
		{
			return new Tuple12<>(a, b, c, d, e, f, g, h, i, j, k, l);
		}

		public @Override String toString()
 	    {
 	        return "Tuple12.Mutable12" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
 	    }

		public Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> setA(A a) { this.a = a; return this; }
		public Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> setB(B b) { this.b = b; return this; }
		public Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> setC(C c) { this.c = c; return this; }
		public Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> setD(D d) { this.d = d; return this; }
		public Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> setE(E e) { this.e = e; return this; }
		public Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> setF(F f) { this.f = f; return this; }
		public Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> setG(G g) { this.g = g; return this; }
		public Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> setH(H h) { this.h = h; return this; }
		public Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> setI(I i) { this.i = i; return this; }
		public Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> setJ(J j) { this.j = j; return this; }
		public Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> setK(K k) { this.k = k; return this; }
		public Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> setL(L l) { this.l = l; return this; }
	
		public @Override Tuple1.Mutable1<A> strip11R() { return Tuples.mutable(a); }
		public @Override Tuple1.Mutable1<L> strip11L() { return Tuples.mutable(l); }
		public @Override Tuple2.Mutable2<A, B> strip10R() { return Tuples.mutable(a, b); }
		public @Override Tuple2.Mutable2<K, L> strip10L() { return Tuples.mutable(k, l); }
		public @Override Tuple3.Mutable3<A, B, C> strip9R() { return Tuples.mutable(a, b, c); }
		public @Override Tuple3.Mutable3<J, K, L> strip9L() { return Tuples.mutable(j, k, l); }
		public @Override Tuple4.Mutable4<A, B, C, D> strip8R() { return Tuples.mutable(a, b, c, d); }
		public @Override Tuple4.Mutable4<I, J, K, L> strip8L() { return Tuples.mutable(i, j, k, l); }
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip7R() { return Tuples.mutable(a, b, c, d, e); }
		public @Override Tuple5.Mutable5<H, I, J, K, L> strip7L() { return Tuples.mutable(h, i, j, k, l); }
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip6R() { return Tuples.mutable(a, b, c, d, e, f); }
		public @Override Tuple6.Mutable6<G, H, I, J, K, L> strip6L() { return Tuples.mutable(g, h, i, j, k, l); }
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip5R() { return Tuples.mutable(a, b, c, d, e, f, g); }
		public @Override Tuple7.Mutable7<F, G, H, I, J, K, L> strip5L() { return Tuples.mutable(f, g, h, i, j, k, l); }
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip4R() { return Tuples.mutable(a, b, c, d, e, f, g, h); }
		public @Override Tuple8.Mutable8<E, F, G, H, I, J, K, L> strip4L() { return Tuples.mutable(e, f, g, h, i, j, k, l); }
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip3R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i); }
		public @Override Tuple9.Mutable9<D, E, F, G, H, I, J, K, L> strip3L() { return Tuples.mutable(d, e, f, g, h, i, j, k, l); }
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> strip2R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j); }
		public @Override Tuple10.Mutable10<C, D, E, F, G, H, I, J, K, L> strip2L() { return Tuples.mutable(c, d, e, f, g, h, i, j, k, l); }
		public @Override Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> strip1R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k); }
		public @Override Tuple11.Mutable11<B, C, D, E, F, G, H, I, J, K, L> strip1L() { return Tuples.mutable(b, c, d, e, f, g, h, i, j, k, l); }
	
		public @Override <M> Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(M m) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
		public @Override <M, N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(M m, N n) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
		public @Override <M, N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(M m, N n, O o) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
		public @Override <M, N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(M m, N n, O o, P p) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
		public @Override <M, N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(M m, N n, O o, P p, Q q) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
		public @Override <M, N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(M m, N n, O o, P p, Q q, R r) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
		public @Override <M, N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(M m, N n, O o, P p, Q q, R r, S s) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
		public @Override <M, N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(M m, N n, O o, P p, Q q, R r, S s, T t) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
		public @Override <M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(M m, N n, O o, P p, Q q, R r, S s, T t, U u) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
		public @Override <M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
		public @Override <M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
		public @Override <M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override <M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
		public @Override <M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	}
}