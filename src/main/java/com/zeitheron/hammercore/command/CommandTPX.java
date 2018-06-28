package com.zeitheron.hammercore.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zeitheron.hammercore.utils.WorldUtil;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandTPX extends CommandBase
{
	@Override
	public String getName()
	{
		return "hc_tpx";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Used to tp between 2 points on server. Can be used between dimensions";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		EntityPlayerMP mp = getCommandSenderAsPlayer(sender);
		
		if(args.length == 1)
		{
			EntityPlayerMP target = getPlayer(server, sender, args[0]);
			int dim = target.world.provider.getDimension();
			WorldUtil.teleportPlayer(mp, dim, target.posX, target.posY, target.posZ);
			mp.sendMessage(new TextComponentString("Teleported to " + args[0] + " in dimension " + dim));
		} else if(args.length == 3)
		{
			double x = args[0].startsWith("~") ? mp.posX + (args[0].length() > 1 ? parseDouble(args[0].substring(1)) : 0) : parseDouble(args[0]);
			double y = args[1].startsWith("~") ? mp.posY + (args[1].length() > 1 ? parseDouble(args[1].substring(1)) : 0) : parseDouble(args[1]);
			double z = args[2].startsWith("~") ? mp.posZ + (args[2].length() > 1 ? parseDouble(args[2].substring(1)) : 0) : parseDouble(args[2]);
			
			WorldUtil.teleportPlayer(mp, mp.world.provider.getDimension(), x, y, z);
			mp.sendMessage(new TextComponentString("Teleported to " + x + " " + y + " " + z));
		} else if(args.length == 4)
		{
			try
			{
				mp = getPlayer(server, sender, args[0]);
				
				double x = args[1].startsWith("~") ? mp.posX + (args[1].length() > 1 ? parseDouble(args[1].substring(1)) : 0) : parseDouble(args[1]);
				double y = args[2].startsWith("~") ? mp.posY + (args[2].length() > 1 ? parseDouble(args[2].substring(1)) : 0) : parseDouble(args[2]);
				double z = args[3].startsWith("~") ? mp.posZ + (args[3].length() > 1 ? parseDouble(args[3].substring(1)) : 0) : parseDouble(args[3]);
				
				WorldUtil.teleportPlayer(mp, mp.world.provider.getDimension(), x, y, z);
				mp.sendMessage(new TextComponentString("Teleported " + args[0] + " to " + x + " " + y + " " + z));
			} catch(PlayerNotFoundException nfe)
			{
				double x = args[0].startsWith("~") ? mp.posX + (args[0].length() > 1 ? parseDouble(args[0].substring(1)) : 0) : parseDouble(args[0]);
				double y = args[1].startsWith("~") ? mp.posY + (args[1].length() > 1 ? parseDouble(args[1].substring(1)) : 0) : parseDouble(args[1]);
				double z = args[2].startsWith("~") ? mp.posZ + (args[2].length() > 1 ? parseDouble(args[2].substring(1)) : 0) : parseDouble(args[2]);
				int dim = parseInt(args[3]);
				
				WorldUtil.teleportPlayer(mp, dim, x, y, z);
				mp.sendMessage(new TextComponentString("Teleported to " + x + " " + y + " " + z + " in dimension " + dim));
			}
		} else if(args.length == 5)
		{
			mp = getPlayer(server, sender, args[0]);
			
			double x = args[1].startsWith("~") ? mp.posX + (args[1].length() > 1 ? parseDouble(args[1].substring(1)) : 0) : parseDouble(args[1]);
			double y = args[2].startsWith("~") ? mp.posY + (args[2].length() > 1 ? parseDouble(args[2].substring(1)) : 0) : parseDouble(args[2]);
			double z = args[3].startsWith("~") ? mp.posZ + (args[3].length() > 1 ? parseDouble(args[3].substring(1)) : 0) : parseDouble(args[3]);
			int dim = parseInt(args[4]);
			
			WorldUtil.teleportPlayer(mp, dim, x, y, z);
			mp.sendMessage(new TextComponentString("Teleported " + args[0] + " to " + x + " " + y + " " + z + " in dimension " + dim));
		} else
			sender.sendMessage(new TextComponentString("Invalid length of " + args.length + ". Expected 1, 3, 4 or 5."));
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
	{
		return args.length == 1 ? Arrays.asList(server.getPlayerList().getOnlinePlayerNames()) : new ArrayList<>();
	}
}