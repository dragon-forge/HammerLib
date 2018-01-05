package com.pengu.hammercore.world.data;

import java.util.HashMap;
import java.util.Map;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.utils.NPEUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MCFBus
public class PerChunkDataManager
{
	private static PerChunkDataManager INSTANCE = null;
	private Map<String, NBTTagCompound> datas = new HashMap<>();
	
	{
		if(INSTANCE != null)
			NPEUtils.noInstancesError();
		INSTANCE = this;
	}
	
	public static NBTTagCompound getData(Chunk chunk, String id)
	{
		String b = build(chunk);
		NBTTagCompound st = INSTANCE.datas.get(b);
		if(st == null)
			INSTANCE.datas.put(b, st = new NBTTagCompound());
		if(!st.hasKey(id))
			st.setTag(id, new NBTTagCompound());
		return st.getCompoundTag(id);
	}
	
	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load e)
	{
		datas.put(build(e.getChunk()), e.getData().getCompoundTag("HammerCoreData"));
	}
	
	@SubscribeEvent
	public void chunkSave(ChunkDataEvent.Save e)
	{
		NBTTagCompound n = datas.get(build(e.getChunk()));
		if(n != null)
			e.getData().setTag("HammerCoreData", n);
	}
	
	public static String build(Chunk c)
	{
		return c.getWorld().provider.getDimension() + ":" + c.x + "," + c.z;
	}
	
	public static void cleanup()
	{
		/** In hope that all data is saved :D */
		INSTANCE.datas.clear();
	}
}