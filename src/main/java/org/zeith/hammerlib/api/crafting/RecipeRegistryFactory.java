package org.zeith.hammerlib.api.crafting;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModLoadingContext;
import org.zeith.hammerlib.api.crafting.building.GeneralRecipeBuilder;
import org.zeith.hammerlib.api.crafting.building.IRecipeBuilderFactory;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

import java.util.HashMap;
import java.util.Map;

public class RecipeRegistryFactory
{
	private static final Map<RegistryFingerprint, NamespacedRecipeRegistry<?>> NAMESPACED_REGISTRIES = new HashMap<>();
	static final Map<RegistryFingerprint, BiMap<ResourceLocation, ?>> NAMESPACED_REGISTRY_STORAGE = new HashMap<>();
	
	public static <T extends INameableRecipe> NamespacedBuilder<T> namespacedBuilder(Class<T> type)
	{
		return new NamespacedBuilder<>(type);
	}
	
	public static class NamespacedBuilder<T extends INameableRecipe>
	{
		final Class<T> type;
		ResourceLocation id;
		IRecipeBuilderFactory<T, ?> recipeBuilder;
		
		public NamespacedBuilder(Class<T> type)
		{
			this.type = type;
		}
		
		public NamespacedBuilder<T> registryId(String path)
		{
			return registryId(new ResourceLocation(ModLoadingContext.get().getActiveNamespace(), path));
		}
		
		public NamespacedBuilder<T> registryId(ResourceLocation location)
		{
			this.id = location;
			return this;
		}
		
		public <B extends GeneralRecipeBuilder<T, B>> NamespacedBuilder<T> recipeBuilderFactory(IRecipeBuilderFactory<T, B> factory)
		{
			this.recipeBuilder = factory;
			return this;
		}
		
		public NamespacedRecipeRegistry<T> build()
		{
			if(id == null)
				throw new IllegalArgumentException("Missing registry id for NamespacedRecipeRegistry<" + type.getSimpleName() + ">");
			var fp = new RegistryFingerprint<T>(type, id);
			
			return Cast.cast(NAMESPACED_REGISTRIES.computeIfAbsent(fp, fprint ->
							new NamespacedRecipeRegistry<T>(fprint)
							{
								@Override
								public GeneralRecipeBuilder<T, ?> builder(IRecipeRegistrationEvent<T> event)
								{
									if(recipeBuilder != null)
										return recipeBuilder.newBuilder(event);
									return super.builder(event);
								}
							}
					)
			);
		}
	}
	
	record RegistryFingerprint<T extends INameableRecipe>(Class<T> type, ResourceLocation regId)
	{
		BiMap<ResourceLocation, T> storage()
		{
			return Cast.cast(NAMESPACED_REGISTRY_STORAGE.computeIfAbsent(this, t -> HashBiMap.create()));
		}
	}
}