package org.zeith.hammerlib.util.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.data.IDataArray;
import org.zeith.hammerlib.api.data.IDataTree;
import org.zeith.hammerlib.util.shaded.json.JSONArray;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

import java.util.Set;

public class JSONHelper
{
	public static JSONObject computeObject(JSONObject root, String key)
	{
		if(root.has(key)) return root.getJSONObject(key);
		return root.put(key, new JSONObject()).getJSONObject(key);
	}
	
	public static JSONArray computeArray(JSONObject root, String key)
	{
		if(root.has(key)) return root.getJSONArray(key);
		return root.put(key, new JSONArray()).getJSONArray(key);
	}
	
	private static Object data(Object val)
	{
		if(val instanceof JSONObject o) return dataView(o);
		if(val instanceof JSONArray a) return dataView(a);
		return val;
	}
	
	public static IDataTree dataView(JSONObject obj)
	{
		return new IDataTree()
		{
			@Override
			public @Nullable Object get(String key)
			{
				return data(obj.opt(key));
			}
			
			@Override
			public @NotNull Set<String> keys()
			{
				return obj.keySet();
			}
		};
	}
	
	public static IDataArray dataView(JSONArray obj)
	{
		return new IDataArray()
		{
			@Override
			public @Nullable Object get(int index)
			{
				return data(obj.opt(index));
			}
			
			@Override
			public int length()
			{
				return obj.length();
			}
		};
	}
}