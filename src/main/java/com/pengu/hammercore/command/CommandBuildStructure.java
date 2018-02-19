package com.pengu.hammercore.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pengu.hammercore.common.structure.StructureAPI;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandBuildStructure implements ICommand
{
	@Override
	public String getName()
	{
		return "hc_buildstr";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Use /hc_buildstr <structure, use TAB> OR /hc_buildstr <x> <y> <z> <structure, use TAB>";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length != 1 && args.length != 4)
		{
			sender.sendMessage(new TextComponentString(TextFormatting.RED + "Illegal argument count, required: 1 or 4"));
			return;
		}
		
		BlockPos startPos = null;
		if(args.length == 1)
			startPos = sender.getPosition();
		if(args.length == 4)
			startPos = CommandBase.parseBlockPos(sender, args, 0, false);
		
		if(!StructureAPI.GetAllStructures().contains(args[0]))
		{
			sender.sendMessage(new TextComponentString(TextFormatting.RED + "Illegal value (" + args[0] + ")"));
			return;
		}
		
		sender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Building " + args[0] + "..."));
		long start = System.currentTimeMillis();
		try
		{
			StructureAPI.Import(sender.getEntityWorld(), startPos, StructureAPI.ParseBuiltin(new ResourceLocation(args[0])));
			sender.sendMessage(new TextComponentString(TextFormatting.GREEN + args[0] + " built in " + (System.currentTimeMillis() - start) + " ms."));
		} catch(IOException e)
		{
			e.printStackTrace();
			sender.sendMessage(new TextComponentString(TextFormatting.RED + args[0] + " wasn't built: " + e.getMessage()));
			return;
		}
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
	{
		return args.length == 1 ? new ArrayList<>(StructureAPI.GetAllStructures()) : args.length > 1 && args.length < 5 ? CommandBase.getTabCompletionCoordinate(args, 1, targetPos) : new ArrayList<>();
	}
	
	@Override
	public int compareTo(ICommand o)
	{
		return -1;
	}
	
	@Override
	public List<String> getAliases()
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return sender.canUseCommand(2, "gamemode");
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return false;
	}
}