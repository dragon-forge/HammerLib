package com.zeitheron.hammercore.api.crafting;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An abstract registry for any subtype of {@link IGeneralRecipe}.
 */
public abstract class AbstractRecipeRegistry<T extends IGeneralRecipe, C extends IRecipeContainer<T>, A>
{
	private static final List<AbstractRecipeRegistry<?, ?, ?>> ALL_REGISTRIES = new ArrayList<>();
	private static final List<AbstractRecipeRegistry<?, ?, ?>> ALL_REGISTRIES_VIEW = Collections.unmodifiableList(ALL_REGISTRIES);
	protected final Class<T> type;
	protected final C container;
	
	private final ResourceLocation id;
	
	public AbstractRecipeRegistry(Class<T> type, C container, ResourceLocation id)
	{
		if(Loader.instance().hasReachedState(LoaderState.INITIALIZATION))
			throw new IllegalStateException("Attempted to create recipe registry for type " + type.getName() + " too late! Please do it in construct or preInit!");
		
		this.id = id;
		this.type = type;
		this.container = container;
		ALL_REGISTRIES.add(this);
	}
	
	public abstract A addRecipe(T recipe);
	
	public abstract void removeRecipe(T recipe);
	
	public abstract T getRecipe(A identifier);
	
	public Collection<T> getRecipes()
	{
		return container.getRecipes();
	}
	
	public static List<AbstractRecipeRegistry<?, ?, ?>> getAllRegistries()
	{
		return ALL_REGISTRIES_VIEW;
	}
	
	public ResourceLocation getRegistryId()
	{
		return id;
	}
}