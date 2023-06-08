package org.zeith.hammerlib.api.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Objects;

public abstract class BaseRecipe<R extends BaseRecipe<R>>
		implements Recipe<Container>
{
	protected final SerializableRecipeType<R> type = getRecipeType();
	protected final ResourceLocation id;
	
	protected String group = "";
	protected boolean isHidden;
	protected NonNullList<Ingredient> vanillaIngredients = NonNullList.create();
	protected ItemStack vanillaResult = ItemStack.EMPTY;
	
	public BaseRecipe(ResourceLocation id, String group)
	{
		this.id = Objects.requireNonNull(id, "Recipe ID can not be null.");
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
	public ResourceLocation getId()
	{
		return id;
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
