package org.zeith.hammerlib.api.multipart;

import net.minecraft.nbt.CompoundNBT;

public class MultipartState
{
	public final MultipartBlock block;

	public MultipartState(MultipartBlock block)
	{
		this.block = block;
	}

	public void writeNBT(CompoundNBT nbt)
	{
	}

	public void readNBT(CompoundNBT nbt)
	{
	}
}