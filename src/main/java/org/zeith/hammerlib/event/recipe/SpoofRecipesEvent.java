package org.zeith.hammerlib.event.recipe;

import com.google.common.collect.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.IModBusEvent;

public class SpoofRecipesEvent
		extends Event
		implements IModBusEvent
{
	private final Multimap<ResourceKey<Recipe<?>>, ResourceKey<Recipe<?>>> spoofedRecipes;
	
	public SpoofRecipesEvent(Multimap<ResourceKey<Recipe<?>>, ResourceKey<Recipe<?>>> spoofedRecipes)
	{
		this.spoofedRecipes = spoofedRecipes;
	}
	
	public void spoofRecipe(ResourceLocation oldId, ResourceLocation newId)
	{
		spoofRecipe(RegisterRecipesEvent.key(oldId), RegisterRecipesEvent.key(newId));
	}
	
	public void spoofRecipe(ResourceKey<Recipe<?>> oldId, ResourceKey<Recipe<?>> newId)
	{
		spoofedRecipes.put(oldId, newId);
	}
	
	public static Multimap<ResourceKey<Recipe<?>>, ResourceKey<Recipe<?>>> gather()
	{
		Multimap<ResourceKey<Recipe<?>>, ResourceKey<Recipe<?>>> mm = MultimapBuilder
				.hashKeys()
				.hashSetValues()
				.build();
		ModLoader.postEvent(
				new SpoofRecipesEvent(Multimaps.synchronizedMultimap(mm))
		);
		return Multimaps.unmodifiableMultimap(mm);
	}
}