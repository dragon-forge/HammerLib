package org.zeith.hammerlib.compat.jade;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.tiles.tooltip.EnumTooltipEngine;
import org.zeith.hammerlib.tiles.tooltip.ITooltipTile;
import org.zeith.hammerlib.util.java.Cast;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.impl.ui.ItemStackElement;

public enum WailaBlockRenderer
		implements IBlockComponentProvider
{
	INSTANCE;
	
	private final ResourceLocation id = HLConstants.id("root");
	
	@Override
	public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon)
	{
		ITooltipTile itt = Cast.cast(accessor.getBlockEntity(), ITooltipTile.class);
		if(itt != null && itt.hasItemIconOverride())
			return ItemStackElement.of(itt.getItemIconOverride());
		return IBlockComponentProvider.super.getIcon(accessor, config, currentIcon);
	}
	
	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
	{
		ITooltipTile itt = Cast.cast(accessor.getBlockEntity(), ITooltipTile.class);
		if(itt != null && itt.isEngineSupported(EnumTooltipEngine.WAILA))
			itt.addTooltip(new JadeTooltipConsumer(tooltip, accessor), accessor.getPlayer());
	}
	
	@Override
	public ResourceLocation getUid()
	{
		return id;
	}
}