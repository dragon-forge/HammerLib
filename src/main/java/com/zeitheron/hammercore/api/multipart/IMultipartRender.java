package com.zeitheron.hammercore.api.multipart;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.client.render.tesr.TESR;

import net.minecraft.util.ResourceLocation;

/**
 * Provides rendering of {@link MP} extends {@link MultipartSignature}. Use
 * {@code MultipartRenderingRegistry} to bind it to {@link MultipartSignature}
 */
public interface IMultipartRender<MP extends MultipartSignature>
{
	/**
	 * Handles rendering for multipart instances. Similar to {@link TESR}
	 */
	void renderMultipartAt(MP signature, double x, double y, double z, float partialTicks, ResourceLocation destroyStage);
	
	default void bindTexture(ResourceLocation location)
	{
		HammerCore.renderProxy.bindTexture(location);
	}
}