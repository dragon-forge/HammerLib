package org.zeith.hammerlib.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.zeith.hammerlib.client.texture.HttpTextureDownloader;
import org.zeith.hammerlib.util.java.Hashers;

import java.util.*;

public class FXUtils
{
	private static Map<String, ResourceLocation> textures = new HashMap<>();
	
	public static void bindTextureURL(String url)
	{
		String withoutHTTP = url.substring(url.indexOf("://") + 3);
		String protocol = url.substring(0, url.indexOf("://"));
		ResourceLocation loca = new ResourceLocation("hammercore", protocol + "/" + Hashers.SHA1.hashify(withoutHTTP));
		HttpTextureDownloader.create(loca, url).bind();
	}
	
	public static void bindTexture(String f)
	{
		if(f.startsWith("http"))
			bindTextureURL(f);
		else
		{
			if(textures.containsKey(f))
			{
				bindTexture(textures.get(f));
				return;
			}
			
			ResourceLocation value = new ResourceLocation(f);
			textures.put(f, value);
			bindTexture(value);
		}
	}
	
	public static void bindTexture(ResourceLocation loca)
	{
		Minecraft.getInstance().getTextureManager().bind(loca);
	}
	
	public static void bindTexture(String dom, String tex)
	{
		if(textures.containsKey(dom + ":" + tex))
		{
			Minecraft.getInstance().getTextureManager().bind(textures.get(dom + ":" + tex));
			return;
		}
		ResourceLocation value = new ResourceLocation(dom, tex);
		textures.put(dom + ":" + tex, value);
		Minecraft.getInstance().getTextureManager().bind(value);
	}
}