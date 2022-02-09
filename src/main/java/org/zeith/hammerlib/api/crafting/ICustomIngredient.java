package org.zeith.hammerlib.api.crafting;

/**
 * A custom implementation that allows to get copies and origins of contained
 * object.
 */
public interface ICustomIngredient<T>
		extends IBaseIngredient
{
	/**
	 * Gets the held copy of content
	 */
	T getCopy();

	/**
	 * Gets the held original of content
	 */
	T getOrigin();
}