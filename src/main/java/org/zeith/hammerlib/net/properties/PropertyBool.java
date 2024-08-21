package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;

public class PropertyBool
		extends PropertyBase<Boolean>
{
	public PropertyBool(DirectStorage<Boolean> value)
	{
		super(Boolean.class, value);
	}
	
	public PropertyBool()
	{
		this(DirectStorage.allocate(false));
	}
	
	public boolean setBool(boolean value)
	{
		return set(value);
	}
	
	public boolean getBoolean()
	{
		return value.get();
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeBoolean(value.get());
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readBoolean());
	}
}