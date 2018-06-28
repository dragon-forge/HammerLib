package com.zeitheron.hammercore.net.internal.opts;

import java.util.UUID;

import com.zeitheron.hammercore.ServerHCClientPlayerData;
import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.lib.zlib.json.JSONObject;
import com.zeitheron.hammercore.lib.zlib.json.JSONTokener;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class PacketCHCOpts implements IPacket
{
	static
	{
		IPacket.handle(PacketCHCOpts.class, () -> new PacketCHCOpts());
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
	public IPacket execute(Side side, PacketContext net)
	{
		ServerHCClientPlayerData dat = ServerHCClientPlayerData.DATAS.get(side);
		if(opts != null)
		{
			dat.assign(player, opts);
			if(dat.side == Side.SERVER)
				HCNet.INSTANCE.sendToAll(new PacketCHCOpts().setOpts(opts).setLPlayer(player));
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