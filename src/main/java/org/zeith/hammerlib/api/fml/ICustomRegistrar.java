package org.zeith.hammerlib.api.fml;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;

public interface ICustomRegistrar
{
	void performRegister(RegisterEvent event, ResourceLocation id);
}