package org.zeith.hammerlib.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class ScreenWTFMojang<T extends AbstractContainerMenu>
		extends AbstractContainerScreen<T>
{
	public ScreenWTFMojang(T container, Inventory plyerInv, Component name)
	{
		super(container, plyerInv, name);
	}

	protected void setSize(int xSize, int ySize)
	{
		imageWidth = xSize;
		imageHeight = ySize;
	}

	protected boolean renderForeground(PoseStack matrix, int mouseX, int mouseY)
	{
		return false;
	}

	protected abstract void renderBackground(PoseStack matrix, float partialTime, int mouseX, int mouseY);

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTime)
	{
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTime);
		this.renderTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack matrix, int mouseX, int mouseY)
	{
		if(!renderForeground(matrix, mouseX, mouseY))
			super.renderLabels(matrix, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack matrix, float partialTime, int mouseX, int mouseY)
	{
		renderBackground(matrix, partialTime, mouseX, mouseY);
	}

	protected boolean clickMenuButton(int button)
	{
		if(this.menu.clickMenuButton(this.minecraft.player, button))
		{
			this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, button);
			return true;
		}
		return false;
	}
}