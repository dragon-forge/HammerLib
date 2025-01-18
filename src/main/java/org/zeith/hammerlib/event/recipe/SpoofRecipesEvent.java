package org.zeith.hammerlib.event.recipe;

import com.google.common.collect.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.event.IModBusEvent;

public class SpoofRecipesEvent
		extends Event
		implements IModBusEvent
{
	private final Multimap<ResourceLocation, ResourceLocation> spoofedRecipes;
	
	public SpoofRecipesEvent(Multimap<ResourceLocation, ResourceLocation> spoofedRecipes)
	{
		this.spoofedRecipes = spoofedRecipes;
	}
	
	public void spoofRecipe(ResourceLocation oldId, ResourceLocation newId)
	{
		spoofedRecipes.put(oldId, newId);
	}
	
	public static Multimap<ResourceLocation, ResourceLocation> gather()
	{
		Multimap<ResourceLocation, ResourceLocation> mm = MultimapBuilder
				.hashKeys()
				.hashSetValues()
				.build();
		ModLoader.get().postEvent(
				new SpoofRecipesEvent(Multimaps.synchronizedMultimap(mm))
		);
		return Multimaps.unmodifiableMultimap(mm);
	}
}