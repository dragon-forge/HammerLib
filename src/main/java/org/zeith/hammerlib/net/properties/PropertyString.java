package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public class PropertyString
		implements IProperty<String>
{
	final DirectStorage<String> value;

	public PropertyString(DirectStorage<String> value)
	{
		this.value = value;
	}

	public PropertyString()
	{
		this(DirectStorage.allocate());
	}

	@Override
	public Class<String> getType()
	{
		return String.class;
	}

	@Override
	public String set(String value)
	{
		String pv = this.value.get();
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
		String value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null) buf.writeUtf(value);
	}

	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readBoolean() ? buf.readUtf() : null);
	}

	@Override
	public String get()
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