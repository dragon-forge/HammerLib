package com.zeitheron.hammercore.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandSetEnchantmentColor extends CommandBase
{
	@Override
	public String getName()
	{
		return "hc_setenchcol";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Use /hc_setenchcol <hex color> to apply custom color to held item";
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(sender instanceof EntityPlayer)
		{
			EntityPlayer pl = (EntityPlayer) sender;
			pl.sendMessage(new TextComponentString("Color changed."));
			ItemStack stack = pl.getHeldItemMainhand();
			stack.setTagCompound(stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound());
			stack.getTagCompound().setString("HCCustomEnch", args[0]);
		}
	}
}