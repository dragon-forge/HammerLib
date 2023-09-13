package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(FluidStack.class)
public class FluidStackSerializer
		implements INBTSerializer<FluidStack>
{
	@Override
	public void serialize(CompoundTag nbt, String key, @NotNull FluidStack value)
	{
		if(!value.isEmpty())
			nbt.put(key, value.writeToNBT(new CompoundTag()));
	}
	
	@Override
	public FluidStack deserialize(CompoundTag nbt, String key)
	{
		return nbt.contains(key, Tag.TAG_COMPOUND) ? FluidStack.loadFluidStackFromNBT(nbt.getCompound(key)) : FluidStack.EMPTY;
	}
}
