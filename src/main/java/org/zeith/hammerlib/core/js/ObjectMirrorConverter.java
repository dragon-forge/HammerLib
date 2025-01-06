package org.zeith.hammerlib.core.js;

import com.google.gson.*;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;
import org.zeith.hammerlib.util.shaded.json.JSONArray;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

/**
 * More effective way of converting JSON from JS to Java without having to use JSON.stringify and parsing json.
 */
public class ObjectMirrorConverter
{
	public static JsonElement toGson(ScriptObjectMirror mirror)
	{
		if(mirror.isArray())
		{
			JsonArray a = new JsonArray();
			int s = mirror.size();
			for(int i = 0; i < s; i++) a.add(toGson(mirror.getSlot(i)));
			return a;
		} else
		{
			JsonObject o = new JsonObject();
			for(String key : mirror.getOwnKeys(true))
				o.add(key, toGson(mirror.get(key)));
			return o;
		}
	}
	
	private static JsonElement toGson(Object o)
	{
		if(o == null) return JsonNull.INSTANCE;
		if(o instanceof ScriptObjectMirror som) return toGson(som);
		if(o instanceof Boolean b) return new JsonPrimitive(b);
		if(o instanceof Number n) return new JsonPrimitive(n);
		if(o instanceof String s) return new JsonPrimitive(s);
		if(o instanceof Character c) return new JsonPrimitive(c);
		return JsonNull.INSTANCE;
	}
	
	public static Object toJson(ScriptObjectMirror mirror)
	{
		if(mirror.isArray())
		{
			JSONArray a = new JSONArray();
			int s = mirror.size();
			for(int i = 0; i < s; i++) a.put(toJson(mirror.getSlot(i)));
			return a;
		} else
		{
			JSONObject o = new JSONObject();
			for(String key : mirror.getOwnKeys(true))
				o.put(key, toJson(mirror.get(key)));
			return o;
		}
	}
	
	private static Object toJson(Object o)
	{
		if(o == null) return null;
		if(o instanceof ScriptObjectMirror som) return toJson(som);
		if(o instanceof Boolean b) return b;
		if(o instanceof Number n) return n;
		if(o instanceof String s) return s;
		if(o instanceof Character c) return c;
		return null;
	}
}