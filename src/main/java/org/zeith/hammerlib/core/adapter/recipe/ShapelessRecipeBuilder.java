package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.RegistriesHL;
import org.zeith.hammerlib.core.recipes.HLShapelessRecipe;
import org.zeith.hammerlib.core.recipes.replacers.IRemainingItemReplacer;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

import java.util.*;
import java.util.stream.Stream;

public class ShapelessRecipeBuilder
		extends RecipeBuilder<ShapelessRecipeBuilder>
{
	protected final NonNullList<Ingredient> ingredients = NonNullList.create();
	protected final List<ResourceLocation> replacers = new ArrayList<>();
	protected CraftingBookCategory category = CraftingBookCategory.MISC;
	
	public ShapelessRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}
	
	public ShapelessRecipeBuilder replacers(IRemainingItemReplacer... replacers)
	{
		Stream.of(replacers).map(RegistriesHL.REMAINING_REPLACER::getKey).filter(Objects::nonNull).forEach(this.replacers::add);
		return this;
	}
	
	public ShapelessRecipeBuilder category(CraftingBookCategory cat)
	{
		this.category = cat;
		return this;
	}
	
	public ShapelessRecipeBuilder add(Object ingredient)
	{
		this.ingredients.add(RecipeHelper.fromComponent(itemRegistry, ingredient));
		return this;
	}
	
	public ShapelessRecipeBuilder addAll(Object... ingredients)
	{
		for(Object ingredient : ingredients) this.ingredients.add(RecipeHelper.fromComponent(itemRegistry, ingredient));
		return this;
	}
	
	public ShapelessRecipeBuilder addAll(Iterable<Object> ingredients)
	{
		for(Object ingredient : ingredients) this.ingredients.add(RecipeHelper.fromComponent(itemRegistry, ingredient));
		return this;
	}
	
	@Override
	protected void validate()
	{
		super.validate();
		if(ingredients.isEmpty())
			throw new IllegalStateException(getClass().getSimpleName() + " does not have any defined ingredients!");
	}
	
	@Override
	protected Recipe<?> createRecipe()
	{
		var rec = new HLShapelessRecipe(group, category, result, ingredients);
		rec.addReplacers(replacers);
		return rec;
	}
}