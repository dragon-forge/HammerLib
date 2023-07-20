package org.zeith.hammerlib.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.api.level.ISpoofedRecipeManager;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.crafting.AbstractRecipeRegistry;
import org.zeith.hammerlib.api.items.IIngredientProvider;
import org.zeith.hammerlib.core.adapter.OreDictionaryAdapter;
import org.zeith.hammerlib.event.ParseIngredientEvent;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeHelper
{
	public static void registerCustomRecipes(Predicate<ResourceLocation> idInUse, Consumer<Recipe<?>> addRecipe, Consumer<Set<ResourceLocation>> removeRecipes, Map<ResourceLocation, List<ResourceLocation>> spoofedRecipes, boolean silent, ICondition.IContext context)
	{
		RegisterRecipesEvent rre = new RegisterRecipesEvent(idInUse);
		HammerLib.postEvent(rre);
		rre.cleanup();
		
		if(!silent) HLConstants.LOG.info("Reloading HammerLib recipes...");
		
		AtomicLong count = new AtomicLong();
		
		var rem = rre.removedRecipes();
		
		rre.getRecipes().filter(rec -> !rem.contains(rec.getId())).forEach(recipe ->
		{
			addRecipe.accept(recipe);
			count.incrementAndGet();
		});
		
		removeRecipes.accept(rem);
		
		if(!silent)
			HLConstants.LOG.info("HammerLib injected {} recipes into recipe map. Removed {} recipes from the game.", count.longValue(), rem.size());
		
		List<AbstractRecipeRegistry<?, ?, ?>> registries = AbstractRecipeRegistry.getAllRegistries();
		if(!silent) HLConstants.LOG.info("Reloading {} custom registries.", registries.size());
		for(AbstractRecipeRegistry<?, ?, ?> registry : registries)
			registry.reload(null, context);
		if(!silent)
			HLConstants.LOG.info("{} custom registries reloaded, added {} total recipes.", registries.size(), registries.stream()
					.mapToInt(AbstractRecipeRegistry::getRecipeCount).sum());
	}
	
	public static void injectRecipes(RecipeManager mgr, ICondition.IContext context)
	{
		Internal.mutableManager(mgr);
		var spoofed = ((ISpoofedRecipeManager) mgr).getSpoofedRecipes();
		
		List<Recipe<?>> recipeList = new ArrayList<>();
		Set<ResourceLocation> removed = new HashSet<>();
		registerCustomRecipes(id -> mgr.byKey(id)
				.isPresent(), recipeList::add, removed::addAll, spoofed, false, context);
		Internal.addRecipes(mgr, recipeList);
		Internal.removeRecipes(mgr, removed::stream);
	}
	
	public static void injectRecipesCustom(Map<ResourceLocation, Recipe<?>> handler, Set<ResourceLocation> removed, Map<ResourceLocation, List<ResourceLocation>> spoofedRecipes, ICondition.IContext ctx)
	{
		registerCustomRecipes(handler::containsKey, r -> handler.put(r.getId(), r), removed::addAll, spoofedRecipes, false, ctx);
	}
	
	public static <C extends Container, T extends Recipe<C>> Map<ResourceLocation, T> getRecipeMap(Level level, RecipeType<T> type)
	{
		return level.getRecipeManager().byType(type);
	}
	
	public static <C extends Container, T extends Recipe<C>> Stream<T> getRecipes(Level level, RecipeType<T> type)
	{
		return getRecipeMap(level, type).values().stream();
	}
	
	public static class Internal
	{
		public static void addRecipes(RecipeManager mgr, Collection<Recipe<?>> recipes)
		{
			recipes.forEach(r ->
			{
				Map<ResourceLocation, Recipe<?>> map = mgr.recipes.computeIfAbsent(r.getType(), t -> new HashMap<>());
				map.putIfAbsent(r.getId(), r);
				mgr.byName.putAll(map);
			});
			HammerLib.LOG.info("Registered {} additional recipes.", recipes.size());
		}
		
		public static void removeRecipes(RecipeManager mgr, Supplier<Stream<ResourceLocation>> recipes)
		{
			recipes.get().forEach(id ->
			{
				mgr.byKey(id).ifPresent(recipe ->
				{
					var rmap = mgr.recipes.get(recipe.getType());
					if(rmap != null) rmap.remove(id);
					mgr.byName.remove(id);
				});
			});
		}
		
		public static void mutableManager(RecipeManager mgr)
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
	
	public static Ingredient composeIngredient(Object... comps)
	{
		return Ingredient.merge(
				Arrays.stream(comps)
						.map(RecipeHelper::fromComponent)
						.filter(i -> !i.isEmpty())
						.toList()
		);
	}
	
	public static Ingredient fromComponent(Object comp)
	{
		Ingredient ingr = Ingredient.EMPTY;
		if(comp instanceof Supplier<?> su)
			comp = su.get();
		if(comp instanceof ItemLike)
			ingr = Ingredient.of((ItemLike) comp);
		else if(comp instanceof IIngredientProvider)
			ingr = ((IIngredientProvider) comp).asIngredient();
		else if(comp instanceof ItemStack)
			ingr = Ingredient.of(((ItemStack) comp).copy());
		else if(comp instanceof TagKey<?>)
		{
			ingr = fromTag((TagKey<Item>) comp);
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
					return fromTag(getItemTag(tag));
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
			HammerLib.postEvent(event);
			if(event.hasIngredient())
				ingr = event.getIngredient();
		}
		return ingr;
	}
	
	public static TagKey<Item> getItemTag(ResourceLocation path)
	{
		return TagKey.create(ForgeRegistries.Keys.ITEMS, path);
	}
	
	public static Ingredient fromTag(TagKey<Item> tag)
	{
		return Ingredient.fromValues(Stream.of(new Ingredient.TagValue(tag)));
	}
}