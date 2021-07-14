package org.zeith.hammerlib.event.recipe;

import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.GenericEvent;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;

import java.util.List;

public class PopulateTagsEvent<T>
		extends GenericEvent<T>
{
	public final ResourceLocation id;
	public final ITag<T> tag;
	private final List<T> values;

	public PopulateTagsEvent(ResourceLocation id, ITag<T> tag, List<T> values)
	{
		super(Cast.cast(ReflectionUtil.findCommonSuperClass(tag.getValues())));
		this.id = id;
		this.tag = tag;
		this.values = values;
	}

	public void add(T thing)
	{
		values.add(thing);
	}

	public List<T> getValues()
	{
		return values;
	}

	public ResourceLocation getId()
	{
		return id;
	}

	public ITag<T> getTag()
	{
		return tag;
	}

	public boolean is(ITag.INamedTag<T> tag)
	{
		return id.equals(tag.getName());
	}
}