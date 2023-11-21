package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate13<A, B, C, D, E, F, G, H, I, J, K, L, M>
{
	boolean test(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m);
	
	default Predicate13<A, B, C, D, E, F, G, H, I, J, K, L, M> and(Predicate13<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m) && other.test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate13<A, B, C, D, E, F, G, H, I, J, K, L, M> negate()
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> !test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate13<A, B, C, D, E, F, G, H, I, J, K, L, M> or(Predicate13<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L, ? super M> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m) || other.test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate12<B, C, D, E, F, G, H, I, J, K, L, M> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate11<C, D, E, F, G, H, I, J, K, L, M> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate10<D, E, F, G, H, I, J, K, L, M> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate9<E, F, G, H, I, J, K, L, M> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate8<F, G, H, I, J, K, L, M> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate7<G, H, I, J, K, L, M> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate6<H, I, J, K, L, M> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate5<I, J, K, L, M> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate4<J, K, L, M> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate3<K, L, M> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate2<L, M> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l, m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate1<M> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (m) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate12<A, B, C, D, E, F, G, H, I, J, K, L> constR(M m)
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate11<A, B, C, D, E, F, G, H, I, J, K> constR(L l, M m)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate10<A, B, C, D, E, F, G, H, I, J> constR(K k, L l, M m)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate9<A, B, C, D, E, F, G, H, I> constR(J j, K k, L l, M m)
	{
		return (a, b, c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate8<A, B, C, D, E, F, G, H> constR(I i, J j, K k, L l, M m)
	{
		return (a, b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b, c) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a, b) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l, M m)
	{
		return (a) -> test(a, b, c, d, e, f, g, h, i, j, k, l, m);
	}
}