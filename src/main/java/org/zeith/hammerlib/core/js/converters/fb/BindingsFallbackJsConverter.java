package org.zeith.hammerlib.core.js.converters.fb;

import lombok.val;
import org.zeith.hammerlib.core.js.converters.IJsConverter;

import javax.script.*;
import java.lang.reflect.Proxy;

public class BindingsFallbackJsConverter
		implements IJsConverter
{
	@Override
	public <T> T parseAsInterface(ScriptEngine engine, String eval, Class<T> targetItf)
			throws ScriptException
	{
		engine.eval(eval);
		
		if(!(engine instanceof Invocable))
			return null;
		
		Invocable inv = (Invocable) engine;
		
		//noinspection unchecked
		return (T) Proxy.newProxyInstance(targetItf.getClassLoader(), new Class[] { targetItf }, (proxy, method, args) ->
		{
			val n = method.getName();
			return inv.invokeFunction(n, args);
		});
	}
}