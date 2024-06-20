package org.zeith.hammerlib.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public interface IBESR<T extends BlockEntity>
{
	void render(T entity, float partial, PoseStack matrix, MultiBufferSource buf, int lighting, int overlay);
	
	default boolean shouldRenderOffScreen(T tile)
	{
		return false;
	}
	
	default int getViewDistance()
	{
		return 64;
	}
	
	default AABB getRenderBoundingBox(T blockEntity)
	{
		return new AABB(blockEntity.getBlockPos());
	}
	
	default boolean shouldRender(T entity, Vec3 camera)
	{
		return Vec3.atCenterOf(entity.getBlockPos()).closerThan(camera, this.getViewDistance());
	}
	
	/**
	 * An infinite rendering implementation to force this tile to get rendered regardless of distance, as long as the tile exists on the client side.
	 */
	interface Infinite<T extends BlockEntity>
			extends IBESR<T>
	{
		@Override
		default AABB getRenderBoundingBox(T blockEntity)
		{
			return AABB.INFINITE;
		}
		
		@Override
		default int getViewDistance()
		{
			return Integer.MAX_VALUE;
		}
		
		// By making it render offscreen we remove the need to check culling with Frustum, potentially saving a bit of performance
		@Override
		default boolean shouldRenderOffScreen(T tile)
		{
			return true;
		}
	}
}