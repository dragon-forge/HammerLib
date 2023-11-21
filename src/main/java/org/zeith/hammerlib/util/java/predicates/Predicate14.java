package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>
{
	boolean test(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n);
	
	default Predicate14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> and(Predicate14<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n) && other.test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> negate()
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> !test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> or(Predicate14<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M, ? super N> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n) || other.test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate13<B, C, D, E, F, G, H, I, J, K, L, M, N> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate12<C, D, E, F, G, H, I, J, K, L, M, N> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate11<D, E, F, G, H, I, J, K, L, M, N> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate10<E, F, G, H, I, J, K, L, M, N> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate9<F, G, H, I, J, K, L, M, N> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate8<G, H, I, J, K, L, M, N> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate7<H, I, J, K, L, M, N> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate6<I, J, K, L, M, N> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate5<J, K, L, M, N> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate4<K, L, M, N> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate3<L, M, N> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate2<M, N> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m, n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate1<N> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (n) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate13<A, B, C, D, E, F, G, H, I, J, K, L, M> constR(N n)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate12<A, B, C, D, E, F, G, H, I, J, K, L> constR(M m, N n)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate11<A, B, C, D, E, F, G, H, I, J, K> constR(L l, M m, N n)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate10<A, B, C, D, E, F, G, H, I, J> constR(K k, L l, M m, N n)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate9<A, B, C, D, E, F, G, H, I> constR(J j, K k, L l, M m, N n)
	{
		return (a, b, c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate8<A, B, C, D, E, F, G, H> constR(I i, J j, K k, L l, M m, N n)
	{
		return (a, b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k, L l, M m, N n)
	{
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (a, b, c) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (a, b) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m, N n)
	{
		return (a) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}
}