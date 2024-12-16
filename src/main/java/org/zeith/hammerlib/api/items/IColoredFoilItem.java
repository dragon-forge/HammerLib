package org.zeith.hammerlib.api.items;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;
import org.zeith.hammerlib.api.items.coms.CustomGlintComponent;
import org.zeith.hammerlib.core.init.ComponentTypesHL;

import java.util.Map;
import java.util.Stack;

/**
 * Provide a custom glint color in format of ARGB (Refer to {@link org.zeith.hammerlib.util.colors.ColorHelper}).
 * When the item is {@link net.minecraft.world.item.Item#isFoil(ItemStack)}, then this interface will indicate to HammerLib to replace the glint with custom colored glint.
 * <p>
 * For using custom glint {@link net.minecraft.client.renderer.RenderType}, refer to {@link org.zeith.hammerlib.client.render.RenderCustomGlint#glintBuffer}
 */
@FunctionalInterface
public interface IColoredFoilItem
{
	/**
	 * Should be used unless you want to control the intensity of said foil.
	 * Example use case:
	 * <p>
	 * <code>return 0xFF00FF | FULL_ALPHA;</code>
	 */
	int FULL_ALPHA = 255 << 24;
	
	int getFoilColor(@NotNull ItemStack stack);
	
	@Nullable
	static IColoredFoilItem get(ItemStack stack)
	{
		// Data component overrides go first!
		CustomGlintComponent<?> comp = stack.get(ComponentTypesHL.CUSTOM_GLINT);
		if(comp != null) return comp;
		
		// Then we check if the block is IColoredFoilItem
		if(stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof IColoredFoilItem f)
			return f;
		
		// Lastly we check if the item is IColoredFoilItem, and lastly - check overrides.
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
		return stack -> rgba;
	}
	
	class Binds
	{
		private static final Stack<ItemStack> CONTEXT_STACK = new Stack<>();
		
		private static final Map<Item, IColoredFoilItem> OVERRIDES = new Object2ObjectLinkedOpenHashMap<>();
		
		public static ItemStack getContextStack()
		{
			return CONTEXT_STACK.isEmpty() ? ItemStack.EMPTY : CONTEXT_STACK.peek();
		}
		
		public static void pushContextStack(ItemStack pStack)
		{
			CONTEXT_STACK.push(pStack);
		}
		
		public static void popContextStack()
		{
			if(!CONTEXT_STACK.isEmpty())
				CONTEXT_STACK.pop();
		}
		
		public static void clearContextStack()
		{
			CONTEXT_STACK.clear();
		}
	}
}