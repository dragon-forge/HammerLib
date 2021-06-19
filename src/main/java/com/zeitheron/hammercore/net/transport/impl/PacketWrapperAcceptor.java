package com.zeitheron.hammercore.net.transport.impl;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.net.transport.ITransportAcceptor;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PacketWrapperAcceptor
		implements ITransportAcceptor
{
	NBTTagCompound data;
	IPacket decoded;

	@Override
	public void read(InputStream readable, int length)
	{
		try
		{
			decoded = HCNet.readPacket(data = CompressedStreamTools.read(new DataInputStream(readable), new NBTSizeTracker(2097152L)));
		} catch(IOException ioexception)
		{
			throw new EncoderException(ioexception);
		}
	}

	@Override
	public void onTransmissionComplete(Side side, PacketContext ctx)
	{
		if(decoded == null)
			HammerCore.LOG.error("Received bad packet on packet transport (WHAT IS THIS?!): " + data);
		else switch(side)
		{
			case CLIENT:
				decoded.executeOnClient2(ctx);
				break;
			case SERVER:
				decoded.executeOnServer2(ctx);
				break;
			default:
				HammerCore.LOG.error("WTF is this side " + side + " ?!");
				break;
		}
	}

	@Override
	public boolean executeOnMainThread()
	{
		return ITransportAcceptor.super.executeOnMainThread() || (decoded != null && decoded.executeOnMainThread());
	}
}