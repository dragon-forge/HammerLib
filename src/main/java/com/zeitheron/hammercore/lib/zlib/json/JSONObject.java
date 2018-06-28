package com.zeitheron.hammercore.lib.zlib.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.zeitheron.hammercore.lib.zlib.error.JSONException;

import java.util.Set;

public class JSONObject
{
	private static final Double NEGATIVE_ZERO = -0d;
	
	public static final Object NULL = new Object()
	{
		@Override
		public boolean equals(Object o)
		{
			return o == this || o == null;
		}
		
		@Override
		public String toString()
		{
			return "null";
		}
	};
	
	private final LinkedHashMap<String, Object> nameValuePairs;
	
	public JSONObject()
	{
		nameValuePairs = new LinkedHashMap<String, Object>();
	}
	
	public JSONObject(Map<?, ?> copyFrom)
	{
		this();
		Map<?, ?> contentsTyped = (Map<?, ?>) copyFrom;
		for(Entry<?, ?> entry : contentsTyped.entrySet())
		{
			String key = (String) entry.getKey();
			if(key == null)
				throw new NullPointerException("key == null");
			nameValuePairs.put(key, wrap(entry.getValue()));
		}
	}
	
	public JSONObject(JSONTokener readFrom) throws JSONException
	{
		Object object = readFrom.nextValue();
		if(object instanceof JSONObject)
			nameValuePairs = ((JSONObject) object).nameValuePairs;
		else
			throw JSON.typeMismatch(object, "JSONObject");
	}
	
	public JSONObject(String json) throws JSONException
	{
		this(new JSONTokener(json));
	}
	
	public JSONObject(JSONObject copyFrom, String[] names) throws JSONException
	{
		this();
		for(String name : names)
		{
			Object value = copyFrom.opt(name);
			if(value != null)
				nameValuePairs.put(name, value);
		}
	}
	
	public int length()
	{
		return nameValuePairs.size();
	}
	
	public JSONObject put(String name, boolean value) throws JSONException
	{
		nameValuePairs.put(checkName(name), value);
		return this;
	}
	
	public JSONObject put(String name, double value) throws JSONException
	{
		nameValuePairs.put(checkName(name), JSON.checkDouble(value));
		return this;
	}
	
	public JSONObject put(String name, int value) throws JSONException
	{
		nameValuePairs.put(checkName(name), value);
		return this;
	}
	
	public JSONObject put(String name, long value) throws JSONException
	{
		nameValuePairs.put(checkName(name), value);
		return this;
	}
	
	public JSONObject put(String name, Object value) throws JSONException
	{
		if(value == null)
		{
			nameValuePairs.remove(name);
			return this;
		}
		if(value instanceof Number)
			JSON.checkDouble(((Number) value).doubleValue());
		nameValuePairs.put(checkName(name), value);
		return this;
	}
	
	public JSONObject putOpt(String name, Object value) throws JSONException
	{
		if(name == null || value == null)
			return this;
		return put(name, value);
	}
	
	public JSONObject accumulate(String name, Object value) throws JSONException
	{
		Object current = nameValuePairs.get(checkName(name));
		if(current == null)
			return put(name, value);
		
		if(current instanceof JSONArray)
		{
			JSONArray array = (JSONArray) current;
			array.checkedPut(value);
		} else
		{
			JSONArray array = new JSONArray();
			array.checkedPut(current);
			array.checkedPut(value);
			nameValuePairs.put(name, array);
		}
		return this;
	}
	
	public JSONObject append(String name, Object value) throws JSONException
	{
		Object current = nameValuePairs.get(checkName(name));
		final JSONArray array;
		if(current instanceof JSONArray)
			array = (JSONArray) current;
		else if(current == null)
		{
			JSONArray newArray = new JSONArray();
			nameValuePairs.put(name, newArray);
			array = newArray;
		} else
			throw new JSONException("Key " + name + " is not a JSONArray");
		array.checkedPut(value);
		return this;
	}
	
	String checkName(String name) throws JSONException
	{
		if(name == null)
			throw new JSONException("Names must be non-null");
		return name;
	}
	
	public Object remove(String name)
	{
		return nameValuePairs.remove(name);
	}
	
	public boolean isNull(String name)
	{
		Object value = nameValuePairs.get(name);
		return value == null || value == NULL;
	}
	
	public boolean has(String name)
	{
		return nameValuePairs.containsKey(name);
	}
	
	public Object get(String name) throws JSONException
	{
		Object result = nameValuePairs.get(name);
		if(result == null)
			throw new JSONException("No value for " + name);
		return result;
	}
	
	public Object opt(String name)
	{
		return nameValuePairs.get(name);
	}
	
	public boolean getBoolean(String name) throws JSONException
	{
		Object object = get(name);
		Boolean result = JSON.toBoolean(object);
		if(result == null)
			throw JSON.typeMismatch(name, object, "boolean");
		return result;
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists and is a boolean or
	 * can be coerced to a boolean, or false otherwise.
	 */
	public boolean optBoolean(String name)
	{
		return optBoolean(name, false);
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists and is a boolean or
	 * can be coerced to a boolean, or {@code fallback} otherwise.
	 */
	public boolean optBoolean(String name, boolean fallback)
	{
		Object object = opt(name);
		Boolean result = JSON.toBoolean(object);
		return result != null ? result : fallback;
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists and is a double or
	 * can be coerced to a double, or throws otherwise.
	 *
	 * @throws JSONException
	 *             if the mapping doesn't exist or cannot be coerced to a
	 *             double.
	 */
	public double getDouble(String name) throws JSONException
	{
		Object object = get(name);
		Double result = JSON.toDouble(object);
		if(result == null)
		{
			throw JSON.typeMismatch(name, object, "double");
		}
		return result;
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists and is a double or
	 * can be coerced to a double, or {@code NaN} otherwise.
	 */
	public double optDouble(String name)
	{
		return optDouble(name, Double.NaN);
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists and is a double or
	 * can be coerced to a double, or {@code fallback} otherwise.
	 */
	public double optDouble(String name, double fallback)
	{
		Object object = opt(name);
		Double result = JSON.toDouble(object);
		return result != null ? result : fallback;
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists and is an int or
	 * can be coerced to an int, or throws otherwise.
	 *
	 * @throws JSONException
	 *             if the mapping doesn't exist or cannot be coerced to an int.
	 */
	public int getInt(String name) throws JSONException
	{
		Object object = get(name);
		Integer result = JSON.toInteger(object);
		if(result == null)
		{
			throw JSON.typeMismatch(name, object, "int");
		}
		return result;
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists and is an int or
	 * can be coerced to an int, or 0 otherwise.
	 */
	public int optInt(String name)
	{
		return optInt(name, 0);
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists and is an int or
	 * can be coerced to an int, or {@code fallback} otherwise.
	 */
	public int optInt(String name, int fallback)
	{
		Object object = opt(name);
		Integer result = JSON.toInteger(object);
		return result != null ? result : fallback;
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists and is a long or
	 * can be coerced to a long, or throws otherwise. Note that JSON represents
	 * numbers as doubles, so this is <a href="#lossy">lossy</a>; use strings to
	 * transfer numbers via JSON.
	 *
	 * @throws JSONException
	 *             if the mapping doesn't exist or cannot be coerced to a long.
	 */
	public long getLong(String name) throws JSONException
	{
		Object object = get(name);
		Long result = JSON.toLong(object);
		if(result == null)
		{
			throw JSON.typeMismatch(name, object, "long");
		}
		return result;
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists and is a long or
	 * can be coerced to a long, or 0 otherwise. Note that JSON represents
	 * numbers as doubles, so this is <a href="#lossy">lossy</a>; use strings to
	 * transfer numbers via JSON.
	 */
	public long optLong(String name)
	{
		return optLong(name, 0L);
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists and is a long or
	 * can be coerced to a long, or {@code fallback} otherwise. Note that JSON
	 * represents numbers as doubles, so this is <a href="#lossy">lossy</a>; use
	 * strings to transfer numbers via JSON.
	 */
	public long optLong(String name, long fallback)
	{
		Object object = opt(name);
		Long result = JSON.toLong(object);
		return result != null ? result : fallback;
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists, coercing it if
	 * necessary, or throws if no such mapping exists.
	 *
	 * @throws JSONException
	 *             if no such mapping exists.
	 */
	public String getString(String name) throws JSONException
	{
		Object object = get(name);
		String result = JSON.toString(object);
		if(result == null)
		{
			throw JSON.typeMismatch(name, object, "String");
		}
		return result;
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists, coercing it if
	 * necessary, or the empty string if no such mapping exists.
	 */
	public String optString(String name)
	{
		return optString(name, "");
	}
	
	/**
	 * Returns the value mapped by {@code name} if it exists, coercing it if
	 * necessary, or {@code fallback} if no such mapping exists.
	 */
	public String optString(String name, String fallback)
	{
		Object object = opt(name);
		String result = JSON.toString(object);
		return result != null ? result : fallback;
	}
	
	public JSONArray getJSONArray(String name) throws JSONException
	{
		Object object = get(name);
		if(object instanceof JSONArray)
			return (JSONArray) object;
		else
			throw JSON.typeMismatch(name, object, "JSONArray");
	}
	
	public JSONArray optJSONArray(String name)
	{
		Object object = opt(name);
		return object instanceof JSONArray ? (JSONArray) object : null;
	}
	
	public JSONObject getJSONObject(String name) throws JSONException
	{
		Object object = get(name);
		if(object instanceof JSONObject)
			return (JSONObject) object;
		else
			throw JSON.typeMismatch(name, object, "JSONObject");
	}
	
	public JSONObject optJSONObject(String name)
	{
		Object object = opt(name);
		return object instanceof JSONObject ? (JSONObject) object : null;
	}
	
	public JSONArray toJSONArray(JSONArray names) throws JSONException
	{
		JSONArray result = new JSONArray();
		if(names == null)
			return null;
		int length = names.length();
		if(length == 0)
			return null;
		for(int i = 0; i < length; i++)
		{
			String name = JSON.toString(names.opt(i));
			result.put(opt(name));
		}
		return result;
	}
	
	public Iterator<String> keys()
	{
		return nameValuePairs.keySet().iterator();
	}
	
	public Set<String> keySet()
	{
		return nameValuePairs.keySet();
	}
	
	public JSONArray names()
	{
		return nameValuePairs.isEmpty() ? null : new JSONArray(new ArrayList<String>(nameValuePairs.keySet()));
	}
	
	@Override
	public String toString()
	{
		try
		{
			JSONStringer stringer = new JSONStringer();
			writeTo(stringer);
			return stringer.toString();
		} catch(JSONException e)
		{
			return null;
		}
	}
	
	public String toString(int indentSpaces) throws JSONException
	{
		JSONStringer stringer = new JSONStringer(indentSpaces);
		writeTo(stringer);
		return stringer.toString();
	}
	
	void writeTo(JSONStringer stringer) throws JSONException
	{
		stringer.object();
		for(Map.Entry<String, Object> entry : nameValuePairs.entrySet())
			stringer.key(entry.getKey()).value(entry.getValue());
		stringer.endObject();
	}
	
	public static String numberToString(Number number) throws JSONException
	{
		if(number == null)
			throw new JSONException("Number must be non-null");
		double doubleValue = number.doubleValue();
		JSON.checkDouble(doubleValue);
		if(number.equals(NEGATIVE_ZERO))
			return "-0";
		long longValue = number.longValue();
		if(doubleValue == (double) longValue)
			return Long.toString(longValue);
		return number.toString();
	}
	
	public static String quote(String data)
	{
		if(data == null)
			return "\"\"";
		try
		{
			JSONStringer stringer = new JSONStringer();
			stringer.open(JSONStringer.Scope.NULL, "");
			stringer.value(data);
			stringer.close(JSONStringer.Scope.NULL, JSONStringer.Scope.NULL, "");
			return stringer.toString();
		} catch(JSONException e)
		{
			throw new AssertionError();
		}
	}
	
	public static Object wrap(Object o)
	{
		if(o == null)
			return NULL;
		if(o instanceof JSONArray || o instanceof JSONObject)
			return o;
		if(o.equals(NULL))
			return o;
		try
		{
			if(o instanceof Collection)
				return new JSONArray((Collection<?>) o);
			else if(o.getClass().isArray())
				return new JSONArray(o);
			if(o instanceof Map)
				return new JSONObject((Map<?, ?>) o);
			if(o instanceof Boolean || o instanceof Byte || o instanceof Character || o instanceof Double || o instanceof Float || o instanceof Integer || o instanceof Long || o instanceof Short || o instanceof String)
				return o;
			if(o.getClass().getPackage().getName().startsWith("java."))
				return o.toString();
		} catch(Exception ignored)
		{
		}
		return null;
	}
}
