package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public class PropertyResourceLocation
		implements IProperty<ResourceLocation>
{
	final DirectStorage<ResourceLocation> value;

	public PropertyResourceLocation(DirectStorage<ResourceLocation> value)
	{
		this.value = value;
	}

	public PropertyResourceLocation()
	{
		this(DirectStorage.allocate());
	}

	@Override
	public Class<ResourceLocation> getType()
	{
		return ResourceLocation.class;
	}

	@Override
	public ResourceLocation set(ResourceLocation value)
	{
		ResourceLocation pv = this.value.get();
		if(!Objects.equals(pv, value))
		{
			this.value.set(value);
			markChanged(true);
		}
		return pv;
	}

	boolean changed;

	@Override
	public void markChanged(boolean changed)
	{
		this.changed = changed;
		if(changed) notifyDispatcherOfChange();
	}

	@Override
	public boolean hasChanged()
	{
		return changed;
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		ResourceLocation value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null)
			buf.writeResourceLocation(value);
	}

	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readBoolean() ? buf.readResourceLocation() : null);
	}

	@Override
	public ResourceLocation get()
	{
		return value.get();
	}

	PropertyDispatcher dispatcher;

	@Override
	public PropertyDispatcher getDispatcher()
	{
		return dispatcher;
	}

	@Override
	public void setDispatcher(PropertyDispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}
}