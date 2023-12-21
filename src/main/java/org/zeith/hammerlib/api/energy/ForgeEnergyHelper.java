package org.zeith.hammerlib.api.energy;

import net.minecraft.core.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.concurrent.atomic.AtomicInteger;

public class ForgeEnergyHelper
{
	public static int suckPower(Level level, BlockPos into, int lim, boolean simulate)
	{
		AtomicInteger sucked = new AtomicInteger();
		
		for(Direction dir : Direction.values())
		{
			var acceptor = level.getCapability(Capabilities.EnergyStorage.BLOCK, into, dir);
			if(acceptor == null || !acceptor.canReceive()) continue;
			
			var extractor = level.getCapability(Capabilities.EnergyStorage.BLOCK, into.relative(dir), dir.getOpposite());
			if(extractor == null || !extractor.canExtract()) continue;
			
			int e = Math.min(extractor.getEnergyStored(), lim - sucked.get());
			if(e <= 0) continue;
			e = acceptor.receiveEnergy(e, true);
			
			sucked.addAndGet(acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate));
		}
		
		return sucked.get();
	}
	
	public static void suckPowerNoTrack(Level level, BlockPos into, int lim, boolean simulate)
	{
		for(Direction dir : Direction.values())
		{
			var acceptor = level.getCapability(Capabilities.EnergyStorage.BLOCK, into, dir);
			if(acceptor == null || !acceptor.canReceive()) continue;
			
			var extractor = level.getCapability(Capabilities.EnergyStorage.BLOCK, into.relative(dir), dir.getOpposite());
			if(extractor == null || !extractor.canExtract()) continue;
			
			int e = Math.min(extractor.getEnergyStored(), lim);
			if(e <= 0) continue;
			e = acceptor.receiveEnergy(e, true);
			
			acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate);
		}
	}
	
	public static int spreadPower(Level level, BlockPos from, int lim, boolean simulate)
	{
		AtomicInteger sucked = new AtomicInteger();
		for(Direction dir : Direction.values())
		{
			var extractor = level.getCapability(Capabilities.EnergyStorage.BLOCK, from, dir);
			if(extractor == null || !extractor.canExtract()) continue;
			
			var acceptor = level.getCapability(Capabilities.EnergyStorage.BLOCK, from.relative(dir), dir.getOpposite());
			if(acceptor == null || !acceptor.canReceive()) continue;
			
			int e = Math.min(extractor.getEnergyStored(), lim - sucked.get());
			if(e <= 0) continue;
			e = acceptor.receiveEnergy(e, true);
			
			sucked.addAndGet(acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate));
		}
		return sucked.get();
	}
	
	public static void spreadPowerNoTrack(Level level, BlockPos from, int lim, boolean simulate)
	{
		for(Direction dir : Direction.values())
		{
			var extractor = level.getCapability(Capabilities.EnergyStorage.BLOCK, from, dir);
			if(extractor == null || !extractor.canExtract()) continue;
			
			var acceptor = level.getCapability(Capabilities.EnergyStorage.BLOCK, from.relative(dir), dir.getOpposite());
			if(acceptor == null || !acceptor.canReceive()) continue;
			
			int e = Math.min(extractor.getEnergyStored(), lim);
			if(e <= 0) continue;
			e = acceptor.receiveEnergy(e, true);
			
			acceptor.receiveEnergy(extractor.extractEnergy(e, simulate), simulate);
		}
	}
}