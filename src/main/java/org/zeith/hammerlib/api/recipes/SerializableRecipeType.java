package org.zeith.hammerlib.api.recipes;

import com.google.gson.*;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.abstractions.recipes.IRecipeVisualizer;
import org.zeith.hammerlib.abstractions.recipes.IVisualizedRecipeType;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;
import org.zeith.hammerlib.util.java.Cast;

import java.util.function.Consumer;

/**
 * Abstract class for serializable recipe types.
 *
 * @param <T>
 * 		the type of recipe being serialized
 * 		Implements {@link RecipeType}, {@link RecipeSerializer}, and {@link ICustomRegistrar}.
 */
public abstract class SerializableRecipeType<T extends Recipe<?>>
		implements RecipeType<T>, RecipeSerializer<T>, ICustomRegistrar, IVisualizedRecipeType<T>
{
	public static final String DEFAULT_INGREDIENTS_KEY = "ingredients";
	public static final String DEFAULT_INGREDIENT_KEY = "ingredient";
	public static final String DEFAULT_OUTPUT_KEY = "result";
	public static final String DEFAULT_GROUP_KEY = "group";
	
	/**
	 * Deserializes a recipe from a JSON object.
	 *
	 * @param recipeLoc
	 * 		the location of the recipe
	 * @param recipeJson
	 * 		the JSON object containing the recipe data
	 *
	 * @return the deserialized recipe
	 */
	@Override
	public abstract T fromJson(ResourceLocation recipeLoc, JsonObject recipeJson);
	
	/**
	 * Deserializes a recipe from a network buffer.
	 *
	 * @param recipeLoc
	 * 		the location of the recipe
	 * @param buf
	 * 		the network buffer containing the recipe data
	 *
	 * @return the deserialized recipe, or null if deserialization failed
	 */
	@Override
	public abstract @Nullable T fromNetwork(ResourceLocation recipeLoc, FriendlyByteBuf buf);
	
	/**
	 * Serializes a recipe to a network buffer.
	 *
	 * @param buf
	 * 		the network buffer to serialize the recipe to
	 * @param recipe
	 * 		the recipe to be serialized
	 */
	@Override
	public abstract void toNetwork(FriendlyByteBuf buf, T recipe);
	
	/**
	 * Registers the recipe type and serializer with the Forge registry, using @{@link org.zeith.hammerlib.annotations.SimplyRegister} and @{@link org.zeith.hammerlib.annotations.RegistryName}.
	 *
	 * @param event
	 * 		the register event
	 * @param id
	 * 		the registry ID of the recipe type and serializer
	 */
	@Override
	public void performRegister(RegisterEvent event, ResourceLocation id)
	{
		event.register(ForgeRegistries.Keys.RECIPE_TYPES, id, Cast.constant(this));
		event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, id, Cast.constant(this));
	}
	
	@Override
	public void initVisuals(Consumer<IRecipeVisualizer<T, ?>> viualizerConsumer)
	{
	}
	
	/**
	 * Returns a string representation of the recipe type and its associated registry ID.
	 *
	 * @return a string in the format "ClassName{recipeTypeId=[recipeTypeID],recipeSerializerId=[recipeSerializerID]}"
	 */
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "{recipeTypeId=" + ForgeRegistries.RECIPE_TYPES.getKey(this) + ",recipeSerializerId=" + ForgeRegistries.RECIPE_SERIALIZERS.getKey(this) + "}";
	}
	
	public ItemStack getToastSymbol(Recipe<?> recipe)
	{
		return new ItemStack(Blocks.CRAFTING_TABLE);
	}
	
	public static Ingredient ingredientFromJson(JsonElement obj)
	{
		return Ingredient.fromJson(obj);
	}
	
	public static NonNullList<Ingredient> ingredientsFromArray(JsonArray array)
	{
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		for(int i = 0; i < array.size(); ++i) nonnulllist.add(Ingredient.fromJson(array.get(i)));
		return nonnulllist;
	}
	
	public static NonNullList<IngredientWithCount> ingredientsWithCountFromArray(JsonArray array)
	{
		NonNullList<IngredientWithCount> nonnulllist = NonNullList.create();
		for(int i = 0; i < array.size(); ++i) nonnulllist.add(IngredientWithCount.fromJson(array.get(i)));
		return nonnulllist;
	}
	
	public static ItemStack itemStackFromJson(JsonObject obj)
	{
		return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(obj, true, true);
	}
	
	public static ItemStack itemStackOrEmptyFromJson(JsonObject obj)
	{
		return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(obj, true, false);
	}
	
	public static Item itemFromJson(JsonObject obj)
	{
		String s = GsonHelper.getAsString(obj, "item");
		Item item = BuiltInRegistries.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + s + "'"));
		if(item == Items.AIR) throw new JsonSyntaxException("Invalid item: " + s);
		else return item;
	}
}
