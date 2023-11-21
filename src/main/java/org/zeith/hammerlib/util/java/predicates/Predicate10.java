package org.zeith.hammerlib.util.java.predicates;

import java.util.Objects;

@FunctionalInterface
public interface Predicate10<A, B, C, D, E, F, G, H, I, J>
{
	boolean test(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j);

	default Predicate10<A, B, C, D, E, F, G, H, I, J> and(Predicate10<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j) && other.test(a, b, c, d, e, f, g, h, i, j);
	}

	default Predicate10<A, B, C, D, E, F, G, H, I, J> negate()
	{
		return (a, b, c, d, e, f, g, h, i, j) -> !test(a, b, c, d, e, f, g, h, i, j);
	}

	default Predicate10<A, B, C, D, E, F, G, H, I, J> or(Predicate10<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? super J> other)
	{
		Objects.requireNonNull(other);
		return (a, b, c, d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j) || other.test(a, b, c, d, e, f, g, h, i, j);
	}

	default Predicate9<B, C, D, E, F, G, H, I, J> constL(A a)
	{
		return (b, c, d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate8<C, D, E, F, G, H, I, J> constL(A a, B b)
	{
		return (c, d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate7<D, E, F, G, H, I, J> constL(A a, B b, C c)
	{
		return (d, e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate6<E, F, G, H, I, J> constL(A a, B b, C c, D d)
	{
		return (e, f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate5<F, G, H, I, J> constL(A a, B b, C c, D d, E e)
	{
		return (f, g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate4<G, H, I, J> constL(A a, B b, C c, D d, E e, F f)
	{
		return (g, h, i, j) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate3<H, I, J> constL(A a, B b, C c, D d, E e, F f, G g)
	{
		return (h, i, j) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate2<I, J> constL(A a, B b, C c, D d, E e, F f, G g, H h)
	{
		return (i, j) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate1<J> constL(A a, B b, C c, D d, E e, F f, G g, H h, I i)
	{
		return (j) -> test(a, b, c, d, e, f, g, h, i, j);
	}

	default Predicate9<A, B, C, D, E, F, G, H, I> constR(J j)
	{
		return (a, b, c, d, e, f, g, h, i) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate8<A, B, C, D, E, F, G, H> constR(I i, J j)
	{
		return (a, b, c, d, e, f, g, h) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate7<A, B, C, D, E, F, G> constR(H h, I i, J j)
	{
		return (a, b, c, d, e, f, g) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate6<A, B, C, D, E, F> constR(G g, H h, I i, J j)
	{
		return (a, b, c, d, e, f) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate5<A, B, C, D, E> constR(F f, G g, H h, I i, J j)
	{
		return (a, b, c, d, e) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate4<A, B, C, D> constR(E e, F f, G g, H h, I i, J j)
	{
		return (a, b, c, d) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate3<A, B, C> constR(D d, E e, F f, G g, H h, I i, J j)
	{
		return (a, b, c) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate2<A, B> constR(C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (a, b) -> test(a, b, c, d, e, f, g, h, i, j);
	}
	
	default Predicate1<A> constR(B b, C c, D d, E e, F f, G g, H h, I i, J j)
	{
		return (a) -> test(a, b, c, d, e, f, g, h, i, j);
	}
}