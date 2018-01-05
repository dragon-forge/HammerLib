package com.pengu.hammercore.net.packetAPI;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface iPacketListener<REQ extends iPacket, REPLY extends iPacket>
{
	public REPLY onArrived(REQ packet, MessageContext context);
}