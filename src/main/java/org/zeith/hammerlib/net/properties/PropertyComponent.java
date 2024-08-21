package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public class PropertyComponent
		extends PropertyBase<ITextComponent>
{
	public PropertyComponent(DirectStorage<ITextComponent> value)
	{
		super(ITextComponent.class, value);
	}
	
	public PropertyComponent()
	{
		super(ITextComponent.class);
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		ITextComponent value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null) buf.writeString(ITextComponent.Serializer.componentToJson(value));
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readBoolean() ? ITextComponent.Serializer.jsonToComponent(buf.readString(32768)) : null);
	}
}