package org.zeith.hammerlib.net.properties;

import net.minecraft.network.PacketBuffer;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public class PropertyBool
		implements IProperty<Boolean>
{
	final DirectStorage<Boolean> value;

	public PropertyBool(DirectStorage<Boolean> value)
	{
		this.value = value;
	}

	public PropertyBool()
	{
		this(DirectStorage.allocate(false));
	}

	@Override
	public Class<Boolean> getType()
	{
		return Boolean.class;
	}

	@Override
	public Boolean set(Boolean value)
	{
		Boolean pv = this.value.get();
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
		buf.writeBoolean(value.get());
	}

	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readBoolean());
	}

	@Override
	public Boolean get()
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