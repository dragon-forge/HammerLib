package org.zeith.hammerlib.api.crafting;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.recipe.ReloadRecipeRegistryEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An abstract registry for any subtype of {@link IGeneralRecipe}.
 */
public abstract class AbstractRecipeRegistry<T extends IGeneralRecipe, CTR extends IRecipeContainer<T>, IDX>
{
	private static final List<AbstractRecipeRegistry<?, ?, ?>> ALL_REGISTRIES = new ArrayList<>();
	private static final List<AbstractRecipeRegistry<?, ?, ?>> ALL_REGISTRIES_VIEW = Collections.unmodifiableList(ALL_REGISTRIES);
	protected final Class<T> type;
	protected final CTR container;

	private final ResourceLocation id;

	public AbstractRecipeRegistry(Class<T> type, CTR container, ResourceLocation id)
	{
		if(HammerLib.PROXY.hasFinishedLoading())
			throw new IllegalStateException("Attempted to create recipe registry for type " + type.getName() + " too late! Please do it in construct or preInit!");

		this.id = id;
		this.type = type;
		this.container = container;
		ALL_REGISTRIES.add(this);
	}

	public abstract IDX addRecipe(T recipe);

	public abstract void removeRecipe(T recipe);

	protected abstract void removeAllRecipes();

	public abstract T getRecipe(IDX identifier);

	public Collection<T> getRecipes()
	{
		return container.getRecipes();
	}

	public int getRecipeCount()
	{
		return container.getRecipeCount();
	}

	public Class<T> getRecipeType()
	{
		return type;
	}

	public static List<AbstractRecipeRegistry<?, ?, ?>> getAllRegistries()
	{
		return ALL_REGISTRIES_VIEW;
	}

	public ResourceLocation getRegistryId()
	{
		return id;
	}

	public void reload()
	{
		MinecraftForge.EVENT_BUS.post(new ReloadRecipeRegistryEvent.Pre(this));
		removeAllRecipes();
		MinecraftForge.EVENT_BUS.post(new ReloadRecipeRegistryEvent.AddRecipes<>(this));
		MinecraftForge.EVENT_BUS.post(new ReloadRecipeRegistryEvent.Post(this));
	}
}