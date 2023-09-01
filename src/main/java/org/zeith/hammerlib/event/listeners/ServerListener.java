package org.zeith.hammerlib.event.listeners;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.TickEvent.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.zeith.hammerlib.api.tiles.ISyncableTile;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.SyncTileEntityPacket;
import org.zeith.hammerlib.net.properties.*;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

import java.util.*;

@EventBusSubscriber(bus = Bus.FORGE)
public class ServerListener
{
	public static final List<BlockEntity> NEED_SYNC = new ArrayList<>();
	public static final List<BlockEntity> NEED_PROP_SYNC = new ArrayList<>();
	public static final List<IBasePropertyHolder> NEED_PROP_SYNC_GENERIC = new ArrayList<>();
	
	@SubscribeEvent
	public static void serverTick(ServerTickEvent e)
	{
		// After we're done with ticking tiles...
		if(e.phase == Phase.END)
		{
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