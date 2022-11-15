package org.zeith.hammerlib.client.render.entity.player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import org.zeith.hammerlib.util.java.net.HttpRequest;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;

import java.util.*;
import java.util.stream.*;

public final class EmissiveSkinHelper
{
	private static final String USERNAME_TO_ID_CHECK = "https://skins.zeith.org/usernames/%s.json";
	private static final String UUID_TO_EMISSIVE = "https://skins.zeith.org/emissive/%s/%s/%s";
	
	private EmissiveSkinHelper()
	{
	}
	
	public static GameProfile adaptGameProfileToEmissiveTextures(GameProfile profile)
	{
		if(profile.getId() != null)
		{
			try(var req = HttpRequest.head(getEmissiveRequestURL(profile.getId().toString(), "textures.json")))
			{
				if(req.isResponse2xx())
				{
					return profile;
				}
			}
		}
		
		var fburl = USERNAME_TO_ID_CHECK.formatted(profile.getName());
		
		try(var req = HttpRequest.get(fburl))
		{
			if(req.isResponse2xx())
			{
				return new GameProfile(new JSONTokener(req.body())
						.nextValueOBJ()
						.map(j -> j.getString("uuid"))
						.map(UUID::fromString)
						.orElse(profile.getId()), profile.getName());
			}
		}
		
		return profile;
	}
	
	public static EnumSet<MinecraftProfileTexture.Type> findEmissiveTextureTypes(String uuid)
	{
		try(var req = HttpRequest.get(getEmissiveRequestURL(uuid, "textures.json")))
		{
			if(req.isResponse2xx())
			{
				var textures = new JSONTokener(req.body())
						.nextValueARR()
						.stream()
						.flatMap(arr -> StreamSupport.stream(arr.spliterator(), false))
						.map(Objects::toString)
						.collect(Collectors.toSet());
				
				return EnumSet.copyOf(
						Stream.of(MinecraftProfileTexture.Type.values())
								.filter(type -> textures.contains(type.name().toLowerCase(Locale.ROOT)))
								.toList()
				);
			}
		}
		
		return EnumSet.noneOf(MinecraftProfileTexture.Type.class);
	}
	
	public static String getEmissiveRequestURL(String uuid, MinecraftProfileTexture.Type textureType)
	{
		return getEmissiveRequestURL(uuid, textureType.name().toLowerCase(Locale.ROOT) + ".png");
	}
	
	private static String getEmissiveRequestURL(String uuid, String rawType)
	{
		return UUID_TO_EMISSIVE.formatted(
				uuid.substring(0, 2), // first 2 hexadecimal digits of the UUID
				uuid.replace("-", ""), // entire UUID, without dashes
				rawType // texture type to load, either "skin", "cape", or "elytra".
		);
	}
}