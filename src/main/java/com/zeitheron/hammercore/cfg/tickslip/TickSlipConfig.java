package com.zeitheron.hammercore.cfg.tickslip;

import com.zeitheron.hammercore.lib.zlib.json.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TickSlipConfig
{
	public static Object2IntMap<ResourceLocation> tileTicks = new Object2IntOpenHashMap<>();

	public static File ioFile;

	public static void reload(File file)
	{
		if(ioFile != null) file = ioFile;
		else ioFile = file;

		tileTicks.clear();

		if(!file.isFile())
		{
			file.mkdirs();
			file.delete();

			try(FileOutputStream fos = new FileOutputStream(file))
			{
				JSONObject body = new JSONObject();
				body.put("modid:block_id", 5);
				fos.write(body.toString(2).getBytes(StandardCharsets.UTF_8));
			} catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
		}

		if(file.isFile()) try(FileInputStream fis = new FileInputStream(file))
		{
			new JSONTokener(fis).nextValueOBJ().ifPresent(body -> body.keyStream().forEach(key ->
			{
				int val = body.getInt(key);
				tileTicks.put(new ResourceLocation(key), val);
			}));
		} catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	public static int getTickRate(Block block)
	{
		return Math.max(1, tileTicks.getInt(block.getRegistryName()));
	}
}