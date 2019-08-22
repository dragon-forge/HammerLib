package com.zeitheron.hammercore.api.inconnect.block;

import com.zeitheron.hammercore.api.inconnect.IBlockConnectable;
import com.zeitheron.hammercore.api.inconnect.InConnectAPI;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Simple block that gives ability to make connected textures. Refer to this
 * block if you want to extend any other block, but still let it have the same
 * connection.
 */
public abstract class BlockConnectable extends Block implements IBlockConnectable
{
	public BlockConnectable(Material materialIn)
	{
		super(materialIn);
	}
	
	public BlockConnectable(Material materialIn, MapColor mapColorIn)
	{
		super(materialIn, mapColorIn);
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return InConnectAPI.makeExtendedPositionedState(world, pos, state);
	}
	
	@Override
	public AxisAlignedBB getBlockShape(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return FULL_BLOCK_AABB;
	}
}