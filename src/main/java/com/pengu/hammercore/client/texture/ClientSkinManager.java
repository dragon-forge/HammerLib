package com.pengu.hammercore.client.texture;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.pengu.hammercore.ServerHCClientPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;

public class ClientSkinManager
{
	private static final Map<String, String> playerSTs = new HashMap<>();
	
	public static final Field playerTextures = NetworkPlayerInfo.class.getDeclaredFields()[1];
	public static final Field skinType = NetworkPlayerInfo.class.getDeclaredFields()[5];
	static
	{
		playerTextures.setAccessible(true);
		skinType.setAccessible(true);
	}
	
	/**
	 * Gets the texture map for certain player. It may be modified.
	 */
	public static Map<Type, ResourceLocation> getPlayerMap(AbstractClientPlayer acp)
	{
		NetworkPlayerInfo npi = Minecraft.getMinecraft().getConnection().getPlayerInfo(acp.getUniqueID());
		
		String uuids = acp.getUniqueID().toString();
		if(!playerSTs.containsKey(uuids))
			try
			{
				playerSTs.put(uuids, "" + skinType.get(npi));
			} catch(Throwable e)
			{
			}
		
		int skinTypei = ServerHCClientPlayerData.getOptionsFor(acp).skinType;
		
		try
		{
			// Force the skin type
			skinType.set(npi, skinTypei == 0 ? playerSTs.get(uuids) : skinTypei % 2 == 1 ? "default" : "slim");
		} catch(Throwable e)
		{
		}
		
		try
		{
			return (Map<Type, ResourceLocation>) playerTextures.get(npi);
		} catch(Throwable err)
		{
		}
		return null;
	}
	
	public static boolean bindTexture(AbstractClientPlayer acp, Type type, ResourceLocation location)
	{
		Map<Type, ResourceLocation> mp = getPlayerMap(acp);
		if(mp != null)
		{
			mp.put(type, location);
			return true;
		}
		return false;
	}
}