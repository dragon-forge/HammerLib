package com.pengu.hammercore.common.capabilities;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemCapabilityProvider implements ICapabilityProvider
{
	public Map<Capability<?>, Object> CAPS = new HashMap<>();
	
	public <T> void putCapability(Capability<T> cap, T instance)
	{
		CAPS.put(cap, instance);
	}
	
	public void clearCaps(EnumFacing... facings)
	{
		CAPS.clear();
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return CAPS.containsKey(capability);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return (T) CAPS.get(capability);
	}
}