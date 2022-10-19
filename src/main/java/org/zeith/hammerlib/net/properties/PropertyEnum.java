package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyEnum<E extends Enum<E>>
		extends PropertyBase<E>
{
	public PropertyEnum(Class<E> type, DirectStorage<E> value)
	{
		super(type, value);
	}
	
	public PropertyEnum(E value)
	{
		this(value.getDeclaringClass(), DirectStorage.allocate(value));
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeEnum(value.get());
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readEnum(type));
	}
}