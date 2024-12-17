package org.zeith.hammerlib.client.flowgui.objects;

import net.minecraft.util.ARGB;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.client.render.texture.GuiTexture;

public class GuiImageObject
		extends GuiObject
{
	public GuiTexture tex;
	public float uOffset, vOffset;
	public float width, height, txWidth, txHeight;
	
	public float alpha = 1F;
	
	public GuiImageObject(
			String name,
			GuiTexture tex,
			float uOffset, float vOffset,
			float width, float height,
			float txWidth, float txHeight
	)
	{
		super(name);
		this.tex = tex;
		this.uOffset = uOffset;
		this.vOffset = vOffset;
		this.width = width;
		this.height = height;
		this.txWidth = txWidth;
		this.txHeight = txHeight;
		size(width, height);
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		var drawer = tex.with(gfx);
		drawer.state().color = ARGB.white(alpha);
		drawer.blitSegment(0, 0, uOffset, vOffset, width, height, txWidth, txHeight);
	}
}