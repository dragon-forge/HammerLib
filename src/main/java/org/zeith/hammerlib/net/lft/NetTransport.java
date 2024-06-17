package org.zeith.hammerlib.net.lft;

import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.connection.ConnectionType;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.SidedLocal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetTransport
{
	public static final SidedLocal<Map<String, TransportSession>> SESSIONS = SidedLocal.initializeForBoth(ConcurrentHashMap::new);
	
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
	
	@Deprecated(forRemoval = true)
	public static TransportSession wrap(IPacket packet)
	{
		return wrap(packet, RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
	}
	
	public static TransportSession wrap(IPacket packet, RegistryAccess regs)
	{
		final var buffer = new RegistryFriendlyByteBuf(new FriendlyByteBuf(Unpooled.buffer()), regs, ConnectionType.NEOFORGE);
		Network.toPlain(packet).write(buffer);
		byte[] data = new byte[buffer.readableBytes()];
		buffer.getBytes(0, data);
		return builder().addData(data).setAcceptor(PacketWrapperAcceptor.class).build();
	}
}