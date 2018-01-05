package com.pengu.hammercore.proxy;

import java.lang.reflect.Constructor;

import javax.annotation.Nullable;

import net.minecraftforge.fml.relauncher.Side;

public class PipelineProxy_Common
{
	public Side getGameSide()
	{
		return Side.SERVER;
	}
	
	@Nullable
	public final <T> T pipeIfOnGameSide(T t, Side passSide)
	{
		if(passSide == getGameSide())
			return t;
		return null;
	}
	
	@Nullable
	public final <T> T createAndPipeIfOnGameSide(String pipedClass, Side passSide, Object... arguments)
	{
		if(passSide == getGameSide())
		{
			try
			{
				Class<T> c = (Class<T>) Class.forName(pipedClass);
				Class<?>[] args = new Class<?>[arguments.length];
				for(int i = 0; i < args.length; ++i)
					args[i] = arguments[i].getClass();
				Constructor<T> constr = c.getConstructor(args);
				return constr.newInstance(arguments);
			} catch(Throwable err)
			{
			}
		}
		return null;
	}
	
	@Nullable
	public final <T> T createAndPipeDependingOnSide(String clientClass, String serverClass, Object... arguments)
	{
		if(getGameSide() == Side.CLIENT)
			return createAndPipeIfOnGameSide(clientClass, getGameSide(), arguments);
		if(getGameSide() == Side.SERVER)
			return createAndPipeIfOnGameSide(serverClass, getGameSide(), arguments);
		return null;
	}
}