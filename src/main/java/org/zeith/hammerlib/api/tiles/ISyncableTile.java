package org.zeith.hammerlib.api.tiles;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.event.listeners.ServerListener;
import org.zeith.hammerlib.net.HLTargetPoint;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.SyncTileEntityPacket;

public interface ISyncableTile
{
	/**
	 * A default call to sync your tile entity after the world has ticked
	 */
	default void sync()
	{
		if(this instanceof BlockEntity)
			ServerListener.syncTileEntity((BlockEntity) this);
	}

	/**
	 * Implement yourself: a technique to sync your tile entity.
	 * If you want to use HammerLib pipeline, use syncWithHLPipeline (don't override this method)
	 */
	default void syncNow()
	{
		syncWithHLPipeline();
	}

	default void syncWithHLPipeline()
	{
		if(this instanceof BlockEntity tile)
			Network.sendToTracking(tile.getLevel().getChunkAt(tile.getBlockPos()), new SyncTileEntityPacket(tile));
	}
}