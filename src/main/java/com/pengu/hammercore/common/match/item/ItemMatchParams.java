package com.pengu.hammercore.common.match.item;

public class ItemMatchParams
{
	public boolean useNBT = false, useOredict = false, useDamage = false, useCount = false, useMod = false;
	
	public ItemMatchParams setUseNBT(boolean nbt)
	{
		useNBT = nbt;
		return this;
	}
	
	public ItemMatchParams setUseOredict(boolean od)
	{
		useOredict = od;
		return this;
	}
	
	public ItemMatchParams setUseDamage(boolean dmg)
	{
		useDamage = dmg;
		return this;
	}
	
	public ItemMatchParams setUseCount(boolean ctn)
	{
		useCount = ctn;
		return this;
	}
	
	public ItemMatchParams setUseMod(boolean mod)
	{
		useMod = mod;
		return this;
	}
}