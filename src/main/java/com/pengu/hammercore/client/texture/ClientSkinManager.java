package com.pengu.hammercore.client.texture;

import java.lang.reflect.Field;
import java.util.Map;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;

public class ClientSkinManager
{
	public static final Field playerTextures = NetworkPlayerInfo.class.getDeclaredFields()[1];
	static
	{
		playerTextures.setAccessible(true);
	}
	
	/**
	 * Gets the texture map for certain player. It may be modified.
	 */
	public static Map<Type, ResourceLocation> getPlayerMap(AbstractClientPlayer acp)
	{
		NetworkPlayerInfo npi = Minecraft.getMinecraft().getConnection().getPlayerInfo(acp.getUniqueID());
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