package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(FluidStack.class)
public class FluidStackSerializer
		implements INBTSerializer<FluidStack>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, @NotNull FluidStack value, CompoundTag nbt)
	{
		if(!value.isEmpty())
			nbt.put(key, value.saveOptional(provider));
	}
	
	@Override
	public FluidStack deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		return nbt.contains(key, Tag.TAG_COMPOUND) ? FluidStack.parseOptional(provider, nbt.getCompound(key)) : FluidStack.EMPTY;
	}
}
