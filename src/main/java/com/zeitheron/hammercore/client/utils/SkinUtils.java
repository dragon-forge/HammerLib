package com.zeitheron.hammercore.client.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.zeitheron.hammercore.utils.PlayerUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;

public class SkinUtils
{
	private static final Map<UUID, GameProfile> uuidProfiles = new HashMap<>();
	private static final Map<String, GameProfile> nameProfiles = new HashMap<>();
	
	public static GameProfile bakeProfileByUUID(UUID uid)
	{
		if(uuidProfiles.get(uid) != null)
			return uuidProfiles.get(uid);
		GameProfile profile = TileEntitySkull.updateGameProfile(new GameProfile(uid, PlayerUtil.getUsernameFromUUID(uid)));
		uuidProfiles.put(uid, profile);
		return profile;
	}
	
	public static GameProfile bakeProfileByUsername(String username)
	{
		if(nameProfiles.get(username) != null)
			return nameProfiles.get(username);
		GameProfile profile = TileEntitySkull.updateGameProfile(new GameProfile(PlayerUtil.getUUIDFromUsername(username), username));
		nameProfiles.put(username, profile);
		return profile;
	}
	
	public static ResourceLocation getSkinTexture(GameProfile profile)
	{
		profile = TileEntitySkull.updateGameProfile(profile);
		ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
		if(profile != null)
		{
			Minecraft minecraft = Minecraft.getMinecraft();
			Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(profile);
			
			if(map.containsKey(Type.SKIN))
			{
				resourcelocation = minecraft.getSkinManager().loadSkin(map.get(Type.SKIN), Type.SKIN);
			} else
			{
				UUID uuid = EntityPlayer.getUUID(profile);
				resourcelocation = DefaultPlayerSkin.getDefaultSkin(uuid);
			}
		}
		return resourcelocation;
	}
}