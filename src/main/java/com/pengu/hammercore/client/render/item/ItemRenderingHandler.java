package com.pengu.hammercore.client.render.item;

import java.util.HashMap;
import java.util.Map;

import com.pengu.hammercore.common.items.MultiVariantItem;
import com.pengu.hammercore.core.init.ItemsHC;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum ItemRenderingHandler
{
	INSTANCE;
	
	private final Map<Item, iItemRender> renders = new HashMap<>();
	
	/**
	 * @deprecated Use {@link #setItemRender(Item, iItemRender)} instead. To
	 *             remove in 1.13 This method changes the look of the item. It
	 *             must be called at {@link FMLInitializationEvent}.
	 */
	@Deprecated
	public void bindItemRender(Item item, iItemRender render)
	{
		renders.put(item, render);
		Minecraft.getMinecraft().getRenderItem().registerItem(item, 0, "chest");
	}
	
	/**
	 * This method changes the look of the item. It must be called at
	 * {@link FMLPreInitializationEvent}.
	 */
	public void setItemRender(Item item, iItemRender render)
	{
		renders.put(item, render);
		ItemsHC.rendered_items.add(item);
		ItemsHC.items.remove(item);
		if(item instanceof MultiVariantItem)
			ItemsHC.multiitems.remove(item);
	}
	
	/**
	 * Calling this method doesn't change the renderer for an item but it
	 * appends a new effect layer onto it. May be useful for some fancy FX on
	 * top of an item/block
	 */
	public void appendItemRender(Item item, iItemRender render)
	{
		renders.put(item, render);
	}
	
	public boolean canRender(Item item)
	{
		return getRender(item) != null;
	}
	
	public iItemRender getRender(Item item)
	{
		return renders.get(item);
	}
}