package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

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
	public void write(FriendlyByteBuf buf)
	{
		buf.writeFloat(value.get());
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readFloat());
	}
}