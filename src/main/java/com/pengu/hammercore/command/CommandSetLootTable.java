package com.pengu.hammercore.command;

import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.common.utils.WorldUtil;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.loot.LootTableList;

public class CommandSetLootTable extends CommandBase
{
	@Override
	public String getName()
	{
		return "hc_setloottable";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Applies a loot table to inventory";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		int x = parseInt(args[0]);
		int y = parseInt(args[1]);
		int z = parseInt(args[2]);
		ResourceLocation table = new ResourceLocation(args[3]);
		
		TileEntityLockableLoot loot = WorldUtil.cast(sender.getEntityWorld().getTileEntity(new BlockPos(x, y, z)), TileEntityLockableLoot.class);
		
		if(loot != null)
		{
			sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Loot table applied!"));
			loot.clear();
			loot.setLootTable(table, 0);
		} else
			sender.sendMessage(new TextComponentString(TextFormatting.RED + "Tile Entity at " + x + "," + y + "," + z + " is not a lootable tile!"));
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
	{
		List<String> tabs = new ArrayList<>();
		tabs.addAll(getTabCompletionCoordinate(args, 0, targetPos));
		if(args.length == 4)
			tabs.addAll(getListOfStringsMatchingLastWord(args, LootTableList.getAll()));
		return tabs;
	}
}