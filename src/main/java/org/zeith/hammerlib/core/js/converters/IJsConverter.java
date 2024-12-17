package org.zeith.hammerlib.core.js.converters;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

public interface IJsConverter
{
	<T> T parseAsInterface(ScriptEngine engine, String eval, Class<T> targetItf)
			throws ScriptException;
}