package com.zeitheron.hammercore.utils.match.item;

import com.google.common.base.Predicate;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class ItemContainer implements Predicate<ItemStack>
{
	private final String item;
	private final int count, damage;
	private final String nbt;
	private final String oredictNames;
	private final String mod;
	private final byte type;
	
	public static ItemContainer create(ItemStack stack)
	{
		return new ItemContainer(stack);
	}
	
	public static ItemContainer create(String od)
	{
		return new ItemContainer(od);
	}
	
	private ItemContainer(ItemStack stack)
	{
		item = stack.getItem().getRegistryName().toString();
		count = stack.getCount();
		nbt = stack.getTagCompound() + "";
		int[] oredicts = OreDictionary.getOreIDs(stack);
		String ods = "";
		for(int i : oredicts)
			ods += OreDictionary.getOreName(i) + ";";
		if(ods.endsWith(";"))
			ods = ods.substring(0, ods.length() - 1);
		oredictNames = ods;
		mod = stack.getItem().getRegistryName().getResourceDomain();
		damage = stack.getItemDamage();
		type = 0;
	}
	
	private ItemContainer(String oredict)
	{
		oredictNames = oredict;
		item = null;
		count = 0;
		damage = -1;
		nbt = null;
		mod = null;
		type = 1;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public int getDamage()
	{
		return damage == -1 ? OreDictionary.WILDCARD_VALUE : damage;
	}
	
	public Object getItem()
	{
		return item != null ? Item.REGISTRY.getObject(new ResourceLocation(item)) : oredictNames.split(";");
	}
	
	public boolean matches(ItemContainer other, ItemMatchParams params)
	{
		boolean modOK = !params.useMod || (mod != null && other.mod != null && other.mod.equals(mod));
		boolean nbtOK = !params.useNBT || (nbt != null && other.nbt != null && other.nbt.equals(nbt));
		boolean dmgOK = !params.useDamage || (damage == other.damage || damage == -1 || other.damage == -1 || damage == OreDictionary.WILDCARD_VALUE || other.damage == OreDictionary.WILDCARD_VALUE);
		boolean itmOK = itemsMatch(item, other.item);
		boolean ctnOK = !params.useCount || count == other.count;
		boolean ordOK = params.useOredict && oredictMatches(oredictNames, other.oredictNames);
		return ordOK || (modOK && nbtOK && dmgOK && itmOK && ctnOK);
	}
	
	public boolean matches(ItemStack stack, ItemMatchParams params)
	{
		return matches(new ItemContainer(stack), params);
	}
	
	private static boolean itemsMatch(String a, String b)
	{
		if(a == b)
			return true;
		if(a == null || b == null)
			return false;
		return a.equals(b);
	}
	
	private static boolean oredictMatches(String a, String b)
	{
		if(a == b)
			return true;
		if(a == null || b == null)
			return false;
		for(String as : a.split(";"))
			if(b.contains(as))
				return true;
		return false;
	}
	
	public byte getType()
	{
		return type;
	}
	
	ItemMatchParams defParams = new ItemMatchParams().setUseOredict(true);
	
	@Override
	public boolean apply(ItemStack input)
	{
		return matches(input, defParams);
	}
}