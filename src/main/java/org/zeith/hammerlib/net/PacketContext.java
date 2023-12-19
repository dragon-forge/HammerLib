package org.zeith.hammerlib.net;

import lombok.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.util.LogicalSidedProvider;
import net.neoforged.neoforge.network.NetworkEvent;

@Getter
public class PacketContext
{
	private final ServerPlayer sender;
	private final LogicalSide side;
	private @Setter IPacket reply;
	
	public PacketContext(NetworkEvent.Context ctx)
	{
		this.side = ctx.getDirection().getReceptionSide();
		this.sender = ctx.getSender();
	}
	
	public boolean hasSender()
	{
		return sender != null;
	}
	
	public void withReply(IPacket reply)
	{
		setReply(reply);
	}
	
	public Level getLevel()
	{
		if(sender != null) return sender.level();
		return LogicalSidedProvider.CLIENTWORLD.get(side).orElse(null);
	}
}