package com.zeitheron.hammercore.netv2.internal.opts;

import com.pengu.hammercore.proxy.RenderProxy_Client;
import com.zeitheron.hammercore.netv2.IV2Packet;
import com.zeitheron.hammercore.netv2.PacketContext;

public class PacketReqOpts implements IV2Packet
{
	static
	{
		IV2Packet.handle(PacketReqOpts.class, () -> new PacketReqOpts());
	}
	
	@Override
	public IV2Packet executeOnClient(PacketContext net)
	{
		RenderProxy_Client.needsClConfigSync = true;
		return null;
	}
	
	@Override
	public IV2Packet executeOnServer(PacketContext net)
	{
		return null;
	}
}