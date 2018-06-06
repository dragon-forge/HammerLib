package com.zeitheron.hammercore.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;

public class CommandBanV6 extends CommandBase
{
	public static final CommandBanV6 instance = new CommandBanV6();
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void playerJoin(ServerConnectionFromClientEvent e)
	{
		// e.getHandler().onDisconnect(new TextComponentString(""));
	}
	
	@Override
	public String getName()
	{
		return "hc_banmachine";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Bans a user by hardware.";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		
	}
}