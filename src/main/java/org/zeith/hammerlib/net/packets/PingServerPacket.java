package org.zeith.hammerlib.net.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.api.level.IBlockEntityLevel;
import org.zeith.hammerlib.net.*;

public class PingServerPacket
		implements IPacket
{
	public static long lastPingTime = 0;
	public static int lastLoadedBEs = 0;
	long time, lastPing;
	int loadedBEs;
	
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
		buf.writeVarInt(loadedBEs);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		this.time = buf.readLong();
		this.lastPing = buf.readLong();
		this.loadedBEs = buf.readVarInt();
	}
	
	@Override
	public void clientExecute(PacketContext ctx)
	{
		lastPingTime = (System.currentTimeMillis() - time) / 2L;
		lastLoadedBEs = loadedBEs;
	}
	
	@Override
	public void serverExecute(PacketContext ctx)
	{
		ServerPlayer sp = ctx.getSender();
		if(sp != null && sp.level() instanceof IBlockEntityLevel be && sp.hasPermissions(3))
			loadedBEs = be.getLoadedBlockEntities_HammerLib().size();
		else
			loadedBEs = -1;
		ctx.withReply(this);
	}
}