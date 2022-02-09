package org.zeith.hammerlib.event.recipe;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.GenericEvent;
import org.zeith.hammerlib.api.crafting.AbstractRecipeRegistry;
import org.zeith.hammerlib.api.crafting.IGeneralRecipe;

/**
 * This event is fired when recipe registries get reloaded.
 * Register all recipes during this event!
 * This event is fired on {@link org.zeith.hammerlib.HammerLib#EVENT_BUS}
 */
public class ReloadRecipeRegistryEvent
		extends Event
{
	final AbstractRecipeRegistry<?, ?, ?> registry;

	public ReloadRecipeRegistryEvent(AbstractRecipeRegistry<?, ?, ?> registry)
	{
		this.registry = registry;
	}

	public AbstractRecipeRegistry<?, ?, ?> getRegistry()
	{
		return registry;
	}

	public boolean isThisRegistry(AbstractRecipeRegistry<?, ?, ?> registry)
	{
		return registry.getRegistryId().equals(this.registry.getRegistryId());
	}

	public static class Pre
			extends ReloadRecipeRegistryEvent
	{
		public Pre(AbstractRecipeRegistry<?, ?, ?> registry)
		{
			super(registry);
		}
	}

	public static class Post
			extends ReloadRecipeRegistryEvent
	{
		public Post(AbstractRecipeRegistry<?, ?, ?> registry)
		{
			super(registry);
		}
	}

	public static class AddRecipes<R extends IGeneralRecipe>
			extends GenericEvent<R>
	{
		final AbstractRecipeRegistry<R, ?, ?> registry;

		public AddRecipes(AbstractRecipeRegistry<R, ?, ?> registry)
		{
			super(registry.getRecipeType());
			this.registry = registry;
		}

		public void addRecipe(R recipe)
		{
			registry.addRecipe(recipe);
		}

		public boolean is(AbstractRecipeRegistry<R, ?, ?> registry)
		{
			return registry.getRegistryId().equals(this.registry.getRegistryId());
		}
	}
}