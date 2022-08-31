package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.UUID;

public class PropertyUUID
		extends PropertyBase<UUID>
{
	public PropertyUUID(DirectStorage<UUID> value)
	{
		super(UUID.class, value);
	}
	
	public PropertyUUID()
	{
		super(UUID.class);
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		UUID value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null) buf.writeUUID(value);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readBoolean() ? buf.readUUID() : null);
	}
}