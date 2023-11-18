package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

@NBTSerializer(FluidStack.class)
public class FluidStackSerializer
		implements INBTSerializer<FluidStack>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull FluidStack value)
	{
		if(value != null)
			nbt.setTag(key, value.writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public FluidStack deserialize(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, Constants.NBT.TAG_COMPOUND) ? FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(key)) : null;
	}
}
