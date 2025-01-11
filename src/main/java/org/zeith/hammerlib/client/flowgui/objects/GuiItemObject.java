package org.zeith.hammerlib.client.flowgui.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.client.flowgui.util.Tooltip;
import org.zeith.hammerlib.client.screen.IAdvancedComponent;

import java.util.function.Supplier;

public class GuiItemObject
		extends GuiObject
		implements IAdvancedComponent
{
	private static final ResourceLocation SLOT_HIGHLIGHT_BACK_SPRITE = ResourceLocation.withDefaultNamespace("container/slot_highlight_back");
	private static final ResourceLocation SLOT_HIGHLIGHT_FRONT_SPRITE = ResourceLocation.withDefaultNamespace("container/slot_highlight_front");
	
	public Font font = Minecraft.getInstance().font;
	public Supplier<ItemStack> stack;
	public boolean hoverable;
	public boolean decorated = true;
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
	
	public GuiItemObject decorated(boolean decorated)
	{
		this.decorated = decorated;
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
		
		isMouseOver = pos.isMouseWithin(this);
		
		var g = gfx.gfx();
		
		if(hoverable && isMouseOver)
			g.blitSprite(RenderType::guiTextured, SLOT_HIGHLIGHT_BACK_SPRITE, -4, -4, 24, 24);
		
		gfx.renderItem(stack, 0, 0, seed);
		if(decorated) gfx.renderItemDecorations(font, stack, 0, 0);
		
		if(hoverable && isMouseOver)
		{
			g.blitSprite(RenderType::guiTexturedOverlay, SLOT_HIGHLIGHT_FRONT_SPRITE, -4, -4, 24, 24);
			drawTooltip(gfx, pos, font, Tooltip.ofItem(stack));
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