package org.zeith.hammerlib.compat.ic2;

import net.minecraft.core.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.compat.base.*;
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
	public IC2HLCompat(CompatContext ctx)
	{
		super(ctx);
		HammerLib.EVENT_BUS.addListener(this::addWrenches);
	}
	
	private void addWrenches(BuildTagsEvent e)
	{
		if(e.is(Registries.ITEM))
		{
			Function<ResourceLocation, Item> its = BuiltInRegistries.ITEM::get;
			
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