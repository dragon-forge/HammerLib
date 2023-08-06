package com.zeitheron.hammercore.utils.java.tuples;

import com.zeitheron.hammercore.utils.java.consumers.*;
import com.zeitheron.hammercore.utils.java.functions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple8<A, B, C, D, E, F, G, H>
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
	
	public Tuple8(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.g = g;
		this.h = h;
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
	
	public Tuple1<A> strip7R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<H> strip7L()
	{
		return Tuples.immutable(h);
	}
	
	public Tuple2<A, B> strip6R()
	{
		return Tuples.immutable(a, b);
	}
	
	public Tuple2<G, H> strip6L()
	{
		return Tuples.immutable(g, h);
	}
	
	public Tuple3<A, B, C> strip5R()
	{
		return Tuples.immutable(a, b, c);
	}
	
	public Tuple3<F, G, H> strip5L()
	{
		return Tuples.immutable(f, g, h);
	}
	
	public Tuple4<A, B, C, D> strip4R()
	{
		return Tuples.immutable(a, b, c, d);
	}
	
	public Tuple4<E, F, G, H> strip4L()
	{
		return Tuples.immutable(e, f, g, h);
	}
	
	public Tuple5<A, B, C, D, E> strip3R()
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public Tuple5<D, E, F, G, H> strip3L()
	{
		return Tuples.immutable(d, e, f, g, h);
	}
	
	public Tuple6<A, B, C, D, E, F> strip2R()
	{
		return Tuples.immutable(a, b, c, d, e, f);
	}
	
	public Tuple6<C, D, E, F, G, H> strip2L()
	{
		return Tuples.immutable(c, d, e, f, g, h);
	}
	
	public Tuple7<A, B, C, D, E, F, G> strip1R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public Tuple7<B, C, D, E, F, G, H> strip1L()
	{
		return Tuples.immutable(b, c, d, e, f, g, h);
	}
	
	
	public <I> Tuple9<A, B, C, D, E, F, G, H, I> add(I i)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public <I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(I i, J j)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j);
	}
	
	public <I, J, K> Tuple11<A, B, C, D, E, F, G, H, I, J, K> add(I i, J j, K k)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public <I, J, K, L> Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> add(I i, J j, K k, L l)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public <I, J, K, L, M> Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(I i, J j, K k, L l, M m)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public <I, J, K, L, M, N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(I i, J j, K k, L l, M m, N n)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <I, J, K, L, M, N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(I i, J j, K k, L l, M m, N n, O o)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <I, J, K, L, M, N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <I, J, K, L, M, N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <I> Tuple9<I, A, B, C, D, E, F, G, H> insert(I i)
	{
		return Tuples.immutable(i, a, b, c, d, e, f, g, h);
	}
	
	public <I, J> Tuple10<I, J, A, B, C, D, E, F, G, H> insert(I i, J j)
	{
		return Tuples.immutable(i, j, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K> Tuple11<I, J, K, A, B, C, D, E, F, G, H> insert(I i, J j, K k)
	{
		return Tuples.immutable(i, j, k, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L> Tuple12<I, J, K, L, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l)
	{
		return Tuples.immutable(i, j, k, l, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M> Tuple13<I, J, K, L, M, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m)
	{
		return Tuples.immutable(i, j, k, l, m, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N> Tuple14<I, J, K, L, M, N, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n)
	{
		return Tuples.immutable(i, j, k, l, m, n, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N, O> Tuple15<I, J, K, L, M, N, O, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o)
	{
		return Tuples.immutable(i, j, k, l, m, n, o, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N, O, P> Tuple16<I, J, K, L, M, N, O, P, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N, O, P, Q> Tuple17<I, J, K, L, M, N, O, P, Q, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R> Tuple18<I, J, K, L, M, N, O, P, Q, R, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S> Tuple19<I, J, K, L, M, N, O, P, Q, R, S, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20<I, J, K, L, M, N, O, P, Q, R, S, T, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s, t, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21<I, J, K, L, M, N, O, P, Q, R, S, T, U, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s, t, u, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, a, b, c, d, e, f, g, h);
	}
	
	public <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, a, b, c, d, e, f, g, h);
	}
	
	public <RES> RES applyL(Function1<A, RES> func)
	{
		return func.apply(a);
	}
	
	public <RES> RES applyR(Function1<H, RES> func)
	{
		return func.apply(h);
	}
	
	public <RES> RES applyL(Function2<A, B, RES> func)
	{
		return func.apply(a, b);
	}
	
	public <RES> RES applyR(Function2<G, H, RES> func)
	{
		return func.apply(g, h);
	}
	
	public <RES> RES applyL(Function3<A, B, C, RES> func)
	{
		return func.apply(a, b, c);
	}
	
	public <RES> RES applyR(Function3<F, G, H, RES> func)
	{
		return func.apply(f, g, h);
	}
	
	public <RES> RES applyL(Function4<A, B, C, D, RES> func)
	{
		return func.apply(a, b, c, d);
	}
	
	public <RES> RES applyR(Function4<E, F, G, H, RES> func)
	{
		return func.apply(e, f, g, h);
	}
	
	public <RES> RES applyL(Function5<A, B, C, D, E, RES> func)
	{
		return func.apply(a, b, c, d, e);
	}
	
	public <RES> RES applyR(Function5<D, E, F, G, H, RES> func)
	{
		return func.apply(d, e, f, g, h);
	}
	
	public <RES> RES applyL(Function6<A, B, C, D, E, F, RES> func)
	{
		return func.apply(a, b, c, d, e, f);
	}
	
	public <RES> RES applyR(Function6<C, D, E, F, G, H, RES> func)
	{
		return func.apply(c, d, e, f, g, h);
	}
	
	public <RES> RES applyL(Function7<A, B, C, D, E, F, G, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g);
	}
	
	public <RES> RES applyR(Function7<B, C, D, E, F, G, H, RES> func)
	{
		return func.apply(b, c, d, e, f, g, h);
	}
	
	public <RES> RES apply(Function8<A, B, C, D, E, F, G, H, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h);
	}
	
	public void acceptL(Consumer1<A> consumer)
	{
		consumer.accept(a);
	}
	
	public void acceptR(Consumer1<H> consumer)
	{
		consumer.accept(h);
	}
	
	public void acceptL(Consumer2<A, B> consumer)
	{
		consumer.accept(a, b);
	}
	
	public void acceptR(Consumer2<G, H> consumer)
	{
		consumer.accept(g, h);
	}
	
	public void acceptL(Consumer3<A, B, C> consumer)
	{
		consumer.accept(a, b, c);
	}
	
	public void acceptR(Consumer3<F, G, H> consumer)
	{
		consumer.accept(f, g, h);
	}
	
	public void acceptL(Consumer4<A, B, C, D> consumer)
	{
		consumer.accept(a, b, c, d);
	}
	
	public void acceptR(Consumer4<E, F, G, H> consumer)
	{
		consumer.accept(e, f, g, h);
	}
	
	public void acceptL(Consumer5<A, B, C, D, E> consumer)
	{
		consumer.accept(a, b, c, d, e);
	}
	
	public void acceptR(Consumer5<D, E, F, G, H> consumer)
	{
		consumer.accept(d, e, f, g, h);
	}
	
	public void acceptL(Consumer6<A, B, C, D, E, F> consumer)
	{
		consumer.accept(a, b, c, d, e, f);
	}
	
	public void acceptR(Consumer6<C, D, E, F, G, H> consumer)
	{
		consumer.accept(c, d, e, f, g, h);
	}
	
	public void acceptL(Consumer7<A, B, C, D, E, F, G> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g);
	}
	
	public void acceptR(Consumer7<B, C, D, E, F, G, H> consumer)
	{
		consumer.accept(b, c, d, e, f, g, h);
	}
	
	public void accept(Consumer8<A, B, C, D, E, F, G, H> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h);
	}
	
	public @Override int arity()
	{
		return 8;
	}
	
	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h);
	}
	
	public Tuple8.Mutable8<A, B, C, D, E, F, G, H> mutable()
	{
		return new Tuple8.Mutable8<>(a, b, c, d, e, f, g, h);
	}
	
	public Tuple8<A, B, C, D, E, F, G, H> immutable()
	{
		return this;
	}
	
	public Tuple8<A, B, C, D, E, F, G, H> copy()
	{
		return new Tuple8<>(a, b, c, d, e, f, g, h);
	}
	
	public @Override String toString()
	{
		return "Tuple8" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}
	
	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h);
	}
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple8)) return false;
		Tuple8 tuple = (Tuple8) other;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}
	
	public static class Mutable8<A, B, C, D, E, F, G, H>
			extends Tuple8<A, B, C, D, E, F, G, H>
	{
		public Mutable8(A a, B b, C c, D d, E e, F f, G g, H h)
		{
			super(a, b, c, d, e, f, g, h);
		}
		
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> mutable()
		{
			return this;
		}
		
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> copy()
		{
			return new Tuple8.Mutable8<>(a, b, c, d, e, f, g, h);
		}
		
		public @Override Tuple8<A, B, C, D, E, F, G, H> immutable()
		{
			return new Tuple8<>(a, b, c, d, e, f, g, h);
		}
		
		public @Override String toString()
		{
			return "Tuple8.Mutable8" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
		}
		
		public Mutable8<A, B, C, D, E, F, G, H> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable8<A, B, C, D, E, F, G, H> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public Mutable8<A, B, C, D, E, F, G, H> setC(C c)
		{
			this.c = c;
			return this;
		}
		
		public Mutable8<A, B, C, D, E, F, G, H> setD(D d)
		{
			this.d = d;
			return this;
		}
		
		public Mutable8<A, B, C, D, E, F, G, H> setE(E e)
		{
			this.e = e;
			return this;
		}
		
		public Mutable8<A, B, C, D, E, F, G, H> setF(F f)
		{
			this.f = f;
			return this;
		}
		
		public Mutable8<A, B, C, D, E, F, G, H> setG(G g)
		{
			this.g = g;
			return this;
		}
		
		public Mutable8<A, B, C, D, E, F, G, H> setH(H h)
		{
			this.h = h;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip7R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<H> strip7L()
		{
			return Tuples.mutable(h);
		}
		
		public @Override Tuple2.Mutable2<A, B> strip6R()
		{
			return Tuples.mutable(a, b);
		}
		
		public @Override Tuple2.Mutable2<G, H> strip6L()
		{
			return Tuples.mutable(g, h);
		}
		
		public @Override Tuple3.Mutable3<A, B, C> strip5R()
		{
			return Tuples.mutable(a, b, c);
		}
		
		public @Override Tuple3.Mutable3<F, G, H> strip5L()
		{
			return Tuples.mutable(f, g, h);
		}
		
		public @Override Tuple4.Mutable4<A, B, C, D> strip4R()
		{
			return Tuples.mutable(a, b, c, d);
		}
		
		public @Override Tuple4.Mutable4<E, F, G, H> strip4L()
		{
			return Tuples.mutable(e, f, g, h);
		}
		
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip3R()
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override Tuple5.Mutable5<D, E, F, G, H> strip3L()
		{
			return Tuples.mutable(d, e, f, g, h);
		}
		
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip2R()
		{
			return Tuples.mutable(a, b, c, d, e, f);
		}
		
		public @Override Tuple6.Mutable6<C, D, E, F, G, H> strip2L()
		{
			return Tuples.mutable(c, d, e, f, g, h);
		}
		
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip1R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override Tuple7.Mutable7<B, C, D, E, F, G, H> strip1L()
		{
			return Tuples.mutable(b, c, d, e, f, g, h);
		}
		
		public @Override <I> Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> add(I i)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override <I, J> Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> add(I i, J j)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <I, J, K> Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> add(I i, J j, K k)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override <I, J, K, L> Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> add(I i, J j, K k, L l)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override <I, J, K, L, M> Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(I i, J j, K k, L l, M m)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override <I, J, K, L, M, N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(I i, J j, K k, L l, M m, N n)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override <I, J, K, L, M, N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(I i, J j, K k, L l, M m, N n, O o)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override <I, J, K, L, M, N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(I i, J j, K k, L l, M m, N n, O o, P p)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override <I> Tuple9.Mutable9<I, A, B, C, D, E, F, G, H> insert(I i)
		{
			return Tuples.mutable(i, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J> Tuple10.Mutable10<I, J, A, B, C, D, E, F, G, H> insert(I i, J j)
		{
			return Tuples.mutable(i, j, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K> Tuple11.Mutable11<I, J, K, A, B, C, D, E, F, G, H> insert(I i, J j, K k)
		{
			return Tuples.mutable(i, j, k, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L> Tuple12.Mutable12<I, J, K, L, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l)
		{
			return Tuples.mutable(i, j, k, l, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M> Tuple13.Mutable13<I, J, K, L, M, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m)
		{
			return Tuples.mutable(i, j, k, l, m, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N> Tuple14.Mutable14<I, J, K, L, M, N, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n)
		{
			return Tuples.mutable(i, j, k, l, m, n, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N, O> Tuple15.Mutable15<I, J, K, L, M, N, O, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o)
		{
			return Tuples.mutable(i, j, k, l, m, n, o, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N, O, P> Tuple16.Mutable16<I, J, K, L, M, N, O, P, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p)
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q> Tuple17.Mutable17<I, J, K, L, M, N, O, P, Q, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q)
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R> Tuple18.Mutable18<I, J, K, L, M, N, O, P, Q, R, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<I, J, K, L, M, N, O, P, Q, R, S, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<I, J, K, L, M, N, O, P, Q, R, S, T, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s, t, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<I, J, K, L, M, N, O, P, Q, R, S, T, U, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s, t, u, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, a, b, c, d, e, f, g, h);
		}
		
		public @Override <I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, A, B, C, D, E, F, G, H> insert(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, a, b, c, d, e, f, g, h);
		}
	}
}