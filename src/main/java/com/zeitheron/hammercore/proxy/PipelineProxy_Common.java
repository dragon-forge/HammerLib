package com.zeitheron.hammercore.proxy;

import java.lang.reflect.Constructor;

import javax.annotation.Nullable;

import com.zeitheron.hammercore.HammerCore;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class PipelineProxy_Common
{
	public Side getGameSide()
	{
		return Side.SERVER;
	}
	
	public void sendToServer(Packet packet)
	{
		
	}
	
	public void sendTo(Packet packet, EntityPlayerMP player)
	{
		player.connection.sendPacket(packet);
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
	
	public void runFromMainThread(Side side, Runnable task)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(side == Side.SERVER)
			if(server != null)
				server.addScheduledTask(task);
			else
				HammerCore.LOG.error("Dropped runnable task " + task + "! This shouldn't happen!");
	}
}