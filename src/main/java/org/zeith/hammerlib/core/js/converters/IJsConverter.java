package org.zeith.hammerlib.core.js.converters;

import javax.script.*;

public interface IJsConverter
{
	<T> T parseAsInterface(ScriptEngine engine, String eval, Class<T> targetItf)
			throws ScriptException;
}