package org.zeith.hammerlib.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.zeith.hammerlib.HammerLib;

import java.util.concurrent.CompletableFuture;

public class PlainHLMessage
		implements CustomPacketPayload
{
	IPacket packet;
	
	public PlainHLMessage()
	{
	}
	
	public PlainHLMessage(IPacket packet)
	{
		this.packet = packet;
	}
	
	public PlainHLMessage(FriendlyByteBuf buf)
	{
		packet = PacketFactory.createEmpty(buf.readUtf(256));
		if(packet != null)
			packet.read(buf);
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeUtf(PacketFactory.getPacketId(packet));
		if(packet != null)
			packet.write(buf);
	}
	
	@Override
	public ResourceLocation id()
	{
		return Network.MAIN_CHANNEL;
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
		PacketContext pctx = new PacketContext(ctx);
		if(packet != null)
		{
			CompletableFuture<Void> exec;
			if(packet.executeOnMainThread())
			{
				exec = ctx.workHandler().submitAsync(() ->
				{
					try
					{
						packet.execute(pctx);
					} catch(Throwable e)
					{
						HammerLib.LOG.error("Failed to handle packet {}", packet.getClass().getName(), e);
					}
				});
			} else
			{
				try
				{
					packet.execute(pctx);
				} catch(Throwable e)
				{
					HammerLib.LOG.error("Failed to handle packet {}", packet.getClass().getName(), e);
				}
				exec = CompletableFuture.completedFuture(null);
			}
			
			exec.thenRun(() ->
			{
				IPacket reply = pctx.getReply();
				if(reply != null)
					ctx.replyHandler().send(new PlainHLMessage(reply));
			});
		}
	}
}