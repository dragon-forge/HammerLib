package com.pengu.hammercore.common.blocks;

import com.pengu.hammercore.api.iTileBlock;
import com.pengu.hammercore.tile.TileInfiRF;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockInfiRF extends Block implements ITileEntityProvider, iTileBlock<TileInfiRF>
{
	public BlockInfiRF()
	{
		super(Material.IRON);
		setUnlocalizedName("infi_rf");
		setHardness(-1);
		setResistance(Float.MAX_VALUE);
	}
	
	@Override
	public Class<TileInfiRF> getTileClass()
	{
		return TileInfiRF.class;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileInfiRF();
	}
}