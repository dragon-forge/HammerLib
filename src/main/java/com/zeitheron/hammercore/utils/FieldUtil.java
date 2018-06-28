package com.zeitheron.hammercore.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Contains some NBT utilities for fields
 */
public class FieldUtil
{
	/**
	 * Writes all possible variables to NBT, including privates if you want to
	 */
	public static void writeFieldsToNBT(Object obj, NBTTagCompound to, boolean privates)
	{
		try
		{
			Class<? extends Object> c = obj.getClass();
			Field[] fs = c.getFields();
			
			for(Field f : fs)
			{
				if(!f.isAccessible() && !privates)
					continue;
				
				int mod = f.getModifiers();
				if(Modifier.isStatic(mod))
					continue;
				
				f.setAccessible(true);
				Object oj = f.get(obj);
				
				Class<?> o = oj.getClass();
				if(o.isAssignableFrom(boolean.class))
					to.setBoolean(f.getName(), (Boolean) oj);
				else if(o.isAssignableFrom(byte.class))
					to.setByte(f.getName(), (Byte) oj);
				else if(o.isAssignableFrom(byte[].class))
					to.setByteArray(f.getName(), (byte[]) oj);
				else if(o.isAssignableFrom(double.class))
					to.setDouble(f.getName(), (Double) oj);
				else if(o.isAssignableFrom(float.class))
					to.setFloat(f.getName(), (Float) oj);
				else if(o.isAssignableFrom(int[].class))
					to.setIntArray(f.getName(), (int[]) oj);
				else if(o.isAssignableFrom(int.class))
					to.setInteger(f.getName(), (Integer) oj);
				else if(o.isAssignableFrom(long.class))
					to.setLong(f.getName(), (Long) oj);
				else if(o.isAssignableFrom(short.class))
					to.setShort(f.getName(), (Short) oj);
				else if(o.isAssignableFrom(String.class))
					to.setString(f.getName(), (String) oj);
				else if(o.isAssignableFrom(NBTBase.class))
					to.setTag(f.getName(), (NBTBase) oj);
				else if(o.isAssignableFrom(UUID.class))
					to.setUniqueId(f.getName(), (UUID) oj);
			}
		} catch(Throwable err)
		{
		}
	}
	
	/**
	 * Sets all variables that were found in NBT tag compound
	 */
	public static void readFieldsFromNBT(Object to, NBTTagCompound from)
	{
		try
		{
			Class<? extends Object> c = to.getClass();
			Field[] fs = c.getFields();
			
			for(Field f : fs)
			{
				f.setAccessible(true);
				
				int mod = f.getModifiers();
				if(Modifier.isStatic(mod))
					continue;
				
				Class<?> o = f.getType();
				
				if(o.isAssignableFrom(boolean.class))
					f.setBoolean(to, from.getBoolean(f.getName()));
				else if(o.isAssignableFrom(byte.class))
					f.setByte(to, from.getByte(f.getName()));
				else if(o.isAssignableFrom(byte[].class))
					f.set(to, from.getByteArray(f.getName()));
				else if(o.isAssignableFrom(double.class))
					f.setDouble(to, from.getDouble(f.getName()));
				else if(o.isAssignableFrom(float.class))
					f.setFloat(to, from.getFloat(f.getName()));
				else if(o.isAssignableFrom(int[].class))
					f.set(to, from.getIntArray(f.getName()));
				else if(o.isAssignableFrom(int.class))
					f.setInt(to, from.getInteger(f.getName()));
				else if(o.isAssignableFrom(long.class))
					f.setLong(to, from.getLong(f.getName()));
				else if(o.isAssignableFrom(short.class))
					f.setShort(to, from.getShort(f.getName()));
				else if(o.isAssignableFrom(String.class))
					f.set(to, from.getString(f.getName()));
				else if(o.isAssignableFrom(NBTBase.class))
					f.set(to, from.getTag(f.getName()));
				else if(o.isAssignableFrom(UUID.class))
					f.set(to, from.getUniqueId(f.getName()));
			}
		} catch(Throwable err)
		{
		}
	}
}