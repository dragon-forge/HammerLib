package com.pengu.hammercore.net.packetAPI;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.net.iPacketManager;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * The experimental packet manager. Apparently should be better in a way? (from
 * what I know this may be because old packet system didn't cleanup payloads
 * thus leaving large byte[] in memory)
 */
public class PacketManagerExperimental implements iPacketManager
{
	private static final Map<String, PacketManagerExperimental> managers = new HashMap<String, PacketManagerExperimental>();
	
	final Map<Class<? extends iPacket>, iPacketListener<?, ?>> registry = new HashMap<Class<? extends iPacket>, iPacketListener<?, ?>>();
	final Map<String, iPacketListener<?, ?>> stringClassRegistry = new HashMap<String, iPacketListener<?, ?>>();
	private final FMLEventChannel wrapper;
	final String channel;
	
	/**
	 * Creates a new {@link PacketManagerExperimental} with passed string as a
	 * channel ID or name. <br>
	 * MUST be constructed under FML initialization event, if you want it to
	 * work properly.
	 * 
	 * @param channel
	 *            A channel that this manager is working on.
	 */
	public PacketManagerExperimental(String channel)
	{
		if(getManagerByChannel(channel) != null)
			throw new RuntimeException("Duplicate channel ID for " + channel + " (" + this + ") and (" + getManagerByChannel(channel) + ")!!!");
		
		managers.put(channel, this);
		
		this.channel = channel;
		
		this.wrapper = NetworkRegistry.INSTANCE.newEventDrivenChannel("hammercore" + channel);
		this.wrapper.register(this);
	}
	
	/**
	 * Gets a channel for {@link PacketManagerExperimental}.
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
	 * @return A {@link PacketManagerExperimental} or null, if not exists for
	 *         passed channel.
	 */
	public static PacketManagerExperimental getManagerByChannel(String channel)
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
		wrapper.sendToAll(wrap(packet, null));
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
		wrapper.sendTo(wrap(packet, null), player);
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
		wrapper.sendToAllAround(wrap(packet, null), point);
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
		wrapper.sendToDimension(wrap(packet, null), dimensionId);
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
		wrapper.sendToServer(wrap(packet, null));
	}
	
	private FMLProxyPacket wrap(iPacket pkt, @Nullable FMLProxyPacket origin)
	{
		PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
		buf.writeCompoundTag(new PacketCustomNBT(pkt, channel).nbt);
		FMLProxyPacket fmlpp = new FMLProxyPacket(buf, "hammercore" + channel);
		/**
		 * Fixes #56. This may not be the best way to do this, but hey! It
		 * seems to work, so I'll leave it as-is.
		 */
		if(origin != null)
		{
			fmlpp.setDispatcher(origin.getDispatcher());
			fmlpp.setTarget(origin.getTarget() == Side.CLIENT ? Side.SERVER : Side.CLIENT);
		}
		return fmlpp;
	}
	
	/**
	 * This method reads the FMLProxyPacket, executes whatever should be
	 * executed, releases the payload and gets the response back to caller.
	 */
	private iPacket unwrap(FMLProxyPacket p, INetHandler h, Side s)
	{
		PacketBuffer payload = new PacketBuffer(p.payload());
		PacketCustomNBT n = new PacketCustomNBT();
		try
		{
			n.nbt = payload.readCompoundTag();
		} catch(IOException e)
		{
			// Corrupted NBT tag? BAD stuff...
			payload.release();
			return null;
		}
		payload.release();
		return n.handle(ctx(h, s));
	}
	
	@SubscribeEvent
	public void packetToClient(ClientCustomPacketEvent e)
	{
		iPacket p = unwrap(e.getPacket(), e.getHandler(), Side.CLIENT);
		if(p != null)
			e.setReply(wrap(p, e.getPacket()));
	}
	
	@SubscribeEvent
	public void packetToServer(ServerCustomPacketEvent e)
	{
		iPacket p = unwrap(e.getPacket(), e.getHandler(), Side.SERVER);
		if(p != null)
			e.setReply(wrap(p, e.getPacket()));
	}
	
	/**
	 * Creates a forge MessageContext using reflections. I'm going to move to my
	 * own contexts if forge wouldn't make the constructor public in 1.13 (or
	 * sooner?)
	 */
	@Nullable
	private static final MessageContext ctx(INetHandler h, Side s)
	{
		if(h == null)
			return null;
		try
		{
			Constructor<MessageContext> ctr = MessageContext.class.getDeclaredConstructor(INetHandler.class, Side.class);
			ctr.setAccessible(true);
			return ctr.newInstance(h, s);
		} catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			HammerCore.LOG.error("We can't construct a new message context for handler " + h.getClass() + " " + e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}
}