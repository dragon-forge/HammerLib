package org.zeith.hammerlib.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public record TESRBase<T extends BlockEntity>(IBESR<T> wrap)
		implements BlockEntityRenderer<T>
{
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
	
	@Override
	public int getViewDistance()
	{
		return wrap.getViewDistance();
	}
	
	@Override
	public boolean shouldRender(T entity, Vec3 pos)
	{
		return wrap.shouldRender(entity, pos);
	}
}
