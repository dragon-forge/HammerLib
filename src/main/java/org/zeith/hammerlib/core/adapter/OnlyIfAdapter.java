package org.zeith.hammerlib.core.adapter;

import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OnlyIfAdapter
{
	private static boolean recursiveGetBool(Object inst, Object objInQuestion, Class<?> c, String... subs)
	{
		try
		{
			Object add = null;
			int i;
			try
			{
				Field f = c.getDeclaredField(subs[0]);
				f.setAccessible(true);
				add = f.get(inst);
			} catch(NoSuchFieldException e)
			{
				Method m = ReflectionUtil.findDeclaredMethod(c, subs[0], mt -> mt.getParameterCount() == 0 || mt.getParameterCount() == 1);
				m.setAccessible(true);
				if(m.getParameterCount() == 0)
					add = m.invoke(inst);
				if(m.getParameterCount() == 1)
					add = m.invoke(inst, objInQuestion);
			} catch(IllegalAccessException e)
			{
				e.printStackTrace();
			}

			if(add == null)
				throw new NoSuchMethodException("Unable to get property of " + c + "." + subs[0]);

			if(boolean.class.isAssignableFrom(add.getClass()) || Boolean.class.isAssignableFrom(add.getClass()))
				return (Boolean) add;
			else if(subs.length > 1)
			{
				String[] subs2 = new String[subs.length - 1];
				System.arraycopy(subs, 1, subs2, 0, subs2.length);
				return recursiveGetBool(add, objInQuestion, add.getClass(), subs2);
			} else
				throw new NoSuchMethodException("Unable to get property of " + c + "." + subs[0] + " as a boolean.");
		} catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static boolean checkCondition(OnlyIf onlyIf, String source, String type, Object objInQuestion)
	{
		boolean add = true;

		if(onlyIf != null)
		{
			String member = onlyIf.member();
			try
			{
				String[] mp = member.split("[.]");

				int i;
				Class<?> c = onlyIf.owner();
				add = recursiveGetBool(null, objInQuestion, c, mp);
			} catch(RuntimeException e)
			{
				e.getCause().printStackTrace();
				HammerLib.LOG.warn("Failed to parse @OnlyIf({}) in {}! {} will" + (onlyIf.invert() ? " not be registered due to inversion." : " be registered anyway."), JSONObject.quote(member), source, type);
			}

			if(onlyIf.invert()) add = !add;
		}

		return add;
	}
}