package org.zeith.hammerlib.event.recipe;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.ApiStatus;
import org.zeith.hammerlib.util.mcf.RecipeRegistrationContext;
import org.zeith.hammerlib.util.mcf.TagRegistrationContext;

import java.util.*;

public class BuildTagsEvent
		extends Event
{
	public final String directory;
	public final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tags;
	public final Registry reg;
	
	private final Map<String, TagRegistrationContext> contextMap = Maps.newHashMap();
	
	public BuildTagsEvent(Registry reg, String directory, Map<ResourceLocation, List<TagLoader.EntryWithSource>> tags)
	{
		this.reg = reg;
		this.directory = directory;
		this.tags = tags;
	}
	
	public <T> void addToTag(TagKey<T> key, T value)
	{
		var te = valueToEntry(value);
		if(te == null) return;
		ResourceLocation id = te.entry().getId();
		if(!getContext(id.getNamespace()).addToTag(key, id))
			return;
		tags.computeIfAbsent(key.location(), k -> new ArrayList<>()).add(te);
	}
	
	public <T> void addAllToTag(TagKey<T> key, Collection<T> values)
	{
		var path = key.location();
		var regTag = tags.computeIfAbsent(path, k -> new ArrayList<>());
		
		for(T value : values)
		{
			var te = valueToEntry(value);
			if(te == null) continue;
			ResourceLocation id = te.entry().getId();
			if(!getContext(id.getNamespace()).addToTag(key, id))
				continue;
			regTag.add(te);
		}
	}
	
	protected TagLoader.EntryWithSource valueToEntry(Object value)
	{
		var key = reg.getKey(value);
		if(key == null) return null;
		return new TagLoader.EntryWithSource(TagEntry.element(key), "Java");
	}
	
	protected TagRegistrationContext getContext(String modid)
	{
		return contextMap.computeIfAbsent(modid, TagRegistrationContext::load);
	}
	
	@ApiStatus.Internal
	public void cleanup()
	{
		for(TagRegistrationContext value : contextMap.values())
			value.save();
		contextMap.clear();
	}
	
	@Override
	public String toString()
	{
		return "BuildTagsEvent<" + reg.key().location() + ">{" +
			   "directory=" + directory +
			   '}';
	}
	
	public boolean is(ResourceKey<? extends Registry<?>> reg)
	{
		return this.reg.key().equals(reg);
	}
}