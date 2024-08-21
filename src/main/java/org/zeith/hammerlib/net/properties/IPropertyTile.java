package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.net.HCNet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.zeith.hammerlib.event.listeners.ServerListener;

/**
 * Attachable to any tile to allow keep track of any properties.
 */
public interface IPropertyTile
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
		World world = tile.getWorld();
		if(world != null && !world.isRemote)
			HCNet.INSTANCE.sendToAllAroundTracking(getProperties().detectAndGenerateChanges(true), world, tile.getPos());
	}
}