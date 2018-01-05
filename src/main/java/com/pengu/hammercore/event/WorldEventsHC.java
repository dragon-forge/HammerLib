package com.pengu.hammercore.event;

import java.io.Serializable;

import com.pengu.hammercore.utils.IndexedMap;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class WorldEventsHC
{
	/**
	 * This event is fired when Hammer Core loads world data.
	 */
	public static class LoadData extends WorldEvent
	{
		public final IndexedMap<String, Serializable> additionalData;
		
		public LoadData(World world, IndexedMap<String, Serializable> pars)
		{
			super(world);
			this.additionalData = pars;
		}
	}
	
	/**
	 * This event is fired when Hammer Core saves world data.
	 */
	public static class SaveData extends WorldEvent
	{
		public final IndexedMap<String, Serializable> additionalData;
		
		public SaveData(World world, IndexedMap<String, Serializable> pars)
		{
			super(world);
			this.additionalData = pars;
		}
	}
}