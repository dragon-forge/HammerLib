package com.pengu.hammercore.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandPosToLong extends CommandBase
{
	@Override
	public String getName()
	{
		return "hc_postolong";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length == 3)
		{
			int x = parseInt(args[0], -30_000_0000, 30_000_0000);
			int y = parseInt(args[1], 0, 256);
			int z = parseInt(args[2], -30_000_0000, 30_000_0000);
			
			sender.sendMessage(new TextComponentString("Position: " + new BlockPos(x, y, z).toLong()));
		} else
			sender.sendMessage(new TextComponentString("Invalid length of " + args.length + ". Expected 3."));
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
	{
		return Arrays.asList(args.length == 1 ? targetPos.getX() + "" : args.length == 2 ? targetPos.getY() + "" : args.length == 3 ? targetPos.getZ() + "" : "");
	}
}