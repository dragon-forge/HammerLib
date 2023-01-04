package org.zeith.hammerlib.util.java.tuples;

import org.zeith.hammerlib.util.java.consumers.Consumer1;
import org.zeith.hammerlib.util.java.consumers.Consumer2;
import org.zeith.hammerlib.util.java.functions.Function1;
import org.zeith.hammerlib.util.java.functions.Function2;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple2<A, B>
		implements ITuple
{
	protected A a;
	protected B b;
	
	public Tuple2(A a, B b)
	{
		this.a = a;
		this.b = b;
	}
	
	public A a()
	{
		return a;
	}
	
	public B b()
	{
		return b;
	}
	
	public Tuple1<A> strip1R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<B> strip1L()
	{
		return Tuples.immutable(b);
	}
	
	
	public <C> Tuple3<A, B, C> add(C c)
	{
		return Tuples.immutable(a, b, c);
	}
	
	public <C, D> Tuple4<A, B, C, D> add(C c, D d)
	{
		return Tuples.immutable(a, b, c, d);
	}
	
	public <C, D, E> Tuple5<A, B, C, D, E> add(C c, D d, E e)
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public <C, D, E, F> Tuple6<A, B, C, D, E, F> add(C c, D d, E e, F f)
	{
		return Tuples.immutable(a, b, c, d, e, f);
	}
	
	public <C, D, E, F, G> Tuple7<A, B, C, D, E, F, G> add(C c, D d, E e, F f, G g)
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public <C, D, E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> add(C c, D d, E e, F f, G g, H h)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h);
	}
	
	public <C, D, E, F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> add(C c, D d, E e, F f, G g, H h, I i)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public <C, D, E, F, G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> add(C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j);
	}
	
	public <C, D, E, F, G, H, I, J, K> Tuple11<A, B, C, D, E, F, G, H, I, J, K> add(C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public <C, D, E, F, G, H, I, J, K, L> Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M> Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <C> Tuple3<C, A, B> insert(C c)
	{
		return Tuples.immutable(c, a, b);
	}
	
	public <C, D> Tuple4<C, D, A, B> insert(C c, D d)
	{
		return Tuples.immutable(c, d, a, b);
	}
	
	public <C, D, E> Tuple5<C, D, E, A, B> insert(C c, D d, E e)
	{
		return Tuples.immutable(c, d, e, a, b);
	}
	
	public <C, D, E, F> Tuple6<C, D, E, F, A, B> insert(C c, D d, E e, F f)
	{
		return Tuples.immutable(c, d, e, f, a, b);
	}
	
	public <C, D, E, F, G> Tuple7<C, D, E, F, G, A, B> insert(C c, D d, E e, F f, G g)
	{
		return Tuples.immutable(c, d, e, f, g, a, b);
	}
	
	public <C, D, E, F, G, H> Tuple8<C, D, E, F, G, H, A, B> insert(C c, D d, E e, F f, G g, H h)
	{
		return Tuples.immutable(c, d, e, f, g, h, a, b);
	}
	
	public <C, D, E, F, G, H, I> Tuple9<C, D, E, F, G, H, I, A, B> insert(C c, D d, E e, F f, G g, H h, I i)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, a, b);
	}
	
	public <C, D, E, F, G, H, I, J> Tuple10<C, D, E, F, G, H, I, J, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K> Tuple11<C, D, E, F, G, H, I, J, K, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L> Tuple12<C, D, E, F, G, H, I, J, K, L, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M> Tuple13<C, D, E, F, G, H, I, J, K, L, M, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N> Tuple14<C, D, E, F, G, H, I, J, K, L, M, N, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O> Tuple15<C, D, E, F, G, H, I, J, K, L, M, N, O, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple16<C, D, E, F, G, H, I, J, K, L, M, N, O, P, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, a, b);
	}
	
	public <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, a, b);
	}
	
	public <RES> RES applyL(Function1<A, RES> func)
	{
		return func.apply(a);
	}
	
	public <RES> RES applyR(Function1<B, RES> func)
	{
		return func.apply(b);
	}
	
	public <RES> RES apply(Function2<A, B, RES> func)
	{
		return func.apply(a, b);
	}
	
	public void acceptL(Consumer1<A> consumer)
	{
		consumer.accept(a);
	}
	
	public void acceptR(Consumer1<B> consumer)
	{
		consumer.accept(b);
	}
	
	public void accept(Consumer2<A, B> consumer)
	{
		consumer.accept(a, b);
	}
	
	public @Override int arity()
	{
		return 2;
	}
	
	public @Override Stream<?> stream()
	{
		return Stream.of(a, b);
	}
	
	public Mutable2<A, B> mutable()
	{
		return new Mutable2<>(a, b);
	}
	
	public Tuple2<A, B> immutable()
	{
		return this;
	}
	
	public Tuple2<A, B> copy()
	{
		return new Tuple2<>(a, b);
	}
	
	public @Override String toString()
	{
		return "Tuple2" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}
	
	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b);
	}
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof ITuple)) return false;
		ITuple tuple = (ITuple) other;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}
	
	public static class Mutable2<A, B>
			extends Tuple2<A, B>
	{
		public Mutable2(A a, B b)
		{
			super(a, b);
		}
		
		public @Override Mutable2<A, B> mutable()
		{
			return this;
		}
		
		public @Override Mutable2<A, B> copy()
		{
			return new Mutable2<>(a, b);
		}
		
		public @Override Tuple2<A, B> immutable()
		{
			return new Tuple2<>(a, b);
		}
		
		public @Override String toString()
		{
			return "Tuple2.Mutable2" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
		}
		
		public Mutable2<A, B> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable2<A, B> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip1R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<B> strip1L()
		{
			return Tuples.mutable(b);
		}
		
		public @Override <C> Tuple3.Mutable3<A, B, C> add(C c)
		{
			return Tuples.mutable(a, b, c);
		}
		
		public @Override <C, D> Tuple4.Mutable4<A, B, C, D> add(C c, D d)
		{
			return Tuples.mutable(a, b, c, d);
		}
		
		public @Override <C, D, E> Tuple5.Mutable5<A, B, C, D, E> add(C c, D d, E e)
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override <C, D, E, F> Tuple6.Mutable6<A, B, C, D, E, F> add(C c, D d, E e, F f)
		{
			return Tuples.mutable(a, b, c, d, e, f);
		}
		
		public @Override <C, D, E, F, G> Tuple7.Mutable7<A, B, C, D, E, F, G> add(C c, D d, E e, F f, G g)
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override <C, D, E, F, G, H> Tuple8.Mutable8<A, B, C, D, E, F, G, H> add(C c, D d, E e, F f, G g, H h)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h);
		}
		
		public @Override <C, D, E, F, G, H, I> Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> add(C c, D d, E e, F f, G g, H h, I i)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override <C, D, E, F, G, H, I, J> Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> add(C c, D d, E e, F f, G g, H h, I i, J j)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K> Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> add(C c, D d, E e, F f, G g, H h, I i, J j, K k)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L> Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M> Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override <C> Tuple3.Mutable3<C, A, B> insert(C c)
		{
			return Tuples.mutable(c, a, b);
		}
		
		public @Override <C, D> Tuple4.Mutable4<C, D, A, B> insert(C c, D d)
		{
			return Tuples.mutable(c, d, a, b);
		}
		
		public @Override <C, D, E> Tuple5.Mutable5<C, D, E, A, B> insert(C c, D d, E e)
		{
			return Tuples.mutable(c, d, e, a, b);
		}
		
		public @Override <C, D, E, F> Tuple6.Mutable6<C, D, E, F, A, B> insert(C c, D d, E e, F f)
		{
			return Tuples.mutable(c, d, e, f, a, b);
		}
		
		public @Override <C, D, E, F, G> Tuple7.Mutable7<C, D, E, F, G, A, B> insert(C c, D d, E e, F f, G g)
		{
			return Tuples.mutable(c, d, e, f, g, a, b);
		}
		
		public @Override <C, D, E, F, G, H> Tuple8.Mutable8<C, D, E, F, G, H, A, B> insert(C c, D d, E e, F f, G g, H h)
		{
			return Tuples.mutable(c, d, e, f, g, h, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I> Tuple9.Mutable9<C, D, E, F, G, H, I, A, B> insert(C c, D d, E e, F f, G g, H h, I i)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J> Tuple10.Mutable10<C, D, E, F, G, H, I, J, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K> Tuple11.Mutable11<C, D, E, F, G, H, I, J, K, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L> Tuple12.Mutable12<C, D, E, F, G, H, I, J, K, L, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M> Tuple13.Mutable13<C, D, E, F, G, H, I, J, K, L, M, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N> Tuple14.Mutable14<C, D, E, F, G, H, I, J, K, L, M, N, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O> Tuple15.Mutable15<C, D, E, F, G, H, I, J, K, L, M, N, O, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple16.Mutable16<C, D, E, F, G, H, I, J, K, L, M, N, O, P, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17.Mutable17<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18.Mutable18<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, a, b);
		}
		
		public @Override <C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, A, B> insert(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, a, b);
		}
	}
}