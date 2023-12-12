package org.zeith.hammerlib.event.recipe;

import com.google.common.collect.*;
import lombok.var;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus;
import org.zeith.hammerlib.api.recipes.RecipeBuilderExtension;
import org.zeith.hammerlib.core.adapter.recipe.*;
import org.zeith.hammerlib.util.mcf.RecipeRegistrationContext;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RegisterRecipesEvent
		extends Event
{
	private final List<IRecipe<?>> recipes = Lists.newArrayList();
	private final Set<ResourceLocation> removeRecipes = Sets.newHashSet();
	protected final Predicate<ResourceLocation> idInUse;
	private final Map<String, RecipeRegistrationContext> contextMap = Maps.newHashMap();
	private final Map<Class<?>, RecipeBuilderExtension> extensions = RecipeBuilderExtension.attach(this);
	
	public RegisterRecipesEvent(Predicate<ResourceLocation> idInUse)
	{
		this.idInUse = idInUse;
	}
	
	public void add(IRecipe<?> recipe)
	{
		if(recipe != null && enableRecipe(recipe.getType(), recipe.getId()))
			recipes.add(recipe);
	}
	
	public StoneCuttingRecipeBuilder stoneCutting()
	{
		return new StoneCuttingRecipeBuilder(this);
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
		return idInUse.test(id) || recipes.stream().map(IRecipe::getId).anyMatch(id::equals);
	}
	
	public ResourceLocation nextId(Item item)
	{
		if(item == null || item == Items.AIR) return null;
		
		ResourceLocation rl = item.getRegistryName();
		
		if(!isRecipeIdTaken(rl)) return rl;
		
		int lastIdx = 1;
		while(true)
		{
			var tf = new ResourceLocation(rl.getNamespace(), rl.getPath() + "_" + (lastIdx++));
			if(!isRecipeIdTaken(tf)) return tf;
		}
	}
	
	public Set<ResourceLocation> removedRecipes()
	{
		return Collections.unmodifiableSet(removeRecipes);
	}
	
	public RegisterRecipesEvent removeRecipe(ResourceLocation id)
	{
		removeRecipes.add(id);
		return this;
	}
	
	public Stream<IRecipe<?>> getRecipes()
	{
		return recipes.stream();
	}
	
	public boolean enableRecipe(IRecipeType<?> type, ResourceLocation recipeId)
	{
		return getContext(recipeId.getNamespace()).enableRecipe(type, recipeId);
	}
	
	public boolean enableRecipe(IRecipe<?> recipe)
	{
		return enableRecipe(recipe.getType(), recipe.getId());
	}
	
	public RecipeRegistrationContext getContext(String modid)
	{
		return contextMap.computeIfAbsent(modid, RecipeRegistrationContext::load);
	}
	
	@ApiStatus.Internal
	public void cleanup()
	{
		for(RecipeRegistrationContext value : contextMap.values())
			value.save();
		contextMap.clear();
	}
}