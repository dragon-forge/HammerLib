package org.zeith.hammerlib.api.crafting.building;

import org.zeith.hammerlib.api.crafting.IGeneralRecipe;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

@FunctionalInterface
public interface IRecipeBuilderFactory<R extends IGeneralRecipe, T extends GeneralRecipeBuilder<R, T>>
{
	T newBuilder(IRecipeRegistrationEvent<R> event);
}