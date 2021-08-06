package org.zeith.hammerlib.mixins.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.chunk.ChunkRenderCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.api.blocks.IVisuallyDifferentBlock;
import org.zeith.hammerlib.event.client.model.OverrideRenderStateEvent;

@Mixin(ChunkRenderCache.class)
public abstract class ChunkRenderCacheMixin
{
	@Shadow
	@Final
	protected BlockState[] blockStates;

	@Shadow
	protected abstract int index(BlockPos p_212398_1_);

	@Shadow
	@Final
	protected World level;

	@Inject(
			method = "getBlockState",
			at = @At("HEAD"),
			cancellable = true
	)
	public void getBlockStateHLHook(BlockPos pos, CallbackInfoReturnable<BlockState> cir)
	{
		BlockState state = blockStates[index(pos)], nState = state;

		if(state != null)
		{
			Block b = state.getBlock();
			if(b instanceof IVisuallyDifferentBlock)
				nState = ((IVisuallyDifferentBlock) b).handle(level, pos, state);
			if(OverrideRenderStateEvent.isEnabled())
			{
				OverrideRenderStateEvent evt = new OverrideRenderStateEvent(level, pos, state, nState);
				MinecraftForge.EVENT_BUS.post(evt);
				nState = evt.getNewState();
			}
		}
		if(state != nState)
			cir.setReturnValue(nState);
	}
}