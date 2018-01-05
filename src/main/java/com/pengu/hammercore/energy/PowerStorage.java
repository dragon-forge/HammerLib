package com.pengu.hammercore.energy;

import net.minecraft.nbt.NBTTagCompound;

public class PowerStorage implements iPowerStorage
{
	public int energy, capacity, maxReceive, maxExtract;
	
	public PowerStorage(int capacity)
	{
		this(capacity, capacity, capacity);
	}
	
	public PowerStorage(int capacity, int maxTransfer)
	{
		this(capacity, maxTransfer, maxTransfer);
	}
	
	public PowerStorage(int capacity, int maxReceive, int maxExtract)
	{
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}
	
	public PowerStorage readFromNBT(NBTTagCompound nbt)
	{
		energy = Math.min(nbt.getInteger("Energy"), capacity);
		return this;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Energy", Math.max(energy, 0));
		return nbt;
	}
	
	public PowerStorage setCapacity(int capacity)
	{
		this.capacity = capacity;
		energy = Math.min(energy, capacity);
		return this;
	}
	
	public PowerStorage setMaxTransfer(int maxTransfer)
	{
		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
		return this;
	}
	
	public PowerStorage setMaxReceive(int maxReceive)
	{
		this.maxReceive = maxReceive;
		return this;
	}
	
	public PowerStorage setMaxExtract(int maxExtract)
	{
		this.maxExtract = maxExtract;
		return this;
	}
	
	public int getMaxReceive()
	{
		return maxReceive;
	}
	
	public int getMaxExtract()
	{
		return maxExtract;
	}
	
	public void setEnergyStored(int energy)
	{
		this.energy = Math.max(Math.min(energy, this.capacity), 0);
	}
	
	public void modifyEnergyStored(int energy)
	{
		this.energy = Math.max(Math.min(this.energy + energy, this.capacity), 0);
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
		if(!simulate)
			energy += energyReceived;
		return energyReceived;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
		if(!simulate)
			energy -= energyExtracted;
		return energyExtracted;
	}
	
	@Override
	public int getEnergyStored()
	{
		return energy;
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return capacity;
	}
}