package org.zeith.hammerlib.compat.rubidium;

import com.mojang.blaze3d.vertex.*;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.ColorAttribute;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import org.lwjgl.system.MemoryStack;
import org.zeith.hammerlib.client.render.TintingVertexConsumer;

public class TintingVertexConsumerRB
		extends TintingVertexConsumer
		implements VertexBufferWriter
{
	protected final VertexBufferWriter writer;
	
	public TintingVertexConsumerRB(VertexConsumer parent, int rgba)
	{
		super(parent, rgba);
		writer = VertexBufferWriter.of(parent);
	}
	
	public TintingVertexConsumerRB(VertexConsumer parent, float r, float g, float b, float a)
	{
		super(parent, r, g, b, a);
		writer = VertexBufferWriter.of(parent);
	}
	
	@Override
	public void push(MemoryStack stack, long ptr, int count, VertexFormat format)
	{
		transform(ptr, count, format, r, g, b, a);
		writer.push(stack, ptr, count, format);
	}
	
	private static void transform(long ptr, int count, VertexFormat format, float r, float g, float b, float a)
	{
		long stride = format.getVertexSize();
		long offsetColor = format.getOffset(VertexFormatElement.COLOR);
		
		for(int vertexIndex = 0; vertexIndex < count; ++vertexIndex)
		{
			int color = ColorAttribute.get(ptr + offsetColor);
			
			ColorAttribute.set(ptr + offsetColor,
					(Math.round(((color >> 24) & 0xFF) * a) << 24)
							| (Math.round(((color >> 16) & 0xFF) * b) << 16)
							| (Math.round(((color >> 8) & 0xFF) * g) << 8)
							| Math.round(((color >> 0) & 0xFF) * r)
			);
			
			ptr += stride;
		}
	}
}