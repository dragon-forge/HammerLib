package com.zeitheron.hammercore.client.render.world;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;

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
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		
		for(BlockPos pos : world.getAllPlacedStatePositions())
			brd.renderBlock(world.getBlockState(pos), pos, world, vb);
		
		if(scissorAvailable && shouldCut)
		{
			ScaledResolution sr = new ScaledResolution(mc);
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor((guiLeft + panel.getX()) * sr.getScaleFactor(), mc.displayHeight - (guiTop + panel.getY() + panel.getHeight()) * sr.getScaleFactor(), panel.getWidth() * sr.getScaleFactor(), panel.getHeight() * sr.getScaleFactor());
		}
		
		Tessellator.getInstance().draw();
		
		float p = Minecraft.getMinecraft().getRenderPartialTicks();
		for(BlockPos pos : world.tiles.toKeyArray())
			TileEntityRendererDispatcher.instance.render(world.getTileEntity(pos), pos.getX(), pos.getY(), pos.getZ(), p);
		
		if(scissorAvailable && shouldCut)
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		GlStateManager.popMatrix();
		GlStateManager.disableDepth();
	}
}