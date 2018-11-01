package com.zeitheron.hammercore.internal.capabilities;

import com.zeitheron.hammercore.utils.math.MathHelper;

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
	
	public void setEnergyStored(int fe)
	{
		this.energy = fe;
		stabilize();
	}
	
	public void setCapacity(int fe)
	{
		this.capacity = fe;
		stabilize();
	}
	
	public void stabilize()
	{
		this.energy = MathHelper.clip(this.energy, 0, this.capacity);
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