package org.zeith.hammerlib.tiles.tooltip.own.impl;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.*;
import org.zeith.hammerlib.tiles.tooltip.own.*;

import javax.annotation.Nullable;
import java.util.LinkedList;

@OnlyIn(Dist.CLIENT)
public class GuiTooltip
		implements ITooltip
{
	public final LinkedList<TooltipLine> infos = new LinkedList<>();
	protected Level world;
	protected BlockPos pos;
	
	protected Player player;
	protected Entity ent;
	protected float width, height;
	
	protected void refresh()
	{
		width = 0;
		height = 0;
		
		for(int i = 0; i < infos.size(); ++i)
		{
			TooltipLine ln = infos.get(i);
			ln.refresh();
			
			width = Math.max(width, ln.width);
			height += ln.height;
		}
	}
	
	@Override
	public ITooltip add(IRenderableInfo info)
	{
		if(infos.isEmpty())
			newLine();
		infos.getLast().addLast(info);
		refresh();
		return this;
	}
	
	@Override
	public ITooltip newLine()
	{
		infos.addLast(new TooltipLine());
		return this;
	}
	
	@Override
	public float getWidth()
	{
		return width;
	}
	
	@Override
	public float getHeight()
	{
		return height;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(GuiGraphics pose, float x, float y, float partialTime)
	{
		for(TooltipLine ln : infos)
		{
			ln.render(pose, x, y, partialTime);
			y += ln.height;
		}
	}
	
	@Override
	public void reset()
	{
		infos.clear();
		refresh();
	}
	
	public boolean isDirty()
	{
		return provider != null && provider.isTooltipDirty();
	}
	
	public static class TooltipLine
			extends LinkedList<IRenderableInfo>
	{
		protected float width, height;
		
		@OnlyIn(Dist.CLIENT)
		public void render(GuiGraphics pose, float x, float y, float partialTime)
		{
			for(IRenderableInfo info : this)
			{
				info.render(pose, x, y + (height - info.getHeight()) / 2, partialTime);
				x += info.getWidth();
			}
		}
		
		protected void refresh()
		{
			width = 0;
			height = 0;
			
			for(IRenderableInfo info : this)
			{
				width += info.getWidth();
				height = Math.max(height, info.getHeight());
			}
		}
	}
	
	@Nullable
	@Override
	public Level getWorld()
	{
		return world;
	}
	
	@Nullable
	@Override
	public BlockPos getPos()
	{
		return pos;
	}
	
	@Override
	public Entity getEntity()
	{
		return ent;
	}
	
	@Override
	public Player getPlayer()
	{
		return player;
	}
	
	public GuiTooltip withLocation(Level world, BlockPos pos)
	{
		this.world = world;
		this.pos = pos;
		return this;
	}
	
	public GuiTooltip withEntity(Entity ent)
	{
		this.ent = ent;
		this.world = ent.level();
		return this;
	}
	
	public GuiTooltip withPlayer(Player player)
	{
		this.player = player;
		return this;
	}
	
	public ITooltipProvider provider;
	
	public GuiTooltip withProvider(ITooltipProvider provider)
	{
		this.provider = provider;
		provider.addInformation(this);
		return this;
	}
}