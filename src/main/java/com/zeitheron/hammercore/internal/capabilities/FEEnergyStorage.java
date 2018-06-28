package com.zeitheron.hammercore.internal.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

public class FEEnergyStorage extends EnergyStorage
{
	public FEEnergyStorage(int capacity, int maxTransfer)
	{
		super(capacity, maxTransfer);
	}
	
	public FEEnergyStorage(int capacity)
	{
		super(capacity);
	}
	
	public FEEnergyStorage(int capacity, int maxReceive, int maxExtract)
	{
		super(capacity, maxReceive, maxExtract);
	}
	
	public int getMaxExtract()
	{
		return maxExtract;
	}
	
	public int getMaxReceive()
	{
		return maxReceive;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(energy < 0)
			energy = 0;
		nbt.setInteger("Energy", energy);
		return nbt;
	}
	
	public FEEnergyStorage readFromNBT(NBTTagCompound nbt)
	{
		energy = nbt.getInteger("Energy");
		if(energy > capacity)
			energy = capacity;
		return this;
	}
}