package com.pengu.hammercore.core.gui.smooth;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.texture.gui.GuiTexBakery;
import com.pengu.hammercore.client.texture.gui.theme.GuiTheme;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.client.utils.UtilsFX;
import com.pengu.hammercore.utils.ColorHelper;

import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
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
		UtilsFX.bindTexture("textures/gui/def_widgets.png");
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		
		GuiTexBakery b = GuiTexBakery.start().body(0, 0, xSize, ySize);
		for(int k = 0; k < inventorySlots.inventorySlots.size(); ++k)
		{
			Slot s = inventorySlots.inventorySlots.get(k);
			b.slot(s.xPos - 1, s.yPos - 1);
		}
		b.bake().render(i, j);
		
		int col = GuiTheme.CURRENT_THEME.slotColor;
		GL11.glColor4f(ColorHelper.getRed(col), ColorHelper.getGreen(col), ColorHelper.getBlue(col), 1);
		RenderUtil.drawTexturedModalRect(i + 57, j + 37, 43, 0, 13, 13);
		
		if(TileEntityFurnace.isBurning(furn))
		{
			double k = burnLeftScaled(13);
			
			RenderUtil.drawTexturedModalRect(i + 57, j + 36, 14, 0, 14, 14);
			
			GL11.glColor4f(1, 1, 1, 1);
			RenderUtil.drawTexturedModalRect(i + 56, j + 37 + 12 - k, 0, 12 - k, 14, k + 1);
		}
		
		GL11.glColor4f(ColorHelper.getRed(col), ColorHelper.getGreen(col), ColorHelper.getBlue(col), 1);
		RenderUtil.drawTexturedModalRect(i + 81, j + 34, 0, 14, 22, 16);
		
		double l = cookProgressScaled(24);
		RenderUtil.drawTexturedModalRect(i + 81, j + 34, 0, 30, l, 16);
		col = GuiTheme.CURRENT_THEME.getColor(1);
		GL11.glColor4f(ColorHelper.getRed(col), ColorHelper.getGreen(col), ColorHelper.getBlue(col), 1);
		RenderUtil.drawTexturedModalRect(i + 81, j + 34, 0, 14, l, 16);
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