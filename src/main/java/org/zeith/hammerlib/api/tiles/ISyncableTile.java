package org.zeith.hammerlib.api.tiles;

import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.PacketSyncAnyTile;
import net.minecraft.tileentity.TileEntity;
import org.zeith.hammerlib.event.listeners.ServerListener;

public interface ISyncableTile
{
	/**
	 * A default call to sync your tile entity after the world has ticked
	 */
	default void sync()
	{
		if(this instanceof TileEntity && ((TileEntity) this).hasWorld() && !((TileEntity) this).getWorld().isRemote)
			ServerListener.syncTileEntity((TileEntity) this);
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
		if(this instanceof TileEntity)
			HCNet.INSTANCE.sendToAllAroundTracking(new PacketSyncAnyTile((TileEntity) this), (TileEntity) this);
	}
	
	default void syncUpdateTagHLPipeline()
	{
		if(this instanceof TileEntity)
			HCNet.INSTANCE.sendToAllAroundTracking(new PacketSyncAnyTile((TileEntity) this), (TileEntity) this);
	}
}