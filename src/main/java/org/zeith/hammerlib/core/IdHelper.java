package org.zeith.hammerlib.core;

import net.minecraft.Util;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.zeith.hammerlib.mixins.world.block.state.BlockBehaviourAccessor;
import org.zeith.hammerlib.mixins.world.item.ItemAccessor;

public class IdHelper
{
	// Taken from Item.Properties
	public static final DependantName<Item, String> BLOCK_DESCRIPTION_ID = (id) -> Util.makeDescriptionId("block", id.location());
	public static final DependantName<Item, String> ITEM_DESCRIPTION_ID = (id) -> Util.makeDescriptionId("item", id.location());
	
	// Taken from BlockBehavior.Properties
	public static final DependantName<Block, String> BLOCK_B_DESCRIPTION_ID = (id) -> Util.makeDescriptionId("block", id.location());
	
	public static void hotswap(Item item, ResourceLocation id)
	{
		ItemAccessor acc = (ItemAccessor) item;
		
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
		var descriptionId = (item instanceof BlockItem ? BLOCK_DESCRIPTION_ID : ITEM_DESCRIPTION_ID).get(key);
		
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
		
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
		var descriptionId = BLOCK_B_DESCRIPTION_ID.get(key);
		
		acc.setDescriptionId(descriptionId);
	}
}