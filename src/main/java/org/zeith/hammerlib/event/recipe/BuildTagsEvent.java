package org.zeith.hammerlib.event.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BuildTagsEvent<T extends IForgeRegistryEntry<T>>
		extends GenericEvent<T>
{
	public final String directory;
	public final Map<ResourceLocation, Tag.Builder> tags;

	private final Map<ResourceLocation, SubTag> builders = new HashMap<>();

	public final ForgeRegistry<T> reg;

	public BuildTagsEvent(ForgeRegistry<T> reg, String directory, Map<ResourceLocation, Tag.Builder> tags)
	{
		super(reg.getRegistrySuperType());
		this.reg = reg;
		this.directory = directory;
		this.tags = tags;
	}

	public void addToTag(TagKey<T> key, T value)
	{
		if(reg.containsKey(value.getRegistryName()))
			getSubTag(key.location()).add(value);
	}

	public void addAllToTag(TagKey<T> key, Iterable<T> value)
	{
		var s = getSubTag(key.location());
		value.forEach(i ->
		{
			if(reg.containsKey(i.getRegistryName()))
				s.add(i);
		});
	}

	@Override
	public String toString()
	{
		return "BuildTagsEvent<" + ((Class<?>) getGenericType()).getSimpleName() + ">{" +
				"directory=" + directory +
				'}';
	}

	public SubTag<T> getSubTag(ResourceLocation loc)
	{
		return builders.computeIfAbsent(loc, l ->
		{
			Tag.Builder builder = tags.get(l);
			return new SubTag<>(l, e ->
			{
				builder.addElement(e.getRegistryName(), "HammerLib");
			}, e ->
			{
				builder.removeElement(e.getRegistryName(), "HammerLib");
			}, t ->
			{
				builder.addTag(t.location(), "HammerLib");
			}, t ->
			{
				builder.removeTag(t.location(), "HammerLib");
			});
		});
	}

	public record SubTag<T extends IForgeRegistryEntry<T>>(ResourceLocation loc,
														   Consumer<T> add,
														   Consumer<T> remove,
														   Consumer<TagKey<T>> addTag,
														   Consumer<TagKey<T>> removeTag)
	{
		public void add(T it)
		{
			add.accept(it);
		}

		public void remove(T it)
		{
			remove.accept(it);
		}

		public void addTag(TagKey<T> it)
		{
			addTag.accept(it);
		}

		public void remove(TagKey<T> it)
		{
			removeTag.accept(it);
		}
	}
}