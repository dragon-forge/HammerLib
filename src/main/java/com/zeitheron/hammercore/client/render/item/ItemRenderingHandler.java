package com.zeitheron.hammercore.client.render.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.zeitheron.hammercore.internal.init.ItemsHC;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum ItemRenderingHandler
{
	INSTANCE;
	
	private final Map<Item, IItemRender> renders = new HashMap<>();
	private final Map<Item, List<IItemRender>> renderHooks = new HashMap<>();
	
	public void bindItemRender(Item item, IItemRender render)
	{
		List<IItemRender> hk = renderHooks.get(item);
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
	public void setItemRender(Item item, IItemRender render)
	{
		List<IItemRender> hk = renderHooks.get(item);
		if(hk == null)
			renderHooks.put(item, hk = new ArrayList<>());
		hk.clear();
		hk.add(render);
		ItemsHC.rendered_items.add(item);
		ItemsHC.items.remove(item);
	}
	
	/**
	 * Calling this method doesn't change the renderer for an item but it
	 * appends a new effect layer onto it. May be useful for some fancy FX on
	 * top of an item/block
	 */
	public void appendItemRender(Item item, IItemRender render)
	{
		List<IItemRender> hk = renderHooks.get(item);
		if(hk == null)
			renderHooks.put(item, hk = new ArrayList<>());
		hk.add(render);
	}
	
	public void applyItemRender(IItemRender render, Predicate<Item> applier)
	{
		GameRegistry.findRegistry(Item.class) //
		        .getValuesCollection() //
		        .stream() //
		        .filter(applier) //
		        .forEach(it -> appendItemRender(it, render));
	}
	
	public boolean canRender(Item item)
	{
		return getRender(item) != null;
	}
	
	public IItemRender getRender(Item item)
	{
		return renders.get(item);
	}
	
	public List<IItemRender> getRenderHooks(Item item)
	{
		List<IItemRender> hk = renderHooks.get(item);
		return hk != null ? hk : Collections.emptyList();
	}
}