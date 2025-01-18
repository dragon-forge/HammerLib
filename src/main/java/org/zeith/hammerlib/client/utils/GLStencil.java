package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

import static com.mojang.blaze3d.systems.RenderSystem.*;

public class GLStencil
		implements AutoCloseable
{
	private static final GLStencil INSTANCE = new GLStencil();
	
	public static GLStencil of()
	{
		INSTANCE.initializeStencil();
		return INSTANCE;
	}
	
	public static boolean isEnabled()
	{
		return Minecraft.getInstance().getMainRenderTarget().useStencil;
	}
	
	private GLStencil() {}
	
	/**
	 * Initializes the stencil buffer.
	 */
	private void initializeStencil()
	{
		clearStencil(0);
		stencilMask(0xFF);
		clear(GL11.GL_STENCIL_BUFFER_BIT);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
	}
	
	/**
	 * Populates the stencil buffer with the shape drawn within the provided render.
	 *
	 * @param render
	 * 		Code that draws the shape to populate the stencil buffer.
	 */
	public void populateStencil(Runnable render)
	{
		colorMask(false, false, false, false);
		
		stencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
		stencilMask(0xFF);
		stencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
		
		render.run();
		
		colorMask(true, true, true, true);
		stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
	}
	
	/**
	 * Populates the stencil buffer with the shape drawn within the provided render.
	 *
	 * @param renderer
	 * 		Code that draws the shape to populate the stencil buffer.
	 */
	public void populateStencil(Consumer<VertexConsumer> renderer)
	{
		populateStencil(() ->
		{
			var tess = Tesselator.getInstance();
			var b = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
			renderer.accept(b);
			var mesh = b.build();
			if(mesh != null) BufferUploader.drawWithShader(mesh);
		});
	}
	
	/**
	 * Populates the stencil buffer with the shape drawn within the provided render.
	 *
	 * @param src
	 * 		The source of vertex transformations
	 * @param renderer
	 * 		Code that draws the shape to populate the stencil buffer.
	 */
	public void populateStencil(GuiGraphics src, Consumer<GuiGraphics> renderer)
	{
		src.flush();
		populateStencil(() ->
		{
			var mc = Minecraft.getInstance();
			GuiGraphics gfx = new GuiGraphics(mc, mc.renderBuffers().crumblingBufferSource());
			PoseStack p = gfx.pose();
			p.mulPose(src.pose().last().pose());
			renderer.accept(gfx);
			gfx.flush();
		});
	}
	
	/**
	 * Renders content with the stencil filter applied.
	 *
	 * @param job
	 * 		Task that renders the content with the stencil.
	 */
	public void renderWithStencil(Runnable job)
	{
		colorMask(true, true, true, true);
		stencilFunc(GL11.GL_EQUAL, 1, 0xFF);
		job.run();
		stencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
	}
	
	@Override
	public void close()
	{
		assertOnRenderThread();
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}
}