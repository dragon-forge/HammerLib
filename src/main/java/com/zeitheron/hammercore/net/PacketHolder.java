package com.zeitheron.hammercore.net;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.net.PacketContext.ContextSenderPlayerMP;
import com.zeitheron.hammercore.net.PacketContext.ContextSenderServer;
import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class PacketHolder
{
	public final IPacket packet;
	public final String playerName;
	public final UUID playerUUID;
	public final int playerId;

	public PacketHolder(NBTTagCompound nbt)
	{
		packet = HCNet.readPacket(nbt);
		playerName = nbt.getString("PN");
		playerUUID = nbt.getUniqueId("PU");
		playerId = nbt.getInteger("PI");
	}

	public PacketHolder(IPacket packet, EntityPlayer player)
	{
		this.packet = packet;
		if(player != null)
		{
			this.playerName = player.getGameProfile().getName();
			this.playerUUID = player.getGameProfile().getId();
			this.playerId = player.getEntityId();
		} else
		{
			this.playerName = "Undefined";
			this.playerUUID = UUID.randomUUID();
			this.playerId = -1;
		}
	}

	public PacketHolder(IPacket packet)
	{
		this.packet = packet;
		this.playerName = "Undefined";
		this.playerUUID = UUID.randomUUID();
		this.playerId = -1;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = HCNet.writePacket(packet, nbt);
		nbt.setString("PN", playerName);
		nbt.setUniqueId("PU", playerUUID);
		nbt.setInteger("PI", playerId);
		return nbt;
	}

	public boolean containsPlayer()
	{
		return playerId > 0;
	}

	public boolean enforceMainThread()
	{
		return packet != null && packet.executeOnMainThread();
	}

	@SideOnly(Side.CLIENT)
	public PacketHolder execute(ClientCustomPacketEvent handler)
	{
		EntityPlayer player = HammerCore.renderProxy.getClientPlayer();
		PacketContext ctx = new PacketContext(new ContextSenderServer(player), this, null);
		packet.executeOnClient2(ctx);
		return new PacketHolder(ctx.getReply(), player);
	}

	public PacketHolder execute(ServerCustomPacketEvent handler)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		NetHandlerPlayServer net = Cast.cast(handler.getHandler(), NetHandlerPlayServer.class);
		EntityPlayerMP mp = net != null ? net.player : getPlayer(server);
		PacketContext ctx = new PacketContext(new ContextSenderPlayerMP(mp), this, server);
		packet.executeOnServer2(ctx);
		return new PacketHolder(ctx.getReply());
	}

	public EntityPlayerMP getPlayer(MinecraftServer server)
	{
		return server != null && containsPlayer() ? server.getPlayerList().getPlayerByUUID(playerUUID) : null;
	}
}