package com.zeitheron.hammercore.intr.top;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.tile.tooltip.ITooltipTile;
import com.zeitheron.hammercore.tile.tooltip.ProgressBar;
import com.zeitheron.hammercore.tile.tooltip.eTooltipEngine;
import com.zeitheron.hammercore.utils.WorldLocation;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GetTOP implements IProbeInfoProvider, Function<ITheOneProbe, Void>
{
	@Override
	public Void apply(ITheOneProbe top)
	{
		HammerCore.LOG.info("TheOneProbe API hooked!");
		top.registerProvider(this);
		return null;
	}
	
	@Override
	public String getID()
	{
		return "hammercore";
	}
	
	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState state, IProbeHitData hitData)
	{
		WorldLocation loc = new WorldLocation(world, hitData.getPos());
		ITooltipTile tile = loc.getTileOfInterface(ITooltipTile.class);
		
		if(tile != null && tile.isEngineSupported(eTooltipEngine.THEONEPROBE))
		{
			List<String> tip = new ArrayList<>();
			tile.getTextTooltip(tip, player);
			for(String s : tip)
				if(s.contains("\n"))
					for(String t : s.split("\n"))
						info.text(t);
				else
					info.text(s);
			if(tile.hasProgressBars(player))
			{
				ProgressBar[] bars = tile.getProgressBars(player);
				if(bars != null && bars.length > 0)
				{
					for(ProgressBar bar : bars)
					{
						IProgressStyle style = info.defaultProgressStyle().filledColor(bar.filledMainColor).alternateFilledColor(bar.filledAlternateColor).backgroundColor(bar.backgroundColor).borderColor(bar.borderColor);
						if(bar.suffix != null && !bar.suffix.isEmpty())
							style = style.suffix(bar.suffix);
						if(bar.prefix != null && !bar.prefix.isEmpty())
							style = style.prefix(bar.prefix);
						if(bar.numberFormat != null)
							style = style.numberFormat(mcjty.theoneprobe.api.NumberFormat.values()[bar.numberFormat.ordinal()]);
						info.progress(bar.getProgressPercent(), 100, style);
					}
				}
			}
		}
	}
}