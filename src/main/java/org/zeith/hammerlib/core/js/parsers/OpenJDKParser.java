package org.zeith.hammerlib.core.js.parsers;

import org.openjdk.nashorn.api.scripting.ClassFilter;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.zeith.hammerlib.core.js.converters.IJsConverter;
import org.zeith.hammerlib.core.js.converters.fb.BindingsFallbackJsConverter;
import org.zeith.hammerlib.core.js.converters.fb.CastingFallbackJsConverter;
import org.zeith.hammerlib.core.js.converters.openjdk.*;

import javax.script.ScriptEngine;
import java.util.Arrays;
import java.util.List;

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
		return engineFactory.getScriptEngine(classFilter);
	}
}