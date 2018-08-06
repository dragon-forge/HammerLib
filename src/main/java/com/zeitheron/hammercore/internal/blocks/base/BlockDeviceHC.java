package com.zeitheron.hammercore.internal.blocks.base;

import java.util.ArrayList;

import com.zeitheron.hammercore.utils.WorldUtil;

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
	protected boolean reactsToRedstone;
	
	public BlockDeviceHC(Material mat, Class<T> tc, String name)
	{
		super(mat, tc, name);
		
		{
			IBlockState bs = blockState.getBaseState();
			if(this instanceof IBlockHorizontal)
				bs.withProperty(IBlockHorizontal.FACING, EnumFacing.NORTH);
			else if(this instanceof IBlockOrientable)
				bs.withProperty(IBlockOrientable.FACING, EnumFacing.UP);
			if(this instanceof IBlockEnableable)
				bs.withProperty(IBlockEnableable.ENABLED, ((IBlockEnableable) this).enableableDefault());
			setDefaultState(bs);
		}
	}
	
	public static void updateStateKeepTile(World world, BlockPos pos, IBlockState newState)
	{
		TileEntity tile = world.getTileEntity(pos);
		world.removeTileEntity(pos);
		world.setBlockState(pos, newState, 3);
		tile.validate();
		world.setTileEntity(pos, tile);
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
		if(this instanceof IBlockHorizontal)
			bs = bs.withProperty(IBlockHorizontal.FACING, placer.isSneaking() ? placer.getHorizontalFacing() : placer.getHorizontalFacing().getOpposite());
		if(this instanceof IBlockOrientable)
			bs = bs.withProperty(IBlockOrientable.FACING, placer.isSneaking() ? EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite() : EnumFacing.getDirectionFromEntityLiving(pos, placer));
		if(this instanceof IBlockEnableable)
			bs = bs.withProperty(IBlockEnableable.ENABLED, ((IBlockEnableable) this).enableableDefault());
		return bs;
	}
	
	protected void updateState(World worldIn, BlockPos pos, IBlockState state)
	{
		if(reactsToRedstone && this instanceof IBlockEnableable)
		{
			boolean flag;
			boolean bl = flag = !worldIn.isBlockPowered(pos);
			if(flag != state.getValue(IBlockEnableable.ENABLED))
				updateStateKeepTile(worldIn, pos, state.withProperty(IBlockEnableable.ENABLED, flag));
		}
	}
	
	public void updateFacing(World world, BlockPos pos, EnumFacing face)
	{
		if(this instanceof IBlockOrientable || this instanceof IBlockHorizontal)
		{
			if(face == WorldUtil.getFacing(world.getBlockState(pos)))
				return;
			if(this instanceof IBlockHorizontal && face.getHorizontalIndex() >= 0)
				updateStateKeepTile(world, pos, world.getBlockState(pos).withProperty(IBlockHorizontal.FACING, face));
			if(this instanceof IBlockOrientable)
				updateStateKeepTile(world, pos, world.getBlockState(pos).withProperty(IBlockOrientable.FACING, face));
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState bs = this.getDefaultState();
		
		try
		{
			if(this instanceof IBlockHorizontal)
				bs = bs.withProperty(IBlockHorizontal.FACING, WorldUtil.getFacing(meta));
			if(this instanceof IBlockOrientable)
				bs = bs.withProperty(IBlockOrientable.FACING, WorldUtil.getFacing(meta));
			if(this instanceof IBlockEnableable)
				bs = bs.withProperty(IBlockEnableable.ENABLED, Boolean.valueOf(WorldUtil.isEnabled(meta)));
		} catch(Exception var4)
		{
		}
		
		return bs;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = this instanceof IBlockHorizontal ? b0 | state.getValue(IBlockHorizontal.FACING).getIndex() : (this instanceof IBlockOrientable ? b0 | state.getValue(IBlockOrientable.FACING).getIndex() : b0);
		if(this instanceof IBlockEnableable && !state.getValue(IBlockEnableable.ENABLED))
			i |= 8;
		return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		ArrayList<IProperty> ip = new ArrayList<>();
		if(this instanceof IBlockHorizontal)
			ip.add(IBlockHorizontal.FACING);
		if(this instanceof IBlockOrientable)
			ip.add(IBlockOrientable.FACING);
		if(this instanceof IBlockEnableable)
			ip.add(IBlockEnableable.ENABLED);
		return ip.size() == 0 ? super.createBlockState() : new BlockStateContainer(this, ip.toArray(new IProperty[ip.size()]));
	}
}