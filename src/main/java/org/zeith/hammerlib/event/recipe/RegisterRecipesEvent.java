package org.zeith.hammerlib.event.recipe;

import com.google.common.collect.*;
import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.recipes.RecipeBuilderExtension;
import org.zeith.hammerlib.api.recipes.RegisterExt;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.adapter.recipe.*;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.RecipeRegistrationContext;
import org.zeith.hammerlib.util.mcf.Resources;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * This event is fired on {@link org.zeith.hammerlib.HammerLib#EVENT_BUS}
 */
public class RegisterRecipesEvent
		extends Event
		implements IRecipeRegistrationEvent<Recipe<?>>, IModBusEvent
{
	protected final HolderLookup.Provider registries;
	private final HolderLookup.RegistryLookup<Item> itemRegistry;
	protected final @Getter ICondition.IContext context;
	private final List<RecipeHolder<?>> recipes = Lists.newArrayList();
	private final Set<ResourceLocation> removeRecipes = Sets.newHashSet();
	private final Predicate<ResourceLocation> idInUse;
	
	private final Map<String, RecipeRegistrationContext> contextMap = Maps.newHashMap();
	
	private final Map<Class<?>, RecipeBuilderExtension> extensions;
	
	public RegisterRecipesEvent(HolderLookup.Provider registries, ICondition.IContext context, Predicate<ResourceLocation> idInUse)
	{
		this.registries = registries;
		this.context = context;
		this.idInUse = idInUse;
		this.extensions = RecipeBuilderExtension.attach(this);
		this.itemRegistry = registries.lookupOrThrow(Registries.ITEM);
	}
	
	/**
	 * Returns a non-null instance of the provided extension class if the class has the @{@link RegisterExt} annotation.
	 *
	 * @param type
	 * 		The class object representing the extension type.
	 * @param <T>
	 * 		The type parameter representing the extension class.
	 *
	 * @return A non-null instance of the provided extension class if the class has the @{@link RegisterExt} annotation.
	 *
	 * @throws IllegalArgumentException
	 * 		If the provided extension class does not have the @{@link RegisterExt} annotation.
	 */
	@Nullable
	public <T extends RecipeBuilderExtension> T extension(Class<T> type)
	{
		return Cast.cast(extensions.get(type), type);
	}
	
	public void add(RecipeHolder<?> recipe)
	{
		// Not sure if it's a good idea, but sure, let's do it.
		if(recipe == null || !enableRecipe(recipe)) return;
		recipes.add(recipe);
	}
	
	/**
	 * Registers a recipe.
	 *
	 * @param recipe
	 * 		the recipe to register
	 *
	 * @return {@code true} if the recipe is enabled via configs and was successfully registered, {@code false} otherwise
	 */
	public boolean register(RecipeHolder<?> recipe)
	{
		if(recipe != null && enableRecipe(recipe.value().getType(), recipe.id().location()))
		{
			recipes.add(recipe);
			return true;
		}
		
		return false;
	}
	
	public Set<ResourceLocation> removedRecipes()
	{
		return Collections.unmodifiableSet(removeRecipes);
	}
	
	public RegisterRecipesEvent removeRecipe(ResourceLocation id)
	{
		removeRecipes.add(id);
		return this;
	}
	
	public StoneCutterRecipeBuilder stoneCutting()
	{
		return new StoneCutterRecipeBuilder(this);
	}
	
	public SmokingRecipeBuilder smoking()
	{
		return new SmokingRecipeBuilder(this);
	}
	
	public BlastingRecipeBuilder blasting()
	{
		return new BlastingRecipeBuilder(this);
	}
	
	public CampfireRecipeBuilder campfire()
	{
		return new CampfireRecipeBuilder(this);
	}
	
	public SmeltingRecipeBuilder smelting()
	{
		return new SmeltingRecipeBuilder(this);
	}
	
	public ShapedRecipeBuilder shaped()
	{
		return new ShapedRecipeBuilder(this);
	}
	
	public ShapelessRecipeBuilder shapeless()
	{
		return new ShapelessRecipeBuilder(this);
	}
	
	public boolean isRecipeIdTaken(ResourceLocation id)
	{
		return idInUse.test(id) || recipes.stream().map(RecipeHolder::id).anyMatch(id::equals);
	}
	
	protected ResourceLocation transformRecipeIdToContext(ResourceLocation loc)
	{
		var contextModId = ModLoadingContext.get().getActiveNamespace();
		if(loc.getNamespace().equals(contextModId))
			return loc;
		return Resources.location(contextModId, loc.getNamespace() + "/" + loc.getPath());
	}
	
	@Override
	public ResourceLocation nextId(Item item)
	{
		if(item == null || item == Items.AIR) return null;
		
		ResourceLocation rl = transformRecipeIdToContext(BuiltInRegistries.ITEM.getKey(item));
		
		if(!isRecipeIdTaken(rl)) return rl;
		
		int lastIdx = 1;
		while(true)
		{
			rl = Resources.location(rl.getNamespace(), rl.getPath() + "_" + (lastIdx++));
			var tf = transformRecipeIdToContext(rl);
			if(!isRecipeIdTaken(tf)) return tf;
		}
	}
	
	@Override
	public void register(ResourceLocation id, Recipe<?> entry)
	{
		add(new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, id), entry));
	}
	
	public Stream<RecipeHolder<?>> getRecipes()
	{
		return recipes.stream();
	}
	
	@Override
	public HolderLookup.Provider registryAccess()
	{
		return registries;
	}
	
	@Override
	public HolderLookup.RegistryLookup<Item> getItemLookup()
	{
		return itemRegistry;
	}
	
	@Override
	public boolean enableRecipe(RecipeType<?> type, ResourceLocation recipeId)
	{
		return getContext(recipeId.getNamespace()).enableRecipe(type, recipeId);
	}
	
	public boolean enableRecipe(RecipeHolder<?> recipe)
	{
		return enableRecipe(recipe.value().getType(), recipe.id().location());
	}
	
	public RecipeRegistrationContext getContext(String modid)
	{
		return contextMap.computeIfAbsent(modid, RecipeRegistrationContext::load);
	}
	
	@ApiStatus.Internal
	public void cleanup()
	{
		for(RecipeRegistrationContext value : contextMap.values())
			value.save();
		contextMap.clear();
	}
	
	public static ResourceKey<Recipe<?>> key(ResourceLocation location)
	{
		return ResourceKey.create(Registries.RECIPE, location);
	}
	
	public Optional<Ingredient> optionalIngredient(Object tag)
	{
		return Optional.ofNullable(ingredient(tag));
	}
	
	public Ingredient ingredient(Object tag)
	{
		return RecipeHelper.fromComponent(itemRegistry, tag);
	}
}