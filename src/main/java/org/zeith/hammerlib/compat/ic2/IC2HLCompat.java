package org.zeith.hammerlib.compat.ic2;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.compat.base.BaseCompat;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
import org.zeith.hammerlib.core.init.TagsHL;
import org.zeith.hammerlib.event.recipe.BuildTagsEvent;

import java.util.function.Function;

@BaseCompat.LoadCompat(
		modid = "ic2",
		compatType = BaseHLCompat.class
)
public class IC2HLCompat
		extends BaseHLCompat
{
	public IC2HLCompat()
	{
		HammerLib.EVENT_BUS.addListener(this::addWrenches);
	}
	
	private void addWrenches(BuildTagsEvent e)
	{
		if(e.reg.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS))
		{
			Function<ResourceLocation, Item> its = ForgeRegistries.ITEMS::getValue;
			
			var wrench = its.apply(new ResourceLocation("ic2", "wrench"));
			if(!new ItemStack(wrench).isEmpty())
				e.addToTag(TagsHL.Items.TOOLS_WRENCH, wrench);
			
			wrench = its.apply(new ResourceLocation("ic2", "electric_wrench"));
			if(!new ItemStack(wrench).isEmpty())
				e.addToTag(TagsHL.Items.TOOLS_WRENCH, wrench);
			
			wrench = its.apply(new ResourceLocation("ic2", "precision_wrench"));
			if(!new ItemStack(wrench).isEmpty())
				e.addToTag(TagsHL.Items.TOOLS_WRENCH, wrench);
		}
	}
}