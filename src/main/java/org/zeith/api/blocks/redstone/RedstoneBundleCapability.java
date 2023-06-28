package org.zeith.api.blocks.redstone;

import net.minecraftforge.common.capabilities.*;

import static net.minecraftforge.common.capabilities.CapabilityManager.get;

public class RedstoneBundleCapability
{
	/**
	 * Capability instance for IRedstoneBundle.
	 * Use this to access the redstone bundle capability.
	 */
	public static final Capability<IRedstoneBundle> REDSTONE_BUNDLE = get(new CapabilityToken<>() {});
}