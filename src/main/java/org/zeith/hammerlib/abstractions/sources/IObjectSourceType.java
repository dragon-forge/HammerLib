package org.zeith.hammerlib.abstractions.sources;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.core.RegistriesHL;

public interface IObjectSourceType
{
	IObjectSource<?> readSource(CompoundTag tag);
	
	default ResourceLocation getRegistryKey()
	{
		return RegistriesHL.animationSources().getKey(this);
	}
}