package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;

public class PropertyFloat
		extends PropertyBase<Float>
{
	public PropertyFloat(DirectStorage<Float> value)
	{
		super(Float.class, value);
	}
	
	public PropertyFloat()
	{
		this(DirectStorage.allocate(0.0F));
	}
	
	public float setFloat(float value)
	{
		return set(value);
	}
	
	public float getFloat()
	{
		return value.get();
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeFloat(value.get());
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readFloat());
	}
}