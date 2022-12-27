package org.zeith.hammerlib.api.blocks;

/**
 * Marker interface for blocks that should not have an associated {@link net.minecraft.world.item.Item}.
 * This is useful for blocks that are only used as part of a structure or in a specific way and do not need to be
 * placed or picked up as a standalone item.
 */
public interface INoItemBlock
{
}