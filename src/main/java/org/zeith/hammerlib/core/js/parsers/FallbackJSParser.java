package org.zeith.hammerlib.core.js.parsers;

import org.zeith.hammerlib.core.js.converters.IJsConverter;
import org.zeith.hammerlib.core.js.converters.fb.*;

import javax.script.*;
import java.util.*;

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