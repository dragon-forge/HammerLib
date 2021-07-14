package org.zeith.hammerlib.event.client.model;


import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.BlockEvent;
import org.zeith.hammerlib.HammerLib;

@OnlyIn(Dist.CLIENT)
public class OverrideRenderStateEvent
		extends BlockEvent
{
	private BlockState overrideState;

	public OverrideRenderStateEvent(IWorld world, BlockPos pos, BlockState state, BlockState overrideState)
	{
		super(world, pos, state);
		this.overrideState = overrideState;
	}

	public void setNewState(BlockState overrideState)
	{
		this.overrideState = overrideState;
	}

	public BlockState getNewState()
	{
		return overrideState;
	}

	public static boolean isEnabled;

	public static void enable()
	{
		if(!isEnabled)
		{
			isEnabled = true;
			HammerLib.LOG.info("OverrideRenderStateEvent was enabled.");
		}
	}

	public static boolean isEnabled()
	{
		return isEnabled;
	}
}