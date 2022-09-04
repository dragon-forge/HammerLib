package org.zeith.hammerlib.util.mcf.itf;

import net.minecraft.resources.ResourceLocation;

public interface IResourceRegistrar<T>
{
	void register(ResourceLocation id, T entry);
}