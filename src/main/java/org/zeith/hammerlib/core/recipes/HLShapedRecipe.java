package org.zeith.hammerlib.core.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.zeith.hammerlib.core.RegistriesHL;
import org.zeith.hammerlib.core.init.RecipesHL;
import org.zeith.hammerlib.core.recipes.replacers.IRemainingItemReplacer;

import java.util.*;

@Getter
public class HLShapedRecipe
		extends ShapedRecipe
{
	protected final List<IRemainingItemReplacer> inputModifier = new ArrayList<>();
	
	public HLShapedRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification)
	{
		super(group, category, pattern, result, showNotification);
	}
	
	public HLShapedRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification, List<IRemainingItemReplacer> replacers)
	{
		super(group, category, pattern, result, showNotification);
		inputModifier.addAll(replacers);
	}
	
	public HLShapedRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result)
	{
		super(group, category, pattern, result);
	}
	
	public HLShapedRecipe(ShapedRecipe base, List<IRemainingItemReplacer> replacers)
	{
		this(
				base.group,
				base.category,
				base.pattern,
				base.result,
				base.showNotification
		);
		inputModifier.addAll(replacers);
	}
	
	public HLShapedRecipe addReplacer(ResourceLocation id)
	{
		var m = RegistriesHL.REMAINING_REPLACER.getValue(id);
		if(m != null)
			inputModifier.add(m);
		return this;
	}
	
	public HLShapedRecipe addReplacers(ResourceLocation... id)
	{
		for(var i : id) inputModifier.add(RegistriesHL.REMAINING_REPLACER.getValue(i));
		inputModifier.removeIf(Objects::isNull);
		return this;
	}
	
	public HLShapedRecipe addReplacers(Iterable<ResourceLocation> id)
	{
		for(var i : id) inputModifier.add(RegistriesHL.REMAINING_REPLACER.getValue(i));
		inputModifier.removeIf(Objects::isNull);
		return this;
	}
	
	@Override
	public RecipeSerializer<? extends ShapedRecipe> getSerializer()
	{
		return RecipesHL.SHAPED_HL_SERIALIZER;
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
	
	static String[] shrink(List<String> lst)
	{
		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;
		
		for(int i1 = 0; i1 < lst.size(); ++i1)
		{
			String s = lst.get(i1);
			i = Math.min(i, firstNonSpace(s));
			int j1 = lastNonSpace(s);
			j = Math.max(j, j1);
			if(j1 < 0)
			{
				if(k == i1)
				{
					++k;
				}
				
				++l;
			} else
			{
				l = 0;
			}
		}
		
		if(lst.size() == l)
		{
			return new String[0];
		} else
		{
			String[] v = new String[lst.size() - l - k];
			for(int k1 = 0; k1 < v.length; ++k1) v[k1] = lst.get(k1 + k).substring(i, j + 1);
			return v;
		}
	}
	
	private static int firstNonSpace(String str)
	{
		int i = 0;
		while(i < str.length() && str.charAt(i) == ' ') ++i;
		return i;
	}
	
	private static int lastNonSpace(String str)
	{
		int i = str.length() - 1;
		while(i >= 0 && str.charAt(i) == ' ') --i;
		return i;
	}
	
	public static class HLSerializer
			implements RecipeSerializer<HLShapedRecipe>
	{
		public static final MapCodec<HLShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
				inst.group(
						Codec.STRING.optionalFieldOf("group", "").forGetter(HLShapedRecipe::group),
						CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(i -> i.category),
						ShapedRecipePattern.MAP_CODEC.forGetter(i -> i.pattern),
						ItemStack.CODEC.fieldOf("result").forGetter(i -> i.result),
						Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(HLShapedRecipe::showNotification),
						IRemainingItemReplacer.LIST_CODEC.fieldOf("input_modifiers").forGetter(HLShapedRecipe::getInputModifier)
				).apply(inst, HLShapedRecipe::new)
		);
		
		public static final StreamCodec<RegistryFriendlyByteBuf, HLShapedRecipe> STREAM_CODEC = StreamCodec.composite(
				ShapedRecipe.Serializer.STREAM_CODEC, r -> r,
				IRemainingItemReplacer.STREAM_CODEC, HLShapedRecipe::getInputModifier,
				HLShapedRecipe::new
		);
		
		public HLSerializer()
		{
		}
		
		@Override
		public MapCodec<HLShapedRecipe> codec()
		{
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, HLShapedRecipe> streamCodec()
		{
			return STREAM_CODEC;
		}
	}
}