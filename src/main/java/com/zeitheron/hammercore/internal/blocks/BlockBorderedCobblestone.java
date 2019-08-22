package com.zeitheron.hammercore.internal.blocks;

import com.zeitheron.hammercore.api.inconnect.block.BlockConnectable;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;

public class BlockBorderedCobblestone extends BlockConnectable
{
	public BlockBorderedCobblestone(String sub)
	{
		super(Material.ROCK);
		setTranslationKey(sub + "_bordered_cobblestone");
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public ResourceLocation getTxMap()
	{
		return new ResourceLocation(getRegistryName().getNamespace(), "blocks/" + getRegistryName().getPath() + "_ic");
	}
}