package org.zeith.hammerlib.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;

public interface ITESR<T extends TileEntity>
{
	void render(T tile, float partial, MatrixStack matrix, IRenderTypeBuffer buf, int lighting, int overlay, TileEntityRendererDispatcher renderer);

	default boolean shouldRenderOffScreen(T tile)
	{
		return false;
	}
}