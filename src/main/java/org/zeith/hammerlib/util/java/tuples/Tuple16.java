package org.zeith.hammerlib.util.java.tuples;

import org.zeith.hammerlib.util.java.consumers.*;
import org.zeith.hammerlib.util.java.functions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>
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
	
	public Tuple16(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
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
	
	public Tuple1<A> strip15R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<P> strip15L()
	{
		return Tuples.immutable(p);
	}
	
	public Tuple2<A, B> strip14R()
	{
		return Tuples.immutable(a, b);
	}
	
	public Tuple2<O, P> strip14L()
	{
		return Tuples.immutable(o, p);
	}
	
	public Tuple3<A, B, C> strip13R()
	{
		return Tuples.immutable(a, b, c);
	}
	
	public Tuple3<N, O, P> strip13L()
	{
		return Tuples.immutable(n, o, p);
	}
	
	public Tuple4<A, B, C, D> strip12R()
	{
		return Tuples.immutable(a, b, c, d);
	}
	
	public Tuple4<M, N, O, P> strip12L()
	{
		return Tuples.immutable(m, n, o, p);
	}
	
	public Tuple5<A, B, C, D, E> strip11R()
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public Tuple5<L, M, N, O, P> strip11L()
	{
		return Tuples.immutable(l, m, n, o, p);
	}
	
	public Tuple6<A, B, C, D, E, F> strip10R()
	{
		return Tuples.immutable(a, b, c, d, e, f);
	}
	
	public Tuple6<K, L, M, N, O, P> strip10L()
	{
		return Tuples.immutable(k, l, m, n, o, p);
	}
	
	public Tuple7<A, B, C, D, E, F, G> strip9R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public Tuple7<J, K, L, M, N, O, P> strip9L()
	{
		return Tuples.immutable(j, k, l, m, n, o, p);
	}
	
	public Tuple8<A, B, C, D, E, F, G, H> strip8R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h);
	}
	
	public Tuple8<I, J, K, L, M, N, O, P> strip8L()
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p);
	}
	
	public Tuple9<A, B, C, D, E, F, G, H, I> strip7R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public Tuple9<H, I, J, K, L, M, N, O, P> strip7L()
	{
		return Tuples.immutable(h, i, j, k, l, m, n, o, p);
	}
	
	public Tuple10<A, B, C, D, E, F, G, H, I, J> strip6R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j);
	}
	
	public Tuple10<G, H, I, J, K, L, M, N, O, P> strip6L()
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p);
	}
	
	public Tuple11<A, B, C, D, E, F, G, H, I, J, K> strip5R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public Tuple11<F, G, H, I, J, K, L, M, N, O, P> strip5L()
	{
		return Tuples.immutable(f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> strip4R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public Tuple12<E, F, G, H, I, J, K, L, M, N, O, P> strip4L()
	{
		return Tuples.immutable(e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip3R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public Tuple13<D, E, F, G, H, I, J, K, L, M, N, O, P> strip3L()
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip2R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public Tuple14<C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip2L()
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip1R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public Tuple15<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip1L()
	{
		return Tuples.immutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	
	public <Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(Q q)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(Q q, R r)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(Q q, R r, S s)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(Q q, R r, S s, T t)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <Q> Tuple17<Q, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q)
	{
		return Tuples.immutable(q, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <Q, R> Tuple18<Q, R, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r)
	{
		return Tuples.immutable(q, r, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <Q, R, S> Tuple19<Q, R, S, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s)
	{
		return Tuples.immutable(q, r, s, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <Q, R, S, T> Tuple20<Q, R, S, T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t)
	{
		return Tuples.immutable(q, r, s, t, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <Q, R, S, T, U> Tuple21<Q, R, S, T, U, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(q, r, s, t, u, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <Q, R, S, T, U, V> Tuple22<Q, R, S, T, U, V, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(q, r, s, t, u, v, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <Q, R, S, T, U, V, W> Tuple23<Q, R, S, T, U, V, W, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(q, r, s, t, u, v, w, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <Q, R, S, T, U, V, W, X> Tuple24<Q, R, S, T, U, V, W, X, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(q, r, s, t, u, v, w, x, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <Q, R, S, T, U, V, W, X, Y> Tuple25<Q, R, S, T, U, V, W, X, Y, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(q, r, s, t, u, v, w, x, y, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <Q, R, S, T, U, V, W, X, Y, Z> Tuple26<Q, R, S, T, U, V, W, X, Y, Z, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(q, r, s, t, u, v, w, x, y, z, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyL(Function1<A, RES> func)
	{
		return func.apply(a);
	}
	
	public <RES> RES applyR(Function1<P, RES> func)
	{
		return func.apply(p);
	}
	
	public <RES> RES applyL(Function2<A, B, RES> func)
	{
		return func.apply(a, b);
	}
	
	public <RES> RES applyR(Function2<O, P, RES> func)
	{
		return func.apply(o, p);
	}
	
	public <RES> RES applyL(Function3<A, B, C, RES> func)
	{
		return func.apply(a, b, c);
	}
	
	public <RES> RES applyR(Function3<N, O, P, RES> func)
	{
		return func.apply(n, o, p);
	}
	
	public <RES> RES applyL(Function4<A, B, C, D, RES> func)
	{
		return func.apply(a, b, c, d);
	}
	
	public <RES> RES applyR(Function4<M, N, O, P, RES> func)
	{
		return func.apply(m, n, o, p);
	}
	
	public <RES> RES applyL(Function5<A, B, C, D, E, RES> func)
	{
		return func.apply(a, b, c, d, e);
	}
	
	public <RES> RES applyR(Function5<L, M, N, O, P, RES> func)
	{
		return func.apply(l, m, n, o, p);
	}
	
	public <RES> RES applyL(Function6<A, B, C, D, E, F, RES> func)
	{
		return func.apply(a, b, c, d, e, f);
	}
	
	public <RES> RES applyR(Function6<K, L, M, N, O, P, RES> func)
	{
		return func.apply(k, l, m, n, o, p);
	}
	
	public <RES> RES applyL(Function7<A, B, C, D, E, F, G, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g);
	}
	
	public <RES> RES applyR(Function7<J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyL(Function8<A, B, C, D, E, F, G, H, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h);
	}
	
	public <RES> RES applyR(Function8<I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyL(Function9<A, B, C, D, E, F, G, H, I, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i);
	}
	
	public <RES> RES applyR(Function9<H, I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(h, i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyL(Function10<A, B, C, D, E, F, G, H, I, J, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	public <RES> RES applyR(Function10<G, H, I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(g, h, i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyL(Function11<A, B, C, D, E, F, G, H, I, J, K, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public <RES> RES applyR(Function11<F, G, H, I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyL(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public <RES> RES applyR(Function12<E, F, G, H, I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyL(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public <RES> RES applyR(Function13<D, E, F, G, H, I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyL(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <RES> RES applyR(Function14<C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyL(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <RES> RES applyR(Function15<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES apply(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public void acceptL(Consumer1<A> consumer)
	{
		consumer.accept(a);
	}
	
	public void acceptR(Consumer1<P> consumer)
	{
		consumer.accept(p);
	}
	
	public void acceptL(Consumer2<A, B> consumer)
	{
		consumer.accept(a, b);
	}
	
	public void acceptR(Consumer2<O, P> consumer)
	{
		consumer.accept(o, p);
	}
	
	public void acceptL(Consumer3<A, B, C> consumer)
	{
		consumer.accept(a, b, c);
	}
	
	public void acceptR(Consumer3<N, O, P> consumer)
	{
		consumer.accept(n, o, p);
	}
	
	public void acceptL(Consumer4<A, B, C, D> consumer)
	{
		consumer.accept(a, b, c, d);
	}
	
	public void acceptR(Consumer4<M, N, O, P> consumer)
	{
		consumer.accept(m, n, o, p);
	}
	
	public void acceptL(Consumer5<A, B, C, D, E> consumer)
	{
		consumer.accept(a, b, c, d, e);
	}
	
	public void acceptR(Consumer5<L, M, N, O, P> consumer)
	{
		consumer.accept(l, m, n, o, p);
	}
	
	public void acceptL(Consumer6<A, B, C, D, E, F> consumer)
	{
		consumer.accept(a, b, c, d, e, f);
	}
	
	public void acceptR(Consumer6<K, L, M, N, O, P> consumer)
	{
		consumer.accept(k, l, m, n, o, p);
	}
	
	public void acceptL(Consumer7<A, B, C, D, E, F, G> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g);
	}
	
	public void acceptR(Consumer7<J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(j, k, l, m, n, o, p);
	}
	
	public void acceptL(Consumer8<A, B, C, D, E, F, G, H> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h);
	}
	
	public void acceptR(Consumer8<I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(i, j, k, l, m, n, o, p);
	}
	
	public void acceptL(Consumer9<A, B, C, D, E, F, G, H, I> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i);
	}
	
	public void acceptR(Consumer9<H, I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(h, i, j, k, l, m, n, o, p);
	}
	
	public void acceptL(Consumer10<A, B, C, D, E, F, G, H, I, J> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	public void acceptR(Consumer10<G, H, I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(g, h, i, j, k, l, m, n, o, p);
	}
	
	public void acceptL(Consumer11<A, B, C, D, E, F, G, H, I, J, K> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public void acceptR(Consumer11<F, G, H, I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public void acceptL(Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public void acceptR(Consumer12<E, F, G, H, I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public void acceptL(Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public void acceptR(Consumer13<D, E, F, G, H, I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public void acceptL(Consumer14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public void acceptR(Consumer14<C, D, E, F, G, H, I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public void acceptL(Consumer15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public void acceptR(Consumer15<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public void accept(Consumer16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public @Override int arity()
	{
		return 16;
	}
	
	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> mutable()
	{
		return new Mutable16<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> immutable()
	{
		return this;
	}
	
	public Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> copy()
	{
		return new Tuple16<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public @Override String toString()
	{
		return "Tuple16" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}
	
	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof ITuple)) return false;
		ITuple tuple = (ITuple) other;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}
	
	public static class Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>
			extends Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>
	{
		public Mutable16(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
		{
			super(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> mutable()
		{
			return this;
		}
		
		public @Override Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> copy()
		{
			return new Mutable16<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> immutable()
		{
			return new Tuple16<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override String toString()
		{
			return "Tuple16.Mutable16" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setC(C c)
		{
			this.c = c;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setD(D d)
		{
			this.d = d;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setE(E e)
		{
			this.e = e;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setF(F f)
		{
			this.f = f;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setG(G g)
		{
			this.g = g;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setH(H h)
		{
			this.h = h;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setI(I i)
		{
			this.i = i;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setJ(J j)
		{
			this.j = j;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setK(K k)
		{
			this.k = k;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setL(L l)
		{
			this.l = l;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setM(M m)
		{
			this.m = m;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setN(N n)
		{
			this.n = n;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setO(O o)
		{
			this.o = o;
			return this;
		}
		
		public Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> setP(P p)
		{
			this.p = p;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip15R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<P> strip15L()
		{
			return Tuples.mutable(p);
		}
		
		public @Override Tuple2.Mutable2<A, B> strip14R()
		{
			return Tuples.mutable(a, b);
		}
		
		public @Override Tuple2.Mutable2<O, P> strip14L()
		{
			return Tuples.mutable(o, p);
		}
		
		public @Override Tuple3.Mutable3<A, B, C> strip13R()
		{
			return Tuples.mutable(a, b, c);
		}
		
		public @Override Tuple3.Mutable3<N, O, P> strip13L()
		{
			return Tuples.mutable(n, o, p);
		}
		
		public @Override Tuple4.Mutable4<A, B, C, D> strip12R()
		{
			return Tuples.mutable(a, b, c, d);
		}
		
		public @Override Tuple4.Mutable4<M, N, O, P> strip12L()
		{
			return Tuples.mutable(m, n, o, p);
		}
		
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip11R()
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override Tuple5.Mutable5<L, M, N, O, P> strip11L()
		{
			return Tuples.mutable(l, m, n, o, p);
		}
		
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip10R()
		{
			return Tuples.mutable(a, b, c, d, e, f);
		}
		
		public @Override Tuple6.Mutable6<K, L, M, N, O, P> strip10L()
		{
			return Tuples.mutable(k, l, m, n, o, p);
		}
		
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip9R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override Tuple7.Mutable7<J, K, L, M, N, O, P> strip9L()
		{
			return Tuples.mutable(j, k, l, m, n, o, p);
		}
		
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip8R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h);
		}
		
		public @Override Tuple8.Mutable8<I, J, K, L, M, N, O, P> strip8L()
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p);
		}
		
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip7R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override Tuple9.Mutable9<H, I, J, K, L, M, N, O, P> strip7L()
		{
			return Tuples.mutable(h, i, j, k, l, m, n, o, p);
		}
		
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> strip6R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override Tuple10.Mutable10<G, H, I, J, K, L, M, N, O, P> strip6L()
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> strip5R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override Tuple11.Mutable11<F, G, H, I, J, K, L, M, N, O, P> strip5L()
		{
			return Tuples.mutable(f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> strip4R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override Tuple12.Mutable12<E, F, G, H, I, J, K, L, M, N, O, P> strip4L()
		{
			return Tuples.mutable(e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip3R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override Tuple13.Mutable13<D, E, F, G, H, I, J, K, L, M, N, O, P> strip3L()
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip2R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override Tuple14.Mutable14<C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip2L()
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip1R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override Tuple15.Mutable15<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip1L()
		{
			return Tuples.mutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(Q q)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override <Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(Q q, R r)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override <Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(Q q, R r, S s)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(Q q, R r, S s, T t)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override <Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override <Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override <Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override <Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override <Q> Tuple17.Mutable17<Q, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q)
		{
			return Tuples.mutable(q, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <Q, R> Tuple18.Mutable18<Q, R, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r)
		{
			return Tuples.mutable(q, r, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <Q, R, S> Tuple19.Mutable19<Q, R, S, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s)
		{
			return Tuples.mutable(q, r, s, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <Q, R, S, T> Tuple20.Mutable20<Q, R, S, T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t)
		{
			return Tuples.mutable(q, r, s, t, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <Q, R, S, T, U> Tuple21.Mutable21<Q, R, S, T, U, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(q, r, s, t, u, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <Q, R, S, T, U, V> Tuple22.Mutable22<Q, R, S, T, U, V, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(q, r, s, t, u, v, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <Q, R, S, T, U, V, W> Tuple23.Mutable23<Q, R, S, T, U, V, W, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(q, r, s, t, u, v, w, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <Q, R, S, T, U, V, W, X> Tuple24.Mutable24<Q, R, S, T, U, V, W, X, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(q, r, s, t, u, v, w, x, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<Q, R, S, T, U, V, W, X, Y, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(q, r, s, t, u, v, w, x, y, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<Q, R, S, T, U, V, W, X, Y, Z, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> insert(Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(q, r, s, t, u, v, w, x, y, z, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
	}
}