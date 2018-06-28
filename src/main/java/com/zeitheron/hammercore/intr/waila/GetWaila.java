package com.zeitheron.hammercore.intr.waila;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.api.ITileBlock;
import com.zeitheron.hammercore.tile.tooltip.ITooltipTile;
import com.zeitheron.hammercore.tile.tooltip.ProgressBar;
import com.zeitheron.hammercore.tile.tooltip.eTooltipEngine;
import com.zeitheron.hammercore.utils.WorldLocation;
import com.zeitheron.hammercore.utils.WorldUtil;

import mcp.mobius.waila.api.IWailaCommonAccessor;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.IWailaTooltipRenderer;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GetWaila implements IWailaDataProvider
{
	public static void register(IWailaRegistrar reg)
	{
		HammerCore.LOG.info("Waila API hooked!");
		
		GetWaila provider = new GetWaila();
		reg.registerBodyProvider(provider, Block.class);
		
		for(ResourceLocation r : Block.REGISTRY.getKeys())
		{
			Block b = Block.REGISTRY.getObject(r);
			if(b instanceof ITileBlock && ITooltipTile.class.isAssignableFrom(((ITileBlock) b).getTileClass()))
				reg.registerStackProvider(provider, b.getClass());
		}
		
		reg.registerTooltipRenderer("hammercore:progress_bar", new IWailaTooltipRenderer()
		{
			@Override
			public Dimension getSize(String[] args, IWailaCommonAccessor accessor)
			{
				ITooltipTile tile = WorldUtil.cast(accessor.getTileEntity(), ITooltipTile.class);
				
				if(tile != null && tile.isEngineSupported(eTooltipEngine.WAILA) && tile.hasProgressBars(accessor.getPlayer()))
				{
					ProgressBar[] bars = tile.getProgressBars(accessor.getPlayer());
					
					if(bars != null && bars.length > 0)
						return new Dimension(101, 13 * bars.length);
				}
				
				return new Dimension(0, 0);
			}
			
			@Override
			public void draw(String[] args, IWailaCommonAccessor accessor)
			{
				ITooltipTile tile = WorldUtil.cast(accessor.getTileEntity(), ITooltipTile.class);
				
				if(tile != null && tile.isEngineSupported(eTooltipEngine.WAILA) && tile.hasProgressBars(accessor.getPlayer()))
				{
					ProgressBar[] bars = tile.getProgressBars(accessor.getPlayer());
					
					GL11.glPushMatrix();
					if(bars != null && bars.length > 0)
						for(int i = 0; i < bars.length; ++i)
						{
							ProgressBar bar = bars[i];
							Gui.drawRect(0, 0, 101, 12, bar.borderColor);
							Gui.drawRect(1, 1, 100, 11, bar.backgroundColor);
							
							int fill = 1 + bar.getProgressPercent();
							
							Gui.drawRect(1, 1, fill, 11, bar.filledMainColor);
							
							for(int j = 0; j < fill; j += 2)
								Gui.drawRect(1 + j, 1, 2 + j, 11, bar.filledAlternateColor);
							
							Minecraft.getMinecraft().fontRenderer.drawString((bar.prefix != null ? bar.prefix : "") + (bar.suffix != null ? bar.suffix : ""), 3, 2, 0xFFFFFF, true);
							
							GL11.glTranslated(0, 13, 0);
						}
					GL11.glPopMatrix();
				}
			}
		});
	}
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		WorldLocation loc = new WorldLocation(accessor.getWorld(), accessor.getPosition());
		ITooltipTile itt = loc.getTileOfInterface(ITooltipTile.class);
		
		if(itt != null && itt.hasItemIconOverride())
			return itt.getItemIconOverride();
		
		return accessor.getStack();
	}
	
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		WorldLocation loc = new WorldLocation(accessor.getWorld(), accessor.getPosition());
		ITooltipTile itt = loc.getTileOfInterface(ITooltipTile.class);
		
		if(itt != null && itt.isEngineSupported(eTooltipEngine.WAILA))
		{
			List<String> tip = new ArrayList<>();
			itt.getTextTooltip(tip, accessor.getPlayer());
			for(String s : tip)
				if(s.contains("\n"))
					for(String t : s.split("\n"))
						currenttip.add(t);
				else
					currenttip.add(s);
				
			if(itt.hasProgressBars(accessor.getPlayer()))
				currenttip.add(SpecialChars.getRenderString("hammercore:progress_bar", "handle"));
		}
		
		return currenttip;
	}
	
}