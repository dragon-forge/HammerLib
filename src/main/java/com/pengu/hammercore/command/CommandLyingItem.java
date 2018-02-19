package com.pengu.hammercore.command;

import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.common.blocks.BlockLyingItem;
import com.pengu.hammercore.common.utils.Chars;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandLyingItem extends CommandBase
{
	@Override
	public String getName()
	{
		return "hc_lying_item";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		BlockPos blockpos = parseBlockPos(sender, args, 0, false);
		ItemStack stack = parseStack(sender, args, 3);
		if(!stack.isEmpty() && BlockLyingItem.place(sender.getEntityWorld(), blockpos, stack) != null)
			sender.sendMessage(new TextComponentString("Lying item successfully placed!"));
		else
			throw new CommandException("Unable to place item in world!");
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
	{
		List<String> c = new ArrayList<>();
		if(args.length < 4)
			c.addAll(getTabCompletionCoordinate(args, 0, targetPos));
		if(args.length == 4)
			c.addAll(getListOfStringsMatchingLastWord(args, Item.REGISTRY.getKeys()));
		return c;
	}
	
	public static ItemStack parseStack(ICommandSender sender, String[] args, int startIndex) throws CommandException
	{
		Item item = getItemByText(sender, args[startIndex]);
		int i = args.length >= 2 + startIndex ? parseInt(args[startIndex + 1], 1, item.getItemStackLimit()) : 1;
		int j = args.length >= 3 + startIndex ? parseInt(args[startIndex + 2]) : 0;
		ItemStack itemstack = new ItemStack(item, i, j);
		if(args.length >= 4 + startIndex)
		{
			String s = buildString(args, startIndex + 3);
			
			try
			{
				itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
			} catch(NBTException nbtexception)
			{
				throw new CommandException("commands.give.tagError", nbtexception.getMessage());
			}
		}
		return itemstack;
	}
}