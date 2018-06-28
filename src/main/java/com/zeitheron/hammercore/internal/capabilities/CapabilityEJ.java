package com.zeitheron.hammercore.internal.capabilities;

import java.util.concurrent.Callable;

import com.zeitheron.hammercore.utils.energy.IPowerStorage;
import com.zeitheron.hammercore.utils.energy.PowerStorage;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityEJ
{
	@CapabilityInject(IPowerStorage.class)
	public static Capability<IPowerStorage> ENERGY = null;
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IPowerStorage.class, new IStorage<IPowerStorage>()
		{
			@Override
			public NBTBase writeNBT(Capability<IPowerStorage> capability, IPowerStorage instance, EnumFacing side)
			{
				return new NBTTagInt(instance.getEnergyStored());
			}
			
			@Override
			public void readNBT(Capability<IPowerStorage> capability, IPowerStorage instance, EnumFacing side, NBTBase nbt)
			{
				if(!(instance instanceof PowerStorage))
					throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
				((PowerStorage) instance).energy = ((NBTTagInt) nbt).getInt();
			}
		}, new Callable<IPowerStorage>()
		{
			@Override
			public IPowerStorage call() throws Exception
			{
				return new PowerStorage(1000);
			}
		});
	}
}