package com.pengu.hammercore.net.packetAPI;

import java.util.HashMap;
import java.util.Map;

import com.pengu.hammercore.net.iPacketManager;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Deprecated
public class PacketManager implements iPacketManager
{
	private static final Map<String, PacketManager> managers = new HashMap<String, PacketManager>();
	
	final Map<Class<? extends iPacket>, iPacketListener<?, ?>> registry = new HashMap<Class<? extends iPacket>, iPacketListener<?, ?>>();
	final Map<String, iPacketListener<?, ?>> stringClassRegistry = new HashMap<String, iPacketListener<?, ?>>();
	private final SimpleNetworkWrapper wrapper;
	final String channel;
	
	/**
	 * Creates a new {@link PacketManager} with passed string as a channel ID or
	 * name. <br>
	 * MUST be constructed under FML initialization event, if you want it to
	 * work properly.
	 * 
	 * @param channel
	 *            A channel that this manager is working on.
	 */
	public PacketManager(String channel)
	{
		if(getManagerByChannel(channel) != null)
			throw new RuntimeException("Duplicate channel ID for " + channel + " (" + this + ") and (" + getManagerByChannel(channel) + ")!!!");
		managers.put(channel, this);
		this.channel = channel;
		this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("hammercore" + channel);
		this.wrapper.registerMessage(PacketCustomNBT.class, PacketCustomNBT.class, 1, Side.CLIENT);
		this.wrapper.registerMessage(PacketCustomNBT.class, PacketCustomNBT.class, 1, Side.SERVER);
	}
	
	/**
	 * Gets a channel for {@link PacketManager}.
	 * 
	 * @return A {@link String} representation of channel.
	 */
	@Override
	public String getChannel()
	{
		return channel;
	}
	
	/**
	 * Returns packet manager for passed channel, or null.
	 * 
	 * @param channel
	 *            A channel to lookup with.
	 * @return A {@link PacketManager} or null, if not exists for passed
	 *         channel.
	 */
	public static PacketManager getManagerByChannel(String channel)
	{
		return managers.get(channel);
	}
	
	/**
	 * Register a Packet listener for specified IPacket class.
	 * 
	 * @param packet
	 *            The packet class to add listener for
	 * @param listener
	 *            The listener instance that will listen for packet events
	 */
	@Override
	public <PKT extends iPacket, REPLY extends iPacket> void registerPacket(Class<PKT> packet, iPacketListener<PKT, REPLY> listener)
	{
		registry.put(packet, listener);
		stringClassRegistry.put(packet.getName(), listener);
	}
	
	@Override
	public <PKT extends iPacket> iPacketListener<PKT, ?> getListener(Class<PKT> packet)
	{
		return (iPacketListener<PKT, ?>) stringClassRegistry.get(packet.getName());
	}
	
	/**
	 * Send this packet to everyone. The {@link iPacketListener} for this packet
	 * type should be on the CLIENT side.
	 *
	 * @param packet
	 *            The packet to send
	 */
	@Override
	public void sendToAll(iPacket packet)
	{
		wrapper.sendToAll(new PacketCustomNBT(packet, channel));
	}
	
	/**
	 * Send this packet to the specified player. The {@link iPacketListener} for
	 * this packet type should be on the CLIENT side.
	 *
	 * @param packet
	 *            The packet to send
	 * @param player
	 *            The player to send it to
	 */
	@Override
	public void sendTo(iPacket packet, EntityPlayerMP player)
	{
		wrapper.sendTo(new PacketCustomNBT(packet, channel), player);
	}
	
	/**
	 * Send this packet to everyone within a certain range of a point. The
	 * {@link iPacketListener} for this packet type should be on the CLIENT
	 * side.
	 *
	 * @param packet
	 *            The packet to send
	 * @param point
	 *            The {@link TargetPoint} around which to send
	 */
	@Override
	public void sendToAllAround(iPacket packet, TargetPoint point)
	{
		wrapper.sendToAllAround(new PacketCustomNBT(packet, channel), point);
	}
	
	/**
	 * Send this packet to everyone within the supplied dimension. The
	 * {@link iPacketListener} for this packet type should be on the CLIENT
	 * side.
	 *
	 * @param packet
	 *            The packet to send
	 * @param dimensionId
	 *            The dimension id to target
	 */
	@Override
	public void sendToDimension(iPacket packet, int dimensionId)
	{
		wrapper.sendToDimension(new PacketCustomNBT(packet, channel), dimensionId);
	}
	
	/**
	 * Send this packet to the server. The {@link iPacketListener} for this
	 * packet type should be on the SERVER side.
	 *
	 * @param packet
	 *            The packet to send
	 */
	@Override
	public void sendToServer(iPacket packet)
	{
		wrapper.sendToServer(new PacketCustomNBT(packet, channel));
	}
}