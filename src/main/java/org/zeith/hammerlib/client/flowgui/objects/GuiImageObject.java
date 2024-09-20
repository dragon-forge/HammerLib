package org.zeith.hammerlib.client.flowgui.objects;

import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.client.flowgui.*;

public class GuiImageObject
		extends GuiObject
{
	public ResourceLocation tex;
	public float uOffset, vOffset;
	public int width, height, txWidth, txHeight;
	
	public GuiImageObject(
			String name,
			ResourceLocation tex,
			float uOffset, float vOffset,
			int width, int height,
			int txWidth, int txHeight
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
		gfx.blit(tex, 0, 0, uOffset, vOffset, width, height, txWidth, txHeight);
	}
}