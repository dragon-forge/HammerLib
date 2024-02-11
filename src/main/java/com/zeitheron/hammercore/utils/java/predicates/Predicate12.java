package com.zeitheron.hammercore.utils.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate12<A, B, C, D, E, F, G, H, I, J, K, L>
{
	boolean test(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l);

	default Predicate12<A, B, C, D, E, F, G, H, I, J, K, L> and(Predicate12<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l) && other.test(a, b, c, d, e, f, g, h, i, j, k, l);
	}

	default Predicate12<A, B, C, D, E, F, G, H, I, J, K, L> negate()
	{
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> !test(a, b, c, d, e, f, g, h, i, j, k, l);
	}

	default Predicate12<A, B, C, D, E, F, G, H, I, J, K, L> or(Predicate12<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J, ? super K, ? super L> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l) || other.test(a, b, c, d, e, f, g, h, i, j, k, l);
	}

	default Predicate11<B, C, D, E, F, G, H, I, J, K, L> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate10<C, D, E, F, G, H, I, J, K, L> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate9<D, E, F, G, H, I, J, K, L> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate8<E, F, G, H, I, J, K, L> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate7<F, G, H, I, J, K, L> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate6<G, H, I, J, K, L> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate5<H, I, J, K, L> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate4<I, J, K, L> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate3<J, K, L> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j, k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate2<K, L> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (k, l) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate1<L> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k)
	{
		return (l) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}

	default Predicate11<A, B, C, D, E, F, G, H, I, J, K> constR(L l)
	{
		return (a, b, c, d, e, f, g, h, i, j, k) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate10<A, B, C, D, E, F, G, H, I, J> constR(K k, L l)
	{
		return (a, b, c, d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate9<A, B, C, D, E, F, G, H, I> constR(J j, K k, L l)
	{
		return (a, b, c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate8<A, B, C, D, E, F, G, H> constR(I i, J j, K k, L l)
	{
		return (a, b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate7<A, B, C, D, E, F, G> constR(H h, I i, J j, K k, L l)
	{
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate6<A, B, C, D, E, F> constR(G g, H h, I i, J j, K k, L l)
	{
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f, G g, H h, I i, J j, K k, L l)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (a, b, c) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (a, b) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j, K k, L l)
	{
		return (a) -> test(a, b, c, d, e, f, g, h, i, j, k, l);
	}
}