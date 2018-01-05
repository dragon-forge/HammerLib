package com.pengu.hammercore.bookAPI.fancy;

import java.util.List;
import java.util.Random;

import com.pengu.hammercore.utils.ColorHelper;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

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
	private eEntryShape shape = eEntryShape.SQUARE;
	private ManualPage[] pages = null;
	
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
	
	public ManualEntry(String key, String category, int col, int row, ResourceLocation icon)
	{
		this.key = key;
		this.category = category;
		this.icon_resource = icon;
		this.icon_item = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	public ManualEntry(String key, String category, int col, int row, ItemStack icon)
	{
		this.key = key;
		this.category = category;
		this.icon_item = icon;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	public ManualEntry(String key, String category, int col, int row, ItemStack... icons)
	{
		this.key = key;
		this.category = category;
		this.icon_item = icons;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	public ManualEntry(String key, String category, int col, int row, List<ItemStack> icons)
	{
		this.key = key;
		this.category = category;
		this.icon_item = icons;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	public ManualEntry(String key, String category, int col, int row, Ingredient icon)
	{
		this.key = key;
		this.category = category;
		this.icon_item = icon;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	public ManualEntry(String key, String category, int col, int row, String iconOreDict)
	{
		this.key = key;
		this.category = category;
		this.icon_item = iconOreDict;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
	}
	
	public ManualEntry setSpecial()
	{
		this.isSpecial = true;
		return this;
	}
	
	public ManualEntry setParents(String... par)
	{
		this.parents = par;
		return this;
	}
	
	public ManualEntry setPages(ManualPage... par)
	{
		this.pages = par;
		return this;
	}
	
	public ManualPage[] getPages()
	{
		return this.pages;
	}
	
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
	
	public eEntryShape getShape()
	{
		return this.shape;
	}
	
	public ManualEntry setShape(eEntryShape shape)
	{
		this.shape = shape;
		return this;
	}
	
	public int getColor()
	{
		return color;
	}
	
	public ManualEntry setColor(int color)
	{
		this.color = color;
		return this;
	}
	
	public enum eEntryShape
	{
		SQUARE, ROUND, HEX;
	}
}