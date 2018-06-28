package com.zeitheron.hammercore.utils.structure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableManager;

public class StructureAPI
{
	public static Structure Export(World world, BlockPos start, BlockPos end, boolean includeAir)
	{
		Structure s = new Structure();
		for(int x = Math.min(start.getX(), end.getX()); x <= Math.max(start.getX(), end.getX()); ++x)
			for(int y = Math.min(start.getY(), end.getY()); y <= Math.max(start.getY(), end.getY()); ++y)
				for(int z = Math.min(start.getZ(), end.getZ()); z <= Math.max(start.getZ(), end.getZ()); ++z)
				{
					BlockPos pos = new BlockPos(x, y, z);
					if(world.isAirBlock(pos) && !includeAir)
						continue;
					BlockPos absPos = new BlockPos(x - Math.min(start.getX(), end.getX()), y - Math.min(start.getY(), end.getY()), z - Math.min(start.getZ(), end.getZ()));
					s.placeStateAt(absPos, world.getBlockState(pos));
					
					if(world.getTileEntity(pos) != null)
					{
						NBTTagCompound nbt = world.getTileEntity(pos).serializeNBT();
						nbt.removeTag("x");
						nbt.removeTag("y");
						nbt.removeTag("z");
						s.placeTileNBTAt(absPos, nbt);
					}
				}
		return s;
	}
	
	public static void Import(World world, BlockPos startPos, Structure structure)
	{
		structure.build(world, startPos);
	}
	
	public static boolean IsValid(World world, BlockPos centerPos, Structure structure)
	{
		for(Long pl : structure.stateMap.keySet())
		{
			BlockPos tpos = BlockPos.fromLong(pl);
			if(!world.isBlockLoaded(tpos))
				world.getChunkFromBlockCoords(tpos);
			IBlockState src = structure.getStateAt(tpos);
			IBlockState state = world.getBlockState(tpos.add(centerPos));
			
			if(state.getBlock() == src.getBlock() && state.getBlock().getMetaFromState(src) == state.getBlock().getMetaFromState(state))
				;
			else
				return false;
		}
		
		for(Long pl : structure.tileMap.keySet())
		{
			BlockPos tpos = BlockPos.fromLong(pl);
			if(!world.isBlockLoaded(tpos))
				world.getChunkFromBlockCoords(tpos);
			TileEntity te = world.getTileEntity(tpos.add(centerPos));
			
			NBTTagCompound src = structure.getTileNBTAt(tpos);
			NBTTagCompound nbt = te.serializeNBT();
			
			NBTTagCompound merged = nbt.copy();
			merged.merge(src);
			
			if(!nbt.equals(merged))
				return false;
		}
		
		return true;
	}
	
	public static void Save(Structure s, OutputStream os) throws IOException
	{
		CompressedStreamTools.writeCompressed(s.serialize(), os);
	}
	
	public static Structure Parse(Scanner in) throws IOException
	{
		Structure struct = new Structure();
		
		in.nextLine();
		
		int line = 0;
		while(in.hasNextLine())
		{
			++line;
			String ln = in.nextLine().replaceAll("\r", "");
			if(ln.startsWith("#") || ln.trim().isEmpty())
				continue;
			
			try
			{
				if(ln.startsWith("{block}.{"))
				{
					ln = ln.substring(9);
					String pos[] = ln.substring(0, ln.indexOf("}")).split(",");
					if(pos.length != 3)
						throw new IOException("Failed to parse position at line #" + line + "!");
					
					int[] xs = From(pos[0]);
					int[] ys = From(pos[1]);
					int[] zs = From(pos[2]);
					
					String state = ln.substring(ln.indexOf("}.") + 2);
					int meta = Integer.parseInt(state.substring(0, state.indexOf('.')));
					Block block = Block.REGISTRY.getObject(new ResourceLocation(state.substring(state.indexOf('.') + 1)));
					
					for(int x : xs)
						for(int y : ys)
							for(int z : zs)
								struct.placeStateAt(new BlockPos(x, y, z), block.getStateFromMeta(meta));
				} else if(ln.startsWith("{tile}.{"))
				{
					ln = ln.substring(8);
					String pos[] = ln.substring(0, ln.indexOf("}")).split(",");
					if(pos.length != 3)
						throw new IOException("Failed to parse position at line #" + line + "!");
					
					int[] xs = From(pos[0]);
					int[] ys = From(pos[1]);
					int[] zs = From(pos[2]);
					
					String nbtStr = ln.substring(ln.indexOf("}.") + 2);
					NBTTagCompound nbt = JsonToNBT.getTagFromJson(nbtStr);
					
					for(int x : xs)
						for(int y : ys)
							for(int z : zs)
								struct.placeTileNBTAt(new BlockPos(x, y, z), nbt);
				} else
					throw new IOException("Unable to read modifier! [" + ln + "]");
			} catch(Throwable err)
			{
				throw new IOException("Failed to parse line #" + line + ": " + err.getMessage(), err);
			}
		}
		
		return struct;
	}
	
	private static final Set<String> allStructures = new HashSet<>();
	
	/** Note -- this method doesn't register this structure to worldgen! */
	public static ResourceLocation registerSpawnableStructure(ResourceLocation resource)
	{
		allStructures.add(resource.toString());
		return resource;
	}
	
	public static Structure ParseBuiltin(ResourceLocation resource) throws IOException
	{
		URL url = LootTableManager.class.getResource("/assets/" + resource.getResourceDomain() + "/structures/" + resource.getResourcePath() + ".hcstr");
		if(url != null)
		{
			Scanner s = new Scanner(url.openStream());
			Structure struct = Parse(s);
			s.close();
			
			return struct;
		}
		return new Structure(); // empty structure, no blocks/tiles
		                        // placed/validated.
	}
	
	public static Set<String> GetAllStructures()
	{
		return allStructures;
	}
	
	private static int[] From(String str)
	{
		if(!str.contains(" to "))
			return new int[] { Integer.parseInt(str) };
		String[] ab = str.split(" to ");
		int a = Integer.parseInt(ab[0]);
		int b = Integer.parseInt(ab[1]);
		if(a > b)
			return new int[0]; // check if min number is bigger than max
		int[] abs = new int[b - a + 1];
		for(int i = 0; i < abs.length; ++i)
			abs[i] = a + i;
		return abs;
	}
	
	public static Structure Load(InputStream in) throws IOException
	{
		Structure s = new Structure();
		s.deserialize(CompressedStreamTools.readCompressed(in));
		return s;
	}
}