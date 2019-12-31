package com.zeitheron.hammercore.internal.blocks.multipart;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.api.INoItemBlock;
import com.zeitheron.hammercore.api.ITileBlock;
import com.zeitheron.hammercore.api.blocks.INoBlockstate;
import com.zeitheron.hammercore.api.inconnect.InConnectAPI;
import com.zeitheron.hammercore.api.mhb.BlockTraceable;
import com.zeitheron.hammercore.api.mhb.ICubeManager;
import com.zeitheron.hammercore.api.multipart.ItemBlockMultipartProvider;
import com.zeitheron.hammercore.api.multipart.MultipartSignature;
import com.zeitheron.hammercore.utils.base.Cast;
import com.zeitheron.hammercore.utils.math.vec.Cuboid6;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockMultipart
		extends BlockTraceable
		implements ITileEntityProvider, ICubeManager, INoItemBlock, ITileBlock<TileMultipart>, INoBlockstate
{
	private static final Cuboid6[] EMPTY_CUBOID_ARRAY = new Cuboid6[0];

	public BlockMultipart()
	{
		super(Material.IRON);
		MinecraftForge.EVENT_BUS.register(this);
		setTranslationKey("multipart");
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return InConnectAPI.makeExtendedPositionedState(world, pos, state);
	}

	@Override
	public Class<TileMultipart> getTileClass()
	{
		return TileMultipart.class;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		TileMultipart tmp = Cast.cast(world.getTileEntity(pos));
		Cuboid6 cbd = getCuboidFromPlayer(player, pos);

		if(tmp != null && cbd != null)
		{
			MultipartSignature signature = tmp.getSignature(cbd.center().toVec3d());
			if(signature != null)
				return signature.getPickBlock(player);
		}

		return super.getPickBlock(state, target, world, pos, player);
	}

	@Override
	public AxisAlignedBB getFullBoundingBox(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return FULL_BLOCK_AABB;
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity)
	{
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;

			Cuboid6 cbd = getCuboidFromPlayer(player, pos);
			TileMultipart tmp = Cast.cast(world.getTileEntity(pos), TileMultipart.class);
			if(tmp != null && cbd != null)
			{
				MultipartSignature s = tmp.getSignature(cbd.center().toVec3d());
				if(s.getState() != null)
					return s.getSoundType(player);
			}
		}
		return super.getSoundType(state, world, pos, entity);
	}

	@Override
	public boolean isOpaqueCube(IBlockState p_isOpaqueCube_1_)
	{
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState p_isOpaqueCube_1_)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState p_isFullCube_1_)
	{
		return false;
	}

	@Override
	public boolean causesSuffocation(IBlockState state)
	{
		return false;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileMultipart();
	}

	@Override
	public Cuboid6[] getCuboids(World world, BlockPos pos, IBlockState state)
	{
		TileMultipart tmp = Cast.cast(world.getTileEntity(pos), TileMultipart.class);
		return tmp != null ? tmp.getCuboids() : EMPTY_CUBOID_ARRAY;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileMultipart tmp = Cast.cast(world.getTileEntity(pos), TileMultipart.class);
		return tmp != null ? tmp.getLightLevel() : 0;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean onBoxActivated(int boxID, Cuboid6 box, World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		Cuboid6 cbd = getCuboidFromPlayer(playerIn, pos);
		TileMultipart tmp = Cast.cast(worldIn.getTileEntity(pos), TileMultipart.class);
		boolean activated = tmp != null ? tmp.onBoxActivated(boxID, cbd, worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ) : false;
		if(!activated)
		{
			ItemStack stack = playerIn.getHeldItem(hand);
			if(stack.getItem() instanceof ItemBlockMultipartProvider)
			{
				EnumActionResult r = stack.getItem().onItemUse(playerIn, worldIn, pos, hand, facing, hitX + 1, hitY, hitZ);
				if(r == EnumActionResult.SUCCESS)
					playerIn.swingArm(hand);
			}
		}
		return activated;
	}

	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		Cuboid6 cbd = getCuboidFromPlayer(player, pos);
		TileMultipart tmp = Cast.cast(worldIn.getTileEntity(pos), TileMultipart.class);

		if(tmp != null && cbd != null)
		{
			MultipartSignature signature = tmp.getSignature(cbd.center().toVec3d());
			return signature.getHardness(player);
		}
		return 0F;
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TileMultipart tmp = Cast.cast(blockAccess.getTileEntity(pos), TileMultipart.class);
		return tmp != null ? tmp.getWeakPower(side) : 0;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TileMultipart tmp = Cast.cast(blockAccess.getTileEntity(pos), TileMultipart.class);
		return tmp != null ? tmp.getStrongPower(side) : 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager)
	{
		TileMultipart tmp = Cast.cast(world.getTileEntity(target.getBlockPos()), TileMultipart.class);
		Cuboid6 cbd = getCuboidFromRTR(world, com.zeitheron.hammercore.raytracer.RayTracer.retrace(HammerCore.renderProxy.getClientPlayer()));
		if(tmp != null && cbd != null)
		{
			MultipartSignature signature = tmp.getSignature(cbd.aabb().getCenter());
			if(signature != null)
				signature.addHitEffects(world, target, manager);
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		TileMultipart tmp = Cast.cast(world.getTileEntity(pos), TileMultipart.class);
		if(tmp != null)
			for(MultipartSignature s : tmp.signatures())
				s.addDestroyEffects(world, pos, manager);
		return true;
	}

	/**
	 * We shall disable vanilla missing textures landing animation
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addLandingEffects(IBlockState state, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles)
	{
		return true;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileMultipart tmp = Cast.cast(world.getTileEntity(pos), TileMultipart.class);
		if(tmp != null)
			for(MultipartSignature s : tmp.signatures())
				if(s != null && s.canConnectRedstone(side))
					return true;
		return false;
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type)
	{
		return false;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return new ArrayList<>();
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	{
		TileMultipart tmp = Cast.cast(worldIn.getTileEntity(pos), TileMultipart.class);
		if(!worldIn.isRemote && tmp != null)
			for(MultipartSignature s : tmp.signatures())
				tmp.removeMultipart(s, true); // Drop everything!
	}

	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		TileMultipart tmp = Cast.cast(worldIn.getTileEntity(pos), TileMultipart.class);
		if(tmp != null)
			tmp.randomDisplayTick(rand);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		TileMultipart tmp = Cast.cast(world.getTileEntity(pos), TileMultipart.class);
		if(tmp != null)
			for(MultipartSignature sign : tmp.signatures())
				sign.onNeighborChange(world, pos, neighbor);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void tryBreakBlock(BlockEvent.BreakEvent evt)
	{
		if(evt.getState().getBlock() == this)
		{
			TileMultipart tmp = Cast.cast(evt.getWorld().getTileEntity(evt.getPos()), TileMultipart.class);
			if(tmp != null)
			{
				if(evt.getWorld().isRemote)
					evt.setCanceled(true);
				Cuboid6 cbd = getCuboidFromPlayer(evt.getPlayer(), evt.getPos());
				if(cbd != null)
				{
					MultipartSignature signature = tmp.getSignature(cbd.center().toVec3d());
					if(signature != null)
					{
						tmp.removeMultipart(signature, !evt.getPlayer().capabilities.isCreativeMode);
						evt.setCanceled(true);
					}
				}
			}
		}
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
	{
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}
}