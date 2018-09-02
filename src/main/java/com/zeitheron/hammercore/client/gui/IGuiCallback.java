package com.zeitheron.hammercore.client.gui;

import java.util.function.BiFunction;

import com.zeitheron.hammercore.internal.GuiManager;
import com.zeitheron.hammercore.lib.zlib.utils.IndexedMap;
import com.zeitheron.hammercore.utils.WorldLocation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A callback class to create GUIs. See {@link GuiManager} for more.
 */
public interface IGuiCallback
{
	default void setGuiID(int id)
	{
		Vars.ids.put(id, this);
	}
	
	default int getGuiID()
	{
		Integer i = Vars.ids.getKey(this);
		return i != null ? i : 0;
	}
	
	/**
	 * Creates a callback using client gui creator and server container creator.
	 * 
	 * @param client
	 *            The supplier of GUI, based on the player and location.
	 * @param server
	 *            The supplier of container, based on the player and location.
	 * @return A new callback that must be registered using
	 *         {@link GuiManager#registerGuiCallback(IGuiCallback)}
	 */
	static IGuiCallback create(BiFunction<EntityPlayer, WorldLocation, Object> client, BiFunction<EntityPlayer, WorldLocation, Object> server)
	{
		return new IGuiCallback()
		{
			@Override
			public Object getServerGuiElement(EntityPlayer player, World world, BlockPos pos)
			{
				return server.apply(player, new WorldLocation(world, pos));
			}
			
			@Override
			public Object getClientGuiElement(EntityPlayer player, World world, BlockPos pos)
			{
				return client.apply(player, new WorldLocation(world, pos));
			}
		};
	}
	
	/**
	 * Returns a Server side Container to be displayed to the user. On the
	 * server side, this needs to return a instance of Container
	 *
	 * @param player
	 *            The player viewing the Gui
	 * @param world
	 *            The current world
	 * @param pos
	 *            The Position
	 * @return A Container to be displayed to the user, null if none.
	 */
	Object getServerGuiElement(EntityPlayer player, World world, BlockPos pos);
	
	/**
	 * Returns a GuiScreen to be displayed to the user. On the client side, this
	 * needs to return a instance of GuiScreen.
	 *
	 * @param player
	 *            The player viewing the Gui
	 * @param world
	 *            The current world
	 * @param pos
	 *            The Position
	 * @return A GuiScreen to be displayed to the user, null if none.
	 */
	Object getClientGuiElement(EntityPlayer player, World world, BlockPos pos);
	
	static class Vars
	{
		private static final IndexedMap<Integer, IGuiCallback> ids = new IndexedMap<>();
	}
}