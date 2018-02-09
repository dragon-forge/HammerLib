package com.pengu.hammercore.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.common.structure.io.StructureElement;
import com.pengu.hammercore.common.structure.io.StructureOutputStream;
import com.pengu.hammercore.common.utils.IOUtils;
import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CommandExportStructure extends CommandBase
{
	@Override
	public String getName()
	{
		return "hc_export";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "/hc_export <x1> <y1> <z1> <x2> <y2> <z2> <name> [save air]";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		int x = parseInt(args[0]);
		int y = parseInt(args[1]);
		int z = parseInt(args[2]);
		
		int x2 = parseInt(args[3]);
		int y2 = parseInt(args[4]);
		int z2 = parseInt(args[5]);
		
		int mx = Math.min(x, x2);
		int my = Math.min(y, y2);
		int mz = Math.min(z, z2);
		
		int xx = Math.max(x, x2);
		int xy = Math.max(y, y2);
		int xz = Math.max(z, z2);
		
		String name = args[6];
		
		boolean bool = args.length == 8 ? parseBoolean(args[7]) : false;
		
		final World world = sender.getEntityWorld();
		
		sender.sendMessage(new TextComponentString("Saving tiles..."));
		new Thread(() ->
		{
			File f = new File("HammerCore", "structures");
			if(!f.isDirectory())
				f.mkdirs();
			File struct = IOUtils.pickFile(new File(f, name + ".hsf"));
			
			try(StructureOutputStream o = new StructureOutputStream(new FileOutputStream(struct)))
			{
				WorldLocation center = new WorldLocation(world, new BlockPos(mx, my, mz));
				int sa = 0;
				int tot = 0;
				for(int ox = mx; ox <= xx; ++ox)
					for(int oy = my; oy <= xy; ++oy)
						for(int oz = mz; oz <= xz; ++oz)
						{
							++tot;
							WorldLocation loc = new WorldLocation(world, new BlockPos(ox, oy, oz));
							if(!loc.isAir() || bool)
							{
								++sa;
								o.write(StructureElement.capture(loc, center));
							}
						}
				sender.sendMessage(new TextComponentString("Saved " + sa + " tiles." + (sa != tot ? " (" + (tot - sa) + " air tiles skipped)" : "")));
			} catch(IOException io)
			{
				io.printStackTrace();
			}
		}).start();
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
	{
		List<String> tabs = new ArrayList<>();
		if(args.length < 4)
			tabs.addAll(getTabCompletionCoordinate(args, 0, targetPos));
		if(args.length < 7)
			tabs.addAll(getTabCompletionCoordinate(args, 3, targetPos));
		if(args.length == 7)
			tabs.addAll(getListOfStringsMatchingLastWord(args, "NAME"));
		if(args.length == 8)
			tabs.addAll(getListOfStringsMatchingLastWord(args, "true", "false"));
		return tabs;
	}
}