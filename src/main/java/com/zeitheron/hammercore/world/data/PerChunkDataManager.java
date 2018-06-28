package com.zeitheron.hammercore.world.data;

import com.zeitheron.hammercore.annotations.MCFBus;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MCFBus
public class PerChunkDataManager
{
	@CapabilityInject(IChunkData.class)
	public static Capability<IChunkData> CHUNK_DATA;
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IChunkData.class, new IStorage<IChunkData>()
		{
			@Override
			public NBTBase writeNBT(Capability<IChunkData> capability, IChunkData instance, EnumFacing side)
			{
				return instance.serializeNBT();
			}
			
			@Override
			public void readNBT(Capability<IChunkData> capability, IChunkData instance, EnumFacing side, NBTBase nbt)
			{
				instance.deserializeNBT((NBTTagCompound) instance);
			}
		}, () -> new ChunkData());
	}
	
	public static IChunkData getData(Chunk chunk)
	{
		return chunk.getCapability(CHUNK_DATA, null);
	}
	
	@SubscribeEvent
	public void attachCaps(AttachCapabilitiesEvent<Chunk> e)
	{
		e.addCapability(new ResourceLocation("hammercore", "data"), new ChunkDataProvider());
	}
	
	public static String build(Chunk c)
	{
		return c.getWorld().provider.getDimension() + ":" + c.x + "," + c.z;
	}
}