package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>
{
	boolean test(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o);

	default Predicate15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> and(Predicate15<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N, ? super O> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) && other.test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}

	default Predicate15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> negate()
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> !test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}

	default Predicate15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> or(Predicate15<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N, ? super O> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) || other.test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}

	default Predicate14<B, C, D, E, F, G, H, I, J, K, L, M, N, O> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate13<C, D, E, F, G, H, I, J, K, L, M, N, O> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate12<D, E, F, G, H, I, J, K, L, M, N, O> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate11<E, F, G, H, I, J, K, L, M, N, O> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate10<F, G, H, I, J, K, L, M, N, O> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate9<G, H, I, J, K, L, M, N, O> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate8<H, I, J, K, L, M, N, O> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate7<I, J, K, L, M, N, O> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate6<J, K, L, M, N, O> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate5<K, L, M, N, O> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate4<L, M, N, O> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate3<M, N, O> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m, n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate2<N, O> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (n, o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate1<O> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (o) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}

	default Predicate14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> constR(O o)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate13<A, B, C, D, E, F, G, H, I, J, K, L, M> constR(N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate12<A, B, C, D, E, F, G, H, I, J, K, L> constR(M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate11<A, B, C, D, E, F, G, H, I, J, K> constR(L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate10<A, B, C, D, E, F, G, H, I, J> constR(K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate9<A, B, C, D, E, F, G, H, I> constR(J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate8<A, B, C, D, E, F, G, H> constR(I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b, c) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a, b) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n, O o)
	{
		return (a) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}
}