package com.pengu.hammercore.client.render.world;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;

public class VirtualWorldRenderer
{
	private static final boolean scissorAvailable = GLContext.getCapabilities().OpenGL20;
	
	public static int guiLeft = 0;
	public static int guiTop = 0;
	
	public static void renderVirtualWorld(VirtualWorld world, Minecraft mc, Rectangle panel, float rotX, float rotY, float zoom)
	{
		GlStateManager.enableDepth();
		boolean shouldCut = (panel.getHeight() == 0) && (panel.getWidth() == 0);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(panel.getX() + panel.getWidth() / 2, panel.getY() + panel.getHeight() / 2, 10.0F);
		
		double sc = Math.sqrt(zoom + 99.0D) - 9.0D;
		GlStateManager.scale(-sc, -sc, -sc);
		
		GlStateManager.translate(.5D, .5D, .5D);
		GlStateManager.rotate(rotX, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(rotY, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-.5D, -.5D, -.5D);
		
		BlockRendererDispatcher brd = mc.getBlockRendererDispatcher();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		BufferBuilder vb = Tessellator.getInstance().getBuffer();
		vb.begin(7, DefaultVertexFormats.BLOCK);
		
		for(BlockPos pos : world.getAllPlacedStatePositions())
			brd.renderBlock(world.getBlockState(pos), pos, world, vb);
		
		if(scissorAvailable && shouldCut)
		{
			ScaledResolution sr = new ScaledResolution(mc);
			GL11.glEnable(3089);
			GL11.glScissor((guiLeft + panel.getX()) * sr.getScaleFactor(), mc.displayHeight - (guiTop + panel.getY() + panel.getHeight()) * sr.getScaleFactor(), panel.getWidth() * sr.getScaleFactor(), panel.getHeight() * sr.getScaleFactor());
		}
		
		Tessellator.getInstance().draw();
		if(scissorAvailable && shouldCut)
			GL11.glDisable(3089);
		GlStateManager.popMatrix();
		GlStateManager.disableDepth();
	}
}