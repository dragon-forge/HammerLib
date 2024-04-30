package org.zeith.hammerlib.api.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record IngredientWithCount(Ingredient input, int count)
{
	public static final MapCodec<IngredientWithCount> CODEC = RecordCodecBuilder.mapCodec(inst ->
			inst.group(
					Ingredient.CODEC.fieldOf("input").forGetter(IngredientWithCount::input),
					Codec.INT.fieldOf("count").forGetter(IngredientWithCount::count)
			).apply(inst, IngredientWithCount::new)
	);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, IngredientWithCount> STREAM_CODEC = StreamCodec.composite(
			Ingredient.CONTENTS_STREAM_CODEC, IngredientWithCount::input,
			ByteBufCodecs.INT, IngredientWithCount::count,
			IngredientWithCount::new
	);
	
	public static final IngredientWithCount EMPTY = new IngredientWithCount(Ingredient.EMPTY, 0);
	
	public boolean isEmpty()
	{
		return input.isEmpty() || count <= 0;
	}
	
	public NonNullList<Ingredient> applyCount()
	{
		return NonNullList.withSize(count, input);
	}
	
	public boolean test(ItemStack item)
	{
		return input.test(item) && item.getCount() >= count;
	}
	
	public void toNetwork(RegistryFriendlyByteBuf buf)
	{
		STREAM_CODEC.encode(buf, this);
	}
	
	public static IngredientWithCount fromNetwork(RegistryFriendlyByteBuf buf)
	{
		return STREAM_CODEC.decode(buf);
	}
}