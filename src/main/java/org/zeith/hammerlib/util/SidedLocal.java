package org.zeith.hammerlib.util;

import net.minecraftforge.fml.LogicalSide;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

import java.util.EnumMap;
import java.util.Objects;
import java.util.function.Function;

public class SidedLocal<T>
{
	final EnumMap<LogicalSide, T> values = new EnumMap<>(LogicalSide.class);
	final Function<LogicalSide, T> defaultValues;

	public SidedLocal()
	{
		this.defaultValues = side -> null;
	}

	public SidedLocal(Function<LogicalSide, T> defaultValue)
	{
		this.defaultValues = defaultValue;
	}

	public LogicalSide currentEnvironment()
	{
		return LogicalSidePredictor.getCurrentLogicalSide();
	}

	public boolean equalsTo(T value)
	{
		return Objects.equals(get(), value);
	}

	public T set(T value)
	{
		return values.put(currentEnvironment(), value);
	}

	public T get()
	{
		return values.computeIfAbsent(currentEnvironment(), defaultValues);
	}

	public T set(LogicalSide side, T value)
	{
		return values.put(side, value);
	}

	public T get(LogicalSide side)
	{
		return values.computeIfAbsent(side, defaultValues);
	}
}