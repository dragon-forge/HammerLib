package org.zeith.hammerlib.net.lft;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.SidedLocal;

import java.util.HashMap;
import java.util.Map;

public class NetTransport
{
	public static final SidedLocal<Map<String, TransportSession>> SESSIONS = new SidedLocal<>(s -> new HashMap<>());

	public static void indexSession(TransportSession session)
	{
		SESSIONS.get(session.createSide).put(session.id, session);
	}

	public static TransportSession getSession(LogicalSide side, String id)
	{
		return SESSIONS.get(side).get(id);
	}

	public static TransportSessionBuilder builder()
	{
		return new TransportSessionBuilder();
	}

	public static TransportSession wrap(IPacket packet)
	{
		final var buffer = new FriendlyByteBuf(Unpooled.buffer());
		Network.toPlain(packet).write(buffer);
		byte[] data = new byte[buffer.readableBytes()];
		buffer.getBytes(0, data);
		return builder().addData(data).setAcceptor(PacketWrapperAcceptor.class).build();
	}
}