package com.zeitheron.hammercore.internal.capabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class SidedCapabilityProvider implements ICapabilityProvider
{
	public Map<EnumFacing, Map<Capability<?>, Object>> CAPS = new HashMap<>();

	public <T> void putCapability(Capability<T> cap, T instance)
	{
		for(EnumFacing f : EnumFacing.VALUES)
			putCapability(f, cap, instance);
	}
	
	public <T> void putCapability(EnumFacing side, Capability<T> cap, T instance)
	{
		CAPS.computeIfAbsent(side, k -> new HashMap<>()).put(cap, instance);
	}
	
	public void clearCaps(EnumFacing... facings)
	{
		for(EnumFacing facing : facings)
			CAPS.remove(facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		Map<Capability<?>, ?> SIDED = CAPS.get(facing);
		if(SIDED != null)
			return SIDED.containsKey(capability);
		return false;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		Map<Capability<?>, ?> SIDED = CAPS.get(facing);
		if(SIDED != null)
			return (T) SIDED.get(capability);
		return null;
	}

	public <T> T getCapabilityOr(Capability<T> capability, EnumFacing facing, BiFunction<Capability<T>, EnumFacing, T> def)
	{
		if(!hasCapability(capability, facing)) return def.apply(capability, facing);
		return getCapability(capability, facing);
	}
}