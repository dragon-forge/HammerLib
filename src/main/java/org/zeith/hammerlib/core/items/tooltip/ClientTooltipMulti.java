package org.zeith.hammerlib.core.items.tooltip;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.zeith.hammerlib.api.items.tooltip.TooltipMulti;

import java.util.List;

public class ClientTooltipMulti
		implements ClientTooltipComponent
{
	protected final List<ClientTooltipComponent> children;
	
	public ClientTooltipMulti(TooltipMulti multi)
	{
		children = multi.children()
				.stream()
				.map(ClientTooltipComponent::create)
				.toList();
	}
	
	@Override
	public int getHeight()
	{
		return Math.max(0, children.stream()
				.mapToInt(ClientTooltipComponent::getHeight)
				.map(i -> i + 2)
				.sum() - 2);
	}
	
	@Override
	public int getWidth(Font font)
	{
		return children.stream()
				.mapToInt(c -> c.getWidth(font))
				.max()
				.orElse(0);
	}
	
	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics pose)
	{
		for(ClientTooltipComponent child : children)
		{
			child.renderImage(font, x, y, pose);
			y += child.getHeight() + 2;
		}
	}
}