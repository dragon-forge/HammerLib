package org.zeith.hammerlib.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class Stack2ImageRenderer
{
	private static List<Tuple<ItemTextureRenderer, List<Tuple<ItemStack, File>>>> queue = new ArrayList<>();

	/**
	 * Adds this element to render queue. Once it's done, the <code>done</code>
	 * will be called with the image.
	 *
	 * @param stack  The item to render.
	 * @param size   The size of an output image.
	 * @param target The File to where the image is rendered.
	 */
	public static void queueRenderer(ItemStack stack, int size, File target)
	{
		Minecraft.getInstance().execute(() ->
		{
			queue.add(new Tuple<>(new ItemTextureRenderer(Minecraft.getInstance().itemRenderer, size), new ArrayList<>(Collections.singletonList(new Tuple<>(stack, target)))));
		});
	}

	public static void renderAll(int size)
	{
		NonNullList<ItemStack> sub = NonNullList.create();

		for(Item item : ForgeRegistries.ITEMS.getValues())
		{
			NonNullList<ItemStack> sb = NonNullList.create();
			try
			{
				item.fillItemCategory(item.getItemCategory(), sb);
			} catch(Throwable err)
			{
			}
			for(ItemStack sbs : sb)
				sub.add(sbs);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-hh.mm.ss");
		File faild = new File("hammercore", "renderers" + File.separator + "all-" + sdf.format(Date.from(Instant.now())));

		queue.add(new Tuple<>(new ItemTextureRenderer(Minecraft.getInstance().itemRenderer, size), new ArrayList<>(sub.stream().map(s ->
		{
			ResourceLocation rl = s.getItem().getRegistryName();
			return new Tuple<>(s, new File(faild, rl.getNamespace() + File.separator + (rl.getPath() + (s.getDamageValue() == 0 ? "" : "." + s.getDamageValue()) + (s.hasTag() ? "_" + s.getTag() : "") + ".png").replaceAll("[^a-zA-Z0-9\\.\\-]", "_")));
		}).collect(Collectors.toList()))));
	}

	public static void renderMod(String modid, int size)
	{
		NonNullList<ItemStack> sub = NonNullList.create();

		for(Item item : ForgeRegistries.ITEMS.getValues())
		{
			NonNullList<ItemStack> sb = NonNullList.create();
			try
			{
				item.fillItemCategory(item.getItemCategory(), sb);
			} catch(Throwable err)
			{
			}
			for(ItemStack sbs : sb)
				if(sbs.getItem().getRegistryName().getNamespace().equals(modid))
					sub.add(sbs);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-hh.mm.ss");

		File faild = new File("hammercore", "renderers" + File.separator + modid + "-" + sdf.format(Date.from(Instant.now())));

		queue.add(new Tuple<>(new ItemTextureRenderer(Minecraft.getInstance().itemRenderer, size), new ArrayList<>(sub.stream().map(s ->
		{
			ResourceLocation rl = s.getItem().getRegistryName();
			return new Tuple<>(s, new File(faild, (rl.getPath() + (s.getDamageValue() == 0 ? "" : "." + s.getDamageValue()) + (s.hasTag() ? "_" + s.getTag() : "") + ".png").replaceAll("[^a-zA-Z0-9\\.\\-]", "_")));
		}).collect(Collectors.toList()))));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onFrameStart(TickEvent.RenderTickEvent e)
	{
		if(e.phase == TickEvent.Phase.START)
		{
			if(!queue.isEmpty())
			{
				Tuple<ItemTextureRenderer, List<Tuple<ItemStack, File>>> data = queue.get(0);
				if(!data.getB().isEmpty())
				{
					Tuple<ItemStack, File> remove = data.getB().remove(0);
					ItemTextureRenderer a = data.getA();
					try
					{
						a.renderItemstack(remove.getA(), remove.getB(), true);
					} catch(Throwable er)
					{
						er.printStackTrace();
					}
					if(data.getB().isEmpty())
						a.close();
				} else queue.remove(0);
			}
		}
	}
}