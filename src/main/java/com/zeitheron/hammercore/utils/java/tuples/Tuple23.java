package com.zeitheron.hammercore.utils.java.tuples;

import com.zeitheron.hammercore.utils.java.consumers.*;
import com.zeitheron.hammercore.utils.java.functions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W>
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
	protected W w;
	
	public Tuple23(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
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
	
	public W w()
	{
		return w;
	}
	
	public Tuple1<A> strip22R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<W> strip22L()
	{
		return Tuples.immutable(w);
	}
	
	public Tuple2<A, B> strip21R()
	{
		return Tuples.immutable(a, b);
	}
	
	public Tuple2<V, W> strip21L()
	{
		return Tuples.immutable(v, w);
	}
	
	public Tuple3<A, B, C> strip20R()
	{
		return Tuples.immutable(a, b, c);
	}
	
	public Tuple3<U, V, W> strip20L()
	{
		return Tuples.immutable(u, v, w);
	}
	
	public Tuple4<A, B, C, D> strip19R()
	{
		return Tuples.immutable(a, b, c, d);
	}
	
	public Tuple4<T, U, V, W> strip19L()
	{
		return Tuples.immutable(t, u, v, w);
	}
	
	public Tuple5<A, B, C, D, E> strip18R()
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public Tuple5<S, T, U, V, W> strip18L()
	{
		return Tuples.immutable(s, t, u, v, w);
	}
	
	public Tuple6<A, B, C, D, E, F> strip17R()
	{
		return Tuples.immutable(a, b, c, d, e, f);
	}
	
	public Tuple6<R, S, T, U, V, W> strip17L()
	{
		return Tuples.immutable(r, s, t, u, v, w);
	}
	
	public Tuple7<A, B, C, D, E, F, G> strip16R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public Tuple7<Q, R, S, T, U, V, W> strip16L()
	{
		return Tuples.immutable(q, r, s, t, u, v, w);
	}
	
	public Tuple8<A, B, C, D, E, F, G, H> strip15R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h);
	}
	
	public Tuple8<P, Q, R, S, T, U, V, W> strip15L()
	{
		return Tuples.immutable(p, q, r, s, t, u, v, w);
	}
	
	public Tuple9<A, B, C, D, E, F, G, H, I> strip14R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public Tuple9<O, P, Q, R, S, T, U, V, W> strip14L()
	{
		return Tuples.immutable(o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple10<A, B, C, D, E, F, G, H, I, J> strip13R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j);
	}
	
	public Tuple10<N, O, P, Q, R, S, T, U, V, W> strip13L()
	{
		return Tuples.immutable(n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple11<A, B, C, D, E, F, G, H, I, J, K> strip12R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public Tuple11<M, N, O, P, Q, R, S, T, U, V, W> strip12L()
	{
		return Tuples.immutable(m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> strip11R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public Tuple12<L, M, N, O, P, Q, R, S, T, U, V, W> strip11L()
	{
		return Tuples.immutable(l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip10R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public Tuple13<K, L, M, N, O, P, Q, R, S, T, U, V, W> strip10L()
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip9R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public Tuple14<J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip9L()
	{
		return Tuples.immutable(j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip8R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public Tuple15<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip8L()
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip7R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public Tuple16<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip7L()
	{
		return Tuples.immutable(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> strip6R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public Tuple17<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip6L()
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> strip5R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public Tuple18<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip5L()
	{
		return Tuples.immutable(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip4R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public Tuple19<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip4L()
	{
		return Tuples.immutable(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> strip3R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public Tuple20<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip3L()
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> strip2R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public Tuple21<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip2L()
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip1R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple22<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip1L()
	{
		return Tuples.immutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	
	public <X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(X x)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(X x, Y y)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(X x, Y y, Z z)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <X> Tuple24<X, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> insert(X x)
	{
		return Tuples.immutable(x, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <X, Y> Tuple25<X, Y, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> insert(X x, Y y)
	{
		return Tuples.immutable(x, y, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <X, Y, Z> Tuple26<X, Y, Z, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> insert(X x, Y y, Z z)
	{
		return Tuples.immutable(x, y, z, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function1<A, RES> func)
	{
		return func.apply(a);
	}
	
	public <RES> RES applyR(Function1<W, RES> func)
	{
		return func.apply(w);
	}
	
	public <RES> RES applyL(Function2<A, B, RES> func)
	{
		return func.apply(a, b);
	}
	
	public <RES> RES applyR(Function2<V, W, RES> func)
	{
		return func.apply(v, w);
	}
	
	public <RES> RES applyL(Function3<A, B, C, RES> func)
	{
		return func.apply(a, b, c);
	}
	
	public <RES> RES applyR(Function3<U, V, W, RES> func)
	{
		return func.apply(u, v, w);
	}
	
	public <RES> RES applyL(Function4<A, B, C, D, RES> func)
	{
		return func.apply(a, b, c, d);
	}
	
	public <RES> RES applyR(Function4<T, U, V, W, RES> func)
	{
		return func.apply(t, u, v, w);
	}
	
	public <RES> RES applyL(Function5<A, B, C, D, E, RES> func)
	{
		return func.apply(a, b, c, d, e);
	}
	
	public <RES> RES applyR(Function5<S, T, U, V, W, RES> func)
	{
		return func.apply(s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function6<A, B, C, D, E, F, RES> func)
	{
		return func.apply(a, b, c, d, e, f);
	}
	
	public <RES> RES applyR(Function6<R, S, T, U, V, W, RES> func)
	{
		return func.apply(r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function7<A, B, C, D, E, F, G, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g);
	}
	
	public <RES> RES applyR(Function7<Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function8<A, B, C, D, E, F, G, H, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h);
	}
	
	public <RES> RES applyR(Function8<P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function9<A, B, C, D, E, F, G, H, I, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i);
	}
	
	public <RES> RES applyR(Function9<O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function10<A, B, C, D, E, F, G, H, I, J, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	public <RES> RES applyR(Function10<N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function11<A, B, C, D, E, F, G, H, I, J, K, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public <RES> RES applyR(Function11<M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public <RES> RES applyR(Function12<L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public <RES> RES applyR(Function13<K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <RES> RES applyR(Function14<J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <RES> RES applyR(Function15<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyR(Function16<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <RES> RES applyR(Function17<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <RES> RES applyR(Function18<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyR(Function19<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <RES> RES applyR(Function20<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <RES> RES applyR(Function21<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyL(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyR(Function22<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES apply(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer1<A> consumer)
	{
		consumer.accept(a);
	}
	
	public void acceptR(Consumer1<W> consumer)
	{
		consumer.accept(w);
	}
	
	public void acceptL(Consumer2<A, B> consumer)
	{
		consumer.accept(a, b);
	}
	
	public void acceptR(Consumer2<V, W> consumer)
	{
		consumer.accept(v, w);
	}
	
	public void acceptL(Consumer3<A, B, C> consumer)
	{
		consumer.accept(a, b, c);
	}
	
	public void acceptR(Consumer3<U, V, W> consumer)
	{
		consumer.accept(u, v, w);
	}
	
	public void acceptL(Consumer4<A, B, C, D> consumer)
	{
		consumer.accept(a, b, c, d);
	}
	
	public void acceptR(Consumer4<T, U, V, W> consumer)
	{
		consumer.accept(t, u, v, w);
	}
	
	public void acceptL(Consumer5<A, B, C, D, E> consumer)
	{
		consumer.accept(a, b, c, d, e);
	}
	
	public void acceptR(Consumer5<S, T, U, V, W> consumer)
	{
		consumer.accept(s, t, u, v, w);
	}
	
	public void acceptL(Consumer6<A, B, C, D, E, F> consumer)
	{
		consumer.accept(a, b, c, d, e, f);
	}
	
	public void acceptR(Consumer6<R, S, T, U, V, W> consumer)
	{
		consumer.accept(r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer7<A, B, C, D, E, F, G> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g);
	}
	
	public void acceptR(Consumer7<Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer8<A, B, C, D, E, F, G, H> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h);
	}
	
	public void acceptR(Consumer8<P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer9<A, B, C, D, E, F, G, H, I> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i);
	}
	
	public void acceptR(Consumer9<O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer10<A, B, C, D, E, F, G, H, I, J> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	public void acceptR(Consumer10<N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer11<A, B, C, D, E, F, G, H, I, J, K> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public void acceptR(Consumer11<M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public void acceptR(Consumer12<L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public void acceptR(Consumer13<K, L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public void acceptR(Consumer14<J, K, L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public void acceptR(Consumer15<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public void acceptR(Consumer16<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public void acceptR(Consumer17<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public void acceptR(Consumer18<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptR(Consumer19<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public void acceptR(Consumer20<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public void acceptR(Consumer21<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptL(Consumer22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptR(Consumer22<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void accept(Consumer23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public @Override int arity()
	{
		return 23;
	}
	
	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> mutable()
	{
		return new Tuple23.Mutable23<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> immutable()
	{
		return this;
	}
	
	public Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> copy()
	{
		return new Tuple23<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public @Override String toString()
	{
		return "Tuple23" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}
	
	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple23)) return false;
		ITuple tuple = (ITuple) other;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}
	
	public static class Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W>
			extends Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W>
	{
		public Mutable23(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			super(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> mutable()
		{
			return this;
		}
		
		public @Override Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> copy()
		{
			return new Tuple23.Mutable23<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> immutable()
		{
			return new Tuple23<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override String toString()
		{
			return "Tuple23.Mutable23" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setC(C c)
		{
			this.c = c;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setD(D d)
		{
			this.d = d;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setE(E e)
		{
			this.e = e;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setF(F f)
		{
			this.f = f;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setG(G g)
		{
			this.g = g;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setH(H h)
		{
			this.h = h;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setI(I i)
		{
			this.i = i;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setJ(J j)
		{
			this.j = j;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setK(K k)
		{
			this.k = k;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setL(L l)
		{
			this.l = l;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setM(M m)
		{
			this.m = m;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setN(N n)
		{
			this.n = n;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setO(O o)
		{
			this.o = o;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setP(P p)
		{
			this.p = p;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setQ(Q q)
		{
			this.q = q;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setR(R r)
		{
			this.r = r;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setS(S s)
		{
			this.s = s;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setT(T t)
		{
			this.t = t;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setU(U u)
		{
			this.u = u;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setV(V v)
		{
			this.v = v;
			return this;
		}
		
		public Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> setW(W w)
		{
			this.w = w;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip22R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<W> strip22L()
		{
			return Tuples.mutable(w);
		}
		
		public @Override Tuple2.Mutable2<A, B> strip21R()
		{
			return Tuples.mutable(a, b);
		}
		
		public @Override Tuple2.Mutable2<V, W> strip21L()
		{
			return Tuples.mutable(v, w);
		}
		
		public @Override Tuple3.Mutable3<A, B, C> strip20R()
		{
			return Tuples.mutable(a, b, c);
		}
		
		public @Override Tuple3.Mutable3<U, V, W> strip20L()
		{
			return Tuples.mutable(u, v, w);
		}
		
		public @Override Tuple4.Mutable4<A, B, C, D> strip19R()
		{
			return Tuples.mutable(a, b, c, d);
		}
		
		public @Override Tuple4.Mutable4<T, U, V, W> strip19L()
		{
			return Tuples.mutable(t, u, v, w);
		}
		
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip18R()
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override Tuple5.Mutable5<S, T, U, V, W> strip18L()
		{
			return Tuples.mutable(s, t, u, v, w);
		}
		
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip17R()
		{
			return Tuples.mutable(a, b, c, d, e, f);
		}
		
		public @Override Tuple6.Mutable6<R, S, T, U, V, W> strip17L()
		{
			return Tuples.mutable(r, s, t, u, v, w);
		}
		
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip16R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override Tuple7.Mutable7<Q, R, S, T, U, V, W> strip16L()
		{
			return Tuples.mutable(q, r, s, t, u, v, w);
		}
		
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip15R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h);
		}
		
		public @Override Tuple8.Mutable8<P, Q, R, S, T, U, V, W> strip15L()
		{
			return Tuples.mutable(p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip14R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override Tuple9.Mutable9<O, P, Q, R, S, T, U, V, W> strip14L()
		{
			return Tuples.mutable(o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> strip13R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override Tuple10.Mutable10<N, O, P, Q, R, S, T, U, V, W> strip13L()
		{
			return Tuples.mutable(n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> strip12R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override Tuple11.Mutable11<M, N, O, P, Q, R, S, T, U, V, W> strip12L()
		{
			return Tuples.mutable(m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> strip11R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override Tuple12.Mutable12<L, M, N, O, P, Q, R, S, T, U, V, W> strip11L()
		{
			return Tuples.mutable(l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip10R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override Tuple13.Mutable13<K, L, M, N, O, P, Q, R, S, T, U, V, W> strip10L()
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip9R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override Tuple14.Mutable14<J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip9L()
		{
			return Tuples.mutable(j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip8R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override Tuple15.Mutable15<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip8L()
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip7R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override Tuple16.Mutable16<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip7L()
		{
			return Tuples.mutable(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> strip6R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override Tuple17.Mutable17<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip6L()
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> strip5R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override Tuple18.Mutable18<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip5L()
		{
			return Tuples.mutable(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip4R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple19.Mutable19<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip4L()
		{
			return Tuples.mutable(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> strip3R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override Tuple20.Mutable20<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip3L()
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> strip2R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override Tuple21.Mutable21<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip2L()
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip1R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple22.Mutable22<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip1L()
		{
			return Tuples.mutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(X x)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override <X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(X x, Y y)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override <X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(X x, Y y, Z z)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override <X> Tuple24.Mutable24<X, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> insert(X x)
		{
			return Tuples.mutable(x, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <X, Y> Tuple25.Mutable25<X, Y, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> insert(X x, Y y)
		{
			return Tuples.mutable(x, y, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <X, Y, Z> Tuple26.Mutable26<X, Y, Z, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> insert(X x, Y y, Z z)
		{
			return Tuples.mutable(x, y, z, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
	}
}