package com.pengu.hammercore.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public abstract class MultiVariantItem extends Item
{
	public final String[] names;
	
	public MultiVariantItem(String registryName, String... subitems)
	{
		names = subitems;
		setUnlocalizedName(registryName);
		setHasSubtypes(true);
	}
	
	protected final void insertPrefix(String prefix)
	{
		for(int i = 0; i < names.length; ++i)
			names[i] = prefix + names[i];
	}
	
	protected final void insertPostfix(String postfix)
	{
		for(int i = 0; i < names.length; ++i)
			names[i] = names[i] + postfix;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "item." + (stack.getItemDamage() < names.length ? names[stack.getItemDamage()] : "unnamed");
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(isInCreativeTab(tab))
			for(int i = 0; i < names.length; ++i)
				items.add(new ItemStack(this, 1, i));
	}
}