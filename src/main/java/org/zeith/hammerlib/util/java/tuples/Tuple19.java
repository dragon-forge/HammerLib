package org.zeith.hammerlib.util.java.tuples;

import java.util.stream.Stream;
import java.util.stream.Collectors;

public class Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> implements ITuple
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
	protected P p;
	protected Q q;
	protected R r;
	protected S s;
	
	public Tuple19(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
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
		this.p = p;
		this.q = q;
		this.r = r;
		this.s = s;
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
	public P p() { return p; }
	public Q q() { return q; }
	public R r() { return r; }
	public S s() { return s; }
	
	public Tuple1<A> strip18R() { return Tuples.immutable(a); }
	public Tuple1<S> strip18L() { return Tuples.immutable(s); }

	public Tuple2<A, B> strip17R() { return Tuples.immutable(a, b); }
	public Tuple2<R, S> strip17L() { return Tuples.immutable(r, s); }

	public Tuple3<A, B, C> strip16R() { return Tuples.immutable(a, b, c); }
	public Tuple3<Q, R, S> strip16L() { return Tuples.immutable(q, r, s); }

	public Tuple4<A, B, C, D> strip15R() { return Tuples.immutable(a, b, c, d); }
	public Tuple4<P, Q, R, S> strip15L() { return Tuples.immutable(p, q, r, s); }

	public Tuple5<A, B, C, D, E> strip14R() { return Tuples.immutable(a, b, c, d, e); }
	public Tuple5<O, P, Q, R, S> strip14L() { return Tuples.immutable(o, p, q, r, s); }

	public Tuple6<A, B, C, D, E, F> strip13R() { return Tuples.immutable(a, b, c, d, e, f); }
	public Tuple6<N, O, P, Q, R, S> strip13L() { return Tuples.immutable(n, o, p, q, r, s); }

	public Tuple7<A, B, C, D, E, F, G> strip12R() { return Tuples.immutable(a, b, c, d, e, f, g); }
	public Tuple7<M, N, O, P, Q, R, S> strip12L() { return Tuples.immutable(m, n, o, p, q, r, s); }

	public Tuple8<A, B, C, D, E, F, G, H> strip11R() { return Tuples.immutable(a, b, c, d, e, f, g, h); }
	public Tuple8<L, M, N, O, P, Q, R, S> strip11L() { return Tuples.immutable(l, m, n, o, p, q, r, s); }

	public Tuple9<A, B, C, D, E, F, G, H, I> strip10R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i); }
	public Tuple9<K, L, M, N, O, P, Q, R, S> strip10L() { return Tuples.immutable(k, l, m, n, o, p, q, r, s); }

	public Tuple10<A, B, C, D, E, F, G, H, I, J> strip9R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j); }
	public Tuple10<J, K, L, M, N, O, P, Q, R, S> strip9L() { return Tuples.immutable(j, k, l, m, n, o, p, q, r, s); }

	public Tuple11<A, B, C, D, E, F, G, H, I, J, K> strip8R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k); }
	public Tuple11<I, J, K, L, M, N, O, P, Q, R, S> strip8L() { return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s); }

	public Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> strip7R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l); }
	public Tuple12<H, I, J, K, L, M, N, O, P, Q, R, S> strip7L() { return Tuples.immutable(h, i, j, k, l, m, n, o, p, q, r, s); }

	public Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip6R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
	public Tuple13<G, H, I, J, K, L, M, N, O, P, Q, R, S> strip6L() { return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s); }

	public Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip5R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
	public Tuple14<F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip5L() { return Tuples.immutable(f, g, h, i, j, k, l, m, n, o, p, q, r, s); }

	public Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip4R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
	public Tuple15<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip4L() { return Tuples.immutable(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }

	public Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip3R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
	public Tuple16<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip3L() { return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }

	public Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> strip2R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
	public Tuple17<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip2L() { return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }

	public Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> strip1R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
	public Tuple18<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip1L() { return Tuples.immutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }

	
	public <T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(T t) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
	public <T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(T t, U u) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
	public <T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(T t, U u, V v) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
	public <T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(T t, U u, V v, W w) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
	public <T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(T t, U u, V v, W w, X x) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
	public <T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(T t, U u, V v, W w, X x, Y y) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
	public <T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(T t, U u, V v, W w, X x, Y y, Z z) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	
	public @Override int arity()
	{
		return 19;
	}

	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}

	public Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> mutable()
	{
		return new Tuple19.Mutable19<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}

	public Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> immutable()
	{
		return this;
	}

	public Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> copy()
	{
		return new Tuple19<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}

	public @Override String toString()
	{
		return "Tuple19" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}

	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}

	public @Override @SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple19 tuple)) return false;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}

	public static class Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>
			extends Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>
	{
		public Mutable19(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			super(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}

		public @Override Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> mutable()
		{
			return this;
		}

		public @Override Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> copy()
		{
		    return new Tuple19.Mutable19<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}

		public @Override Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> immutable()
		{
			return new Tuple19<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}

		public @Override String toString()
 	    {
 	        return "Tuple19.Mutable19" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
 	    }

		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setA(A a) { this.a = a; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setB(B b) { this.b = b; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setC(C c) { this.c = c; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setD(D d) { this.d = d; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setE(E e) { this.e = e; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setF(F f) { this.f = f; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setG(G g) { this.g = g; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setH(H h) { this.h = h; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setI(I i) { this.i = i; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setJ(J j) { this.j = j; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setK(K k) { this.k = k; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setL(L l) { this.l = l; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setM(M m) { this.m = m; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setN(N n) { this.n = n; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setO(O o) { this.o = o; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setP(P p) { this.p = p; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setQ(Q q) { this.q = q; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setR(R r) { this.r = r; return this; }
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setS(S s) { this.s = s; return this; }
	
		public @Override Tuple1.Mutable1<A> strip18R() { return Tuples.mutable(a); }
		public @Override Tuple1.Mutable1<S> strip18L() { return Tuples.mutable(s); }
		public @Override Tuple2.Mutable2<A, B> strip17R() { return Tuples.mutable(a, b); }
		public @Override Tuple2.Mutable2<R, S> strip17L() { return Tuples.mutable(r, s); }
		public @Override Tuple3.Mutable3<A, B, C> strip16R() { return Tuples.mutable(a, b, c); }
		public @Override Tuple3.Mutable3<Q, R, S> strip16L() { return Tuples.mutable(q, r, s); }
		public @Override Tuple4.Mutable4<A, B, C, D> strip15R() { return Tuples.mutable(a, b, c, d); }
		public @Override Tuple4.Mutable4<P, Q, R, S> strip15L() { return Tuples.mutable(p, q, r, s); }
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip14R() { return Tuples.mutable(a, b, c, d, e); }
		public @Override Tuple5.Mutable5<O, P, Q, R, S> strip14L() { return Tuples.mutable(o, p, q, r, s); }
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip13R() { return Tuples.mutable(a, b, c, d, e, f); }
		public @Override Tuple6.Mutable6<N, O, P, Q, R, S> strip13L() { return Tuples.mutable(n, o, p, q, r, s); }
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip12R() { return Tuples.mutable(a, b, c, d, e, f, g); }
		public @Override Tuple7.Mutable7<M, N, O, P, Q, R, S> strip12L() { return Tuples.mutable(m, n, o, p, q, r, s); }
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip11R() { return Tuples.mutable(a, b, c, d, e, f, g, h); }
		public @Override Tuple8.Mutable8<L, M, N, O, P, Q, R, S> strip11L() { return Tuples.mutable(l, m, n, o, p, q, r, s); }
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip10R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i); }
		public @Override Tuple9.Mutable9<K, L, M, N, O, P, Q, R, S> strip10L() { return Tuples.mutable(k, l, m, n, o, p, q, r, s); }
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> strip9R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j); }
		public @Override Tuple10.Mutable10<J, K, L, M, N, O, P, Q, R, S> strip9L() { return Tuples.mutable(j, k, l, m, n, o, p, q, r, s); }
		public @Override Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> strip8R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k); }
		public @Override Tuple11.Mutable11<I, J, K, L, M, N, O, P, Q, R, S> strip8L() { return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s); }
		public @Override Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> strip7R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l); }
		public @Override Tuple12.Mutable12<H, I, J, K, L, M, N, O, P, Q, R, S> strip7L() { return Tuples.mutable(h, i, j, k, l, m, n, o, p, q, r, s); }
		public @Override Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip6R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
		public @Override Tuple13.Mutable13<G, H, I, J, K, L, M, N, O, P, Q, R, S> strip6L() { return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s); }
		public @Override Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip5R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
		public @Override Tuple14.Mutable14<F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip5L() { return Tuples.mutable(f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
		public @Override Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip4R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
		public @Override Tuple15.Mutable15<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip4L() { return Tuples.mutable(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
		public @Override Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip3R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
		public @Override Tuple16.Mutable16<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip3L() { return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
		public @Override Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> strip2R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
		public @Override Tuple17.Mutable17<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip2L() { return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
		public @Override Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> strip1R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
		public @Override Tuple18.Mutable18<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip1L() { return Tuples.mutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
	
		public @Override <T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(T t) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
		public @Override <T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(T t, U u) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
		public @Override <T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(T t, U u, V v) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
		public @Override <T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(T t, U u, V v, W w) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
		public @Override <T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(T t, U u, V v, W w, X x) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override <T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(T t, U u, V v, W w, X x, Y y) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
		public @Override <T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(T t, U u, V v, W w, X x, Y y, Z z) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	}
}