package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.zeith.hammerlib.api.recipes.IngredientWithCount;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public abstract class AbstractRecipeBuilder<R extends AbstractRecipeBuilder<R>>
{
	protected ResourceLocation identifier;
	protected String group = "";
	protected final IRecipeRegistrationEvent<Recipe<?>> event;
	
	protected final HolderLookup.RegistryLookup<Item> itemRegistry;
	
	public AbstractRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		this.event = event;
		this.itemRegistry = event.getItemLookup();
	}
	
	/**
	 * Optional
	 */
	public R id(ResourceLocation identifier)
	{
		this.identifier = identifier;
		return (R) this;
	}
	
	public R group(String group)
	{
		this.group = group;
		return (R) this;
	}
	
	public R group(RecipeGroup group)
	{
		this.group = group.toString();
		return (R) this;
	}
	
	public ResourceLocation getIdentifier()
	{
		if(this.identifier != null) return this.identifier;
		return this.identifier = event.nextId(getResultIdentifier());
	}
	
	protected abstract ResourceLocation getResultIdentifier();
	
	protected abstract void validate();
	
	protected abstract Recipe<?> createRecipe();
	
	public final void register()
	{
		validate();
		event.register(getIdentifier(), createRecipe());
	}
	
	public ResourceLocation registerAndGetId()
	{
		register();
		return getIdentifier();
	}
	
	public void registerIf(BooleanSupplier condition)
	{
		if(condition.getAsBoolean())
			register();
	}
	
	public void registerIf(Predicate<ResourceLocation> condition)
	{
		if(condition.test(identifier))
			register();
	}
	
	public Optional<ResourceLocation> registerIfAndGetId(BooleanSupplier condition)
	{
		if(condition.getAsBoolean())
		{
			register();
			return Optional.of(getIdentifier());
		}
		return Optional.empty();
	}
	
	public Optional<ResourceLocation> registerIfAndGetId(Predicate<ResourceLocation> condition)
	{
		if(condition.test(identifier))
		{
			register();
			return Optional.of(getIdentifier());
		}
		return Optional.empty();
	}
	
	protected Ingredient parseIngredient(Object obj)
	{
		return RecipeHelper.fromComponent(itemRegistry, obj);
	}
	
	protected IngredientWithCount parseIngredient(Object obj, int count)
	{
		return new IngredientWithCount(RecipeHelper.fromComponent(itemRegistry, obj), count);
	}
}