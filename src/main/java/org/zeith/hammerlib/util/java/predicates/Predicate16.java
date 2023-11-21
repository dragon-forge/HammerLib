package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>
{
	boolean test(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p);
	
	default Predicate16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> and(Predicate16<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N, ? super O, ? super P> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) && other.test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> negate()
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> !test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> or(Predicate16<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N, ? super O, ? super P> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) || other.test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate15<B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate14<C, D, E, F, G, H, I, J, K, L, M, N, O, P> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate13<D, E, F, G, H, I, J, K, L, M, N, O, P> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate12<E, F, G, H, I, J, K, L, M, N, O, P> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate11<F, G, H, I, J, K, L, M, N, O, P> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate10<G, H, I, J, K, L, M, N, O, P> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate9<H, I, J, K, L, M, N, O, P> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate8<I, J, K, L, M, N, O, P> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate7<J, K, L, M, N, O, P> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate6<K, L, M, N, O, P> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate5<L, M, N, O, P> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate4<M, N, O, P> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m, n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate3<N, O, P> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (n, o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate2<O, P> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (o, p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate1<P> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (p) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> constR(P p)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> constR(O o, P p)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate13<A, B, C, D, E, F, G, H, I, J, K, L, M> constR(N n, O o, P p)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate12<A, B, C, D, E, F, G, H, I, J, K, L> constR(M m, N n, O o, P p)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate11<A, B, C, D, E, F, G, H, I, J, K> constR(L l, M m, N n, O o, P p)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate10<A, B, C, D, E, F, G, H, I, J> constR(K k, L l, M m, N n, O o, P p)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate9<A, B, C, D, E, F, G, H, I> constR(J j, K k, L l, M m, N n, O o, P p)
	{
		return (a, b, c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate8<A, B, C, D, E, F, G, H> constR(I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (a, b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (a, b, c) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (a, b) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o, P p)
	{
		return (a) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}
}