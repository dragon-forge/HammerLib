package com.zeitheron.hammercore.utils.java.tuples;

import com.zeitheron.hammercore.utils.java.consumers.*;
import com.zeitheron.hammercore.utils.java.functions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>
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
	
	public Tuple1<A> strip18R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<S> strip18L()
	{
		return Tuples.immutable(s);
	}
	
	public Tuple2<A, B> strip17R()
	{
		return Tuples.immutable(a, b);
	}
	
	public Tuple2<R, S> strip17L()
	{
		return Tuples.immutable(r, s);
	}
	
	public Tuple3<A, B, C> strip16R()
	{
		return Tuples.immutable(a, b, c);
	}
	
	public Tuple3<Q, R, S> strip16L()
	{
		return Tuples.immutable(q, r, s);
	}
	
	public Tuple4<A, B, C, D> strip15R()
	{
		return Tuples.immutable(a, b, c, d);
	}
	
	public Tuple4<P, Q, R, S> strip15L()
	{
		return Tuples.immutable(p, q, r, s);
	}
	
	public Tuple5<A, B, C, D, E> strip14R()
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public Tuple5<O, P, Q, R, S> strip14L()
	{
		return Tuples.immutable(o, p, q, r, s);
	}
	
	public Tuple6<A, B, C, D, E, F> strip13R()
	{
		return Tuples.immutable(a, b, c, d, e, f);
	}
	
	public Tuple6<N, O, P, Q, R, S> strip13L()
	{
		return Tuples.immutable(n, o, p, q, r, s);
	}
	
	public Tuple7<A, B, C, D, E, F, G> strip12R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public Tuple7<M, N, O, P, Q, R, S> strip12L()
	{
		return Tuples.immutable(m, n, o, p, q, r, s);
	}
	
	public Tuple8<A, B, C, D, E, F, G, H> strip11R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h);
	}
	
	public Tuple8<L, M, N, O, P, Q, R, S> strip11L()
	{
		return Tuples.immutable(l, m, n, o, p, q, r, s);
	}
	
	public Tuple9<A, B, C, D, E, F, G, H, I> strip10R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public Tuple9<K, L, M, N, O, P, Q, R, S> strip10L()
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, s);
	}
	
	public Tuple10<A, B, C, D, E, F, G, H, I, J> strip9R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j);
	}
	
	public Tuple10<J, K, L, M, N, O, P, Q, R, S> strip9L()
	{
		return Tuples.immutable(j, k, l, m, n, o, p, q, r, s);
	}
	
	public Tuple11<A, B, C, D, E, F, G, H, I, J, K> strip8R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public Tuple11<I, J, K, L, M, N, O, P, Q, R, S> strip8L()
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> strip7R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public Tuple12<H, I, J, K, L, M, N, O, P, Q, R, S> strip7L()
	{
		return Tuples.immutable(h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip6R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public Tuple13<G, H, I, J, K, L, M, N, O, P, Q, R, S> strip6L()
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip5R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public Tuple14<F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip5L()
	{
		return Tuples.immutable(f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip4R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public Tuple15<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip4L()
	{
		return Tuples.immutable(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip3R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public Tuple16<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip3L()
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> strip2R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public Tuple17<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip2L()
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> strip1R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public Tuple18<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip1L()
	{
		return Tuples.immutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	
	public <T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(T t)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(T t, U u)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(T t, U u, V v)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(T t, U u, V v, W w)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <T> Tuple20<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t)
	{
		return Tuples.immutable(t, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <T, U> Tuple21<T, U, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t, U u)
	{
		return Tuples.immutable(t, u, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <T, U, V> Tuple22<T, U, V, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t, U u, V v)
	{
		return Tuples.immutable(t, u, v, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <T, U, V, W> Tuple23<T, U, V, W, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t, U u, V v, W w)
	{
		return Tuples.immutable(t, u, v, w, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <T, U, V, W, X> Tuple24<T, U, V, W, X, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(t, u, v, w, x, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <T, U, V, W, X, Y> Tuple25<T, U, V, W, X, Y, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(t, u, v, w, x, y, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <T, U, V, W, X, Y, Z> Tuple26<T, U, V, W, X, Y, Z, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(t, u, v, w, x, y, z, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function1<A, RES> func)
	{
		return func.apply(a);
	}
	
	public <RES> RES applyR(Function1<S, RES> func)
	{
		return func.apply(s);
	}
	
	public <RES> RES applyL(Function2<A, B, RES> func)
	{
		return func.apply(a, b);
	}
	
	public <RES> RES applyR(Function2<R, S, RES> func)
	{
		return func.apply(r, s);
	}
	
	public <RES> RES applyL(Function3<A, B, C, RES> func)
	{
		return func.apply(a, b, c);
	}
	
	public <RES> RES applyR(Function3<Q, R, S, RES> func)
	{
		return func.apply(q, r, s);
	}
	
	public <RES> RES applyL(Function4<A, B, C, D, RES> func)
	{
		return func.apply(a, b, c, d);
	}
	
	public <RES> RES applyR(Function4<P, Q, R, S, RES> func)
	{
		return func.apply(p, q, r, s);
	}
	
	public <RES> RES applyL(Function5<A, B, C, D, E, RES> func)
	{
		return func.apply(a, b, c, d, e);
	}
	
	public <RES> RES applyR(Function5<O, P, Q, R, S, RES> func)
	{
		return func.apply(o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function6<A, B, C, D, E, F, RES> func)
	{
		return func.apply(a, b, c, d, e, f);
	}
	
	public <RES> RES applyR(Function6<N, O, P, Q, R, S, RES> func)
	{
		return func.apply(n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function7<A, B, C, D, E, F, G, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g);
	}
	
	public <RES> RES applyR(Function7<M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function8<A, B, C, D, E, F, G, H, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h);
	}
	
	public <RES> RES applyR(Function8<L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function9<A, B, C, D, E, F, G, H, I, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i);
	}
	
	public <RES> RES applyR(Function9<K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function10<A, B, C, D, E, F, G, H, I, J, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	public <RES> RES applyR(Function10<J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function11<A, B, C, D, E, F, G, H, I, J, K, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public <RES> RES applyR(Function11<I, J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public <RES> RES applyR(Function12<H, I, J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public <RES> RES applyR(Function13<G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <RES> RES applyR(Function14<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <RES> RES applyR(Function15<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyR(Function16<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <RES> RES applyR(Function17<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyL(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <RES> RES applyR(Function18<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES apply(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer1<A> consumer)
	{
		consumer.accept(a);
	}
	
	public void acceptR(Consumer1<S> consumer)
	{
		consumer.accept(s);
	}
	
	public void acceptL(Consumer2<A, B> consumer)
	{
		consumer.accept(a, b);
	}
	
	public void acceptR(Consumer2<R, S> consumer)
	{
		consumer.accept(r, s);
	}
	
	public void acceptL(Consumer3<A, B, C> consumer)
	{
		consumer.accept(a, b, c);
	}
	
	public void acceptR(Consumer3<Q, R, S> consumer)
	{
		consumer.accept(q, r, s);
	}
	
	public void acceptL(Consumer4<A, B, C, D> consumer)
	{
		consumer.accept(a, b, c, d);
	}
	
	public void acceptR(Consumer4<P, Q, R, S> consumer)
	{
		consumer.accept(p, q, r, s);
	}
	
	public void acceptL(Consumer5<A, B, C, D, E> consumer)
	{
		consumer.accept(a, b, c, d, e);
	}
	
	public void acceptR(Consumer5<O, P, Q, R, S> consumer)
	{
		consumer.accept(o, p, q, r, s);
	}
	
	public void acceptL(Consumer6<A, B, C, D, E, F> consumer)
	{
		consumer.accept(a, b, c, d, e, f);
	}
	
	public void acceptR(Consumer6<N, O, P, Q, R, S> consumer)
	{
		consumer.accept(n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer7<A, B, C, D, E, F, G> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g);
	}
	
	public void acceptR(Consumer7<M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(m, n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer8<A, B, C, D, E, F, G, H> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h);
	}
	
	public void acceptR(Consumer8<L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(l, m, n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer9<A, B, C, D, E, F, G, H, I> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i);
	}
	
	public void acceptR(Consumer9<K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer10<A, B, C, D, E, F, G, H, I, J> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	public void acceptR(Consumer10<J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(j, k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer11<A, B, C, D, E, F, G, H, I, J, K> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public void acceptR(Consumer11<I, J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public void acceptR(Consumer12<H, I, J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public void acceptR(Consumer13<G, H, I, J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public void acceptR(Consumer14<F, G, H, I, J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public void acceptR(Consumer15<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public void acceptR(Consumer16<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public void acceptR(Consumer17<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptL(Consumer18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public void acceptR(Consumer18<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public void accept(Consumer19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
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
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple19)) return false;
		ITuple tuple = (ITuple) other;
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
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setC(C c)
		{
			this.c = c;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setD(D d)
		{
			this.d = d;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setE(E e)
		{
			this.e = e;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setF(F f)
		{
			this.f = f;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setG(G g)
		{
			this.g = g;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setH(H h)
		{
			this.h = h;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setI(I i)
		{
			this.i = i;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setJ(J j)
		{
			this.j = j;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setK(K k)
		{
			this.k = k;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setL(L l)
		{
			this.l = l;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setM(M m)
		{
			this.m = m;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setN(N n)
		{
			this.n = n;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setO(O o)
		{
			this.o = o;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setP(P p)
		{
			this.p = p;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setQ(Q q)
		{
			this.q = q;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setR(R r)
		{
			this.r = r;
			return this;
		}
		
		public Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> setS(S s)
		{
			this.s = s;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip18R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<S> strip18L()
		{
			return Tuples.mutable(s);
		}
		
		public @Override Tuple2.Mutable2<A, B> strip17R()
		{
			return Tuples.mutable(a, b);
		}
		
		public @Override Tuple2.Mutable2<R, S> strip17L()
		{
			return Tuples.mutable(r, s);
		}
		
		public @Override Tuple3.Mutable3<A, B, C> strip16R()
		{
			return Tuples.mutable(a, b, c);
		}
		
		public @Override Tuple3.Mutable3<Q, R, S> strip16L()
		{
			return Tuples.mutable(q, r, s);
		}
		
		public @Override Tuple4.Mutable4<A, B, C, D> strip15R()
		{
			return Tuples.mutable(a, b, c, d);
		}
		
		public @Override Tuple4.Mutable4<P, Q, R, S> strip15L()
		{
			return Tuples.mutable(p, q, r, s);
		}
		
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip14R()
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override Tuple5.Mutable5<O, P, Q, R, S> strip14L()
		{
			return Tuples.mutable(o, p, q, r, s);
		}
		
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip13R()
		{
			return Tuples.mutable(a, b, c, d, e, f);
		}
		
		public @Override Tuple6.Mutable6<N, O, P, Q, R, S> strip13L()
		{
			return Tuples.mutable(n, o, p, q, r, s);
		}
		
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip12R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override Tuple7.Mutable7<M, N, O, P, Q, R, S> strip12L()
		{
			return Tuples.mutable(m, n, o, p, q, r, s);
		}
		
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip11R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h);
		}
		
		public @Override Tuple8.Mutable8<L, M, N, O, P, Q, R, S> strip11L()
		{
			return Tuples.mutable(l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip10R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override Tuple9.Mutable9<K, L, M, N, O, P, Q, R, S> strip10L()
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> strip9R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override Tuple10.Mutable10<J, K, L, M, N, O, P, Q, R, S> strip9L()
		{
			return Tuples.mutable(j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> strip8R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override Tuple11.Mutable11<I, J, K, L, M, N, O, P, Q, R, S> strip8L()
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> strip7R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override Tuple12.Mutable12<H, I, J, K, L, M, N, O, P, Q, R, S> strip7L()
		{
			return Tuples.mutable(h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip6R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override Tuple13.Mutable13<G, H, I, J, K, L, M, N, O, P, Q, R, S> strip6L()
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip5R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override Tuple14.Mutable14<F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip5L()
		{
			return Tuples.mutable(f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip4R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override Tuple15.Mutable15<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip4L()
		{
			return Tuples.mutable(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip3R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override Tuple16.Mutable16<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip3L()
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> strip2R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override Tuple17.Mutable17<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip2L()
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> strip1R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override Tuple18.Mutable18<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip1L()
		{
			return Tuples.mutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(T t)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override <T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(T t, U u)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override <T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(T t, U u, V v)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(T t, U u, V v, W w)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override <T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override <T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override <T> Tuple20.Mutable20<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t)
		{
			return Tuples.mutable(t, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <T, U> Tuple21.Mutable21<T, U, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t, U u)
		{
			return Tuples.mutable(t, u, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <T, U, V> Tuple22.Mutable22<T, U, V, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t, U u, V v)
		{
			return Tuples.mutable(t, u, v, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <T, U, V, W> Tuple23.Mutable23<T, U, V, W, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t, U u, V v, W w)
		{
			return Tuples.mutable(t, u, v, w, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <T, U, V, W, X> Tuple24.Mutable24<T, U, V, W, X, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(t, u, v, w, x, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <T, U, V, W, X, Y> Tuple25.Mutable25<T, U, V, W, X, Y, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(t, u, v, w, x, y, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <T, U, V, W, X, Y, Z> Tuple26.Mutable26<T, U, V, W, X, Y, Z, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> insert(T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(t, u, v, w, x, y, z, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
	}
}