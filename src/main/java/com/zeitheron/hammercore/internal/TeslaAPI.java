package com.zeitheron.hammercore.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;

public class TeslaAPI
{
	public static Class ITeslaConsumer = teslaClassExists("ITeslaConsumer");
	public static Class ITeslaHolder = teslaClassExists("ITeslaHolder");
	public static Class ITeslaProducer = teslaClassExists("ITeslaProducer");
	public static final List<Class> classes = new ArrayList<Class>();
	public static final List<String> allClasses = new ArrayList<String>();
	
	/**
	 * Refreshes all Tesla class data, should be called, when loading world
	 **/
	public static int refreshTeslaClassData()
	{
		allClasses.clear();
		classes.clear();
		ITeslaConsumer = teslaClassExists("ITeslaConsumer");
		ITeslaHolder = teslaClassExists("ITeslaHolder");
		ITeslaProducer = teslaClassExists("ITeslaProducer");
		return classes.size();
	}
	
	/**
	 * Checks if a subclass of Tesla|API exists
	 * 
	 * @return class The Tesla|API subclass or null, if not present
	 **/
	public static Class teslaClassExists(String api)
	{
		try
		{
			String cn = "net.darkhax.tesla.api." + api;
			allClasses.add(cn);
			Class c = Class.forName(cn);
			classes.add(c);
			return c;
		} catch(Throwable err)
		{
		}
		return null;
	}
	
	/**
	 * Checks if given {@link TileEntity} is ITeslaConsumer
	 **/
	public static boolean isTeslaConsumer(TileEntity tile)
	{
		return ITeslaConsumer != null && tile != null ? tile.getClass().isAssignableFrom(ITeslaConsumer) : false;
	}
	
	/**
	 * Checks if given {@link TileEntity} is ITeslaHolder
	 **/
	public static boolean isTeslaHolder(TileEntity tile)
	{
		return ITeslaHolder != null && tile != null ? tile.getClass().isAssignableFrom(ITeslaHolder) : false;
	}
	
	/**
	 * Checks if given {@link TileEntity} is ITeslaProducer
	 **/
	public static boolean isTeslaProducer(TileEntity tile)
	{
		return ITeslaProducer != null && tile != null ? tile.getClass().isAssignableFrom(ITeslaProducer) : false;
	}
	
	/**
	 * Offers power to the Tesla Consumer.
	 * 
	 * @param power
	 *            The amount of power to offer.
	 * @param simulated
	 *            Whether or not this is being called as part of a simulation.
	 *            Simulations are used to get information without affecting the
	 *            Tesla Producer.
	 * @return The amount of power that the consumer accepts.
	 */
	public static long givePowerToConsumer(TileEntity consumer, long power, boolean simulated)
	{
		if(isTeslaConsumer(consumer))
		{
			try
			{
				Method givePower = consumer.getClass().getMethod("givePower", long.class, boolean.class);
				return ((Long) givePower.invoke(consumer, power, simulated)).longValue();
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
		}
		return 0L;
	}
	
	/**
	 * Gets the amount of Tesla power stored being stored.
	 * 
	 * @return The amount of Tesla power being stored.
	 */
	public static long getStoredPowerInHolder(TileEntity holder)
	{
		if(isTeslaHolder(holder))
		{
			try
			{
				Method givePower = holder.getClass().getMethod("getStoredPower");
				return ((Long) givePower.invoke(holder)).longValue();
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
		}
		return 0L;
	}
	
	/**
	 * Gets the maximum amount of Tesla power that can be held.
	 * 
	 * @return The maximum amount of Tesla power that can be held.
	 */
	public static long getCapacityPowerInHolder(TileEntity holder)
	{
		if(isTeslaHolder(holder))
		{
			try
			{
				Method givePower = holder.getClass().getMethod("getCapacity");
				return ((Long) givePower.invoke(holder)).longValue();
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
		}
		return 0L;
	}
	
	/**
	 * Requests an amount of power from the Tesla Producer.
	 * 
	 * @param power
	 *            The amount of power to request.
	 * @param simulated
	 *            Whether or not this is being called as part of a simulation.
	 *            Simulations are used to get information without affecting the
	 *            Tesla Producer.
	 * @return The amount of power that the Tesla Producer will give.
	 */
	public static long takePowerFromProducer(TileEntity producer, long power, boolean simulated)
	{
		if(isTeslaProducer(producer))
		{
			try
			{
				Method givePower = producer.getClass().getMethod("takePower", long.class, boolean.class);
				return ((Long) givePower.invoke(producer, power, simulated)).longValue();
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
		}
		return 0L;
	}
}