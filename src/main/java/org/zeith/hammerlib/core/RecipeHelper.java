package org.zeith.hammerlib.core;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.neoforged.fml.*;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.zeith.api.level.ISpoofedRecipeManager;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.items.IIngredientProvider;
import org.zeith.hammerlib.core.adapter.OreDictionaryAdapter;
import org.zeith.hammerlib.event.ParseIngredientEvent;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.mixins.IngredientAccessor;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;
import java.util.stream.*;

public class RecipeHelper
{
	public static final String NEOFORGE_MOD_ID_FOR_TAGS = "forge";
	
	public static void registerCustomRecipes(Predicate<ResourceLocation> idInUse, Consumer<RecipeHolder<?>> addRecipe, Consumer<Set<ResourceLocation>> removeRecipes, boolean silent, ICondition.IContext context)
	{
		RegisterRecipesEvent rre = new RegisterRecipesEvent(context, idInUse);
		ModList.get().forEachModInOrder(mc ->
		{
			var bus = mc.getEventBus();
			if(bus == null) return;
			ModLoadingContext.get().setActiveContainer(mc);
			bus.post(rre);
		});
		ModLoadingContext.get().setActiveContainer(null);
		rre.cleanup();
		
		if(!silent)
			HLConstants.LOG.info("Reloading HammerLib recipes...");
		
		AtomicLong count = new AtomicLong();
		
		rre.getRecipes().forEach(recipe ->
		{
			addRecipe.accept(recipe);
			count.incrementAndGet();
		});
		
		removeRecipes.accept(rre.removedRecipes());
		
		if(!silent)
			HLConstants.LOG.info("HammerLib injected {} recipes into recipe map. Removed {} recipes from the game.",
					count.longValue(),
					rre.removedRecipes().size()
			);
	}
	
	public static void injectRecipes(RecipeManager mgr, ICondition.IContext context)
	{
		Internal.mutableManager(mgr);
		var spoofed = ((ISpoofedRecipeManager) mgr).getSpoofedRecipes();
		
		List<RecipeHolder<?>> recipeList = new ArrayList<>();
		Set<ResourceLocation> removed = new HashSet<>();
		registerCustomRecipes(id -> mgr.byKey(id)
				.isPresent(), recipeList::add, removed::addAll, false, context);
		Internal.addRecipes(mgr, recipeList);
		Internal.removeRecipes(mgr, removed::stream);
	}
	
	public static void injectRecipesCustom(Map<ResourceLocation, Recipe<?>> handler, Set<ResourceLocation> removed, Map<ResourceLocation, List<ResourceLocation>> spoofedRecipes, ICondition.IContext ctx)
	{
		registerCustomRecipes(handler::containsKey, r -> handler.put(r.id(), r.value()), removed::addAll, false, ctx);
	}
	
	public static <C extends Container, T extends Recipe<C>> Map<ResourceLocation, RecipeHolder<T>> getRecipeMap(Level level, RecipeType<T> type)
	{
		return level.getRecipeManager().byType(type);
	}
	
	public static <C extends Container, T extends Recipe<C>> Stream<RecipeHolder<T>> getRecipeHolders(Level level, RecipeType<T> type)
	{
		return getRecipeMap(level, type).values().stream();
	}
	
	public static <C extends Container, T extends Recipe<C>> Stream<T> getRecipes(Level level, RecipeType<T> type)
	{
		return getRecipeHolders(level, type).map(RecipeHolder::value);
	}
	
	private static class Internal
	{
		private static void addRecipes(RecipeManager mgr, List<RecipeHolder<?>> recipes)
		{
			recipes.forEach(r ->
			{
				Map<ResourceLocation, RecipeHolder<?>> map = mgr.recipes.computeIfAbsent(r.value().getType(), t -> new HashMap<>());
				map.putIfAbsent(r.id(), r);
				mgr.byName.putAll(map);
			});
			HammerLib.LOG.info("Registered {} additional recipes.", recipes.size());
		}
		
		private static void removeRecipes(RecipeManager mgr, Supplier<Stream<ResourceLocation>> recipes)
		{
			recipes.get().forEach(id ->
			{
				mgr.byKey(id).ifPresent(recipe ->
				{
					var rmap = mgr.recipes.get(recipe.value().getType());
					if(rmap != null) rmap.remove(id);
					mgr.byName.remove(id);
				});
			});
		}
		
		private static void mutableManager(RecipeManager mgr)
		{
			mgr.byName = new HashMap<>(mgr.byName);
			mgr.recipes = new HashMap<>(mgr.recipes);
			for(RecipeType<?> type : mgr.recipes.keySet())
				mgr.recipes.put(type, new HashMap<>(mgr.recipes.get(type)));
		}
	}
	
	public static ItemStack cycleIngredientStack(Ingredient ingr, long displayDurationMS)
	{
		if(ingr.isEmpty()) return ItemStack.EMPTY;
		var items = ingr.getItems();
		return items[(int) ((System.currentTimeMillis() % (items.length * displayDurationMS)) / displayDurationMS) %
					 items.length];
	}
	
	public static Ingredient merge(List<Ingredient> comps)
	{
		return merge(comps.stream());
	}
	
	public static Ingredient merge(Stream<Ingredient> comps)
	{
		return IngredientAccessor.createIngredient(
				comps.filter(Objects::nonNull)
						.map(Ingredient::getValues)
						.flatMap(Arrays::stream)
		);
	}

//	public static Ingredient composeIngredient(Object... comps)
//	{
//		return IngredientAccessor.createIngredient(
//				Arrays.stream(comps)
//						.map(RecipeHelper::fromComponent)
//						.filter(Objects::nonNull)
//						.map(Ingredient::getValues)
//						.flatMap(Arrays::stream)
//		);
//	}
	
	public static Ingredient fromComponent(Object comp)
	{
		if(comp == null) return Ingredient.EMPTY;
		
		if(comp instanceof Ingredient i) return i;
		if(comp instanceof Supplier<?> su) return fromComponent(su.get());
		if(comp instanceof ItemLike il) return Ingredient.of(il);
		if(comp instanceof IIngredientProvider ip) return ip.asIngredient();
		if(comp instanceof ItemStack is) return Ingredient.of(is.copy());
		if(comp instanceof TagKey<?>) return fromTag((TagKey<Item>) comp);
		
		if(comp instanceof ItemStack[] items)
		{
			items = items.clone();
			for(int l = 0; l < items.length; ++l)
				items[l] = items[l].copy();
			return Ingredient.of(items);
		}
		
		if(comp instanceof String || comp instanceof ResourceLocation)
		{
			String st = comp.toString();
			return merge(OreDictionaryAdapter.get(st).stream().map(obj ->
			{
				if(obj != null)
				{
					ResourceLocation odConv = Cast.cast(obj, ResourceLocation.class);
					
					ResourceLocation tag;
					if(odConv != null) tag = odConv;
					else tag = new ResourceLocation(st.contains(":") ? st : (NEOFORGE_MOD_ID_FOR_TAGS + ":" + st));
					
					return fromTag(getItemTag(tag));
				}
				return Ingredient.EMPTY;
			}));
		}
		
		ParseIngredientEvent event = new ParseIngredientEvent(comp);
		HammerLib.postEvent(event);
		if(event.hasIngredient()) return event.getIngredient();
		
		if(comp.getClass().isArray())
		{
			var len = Array.getLength(comp);
			return merge(IntStream.range(0, len)
					.mapToObj(i -> fromComponent(Array.get(comp, i)))
			);
		} else if(comp instanceof Collection<?> col)
		{
			return merge(col.stream()
					.map(RecipeHelper::fromComponent)
			);
		}
		
		return Ingredient.EMPTY;
	}
	
	public static TagKey<Item> getItemTag(ResourceLocation path)
	{
		return TagKey.create(BuiltInRegistries.ITEM.key(), path);
	}
	
	public static Ingredient fromTag(TagKey<Item> tag)
	{
		return IngredientAccessor.createIngredient(Stream.of(new Ingredient.TagValue(tag)));
	}
}