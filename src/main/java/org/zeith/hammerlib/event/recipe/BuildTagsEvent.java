package org.zeith.hammerlib.event.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BuildTagsEvent
		extends Event
{
	public final String directory;
	public final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tags;
	public final ForgeRegistry reg;
	
	public BuildTagsEvent(ForgeRegistry reg, String directory, Map<ResourceLocation, List<TagLoader.EntryWithSource>> tags)
	{
		this.reg = reg;
		this.directory = directory;
		this.tags = tags;
	}
	
	public <T> void addToTag(TagKey<T> key, T value)
	{
		var te = valueToEntry(value);
		if(te != null) tags.computeIfAbsent(key.location(), k -> new ArrayList<>()).add(te);
	}
	
	public <T> void addAllToTag(TagKey<T> key, Collection<T> values)
	{
		var path = key.location();
		var regTag = tags.computeIfAbsent(path, k -> new ArrayList<>());
		
		for(T value : values)
		{
			var te = valueToEntry(value);
			if(te != null) regTag.add(te);
		}
	}
	
	public TagLoader.EntryWithSource valueToEntry(Object value)
	{
		var key = reg.getKey(value);
		if(key == null) return null;
		return new TagLoader.EntryWithSource(TagEntry.element(key), "Default");
	}
	
	@Override
	public String toString()
	{
		return "BuildTagsEvent<" + reg.getRegistryKey().location() + ">{" +
				"directory=" + directory +
				'}';
	}
}