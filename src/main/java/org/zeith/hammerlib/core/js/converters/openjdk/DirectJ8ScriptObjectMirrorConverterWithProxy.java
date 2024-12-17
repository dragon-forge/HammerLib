package org.zeith.hammerlib.core.js.converters.openjdk;

import lombok.val;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;
import org.zeith.hammerlib.core.js.converters.IJsConverter;
import org.zeith.hammerlib.util.java.Cast;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.lang.reflect.*;

public class DirectJ8ScriptObjectMirrorConverterWithProxy
		implements IJsConverter
{
	@Override
	public <T> T parseAsInterface(ScriptEngine engine, String eval, Class<T> targetItf)
			throws ScriptException
	{
		ScriptObjectMirror mirror = Cast.cast(engine.eval(eval), ScriptObjectMirror.class);
		if(mirror == null) return null;
		
		//noinspection unchecked
		return (T) Proxy.newProxyInstance(targetItf.getClassLoader(), new Class[] { targetItf }, new InvocationHandler()
		{
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable
			{
				val n = method.getName();
				return mirror.hasMember(n) ? mirror.callMember(n, args) : null;
			}
		});
	}
}