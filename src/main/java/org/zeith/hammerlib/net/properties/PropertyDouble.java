package org.zeith.hammerlib.net.properties;

import net.minecraft.network.RegistryFriendlyByteBuf;
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
	public void write(RegistryFriendlyByteBuf buf)
	{
		buf.writeDouble(value.get());
	}
	
	@Override
	public void read(RegistryFriendlyByteBuf buf)
	{
		value.set(buf.readDouble());
	}
}