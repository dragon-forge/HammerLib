package org.zeith.hammerlib.api.crafting;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModLoadingContext;
import org.zeith.hammerlib.api.crafting.building.*;
import org.zeith.hammerlib.api.crafting.itf.IRecipeReceiver;
import org.zeith.hammerlib.util.SidedLocal;
import org.zeith.hammerlib.util.java.Cast;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class RecipeRegistryFactory
{
	private static final Map<RegistryFingerprint<?>, NamespacedRecipeRegistry<?>> NAMESPACED_REGISTRIES = new ConcurrentHashMap<>();
	static final Map<RegistryFingerprint<?>, SidedLocal<BiMap<ResourceLocation, ?>>> NAMESPACED_REGISTRY_STORAGE = new ConcurrentHashMap<>();
	static final Map<RegistryFingerprint<?>, BiMap<ResourceLocation, ?>> NAMESPACED_REGISTRY_STORAGE_CLIENT_EXTRA = new ConcurrentHashMap<>();
	static final Map<RegistryFingerprint<?>, List<IRecipeReceiver>> NAMESPACED_RECIPE_RECEIVER_STORAGE = new ConcurrentHashMap<>();
	
	public static <T extends INameableRecipe> NamespacedBuilder<T> namespacedBuilder(Class<T> type)
	{
		return new NamespacedBuilder<>(type);
	}
	
	public static class NamespacedBuilder<T extends INameableRecipe>
	{
		final Class<T> type;
		ResourceLocation id;
		IRecipeBuilderFactory<T, ?> recipeBuilder;
		Function<ResourceLocation, CustomRecipeGenerator<T, ?, ?>> customRecipes;
		
		final List<IRecipeReceiver<T>> clientReceive = new ArrayList<>();
		
		public NamespacedBuilder(Class<T> type)
		{
			this.type = type;
		}
		
		public NamespacedBuilder<T> registryId(String path)
		{
			return registryId(new ResourceLocation(ModLoadingContext.get().getActiveNamespace(), path));
		}
		
		public NamespacedBuilder<T> customRecipes(Function<ResourceLocation, CustomRecipeGenerator<T, ?, ?>> customRecipes)
		{
			this.customRecipes = customRecipes;
			return this;
		}
		
		public NamespacedBuilder<T> onClientRecipeReceive(IRecipeReceiver<T> receiver)
		{
			this.clientReceive.add(receiver);
			return this;
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
			
			fp.clientReceivers().addAll(clientReceive);
			
			return Cast.cast(NAMESPACED_REGISTRIES.computeIfAbsent(fp, fprint ->
			{
				var r = new NamespacedRecipeRegistry<>(fp, customRecipes != null ? customRecipes.apply(id) : null);
				r.recipeBuilder = recipeBuilder;
				return r;
			}));
		}
	}
	
	record RegistryFingerprint<T extends INameableRecipe>(Class<T> type, ResourceLocation regId)
	{
		SidedLocal<BiMap<ResourceLocation, T>> storage()
		{
			return Cast.cast(NAMESPACED_REGISTRY_STORAGE.computeIfAbsent(this, t -> new SidedLocal<>(side -> HashBiMap.<ResourceLocation, T> create())));
		}
		
		BiMap<ResourceLocation, T> clientExtraStorage()
		{
			return Cast.cast(NAMESPACED_REGISTRY_STORAGE_CLIENT_EXTRA.computeIfAbsent(this, t -> HashBiMap.create()));
		}
		
		List<IRecipeReceiver<T>> clientReceivers()
		{
			return Cast.cast(NAMESPACED_RECIPE_RECEIVER_STORAGE.computeIfAbsent(this, t -> new ArrayList<>()));
		}
	}
}