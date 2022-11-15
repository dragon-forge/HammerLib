package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.client.texture.HttpTextureDownloader;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Hashers;

import java.util.HashMap;
import java.util.Map;

public class FXUtils
{
	public static final ResourceLocation EMPTY_TEXTURE = new ResourceLocation(HLConstants.MOD_ID, "textures/empty.png");
	private static final Map<String, ResourceLocation> textures = new HashMap<>();
	
	public static ResourceLocation urlToTexturePath(String url)
	{
		String withoutHTTP = url.substring(url.indexOf("://") + 3);
		String protocol = url.substring(0, url.indexOf("://"));
		return new ResourceLocation(HLConstants.MOD_ID, protocol + "/" + Hashers.SHA1.hashify(withoutHTTP));
	}
	
	public static AbstractTexture downloadTexture(ResourceLocation texture, String url)
	{
		return HttpTextureDownloader.create(texture, url);
	}
	
	public static void bindTextureURL(String url)
	{
		downloadTexture(urlToTexturePath(url), url).bind();
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
	
	public static void bindTexture(ResourceLocation path)
	{
		bindTextureCurShader(path);
		setPositionTexShader();
	}
	
	public static void bindTextureCurShader(ResourceLocation path)
	{
		RenderSystem.setShaderTexture(0, path);
	}
	
	public static void setPositionTexShader()
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
	}
	
	public static void setPositionTexColorShader()
	{
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
	}
	
	public static void setColor(float red, float green, float blue, float alpha)
	{
		RenderSystem.setShaderColor(red, green, blue, alpha);
	}
	
	public static void bindTexture(String namespace, String path)
	{
		String f = namespace + ':' + path;
		
		if(textures.containsKey(f))
		{
			bindTexture(textures.get(f));
			return;
		}
		
		ResourceLocation value = new ResourceLocation(namespace, path);
		textures.put(f, value);
		bindTexture(value);
	}
}