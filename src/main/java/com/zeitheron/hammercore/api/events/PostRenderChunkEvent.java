package com.zeitheron.hammercore.api.events;

import com.zeitheron.hammercore.asm.McHooks;
import com.zeitheron.hammercore.asm.McHooks.EnumMcHook;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PostRenderChunkEvent extends Event
{
	RenderChunk renderChunk;
	BlockRenderLayer layer;
	float x, y, z;
	BufferBuilder bufferBuilderIn;
	CompiledChunk compiledChunkIn;
	
	public PostRenderChunkEvent(RenderChunk renderChunk, BlockRenderLayer layer, float x, float y, float z, BufferBuilder bufferBuilderIn, CompiledChunk compiledChunkIn)
	{
		this.renderChunk = renderChunk;
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.z = z;
		this.bufferBuilderIn = bufferBuilderIn;
		this.compiledChunkIn = compiledChunkIn;
	}
	
	public RenderChunk getRenderChunk()
	{
		return renderChunk;
	}
	
	public BlockRenderLayer getLayer()
	{
		return layer;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getZ()
	{
		return z;
	}
	
	public BufferBuilder getBufferBuilder()
	{
		return bufferBuilderIn;
	}
	
	public CompiledChunk getCompiledChunk()
	{
		return compiledChunkIn;
	}
	
	public static void enable()
	{
		McHooks.enableFeature(EnumMcHook.POST_RENDER_CHUNK);
	}
}