package org.zeith.hammerlib.event.listeners;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.zeith.hammerlib.api.tiles.ISyncableTile;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.SyncTileEntityPacket;
import org.zeith.hammerlib.net.properties.IPropertyTile;

import java.util.*;

@EventBusSubscriber(bus = Bus.FORGE)
public class ServerListener
{
	public static final List<BlockEntity> NEED_SYNC = new ArrayList<>();
	public static final List<BlockEntity> NEED_PROP_SYNC = new ArrayList<>();
	
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
		}
	}
	
	public static void syncProperties(BlockEntity tileEntity)
	{
		if(tileEntity != null && tileEntity.getLevel() instanceof ServerLevel sl && !sl.isClientSide)
			NEED_PROP_SYNC.add(tileEntity);
	}
	
	public static void syncTileEntity(BlockEntity tileEntity)
	{
		if(tileEntity != null && tileEntity.getLevel() instanceof ServerLevel sl && !sl.isClientSide)
			NEED_SYNC.add(tileEntity);
	}
}