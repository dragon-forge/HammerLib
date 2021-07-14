package org.zeith.hammerlib.net;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketContext
{
	private final ServerPlayerEntity sender;
	private final LogicalSide side;
	private IPacket reply;

	public PacketContext(Context ctx)
	{
		this.side = ctx.getDirection().getReceptionSide();
		this.sender = ctx.getSender();
	}

	public boolean hasSender()
	{
		return sender != null;
	}

	public ServerPlayerEntity getSender()
	{
		return sender;
	}

	public LogicalSide getSide()
	{
		return side;
	}

	public void withReply(IPacket reply)
	{
		this.reply = reply;
	}

	public IPacket getReply()
	{
		return reply;
	}
}