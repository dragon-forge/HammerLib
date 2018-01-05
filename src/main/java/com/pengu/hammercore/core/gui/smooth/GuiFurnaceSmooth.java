package com.pengu.hammercore.core.gui.smooth;

import com.pengu.hammercore.client.utils.RenderUtil;

import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;

public class GuiFurnaceSmooth extends GuiFurnace
{
	private static final ResourceLocation FURNACE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");
	private final InventoryPlayer inv;
	private final IInventory furn;
	
	public GuiFurnaceSmooth(InventoryPlayer playerInv, IInventory furnaceInv)
	{
		super(playerInv, furnaceInv);
		inv = playerInv;
		furn = furnaceInv;
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURE);
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
		
		if(TileEntityFurnace.isBurning(furn))
		{
			double k = burnLeftScaled(13);
			RenderUtil.drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		
		double l = cookProgressScaled(24);
		RenderUtil.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
	}
	
	private double cookProgressScaled(double pixels)
	{
		double i = this.furn.getField(2);
		double j = this.furn.getField(3);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
	
	private double burnLeftScaled(double pixels)
	{
		double i = this.furn.getField(1);
		if(i == 0)
			i = 200;
		return this.furn.getField(0) * pixels / i;
	}
}