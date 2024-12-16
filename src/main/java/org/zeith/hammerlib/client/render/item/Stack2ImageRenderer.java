package org.zeith.hammerlib.client.render.item;

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.compat.jei.IJeiPluginHL;
import org.zeith.hammerlib.core.ConfigHL;
import org.zeith.hammerlib.proxy.HLClientProxy;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

@EventBusSubscriber(Dist.CLIENT)
public class Stack2ImageRenderer
{
	private static final List<RenderQueueItem> QUEUE = new ArrayList<>();
	
	private record RenderQueueItem(Component work, int resolution, ItemStack stack, Consumer<NativeImage> finishCallback)
	{
	}
	
	/**
	 * Adds this element to render queue. Once it's done, the <code>done</code>
	 * will be called with the image.
	 *
	 * @param stack
	 * 		The item to render.
	 * @param size
	 * 		The size of an output image.
	 * @param targetIn
	 * 		The File to where the image is rendered.
	 */
	public static void queueRenderer(Component type, ItemStack stack, int size, File targetIn)
	{
		String origin = targetIn.getAbsolutePath();
		int itr = 1;
		while(targetIn.isFile())
		{
			int dot = origin.lastIndexOf(".");
			if(dot >= 0)
				targetIn = new File(origin.substring(0, dot) + " (" + (++itr) + ")." + origin.substring(dot + 1));
			else
				targetIn = new File(origin + " (" + (++itr) + ")");
		}
		
		final File target = targetIn;
		
		renderItemStack(type, size, stack, image ->
		{
			Util.ioPool().execute(() ->
			{
				try
				{
					Files.createDirectories(target.toPath().getParent());
					image.writeToFile(target);
					image.close();
					
					var buffered = ImageIO.read(target);
					RecenterFilter.recenter(buffered).thenAccept(result ->
					{
						Util.ioPool().execute(() ->
						{
							try
							{
								ImageIO.write(result, "png", target);
							} catch(Exception exception)
							{
								HammerLib.LOG.warn("Couldn't save render", exception);
							}
						});
					});
				} catch(Exception exception)
				{
					HammerLib.LOG.warn("Couldn't save render", exception);
				}
			});
		});
	}
	
	private record ItemWithData(Item item, DataComponentPatch data)
	{
		public ItemStack stack()
		{
			ItemStack stack = new ItemStack(item);
			stack.applyComponents(data);
			return stack;
		}
	}
	
	public static void renderItem(Component type, ItemStack stack, int size)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-hh.mm.ss");
		ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
		var faild = new File(HLConstants.MOD_ID, "renderers" + File.separator + rl.getNamespace());
		var fl = new File(faild, (rl.getPath() + "-" + sdf.format(Date.from(Instant.now())) + ".png").replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
		queueRenderer(type, stack, size, fl);
	}
	
	public static void renderAll(int size)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-hh.mm.ss");
		File faild = new File(HLConstants.MOD_ID, "renderers" + File.separator + "all-" + sdf.format(Date.from(Instant.now())));
		
		CreativeModeTabRegistry.getSortedCreativeModeTabs()
				.stream()
				.flatMap(tab -> tab.getDisplayItems().stream())
				.map(stack -> new ItemWithData(stack.getItem(), stack.getComponentsPatch()))
				.distinct()
				.forEach(s ->
				{
					ResourceLocation rl = BuiltInRegistries.ITEM.getKey(s.item());
					var suf = "";
					if(s.data() != null && !s.data().isEmpty()) suf = s.data().toString();
					var fl = new File(faild, rl.getNamespace() + File.separator + (rl.getPath() + suf + ".png").replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
					queueRenderer(Component.literal("Everything"), s.stack(), size, fl);
				});
	}
	
	public static void renderTab(ResourceLocation tabId, CreativeModeTab tab, int size)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-hh.mm.ss");
		File faild = new File(HLConstants.MOD_ID, "renderers" + File.separator + tabId.getNamespace() + "_" + tabId.getPath() + "-" + sdf.format(Date.from(Instant.now())));
		tab.getDisplayItems()
				.stream()
				.distinct()
				.map(stack -> new ItemWithData(stack.getItem(), stack.getComponentsPatch()))
				.forEach(s ->
				{
					ResourceLocation rl = BuiltInRegistries.ITEM.getKey(s.item());
					var suf = "";
					if(s.data() != null && !s.data().isEmpty()) suf = s.data().toString();
					var fl = new File(faild, (rl.getPath() + suf + ".png").replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
					queueRenderer(Component.literal("Tab " + tabId), s.stack(), size, fl);
				});
	}
	
	public static void renderMod(String modid, int size)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-hh.mm.ss");
		File faild = new File(HLConstants.MOD_ID, "renderers" + File.separator + modid + "-" + sdf.format(Date.from(Instant.now())));
		
		CreativeModeTabRegistry.getSortedCreativeModeTabs()
				.stream()
				.flatMap(tab -> tab.getDisplayItems().stream())
				.filter(item -> BuiltInRegistries.ITEM.getKey(item.getItem()).getNamespace().equals(modid))
				.map(stack -> new ItemWithData(stack.getItem(), stack.getComponentsPatch()))
				.distinct()
				.forEach(s ->
				{
					ResourceLocation rl = BuiltInRegistries.ITEM.getKey(s.item());
					var suf = "";
					if(s.data() != null && !s.data().isEmpty()) suf = s.data().toString();
					var fl = new File(faild, (rl.getPath() + suf + ".png").replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
					queueRenderer(Component.literal("Mod " + modid), s.stack(), size, fl);
				});
	}
	
	public static synchronized void renderItemStack(Component type, int resolution, ItemStack stack, Consumer<NativeImage> finishCallback)
	{
		QUEUE.add(new RenderQueueItem(type, resolution, stack, finishCallback));
	}
	
	static RenderTarget target;
	static long lastTargetUse;
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onFrameStart(ClientTickEvent.Pre eventThatWeDoNotCareMuchAbout)
	{
		if(HLClientProxy.RENDER_GUI_ITEM.consumeClick())
		{
			var mc = Minecraft.getInstance();
			
			Cast.optionally(mc.screen, AbstractContainerScreen.class)
					.map(AbstractContainerScreen::getSlotUnderMouse)
					.map(Slot::getItem)
					.or(() -> IJeiPluginHL.get().getIngredientUnderMouseJEI(ItemStack.class))
					.ifPresent(stack ->
					{
						int res = Mth.clamp(ConfigHL.INSTANCE.get(LogicalSide.CLIENT).clientSide.guiItemRenderResolution, 16, 32768);
						renderItem(Component.literal("Hotkey"), stack, res);
					});
		}
		
		if(QUEUE.isEmpty())
		{
			if(target != null && System.currentTimeMillis() - lastTargetUse > 500L)
			{
				target.destroyBuffers();
				target = null;
			}
			
			return;
		}
		
		var mc = Minecraft.getInstance();
		
		var elem = QUEUE.removeFirst();
		
		var a = elem.work;
		
		var stack = elem.stack();
		
		var resolution = elem.resolution();
		
		{
			var pStack = RenderSystem.getModelViewStack();
			GuiGraphics guiGraphics = new GuiGraphics(mc, mc.renderBuffers().bufferSource());
			
			if(target == null) target = new RenderTarget(true) {};
			if(target.width < resolution || target.height < resolution)
				target.resize(resolution, resolution);
			
			float max = resolution * 16F / resolution;
			Matrix4f proj = new Matrix4f()
					.setOrtho(0, max,
							max, 0,
							-3000, 3000
					);
			RenderSystem.setProjectionMatrix(proj, ProjectionType.ORTHOGRAPHIC);
			
			pStack.pushMatrix();
			pStack.identity();
			target.bindWrite(true);
			
			RenderSystem.clearColor(0, 0, 0, 0);
			RenderSystem.clearDepth(1);
			RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			Lighting.setupFor3DItems();
			RenderSystem.enableCull();
			
			guiGraphics.renderItem(stack, 0, 0);
			
			NativeImage img = new NativeImage(resolution, resolution, false);
			target.bindRead();
			img.downloadTexture(0, false);
			img.flipY();
			
			pStack.popMatrix();
			
			target.unbindWrite();
			
			elem.finishCallback.accept(img);
			
			if(a != null)
				SystemToast.addOrUpdate(mc.toastManager, SystemToast.SystemToastId.NARRATOR_TOGGLE, a.copy().append(Component.literal(": Rendered!").withStyle(ChatFormatting.GREEN)), elem.stack.getDisplayName());
		}
		
		lastTargetUse = System.currentTimeMillis();
	}
}