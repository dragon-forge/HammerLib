package com.zeitheron.hammercore.command;

import com.zeitheron.hammercore.cfg.tickslip.TickSlipConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandReloadTickRates
		extends CommandBase
{
	@Override
	public String getName()
	{
		return "hc_reload_tick_rates";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Used to reload tick rates configs in HammerCore";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 4;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		sender.sendMessage(new TextComponentString("Reloading server " + TickSlipConfig.ioFile.getName() + "...."));
		TickSlipConfig.reload(null);
		sender.sendMessage(new TextComponentString("Done!"));
	}
}