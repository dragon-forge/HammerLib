package org.zeith.hammerlib.core.adapter;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

/**
 * A class that provides utility methods for altering {@link LootTable} instances.
 */
public class LootTableAdapter
{
	private static final Map<ResourceLocation, UnaryOperator<LootTable>> TABLE_ALTERATORS = new ConcurrentHashMap<>();
	
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
		return TABLE_ALTERATORS.getOrDefault(table.getLootTableId(), UnaryOperator.identity()).apply(table);
	}
}