package com.pengu.hammercore.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;

import com.pengu.hammercore.utils.NumberUtils.EnumNumberType;

import net.minecraft.nbt.CompressedStreamTools;
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
	
	public static int[] toIA(NBTTagCompound comp)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			CompressedStreamTools.write(comp, new DataOutputStream(baos));
		} catch(IOException e)
		{
			// Shouldn't happen, be silent.
		}
		byte[] data = baos.toByteArray();
		while(data.length % 4 != 0)
			data = ArrayUtils.add(data, (byte) 0);
		ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
		int[] ret = new int[data.length / 4];
		buffer.asIntBuffer().get(ret);
		return ret;
	}
	
	public static NBTTagCompound toNBT(int[] ia)
	{
		ByteBuffer buffer = ByteBuffer.allocate(ia.length * 4).order(ByteOrder.LITTLE_ENDIAN);
		buffer.asIntBuffer().put(ia);
		buffer.flip();
		byte[] data = buffer.array();
		try
		{
			return CompressedStreamTools.read(new DataInputStream(new ByteArrayInputStream(data)));
		} catch(IOException e)
		{
			// Shouldn't happen, but return new tag compound.
			return new NBTTagCompound();
		}
	}
}