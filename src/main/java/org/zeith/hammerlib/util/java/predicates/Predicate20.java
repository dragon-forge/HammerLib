package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>
{
	boolean test(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t);
	
	default Predicate20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> and(Predicate20<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N, ? super O, ? super P, ? super Q, ? super R, ? super S, ? super T> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) && other.test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> negate()
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> !test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> or(Predicate20<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N, ? super O, ? super P, ? super Q, ? super R, ? super S, ? super T> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) || other.test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate19<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate18<C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate17<D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate16<E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate15<F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate14<G, H, I, J, K, L, M, N, O, P, Q, R, S, T> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate13<H, I, J, K, L, M, N, O, P, Q, R, S, T> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate12<I, J, K, L, M, N, O, P, Q, R, S, T> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate11<J, K, L, M, N, O, P, Q, R, S, T> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate10<K, L, M, N, O, P, Q, R, S, T> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate9<L, M, N, O, P, Q, R, S, T> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate8<M, N, O, P, Q, R, S, T> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m, n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate7<N, O, P, Q, R, S, T> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (n, o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate6<O, P, Q, R, S, T> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (o, p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate5<P, Q, R, S, T> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (p, q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate4<Q, R, S, T> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (q, r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate3<R, S, T> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q)
	{
		return (r, s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate2<S, T> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r)
	{
		return (s, t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate1<T> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s)
	{
		return (t) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> constR(T t)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> constR(S s, T t)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> constR(R r, S s, T t)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> constR(Q q, R r, S s, T t)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> constR(P p, Q q, R r, S s, T t)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> constR(O o, P p, Q q, R r, S s, T t)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate13<A, B, C, D, E, F, G, H, I, J, K, L, M> constR(N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate12<A, B, C, D, E, F, G, H, I, J, K, L> constR(M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate11<A, B, C, D, E, F, G, H, I, J, K> constR(L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate10<A, B, C, D, E, F, G, H, I, J> constR(K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate9<A, B, C, D, E, F, G, H, I> constR(J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a, b, c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate8<A, B, C, D, E, F, G, H> constR(I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a, b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a, b, c) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a, b) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p, Q q, R r, S s, T t)
	{
		return (a) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}
}