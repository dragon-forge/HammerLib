package org.zeith.hammerlib.api.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.api.registrars.SerializableRecipeType;

public abstract class BaseRecipe<R extends BaseRecipe<R, INPUT>, INPUT extends RecipeInput>
		implements Recipe<INPUT>
{
	protected final SerializableRecipeType<R> type = getRecipeType();
	
	protected boolean isHidden;
	protected NonNullList<Ingredient> vanillaIngredients = NonNullList.create();
	protected ItemStack vanillaResult = ItemStack.EMPTY;
	
	public BaseRecipe()
	{
	}
	
	protected abstract SerializableRecipeType<R> getRecipeType();
	
	@Override
	public boolean isSpecial()
	{
		return isHidden;
	}
	
	@Override
	public boolean matches(INPUT container, Level level)
	{
		return false;
	}
	
	@Override
	public ItemStack assemble(INPUT container, HolderLookup.Provider provider)
	{
		return vanillaResult.copy();
	}
	
	@Override
	public RecipeSerializer<R> getSerializer()
	{
		return type;
	}
	
	@Override
	public RecipeType<R> getType()
	{
		return type;
	}
	
	@Override
	public PlacementInfo placementInfo()
	{
		return PlacementInfo.NOT_PLACEABLE;
	}
}
