package org.zeith.hammerlib.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IBESR<T extends BlockEntity>
{
	void render(T entity, float partial, PoseStack matrix, MultiBufferSource buf, int lighting, int overlay);

	default boolean shouldRenderOffScreen(T tile)
	{
		return false;
	}
}