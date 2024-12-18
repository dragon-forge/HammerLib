package org.zeith.hammerlib.client.flowgui.reader;

import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.core.js.*;
import org.zeith.hammerlib.util.java.Cast;

import javax.script.ScriptEngine;
import java.util.HashMap;
import java.util.Map;

public class JsContext
{
	private final Map<CachedType, ScriptType> scriptCache = new HashMap<>();
	
	public <T> T eval(Class<T> interfaceType, String input, CallerSpec spec)
	{
		var se = scriptCache.computeIfAbsent(new CachedType(interfaceType, input, spec), JsContext::load);
		return Cast.cast(se.itf(), interfaceType);
	}
	
	private static ScriptType load(CachedType expression)
	{
		try
		{
			var text = ExpressionFixer.fixExpression(expression.input());
			var tup = ExpressionParser.parse(text, expression.spec(), expression.type());
			return new ScriptType(tup.b(), tup.a());
		} catch(Exception e)
		{
			HammerLib.LOG.error("Failed to parse script <{}>", expression.input(), e);
			return new ScriptType(null, null);
		}
	}
	
	public void clear()
	{
		scriptCache.clear();
	}
	
	private record ScriptType(Object itf, ScriptEngine engine) {}
	
	private record CachedType(Class<?> type, String input, CallerSpec spec) {}
}