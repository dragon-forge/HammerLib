package org.zeith.hammerlib.net.properties;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public class PropertyTextComponent
		implements IProperty<ITextComponent>
{
	final DirectStorage<ITextComponent> value;

	public PropertyTextComponent(DirectStorage<ITextComponent> value)
	{
		this.value = value;
	}

	public PropertyTextComponent()
	{
		this(DirectStorage.allocate());
	}

	@Override
	public Class<ITextComponent> getType()
	{
		return ITextComponent.class;
	}

	@Override
	public ITextComponent set(ITextComponent value)
	{
		ITextComponent pv = this.value.get();
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
		ITextComponent value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null) buf.writeComponent(value);
	}

	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readBoolean() ? buf.readComponent() : null);
	}

	@Override
	public ITextComponent get()
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