package org.zeith.hammerlib.util.java.tuples;

import java.util.stream.Stream;
import java.util.stream.Collectors;

public class Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> implements ITuple
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
	protected T t;
	protected U u;
	protected V v;
	protected W w;
	protected X x;
	
	public Tuple24(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
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
		this.t = t;
		this.u = u;
		this.v = v;
		this.w = w;
		this.x = x;
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
	public T t() { return t; }
	public U u() { return u; }
	public V v() { return v; }
	public W w() { return w; }
	public X x() { return x; }
	
	public Tuple1<A> strip23R() { return Tuples.immutable(a); }
	public Tuple1<X> strip23L() { return Tuples.immutable(x); }

	public Tuple2<A, B> strip22R() { return Tuples.immutable(a, b); }
	public Tuple2<W, X> strip22L() { return Tuples.immutable(w, x); }

	public Tuple3<A, B, C> strip21R() { return Tuples.immutable(a, b, c); }
	public Tuple3<V, W, X> strip21L() { return Tuples.immutable(v, w, x); }

	public Tuple4<A, B, C, D> strip20R() { return Tuples.immutable(a, b, c, d); }
	public Tuple4<U, V, W, X> strip20L() { return Tuples.immutable(u, v, w, x); }

	public Tuple5<A, B, C, D, E> strip19R() { return Tuples.immutable(a, b, c, d, e); }
	public Tuple5<T, U, V, W, X> strip19L() { return Tuples.immutable(t, u, v, w, x); }

	public Tuple6<A, B, C, D, E, F> strip18R() { return Tuples.immutable(a, b, c, d, e, f); }
	public Tuple6<S, T, U, V, W, X> strip18L() { return Tuples.immutable(s, t, u, v, w, x); }

	public Tuple7<A, B, C, D, E, F, G> strip17R() { return Tuples.immutable(a, b, c, d, e, f, g); }
	public Tuple7<R, S, T, U, V, W, X> strip17L() { return Tuples.immutable(r, s, t, u, v, w, x); }

	public Tuple8<A, B, C, D, E, F, G, H> strip16R() { return Tuples.immutable(a, b, c, d, e, f, g, h); }
	public Tuple8<Q, R, S, T, U, V, W, X> strip16L() { return Tuples.immutable(q, r, s, t, u, v, w, x); }

	public Tuple9<A, B, C, D, E, F, G, H, I> strip15R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i); }
	public Tuple9<P, Q, R, S, T, U, V, W, X> strip15L() { return Tuples.immutable(p, q, r, s, t, u, v, w, x); }

	public Tuple10<A, B, C, D, E, F, G, H, I, J> strip14R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j); }
	public Tuple10<O, P, Q, R, S, T, U, V, W, X> strip14L() { return Tuples.immutable(o, p, q, r, s, t, u, v, w, x); }

	public Tuple11<A, B, C, D, E, F, G, H, I, J, K> strip13R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k); }
	public Tuple11<N, O, P, Q, R, S, T, U, V, W, X> strip13L() { return Tuples.immutable(n, o, p, q, r, s, t, u, v, w, x); }

	public Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> strip12R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l); }
	public Tuple12<M, N, O, P, Q, R, S, T, U, V, W, X> strip12L() { return Tuples.immutable(m, n, o, p, q, r, s, t, u, v, w, x); }

	public Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip11R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
	public Tuple13<L, M, N, O, P, Q, R, S, T, U, V, W, X> strip11L() { return Tuples.immutable(l, m, n, o, p, q, r, s, t, u, v, w, x); }

	public Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip10R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
	public Tuple14<K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip10L() { return Tuples.immutable(k, l, m, n, o, p, q, r, s, t, u, v, w, x); }

	public Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip9R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
	public Tuple15<J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip9L() { return Tuples.immutable(j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }

	public Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip8R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
	public Tuple16<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip8L() { return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }

	public Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> strip7R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
	public Tuple17<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip7L() { return Tuples.immutable(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }

	public Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> strip6R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
	public Tuple18<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip6L() { return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }

	public Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip5R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
	public Tuple19<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip5L() { return Tuples.immutable(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }

	public Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> strip4R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
	public Tuple20<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip4L() { return Tuples.immutable(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }

	public Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> strip3R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
	public Tuple21<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip3L() { return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }

	public Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip2R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
	public Tuple22<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip2L() { return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }

	public Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip1R() { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
	public Tuple23<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip1L() { return Tuples.immutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }

	
	public <Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(Y y) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
	public <Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(Y y, Z z) { return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	
	public @Override int arity()
	{
		return 24;
	}

	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}

	public Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> mutable()
	{
		return new Tuple24.Mutable24<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}

	public Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> immutable()
	{
		return this;
	}

	public Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> copy()
	{
		return new Tuple24<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}

	public @Override String toString()
	{
		return "Tuple24" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}

	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}

	public @Override @SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple24 tuple)) return false;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}

	public static class Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X>
			extends Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X>
	{
		public Mutable24(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			super(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}

		public @Override Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> mutable()
		{
			return this;
		}

		public @Override Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> copy()
		{
		    return new Tuple24.Mutable24<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}

		public @Override Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> immutable()
		{
			return new Tuple24<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}

		public @Override String toString()
 	    {
 	        return "Tuple24.Mutable24" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
 	    }

		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setA(A a) { this.a = a; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setB(B b) { this.b = b; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setC(C c) { this.c = c; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setD(D d) { this.d = d; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setE(E e) { this.e = e; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setF(F f) { this.f = f; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setG(G g) { this.g = g; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setH(H h) { this.h = h; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setI(I i) { this.i = i; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setJ(J j) { this.j = j; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setK(K k) { this.k = k; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setL(L l) { this.l = l; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setM(M m) { this.m = m; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setN(N n) { this.n = n; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setO(O o) { this.o = o; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setP(P p) { this.p = p; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setQ(Q q) { this.q = q; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setR(R r) { this.r = r; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setS(S s) { this.s = s; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setT(T t) { this.t = t; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setU(U u) { this.u = u; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setV(V v) { this.v = v; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setW(W w) { this.w = w; return this; }
		public Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> setX(X x) { this.x = x; return this; }
	
		public @Override Tuple1.Mutable1<A> strip23R() { return Tuples.mutable(a); }
		public @Override Tuple1.Mutable1<X> strip23L() { return Tuples.mutable(x); }
		public @Override Tuple2.Mutable2<A, B> strip22R() { return Tuples.mutable(a, b); }
		public @Override Tuple2.Mutable2<W, X> strip22L() { return Tuples.mutable(w, x); }
		public @Override Tuple3.Mutable3<A, B, C> strip21R() { return Tuples.mutable(a, b, c); }
		public @Override Tuple3.Mutable3<V, W, X> strip21L() { return Tuples.mutable(v, w, x); }
		public @Override Tuple4.Mutable4<A, B, C, D> strip20R() { return Tuples.mutable(a, b, c, d); }
		public @Override Tuple4.Mutable4<U, V, W, X> strip20L() { return Tuples.mutable(u, v, w, x); }
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip19R() { return Tuples.mutable(a, b, c, d, e); }
		public @Override Tuple5.Mutable5<T, U, V, W, X> strip19L() { return Tuples.mutable(t, u, v, w, x); }
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip18R() { return Tuples.mutable(a, b, c, d, e, f); }
		public @Override Tuple6.Mutable6<S, T, U, V, W, X> strip18L() { return Tuples.mutable(s, t, u, v, w, x); }
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip17R() { return Tuples.mutable(a, b, c, d, e, f, g); }
		public @Override Tuple7.Mutable7<R, S, T, U, V, W, X> strip17L() { return Tuples.mutable(r, s, t, u, v, w, x); }
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip16R() { return Tuples.mutable(a, b, c, d, e, f, g, h); }
		public @Override Tuple8.Mutable8<Q, R, S, T, U, V, W, X> strip16L() { return Tuples.mutable(q, r, s, t, u, v, w, x); }
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip15R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i); }
		public @Override Tuple9.Mutable9<P, Q, R, S, T, U, V, W, X> strip15L() { return Tuples.mutable(p, q, r, s, t, u, v, w, x); }
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> strip14R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j); }
		public @Override Tuple10.Mutable10<O, P, Q, R, S, T, U, V, W, X> strip14L() { return Tuples.mutable(o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> strip13R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k); }
		public @Override Tuple11.Mutable11<N, O, P, Q, R, S, T, U, V, W, X> strip13L() { return Tuples.mutable(n, o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> strip12R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l); }
		public @Override Tuple12.Mutable12<M, N, O, P, Q, R, S, T, U, V, W, X> strip12L() { return Tuples.mutable(m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip11R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m); }
		public @Override Tuple13.Mutable13<L, M, N, O, P, Q, R, S, T, U, V, W, X> strip11L() { return Tuples.mutable(l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip10R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n); }
		public @Override Tuple14.Mutable14<K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip10L() { return Tuples.mutable(k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip9R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o); }
		public @Override Tuple15.Mutable15<J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip9L() { return Tuples.mutable(j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip8R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p); }
		public @Override Tuple16.Mutable16<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip8L() { return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> strip7R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q); }
		public @Override Tuple17.Mutable17<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip7L() { return Tuples.mutable(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> strip6R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r); }
		public @Override Tuple18.Mutable18<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip6L() { return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip5R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s); }
		public @Override Tuple19.Mutable19<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip5L() { return Tuples.mutable(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> strip4R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t); }
		public @Override Tuple20.Mutable20<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip4L() { return Tuples.mutable(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> strip3R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u); }
		public @Override Tuple21.Mutable21<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip3L() { return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip2R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v); }
		public @Override Tuple22.Mutable22<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip2L() { return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
		public @Override Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip1R() { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w); }
		public @Override Tuple23.Mutable23<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip1L() { return Tuples.mutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x); }
	
		public @Override <Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(Y y) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y); }
		public @Override <Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(Y y, Z z) { return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z); }
	}
}