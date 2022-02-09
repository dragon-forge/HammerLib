package org.zeith.hammerlib.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TESRBase<T extends BlockEntity>
		implements BlockEntityRenderer<T>
{
	public final IBESR<T> wrap;

	public TESRBase(IBESR<T> wrap)
	{
		this.wrap = wrap;
	}

	@Override
	public void render(T tile, float partial, PoseStack matrix, MultiBufferSource buf, int lighting, int overlay)
	{
		wrap.render(tile, partial, matrix, buf, lighting, overlay);
	}

	@Override
	public boolean shouldRenderOffScreen(T tile)
	{
		return wrap.shouldRenderOffScreen(tile);
	}
}
