package org.zeith.hammerlib.event.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import org.zeith.hammerlib.HammerLib;

import java.util.*;

public class SpoofRecipesEvent
		extends Event
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
		var event = new SpoofRecipesEvent(new HashMap<>());
		HammerLib.EVENT_BUS.post(event);
		return event.spoofedRecipes;
	}
}