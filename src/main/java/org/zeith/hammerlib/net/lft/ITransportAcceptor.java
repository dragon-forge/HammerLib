package org.zeith.hammerlib.net.lft;

import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.util.java.Threading;

import java.io.InputStream;

public interface ITransportAcceptor
{
	/**
	 * Reads from the stream. The data accepted by transporter will not come all
	 * at once, so don't check available bytes or you can cause serious lags!
	 */
	void read(InputStream readable, int length);

	/**
	 * Sets the context of this session. Used to get side on wich the packet is
	 * being received, the sender and/or server.
	 */
	default void setInitialContext(PacketContext ctx)
	{
	}

	/**
	 * Called after the last packet passed remaining data to the {@link #read(InputStream, int)} method.
	 * Essentially used to handle stuff after it was accumulated.
	 */
	default void onTransmissionComplete(PacketContext ctx)
	{
	}

	/**
	 * @return Should this transport acceptor's {@link #onTransmissionComplete(PacketContext)} execute from main thread, or from network thread?
	 */
	default boolean executeOnMainThread()
	{
		return Threading.isMainThreaded(getClass());
	}
}