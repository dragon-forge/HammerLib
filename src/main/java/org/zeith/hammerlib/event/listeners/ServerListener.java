package org.zeith.hammerlib.event.listeners;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.zeith.hammerlib.api.tiles.ISyncableTile;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.SyncTileEntityPacket;
import org.zeith.hammerlib.net.properties.IBasePropertyHolder;
import org.zeith.hammerlib.net.properties.IPropertyTile;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

import java.util.*;

@EventBusSubscriber
public class ServerListener
{
	public static final List<BlockEntity> NEED_SYNC = new ArrayList<>();
	public static final List<BlockEntity> NEED_PROP_SYNC = new ArrayList<>();
	public static final List<IBasePropertyHolder> NEED_PROP_SYNC_GENERIC = new ArrayList<>();
	
	@SubscribeEvent
	public static void serverTick(ServerTickEvent.Post e)
	{
		// After we're done with ticking tiles...
		Set<BlockPos> processed = new HashSet<>();
		
		while(!NEED_SYNC.isEmpty())
		{
			BlockEntity tile = NEED_SYNC.remove(0);
			if(!processed.add(tile.getBlockPos())) continue;
			
			if(tile instanceof ISyncableTile s)
				s.syncNow();
			else
				Network.sendToTracking(new SyncTileEntityPacket(tile, false), tile);
		}
		
		processed.clear();
		
		while(!NEED_PROP_SYNC.isEmpty())
		{
			BlockEntity tile = NEED_PROP_SYNC.remove(0);
			if(!processed.add(tile.getBlockPos())) continue;
			
			if(tile instanceof IPropertyTile ipt)
				ipt.syncPropertiesNow();
		}
		
		while(!NEED_PROP_SYNC_GENERIC.isEmpty())
		{
			var data = NEED_PROP_SYNC_GENERIC.remove(0);
			if(data != null)
				data.syncPropertiesNow();
		}
	}
	
	public static void syncProperties(IBasePropertyHolder ent)
	{
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
			NEED_PROP_SYNC_GENERIC.add(ent);
	}
	
	public static void syncTileEntity(BlockEntity tileEntity)
	{
		if(tileEntity != null && tileEntity.getLevel() instanceof ServerLevel sl && !sl.isClientSide)
			NEED_SYNC.add(tileEntity);
	}
}