package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public class PropertyInt
		implements IProperty<Integer>
{
	final DirectStorage<Integer> value;

	public PropertyInt(DirectStorage<Integer> value)
	{
		this.value = value;
	}

	public PropertyInt()
	{
		this(DirectStorage.allocate(0));
	}

	@Override
	public Class<Integer> getType()
	{
		return Integer.class;
	}

	public int setInt(int value)
	{
		return set(value);
	}

	public int getInt()
	{
		return value.get();
	}

	@Override
	public Integer set(Integer value)
	{
		Integer pv = this.value.get();
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
		buf.writeInt(value.get());
	}

	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readInt());
	}

	@Override
	public Integer get()
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