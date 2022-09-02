package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FXUtils
{
	private static Map<String, ResourceLocation> textures = new HashMap<>();

	/*
	public static boolean bindTextureURL(String url)
	{
		String withoutHTTP = url.substring(url.indexOf("://") + 3);
		String protocol = url.substring(0, url.indexOf("://"));
		ResourceLocation loca = new ResourceLocation("hammercore", protocol + "/" + Hashers.SHA1.encrypt(withoutHTTP));
		if(!TexLocUploader.cleanup.contains(loca))
		{
			final String lpa = loca.toString();
			new Thread(() ->
			{
				try(InputStream input = IOUtils.getInput(url).get1())
				{
					BufferedImage bufferedimage = ImageIO.read(input);
					Minecraft.getInstance().deferTask(() -> TexLocUploader.upload(loca, bufferedimage));
				} catch(IOException ioe)
				{
					HammerLib.LOG.error("Failed to load texture from url \"" + url + "\"", ioe);
				}
			}).start();
			TexLocUploader.cleanupAfterLogoff(loca, () -> textures.remove(lpa));
			textures.put(lpa, loca);
		}
		boolean b = Minecraft.getInstance().getTextureManager().getTexture(loca) != null;
		if(b) bindTexture(loca);
		return b;
	}

	public static void bindTexture(String tex)
	{
		if(tex.startsWith("http"))
			bindTextureURL(tex);
		else
			bindTexture("hammercore", tex);
	}
	 */
	
	public static void bindTexture(ResourceLocation path)
	{
		bindTexture(path, GameRenderer::getPositionTexShader);
	}
	
	public static void bindTexture(String namespace, String path)
	{
		bindTexture(namespace, path, GameRenderer::getPositionTexShader);
	}
	
	public static void bindTexture(ResourceLocation path, Supplier<ShaderInstance> shader)
	{
		RenderSystem.setShaderTexture(0, path);
		RenderSystem.setShader(shader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public static void bindTexture(String namespace, String path, Supplier<ShaderInstance> shader)
	{
		String f = namespace + ':' + path;
		
		if(textures.containsKey(f))
		{
			bindTexture(textures.get(f), shader);
			return;
		}
		
		ResourceLocation value = new ResourceLocation(namespace, path);
		textures.put(f, value);
		bindTexture(value, shader);
	}
}