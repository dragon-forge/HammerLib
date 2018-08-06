package com.zeitheron.hammercore.bookAPI.fancy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Represents some info about the manual category. Register these categories
 * using {@link ManualCategories}
 */
public class ManualCategory
{
	public static final ResourceLocation DEF_BG = new ResourceLocation("hammercore", "textures/gui/manual_back.png");
	
	public int minDisplayColumn;
	public int minDisplayRow;
	public int maxDisplayColumn;
	public int maxDisplayRow;
	public ResourceLocation icon;
	public ResourceLocation background = DEF_BG;
	public Map<String, ManualEntry> entries = new HashMap<>();
	
	/**
	 * Constructs a new {@link ManualCategory} with icon and background.
	 * 
	 * @param icon
	 *            The icon of this category.
	 * @param background
	 *            The background of this category.
	 */
	public ManualCategory(ResourceLocation icon, ResourceLocation background)
	{
		this.icon = icon;
		this.background = background;
	}
	
	/**
	 * Constructs a new {@link ManualCategory} with icon and default background.
	 * 
	 * @param icon
	 *            The icon of this category.
	 */
	public ManualCategory(ResourceLocation icon)
	{
		this.icon = icon;
	}
	
	public boolean isVisibleTo(EntityPlayer player)
	{
		return true;
	}
}