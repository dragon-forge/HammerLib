package org.zeith.hammerlib.mixins;

import net.minecraft.core.RegistryAccess;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.resource.ContextAwareReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ContextAwareReloadListener.class)
public interface ContextAwareReloadListenerAccessor
{
	@Accessor
	ICondition.IContext getConditionContext();
	
	@Accessor
	RegistryAccess getRegistryAccess();
}
