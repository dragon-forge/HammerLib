package org.zeith.hammerlib.compat.base;

import lombok.Builder;
import lombok.Getter;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import org.zeith.hammerlib.abstractions.props.Key;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.api.proxy.IProxy;
import org.zeith.hammerlib.util.java.Cast;

import java.util.Optional;
import java.util.function.Supplier;

@Getter
@Builder(builderClassName = "Builder")
public class CompatContext
{
	protected final IEventBus modBus;
	protected final KeyMap properties;
	
	public void runWhenOn(Dist dist, Supplier<Runnable> toRun)
	{
		IProxy.runOn(dist, toRun);
	}
	
	public <T> Optional<T> data(Key<T> type)
	{
		return properties.opt(type);
	}
	
	public static Builder builder(IEventBus bus)
	{
		return new Builder()
				.modBus(bus);
	}
	
	public static class Builder
	{
		public Builder()
		{
			this.properties = KeyMap.createHash();
		}
		
		public <T> Builder property(Key<T> key, T value)
		{
			this.properties.put(key, value);
			return this;
		}
		
		private Builder properties(KeyMap properties)
		{
			return this;
		}
	}
}