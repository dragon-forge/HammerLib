package org.zeith.hammerlib.abstractions.sources;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class IObjectSourceType
		extends ForgeRegistryEntry<IObjectSourceType>
{
	public abstract IObjectSource<?> readSource(CompoundNBT tag);
	
	public final ResourceLocation getRegistryKey()
	{
		return getRegistryName();
	}
}