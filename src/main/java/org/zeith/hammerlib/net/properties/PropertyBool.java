package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

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
	public void write(FriendlyByteBuf buf)
	{
		buf.writeBoolean(value.get());
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readBoolean());
	}
}