package org.zeith.hammerlib.api.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.concurrent.atomic.AtomicInteger;

public class ForgeEnergyHelper
{
	public static int suckPower(BlockEntity into, int lim, boolean simulate)
	{
		AtomicInteger sucked = new AtomicInteger();
		if(into != null && into.hasLevel())
			for(Direction dir : Direction.values())
				into.getCapability(ForgeCapabilities.ENERGY, dir).filter(IEnergyStorage::canReceive).ifPresent(acceptor ->
				{
					BlockEntity remote = into.getLevel().getBlockEntity(into.getBlockPos().relative(dir));
					if(remote != null)
						remote.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canExtract).ifPresent(extractor ->
						{
							int e = Math.min(extractor.getEnergyStored(), lim - sucked.get());
							if(e <= 0) return;
							e = acceptor.receiveEnergy(e, true);
							sucked.addAndGet(acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate));
						});
				});
		return sucked.get();
	}
	
	public static void suckPowerNoTrack(BlockEntity into, int lim, boolean simulate)
	{
		if(into != null && into.hasLevel())
			for(Direction dir : Direction.values())
				into.getCapability(ForgeCapabilities.ENERGY, dir).filter(IEnergyStorage::canReceive).ifPresent(acceptor ->
				{
					BlockEntity remote = into.getLevel().getBlockEntity(into.getBlockPos().relative(dir));
					if(remote != null)
						remote.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canExtract).ifPresent(extractor ->
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
		if(into != null && into.hasLevel())
			for(Direction dir : Direction.values())
				into.getCapability(ForgeCapabilities.ENERGY, dir).filter(IEnergyStorage::canExtract).ifPresent(extractor ->
				{
					BlockEntity remote = into.getLevel().getBlockEntity(into.getBlockPos().relative(dir));
					if(remote != null)
						remote.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canReceive).ifPresent(acceptor ->
						{
							int e = Math.min(extractor.getEnergyStored(), lim - sucked.get());
							if(e <= 0) return;
							e = acceptor.receiveEnergy(e, true);
							sucked.addAndGet(acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate));
						});
				});
		return sucked.get();
	}
	
	public static void spreadPowerNoTrack(BlockEntity into, int lim, boolean simulate)
	{
		if(into != null && into.hasLevel())
			for(Direction dir : Direction.values())
				into.getCapability(ForgeCapabilities.ENERGY, dir).filter(IEnergyStorage::canExtract).ifPresent(extractor ->
				{
					BlockEntity remote = into.getLevel().getBlockEntity(into.getBlockPos().relative(dir));
					if(remote != null)
						remote.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canReceive).ifPresent(acceptor ->
						{
							int e = Math.min(extractor.getEnergyStored(), lim);
							e = acceptor.receiveEnergy(e, true);
							acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate);
						});
				});
	}
}