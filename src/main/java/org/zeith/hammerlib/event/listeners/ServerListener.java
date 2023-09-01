package org.zeith.hammerlib.event.listeners;

import lombok.var;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.zeith.hammerlib.api.LanguageHelper;
import org.zeith.hammerlib.api.tiles.ISyncableTile;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.SyncTileEntityPacket;
import org.zeith.hammerlib.net.properties.*;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

import java.util.*;

@EventBusSubscriber(bus = Bus.FORGE)
public class ServerListener
{
	public static final List<TileEntity> NEED_SYNC = new ArrayList<>();
	public static final List<TileEntity> NEED_PROP_SYNC = new ArrayList<>();
	public static final List<IBasePropertyHolder> NEED_PROP_SYNC_GENERIC = new ArrayList<>();
	
	@SubscribeEvent
	public static void serverTick(ServerTickEvent e)
	{
		if(e.phase == Phase.START && FMLEnvironment.dist == Dist.DEDICATED_SERVER)
			LanguageHelper.update();
		
		// After we're done with ticking tiles...
		if(e.phase == Phase.END)
		{
			Set<BlockPos> processed = new HashSet<>();
			
			while(!NEED_SYNC.isEmpty())
			{
				TileEntity tile = NEED_SYNC.remove(0);
				if(!processed.add(tile.getBlockPos())) continue;
				
				if(tile instanceof ISyncableTile)
					((ISyncableTile) tile).syncNow();
				else
					Network.sendToTracking(new SyncTileEntityPacket(tile), tile);
			}
			
			processed.clear();
			
			while(!NEED_PROP_SYNC.isEmpty())
			{
				TileEntity tile = NEED_PROP_SYNC.remove(0);
				if(!processed.add(tile.getBlockPos())) continue;
				
				if(tile instanceof IPropertyTile)
					((IPropertyTile) tile).syncPropertiesNow();
			}
			
			while(!NEED_PROP_SYNC_GENERIC.isEmpty())
			{
				var data = NEED_PROP_SYNC_GENERIC.remove(0);
				if(data != null)
					data.syncPropertiesNow();
			}
		}
	}
	
	public static void syncProperties(TileEntity tileEntity)
	{
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER && tileEntity instanceof IPropertyTile)
			NEED_PROP_SYNC.add(tileEntity);
	}
	
	public static void syncProperties(IBasePropertyHolder ent)
	{
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
			NEED_PROP_SYNC_GENERIC.add(ent);
	}
	
	public static void syncTileEntity(TileEntity tileEntity)
	{
		if(tileEntity != null && tileEntity.getLevel() instanceof ServerWorld)
			NEED_SYNC.add(tileEntity);
	}
}