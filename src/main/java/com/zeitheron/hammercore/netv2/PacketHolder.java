package com.zeitheron.hammercore.netv2;

import java.util.UUID;

import com.pengu.hammercore.HammerCore;
import com.zeitheron.hammercore.netv2.PacketContext.ContextSenderPlayerMP;
import com.zeitheron.hammercore.netv2.PacketContext.ContextSenderServer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketHolder
{
	public final IV2Packet packet;
	public final String playerName;
	public final UUID playerUUID;
	public final int playerId;
	
	public PacketHolder(NBTTagCompound nbt)
	{
		packet = HCV2Net.readPacket(nbt);
		playerName = nbt.getString("PN");
		playerUUID = nbt.getUniqueId("PU");
		playerId = nbt.getInteger("PI");
	}
	
	public PacketHolder(IV2Packet packet, EntityPlayer player)
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
	
	public PacketHolder(IV2Packet packet)
	{
		this.packet = packet;
		this.playerName = "Undefined";
		this.playerUUID = UUID.randomUUID();
		this.playerId = -1;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = HCV2Net.writePacket(packet, nbt);
		nbt.setString("PN", playerName);
		nbt.setUniqueId("PU", playerUUID);
		nbt.setInteger("PI", playerId);
		return nbt;
	}
	
	public boolean containsPlayer()
	{
		return playerId > 0;
	}
	
	@SideOnly(Side.CLIENT)
	public PacketHolder execute(ClientCustomPacketEvent handler)
	{
		EntityPlayer player = HammerCore.renderProxy.getClientPlayer();
		return new PacketHolder(packet.executeOnClient(new PacketContext(new ContextSenderServer(), this, null)), player);
	}
	
	public PacketHolder execute(ServerCustomPacketEvent handler)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		EntityPlayerMP mp = getPlayer(server);
		return new PacketHolder(packet.executeOnServer(new PacketContext(new ContextSenderPlayerMP(mp), this, server)));
	}

	public EntityPlayerMP getPlayer(MinecraftServer server)
	{
		return server != null && containsPlayer() ? server.getPlayerList().getPlayerByUUID(playerUUID) : null;
	}
}