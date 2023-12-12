package org.zeith.hammerlib.core.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.zeith.hammerlib.util.CommonMessages.CRAFTING_MATERIAL;

public class ItemGear
		extends Item
{
	protected boolean defaultRecipe = true;
	
	public ItemGear(Properties props)
	{
		super(props);
	}
	
	@Override
	public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_)
	{
		super.appendHoverText(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
		p_77624_3_.add(CRAFTING_MATERIAL.get());
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