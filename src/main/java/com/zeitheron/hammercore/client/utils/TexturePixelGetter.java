package com.zeitheron.hammercore.client.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.zeitheron.hammercore.utils.color.ColorHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TexturePixelGetter
{
	private static final Map<String, int[]> colors = new HashMap<>();
	
	@SubscribeEvent
	public void reloadTexture(TextureStitchEvent e)
	{
		colors.clear();
	}
	
	public static int[] getAllColors(ItemStack stack)
	{
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
		TextureAtlasSprite spr = model.getParticleTexture();
		if(spr == null)
			spr = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
		ResourceLocation resourcelocation = new ResourceLocation(spr.getIconName());
		return getAllColors(resourcelocation.getResourceDomain() + ":" + String.format("%s/%s%s", "textures", resourcelocation.getResourcePath(), ".png"));
	}
	
	public static int[] getAllColors(String texture)
	{
		if(colors.containsKey(texture))
			return colors.get(texture);
		
		if(texture.equals("minecraft:textures/missingno.png") || texture.equals("textures/missingno.png"))
			colors.put(texture, new int[] { 0xFF00FF, 0x000000 });
		else
			try
			{
				Set<Integer> ints = new HashSet<>();
				BufferedImage img = TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(texture)).getInputStream());
				
				for(int x = 0; x < img.getWidth(); ++x)
					for(int y = 0; y < img.getHeight(); ++y)
					{
						int rgb = img.getRGB(x, y);
						
						if(ColorHelper.getAlpha(rgb) <= 0F)
							continue;
						
						ints.add(rgb);
					}
				
				int[] buf = new int[ints.size()];
				
				int pointer = 0;
				for(Integer i : ints)
				{
					buf[pointer] = i.intValue();
					++pointer;
				}
				
				colors.put(texture, buf);
			} catch(IOException e)
			{
				e.printStackTrace();
				colors.put(texture, new int[0]);
			}
		
		return colors.get(texture);
	}
}