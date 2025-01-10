package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.compat.jei.IJeiPluginHL;
import org.zeith.hammerlib.compat.jei.JeiKeyRole;

import java.util.function.Supplier;

public class GuiItemObject
		extends GuiObject
{
	private static final ResourceLocation SLOT_HIGHLIGHT_BACK_SPRITE = ResourceLocation.withDefaultNamespace("container/slot_highlight_back");
	private static final ResourceLocation SLOT_HIGHLIGHT_FRONT_SPRITE = ResourceLocation.withDefaultNamespace("container/slot_highlight_front");
	
	public Supplier<ItemStack> stack;
	public boolean hoverable;
	public boolean jeiable;
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
	
	public GuiItemObject jeiable(boolean jeiable)
	{
		this.jeiable = jeiable;
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
		
		if(hoverable && isMouseOver)
		{
			g.blitSprite(RenderType::guiTexturedOverlay, SLOT_HIGHLIGHT_FRONT_SPRITE, -4, -4, 24, 24);
			
			var ps = g.pose();
			ps.pushPose();
			ps.mulPose(new Matrix4f(ps.last().pose()).invert()); // Untransform from component to screen space
			{
				var gp = pos.globalPos();
				float x = gp.x(), y = gp.y();
				ps.translate(x % 1F, y % 1F, 0);
				g.renderTooltip(Minecraft.getInstance().font, stack, (int) x, (int) y);
			}
			ps.popPose();
		}
	}
	
	@Override
	protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers)
	{
		if(jeiable && isMouseOver)
		{
			InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
			var jei = IJeiPluginHL.get();
			if(jei != null)
			{
				JeiKeyRole role = jei.getRoleForKey(key);
				if(role != null) role.sendToJei(stack.get());
			}
		}
		return super.onKeyPressed(keyCode, scanCode, modifiers);
	}
}