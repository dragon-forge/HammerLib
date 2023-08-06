package com.zeitheron.hammercore.utils.java.tuples;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public class Tuples
{
	static boolean streamEquals(Stream<?> s1, Stream<?> s2)
	{
		Iterator<?> iter1 = s1.iterator(), iter2 = s2.iterator();
		while(iter1.hasNext() && iter2.hasNext())
			if(!Objects.equals(iter1.next(), iter2.next()))
				return false;
		return !iter1.hasNext() && !iter2.hasNext();
	}
	
	public static <A> Tuple1<A> immutable(A a)
	{
		return new Tuple1<>(a);
	}
	
	public static <A> Tuple1.Mutable1<A> mutable(A a)
	{
		return new Tuple1.Mutable1<>(a);
	}
	
	public static <A, B> Tuple2<A, B> immutable(A a, B b)
	{
		return new Tuple2<>(a, b);
	}
	
	public static <A, B> Tuple2.Mutable2<A, B> mutable(A a, B b)
	{
		return new Tuple2.Mutable2<>(a, b);
	}
	
	public static <A, B, C> Tuple3<A, B, C> immutable(A a, B b, C c)
	{
		return new Tuple3<>(a, b, c);
	}
	
	public static <A, B, C> Tuple3.Mutable3<A, B, C> mutable(A a, B b, C c)
	{
		return new Tuple3.Mutable3<>(a, b, c);
	}
	
	public static <A, B, C, D> Tuple4<A, B, C, D> immutable(A a, B b, C c, D d)
	{
		return new Tuple4<>(a, b, c, d);
	}
	
	public static <A, B, C, D> Tuple4.Mutable4<A, B, C, D> mutable(A a, B b, C c, D d)
	{
		return new Tuple4.Mutable4<>(a, b, c, d);
	}
	
	public static <A, B, C, D, E> Tuple5<A, B, C, D, E> immutable(A a, B b, C c, D d, E e)
	{
		return new Tuple5<>(a, b, c, d, e);
	}
	
	public static <A, B, C, D, E> Tuple5.Mutable5<A, B, C, D, E> mutable(A a, B b, C c, D d, E e)
	{
		return new Tuple5.Mutable5<>(a, b, c, d, e);
	}
	
	public static <A, B, C, D, E, F> Tuple6<A, B, C, D, E, F> immutable(A a, B b, C c, D d, E e, F f)
	{
		return new Tuple6<>(a, b, c, d, e, f);
	}
	
	public static <A, B, C, D, E, F> Tuple6.Mutable6<A, B, C, D, E, F> mutable(A a, B b, C c, D d, E e, F f)
	{
		return new Tuple6.Mutable6<>(a, b, c, d, e, f);
	}
	
	public static <A, B, C, D, E, F, G> Tuple7<A, B, C, D, E, F, G> immutable(A a, B b, C c, D d, E e, F f, G g)
	{
		return new Tuple7<>(a, b, c, d, e, f, g);
	}
	
	public static <A, B, C, D, E, F, G> Tuple7.Mutable7<A, B, C, D, E, F, G> mutable(A a, B b, C c, D d, E e, F f, G g)
	{
		return new Tuple7.Mutable7<>(a, b, c, d, e, f, g);
	}
	
	public static <A, B, C, D, E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> immutable(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return new Tuple8<>(a, b, c, d, e, f, g, h);
	}
	
	public static <A, B, C, D, E, F, G, H> Tuple8.Mutable8<A, B, C, D, E, F, G, H> mutable(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return new Tuple8.Mutable8<>(a, b, c, d, e, f, g, h);
	}
	
	public static <A, B, C, D, E, F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return new Tuple9<>(a, b, c, d, e, f, g, h, i);
	}
	
	public static <A, B, C, D, E, F, G, H, I> Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return new Tuple9.Mutable9<>(a, b, c, d, e, f, g, h, i);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return new Tuple10<>(a, b, c, d, e, f, g, h, i, j);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J> Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return new Tuple10.Mutable10<>(a, b, c, d, e, f, g, h, i, j);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K> Tuple11<A, B, C, D, E, F, G, H, I, J, K> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return new Tuple11<>(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K> Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return new Tuple11.Mutable11<>(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L> Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return new Tuple12<>(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L> Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return new Tuple12.Mutable12<>(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M> Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return new Tuple13<>(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M> Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return new Tuple13.Mutable13<>(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return new Tuple14<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return new Tuple14.Mutable14<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return new Tuple15<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return new Tuple15.Mutable15<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return new Tuple16<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return new Tuple16.Mutable16<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return new Tuple17<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return new Tuple17.Mutable17<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return new Tuple18<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return new Tuple18.Mutable18<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return new Tuple19<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return new Tuple19.Mutable19<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return new Tuple20<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return new Tuple20.Mutable20<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return new Tuple21<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return new Tuple21.Mutable21<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return new Tuple22<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return new Tuple22.Mutable22<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return new Tuple23<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return new Tuple23.Mutable23<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return new Tuple24<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return new Tuple24.Mutable24<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return new Tuple25<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return new Tuple25.Mutable25<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> immutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return new Tuple26<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> mutable(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return new Tuple26.Mutable26<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
}
