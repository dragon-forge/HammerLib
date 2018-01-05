package com.pengu.hammercore.utils;

import java.util.UUID;

import com.pengu.hammercore.utils.NumberUtils.EnumNumberType;

import net.minecraft.nbt.NBTTagCompound;

public class NBTUtils
{
	public static void writeNumberToNBT(String key, NBTTagCompound nbt, Number number)
	{
		EnumNumberType type = NumberUtils.getType(number);
		if(type != null && type != EnumNumberType.UNDEFINED)
			nbt.setByteArray(key, NumberUtils.asBytes(number));
	}
	
	public static Number readNumberFromNBT(String key, NBTTagCompound nbt)
	{
		return NumberUtils.fromBytes(nbt.getByteArray(key));
	}
	
	public static void writeStringArrayToNBT(String key, NBTTagCompound nbt, String... $)
	{
		nbt.setInteger(key + "Length", $.length);
		for(int i = 0; i < $.length; ++i)
			nbt.setString(key + i, $[i]);
	}
	
	public static String[] readStringArrayFromNBT(String key, NBTTagCompound nbt)
	{
		String[] $ = new String[nbt.getInteger(key + "Length")];
		for(int i = 0; i < $.length; ++i)
			$[i] = nbt.getString(key + i);
		return $;
	}
	
	public static void writeUUIDArrayToNBT(String key, NBTTagCompound nbt, UUID... $)
	{
		nbt.setInteger(key + "Length", $.length);
		for(int i = 0; i < $.length; ++i)
			nbt.setUniqueId(key + i, $[i]);
	}
	
	public static UUID[] readUUIDArrayFromNBT(String key, NBTTagCompound nbt)
	{
		UUID[] $ = new UUID[nbt.getInteger(key + "Length")];
		for(int i = 0; i < $.length; ++i)
			$[i] = nbt.getUniqueId(key + i);
		return $;
	}
}