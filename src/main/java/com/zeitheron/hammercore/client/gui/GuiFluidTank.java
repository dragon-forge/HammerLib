package com.zeitheron.hammercore.client.gui;

import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.net.HCNet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 * A simple texture-less fluid tank rendering with tooltip support.
 */
public class GuiFluidTank extends Gui
{
	private static final int TEX_WIDTH = 16;
	private static final int TEX_HEIGHT = 16;
	private static final int MIN_FLUID_HEIGHT = 1;
	
	public int x, y, width, height;
	public FluidTank tank;
	
	public int tankColor = 0xFF7F0000;
	
	/**
	 * Constructs a new tank renderer. Call under {@link GuiScreen#initGui()}
	 * 
	 * @param x
	 *            the X position of the tank
	 * @param y
	 *            the Y position of the tank
	 * @param width
	 *            the width of the tank
	 * @param height
	 *            the height of the tank
	 * @param tank
	 *            the fluid tank to be rendered.
	 */
	public GuiFluidTank(int x, int y, int width, int height, FluidTank tank)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tank = tank;
	}
	
	/**
	 * Renders a tank in the gui.
	 * 
	 * @param mouseX
	 *            The mouse X position
	 * @param mouseY
	 *            The mouse Y position
	 */
	public void render(int mouseX, int mouseY)
	{
		int totalLines = Math.round(height / 5F);
		int half = totalLines / 2;
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		drawFluid(Minecraft.getMinecraft(), x, y);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, 200);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		for(int l = 0; l < totalLines; ++l)
			RenderUtil.drawColoredModalRect(x, y + l * 5, width / (l == half ? 1 : 2), 1, tankColor);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
	
	/**
	 * Returns if you should draw the tooltip (get it from
	 * {@link #getTooltip(int, int)}).
	 * 
	 * @param mouseX
	 *            The mouse X position
	 * @param mouseY
	 *            The mouse Y position
	 * @return should the tooltip be drawn
	 */
	public boolean postRender(int mouseX, int mouseY)
	{
		if(isHovered(mouseX, mouseY))
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			RenderUtil.drawColoredModalRect(x, y, width, height, 0xAAFFFFFF);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			return HCNet.getMouseStack(Minecraft.getMinecraft().player).isEmpty();
		}
		
		return false;
	}
	
	/**
	 * Appends the tooltip to already existing one.
	 * 
	 * @param mouseX
	 *            The mouse X position
	 * @param mouseY
	 *            The mouse Y position
	 * @param tooltip
	 *            The tooltip
	 */
	public void addTooltip(int mouseX, int mouseY, List<String> tooltip)
	{
		tooltip.addAll(getTooltip(mouseX, mouseY));
	}
	
	/**
	 * Creates the tooltip for this tank.
	 * 
	 * @param mouseX
	 *            The mouse X position
	 * @param mouseY
	 *            The mouse Y position
	 * 
	 * @return The new tooltip
	 */
	public List<String> getTooltip(int mouseX, int mouseY)
	{
		if(isHovered(mouseX, mouseY))
			return GuiWidgets.createTooltipForFluidTank(tank);
		return Collections.emptyList();
	}
	
	/**
	 * @param mouseX
	 *            The X position of the mouse.
	 * @param mouseY
	 *            The Y positions of the mouse.
	 * @return if the mouse is over the tank element.
	 */
	public boolean isHovered(int mouseX, int mouseY)
	{
		return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
	}
	
	private void drawFluid(Minecraft minecraft, final int xPosition, final int yPosition)
	{
		FluidStack fluidStack = tank.getFluid();
		if(fluidStack == null)
			return;
		Fluid fluid = fluidStack.getFluid();
		if(fluid == null)
			return;
		TextureAtlasSprite tas = getStillFluidSprite(minecraft, fluid);
		int fluidColor = fluid.getColor(fluidStack);
		float scaledAmount = (fluidStack.amount * height) / (float) tank.getCapacity();
		if(scaledAmount > height)
			scaledAmount = height;
		
		minecraft.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		setGLColorFromInt(fluidColor);
		
		int start = 0;
		
		float BarHeight = scaledAmount;
		
		while(true)
		{
			float x = 0;
			
			if(BarHeight > 16)
			{
				x = 16;
				BarHeight -= 16;
			} else
			{
				x = BarHeight;
				BarHeight = 0;
			}
			
			RenderUtil.drawTexturedModalRect(this.x, y + height - x - start, tas, width, x);
			start = start + 16;
			
			if(x == 0 || BarHeight == 0)
				break;
		}
	}
	
	private static TextureAtlasSprite getStillFluidSprite(Minecraft minecraft, Fluid fluid)
	{
		TextureMap textureMapBlocks = minecraft.getTextureMapBlocks();
		ResourceLocation fluidStill = fluid.getStill();
		TextureAtlasSprite fluidStillSprite = null;
		if(fluidStill != null)
			fluidStillSprite = textureMapBlocks.getTextureExtry(fluidStill.toString());
		if(fluidStillSprite == null)
			fluidStillSprite = textureMapBlocks.getMissingSprite();
		return fluidStillSprite;
	}
	
	private void drawTiledSprite(Minecraft minecraft, final int xPosition, final int yPosition, final int tiledWidth, final int tiledHeight, int color, float scaledAmount, TextureAtlasSprite sprite)
	{
		final int xTileCount = tiledWidth / TEX_WIDTH;
		final int xRemainder = tiledWidth - (xTileCount * TEX_WIDTH);
		final float yTileCount = scaledAmount / TEX_HEIGHT;
		final float yRemainder = scaledAmount - (yTileCount * TEX_HEIGHT);
		
		final int yStart = yPosition + tiledHeight;
		
		for(int xTile = 0; xTile <= xTileCount; xTile++)
			for(int yTile = 0; yTile <= yTileCount; yTile++)
			{
				int width = (xTile == xTileCount) ? xRemainder : TEX_WIDTH;
				float height = (yTile == yTileCount) ? yRemainder : TEX_HEIGHT;
				int x = xPosition + (xTile * TEX_WIDTH);
				int y = yStart - ((yTile + 1) * TEX_HEIGHT);
				if(width > 0 && height > 0)
					drawTextureWithMasking(x, y, sprite, TEX_HEIGHT - height, TEX_WIDTH - width, 100);
			}
	}
	
	private static void setGLColorFromInt(int color)
	{
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		
		GlStateManager.color(red, green, blue, 1.0F);
	}
	
	private static void drawTextureWithMasking(double xCoord, double yCoord, TextureAtlasSprite textureSprite, float maskTop, int maskRight, double zLevel)
	{
		double uMin = (double) textureSprite.getMinU();
		double uMax = (double) textureSprite.getMaxU();
		double vMin = (double) textureSprite.getMinV();
		double vMax = (double) textureSprite.getMaxV();
		uMax = uMax - (maskRight / 16.0 * (uMax - uMin));
		vMax = vMax - (maskTop / 16.0 * (vMax - vMin));
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.pos(xCoord, yCoord + 16, zLevel).tex(uMin, vMax).endVertex();
		bufferBuilder.pos(xCoord + 16 - maskRight, yCoord + 16, zLevel).tex(uMax, vMax).endVertex();
		bufferBuilder.pos(xCoord + 16 - maskRight, yCoord + maskTop, zLevel).tex(uMax, vMin).endVertex();
		bufferBuilder.pos(xCoord, yCoord + maskTop, zLevel).tex(uMin, vMin).endVertex();
		tessellator.draw();
	}
}