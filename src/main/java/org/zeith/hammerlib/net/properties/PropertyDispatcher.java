package org.zeith.hammerlib.net.properties;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import org.zeith.hammerlib.net.packets.SendPropertiesPacket;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class PropertyDispatcher
{
	public final BiMap<String, IProperty<?>> properties = HashBiMap.create();
	public final BiMap<IProperty<?>, String> propertiesInverse = properties.inverse();
	public final Map<String, IProperty<?>> dirty = new HashMap<>();
	public final Runnable onDirty;

	public PropertyDispatcher(Runnable onDirty)
	{
		this.onDirty = onDirty;
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

	@Nullable
	public SendPropertiesPacket detectAndGenerateChanges(BlockPos pos, boolean cleanse)
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
		buf.writeUtf("!");
		int size = bb.writerIndex();
		if(size > 0)
		{
			bb.readerIndex(0);
			byte[] data = new byte[size];
			bb.readBytes(data);
			return new SendPropertiesPacket(pos.asLong(), data);
		}
		return null;
	}
}