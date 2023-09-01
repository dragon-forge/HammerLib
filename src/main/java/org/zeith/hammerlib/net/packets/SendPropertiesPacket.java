package org.zeith.hammerlib.net.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.net.*;
import org.zeith.hammerlib.net.properties.*;
import org.zeith.hammerlib.util.java.Cast;

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
	public void write(FriendlyByteBuf buf)
	{
		buf.writeNbt(IObjectSource.writeSource(source));
		buf.writeShort(data.length);
		buf.writeBytes(data);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		source = IObjectSource.readSource(buf.readNbt()).orElse(null);
		data = new byte[buf.readShort()];
		buf.readBytes(data);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		ClientLevel cw = Minecraft.getInstance().level;
		if(cw != null && source != null)
		{
			IBasePropertyHolder tile = source.get(IBasePropertyHolder.class, cw).orElse(null);
			if(tile != null)
			{
				FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
				tile.getProperties().decodeChanges(buf);
			}
		}
	}
}