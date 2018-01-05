package com.pengu.hammercore.energy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemPowerContainer extends Item implements iPowerContainerItem
{
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;
	
	public ItemPowerContainer()
	{
	}
	
	public ItemPowerContainer(int capacity)
	{
		this(capacity, capacity, capacity);
	}
	
	public ItemPowerContainer(int capacity, int maxTransfer)
	{
		this(capacity, maxTransfer, maxTransfer);
	}
	
	public ItemPowerContainer(int capacity, int maxReceive, int maxExtract)
	{
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}
	
	public ItemPowerContainer setCapacity(int capacity)
	{
		this.capacity = capacity;
		return this;
	}
	
	public ItemPowerContainer setMaxTransfer(int maxTransfer)
	{
		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
		return this;
	}
	
	public ItemPowerContainer setMaxReceive(int maxReceive)
	{
		this.maxReceive = maxReceive;
		return this;
	}
	
	public ItemPowerContainer setMaxExtract(int maxExtract)
	{
		this.maxExtract = maxExtract;
		return this;
	}
	
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate)
	{
		if(!container.hasTagCompound())
			container.setTagCompound(new NBTTagCompound());
		int energy = container.getTagCompound().getInteger("Energy");
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
		if(!simulate)
		{
			energy += energyReceived;
			container.getTagCompound().setInteger("Energy", energy);
		}
		return energyReceived;
	}
	
	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
	{
		if(container.getTagCompound() == null || !container.getTagCompound().hasKey("Energy"))
			return 0;
		int energy = container.getTagCompound().getInteger("Energy");
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
		if(!simulate)
		{
			energy -= energyExtracted;
			container.getTagCompound().setInteger("Energy", energy);
		}
		return energyExtracted;
	}
	
	@Override
	public int getEnergyStored(ItemStack container)
	{
		if(container.getTagCompound() == null || !container.getTagCompound().hasKey("Energy"))
			return 0;
		return container.getTagCompound().getInteger("Energy");
	}
	
	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return capacity;
	}
}