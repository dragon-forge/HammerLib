package org.zeith.hammerlib.core.js;

import lombok.val;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.core.js.converters.IJsConverter;
import org.zeith.hammerlib.core.js.math.IDoubleTest;
import org.zeith.hammerlib.core.js.parsers.*;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.*;

public class JsFactory
{
	private static final List<IJsParser> PARSERS = Arrays.asList(
			new OpenJDKParser(),
			new FallbackJSParser()
	);
	
	private static boolean loadedParser, loadedConverter;
	private static IJsParser parser;
	private static IJsConverter converter;
	
	public static IJsParser getParser()
	{
		if(!loadedParser) newEngine();
		return parser;
	}
	
	public static IJsConverter getConverter()
	{
		if(loadedConverter) return converter;
		val parser = getParser();
		
		String expression = "a+b";
		String fun = "function get() {\n\treturn " + expression + ";\n}";
		
		for(IJsConverter c : parser.getConverters())
		{
			val se = parser.create();
			try
			{
				se.put("a", 1);
				se.put("b", 2);
				IDoubleTest idt = c.parseAsInterface(se, fun, IDoubleTest.class);
				if(idt == null) continue;
				
				if(!nearlyEq(idt.get(), 3)) continue;
				se.put("a", 2);
				se.put("b", 2);
				if(!nearlyEq(idt.get(), 4)) continue;
				
				HammerLib.LOG.info("JS Converter {} has passed!", c);
				loadedConverter = true;
				converter = c;
				return c;
			} catch(ScriptException e)
			{
				HammerLib.LOG.info("JS Converter {} has failed.", c);
				continue;
			}
		}
		
		loadedConverter = true;
		return converter;
	}
	
	private static boolean nearlyEq(double a, double b)
	{
		return Math.abs(a - b) < 0.0001;
	}
	
	public static ScriptEngine newEngine()
	{
		if(!loadedParser)
		{
			for(IJsParser jsp : PARSERS)
			{
				val jse = jsp.create();
				if(jse != null)
				{
					parser = jsp;
					loadedParser = true;
					return jse;
				}
			}
			loadedParser = true;
		}
		return parser != null ? parser.create() : null;
	}
	
	public static <T> Tuple2<ScriptEngine, T> parse(Class<T> interfaceType, Map<String, Object> properties, String expression)
			throws ScriptException
	{
		ScriptEngine se = newEngine();
		IJsConverter cs = getConverter();
		if(se == null || cs == null) return null;
		properties.forEach(se::put);
		val t = cs.parseAsInterface(se, expression, interfaceType);
		if(t == null) return null;
		return Tuples.immutable(se, t);
	}
	
	public static void init(boolean require)
	{
		IJsParser p = getParser();
		IJsConverter c = getConverter();
		if((p == null || c == null) && require) throw new IllegalStateException("Nashorn JS environment was not found.");
		HammerLib.LOG.info("Using {} JS parser with {} converter.", p, c);
	}
	
	public static void isolateJava(Map<String, Object> props)
	{
		props.put("load", null);
		props.put("loadWithNewGlobal", null);
		props.put("exit", null);
		props.put("quit", null);
		props.put("Java", null);
		
		props.put("java", null);
		props.put("javax", null);
		props.put("javafx", null);
		props.put("org", null);
		props.put("com", null);
		props.put("edu", null);
	}
}