package com.zeitheron.hammercore.net.transport;

import java.util.Arrays;
import java.util.UUID;

import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.ArrayUtils;

import com.zeitheron.hammercore.lib.zlib.utils.ByteHelper;

public class TransportSessionBuilder
{
	public final String sessionId = UUID.randomUUID().toString();
	
	public Class<? extends ITransportAcceptor> acceptor;
	public byte[] data = new byte[0];
	
	public TransportSessionBuilder addData(byte[] data)
	{
		this.data = ArrayUtils.addAll(this.data, data);
		return this;
	}
	
	public TransportSessionBuilder setAcceptor(Class<? extends ITransportAcceptor> acceptor)
	{
		this.acceptor = acceptor;
		return this;
	}
	
	public TransportSession build()
	{
		// These limits are taken from IPacket's suggested limits
		// Server -> Client has a bigger payload limit, so we should use that to make things run faster!
		int maxPacketSize = FMLCommonHandler.instance().getEffectiveSide().isServer()
							? 267_364_000
							: 30_000;
		
		return new TransportSession(sessionId, acceptor, Arrays.asList(ByteHelper.divide(maxPacketSize, data)), null, data.length);
	}
}