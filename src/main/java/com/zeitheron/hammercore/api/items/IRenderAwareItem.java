package com.zeitheron.hammercore.api.items;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public interface IRenderAwareItem
{
	Map<Item, IRenderAwareItem> overrides = new HashMap<>();

	static void overrideRenderAwareness(Block block, IRenderAwareItem aware)
	{
		overrideRenderAwareness(Item.getItemFromBlock(block), aware);
	}

	static void appendRenderAwareness(Block block, IRenderAwareItem aware)
	{
		appendRenderAwareness(Item.getItemFromBlock(block), aware);
	}

	static void overrideRenderAwareness(Item item, IRenderAwareItem aware)
	{
		overrides.put(item, aware);
	}

	static void appendRenderAwareness(Item item, IRenderAwareItem aware)
	{
		overrides.put(item, overrides.computeIfAbsent(item, i -> aware).and(aware));
	}

	static IRenderAwareItem get(ItemStack stack)
	{
		if(stack.isEmpty()) return null;
		Item it = stack.getItem();
		if(overrides.containsKey(it)) return overrides.get(it);
		if(it instanceof IRenderAwareItem) return (IRenderAwareItem) it;
		return null;
	}

	@SideOnly(Side.CLIENT)
	default void preRenderInGUI(ItemStack stack, int x, int y, IBakedModel model)
	{
	}

	@SideOnly(Side.CLIENT)
	default void postRenderInGUI(ItemStack stack, int x, int y, IBakedModel model)
	{
	}

	default IRenderAwareItem and(IRenderAwareItem secondary)
	{
		final IRenderAwareItem primary = this;
		if(primary == secondary) return primary;
		return new IRenderAwareItem()
		{
			@Override
			public void preRenderInGUI(ItemStack stack, int x, int y, IBakedModel model)
			{
				primary.preRenderInGUI(stack, x, y, model);
				secondary.preRenderInGUI(stack, x, y, model);
			}

			@Override
			public void postRenderInGUI(ItemStack stack, int x, int y, IBakedModel model)
			{
				primary.postRenderInGUI(stack, x, y, model);
				secondary.postRenderInGUI(stack, x, y, model);
			}
		};
	}
}