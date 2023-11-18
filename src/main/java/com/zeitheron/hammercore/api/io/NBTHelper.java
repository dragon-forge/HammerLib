package com.zeitheron.hammercore.api.io;

import net.minecraft.nbt.*;

public class NBTHelper
{
	public static NBTBase toNBT(long[] la)
	{
		return new NBTTagLongArray(la);
	}
	
	public static long[] fromNBT(NBTTagLongArray nbt)
	{
		// This is inaccessible by default, so we use ATs to make it accessible.
		return nbt.data;
	}
}