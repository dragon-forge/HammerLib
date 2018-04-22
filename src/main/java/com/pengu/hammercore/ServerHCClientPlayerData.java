package com.pengu.hammercore;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.pengu.hammercore.client.HCClientOptions;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.pkt.opts.PacketCHCOpts;

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
	
	public Map<String, HCClientOptions> playerMap = new HashMap<>();
	
	public void assign(String player, HCClientOptions opts)
	{
		playerMap.put(player, opts);
	}
	
	private HCClientOptions opts(String player)
	{
		HCClientOptions hc = playerMap.get(player);
		if(hc == null)
			playerMap.put(player, hc = new HCClientOptions());
		if(hc.def && side == Side.CLIENT)
		{
			hc.def = false;
			HCNetwork.manager.sendToServer(new PacketCHCOpts().setLPlayer(player));
		}
		return hc;
	}
	
	public HCClientOptions getOptionsForPlayer(EntityPlayer player)
	{
		return opts(player.getGameProfile().getId().toString());
	}
}