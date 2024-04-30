package org.zeith.hammerlib.net;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.util.LogicalSidedProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.zeith.hammerlib.util.java.Cast;

@Getter
public class PacketContext
{
	private @Getter
	final IPayloadContext neo;
	private final ServerPlayer sender;
	private final LogicalSide side;
	private @Setter IPacket reply;
	
	public PacketContext(IPayloadContext ctx)
	{
		this.neo = ctx;
		this.side = ctx.flow().getReceptionSide();
		this.sender = Cast.cast(ctx.player(), ServerPlayer.class);
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