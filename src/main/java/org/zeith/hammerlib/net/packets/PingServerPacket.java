package org.zeith.hammerlib.net.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class PingServerPacket
		implements IPacket
{
	public static long lastPingTime = 0;
	long time, lastPing;

	public PingServerPacket()
	{
	}

	public PingServerPacket(long time)
	{
		this.time = time;
		this.lastPing = lastPingTime;
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeLong(time);
		buf.writeLong(lastPing);
	}

	@Override
	public void read(FriendlyByteBuf buf)
	{
		this.time = buf.readLong();
		this.lastPing = buf.readLong();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		lastPingTime = (System.currentTimeMillis() - time) / 2L;
	}

	@Override
	public void serverExecute(PacketContext ctx)
	{
		ctx.withReply(this);
	}
}