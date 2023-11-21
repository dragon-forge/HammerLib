package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V>
{
	boolean test(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v);

	default Predicate22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> and(Predicate22<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N, ? super O, ? super P, ? super Q, ? super R, ? super S, ? super T, ? super U, ? super V> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) && other.test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}

	default Predicate22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> negate()
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> !test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}

	default Predicate22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> or(Predicate22<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N, ? super O, ? super P, ? super Q, ? super R, ? super S, ? super T, ? super U, ? super V> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) || other.test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}

	default Predicate21<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate20<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate19<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate18<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate17<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate16<G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate15<H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate14<I, J, K, L, M, N, O, P, Q, R, S, T, U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate13<J, K, L, M, N, O, P, Q, R, S, T, U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate12<K, L, M, N, O, P, Q, R, S, T, U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate11<L, M, N, O, P, Q, R, S, T, U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate10<M, N, O, P, Q, R, S, T, U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m, n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate9<N, O, P, Q, R, S, T, U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (n, o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate8<O, P, Q, R, S, T, U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (o, p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate7<P, Q, R, S, T, U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (p, q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate6<Q, R, S, T, U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (q, r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate5<R, S, T, U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (r, s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate4<S, T, U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return (s, t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate3<T, U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return (t, u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate2<U, V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (u, v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate1<V> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u)
	{
		return (v) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}

	default Predicate21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> constR(V v)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> constR(U u, V v)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> constR(T t, U u, V v)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> constR(S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> constR(R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> constR(Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> constR(P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> constR(O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate13<A, B, C, D, E, F, G, H, I, J, K, L, M> constR(N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate12<A, B, C, D, E, F, G, H, I, J, K, L> constR(M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate11<A, B, C, D, E, F, G, H, I, J, K> constR(L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate10<A, B, C, D, E, F, G, H, I, J> constR(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate9<A, B, C, D, E, F, G, H, I> constR(J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate8<A, B, C, D, E, F, G, H> constR(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b, c) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a, b) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t, U u, V v)
	{
		return (a) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}
}