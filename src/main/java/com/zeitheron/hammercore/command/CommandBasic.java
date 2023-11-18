package com.zeitheron.hammercore.command;

import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;

public class CommandBasic
		extends CommandBase
{
	protected final String name;
	protected final int permLevel;
	protected final ICommandExecutor executor;
	
	public CommandBasic(String name, int permLevel, ICommandExecutor executor)
	{
		this.name = name;
		this.permLevel = permLevel;
		this.executor = executor;
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return permLevel;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return name;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args)
			throws CommandException
	{
		executor.execute(server, sender, args);
	}
	
	public interface ICommandExecutor
	{
		void execute(MinecraftServer server, ICommandSender sender, String[] args)
				throws CommandException;
	}
}