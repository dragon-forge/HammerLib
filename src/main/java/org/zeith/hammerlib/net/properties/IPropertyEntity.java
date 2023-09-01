package org.zeith.hammerlib.net.properties;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.event.listeners.ServerListener;
import org.zeith.hammerlib.net.Network;

/**
 * Attachable to any entity to allow keep track of any properties.
 */
public interface IPropertyEntity
		extends IBasePropertyHolder
{
	/**
	 * Call this each tick to detect potential changes and send them to everyone
	 * around.
	 */
	@Override
	default void syncProperties()
	{
		ServerListener.syncProperties(this);
	}
	
	@Override
	default void syncPropertiesNow()
	{
		BlockEntity tile = (BlockEntity) this;
		Level world = tile.getLevel();
		if(world != null && !world.isClientSide)
			Network.sendToTracking(tile, getProperties().detectAndGenerateChanges(true));
	}
}