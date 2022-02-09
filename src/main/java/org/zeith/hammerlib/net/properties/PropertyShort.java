package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public class PropertyShort
		implements IProperty<Short>
{
	final DirectStorage<Short> value;

	public PropertyShort(DirectStorage<Short> value)
	{
		this.value = value;
	}

	public PropertyShort()
	{
		this(DirectStorage.allocate((short) 0));
	}

	@Override
	public Class<Short> getType()
	{
		return Short.class;
	}

	@Override
	public Short set(Short value)
	{
		Short pv = this.value.get();
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
	public void write(FriendlyByteBuf buf)
	{
		buf.writeShort(value.get());
	}

	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readShort());
	}

	@Override
	public Short get()
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