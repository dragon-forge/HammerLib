package org.zeith.hammerlib.core.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.zeith.hammerlib.core.init.RecipesHL;
import org.zeith.hammerlib.core.recipes.replacers.*;

import java.util.*;

public class HLShapedRecipe
		extends ShapedRecipe
{
	protected final List<IRemainingItemReplacer> inputModifier = new ArrayList<>();
	
	public HLShapedRecipe(ResourceLocation id, String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
	{
		super(id, group, category, width, height, ingredients, result);
	}
	
	public HLShapedRecipe addReplacer(ResourceLocation id)
	{
		var m = RemainingReplacerRegistrar.get(id);
		if(m != null)
			inputModifier.add(m);
		return this;
	}
	
	public HLShapedRecipe addReplacers(ResourceLocation... id)
	{
		for(var i : id) inputModifier.add(RemainingReplacerRegistrar.get(i));
		inputModifier.removeIf(Objects::isNull);
		return this;
	}
	
	public HLShapedRecipe addReplacers(Iterable<ResourceLocation> id)
	{
		for(var i : id) inputModifier.add(RemainingReplacerRegistrar.get(i));
		inputModifier.removeIf(Objects::isNull);
		return this;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return RecipesHL.SHAPED_HL_SERIALIZER;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer ctr)
	{
		var remaining = super.getRemainingItems(ctr);
		for(var replacer : inputModifier)
			for(int i = 0; i < remaining.size(); i++)
				remaining.set(i, replacer.replace(ctr, i, remaining.get(i)));
		return remaining;
	}
	
	public static class HLSerializer
			implements RecipeSerializer<HLShapedRecipe>
	{
		public HLSerializer()
		{
		}
		
		@Override
		public HLShapedRecipe fromJson(ResourceLocation id, JsonObject json)
		{
			var base = RecipeSerializer.SHAPED_RECIPE.fromJson(id, json);
			var mod = new HLShapedRecipe(base.getId(), base.getGroup(), base.category(), base.getRecipeWidth(), base.getRecipeHeight(), base.getIngredients(), base.getResultItem(RegistryAccess.EMPTY));
			
			if(json.has("hl_replacers"))
				mod.inputModifier.addAll(RemainingReplacerRegistrar.fromJson(json.getAsJsonArray("hl_replacers")));
			
			return mod;
		}
		
		@Override
		public HLShapedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
		{
			var base = RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buf);
			var mod = new HLShapedRecipe(base.getId(), base.getGroup(), base.category(), base.getRecipeWidth(), base.getRecipeHeight(), base.getIngredients(), base.getResultItem(RegistryAccess.EMPTY));
			
			mod.inputModifier.addAll(RemainingReplacerRegistrar.fromNetwork(buf));
			
			return mod;
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buf, HLShapedRecipe r)
		{
			RecipeSerializer.SHAPED_RECIPE.toNetwork(buf, r);
			
			RemainingReplacerRegistrar.toNetwork(r.inputModifier, buf);
		}
	}
}