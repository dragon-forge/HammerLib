package org.zeith.hammerlib.client.utils;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

@FunctionalInterface
public interface IRenderTypeFactory
	extends Function<ResourceLocation, RenderType>
{
	@Override
	RenderType apply(ResourceLocation texture);
}