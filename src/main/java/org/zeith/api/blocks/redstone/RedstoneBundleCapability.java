package org.zeith.api.blocks.redstone;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.*;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.proxy.HLConstants;

/**
 * Capability instances for IRedstoneBundle.
 * Use this to access the redstone bundle capability.
 */
public class RedstoneBundleCapability
{
	public static final BlockCapability<IRedstoneBundle, @Nullable Direction> BLOCK = BlockCapability.createSided(create("redstone_bundle"), IRedstoneBundle.class);
	public static final EntityCapability<IRedstoneBundle, @Nullable Direction> ENTITY = EntityCapability.createSided(create("redstone_bundle"), IRedstoneBundle.class);
	public static final ItemCapability<IRedstoneBundle, Void> ITEM = ItemCapability.createVoid(create("redstone_bundle"), IRedstoneBundle.class);
	
	private static ResourceLocation create(String path)
	{
		return HLConstants.id(path);
	}
}