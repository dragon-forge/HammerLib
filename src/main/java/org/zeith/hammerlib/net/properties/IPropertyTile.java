package org.zeith.hammerlib.net.properties;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.event.listeners.ServerListener;
import org.zeith.hammerlib.net.HLTargetPoint;
import org.zeith.hammerlib.net.Network;

/**
 * Attachable to any tile to allow keep track of any properties.
 */
public interface IPropertyTile
{
	PropertyDispatcher getProperties();

	/**
	 * Call this each tick to detect potential changes and send them to everyone
	 * around.
	 */
	default void syncProperties()
	{
		ServerListener.syncProperties((BlockEntity) this);
	}

	default void syncPropertiesNow()
	{
		BlockEntity tile = (BlockEntity) this;
		Level world = tile.getLevel();
		if(world != null && !world.isClientSide)
			Network.sendToArea(new HLTargetPoint(tile.getBlockPos(), 256, world), getProperties().detectAndGenerateChanges(tile.getBlockPos(), true));
	}
}