package org.zeith.hammerlib.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

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

	public void write(FriendlyByteBuf buf)
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
		PacketContext pctx = new PacketContext(ctx);
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