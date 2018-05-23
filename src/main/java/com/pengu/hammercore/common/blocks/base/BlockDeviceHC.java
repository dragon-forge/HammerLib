package com.pengu.hammercore.common.blocks.base;

import java.util.ArrayList;

import com.pengu.hammercore.common.utils.WorldUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDeviceHC<T extends TileEntity> extends BlockTileHC<T>
{
	public BlockDeviceHC(Material mat, Class<T> tc, String name)
	{
		super(mat, tc, name);
		
		{
			IBlockState bs = blockState.getBaseState();
			if(this instanceof iBlockHorizontal)
				bs.withProperty(iBlockHorizontal.FACING, EnumFacing.NORTH);
			else if(this instanceof iBlockOrientable)
				bs.withProperty(iBlockOrientable.FACING, EnumFacing.UP);
			if(this instanceof iBlockEnableable)
				bs.withProperty(iBlockEnableable.ENABLED, true);
			setDefaultState(bs);
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		this.updateState(worldIn, pos, state);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos frompos)
	{
		this.updateState(worldIn, pos, state);
		super.neighborChanged(state, worldIn, pos, blockIn, frompos);
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		IBlockState bs = this.getDefaultState();
		if(this instanceof iBlockHorizontal)
			bs = bs.withProperty(iBlockHorizontal.FACING, placer.isSneaking() ? placer.getHorizontalFacing() : placer.getHorizontalFacing().getOpposite());
		if(this instanceof iBlockOrientable)
			bs = bs.withProperty(iBlockOrientable.FACING, placer.isSneaking() ? EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite() : EnumFacing.getDirectionFromEntityLiving(pos, placer));
		if(this instanceof iBlockEnableable)
			bs = bs.withProperty(iBlockEnableable.ENABLED, true);
		return bs;
	}
	
	protected void updateState(World worldIn, BlockPos pos, IBlockState state)
	{
		if(this instanceof iBlockEnableable)
		{
			boolean flag;
			boolean bl = flag = !worldIn.isBlockPowered(pos);
			if(flag != state.getValue(iBlockEnableable.ENABLED))
				worldIn.setBlockState(pos, state.withProperty(iBlockEnableable.ENABLED, flag), 3);
		}
	}
	
	public void updateFacing(World world, BlockPos pos, EnumFacing face)
	{
		if(this instanceof iBlockOrientable || this instanceof iBlockHorizontal)
		{
			if(face == WorldUtil.getFacing(world.getBlockState(pos)))
				return;
			if(this instanceof iBlockHorizontal && face.getHorizontalIndex() >= 0)
				world.setBlockState(pos, world.getBlockState(pos).withProperty(iBlockHorizontal.FACING, face), 3);
			if(this instanceof iBlockOrientable)
				world.setBlockState(pos, world.getBlockState(pos).withProperty(iBlockOrientable.FACING, face), 3);
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState bs = this.getDefaultState();
		
		try
		{
			if(this instanceof iBlockHorizontal)
				bs = bs.withProperty(iBlockHorizontal.FACING, WorldUtil.getFacing(meta));
			if(this instanceof iBlockOrientable)
				bs = bs.withProperty(iBlockOrientable.FACING, WorldUtil.getFacing(meta));
			if(this instanceof iBlockEnableable)
				bs = bs.withProperty(iBlockEnableable.ENABLED, Boolean.valueOf(WorldUtil.isEnabled(meta)));
		} catch(Exception var4)
		{
		}
		
		return bs;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = this instanceof iBlockHorizontal ? b0 | state.getValue(iBlockHorizontal.FACING).getIndex() : (this instanceof iBlockOrientable ? b0 | state.getValue(iBlockOrientable.FACING).getIndex() : b0);
		if(this instanceof iBlockEnableable && !state.getValue(iBlockEnableable.ENABLED))
			i |= 8;
		return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		ArrayList<IProperty> ip = new ArrayList<>();
		if(this instanceof iBlockHorizontal)
			ip.add(iBlockHorizontal.FACING);
		if(this instanceof iBlockOrientable)
			ip.add(iBlockOrientable.FACING);
		if(this instanceof iBlockEnableable)
			ip.add(iBlockEnableable.ENABLED);
		return ip.size() == 0 ? super.createBlockState() : new BlockStateContainer(this, ip.toArray(new IProperty[ip.size()]));
	}
}