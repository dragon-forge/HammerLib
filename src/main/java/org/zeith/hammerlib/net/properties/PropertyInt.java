package org.zeith.hammerlib.net.properties;

import net.minecraft.network.RegistryFriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

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
	public void write(RegistryFriendlyByteBuf buf)
	{
		buf.writeInt(value.get());
	}
	
	@Override
	public void read(RegistryFriendlyByteBuf buf)
	{
		value.set(buf.readInt());
	}
}