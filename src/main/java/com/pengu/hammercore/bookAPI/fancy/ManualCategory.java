package com.pengu.hammercore.bookAPI.fancy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ManualCategory
{
	public int minDisplayColumn;
	public int minDisplayRow;
	public int maxDisplayColumn;
	public int maxDisplayRow;
	public ResourceLocation icon;
	public ResourceLocation background;
	public Map<String, ManualEntry> entries = new HashMap<>();
	
	public ManualCategory(ResourceLocation icon, ResourceLocation background)
	{
		this.icon = icon;
		this.background = background;
	}
	
	public boolean isVisibleTo(EntityPlayer player)
	{
		return true;
	}
}