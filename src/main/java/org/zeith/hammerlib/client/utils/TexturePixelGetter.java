package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.zeith.hammerlib.client.render.item.Stack2ImageRenderer;
import org.zeith.hammerlib.util.colors.ColorHelper;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class TexturePixelGetter
{
	private static final Map<ResourceLocation, int[]> colors = new HashMap<>();
	private static final Map<String, int[]> cachedRenderedColorsRaw = new HashMap<>();
	private static final Map<String, CompletableFuture<int[]>> cachedRenderedColors = new HashMap<>();
	private static final Map<String, Long> cachedRenderedColorsCompletionTimes = new ConcurrentHashMap<>();
	
	public static void reloadTexture(TextureAtlasStitchedEvent e)
	{
		cachedRenderedColorsCompletionTimes.clear();
		cachedRenderedColors.clear();
		colors.clear();
	}
	
	private static CompletableFuture<int[]> getRenderedColors(ItemStack stack)
	{
		return cachedRenderedColors.computeIfAbsent(stack.toString() + "_" + stack.getTag(), ignore ->
		{
			AtomicReference<NativeImage> colors = new AtomicReference<>();
			AtomicBoolean complete = new AtomicBoolean(false);
			
			Stack2ImageRenderer.renderItemStack(null, 64, 64, stack, image ->
			{
				synchronized(colors)
				{
					colors.set(image);
					complete.set(true);
					colors.notifyAll();
				}
			});
			
			return CompletableFuture.supplyAsync(() ->
			{
				synchronized(colors)
				{
					try
					{
						if(!complete.get())
							colors.wait(1000L);
					} catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
				return decompose(colors.get(), 0);
			}).thenApply(i ->
			{
				cachedRenderedColorsCompletionTimes.put(ignore, System.currentTimeMillis());
				return i;
			});
		});
	}
	
	public static int[] getAllColors(ItemStack stack)
	{
		var key = stack.toString() + "_" + stack.getTag();
		
		var rendered = getRenderedColors(stack);
		if(rendered.isDone() && !rendered.isCompletedExceptionally())
		{
			int[] colors = rendered.join();
			cachedRenderedColorsRaw.put(key, colors);
			cachedRenderedColors.remove(key);
		}
		
		if(cachedRenderedColorsRaw.containsKey(key))
			return cachedRenderedColorsRaw.get(key);
		
		var mc = Minecraft.getInstance();
		BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, mc.level, mc.player, 0);
		TextureAtlasSprite spr = model.getParticleIcon(ModelData.EMPTY);
		if(spr == null) spr = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation());
		return getAllColors(getResourceLocation(spr.contents().name()));
	}
	
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
					colors.put(texture, decompose(img, 0));
				}
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
	
	private static ResourceLocation getResourceLocation(ResourceLocation txPath)
	{
		return new ResourceLocation(txPath.getNamespace(), String.format(Locale.ROOT, "textures/%s%s", txPath.getPath(), ".png"));
	}
	
	private static final ResourceLocation MISSINGNO = getResourceLocation(MissingTextureAtlasSprite.getLocation());
	
	public static int[] decompose(NativeImage img, int alphaThreshold)
	{
		if(img == null)
		{
			return new int[] {
					0xFF00FF,
					0x000000
			};
		}
		
		var ints = new IntOpenHashSet();
		
		for(int x = 0; x < img.getWidth(); ++x)
			for(int y = 0; y < img.getHeight(); ++y)
			{
				int rgba = img.getPixelRGBA(x, y);
				
				int a = FastColor.ABGR32.alpha(rgba);
				
				if(a <= alphaThreshold)
					continue;
				
				int rgb = ColorHelper.packARGBi(
						a,
						FastColor.ABGR32.red(rgba),
						FastColor.ABGR32.green(rgba),
						FastColor.ABGR32.blue(rgba)
				);
				
				ints.add(rgb);
			}
		
		return ints.toIntArray();
	}
}