package com.zeitheron.hammercore.event.vanilla;

import com.zeitheron.hammercore.event.WrenchEvent;
import com.zeitheron.hammercore.internal.blocks.IWitherProofBlock;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.tile.*;
import com.zeitheron.hammercore.utils.*;
import com.zeitheron.hammercore.utils.base.Cast;
import com.zeitheron.hammercore.utils.wrench.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;

@Mod.EventBusSubscriber
public class TileHandler
{
	@SubscribeEvent
	public static void breakBlock(BlockEvent.BreakEvent evt)
	{
		IBlockState state = evt.getState();
		if(state.getBlock() instanceof ITileDroppable)
			((ITileDroppable) state.getBlock()).createDrop(evt.getPlayer(), evt.getWorld(), evt.getPos());
		TileEntity te = evt.getWorld().getTileEntity(evt.getPos());
		if(te instanceof ITileDroppable)
			((ITileDroppable) te).createDrop(evt.getPlayer(), evt.getWorld(), evt.getPos());
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerInteract(PlayerInteractEvent.RightClickBlock evt)
	{
		EntityPlayer player = evt.getEntityPlayer();

		if(player != null && !player.getHeldItem(evt.getHand()).isEmpty())
		{
			IWrenchItem item = Cast.cast(player.getHeldItem(evt.getHand()).getItem(), IWrenchItem.class);
			if(item != null && item.canWrench(player.getHeldItem(evt.getHand())))
			{
				MinecraftForge.EVENT_BUS.post(new WrenchEvent(player, evt.getPos(), evt.getHand(), evt.getFace()));

				item.onWrenchUsed(player, evt.getPos(), evt.getHand());

				WorldLocation wl = new WorldLocation(evt.getWorld(), evt.getPos());

				boolean swing = false;
				if(wl.getBlock() instanceof IWrenchable && ((IWrenchable) wl.getBlock()).onWrenchUsed(wl, player, evt.getHand()))
					swing = true;
				if(wl.getTile() instanceof IWrenchable && ((IWrenchable) wl.getTile()).onWrenchUsed(wl, player, evt.getHand()))
					swing = true;
				if(swing)
				{
					HCNet.swingArm(player, evt.getHand());
					evt.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void placeBlock(BlockEvent.PlaceEvent evt)
	{
		TileEntity te = evt.getWorld().getTileEntity(evt.getPos());

		if(te == null)
		{
			Block block = evt.getPlacedBlock().getBlock();
			if(block instanceof ITileEntityProvider)
			{
				te = block.createTileEntity(evt.getWorld(), evt.getPlacedBlock());
				evt.getWorld().setTileEntity(evt.getPos(), te);
			}
		}

		if(te instanceof TileSyncable)
		{
			TileSyncable tile = (TileSyncable) te;
			tile.onPlacedBy(evt.getPlayer(), evt.getHand());
		}
	}

	@SubscribeEvent
	public static void handleWitherBlocks(LivingDestroyBlockEvent e)
	{
		if(e.getEntityLiving() instanceof EntityWither)
		{
			IBlockState state = e.getState();
			if(state.getBlock() instanceof IWitherProofBlock && ((IWitherProofBlock) state.getBlock()).isWitherproof(state))
				e.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void handleWitherBlocks(ExplosionEvent.Detonate w)
	{
		Explosion expl = w.getExplosion();
		if(expl != null && expl.getExplosivePlacedBy() instanceof EntityWither)
		{
			List<BlockPos> b = w.getAffectedBlocks();
			for(int i = 0; i < b.size(); ++i)
			{
				BlockPos p = b.get(i);
				IBlockState state = w.getWorld().getBlockState(p);
				if(state.getBlock() instanceof IWitherProofBlock && ((IWitherProofBlock) state.getBlock()).isWitherproof(state))
				{
					b.remove(i);
					--i;
					continue;
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void overrideBlockHiglight(DrawBlockHighlightEvent e)
	{
		RayTraceResult rtr = e.getTarget();
		if(rtr != null && rtr.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			float partialTicks = e.getPartialTicks();
			EntityPlayer player = e.getPlayer();

			TileEntity tile = player.world.getTileEntity(rtr.getBlockPos());

			if(tile instanceof IVoxelShapeTile)
			{
				IVoxelShapeTile lineHighlight = (IVoxelShapeTile) tile;
				e.setCanceled(true);

				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.glLineWidth(2.0F);
				GlStateManager.disableTexture2D();
				GlStateManager.depthMask(false);

				BlockPos blockpos = rtr.getBlockPos();
				IBlockState iblockstate = player.world.getBlockState(blockpos);

				if(iblockstate.getMaterial() != Material.AIR && player.world.getWorldBorder().contains(blockpos))
				{
					double d3 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
					double d4 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
					double d5 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

					// Instead of drawing selection BB, we shall render lines
					AABBUtils.Line.LineRenderer.drawBoundingBox(lineHighlight.getHighlightedLines().grow(0.0020000000949949026D).offset(blockpos.getX() - d3, blockpos.getY() - d4, blockpos.getZ() - d5).asLines().stream(), 0, 0, 0, 0.4F);
				}

				GlStateManager.depthMask(true);
				GlStateManager.enableTexture2D();
				GlStateManager.disableBlend();
			}
		}
	}
}