package com.zeitheron.hammercore.api.events;

import com.zeitheron.hammercore.asm.McHooks;
import com.zeitheron.hammercore.asm.McHooks.EnumMcHook;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTileEntityEvent extends Event
{
	private final TileEntity e;
	
	public RenderTileEntityEvent(TileEntity e)
	{
		super();
		this.e = e;
	}
	
	public TileEntity getTile()
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
		McHooks.enableFeature(EnumMcHook.RENDER_TILE_ENTITY);
	}
}