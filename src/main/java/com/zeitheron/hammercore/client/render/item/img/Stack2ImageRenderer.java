package com.zeitheron.hammercore.client.render.item.img;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.zeitheron.hammercore.lib.zlib.tuple.ThreeTuple;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class Stack2ImageRenderer
{
	public static final Stack2ImageRenderer INSTANCE = new Stack2ImageRenderer();
	
	private List<ThreeTuple<ItemStack, Integer, Consumer<BufferedImage>>> queue = new ArrayList<>();
	
	/**
	 * Adds this element to render queue. Once it's done, the <code>done</code>
	 * will be called with the image.
	 * 
	 * @param stack
	 *            The item to render.
	 * @param size
	 *            The size of an output image.
	 * @param done
	 *            The callback for when the image is rendered.
	 */
	public static void queueRenderer(ItemStack stack, int size, Consumer<BufferedImage> done)
	{
		INSTANCE.queue.add(new ThreeTuple<>(stack, size, done));
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onFrameStart(RenderTickEvent e)
	{
		if(e.phase == Phase.START)
		{
			if(!queue.isEmpty())
			{
				ThreeTuple<ItemStack, Integer, Consumer<BufferedImage>> data = queue.remove(0);
				if(!data.get1().isEmpty() && data.get2() > 0)
				{
					BufferedImage i = bulkRender(data.get1(), data.get2());
					Consumer<BufferedImage> callback = data.get3();
					if(callback != null)
						callback.accept(i);
				}
			}
		}
	}
	
	/**
	 * DO NOT USE UNLESS IN {@link RenderTickEvent}'s
	 * {@link TickEvent.Phase#START} PHASE!
	 */
	public BufferedImage bulkRender(ItemStack is, int size)
	{
		long lastUpdate = 0;
		setUpRenderState(size);
		BufferedImage img = render(is);
		tearDownRenderState();
		return img;
	}
	
	private BufferedImage render(ItemStack is)
	{
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.pushMatrix();
		GlStateManager.clearColor(0, 0, 0, 0);
		GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		mc.getRenderItem().renderItemAndEffectIntoGUI(is, 0, 0);
		GlStateManager.popMatrix();
		try
		{
			return createFlipped(readPixels(size, size));
		} catch(InterruptedException e)
		{
			return null;
		}
	}
	
	private int size;
	private float oldZLevel;
	
	private void setUpRenderState(int desiredSize)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution res = new ScaledResolution(mc);
		size = Math.min(Math.min(mc.displayHeight, mc.displayWidth), desiredSize);
		mc.entityRenderer.setupOverlayRendering();
		RenderHelper.enableGUIStandardItemLighting();
		float scale = size / (16f * res.getScaleFactor());
		GlStateManager.translate(0, 0, -(scale * 100));
		GlStateManager.scale(scale, scale, scale);
		oldZLevel = mc.getRenderItem().zLevel;
		mc.getRenderItem().zLevel = -50;
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableColorMaterial();
		GlStateManager.enableDepth();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableAlpha();
	}
	
	private void tearDownRenderState()
	{
		GlStateManager.disableLighting();
		GlStateManager.disableColorMaterial();
		GlStateManager.disableDepth();
		GlStateManager.disableBlend();
		Minecraft.getMinecraft().getRenderItem().zLevel = oldZLevel;
	}
	
	public BufferedImage readPixels(int width, int height) throws InterruptedException
	{
		ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);
		GL11.glReadPixels(0, Minecraft.getMinecraft().displayHeight - height, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buf);
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] pixels = new int[width * height];
		buf.asIntBuffer().get(pixels);
		img.setRGB(0, 0, width, height, pixels, 0, width);
		return img;
	}
	
	private static BufferedImage createFlipped(BufferedImage image)
	{
		AffineTransform at = new AffineTransform();
		at.concatenate(AffineTransform.getScaleInstance(1, -1));
		at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
		return createTransformed(image, at);
	}
	
	private static BufferedImage createTransformed(BufferedImage image, AffineTransform at)
	{
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.transform(at);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}
}