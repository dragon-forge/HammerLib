package com.zeitheron.hammercore.bookAPI.fancy;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.zeitheron.hammercore.HammerCore;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A collection of categories known to a manual.
 */
public class ManualCategories
{
	public static LinkedHashMap<String, ManualCategory> manualCategories = new LinkedHashMap();
	
	/**
	 * @param key
	 *            The category unique ID
	 * @return The category, or null
	 */
	public static ManualCategory getCategory(String key)
	{
		return manualCategories.get(key);
	}
	
	/**
	 * @param key
	 *            The category unique ID
	 * @return The category's translated name.
	 */
	@SideOnly(Side.CLIENT)
	public static String getCategoryName(String key)
	{
		return I18n.format("hc.manual_cat." + key);
	}
	
	/**
	 * @param key
	 *            The category unique ID
	 * @return The {@link ManualEntry} by it's unique ID.
	 */
	public static ManualEntry getEntry(String key)
	{
		Collection<ManualCategory> rc = manualCategories.values();
		Iterator<ManualCategory> i$ = rc.iterator();
		
		while(i$.hasNext())
		{
			ManualCategory cat = i$.next();
			Collection<ManualEntry> rl = cat.entries.values();
			Iterator<ManualEntry> i$1 = rl.iterator();
			
			while(i$1.hasNext())
			{
				ManualEntry ri = i$1.next();
				if(ri.key.equals(key))
					return ri;
			}
		}
		
		return null;
	}
	
	/**
	 * Registers a new category.
	 * 
	 * @param key
	 *            The unique ID of this category.
	 * @param icon
	 *            The icon of this category.
	 * @param background
	 *            The background of this category.
	 */
	public static void registerCategory(String key, ResourceLocation icon, ResourceLocation background)
	{
		if(getCategory(key) == null)
		{
			ManualCategory rl = new ManualCategory(icon, background);
			manualCategories.put(key, rl);
		}
	}
	
	/**
	 * Registers a new category with default background.
	 * 
	 * @param key
	 *            The unique ID of this category.
	 * @param icon
	 *            The icon of this category.
	 */
	public static void registerCategory(String key, ResourceLocation icon)
	{
		if(getCategory(key) == null)
		{
			ManualCategory rl = new ManualCategory(icon);
			manualCategories.put(key, rl);
		}
	}
	
	/**
	 * Registers a new category.
	 * 
	 * @param key
	 *            The unique ID of this category.
	 * @param rl
	 *            The manual cateogory to register.
	 */
	public static void registerCategory(String key, ManualCategory rl)
	{
		if(getCategory(key) == null)
			manualCategories.put(key, rl);
	}
	
	/**
	 * Internal use only, registers an entry.
	 * 
	 * @param ri
	 *            The entry
	 */
	public static void addEntry(ManualEntry ri)
	{
		ManualCategory rl = getCategory(ri.category);
		if(rl != null && !rl.entries.containsKey(ri.key))
		{
			Iterator i$ = rl.entries.values().iterator();
			
			while(i$.hasNext())
			{
				ManualEntry rr = (ManualEntry) i$.next();
				if(rr.displayColumn == ri.displayColumn && rr.displayRow == ri.displayRow)
				{
					HammerCore.LOG.error("Manual Entry [" + ri.getName() + "] not added as it overlaps with existing one [" + rr.getName() + "]");
					return;
				}
			}
			
			rl.entries.put(ri.key, ri);
			
			if(ri.displayColumn < rl.minDisplayColumn)
				rl.minDisplayColumn = ri.displayColumn;
			
			if(ri.displayRow < rl.minDisplayRow)
				rl.minDisplayRow = ri.displayRow;
			
			if(ri.displayColumn > rl.maxDisplayColumn)
				rl.maxDisplayColumn = ri.displayColumn;
			
			if(ri.displayRow > rl.maxDisplayRow)
				rl.maxDisplayRow = ri.displayRow;
		}
	}
}