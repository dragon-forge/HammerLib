package org.zeith.hammerlib.core.js.parsers;

import org.openjdk.nashorn.api.scripting.*;
import org.zeith.hammerlib.core.js.converters.IJsConverter;
import org.zeith.hammerlib.core.js.converters.fb.*;
import org.zeith.hammerlib.core.js.converters.openjdk.*;

import javax.script.ScriptEngine;
import java.util.*;

public class OpenJDKParser
		implements IJsParser
{
	private final NashornScriptEngineFactory engineFactory;
	private final ClassFilter classFilter;
	
	public OpenJDKParser()
	{
		engineFactory = new NashornScriptEngineFactory();
		classFilter = c -> false;
	}
	
	@Override
	public List<IJsConverter> getConverters()
	{
		return Arrays.asList(
				new CastingFallbackJsConverter(),
				new DirectJ8ScriptObjectMirrorConverter(),
				new DirectJ8ScriptObjectMirrorConverterWithProxy(),
				new BindingsFallbackJsConverter(),
				new BindingsJ8ScriptObjectMirrorConverter()
		);
	}
	
	@Override
	public ScriptEngine create()
	{
		return engineFactory.getScriptEngine(new String[] {
						"-doe",
						"--language=es6"
				}, getAppClassLoader(), classFilter
		);
	}
	
	private static ClassLoader getAppClassLoader()
	{
		// Revisit: script engine implementation needs the capability to
		// find the class loader of the context in which the script engine
		// is running so that classes will be found and loaded properly
		return Objects.requireNonNullElseGet(
				Thread.currentThread().getContextClassLoader(),
				NashornScriptEngineFactory.class::getClassLoader
		);
	}
}