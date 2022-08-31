package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyResourceLocation
		extends PropertyBase<ResourceLocation>
{
	public PropertyResourceLocation(DirectStorage<ResourceLocation> value)
	{
		super(ResourceLocation.class, value);
	}
	
	public PropertyResourceLocation()
	{
		super(ResourceLocation.class);
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
}