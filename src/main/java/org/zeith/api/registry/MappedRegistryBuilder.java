package org.zeith.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Use this instead of {@link RegistryBuilder} to bind a type to your registry!
 */
public class MappedRegistryBuilder<T>
		extends RegistryBuilder<T>
{
	protected final Class<? super T> base;
	
	public MappedRegistryBuilder(Class<? super T> base, ResourceKey<? extends Registry<T>> registryKey)
	{
		super(registryKey);
		this.base = base;
	}
	
	@Override
	public Registry<T> create()
	{
		var reg = super.create();
		RegistryMapping.report(base, reg);
		return reg;
	}
}