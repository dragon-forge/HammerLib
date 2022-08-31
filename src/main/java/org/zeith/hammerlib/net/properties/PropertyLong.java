package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyLong
		extends PropertyBase<Long>
{
	public PropertyLong(DirectStorage<Long> value)
	{
		super(Long.class, value);
	}
	
	public PropertyLong()
	{
		this(DirectStorage.allocate(0L));
	}
	
	public long setLong(long value)
	{
		return set(value);
	}
	
	public long getLong()
	{
		return value.get();
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeLong(value.get());
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readLong());
	}
}