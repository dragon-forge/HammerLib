package org.zeith.hammerlib.event.recipe;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.zeith.hammerlib.core.adapter.recipe.*;

import java.util.List;
import java.util.stream.Stream;

/**
 * This event is fired on {@link org.zeith.hammerlib.HammerLib#EVENT_BUS}
 */
public class RegisterRecipesEvent
		extends Event
{
	private final List<Recipe<?>> recipes = Lists.newArrayList();

	public void add(Recipe<?> recipe)
	{
		if(recipe != null)
			recipes.add(recipe);
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

	private int lastRecipeID;

	public ResourceLocation nextId(Item item)
	{
		ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
		return new ResourceLocation(rl.getNamespace(), rl.getPath() + "/" + (lastRecipeID++));
	}

	public Stream<Recipe<?>> getRecipes()
	{
		return recipes.stream();
	}
}