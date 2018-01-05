package com.pengu.hammercore.common.capabilities;

import java.util.concurrent.Callable;

import com.pengu.hammercore.energy.PowerStorage;
import com.pengu.hammercore.energy.iPowerStorage;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityEJ
{
	@CapabilityInject(iPowerStorage.class)
	public static Capability<iPowerStorage> ENERGY = null;
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(iPowerStorage.class, new IStorage<iPowerStorage>()
		{
			@Override
			public NBTBase writeNBT(Capability<iPowerStorage> capability, iPowerStorage instance, EnumFacing side)
			{
				return new NBTTagInt(instance.getEnergyStored());
			}
			
			@Override
			public void readNBT(Capability<iPowerStorage> capability, iPowerStorage instance, EnumFacing side, NBTBase nbt)
			{
				if(!(instance instanceof PowerStorage))
					throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
				((PowerStorage) instance).energy = ((NBTTagInt) nbt).getInt();
			}
		}, new Callable<iPowerStorage>()
		{
			@Override
			public iPowerStorage call() throws Exception
			{
				return new PowerStorage(1000);
			}
		});
	}
}