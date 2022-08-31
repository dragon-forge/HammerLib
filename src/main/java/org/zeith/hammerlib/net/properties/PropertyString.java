package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyString
		extends PropertyBase<String>
{
	public PropertyString(DirectStorage<String> value)
	{
		super(String.class, value);
	}
	
	public PropertyString()
	{
		super(String.class);
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		String value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null) buf.writeUtf(value);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readBoolean() ? buf.readUtf() : null);
	}
}