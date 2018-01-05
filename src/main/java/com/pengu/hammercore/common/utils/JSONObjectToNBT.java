package com.pengu.hammercore.common.utils;

import com.pengu.hammercore.json.JSONArray;
import com.pengu.hammercore.json.JSONException;
import com.pengu.hammercore.json.JSONObject;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

public class JSONObjectToNBT
{
	public static NBTTagCompound convert(JSONObject obj) throws JSONException
	{
		NBTTagCompound nbt = new NBTTagCompound();
		for(String k : obj.keySet())
			nbt.setTag(k, convertToBase(obj.get(k)));
		return nbt;
	}
	
	public static NBTTagList convert(JSONArray array) throws JSONException
	{
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < array.length(); ++i)
			list.appendTag(convertToBase(array.get(i)));
		return list;
	}
	
	public static NBTBase convertToBase(Object obj) throws JSONException
	{
		if(obj instanceof JSONObject)
			return convert((JSONObject) obj);
		if(obj instanceof JSONArray)
			return convert((JSONArray) obj);
		NBTBase base = null;
		if(obj instanceof Byte)
			base = new NBTTagByte((Byte) obj);
		if(obj instanceof byte[])
			base = new NBTTagByteArray((byte[]) obj);
		if(obj instanceof Double)
			base = new NBTTagDouble((Double) obj);
		if(obj instanceof Float)
			base = new NBTTagFloat((Float) obj);
		if(obj instanceof Integer)
			base = new NBTTagInt((Integer) obj);
		if(obj instanceof int[])
			base = new NBTTagIntArray((int[]) obj);
		if(obj instanceof Long)
			base = new NBTTagLong((Long) obj);
		if(obj instanceof Short)
			base = new NBTTagShort((Short) obj);
		if(obj instanceof String)
			base = new NBTTagString((String) obj);
		return base;
	}
}