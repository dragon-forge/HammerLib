package com.zeitheron.hammercore.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;

public class FluidEnergyAccessPoint
{
	World world;
	BlockPos pos;
	
	public FluidEnergyAccessPoint(World world, BlockPos pos)
	{
		this.world = world;
		this.pos = pos;
	}
	
	public static FluidEnergyAccessPoint create(World world, BlockPos pos)
	{
		return new FluidEnergyAccessPoint(world, pos);
	}
	
	public List<IEnergyStorage> findFEAcceptors()
	{
		List<IEnergyStorage> list = new ArrayList<>();
		for(EnumFacing face : EnumFacing.VALUES)
		{
			BlockPos apos = pos.offset(face);
			TileEntity tile = world.getTileEntity(apos);
			if(tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite()))
				list.add(tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()));
		}
		list.removeIf(es -> es == null || !es.canReceive());
		return list;
	}
	
	public List<IFluidHandler> findFLAcceptors(FluidStack fluid)
	{
		List<IFluidHandler> list = new ArrayList<>();
		for(EnumFacing face : EnumFacing.VALUES)
		{
			BlockPos apos = pos.offset(face);
			IFluidHandler ifh = FluidUtil.getFluidHandler(world, apos, face.getOpposite());
			if(ifh instanceof FluidBlockWrapper || ifh instanceof BlockLiquidWrapper)
				continue;
			if(ifh != null && FluidHelper.canAccept(ifh, fluid))
				list.add(ifh);
		}
		return list;
	}
	
	public int emitFluid(FluidStack fluid)
	{
		if(fluid == null)
			return 0;
		
		List<IFluidHandler> list = findFLAcceptors(fluid);
		
		if(list.isEmpty())
			return 0;
		
		int per = fluid.amount / list.size();
		
		int quant = 0;
		
		for(int i = 0; i < list.size(); ++i)
		{
			IFluidHandler ies = list.get(i);
			int accepted = ies.fill(fluid.copy(), true);
			fluid.amount -= accepted;
			if(accepted < per && i != list.size() - 1)
				per = fluid.amount / (list.size() - i - 1);
			quant += accepted;
		}
		
		return quant;
	}
	
	public int emitEnergy(int FE)
	{
		List<IEnergyStorage> list = findFEAcceptors();
		
		if(list.isEmpty())
			return 0;
		
		int ffe = FE;
		int per = FE / list.size();
		
		int quant = 0;
		
		for(int i = 0; i < list.size(); ++i)
		{
			IEnergyStorage ies = list.get(i);
			int accepted = ies.receiveEnergy(per, false);
			FE -= accepted;
			if(accepted < per && i != list.size() - 1)
				per = FE / (list.size() - i - 1);
			quant += accepted;
		}
		
		return quant;
	}
}