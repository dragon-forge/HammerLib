package org.zeith.hammerlib.net.properties;

import net.minecraft.network.PacketBuffer;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public class PropertyByte
		implements IProperty<Byte>
{
	final DirectStorage<Byte> value;

	public PropertyByte(DirectStorage<Byte> value)
	{
		this.value = value;
	}

	public PropertyByte()
	{
		this(DirectStorage.allocate((byte) 0));
	}

	@Override
	public Class<Byte> getType()
	{
		return Byte.class;
	}

	@Override
	public Byte set(Byte value)
	{
		Byte pv = this.value.get();
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
		buf.writeByte(value.get());
	}

	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readByte());
	}

	@Override
	public Byte get()
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