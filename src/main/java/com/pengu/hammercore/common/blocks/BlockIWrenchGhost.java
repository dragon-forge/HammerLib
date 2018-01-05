package com.pengu.hammercore.common.blocks;

import java.util.List;

import com.pengu.hammercore.api.iNoItemBlock;
import com.pengu.hammercore.api.iTileBlock;
import com.pengu.hammercore.core.init.BlocksHC;
import com.pengu.hammercore.tile.TileIWrenchGhost;
import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockIWrenchGhost extends BlockContainer implements iNoItemBlock, iTileBlock<TileIWrenchGhost>, ITileEntityProvider
{
	public BlockIWrenchGhost()
	{
		super(Material.ROCK);
		setUnlocalizedName("iwrench_block");
	}
	
	public static void to(WorldLocation loc, int life)
	{
		loc.setBlock(BlocksHC.IWRENCH_GHOST);
		loc.setTile(new TileIWrenchGhost(life));
	}
	
	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return new AxisAlignedBB(.3, .3, .3, .7, .7, .7);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_)
	{
		
	}
	
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return newTile();
	}
	
	@Override
	public Class<TileIWrenchGhost> getTileClass()
	{
		return TileIWrenchGhost.class;
	}
}