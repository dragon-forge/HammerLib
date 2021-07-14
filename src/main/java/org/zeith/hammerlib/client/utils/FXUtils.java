package org.zeith.hammerlib.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

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