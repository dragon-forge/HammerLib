package org.zeith.hammerlib.mixins.world.block.state;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.class)
public interface BlockBehaviourAccessor
{
	@Mutable
	@Accessor("descriptionId")
	void setDescriptionId(String descriptionId);
}