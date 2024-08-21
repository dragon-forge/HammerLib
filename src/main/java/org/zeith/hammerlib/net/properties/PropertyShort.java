package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;

public class PropertyShort
		extends PropertyBase<Short>
{
	public PropertyShort(DirectStorage<Short> value)
	{
		super(Short.class, value);
	}
	
	public PropertyShort()
	{
		this(DirectStorage.allocate((short) 0));
	}
	
	public short setShort(short value)
	{
		return set(value);
	}
	
	public short getShort()
	{
		return value.get();
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeShort(value.get());
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readShort());
	}
}