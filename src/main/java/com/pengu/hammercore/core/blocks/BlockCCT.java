package com.pengu.hammercore.core.blocks;

import com.pengu.hammercore.api.iTileBlock;
import com.pengu.hammercore.common.blocks.iItemBlock;
import com.pengu.hammercore.core.blocks.item.ItemCCT;
import com.pengu.hammercore.tile.TileCCT;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCCT extends Block implements ITileEntityProvider, iItemBlock, iTileBlock<TileCCT>
{
	public final ItemCCT item = new ItemCCT(this);
	public final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, .5, 1);
	
	public BlockCCT()
	{
		super(Material.WOOD);
		setUnlocalizedName("compound_crafting_table");
	}
	
	@Override
	public ItemBlock getItemBlock()
	{
		return item;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileCCT();
	}
	
	@Override
	public Class<TileCCT> getTileClass()
	{
		return TileCCT.class;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		
		return true;
	}
}