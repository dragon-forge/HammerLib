package com.zeitheron.hammercore.netv2.internal.opts;

import java.util.UUID;

import com.pengu.hammercore.ServerHCClientPlayerData;
import com.pengu.hammercore.client.HCClientOptions;
import com.pengu.hammercore.json.JSONObject;
import com.pengu.hammercore.json.JSONTokener;
import com.zeitheron.hammercore.netv2.HCV2Net;
import com.zeitheron.hammercore.netv2.IV2Packet;
import com.zeitheron.hammercore.netv2.PacketContext;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class PacketCHCOpts implements IV2Packet
{
	static
	{
		IV2Packet.handle(PacketCHCOpts.class, () -> new PacketCHCOpts());
	}
	
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
	public IV2Packet execute(Side side, PacketContext net)
	{
		ServerHCClientPlayerData dat = ServerHCClientPlayerData.DATAS.get(side);
		if(opts != null)
		{
			dat.assign(player, opts);
			if(dat.side == Side.SERVER)
				HCV2Net.INSTANCE.sendToAll(new PacketCHCOpts().setOpts(opts).setLPlayer(player));
		} else if(player != null && net.server != null)
		{
			EntityPlayerMP mp = net.server.getPlayerList().getPlayerByUUID(UUID.fromString(player));
			if(mp != null)
				return new PacketCHCOpts().setLPlayer(player).setOpts(dat.getOptionsForPlayer(mp));
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