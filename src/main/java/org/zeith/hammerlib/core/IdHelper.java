package org.zeith.hammerlib.core;

import net.minecraft.Util;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.zeith.hammerlib.mixins.world.block.state.BlockBehaviourAccessor;
import org.zeith.hammerlib.mixins.world.item.ItemAccessor;

public class IdHelper
{
	public static void hotswap(Item item, ResourceLocation id)
	{
		ItemAccessor acc = (ItemAccessor) item;
		
		var descriptionId = Util.makeDescriptionId(item instanceof BlockItem ? "block" : "item", id);
		
		DataComponentMap.Builder builder = DataComponentMap.builder();
		builder.addAll(acc.getComponents());
		builder.set(DataComponents.ITEM_NAME, Component.translatable(descriptionId));
		builder.set(DataComponents.ITEM_MODEL, id);
		
		acc.setComponents(builder.build());
		acc.setDescriptionId(descriptionId);
	}
	
	public static void hotswap(BlockBehaviour block, ResourceLocation id)
	{
		BlockBehaviourAccessor acc = (BlockBehaviourAccessor) block;
		
		var descriptionId = Util.makeDescriptionId("block", id);
		
		acc.setDescriptionId(descriptionId);
	}
}