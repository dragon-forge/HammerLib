package com.zeitheron.hammercore.internal.blocks;

import com.zeitheron.hammercore.api.INoItemBlock;
import com.zeitheron.hammercore.api.ITileBlock;
import com.zeitheron.hammercore.api.blocks.INoBlockstate;
import com.zeitheron.hammercore.event.LyingItemPickedUpEvent;
import com.zeitheron.hammercore.internal.init.BlocksHC;
import com.zeitheron.hammercore.tile.TileLyingItem;
import com.zeitheron.hammercore.utils.WorldUtil;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class BlockLyingItem extends Block implements ITileEntityProvider, ITileBlock<TileLyingItem>, INoItemBlock, INoBlockstate
{
	public BlockLyingItem()
	{
		super(Material.ROCK);
		setTranslationKey("lying_item");
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		TileLyingItem item = WorldUtil.cast(world.getTileEntity(pos), TileLyingItem.class);
		return item != null ? item.lying.get().copy() : ItemStack.EMPTY;
	}
	
	public static TileLyingItem place(World world, BlockPos pos, ItemStack stack)
	{
		if(world.isBlockLoaded(pos) && world.getBlockState(pos).getBlock().isReplaceable(world, pos))
		{
			world.setBlockState(pos, BlocksHC.LYING_ITEM.getDefaultState());
			TileLyingItem tile = WorldUtil.cast(world.getTileEntity(pos), TileLyingItem.class);
			if(tile == null)
				world.setTileEntity(pos, tile = new TileLyingItem());
			tile.lying.set(stack.copy());
			return tile;
		}
		return null;
	}
	
	public static final AxisAlignedBB aabb = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return aabb;
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if(entityIn instanceof EntityPlayer)
		{
			TileLyingItem tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileLyingItem.class);
			if(tile != null)
			{
				try
				{
					LyingItemPickedUpEvent evt = new LyingItemPickedUpEvent((EntityPlayer) entityIn, pos, tile.lying.get().copy(), tile.placedByPlayer.get() != Boolean.TRUE);
					if(MinecraftForge.EVENT_BUS.post(evt))
						return;
					
					EntityItem ei = ((EntityPlayer) entityIn).dropItem(evt.drop, true);
					if(ei != null)
						ei.setNoPickupDelay();
				} finally
				{
					worldIn.setBlockToAir(pos);
				}
			}
		}
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
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
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileLyingItem();
	}
	
	@Override
	public Class<TileLyingItem> getTileClass()
	{
		return TileLyingItem.class;
	}
}