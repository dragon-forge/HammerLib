package org.zeith.hammerlib.api.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.concurrent.atomic.AtomicInteger;

public class ForgeEnergyHelper
{
	public static int suckPower(TileEntity into, int lim, boolean simulate)
	{
		AtomicInteger sucked = new AtomicInteger();
		if(into != null)
			for(Direction dir : Direction.values())
				into.getCapability(CapabilityEnergy.ENERGY, dir).filter(IEnergyStorage::canReceive).ifPresent(acceptor ->
				{
					TileEntity remote = into.getLevel().getBlockEntity(into.getBlockPos().relative(dir));
					remote.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canExtract).ifPresent(extractor ->
					{
						int e = Math.min(extractor.getEnergyStored(), lim);
						e = acceptor.receiveEnergy(e, true);
						sucked.addAndGet(acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate));
					});
				});
		return sucked.get();
	}

	public static void suckPowerNoTrack(TileEntity into, int lim, boolean simulate)
	{
		if(into != null)
			for(Direction dir : Direction.values())
				into.getCapability(CapabilityEnergy.ENERGY, dir).filter(IEnergyStorage::canReceive).ifPresent(acceptor ->
				{
					TileEntity remote = into.getLevel().getBlockEntity(into.getBlockPos().relative(dir));
					remote.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canExtract).ifPresent(extractor ->
					{
						int e = Math.min(extractor.getEnergyStored(), lim);
						e = acceptor.receiveEnergy(e, true);
						acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate);
					});
				});
	}

	public static int spreadPower(TileEntity into, int lim, boolean simulate)
	{
		AtomicInteger sucked = new AtomicInteger();
		if(into != null)
			for(Direction dir : Direction.values())
				into.getCapability(CapabilityEnergy.ENERGY, dir).filter(IEnergyStorage::canExtract).ifPresent(extractor ->
				{
					TileEntity remote = into.getLevel().getBlockEntity(into.getBlockPos().relative(dir));
					remote.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canReceive).ifPresent(acceptor ->
					{
						int e = Math.min(extractor.getEnergyStored(), lim);
						e = acceptor.receiveEnergy(e, true);
						sucked.addAndGet(acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate));
					});
				});
		return sucked.get();
	}

	public static void spreadPowerNoTrack(TileEntity into, int lim, boolean simulate)
	{
		if(into != null)
			for(Direction dir : Direction.values())
				into.getCapability(CapabilityEnergy.ENERGY, dir).filter(IEnergyStorage::canExtract).ifPresent(extractor ->
				{
					TileEntity remote = into.getLevel().getBlockEntity(into.getBlockPos().relative(dir));
					remote.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canReceive).ifPresent(acceptor ->
					{
						int e = Math.min(extractor.getEnergyStored(), lim);
						e = acceptor.receiveEnergy(e, true);
						acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate);
					});
				});
	}
}