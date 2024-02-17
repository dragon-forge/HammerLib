package com.zeitheron.hammercore.internal.ap;

import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public interface IAPContext
{
	IAPContext DUMMY = new IAPContext() {};
	
	/**
	 * Provides the resource location for an object, if its a field with annotation.
	 */
	default Optional<ResourceLocation> getRegistryName()
	{
		return Optional.empty();
	}
	
	/**
	 * Defines if the object will be registered or not.
	 * Determined by @{@link com.zeitheron.hammercore.annotations.RegisterIf}
	 */
	default boolean shouldRegister()
	{
		return true;
	}
	
	static IAPContext.Builder builder()
	{
		return new Builder();
	}
	
	class Builder
	{
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		protected Optional<ResourceLocation> id = Optional.empty();
		protected boolean shouldRegister = true;
		
		public Builder id(ResourceLocation id)
		{
			this.id = Optional.ofNullable(id);
			return this;
		}
		
		public Builder shouldRegister(boolean shouldRegister)
		{
			this.shouldRegister = shouldRegister;
			return this;
		}
		
		public IAPContext build()
		{
			return new IAPContext()
			{
				@Override
				public Optional<ResourceLocation> getRegistryName()
				{
					return id;
				}
				
				@Override
				public boolean shouldRegister()
				{
					return shouldRegister;
				}
			};
		}
	}
}