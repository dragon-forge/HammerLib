package com.pengu.hammercore.json.cfg;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import com.pengu.hammercore.error.Errors;
import com.pengu.hammercore.json.cfg.entries.JSONConfigEntryBool;
import com.pengu.hammercore.json.cfg.entries.JSONConfigEntryDouble;
import com.pengu.hammercore.json.cfg.entries.JSONConfigEntryJsonable;
import com.pengu.hammercore.json.cfg.entries.JSONConfigEntryLong;
import com.pengu.hammercore.json.cfg.entries.JSONConfigEntryString;
import com.pengu.hammercore.json.io.Jsonable;

public interface iJSONConfigEntry extends Jsonable
{
	String getName();
	
	String getComment();
	
	Object getValue();
	
	void setName(String name);
	
	void setComment(String comment);
	
	void setValue(Object value);
	
	default boolean equalsdef(Object obj)
	{
		if(!(obj instanceof iJSONConfigEntry))
			return false;
		iJSONConfigEntry entry = (iJSONConfigEntry) obj;
		if(!entry.getName().equals(getName()))
			return false;
		if(!entry.getComment().equals(getComment()))
			return false;
		if(!Objects.equals(entry.getValue(), getValue()))
			return false;
		return true;
	}
	
	public static iJSONConfigEntry entryByClassAndData(Class<?> type, String name, String desc, Object value)
	{
		iJSONConfigEntry s = entryByClass(type);
		s.setName(name);
		s.setComment(desc);
		s.setValue(value);
		return s;
	}
	
	public static Class<? extends iJSONConfigEntry> entryClassByClass(Class<?> type)
	{
		if(int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type) || long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type) || short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type) || byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type))
			return JSONConfigEntryLong.class;
		
		if(boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type))
			return JSONConfigEntryBool.class;
		
		if(double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type) || float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type))
			return JSONConfigEntryDouble.class;
		
		if(Jsonable.class.isAssignableFrom(type))
			return JSONConfigEntryJsonable.class;
		
		if(String.class.isAssignableFrom(type))
			return JSONConfigEntryString.class;
		
		return null;
	}
	
	public static iJSONConfigEntry entryByClass(Class<?> type)
	{
		try
		{
			return entryClassByClass(type).getDeclaredConstructor().newInstance();
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			Errors.propagate(e);
		}
		return null;
	}
}