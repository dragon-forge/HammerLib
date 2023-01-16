package org.zeith.hammerlib.api.recipes;

import com.google.gson.JsonElement;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record IngredientWithCount(Ingredient input, int count)
{
	public static final IngredientWithCount EMPTY = new IngredientWithCount(Ingredient.EMPTY, 0);
	
	public void toNetwork(FriendlyByteBuf buf)
	{
		input.toNetwork(buf);
		buf.writeVarInt(count);
	}
	
	public boolean isEmpty()
	{
		return input.isEmpty() || count <= 0;
	}
	
	public NonNullList<Ingredient> applyCount()
	{
		return NonNullList.withSize(count, input);
	}
	
	public static IngredientWithCount fromJson(JsonElement obj)
	{
		var input = Ingredient.fromJson(obj);
		var count = GsonHelper.getAsInt(obj.getAsJsonObject(), "count", 1);
		return new IngredientWithCount(input, count);
	}
	
	public static IngredientWithCount fromNetwork(FriendlyByteBuf buf)
	{
		var input = Ingredient.fromNetwork(buf);
		var count = buf.readVarInt();
		return new IngredientWithCount(input, count);
	}
	
	public boolean test(ItemStack item)
	{
		return input.test(item) && item.getCount() >= count;
	}
}