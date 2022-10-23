package org.zeith.hammerlib.client.render.item;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.platform.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.client.utils.TexturePixelGetter;
import org.zeith.hammerlib.compat.jei.IJeiPluginHL;
import org.zeith.hammerlib.core.ConfigHL;
import org.zeith.hammerlib.proxy.HLClientProxy;
import org.zeith.hammerlib.util.java.Cast;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class Stack2ImageRenderer
{
	private static final List<RenderQueueItem> QUEUE = new ArrayList<>();
	
	private record RenderQueueItem(Component work, int width, int height, ItemStack stack, Consumer<NativeImage> finishCallback)
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
		
		renderItemStack(type, size, size, stack, image ->
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
	
	private record ItemWithData(Item item, CompoundTag data)
	{
		public ItemStack stack()
		{
			ItemStack stack = new ItemStack(item);
			stack.setTag(data);
			return stack;
		}
	}
	
	public static void renderItem(Component type, ItemStack stack, int size)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-hh.mm.ss");
		ResourceLocation rl = ForgeRegistries.ITEMS.getKey(stack.getItem());
		var faild = new File("hammercore", "renderers" + File.separator + rl.getNamespace());
		var fl = new File(faild, (rl.getPath() + (stack.getTag() != null ? "_" + stack.getTag() : "") + "-" + sdf.format(Date.from(Instant.now())) + ".png").replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
		queueRenderer(type, stack, size, fl);
	}
	
	public static void renderAll(int size)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-hh.mm.ss");
		File faild = new File("hammercore", "renderers" + File.separator + "all-" + sdf.format(Date.from(Instant.now())));
		
		ForgeRegistries.ITEMS.getValues().stream()
				.flatMap(item ->
				{
					NonNullList<ItemStack> sb = NonNullList.create();
					try
					{
						for(var tab : item.getCreativeTabs())
							item.fillItemCategory(tab, sb);
					} catch(Throwable err)
					{
					}
					return sb.stream().map(stack -> new ItemWithData(stack.getItem(), stack.getTag()));
				}).distinct()
				.forEach(s ->
				{
					ResourceLocation rl = ForgeRegistries.ITEMS.getKey(s.item());
					var fl = new File(faild, rl.getNamespace() + File.separator + (rl.getPath() + ".png").replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
					queueRenderer(Component.literal("Everything"), s.stack(), size, fl);
				});
	}
	
	public static void renderMod(String modid, int size)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-hh.mm.ss");
		File faild = new File("hammercore", "renderers" + File.separator + modid + "-" + sdf.format(Date.from(Instant.now())));
		
		ForgeRegistries.ITEMS.getKeys().stream()
				.flatMap(rl ->
				{
					if(!rl.getNamespace().equals(modid))
						return Stream.empty();
					var item = ForgeRegistries.ITEMS.getValue(rl);
					NonNullList<ItemStack> sb = NonNullList.create();
					try
					{
						for(var tab : item.getCreativeTabs())
							item.fillItemCategory(tab, sb);
					} catch(Throwable err)
					{
					}
					return sb.stream().map(stack -> new ItemWithData(stack.getItem(), stack.getTag()));
				})
				.distinct()
				.forEach(s ->
				{
					ResourceLocation rl = ForgeRegistries.ITEMS.getKey(s.item());
					var fl = new File(faild, (rl.getPath() + ".png").replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
					queueRenderer(Component.literal("Mod " + modid), s.stack(), size, fl);
				});
	}
	
	public static synchronized void renderItemStack(Component type, int width, int height, ItemStack stack, Consumer<NativeImage> finishCallback)
	{
		QUEUE.add(new RenderQueueItem(type, width, height, stack, finishCallback));
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onFrameStart(TickEvent.RenderTickEvent eventThatWeDoNotCareMuchAbout)
	{
		if(eventThatWeDoNotCareMuchAbout.phase == TickEvent.Phase.START)
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
			
			return;
		}
		
		if(!QUEUE.isEmpty())
		{
			var mc = Minecraft.getInstance();
			var ir = mc.getItemRenderer();
			
			var elem = QUEUE.remove(0);
			
			var a = elem.work;
			
			MainTarget renderTarget = new MainTarget(elem.width(), elem.height());
			renderTarget.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
			renderTarget.clear(Minecraft.ON_OSX);
			renderTarget.bindWrite(true);
			
			var stack = elem.stack();
			var model = ir.getModel(stack, null, null, 0);
			
			HammerLib.LOG.info("Colors for {}: {}", stack.getDisplayName().getString(), Arrays.stream(TexturePixelGetter.getAllColors(stack)).mapToObj(Integer::toHexString).collect(Collectors.joining(", ")));
			
			{
				mc.textureManager.getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
				RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				PoseStack mview = RenderSystem.getModelViewStack();
				mview.pushPose();
				mview.scale(16, 16, 16);
				mview.translate(0, 0, 0.0F);
				mview.translate(13.333333333333D, 7.5D, 0.0D);
				mview.scale(1.783549783549338F, -1.0F, 1.0F);
				mview.scale(15.0F, 15.0F, 15.0F);
				RenderSystem.applyModelViewMatrix();
				
				PoseStack pose = new PoseStack();
				
				MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
				boolean flat = !model.usesBlockLight();
				if(flat) Lighting.setupForFlatItems();
				else Lighting.setupFor3DItems();
				
				ir.render(stack, ItemTransforms.TransformType.GUI, false, pose, buffers, 15728880, OverlayTexture.NO_OVERLAY, model);
				buffers.endBatch();
				RenderSystem.enableDepthTest();
				if(flat) Lighting.setupFor3DItems();
				
				mview.popPose();
				RenderSystem.applyModelViewMatrix();
			}
			
			try
			{
				NativeImage img = new NativeImage(NativeImage.Format.RGBA, renderTarget.width, renderTarget.height, false);
				RenderSystem.bindTexture(renderTarget.getColorTextureId());
				img.downloadTexture(0, false);
				img.flipY();
				elem.finishCallback.accept(img);
				
				if(a != null)
					SystemToast.addOrUpdate(mc.toast, SystemToast.SystemToastIds.NARRATOR_TOGGLE, a.copy().append(Component.literal(": Rendered!").withStyle(ChatFormatting.GREEN)), elem.stack.getDisplayName());
			} catch(Throwable e)
			{
				e.printStackTrace();
				
				if(a != null)
					SystemToast.addOrUpdate(mc.toast, SystemToast.SystemToastIds.NARRATOR_TOGGLE, a.copy().append(Component.literal(": Failed!").withStyle(ChatFormatting.RED)), elem.stack.getDisplayName());
			}
			
			renderTarget.destroyBuffers();
		}
	}
}