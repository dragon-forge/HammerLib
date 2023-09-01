package org.zeith.hammerlib.net.properties;

import lombok.var;
import net.minecraft.tileentity.TileEntity;
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
		TileEntity tile = (TileEntity) this;
		var world = tile.getLevel();
		if(world != null && !world.isClientSide)
			Network.sendToTracking(tile, getProperties().detectAndGenerateChanges(true));
	}
}