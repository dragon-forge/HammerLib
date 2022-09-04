package org.zeith.hammerlib.api.crafting.building;

import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.api.crafting.IGeneralRecipe;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public abstract class GeneralRecipeBuilder<T extends IGeneralRecipe, R extends GeneralRecipeBuilder<T, R>>
{
	protected final IRecipeRegistrationEvent<T> registrar;
	protected ResourceLocation identifier;
	
	public GeneralRecipeBuilder(IRecipeRegistrationEvent<T> registrar)
	{
		this.registrar = registrar;
	}
	
	public R id(ResourceLocation identifier)
	{
		this.identifier = identifier;
		return (R) this;
	}
	
	protected abstract ResourceLocation generateId();
	
	protected ResourceLocation getIdentifier()
	{
		if(this.identifier != null) return this.identifier;
		return this.identifier = generateId();
	}
	
	protected abstract void validate() throws IllegalStateException;
	
	protected abstract T createRecipe() throws IllegalStateException;
	
	public T build()
	{
		validate();
		return createRecipe();
	}
	
	public void register()
	{
		validate();
		registrar.register(getIdentifier(), createRecipe());
	}
	
	public void registerIf(BooleanSupplier condition)
	{
		if(condition.getAsBoolean())
			register();
	}
	
	public void registerIf(Predicate<ResourceLocation> condition)
	{
		if(condition.test(identifier))
			register();
	}
}