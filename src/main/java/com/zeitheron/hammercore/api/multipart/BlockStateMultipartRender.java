package com.zeitheron.hammercore.api.multipart;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.internal.blocks.multipart.TileMultipart;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

/**
 * Renders a block state in the place of {@link TileMultipart}
 */
public class BlockStateMultipartRender implements IMultipartRender<MultipartSignature>
{
	@Override
	public void renderMultipartAt(MultipartSignature signature, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		IBlockState state = signature.getState();
		if(state != null)
		{
			BufferBuilder buf = Tessellator.getInstance().getBuffer();
			buf.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(state, signature.pos, signature.world, buf);
			Tessellator.getInstance().draw();
		}
	}
}