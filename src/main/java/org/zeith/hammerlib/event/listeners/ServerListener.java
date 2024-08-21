package org.zeith.hammerlib.event.listeners;

import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.PacketSyncAnyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.zeith.hammerlib.api.tiles.ISyncableTile;
import org.zeith.hammerlib.net.properties.IBasePropertyHolder;
import org.zeith.hammerlib.net.properties.IPropertyTile;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

import java.util.*;

@Mod.EventBusSubscriber
public class ServerListener
{
	public static final List<TileEntity> NEED_SYNC = new ArrayList<>();
	public static final List<TileEntity> NEED_PROP_SYNC = new ArrayList<>();
	public static final List<IBasePropertyHolder> NEED_PROP_SYNC_GENERIC = new ArrayList<>();
	
	@SubscribeEvent
	public static void serverTick(TickEvent.ServerTickEvent e)
	{
		if(e.phase != TickEvent.Phase.END) return;
		
		// After we're done with ticking tiles...
		Set<BlockPos> processed = new HashSet<>();
		
		while(!NEED_SYNC.isEmpty())
		{
			TileEntity tile = NEED_SYNC.remove(0);
			if(!processed.add(tile.getPos())) continue;
			
			if(tile instanceof ISyncableTile)
				((ISyncableTile) tile).syncNow();
			else
				HCNet.INSTANCE.sendToAllAroundTracking(new PacketSyncAnyTile(tile), tile);
		}
		
		processed.clear();
		
		while(!NEED_PROP_SYNC.isEmpty())
		{
			TileEntity tile = NEED_PROP_SYNC.remove(0);
			if(!processed.add(tile.getPos())) continue;
			
			if(tile instanceof IPropertyTile)
				((IPropertyTile) tile).syncPropertiesNow();
		}
		
		while(!NEED_PROP_SYNC_GENERIC.isEmpty())
		{
			IBasePropertyHolder data = NEED_PROP_SYNC_GENERIC.remove(0);
			if(data != null)
				data.syncPropertiesNow();
		}
	}
	
	public static void syncProperties(IBasePropertyHolder ent)
	{
		if(LogicalSidePredictor.getCurrentLogicalSide() == Side.SERVER)
			NEED_PROP_SYNC_GENERIC.add(ent);
	}
	
	public static void syncTileEntity(TileEntity tileEntity)
	{
		if(tileEntity != null && tileEntity.getWorld() instanceof WorldServer)
			NEED_SYNC.add(tileEntity);
	}
}