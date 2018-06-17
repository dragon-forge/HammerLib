package com.zeitheron.hammercore.api.crafting;

public interface ICustomIngredient<T> extends IBaseIngredient
{
	T getCopy();
	
	T getOrigin();
}