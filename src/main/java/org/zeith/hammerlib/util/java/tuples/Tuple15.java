package org.zeith.hammerlib.util.java.tuples;

import java.util.stream.Stream;
import java.util.stream.Collectors;

public class Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> implements ITuple
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
	protected N n;
	protected O o;
	
	public Tuple15(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
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
		this.n = n;
		this.o = o;
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
	public M m() { return m; }
	public N n() { return n; }
	public O o() { return o; }
	
	public Tuple1<A> strip14R() { return Tuples.immutable(a); }
	public Tuple1<O> strip14L() { return Tuples.immutable(o); }

	public Tuple2<A, B> strip13R() { return Tuples.immutable(a, b); }
	public Tuple2<N, O> strip13L() { return Tuples.immutable(n, o); }

	public Tuple3<A, B, C> strip12R() { return Tuples.immutable(a, b, c); }
	public Tuple3<M, N, O> strip12L() { return Tuples.immutable(m, n, o); }

	public Tuple4<A, B, C, D> strip11R() { return Tuples.immutable(a, b, c, d); }
	public Tuple4<L, M, N, O> strip11L() { return Tuples.immutable(l, m, n, o); }

	public Tuple5<A, B, C, D, E> strip10R() { return Tuples.immutable(a, b, c, d, e); }
	public Tuple5<K, L, M, N, O> strip10L() { return Tuples.immutable(k, l, m, n, o); }

	public Tuple6<A, B, C, D, E, F> strip9R() { return Tuples.immutable(a, b, c, d, e, f); }
	public Tuple6<J, K, L, M, N, O> strip9L() { return Tuples.immutable(j, k, l, m, n, o); }

	public Tuple7<A, B, C, D, E, F, G> strip8R() { return Tuples.immutable(a, b, c, d, e, f, g); }
	public Tuple7<I, J, K, L, M, N, O> strip8L() { return Tuples.immutable(i, j, k, l, m, n, o); }

	public Tuple8<A, B, C, D, E, F, G, H> strip7R() { return Tuples.immutable(a, b, c, d, e, f, g, h); }
	public Tuple8<H, I, J, K, L, M, N, O> strip7L() { return Tuples.immutable(h, i, j, k, l, m, n, o); }

	public Tuple9<A, B, C, D, E, F, G, H, I> strip6R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i); }
	public Tuple9<G, H, I, J, K, L, M, N, O> strip6L() { return Tuples.immutable(g, h, i, j, k, l, m, n, o); }

	public Tuple10<A, B, C, D, E, F, G, H, I, J> strip5R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j); }
	public Tuple10<F, G, H, I, J, K, L, M, N, O> strip5L() { return Tuples.immutable(f, g, h, i, j, k, l, m, n, o); }

	public Tuple11<A, B, C, D, E, F, G, H, I, J, K> strip4R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k); }
	public Tuple11<E, F, G, H, I, J, K, L, M, N, O> strip4L() { return Tuples.immutable(e, f, g, h, i, j, k, l, m, n, o); }

	public Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> strip3R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l); }
	public Tuple12<D, E, F, G, H, I, J, K, L, M, N, O> strip3L() { return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o); }

	public Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip2R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
	public Tuple13<C, D, E, F, G, H, I, J, K, L, M, N, O> strip2L() { return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o); }

	public Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip1R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
	public Tuple14<B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip1L() { return Tuples.immutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o); }

	
	public <P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(P p) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
	public <P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(P p, Q q) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
	public <P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(P p, Q q, R r) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
	public <P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(P p, Q q, R r, S s) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
	public <P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(P p, Q q, R r, S s, T t) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
	public <P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(P p, Q q, R r, S s, T t, U u) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
	public <P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(P p, Q q, R r, S s, T t, U u, V v) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
	public <P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(P p, Q q, R r, S s, T t, U u, V v, W w) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
	public <P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(P p, Q q, R r, S s, T t, U u, V v, W w, X x) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
	public <P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
	public <P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	
	public @Override int arity()
	{
		return 15;
	}

	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}

	public Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> mutable()
	{
		return new Tuple15.Mutable15<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}

	public Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> immutable()
	{
		return this;
	}

	public Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> copy()
	{
		return new Tuple15<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}

	public @Override String toString()
	{
		return "Tuple15" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}

	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}

	public @Override @SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple15 tuple)) return false;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}

	public static class Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>
			extends Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>
	{
		public Mutable15(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
		{
			super(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}

		public @Override Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> mutable()
		{
			return this;
		}

		public @Override Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> copy()
		{
		    return new Tuple15.Mutable15<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}

		public @Override Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> immutable()
		{
			return new Tuple15<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}

		public @Override String toString()
 	    {
 	        return "Tuple15.Mutable15" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
 	    }

		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setA(A a) { this.a = a; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setB(B b) { this.b = b; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setC(C c) { this.c = c; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setD(D d) { this.d = d; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setE(E e) { this.e = e; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setF(F f) { this.f = f; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setG(G g) { this.g = g; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setH(H h) { this.h = h; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setI(I i) { this.i = i; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setJ(J j) { this.j = j; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setK(K k) { this.k = k; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setL(L l) { this.l = l; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setM(M m) { this.m = m; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setN(N n) { this.n = n; return this; }
		public Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> setO(O o) { this.o = o; return this; }
	
		public @Override Tuple1.Mutable1<A> strip14R() { return Tuples.mutable(a); }
		public @Override Tuple1.Mutable1<O> strip14L() { return Tuples.mutable(o); }
		public @Override Tuple2.Mutable2<A, B> strip13R() { return Tuples.mutable(a, b); }
		public @Override Tuple2.Mutable2<N, O> strip13L() { return Tuples.mutable(n, o); }
		public @Override Tuple3.Mutable3<A, B, C> strip12R() { return Tuples.mutable(a, b, c); }
		public @Override Tuple3.Mutable3<M, N, O> strip12L() { return Tuples.mutable(m, n, o); }
		public @Override Tuple4.Mutable4<A, B, C, D> strip11R() { return Tuples.mutable(a, b, c, d); }
		public @Override Tuple4.Mutable4<L, M, N, O> strip11L() { return Tuples.mutable(l, m, n, o); }
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip10R() { return Tuples.mutable(a, b, c, d, e); }
		public @Override Tuple5.Mutable5<K, L, M, N, O> strip10L() { return Tuples.mutable(k, l, m, n, o); }
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip9R() { return Tuples.mutable(a, b, c, d, e, f); }
		public @Override Tuple6.Mutable6<J, K, L, M, N, O> strip9L() { return Tuples.mutable(j, k, l, m, n, o); }
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip8R() { return Tuples.mutable(a, b, c, d, e, f, g); }
		public @Override Tuple7.Mutable7<I, J, K, L, M, N, O> strip8L() { return Tuples.mutable(i, j, k, l, m, n, o); }
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip7R() { return Tuples.mutable(a, b, c, d, e, f, g, h); }
		public @Override Tuple8.Mutable8<H, I, J, K, L, M, N, O> strip7L() { return Tuples.mutable(h, i, j, k, l, m, n, o); }
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip6R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i); }
		public @Override Tuple9.Mutable9<G, H, I, J, K, L, M, N, O> strip6L() { return Tuples.mutable(g, h, i, j, k, l, m, n, o); }
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> strip5R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j); }
		public @Override Tuple10.Mutable10<F, G, H, I, J, K, L, M, N, O> strip5L() { return Tuples.mutable(f, g, h, i, j, k, l, m, n, o); }
		public @Override Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> strip4R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k); }
		public @Override Tuple11.Mutable11<E, F, G, H, I, J, K, L, M, N, O> strip4L() { return Tuples.mutable(e, f, g, h, i, j, k, l, m, n, o); }
		public @Override Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> strip3R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l); }
		public @Override Tuple12.Mutable12<D, E, F, G, H, I, J, K, L, M, N, O> strip3L() { return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o); }
		public @Override Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip2R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
		public @Override Tuple13.Mutable13<C, D, E, F, G, H, I, J, K, L, M, N, O> strip2L() { return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o); }
		public @Override Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip1R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
		public @Override Tuple14.Mutable14<B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip1L() { return Tuples.mutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
	
		public @Override <P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(P p) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
		public @Override <P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(P p, Q q) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
		public @Override <P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(P p, Q q, R r) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
		public @Override <P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(P p, Q q, R r, S s) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
		public @Override <P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(P p, Q q, R r, S s, T t) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
		public @Override <P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(P p, Q q, R r, S s, T t, U u) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
		public @Override <P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(P p, Q q, R r, S s, T t, U u, V v) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
		public @Override <P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(P p, Q q, R r, S s, T t, U u, V v, W w) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
		public @Override <P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(P p, Q q, R r, S s, T t, U u, V v, W w, X x) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override <P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
		public @Override <P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	}
}