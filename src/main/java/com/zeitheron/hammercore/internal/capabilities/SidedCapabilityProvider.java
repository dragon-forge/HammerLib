package com.zeitheron.hammercore.internal.capabilities;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class SidedCapabilityProvider implements ICapabilityProvider
{
	public Map<EnumFacing, Map<Capability<?>, Object>> CAPS = new HashMap<>();
	
	public <T> void putCapability(EnumFacing side, Capability<T> cap, T instance)
	{
		Map<Capability<?>, Object> SIDED = CAPS.get(side);
		if(SIDED != null)
			CAPS.put(side, SIDED = new HashMap<>());
		SIDED.put(cap, instance);
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
}