package com.zeitheron.hammercore.utils.match.fluid;

import java.util.List;

import com.zeitheron.hammercore.fluiddict.FluidDictionary;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidContainer
{
	private final String fluid;
	private final int count;
	private final String nbt;
	private final String fluiddictNames;
	private final String mod;
	private final byte type;
	
	private FluidContainer(FluidStack stack)
	{
		fluid = FluidRegistry.getFluidName(stack);
		count = stack.amount;
		nbt = stack.tag + "";
		String ods = "";
		List<String> fluiddicts = FluidDictionary.getNamesByFluidId(FluidDictionary.getFluidId(stack));
		for(String i : fluiddicts)
			ods += i + ";";
		if(ods.endsWith(";"))
			ods = ods.substring(0, ods.length() - 1);
		fluiddictNames = ods;
		mod = stack.getFluid().getBlock().delegate.name().getNamespace();
		type = 0;
	}
	
	private FluidContainer(String fluiddict)
	{
		fluiddictNames = fluiddict;
		fluid = null;
		count = 0;
		nbt = null;
		mod = null;
		type = 1;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public Object getFluid()
	{
		return fluid != null ? FluidRegistry.getFluid(fluid) : fluiddictNames.split(";");
	}
	
	public static boolean fluiddictMatches(String a, String b)
	{
		if(a == b)
			return true;
		if(a == null || b == null)
			return false;
		for(String as : a.split(";"))
			if(b.contains(as))
				return true;
		return false;
	}
	
	public byte getType()
	{
		return type;
	}
	
	private static boolean fluidsMatch(String a, String b)
	{
		if(a == b)
			return true;
		if(a == null || b == null)
			return false;
		return a.equals(b);
	}
	
	public boolean matches(FluidContainer other, FluidMatchParams params)
	{
		boolean modOK = !params.useMod || (mod != null && other.mod != null && other.mod.equals(mod));
		boolean nbtOK = !params.useNBT || (nbt != null && other.nbt != null && other.nbt.equals(nbt));
		boolean itmOK = fluidsMatch(fluid, other.fluid);
		boolean ctnOK = !params.useCount || count == other.count;
		boolean ordOK = params.useFluiddict && fluiddictMatches(fluiddictNames, other.fluiddictNames);
		return ordOK || (modOK && nbtOK && itmOK && ctnOK);
	}
	
	public boolean matches(FluidStack stack, FluidMatchParams params)
	{
		return matches(new FluidContainer(stack), params);
	}
}