package com.zeitheron.hammercore.net.transport;

import java.util.Arrays;
import java.util.UUID;

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
		return new TransportSession(sessionId, acceptor, Arrays.asList(ByteHelper.divide(16 * 1024, data)), null, data.length);
	}
}