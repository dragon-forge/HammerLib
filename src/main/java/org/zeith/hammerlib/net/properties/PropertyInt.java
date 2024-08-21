package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;

public class PropertyInt
		extends PropertyBase<Integer>
{
	public PropertyInt(DirectStorage<Integer> value)
	{
		super(Integer.class, value);
	}
	
	public PropertyInt()
	{
		this(DirectStorage.allocate(0));
	}
	
	public int setInt(int value)
	{
		return set(value);
	}
	
	public int getInt()
	{
		return value.get();
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeInt(value.get());
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readInt());
	}
}