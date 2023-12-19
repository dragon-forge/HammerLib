package org.zeith.hammerlib.core.recipes;

import com.google.common.collect.Sets;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.core.RegistriesHL;
import org.zeith.hammerlib.core.init.RecipesHL;
import org.zeith.hammerlib.core.recipes.replacers.IRemainingItemReplacer;

import javax.xml.crypto.Data;
import java.util.*;

public class HLShapedRecipe
		extends ShapedRecipe
{
	protected final List<IRemainingItemReplacer> inputModifier = new ArrayList<>();
	
	public HLShapedRecipe(String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result, boolean showNotification)
	{
		super(group, category, width, height, ingredients, result, showNotification);
	}
	
	public HLShapedRecipe(String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
	{
		super(group, category, width, height, ingredients, result);
	}
	
	public HLShapedRecipe addReplacer(ResourceLocation id)
	{
		var m = RegistriesHL.remainingReplacer().get(id);
		if(m != null)
			inputModifier.add(m);
		return this;
	}
	
	public HLShapedRecipe addReplacers(ResourceLocation... id)
	{
		for(var i : id) inputModifier.add(RegistriesHL.remainingReplacer().get(i));
		inputModifier.removeIf(Objects::isNull);
		return this;
	}
	
	public HLShapedRecipe addReplacers(Iterable<ResourceLocation> id)
	{
		for(var i : id) inputModifier.add(RegistriesHL.remainingReplacer().get(i));
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
		static int MAX_WIDTH = 3;
		static int MAX_HEIGHT = 3;
		
		static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().flatXmap(strList ->
		{
			if(strList.size() > MAX_HEIGHT)
			{
				return DataResult.error(() -> "Invalid pattern: too many rows, %s is maximum".formatted(MAX_HEIGHT));
			} else if(strList.isEmpty())
			{
				return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
			} else
			{
				int i = strList.get(0).length();
				
				for(String s : strList)
				{
					if(s.length() > MAX_WIDTH)
					{
						return DataResult.error(() -> "Invalid pattern: too many columns, %s is maximum".formatted(MAX_WIDTH));
					}
					
					if(i != s.length())
					{
						return DataResult.error(() -> "Invalid pattern: each row must be the same width");
					}
				}
				
				return DataResult.success(strList);
			}
		}, DataResult::success);
		
		static final Codec<String> SINGLE_CHARACTER_STRING_CODEC = Codec.STRING.flatXmap(s ->
		{
			if(s.length() != 1)
			{
				return DataResult.error(() -> "Invalid key entry: '" + s + "' is an invalid symbol (must be 1 character only).");
			} else
			{
				return " ".equals(s) ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") : DataResult.success(s);
			}
		}, DataResult::success);
		
		private static final Codec<HLShapedRecipe> CODEC = RawShapedRecipeHL.CODEC.flatXmap(raw ->
		{
			String[] astring = shrink(raw.pattern);
			int i = astring[0].length();
			int j = astring.length;
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
			Set<String> set = Sets.newHashSet(raw.key.keySet());
			
			for(int k = 0; k < astring.length; ++k)
			{
				String s = astring[k];
				
				for(int l = 0; l < s.length(); ++l)
				{
					String s1 = s.substring(l, l + 1);
					Ingredient ingredient = s1.equals(" ") ? Ingredient.EMPTY : raw.key.get(s1);
					if(ingredient == null)
					{
						return DataResult.error(() -> "Pattern references symbol '" + s1 + "' but it's not defined in the key");
					}
					
					set.remove(s1);
					nonnulllist.set(l + i * k, ingredient);
				}
			}
			
			if(!set.isEmpty())
			{
				return DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + set);
			} else
			{
				HLShapedRecipe shapedrecipe = new HLShapedRecipe(raw.group, raw.category, i, j, nonnulllist, raw.result, raw.showNotification);
				return DataResult.success(shapedrecipe);
			}
		}, recipe -> DataResult.error(() -> "Serializing ShapedRecipe is not implemented yet."));
		
		public HLSerializer()
		{
		}
		
		@Override
		public Codec<HLShapedRecipe> codec()
		{
			return CODEC;
		}
		
		@Override
		public HLShapedRecipe fromNetwork(FriendlyByteBuf buf)
		{
			var base = RecipeSerializer.SHAPED_RECIPE.fromNetwork(buf);
			var mod = new HLShapedRecipe(
					base.getGroup(),
					base.category(),
					base.getRecipeWidth(),
					base.getRecipeHeight(),
					base.getIngredients(),
					base.getResultItem(RegistryAccess.EMPTY),
					base.showNotification()
			);
			
			mod.inputModifier.addAll(IRemainingItemReplacer.fromNetwork(buf));
			
			return mod;
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buf, HLShapedRecipe r)
		{
			RecipeSerializer.SHAPED_RECIPE.toNetwork(buf, r);
			IRemainingItemReplacer.toNetwork(r.inputModifier, buf);
		}
		
		record RawShapedRecipeHL(
				String group,
				CraftingBookCategory category,
				Map<String, Ingredient> key,
				List<String> pattern,
				ItemStack result,
				boolean showNotification,
				List<IRemainingItemReplacer> replacers
		)
		{
			public static final Codec<RawShapedRecipeHL> CODEC = RecordCodecBuilder.create(
					inst -> inst.group(
							ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(RawShapedRecipeHL::group),
							CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(RawShapedRecipeHL::category),
							ExtraCodecs.strictUnboundedMap(SINGLE_CHARACTER_STRING_CODEC, Ingredient.CODEC_NONEMPTY)
									.fieldOf("key")
									.forGetter(RawShapedRecipeHL::key),
							PATTERN_CODEC.fieldOf("pattern").forGetter(RawShapedRecipeHL::pattern),
							CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(RawShapedRecipeHL::result),
							ExtraCodecs.strictOptionalField(Codec.BOOL, "show_notification", true).forGetter(RawShapedRecipeHL::showNotification),
							RegistryMapping.registryCodec(RegistriesHL.Keys.REMAINING_ITEM_REPLACER).listOf().fieldOf("replacers").forGetter(RawShapedRecipeHL::replacers)
					).apply(inst, RawShapedRecipeHL::new)
			);
		}
	}
}