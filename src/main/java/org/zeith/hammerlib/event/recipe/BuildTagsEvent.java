package org.zeith.hammerlib.event.recipe;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.ForgeRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.zeith.hammerlib.util.mcf.TagRegistrationContext;

import java.util.*;

public class BuildTagsEvent
		extends Event
{
	private static final Object IO_SYNC = new Object();
	
	public final String directory;
	public final Map<ResourceLocation, List<TagLoader.EntryWithSource>> tags;
	public final ForgeRegistry reg;
	
	private final Map<String, TagRegistrationContext> contextMap = Maps.newHashMap();
	
	public BuildTagsEvent(ForgeRegistry reg, String directory, Map<ResourceLocation, List<TagLoader.EntryWithSource>> tags)
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
	
	protected TagRegistrationContext getContext(String modid)
	{
		return contextMap.computeIfAbsent(modid, m ->
				{
					synchronized(IO_SYNC)
					{
						return TagRegistrationContext.load(m);
					}
				}
		);
	}
	
	@ApiStatus.Internal
	public void cleanup()
	{
		synchronized(IO_SYNC)
		{
			for(TagRegistrationContext value : contextMap.values())
				value.save();
		}
		contextMap.clear();
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