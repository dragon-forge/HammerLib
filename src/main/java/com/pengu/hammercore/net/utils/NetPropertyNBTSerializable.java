package com.pengu.hammercore.net.utils;

import java.lang.reflect.Constructor;

import javax.annotation.Nonnull;

import com.pengu.hammercore.utils.NPEUtils;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class NetPropertyNBTSerializable<T extends NBTBase, N extends INBTSerializable<T>> extends NetPropertyAbstract<N>
{
	public NetPropertyNBTSerializable(iPropertyChangeHandler handler, @Nonnull N initialValue)
	{
		super(handler, initialValue);
		
		NPEUtils.checkNotNull(initialValue, "initialValue");
		
		try
		{
			initialValue.getClass().getConstructor();
		} catch(NoSuchMethodException e)
		{
			throw new NullPointerException("initialValue doesn't have an empty constructor!");
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		try
		{
			Constructor<N> nc = (Constructor<N>) Class.forName(nbt.getString("Class")).getConstructor();
			nc.setAccessible(true);
			value = nc.newInstance();
		} catch(Throwable err)
		{
		}
		value.deserializeNBT((T) nbt.getTag("Tag"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Class", value.getClass().getName());
		nbt.setTag("Tag", value.serializeNBT());
		return nbt;
	}
}