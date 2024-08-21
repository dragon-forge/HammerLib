package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;

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
	public void write(PacketBuffer buf)
	{
		buf.writeLong(value.get());
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readLong());
	}
}