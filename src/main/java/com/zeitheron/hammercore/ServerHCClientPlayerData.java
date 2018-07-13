package com.zeitheron.hammercore;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.opts.PacketCHCOpts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public class ServerHCClientPlayerData
{
	public static final EnumMap<Side, ServerHCClientPlayerData> DATAS = new EnumMap<>(Side.class);
	static
	{
		DATAS.put(Side.SERVER, new ServerHCClientPlayerData(Side.SERVER));
		DATAS.put(Side.CLIENT, new ServerHCClientPlayerData(Side.CLIENT));
	}
	
	public final Side side;
	
	private ServerHCClientPlayerData(Side s)
	{
		this.side = s;
	}
	
	/**
	 * Automatically selects option map and gets client's settings 
	 */
	public static HCClientOptions getOptionsFor(EntityPlayer player)
	{
		return DATAS.get(player.world.isRemote ? Side.CLIENT : Side.SERVER).getOptionsForPlayer(player);
	}
	
	public Map<String, HCClientOptions> playerMap = new HashMap<>();
	
	public void assign(String player, HCClientOptions opts)
	{
		playerMap.put(player, opts);
	}
	
	public HCClientOptions opts(String player)
	{
		HCClientOptions hc = playerMap.get(player);
		if(hc == null)
			playerMap.put(player, hc = new HCClientOptions());
		if(hc.def && side == Side.CLIENT)
		{
			hc.def = false;
			HCNet.INSTANCE.sendToServer(new PacketCHCOpts().setLPlayer(player));
		}
		return hc;
	}
	
	public HCClientOptions getOptionsForPlayer(EntityPlayer player)
	{
		return opts(player.getGameProfile().getId().toString());
	}
}