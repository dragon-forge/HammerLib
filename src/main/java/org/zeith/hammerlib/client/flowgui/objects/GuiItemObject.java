package org.zeith.hammerlib.client.flowgui.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.client.flowgui.util.Tooltip;
import org.zeith.hammerlib.client.screen.IAdvancedComponent;

import java.util.function.Supplier;

public class GuiItemObject
		extends GuiObject
		implements IAdvancedComponent
{
	public Supplier<ItemStack> stack;
	public boolean hoverable;
	public boolean provideIngredient;
	public int seed = 0;
	
	public boolean isMouseOver;
	
	public GuiItemObject(String name, Supplier<ItemStack> stack)
	{
		super(name);
		size(16, 16);
		this.stack = stack;
	}
	
	public GuiItemObject seed(int seed)
	{
		this.seed = seed;
		return this;
	}
	
	public GuiItemObject hoverable(boolean hoverable)
	{
		this.hoverable = hoverable;
		return this;
	}
	
	public GuiItemObject provideIngredient(boolean provideIngredient)
	{
		this.provideIngredient = provideIngredient;
		return this;
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		var stack = this.stack.get();
		if(stack.isEmpty()) return;
		
		gfx.renderItem(stack, 0, 0, seed);
		
		isMouseOver = pos.isMouseWithin(this);
		
		if(hoverable && isMouseOver)
		{
			var g = gfx.gfx();
			AbstractContainerScreen.renderSlotHighlight(g, 0, 0, 0);
			drawTooltip(gfx, pos, Minecraft.getInstance().font, Tooltip.ofItem(stack));
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