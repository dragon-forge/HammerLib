package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.client.HammerCoreClient;
import com.zeitheron.hammercore.net.*;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class PacketPing
		implements IPacket
{
	static
	{
		IPacket.handle(PacketPing.class, () -> new PacketPing(0L));
	}
	
	public PacketPing(long start)
	{
		create = start;
	}
	
	public long create;
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeLong(create);
	}
	
	@Override
	public void read(PacketBuffer buf)
			throws IOException
	{
		create = buf.readLong();
	}
	
	@Override
	public void executeOnClient2(PacketContext ctx)
	{
		HammerCoreClient.ping = System.currentTimeMillis() - create;
	}
	
	@Override
	public void executeOnServer2(PacketContext ctx)
	{
		ctx.withReply(this);
	}
}