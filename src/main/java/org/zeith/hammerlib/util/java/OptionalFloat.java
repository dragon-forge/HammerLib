package org.zeith.hammerlib.util.java;

import org.zeith.hammerlib.util.java.itf.*;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

public final class OptionalFloat
{
	private static final OptionalFloat EMPTY = new OptionalFloat();
	
	private final boolean isPresent;
	private final float value;
	
	private OptionalFloat()
	{
		this.isPresent = false;
		this.value = Float.NaN;
	}
	
	public static OptionalFloat empty()
	{
		return EMPTY;
	}
	
	private OptionalFloat(float value)
	{
		this.isPresent = true;
		this.value = value;
	}
	
	public static OptionalFloat of(float value)
	{
		return new OptionalFloat(value);
	}
	
	public float getAsFloat()
	{
		if(!isPresent)
		{
			throw new NoSuchElementException("No value present");
		}
		return value;
	}
	
	public boolean isPresent()
	{
		return isPresent;
	}
	
	public boolean isEmpty()
	{
		return !isPresent;
	}
	
	public void ifPresent(FloatConsumer action)
	{
		if(isPresent)
		{
			action.accept(value);
		}
	}
	
	public void ifPresentOrElse(FloatConsumer action, Runnable emptyAction)
	{
		if(isPresent)
		{
			action.accept(value);
		} else
		{
			emptyAction.run();
		}
	}
	
	public OptionalFloat filter(FloatPredicate predicate)
	{
		Objects.requireNonNull(predicate);
		if(isEmpty())
		{
			return this;
		} else
		{
			return predicate.test(value) ? this : empty();
		}
	}
	
	public float orElse(float other)
	{
		return isPresent ? value : other;
	}
	
	public float orElseGet(FloatSupplier supplier)
	{
		return isPresent ? value : supplier.getAsFloat();
	}
	
	public float orElseThrow()
	{
		if(!isPresent)
		{
			throw new NoSuchElementException("No value present");
		}
		return value;
	}
	
	public <X extends Throwable> float orElseThrow(Supplier<? extends X> exceptionSupplier)
			throws X
	{
		if(isPresent)
		{
			return value;
		} else
		{
			throw exceptionSupplier.get();
		}
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		return obj instanceof OptionalFloat other
			   && (isPresent && other.isPresent
				   ? Float.compare(value, other.value) == 0
				   : isPresent == other.isPresent);
	}
	
	@Override
	public int hashCode()
	{
		return isPresent ? Float.hashCode(value) : 0;
	}
	
	@Override
	public String toString()
	{
		return isPresent
			   ? ("OptionalFloat[" + value + "]")
			   : "OptionalFloat.empty";
	}
	
	public OptionalFloat or(OptionalFloat or)
	{
		return isPresent() ? this : or;
	}
}