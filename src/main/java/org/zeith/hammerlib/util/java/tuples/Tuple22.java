package org.zeith.hammerlib.util.java.tuples;

import org.zeith.hammerlib.util.java.consumers.*;
import org.zeith.hammerlib.util.java.functions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V>
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
	protected N n;
	protected O o;
	protected P p;
	protected Q q;
	protected R r;
	protected S s;
	protected T t;
	protected U u;
	protected V v;
	
	public Tuple22(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
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
	
	public N n()
	{
		return n;
	}
	
	public O o()
	{
		return o;
	}
	
	public P p()
	{
		return p;
	}
	
	public Q q()
	{
		return q;
	}
	
	public R r()
	{
		return r;
	}
	
	public S s()
	{
		return s;
	}
	
	public T t()
	{
		return t;
	}
	
	public U u()
	{
		return u;
	}
	
	public V v()
	{
		return v;
	}
	
	public Tuple1<A> strip21R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<V> strip21L()
	{
		return Tuples.immutable(v);
	}
	
	public Tuple2<A, B> strip20R()
	{
		return Tuples.immutable(a, b);
	}
	
	public Tuple2<U, V> strip20L()
	{
		return Tuples.immutable(u, v);
	}
	
	public Tuple3<A, B, C> strip19R()
	{
		return Tuples.immutable(a, b, c);
	}
	
	public Tuple3<T, U, V> strip19L()
	{
		return Tuples.immutable(t, u, v);
	}
	
	public Tuple4<A, B, C, D> strip18R()
	{
		return Tuples.immutable(a, b, c, d);
	}
	
	public Tuple4<S, T, U, V> strip18L()
	{
		return Tuples.immutable(s, t, u, v);
	}
	
	public Tuple5<A, B, C, D, E> strip17R()
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public Tuple5<R, S, T, U, V> strip17L()
	{
		return Tuples.immutable(r, s, t, u, v);
	}
	
	public Tuple6<A, B, C, D, E, F> strip16R()
	{
		return Tuples.immutable(a, b, c, d, e, f);
	}
	
	public Tuple6<Q, R, S, T, U, V> strip16L()
	{
		return Tuples.immutable(q, r, s, t, u, v);
	}
	
	public Tuple7<A, B, C, D, E, F, G> strip15R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public Tuple7<P, Q, R, S, T, U, V> strip15L()
	{
		return Tuples.immutable(p, q, r, s, t, u, v);
	}
	
	public Tuple8<A, B, C, D, E, F, G, H> strip14R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h);
	}
	
	public Tuple8<O, P, Q, R, S, T, U, V> strip14L()
	{
		return Tuples.immutable(o, p, q, r, s, t, u, v);
	}
	
	public Tuple9<A, B, C, D, E, F, G, H, I> strip13R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public Tuple9<N, O, P, Q, R, S, T, U, V> strip13L()
	{
		return Tuples.immutable(n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple10<A, B, C, D, E, F, G, H, I, J> strip12R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j);
	}
	
	public Tuple10<M, N, O, P, Q, R, S, T, U, V> strip12L()
	{
		return Tuples.immutable(m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple11<A, B, C, D, E, F, G, H, I, J, K> strip11R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public Tuple11<L, M, N, O, P, Q, R, S, T, U, V> strip11L()
	{
		return Tuples.immutable(l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> strip10R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public Tuple12<K, L, M, N, O, P, Q, R, S, T, U, V> strip10L()
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip9R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public Tuple13<J, K, L, M, N, O, P, Q, R, S, T, U, V> strip9L()
	{
		return Tuples.immutable(j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip8R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public Tuple14<I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip8L()
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip7R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public Tuple15<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip7L()
	{
		return Tuples.immutable(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip6R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public Tuple16<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip6L()
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> strip5R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public Tuple17<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip5L()
	{
		return Tuples.immutable(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> strip4R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public Tuple18<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip4L()
	{
		return Tuples.immutable(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip3R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public Tuple19<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip3L()
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> strip2R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public Tuple20<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip2L()
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> strip1R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public Tuple21<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip1L()
	{
		return Tuples.immutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	
	public <W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(W w)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(W w, X x)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(W w, X x, Y y)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(W w, X x, Y y, Z z)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <W> Tuple23<W, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> insert(W w)
	{
		return Tuples.immutable(w, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <W, X> Tuple24<W, X, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> insert(W w, X x)
	{
		return Tuples.immutable(w, x, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <W, X, Y> Tuple25<W, X, Y, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> insert(W w, X x, Y y)
	{
		return Tuples.immutable(w, x, y, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <W, X, Y, Z> Tuple26<W, X, Y, Z, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> insert(W w, X x, Y y, Z z)
	{
		return Tuples.immutable(w, x, y, z, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function1<A, RES> func)
	{
		return func.apply(a);
	}
	
	public <RES> RES applyR(Function1<V, RES> func)
	{
		return func.apply(v);
	}
	
	public <RES> RES applyL(Function2<A, B, RES> func)
	{
		return func.apply(a, b);
	}
	
	public <RES> RES applyR(Function2<U, V, RES> func)
	{
		return func.apply(u, v);
	}
	
	public <RES> RES applyL(Function3<A, B, C, RES> func)
	{
		return func.apply(a, b, c);
	}
	
	public <RES> RES applyR(Function3<T, U, V, RES> func)
	{
		return func.apply(t, u, v);
	}
	
	public <RES> RES applyL(Function4<A, B, C, D, RES> func)
	{
		return func.apply(a, b, c, d);
	}
	
	public <RES> RES applyR(Function4<S, T, U, V, RES> func)
	{
		return func.apply(s, t, u, v);
	}
	
	public <RES> RES applyL(Function5<A, B, C, D, E, RES> func)
	{
		return func.apply(a, b, c, d, e);
	}
	
	public <RES> RES applyR(Function5<R, S, T, U, V, RES> func)
	{
		return func.apply(r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function6<A, B, C, D, E, F, RES> func)
	{
		return func.apply(a, b, c, d, e, f);
	}
	
	public <RES> RES applyR(Function6<Q, R, S, T, U, V, RES> func)
	{
		return func.apply(q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function7<A, B, C, D, E, F, G, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g);
	}
	
	public <RES> RES applyR(Function7<P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function8<A, B, C, D, E, F, G, H, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h);
	}
	
	public <RES> RES applyR(Function8<O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function9<A, B, C, D, E, F, G, H, I, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i);
	}
	
	public <RES> RES applyR(Function9<N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function10<A, B, C, D, E, F, G, H, I, J, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	public <RES> RES applyR(Function10<M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function11<A, B, C, D, E, F, G, H, I, J, K, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public <RES> RES applyR(Function11<L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public <RES> RES applyR(Function12<K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public <RES> RES applyR(Function13<J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <RES> RES applyR(Function14<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <RES> RES applyR(Function15<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyR(Function16<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <RES> RES applyR(Function17<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <RES> RES applyR(Function18<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyR(Function19<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <RES> RES applyR(Function20<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyL(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <RES> RES applyR(Function21<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES apply(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer1<A> consumer)
	{
		consumer.accept(a);
	}
	
	public void acceptR(Consumer1<V> consumer)
	{
		consumer.accept(v);
	}
	
	public void acceptL(Consumer2<A, B> consumer)
	{
		consumer.accept(a, b);
	}
	
	public void acceptR(Consumer2<U, V> consumer)
	{
		consumer.accept(u, v);
	}
	
	public void acceptL(Consumer3<A, B, C> consumer)
	{
		consumer.accept(a, b, c);
	}
	
	public void acceptR(Consumer3<T, U, V> consumer)
	{
		consumer.accept(t, u, v);
	}
	
	public void acceptL(Consumer4<A, B, C, D> consumer)
	{
		consumer.accept(a, b, c, d);
	}
	
	public void acceptR(Consumer4<S, T, U, V> consumer)
	{
		consumer.accept(s, t, u, v);
	}
	
	public void acceptL(Consumer5<A, B, C, D, E> consumer)
	{
		consumer.accept(a, b, c, d, e);
	}
	
	public void acceptR(Consumer5<R, S, T, U, V> consumer)
	{
		consumer.accept(r, s, t, u, v);
	}
	
	public void acceptL(Consumer6<A, B, C, D, E, F> consumer)
	{
		consumer.accept(a, b, c, d, e, f);
	}
	
	public void acceptR(Consumer6<Q, R, S, T, U, V> consumer)
	{
		consumer.accept(q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer7<A, B, C, D, E, F, G> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g);
	}
	
	public void acceptR(Consumer7<P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer8<A, B, C, D, E, F, G, H> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h);
	}
	
	public void acceptR(Consumer8<O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer9<A, B, C, D, E, F, G, H, I> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i);
	}
	
	public void acceptR(Consumer9<N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer10<A, B, C, D, E, F, G, H, I, J> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	public void acceptR(Consumer10<M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer11<A, B, C, D, E, F, G, H, I, J, K> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public void acceptR(Consumer11<L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public void acceptR(Consumer12<K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public void acceptR(Consumer13<J, K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public void acceptR(Consumer14<I, J, K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public void acceptR(Consumer15<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public void acceptR(Consumer16<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public void acceptR(Consumer17<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public void acceptR(Consumer18<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptR(Consumer19<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public void acceptR(Consumer20<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptL(Consumer21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public void acceptR(Consumer21<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void accept(Consumer22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public @Override int arity()
	{
		return 22;
	}
	
	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> mutable()
	{
		return new Mutable22<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> immutable()
	{
		return this;
	}
	
	public Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> copy()
	{
		return new Tuple22<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public @Override String toString()
	{
		return "Tuple22" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}
	
	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof ITuple)) return false;
		ITuple tuple = (ITuple) other;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}
	
	public static class Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V>
			extends Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V>
	{
		public Mutable22(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			super(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> mutable()
		{
			return this;
		}
		
		public @Override Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> copy()
		{
			return new Mutable22<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> immutable()
		{
			return new Tuple22<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override String toString()
		{
			return "Tuple22.Mutable22" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setC(C c)
		{
			this.c = c;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setD(D d)
		{
			this.d = d;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setE(E e)
		{
			this.e = e;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setF(F f)
		{
			this.f = f;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setG(G g)
		{
			this.g = g;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setH(H h)
		{
			this.h = h;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setI(I i)
		{
			this.i = i;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setJ(J j)
		{
			this.j = j;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setK(K k)
		{
			this.k = k;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setL(L l)
		{
			this.l = l;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setM(M m)
		{
			this.m = m;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setN(N n)
		{
			this.n = n;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setO(O o)
		{
			this.o = o;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setP(P p)
		{
			this.p = p;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setQ(Q q)
		{
			this.q = q;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setR(R r)
		{
			this.r = r;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setS(S s)
		{
			this.s = s;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setT(T t)
		{
			this.t = t;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setU(U u)
		{
			this.u = u;
			return this;
		}
		
		public Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> setV(V v)
		{
			this.v = v;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip21R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<V> strip21L()
		{
			return Tuples.mutable(v);
		}
		
		public @Override Tuple2.Mutable2<A, B> strip20R()
		{
			return Tuples.mutable(a, b);
		}
		
		public @Override Tuple2.Mutable2<U, V> strip20L()
		{
			return Tuples.mutable(u, v);
		}
		
		public @Override Tuple3.Mutable3<A, B, C> strip19R()
		{
			return Tuples.mutable(a, b, c);
		}
		
		public @Override Tuple3.Mutable3<T, U, V> strip19L()
		{
			return Tuples.mutable(t, u, v);
		}
		
		public @Override Tuple4.Mutable4<A, B, C, D> strip18R()
		{
			return Tuples.mutable(a, b, c, d);
		}
		
		public @Override Tuple4.Mutable4<S, T, U, V> strip18L()
		{
			return Tuples.mutable(s, t, u, v);
		}
		
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip17R()
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override Tuple5.Mutable5<R, S, T, U, V> strip17L()
		{
			return Tuples.mutable(r, s, t, u, v);
		}
		
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip16R()
		{
			return Tuples.mutable(a, b, c, d, e, f);
		}
		
		public @Override Tuple6.Mutable6<Q, R, S, T, U, V> strip16L()
		{
			return Tuples.mutable(q, r, s, t, u, v);
		}
		
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip15R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override Tuple7.Mutable7<P, Q, R, S, T, U, V> strip15L()
		{
			return Tuples.mutable(p, q, r, s, t, u, v);
		}
		
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip14R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h);
		}
		
		public @Override Tuple8.Mutable8<O, P, Q, R, S, T, U, V> strip14L()
		{
			return Tuples.mutable(o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip13R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override Tuple9.Mutable9<N, O, P, Q, R, S, T, U, V> strip13L()
		{
			return Tuples.mutable(n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> strip12R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override Tuple10.Mutable10<M, N, O, P, Q, R, S, T, U, V> strip12L()
		{
			return Tuples.mutable(m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> strip11R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override Tuple11.Mutable11<L, M, N, O, P, Q, R, S, T, U, V> strip11L()
		{
			return Tuples.mutable(l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> strip10R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override Tuple12.Mutable12<K, L, M, N, O, P, Q, R, S, T, U, V> strip10L()
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip9R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override Tuple13.Mutable13<J, K, L, M, N, O, P, Q, R, S, T, U, V> strip9L()
		{
			return Tuples.mutable(j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip8R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override Tuple14.Mutable14<I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip8L()
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip7R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override Tuple15.Mutable15<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip7L()
		{
			return Tuples.mutable(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip6R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override Tuple16.Mutable16<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip6L()
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> strip5R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override Tuple17.Mutable17<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip5L()
		{
			return Tuples.mutable(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> strip4R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override Tuple18.Mutable18<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip4L()
		{
			return Tuples.mutable(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip3R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple19.Mutable19<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip3L()
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> strip2R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override Tuple20.Mutable20<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip2L()
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> strip1R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override Tuple21.Mutable21<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip1L()
		{
			return Tuples.mutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(W w)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(W w, X x)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override <W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(W w, X x, Y y)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override <W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(W w, X x, Y y, Z z)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override <W> Tuple23.Mutable23<W, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> insert(W w)
		{
			return Tuples.mutable(w, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <W, X> Tuple24.Mutable24<W, X, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> insert(W w, X x)
		{
			return Tuples.mutable(w, x, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <W, X, Y> Tuple25.Mutable25<W, X, Y, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> insert(W w, X x, Y y)
		{
			return Tuples.mutable(w, x, y, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <W, X, Y, Z> Tuple26.Mutable26<W, X, Y, Z, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> insert(W w, X x, Y y, Z z)
		{
			return Tuples.mutable(w, x, y, z, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
	}
}