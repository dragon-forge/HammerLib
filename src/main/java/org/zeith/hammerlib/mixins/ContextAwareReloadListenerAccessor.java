package org.zeith.hammerlib.mixins;

import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.resource.ContextAwareReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ContextAwareReloadListener.class)
public interface ContextAwareReloadListenerAccessor
{
	@Invoker
	HolderLookup.Provider callGetRegistryLookup();
	
	@Invoker
	ICondition.IContext callGetContext();
}
