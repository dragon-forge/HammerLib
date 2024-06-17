package org.zeith.hammerlib.net;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.concurrent.CompletableFuture;

public class PlainHLMessage
		implements CustomPacketPayload
{
	IPacket packet;
	RegistryAccess registry;
	
	public PlainHLMessage()
	{
	}
	
	public PlainHLMessage(IPacket packet)
	{
		this.packet = packet;
	}
	
	public PlainHLMessage(RegistryFriendlyByteBuf buf)
	{
		packet = PacketFactory.createEmpty(buf.readUtf(256));
		if(packet != null)
			packet.read(buf);
		this.registry = buf.registryAccess();
	}
	
	public void write(RegistryFriendlyByteBuf buf)
	{
		buf.writeUtf(PacketFactory.getPacketId(packet));
		if(packet != null)
			packet.write(buf);
	}
	
	public boolean isValid()
	{
		return packet != null;
	}
	
	public IPacket unwrap()
	{
		return packet;
	}
	
	public void handle(IPayloadContext ctx)
	{
		PacketContext pctx = new PacketContext(ctx, registry);
		if(packet != null)
		{
			CompletableFuture<Void> exec;
			if(packet.executeOnMainThread())
			{
				exec = ctx.enqueueWork(() -> packet.execute(pctx));
			} else
			{
				packet.execute(pctx);
				exec = CompletableFuture.completedFuture(null);
			}
			
			exec.thenRun(() ->
			{
				IPacket reply = pctx.getReply();
				if(reply != null)
					ctx.reply(new PlainHLMessage(reply));
			});
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return Network.MAIN_CHANNEL;
	}
}