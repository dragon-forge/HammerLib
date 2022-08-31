package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

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
	public void write(FriendlyByteBuf buf)
	{
		buf.writeByte(value.get());
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readByte());
	}
}