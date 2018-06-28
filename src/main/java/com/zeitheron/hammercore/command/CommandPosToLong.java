package com.zeitheron.hammercore.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
		BlockPos pos = parseBlockPos(sender, args, 0, true);
		Vec3d e = sender.getPositionVector();
		
		if(args.length == 3)
			sender.sendMessage(new TextComponentString("Position: " + pos.toLong()));
		else
			sender.sendMessage(new TextComponentString("Invalid length of " + args.length + ". Expected 3."));
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
	{
		return Arrays.asList(args.length == 1 ? targetPos.getX() + "" : args.length == 2 ? targetPos.getY() + "" : args.length == 3 ? targetPos.getZ() + "" : "");
	}
}