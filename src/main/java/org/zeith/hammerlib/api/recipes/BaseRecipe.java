package org.zeith.hammerlib.api.recipes;

import net.minecraft.core.*;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public abstract class BaseRecipe<R extends BaseRecipe<R>>
		implements Recipe<Container>
{
	protected final SerializableRecipeType<R> type = getRecipeType();
	
	protected String group = "";
	protected boolean isHidden;
	protected NonNullList<Ingredient> vanillaIngredients = NonNullList.create();
	protected ItemStack vanillaResult = ItemStack.EMPTY;
	
	public BaseRecipe( String group)
	{
		this.group = group == null ? "" : group;
	}
	
	protected abstract SerializableRecipeType<R> getRecipeType();
	
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		return vanillaIngredients;
	}
	
	@Override
	public boolean isSpecial()
	{
		return isHidden;
	}
	
	@Override
	public String getGroup()
	{
		return group;
	}
	
	@Override
	public ItemStack getToastSymbol()
	{
		return type.getToastSymbol(this);
	}
	
	@Override
	public boolean matches(Container container, Level level)
	{
		return false;
	}
	
	@Override
	public ItemStack assemble(Container container, RegistryAccess access)
	{
		return vanillaResult.copy();
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return false;
	}
	
	@Override
	public ItemStack getResultItem(RegistryAccess access)
	{
		return vanillaResult.copy();
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return type;
	}
	
	@Override
	public RecipeType<?> getType()
	{
		return type;
	}
}
