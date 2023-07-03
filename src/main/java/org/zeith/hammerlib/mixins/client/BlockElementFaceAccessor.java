package org.zeith.hammerlib.mixins.client;

import net.minecraft.client.renderer.block.model.BlockElementFace;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockElementFace.class)
public interface BlockElementFaceAccessor
{
	@Mutable
	@Accessor
	void setTexture(String texture);
}
