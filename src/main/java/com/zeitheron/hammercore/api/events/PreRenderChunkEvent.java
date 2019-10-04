package com.zeitheron.hammercore.api.events;

import com.zeitheron.hammercore.asm.McHooks;
import com.zeitheron.hammercore.asm.McHooks.EnumMcHook;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PreRenderChunkEvent extends Event
{
	private final RenderChunk rc;
	
	public PreRenderChunkEvent(RenderChunk rc)
	{
		this.rc = rc;
	}
	
	public RenderChunk getRenderChunk()
	{
		return rc;
	}
	
	public static void enable()
	{
		McHooks.enableFeature(EnumMcHook.PRE_RENDER_CHUNK);
	}
}