package org.zeith.hammerlib.net.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.net.properties.IPropertyTile;
import org.zeith.hammerlib.util.java.Cast;

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
	public void write(PacketBuffer buf)
	{
		buf.writeLong(pos);
		buf.writeShort(data.length);
		buf.writeBytes(data);
	}

	@Override
	public void read(PacketBuffer buf)
	{
		pos = buf.readLong();
		data = new byte[buf.readShort()];
		buf.readBytes(data);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		ClientWorld cw = Minecraft.getInstance().level;
		if(cw != null)
		{
			IPropertyTile tile = Cast.cast(cw.getBlockEntity(BlockPos.of(pos)), IPropertyTile.class);
			if(tile != null)
			{
				PacketBuffer buf = new PacketBuffer(Unpooled.wrappedBuffer(data));
				tile.getProperties().decodeChanges(buf);
			}
		}
	}
}