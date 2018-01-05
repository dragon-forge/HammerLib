package com.pengu.hammercore.recipeAPI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.intr.jei.iJeiRecipeModifier;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SimpleRecipeScript implements iRecipeScript
{
	public final Map<Object, iRecipeType> types = new HashMap<>();
	public final Set<Object> swaps = new HashSet<>();
	public NBTTagList makeTag;
	private final Set<Object> JEIRecipesAdded = new HashSet<>();
	private final Set<Object> JEIRecipesRemoved = new HashSet<>();
	
	@Override
	public void add()
	{
		for(Object o : types.keySet())
		{
			iRecipeType type = types.get(o);
			if(swaps.contains(o) && type.swapAddRemoveSupported(o))
			{
				type.removeOnLoad(o);
				if(type.isJeiSupported(o) && iJeiRecipeModifier.Instance.JEIModifier != null)
				{
					Object recipe = type.getJeiRecipeFor(o, true);
					JEIRecipesRemoved.add(recipe);
					iJeiRecipeModifier.Instance.JEIModifier.removeJEI(recipe);
				}
			} else
			{
				if(swaps.contains(o))
					HammerCore.LOG.warn("Found recipe to remove but it doesn't support remove reverse operation!");
				
				type.addRecipe(o);
				if(type.isJeiSupported(o) && iJeiRecipeModifier.Instance.JEIModifier != null)
				{
					Object recipe = type.getJeiRecipeFor(o, false);
					JEIRecipesAdded.add(recipe);
					iJeiRecipeModifier.Instance.JEIModifier.addJEI(recipe);
				}
			}
		}
	}
	
	public void remove()
	{
		if(iJeiRecipeModifier.Instance.JEIModifier != null)
		{
			for(Object recipe : JEIRecipesAdded)
				iJeiRecipeModifier.Instance.JEIModifier.removeJEI(recipe);
			for(Object recipe : JEIRecipesRemoved)
				iJeiRecipeModifier.Instance.JEIModifier.addJEI(recipe);
		}
		
		for(Object o : types.keySet())
		{
			iRecipeType type = types.get(o);
			if(type.swapAddRemoveSupported(o))
				type.addOnUnload(o);
			else
				type.removeRecipe(o);
		}
	}
	
	@Override
	public NBTTagCompound writeToNbt()
	{
		return null;
	}
	
	@Override
	public void readFromNbt(NBTTagCompound nbt)
	{
		
	}
}