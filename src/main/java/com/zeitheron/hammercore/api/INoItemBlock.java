package com.zeitheron.hammercore.api;

import com.zeitheron.hammercore.internal.SimpleRegistration;

import net.minecraft.block.Block;

/**
 * Disables item for block (only applied to Hammer Core's multipart block. You
 * need to check it for yourself). Implement at any {@link Block} and the item
 * will not register. Note: only applies when block is registered through
 * {@link SimpleRegistration} class.
 */
public interface INoItemBlock
{
}