package com.pengu.hammercore.recipeAPI;

import com.pengu.hammercore.HammerCore;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class GlobalRecipeScript extends SimpleRecipeScript
{
	public SimpleRecipeScript[] scripts;
	
	public GlobalRecipeScript(SimpleRecipeScript... sub)
	{
		scripts = sub;
	}
	
	@Override
	public void add()
	{
		for(SimpleRecipeScript s : scripts)
			if(s != null)
				s.add();
	}
	
	@Override
	public void remove()
	{
		for(SimpleRecipeScript s : scripts)
			if(s != null)
				s.remove();
	}
	
	@Override
	public NBTTagCompound writeToNbt()
	{
		NBTTagList l = new NBTTagList();
		for(SimpleRecipeScript s : scripts)
			l.appendTag(s.makeTag.copy());
		NBTTagCompound n = new NBTTagCompound();
		n.setTag("l", l);
		return n;
	}
	
	@Override
	public void readFromNbt(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("l", NBT.TAG_LIST);
		scripts = new SimpleRecipeScript[list.tagCount()];
		for(int i = 0; i < scripts.length; ++i)
			scripts[i] = HammerCore.registry.parse((NBTTagList) list.get(i));
	}
}