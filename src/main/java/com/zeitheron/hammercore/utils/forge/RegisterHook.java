package com.zeitheron.hammercore.utils.forge;

import com.zeitheron.hammercore.utils.IRegisterListener;

import java.lang.annotation.*;
import java.lang.reflect.*;

/**
 * Annotate a method inside objects implementing {@link IRegisterListener} whenever the {@link IRegisterListener#onRegistered()} is final.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterHook
{
	class HookCollector
	{
		public static void propagate(IRegisterListener inst)
		{
			propagateInstance(inst.getClass(), inst);
		}
		
		private static void propagateInstance(Class<?> cls, IRegisterListener inst)
		{
			if(cls == null || cls.equals(Object.class))
				return;
			
			for(Method method : cls.getDeclaredMethods())
			{
				if(method.isAnnotationPresent(RegisterHook.class) && method.getParameterCount() == 0)
				{
					method.setAccessible(true);
					try
					{
						method.invoke(inst);
					} catch(IllegalAccessException | InvocationTargetException e)
					{
						throw new RuntimeException(e);
					}
				}
			}
			
			propagateInstance(cls.getSuperclass(), inst);
		}
	}
}