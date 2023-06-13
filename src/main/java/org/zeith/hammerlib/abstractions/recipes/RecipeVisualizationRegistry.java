package org.zeith.hammerlib.abstractions.recipes;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class RecipeVisualizationRegistry
{
	private final Map<RecipeType<?>, IRecipeVisualizer<?, ?>> VIS_REGISTRY = new ConcurrentHashMap<>();
	
	public <T extends Recipe<?>> void register(RecipeType<T> type, IRecipeVisualizer<T, ?> visualizer)
	{
		VIS_REGISTRY.put(type, visualizer);
	}
	
	public <T extends Recipe<?>> Optional<IRecipeVisualizer<T, ?>> getVisualizer(RecipeType<T> type)
	{
		return Optional.ofNullable(Cast.cast(VIS_REGISTRY.get(type)));
	}
	
	public Stream<RecipeType<?>> getVisualizedRecipeTypes()
	{
		return VIS_REGISTRY.keySet().stream();
	}
	
	public Stream<Tuple2<RecipeType<?>, IRecipeVisualizer<?, ?>>> getVisualizedEntries()
	{
		return VIS_REGISTRY.entrySet()
				.stream()
				.map(e -> Tuples.immutable(e.getKey(), e.getValue()));
	}
	
	public void reload()
	{
		VIS_REGISTRY.clear();
		HammerLib.EVENT_BUS.post(new RegisterRecipeVisualizationEvent(this));
		for(RecipeType<?> type : ForgeRegistries.RECIPE_TYPES.getValues())
		{
			if(type instanceof IVisualizedRecipeType<?> visual)
			{
				AtomicReference<IRecipeVisualizer<?, ?>> visualizer = new AtomicReference<>();
				visual.initVisuals(visualizer::set);
				var vis = visualizer.get();
				if(vis != null)
					register(type, Cast.cast(vis));
			}
		}
	}
	
	public static class RegisterRecipeVisualizationEvent
			extends Event
	{
		protected final RecipeVisualizationRegistry registry;
		
		public RegisterRecipeVisualizationEvent(RecipeVisualizationRegistry registry)
		{
			this.registry = registry;
		}
		
		public RecipeVisualizationRegistry registry()
		{
			return registry;
		}
		
		public <T extends Recipe<?>> void register(RecipeType<T> type, IRecipeVisualizer<T, ?> visualizer)
		{
			registry.register(type, visualizer);
		}
	}
}