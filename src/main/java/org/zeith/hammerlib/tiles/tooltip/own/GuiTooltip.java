package org.zeith.hammerlib.tiles.tooltip.own;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.LinkedList;

public class GuiTooltip
		implements ITooltip
{
	public final LinkedList<TooltipLine> infos = new LinkedList<>();
	protected Level world;
	protected BlockPos pos;
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
	@OnlyIn(Dist.CLIENT)
	public void render(PoseStack pose, float x, float y, float partialTime)
	{
		for(int i = 0; i < infos.size(); ++i)
		{
			TooltipLine ln = infos.get(i);
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

	public static class TooltipLine
			extends LinkedList<IRenderableInfo>
	{
		protected int width, height;

		@OnlyIn(Dist.CLIENT)
		public void render(PoseStack pose, float x, float y, float partialTime)
		{
			for(int i = 0; i < size(); ++i)
			{
				IRenderableInfo info = get(i);
				info.render(pose, x, y + (height - info.getHeight()) / 2, partialTime);
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

	public GuiTooltip withLocation(Level world, BlockPos pos)
	{
		this.world = world;
		this.pos = pos;
		return this;
	}

	public GuiTooltip withEntity(Entity ent)
	{
		this.ent = ent;
		this.world = ent.level;
		return this;
	}

	public GuiTooltip withProvider(ITooltipProviderHC provider)
	{
		provider.addInformation(this);
		return this;
	}
}