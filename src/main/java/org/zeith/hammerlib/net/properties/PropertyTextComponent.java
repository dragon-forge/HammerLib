package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public class PropertyTextComponent
		implements IProperty<Component>
{
	final DirectStorage<Component> value;

	public PropertyTextComponent(DirectStorage<Component> value)
	{
		this.value = value;
	}

	public PropertyTextComponent()
	{
		this(DirectStorage.allocate());
	}

	@Override
	public Class<Component> getType()
	{
		return Component.class;
	}

	@Override
	public Component set(Component value)
	{
		Component pv = this.value.get();
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
		Component value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null) buf.writeComponent(value);
	}

	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readBoolean() ? buf.readComponent() : null);
	}

	@Override
	public Component get()
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