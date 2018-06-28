package com.zeitheron.hammercore.client.gui.impl.smooth;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.client.gui.GuiWidgets;
import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.client.utils.texture.gui.DynGuiTex;
import com.zeitheron.hammercore.client.utils.texture.gui.GuiTexBakery;
import com.zeitheron.hammercore.client.utils.texture.gui.theme.GuiTheme;
import com.zeitheron.hammercore.utils.color.ColorHelper;

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
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = tileFurnace.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, GuiTheme.current().textShadeColor);
		fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, GuiTheme.current().textShadeColor);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		UtilsFX.bindTexture("textures/gui/def_widgets.png");
		
		tex.render(guiLeft, guiTop);
		
		int col = GuiTheme.current().slotColor;
		GL11.glColor4f(ColorHelper.getRed(col), ColorHelper.getGreen(col), ColorHelper.getBlue(col), 1);
		RenderUtil.drawTexturedModalRect(guiLeft + 57, guiTop + 37, 43, 0, 13, 13);
		
		if(TileEntityFurnace.isBurning(furn))
		{
			double k = burnLeftScaled(13);
			
			RenderUtil.drawTexturedModalRect(guiLeft + 57, guiTop + 36, 14, 0, 14, 14);
			
			GL11.glColor4f(1, 1, 1, 1);
			RenderUtil.drawTexturedModalRect(guiLeft + 56, guiTop + 37 + 12 - k, 0, 12 - k, 14, k + 1);
		}
		
		double l = cookProgressScaled(24);
		GuiWidgets.drawFurnaceArrow(guiLeft + 81, guiTop + 34, l);
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