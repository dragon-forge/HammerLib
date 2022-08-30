package org.zeith.hammerlib.mixins;

import com.google.common.collect.BiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RegistryManager.class)
public interface RegistryManagerAccessor
{
	@Accessor
	BiMap<ResourceLocation, ForgeRegistry<?>> getRegistries();
}
