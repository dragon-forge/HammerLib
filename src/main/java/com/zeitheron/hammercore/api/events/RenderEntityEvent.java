package com.zeitheron.hammercore.api.events;

import com.zeitheron.hammercore.asm.McHooks;
import com.zeitheron.hammercore.asm.McHooks.EnumMcHook;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityEvent extends Event
{
	private final Entity e;
	
	public RenderEntityEvent(Entity e)
	{
		super();
		this.e = e;
	}
	
	public Entity getEntity()
	{
		return e;
	}
	
	@Override
	public boolean isCancelable()
	{
		return false;
	}
	
	public static void enable()
	{
		McHooks.enableFeature(EnumMcHook.RENDER_ENTITY);
	}
}