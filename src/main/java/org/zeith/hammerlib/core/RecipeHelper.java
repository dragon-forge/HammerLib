package org.zeith.hammerlib.core;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.crafting.AbstractRecipeRegistry;
import org.zeith.hammerlib.api.items.IIngredientProvider;
import org.zeith.hammerlib.core.adapter.OreDictionaryAdapter;
import org.zeith.hammerlib.event.ParseIngredientEvent;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.RunnableReloader;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeHelper
{
	public static void registerCustomRecipes(Consumer<IRecipe<?>> addRecipe, boolean silent)
	{
		RegisterRecipesEvent rre = new RegisterRecipesEvent();
		MinecraftForge.EVENT_BUS.post(rre);

		if(!silent) HLConstants.LOG.info("Reloading HammerLib recipes...");

		AtomicLong count = new AtomicLong();

		rre.getRecipes().forEach(recipe ->
		{
			addRecipe.accept(recipe);
			count.incrementAndGet();
		});

		if(!silent) HLConstants.LOG.info("HammerLib injected {} recipes into recipe map.", count.longValue());

		List<AbstractRecipeRegistry<?, ?, ?>> registries = AbstractRecipeRegistry.getAllRegistries();
		if(!silent) HLConstants.LOG.info("Reloading {} custom registries.", registries.size());
		registries.forEach(AbstractRecipeRegistry::reload);
		if(!silent)
			HLConstants.LOG.info("{} custom registries reloaded, added {} total recipes.", registries.size(), registries.stream().mapToInt(AbstractRecipeRegistry::getRecipeCount).sum());
	}

	public static void reload(RecipeManager mgr, IReloadableResourceManager rel)
	{
		rel.registerReloadListener(RunnableReloader.of(() ->
		{
			Internal.mutableManager(mgr);
			List<IRecipe<?>> recipeList = new ArrayList<>();
			registerCustomRecipes(recipeList::add, false);
			Internal.addRecipes(mgr, recipeList);
		}));
	}

	private static class Internal
	{
		private static void addRecipes(RecipeManager mgr, List<IRecipe<?>> recipes)
		{
			recipes.forEach(r ->
			{
				Map<ResourceLocation, IRecipe<?>> map = mgr.recipes.computeIfAbsent(r.getType(), t -> new HashMap<>());
				map.putIfAbsent(r.getId(), r);
			});
			HammerLib.LOG.info("Registered {} additional recipes.", recipes.size());
		}

		private static void mutableManager(RecipeManager mgr)
		{
			mgr.recipes = new HashMap<>(mgr.recipes);
			for(IRecipeType<?> type : mgr.recipes.keySet())
				mgr.recipes.put(type, new HashMap<>(mgr.recipes.get(type)));
		}
	}

	public enum MapMode
	{
		IMMUTABLE(ImmutableMap.Builder.class),
		MAP(Map.class),
		UNKNOWN(null);

		final Class<?> type;

		MapMode(Class<?> type)
		{
			this.type = type;
		}

		public Consumer<IRecipe<?>> addRecipe(Map<IRecipeType<?>, ?> map)
		{
			if(this == IMMUTABLE)
			{
				Map<IRecipeType<?>, ImmutableMap.Builder<ResourceLocation, IRecipe<?>>> imap = Cast.cast(map);
				return recipe ->
				{
					ImmutableMap.Builder<ResourceLocation, IRecipe<?>> builder = imap.computeIfAbsent(recipe.getType(), type -> ImmutableMap.builder());
					builder.put(recipe.getId(), recipe);
				};
			}

			if(this == MAP)
			{
				Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> mmap = Cast.cast(map);

				return recipe ->
				{
					Map<ResourceLocation, IRecipe<?>> builder = mmap.computeIfAbsent(recipe.getType(), type -> new Object2ObjectLinkedOpenHashMap<>());
					builder.put(recipe.getId(), recipe);
				};
			}

			return recipe ->
			{
				HammerLib.LOG.warn("Unable to add recipe " + recipe.getId() + " due to the uncertainty of the recipe map!");
			};
		}
	}

	public static Ingredient fromComponent(Object comp)
	{
		Ingredient ingr = Ingredient.EMPTY;
		if(comp instanceof IItemProvider)
			ingr = Ingredient.of((IItemProvider) comp);
		else if(comp instanceof IIngredientProvider)
			ingr = ((IIngredientProvider) comp).asIngredient();
		else if(comp instanceof ItemStack)
			ingr = Ingredient.of(((ItemStack) comp).copy());
		else if(comp instanceof ITag.INamedTag)
		{
			ITag<Item> itag = TagCollectionManager.getInstance().getItems().getTag(((ITag.INamedTag<?>) comp).getName());
			ingr = Ingredient.fromValues(Stream.of(new Ingredient.TagList(itag)));
		} else if(comp instanceof String || comp instanceof ResourceLocation)
		{
			String st = comp.toString();
			ingr = Ingredient.merge(OreDictionaryAdapter.get(st).stream().map(obj ->
			{
				if(obj != null)
				{
					ResourceLocation odConv = Cast.cast(obj, ResourceLocation.class);
					ResourceLocation tag;
					if(odConv != null) tag = odConv;
					else tag = new ResourceLocation(st.contains(":") ? st : ("forge:" + st));

					ITag<Item> itag = TagCollectionManager.getInstance().getItems().getTag(tag);
					return Ingredient.fromValues(Stream.of(new Ingredient.TagList(itag)));
				}
				return fromComponent(obj);
			}).filter(Objects::nonNull).collect(Collectors.toList()));
		} else if(comp instanceof ItemStack[])
		{
			ItemStack[] items = ((ItemStack[]) comp).clone();
			for(int l = 0; l < items.length; ++l)
				items[l] = items[l].copy();
			ingr = Ingredient.of(items);
		} else if(comp instanceof Ingredient)
			ingr = (Ingredient) comp;
		else if(comp != null)
		{
			ParseIngredientEvent<?> event = new ParseIngredientEvent<>(comp);
			MinecraftForge.EVENT_BUS.post(event);
			if(event.hasIngredient())
				ingr = event.getIngredient();
		}
		return ingr;
	}

	public static Ingredient fromTag(ITag.INamedTag<Item> tag)
	{
		ITag<Item> itag = TagCollectionManager.getInstance().getItems().getTag(tag.getName());
		return Ingredient.fromValues(Stream.of(new Ingredient.TagList(itag)));
	}
}