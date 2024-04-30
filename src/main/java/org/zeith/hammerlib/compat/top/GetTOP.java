package org.zeith.hammerlib.compat.top;

import mcjty.theoneprobe.api.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.tiles.tooltip.EnumTooltipEngine;
import org.zeith.hammerlib.tiles.tooltip.ITooltipTile;
import org.zeith.hammerlib.util.java.Cast;

import java.util.function.Function;

public class GetTOP
		implements IProbeInfoProvider, Function<ITheOneProbe, Void>
{
	protected final ResourceLocation id = HLConstants.id("root");
	
	@Override
	public Void apply(ITheOneProbe top)
	{
		HammerLib.LOG.info("TheOneProbe API hooked!");
		top.registerProvider(this);
		return null;
	}
	
	@Override
	public ResourceLocation getID()
	{
		return id;
	}
	
	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level world, BlockState state, IProbeHitData hitData)
	{
		ITooltipTile tile = Cast.cast(world.getBlockEntity(hitData.getPos()), ITooltipTile.class);
		if(tile != null && tile.isEngineSupported(EnumTooltipEngine.THEONEPROBE))
			tile.addTooltip(new TOPTooltipConsumer(info, hitData), player);
	}
}