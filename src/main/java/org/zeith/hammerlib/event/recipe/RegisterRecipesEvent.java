package org.zeith.hammerlib.event.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.recipes.RecipeBuilderExtension;
import org.zeith.hammerlib.core.adapter.recipe.*;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * This event is fired on {@link org.zeith.hammerlib.HammerLib#EVENT_BUS}
 */
public class RegisterRecipesEvent
		extends Event
		implements IRecipeRegistrationEvent<Recipe<?>>
{
	private final List<Recipe<?>> recipes = Lists.newArrayList();
	private final Set<ResourceLocation> removeRecipes = Sets.newHashSet();
	private final Predicate<ResourceLocation> idInUse;
	
	private final Map<Class<?>, RecipeBuilderExtension> extensions;
	
	private final Map<ResourceLocation, List<ResourceLocation>> spoofedRecipes;
	
	public RegisterRecipesEvent(Predicate<ResourceLocation> idInUse, Map<ResourceLocation, List<ResourceLocation>> spoofedRecipes)
	{
		this.idInUse = idInUse;
		this.spoofedRecipes = spoofedRecipes;
		this.extensions = RecipeBuilderExtension.attach(this);
	}
	
	public Map<ResourceLocation, List<ResourceLocation>> getSpoofedRecipes()
	{
		return spoofedRecipes;
	}
	
	@Nullable
	public <T> T extension(Class<T> type)
	{
		return Cast.cast(extensions.get(type), type);
	}
	
	public void add(Recipe<?> recipe)
	{
		if(recipe != null)
			recipes.add(recipe);
	}
	
	public Set<ResourceLocation> removedRecipes()
	{
		return Collections.unmodifiableSet(removeRecipes);
	}
	
	public void removeRecipe(ResourceLocation id)
	{
		removeRecipes.add(id);
	}
	
	/**
	 * Removes the recipe with removeId, and points its location to a newRecipeId.
	 */
	public void redirectRecipe(ResourceLocation removeId, ResourceLocation newRecipeId)
	{
		removeRecipe(removeId);
		spoofedRecipes.computeIfAbsent(removeId, v -> new ArrayList<>()).add(newRecipeId);
	}
	
	public StoneCutterRecipeBuilder stoneCutting()
	{
		return new StoneCutterRecipeBuilder(this);
	}
	
	public SmokingRecipeBuilder smoking()
	{
		return new SmokingRecipeBuilder(this);
	}
	
	public BlastingRecipeBuilder blasting()
	{
		return new BlastingRecipeBuilder(this);
	}
	
	public CampfireRecipeBuilder campfire()
	{
		return new CampfireRecipeBuilder(this);
	}
	
	public SmeltingRecipeBuilder smelting()
	{
		return new SmeltingRecipeBuilder(this);
	}
	
	public ShapedRecipeBuilder shaped()
	{
		return new ShapedRecipeBuilder(this);
	}
	
	public ShapelessRecipeBuilder shapeless()
	{
		return new ShapelessRecipeBuilder(this);
	}
	
	public boolean isRecipeIdTaken(ResourceLocation id)
	{
		return idInUse.test(id) || recipes.stream().map(Recipe::getId).anyMatch(id::equals);
	}
	
	@Override
	public ResourceLocation nextId(Item item)
	{
		if(item == null || item == Items.AIR) return null;
		
		ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
		
		if(!isRecipeIdTaken(rl)) return rl;
		
		int lastIdx = 1;
		while(true)
		{
			rl = new ResourceLocation(rl.getNamespace(), rl.getPath() + "_" + (lastIdx++));
			if(!isRecipeIdTaken(rl)) return rl;
		}
	}
	
	@Override
	public void register(ResourceLocation id, Recipe<?> entry)
	{
		add(entry);
	}
	
	public Stream<Recipe<?>> getRecipes()
	{
		return recipes.stream();
	}
}