package org.zeith.hammerlib.net.properties;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
		ServerListener.syncProperties((TileEntity) this);
	}

	default void syncPropertiesNow()
	{
		TileEntity tile = (TileEntity) this;
		World world = tile.getLevel();
		if(world != null && !world.isClientSide)
			Network.sendToArea(new HLTargetPoint(tile.getBlockPos(), 256, world), getProperties().detectAndGenerateChanges(tile.getBlockPos(), true));
	}
}