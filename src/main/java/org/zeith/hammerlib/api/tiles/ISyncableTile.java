package org.zeith.hammerlib.api.tiles;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.event.listeners.ServerListener;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.SyncTileEntityPacket;

public interface ISyncableTile
{
	/**
	 * A default call to sync your tile entity after the world has ticked
	 */
	default void sync()
	{
		if(this instanceof BlockEntity tile && tile.hasLevel() && !tile.getLevel().isClientSide)
			ServerListener.syncTileEntity(tile);
	}
	
	/**
	 * Implement yourself: a technique to sync your tile entity.
	 * If you want to use HammerLib pipeline, use syncWithHLPipeline (don't override this method)
	 */
	default void syncNow()
	{
		syncUpdateTagHLPipeline();
	}
	
	default void syncWithHLPipeline()
	{
		if(this instanceof BlockEntity tile)
			Network.sendToTracking(tile, new SyncTileEntityPacket(tile, false));
	}
	
	default void syncUpdateTagHLPipeline()
	{
		if(this instanceof BlockEntity tile)
			Network.sendToTracking(tile, new SyncTileEntityPacket(tile, true));
	}
}