package org.zeith.hammerlib.net.properties;

import net.minecraft.network.PacketBuffer;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public class PropertyFloat
		implements IProperty<Float>
{
	final DirectStorage<Float> value;

	public PropertyFloat(DirectStorage<Float> value)
	{
		this.value = value;
	}

	public PropertyFloat()
	{
		this(DirectStorage.allocate(0F));
	}

	@Override
	public Class<Float> getType()
	{
		return Float.class;
	}

	@Override
	public Float set(Float value)
	{
		Float pv = this.value.get();
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
		buf.writeFloat(value.get());
	}

	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readFloat());
	}

	@Override
	public Float get()
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