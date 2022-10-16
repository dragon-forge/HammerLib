package org.zeith.hammerlib.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockHitResult.class)
public interface BlockHitResultAccessor
{
	@Invoker("<init>")
	static BlockHitResult createBlockHitResult(boolean p_82420_, Vec3 p_82421_, Direction p_82422_, BlockPos p_82423_, boolean p_82424_)
	{
		throw new UnsupportedOperationException();
	}
}
