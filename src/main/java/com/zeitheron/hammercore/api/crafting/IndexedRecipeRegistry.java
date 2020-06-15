package com.zeitheron.hammercore.api.crafting;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;

/**
 * A simple index-based registry for any subtype of {@link IGeneralRecipe}.
 */
public class IndexedRecipeRegistry<T extends IGeneralRecipe> extends AbstractRecipeRegistry<T, IndexedRecipeRegistry.IndexedRecipeContainer<T>, IntSupplier>
{
	public IndexedRecipeRegistry(Class<T> type, ResourceLocation id)
	{
		super(type, new IndexedRecipeContainer<>(type), id);
	}
	
	@Override
	public IntSupplier addRecipe(T recipe)
	{
		AtomicInteger id = container.register(recipe);
		return id::get;
	}
	
	@Override
	public void removeRecipe(T recipe)
	{
		container.unregister(recipe);
	}
	
	@Override
	public T getRecipe(IntSupplier identifier)
	{
		return getRecipe(identifier.getAsInt());
	}
	
	@Nullable
	public T getRecipe(int id)
	{
		if(container.elements.size() > id) return container.elements.get(id);
		return null;
	}
	
	protected static class IndexedRecipeContainer<T extends IGeneralRecipe> implements IRecipeContainer<T>
	{
		private final Class<T> type;
		private final List<T> elements = new ArrayList<>();
		private final List<T> elementView = Collections.unmodifiableList(elements);
		private final BiMap<AtomicInteger, T> indices = HashBiMap.create();
		private final BiMap<T, AtomicInteger> indicesInv = indices.inverse();
		
		public IndexedRecipeContainer(Class<T> type)
		{
			this.type = type;
		}
		
		public AtomicInteger register(T elem)
		{
			Objects.requireNonNull(elem, "Recipe element must not be null!");
			AtomicInteger id = new AtomicInteger(elements.size());
			elements.add(elem);
			indices.put(id, elem);
			return id;
		}
		
		public boolean unregister(T elem)
		{
			if(elem != null && elements.contains(elem))
			{
				elements.remove(elem);
				sort();
				return true;
			}
			
			return false;
		}
		
		public void sort()
		{
			indices.values().removeIf(e -> !elements.contains(e));
			
			for(int i = 0; i < elements.size(); ++i)
			{
				T e = elements.get(i);
				if(!indices.containsValue(e)) indices.put(new AtomicInteger(i), e);
				else indicesInv.get(e).set(i);
			}
		}
		
		@Override
		public Collection<T> getRecipes()
		{
			return elementView;
		}
		
		@Override
		public Class<T> getType()
		{
			return type;
		}
	}
}