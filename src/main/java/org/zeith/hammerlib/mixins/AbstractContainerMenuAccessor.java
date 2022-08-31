package org.zeith.hammerlib.mixins;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(AbstractContainerMenu.class)
public interface AbstractContainerMenuAccessor
{
	@Accessor
	List<ContainerListener> getContainerListeners();
}