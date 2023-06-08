package org.zeith.hammerlib.core.init;

import net.minecraft.world.item.Item;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.core.items.ItemWrench;

@SimplyRegister
public interface ItemsHL
		extends GearsHL
{
	@RegistryName("wrench")
	ItemWrench WRENCH = new ItemWrench(new Item.Properties().stacksTo(1));
}