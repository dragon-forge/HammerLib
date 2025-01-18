package org.zeith.hammerlib.core;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.items.IIngredientProvider;
import org.zeith.hammerlib.core.adapter.OreDictionaryAdapter;
import org.zeith.hammerlib.core.recipes.ServerContext;
import org.zeith.hammerlib.event.ParseIngredientEvent;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.mixins.ContextAwareReloadListenerAccessor;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.Resources;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RecipeHelper
{
	public static final String NEOFORGE_MOD_ID_FOR_TAGS = "c";
	
	public static void registerCustomRecipes(
			Predicate<ResourceLocation> idInUse,
			Consumer<RecipeHolder<?>> addRecipe,
			Consumer<Set<ResourceLocation>> removeRecipes,
			boolean silent,
			ServerContext context
	)
	{
		RegisterRecipesEvent rre = new RegisterRecipesEvent(idInUse, context);
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
	
	public static RecipeMap performInjectionWizardry(RecipeManager mgr, RecipeMap recipes)
	{
		var ctx = ((ContextAwareReloadListenerAccessor) mgr).callGetContext();
		
		Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> addedRecipes = new HashMap<>();
		Set<ResourceKey<Recipe<?>>> removedRecipes = new HashSet<>();
		
		RecipeHelper.injectRecipes(mgr, ctx,
				id -> recipes.byKey(id) != null || addedRecipes.containsKey(id),
				holder -> addedRecipes.put(holder.id(), holder),
				removedRecipes::add
		);
		
		addedRecipes.keySet().removeAll(removedRecipes);
		
		return patchRecipeMap(recipes, addedRecipes, removedRecipes);
	}
	
	public static RecipeMap patchRecipeMap(
			RecipeMap input,
			Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> add,
			Set<ResourceKey<Recipe<?>>> remove
	)
	{
		return RecipeMap.create(
				Stream.concat(input.values().stream(), add.values().stream())
				.filter(h->!remove.contains(h.id()))
				.toList()
		);
	}
	
	public static void injectRecipes(RecipeManager mgr, ICondition.IContext context, Predicate<ResourceKey<Recipe<?>>> recipeIdUsed, Consumer<RecipeHolder<?>> registrar, Consumer<ResourceKey<Recipe<?>>> delete)
	{
		registerCustomRecipes(loc -> recipeIdUsed.test(RegisterRecipesEvent.key(loc)), registrar, s -> s.stream().map(RegisterRecipesEvent::key).forEach(delete), false, ServerContext.gather(mgr, context));
	}
	
	public static <C extends RecipeInput, T extends Recipe<C>> Collection<RecipeHolder<T>> getRecipeMap(ServerLevel level, RecipeType<T> type)
	{
		return level.recipeAccess().recipes.byType(type);
	}
	
	public static <C extends RecipeInput, T extends Recipe<C>> Stream<RecipeHolder<T>> getRecipeHolders(ServerLevel level, RecipeType<T> type)
	{
		return getRecipeMap(level, type).stream();
	}
	
	public static <C extends RecipeInput, T extends Recipe<C>> Stream<T> getRecipes(ServerLevel level, RecipeType<T> type)
	{
		return getRecipeHolders(level, type).map(RecipeHolder::value);
	}
	
	public static ItemStack cycleIngredientStack(ContextMap context, Ingredient ingr, long displayDurationMS)
	{
		if(ingr == null) return ItemStack.EMPTY;
		var items = ingr.display().resolveForStacks(context);
		return items.get((int) ((System.currentTimeMillis() % (items.size() * displayDurationMS)) / displayDurationMS) % items.size());
	}
	
	public static Ingredient merge(List<Ingredient> comps)
	{
		return merge(comps.stream());
	}
	
	public static Ingredient merge(Stream<Ingredient> comps)
	{
		return CompoundIngredient.of(comps.toArray(Ingredient[]::new));
	}
	
	public static Ingredient fromComponent(HolderGetter<Item> lookup, Object comp)
	{
		if(comp == null) return null;
		
		if(comp instanceof Ingredient i) return i;
		if(comp instanceof Supplier<?> su) return fromComponent(lookup, su.get());
		if(comp instanceof ItemLike il) return Ingredient.of(il);
		if(comp instanceof IIngredientProvider ip) return ip.asIngredient();
		if(comp instanceof ItemStack is) return Ingredient.of(is.getItem());
		if(comp instanceof TagKey<?>) return fromTag(lookup, (TagKey<Item>) comp);
		
		if(comp instanceof ItemStack[] items)
			return Ingredient.of(Arrays.stream(items).map(ItemStack::getItem));
		
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
					else tag = Resources.location(st.contains(":") ? st : (NEOFORGE_MOD_ID_FOR_TAGS + ":" + st));
					
					return fromTag(lookup, getItemTag(tag));
				}
				return null;
			}));
		}
		
		ParseIngredientEvent event = new ParseIngredientEvent(comp);
		HammerLib.postEvent(event);
		if(event.hasIngredient()) return event.getIngredient();
		
		if(comp.getClass().isArray())
		{
			var len = Array.getLength(comp);
			return merge(IntStream.range(0, len)
					.mapToObj(i -> fromComponent(lookup, Array.get(comp, i)))
			);
		} else if(comp instanceof Collection<?> col)
		{
			return merge(col.stream()
					.map(e -> fromComponent(lookup, e))
			);
		}
		
		return Ingredient.of(Stream.empty());
	}
	
	public static TagKey<Item> getItemTag(ResourceLocation path)
	{
		return TagKey.create(BuiltInRegistries.ITEM.key(), path);
	}
	
	public static Ingredient fromTag(HolderGetter<Item> provider, TagKey<Item> tag)
	{
		return Ingredient.of(provider.getOrThrow(tag));
	}
}