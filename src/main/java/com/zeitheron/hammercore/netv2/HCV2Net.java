package com.zeitheron.hammercore.netv2;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum HCV2Net
{
	INSTANCE;
	
	public final String ch_name = "hammercore2";
	final FMLEventChannel channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(ch_name);
	
	public void init()
	{
		channel.register(this);
	}
	
	private final Map<Class<? extends IV2Packet>, Supplier<? extends IV2Packet>> reconstruction = new HashMap<>();
	
	public <T extends IV2Packet> void handle(Class<T> t, Supplier<T> nev)
	{
		reconstruction.put(t, nev);
	}
	
	public <T extends IV2Packet> T newPacket(Class<T> t)
	{
		Supplier sup = reconstruction.get(t);
		if(sup != null)
			return (T) sup.get();
		try
		{
			Constructor<T> c = t.getDeclaredConstructor();
			c.setAccessible(true);
			return c.newInstance();
		} catch(Throwable err)
		{
		}
		return null;
	}
	
	public void sendToAll(IV2Packet packet)
	{
		channel.sendToAll(wrap(new PacketHolder(packet), null));
	}
	
	public void sendTo(IV2Packet packet, EntityPlayerMP player)
	{
		channel.sendTo(wrap(new PacketHolder(packet), null), player);
	}
	
	public void sendToAllAround(IV2Packet packet, TargetPoint point)
	{
		channel.sendToAllAround(wrap(new PacketHolder(packet), null), point);
	}
	
	public void sendToDimension(IV2Packet packet, int dimensionId)
	{
		channel.sendToDimension(wrap(new PacketHolder(packet), null), dimensionId);
	}
	
	@SideOnly(Side.CLIENT)
	public void sendToServer(IV2Packet packet)
	{
		EntityPlayer ep = Minecraft.getMinecraft().player;
		if(ep != null)
			channel.sendToServer(wrap(new PacketHolder(packet, ep), null));
	}
	
	public static NBTTagCompound writePacket(IV2Packet packet, NBTTagCompound nbt)
	{
		if(packet == null)
			return nbt;
		NBTTagCompound data;
		packet.writeToNBT(data = new NBTTagCompound());
		nbt.setTag("Data", data);
		nbt.setString("Class", packet.getClass().getCanonicalName());
		return nbt;
	}
	
	public static IV2Packet readPacket(NBTTagCompound nbt)
	{
		try
		{
			Class<? extends IV2Packet> pktc = Class.forName(nbt.getString("Class")).asSubclass(IV2Packet.class);
			IV2Packet pkt = INSTANCE.newPacket(pktc);
			pkt.readFromNBT(nbt.getCompoundTag("Data"));
			return pkt;
		} catch(Throwable err)
		{
		}
		return null;
	}
	
	private FMLProxyPacket wrap(PacketHolder pkt, Side target)
	{
		return wrap(pkt, target, null);
	}
	
	private FMLProxyPacket wrap(PacketHolder pkt, Side target, @Nullable FMLProxyPacket origin)
	{
		PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
		buf.writeCompoundTag(pkt.writeToNBT(new NBTTagCompound()));
		FMLProxyPacket fmlpp = new FMLProxyPacket(buf, ch_name);
		if(origin != null)
			fmlpp.setDispatcher(origin.getDispatcher());
		if(target != null)
			fmlpp.setTarget(target);
		return fmlpp;
	}
	
	private PacketHolder unwrap(FMLProxyPacket pkt)
	{
		PacketBuffer payload = new PacketBuffer(pkt.payload());
		NBTTagCompound nbt = null;
		try
		{
			nbt = payload.readCompoundTag();
		} catch(Throwable err)
		{
		}
		payload.release();
		return nbt != null ? new PacketHolder(nbt) : null;
	}
	
	@SubscribeEvent
	public void packetToClient(ClientCustomPacketEvent e)
	{
		FMLProxyPacket fmlpp = e.getPacket();
		PacketHolder pkt = unwrap(fmlpp);
		pkt = pkt.execute(e);
		if(pkt.packet != null)
			e.setReply(wrap(pkt, oppositeSide(e.side()), fmlpp));
	}
	
	@SubscribeEvent
	public void packetToServer(ServerCustomPacketEvent e)
	{
		FMLProxyPacket fmlpp = e.getPacket();
		PacketHolder pkt = unwrap(fmlpp);
		pkt = pkt.execute(e);
		if(pkt.packet != null)
			e.setReply(wrap(pkt, oppositeSide(e.side()), fmlpp));
	}
	
	private Side oppositeSide(Side s)
	{
		return s == Side.CLIENT ? Side.SERVER : Side.CLIENT;
	}
}