package org.zeith.hammerlib.net.properties;

import net.minecraft.network.RegistryFriendlyByteBuf;
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
	public void write(RegistryFriendlyByteBuf buf)
	{
		Component value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null) buf.writeUtf(Component.Serializer.toJson(value, buf.registryAccess()));
	}
	
	@Override
	public void read(RegistryFriendlyByteBuf buf)
	{
		value.set(buf.readBoolean() ? Component.Serializer.fromJson(buf.readUtf(), buf.registryAccess()) : null);
	}
}