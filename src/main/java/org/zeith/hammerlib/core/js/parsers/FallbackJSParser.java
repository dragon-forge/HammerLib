package org.zeith.hammerlib.core.js.parsers;

import org.zeith.hammerlib.core.js.converters.IJsConverter;
import org.zeith.hammerlib.core.js.converters.fb.BindingsFallbackJsConverter;
import org.zeith.hammerlib.core.js.converters.fb.CastingFallbackJsConverter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;
import java.util.List;

public class FallbackJSParser
		implements IJsParser
{
	@Override
	public List<IJsConverter> getConverters()
	{
		return Arrays.asList(
				new CastingFallbackJsConverter(),
				new BindingsFallbackJsConverter()
		);
	}
	
	@Override
	public ScriptEngine create()
	{
		return new ScriptEngineManager().getEngineByExtension("js");
	}
}