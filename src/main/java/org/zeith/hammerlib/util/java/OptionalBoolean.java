package org.zeith.hammerlib.util.java;

import lombok.Getter;
import org.zeith.hammerlib.util.java.itf.BooleanConsumer;

import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.function.*;

public enum OptionalBoolean
{
	EMPTY(false, false),
	OPTIONAL_FALSE(true, false),
	OPTIONAL_TRUE(true, true);
	
	@Getter
	private final boolean present;
	private final boolean value;
	
	private OptionalBoolean(boolean present, boolean value)
	{
		this.present = present;
		this.value = value;
	}
	
	public static OptionalBoolean empty()
	{
		return EMPTY;
	}
	
	public static OptionalBoolean of(boolean value)
	{
		return value ? OPTIONAL_TRUE : OPTIONAL_FALSE;
	}
	
	public boolean isEmpty()
	{
		return !present;
	}
	
	public boolean getAsBoolean()
	{
		if(!present)
			throw new NoSuchElementException();
		return value;
	}
	
	public boolean orElse(boolean other)
	{
		return present ? value : other;
	}
	
	public boolean orElseGet(BooleanSupplier other)
	{
		return present ? value : other.getAsBoolean();
	}
	
	public <X extends Throwable> boolean orElseThrow(Supplier<X> exceptionSupplier)
			throws X
	{
		if(!present)
			throw exceptionSupplier.get();
		return value;
	}
	
	public boolean orElseThrow()
	{
		if(!present)
			throw new NoSuchElementException();
		return value;
	}
	
	public void ifPresent(BooleanConsumer action)
	{
		if(present)
			action.accept(value);
	}
	
	public void ifPresentOrElse(BooleanConsumer action, Runnable emptyAction)
	{
		if(present)
			action.accept(value);
		else
			emptyAction.run();
	}
	
	public OptionalInt toInt()
	{
		if(present)
			return OptionalInt.of(value ? 1 : 0);
		else
			return OptionalInt.empty();
	}
	
	@Override
	public String toString()
	{
		return !present ? "empty" : value ? "true" : "false";
	}
	
	public OptionalBoolean or(OptionalBoolean fallback)
	{
		return isPresent() ? this : fallback;
	}
}