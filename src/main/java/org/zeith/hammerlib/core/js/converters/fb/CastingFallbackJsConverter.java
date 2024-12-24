package org.zeith.hammerlib.core.js.converters.fb;

import org.zeith.hammerlib.core.js.converters.IJsConverter;

import javax.script.*;

public class CastingFallbackJsConverter
		implements IJsConverter
{
	@Override
	public <T> T parseAsInterface(ScriptEngine engine, String eval, Class<T> targetItf)
			throws ScriptException
	{
		engine.eval(eval);
		if(!(engine instanceof Invocable)) return null;
		Invocable inv = (Invocable) engine;
		return inv.getInterface(targetItf);
	}
}