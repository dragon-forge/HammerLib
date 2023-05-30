package org.zeith.hammerlib.core.adapter;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;

import java.util.*;

/**
 * The CreativeTabHelper class provides utility methods for assigning creative tabs to items.
 * It allows associating one or more creative tabs with an item.
 */
public class CreativeTabAdapter
{
	private static final List<Tuple2<ItemLike, CreativeModeTab[]>> REGISTRARS = new ArrayList<>();
	private static final Map<Item, Set<CreativeModeTab>> TABS = Maps.newConcurrentMap();
	
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
	public static <T extends ItemLike> T bindTab(T item, CreativeModeTab... tabs)
	{
		REGISTRARS.add(Tuples.immutable(item, tabs)); // store ItemLike(s) so that if that is a block, it would unwrap properly.
		return item;
	}
	
	/**
	 * Retrieves the set of creative tab overrides associated with an item.
	 *
	 * @param item
	 * 		the item to retrieve the creative tab overrides for
	 *
	 * @return the set of creative tab overrides, or an empty set if no overrides are found
	 */
	@NotNull
	public static Set<CreativeModeTab> getTabOverrides(Item item)
	{
		return TABS.getOrDefault(item, Collections.emptySet());
	}
	
	public static void deque()
	{
		while(!REGISTRARS.isEmpty())
		{
			var tup = REGISTRARS.remove(0);
			TABS.computeIfAbsent(tup.a().asItem(), s -> Sets.newConcurrentHashSet()).addAll(Set.of(tup.b()));
		}
	}
}