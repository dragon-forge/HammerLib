package org.zeith.hammerlib.net.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.*;
import org.zeith.hammerlib.net.properties.IPropertyTile;
import org.zeith.hammerlib.util.java.Cast;

@MainThreaded
public class SendPropertiesPacket
		implements IPacket
{
	long pos;
	byte[] data;
	
	public SendPropertiesPacket(long pos, byte[] data)
	{
		this.pos = pos;
		this.data = data;
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeLong(pos);
		buf.writeShort(data.length);
		buf.writeBytes(data);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		pos = buf.readLong();
		data = new byte[buf.readShort()];
		buf.readBytes(data);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		ClientLevel cw = Minecraft.getInstance().level;
		if(cw != null)
		{
			IPropertyTile tile = Cast.cast(cw.getBlockEntity(BlockPos.of(pos)), IPropertyTile.class);
			if(tile != null)
			{
				FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
				tile.getProperties().decodeChanges(buf);
			}
		}
	}
}