package org.zeith.hammerlib.net.lft;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.EncoderException;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.net.PlainHLMessage;

import java.io.IOException;
import java.io.InputStream;

public class PacketWrapperAcceptor
		implements ITransportAcceptor
{
	PlainHLMessage decoded;

	byte[] data;

	@Override
	public void read(InputStream readable, int length)
	{
		try
		{
			ByteBuf buf = Unpooled.buffer(length);
			buf.writeBytes(readable, length);
			data = new byte[length];
			buf.readBytes(data);
			buf.readerIndex(0);
			decoded = new PlainHLMessage(new FriendlyByteBuf(buf));
		} catch(IOException ioexception)
		{
			throw new EncoderException(ioexception);
		}
	}

	@Override
	public void onTransmissionComplete(PacketContext ctx)
	{
		if(!decoded.isValid())
			HammerLib.LOG.error("Received bad packet on packet transport (WHAT IS THIS?!): " + new String(data));
		else switch(ctx.getSide())
		{
			case CLIENT -> decoded.unwrap().clientExecute(ctx);
			case SERVER -> decoded.unwrap().serverExecute(ctx);
			default -> HammerLib.LOG.error("WTF is this side " + ctx.getSide() + " ?!");
		}
		data = null;

		IPacket pkt = ctx.getReply();
		if(pkt != null) ctx.withReply(NetTransport.wrap(pkt).createPacket());
	}

	@Override
	public boolean executeOnMainThread()
	{
		return ITransportAcceptor.super.executeOnMainThread() || (decoded.isValid() && decoded.unwrap().executeOnMainThread());
	}
}