package org.zeith.hammerlib.api.crafting;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Collections;

/**
 * A more advanced registry name based registry for any subtype of {@link INameableRecipe}.
 */
public class NamespacedRecipeRegistry<T extends INameableRecipe>
		extends AbstractRecipeRegistry<T, NamespacedRecipeRegistry.NamespacedRecipeContainer<T>, ResourceLocation>
{
	public NamespacedRecipeRegistry(Class<T> type, ResourceLocation id)
	{
		super(type, new NamespacedRecipeContainer<>(type), id);
	}

	@Override
	public ResourceLocation addRecipe(T recipe)
	{
		ResourceLocation loc = recipe.getRecipeName();

		if(loc == null) throw new IllegalArgumentException("Attempted to register a recipe with null recipe name!");
		if(container.elements.containsKey(loc))
			throw new IllegalArgumentException("Attempted to register a recipe with registry name that is already taken!");

		container.elements.put(loc, recipe);
		return loc;
	}

	@Override
	public void removeRecipe(T recipe)
	{
		ResourceLocation loc = container.elementsInv.get(recipe);
		if(loc != null) container.remove(loc);
	}

	@Override
	protected void removeAllRecipes()
	{
		container.removeAllRecipes();
	}

	@Override
	public T getRecipe(ResourceLocation identifier)
	{
		if(identifier == null) return null;
		return container.elements.get(identifier);
	}

	protected static class NamespacedRecipeContainer<T extends INameableRecipe>
			implements IRecipeContainer<T>
	{
		private final Class<T> type;
		private final BiMap<ResourceLocation, T> elements = HashBiMap.create();
		private final BiMap<T, ResourceLocation> elementsInv = elements.inverse();
		private final Collection<T> elementsView = Collections.unmodifiableCollection(elements.values());

		public NamespacedRecipeContainer(Class<T> type)
		{
			this.type = type;
		}

		@Override
		public Collection<T> getRecipes()
		{
			return elementsView;
		}

		void removeAllRecipes()
		{
			elements.clear();
		}

		@Override
		public Class<T> getType()
		{
			return type;
		}

		public void remove(ResourceLocation loc)
		{
			elements.remove(loc);
		}
	}
}