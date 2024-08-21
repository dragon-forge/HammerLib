package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;

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
	public void write(PacketBuffer buf)
	{
		buf.writeEnumValue(value.get());
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readEnumValue(type));
	}
}