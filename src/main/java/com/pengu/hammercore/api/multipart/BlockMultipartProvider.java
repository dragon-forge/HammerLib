package com.pengu.hammercore.api.multipart;

import com.pengu.hammercore.api.iNoItemBlock;
import com.pengu.hammercore.core.init.BlocksHC;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

/**
 * Baseline block that supports multipart. Use
 * {@link BlocksHC#registerBlock(Block, String)} to register it. <br>
 * Optional interfaces: {@link iNoItemBlock} (why do you need that?)
 */
public abstract class BlockMultipartProvider extends Block implements iMultipartProvider
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
		return new ItemBlockMultipartProvider(this).setUnlocalizedName(getUnlocalizedName());
	}
}