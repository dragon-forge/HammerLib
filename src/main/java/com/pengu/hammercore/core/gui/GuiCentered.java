package com.pengu.hammercore.core.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

/**
 * Similar to {@link GuiContainer} but doesn't have container. This gets rid of
 * JEI but still provides function to make GUI centered.
 */
public abstract class GuiCentered extends GuiScreen
{
	protected double xSize, ySize, guiLeft, guiTop;
	
	@Override
	public void initGui()
	{
		super.initGui();
		guiLeft = (width - xSize) / 2D;
		guiTop = (height - ySize) / 2D;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	protected abstract void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY);
}