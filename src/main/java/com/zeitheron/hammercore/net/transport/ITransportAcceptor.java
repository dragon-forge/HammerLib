package com.zeitheron.hammercore.net.transport;

import java.io.InputStream;

import com.zeitheron.hammercore.net.PacketContext;

import net.minecraftforge.fml.relauncher.Side;

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
}