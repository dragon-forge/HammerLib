package org.zeith.hammerlib.event.recipe;

import com.google.common.collect.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.*;
import org.zeith.hammerlib.api.recipes.RecipeBuilderExtension;
import org.zeith.hammerlib.core.adapter.recipe.*;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.RecipeRegistrationContext;
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
	private final List<Recipe<?>> recipes = Lists.newArrayList();
	private final Set<ResourceLocation> removeRecipes = Sets.newHashSet();
	private final Predicate<ResourceLocation> idInUse;
	
	protected String contextModId;
	
	private final Map<String, RecipeRegistrationContext> contextMap = Maps.newHashMap();
	
	private final Map<Class<?>, RecipeBuilderExtension> extensions;
	
	public RegisterRecipesEvent(Predicate<ResourceLocation> idInUse)
	{
		this.idInUse = idInUse;
		this.extensions = RecipeBuilderExtension.attach(this);
	}
	
	public void setContextModId(String contextModId)
	{
		this.contextModId = contextModId;
	}
	
	/**
	 * Returns a non-null instance of the provided extension class if the class has the @{@link RecipeBuilderExtension.RegisterExt} annotation.
	 *
	 * @param type
	 * 		The class object representing the extension type.
	 * @param <T>
	 * 		The type parameter representing the extension class.
	 *
	 * @return A non-null instance of the provided extension class if the class has the @{@link RecipeBuilderExtension.RegisterExt} annotation.
	 *
	 * @throws IllegalArgumentException
	 * 		If the provided extension class does not have the @{@link RecipeBuilderExtension.RegisterExt} annotation.
	 */
	@Nullable
	public <T extends RecipeBuilderExtension> T extension(Class<T> type)
	{
		return Cast.cast(extensions.get(type), type);
	}
	
	public void add(Recipe<?> recipe)
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
	public boolean register(Recipe<?> recipe)
	{
		if(recipe != null && enableRecipe(recipe.getType(), recipe.getId()))
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
		return idInUse.test(id) || recipes.stream().map(Recipe::getId).anyMatch(id::equals);
	}
	
	protected ResourceLocation transformRecipeIdToContext(ResourceLocation loc)
	{
		if(contextModId == null) return loc;
		if(loc.getNamespace().equals(contextModId))
			return loc;
		return new ResourceLocation(contextModId, loc.getNamespace() + "/" + loc.getPath());
	}
	
	@Override
	public ResourceLocation nextId(Item item)
	{
		if(item == null || item == Items.AIR) return null;
		
		ResourceLocation rl = transformRecipeIdToContext(ForgeRegistries.ITEMS.getKey(item));
		
		if(!isRecipeIdTaken(rl)) return rl;
		
		int lastIdx = 1;
		while(true)
		{
			rl = new ResourceLocation(rl.getNamespace(), rl.getPath() + "_" + (lastIdx++));
			var tf = transformRecipeIdToContext(rl);
			if(!isRecipeIdTaken(tf)) return tf;
		}
	}
	
	@Override
	public void register(ResourceLocation id, Recipe<?> entry)
	{
		add(entry);
	}
	
	public Stream<Recipe<?>> getRecipes()
	{
		return recipes.stream();
	}
	
	@Override
	public boolean enableRecipe(RecipeType<?> type, ResourceLocation recipeId)
	{
		return getContext(recipeId.getNamespace()).enableRecipe(type, recipeId);
	}
	
	public boolean enableRecipe(Recipe<?> recipe)
	{
		return enableRecipe(recipe.getType(), recipe.getId());
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
}