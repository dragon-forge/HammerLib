package com.zeitheron.hammercore.lib.nashorn;

import com.zeitheron.hammercore.utils.java.itf.IArgumentFunction;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSScript
{
	private final JSSource sourceUnmodified;
	protected final Invocable root;
	protected final ScriptEngine engine;
	
	public JSScript(JSSource source) throws ScriptException
	{
		this.sourceUnmodified = source;
		
		this.engine = new ScriptEngineManager(null).getEngineByName("nashorn");
		this.engine.eval(handlePreprocessing(getSource()).read());
		this.root = (Invocable) this.engine;
	}
	
	public IArgumentFunction getFunction(String name)
	{
		Object o = engine.get(name);
		if(o instanceof ScriptObjectMirror)
		{
			ScriptObjectMirror mirror = (ScriptObjectMirror) o;
			if(mirror.isFunction()) return args -> toJava(mirror.call(mirror, args));
		}
		return null;
	}
	
	public JSCallbackInfo callFunction(String name, Object... arguments)
	{
		Object o = engine.get(name);
		if(o instanceof ScriptObjectMirror)
		{
			ScriptObjectMirror mirror = (ScriptObjectMirror) o;
			if(!mirror.isFunction()) return new JSCallbackInfo(false, false, new NoSuchMethodException(name));
			try
			{
				return new JSCallbackInfo(toJava(mirror.call(mirror, arguments)));
			} catch(Throwable e)
			{
				return new JSCallbackInfo(true, false, e);
			}
		}
		return new JSCallbackInfo(false, false, new NoSuchMethodException(name));
	}
	
	public static Object toJava(Object jsObj)
	{
		if(jsObj instanceof ScriptObjectMirror)
		{
			ScriptObjectMirror js = (ScriptObjectMirror) jsObj;
			if(js.isFunction())
			{
				return (IArgumentFunction) (args) -> toJava(js.call(js, args));
			} else if(js.isArray())
			{
				List<Object> list = new ArrayList<>();
				for(Map.Entry<String, Object> entry : js.entrySet())
					list.add(toJava(entry.getValue()));
				return list;
			} else
			{
				Map<String, Object> map = new HashMap<>();
				for(Map.Entry<String, Object> entry : js.entrySet())
					map.put(entry.getKey(), toJava(entry.getValue()));
				return map;
			}
		} else return jsObj;
	}
	
	protected JSSource handlePreprocessing(JSSource src)
	{
		return src;
	}
	
	public final JSSource getSource()
	{
		return sourceUnmodified.copy();
	}
}