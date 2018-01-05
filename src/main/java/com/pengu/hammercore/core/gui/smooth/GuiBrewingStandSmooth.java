package com.pengu.hammercore.core.gui.smooth;

import com.pengu.hammercore.client.utils.RenderUtil;

import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiBrewingStandSmooth extends GuiBrewingStand
{
	private static final ResourceLocation BREWING_STAND_GUI_TEXTURE = new ResourceLocation("textures/gui/container/brewing_stand.png");
	private static final double[] BUBBLELENGTH = new double[] { 29, 24, 20, 16, 11, 6, 0 };
	private final InventoryPlayer inv;
	private final IInventory bs;
	
	public GuiBrewingStandSmooth(InventoryPlayer playerInv, IInventory bs)
	{
		super(playerInv, bs);
		inv = playerInv;
		this.bs = bs;
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		mc.getTextureManager().bindTexture(BREWING_STAND_GUI_TEXTURE);
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
		double k = bs.getField(1);
		double l = MathHelper.clamp((18D * k + 20D - 1D) / 20D, 0, 18);
		
		// Draw catalyst bar
		if(l >= 1)
			RenderUtil.drawTexturedModalRect(i + 60, j + 44, 176, 29, l, 4);
		
		int i1 = bs.getField(0);
		
		if(i1 > 0)
		{
			double j1 = (28.0F * (1.0F - (float) i1 / 400.0F));
			if(j1 > 0)
				RenderUtil.drawTexturedModalRect(i + 97, j + 16, 176, 0, 9, j1);
			j1 = BUBBLELENGTH[i1 / 2 % 7];
			if(j1 > 0)
				RenderUtil.drawTexturedModalRect(i + 63, j + 14 + 29 - j1, 185, 29 - j1, 12, j1);
		}
	}
}