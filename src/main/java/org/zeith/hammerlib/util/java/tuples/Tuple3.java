package org.zeith.hammerlib.util.java.tuples;

import org.zeith.hammerlib.util.java.consumers.*;
import org.zeith.hammerlib.util.java.functions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple3<A, B, C>
		implements ITuple
{
	protected A a;
	protected B b;
	protected C c;
	
	public Tuple3(A a, B b, C c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
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
	
	public Tuple1<A> strip2R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<C> strip2L()
	{
		return Tuples.immutable(c);
	}
	
	public Tuple2<A, B> strip1R()
	{
		return Tuples.immutable(a, b);
	}
	
	public Tuple2<B, C> strip1L()
	{
		return Tuples.immutable(b, c);
	}
	
	
	public <D> Tuple4<A, B, C, D> add(D d)
	{
		return Tuples.immutable(a, b, c, d);
	}
	
	public <D, E> Tuple5<A, B, C, D, E> add(D d, E e)
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public <D, E, F> Tuple6<A, B, C, D, E, F> add(D d, E e, F f)
	{
		return Tuples.immutable(a, b, c, d, e, f);
	}
	
	public <D, E, F, G> Tuple7<A, B, C, D, E, F, G> add(D d, E e, F f, G g)
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public <D, E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> add(D d, E e, F f, G g, H h)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h);
	}
	
	public <D, E, F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> add(D d, E e, F f, G g, H h, I i)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public <D, E, F, G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(D d, E e, F f, G g, H h, I i, J j)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j);
	}
	
	public <D, E, F, G, H, I, J, K> Tuple11<A, B, C, D, E, F, G, H, I, J, K> add(D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public <D, E, F, G, H, I, J, K, L> Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> add(D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public <D, E, F, G, H, I, J, K, L, M> Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <D> Tuple4<D, A, B, C> insert(D d)
	{
		return Tuples.immutable(d, a, b, c);
	}
	
	public <D, E> Tuple5<D, E, A, B, C> insert(D d, E e)
	{
		return Tuples.immutable(d, e, a, b, c);
	}
	
	public <D, E, F> Tuple6<D, E, F, A, B, C> insert(D d, E e, F f)
	{
		return Tuples.immutable(d, e, f, a, b, c);
	}
	
	public <D, E, F, G> Tuple7<D, E, F, G, A, B, C> insert(D d, E e, F f, G g)
	{
		return Tuples.immutable(d, e, f, g, a, b, c);
	}
	
	public <D, E, F, G, H> Tuple8<D, E, F, G, H, A, B, C> insert(D d, E e, F f, G g, H h)
	{
		return Tuples.immutable(d, e, f, g, h, a, b, c);
	}
	
	public <D, E, F, G, H, I> Tuple9<D, E, F, G, H, I, A, B, C> insert(D d, E e, F f, G g, H h, I i)
	{
		return Tuples.immutable(d, e, f, g, h, i, a, b, c);
	}
	
	public <D, E, F, G, H, I, J> Tuple10<D, E, F, G, H, I, J, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K> Tuple11<D, E, F, G, H, I, J, K, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L> Tuple12<D, E, F, G, H, I, J, K, L, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M> Tuple13<D, E, F, G, H, I, J, K, L, M, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N> Tuple14<D, E, F, G, H, I, J, K, L, M, N, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O> Tuple15<D, E, F, G, H, I, J, K, L, M, N, O, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple16<D, E, F, G, H, I, J, K, L, M, N, O, P, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, a, b, c);
	}
	
	public <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, a, b, c);
	}
	
	public <RES> RES applyL(Function1<A, RES> func)
	{
		return func.apply(a);
	}
	
	public <RES> RES applyR(Function1<C, RES> func)
	{
		return func.apply(c);
	}
	
	public <RES> RES applyL(Function2<A, B, RES> func)
	{
		return func.apply(a, b);
	}
	
	public <RES> RES applyR(Function2<B, C, RES> func)
	{
		return func.apply(b, c);
	}
	
	public <RES> RES apply(Function3<A, B, C, RES> func)
	{
		return func.apply(a, b, c);
	}
	
	public void acceptL(Consumer1<A> consumer)
	{
		consumer.accept(a);
	}
	
	public void acceptR(Consumer1<C> consumer)
	{
		consumer.accept(c);
	}
	
	public void acceptL(Consumer2<A, B> consumer)
	{
		consumer.accept(a, b);
	}
	
	public void acceptR(Consumer2<B, C> consumer)
	{
		consumer.accept(b, c);
	}
	
	public void accept(Consumer3<A, B, C> consumer)
	{
		consumer.accept(a, b, c);
	}
	
	public @Override int arity()
	{
		return 3;
	}
	
	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c);
	}
	
	public Mutable3<A, B, C> mutable()
	{
		return new Mutable3<>(a, b, c);
	}
	
	public Tuple3<A, B, C> immutable()
	{
		return this;
	}
	
	public Tuple3<A, B, C> copy()
	{
		return new Tuple3<>(a, b, c);
	}
	
	public @Override String toString()
	{
		return "Tuple3" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}
	
	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c);
	}
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof ITuple)) return false;
		ITuple tuple = (ITuple) other;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}
	
	public static class Mutable3<A, B, C>
			extends Tuple3<A, B, C>
	{
		public Mutable3(A a, B b, C c)
		{
			super(a, b, c);
		}
		
		public @Override Mutable3<A, B, C> mutable()
		{
			return this;
		}
		
		public @Override Mutable3<A, B, C> copy()
		{
			return new Mutable3<>(a, b, c);
		}
		
		public @Override Tuple3<A, B, C> immutable()
		{
			return new Tuple3<>(a, b, c);
		}
		
		public @Override String toString()
		{
			return "Tuple3.Mutable3" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
		}
		
		public Mutable3<A, B, C> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable3<A, B, C> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public Mutable3<A, B, C> setC(C c)
		{
			this.c = c;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip2R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<C> strip2L()
		{
			return Tuples.mutable(c);
		}
		
		public @Override Tuple2.Mutable2<A, B> strip1R()
		{
			return Tuples.mutable(a, b);
		}
		
		public @Override Tuple2.Mutable2<B, C> strip1L()
		{
			return Tuples.mutable(b, c);
		}
		
		public @Override <D> Tuple4.Mutable4<A, B, C, D> add(D d)
		{
			return Tuples.mutable(a, b, c, d);
		}
		
		public @Override <D, E> Tuple5.Mutable5<A, B, C, D, E> add(D d, E e)
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override <D, E, F> Tuple6.Mutable6<A, B, C, D, E, F> add(D d, E e, F f)
		{
			return Tuples.mutable(a, b, c, d, e, f);
		}
		
		public @Override <D, E, F, G> Tuple7.Mutable7<A, B, C, D, E, F, G> add(D d, E e, F f, G g)
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override <D, E, F, G, H> Tuple8.Mutable8<A, B, C, D, E, F, G, H> add(D d, E e, F f, G g, H h)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h);
		}
		
		public @Override <D, E, F, G, H, I> Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> add(D d, E e, F f, G g, H h, I i)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override <D, E, F, G, H, I, J> Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> add(D d, E e, F f, G g, H h, I i, J j)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <D, E, F, G, H, I, J, K> Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> add(D d, E e, F f, G g, H h, I i, J j, K k)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L> Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> add(D d, E e, F f, G g, H h, I i, J j, K k, L l)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M> Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override <D> Tuple4.Mutable4<D, A, B, C> insert(D d)
		{
			return Tuples.mutable(d, a, b, c);
		}
		
		public @Override <D, E> Tuple5.Mutable5<D, E, A, B, C> insert(D d, E e)
		{
			return Tuples.mutable(d, e, a, b, c);
		}
		
		public @Override <D, E, F> Tuple6.Mutable6<D, E, F, A, B, C> insert(D d, E e, F f)
		{
			return Tuples.mutable(d, e, f, a, b, c);
		}
		
		public @Override <D, E, F, G> Tuple7.Mutable7<D, E, F, G, A, B, C> insert(D d, E e, F f, G g)
		{
			return Tuples.mutable(d, e, f, g, a, b, c);
		}
		
		public @Override <D, E, F, G, H> Tuple8.Mutable8<D, E, F, G, H, A, B, C> insert(D d, E e, F f, G g, H h)
		{
			return Tuples.mutable(d, e, f, g, h, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I> Tuple9.Mutable9<D, E, F, G, H, I, A, B, C> insert(D d, E e, F f, G g, H h, I i)
		{
			return Tuples.mutable(d, e, f, g, h, i, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J> Tuple10.Mutable10<D, E, F, G, H, I, J, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K> Tuple11.Mutable11<D, E, F, G, H, I, J, K, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L> Tuple12.Mutable12<D, E, F, G, H, I, J, K, L, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M> Tuple13.Mutable13<D, E, F, G, H, I, J, K, L, M, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N> Tuple14.Mutable14<D, E, F, G, H, I, J, K, L, M, N, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O> Tuple15.Mutable15<D, E, F, G, H, I, J, K, L, M, N, O, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple16.Mutable16<D, E, F, G, H, I, J, K, L, M, N, O, P, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17.Mutable17<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18.Mutable18<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, a, b, c);
		}
		
		public @Override <D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, A, B, C> insert(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, a, b, c);
		}
	}
}