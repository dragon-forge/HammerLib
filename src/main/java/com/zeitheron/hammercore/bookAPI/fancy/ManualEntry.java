package com.zeitheron.hammercore.bookAPI.fancy;

import java.util.List;
import java.util.Random;

import com.zeitheron.hammercore.utils.color.ColorHelper;
import com.zeitheron.hammercore.utils.web.URLLocation;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

/**
 * An entry in a manual. Must contain some {@link ManualPage}s.
 */
public class ManualEntry
{
	public final String key;
	public final String category;
	public int color = 0xFFFFFF;
	public String[] parents = null;
	public String[] siblings = null;
	public final int displayColumn;
	public final int displayRow;
	public final Object icon_item;
	public final ResourceLocation icon_resource;
	private boolean isSpecial;
	private EnumEntryShape shape = EnumEntryShape.SQUARE;
	private ManualPage[] pages = null;
	
	/**
	 * Creates an entry in category with specified key.
	 * 
	 * @param key
	 *            The unique ID of this entry.
	 * @param category
	 *            The unique ID of the category.
	 */
	public ManualEntry(String key, String category)
	{
		this.key = key;
		this.category = category;
		this.icon_resource = null;
		this.icon_item = null;
		this.displayColumn = 0;
		this.displayRow = 0;
		
		Random r = new Random((key + category).hashCode());
		float red = .25F + r.nextFloat() * .75F;
		float green = .25F + r.nextFloat() * .75F;
		float blue = .25F + r.nextFloat() * .75F;
		setColor(ColorHelper.packRGB(red, green, blue));
	}
	
	/**
	 * Creates an entry in category with specified key, location and icon.
	 * 
	 * @param key
	 *            The unique ID of this entry.
	 * @param category
	 *            The unique ID of the category.
	 * @param col
	 *            The X position of the entry in the category (bigger values go
	 *            to right)
	 * @param row
	 *            The Y position of the entry in the category (bigger values go
	 *            to down)
	 * @param icon
	 *            The icon of this entry.
	 */
	public ManualEntry(String key, String category, int col, int row, ResourceLocation icon)
	{
		this.key = key;
		this.category = category;
		this.icon_resource = icon;
		this.icon_item = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	/**
	 * Creates an entry in category with specified key, location and icon.
	 * 
	 * @param key
	 *            The unique ID of this entry.
	 * @param category
	 *            The unique ID of the category.
	 * @param col
	 *            The X position of the entry in the category (bigger values go
	 *            to right)
	 * @param row
	 *            The Y position of the entry in the category (bigger values go
	 *            to down)
	 * @param icon
	 *            The icon of this entry.
	 */
	public ManualEntry(String key, String category, int col, int row, ItemStack icon)
	{
		this.key = key;
		this.category = category;
		this.icon_item = icon;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	/**
	 * Creates an entry in category with specified key, location and icon.
	 * 
	 * @param key
	 *            The unique ID of this entry.
	 * @param category
	 *            The unique ID of the category.
	 * @param col
	 *            The X position of the entry in the category (bigger values go
	 *            to right)
	 * @param row
	 *            The Y position of the entry in the category (bigger values go
	 *            to down)
	 * @param icon
	 *            The icon of this entry.
	 */
	public ManualEntry(String key, String category, int col, int row, URLLocation icon)
	{
		this.key = key;
		this.category = category;
		this.icon_item = icon;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	/**
	 * Creates an entry in category with specified key, location and icon.
	 * 
	 * @param key
	 *            The unique ID of this entry.
	 * @param category
	 *            The unique ID of the category.
	 * @param col
	 *            The X position of the entry in the category (bigger values go
	 *            to right)
	 * @param row
	 *            The Y position of the entry in the category (bigger values go
	 *            to down)
	 * @param icons
	 *            The icons of this entry.
	 */
	public ManualEntry(String key, String category, int col, int row, ItemStack... icons)
	{
		this.key = key;
		this.category = category;
		this.icon_item = icons;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	/**
	 * Creates an entry in category with specified key, location and icon.
	 * 
	 * @param key
	 *            The unique ID of this entry.
	 * @param category
	 *            The unique ID of the category.
	 * @param col
	 *            The X position of the entry in the category (bigger values go
	 *            to right)
	 * @param row
	 *            The Y position of the entry in the category (bigger values go
	 *            to down)
	 * @param icons
	 *            The icons of this entry.
	 */
	public ManualEntry(String key, String category, int col, int row, List<ItemStack> icons)
	{
		this.key = key;
		this.category = category;
		this.icon_item = icons;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	/**
	 * Creates an entry in category with specified key, location and icon.
	 * 
	 * @param key
	 *            The unique ID of this entry.
	 * @param category
	 *            The unique ID of the category.
	 * @param col
	 *            The X position of the entry in the category (bigger values go
	 *            to right)
	 * @param row
	 *            The Y position of the entry in the category (bigger values go
	 *            to down)
	 * @param icon
	 *            The icon {@link Ingredient} of this entry.
	 */
	public ManualEntry(String key, String category, int col, int row, Ingredient icon)
	{
		this.key = key;
		this.category = category;
		this.icon_item = icon;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	/**
	 * Creates an entry in category with specified key, location and icon.
	 * 
	 * @param key
	 *            The unique ID of this entry.
	 * @param category
	 *            The unique ID of the category.
	 * @param col
	 *            The X position of the entry in the category (bigger values go
	 *            to right)
	 * @param row
	 *            The Y position of the entry in the category (bigger values go
	 *            to down)
	 * @param iconOreDict
	 *            The OreDictionary icon of this entry.
	 */
	public ManualEntry(String key, String category, int col, int row, String iconOreDict)
	{
		this.key = key;
		this.category = category;
		this.icon_item = iconOreDict;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	/**
	 * Sets this entry as 'special'
	 * 
	 * @return This entry, for building convenience.
	 */
	public ManualEntry setSpecial()
	{
		this.isSpecial = true;
		return this;
	}
	
	/**
	 * Sets this entry parents by their unique IDs.
	 * 
	 * @return This entry, for building convenience.
	 */
	public ManualEntry setParents(String... par)
	{
		this.parents = par;
		return this;
	}
	
	/**
	 * Sets this entry passed pages. See {@link ManualPage}'s constructors for
	 * some options.
	 * 
	 * @return This entry, for building convenience.
	 */
	public ManualEntry setPages(ManualPage... par)
	{
		this.pages = par;
		return this;
	}
	
	public ManualPage[] getPages()
	{
		return this.pages;
	}
	
	/**
	 * Registers this entry to manual.
	 * 
	 * @return This entry, for building convenience.
	 */
	public ManualEntry registerEntry()
	{
		ManualCategories.addEntry(this);
		return this;
	}
	
	public String getName()
	{
		return I18n.format("hc.manual_name." + this.key);
	}
	
	public String getText()
	{
		return I18n.format("hc.manual_text." + this.key);
	}
	
	public boolean isSpecial()
	{
		return this.isSpecial;
	}
	
	@Deprecated
	public eEntryShape getShape()
	{
		return eEntryShape.values()[this.shape.ordinal()];
	}
	
	@Deprecated
	public ManualEntry setShape(eEntryShape shape)
	{
		return setShape(EnumEntryShape.values()[shape.ordinal()]);
	}
	
	/**
	 * Sets this entry a shape.
	 * 
	 * @return This entry, for building convenience.
	 */
	public ManualEntry setShape(EnumEntryShape shape)
	{
		this.shape = shape;
		return this;
	}
	
	public int getColor()
	{
		return color;
	}
	
	/**
	 * Sets this entry a color.
	 * 
	 * @return This entry, for building convenience.
	 */
	public ManualEntry setColor(int color)
	{
		this.color = color;
		return this;
	}
	
	@Deprecated
	public enum eEntryShape
	{
		SQUARE, ROUND, HEX;
	}
	
	public enum EnumEntryShape
	{
		SQUARE, ROUND, HEX;
	}
}