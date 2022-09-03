package org.zeith.hammerlib.event.listeners;

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

import java.util.ArrayList;
import java.util.List;

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
			while(!NEED_SYNC.isEmpty())
			{
				BlockEntity tile = NEED_SYNC.remove(0);
				if(tile instanceof ISyncableTile s)
					s.syncNow();
				else
					Network.sendToTracking(tile.getLevel().getChunkAt(tile.getBlockPos()), new SyncTileEntityPacket(tile));
			}
			
			while(!NEED_PROP_SYNC.isEmpty())
			{
				BlockEntity tile = NEED_PROP_SYNC.remove(0);
				if(tile instanceof IPropertyTile ipt)
					ipt.syncPropertiesNow();
			}
		}
	}
	
	public static void syncProperties(BlockEntity tileEntity)
	{
		NEED_PROP_SYNC.add(tileEntity);
	}
	
	public static void syncTileEntity(BlockEntity tileEntity)
	{
		NEED_SYNC.add(tileEntity);
	}
}