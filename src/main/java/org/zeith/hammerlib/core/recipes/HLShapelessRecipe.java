package org.zeith.hammerlib.core.recipes;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
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
	protected final @Getter List<IRemainingItemReplacer> inputModifier = new ArrayList<>();
	
	protected final ItemStack result;
	
	public HLShapelessRecipe(String group, CraftingBookCategory category, ItemStack result, List<Ingredient> ingredients)
	{
		super(group, category, result, ingredients);
		this.result = result;
	}
	
	public HLShapelessRecipe(String group, CraftingBookCategory category, ItemStack result, List<Ingredient> ingredients, List<IRemainingItemReplacer> replacers)
	{
		this(group, category, result, ingredients);
		this.inputModifier.addAll(replacers);
	}
	
	public HLShapelessRecipe(ShapelessRecipe vanilla, List<IRemainingItemReplacer> replacers)
	{
		this(vanilla.group(),
				vanilla.category(),
				vanilla.result,
				vanilla.ingredients,
				replacers
		);
	}
	
	public HLShapelessRecipe addReplacer(ResourceLocation id)
	{
		var m = RegistriesHL.REMAINING_REPLACER.getValue(id);
		if(m != null)
			inputModifier.add(m);
		return this;
	}
	
	public HLShapelessRecipe addReplacers(ResourceLocation... id)
	{
		for(var i : id) inputModifier.add(RegistriesHL.REMAINING_REPLACER.getValue(i));
		inputModifier.removeIf(Objects::isNull);
		return this;
	}
	
	public HLShapelessRecipe addReplacers(Iterable<ResourceLocation> id)
	{
		for(var i : id) inputModifier.add(RegistriesHL.REMAINING_REPLACER.getValue(i));
		inputModifier.removeIf(Objects::isNull);
		return this;
	}
	
	@Override
	public RecipeSerializer getSerializer()
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
		private static final MapCodec<HLShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(
				inst -> inst.group(
								Codec.STRING.optionalFieldOf("group", "").forGetter(HLShapelessRecipe::group),
								CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(HLShapelessRecipe::category),
								ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_301142_ -> p_301142_.result),
								Codec.lazyInitialized(() -> Ingredient.CODEC.listOf(1, ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth())).fieldOf("ingredients").forGetter(p_360071_ -> p_360071_.ingredients),
								IRemainingItemReplacer.LIST_CODEC.fieldOf("input_modifiers").forGetter(HLShapelessRecipe::getInputModifier)
						)
						.apply(inst, HLShapelessRecipe::new)
		);
		
		public static final StreamCodec<RegistryFriendlyByteBuf, HLShapelessRecipe> STREAM_CODEC = StreamCodec.composite(
				ShapelessRecipe.Serializer.STREAM_CODEC, r -> r,
				IRemainingItemReplacer.STREAM_CODEC, r -> r.inputModifier,
				HLShapelessRecipe::new
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
	}
}