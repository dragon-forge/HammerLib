package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyDouble
		extends PropertyBase<Double>
{
	public PropertyDouble(DirectStorage<Double> value)
	{
		super(Double.class, value);
	}
	
	public PropertyDouble()
	{
		this(DirectStorage.allocate(0.0));
	}
	
	public double setDouble(double value)
	{
		return set(value);
	}
	
	public double getDouble()
	{
		return value.get();
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeDouble(value.get());
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readDouble());
	}
}