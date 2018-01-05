package com.pengu.hammercore.energy;

public interface iPowerStorage
{
	public int receiveEnergy(int maxReceive, boolean simulate);
	
	public int extractEnergy(int maxExtract, boolean simulate);
	
	public int getEnergyStored();
	
	public int getMaxEnergyStored();
}