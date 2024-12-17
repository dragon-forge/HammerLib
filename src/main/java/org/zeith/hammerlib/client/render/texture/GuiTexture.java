package org.zeith.hammerlib.client.render.texture;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.client.flowgui.Graphics;

import java.util.function.Function;

public record GuiTexture(Function<ResourceLocation, RenderType> renderTypeGetter, ResourceLocation texture)
{
	public static GuiTexture of(Function<ResourceLocation, RenderType> renderTypeGetter, ResourceLocation texture)
	{
		return new GuiTexture(renderTypeGetter, texture);
	}
	
	public static GuiTexture of(ResourceLocation texture)
	{
		return of(RenderType::guiTextured, texture);
	}
	
	public TextureGraphics with(GuiGraphics gfx)
	{
		return new TextureGraphics(this, gfx, new GraphicsState());
	}
	
	public TextureGraphics with(Graphics gfx)
	{
		return new TextureGraphics(this, gfx.gfx(), new GraphicsState());
	}
	
	public RenderType type()
	{
		return renderTypeGetter.apply(texture);
	}
}