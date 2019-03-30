package com.zeitheron.hammercore.client.render.world;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.zeitheron.hammercore.client.utils.RenderUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;

public class VirtualWorldRender
{
	private static final FloatBuffer MODELVIEW_MATRIX_BUFFER = ByteBuffer.allocateDirect(16 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
	private static final FloatBuffer PROJECTION_MATRIX_BUFFER = ByteBuffer.allocateDirect(16 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
	private static final IntBuffer VIEWPORT_BUFFER = ByteBuffer.allocateDirect(16 * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
	private static final FloatBuffer PIXEL_DEPTH_BUFFER = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asFloatBuffer();
	private static final FloatBuffer OBJECT_POS_BUFFER = ByteBuffer.allocateDirect(3 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
	
	public final VirtualWorld world;
	
	private Predicate<BlockPos> renderFilter;
	private BlockPos lastHitBlock;
	
	public Consumer<VirtualWorldRender> preRender = null;
	public Consumer<VirtualWorldRender> postRender = null;
	public BiConsumer<VirtualWorldRender, BlockPos> renderBlockHit = null;
	
	public VirtualWorldRender(VirtualWorld world)
	{
		this.world = world;
	}
	
	public BlockPos getLastHitBlock()
	{
		return lastHitBlock;
	}
	
	public void render(int x, int y, int width, int height, int backgroundColor)
	{
		Vec2f mousePosition = setupCamera(x, y, width, height, backgroundColor);
		if(preRender != null)
			preRender.accept(this);
		Minecraft minecraft = Minecraft.getMinecraft();
		minecraft.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		BlockRendererDispatcher dispatcher = minecraft.getBlockRendererDispatcher();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		for(long l : world.states.VALUES.keySet())
		{
			BlockPos pos = BlockPos.fromLong(l);
			if(renderFilter != null && !renderFilter.test(pos))
				continue;
			IBlockState blockState = world.getBlockState(pos);
			dispatcher.renderBlock(blockState, pos, world, buffer);
		}
		tessellator.draw();
		for(long l : world.tiles.VALUES.keySet())
		{
			BlockPos pos = BlockPos.fromLong(l);
			if(renderFilter != null && !renderFilter.test(pos))
				continue;
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null)
				TileEntityRendererDispatcher.instance.render(tile, Minecraft.getMinecraft().getRenderPartialTicks(), -1);
		}
		if(mousePosition != null)
			this.lastHitBlock = handleMouseHit(mousePosition);
		else
			this.lastHitBlock = null;
		if(lastHitBlock != null && renderBlockHit != null)
			renderBlockHit.accept(this, lastHitBlock);
		if(postRender != null)
			postRender.accept(this);
		resetCamera();
	}
	
	private BlockPos handleMouseHit(Vec2f mousePosition)
	{
		GL11.glReadPixels((int) mousePosition.x, (int) mousePosition.y, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, PIXEL_DEPTH_BUFFER);
		PIXEL_DEPTH_BUFFER.rewind();
		float pixelDepth = PIXEL_DEPTH_BUFFER.get();
		PIXEL_DEPTH_BUFFER.rewind();
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, MODELVIEW_MATRIX_BUFFER);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, PROJECTION_MATRIX_BUFFER);
		GL11.glGetInteger(GL11.GL_VIEWPORT, VIEWPORT_BUFFER);
		MODELVIEW_MATRIX_BUFFER.rewind();
		PROJECTION_MATRIX_BUFFER.rewind();
		VIEWPORT_BUFFER.rewind();
		GLU.gluUnProject(mousePosition.x, mousePosition.y, pixelDepth, MODELVIEW_MATRIX_BUFFER, PROJECTION_MATRIX_BUFFER, VIEWPORT_BUFFER, OBJECT_POS_BUFFER);
		VIEWPORT_BUFFER.rewind();
		PROJECTION_MATRIX_BUFFER.rewind();
		MODELVIEW_MATRIX_BUFFER.rewind();
		OBJECT_POS_BUFFER.rewind();
		float posX = OBJECT_POS_BUFFER.get();
		float posY = OBJECT_POS_BUFFER.get();
		float posZ = OBJECT_POS_BUFFER.get();
		OBJECT_POS_BUFFER.rewind();
		if(posY < -100)
			return null;
		BlockPos pos = new BlockPos(posX, posY, posZ);
		if(world.isAirBlock(pos))
			for(EnumFacing offset : EnumFacing.VALUES)
			{
				BlockPos relative = pos.offset(offset);
				if(world.isAirBlock(relative))
					continue;
				pos = relative;
				break;
			}
		if(world.isAirBlock(pos))
			return null;
		return pos;
	}
	
	public static Vec2f setupCamera(int x, int y, int width, int height, int skyColor)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution resolution = new ScaledResolution(mc);
		
		GlStateManager.pushAttrib();
		mc.entityRenderer.disableLightmap();
		GlStateManager.disableLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableBlend();
		
		int windowWidth = (int) (width / (resolution.getScaledWidth() * 1.0) * mc.displayWidth);
		int windowHeight = (int) (height / (resolution.getScaledHeight() * 1.0) * mc.displayHeight);
		
		int windowX = (int) (x / (resolution.getScaledWidth() * 1.0) * mc.displayWidth);
		int windowY = mc.displayHeight - (int) (y / (resolution.getScaledHeight() * 1.0) * mc.displayHeight) - windowHeight;
		
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();
		Vec2f mousePosition = null;
		
		if(mouseX >= windowX && mouseY >= windowY && mouseX - windowX < windowWidth && mouseY - windowY < windowHeight)
			mousePosition = new Vec2f(mouseX, mouseY);
		
		GlStateManager.viewport(windowX, windowY, windowWidth, windowHeight);
		
		if(skyColor >= 0)
		{
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor(windowX, windowY, windowWidth, windowHeight);
			RenderUtil.setGlClearColorFromInt(skyColor, 255);
			GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
		
		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		
		float aspectRatio = width / (height * 1.0f);
		GLU.gluPerspective(60.0f, aspectRatio, 0.1f, 10000.0f);
		
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GLU.gluLookAt(0.0f, 0.0f, -10.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		
		return mousePosition;
	}
	
	public static void resetCamera()
	{
		Minecraft minecraft = Minecraft.getMinecraft();
		GlStateManager.viewport(0, 0, minecraft.displayWidth, minecraft.displayHeight);
		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
}