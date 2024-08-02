package com.zeitheron.hammercore.client.render.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
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
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
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
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder vb = tess.getBuffer();
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		
		for(BlockRenderLayer layer : BlockRenderLayer.values())
		{
			for(BlockPos pos : world.tiles.VALUES.keySet())
			{
				IBlockState iblockstate = world.getBlockState(pos);
				Block block = iblockstate.getBlock();
				
				if(!block.canRenderInLayer(iblockstate, layer)) continue;
				net.minecraftforge.client.ForgeHooksClient.setRenderLayer(layer);
				
				if(block.getDefaultState().getRenderType() != EnumBlockRenderType.INVISIBLE)
				{
					brd.renderBlock(iblockstate, pos, world, vb);
				}
			}
		}
		net.minecraftforge.client.ForgeHooksClient.setRenderLayer(null);
		
		if(scissorAvailable && shouldCut)
		{
			ScaledResolution sr = new ScaledResolution(mc);
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor((guiLeft + panel.getX()) * sr.getScaleFactor(), mc.displayHeight - (guiTop + panel.getY() + panel.getHeight()) * sr.getScaleFactor(), panel.getWidth() * sr.getScaleFactor(), panel.getHeight() * sr.getScaleFactor());
		}
		
		tess.draw();
		
		float p = mc.getRenderPartialTicks();
		for(BlockPos pos : world.tiles.VALUES.keySet())
		{
			TileEntity te = world.getTileEntity(pos);
			if(te == null) continue;
			TileEntityRendererDispatcher.instance.render(te, pos.getX(), pos.getY(), pos.getZ(), p);
		}
		
		if(scissorAvailable && shouldCut)
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		GlStateManager.popMatrix();
		GlStateManager.disableDepth();
	}
}