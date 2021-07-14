package org.zeith.hammerlib.net.properties;

import net.minecraft.network.PacketBuffer;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;
import java.util.UUID;

public class PropertyUUID
		implements IProperty<UUID>
{
	final DirectStorage<UUID> value;

	public PropertyUUID(DirectStorage<UUID> value)
	{
		this.value = value;
	}

	public PropertyUUID()
	{
		this(DirectStorage.allocate());
	}

	@Override
	public Class<UUID> getType()
	{
		return UUID.class;
	}

	@Override
	public UUID set(UUID value)
	{
		UUID pv = this.value.get();
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
		UUID value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null) buf.writeUUID(value);
	}

	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readBoolean() ? buf.readUUID() : null);
	}

	@Override
	public UUID get()
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