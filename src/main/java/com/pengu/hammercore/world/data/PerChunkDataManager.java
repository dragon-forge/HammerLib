package com.pengu.hammercore.world.data;

import com.pengu.hammercore.utils.OnetimeCaller;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class PerChunkDataManager
{
	@CapabilityInject(ChunkHCData.class)
	public static Capability<ChunkHCData> dataCap;
	
	public static final OnetimeCaller register = new OnetimeCaller(PerChunkDataManager::register);
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(ChunkHCData.class, new IStorage<ChunkHCData>()
		{
			@Override
			public NBTBase writeNBT(Capability<ChunkHCData> capability, ChunkHCData instance, EnumFacing side)
			{
				return instance.serializeNBT();
			}
			
			@Override
			public void readNBT(Capability<ChunkHCData> capability, ChunkHCData instance, EnumFacing side, NBTBase nbt)
			{
				instance.nbt = (NBTTagCompound) nbt;
			}
		}, () -> new ChunkHCData());
	}
	
	public static NBTTagCompound getData(Chunk chunk, String id)
	{
		return chunk.getCapability(dataCap, null).nbt;
	}
	
	@SubscribeEvent
	public void attachCaps(AttachCapabilitiesEvent<Chunk> e)
	{
		e.addCapability(new ResourceLocation("hammercore", "data"), new ICapabilityProvider()
		{
			public final ChunkHCData data = new ChunkHCData();
			
			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing)
			{
				return capability == dataCap;
			}
			
			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing)
			{
				return capability == dataCap ? (T) data : null;
			}
		});
	}
	
	public static String build(Chunk c)
	{
		return c.getWorld().provider.getDimension() + ":" + c.x + "," + c.z;
	}
}