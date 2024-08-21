package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;

public class PropertyString
		extends PropertyBase<String>
{
	public PropertyString(DirectStorage<String> value)
	{
		super(String.class, value);
	}
	
	public PropertyString()
	{
		super(String.class);
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		String value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null) buf.writeString(value);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readBoolean() ? buf.readString(32768) : null);
	}
}