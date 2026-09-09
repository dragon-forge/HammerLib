package org.zeith.hammerlib.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.api.level.IBlockEntityLevel;

import java.util.*;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin
{
	@Shadow
	@Final
	Level level;
	
	@Shadow
	public abstract Map<BlockPos, BlockEntity> getBlockEntities();
	
	@Inject(
			method = "removeBlockEntity",
			at = @At("HEAD")
	)
	public void onBlockEntityRemove_HammerLib(BlockPos pos, CallbackInfo ci)
	{
		BlockEntity be = getBlockEntities().get(pos);
		if(be != null) IBlockEntityLevel.unloadBlockEntity(level, be);
	}
	
	@Inject(
			method = "clearAllBlockEntities",
			at = @At("HEAD")
	)
	public void onBlockEntityClearAll_HammerLib(CallbackInfo ci)
	{
		Collection<BlockEntity> bes = getBlockEntities().values();
		if(!bes.isEmpty()) IBlockEntityLevel.unloadBlockEntities(level, bes);
	}
}