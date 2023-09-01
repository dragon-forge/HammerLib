package org.zeith.hammerlib.net.packets;

import io.netty.buffer.Unpooled;
import lombok.var;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.*;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.net.*;
import org.zeith.hammerlib.net.properties.IBasePropertyHolder;

@MainThreaded
public class SendPropertiesPacket
		implements IPacket
{
	IObjectSource<?> source;
	byte[] data;
	
	public SendPropertiesPacket(IObjectSource<?> source, byte[] data)
	{
		this.source = source;
		this.data = data;
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeNbt(IObjectSource.writeSource(source));
		buf.writeShort(data.length);
		buf.writeBytes(data);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		source = IObjectSource.readSource(buf.readNbt()).orElse(null);
		data = new byte[buf.readShort()];
		buf.readBytes(data);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		var cw = Minecraft.getInstance().level;
		if(cw != null && source != null)
		{
			IBasePropertyHolder tile = source.get(IBasePropertyHolder.class, cw).orElse(null);
			if(tile != null)
			{
				PacketBuffer buf = new PacketBuffer(Unpooled.wrappedBuffer(data));
				tile.getProperties().decodeChanges(buf);
			}
		}
	}
}