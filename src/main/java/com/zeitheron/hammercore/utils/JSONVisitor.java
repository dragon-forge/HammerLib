package com.zeitheron.hammercore.utils;

import com.zeitheron.hammercore.lib.zlib.json.*;

import java.util.*;
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
			boolean tree = false;
			if(o0 instanceof JSONObject)
			{
				JSONObject o = (JSONObject) o0;
				visit(s + (s.isEmpty() ? "" : delimiter), o, visitorRef.get());
				tree = true;
			} else if(o0 instanceof JSONArray)
			{
				JSONArray a = (JSONArray) o0;
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
		for(String key : $.keySet())
			visitor.accept(prefix + key, $.get(key));
	}
	
	public static void visit(String prefix, JSONArray $, BiConsumer<String, Object> visitor)
	{
		int l = $.size();
		for(int key = 0; key < l; ++key)
			visitor.accept(prefix + "[" + key + "]", $.get(key));
	}
}