package org.zeith.hammerlib.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.api.level.IBlockEntityLevel;

import java.util.Map;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin
{
	@Shadow
	@Final
	Level level;

	@Shadow
	protected abstract boolean isInLevel();

	@Shadow
	public abstract Map<BlockPos, BlockEntity> getBlockEntities();

	@Inject(
			method = "removeBlockEntity",
			at = @At("HEAD")
	)
	public void onBlockEntityRemoveHL(BlockPos pos, CallbackInfo ci)
	{
		if(isInLevel())
		{
			BlockEntity be = getBlockEntities().get(pos);
			if(be != null) IBlockEntityLevel.unloadBlockEntity(level, be);
		}
	}
}