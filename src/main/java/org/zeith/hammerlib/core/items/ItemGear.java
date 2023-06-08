package org.zeith.hammerlib.core.items;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import org.zeith.hammerlib.proxy.HLConstants;

import java.util.List;

import static org.zeith.hammerlib.proxy.HLConstants.CRAFTING_MATERIAL;

public class ItemGear
		extends Item
{
	protected boolean defaultRecipe = true;
	
	public ItemGear(Properties props, TagKey<Item> tag)
	{
		super(props);
		TagAdapter.bind(tag, this);
		HLConstants.HL_TAB.add(this);
	}
	
	@Override
	public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_)
	{
		super.appendHoverText(p_41421_, p_41422_, tooltip, p_41424_);
		tooltip.add(CRAFTING_MATERIAL);
	}
	
	public void disableDefaultRecipe()
	{
		defaultRecipe = false;
	}
	
	public boolean defaultRecipe()
	{
		return defaultRecipe;
	}
}