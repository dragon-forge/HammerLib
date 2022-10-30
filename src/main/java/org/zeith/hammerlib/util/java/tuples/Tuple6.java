package org.zeith.hammerlib.util.java.tuples;

import org.zeith.hammerlib.util.java.consumers.*;
import org.zeith.hammerlib.util.java.functions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple6<A, B, C, D, E, F>
		implements ITuple
{
	protected A a;
	protected B b;
	protected C c;
	protected D d;
	protected E e;
	protected F f;
	
	public Tuple6(A a, B b, C c, D d, E e, F f)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
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
	
	public Tuple1<A> strip5R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<F> strip5L()
	{
		return Tuples.immutable(f);
	}
	
	public Tuple2<A, B> strip4R()
	{
		return Tuples.immutable(a, b);
	}
	
	public Tuple2<E, F> strip4L()
	{
		return Tuples.immutable(e, f);
	}
	
	public Tuple3<A, B, C> strip3R()
	{
		return Tuples.immutable(a, b, c);
	}
	
	public Tuple3<D, E, F> strip3L()
	{
		return Tuples.immutable(d, e, f);
	}
	
	public Tuple4<A, B, C, D> strip2R()
	{
		return Tuples.immutable(a, b, c, d);
	}
	
	public Tuple4<C, D, E, F> strip2L()
	{
		return Tuples.immutable(c, d, e, f);
	}
	
	public Tuple5<A, B, C, D, E> strip1R()
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public Tuple5<B, C, D, E, F> strip1L()
	{
		return Tuples.immutable(b, c, d, e, f);
	}
	
	
	public <G> Tuple7<A, B, C, D, E, F, G> add(G g)
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public <G, H> Tuple8<A, B, C, D, E, F, G, H> add(G g, H h)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h);
	}
	
	public <G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> add(G g, H h, I i)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public <G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(G g, H h, I i, J j)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j);
	}
	
	public <G, H, I, J, K> Tuple11<A, B, C, D, E, F, G, H, I, J, K> add(G g, H h, I i, J j, K k)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public <G, H, I, J, K, L> Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> add(G g, H h, I i, J j, K k, L l)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public <G, H, I, J, K, L, M> Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(G g, H h, I i, J j, K k, L l, M m)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public <G, H, I, J, K, L, M, N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <G, H, I, J, K, L, M, N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <G, H, I, J, K, L, M, N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <G> Tuple7<G, A, B, C, D, E, F> insert(G g)
	{
		return Tuples.immutable(g, a, b, c, d, e, f);
	}
	
	public <G, H> Tuple8<G, H, A, B, C, D, E, F> insert(G g, H h)
	{
		return Tuples.immutable(g, h, a, b, c, d, e, f);
	}
	
	public <G, H, I> Tuple9<G, H, I, A, B, C, D, E, F> insert(G g, H h, I i)
	{
		return Tuples.immutable(g, h, i, a, b, c, d, e, f);
	}
	
	public <G, H, I, J> Tuple10<G, H, I, J, A, B, C, D, E, F> insert(G g, H h, I i, J j)
	{
		return Tuples.immutable(g, h, i, j, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K> Tuple11<G, H, I, J, K, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k)
	{
		return Tuples.immutable(g, h, i, j, k, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L> Tuple12<G, H, I, J, K, L, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l)
	{
		return Tuples.immutable(g, h, i, j, k, l, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M> Tuple13<G, H, I, J, K, L, M, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N> Tuple14<G, H, I, J, K, L, M, N, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N, O> Tuple15<G, H, I, J, K, L, M, N, O, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N, O, P> Tuple16<G, H, I, J, K, L, M, N, O, P, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q> Tuple17<G, H, I, J, K, L, M, N, O, P, Q, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18<G, H, I, J, K, L, M, N, O, P, Q, R, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19<G, H, I, J, K, L, M, N, O, P, Q, R, S, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, a, b, c, d, e, f);
	}
	
	public <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, a, b, c, d, e, f);
	}
	
	public <RES> RES applyL(Function1<A, RES> func)
	{
		return func.apply(a);
	}
	
	public <RES> RES applyR(Function1<F, RES> func)
	{
		return func.apply(f);
	}
	
	public <RES> RES applyL(Function2<A, B, RES> func)
	{
		return func.apply(a, b);
	}
	
	public <RES> RES applyR(Function2<E, F, RES> func)
	{
		return func.apply(e, f);
	}
	
	public <RES> RES applyL(Function3<A, B, C, RES> func)
	{
		return func.apply(a, b, c);
	}
	
	public <RES> RES applyR(Function3<D, E, F, RES> func)
	{
		return func.apply(d, e, f);
	}
	
	public <RES> RES applyL(Function4<A, B, C, D, RES> func)
	{
		return func.apply(a, b, c, d);
	}
	
	public <RES> RES applyR(Function4<C, D, E, F, RES> func)
	{
		return func.apply(c, d, e, f);
	}
	
	public <RES> RES applyL(Function5<A, B, C, D, E, RES> func)
	{
		return func.apply(a, b, c, d, e);
	}
	
	public <RES> RES applyR(Function5<B, C, D, E, F, RES> func)
	{
		return func.apply(b, c, d, e, f);
	}
	
	public <RES> RES apply(Function6<A, B, C, D, E, F, RES> func)
	{
		return func.apply(a, b, c, d, e, f);
	}
	
	public void acceptL(Consumer1<A> consumer)
	{
		consumer.accept(a);
	}
	
	public void acceptR(Consumer1<F> consumer)
	{
		consumer.accept(f);
	}
	
	public void acceptL(Consumer2<A, B> consumer)
	{
		consumer.accept(a, b);
	}
	
	public void acceptR(Consumer2<E, F> consumer)
	{
		consumer.accept(e, f);
	}
	
	public void acceptL(Consumer3<A, B, C> consumer)
	{
		consumer.accept(a, b, c);
	}
	
	public void acceptR(Consumer3<D, E, F> consumer)
	{
		consumer.accept(d, e, f);
	}
	
	public void acceptL(Consumer4<A, B, C, D> consumer)
	{
		consumer.accept(a, b, c, d);
	}
	
	public void acceptR(Consumer4<C, D, E, F> consumer)
	{
		consumer.accept(c, d, e, f);
	}
	
	public void acceptL(Consumer5<A, B, C, D, E> consumer)
	{
		consumer.accept(a, b, c, d, e);
	}
	
	public void acceptR(Consumer5<B, C, D, E, F> consumer)
	{
		consumer.accept(b, c, d, e, f);
	}
	
	public void accept(Consumer6<A, B, C, D, E, F> consumer)
	{
		consumer.accept(a, b, c, d, e, f);
	}
	
	public @Override int arity()
	{
		return 6;
	}
	
	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f);
	}
	
	public Tuple6.Mutable6<A, B, C, D, E, F> mutable()
	{
		return new Tuple6.Mutable6<>(a, b, c, d, e, f);
	}
	
	public Tuple6<A, B, C, D, E, F> immutable()
	{
		return this;
	}
	
	public Tuple6<A, B, C, D, E, F> copy()
	{
		return new Tuple6<>(a, b, c, d, e, f);
	}
	
	public @Override String toString()
	{
		return "Tuple6" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}
	
	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f);
	}
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof Tuple6 tuple)) return false;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}
	
	public static class Mutable6<A, B, C, D, E, F>
			extends Tuple6<A, B, C, D, E, F>
	{
		public Mutable6(A a, B b, C c, D d, E e, F f)
		{
			super(a, b, c, d, e, f);
		}
		
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> mutable()
		{
			return this;
		}
		
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> copy()
		{
			return new Tuple6.Mutable6<>(a, b, c, d, e, f);
		}
		
		public @Override Tuple6<A, B, C, D, E, F> immutable()
		{
			return new Tuple6<>(a, b, c, d, e, f);
		}
		
		public @Override String toString()
		{
			return "Tuple6.Mutable6" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
		}
		
		public Mutable6<A, B, C, D, E, F> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable6<A, B, C, D, E, F> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public Mutable6<A, B, C, D, E, F> setC(C c)
		{
			this.c = c;
			return this;
		}
		
		public Mutable6<A, B, C, D, E, F> setD(D d)
		{
			this.d = d;
			return this;
		}
		
		public Mutable6<A, B, C, D, E, F> setE(E e)
		{
			this.e = e;
			return this;
		}
		
		public Mutable6<A, B, C, D, E, F> setF(F f)
		{
			this.f = f;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip5R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<F> strip5L()
		{
			return Tuples.mutable(f);
		}
		
		public @Override Tuple2.Mutable2<A, B> strip4R()
		{
			return Tuples.mutable(a, b);
		}
		
		public @Override Tuple2.Mutable2<E, F> strip4L()
		{
			return Tuples.mutable(e, f);
		}
		
		public @Override Tuple3.Mutable3<A, B, C> strip3R()
		{
			return Tuples.mutable(a, b, c);
		}
		
		public @Override Tuple3.Mutable3<D, E, F> strip3L()
		{
			return Tuples.mutable(d, e, f);
		}
		
		public @Override Tuple4.Mutable4<A, B, C, D> strip2R()
		{
			return Tuples.mutable(a, b, c, d);
		}
		
		public @Override Tuple4.Mutable4<C, D, E, F> strip2L()
		{
			return Tuples.mutable(c, d, e, f);
		}
		
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip1R()
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override Tuple5.Mutable5<B, C, D, E, F> strip1L()
		{
			return Tuples.mutable(b, c, d, e, f);
		}
		
		public @Override <G> Tuple7.Mutable7<A, B, C, D, E, F, G> add(G g)
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override <G, H> Tuple8.Mutable8<A, B, C, D, E, F, G, H> add(G g, H h)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h);
		}
		
		public @Override <G, H, I> Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> add(G g, H h, I i)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override <G, H, I, J> Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> add(G g, H h, I i, J j)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <G, H, I, J, K> Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> add(G g, H h, I i, J j, K k)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override <G, H, I, J, K, L> Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> add(G g, H h, I i, J j, K k, L l)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override <G, H, I, J, K, L, M> Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(G g, H h, I i, J j, K k, L l, M m)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override <G, H, I, J, K, L, M, N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(G g, H h, I i, J j, K k, L l, M m, N n)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(G g, H h, I i, J j, K k, L l, M m, N n, O o)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override <G> Tuple7.Mutable7<G, A, B, C, D, E, F> insert(G g)
		{
			return Tuples.mutable(g, a, b, c, d, e, f);
		}
		
		public @Override <G, H> Tuple8.Mutable8<G, H, A, B, C, D, E, F> insert(G g, H h)
		{
			return Tuples.mutable(g, h, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I> Tuple9.Mutable9<G, H, I, A, B, C, D, E, F> insert(G g, H h, I i)
		{
			return Tuples.mutable(g, h, i, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J> Tuple10.Mutable10<G, H, I, J, A, B, C, D, E, F> insert(G g, H h, I i, J j)
		{
			return Tuples.mutable(g, h, i, j, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K> Tuple11.Mutable11<G, H, I, J, K, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k)
		{
			return Tuples.mutable(g, h, i, j, k, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L> Tuple12.Mutable12<G, H, I, J, K, L, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l)
		{
			return Tuples.mutable(g, h, i, j, k, l, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M> Tuple13.Mutable13<G, H, I, J, K, L, M, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N> Tuple14.Mutable14<G, H, I, J, K, L, M, N, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O> Tuple15.Mutable15<G, H, I, J, K, L, M, N, O, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P> Tuple16.Mutable16<G, H, I, J, K, L, M, N, O, P, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q> Tuple17.Mutable17<G, H, I, J, K, L, M, N, O, P, Q, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18.Mutable18<G, H, I, J, K, L, M, N, O, P, Q, R, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<G, H, I, J, K, L, M, N, O, P, Q, R, S, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, a, b, c, d, e, f);
		}
		
		public @Override <G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, A, B, C, D, E, F> insert(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, a, b, c, d, e, f);
		}
	}
}