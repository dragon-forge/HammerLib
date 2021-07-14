package org.zeith.hammerlib.event;

import net.minecraft.network.IPacket;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraftforge.eventbus.api.GenericEvent;

public class PlayerConnectionPacketEvent<T extends IPacket<?>>
		extends GenericEvent<T>
{
	final ServerPlayNetHandler connection;
	final T packet;

	public PlayerConnectionPacketEvent(ServerPlayNetHandler connection, T packet)
	{
		super((Class<T>) packet.getClass());
		this.connection = connection;
		this.packet = packet;
	}

	public ServerPlayNetHandler getConnection()
	{
		return connection;
	}

	public T getPacket()
	{
		return packet;
	}

	public static class Handle<T extends IPacket<?>>
			extends PlayerConnectionPacketEvent<T>
	{
		public Handle(ServerPlayNetHandler connection, T packet)
		{
			super(connection, packet);
		}
	}

	public static class Send<T extends IPacket<?>>
			extends PlayerConnectionPacketEvent<T>
	{
		public Send(ServerPlayNetHandler connection, T packet)
		{
			super(connection, packet);
		}
	}
}