package org.zeith.hammerlib.client.flowgui.objects;

import net.minecraft.util.ARGB;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.client.render.texture.GuiTexture;
import org.zeith.hammerlib.util.java.DirectStorage;

public class GuiImageObject
		extends GuiObject
{
	public GuiTexture tex;
	public float uOffset, vOffset;
	public float imgWidth, imgHeight, txWidth, txHeight;
	
	public float alpha = 1F;
	
	public final DirectStorage<Float> textureUOffset = DirectStorage.create(p -> uOffset = p, () -> uOffset);
	public final DirectStorage<Float> textureVOffset = DirectStorage.create(p -> vOffset = p, () -> vOffset);
	
	public final DirectStorage<Float> imageWidth = DirectStorage.create(p -> imgWidth = p, () -> imgWidth);
	public final DirectStorage<Float> imageHeight = DirectStorage.create(p -> imgHeight = p, () -> imgHeight);
	
	public final DirectStorage<Float> fileWidth = DirectStorage.create(p -> txWidth = p, () -> txWidth);
	public final DirectStorage<Float> fileHeight = DirectStorage.create(p -> txHeight = p, () -> txHeight);
	
	public GuiImageObject(
			String name,
			GuiTexture tex,
			float uOffset, float vOffset,
			float imgWidth, float imgHeight,
			float txWidth, float txHeight
	)
	{
		super(name);
		this.tex = tex;
		this.uOffset = uOffset;
		this.vOffset = vOffset;
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		this.txWidth = txWidth;
		this.txHeight = txHeight;
		size(imgWidth, imgHeight);
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		var drawer = tex.with(gfx);
		drawer.state().color = ARGB.white(alpha);
		drawer.blitSegment(0, 0, uOffset, vOffset, imgWidth, imgHeight, txWidth, txHeight);
	}
}