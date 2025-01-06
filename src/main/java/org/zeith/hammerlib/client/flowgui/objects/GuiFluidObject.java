package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix4f;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.client.flowgui.util.Tooltip;
import org.zeith.hammerlib.client.render.FluidRendererHelper;
import org.zeith.hammerlib.client.screen.IAdvancedComponent;
import org.zeith.hammerlib.client.utils.FluidTextureType;
import org.zeith.hammerlib.util.mcf.fluid.FluidHelper;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class GuiFluidObject
		extends GuiObject
		implements IAdvancedComponent
{
	public FluidTextureType textureType = FluidTextureType.STILL;
	public Supplier<FluidStack> stack;
	public boolean hoverable;
	public boolean provideIngredient;
	public float fill;
	
	public boolean isMouseOver;
	
	public Integer capacity;
	
	public GuiFluidObject(String name, Supplier<FluidStack> stack)
	{
		super(name);
		size(16, 16);
		this.stack = stack;
	}
	
	public GuiFluidObject capacity(Integer capacity)
	{
		this.capacity = capacity;
		return this;
	}
	
	public GuiFluidObject fill(float fill)
	{
		this.fill = fill;
		return this;
	}
	
	public GuiFluidObject hoverable(boolean hoverable)
	{
		this.hoverable = hoverable;
		return this;
	}
	
	public GuiFluidObject provideIngredient(boolean provideIngredient)
	{
		this.provideIngredient = provideIngredient;
		return this;
	}
	
	public GuiFluidObject textureType(FluidTextureType textureType)
	{
		this.textureType = textureType;
		return this;
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		var stack = this.stack.get();
		if(stack.isEmpty()) return;
		
		PoseStack pose = gfx.pose();
		FluidRendererHelper.renderFluidInGui(gfx.gfx(), stack, textureType, fill, 0, 0, width, height);
		
		isMouseOver = pos.isMouseWithin(this);
		if(hoverable && isMouseOver)
		{
			var g = gfx.gfx();
			pose.pushPose();
			pose.scale(width, height, width);
			g.fillGradient(RenderType.guiOverlay(), 0, 0, 1, 1, -2130706433, -2130706433, 0);
			pose.popPose();
			
			var mc = Minecraft.getInstance();
			drawTooltip(gfx, pos, mc.font, Tooltip.ofFluid(stack, capacity != null, capacity != null ? capacity.intValue() : 0));
		}
	}
	
	@Override
	public Object getIngredientUnderMouse(double mouseX, double mouseY)
	{
		if(provideIngredient && mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height)
			return stack.get();
		return null;
	}
}