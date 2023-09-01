package org.zeith.hammerlib.net.properties;

import com.google.common.collect.*;
import io.netty.buffer.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class PropertyDispatcher
{
	public final BiMap<String, IProperty<?>> properties = HashBiMap.create();
	public final BiMap<IProperty<?>, String> propertiesInverse = properties.inverse();
	public final Map<String, IProperty<?>> dirty = new HashMap<>();
	public final Supplier<? extends IObjectSource<?>> source;
	public final Runnable onDirty;
	
	protected boolean clientSideSynced = false;
	
	public PropertyDispatcher(Supplier<? extends IObjectSource<?>> source, Runnable onDirty)
	{
		this.source = source;
		this.onDirty = onDirty;
	}
	
	public void tick(World level)
	{
		if(level.isClientSide && !clientSideSynced)
		{
			clientSideSynced = true;
			requestProperties();
		}
	}
	
	public <T> IProperty<T> registerProperty(String id, IProperty<T> property)
	{
		if(id.startsWith("!")) id = "." + id.substring(1);
		properties.put(id, property);
		property.setDispatcher(this);
		return property;
	}
	
	public IProperty<?> getProperty(String id)
	{
		if(id.startsWith("!")) id = "." + id.substring(1);
		return properties.get(id);
	}
	
	public void decodeChanges(PacketBuffer buf)
	{
		String str;
		while("!".compareTo(str = buf.readUtf()) != 0)
		{
			IProperty<?> prop = properties.get(str);
			if(prop != null)
				prop.read(buf);
		}
	}
	
	public void notifyOfChange(IProperty<?> prop)
	{
		String id = propertiesInverse.get(prop);
		if(prop.hasChanged())
		{
			dirty.put(id, prop);
			if(onDirty != null) onDirty.run();
		}
	}
	
	public void requestProperties()
	{
		Network.sendToServer(new RequestPropertiesPacket(source.get()));
	}
	
	@Nullable
	public SendPropertiesPacket detectAndGenerateChanges(boolean cleanse)
	{
		ByteBuf bb = Unpooled.buffer();
		PacketBuffer buf = new PacketBuffer(bb);
		if(!dirty.isEmpty())
		{
			dirty.forEach((id, prop) ->
			{
				if(prop.hasChanged())
				{
					buf.writeUtf(id);
					prop.write(buf);
					if(cleanse)
						prop.markChanged(false);
				}
			});
			if(cleanse)
				dirty.clear();
		}
		int size = bb.writerIndex();
		if(size > 0)
		{
			buf.writeUtf("!");
			bb.readerIndex(0);
			byte[] data = new byte[size];
			bb.readBytes(data);
			return new SendPropertiesPacket(source.get(), data);
		}
		return null;
	}
	
	@Nullable
	public SendPropertiesPacket createGlobalUpdate()
	{
		if(properties.isEmpty()) return null;
		
		ByteBuf bb = Unpooled.buffer();
		PacketBuffer buf = new PacketBuffer(bb);
		properties.forEach((id, prop) ->
		{
			buf.writeUtf(id);
			prop.write(buf);
		});
		buf.writeUtf("!");
		int size = bb.writerIndex();
		bb.readerIndex(0);
		byte[] data = new byte[size];
		bb.readBytes(data);
		return new SendPropertiesPacket(source.get(), data);
	}
}