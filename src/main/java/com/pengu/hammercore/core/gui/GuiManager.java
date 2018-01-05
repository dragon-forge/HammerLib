package com.pengu.hammercore.core.gui;

import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.core.gui.container.ContainerEmpty;
import com.pengu.hammercore.tile.TileSyncable;
import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class GuiManager implements IGuiHandler
{
	private static final List<iGuiCallback> callbacks = new ArrayList<>(64);
	private static final int lastUsedBuiltintId = 2;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == 0)
		{
			TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
			TileSyncable syncable = WorldUtil.cast(te, TileSyncable.class);
			
			if(syncable != null)
				return syncable.getServerGuiElement(player);
		}
		
		if(ID == 1)
			return new ContainerEmpty();
		
		if(callbacks.size() + lastUsedBuiltintId > ID)
		{
			iGuiCallback c = callbacks.get(ID - lastUsedBuiltintId);
			if(c != null)
				return c.getServerGuiElement(player, world, new BlockPos(x, y, z));
		}
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == 0)
		{
			TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
			TileSyncable syncable = WorldUtil.cast(te, TileSyncable.class);
			
			if(syncable != null)
				return syncable.getClientGuiElement(player);
		}
		
		if(ID == 1)
			return new GuiCalculator();
		
		if(callbacks.size() + lastUsedBuiltintId > ID)
		{
			iGuiCallback c = callbacks.get(ID - lastUsedBuiltintId);
			if(c != null)
				return c.getClientGuiElement(player, world, new BlockPos(x, y, z));
		}
		
		return null;
	}
	
	public static void openGui(EntityPlayer player, TileSyncable tile)
	{
		if(player != null && tile != null && !player.world.isRemote && tile.hasGui())
			FMLNetworkHandler.openGui(player, HammerCore.instance, 0, player.world, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
	}
	
	public static void registerGuiCallback(iGuiCallback callback)
	{
		callback.setGuiID(callbacks.size());
		callbacks.add(callback);
	}
	
	public static void openGuiCallback(int callbackID, EntityPlayer player, World world, BlockPos pos)
	{
		if(!world.isRemote)
			FMLNetworkHandler.openGui(player, HammerCore.instance, 2 + callbackID, world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public static void openGuiCallback(iGuiCallback callbackID, EntityPlayer player, World world, BlockPos pos)
	{
		openGuiCallback(callbackID.getGuiID(), player, world, pos);
	}
	
	public static void openGuiCallback(int callbackID, EntityPlayer player, WorldLocation loc)
	{
		openGuiCallback(callbackID, player, loc.getWorld(), loc.getPos());
	}
	
	public static void openGuiCallback(iGuiCallback callbackID, EntityPlayer player, WorldLocation loc)
	{
		openGuiCallback(callbackID.getGuiID(), player, loc.getWorld(), loc.getPos());
	}
}