package org.zeith.hammerlib.compat.base;

import lombok.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import org.zeith.hammerlib.util.java.Cast;

import java.util.Optional;
import java.util.function.Supplier;

@Getter
@Builder(builderClassName = "Builder")
public class CompatContext
{
	protected final IEventBus modBus;
	protected final Object data;
	
	public void runWhenOn(Dist dist, Supplier<Runnable> toRun)
	{
		if(dist == FMLEnvironment.dist)
		{
			toRun.get().run();
		}
	}
	
	public <T> Optional<T> data(Class<T> type)
	{
		return Cast.optionally(data, type);
	}
	
	public static Builder builder(IEventBus bus)
	{
		return new Builder().modBus(bus);
	}
}