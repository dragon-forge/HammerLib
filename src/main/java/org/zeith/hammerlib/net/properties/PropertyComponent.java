package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyComponent
		extends PropertyBase<Component>
{
	public PropertyComponent(DirectStorage<Component> value)
	{
		super(Component.class, value);
	}
	
	public PropertyComponent()
	{
		super(Component.class);
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
}