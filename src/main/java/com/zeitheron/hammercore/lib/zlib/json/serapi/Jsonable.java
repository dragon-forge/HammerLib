package com.zeitheron.hammercore.lib.zlib.json.serapi;

import java.io.NotSerializableException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.lib.zlib.error.Errors;
import com.zeitheron.hammercore.lib.zlib.error.JSONException;
import com.zeitheron.hammercore.lib.zlib.error.NoDefaultConstructorsExceptions;
import com.zeitheron.hammercore.lib.zlib.error.NoVariableFoundInJSONException;
import com.zeitheron.hammercore.lib.zlib.json.JSONArray;
import com.zeitheron.hammercore.lib.zlib.json.JSONObject;
import com.zeitheron.hammercore.lib.zlib.json.JSONTokener;
import com.zeitheron.hammercore.utils.ClassLoaderWrapper;

public interface Jsonable
{
	public static final ClassLoaderWrapper clsWrapper = new ClassLoaderWrapper(HammerCore.class.getClassLoader());
	
	default SerializationContext serializationContext()
	{
		return null;
	}
	
	default String serialize()
	{
		SerializationContext ctx = serializationContext();
		StringBuilder b = new StringBuilder();
		
		b.append("{");
		
		if(ctx != null)
			for(Map.Entry<String, Object> kvpair : ctx.objects.entrySet())
			{
				String name = formatInsideString(kvpair.getKey());
				Object val = kvpair.getValue();
				b.append("\"" + name + "\":");
				
				if(val instanceof CharSequence)
					b.append("\"" + formatInsideString(val + "") + "\"");
				else if(val instanceof JSONObject)
					b.append(val.toString());
				else if(val instanceof JSONArray)
					b.append(val.toString());
				else if(val instanceof Jsonable)
					b.append(((Jsonable) val).serialize());
				else if(val instanceof Iterable<?>)
					try
					{
						b.append(serializeIterable((Iterable<?>) val));
					} catch(NotSerializableException e)
					{
						Errors.propagate(e);
					}
				else
					b.append(val.toString());
				
				b.append(",");
			}
		
		for(Field f : getClass().getDeclaredFields())
		{
			f.setAccessible(true);
			
			if(f.getAnnotation(IgnoreSerialization.class) != null || Modifier.isStatic(f.getModifiers()))
				continue;
			
			String name = f.getName();
			
			SerializedName aname = f.getAnnotation(SerializedName.class);
			if(aname != null)
				name = aname.value();
			
			name = formatInsideString(name);
			
			try
			{
				if(f.getType().isPrimitive() || CharSequence.class.isAssignableFrom(f.getType()) || Jsonable.class.isAssignableFrom(f.getType()) || Collection.class.isAssignableFrom(f.getType()))
				{
					Object val = f.get(this);
					String $ = formatInsideString(val + "");
					
					if(val instanceof Jsonable)
						$ = ((Jsonable) val).serialize();
					
					if(val instanceof CharSequence)
						$ = "\"" + formatInsideString(val + "") + "\"";
					
					if(val instanceof Collection)
						$ = serializeIterable((Collection<?>) val);
					
					b.append("\"" + name + "\":" + $ + ",");
				} else if(List.class.isAssignableFrom(f.getType()))
				{
					b.append("\"" + name + "\":");
				} else
					throw new NotSerializableException("Field " + f.getName() + " could not be serialized! Please insert @com.pengu.code.json.serapi.IgnoreSerialization !");
			} catch(Throwable err)
			{
				Errors.propagate(err);
			}
		}
		
		if(b.charAt(b.length() - 1) == ',')
			b = b.deleteCharAt(b.length() - 1);
		
		return b.append("}").toString();
	}
	
	public static String formatInsideString(String text)
	{
		return text.replace("\\", "\\\\").replace("\"", "\\\"");
	}
	
	public static <T extends Jsonable> T deserialize(String json, Class<T> type) throws JSONException
	{
		return deserialize((JSONObject) new JSONTokener(json).nextValue(), type);
	}
	
	public static Object deserialize(String json) throws JSONException
	{
		Object o = new JSONTokener(json).nextValue();
		if(o instanceof JSONObject)
			try
			{
				return deserialize((JSONObject) o);
			} catch(ClassNotFoundException e)
			{
				Errors.propagate(e);
			}
		else
			return deserialize((JSONArray) o);
		
		return null;
	}
	
	public static <T extends Jsonable> T deserialize(String json, T type) throws JSONException
	{
		return deserialize((JSONObject) new JSONTokener(json).nextValue(), type);
	}
	
	public static String serializeStream(Stream<?> list) throws NotSerializableException
	{
		Set<NotSerializableException> err = new HashSet<>();
		StringBuilder s = new StringBuilder();
		s.append("[");
		for(Object js : list.collect(Collectors.toList()))
		{
			if(js instanceof Jsonable)
			{
				String l = ((Jsonable) js).serialize();
				s.append(l.substring(0, l.length() - 1) + ",\"__type\":\"" + clsWrapper.getName(js.getClass()) + "\"}" + ",");
			} else if(js instanceof Iterable)
				s.append(serializeIterable((Iterable<?>) js) + ",");
			else
				err.add(new NotSerializableException(js.getClass().getName()));
		}
		if(!err.isEmpty())
			throw err.stream().findFirst().get();
		String ss = s.toString();
		if(ss.endsWith(","))
			ss = s.substring(0, s.length() - 1);
		return ss + "]";
	}
	
	public static String serializeIterable(Iterable<?> list) throws NotSerializableException
	{
		Set<NotSerializableException> err = new HashSet<>();
		StringBuilder s = new StringBuilder();
		s.append("[");
		for(Object js : list)
		{
			if(js instanceof Jsonable)
			{
				String l = ((Jsonable) js).serialize();
				s.append(l.substring(0, l.length() - 1) + ",\"__type\":\"" + clsWrapper.getName(js.getClass()) + "\"}" + ",");
			} else if(js instanceof Iterable)
				s.append(serializeIterable((Iterable<?>) js) + ",");
			else
				err.add(new NotSerializableException(js.getClass().getName()));
		}
		if(!err.isEmpty())
			throw err.stream().findFirst().get();
		String ss = s.toString();
		if(ss.endsWith(","))
			ss = s.substring(0, s.length() - 1);
		return ss + "]";
	}
	
	public static <T extends Jsonable> T deserialize(JSONObject js, Class<T> type) throws JSONException
	{
		try
		{
			Constructor<T> constr = type.getConstructor();
			constr.setAccessible(true);
			T i = null;
			try
			{
				i = constr.newInstance();
			} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				Errors.propagate(e);
			}
			
			return deserialize(js, i);
		} catch(NoSuchMethodException e)
		{
			throw new NoDefaultConstructorsExceptions(type, e);
		}
	}
	
	public static <T extends Jsonable> T deserialize(JSONObject js) throws JSONException, ClassNotFoundException
	{
		return (T) deserialize(js, (Class<? extends Jsonable>) clsWrapper.forName(js.getString("__type")));
	}
	
	default void deserializeJson(JSONObject json) throws JSONException
	{
		
	}
	
	public static <T extends Jsonable> T deserialize(JSONObject js, T i) throws JSONException
	{
		try
		{
			i.deserializeJson(js);
			
			Class<?> type = i.getClass();
			
			for(Field f : type.getDeclaredFields())
			{
				f.setAccessible(true);
				
				if(f.getAnnotation(IgnoreSerialization.class) != null || Modifier.isStatic(f.getModifiers()))
					continue;
				
				String name = f.getName();
				
				SerializedName aname = f.getAnnotation(SerializedName.class);
				if(aname != null)
					name = aname.value();
				
				if(!js.has(name))
					throw new NoVariableFoundInJSONException("No key \"" + name + "\" found to be deserialized!");
				
				Object obj = js.get(name);
				
				if(JSONObject.NULL.equals(obj))
					f.set(i, null);
				else if(obj instanceof JSONObject && Jsonable.class.isAssignableFrom(f.getType()))
				{
					Class<? extends Jsonable> test = (Class<? extends Jsonable>) f.getType();
					f.set(i, deserialize((JSONObject) obj, test));
				} else if(obj instanceof JSONArray && Collection.class.isAssignableFrom(f.getType()))
				{
					Collection<?> c = (Collection<?>) f.get(i);
					if(c == null)
						f.set(i, c = (List.class.isAssignableFrom(f.getType()) ? new ArrayList<>() : new HashSet<>()));
					c.clear();
					c.addAll((Collection) deserialize((JSONArray) obj));
				} else
					f.set(i, obj);
			}
			
			return i;
		} catch(IllegalAccessException e)
		{
			Errors.propagate(e);
		}
		
		return null;
	}
	
	public static List<?> deserialize(JSONArray js) throws JSONException
	{
		List data = new ArrayList<>();
		
		for(int i = 0; i < js.length(); ++i)
		{
			Object obj = js.get(i);
			if(obj instanceof JSONArray)
				data.add(deserialize((JSONArray) obj));
			else if(obj instanceof JSONObject)
				try
				{
					data.add(deserialize((JSONObject) obj));
				} catch(ClassNotFoundException e)
				{
					Errors.propagate(e);
				}
		}
		
		return data;
	}
}