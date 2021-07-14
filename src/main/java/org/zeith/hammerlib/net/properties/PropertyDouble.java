package org.zeith.hammerlib.net.properties;

import net.minecraft.network.PacketBuffer;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public class PropertyDouble
		implements IProperty<Double>
{
	final DirectStorage<Double> value;

	public PropertyDouble(DirectStorage<Double> value)
	{
		this.value = value;
	}

	public PropertyDouble()
	{
		this(DirectStorage.allocate(0D));
	}

	@Override
	public Class<Double> getType()
	{
		return Double.class;
	}

	@Override
	public Double set(Double value)
	{
		Double pv = this.value.get();
		if(!Objects.equals(pv, value))
		{
			this.value.set(value);
			markChanged(true);
		}
		return pv;
	}

	boolean changed;

	@Override
	public void markChanged(boolean changed)
	{
		this.changed = changed;
		if(changed) notifyDispatcherOfChange();
	}

	@Override
	public boolean hasChanged()
	{
		return changed;
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

	@Override
	public Double get()
	{
		return value.get();
	}

	PropertyDispatcher dispatcher;

	@Override
	public PropertyDispatcher getDispatcher()
	{
		return dispatcher;
	}

	@Override
	public void setDispatcher(PropertyDispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}
}