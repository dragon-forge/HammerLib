package org.zeith.hammerlib.api.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.recipe.ReloadRecipeRegistryEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An abstract registry for any subtype of {@link IGeneralRecipe}.
 */
@Mod.EventBusSubscriber
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
		HammerLib.postEvent(new ReloadRecipeRegistryEvent.Pre(this));
		removeAllRecipes();
		HammerLib.postEvent(new ReloadRecipeRegistryEvent.AddRecipes<>(this));
		HammerLib.postEvent(new ReloadRecipeRegistryEvent.Post(this));
	}

	@SubscribeEvent
	public static void serverStart(ServerStartingEvent e)
	{
		HammerLib.LOG.info("Reloading recipes in " + ALL_REGISTRIES.size() + " HL recipe registries.");
		for(AbstractRecipeRegistry<?, ?, ?> reg : ALL_REGISTRIES)
			reg.reload();
	}

	@SubscribeEvent
	public static void serverStop(ServerStoppingEvent e)
	{
		HammerLib.LOG.info("Purging cache from " + ALL_REGISTRIES.size() + " HL recipe registries.");
		for(AbstractRecipeRegistry<?, ?, ?> reg : ALL_REGISTRIES)
			reg.removeAllRecipes();
	}
}