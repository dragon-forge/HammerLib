package org.zeith.hammerlib.core.items.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.zeith.hammerlib.api.items.tooltip.AlignAxis;
import org.zeith.hammerlib.api.items.tooltip.TooltipMulti;

import java.util.List;
import java.util.function.ToIntFunction;

public class ClientTooltipMulti
		implements ClientTooltipComponent
{
	protected final AlignAxis axis;
	protected final List<ClientTooltipComponent> children;
	
	protected final ToIntFunction<Font> width, height;
	
	public ClientTooltipMulti(TooltipMulti multi)
	{
		this.children = multi.children()
				.stream()
				.map(ClientTooltipComponent::create)
				.toList();
		
		this.axis = multi.axis();
		
		int pad = multi.padding();
		
		switch(axis)
		{
			case VERTICAL ->
			{
				this.height = font -> Math.max(0,
						children
								.stream()
								.mapToInt(c -> c.getHeight(font))
								.map(i -> i + pad)
								.sum() - pad
				);
				this.width = font -> Math.max(0,
						children.stream()
								.mapToInt(c -> c.getWidth(font))
								.max()
								.orElse(0)
				);
			}
			case HORIZONTAL ->
			{
				this.height = font -> Math.max(0,
						children.stream()
								.mapToInt(c -> c.getHeight(font))
								.max()
								.orElse(0)
				);
				this.width = font -> Math.max(0,
						children
								.stream()
								.mapToInt(c -> c.getWidth(font))
								.map(i -> i + pad)
								.sum() - pad
				);
			}
			default -> throw new IllegalStateException(axis + " axis is not defined");
		}
	}
	
	@Override
	public int getHeight(Font font)
	{
		return height.applyAsInt(font);
	}
	
	@Override
	public int getWidth(Font font)
	{
		return width.applyAsInt(font);
	}
	
	@Override
	public void renderImage(Font font, int x, int y, int pWidth, int pHeight, GuiGraphics pose)
	{
		switch(axis)
		{
			case VERTICAL ->
			{
				for(ClientTooltipComponent child : children)
				{
					child.renderImage(font, x, y, pWidth, pHeight, pose);
					y += child.getHeight(font) + 2;
				}
			}
			case HORIZONTAL ->
			{
				for(ClientTooltipComponent child : children)
				{
					child.renderImage(font, x, y, pWidth, pHeight, pose);
					x += child.getWidth(font) + 2;
				}
			}
		}
	}
}