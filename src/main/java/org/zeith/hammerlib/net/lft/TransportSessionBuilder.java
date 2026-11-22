package org.zeith.hammerlib.net.lft;

import org.zeith.hammerlib.util.java.ByteHelper;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.UUID;

public class TransportSessionBuilder
{
	public final String sessionId = UUID.randomUUID().toString();

	public Class<? extends ITransportAcceptor> acceptor;
	private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	public TransportSessionBuilder addData(byte[] data)
	{
		if(data != null)
			buffer.writeBytes(data);

		return this;
	}

	public TransportSessionBuilder setAcceptor(Class<? extends ITransportAcceptor> acceptor)
	{
		this.acceptor = acceptor;
		return this;
	}

	public TransportSession build()
	{
		byte[] data = buffer.toByteArray();
		return new TransportSession(sessionId, acceptor, Arrays.asList(ByteHelper.divide(16384, data)), null, data.length);
	}
}