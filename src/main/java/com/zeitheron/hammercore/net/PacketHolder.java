package com.zeitheron.hammercore.net;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.client.HammerCoreClient;
import com.zeitheron.hammercore.net.PacketContext.*;
import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.*;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nullable;

public class PacketHolder
{
	public final IPacket packet;
	
	public PacketHolder(IPacket packet)
	{
		this.packet = packet;
	}
	
	public boolean executeOnMainThread()
	{
		return packet != null && packet.executeOnMainThread();
	}
	
	@Nullable
	@SideOnly(Side.CLIENT)
	public PacketHolder execute(ClientCustomPacketEvent handler)
	{
		EntityPlayer player = HammerCore.renderProxy.getClientPlayer();
		PacketContext ctx = new PacketContext(new ContextSenderServer(player), this, null);
		packet.executeOnClient2(ctx);
		return new PacketHolder(ctx.getReply());
	}
	
	@Nullable
	public PacketHolder execute(ServerCustomPacketEvent handler)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		
		if(packet == null)
		{
			HammerCore.LOG.warn("Failed to process packet that failed to decode.");
			return null;
		}
		
		NetHandlerPlayServer net = Cast.cast(handler.getHandler(), NetHandlerPlayServer.class);
		if(net == null)
		{
			HammerCore.LOG.warn("Failed to process packet " + packet + " on server side due to missing player in the connection.");
			return null;
		}
		
		EntityPlayerMP mp = net.player;
		PacketContext ctx = new PacketContext(new ContextSenderPlayerMP(mp), this, server);
		packet.executeOnServer2(ctx);
		return new PacketHolder(ctx.getReply());
	}
}