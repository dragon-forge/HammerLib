package org.zeith.hammerlib.core.recipes;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.core.RegistriesHL;
import org.zeith.hammerlib.core.init.RecipesHL;
import org.zeith.hammerlib.core.recipes.replacers.IRemainingItemReplacer;

import java.util.*;

public class HLShapelessRecipe
		extends ShapelessRecipe
{
	protected final List<IRemainingItemReplacer> inputModifier = new ArrayList<>();
	
	protected final ItemStack result;
	
	public HLShapelessRecipe(String group, CraftingBookCategory category, NonNullList<Ingredient> ingredients, ItemStack result)
	{
		super(group, category, result, ingredients);
		this.result = result;
	}
	
	public HLShapelessRecipe(String group, CraftingBookCategory category, NonNullList<Ingredient> ingredients, ItemStack result, List<IRemainingItemReplacer> replacers)
	{
		this(group, category, ingredients, result);
		this.inputModifier.addAll(replacers);
	}
	
	public HLShapelessRecipe addReplacer(ResourceLocation id)
	{
		var m = RegistriesHL.REMAINING_REPLACER.get(id);
		if(m != null)
			inputModifier.add(m);
		return this;
	}
	
	public HLShapelessRecipe addReplacers(ResourceLocation... id)
	{
		for(var i : id) inputModifier.add(RegistriesHL.REMAINING_REPLACER.get(i));
		inputModifier.removeIf(Objects::isNull);
		return this;
	}
	
	public HLShapelessRecipe addReplacers(Iterable<ResourceLocation> id)
	{
		for(var i : id) inputModifier.add(RegistriesHL.REMAINING_REPLACER.get(i));
		inputModifier.removeIf(Objects::isNull);
		return this;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return RecipesHL.SHAPELESS_HL_SERIALIZER;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput ctr)
	{
		var remaining = super.getRemainingItems(ctr);
		for(var replacer : inputModifier)
			for(int i = 0; i < remaining.size(); i++)
				remaining.set(i, replacer.replace(ctr, i, remaining.get(i)));
		return remaining;
	}
	
	public static class HLSerializer
			implements RecipeSerializer<HLShapelessRecipe>
	{
		static int MAX_WIDTH = 3;
		static int MAX_HEIGHT = 3;
		
		private static final MapCodec<HLShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(
				inst -> inst.group(
								Codec.STRING.optionalFieldOf("group", "").forGetter(ShapelessRecipe::getGroup),
								CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapelessRecipe::category),
								Ingredient.CODEC_NONEMPTY
										.listOf()
										.fieldOf("ingredients")
										.flatXmap(ingredientList ->
										{
											if(ingredientList.isEmpty())
											{
												return DataResult.error(() -> "No ingredients for shapeless recipe");
											} else
											{
												Ingredient[] ings = ingredientList.toArray(Ingredient[]::new); //Forge skip the empty check and immediately create the array.
												return ings.length > MAX_HEIGHT * MAX_WIDTH
													   ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(MAX_HEIGHT * MAX_WIDTH))
													   : DataResult.success(NonNullList.of(Ingredient.EMPTY, ings));
											}
										}, DataResult::success)
										.forGetter(ShapelessRecipe::getIngredients),
								ItemStack.CODEC.fieldOf("result").forGetter(hlr -> hlr.result),
								RegistryMapping.registryCodec(RegistriesHL.Keys.REMAINING_ITEM_REPLACER).listOf().fieldOf("replacers").forGetter(hlr -> hlr.inputModifier)
						)
						.apply(inst, HLShapelessRecipe::new)
		);
		
		public static final StreamCodec<RegistryFriendlyByteBuf, HLShapelessRecipe> STREAM_CODEC = StreamCodec.of(
				HLShapelessRecipe.HLSerializer::toNetwork, HLShapelessRecipe.HLSerializer::fromNetwork
		);
		
		public HLSerializer()
		{
		}
		
		@Override
		public MapCodec<HLShapelessRecipe> codec()
		{
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, HLShapelessRecipe> streamCodec()
		{
			return STREAM_CODEC;
		}
		
		public static HLShapelessRecipe fromNetwork(RegistryFriendlyByteBuf buf)
		{
			var base = RecipeSerializer.SHAPELESS_RECIPE.streamCodec().decode(buf);
			var mod = new HLShapelessRecipe(base.getGroup(), base.category(), base.getIngredients(), base.getResultItem(RegistryAccess.EMPTY));
			
			mod.inputModifier.addAll(IRemainingItemReplacer.fromNetwork(buf));
			
			return mod;
		}
		
		public static void toNetwork(RegistryFriendlyByteBuf buf, HLShapelessRecipe r)
		{
			RecipeSerializer.SHAPELESS_RECIPE.streamCodec().encode(buf, r);
			
			IRemainingItemReplacer.toNetwork(r.inputModifier, buf);
		}
	}
}