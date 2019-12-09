package com.zeitheron.hammercore.client.utils.texture;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ClientSkinManager
{
	private static final Map<String, String> playerSTs = new HashMap<>();
	
	/**
	 * Gets the texture map for certain player. It may be modified.
	 *
	 * @param acp The player
	 * @return The skin map
	 */
	public static Map<Type, ResourceLocation> getPlayerMap(AbstractClientPlayer acp)
	{
		NetworkPlayerInfo npi = Minecraft.getMinecraft().getConnection().getPlayerInfo(acp.getUniqueID());
		String uuids = acp.getUniqueID().toString();
		if(!playerSTs.containsKey(uuids))
			playerSTs.put(uuids, npi.skinType);
		return npi.playerTextures;
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