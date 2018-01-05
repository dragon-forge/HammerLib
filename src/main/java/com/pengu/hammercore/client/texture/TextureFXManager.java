package com.pengu.hammercore.client.texture;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.TextureStitchEvent.Post;
import net.minecraftforge.client.event.TextureStitchEvent.Pre;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public enum TextureFXManager
{
	INSTANCE;
	private Map<String, TextureAtlasSprite> iconMap = Maps.newHashMap();
	
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void registerIcon(String path, TextureAtlasSprite sprite)
	{
		if(!this.iconMap.containsKey(path))
			this.iconMap.put(path, sprite);
	}
	
	/** upload and reload all custom textures */
	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent event)
	{
		for(TextureAtlasSprite s : iconMap.values())
			if(s instanceof TextureSpriteCustom)
				((TextureSpriteCustom) s).resetFX(event instanceof Pre ? 0 : event instanceof Post ? 1 : 2);
		event.getMap().mapUploadedSprites.putAll(iconMap);
		
		// try
		// {
		// Render3D.loadedPlayers.clear();
		//
		// JSONObject obj = (JSONObject) new JSONTokener(new
		// String(IOUtils.downloadData("https://raw.githubusercontent.com/APengu/HammerCore/1.11.x/hd_skins.json"))).nextValue();
		// for(String key : obj.keySet())
		// {
		// HammerCore.LOG.info("HD Skin for " + key + ": Loading...");
		// ResourceLocation path = new ResourceLocation("skins/" + key);
		// Minecraft.getMinecraft().getTextureManager().mapTextureObjects.put(path,
		// new URLImageTexture(path, obj.getString(key)));
		// HammerCore.LOG.info("HD Skin for " + key + ": Imported");
		// }
		// }
		// catch(Throwable err) {}
	}
}