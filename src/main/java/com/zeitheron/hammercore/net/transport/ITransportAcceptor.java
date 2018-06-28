package com.zeitheron.hammercore.net.transport;

import java.io.InputStream;

public interface ITransportAcceptor
{
	/**
	 * Reads from the stream. The data accepted by transporter will not come all
	 * at once, so don't check available bytes or you can cause serious lags!
	 */
	void read(InputStream readable, int length);
}