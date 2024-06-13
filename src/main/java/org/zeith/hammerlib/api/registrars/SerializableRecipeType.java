package org.zeith.hammerlib.api.registrars;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.RegisterEvent;
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
	 * Serializes a recipe to a network buffer.
	 *
	 * @param buf
	 * 		the network buffer to serialize the recipe to
	 * @param recipe
	 * 		the recipe to be serialized
	 */
	public abstract void toNetwork(RegistryFriendlyByteBuf buf, T recipe);
	
	/**
	 * Deserializes a recipe from a network buffer.
	 *
	 * @param buf
	 * 		the network buffer containing the recipe data
	 *
	 * @return the deserialized recipe, or null if deserialization failed
	 */
	public abstract @Nullable T fromNetwork(RegistryFriendlyByteBuf buf);
	
	protected final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec = StreamCodec.of(this::toNetwork, this::fromNetwork);
	
	@Override
	public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec()
	{
		return streamCodec;
	}
	
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
		event.register(Registries.RECIPE_TYPE, id, Cast.constant(this));
		event.register(Registries.RECIPE_SERIALIZER, id, Cast.constant(this));
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
		return getClass().getSimpleName() + "{recipeTypeId=" + BuiltInRegistries.RECIPE_TYPE.getKey(this) + ",recipeSerializerId=" + BuiltInRegistries.RECIPE_SERIALIZER.getKey(this) + "}";
	}
	
	public ItemStack getToastSymbol(Recipe<?> recipe)
	{
		return new ItemStack(Blocks.CRAFTING_TABLE);
	}
}
