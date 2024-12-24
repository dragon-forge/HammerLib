package org.zeith.hammerlib.client.flowgui.reader;

import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.core.js.*;
import org.zeith.hammerlib.util.java.Cast;

import javax.script.ScriptEngine;
import java.util.*;
import java.util.regex.Pattern;

public class JsContext
{
	private static final Pattern LAMBDA = Pattern.compile("^\\s*\\((?<args>[^)]*)\\)\\s*=>\\s*");
	private final Map<CachedType, ScriptType> scriptCache = new HashMap<>();
	
	public static boolean isScript(String expression)
	{
		return LAMBDA.matcher(expression).find();
	}
	
	public <T> T eval(Class<T> interfaceType, String input, CallerSpec spec)
	{
		var se = scriptCache.computeIfAbsent(new CachedType(interfaceType, input, spec), JsContext::load);
		return Cast.cast(se.itf(), interfaceType);
	}
	
	private static ScriptType load(CachedType expression)
	{
		try
		{
			var text = expression.input();
			var lambda = LAMBDA.matcher(text);
			if(!lambda.find()) throw new IllegalArgumentException("JS expression must start with lambda: " + text);
			String args = lambda.group("args");
			
			text = text.substring(lambda.end());
			text = ExpressionFixer.fixExpression(text);
			
			var usedArgs = Arrays.stream(args.split(",")).map(String::strip).toList();
			
			var tup = ExpressionParser.parse(text, expression.spec(), usedArgs, expression.type());
			
			return new ScriptType(tup.b(), usedArgs, tup.a());
		} catch(Exception e)
		{
			HammerLib.LOG.error("Failed to parse script <{}>", expression.input(), e);
			return new ScriptType(null, List.of(), null);
		}
	}
	
	public void clear()
	{
		scriptCache.clear();
	}
	
	private record ScriptType(Object itf, List<String> usedArgs, ScriptEngine engine) {}
	
	private record CachedType(Class<?> type, String input, CallerSpec spec) {}
}