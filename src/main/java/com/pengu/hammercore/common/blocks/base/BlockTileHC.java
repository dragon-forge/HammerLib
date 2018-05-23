package com.pengu.hammercore.common.blocks.base;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.iTileBlock;
import com.pengu.hammercore.common.inventory.InventoryDummy;
import com.pengu.hammercore.common.utils.WorldUtil;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTileHC<T extends TileEntity> extends BlockHC implements ITileEntityProvider, iTileBlock<T>
{
	protected final Class<T> tileClass;
	protected static boolean keepInventory = false;
	protected static boolean spillEssentia = true;
	
	public BlockTileHC(Material mat, Class<T> tc, String name)
	{
		super(mat, name);
		setHardness(2.0f);
		setResistance(20.0f);
		tileClass = tc;
	}
	
	@Override
	public Class<T> getTileClass()
	{
		return tileClass;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		if(this.tileClass == null)
			return null;
		try
		{
			return this.tileClass.newInstance();
		} catch(InstantiationException e)
		{
			HammerCore.LOGGER.catching(e);
		} catch(IllegalAccessException e)
		{
			HammerCore.LOGGER.catching(e);
		}
		return null;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		InventoryDummy.drop(WorldUtil.cast(worldIn.getTileEntity(pos), IInventory.class), worldIn, pos);
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}
	
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
	{
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}
}