package org.zeith.hammerlib.abstractions.sources;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.zeith.hammerlib.core.RegistriesHL;

public abstract class IObjectSourceType
		extends ForgeRegistryEntry<IObjectSourceType>
{
	public abstract IObjectSource<?> readSource(CompoundNBT tag);
	
	public final ResourceLocation getRegistryKey()
	{
		return RegistriesHL.animationSources().getKey(this);
	}
}