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
	 * 
	 * @param obj
	 *            The object to write it's fields
	 * @param to
	 *            The NBT tag to write to
	 * @param privates
	 *            Include private variables
	 */
	public static void writeFieldsToNBT(Object obj, NBTTagCompound to, boolean privates)
	{
		try
		{
			Class<? extends Object> c = obj.getClass();
			Field[] fs = c.getDeclaredFields();
			
			for(Field f : fs)
			{
				if(!f.isAccessible() && !privates)
					continue;
				
				int mod = f.getModifiers();
				if(Modifier.isStatic(mod) || Modifier.isFinal(mod))
					continue;
				
				f.setAccessible(true);
				Object oj = f.get(obj);
				
				Class<?> o = oj.getClass();
				if(boolean.class.isAssignableFrom(o))
					to.setBoolean(f.getName(), (Boolean) oj);
				else if(byte.class.isAssignableFrom(o))
					to.setByte(f.getName(), (Byte) oj);
				else if(byte[].class.isAssignableFrom(o))
					to.setByteArray(f.getName(), (byte[]) oj);
				else if(double.class.isAssignableFrom(o))
					to.setDouble(f.getName(), (Double) oj);
				else if(float.class.isAssignableFrom(o))
					to.setFloat(f.getName(), (Float) oj);
				else if(int[].class.isAssignableFrom(o))
					to.setIntArray(f.getName(), (int[]) oj);
				else if(int.class.isAssignableFrom(o))
					to.setInteger(f.getName(), (Integer) oj);
				else if(long.class.isAssignableFrom(o))
					to.setLong(f.getName(), (Long) oj);
				else if(short.class.isAssignableFrom(o))
					to.setShort(f.getName(), (Short) oj);
				else if(String.class.isAssignableFrom(o))
					to.setString(f.getName(), (String) oj);
				else if(NBTBase.class.isAssignableFrom(o))
					to.setTag(f.getName(), (NBTBase) oj);
				else if(UUID.class.isAssignableFrom(o))
					to.setUniqueId(f.getName(), (UUID) oj);
			}
		} catch(Throwable err)
		{
		}
	}
	
	/**
	 * Sets all variables that were found in NBT tag compound
	 * 
	 * @param to
	 *            The object to read it's fields
	 * @param from
	 *            The NBT tag to read from
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
				if(Modifier.isStatic(mod) || Modifier.isFinal(mod))
					continue;
				
				Class<?> o = f.getType();
				
				if(boolean.class.isAssignableFrom(o))
					f.setBoolean(to, from.getBoolean(f.getName()));
				else if(byte.class.isAssignableFrom(o))
					f.setByte(to, from.getByte(f.getName()));
				else if(byte[].class.isAssignableFrom(o))
					f.set(to, from.getByteArray(f.getName()));
				else if(double.class.isAssignableFrom(o))
					f.setDouble(to, from.getDouble(f.getName()));
				else if(float.class.isAssignableFrom(o))
					f.setFloat(to, from.getFloat(f.getName()));
				else if(int[].class.isAssignableFrom(o))
					f.set(to, from.getIntArray(f.getName()));
				else if(int.class.isAssignableFrom(o))
					f.setInt(to, from.getInteger(f.getName()));
				else if(long.class.isAssignableFrom(o))
					f.setLong(to, from.getLong(f.getName()));
				else if(short.class.isAssignableFrom(o))
					f.setShort(to, from.getShort(f.getName()));
				else if(String.class.isAssignableFrom(o))
					f.set(to, from.getString(f.getName()));
				else if(NBTBase.class.isAssignableFrom(o))
					f.set(to, from.getTag(f.getName()));
				else if(UUID.class.isAssignableFrom(o))
					f.set(to, from.getUniqueId(f.getName()));
			}
		} catch(Throwable err)
		{
		}
	}
}