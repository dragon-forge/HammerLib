package org.zeith.hammerlib.event.recipe;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class RecipeDeserializeEvent
		extends Event
{
	public final ResourceLocation id;
	public final JsonObject json;

	public RecipeDeserializeEvent(ResourceLocation id, JsonObject json)
	{
		this.id = id;
		this.json = json;
	}
}