package org.zeith.hammerlib.net.packets;

import com.zeitheron.hammercore.net.*;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.net.properties.IBasePropertyHolder;

import java.io.IOException;

@MainThreaded
public class SendPropertiesPacket
		implements IPacket
{
	private static final Logger LOG = LogManager.getLogger();
	
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
		buf.writeCompoundTag(IObjectSource.writeSource(source));
		buf.writeShort(data.length);
		buf.writeBytes(data);
	}
	
	@Override
	public void read(PacketBuffer buf)
			throws IOException
	{
		source = IObjectSource.readSource(buf.readCompoundTag()).orElse(null);
		data = new byte[buf.readShort()];
		buf.readBytes(data);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void executeOnClient2(PacketContext net)
	{
		WorldClient cw = Minecraft.getMinecraft().world;
		if(cw != null && source != null)
		{
			IBasePropertyHolder tile = source.get(IBasePropertyHolder.class, cw).orElse(null);
			if(tile != null)
			{
				PacketBuffer buf = new PacketBuffer(Unpooled.wrappedBuffer(data));
				try
				{
					tile.getProperties().decodeChanges(buf);
				} catch(IOException e)
				{
					LOG.error("Failed to decode changes inside {}", tile, e);
				}
			}
		}
	}
}