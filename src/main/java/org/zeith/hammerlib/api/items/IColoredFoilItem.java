package org.zeith.hammerlib.api.items;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Provide a custom glint color in format of ARGB (Refer to {@link org.zeith.hammerlib.util.colors.ColorHelper}).
 * When the item is {@link net.minecraft.world.item.Item#isFoil(ItemStack)}, then this interface will indicate to HammerLib to replace the glint with custom colored glint.
 * <p>
 * For using custom glint {@link net.minecraft.client.renderer.RenderType}, refer to {@link org.zeith.hammerlib.client.render.RenderCustomGlint#glintBuffer}
 */
public interface IColoredFoilItem
{
	// should be used unless you want to control the intensity of said foil.
	int FULL_ALPHA = 255 << 24;
	
	default int getFoilColor(ItemStack stack)
	{
		return 0xFFFFFF | FULL_ALPHA;
	}
	
	@Nullable
	static IColoredFoilItem get(ItemStack stack)
	{
		if(stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof IColoredFoilItem f)
			return f;
		return stack.getItem() instanceof IColoredFoilItem f ? f : Binds.OVERRIDES.get(stack.getItem());
	}
	
	static void override(Item item, IColoredFoilItem foil)
	{
		Binds.OVERRIDES.put(item, foil);
	}
	
	static void removeOverride(Item item, IColoredFoilItem foil)
	{
		Binds.OVERRIDES.remove(item, foil);
	}
	
	static IColoredFoilItem constant(int rgba)
	{
		return new IColoredFoilItem()
		{
			@Override
			public int getFoilColor(ItemStack stack)
			{
				return rgba;
			}
		};
	}
	
	class Binds
	{
		
		private static final Map<Item, IColoredFoilItem> OVERRIDES = new Object2ObjectLinkedOpenHashMap<>();
	}
}