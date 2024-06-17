package org.zeith.hammerlib.net;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.util.LogicalSidedProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.zeith.hammerlib.util.java.Cast;

@Getter
public class PacketContext
{
	private final @Getter IPayloadContext neo;
	private final ServerPlayer sender;
	private final LogicalSide side;
	private @Setter IPacket reply;
	private final RegistryAccess registryAccess;
	
	public PacketContext(IPayloadContext ctx, RegistryAccess registryAccess)
	{
		this.neo = ctx;
		this.side = ctx.flow().getReceptionSide();
		this.sender = Cast.cast(ctx.player(), ServerPlayer.class);
		this.registryAccess = registryAccess;
	}
	
	public RegistryAccess registryAccess()
	{
		if(registryAccess == null)
		{
			var l = getLevel();
			if(l != null) return l.registryAccess();
		}
		return registryAccess;
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
	
	private RegistryAccess getRegistryAccess()
	{
		return registryAccess;
	}
}