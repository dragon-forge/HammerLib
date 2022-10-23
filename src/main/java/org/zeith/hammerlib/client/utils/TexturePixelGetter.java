package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.data.ModelData;
import org.zeith.hammerlib.util.colors.ColorHelper;

import java.util.*;

public class TexturePixelGetter
{
	private static final Map<ResourceLocation, int[]> colors = new HashMap<>();
	
	public static void reloadTexture(TextureStitchEvent e)
	{
		colors.clear();
	}
	
	public static int[] getAllColors(ItemStack stack)
	{
		var mc = Minecraft.getInstance();
		BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, mc.level, mc.player, 0);
		TextureAtlasSprite spr = model.getParticleIcon(ModelData.EMPTY);
		if(spr == null) spr = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation());
		return getAllColors(getResourceLocation(spr.getName()));
	}
	
	private static ResourceLocation getResourceLocation(ResourceLocation txPath)
	{
		return new ResourceLocation(txPath.getNamespace(), String.format(Locale.ROOT, "textures/%s%s", txPath.getPath(), ".png"));
	}
	
	private static final ResourceLocation MISSINGNO = getResourceLocation(MissingTextureAtlasSprite.getLocation());
	
	public static int[] getAllColors(ResourceLocation texture)
	{
		if(colors.containsKey(texture))
			return colors.get(texture);
		
		if(MISSINGNO.equals(texture))
			colors.put(texture, new int[] {
					0xFF00FF,
					0x000000
			});
		else
			try
			{
				var ints = new IntOpenHashSet();
				
				var res = Minecraft.getInstance()
						.getResourceManager()
						.getResource(texture)
						.orElse(null);
				
				if(res == null)
				{
					colors.put(texture, new int[] {
							0xFF00FF,
							0x000000
					});
					
					return colors.get(texture);
				}
				
				try(var in = res.open())
				{
					var img = NativeImage.read(in);
					
					for(int x = 0; x < img.getWidth(); ++x)
						for(int y = 0; y < img.getHeight(); ++y)
						{
							int rgba = img.getPixelRGBA(x, y);
							
							int a = NativeImage.getA(rgba);
							
							if(a <= 0F)
								continue;
							
							int rgb = ColorHelper.packARGBi(
									NativeImage.getR(rgba),
									NativeImage.getG(rgba),
									NativeImage.getB(rgba),
									a
							);
							
							ints.add(rgb);
						}
				}
				
				colors.put(texture, ints.toIntArray());
			} catch(Exception e)
			{
				e.printStackTrace();
				colors.put(texture, new int[] {
						0xFF00FF,
						0x000000
				});
			}
		
		return colors.get(texture);
	}
}