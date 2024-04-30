package org.zeith.hammerlib.api.inv;

import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.zeith.hammerlib.net.properties.IProperty;

import java.util.List;

public interface ComplexProgressHandler
{
	ComplexProgressManager create();
	
	void update(ComplexProgressManager manager);
	
	void containerTick(ComplexProgressManager manager);
	
	static ComplexProgressHandler withProperties(List<IProperty<?>> properties, RegistryAccess access)
	{
		return withProperties(0, properties, access);
	}
	
	static ComplexProgressHandler withProperties(int offset, List<IProperty<?>> properties, RegistryAccess access)
	{
		var buf = new RegistryFriendlyByteBuf(Unpooled.buffer(32), access);
		for(var property : properties)
			property.write(buf);
		final int cSize = buf.writerIndex();
		
		return new ComplexProgressHandler()
		{
			@Override
			public ComplexProgressManager create()
			{
				var cpm = new ComplexProgressManager(cSize, offset);
				for(int i = 0; i < properties.size(); i++)
					cpm.registerProperty("p" + i, properties.get(i));
				return cpm;
			}
			
			@Override
			public void update(ComplexProgressManager manager)
			{
				var buf = new RegistryFriendlyByteBuf(Unpooled.buffer(32), access);
				for(var property : properties)
					property.write(buf);
				manager.putBytes(0, buf.array(), 0, buf.writerIndex());
			}
			
			@Override
			public void containerTick(ComplexProgressManager manager)
			{
				var buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(manager.getBytes()), access);
				for(var property : properties)
					property.read(buf);
			}
		};
	}
}