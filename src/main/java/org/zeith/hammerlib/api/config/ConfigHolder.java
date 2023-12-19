package org.zeith.hammerlib.api.config;

import net.neoforged.fml.LogicalSide;
import org.zeith.hammerlib.util.SidedLocal;

public class ConfigHolder<T extends IConfigRoot>
{
	private final SidedLocal<T> data = new SidedLocal<>();

	public T getCurrent()
	{
		return data.get();
	}

	public T get(LogicalSide side)
	{
		return data.get(side);
	}

	public void update(T instance, LogicalSide side)
	{
		data.set(side, instance);
	}
}