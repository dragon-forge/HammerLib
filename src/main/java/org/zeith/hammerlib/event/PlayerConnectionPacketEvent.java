package org.zeith.hammerlib.event;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.eventbus.api.GenericEvent;

public class PlayerConnectionPacketEvent<T extends Packet<?>>
		extends GenericEvent<T>
{
	final ServerGamePacketListenerImpl connection;
	final T packet;

	public PlayerConnectionPacketEvent(ServerGamePacketListenerImpl connection, T packet)
	{
		super((Class<T>) packet.getClass());
		this.connection = connection;
		this.packet = packet;
	}

	public ServerGamePacketListenerImpl getConnection()
	{
		return connection;
	}

	public T getPacket()
	{
		return packet;
	}

	public static class Handle<T extends Packet<?>>
			extends PlayerConnectionPacketEvent<T>
	{
		public Handle(ServerGamePacketListenerImpl connection, T packet)
		{
			super(connection, packet);
		}
	}

	public static class Send<T extends Packet<?>>
			extends PlayerConnectionPacketEvent<T>
	{
		public Send(ServerGamePacketListenerImpl connection, T packet)
		{
			super(connection, packet);
		}
	}
}