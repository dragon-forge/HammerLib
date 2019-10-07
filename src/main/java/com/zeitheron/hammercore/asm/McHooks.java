package com.zeitheron.hammercore.asm;

import com.zeitheron.hammercore.api.events.DisableLightingEvent;
import com.zeitheron.hammercore.api.events.EnableLightingEvent;
import com.zeitheron.hammercore.api.events.PostRenderChunkEvent;
import com.zeitheron.hammercore.api.events.PreRenderChunkEvent;
import com.zeitheron.hammercore.api.events.ProfilerEndEvent;
import com.zeitheron.hammercore.api.events.ProfilerEndStartEvent;
import com.zeitheron.hammercore.api.events.ProfilerStartEvent;
import com.zeitheron.hammercore.api.events.RenderEntityEvent;
import com.zeitheron.hammercore.api.events.RenderTileEntityEvent;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
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
	
	@SideOnly(Side.CLIENT)
	public static void preRenderChunk(RenderChunk c)
	{
		if(FEATURES[0])
			MinecraftForge.EVENT_BUS.post(new PreRenderChunkEvent(c));
	}
	
	@SideOnly(Side.CLIENT)
	public static void postRenderChunk(RenderChunk renderChunk, BlockRenderLayer layer, float x, float y, float z, BufferBuilder bufferBuilderIn, CompiledChunk compiledChunkIn)
	{
		if(FEATURES[8])
			MinecraftForge.EVENT_BUS.post(new PostRenderChunkEvent(renderChunk, layer, x, y, z, bufferBuilderIn, compiledChunkIn));
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
		POST_RENDER_CHUNK;
	}
	
	@SideOnly(Side.CLIENT)
	public static class client
	{
		public static ItemCameraTransforms.TransformType itemTransformType = ItemCameraTransforms.TransformType.NONE;
	}
}