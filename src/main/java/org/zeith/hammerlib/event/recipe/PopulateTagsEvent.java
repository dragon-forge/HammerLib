package org.zeith.hammerlib.event.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraftforge.eventbus.api.GenericEvent;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;

import java.util.List;
import java.util.function.Consumer;

/**
 * This event is fired on {@link org.zeith.hammerlib.HammerLib#EVENT_BUS}
 */
public class PopulateTagsEvent<T>
		extends GenericEvent<T>
{
	public final ResourceLocation id;
	public final Tag<T> tag;

	private final Consumer<T> add;
	private boolean hasChanged;

	public PopulateTagsEvent(Consumer<T> add, ResourceLocation id, Tag<T> tag, List<T> values)
	{
		super(Cast.cast(ReflectionUtil.findCommonSuperClass(tag.getValues())));
		this.add = add;
		this.id = id;
		this.tag = tag;
	}

	public PopulateTagsEvent(Consumer<T> add, ResourceLocation id, Tag<T> tag, List<T> values, Class<T> type)
	{
		super(type);
		this.add = add;
		this.id = id;
		this.tag = tag;
	}

	public void add(T thing)
	{
		add.accept(thing);
		hasChanged = true;
	}

	public boolean hasChanged()
	{
		return hasChanged;
	}

	public List<T> getValues()
	{
		return tag.getValues();
	}

	public ResourceLocation getId()
	{
		return id;
	}

	public Tag<T> getTag()
	{
		return tag;
	}

	public boolean is(Tag.Named<T> tag)
	{
		return id.equals(tag.getName());
	}
}