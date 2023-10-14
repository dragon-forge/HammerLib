package com.zeitheron.hammercore.net.transport;

import com.zeitheron.hammercore.net.*;
import com.zeitheron.hammercore.net.transport.impl.PacketWrapperAcceptor;
import com.zeitheron.hammercore.utils.base.SideLocal;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

public class NetTransport
{
	public static final SideLocal<Map<String, TransportSession>> SESSIONS = SideLocal.initializeForBoth(HashMap::new);

	public static void indexSession(TransportSession session)
	{
		SESSIONS.get(session.createSide).put(session.id, session);
	}

	public static TransportSession getSession(Side side, String id)
	{
		return SESSIONS.get(side).get(id);
	}

	public static TransportSessionBuilder builder()
	{
		return new TransportSessionBuilder();
	}

	public static TransportSession wrap(IPacket packet)
	{
		PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
		HCNet.writePacket(packet, buf);
		return builder().addData(buf.array()).setAcceptor(PacketWrapperAcceptor.class).build();
	}
}