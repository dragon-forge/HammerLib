package com.zeitheron.hammercore.utils.energy;

import com.zeitheron.hammercore.utils.math.BigMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.energy.IEnergyStorage;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Energy storage that handles energy with Big Integers
 */
public class BigEnergyStorage
		implements IEnergyStorage
{
	protected BigInteger energy;
	protected BigInteger capacity;
	protected BigInteger maxReceive;
	protected BigInteger maxExtract;

	public BigEnergyStorage(BigInteger capacity)
	{
		this(capacity, capacity, capacity, BigInteger.ZERO);
	}

	public BigEnergyStorage(BigInteger capacity, BigInteger maxTransfer)
	{
		this(capacity, maxTransfer, maxTransfer, BigInteger.ZERO);
	}

	public BigEnergyStorage(BigInteger capacity, BigInteger maxReceive, BigInteger maxExtract)
	{
		this(capacity, maxReceive, maxExtract, BigInteger.ZERO);
	}

	public BigEnergyStorage(BigInteger capacity, BigInteger maxReceive, BigInteger maxExtract, BigInteger energy)
	{
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		this.energy = BigMath.max(BigInteger.ZERO, BigMath.min(capacity, energy));
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		if(!canReceive())
			return 0;
		BigInteger energyReceived = BigMath.min(capacity.subtract(energy), BigMath.min(this.maxReceive, new BigInteger("" + maxReceive)));
		if(!simulate)
			energy = energy.add(energyReceived);
		return energyReceived.intValue();
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		if(!canExtract())
			return 0;
		BigInteger energyExtracted = BigMath.min(energy, BigMath.min(this.maxExtract, new BigInteger(maxExtract + "")));
		if(!simulate)
			energy = energy.subtract(energyExtracted);
		return energyExtracted.intValue();
	}

	@Override
	public int getEnergyStored()
	{
		return BigMath.trimToInt(energy);
	}

	@Override
	public int getMaxEnergyStored()
	{
		return BigMath.trimToInt(capacity);
	}

	public BigInteger getCapacity()
	{
		return capacity;
	}

	public void setEnergy(BigInteger energy)
	{
		this.energy = energy;
	}

	public BigInteger getEnergy()
	{
		return energy;
	}

	@Override
	public boolean canExtract()
	{
		return BigMath.isAGreaterThenB(maxExtract, BigInteger.ZERO, true);
	}

	@Override
	public boolean canReceive()
	{
		return BigMath.isAGreaterThenB(maxReceive, BigInteger.ZERO, true);
	}

	public BigEnergyStorage setCapacity(BigInteger capacity)
	{
		this.capacity = capacity;
		this.energy = BigMath.min(this.energy, this.capacity);
		return this;
	}

	public BigEnergyStorage setMaxReceive(BigInteger receive)
	{
		this.maxReceive = receive;
		return this;
	}

	public BigEnergyStorage setMaxExtract(BigInteger extract)
	{
		this.maxExtract = extract;
		return this;
	}

	public boolean hasEnergy(Number fe)
	{
		if(fe instanceof BigDecimal) fe = ((BigDecimal) fe).toBigInteger();
		return BigMath.isAGreaterThenB(energy, fe instanceof BigInteger ? (BigInteger) fe : BigInteger.valueOf(fe.longValue()), false);
	}

	public double getFilledProgress()
	{
		return energy.multiply(BigInteger.valueOf(1_000_000L)).divide(capacity).doubleValue() / 1_000_000D;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Energy", energy.toString(16));
		return nbt;
	}

	public BigEnergyStorage readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("Energy", NBT.TAG_STRING))
			energy = new BigInteger(nbt.getString("Energy"), 16);
		return this;
	}
}