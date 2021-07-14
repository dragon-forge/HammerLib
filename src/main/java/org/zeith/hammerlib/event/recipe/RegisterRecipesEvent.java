package org.zeith.hammerlib.event.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import org.zeith.hammerlib.core.adapter.recipe.*;

import java.util.List;
import java.util.stream.Stream;

public class RegisterRecipesEvent
		extends Event
{
	private final List<IRecipe<?>> recipes = Lists.newArrayList();

	public void add(IRecipe<?> recipe)
	{
		if(recipe != null && recipe.getType() != null && recipe.getId() != null)
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

	private int lastRecipeID;

	public ResourceLocation nextId(Item item)
	{
		ResourceLocation rl = item.getRegistryName();
		return new ResourceLocation(rl.getNamespace(), rl.getPath() + "/" + (lastRecipeID++));
	}

	public Stream<IRecipe<?>> getRecipes()
	{
		return recipes.stream();
	}
}