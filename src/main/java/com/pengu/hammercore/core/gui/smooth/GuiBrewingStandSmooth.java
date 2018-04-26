package com.pengu.hammercore.core.gui.smooth;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.texture.gui.DynGuiTex;
import com.pengu.hammercore.client.texture.gui.GuiTexBakery;
import com.pengu.hammercore.client.texture.gui.theme.GuiTheme;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.client.utils.UtilsFX;
import com.pengu.hammercore.utils.ColorHelper;

import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
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
	
	public DynGuiTex tex;
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		GuiTexBakery b = GuiTexBakery.start().body(0, 0, xSize, ySize);
		for(int o = 0; o < inventorySlots.inventorySlots.size(); ++o)
		{
			Slot s = inventorySlots.inventorySlots.get(o);
			b.slot(s.xPos - 1, s.yPos - 1);
		}
		tex = b.bake();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		mc.getTextureManager().bindTexture(BREWING_STAND_GUI_TEXTURE);
		
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		double k = bs.getField(1);
		double l = MathHelper.clamp((18D * k + 20D - 1D) / 20D, 0, 18);
		
		tex.render(i, j);
		
		int i1 = bs.getField(0);
		
		UtilsFX.bindTexture("textures/gui/def_widgets.png");
		
		int rgb = GuiTheme.CURRENT_THEME.slotCoverLU;
		GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1);
		drawTexturedModalRect(i + 33, j + 15, 0, 222, 46, 34);
		
		rgb = GuiTheme.CURRENT_THEME.slotColor;
		GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1);
		drawTexturedModalRect(i + 33, j + 15, 46, 222, 46, 34);
		
		rgb = GuiTheme.CURRENT_THEME.bodyColor;
		GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1);
		drawTexturedModalRect(i + 33, j + 15, 92, 222, 46, 34);
		
		rgb = GuiTheme.CURRENT_THEME.slotCoverRD;
		GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1);
		drawTexturedModalRect(i + 18, j + 18, 28, 0, 14, 14);
		
		drawTexturedModalRect(i + 60, j + 54, 31, 15, 9, 12);
		drawTexturedModalRect(i + 83, j + 61, 31, 15, 9, 12);
		drawTexturedModalRect(i + 106, j + 54, 31, 15, 9, 12);
		
		rgb = GuiTheme.CURRENT_THEME.slotCoverRD;
		GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1);
		RenderUtil.drawTexturedModalRect(i + 72, j + 33, 0, 164, 30, 25);
		RenderUtil.drawTexturedModalRect(i + 100, j + 17, 48, 195, 7, 26);
		
		rgb = GuiTheme.CURRENT_THEME.slotColor;
		GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1);
		RenderUtil.drawTexturedModalRect(i + 72, j + 33, 30, 164, 30, 25);
		RenderUtil.drawTexturedModalRect(i + 100, j + 17, 48 + 7, 195, 7, 26);
		
		rgb = GuiTheme.CURRENT_THEME.slotCoverLU;
		GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1);
		RenderUtil.drawTexturedModalRect(i + 72, j + 33, 60, 164, 30, 25);
		RenderUtil.drawTexturedModalRect(i + 100, j + 17, 48 + 14, 195, 7, 26);
		
		if(i1 > 0)
		{
			double j1 = (28.0F * (1.0F - i1 / 400.0F));
			if(j1 > 0)
			{
				rgb = GuiTheme.CURRENT_THEME.bodyLayerLU;
				GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1);
				RenderUtil.drawTexturedModalRect(i + 100, j + 17, 26, 194, 9, j1);
				
				rgb = GuiTheme.CURRENT_THEME.bodyLayerRD;
				GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1);
				RenderUtil.drawTexturedModalRect(i + 102, j + 17, 35, 194, 9, j1);
			}
			j1 = BUBBLELENGTH[i1 / 2 % 7];
			if(j1 > 0)
			{
				rgb = GuiTheme.CURRENT_THEME.bodyLayerLU;
				GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1);
				RenderUtil.drawTexturedModalRect(i + 64, j + 15 + 29 - j1, 11, 194 + 29 - j1, 11, j1);
				
				rgb = GuiTheme.CURRENT_THEME.bodyLayerRD;
				GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1);
				RenderUtil.drawTexturedModalRect(i + 64, j + 15 + 29 - j1, 0, 194 + 29 - j1, 11, j1);
			}
		}
		
		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BREWING_STAND_GUI_TEXTURE);
		if(l > 0)
			RenderUtil.drawTexturedModalRect(i + 60, j + 44, 176, 29, l, 4);
	}
}