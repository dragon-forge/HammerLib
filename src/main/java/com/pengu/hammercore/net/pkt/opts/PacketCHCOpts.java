package com.pengu.hammercore.net.pkt.opts;

import java.util.UUID;

import com.pengu.hammercore.ServerHCClientPlayerData;
import com.pengu.hammercore.client.HCClientOptions;
import com.pengu.hammercore.json.JSONObject;
import com.pengu.hammercore.json.JSONTokener;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketCHCOpts implements iPacket, iPacketListener<PacketCHCOpts, PacketCHCOpts>
{
	public HCClientOptions opts;
	public String player;
	
	public PacketCHCOpts setOpts(HCClientOptions opts)
	{
		this.opts = opts;
		return this;
	}
	
	public PacketCHCOpts setPlayer(EntityPlayer player)
	{
		this.player = player.getGameProfile().getId().toString();
		return this;
	}
	
	public PacketCHCOpts setLPlayer(String player)
	{
		this.player = player;
		return this;
	}
	
	@Override
	public PacketCHCOpts onArrived(PacketCHCOpts packet, MessageContext context)
	{
		ServerHCClientPlayerData dat = ServerHCClientPlayerData.DATAS.get(context.side);
		if(packet.opts != null)
		{
			dat.assign(packet.player, packet.opts);
			if(dat.side == Side.SERVER)
				HCNetwork.manager.sendToAll(new PacketCHCOpts().setOpts(packet.opts).setLPlayer(packet.player));
		} else if(packet.player != null)
		{
			EntityPlayerMP mp = context.getServerHandler().player.getServer().getPlayerList().getPlayerByUUID(UUID.fromString(packet.player));
			if(mp != null)
				return new PacketCHCOpts().setLPlayer(packet.player).setOpts(dat.getOptionsForPlayer(mp));
		}
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Options", opts != null ? opts.serialize() : "-");
		if(player != null)
			nbt.setString("Player", player);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		String os = nbt.getString("Options");
		if(!os.equals("-"))
		{
			opts = new HCClientOptions();
			try
			{
				opts.load((JSONObject) new JSONTokener(os).nextValue());
			} catch(Throwable err)
			{
			}
		}
		if(nbt.hasKey("Player"))
			player = nbt.getString("Player");
	}
}