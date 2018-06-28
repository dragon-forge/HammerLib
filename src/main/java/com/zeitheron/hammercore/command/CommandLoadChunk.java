package com.zeitheron.hammercore.command;

import java.util.Arrays;
import java.util.List;

import com.zeitheron.hammercore.internal.chunk.ChunkLoaderHC;
import com.zeitheron.hammercore.internal.chunk.ChunkPredicate.LoadableChunk;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandLoadChunk extends CommandBase
{
	@Override
	public String getName()
	{
		return "hc_loadchunk";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Use /hc_loadchunk <dim:int> <chunk x:int> <chunk z:int> <time:string(or -1t for unlimited time)>";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		LoadableChunk chunk = new LoadableChunk(parseInt(args[0]), parseInt(args[1]), parseInt(args[2]));
		
		long time = CommandTimeToTicks.formatTimeToTicksWithCommandException(args[3]);
		
		ChunkLoaderHC.INSTANCE.registerChunkWithTimeout(chunk, time);
		
		if(time != -1L)
			sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Added force-chunk-loaded Chunk: x: " + chunk.x + ", z: " + chunk.z + ", dimension: " + chunk.dim + " for " + CommandTimeToTicks.fancyFormat(time) + " ticks (or until next server restart)."));
		else
			sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Added force-chunk-loaded Chunk: x: " + chunk.x + ", z: " + chunk.z + ", dimension: " + chunk.dim + " until next server restart."));
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
	{
		targetPos = sender.getPosition();
		return args.length == 1 ? Arrays.asList(sender.getEntityWorld().provider.getDimension() + "") : args.length == 2 ? Arrays.asList((targetPos.getX() >> 4) + "") : args.length == 3 ? Arrays.asList((targetPos.getZ() >> 4) + "") : Arrays.asList();
	}
}