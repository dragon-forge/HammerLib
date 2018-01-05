package com.pengu.hammercore.api;

import java.util.List;

/**
 * Dynamic java code that loads on mod construct
 */
public interface iJavaCode
{
	public void preInit();
	
	public void init();
	
	public void postInit();
	
	default public void addMCFObjects(List<Object> mcf)
	{
	};
	
	public static class IJavaCode_IMPL implements iJavaCode
	{
		private final Object c;
		
		public IJavaCode_IMPL(Object cls)
		{
			c = cls;
		}
		
		@Override
		public void preInit()
		{
			try
			{
				c.getClass().getDeclaredMethod("preInit").invoke(c);
			} catch(Throwable err)
			{
			}
		}
		
		@Override
		public void init()
		{
			try
			{
				c.getClass().getDeclaredMethod("init").invoke(c);
			} catch(Throwable err)
			{
			}
		}
		
		@Override
		public void postInit()
		{
			try
			{
				c.getClass().getDeclaredMethod("postInit").invoke(c);
			} catch(Throwable err)
			{
			}
		}
		
		@Override
		public void addMCFObjects(List<Object> mcf)
		{
			try
			{
				c.getClass().getDeclaredMethod("addMCFObjects", List.class).invoke(c, mcf);
			} catch(Throwable err)
			{
			}
		}
	}
}