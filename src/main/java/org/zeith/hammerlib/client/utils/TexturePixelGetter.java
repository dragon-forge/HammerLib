package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.zeith.hammerlib.client.render.item.Stack2ImageRenderer;
import org.zeith.hammerlib.util.colors.ColorHelper;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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
		var key = stack.toString() + "_" + stack.getComponentsPatch();
		
		var ct = cachedRenderedColorsCompletionTimes.get(key);
		if(ct != null && System.currentTimeMillis() - ct > 100L)
		{
			cachedRenderedColors.remove(key);
			cachedRenderedColorsCompletionTimes.remove(key);
		}
		
		return cachedRenderedColors.computeIfAbsent(key, ignore ->
		{
			CompletableFuture<int[]> colorsFuture = new CompletableFuture<>();
			
			Stack2ImageRenderer.renderItemStack(null, 64, stack, image ->
			{
				colorsFuture.complete(decompose(image, 0));
				cachedRenderedColorsCompletionTimes.put(ignore, System.currentTimeMillis());
			});
			
			return colorsFuture;
		});
	}
	
	public static int[] getAllColors(ItemStack stack)
	{
		var key = stack.toString() + "_" + stack.getComponentsPatch();
		
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
		
		ItemStackRenderState state = new ItemStackRenderState();
		Minecraft.getInstance()
				.getItemModelResolver()
				.updateForTopItem(state, stack, ItemDisplayContext.GUI, false, mc.level, mc.player, 0);
		
		IntSet ints = new IntOpenHashSet();
		for(int i = 0; i < state.activeLayerCount; i++)
		{
			ItemStackRenderState.LayerRenderState l = state.layers[i];
			BakedModel model = l.model;
			TextureAtlasSprite spr = model.getParticleIcon(ModelData.EMPTY);
			if(spr == null) spr = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(MissingTextureAtlasSprite.getLocation());
			ints.addAll(IntSet.of(getAllColors(getResourceLocation(spr.contents().name()))));
		}
		
		return ints.toIntArray();
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
		return Resources.location(txPath.getNamespace(), String.format(Locale.ROOT, "textures/%s%s", txPath.getPath(), ".png"));
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
				int rgba = img.getPixel(x, y);
				
				int a = ARGB.alpha(rgba);
				
				if(a <= alphaThreshold)
					continue;
				
				int rgb = ColorHelper.packARGBi(
						a,
						ARGB.red(rgba),
						ARGB.green(rgba),
						ARGB.blue(rgba)
				);
				
				ints.add(rgb);
			}
		
		return ints.toIntArray();
	}
}