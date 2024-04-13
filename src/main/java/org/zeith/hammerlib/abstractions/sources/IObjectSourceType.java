package org.zeith.hammerlib.abstractions.sources;

import com.zeitheron.hammercore.internal.init.RegistriesHL;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IObjectSourceType
		extends IForgeRegistryEntry<IObjectSourceType>
{
	IObjectSource<?> readSource(NBTTagCompound tag);
	
	default ResourceLocation getRegistryKey()
	{
		return RegistriesHL.OBJECT_SOURCE().getKey((IObjectSourceType) this);
	}
}