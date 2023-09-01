package org.zeith.hammerlib.mixins;

import net.minecraft.inventory.container.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Container.class)
public interface ContainerAccessor
{
	@Accessor
	List<IContainerListener> getContainerListeners();
}
