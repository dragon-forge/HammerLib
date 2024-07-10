package org.zeith.hammerlib.mixins.client;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
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
	@Unique
	private boolean hl$pendingEmissiveTextures;
	
	@Shadow
	@Final
	private GameProfile profile;
	
	@Unique
	private final Map<MinecraftProfileTexture.Type, ResourceLocation> hl$emissiveTextureLocations = Maps.newEnumMap(MinecraftProfileTexture.Type.class);
	
	@Unique
	private void hl$registerEmissiveTextures()
	{
		synchronized(this)
		{
			if(!hl$pendingEmissiveTextures)
			{
				hl$pendingEmissiveTextures = true;
				
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
								synchronized(hl$emissiveTextureLocations)
								{
									hl$emissiveTextureLocations.put(type, texture);
								}
							});
						}
					}));
				});
			}
		}
	}
	
	@NotNull
	public ResourceLocation IEPI$getEmissiveSkinLocation()
	{
		this.hl$registerEmissiveTextures();
		return MoreObjects.firstNonNull(this.hl$emissiveTextureLocations.get(MinecraftProfileTexture.Type.SKIN), FXUtils.EMPTY_TEXTURE);
	}
	
	@Nullable
	public ResourceLocation IEPI$getEmissiveCapeLocation()
	{
		this.hl$registerEmissiveTextures();
		return this.hl$emissiveTextureLocations.get(MinecraftProfileTexture.Type.CAPE);
	}
	
	@Nullable
	public ResourceLocation IEPI$getEmissiveElytraLocation()
	{
		this.hl$registerEmissiveTextures();
		return this.hl$emissiveTextureLocations.get(MinecraftProfileTexture.Type.ELYTRA);
	}
}