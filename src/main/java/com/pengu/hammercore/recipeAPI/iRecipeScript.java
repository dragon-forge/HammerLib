package com.pengu.hammercore.recipeAPI;

import net.minecraft.nbt.NBTTagCompound;

public interface iRecipeScript
{
	void add();
	
	void remove();
	
	NBTTagCompound writeToNbt();
	
	void readFromNbt(NBTTagCompound nbt);
}