package com.zeitheron.hammercore.internal.ap;

import org.zeith.hammerlib.abstractions.props.KeyMap;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public interface IAPContext
{
	IAPContext DUMMY = new IAPContext() {};
	
	/**
	 * Provides the resource location for an object, if it's a field with annotation.
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
	
	default KeyMap keys()
	{
		return KeyMap.EMPTY;
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
				final KeyMap keys = KeyMap.createHash(0);
				
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
				
				@Override
				public KeyMap keys()
				{
					return keys;
				}
			};
		}
	}
}