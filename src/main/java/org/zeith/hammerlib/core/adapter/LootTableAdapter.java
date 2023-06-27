package org.zeith.hammerlib.core.adapter;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.*;
import org.jetbrains.annotations.ApiStatus;
import org.zeith.hammerlib.mixins.LootTableAccessor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;

/**
 * A class that provides utility methods for altering {@link LootTable} instances.
 */
public class LootTableAdapter
{
	private static final Map<ResourceLocation, UnaryOperator<LootTable>> TABLE_ALTERATORS = new ConcurrentHashMap<>();
	private static final List<BiConsumer<ResourceLocation, LootTable>> TABLE_HOOKS = new ArrayList<>();
	
	/**
	 * Use this to add {@link LootPool}s to {@link LootTable}.
	 * The returned list (if not modified by other mods) must be modifiable {@link List}.
	 */
	public static List<LootPool> getPools(LootTable table)
	{
		return ((LootTableAccessor) table).getPools();
	}
	
	/**
	 * Registers an alterator for a loot table.
	 *
	 * @param id
	 * 		The id of the {@link LootTable} to alter.
	 * @param alterator
	 * 		The {@link UnaryOperator} that alters the {@link LootTable}.
	 */
	public static void alterTable(ResourceLocation id, UnaryOperator<LootTable> alterator)
	{
		var prev = TABLE_ALTERATORS.get(id);
		if(prev != null) alterator = prev.andThen(alterator)::apply;
		TABLE_ALTERATORS.put(id, alterator);
	}
	
	/**
	 * Adds a hook to be called when any and all loot tables load.
	 * This could be used to perform wildcard table modification.
	 */
	public static void addLoadHook(BiConsumer<ResourceLocation, LootTable> hook)
	{
		TABLE_HOOKS.add(hook);
	}
	
	/**
	 * Alters the given {@link LootTable} using the {@link UnaryOperator} that is associated with its id.
	 *
	 * @param table
	 * 		The {@link LootTable} to alter.
	 *
	 * @return The altered {@link LootTable}.
	 */
	@ApiStatus.Internal
	public static LootTable alter(LootTable table)
	{
		final var id = table.getLootTableId();
		final var t1 = TABLE_ALTERATORS.getOrDefault(id, UnaryOperator.identity()).apply(table);
		TABLE_HOOKS.forEach(c -> c.accept(id, t1));
		return t1;
	}
}