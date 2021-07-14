package org.zeith.hammerlib.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;

public class TESRBase<T extends TileEntity>
		extends TileEntityRenderer<T>
{
	public final ITESR<T> wrap;

	public TESRBase(TileEntityRendererDispatcher dispatcher, ITESR<T> wrap)
	{
		super(dispatcher);
		this.wrap = wrap;
	}

	@Override
	public void render(T tile, float partial, MatrixStack matrix, IRenderTypeBuffer buf, int lighting, int overlay)
	{
		wrap.render(tile, partial, matrix, buf, lighting, overlay, renderer);
	}

	@Override
	public boolean shouldRenderOffScreen(T tile)
	{
		return wrap.shouldRenderOffScreen(tile);
	}
}
