package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;

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
	public void write(PacketBuffer buf)
	{
		UUID value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null) buf.writeUniqueId(value);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readBoolean() ? buf.readUniqueId() : null);
	}
}