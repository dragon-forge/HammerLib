package com.zeitheron.hammercore.utils.forge;

import net.minecraft.util.ResourceLocation;

public interface ICustomRegistrar
{
	void register(ResourceLocation id, RegisterEvent event);
}