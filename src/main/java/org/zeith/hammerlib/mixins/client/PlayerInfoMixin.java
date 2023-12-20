package org.zeith.hammerlib.mixins.client;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.zeith.hammerlib.api.client.IEmissivePlayerInfo;
import org.zeith.hammerlib.client.render.entity.player.EmissiveSkinHelper;
import org.zeith.hammerlib.client.texture.HttpTextureDownloader;
import org.zeith.hammerlib.client.utils.FXUtils;

import java.util.*;

@Mixin(PlayerInfo.class)
@Implements({
		@Interface(iface = IEmissivePlayerInfo.class, prefix = "IEPI$")
})
public abstract class PlayerInfoMixin
{
	private boolean pendingEmissiveTextures;
	
	@Shadow
	@Final
	private GameProfile profile;
	private final Map<MinecraftProfileTexture.Type, ResourceLocation> emissiveTextureLocations = Maps.newEnumMap(MinecraftProfileTexture.Type.class);
	
	private void registerEmissiveTextures()
	{
		if(pendingEmissiveTextures) return;
		synchronized(this)
		{
			if(!pendingEmissiveTextures)
			{
				pendingEmissiveTextures = true;
				
				Util.backgroundExecutor().execute(() ->
				{
					// For testing purposes: use my own emissive texture.
//					var gp = EmissiveSkinHelper.adaptGameProfileToEmissiveTextures(new GameProfile(UUID.fromString("63fa4c48-5b56-4080-9727-d5d59663e603"), "Zeitheron"));
					var gp = EmissiveSkinHelper.adaptGameProfileToEmissiveTextures(profile);
					
					var gpid = gp.getId().toString();
					
					Map<MinecraftProfileTexture.Type, String> texturesToDownload = new HashMap<>();
					
					for(MinecraftProfileTexture.Type type : EmissiveSkinHelper.findEmissiveTextureTypes(gpid))
					{
						var url = EmissiveSkinHelper.getEmissiveRequestURL(gpid, type);
						texturesToDownload.put(type, url);
					}
					
					Minecraft.getInstance().execute(() -> RenderSystem.recordRenderCall(() ->
					{
						for(var entry : texturesToDownload.entrySet())
						{
							var texture = FXUtils.urlToTexturePath(entry.getValue());
							final var type = entry.getKey();
							
							HttpTextureDownloader.create(texture, entry.getValue(), () ->
							{
								synchronized(emissiveTextureLocations)
								{
									emissiveTextureLocations.put(type, texture);
								}
							});
						}
					}));
				});
			}
		}
	}
	
	@Nullable
	public ResourceLocation IEPI$getEmissiveSkinLocation()
	{
		this.registerEmissiveTextures();
		return this.emissiveTextureLocations.get(MinecraftProfileTexture.Type.SKIN);
	}
	
	@Nullable
	public ResourceLocation IEPI$getEmissiveCapeLocation()
	{
		this.registerEmissiveTextures();
		return this.emissiveTextureLocations.get(MinecraftProfileTexture.Type.CAPE);
	}
	
	@Nullable
	public ResourceLocation IEPI$getEmissiveElytraLocation()
	{
		this.registerEmissiveTextures();
		return this.emissiveTextureLocations.get(MinecraftProfileTexture.Type.ELYTRA);
	}
}