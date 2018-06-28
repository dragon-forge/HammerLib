package com.zeitheron.hammercore.net.internal.opts;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.proxy.RenderProxy_Client;

public class PacketReqOpts implements IPacket
{
	static
	{
		IPacket.handle(PacketReqOpts.class, () -> new PacketReqOpts());
	}
	
	@Override
	public IPacket executeOnClient(PacketContext net)
	{
		RenderProxy_Client.needsClConfigSync = true;
		return null;
	}
	
	@Override
	public IPacket executeOnServer(PacketContext net)
	{
		return null;
	}
}