package com.pengu.hammercore.tile.tooltip.own;

import java.util.LinkedList;

import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.entity.Entity;

public class GuiTooltip implements ITooltip
{
	public final LinkedList<TooltipLine> infos = new LinkedList<>();
	protected WorldLocation loc;
	protected Entity ent;
	protected int width, height;
	
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
	public void append(IRenderableInfo info)
	{
		if(infos.isEmpty())
			newLine();
		infos.getLast().addLast(info);
		refresh();
	}
	
	@Override
	public void newLine()
	{
		infos.addLast(new TooltipLine());
	}
	
	@Override
	public int getWidth()
	{
		return width;
	}
	
	@Override
	public int getHeight()
	{
		return height;
	}
	
	@Override
	public void render(float x, float y, float partialTime)
	{
		for(int i = 0; i < infos.size(); ++i)
		{
			TooltipLine ln = infos.get(i);
			ln.render(x, y, partialTime);
			y += ln.height;
		}
	}
	
	@Override
	public void reset()
	{
		infos.clear();
		refresh();
	}
	
	public static class TooltipLine extends LinkedList<IRenderableInfo>
	{
		protected int width, height;
		
		public void render(float x, float y, float partialTime)
		{
			for(int i = 0; i < size(); ++i)
			{
				IRenderableInfo info = get(i);
				info.render(x, y + (height - info.getHeight()) / 2, partialTime);
				x += info.getWidth();
			}
		}
		
		protected void refresh()
		{
			width = 0;
			height = 0;
			
			for(int i = 0; i < size(); ++i)
			{
				IRenderableInfo info = get(i);
				
				width += info.getWidth();
				height = Math.max(height, info.getHeight());
			}
		}
	}
	
	@Override
	public WorldLocation getLocation()
	{
		return loc;
	}
	
	@Override
	public Entity getEntity()
	{
		return ent;
	}
	
	public GuiTooltip withLocation(WorldLocation loc)
	{
		this.loc = loc;
		return this;
	}
	
	public GuiTooltip withEntity(Entity ent)
	{
		this.ent = ent;
		return this;
	}
	
	public GuiTooltip withProvider(iTooltipProviderHC provider)
	{
		provider.addInformation(this);
		return this;
	}
}