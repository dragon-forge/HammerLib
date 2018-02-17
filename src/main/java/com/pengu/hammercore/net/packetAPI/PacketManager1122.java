package com.pengu.hammercore.net.packetAPI;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.pengu.hammercore.net.iPacketManager;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketManager1122 implements iPacketManager
{
	private static final Map<String, PacketManager1122> managers = new HashMap<String, PacketManager1122>();
	
	final Map<Class<? extends iPacket>, iPacketListener<?, ?>> registry = new HashMap<Class<? extends iPacket>, iPacketListener<?, ?>>();
	final Map<String, iPacketListener<?, ?>> stringClassRegistry = new HashMap<String, iPacketListener<?, ?>>();
	private final FMLEventChannel wrapper;
	final String channel;
	
	/**
	 * Creates a new {@link PacketManager1122} with passed string as a channel
	 * ID or name. <br>
	 * MUST be constructed under FML initialization event, if you want it to
	 * work properly.
	 * 
	 * @param channel
	 *            A channel that this manager is working on.
	 */
	public PacketManager1122(String channel)
	{
		if(getManagerByChannel(channel) != null)
			throw new RuntimeException("Duplicate channel ID for " + channel + " (" + this + ") and (" + getManagerByChannel(channel) + ")!!!");
		managers.put(channel, this);
		this.channel = channel;
		this.wrapper = NetworkRegistry.INSTANCE.newEventDrivenChannel("hammercore" + channel);
		this.wrapper.register(this);
	}
	
	/**
	 * Gets a channel for {@link PacketManager1122}.
	 * 
	 * @return A {@link String} representation of channel.
	 */
	public String getChannel()
	{
		return channel;
	}
	
	/**
	 * Returns packet manager for passed channel, or null.
	 * 
	 * @param channel
	 *            A channel to lookup with.
	 * @return A {@link PacketManager1122} or null, if not exists for passed
	 *         channel.
	 */
	public static PacketManager1122 getManagerByChannel(String channel)
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
	public <PKT extends iPacket, REPLY extends iPacket> void registerPacket(Class<PKT> packet, iPacketListener<PKT, REPLY> listener)
	{
		registry.put(packet, listener);
		stringClassRegistry.put(packet.getName(), listener);
	}
	
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
	public void sendToAll(iPacket packet)
	{
		wrapper.sendToAll(wrap(packet));
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
	public void sendTo(iPacket packet, EntityPlayerMP player)
	{
		wrapper.sendTo(wrap(packet), player);
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
	public void sendToAllAround(iPacket packet, TargetPoint point)
	{
		wrapper.sendToAllAround(wrap(packet), point);
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
	public void sendToDimension(iPacket packet, int dimensionId)
	{
		wrapper.sendToDimension(wrap(packet), dimensionId);
	}
	
	/**
	 * Send this packet to the server. The {@link iPacketListener} for this
	 * packet type should be on the SERVER side.
	 *
	 * @param packet
	 *            The packet to send
	 */
	public void sendToServer(iPacket packet)
	{
		wrapper.sendToServer(wrap(packet));
	}
	
	private FMLProxyPacket wrap(iPacket pkt)
	{
		PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
		ByteBufUtils.writeTag(buf, new PacketCustomNBT(pkt, channel).nbt);
		return new FMLProxyPacket(buf, channel);
	}
	
	private iPacket unwrap(FMLProxyPacket p, INetHandler h, Side s)
	{
		PacketBuffer payload = new PacketBuffer(p.payload());
		PacketCustomNBT n = new PacketCustomNBT();
		n.nbt = ByteBufUtils.readTag(payload);
		return n.handle(ctx(h, s));
	}
	
	@SubscribeEvent
	public void packetToClient(FMLNetworkEvent.ClientCustomPacketEvent e)
	{
		iPacket p = unwrap(e.getPacket(), e.getHandler(), Side.CLIENT);
		
		if(p != null)
			e.setReply(wrap(p));
	}
	
	@SubscribeEvent
	public void packetToServer(FMLNetworkEvent.ServerCustomPacketEvent e)
	{
		iPacket p = unwrap(e.getPacket(), e.getHandler(), Side.SERVER);
		
		if(p != null)
			e.setReply(wrap(p));
	}
	
	private static final MessageContext ctx(INetHandler h, Side s)
	{
		try
		{
			Constructor<MessageContext> ctr = MessageContext.class.getConstructor(INetHandler.class, Side.class);
			ctr.setAccessible(true);
			return ctr.newInstance(h, s);
		} catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}