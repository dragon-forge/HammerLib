package org.zeith.hammerlib.event.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.api.crafting.*;
import org.zeith.hammerlib.api.crafting.building.GeneralRecipeBuilder;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

import java.util.Optional;
import java.util.function.Supplier;

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
			implements IRecipeRegistrationEvent<R>
	{
		final AbstractRecipeRegistry<R, ?, ?> registry;
		final Optional<MinecraftServer> server;
		final ICondition.IContext context;
		
		public AddRecipes(AbstractRecipeRegistry<R, ?, ?> registry, ICondition.IContext context, MinecraftServer server)
		{
			super(registry.getRecipeType());
			this.registry = registry;
			this.context = context;
			this.server = Optional.ofNullable(server);
		}
		
		public AbstractRecipeRegistry<R, ?, ?> getRegistry()
		{
			return registry;
		}
		
		public ICondition.IContext getContext()
		{
			return context;
		}
		
		/**
		 * Allows access to
		 */
		public Optional<MinecraftServer> getServer()
		{
			return server;
		}
		
		/**
		 * Use to create a type-specific builder that was supplied in the {@link RecipeRegistryFactory}'s builder.
		 */
		public <B extends GeneralRecipeBuilder<R, B>> Supplier<B> builderFactory()
		{
			return this::builder;
		}
		
		public <B extends GeneralRecipeBuilder<R, B>> B builder()
		{
			return Cast.cast(registry.builder(this));
		}
		
		public void addRecipe(R recipe)
		{
			registry.addRecipe(recipe);
		}
		
		public boolean is(AbstractRecipeRegistry<R, ?, ?> registry)
		{
			return registry.getRegistryId().equals(this.registry.getRegistryId());
		}
		
		@Override
		public ResourceLocation nextId(Item item)
		{
			if(item == null || item == Items.AIR) return null;
			
			ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
			
			if(registry.getRecipe(Cast.cast(rl)) == null) return rl;
			
			int lastIdx = 1;
			while(true)
			{
				rl = new ResourceLocation(rl.getNamespace(), rl.getPath() + "_" + (lastIdx++));
				if(registry.getRecipe(Cast.cast(rl)) == null) return rl;
			}
		}
		
		@Override
		public void register(ResourceLocation id, R entry)
		{
			addRecipe(entry);
		}
	}
}