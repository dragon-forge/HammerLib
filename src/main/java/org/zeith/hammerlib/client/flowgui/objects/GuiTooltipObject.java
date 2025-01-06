package org.zeith.hammerlib.client.flowgui.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.client.flowgui.util.Tooltip;
import org.zeith.hammerlib.util.java.Cast;

import java.util.List;
import java.util.function.Supplier;

public class GuiTooltipObject
		extends GuiObject
{
	public boolean isMouseOver;
	public Font font = Minecraft.getInstance().font;
	public Supplier<Tooltip> tooltip;
	
	public GuiTooltipObject(String name)
	{
		super(name);
	}
	
	public GuiTooltipObject tooltip(Supplier<Tooltip> tooltip)
	{
		this.tooltip = tooltip;
		return this;
	}
	
	public GuiTooltipObject tooltip(Tooltip tooltip)
	{
		return tooltip(Cast.constant(tooltip));
	}
	
	@Override
	public List<Rect2i> getUnpositionedBounds()
	{
		return List.of();
	}
	
	@Override
	protected void onAddedInto(GuiObject parent)
	{
		pos(0, 0);
		rotation(0);
		size(parent.getUnscaledWidth(), parent.getUnscaledHeight());
		scale = new Vec3(1, 1, 1);
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		isMouseOver = pos.isMouseWithin(this);
		
		Tooltip t;
		if(isMouseOver && (t = tooltip.get()) != null)
			drawTooltip(gfx, pos, font, t);
	}
}