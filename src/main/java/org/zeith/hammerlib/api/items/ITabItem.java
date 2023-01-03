package org.zeith.hammerlib.api.items;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.*;

import java.util.Collections;
import java.util.Set;

public interface ITabItem
{
	default boolean allowedIn(CreativeModeTab tab)
	{
		if(getCreativeTabs().stream().anyMatch(t -> t == tab)) return true;
		CreativeModeTab creativemodetab = this.getItemCategory();
		return creativemodetab != null && (tab == CreativeModeTabs.searchTab() || tab == creativemodetab);
	}
	
	CreativeModeTab getItemCategory();
	
	default Set<CreativeModeTab> getCreativeTabs()
	{
		var c = getItemCategory();
		return c != null ? Collections.singleton(c) : Collections.emptySet();
	}
	
	default void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items)
	{
		if(allowedIn(tab))
		{
			items.add(((Item) this).getDefaultInstance());
		}
	}
}