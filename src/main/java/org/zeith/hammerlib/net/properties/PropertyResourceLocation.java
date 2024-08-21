package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

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
	public void write(PacketBuffer buf)
	{
		ResourceLocation value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null)
			buf.writeResourceLocation(value);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readBoolean() ? buf.readResourceLocation() : null);
	}
}