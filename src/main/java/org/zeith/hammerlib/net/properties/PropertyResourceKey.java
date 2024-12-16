package org.zeith.hammerlib.net.properties;

import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyResourceKey<T>
		extends PropertyBase<ResourceKey<T>>
{
	protected final ResourceKey<? extends Registry<T>> registry;
	
	public PropertyResourceKey(ResourceKey<? extends Registry<T>> registry, DirectStorage<ResourceKey<T>> value)
	{
		super(Cast.cast(ResourceKey.class), value);
		this.registry = registry;
	}
	
	public PropertyResourceKey(ResourceKey<? extends Registry<T>> registry)
	{
		super(Cast.cast(ResourceKey.class));
		this.registry = registry;
	}
	
	@Override
	public void write(RegistryFriendlyByteBuf buf)
	{
		ResourceKey<T> value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null) buf.writeResourceKey(value);
	}
	
	@Override
	public void read(RegistryFriendlyByteBuf buf)
	{
		value.set(buf.readBoolean() ? buf.readResourceKey(registry) : null);
	}
}