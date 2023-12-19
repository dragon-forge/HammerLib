package org.zeith.hammerlib.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class ScreenWTFMojang<T extends AbstractContainerMenu>
		extends AbstractContainerScreen<T>
{
	public ScreenWTFMojang(T container, Inventory playerInv, Component name)
	{
		super(container, playerInv, name);
	}
	
	protected void setSize(int xSize, int ySize)
	{
		imageWidth = xSize;
		imageHeight = ySize;
	}
	
	protected boolean renderForeground(GuiGraphics pose, int mouseX, int mouseY)
	{
		return false;
	}
	
	protected abstract void renderBackground(GuiGraphics pose, float partialTime, int mouseX, int mouseY);
	
	@Override
	public void render(GuiGraphics pose, int mouseX, int mouseY, float partialTime)
	{
		this.renderBackground(pose, mouseX, mouseY, partialTime);
		super.render(pose, mouseX, mouseY, partialTime);
		this.renderTooltip(pose, mouseX, mouseY);
	}
	
	@Override
	protected void renderLabels(GuiGraphics pose, int mouseX, int mouseY)
	{
		if(!renderForeground(pose, mouseX, mouseY))
			super.renderLabels(pose, mouseX, mouseY);
	}
	
	@Override
	protected void renderBg(GuiGraphics gfx, float partialTime, int mouseX, int mouseY)
	{
		renderBackground(gfx, partialTime, mouseX, mouseY);
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