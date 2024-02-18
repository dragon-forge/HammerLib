package org.zeith.hammerlib.annotations.ap;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

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
	 * Determined by @{@link org.zeith.hammerlib.annotations.OnlyIf}
	 */
	default boolean shouldRegister()
	{
		return true;
	}
	
	default Optional<FMLModContainer> getOwnerMod()
	{
		return Optional.empty();
	}
	
	static IAPContext.Builder builder()
	{
		return new Builder();
	}
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	class Builder
	{
		protected Optional<ResourceLocation> id = Optional.empty();
		protected Optional<FMLModContainer> ownerMod = Optional.empty();
		protected boolean shouldRegister = true;
		
		public Builder id(ResourceLocation id)
		{
			this.id = Optional.ofNullable(id);
			return this;
		}
		
		public Builder owner(FMLModContainer mod)
		{
			this.ownerMod = Optional.ofNullable(mod);
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
				
				@Override
				public Optional<FMLModContainer> getOwnerMod()
				{
					return ownerMod;
				}
			};
		}
	}
}