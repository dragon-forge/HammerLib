package org.zeith.hammerlib.api.crafting;

import com.google.common.collect.BiMap;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;

/**
 * A more advanced registry name based registry for any subtype of {@link INameableRecipe}.
 */
public class NamespacedRecipeRegistry<T extends INameableRecipe>
		extends AbstractRecipeRegistry<T, NamespacedRecipeRegistry.NamespacedRecipeContainer<T>, ResourceLocation>
{
	/**
	 * Please use {@link RecipeRegistryFactory#namespacedBuilder(Class)} instead! This will ensure that multiple registries with same type and ID won't be created, and ensures same instance over few method calls.
	 */
	@Deprecated(forRemoval = true)
	public NamespacedRecipeRegistry(Class<T> type, ResourceLocation id)
	{
		this(new RecipeRegistryFactory.RegistryFingerprint<>(type, id));
	}
	
	NamespacedRecipeRegistry(RecipeRegistryFactory.RegistryFingerprint<T> fingerprint)
	{
		super(fingerprint.type(), new NamespacedRecipeContainer<>(fingerprint), fingerprint.regId());
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
		private final BiMap<ResourceLocation, T> elements;
		private final BiMap<T, ResourceLocation> elementsInv;
		private final Collection<T> elementsView;
		
		public NamespacedRecipeContainer(RecipeRegistryFactory.RegistryFingerprint<T> fingerprint)
		{
			this.type = fingerprint.type();
			this.elements = fingerprint.storage();
			this.elementsInv = elements.inverse();
			this.elementsView = Collections.unmodifiableCollection(elements.values());
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