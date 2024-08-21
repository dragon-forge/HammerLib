package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;

public class PropertyByte
		extends PropertyBase<Byte>
{
	public PropertyByte(DirectStorage<Byte> value)
	{
		super(Byte.class, value);
	}
	
	public PropertyByte()
	{
		this(DirectStorage.allocate((byte) 0));
	}
	
	public byte setByte(byte value)
	{
		return set(value);
	}
	
	public byte getByte()
	{
		return value.get();
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeByte(value.get());
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readByte());
	}
}