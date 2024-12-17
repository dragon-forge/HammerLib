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
	static BlockHitResult createBlockHitResult(boolean miss, Vec3 location, Direction direction, BlockPos blockPos, boolean inside, boolean worldBorderHit)
	{
		throw new UnsupportedOperationException();
	}
}
