package com.zeitheron.hammercore.api.multipart;

import com.zeitheron.hammercore.HammerCore;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.ResourceLocation;

/**
 * Provides rendering of {@link MP} extends {@link MultipartSignature}. Use
 * {@code MultipartRenderingRegistry} to bind it to {@link MultipartSignature}
 */
public interface IMultipartRender<MP extends MultipartSignature>
{
	void renderMultipartAt(MP signature, double x, double y, double z, float partialTicks, ResourceLocation destroyStage);
	
	@Deprecated
	default void renderMultipartAt(MP signature, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, BufferBuilder buffer)
	{
	}
	
	default void bindTexture(ResourceLocation location)
	{
		HammerCore.renderProxy.bindTexture(location);
	}
}