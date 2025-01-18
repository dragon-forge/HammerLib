package org.zeith.hammerlib.mixins.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.tags.TagManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TagManager.class)
public interface TagManagerAccessor
{
	@Accessor
	RegistryAccess getRegistryAccess();
}