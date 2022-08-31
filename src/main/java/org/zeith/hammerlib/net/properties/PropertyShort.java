package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

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
	public void write(FriendlyByteBuf buf)
	{
		buf.writeShort(value.get());
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readShort());
	}
}