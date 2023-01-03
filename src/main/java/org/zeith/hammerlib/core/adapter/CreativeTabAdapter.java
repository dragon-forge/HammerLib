package org.zeith.hammerlib.core.adapter;

import com.google.common.base.Suppliers;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.api.items.ITabItem;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.tuples.Tuple1;
import org.zeith.hammerlib.util.java.tuples.Tuples;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CreativeTabAdapter
{
	private static final List<CreativeTab> CUSTOM_TABS = new ArrayList<>();
	private static final Map<CreativeModeTab, CreativeTab> REGISTERED = new ConcurrentHashMap<>();
	private static final Supplier<Set<ITabItem>> CUSTOM_TAB_ITEMS = Suppliers.memoize(() ->
			ForgeRegistries.ITEMS
					.getValues()
					.stream()
					.filter(ITabItem.class::isInstance)
					.map(Cast.convertTo(ITabItem.class))
					.collect(Collectors.toSet())
	);
	
	private static final Function<ItemStack, CreativeModeTab[]> TABS_BY_ITEM = Util.memoize(item ->
			CreativeModeTabs.allTabs().stream().filter(tab -> tab.contains(item)).toArray(CreativeModeTab[]::new)
	);
	
	public static CreativeModeTab[] getTabs(ItemStack item)
	{
		return TABS_BY_ITEM.apply(item);
	}
	
	public static CreativeTab create(ResourceLocation id, Consumer<CreativeModeTab.Builder> factory)
	{
		return Util.make(new CreativeTab(id, factory, Tuples.mutable(null), new ArrayList<>()), CUSTOM_TABS::add);
	}
	
	@SubscribeEvent
	public static void populate(CreativeModeTabEvent.BuildContents e)
	{
		CreativeTab tab = REGISTERED.get(e.getTab());
		if(tab != null)
		{
			for(ItemLike item : tab.contents)
			{
				if(item instanceof ITabItem)
					continue;
				e.accept(item);
			}
		}
		
		NonNullList<ItemStack> items = NonNullList.create();
		
		for(ITabItem item : CUSTOM_TAB_ITEMS.get())
		{
			item.fillItemCategory(e.getTab(), items);
		}
		
		e.acceptAll(items);
	}
	
	@SubscribeEvent
	public static void register(CreativeModeTabEvent.Register e)
	{
		for(CreativeTab tab : CUSTOM_TABS)
		{
			tab.tab.setA(e.registerCreativeModeTab(tab.id, tab.factory));
			REGISTERED.put(tab.tab.a(), tab);
		}
	}
	
	public record CreativeTab(ResourceLocation id, Consumer<CreativeModeTab.Builder> factory, Tuple1.Mutable1<CreativeModeTab> tab, List<ItemLike> contents)
	{
		public void add(ItemLike item)
		{
			contents.add(item);
		}
		
		public CreativeModeTab get()
		{
			return tab.a();
		}
	}
}