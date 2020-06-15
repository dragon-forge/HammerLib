package com.zeitheron.hammercore.lib.nashorn;

import com.zeitheron.hammercore.lib.zlib.utils.Joiner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class JSImporter
{
	public static final Joiner PARAM_JOINER = Joiner.on(", ");
	
	/**
	 * Creates references to static methods in the passed class
	 */
	public static String createJSWrap(Class<?> type, BiPredicate<String, Method> include)
	{
		StringBuilder sb = new StringBuilder();
		for(Method m : type.getMethods())
			if(Modifier.isPublic(m.getModifiers()) && Modifier.isStatic(m.getModifiers()) && include.test(m.getName(), m))
			{
				List<String> params = new ArrayList<>();
				for(int i = 0; i < m.getParameterCount(); ++i)
					params.add("par" + i);
				String parmsStr = PARAM_JOINER.join(params);
				
				sb
						.append("function ")
						.append(m.getName())
						.append("(")
						.append(parmsStr)
						.append(") { return Java.type(\"")
						.append(type.getCanonicalName())
						.append("\").")
						.append(m.getName())
						.append("(")
						.append(parmsStr)
						.append("); }");
			}
		return sb.toString();
	}
	
	static final BiPredicate<String, Method> alwaysTrue = (s, m) -> true;
	
	public static String createJSWrap(Class<?> type)
	{
		return createJSWrap(type, alwaysTrue);
	}
}