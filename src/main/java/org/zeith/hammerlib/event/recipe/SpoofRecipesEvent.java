package org.zeith.hammerlib.event.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.event.IModBusEvent;
import org.zeith.hammerlib.HammerLib;

import java.util.*;

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
		var event = new SpoofRecipesEvent(new HashMap<>());
		ModLoader.get().postEvent(event);
		HammerLib.EVENT_BUS.post(event);
		return Map.copyOf(event.spoofedRecipes);
	}
}