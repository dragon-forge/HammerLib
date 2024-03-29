package org.zeith.hammerlib.net.properties;

import com.google.common.collect.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.*;
import org.zeith.hammerlib.util.mcf.ByteBufTransposer;

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
	
	public void tick(Level level)
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
	
	public void decodeChanges(FriendlyByteBuf buf)
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
		ByteBufTransposer.Builder transposer = ByteBufTransposer.begin();
		var buf = transposer.buffer();
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
		byte[] data = transposer.transpose();
		if(data.length > 0)
			return new SendPropertiesPacket(source.get(), data);
		return null;
	}
	
	@Nullable
	public SendPropertiesPacket createGlobalUpdate()
	{
		if(properties.isEmpty()) return null;
		
		ByteBufTransposer.Builder transposer = ByteBufTransposer.begin();
		var buf = transposer.buffer();
		properties.forEach((id, prop) ->
		{
			buf.writeUtf(id);
			prop.write(buf);
		});
		buf.writeUtf("!");
		return new SendPropertiesPacket(source.get(), transposer.transpose());
	}
}