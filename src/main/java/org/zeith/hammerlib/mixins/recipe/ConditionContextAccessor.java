package org.zeith.hammerlib.mixins.recipe;

import net.minecraft.tags.TagManager;
import net.minecraftforge.common.crafting.conditions.ConditionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ConditionContext.class, remap = false)
public interface ConditionContextAccessor
{
	@Accessor
	TagManager getTagManager();
}