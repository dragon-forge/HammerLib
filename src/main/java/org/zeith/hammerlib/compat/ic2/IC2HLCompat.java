package org.zeith.hammerlib.compat.ic2;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.compat.base.*;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
import org.zeith.hammerlib.core.init.TagsHL;
import org.zeith.hammerlib.event.recipe.BuildTagsEvent;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.function.Function;

@ModCompat(
		modid = "ic2",
		type = BaseHLCompat.class
)
public class IC2HLCompat
		extends BaseHLCompat
{
	public IC2HLCompat(CompatContext ctx)
	{
		super(ctx);
		HammerLib.EVENT_BUS.addListener(this::addWrenches);
	}
	
	private void addWrenches(BuildTagsEvent e)
	{
		if(e.is(Registries.ITEM))
		{
			Function<ResourceLocation, Item> its = BuiltInRegistries.ITEM::getValue;
			
			var wrench = its.apply(Resources.location("ic2", "wrench"));
			if(!new ItemStack(wrench).isEmpty())
				e.addToTag(TagsHL.Items.TOOLS_WRENCH, wrench);
			
			wrench = its.apply(Resources.location("ic2", "electric_wrench"));
			if(!new ItemStack(wrench).isEmpty())
				e.addToTag(TagsHL.Items.TOOLS_WRENCH, wrench);
			
			wrench = its.apply(Resources.location("ic2", "precision_wrench"));
			if(!new ItemStack(wrench).isEmpty())
				e.addToTag(TagsHL.Items.TOOLS_WRENCH, wrench);
		}
	}
}