package org.zeith.api.blocks.redstone;

import net.neoforged.neoforge.common.capabilities.*;

public class RedstoneBundleCapability
{
	/**
	 * Capability instance for IRedstoneBundle.
	 * Use this to access the redstone bundle capability.
	 */
	public static final Capability<IRedstoneBundle> REDSTONE_BUNDLE = CapabilityManager.get(new CapabilityToken<>() {});
}