package com.zeitheron.hammercore.net.internal.opts;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.proxy.RenderProxy_Client;

public class PacketReqOpts implements IPacket
{
	static
	{
		IPacket.handle(PacketReqOpts.class, PacketReqOpts::new);
	}
	
	@Override
	public void executeOnClient2(PacketContext net)
	{
		RenderProxy_Client.needsClConfigSync = true;
	}
}