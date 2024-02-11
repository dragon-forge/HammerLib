package com.zeitheron.hammercore.utils.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate11<A, B, C, D, E, F, G, H, I, J, K>
{
	boolean test(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k);

	default Predicate11<A, B, C, D, E, F, G, H, I, J, K> and(Predicate11<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k) && other.test(a, b, c, d, e, f, g, h, i, j, k);
	}

	default Predicate11<A, B, C, D, E, F, G, H, I, J, K> negate()
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> !test(a, b, c, d, e, f, g, h, i, j, k);
	}

	default Predicate11<A, B, C, D, E, F, G, H, I, J, K> or(Predicate11<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k) || other.test(a, b, c, d, e, f, g, h, i, j, k);
	}

	default Predicate10<B, C, D, E, F, G, H, I, J, K> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate9<C, D, E, F, G, H, I, J, K> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate8<D, E, F, G, H, I, J, K> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate7<E, F, G, H, I, J, K> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate6<F, G, H, I, J, K> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate5<G, H, I, J, K> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate4<H, I, J, K> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate3<I, J, K> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate2<J, K> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate1<K> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}

	default Predicate10<A, B, C, D, E, F, G, H, I, J> constR(K k)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate9<A, B, C, D, E, F, G, H, I> constR(J j, K k)
	{
		return (a, b, c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate8<A, B, C, D, E, F, G, H> constR(I i, J j, K k)
	{
		return (a, b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k)
	{
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k)
	{
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (a, b, c) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (a, b) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (a) -> test(a, b, c, d, e, f, g, h, i, j, k);
	}
}