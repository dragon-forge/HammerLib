package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.net.HCNet;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.zeith.hammerlib.event.listeners.ServerListener;

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
		Entity tile = (Entity) this;
		World world = tile.world;
		if(world != null && !world.isRemote)
			HCNet.INSTANCE.sendToAllAroundTracking(getProperties().detectAndGenerateChanges(true), tile);
	}
}