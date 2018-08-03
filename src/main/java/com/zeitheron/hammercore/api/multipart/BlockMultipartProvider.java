package com.zeitheron.hammercore.api.multipart;

import com.zeitheron.hammercore.api.INoItemBlock;
import com.zeitheron.hammercore.internal.init.BlocksHC;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

/**
 * Baseline block that supports multipart. Use
 * {@link BlocksHC#registerBlock(Block, String)} to register it. <br>
 * Optional interfaces: {@link INoItemBlock} (why do you need that?)
 */
public abstract class BlockMultipartProvider extends Block implements IMultipartProvider
{
	public BlockMultipartProvider(Material materialIn)
	{
		super(materialIn);
	}
	
	public BlockMultipartProvider(Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(blockMaterialIn, blockMapColorIn);
	}
	
	public Item createItem()
	{
		return new ItemBlockMultipartProvider(this).setTranslationKey(getTranslationKey());
	}
}