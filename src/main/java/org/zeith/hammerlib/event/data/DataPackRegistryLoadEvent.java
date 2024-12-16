package org.zeith.hammerlib.event.data;

import lombok.Getter;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import org.zeith.hammerlib.util.java.Cast;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Fired whenever a datapack registry is being reloaded.
 * You can inspect or register additional things into registries before they get frozen.
 * <p>
 * This event is fired on {@link net.neoforged.neoforge.common.NeoForge#EVENT_BUS}
 */
public class DataPackRegistryLoadEvent
		extends Event
{
	@Getter
	private final Source source;
	private final GenericRegistry registry;
	
	public DataPackRegistryLoadEvent(Source source, WritableRegistry<?> registry)
	{
		this.source = source;
		this.registry = new GenericRegistry(registry);
	}
	
	public <T> Optional<WritableRegistry<T>> getRegistry(ResourceKey<? extends Registry<T>> key)
	{
		return registry.get(key);
	}
	
	public <T> boolean register(ResourceKey<? extends Registry<T>> key, ResourceLocation id, Supplier<T> value)
	{
		return getRegistry(key).map(wr ->
		{
			wr.register(ResourceKey.create(wr.key(), id), value.get(), RegistrationInfo.BUILT_IN);
			return true;
		}).orElse(false);
	}
	
	public <T> boolean inspect(ResourceKey<? extends Registry<T>> key, ResourceLocation id, Consumer<T> inspector)
	{
		return getRegistry(key).map(wr ->
		{
			var t = wr.getValue(id);
			if(t != null) inspector.accept(t);
			return t != null;
		}).orElse(false);
	}
	
	public record GenericRegistry(WritableRegistry<?> registry)
	{
		public <T> Optional<WritableRegistry<T>> get(ResourceKey<? extends Registry<T>> key)
		{
			return registry.key().equals(key) ? Optional.of(Cast.cast(registry)) : Optional.empty();
		}
	}
	
	@Override
	public String toString()
	{
		return "DataPackRegistryLoadEvent{" +
			   "source=" + source +
			   ", registry=" + registry.registry().key() +
			   '}';
	}
	
	public enum Source
	{
		RELOADABLE_SERVER_REGISTRIES,
		REGISTRY_DATA_LOADER
	}
}