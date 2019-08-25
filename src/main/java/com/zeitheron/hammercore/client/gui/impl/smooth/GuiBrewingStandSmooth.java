package com.zeitheron.hammercore.client.gui.impl.smooth;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.client.utils.texture.gui.DynGuiTex;
import com.zeitheron.hammercore.client.utils.texture.gui.GuiTexBakery;
import com.zeitheron.hammercore.client.utils.texture.gui.theme.GuiTheme;
import com.zeitheron.hammercore.utils.color.ColorHelper;

import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

/**
 * An internal class.
 */
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
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		UtilsFX.bindTexture("minecraft", "textures/gui/container/brewing_stand.png");
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		int k = this.tileBrewingStand.getField(1);
		float l = MathHelper.clamp((18 * k) / 20F, 0, 18);
		
		if(l > 0)
		{
			RenderUtil.drawTexturedModalRect(i + 60, j + 44, 176, 29, l, 4);
		}
		
		int i1 = this.tileBrewingStand.getField(0);
		
		if(i1 > 0)
		{
			float j1 = 28.0F * (1.0F - (float) i1 / 400.0F);
			
			if(j1 > 0)
			{
				RenderUtil.drawTexturedModalRect(i + 97, j + 16, 176, 0, 9, j1);
			}
			
			j1 = (float) BUBBLELENGTH[i1 / 2 % 7];
			
			if(j1 > 0)
			{
				RenderUtil.drawTexturedModalRect(i + 63, j + 14 + 29 - j1, 185, 29 - j1, 12, j1);
			}
		}
	}
}