package org.zeith.hammerlib.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import java.util.Map;
import java.util.function.BiFunction;

public class ReplacingMultiBufferSource
		implements MultiBufferSource
{
	protected final MultiBufferSource delegate;
	protected final BiFunction<MultiBufferSource, RenderType, VertexConsumer> replacer;
	
	public ReplacingMultiBufferSource(MultiBufferSource delegate, BiFunction<MultiBufferSource, RenderType, VertexConsumer> replacer)
	{
		this.delegate = delegate;
		this.replacer = replacer;
	}
	
	@Override
	public VertexConsumer getBuffer(RenderType renderType)
	{
		return replacer.apply(delegate, renderType);
	}
	
	public static ReplacingMultiBufferSource fromMap(MultiBufferSource src, Map<RenderType, RenderType> types)
	{
		return new ReplacingMultiBufferSource(src, (d, rt) -> d.getBuffer(types.getOrDefault(rt, rt)));
	}
}