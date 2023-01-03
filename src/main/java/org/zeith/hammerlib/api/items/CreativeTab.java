package org.zeith.hammerlib.api.items;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.CreativeModeTabEvent;
import org.zeith.hammerlib.core.adapter.CreativeTabAdapter;
import org.zeith.hammerlib.util.java.tuples.Tuple1;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CreativeTab
{
	private final ResourceLocation id;
	private final Consumer<CreativeModeTab.Builder> factory;
	private final Tuple1.Mutable1<CreativeModeTab> tab = new Tuple1.Mutable1<>(null);
	private final List<ItemLike> contents = new ArrayList<>();
	
	public CreativeTab(ResourceLocation id, Consumer<CreativeModeTab.Builder> factory)
	{
		this.id = id;
		Consumer<CreativeModeTab.Builder> factory0 = b -> b.title(Component.translatable(Util.makeDescriptionId("creative_tab", id)));
		this.factory = factory0.andThen(factory);
		CreativeTabAdapter.getCustomTabs().add(this);
	}
	
	public <T extends ItemLike> T add(T item)
	{
		contents.add(item);
		return item;
	}
	
	public CreativeModeTab tab()
	{
		return tab.a();
	}
	
	public void tab(CreativeModeTab tab)
	{
		this.tab.setA(tab);
	}
	
	public ResourceLocation id()
	{
		return id;
	}
	
	public Consumer<CreativeModeTab.Builder> factory()
	{
		return factory;
	}
	
	public List<ItemLike> contents()
	{
		return contents;
	}
	
	public void register(CreativeModeTabEvent.Register e)
	{
		tab(e.registerCreativeModeTab(id(), factory()));
		CreativeTabAdapter.getRegistered().put(tab(), this);
	}
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.CLASS)
	public @interface RegisterTab
	{
	}
}