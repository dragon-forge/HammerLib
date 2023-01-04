package org.zeith.hammerlib.util.java.tuples;

import org.zeith.hammerlib.util.java.consumers.*;
import org.zeith.hammerlib.util.java.functions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple10<A, B, C, D, E, F, G, H, I, J>
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
	
	public Tuple10(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
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
	
	public Tuple1<A> strip9R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<J> strip9L()
	{
		return Tuples.immutable(j);
	}
	
	public Tuple2<A, B> strip8R()
	{
		return Tuples.immutable(a, b);
	}
	
	public Tuple2<I, J> strip8L()
	{
		return Tuples.immutable(i, j);
	}
	
	public Tuple3<A, B, C> strip7R()
	{
		return Tuples.immutable(a, b, c);
	}
	
	public Tuple3<H, I, J> strip7L()
	{
		return Tuples.immutable(h, i, j);
	}
	
	public Tuple4<A, B, C, D> strip6R()
	{
		return Tuples.immutable(a, b, c, d);
	}
	
	public Tuple4<G, H, I, J> strip6L()
	{
		return Tuples.immutable(g, h, i, j);
	}
	
	public Tuple5<A, B, C, D, E> strip5R()
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public Tuple5<F, G, H, I, J> strip5L()
	{
		return Tuples.immutable(f, g, h, i, j);
	}
	
	public Tuple6<A, B, C, D, E, F> strip4R()
	{
		return Tuples.immutable(a, b, c, d, e, f);
	}
	
	public Tuple6<E, F, G, H, I, J> strip4L()
	{
		return Tuples.immutable(e, f, g, h, i, j);
	}
	
	public Tuple7<A, B, C, D, E, F, G> strip3R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public Tuple7<D, E, F, G, H, I, J> strip3L()
	{
		return Tuples.immutable(d, e, f, g, h, i, j);
	}
	
	public Tuple8<A, B, C, D, E, F, G, H> strip2R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h);
	}
	
	public Tuple8<C, D, E, F, G, H, I, J> strip2L()
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j);
	}
	
	public Tuple9<A, B, C, D, E, F, G, H, I> strip1R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public Tuple9<B, C, D, E, F, G, H, I, J> strip1L()
	{
		return Tuples.immutable(b, c, d, e, f, g, h, i, j);
	}
	
	
	public <K> Tuple11<A, B, C, D, E, F, G, H, I, J, K> add(K k)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public <K, L> Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> add(K k, L l)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public <K, L, M> Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(K k, L l, M m)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public <K, L, M, N> Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(K k, L l, M m, N n)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <K, L, M, N, O> Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(K k, L l, M m, N n, O o)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <K, L, M, N, O, P> Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(K k, L l, M m, N n, O o, P p)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <K, L, M, N, O, P, Q> Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(K k, L l, M m, N n, O o, P p, Q q)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <K, L, M, N, O, P, Q, R> Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <K, L, M, N, O, P, Q, R, S> Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T> Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U> Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <K> Tuple11<K, A, B, C, D, E, F, G, H, I, J> insert(K k)
	{
		return Tuples.immutable(k, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L> Tuple12<K, L, A, B, C, D, E, F, G, H, I, J> insert(K k, L l)
	{
		return Tuples.immutable(k, l, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M> Tuple13<K, L, M, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m)
	{
		return Tuples.immutable(k, l, m, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N> Tuple14<K, L, M, N, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n)
	{
		return Tuples.immutable(k, l, m, n, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N, O> Tuple15<K, L, M, N, O, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o)
	{
		return Tuples.immutable(k, l, m, n, o, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N, O, P> Tuple16<K, L, M, N, O, P, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p)
	{
		return Tuples.immutable(k, l, m, n, o, p, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N, O, P, Q> Tuple17<K, L, M, N, O, P, Q, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q)
	{
		return Tuples.immutable(k, l, m, n, o, p, q, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N, O, P, Q, R> Tuple18<K, L, M, N, O, P, Q, R, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N, O, P, Q, R, S> Tuple19<K, L, M, N, O, P, Q, R, S, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, s, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T> Tuple20<K, L, M, N, O, P, Q, R, S, T, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, s, t, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U> Tuple21<K, L, M, N, O, P, Q, R, S, T, U, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, s, t, u, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22<K, L, M, N, O, P, Q, R, S, T, U, V, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, s, t, u, v, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23<K, L, M, N, O, P, Q, R, S, T, U, V, W, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, s, t, u, v, w, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24<K, L, M, N, O, P, Q, R, S, T, U, V, W, X, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, s, t, u, v, w, x, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25<K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26<K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, a, b, c, d, e, f, g, h, i, j);
	}
	
	public <RES> RES applyL(Function1<A, RES> func)
	{
		return func.apply(a);
	}
	
	public <RES> RES applyR(Function1<J, RES> func)
	{
		return func.apply(j);
	}
	
	public <RES> RES applyL(Function2<A, B, RES> func)
	{
		return func.apply(a, b);
	}
	
	public <RES> RES applyR(Function2<I, J, RES> func)
	{
		return func.apply(i, j);
	}
	
	public <RES> RES applyL(Function3<A, B, C, RES> func)
	{
		return func.apply(a, b, c);
	}
	
	public <RES> RES applyR(Function3<H, I, J, RES> func)
	{
		return func.apply(h, i, j);
	}
	
	public <RES> RES applyL(Function4<A, B, C, D, RES> func)
	{
		return func.apply(a, b, c, d);
	}
	
	public <RES> RES applyR(Function4<G, H, I, J, RES> func)
	{
		return func.apply(g, h, i, j);
	}
	
	public <RES> RES applyL(Function5<A, B, C, D, E, RES> func)
	{
		return func.apply(a, b, c, d, e);
	}
	
	public <RES> RES applyR(Function5<F, G, H, I, J, RES> func)
	{
		return func.apply(f, g, h, i, j);
	}
	
	public <RES> RES applyL(Function6<A, B, C, D, E, F, RES> func)
	{
		return func.apply(a, b, c, d, e, f);
	}
	
	public <RES> RES applyR(Function6<E, F, G, H, I, J, RES> func)
	{
		return func.apply(e, f, g, h, i, j);
	}
	
	public <RES> RES applyL(Function7<A, B, C, D, E, F, G, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g);
	}
	
	public <RES> RES applyR(Function7<D, E, F, G, H, I, J, RES> func)
	{
		return func.apply(d, e, f, g, h, i, j);
	}
	
	public <RES> RES applyL(Function8<A, B, C, D, E, F, G, H, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h);
	}
	
	public <RES> RES applyR(Function8<C, D, E, F, G, H, I, J, RES> func)
	{
		return func.apply(c, d, e, f, g, h, i, j);
	}
	
	public <RES> RES applyL(Function9<A, B, C, D, E, F, G, H, I, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i);
	}
	
	public <RES> RES applyR(Function9<B, C, D, E, F, G, H, I, J, RES> func)
	{
		return func.apply(b, c, d, e, f, g, h, i, j);
	}
	
	public <RES> RES apply(Function10<A, B, C, D, E, F, G, H, I, J, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	public void acceptL(Consumer1<A> consumer)
	{
		consumer.accept(a);
	}
	
	public void acceptR(Consumer1<J> consumer)
	{
		consumer.accept(j);
	}
	
	public void acceptL(Consumer2<A, B> consumer)
	{
		consumer.accept(a, b);
	}
	
	public void acceptR(Consumer2<I, J> consumer)
	{
		consumer.accept(i, j);
	}
	
	public void acceptL(Consumer3<A, B, C> consumer)
	{
		consumer.accept(a, b, c);
	}
	
	public void acceptR(Consumer3<H, I, J> consumer)
	{
		consumer.accept(h, i, j);
	}
	
	public void acceptL(Consumer4<A, B, C, D> consumer)
	{
		consumer.accept(a, b, c, d);
	}
	
	public void acceptR(Consumer4<G, H, I, J> consumer)
	{
		consumer.accept(g, h, i, j);
	}
	
	public void acceptL(Consumer5<A, B, C, D, E> consumer)
	{
		consumer.accept(a, b, c, d, e);
	}
	
	public void acceptR(Consumer5<F, G, H, I, J> consumer)
	{
		consumer.accept(f, g, h, i, j);
	}
	
	public void acceptL(Consumer6<A, B, C, D, E, F> consumer)
	{
		consumer.accept(a, b, c, d, e, f);
	}
	
	public void acceptR(Consumer6<E, F, G, H, I, J> consumer)
	{
		consumer.accept(e, f, g, h, i, j);
	}
	
	public void acceptL(Consumer7<A, B, C, D, E, F, G> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g);
	}
	
	public void acceptR(Consumer7<D, E, F, G, H, I, J> consumer)
	{
		consumer.accept(d, e, f, g, h, i, j);
	}
	
	public void acceptL(Consumer8<A, B, C, D, E, F, G, H> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h);
	}
	
	public void acceptR(Consumer8<C, D, E, F, G, H, I, J> consumer)
	{
		consumer.accept(c, d, e, f, g, h, i, j);
	}
	
	public void acceptL(Consumer9<A, B, C, D, E, F, G, H, I> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i);
	}
	
	public void acceptR(Consumer9<B, C, D, E, F, G, H, I, J> consumer)
	{
		consumer.accept(b, c, d, e, f, g, h, i, j);
	}
	
	public void accept(Consumer10<A, B, C, D, E, F, G, H, I, J> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	public @Override int arity()
	{
		return 10;
	}
	
	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h, i, j);
	}
	
	public Mutable10<A, B, C, D, E, F, G, H, I, J> mutable()
	{
		return new Mutable10<>(a, b, c, d, e, f, g, h, i, j);
	}
	
	public Tuple10<A, B, C, D, E, F, G, H, I, J> immutable()
	{
		return this;
	}
	
	public Tuple10<A, B, C, D, E, F, G, H, I, J> copy()
	{
		return new Tuple10<>(a, b, c, d, e, f, g, h, i, j);
	}
	
	public @Override String toString()
	{
		return "Tuple10" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}
	
	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h, i, j);
	}
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof ITuple)) return false;
		ITuple tuple = (ITuple) other;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}
	
	public static class Mutable10<A, B, C, D, E, F, G, H, I, J>
			extends Tuple10<A, B, C, D, E, F, G, H, I, J>
	{
		public Mutable10(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
		{
			super(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override Mutable10<A, B, C, D, E, F, G, H, I, J> mutable()
		{
			return this;
		}
		
		public @Override Mutable10<A, B, C, D, E, F, G, H, I, J> copy()
		{
			return new Mutable10<>(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override Tuple10<A, B, C, D, E, F, G, H, I, J> immutable()
		{
			return new Tuple10<>(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override String toString()
		{
			return "Tuple10.Mutable10" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setC(C c)
		{
			this.c = c;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setD(D d)
		{
			this.d = d;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setE(E e)
		{
			this.e = e;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setF(F f)
		{
			this.f = f;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setG(G g)
		{
			this.g = g;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setH(H h)
		{
			this.h = h;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setI(I i)
		{
			this.i = i;
			return this;
		}
		
		public Mutable10<A, B, C, D, E, F, G, H, I, J> setJ(J j)
		{
			this.j = j;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip9R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<J> strip9L()
		{
			return Tuples.mutable(j);
		}
		
		public @Override Tuple2.Mutable2<A, B> strip8R()
		{
			return Tuples.mutable(a, b);
		}
		
		public @Override Tuple2.Mutable2<I, J> strip8L()
		{
			return Tuples.mutable(i, j);
		}
		
		public @Override Tuple3.Mutable3<A, B, C> strip7R()
		{
			return Tuples.mutable(a, b, c);
		}
		
		public @Override Tuple3.Mutable3<H, I, J> strip7L()
		{
			return Tuples.mutable(h, i, j);
		}
		
		public @Override Tuple4.Mutable4<A, B, C, D> strip6R()
		{
			return Tuples.mutable(a, b, c, d);
		}
		
		public @Override Tuple4.Mutable4<G, H, I, J> strip6L()
		{
			return Tuples.mutable(g, h, i, j);
		}
		
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip5R()
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override Tuple5.Mutable5<F, G, H, I, J> strip5L()
		{
			return Tuples.mutable(f, g, h, i, j);
		}
		
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip4R()
		{
			return Tuples.mutable(a, b, c, d, e, f);
		}
		
		public @Override Tuple6.Mutable6<E, F, G, H, I, J> strip4L()
		{
			return Tuples.mutable(e, f, g, h, i, j);
		}
		
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip3R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override Tuple7.Mutable7<D, E, F, G, H, I, J> strip3L()
		{
			return Tuples.mutable(d, e, f, g, h, i, j);
		}
		
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip2R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h);
		}
		
		public @Override Tuple8.Mutable8<C, D, E, F, G, H, I, J> strip2L()
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j);
		}
		
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip1R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override Tuple9.Mutable9<B, C, D, E, F, G, H, I, J> strip1L()
		{
			return Tuples.mutable(b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K> Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> add(K k)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override <K, L> Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> add(K k, L l)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override <K, L, M> Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> add(K k, L l, M m)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override <K, L, M, N> Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> add(K k, L l, M m, N n)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override <K, L, M, N, O> Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> add(K k, L l, M m, N n, O o)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override <K, L, M, N, O, P> Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> add(K k, L l, M m, N n, O o, P p)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override <K, L, M, N, O, P, Q> Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> add(K k, L l, M m, N n, O o, P p, Q q)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override <K, L, M, N, O, P, Q, R> Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> add(K k, L l, M m, N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> add(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override <K> Tuple11.Mutable11<K, A, B, C, D, E, F, G, H, I, J> insert(K k)
		{
			return Tuples.mutable(k, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L> Tuple12.Mutable12<K, L, A, B, C, D, E, F, G, H, I, J> insert(K k, L l)
		{
			return Tuples.mutable(k, l, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M> Tuple13.Mutable13<K, L, M, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m)
		{
			return Tuples.mutable(k, l, m, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N> Tuple14.Mutable14<K, L, M, N, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n)
		{
			return Tuples.mutable(k, l, m, n, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N, O> Tuple15.Mutable15<K, L, M, N, O, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o)
		{
			return Tuples.mutable(k, l, m, n, o, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N, O, P> Tuple16.Mutable16<K, L, M, N, O, P, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p)
		{
			return Tuples.mutable(k, l, m, n, o, p, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N, O, P, Q> Tuple17.Mutable17<K, L, M, N, O, P, Q, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q)
		{
			return Tuples.mutable(k, l, m, n, o, p, q, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N, O, P, Q, R> Tuple18.Mutable18<K, L, M, N, O, P, Q, R, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r)
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S> Tuple19.Mutable19<K, L, M, N, O, P, Q, R, S, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s)
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, s, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T> Tuple20.Mutable20<K, L, M, N, O, P, Q, R, S, T, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, s, t, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U> Tuple21.Mutable21<K, L, M, N, O, P, Q, R, S, T, U, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, s, t, u, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V> Tuple22.Mutable22<K, L, M, N, O, P, Q, R, S, T, U, V, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, s, t, u, v, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V, W> Tuple23.Mutable23<K, L, M, N, O, P, Q, R, S, T, U, V, W, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w)
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, s, t, u, v, w, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V, W, X> Tuple24.Mutable24<K, L, M, N, O, P, Q, R, S, T, U, V, W, X, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x)
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, s, t, u, v, w, x, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> Tuple25.Mutable25<K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y)
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override <K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> Tuple26.Mutable26<K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, A, B, C, D, E, F, G, H, I, J> insert(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, a, b, c, d, e, f, g, h, i, j);
		}
	}
}