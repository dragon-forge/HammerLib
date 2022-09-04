package org.zeith.hammerlib.util.mcf.itf;

import net.minecraft.resources.ResourceLocation;

public interface IResourceLocationGenerator<T>
{
	ResourceLocation nextId(T entry);
}