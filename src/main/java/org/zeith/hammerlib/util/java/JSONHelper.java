package org.zeith.hammerlib.util.java;

import org.zeith.hammerlib.util.shaded.json.JSONArray;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

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
}