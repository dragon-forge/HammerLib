package org.zeith.hammerlib.event.recipe;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.IModBusEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpoofRecipesEvent
		extends Event
		implements IModBusEvent
{
	private final Map<ResourceLocation, List<ResourceLocation>> spoofedRecipes;
	
	public SpoofRecipesEvent(Map<ResourceLocation, List<ResourceLocation>> spoofedRecipes)
	{
		this.spoofedRecipes = spoofedRecipes;
	}
	
	public void spoofRecipe(ResourceLocation oldId, ResourceLocation newId)
	{
		spoofedRecipes.computeIfAbsent(oldId, v -> new ArrayList<>()).add(newId);
	}
	
	public static Map<ResourceLocation, List<ResourceLocation>> gather()
	{
		return ModLoader.get().postEventWithReturn(
				new SpoofRecipesEvent(new ConcurrentHashMap<>())
		).spoofedRecipes;
	}
}