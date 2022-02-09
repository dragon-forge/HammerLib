package org.zeith.hammerlib.api.multipart;

import net.minecraft.nbt.CompoundTag;

public class MultipartState
{
	public final MultipartBlock block;

	public MultipartState(MultipartBlock block)
	{
		this.block = block;
	}

	public void writeNBT(CompoundTag nbt)
	{
	}

	public void readNBT(CompoundTag nbt)
	{
	}
}