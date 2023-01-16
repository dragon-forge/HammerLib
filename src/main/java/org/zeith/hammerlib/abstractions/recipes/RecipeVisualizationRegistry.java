package org.zeith.hammerlib.abstractions.recipes;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class RecipeVisualizationRegistry
{
	private static final Map<RecipeType<?>, IRecipeVisualizer<?, ?>> VIS_REGISTRY = new ConcurrentHashMap<>();
	
	public static <T extends Recipe<?>> void register(RecipeType<T> type, IRecipeVisualizer<T, ?> visualizer)
	{
		VIS_REGISTRY.put(type, visualizer);
	}
	
	public static <T extends Recipe<?>> Optional<IRecipeVisualizer<T, ?>> getVisualizer(RecipeType<T> type)
	{
		return Optional.ofNullable(Cast.cast(VIS_REGISTRY.get(type)));
	}
	
	public static Stream<RecipeType<?>> getVisualizedRecipeTypes()
	{
		return VIS_REGISTRY.keySet().stream();
	}
	
	public static Stream<Tuple2<RecipeType<?>, IRecipeVisualizer<?, ?>>> getVisualizedEntries()
	{
		return VIS_REGISTRY.entrySet()
				.stream()
				.map(e -> Tuples.immutable(e.getKey(), e.getValue()));
	}
}