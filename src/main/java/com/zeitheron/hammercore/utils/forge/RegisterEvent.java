package com.zeitheron.hammercore.utils.forge;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.*;

@SuppressWarnings("rawtypes")
public class RegisterEvent
{
	public final RegistryEvent.Register forge;
	
	public RegisterEvent(RegistryEvent.Register forge)
	{
		this.forge = forge;
	}
	
	public <T extends IForgeRegistryEntry<T>> void register(Class<T> type, ResourceLocation id, T object)
	{
		IForgeRegistry reg = forge.getRegistry();
		if(reg.getRegistrySuperType().equals(type))
		{
			if(object.getRegistryName() == null)
				object.setRegistryName(id);
			//noinspection unchecked
			reg.register(object);
		}
	}
}