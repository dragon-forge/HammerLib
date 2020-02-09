package com.zeitheron.hammercore.asm;

import com.zeitheron.hammercore.api.events.*;
import com.zeitheron.hammercore.api.tile.ITickSlipAwareTile;
import com.zeitheron.hammercore.api.tile.ITickSlipPreventTile;
import com.zeitheron.hammercore.cfg.tickslip.TickSlipConfig;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class McHooks
{
	public static final boolean[] FEATURES = new boolean[EnumMcHook.values().length];

	public static void enableFeature(EnumMcHook hook)
	{
		FEATURES[hook.ordinal()] = true;
	}

	public static void tickTile(ITickable tickable)
	{
		if(tickable instanceof TileEntity && !(tickable instanceof ITickSlipPreventTile))
		{
			TileEntity tile = (TileEntity) tickable;
			int rate = TickSlipConfig.getTickRate(tile.getBlockType());
			if(tile instanceof ITickSlipAwareTile) ((ITickSlipAwareTile) tile).handleInterpolations(rate);
			long val = tile.getWorld().getWorldTime() + tile.getPos().toLong() * 36824626L;
			if(val % rate == 0L) tickable.update();
		} else tickable.update();
	}

	@SideOnly(Side.CLIENT)
	public static void preRenderChunk(RenderChunk c)
	{
		if(FEATURES[0])
			MinecraftForge.EVENT_BUS.post(new PreRenderChunkEvent(c));
	}

	public static void profilerStart(String section)
	{
		if(FEATURES[1])
			MinecraftForge.EVENT_BUS.post(new ProfilerStartEvent(section));
	}

	public static void profilerEndStart(String section)
	{
		if(FEATURES[2])
			MinecraftForge.EVENT_BUS.post(new ProfilerEndStartEvent(section));
	}

	public static void profilerEnd()
	{
		if(FEATURES[3])
			MinecraftForge.EVENT_BUS.post(ProfilerEndEvent.INSTANCE);
	}

	@SideOnly(Side.CLIENT)
	public static void renderEntity(Entity e)
	{
		if(FEATURES[4])
			MinecraftForge.EVENT_BUS.post(new RenderEntityEvent(e));
	}

	@SideOnly(Side.CLIENT)
	public static void renderTileEntity(TileEntity e)
	{
		if(FEATURES[5])
			MinecraftForge.EVENT_BUS.post(new RenderTileEntityEvent(e));
	}

	@SideOnly(Side.CLIENT)
	public static void enableLighting()
	{
		if(FEATURES[6])
			MinecraftForge.EVENT_BUS.post(EnableLightingEvent.INSTANCE);
	}

	@SideOnly(Side.CLIENT)
	public static void disableLighting()
	{
		if(FEATURES[7])
			MinecraftForge.EVENT_BUS.post(DisableLightingEvent.INSTANCE);
	}

	@SideOnly(Side.CLIENT)
	public static void postRenderChunk(RenderChunk renderChunk, BlockRenderLayer layer, float x, float y, float z, BufferBuilder bufferBuilderIn, CompiledChunk compiledChunkIn)
	{
		if(FEATURES[8])
			MinecraftForge.EVENT_BUS.post(new PostRenderChunkEvent(renderChunk, layer, x, y, z, bufferBuilderIn, compiledChunkIn));
	}

	public static boolean attackEntityItem(EntityItem item, DamageSource src, float amount)
	{
		if(FEATURES[9])
		{
			EntityItemHurtEvent e = new EntityItemHurtEvent(item, src, amount);
			return MinecraftForge.EVENT_BUS.post(e);
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public static void setTransform(ItemCameraTransforms.TransformType t)
	{
		client.itemTransformType = t;
	}

	@SideOnly(Side.CLIENT)
	public static ItemCameraTransforms.TransformType getItemTransformType()
	{
		return client.itemTransformType;
	}

	public enum EnumMcHook
	{
		PRE_RENDER_CHUNK, //
		PROFILER_START, //
		PROFILER_END_START, //
		PROFILER_END, //
		RENDER_ENTITY, //
		RENDER_TILE_ENTITY, //
		ENABLE_LIGHTING, //
		DISABLE_LIGHTING, //
		POST_RENDER_CHUNK, //
		ENTITY_ITEM_HURT;
	}

	@SideOnly(Side.CLIENT)
	public static class client
	{
		public static ItemCameraTransforms.TransformType itemTransformType = ItemCameraTransforms.TransformType.NONE;
	}
}