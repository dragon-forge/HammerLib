package com.zeitheron.hammercore.utils.energy;

import net.minecraft.item.ItemStack;

public interface IPowerContainerItem
{
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate);
	
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate);
	
	public int getEnergyStored(ItemStack container);
	
	public int getMaxEnergyStored(ItemStack container);
}