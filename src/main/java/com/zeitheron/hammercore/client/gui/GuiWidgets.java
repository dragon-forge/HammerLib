package com.zeitheron.hammercore.client.gui;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.client.utils.texture.gui.theme.GuiTheme;
import com.zeitheron.hammercore.utils.color.ColorHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

/**
 * A simple widgets class that has some default rendering methods.
 */
public class GuiWidgets
{
	public static final ResourceLocation TEXTURE = new ResourceLocation("hammercore", "textures/gui/widgets.png");
	public static final long fullCycle = 1500;
	
	/**
	 * Creates a tooltip for fluid tank.
	 * 
	 * @param tank
	 *            The tank to create a tooltip for.
	 * @return The tooltip
	 */
	public static List<String> createTooltipForFluidTank(FluidTank tank)
	{
		FluidStack fs = tank.getFluid();
		if(fs != null)
		{
			String mod = "Unknown";
			ModContainer mc = Loader.instance().getIndexedModList().get(FluidRegistry.getModId(fs));
			if(mc != null)
				mod = mc.getName();
			return Arrays.asList(fs.getLocalizedName(), TextFormatting.GRAY + String.format("%,d / %,d mB", fs.amount, tank.getCapacity()), TextFormatting.BLUE.toString() + TextFormatting.ITALIC + mod);
		} else
			return Arrays.asList("Empty", TextFormatting.GRAY + String.format("%,d / %,d mB", 0, tank.getCapacity()));
	}
	
	/**
	 * Renders the furnace progress bar arrow.
	 * 
	 * @param x
	 *            The x position to render on
	 * @param y
	 *            The y position to render on
	 * @param l
	 *            The progress of the bar, may be [0; 1].
	 */
	public static void drawFurnaceArrow(float x, float y, double l)
	{
		UtilsFX.bindTexture("textures/gui/def_widgets.png");
		int col = GuiTheme.current().slotColor;
		
		GL11.glColor4f(ColorHelper.getRed(col), ColorHelper.getGreen(col), ColorHelper.getBlue(col), 1);
		RenderUtil.drawTexturedModalRect(x, y, 0, 14, 22, 16);
		
		RenderUtil.drawTexturedModalRect(x, y, 0, 30, l * 24, 16);
		col = GuiTheme.current().getColor(1);
		GL11.glColor4f(ColorHelper.getRed(col), ColorHelper.getGreen(col), ColorHelper.getBlue(col), 1);
		RenderUtil.drawTexturedModalRect(x, y, 0, 14, l * 24, 16);
		
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	/**
	 * Renders a horizontal line that will be at x, y, it's width is 8, and
	 * height is 1.
	 * 
	 * @param x
	 *            The x position to render on
	 * @param y
	 *            The y position to render on
	 */
	public static void drawLine(float x, float y)
	{
		bind();
		RenderUtil.drawTexturedModalRect(x, y, 0, 8, 8, 1);
	}
	
	/**
	 * Renders a square of 8x8 pixels.
	 * 
	 * @param x
	 *            The x position to render on
	 * @param y
	 *            The y position to render on
	 * @param red
	 *            The amount of red. May be [0; 1]
	 * @param green
	 *            The amount of green. May be [0; 1]
	 * @param blue
	 *            The amount of blue. May be [0; 1]
	 */
	public static void drawSquare(float x, float y, float red, float green, float blue)
	{
		drawSquare(x, y, red, green, blue, 1);
	}
	
	/**
	 * Renders a square of 8x8 pixels.
	 * 
	 * @param x
	 *            The x position to render on
	 * @param y
	 *            The y position to render on
	 * @param red
	 *            The amount of red. May be [0; 1]
	 * @param green
	 *            The amount of green. May be [0; 1]
	 * @param blue
	 *            The amount of blue. May be [0; 1]
	 * @param alpha
	 *            The amount of alpha. May be [0; 1]
	 */
	public static void drawSquare(float x, float y, float red, float green, float blue, float alpha)
	{
		GL11.glColor4f(red, green, blue, alpha);
		drawSquare(x, y);
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	/**
	 * Renders a square of 8x8 pixels.
	 * 
	 * @param x
	 *            The x position to render on
	 * @param y
	 *            The y position to render o
	 */
	public static void drawSquare(float x, float y)
	{
		bind();
		RenderUtil.drawTexturedModalRect(x, y, 0, 0, 8, 8);
	}
	
	/**
	 * Renders an energy bar at x, y, with width and height.
	 * 
	 * @param x
	 *            The x position to render on
	 * @param y
	 *            The y position to render on
	 * @param w
	 *            The bar's width
	 * @param h
	 *            The bar's height
	 */
	public static void drawEnergy(float x, float y, float w, float h)
	{
		drawEnergy(x, y, w, h, true);
	}
	
	/**
	 * Renders an energy bar at x, y, with width and height.
	 * 
	 * @param x
	 *            The x position to render on
	 * @param y
	 *            The y position to render on
	 * @param w
	 *            The bar's width
	 * @param h
	 *            The bar's height
	 * @param animate
	 *            should this bar have an animation?
	 */
	public static void drawEnergy(float x, float y, float w, float h, boolean animate)
	{
		drawEnergy(x, y, w, h, animate ? (x > y ? EnumPowerAnimation.RIGHT : EnumPowerAnimation.UP) : EnumPowerAnimation.NONE);
	}
	
	/**
	 * Renders an energy bar at x, y, with width and height.
	 * 
	 * @param x
	 *            The x position to render on
	 * @param y
	 *            The y position to render on
	 * @param w
	 *            The bar's width
	 * @param h
	 *            The bar's height
	 * @param animate
	 *            the direction that the animation will be directed to
	 */
	public static void drawEnergy(float x, float y, float w, float h, EnumPowerAnimation animate)
	{
		bind();
		
		float time = (System.currentTimeMillis() % fullCycle) / (float) fullCycle;
		
		float animX = Math.abs(time * animate.x) * 8;
		float animY = Math.abs(time * animate.y) * 8;
		
		if(animate.x < 0)
			animX = 8 - animX;
		if(animate.y < 0)
			animY = 8 - animY;
		
		int fullX = (int) Math.floor(w / 8F);
		int fullY = (int) Math.floor(h / 8F);
		
		float leftX = w % 8;
		float leftY = h % 8;
		
		if(fullX >= 0)
			for(int cy = 0; cy < fullY || (cy == 0 && fullY == 0); ++cy)
				RenderUtil.drawTexturedModalRect(x + fullX * 8, y + cy * 8, 8 + animX, animY, leftX, 8);
			
		if(fullY >= 0)
			for(int cx = 0; cx < fullX || (cx == 0 && fullX == 0); ++cx)
				RenderUtil.drawTexturedModalRect(x + cx * 8, y + fullY * 8, 8 + animX, animY, 8, leftY);
			
		if(fullX > 0 && fullY > 0)
			RenderUtil.drawTexturedModalRect(x + fullX * 8, y + fullY * 8, 8 + animX, animY, leftX, leftY);
		
		for(int cx = 0; cx < fullX; ++cx)
			for(int cy = 0; cy < fullY; ++cy)
				RenderUtil.drawTexturedModalRect(x + cx * 8, y + cy * 8, 8 + animX, animY, 8, 8);
	}
	
	/**
	 * Binds to default widget textures.
	 */
	public static void bind()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
	}
	
	/**
	 * The power animation direction class.
	 */
	public enum EnumPowerAnimation
	{
		UP(0, 1), //
		DOWN(0, -1), //
		LEFT(1, 0), //
		RIGHT(-1, 0), //
		RIGHT_UP(-1, 1), //
		LEFT_DOWN(1, -1), //
		NONE(0, 0);
		
		public final int x, y;
		
		private EnumPowerAnimation(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
}