package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;

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
	public void write(PacketBuffer buf)
	{
		buf.writeDouble(value.get());
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readDouble());
	}
}