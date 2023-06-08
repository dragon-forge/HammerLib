package org.zeith.hammerlib.core.adapter;

import com.google.common.base.Suppliers;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.api.items.ITabItem;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTabAdapter
{
	private static final List<Tuple2<ItemLike, CreativeTab[]>> REGISTRARS = new ArrayList<>();
	
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
	
	@SubscribeEvent
	public static void populate(BuildCreativeModeTabContentsEvent e)
	{
		CreativeTab tab = REGISTERED.get(e.getTab());
		if(tab != null)
		{
			for(ItemLike item : tab.contents())
			{
				if(item instanceof ITabItem)
					continue;
				e.accept(item);
			}
		}
		
		NonNullList<ItemStack> items = NonNullList.create();
		for(ITabItem item : CUSTOM_TAB_ITEMS.get()) item.fillItemCategory(e.getTab(), items);
		e.acceptAll(items);
	}
	
	public static Map<CreativeModeTab, CreativeTab> getRegistered()
	{
		return REGISTERED;
	}
	
	public static List<CreativeTab> getCustomTabs()
	{
		return CUSTOM_TABS;
	}
	
	/**
	 * Assigns one or more creative tabs to an item.
	 *
	 * @param item
	 * 		the item to assign the creative tabs to
	 * @param tabs
	 * 		the creative tabs to assign
	 * @param <T>
	 * 		the type of the item
	 *
	 * @return the item with the assigned creative tabs
	 */
	public static <T extends ItemLike> T bindTab(T item, CreativeTab... tabs)
	{
		REGISTRARS.add(Tuples.immutable(item, tabs)); // store ItemLike(s) so that if that is a block, it would unwrap properly.
		return item;
	}
	
	public static void deque()
	{
		while(!REGISTRARS.isEmpty())
		{
			var tup = REGISTRARS.remove(0);
			for(var t : tup.b()) t.add(tup.a());
		}
	}
}