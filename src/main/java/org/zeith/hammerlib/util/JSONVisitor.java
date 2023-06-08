package org.zeith.hammerlib.util;

import org.zeith.hammerlib.util.shaded.json.JSONArray;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class JSONVisitor
{
	public static Map<String, Object> expand(Object object, String delimiter, boolean includeTrees)
	{
		final AtomicReference<BiConsumer<String, Object>> visitorRef = new AtomicReference<>();
		final Map<String, Object> map = new HashMap<>();
		BiConsumer<String, Object> visitor = (s, o0) ->
		{
			var tree = false;
			if(o0 instanceof JSONObject o)
			{
				visit(s + (s.isEmpty() ? "" : delimiter), o, visitorRef.get());
				tree = true;
			} else if(o0 instanceof JSONArray a)
			{
				visit(s, a, visitorRef.get());
				tree = true;
			}
			
			if(!tree || includeTrees)
				map.put(s, o0);
		};
		visitorRef.set(visitor);
		visitor.accept("", object);
		return map;
	}
	
	public static void visit(String prefix, JSONObject $, BiConsumer<String, Object> visitor)
	{
		for(var key : $.keySet())
			visitor.accept(prefix + key, $.get(key));
	}
	
	public static void visit(String prefix, JSONArray $, BiConsumer<String, Object> visitor)
	{
		var l = $.size();
		for(var key = 0; key < l; ++key)
			visitor.accept(prefix + "[" + key + "]", $.get(key));
	}
}