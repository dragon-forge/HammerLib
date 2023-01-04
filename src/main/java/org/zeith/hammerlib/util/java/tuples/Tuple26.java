package org.zeith.hammerlib.util.java.tuples;

import org.zeith.hammerlib.util.java.consumers.*;
import org.zeith.hammerlib.util.java.functions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z>
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
	protected X x;
	protected Y y;
	protected Z z;
	
	public Tuple26(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
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
		this.x = x;
		this.y = y;
		this.z = z;
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
	
	public X x()
	{
		return x;
	}
	
	public Y y()
	{
		return y;
	}
	
	public Z z()
	{
		return z;
	}
	
	public Tuple1<A> strip25R()
	{
		return Tuples.immutable(a);
	}
	
	public Tuple1<Z> strip25L()
	{
		return Tuples.immutable(z);
	}
	
	public Tuple2<A, B> strip24R()
	{
		return Tuples.immutable(a, b);
	}
	
	public Tuple2<Y, Z> strip24L()
	{
		return Tuples.immutable(y, z);
	}
	
	public Tuple3<A, B, C> strip23R()
	{
		return Tuples.immutable(a, b, c);
	}
	
	public Tuple3<X, Y, Z> strip23L()
	{
		return Tuples.immutable(x, y, z);
	}
	
	public Tuple4<A, B, C, D> strip22R()
	{
		return Tuples.immutable(a, b, c, d);
	}
	
	public Tuple4<W, X, Y, Z> strip22L()
	{
		return Tuples.immutable(w, x, y, z);
	}
	
	public Tuple5<A, B, C, D, E> strip21R()
	{
		return Tuples.immutable(a, b, c, d, e);
	}
	
	public Tuple5<V, W, X, Y, Z> strip21L()
	{
		return Tuples.immutable(v, w, x, y, z);
	}
	
	public Tuple6<A, B, C, D, E, F> strip20R()
	{
		return Tuples.immutable(a, b, c, d, e, f);
	}
	
	public Tuple6<U, V, W, X, Y, Z> strip20L()
	{
		return Tuples.immutable(u, v, w, x, y, z);
	}
	
	public Tuple7<A, B, C, D, E, F, G> strip19R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g);
	}
	
	public Tuple7<T, U, V, W, X, Y, Z> strip19L()
	{
		return Tuples.immutable(t, u, v, w, x, y, z);
	}
	
	public Tuple8<A, B, C, D, E, F, G, H> strip18R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h);
	}
	
	public Tuple8<S, T, U, V, W, X, Y, Z> strip18L()
	{
		return Tuples.immutable(s, t, u, v, w, x, y, z);
	}
	
	public Tuple9<A, B, C, D, E, F, G, H, I> strip17R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i);
	}
	
	public Tuple9<R, S, T, U, V, W, X, Y, Z> strip17L()
	{
		return Tuples.immutable(r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple10<A, B, C, D, E, F, G, H, I, J> strip16R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j);
	}
	
	public Tuple10<Q, R, S, T, U, V, W, X, Y, Z> strip16L()
	{
		return Tuples.immutable(q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple11<A, B, C, D, E, F, G, H, I, J, K> strip15R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public Tuple11<P, Q, R, S, T, U, V, W, X, Y, Z> strip15L()
	{
		return Tuples.immutable(p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple12<A, B, C, D, E, F, G, H, I, J, K, L> strip14R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public Tuple12<O, P, Q, R, S, T, U, V, W, X, Y, Z> strip14L()
	{
		return Tuples.immutable(o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip13R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public Tuple13<N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip13L()
	{
		return Tuples.immutable(n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip12R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public Tuple14<M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip12L()
	{
		return Tuples.immutable(m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip11R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public Tuple15<L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip11L()
	{
		return Tuples.immutable(l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip10R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public Tuple16<K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip10L()
	{
		return Tuples.immutable(k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> strip9R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public Tuple17<J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip9L()
	{
		return Tuples.immutable(j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> strip8R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public Tuple18<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip8L()
	{
		return Tuples.immutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip7R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public Tuple19<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip7L()
	{
		return Tuples.immutable(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> strip6R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public Tuple20<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip6L()
	{
		return Tuples.immutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> strip5R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public Tuple21<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip5L()
	{
		return Tuples.immutable(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip4R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public Tuple22<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip4L()
	{
		return Tuples.immutable(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip3R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public Tuple23<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip3L()
	{
		return Tuples.immutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip2R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public Tuple24<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip2L()
	{
		return Tuples.immutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> strip1R()
	{
		return Tuples.immutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public Tuple25<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip1L()
	{
		return Tuples.immutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	
	public <RES> RES applyL(Function1<A, RES> func)
	{
		return func.apply(a);
	}
	
	public <RES> RES applyR(Function1<Z, RES> func)
	{
		return func.apply(z);
	}
	
	public <RES> RES applyL(Function2<A, B, RES> func)
	{
		return func.apply(a, b);
	}
	
	public <RES> RES applyR(Function2<Y, Z, RES> func)
	{
		return func.apply(y, z);
	}
	
	public <RES> RES applyL(Function3<A, B, C, RES> func)
	{
		return func.apply(a, b, c);
	}
	
	public <RES> RES applyR(Function3<X, Y, Z, RES> func)
	{
		return func.apply(x, y, z);
	}
	
	public <RES> RES applyL(Function4<A, B, C, D, RES> func)
	{
		return func.apply(a, b, c, d);
	}
	
	public <RES> RES applyR(Function4<W, X, Y, Z, RES> func)
	{
		return func.apply(w, x, y, z);
	}
	
	public <RES> RES applyL(Function5<A, B, C, D, E, RES> func)
	{
		return func.apply(a, b, c, d, e);
	}
	
	public <RES> RES applyR(Function5<V, W, X, Y, Z, RES> func)
	{
		return func.apply(v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function6<A, B, C, D, E, F, RES> func)
	{
		return func.apply(a, b, c, d, e, f);
	}
	
	public <RES> RES applyR(Function6<U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function7<A, B, C, D, E, F, G, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g);
	}
	
	public <RES> RES applyR(Function7<T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function8<A, B, C, D, E, F, G, H, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h);
	}
	
	public <RES> RES applyR(Function8<S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function9<A, B, C, D, E, F, G, H, I, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i);
	}
	
	public <RES> RES applyR(Function9<R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function10<A, B, C, D, E, F, G, H, I, J, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j);
	}
	
	public <RES> RES applyR(Function10<Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function11<A, B, C, D, E, F, G, H, I, J, K, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public <RES> RES applyR(Function11<P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public <RES> RES applyR(Function12<O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public <RES> RES applyR(Function13<N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public <RES> RES applyR(Function14<M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public <RES> RES applyR(Function15<L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public <RES> RES applyR(Function16<K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public <RES> RES applyR(Function17<J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public <RES> RES applyR(Function18<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public <RES> RES applyR(Function19<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public <RES> RES applyR(Function20<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public <RES> RES applyR(Function21<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public <RES> RES applyR(Function22<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public <RES> RES applyR(Function23<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public <RES> RES applyR(Function24<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES applyL(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public <RES> RES applyR(Function25<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public <RES> RES apply(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> func)
	{
		return func.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer1<A> consumer)
	{
		consumer.accept(a);
	}
	
	public void acceptR(Consumer1<Z> consumer)
	{
		consumer.accept(z);
	}
	
	public void acceptL(Consumer2<A, B> consumer)
	{
		consumer.accept(a, b);
	}
	
	public void acceptR(Consumer2<Y, Z> consumer)
	{
		consumer.accept(y, z);
	}
	
	public void acceptL(Consumer3<A, B, C> consumer)
	{
		consumer.accept(a, b, c);
	}
	
	public void acceptR(Consumer3<X, Y, Z> consumer)
	{
		consumer.accept(x, y, z);
	}
	
	public void acceptL(Consumer4<A, B, C, D> consumer)
	{
		consumer.accept(a, b, c, d);
	}
	
	public void acceptR(Consumer4<W, X, Y, Z> consumer)
	{
		consumer.accept(w, x, y, z);
	}
	
	public void acceptL(Consumer5<A, B, C, D, E> consumer)
	{
		consumer.accept(a, b, c, d, e);
	}
	
	public void acceptR(Consumer5<V, W, X, Y, Z> consumer)
	{
		consumer.accept(v, w, x, y, z);
	}
	
	public void acceptL(Consumer6<A, B, C, D, E, F> consumer)
	{
		consumer.accept(a, b, c, d, e, f);
	}
	
	public void acceptR(Consumer6<U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer7<A, B, C, D, E, F, G> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g);
	}
	
	public void acceptR(Consumer7<T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer8<A, B, C, D, E, F, G, H> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h);
	}
	
	public void acceptR(Consumer8<S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer9<A, B, C, D, E, F, G, H, I> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i);
	}
	
	public void acceptR(Consumer9<R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer10<A, B, C, D, E, F, G, H, I, J> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j);
	}
	
	public void acceptR(Consumer10<Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer11<A, B, C, D, E, F, G, H, I, J, K> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	public void acceptR(Consumer11<P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer12<A, B, C, D, E, F, G, H, I, J, K, L> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	public void acceptR(Consumer12<O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer13<A, B, C, D, E, F, G, H, I, J, K, L, M> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	public void acceptR(Consumer13<N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	public void acceptR(Consumer14<M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	public void acceptR(Consumer15<L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	public void acceptR(Consumer16<K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}
	
	public void acceptR(Consumer17<J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}
	
	public void acceptR(Consumer18<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}
	
	public void acceptR(Consumer19<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	public void acceptR(Consumer20<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}
	
	public void acceptR(Consumer21<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	public void acceptR(Consumer22<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}
	
	public void acceptR(Consumer23<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}
	
	public void acceptR(Consumer24<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void acceptL(Consumer25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}
	
	public void acceptR(Consumer25<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public void accept(Consumer26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> consumer)
	{
		consumer.accept(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public @Override int arity()
	{
		return 26;
	}
	
	public @Override Stream<?> stream()
	{
		return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> mutable()
	{
		return new Mutable26<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> immutable()
	{
		return this;
	}
	
	public Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> copy()
	{
		return new Tuple26<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public @Override String toString()
	{
		return "Tuple26" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
	}
	
	public @Override int hashCode()
	{
		return java.util.Objects.hash(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}
	
	public @Override
	@SuppressWarnings("rawtypes") boolean equals(Object other)
	{
		if(!(other instanceof ITuple)) return false;
		ITuple tuple = (ITuple) other;
		return tuple.arity() == arity() && Tuples.streamEquals(stream(), tuple.stream());
	}
	
	public static class Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z>
			extends Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z>
	{
		public Mutable26(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v, W w, X x, Y y, Z z)
		{
			super(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> mutable()
		{
			return this;
		}
		
		public @Override Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> copy()
		{
			return new Mutable26<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> immutable()
		{
			return new Tuple26<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override String toString()
		{
			return "Tuple26.Mutable26" + stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setA(A a)
		{
			this.a = a;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setB(B b)
		{
			this.b = b;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setC(C c)
		{
			this.c = c;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setD(D d)
		{
			this.d = d;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setE(E e)
		{
			this.e = e;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setF(F f)
		{
			this.f = f;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setG(G g)
		{
			this.g = g;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setH(H h)
		{
			this.h = h;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setI(I i)
		{
			this.i = i;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setJ(J j)
		{
			this.j = j;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setK(K k)
		{
			this.k = k;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setL(L l)
		{
			this.l = l;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setM(M m)
		{
			this.m = m;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setN(N n)
		{
			this.n = n;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setO(O o)
		{
			this.o = o;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setP(P p)
		{
			this.p = p;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setQ(Q q)
		{
			this.q = q;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setR(R r)
		{
			this.r = r;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setS(S s)
		{
			this.s = s;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setT(T t)
		{
			this.t = t;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setU(U u)
		{
			this.u = u;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setV(V v)
		{
			this.v = v;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setW(W w)
		{
			this.w = w;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setX(X x)
		{
			this.x = x;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setY(Y y)
		{
			this.y = y;
			return this;
		}
		
		public Mutable26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> setZ(Z z)
		{
			this.z = z;
			return this;
		}
		
		public @Override Tuple1.Mutable1<A> strip25R()
		{
			return Tuples.mutable(a);
		}
		
		public @Override Tuple1.Mutable1<Z> strip25L()
		{
			return Tuples.mutable(z);
		}
		
		public @Override Tuple2.Mutable2<A, B> strip24R()
		{
			return Tuples.mutable(a, b);
		}
		
		public @Override Tuple2.Mutable2<Y, Z> strip24L()
		{
			return Tuples.mutable(y, z);
		}
		
		public @Override Tuple3.Mutable3<A, B, C> strip23R()
		{
			return Tuples.mutable(a, b, c);
		}
		
		public @Override Tuple3.Mutable3<X, Y, Z> strip23L()
		{
			return Tuples.mutable(x, y, z);
		}
		
		public @Override Tuple4.Mutable4<A, B, C, D> strip22R()
		{
			return Tuples.mutable(a, b, c, d);
		}
		
		public @Override Tuple4.Mutable4<W, X, Y, Z> strip22L()
		{
			return Tuples.mutable(w, x, y, z);
		}
		
		public @Override Tuple5.Mutable5<A, B, C, D, E> strip21R()
		{
			return Tuples.mutable(a, b, c, d, e);
		}
		
		public @Override Tuple5.Mutable5<V, W, X, Y, Z> strip21L()
		{
			return Tuples.mutable(v, w, x, y, z);
		}
		
		public @Override Tuple6.Mutable6<A, B, C, D, E, F> strip20R()
		{
			return Tuples.mutable(a, b, c, d, e, f);
		}
		
		public @Override Tuple6.Mutable6<U, V, W, X, Y, Z> strip20L()
		{
			return Tuples.mutable(u, v, w, x, y, z);
		}
		
		public @Override Tuple7.Mutable7<A, B, C, D, E, F, G> strip19R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g);
		}
		
		public @Override Tuple7.Mutable7<T, U, V, W, X, Y, Z> strip19L()
		{
			return Tuples.mutable(t, u, v, w, x, y, z);
		}
		
		public @Override Tuple8.Mutable8<A, B, C, D, E, F, G, H> strip18R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h);
		}
		
		public @Override Tuple8.Mutable8<S, T, U, V, W, X, Y, Z> strip18L()
		{
			return Tuples.mutable(s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple9.Mutable9<A, B, C, D, E, F, G, H, I> strip17R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i);
		}
		
		public @Override Tuple9.Mutable9<R, S, T, U, V, W, X, Y, Z> strip17L()
		{
			return Tuples.mutable(r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple10.Mutable10<A, B, C, D, E, F, G, H, I, J> strip16R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j);
		}
		
		public @Override Tuple10.Mutable10<Q, R, S, T, U, V, W, X, Y, Z> strip16L()
		{
			return Tuples.mutable(q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple11.Mutable11<A, B, C, D, E, F, G, H, I, J, K> strip15R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k);
		}
		
		public @Override Tuple11.Mutable11<P, Q, R, S, T, U, V, W, X, Y, Z> strip15L()
		{
			return Tuples.mutable(p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple12.Mutable12<A, B, C, D, E, F, G, H, I, J, K, L> strip14R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l);
		}
		
		public @Override Tuple12.Mutable12<O, P, Q, R, S, T, U, V, W, X, Y, Z> strip14L()
		{
			return Tuples.mutable(o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple13.Mutable13<A, B, C, D, E, F, G, H, I, J, K, L, M> strip13R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m);
		}
		
		public @Override Tuple13.Mutable13<N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip13L()
		{
			return Tuples.mutable(n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple14.Mutable14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> strip12R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
		}
		
		public @Override Tuple14.Mutable14<M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip12L()
		{
			return Tuples.mutable(m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple15.Mutable15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> strip11R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
		}
		
		public @Override Tuple15.Mutable15<L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip11L()
		{
			return Tuples.mutable(l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple16.Mutable16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> strip10R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
		}
		
		public @Override Tuple16.Mutable16<K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip10L()
		{
			return Tuples.mutable(k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple17.Mutable17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> strip9R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
		}
		
		public @Override Tuple17.Mutable17<J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip9L()
		{
			return Tuples.mutable(j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple18.Mutable18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> strip8R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
		}
		
		public @Override Tuple18.Mutable18<I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip8L()
		{
			return Tuples.mutable(i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple19.Mutable19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> strip7R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
		}
		
		public @Override Tuple19.Mutable19<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip7L()
		{
			return Tuples.mutable(h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple20.Mutable20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> strip6R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
		}
		
		public @Override Tuple20.Mutable20<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip6L()
		{
			return Tuples.mutable(g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple21.Mutable21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> strip5R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
		}
		
		public @Override Tuple21.Mutable21<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip5L()
		{
			return Tuples.mutable(f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple22.Mutable22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> strip4R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
		}
		
		public @Override Tuple22.Mutable22<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip4L()
		{
			return Tuples.mutable(e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple23.Mutable23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W> strip3R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
		}
		
		public @Override Tuple23.Mutable23<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip3L()
		{
			return Tuples.mutable(d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple24.Mutable24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X> strip2R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
		}
		
		public @Override Tuple24.Mutable24<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip2L()
		{
			return Tuples.mutable(c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		public @Override Tuple25.Mutable25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y> strip1R()
		{
			return Tuples.mutable(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
		}
		
		public @Override Tuple25.Mutable25<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z> strip1L()
		{
			return Tuples.mutable(b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
		}
		
		
	}
}