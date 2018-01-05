package com.pengu.hammercore.recipeAPI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.utils.JSONObjectToNBT;
import com.pengu.hammercore.json.JSONArray;
import com.pengu.hammercore.json.JSONException;
import com.pengu.hammercore.json.JSONTokener;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class RecipeTypeRegistry implements iRecipeTypeRegistry
{
	private final Set<iRecipeType> types = new HashSet<>();
	
	@Override
	public void register(iRecipeType type)
	{
		types.add(type);
	}
	
	public void forEach(Consumer<iRecipeType> cycle)
	{
		types.stream().forEach(cycle);
	}
	
	public iRecipeScript parseAll(String[] jsons) throws JSONException
	{
		List<SimpleRecipeScript> scripts = new ArrayList<>();
		for(String json : jsons)
		{
			SimpleRecipeScript scr = parse(json);
			if(scr != null)
				scripts.add(scr);
		}
		return new GlobalRecipeScript(scripts.toArray(new SimpleRecipeScript[scripts.size()]));
	}
	
	public SimpleRecipeScript parse(String json) throws JSONException
	{
		return parse(JSONObjectToNBT.convert((JSONArray) new JSONTokener(json).nextValue()));
	}
	
	public SimpleRecipeScript parse(NBTTagList list)
	{
		if(list == null)
			return null;
		
		final SimpleRecipeScript script = new SimpleRecipeScript();
		script.makeTag = list.copy();
		
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt = list.getCompoundTagAt(i);
			
			String id = nbt.getString("id");
			NBTTagCompound r = nbt.getCompoundTag("recipe");
			
			boolean[] parsed = new boolean[1];
			
			forEach(t ->
			{
				if(t.getTypeId().equals(id))
				{
					parsed[0] = true;
					Object o = t.createRecipe(r);
					script.types.put(o, t);
					if(nbt.getBoolean("remove"))
						script.swaps.add(o);
				}
			});
			
			if(!parsed[0])
				HammerCore.LOG.warn("Warning: Found non-existing/missing recipe type: " + id + "! This will get ignored.");
		}
		
		return script;
	}
}