package com.zeitheron.hammercore.net.transport;

import com.zeitheron.hammercore.lib.zlib.utils.Threading;
import com.zeitheron.hammercore.net.PacketContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.InputStream;

public interface ITransportAcceptor
{
	/**
	 * Reads from the stream. The data accepted by transporter will not come all
	 * at once, so don't check available bytes or you can cause serious lags!
	 */
	void read(InputStream readable, int length);
	
	/**
	 * Sets the context of this session. Used to get side on wich the packet is being received, the sender and/or server.
	 */
	default void setInitialContext(Side side, PacketContext ctx)
	{
	}
	
	/**
	 * Called after the last packet passed remaining data to the {@link #read(InputStream, int)} method.
	 * Essentially used to handle stuff after it was accumulated.
	 */
	default void onTransmissionComplete(Side side, PacketContext ctx)
	{
	}
	
	/**
	 * @return Should this transport acceptor's {@link #onTransmissionComplete(Side, PacketContext)} execute from main thread, or from network thread?
	 */
	default boolean executeOnMainThread()
	{
		return Threading.isMainThreaded(getClass());
	}
	
	default long getReadWaitTimeout()
	{
		return 1000L;
	}
}