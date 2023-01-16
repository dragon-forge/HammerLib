package org.zeith.hammerlib.util.mcf.itf;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import org.zeith.hammerlib.util.java.Cast;

public interface IResourceLocationGenerator<T>
{
	ResourceLocation nextId(T entry);
	
	default ResourceLocation nextId(ItemLike entry)
	{
		return nextId((T) Cast.cast(entry));
	}
}