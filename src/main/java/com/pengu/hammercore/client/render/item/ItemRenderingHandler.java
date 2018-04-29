package com.pengu.hammercore.client.render.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
	private final Map<Item, List<iItemRender>> renderHooks = new HashMap<>();
	
	/**
	 * @deprecated Use {@link #setItemRender(Item, iItemRender)} instead. To
	 *             remove in 1.13 This method changes the look of the item. It
	 *             must be called at {@link FMLInitializationEvent}.
	 */
	@Deprecated
	public void bindItemRender(Item item, iItemRender render)
	{
		List<iItemRender> hk = renderHooks.get(item);
		if(hk == null)
			renderHooks.put(item, hk = new ArrayList<>());
		hk.clear();
		hk.add(render);
		Minecraft.getMinecraft().getRenderItem().registerItem(item, 0, "chest");
	}
	
	/**
	 * This method changes the look of the item. It must be called at
	 * {@link FMLPreInitializationEvent}.
	 */
	public void setItemRender(Item item, iItemRender render)
	{
		List<iItemRender> hk = renderHooks.get(item);
		if(hk == null)
			renderHooks.put(item, hk = new ArrayList<>());
		hk.clear();
		hk.add(render);
		ItemsHC.rendered_items.add(item);
		ItemsHC.items.remove(item);
		if(item instanceof MultiVariantItem)
			ItemsHC.multiitems.remove(item);
		Minecraft.getMinecraft().getRenderItem().registerItem(item, 0, "chest");
	}
	
	/**
	 * Calling this method doesn't change the renderer for an item but it
	 * appends a new effect layer onto it. May be useful for some fancy FX on
	 * top of an item/block
	 */
	public void appendItemRender(Item item, iItemRender render)
	{
		List<iItemRender> hk = renderHooks.get(item);
		if(hk == null)
			renderHooks.put(item, hk = new ArrayList<>());
		hk.add(render);
	}
	
	public boolean canRender(Item item)
	{
		return getRender(item) != null;
	}
	
	public iItemRender getRender(Item item)
	{
		return renders.get(item);
	}
	
	public List<iItemRender> getRenderHooks(Item item)
	{
		List<iItemRender> hk = renderHooks.get(item);
		return hk != null ? hk : Collections.emptyList();
	}
}