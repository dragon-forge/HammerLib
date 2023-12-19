package org.zeith.hammerlib.api.fml;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * This interface gets invoked when an instance implementing of it is placed in a class of @{@link org.zeith.hammerlib.annotations.SimplyRegister} and the @{@link org.zeith.hammerlib.annotations.RegistryName} for the field.
 * Instances that are implemented by this interface will NOT get treated as individual objects to be registered.
 * Example: If the {@link net.minecraft.world.level.block.Block} implements {@link ICustomRegistrar}, it will not be registered.
 */
public interface ICustomRegistrar
{
	/**
	 * Perform the registration via {@link RegisterEvent}.
	 * This gets called for all registries there are, please filter things out.
	 * Use the {@link ResourceLocation} id
	 */
	void performRegister(RegisterEvent event, ResourceLocation id);
}