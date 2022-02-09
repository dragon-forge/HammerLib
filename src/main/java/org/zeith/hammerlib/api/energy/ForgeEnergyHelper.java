package org.zeith.hammerlib.api.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.concurrent.atomic.AtomicInteger;

public class ForgeEnergyHelper
{
	public static int suckPower(BlockEntity into, int lim, boolean simulate)
	{
		AtomicInteger sucked = new AtomicInteger();
		if(into != null)
			for(Direction dir : Direction.values())
				into.getCapability(CapabilityEnergy.ENERGY, dir).filter(IEnergyStorage::canReceive).ifPresent(acceptor ->
				{
					BlockEntity remote = into.getLevel().getBlockEntity(into.getBlockPos().relative(dir));
					remote.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canExtract).ifPresent(extractor ->
					{
						int e = Math.min(extractor.getEnergyStored(), lim);
						e = acceptor.receiveEnergy(e, true);
						sucked.addAndGet(acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate));
					});
				});
		return sucked.get();
	}

	public static void suckPowerNoTrack(BlockEntity into, int lim, boolean simulate)
	{
		if(into != null)
			for(Direction dir : Direction.values())
				into.getCapability(CapabilityEnergy.ENERGY, dir).filter(IEnergyStorage::canReceive).ifPresent(acceptor ->
				{
					BlockEntity remote = into.getLevel().getBlockEntity(into.getBlockPos().relative(dir));
					remote.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canExtract).ifPresent(extractor ->
					{
						int e = Math.min(extractor.getEnergyStored(), lim);
						e = acceptor.receiveEnergy(e, true);
						acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate);
					});
				});
	}

	public static int spreadPower(BlockEntity into, int lim, boolean simulate)
	{
		AtomicInteger sucked = new AtomicInteger();
		if(into != null)
			for(Direction dir : Direction.values())
				into.getCapability(CapabilityEnergy.ENERGY, dir).filter(IEnergyStorage::canExtract).ifPresent(extractor ->
				{
					BlockEntity remote = into.getLevel().getBlockEntity(into.getBlockPos().relative(dir));
					remote.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canReceive).ifPresent(acceptor ->
					{
						int e = Math.min(extractor.getEnergyStored(), lim);
						e = acceptor.receiveEnergy(e, true);
						sucked.addAndGet(acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate));
					});
				});
		return sucked.get();
	}

	public static void spreadPowerNoTrack(BlockEntity into, int lim, boolean simulate)
	{
		if(into != null)
			for(Direction dir : Direction.values())
				into.getCapability(CapabilityEnergy.ENERGY, dir).filter(IEnergyStorage::canExtract).ifPresent(extractor ->
				{
					BlockEntity remote = into.getLevel().getBlockEntity(into.getBlockPos().relative(dir));
					remote.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canReceive).ifPresent(acceptor ->
					{
						int e = Math.min(extractor.getEnergyStored(), lim);
						e = acceptor.receiveEnergy(e, true);
						acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate);
					});
				});
	}
}