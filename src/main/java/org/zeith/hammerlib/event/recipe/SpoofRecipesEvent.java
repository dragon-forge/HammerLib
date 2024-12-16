package org.zeith.hammerlib.event.recipe;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.IModBusEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpoofRecipesEvent
		extends Event
		implements IModBusEvent
{
	private final Map<ResourceKey<Recipe<?>>, List<ResourceKey<Recipe<?>>>> spoofedRecipes;
	
	public SpoofRecipesEvent(Map<ResourceKey<Recipe<?>>, List<ResourceKey<Recipe<?>>>> spoofedRecipes)
	{
		this.spoofedRecipes = spoofedRecipes;
	}
	
	public void spoofRecipe(ResourceLocation oldId, ResourceLocation newId)
	{
		spoofRecipe(RegisterRecipesEvent.key(oldId), RegisterRecipesEvent.key(newId));
	}
	
	public void spoofRecipe(ResourceKey<Recipe<?>> oldId, ResourceKey<Recipe<?>> newId)
	{
		spoofedRecipes.computeIfAbsent(oldId, v -> new ArrayList<>()).add(newId);
	}
	
	public static Map<ResourceKey<Recipe<?>>, List<ResourceKey<Recipe<?>>>> gather()
	{
		return Map.copyOf(ModLoader.postEventWithReturn(
				new SpoofRecipesEvent(new ConcurrentHashMap<>())
		).spoofedRecipes);
	}
}